#include <iostream>
#include <vector>
#include <set>

int main() {

	int n;
	std::cin >> n;
	std::vector <int> prufer_code(n - 2);
	std::vector <std::pair<int, int>> edges;
	std::set <int> vertexes;
	std::multiset <int> prufer_code_elements;

	for (int  i = 0; i < n; ++i) {
		vertexes.insert(i + 1);
	}

	for (int i = 0; i < n - 2; ++i) {
		std::cin >> prufer_code[i];
		prufer_code_elements.insert(prufer_code[i]);
		vertexes.erase(prufer_code[i]);
	}

	for (int i = 0; i < prufer_code.size(); ++i) {
		int u = prufer_code[i];
		int v = *vertexes.begin();

		edges.push_back({v, u});
		vertexes.erase(v);
		prufer_code_elements.erase(prufer_code_elements.find(u));
		if (prufer_code_elements.find(u) == prufer_code_elements.end()) {
			vertexes.insert(u);	
		}

	}

	edges.push_back({*vertexes.begin(), *++vertexes.begin()});

	for (auto i : edges) {
		std::cout << i.first << ' ' << i.second << std::endl;
	}
	return 0;
}
