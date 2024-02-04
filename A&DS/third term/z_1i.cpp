#include <iostream>
#include <vector>
#include <algorithm>
#include <map>


using namespace std;

constexpr int INF = 1000000000 + 7;

void dfs(int v, vector<vector<int>> &g, vector<int> &used, vector<int> &topsort, bool &is_unreachable, int t) {
	used[v] = 1;
	if (v == t) {
		is_unreachable = 0;
	}
	for (auto u : g[v]) {
		if (!used[u]) {
			dfs(u, g, used, topsort, is_unreachable, t);
		}
	}
	used[v] = 2;
	topsort.push_back(v);
}

int main() {
	int n, m, s, t;
	cin >> n >> m >> s >> t;
	s--; t--;
	vector<vector<int>> g(n);
	map <pair<int, int>, int> edges_weights;
	for (int i = 0; i < m; ++i) {
		int a, b, c;
		cin >> a >> b >> c;
		a--; b--;
		g[a].push_back(b);
		if (edges_weights.find({a, b}) != edges_weights.end()) {
			edges_weights[{a, b}] = min(c, edges_weights[{a, b}]);	
		} else {
			edges_weights[{a, b}] = c;
		}
		
	}

	bool is_unreachable = 1;
	vector<int> used(n, 0);
	vector<int> topsort;

	dfs(s, g, used, topsort, is_unreachable, t);

	reverse(topsort.begin(), topsort.end());
	vector <long long> dist(n, INF);
	dist[s] = 0;	
	for (int it = 0; it < topsort.size(); ++it) {
		int i = topsort[it];
		if (dist[i] == INF) {
			continue;
		}
		for (auto j : g[i]) {
			dist[j] = min(dist[j], dist[i] + edges_weights[{i, j}]);
		}
	}

	if (is_unreachable) {
		cout << "Unreachable" << '\n';
		return 0;
	}
	cout << dist[t] << '\n';
	return 0;
}