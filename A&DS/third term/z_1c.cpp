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
	int num_of_extra_vertices = all_cmp.size() - 1;
	cout << num_of_extra_vertices << '\n';
	for (int i = 1, j = 0; i < all_cmp.size(); i++, j++) {
		cout << all_cmp[i][0] + 1 << ' ' << all_cmp[j][0] + 1 << '\n';
	}
	return 0;
}