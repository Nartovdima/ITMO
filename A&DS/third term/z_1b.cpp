#include <iostream>
#include <vector>

using namespace std;

void dfs(int v, vector<vector<int>>& g, vector<bool>& used, vector <int>& comp) {
	used[v] = 1;
	comp.push_back(v);
	for (auto u : g[v]) {
		if (!used[u]) {
			dfs(u, g, used, comp);
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
	vector<bool> used(n);

	int cnt = 0;
	vector<vector<int>> all_cmp;
	for (int i = 0; i < n; i++) {
		if (!used[i]) {
			vector <int> comp;
			dfs(i, g, used, comp);
			cnt++;
			all_cmp.push_back(comp);
		}
	}
	cout << all_cmp.size() << '\n';
	for (auto & it : all_cmp) {
		cout << it.size() << '\n';
		for (auto it2 : it) {
			cout << it2 + 1 << ' ';
		}
		cout << '\n';
	}
	return 0;
}