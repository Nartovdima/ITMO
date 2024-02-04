#include <iostream>
#include <string>
#include <vector>
#include <cassert>

using namespace std;

vector <int> z_func(string s) {
	vector <int> z_function(s.size());
	int l = 0, r = 0;
	for (int i = 1; i < s.size(); ++i) {
		if (i <= r) {
			z_function[i] = min(r - i + 1, z_function[i - l]);
		}
		while (i + z_function[i] < s.size() && s[z_function[i]] == s[i + z_function[i]]) {
			++z_function[i];
		}
		if (i + z_function[i] - 1 > r) {
			l = i;
			r = i + z_function[i] - 1;
		}
	}
	return z_function;
}

int main() {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	cout.tie(0);

	string s, t;
	cin >> s >> t;
	string true_s = s;
	s = s + "@" + t + t;
	
	vector<int> z_function = z_func(s);
	/*cout << s << '\n';
	for (auto i : z_function) {
		cout << i << ' ';
	}
	cout << '\n';*/
 	int ans = -1;
	for (int i = true_s.size() + 1; i < z_function.size(); ++i) {
		if (z_function[i] == true_s.size()) {
			ans = i - static_cast<int>(true_s.size()) - 1;
			break;
		}
	}

	cout << ans << '\n';
	return 0;
}