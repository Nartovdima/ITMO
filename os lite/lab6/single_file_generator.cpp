#include <fstream>
#include <string>

using namespace std;

int main(int argc, char *argv[]) { // 1 - size, 2 - filename
	int size = stoi(static_cast<string>(argv[1]));
	string file_name = static_cast<string>(argv[2]);

	fstream fstr_out(file_name, fstream::out);

	fstr_out << size << '\n';
	for (int i = 1; i <= size; ++i) {
		fstr_out << i << '\n';
	}
	return 0;
}
