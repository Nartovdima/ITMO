#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main() {
	string s, t;
	cin >> s >> t;
	s = "#" + t + "@" + s;
	vector <int> pref(s.size());
	pref[0] = -1;

	for (int i = 1; i < s.size(); ++i) {
		int k = pref[i - 1];
		while (k != -1 && s[k + 1] != s[i]) {
			k = pref[k];
		}
		pref[i] = k + 1;
	}

	pref[0] = -1;

	for (int i = t.size() + 1; i < s.size(); i++) {
		if (pref[i] == t.size()) {
			cout << i - 2 * t.size() - 1 << ' ';
		}
	}
	return 0;
}