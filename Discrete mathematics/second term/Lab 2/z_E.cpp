#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <unordered_set>

using namespace std;

constexpr int M = 1'000'000'000 + 7;

vector<pair<int, vector<int>>> g;
vector<vector<pair<int, int>>> g2;

int st;
int new_notterm_num = 27;

void pick_out_term() {
	for (int i = 0; i < g.size(); i++) {
		if (g[i].second.size() < 2) {
			continue;
		}
		for (int j = 0; j < g[i].second.size(); j++) {
			if (g[i].second[j] < 0) {
				g.push_back({new_notterm_num, vector<int>{g[i].second[j]}});
				g[i].second[j] = new_notterm_num++;
			}
		}
	}
}

void remove_long_rules() {
	for (int i = 0; i < g.size(); i++) {
		if (g[i].second.size() <= 2) {
			continue;
		}
		int k = g[i].second.size();
		int frst = new_notterm_num;
		for (int j = 1; j < k - 2; j++) {
			g.push_back({new_notterm_num, vector<int>{g[i].second[j], new_notterm_num + 1}});
			new_notterm_num++;
		}
		g.push_back({new_notterm_num++, vector<int>{g[i].second[k - 2], g[i].second[k - 1]}});
		g[i].second = vector<int>{g[i].second[0], frst};
	}
}

vector <bool> find_eps_terms() {
	vector <bool> eps_terms(new_notterm_num);
	bool flag = 1;
	while (flag) {
		flag = 0;
		for (auto [from, to] : g) {
			if (eps_terms[from]) {
				continue;
			}
			if (to[0] == 0) {
				eps_terms[from] = 1;
				flag = 1;
				continue;
			}
			int cnt = 0;
			for (int i = 0; i < to.size(); i++) {
				if (to[i] > 0 && eps_terms[to[i]]) {
					cnt++;
				}
			}
			if (!to.empty() && cnt == to.size()) {
				eps_terms[from] = 1;
				flag = 1;
			}
		}
	}
	return eps_terms;
} 

void remove_eps_terms() {
	vector <bool> eps_terms = find_eps_terms();
	for (int i = 0; i < g.size(); i++) {
		if (g[i].second.size() < 2) {
			continue;
		}
		bool flag = 0;
		if (eps_terms[g[i].second[0]]) {
			g.push_back({g[i].first, vector<int>{g[i].second[1]}});
		}
		if (eps_terms[g[i].second[1]]) {
			g.push_back({g[i].first, vector<int>{g[i].second[0]}});
		}
	}
	int j;
	for (j = 0; j < g.size();) {
		if (g[j].second[0] == 0) {
			swap(g[j], g[g.size() - 1]);
			g.erase(--g.end());
		}
		if (g[j].second[0] != 0) {
			j++;
		}
 	}
}

void remove_chain_rules() {
	vector<vector<bool>> chain_rules(new_notterm_num, vector<bool>(new_notterm_num));
	vector <pair<int, int>> terms;
	for (auto [from, to] : g) {
		chain_rules[from][from] = 1;
		if (to.size() == 1 && to[0] > 0) {
			chain_rules[from][to[0]] = 1;
		}
		if (to.size() == 1 && to[0] < 0) {
			terms.push_back({from, to[0]});
		}
	}

	/*for (int i = 1; i < new_notterm_num; i++) {
		for (int j = 1; j < new_notterm_num; j++) {
			cout << chain_rules[i][j] << ' ';
		}
		cout << '\n';
	}
	cout << '\n';*/
	
	for (int k = 1; k < new_notterm_num; k++) {
		for (int i = 1; i < new_notterm_num; i++) {
			for (int j = 1; j < new_notterm_num; j++) {
				if (chain_rules[i][k] == 1 && chain_rules[k][j] == 1) {
					chain_rules[i][j] = 1;
				}
			}
		}
	}

	/*for (int i = 1; i < new_notterm_num; i++) {
		for (int j = 1; j < new_notterm_num; j++) {
			cout << chain_rules[i][j] << ' ';
		}
		cout << '\n';
	}*/

	/*for (auto [it, symb] : terms) {
		for (int i = 1; i < new_notterm_num; i++) {
			if (i != it && chain_rules[i][it]) {
				g.push_back({i, vector<int>{symb}});
			}
		}
	}*/


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
	
	int i;
	for (i = 0; i < g.size();) {
		if (g[i].second.size() == 1 && g[i].second[0] > 0 && chain_rules[g[i].first][g[i].second[0]]) {
			swap(g[i], g[g.size() - 1]);
			g.erase(--g.end());
		} else {
			i++;
		}
	}
}

