#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

void dfs(int v, std::vector<int> &top_sort, std::vector<bool>& used, std::vector<std::vector<int>>& g) {
	used[v] = 1;
	for (auto u : g[v]) {
		if (!used[u]) {
			dfs(u, top_sort, used, g);
		}
	}
	top_sort.push_back(v);
}


int main() {
	std::ios_base::sync_with_stdio(0);
	std::cin.tie(0);
	std::cout.tie(0);

	int n;
	std::cin >> n;

	std::string trash;
	getline(std::cin, trash);

	std::vector<std::vector<int>> g(n);

	for (int i = 0; i < n; ++i) {
		std::string row;
		getline(std::cin, row);
		for (int j = 0; j < row.size(); ++j) {
			if (row[j] == '1') {
				g[i].push_back(j);
			} else {
				g[j].push_back(i);
			}
		}
	}

	for (int i = 0; i < n; i++) {
		std::vector<bool> used(n, 0);
		std::vector<int> order;
		bool flag = false;
		dfs(i, order, used, g);

		std::reverse(order.begin(), order.end());
		for (int j = 0; j < g[order[n - 1]].size(); j++) {
			if (g[order[n - 1]][j] == order[0]) {
				flag = true;
				break;
			}
		}
		if (flag) {
			for (int j = 0; j < n; j++) {
				std::cout << order[j] + 1 << " ";
			}
			return 0;
		}
	}
	return 0;
}