#include <iostream>
#include <vector>
#include <algorithm>
#include <map>


void print(std::vector<std::vector<int>> & g) {
	std::cerr << "----------------------------------" << std::endl;
	for (int i = 0; i < g.size(); i++) {
		std::cerr << i + 1 << ": ";
		for (int j = 0; j < g[i].size(); j++) {
			std::cerr << g[i][j] + 1 << " ";
		}
		std::cerr << std::endl;
	} 

	std::cerr << "----------------------------------" << std::endl;
}


void delete_oriented_edge(std::vector<std::vector<int>>& g, int a, int b) {
	for (auto it = g[a].begin(); it != g[a].end(); ++it) {
		if (*it == b) {
			g[a].erase(it);
			return;
		}
	}
}

void delete_edge(std::vector<std::vector<int>>& g, int a, int b) {
	delete_oriented_edge(g, a, b);
	delete_oriented_edge(g, b, a);
}

void add_vertexes(std::vector<std::vector<int>>& g, int a, int b) {
	for (auto it : g[a]) {
		g[b].push_back(it);
	}

	for (int i = 0; i < g.size(); i++) {
		for (int j = 0; j < g[i].size(); j++) {
			if (g[i][j] == a) {
				g[i].push_back(b);
			}
		}
		std::sort(g[i].begin(), g[i].end());
		auto it = std::equal_range(g[i].begin(), g[i].end(), a);
		g[i].erase(it.first, it.second);

		auto last = std::unique(g[i].begin(), g[i].end());
		g[i].erase(last, g[i].end());
	}

	std::sort(g[b].begin(), g[b].end());
	auto last = std::unique(g[b].begin(), g[b].end());
	g[b].erase(last, g[b].end());
}

void merge_vertexes(std::vector<std::vector<int>>& g, int a, int b) {
	delete_edge(g, a, b);

	add_vertexes(g, a, b);
	// delete g[a]
	g[a].erase(g[a].begin(), g[a].end());

}

std::vector<std::pair<int, int>> compute_polynom(std::vector<std::vector<int>>& g, int num_of_vertexes) { 
	std::vector<std::pair<int, int>> ans;
	int cnt = 0;
	for (int i = 0; i < g.size(); i++) {
		for (int j = 0; j < g[i].size(); j++) {
			cnt++;
			std::vector<std::vector<int>> removed_edge_g(g), merged_vertexes_g(g);

			delete_edge(removed_edge_g, i, g[i][j]);
			merge_vertexes(merged_vertexes_g, i, g[i][j]);

			std::vector<std::pair<int, int>> removed_edge_g_polynom = compute_polynom(removed_edge_g, num_of_vertexes);
			std::vector<std::pair<int, int>> merged_vertexes_g_polynom = compute_polynom(merged_vertexes_g, num_of_vertexes - 1);

			for (auto it : removed_edge_g_polynom) {
				ans.push_back(it);
			}

			for (auto it : merged_vertexes_g_polynom) {
				ans.push_back({-it.first, it.second});
			}
			break;
		}
		if (cnt == 1) {
			break;
		}
	}
	if (cnt == 0) {
		ans.push_back({1, num_of_vertexes});
	}
	return ans;
}

signed main() {
	int n, m;
	std::cin >> n >> m;

	std::vector<std::vector<int>> g(n);

	for (int i = 0; i < m; i++) {
		int a, b;
		std::cin >> a >> b;
		a--; b--;
		g[a].push_back(b);
	}

	std::vector <std::pair<int, int>> polynom = compute_polynom(g, n);

	std::map<int, int> compressed_polynom;
	for (auto it : polynom) {
		compressed_polynom[it.second] += it.first;
	}

	int max_power = compressed_polynom.rbegin() -> first;
	std::cout << max_power << '\n';
	for (int i = max_power; i > -1; i--) {
		std::cout << compressed_polynom[i] << ' ';
	}
	return 0;
}