bool cyk(string word) {
	int m = word.size();
	int cfg_size = new_notterm_num - 1;

	vector<vector<vector<bool>>> dp(cfg_size);
	for (int i = 0; i < cfg_size; i++) {
		dp[i].resize(m);
	}
	for (int i = 0; i < cfg_size; i++) {
		for (int j = 0; j < m; j++) {
			dp[i][j].resize(m);
		}
	}


	for (int i = 0; i < cfg_size; i++) {
		for (int j = 0; j < m; j++) {
			for (int k = 0; k < m; k++) {
				dp[i][j][k] = 0;
			}
		}
	}

	for (int i = 0; i < m; i++) {
		for (int j = 0; j < cfg_size; j++) {
			for (auto [term1, term2] : g2[j]) {
				if (term2 == -1 && term1 == word[i] - 'a') {
					dp[j][i][i] = 1;
				}
			}
		}
	}

	for (int diff = 1; diff <= m; diff++) {
		for (int term = 0; term < cfg_size; term++) {
			for (auto [term1, term2] : g2[term]) {
				for (int i = 0; i <= m - diff; i++) {
					int j = i + diff - 1;
					for (int k = i; k < j; k++) {
						if (term2 != -1 && dp[term1][i][k] && dp[term2][k + 1][j]) {
							dp[term][i][j] = 1;
						}
					}
				}
			}
		}
	}

	/*for (int i = 0; i < cfg_size; i++) {
		for (int j = 0; j < m; j++) {
			for (int k = 0; k < m; k++) {
				cout << dp[i][j][k] << ' ';
			}
			cout << '\n';
		}
		cout << '\n';
	}*/
	
	return dp[st][0][m - 1];
}

int main() {

	freopen("cf.in", "r", stdin);
	freopen("cf.out", "w", stdout);
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	cout.tie(0);


	int n;
	char tmp_st;
	cin >> n >> tmp_st;
	st = tmp_st - 'A' + 1;

	string in;
	getline(cin, in);
	
	for (int i = 0; i < n; i++) {
		getline(cin, in);
		stringstream a(in);
		string trash, to;
		char from;
		a >> from >> trash >> to;

		vector <int> tmp;
		for (int i = 0; i < to.size(); i++) { // 0 : eps, + : (A...Z), - : (a...z) 
			if ('A' <= to[i] && to[i] <= 'Z') {
				tmp.push_back(to[i] - 'A' + 1);
			} else {
				tmp.push_back(-(to[i] - 'a' + 1));
			}
		}

		if (to.empty()) {
			tmp.push_back(0);
		} 

		g.push_back({from - 'A' + 1, tmp});
	}
	
	
	/*for (auto [a, b] : g) {
		cout << a << ' ';
		for (auto it : b) {
			cout << it << ' ';
		}
		cout << '\n';
	}
	cout << '\n';*/

	pick_out_term();
	
	/*for (auto [a, b] : g) {
		cout << a << ' ';
		for (auto it : b) {
			cout << it << ' ';
		}
		cout << '\n';
	}
	cout << '\n';*/

	remove_long_rules();

	/*for (auto [a, b] : g) {
		cout << a << ' ';
		for (auto it : b) {
			cout << it << ' ';
		}
		cout << '\n';
	}
	cout << '\n';*/

	remove_eps_terms();

	/*for (auto [a, b] : g) {
		cout << a << ' ';
		for (auto it : b) {
			cout << it << ' ';
		}
		cout << '\n';
	}
	cout << '\n';*/

	remove_chain_rules();
	
	/*for (auto [a, b] : g) {
		cout << a << ' ';
		for (auto it : b) {
			cout << it << ' ';
		}
		cout << '\n';
	}
	cout << '\n';*/

	g2.resize(new_notterm_num - 1);
	for (auto [from, to] : g) {
		if (to[0] > 0) {
			g2[from - 1].push_back({to[0] - 1, to[1] - 1});
		} else {
			g2[from - 1].push_back({-to[0] - 1, -1});
		}
	}

	/*for (int i = 0; i < g2.size(); i++) {
		for (int j = 0; j < g2[i].size(); j++) {
			cout << i << " -> " << g2[i][j].first << " " << g2[i][j].second << '\n';
		}
	}
	cout << '\n';*/

	string word;
	cin >> word;
	st--;
	cout << (cyk(word) == 1 ? "yes" : "no") << '\n';
	return 0;
}