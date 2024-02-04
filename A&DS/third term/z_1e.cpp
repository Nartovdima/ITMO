#include <iostream>
#include <vector>

using namespace std;



void dfs(int v, vector<vector<int>>& g, vector<int>& colors, int color, bool& is_correct) {
	colors[v] = color;

	for (auto u : g[v]) {
		if (colors[u] == colors[v]) {
			is_correct = 0;
		}
		if (!colors[u]) {
			dfs(u, g, colors, (color == 1 ? 2 : 1), is_correct);
		} 
	}
}

int main() {
	int n, m;
	cin >> n >> m;

	vector <vector<int>> g(n);
	for (int i = 0; i < m; ++i) {
		int a, b;
		cin >> a >> b;
		a--; b--;
		g[a].push_back(b);
		g[b].push_back(a);
	}

	bool is_correct = 1;
	vector <int> colors(n, 0);
	for (int i = 0; i < n; ++i) {
		if (!colors[i]) {
			dfs(i, g, colors, 1, is_correct);
		}
	}
	if (!is_correct) {
		cout << -1 << '\n';
		return 0;
	}

	for (auto it : colors) {
		cout << it << ' ';
	}
	return 0;
}