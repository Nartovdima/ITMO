#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <functional>
#include <set>

void dfs(int v, std::vector<int>& vertexes_colors, std::vector<std::vector<int>>& g, std::vector<bool>& used) {
	used[v] = 1;
	std::vector<bool> mex(g.size() + 1, 0);
	if (vertexes_colors[v] == 0) {
		for (auto u : g[v]) {
			mex[vertexes_colors[u]] = 1;
		}
		int ans;
		for (int i = 1; i < mex.size(); i++) {
			if (mex[i] == 0) {
				ans = i;
				break;
			}
		}
		vertexes_colors[v] = ans;

	}
	for (auto u : g[v]) {
		if (!used[u]) {
			dfs(u, vertexes_colors, g, used);
		}
	}
}

int main() {
	int n, m;
	std::cin >> n >> m;

	std::vector <std::pair<int, int>> vertexes_powers(n);
	std::vector<std::vector<int>> g(n);

	for (int i = 0; i < m; i++) {
		int a, b;
		std::cin >> a >> b;
		a--; b--;
		vertexes_powers[a].first++;
		vertexes_powers[b].first++;
		g[a].push_back(b);
		g[b].push_back(a);
	}

	for(int i = 0; i < n; i++) {
		vertexes_powers[i].second = i;
	}

	std::sort(vertexes_powers.begin(), vertexes_powers.end(), std::greater<std::pair<int, int>>());

	for (int i = 0; i < n; i++) {
		std::cerr << vertexes_powers[i].second << ' ' << vertexes_powers[i].first << std::endl;
	}

	int color = 1;
	std::vector<int> vertexes_colors(n);

	/*while (vertexes_painted < n) {

		for (auto [v, u] : vertexes_powers_updatable) {
			int i = u;

			if (vertexes_colors[i] == 0) {
				int cnt = 0;
				for (int j = 0; j < g[i].size(); j++) {
					if (vertexes_colors[g[i][j]] != color) {
						cnt++;
					}
				}
				if (cnt == g[i].size()) {
					vertexes_colors[i] = color;
					vertexes_painted++;
					for (int j = 0; j < g[i].size(); j++) {

					}
					vertexes_powers_updatable.erase({v, u});
					break;
				}
			}
		}
		color++;
	}*/

	int max_power = vertexes_powers[0].first;
	std::vector<bool> used(n, 0);
	vertexes_colors[vertexes_powers[0].second] = 1;
	dfs(max_power, vertexes_colors, g, used);

	std::cout << max_power + (1 - max_power % 2) << std::endl;
	for (auto col : vertexes_colors) {
		std::cout << col << std::endl;
	}
	return 0;
}