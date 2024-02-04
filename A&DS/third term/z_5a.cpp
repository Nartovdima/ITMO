#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

constexpr int INF = 1000000000 + 7;

int bfs(int st, int f, int n, vector<vector<int>>& g, vector<int> &parents) {
	vector <int> dist(n, INF);
	parents[st] = -1;
	dist[st] = 0;
	queue <int> q;
	q.push(st);

	while (!q.empty()) {
		int v = q.front();
		q.pop();

		for (auto u : g[v]) {
			if (dist[u] == INF) {
				dist[u] = dist[v] + 1;
				parents[u] = v;
				q.push(u);
			}
		}
	}

	return dist[f];
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
	}
	vector<int> parents(n);
	int s, t;
	cin >> s >> t;
	s--; t--;
	
	int ans = bfs(s, t, n, g, parents);
	if (ans == INF) {
		cout << -1 << '\n';
		return 0;
	}

	vector <int> path;
	int v = t;
	while (v != s) {
		path.push_back(v);
		v = parents[v];
	}
	path.push_back(s);
	reverse(path.begin(), path.end());
	cout << path.size() - 1 << '\n';
	for (auto i : path) {
		cout << i + 1 << ' ';
	}
	return 0;
}