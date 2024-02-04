#include <iostream>
#include <vector>

using namespace std;



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
	vector <int> top_sort(n);
	for (int i = 0; i < n; ++i) {
		cin >> top_sort[i];
		top_sort[i]--;
	}

	vector <bool> used(n, 0);
	bool flag = 0;
	for (int i = 0; i < top_sort.size(); ++i) {
		int v = top_sort[i];
		used[v] = 1;
		for (int u : g[v]) {
			if (used[u] == 1) {
				flag = 1;
			}
		}
	}
	if (flag) {
		cout << "NO" << '\n';
		return 0;
	}
	cout << "YES" << '\n';
	return 0;
}