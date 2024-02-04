#include <iostream>
#include <vector>

using namespace std;

void dfs(int v, vector<vector<int>>& g, vector<bool>& used, vector<pair<int, int>>& tree_edges) {
	used[v] = 1;
	for (auto u : g[v]) {
		if (!used[u]) {
			tree_edges.push_back({v, u});
			dfs(u, g, used, tree_edges);
		}
	}
}


int main() {
	ios_base::sync_with_stdio(false);
	cin.tie(0);
	cout.tie(0);

	int n, m;
	cin >> n >> m;
	vector<vector<int>> g(n);

	for (int i = 0; i < m; i++) {
		int a, b;
		cin >> a >> b;
		a--; b--;
		g[a].push_back(b);
		g[b].push_back(a);
	}
	vector<pair<int, int>> tree_edges;
	vector<bool> used(n);

	dfs(1, g, used, tree_edges);
	for (auto [v, u] : tree_edges) {
		cout << v + 1 << ' ' << u + 1 << '\n';
	}
	return 0;
}