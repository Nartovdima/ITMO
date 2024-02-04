#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

void dfs(int v, vector<vector<int>> &g, vector<int> &used, vector<int> &topsort, bool &is_topsort_exists) {
	used[v] = 1;
	for (auto u : g[v]) {
		if (used[u] == 1) {
			is_topsort_exists = 0;
		} else if (!used[u]) {
			dfs(u, g, used, topsort, is_topsort_exists);
		}
	}
	used[v] = 2;
	topsort.push_back(v);
}

int main() {
	int n, m;
	cin >> n >> m;
	vector<vector<int>> g(n);
	for (int i = 0; i < m; ++i) {
		int a, b;
		cin >> a >> b;
		a--; b--;
		g[a].push_back(b);
	}

	bool is_topsort_exists = 1;
	vector<int> used(n, 0);
	vector<int> topsort;

	for (int i = 0; i < n; ++i) {
		if (!used[i]) {
			dfs(i, g, used, topsort, is_topsort_exists);
		}
	}

	if (!is_topsort_exists) {
		cout << -1 << '\n';
		return 0;
	}

	reverse(topsort.begin(), topsort.end());
	for (auto it : topsort) {
		cout << it + 1 << ' ';
	}
	return 0;
}