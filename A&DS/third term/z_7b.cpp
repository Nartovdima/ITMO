#include <iostream>
#include <vector>
#include <set>

using namespace std;

constexpr long long INF = 1e13 + 7;
 
long long prim(int s, vector<vector<pair<int, long long>>> &g, int n) {
	vector <long long> minimal_edges(n, INF);
	vector <int> edge_end(n, -1);
	long long wght = 0;
	set <pair<long long, int>> vertexes_pool;
	minimal_edges[s] = 0;
	vertexes_pool.insert({minimal_edges[s], s});

	for (int qq = 0; qq < n; ++qq) {
		int curr_vertex = vertexes_pool.begin() -> second;
		vertexes_pool.erase(vertexes_pool.begin());
 
		if (edge_end[curr_vertex] != -1) {
			
			wght += minimal_edges[curr_vertex];
			minimal_edges[curr_vertex] = 0;
		}

		for (auto [target_vertex, target_edge_weight] : g[curr_vertex]) {
			if (minimal_edges[target_vertex] > target_edge_weight) {
				vertexes_pool.erase({minimal_edges[target_vertex], target_vertex});
				minimal_edges[target_vertex] = target_edge_weight;
				edge_end[target_vertex] = curr_vertex;
				vertexes_pool.insert({minimal_edges[target_vertex], target_vertex});
			}
		}
	}
 
	return wght;
}

int main() {
	int n, m;
	cin >> n >> m;
	vector <vector<pair<int, long long>>> g(n);
	for (int i = 0; i < m; ++i) {
		int a, b;
		long long c;
		cin >> a >> b >> c;
		a--; b--;
		g[a].push_back({b, c});
		g[b].push_back({a, c});
	}

	cout << prim(0, g, n) << '\n';
	return 0;
}

