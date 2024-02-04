#include <iostream>
#include <vector>
#include <set>

using namespace std;

constexpr long long INF = 1e13 + 7;

vector<long long> dijkstra(int s, vector<vector<pair<int, int>>> &g, int n) {
	vector <long long> distances(n, INF);
	set <pair<long long, int>> vertexes_pool;
	distances[s] = 0;
	vertexes_pool.insert({distances[s], s});

	while (!vertexes_pool.empty()) {
		int curr_vertex = vertexes_pool.begin() -> second;
		vertexes_pool.erase(vertexes_pool.begin());

		for (auto [target_vertex, target_distance] : g[curr_vertex]) {
			if (distances[target_vertex] > distances[curr_vertex] + target_distance) {
				vertexes_pool.erase({distances[target_vertex], target_vertex});
				distances[target_vertex] = distances[curr_vertex] + target_distance;
				vertexes_pool.insert({distances[target_vertex], target_vertex});
			}
		}
	}

	return distances;
}

int main() {
	int n, m;
	cin >> n >> m;
	vector <vector<pair<int, int>>> g(n);

	for (int i = 0; i < m; ++i) {
		int v, u, w;
		cin >> v >> u >> w;
		v--; u--;
		g[v].push_back({u, w});
		g[u].push_back({v, w});
	}

	for (auto i : dijkstra(0, g, n)) {
		cout << i << ' ';
	}
	return 0;
}