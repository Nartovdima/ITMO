#include <iostream>
#include <vector>
#include <set>

int main() {
	std::ios_base::sync_with_stdio(0);
	std::cin.tie(0);
	std::cout.tie(0);

	int n;
	std::cin >> n;
	std::vector<std::vector<int>> g(n);
	std::vector <int> deg(n);

	for (int i = 0; i < n - 1; ++i) {
		int a, b;
		std::cin >> a >> b;
		a--; b--;
		g[a].push_back(b);
		g[b].push_back(a);
	}

	std::set <int> leaves;

	for (int i = 0; i < n; ++i) {
		deg[i] = g[i].size();
		if (g[i].size() == 1) {
			leaves.insert(i);
		}
	}


	std::vector <int> prufer_code;
	while (!leaves.empty()) {
		/*for (auto it : leaves) {
			std::cout << it + 1 << ' ';
		}
		std::cout << '\n';*/

		int curr_leaf = *leaves.begin();
		leaves.erase(leaves.begin());
		deg[curr_leaf]--;

		int new_leaf;

		for (auto j : g[curr_leaf]) {
			if (deg[j] != 0) {
				new_leaf = j;
				break;
			}
		}

		if (deg[new_leaf] != 0) {
			prufer_code.push_back(new_leaf);
			deg[new_leaf]--;
			if (deg[new_leaf] == 1) {
				leaves.insert(new_leaf);
			}
		}


	}

	for (int i = 0; i < prufer_code.size() - 2; ++i) {
		std::cout << prufer_code[i] + 1 << " ";
	}
	return 0;
}
