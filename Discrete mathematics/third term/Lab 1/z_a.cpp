#include <iostream>
#include <deque>
#include <string>
#include <vector>
#include <algorithm>

int main() {
	std::ios_base::sync_with_stdio(0);
	std::cin.tie(0);
	std::cout.tie(0);

	int n;
	std::cin >> n;
	std::vector <std::vector<int>> g(n, std::vector<int>(n, 0));

	std::string trash;
	getline(std::cin, trash);


	for (int i = 0; i < n; i++) {
		std::string tmp_input;
		getline(std::cin, tmp_input);

		for (int j = 0; j < i; j++) {
			if (tmp_input[j] == '1') {
				g[i][j] = 1;
				g[j][i] = 1;
			}
		}
	}

	std::deque <int> q;

	for (int i = 0; i < n; i++) {
		q.push_front(i);
	}

	for (int i = 0; i < n * (n - 1); i++) {	
		if (g[q[0]][q[1]] != 1) {
			int j = 2;
			while (g[q[0]][q[j]] != 1 || g[q[1]][q[j + 1]] != 1) {
				j++;
			}

			//std::reverse(q.begin() + 1, q.begin() + j + 1);
			for (int k = 0; k <= (j - 1) / 2; ++k) {
				std::swap(q[k + 1], q[j - k]);
			}
		} 
		q.push_back(q[0]);
		q.pop_front();
	}

	for (int i = 0; i < n; i++) {
		std::cout << q[i] + 1 << ' ';
	}
	return 0;
}