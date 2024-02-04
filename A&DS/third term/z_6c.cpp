#include <iostream>
#include <vector>

using namespace std;

constexpr int INF = 30000;

struct edge {
    int v, u, w;
};

int main() {
	int n, m;
	cin >> n >> m;
	vector<edge> edges;
	for (int i = 0; i < m; ++i) {
		int v, u, w;
		cin >> v >> u >> w;
		v--;
		u--;
		edges.push_back({v, u, w});
	}

	vector <int> distances(n, INF);
    distances[0] = 0;
    for (int i = 0; i < n - 1; i++) {
        for (auto j : edges) {
            if (distances[j.v] == INF)
                continue;
            if (distances[j.u] > distances[j.v] + j.w)
                distances[j.u] = distances[j.v] + j.w;
        }
    }

    for (auto i : distances) {
    	cout << i << ' ';
    }
	return 0;
}