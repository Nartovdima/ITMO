#include <iostream>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;

map <int, long long> time_for_detail;

long long count_time(int v, vector<vector<int>> &g, vector<int> &details_time_requirements, vector<int> &details_order) {
	if (time_for_detail.find(v) != time_for_detail.end()) {
		return 0;
	}

	long long res = details_time_requirements[v];
	for (auto i : g[v]) {
		res += count_time(i, g, details_time_requirements, details_order);
	}

	details_order.push_back(v);
	return time_for_detail[v] = res;
}

int main() {
	int n;
	cin >> n;
	vector <int> details_time_requirements(n);
	for (auto &i : details_time_requirements) {
		cin >> i;
	}
	vector <vector<int>> g(n);
	for (int i = 0; i < n; ++i) {
		int k;
		cin >> k;
		g[i].resize(k);
		for (int j = 0; j < k; ++j) {
			cin >> g[i][j];
			g[i][j]--;
		}
	}

	
	vector <int> details_order;
	long long ans = count_time(0, g, details_time_requirements, details_order);
	cout << ans << ' ' << details_order.size() << '\n';

	for (auto i : details_order) {
		cout << i + 1 << ' ';
	}
	return 0;
}