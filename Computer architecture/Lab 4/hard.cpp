#include <omp.h>

#include <array>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

unsigned int width, height, depth;
std::vector<unsigned char> picture;
bool openmp_switch;  // 1 - on 0 - off

void input(const std::filesystem::path &input_file_name) {
    std::ifstream in = std::ifstream(input_file_name, std::ios::in | std::ios::binary);
    in.exceptions(std::ios::failbit);

    std::string sign;
    in >> sign;
    if (sign != "P5") {
        std::cout << "Unsupported file format" << std::endl;
        exit(-1);
    }
    in >> width >> height >> depth;
    in.ignore(1);
    picture.resize(height * width);
    in.read(reinterpret_cast<char *>(picture.data()), width * height);
}

void output(const std::filesystem::path &output_file_name) {
    std::ofstream out = std::ofstream(output_file_name, std::ios::out | std::ios::binary);
    out << "P5\n"
        << width << ' ' << height << '\n'
        << depth << '\n';
    for (const auto &i : picture) {
        out << i;
    }
}

inline void process_image() {
    std::array<unsigned int, 256> histogram{};

#pragma omp parallel if (openmp_switch)
    {
        std::array<unsigned int, 256> thread_histogram{};

#pragma omp for schedule(static)
        for (size_t i = 0; i < picture.size(); i++) {
            thread_histogram[picture[i]]++;
        }

#pragma omp critical
        {
            for (size_t i = 0; i < thread_histogram.size(); i++) {
                histogram[i] += thread_histogram[i];
            }
        }
    }

    std::array<unsigned long long, histogram.size() + 1> sums;
    sums[0] = 0;

    std::array<double, histogram.size() + 1> mul_sums;
    mul_sums[0] = 0;

    for (size_t i = 0; i < histogram.size(); i++) {
        sums[i + 1] = sums[i] + histogram[i];
    }

    for (size_t i = 0; i < histogram.size(); i++) {
        mul_sums[i + 1] = mul_sums[i] + i * histogram[i];
    }

    double best_variance = -1;
    unsigned char best_k1 = 0;
    unsigned char best_k2 = 0;
    unsigned char best_k3 = 0;

#pragma omp parallel if (openmp_switch)
    {
        double best_thread_variance = -1;
        unsigned char best_thread_k1 = 0;
        unsigned char best_thread_k2 = 0;
        unsigned char best_thread_k3 = 0;

#pragma omp for nowait schedule(static)
        for (int k1 = 1; k1 <= 253; k1++) {
            const unsigned long long p1 = sums[k1 + 1] - sums[0];
            if (p1 == 0) {
                continue;
            }
            const double m1 = (mul_sums[k1 + 1] - mul_sums[0]) / static_cast<double>(p1);

            for (int k2 = k1 + 1; k2 <= 254; k2++) {
                const unsigned long long p2 = sums[k2 + 1] - sums[k1 + 1];
                if (p2 == 0) {
                    continue;
                }
                const double m2 = (mul_sums[k2 + 1] - mul_sums[k1 + 1]) / static_cast<double>(p2);

                for (int k3 = k2 + 1; k3 <= 255; k3++) {
                    const unsigned long long p3 = sums[k3 + 1] - sums[k2 + 1];
                    if (p3 == 0) {
                        continue;
                    }
                    const double m3 = (mul_sums[k3 + 1] - mul_sums[k2 + 1]) / static_cast<double>(p3);
                    const unsigned long long p4 = sums[histogram.size()] - sums[k3 + 1];
                    if (p4 == 0) {
                        continue;
                    }
                    const double m4 = (mul_sums[histogram.size()] - mul_sums[k3 + 1]) / static_cast<double>(p4);

                    const double main_m = p1 * m1 + p2 * m2 + p3 * m3 + p4 * m4;
                    const double variance =
                            (p1 * (m1 - main_m) * (m1 - main_m) +
                             p2 * (m2 - main_m) * (m2 - main_m)) +
                            (p3 * (m3 - main_m) * (m3 - main_m) +
                             p4 * (m4 - main_m) * (m4 - main_m));

                    if (variance > best_thread_variance) {
                        best_thread_variance = variance;
                        best_thread_k1 = k1;
                        best_thread_k2 = k2;
                        best_thread_k3 = k3;
                    }
                }
            }
        }

#pragma omp critical
        {
            if (best_variance < best_thread_variance) {
                best_variance = best_thread_variance;
                best_k1 = best_thread_k1;
                best_k2 = best_thread_k2;
                best_k3 = best_thread_k3;
            }
        }
    }

    printf("%u %u %u\n", best_k1, best_k2, best_k3);

#pragma omp parallel for schedule(static) if (openmp_switch)
    for (size_t i = 0; i < picture.size(); i++) {
        if (0 <= picture[i] && picture[i] <= best_k1) {
            picture[i] = 0;
        } else if (best_k1 + 1 <= picture[i] && picture[i] <= best_k2) {
            picture[i] = 84;
        } else if (best_k2 + 1 <= picture[i] && picture[i] <= best_k3) {
            picture[i] = 170;
        } else if (best_k3 + 1 <= picture[i]) {
            picture[i] = 255;
        }
    }
}

int main(int argc, char *argv[]) {
    if (argc != 4) {
        std::cout << "Expected 3 arguments, found " << argc - 1 << '\n';
        return 0;
    }
    const std::filesystem::path input_file_name = argv[2], output_file_name = argv[3];

    omp_set_dynamic(0);

    int num_threads;

    try {
        num_threads = std::stoi(argv[1]);
    } catch (std::exception &e) {
        std::cout << e.what() << std::endl;
        return 0;
    }

    openmp_switch = (num_threads != -1);

    if (num_threads == 0) {
        omp_set_num_threads(omp_get_max_threads());
        num_threads = omp_get_max_threads();
    } else if (num_threads > 0) {
        omp_set_num_threads(num_threads);
    } else if (num_threads == -1){
        num_threads = 1;
    } else {
        std::cout << "Incorrect number of threads" << std::endl;
        return 0;
    }

    try {
        input(input_file_name);
    } catch (std::exception &e) {
        std::cout << e.what() << std::endl;
        return 0;
    }
    const double start_time = omp_get_wtime();

    process_image();

    const double end_time = omp_get_wtime();

    try {
        output(output_file_name);
    } catch (std::exception &e) {
        std::cout << e.what() << std::endl;
        return 0;
    }
    printf("Time (%i thread(s)): %g ms\n", num_threads, (end_time - start_time) * 1000);
    return 0;
}