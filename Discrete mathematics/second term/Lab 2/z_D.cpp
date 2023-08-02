#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <unordered_set>
#include <array>

#define int long long
using namespace std;

constexpr int M = 1000000000 + 7;

int n;
char st;
vector<vector<pair<int, int>>> g;
int dp[101][101][101];

int cyk(string word) {
	int m = word.size();
	
	for (int i = 0; i < 26; i++) {
		for (int j = 0; j < m; j++) {
			for (int k = 0; k < m; k++) {
				dp[i][j][k] = 0;
			}
		}
	}

	for (int i = 0; i < m; i++) {
		for (int j = 0; j < 26; j++) {
			for (auto [term1, term2] : g[j]) {
				if (term2 == -1 && term1 == word[i] - 'a') {
					dp[j][i][i]++;
				}
			}
		}
	}


	for (int diff = 1; diff <= m; diff++) {
		for (int term = 0; term < 26; term++) {
			for (auto [term1, term2] : g[term]) {
				for (int i = 0; i <= m - diff; i++) {
					int j = i + diff - 1;
					for (int k = i; k < j; k++) {
						if (term2 != -1) {
							dp[term][i][j] = (dp[term][i][j] + (dp[term1][i][k] * dp[term2][k + 1][j]) % M ) % M;
						}
					}
				}
			}
		}
	}

	/*for (int i = 0; i < 26; i++) {
		for (int j = 0; j < m; j++) {
			for (int k = 0; k < m; k++) {
				cout << dp[i][j][k] << ' ';
			}
			cout << '\n';
		}
		cout << '\n';
	}*/
	
	return dp[st - 'A'][0][m - 1];
}

signed main() {
	/*freopen("nfc.in", "r", stdin);
	freopen("nfc.out", "w", stdout);*/
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	cout.tie(0);


	
	cin >> n >> st;
	array <bool, 26> epsilon_terms;
	epsilon_terms.fill(0);
	
	string in;
	getline(cin, in);
	g.resize(101);
	for (int i = 0; i < n; i++) {
		getline(cin, in);
		stringstream a(in);
		string trash, to;
		char from;
		a >> from >> trash >> to;
		if ('A' <= to[0] && to[0] <= 'Z') {
			g[from - 'A'].push_back({to[0] - 'A', to[1] - 'A'});
		} else {
			g[from - 'A'].push_back({to[0] - 'a', -1});
		}
		
	}
	
	string word;
	cin >> word;

	cout << cyk(word) << '\n';
	return 0;
}