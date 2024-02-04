#include <iostream>
#include <vector>

using namespace std;

constexpr long long INF = 1e13 + 7;

struct edge {
    int v, u;
    long long w;
};

int main() {
    int n, m, k, s;
    cin >> n >> m >> k >> s;
    s--;
    vector <edge> g;
    for (int i = 0; i < m; ++i) {
        int v, u, w;
        cin >> v >> u >> w;
        v--; u--;
        g.push_back({v, u, w});
    }
    
    vector<long long> distances(n, INF);
    distances[s] = 0;

    for (int i = 0; i < k; ++i) {
        vector <long long> new_distances(n, INF);
        for (edge curr_edge : g) {
            if (distances[curr_edge.v] == INF) {
                continue;
            }
            if (new_distances[curr_edge.u] > distances[curr_edge.v] + curr_edge.w) {
                new_distances[curr_edge.u] = distances[curr_edge.v] + curr_edge.w;
            }
        }
        distances = std::move(new_distances); 
        /*for (auto it : distances) {
            cout << it << ' ';
        }
        cout << '\n';*/
    }

    for (auto i : distances) {
        if (i == INF) {
            cout << -1 << '\n';
        } else {    
            cout << i << '\n';
        }
    }
    return 0;
}