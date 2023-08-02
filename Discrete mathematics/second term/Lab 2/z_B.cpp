#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <unordered_set>

using namespace std;

int main() {

	freopen("epsilon.in", "r", stdin);
	freopen("epsilon.out", "w", stdout);
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	cout.tie(0);


	int n;
	char st;
	cin >> n >> st;
	vector <pair<char, string>> g;
	array <bool, 26> epsilon_terms;
	epsilon_terms.fill(0);
	
	string in;
	getline(cin, in);
	
	for (int i = 0; i < n; i++) {
		getline(cin, in);
		stringstream a(in);
		string trash, to;
		char from;
		a >> from >> trash >> to;
		g.push_back({from, to});
		if (to.empty()) {
			epsilon_terms[from - 'A'] = 1;
		}
	}
	
	bool flag = 1;
	while (flag) {
		flag = 0;
		for (auto [from, to] : g) {
			if (epsilon_terms[from - 'A'] == 1) {
				continue;
			}
			int cnt = 0;
			for (int i = 0; i < to.size(); i++) {
				if ('A' <= to[i] && to[i] <= 'Z' && epsilon_terms[to[i] - 'A']) {
					cnt++;
				}
			}
			if (!to.empty() && cnt == to.size()) {
				epsilon_terms[from - 'A'] = 1;
				flag = 1;
			}
		}
	}

	for (int i = 0; i < 26; i++) {
		if (epsilon_terms[i])
			cout << static_cast<char>(i + 'A') << ' ';
	}
	return 0;
}

int cfg = g.size();
	for (int i = 1; i < new_notterm_num; i++) {
		for (int j = 1; j < new_notterm_num; j++) {
			if (i != j && chain_rules[i][j]) {
				for (int k = 0; k < cfg; k++) {
					if (g[k].first == j) {
						g.push_back({i, g[k].second});
					}
				}
			}
		}
	}