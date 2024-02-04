#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main() {
	string s;
	cin >> s;
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

	z_function[0] = s.size();
	for (auto i : z_function) {
		cout << i << ' ';
	}
	return 0;
}