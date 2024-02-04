#include <iostream>
#include <vector>

using namespace std;

int main() {
	int n;
	cin >> n;

	vector <vector<int>> g(n, vector<int>(n, 0));
	 
	for (int i = 0; i < n; ++i) {
		for (int j = 0; j < n; ++j) {
			cin >> g[i][j];
		}
	}

	for (int k = 0; k < n; ++k) {
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				g[i][j] = g[i][j] | (g[i][k] & g[k][j]);
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