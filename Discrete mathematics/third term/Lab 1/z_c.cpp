#include <iostream>
#include <vector>
#include <algorithm>
#include <string>


bool cmp(int a, int b) {
	std::cout << 1 << ' ' << a << ' ' << b << std::endl;
	std::string response;
	std::cin >> response;
	if (response == "YES") {
		return true;
	} else { 
		return false;
	}
}

int main() {
	int n;
	std::cin >> n;
	std::vector <int> order(n);

	for (int i = 0; i < n; i++) {
		order[i] = i + 1;
	}
	std::stable_sort(order.begin(), order.end(), cmp);

	std::cout << 0 << ' ';
	for (auto it : order) {
		std::cout << it << ' ';
	}
	std::cout << std::endl;
	return 0;
}