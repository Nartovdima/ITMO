#include <iostream>
#include <vector>
#include <set>

using namespace std;

constexpr long long INF = 1e13 + 7;

int n;
vector <pair<int, int>> vertexes;

long long calculate_distance(int v, int u) {
	pair<int, int> point1 = vertexes[v];
	pair<int, int> point2 = vertexes[u];
	return (point1.first - point2.first) * (point1.first - point2.first) + (point1.second - point2.second) * (point1.second - point2.second);
}

long long dijkstra(int s, int t) {
	vector <long long> distances(n, INF);
	vector <bool> used(n, 0);
	distances[s] = 0;

	for (int q = 0; q < n; ++q) {
		int curr_vertex = -1;
		for (int i = 0; i < n; ++i) {
			if (used[i] == 0 && (curr_vertex == -1 || distances[i] < distances[curr_vertex])) {
				curr_vertex = i;
			}
		}
		//cout << curr_vertex << '\n';
		used[curr_vertex] = 1;
		for (int target_vertex = 0; target_vertex < n; ++target_vertex) {
			if (target_vertex == curr_vertex) {
				continue;
			}
			int target_distance = calculate_distance(curr_vertex, target_vertex);
			if (distances[target_vertex] > distances[curr_vertex] + target_distance) {
				distances[target_vertex] = distances[curr_vertex] + target_distance;
			}
		}
	}

	return distances[t];
}

int main() {
	cin >> n;
	
	for (int i = 0; i < n; ++i) {
		int a, b;
		cin >> a >> b;
		vertexes.push_back({a, b});
	}

	int s, t;
	cin >> s >> t;
	s--; t--;
	cout << dijkstra(s, t) << '\n';

	return 0;
}