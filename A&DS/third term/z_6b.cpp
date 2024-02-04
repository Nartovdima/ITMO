#include <iostream>
#include <vector>

using namespace std;

constexpr int INF = 30000;

int main() {
	int n, m;
	cin >> n >> m;

	vector <vector<int>> g(n, vector<int>(n, INF));
	 
	for (int i = 0; i < m; ++i) {
		int a, b, c;
		cin >> a >> b >> c;
		a--; b--;
		g[a][b] = min(c, g[a][b]);
	}

	for (int i = 0; i < n; ++i) {
		g[i][i] = 0;
	}

	for (int k = 0; k < n; ++k) {
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				if (g[i][k] < INF && g[k][j] < INF) {
					g[i][j] = min(g[i][j], g[i][k] + g[k][j]);
				}
			}
		}
	}

	for (auto &i : g) {
		for (auto &j : i) {
			cout << j << ' ';
		}
		cout << '\n';
	}
	return 0;
}