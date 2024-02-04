#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector <int> parents;
int cycle_start_vertex = -1;
int cycle_end_vertex = -1;

void dfs(int v, vector<vector<int>>& g, vector<int>& used) {
	used[v] = 1;
	for (auto u : g[v]) {
		if (used[u] == 1) {
			cycle_start_vertex = u;
			cycle_end_vertex = v;
		} else if (!used[u]) {
			parents[u] = v;
			dfs(u, g, used);
		}
	}
	used[v] = 2;
}

int main() {
	int n, m;
	cin >> n >> m;

	vector<vector<int>> g(n);
	for (int i = 0; i < m; i++) {
		int a, b;
		cin >> a >> b;
		a--; b--;
		g[a].push_back(b);
	}

	vector <int> used(n);
	parents.resize(n);

	for (int i = 0; i < n; i++) {
		if (!used[i]) {
			parents[i] = -1;
			dfs(i, g, used);
		}
	}

	if (cycle_start_vertex == -1) {
		cout << "-1" << '\n';
		return 0;
	}

	int cycle_vertex = cycle_end_vertex;
	vector <int> ans;
	while (cycle_vertex != cycle_start_vertex) {
		ans.push_back(cycle_vertex);
		cycle_vertex = parents[cycle_vertex];
	}
	ans.push_back(cycle_start_vertex);

	reverse(ans.begin(), ans.end());
	cout << ans.size() << '\n';
	for (auto it : ans) {
		cout << it + 1<< ' ';
	}
	return 0;
}