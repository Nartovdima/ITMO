#include <iostream>
#include <vector>
#include <string>

using namespace std;

#define int long long


int n = 20;
int M = 1000000007;

vector<vector<int>> matrix_mul(vector<vector<int>>& x, vector<vector<int>>& y) {
    vector<vector<int>> res(n, vector<int>(n, 0));

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                res[i][j] = (res[i][j] + (x[i][k] * y[k][j]) % M) % M;
            }
        }
    }

    return res;
}

vector<vector<int>> binpow(vector<vector<int>>& a, int num) {
    if (num == 0) {
        vector<vector<int>> tmp_res(n, vector<int>(n, 0));
        for (int i = 0; i < n; i++) {
            tmp_res[i][i] = 1;            
        }
        return tmp_res;
    }
    if (num % 2 == 1) {
        vector<vector<int>> tmp_a = binpow(a, num - 1);
        return matrix_mul(tmp_a, a);
    } else {
        vector<vector<int>> b = binpow(a, num / 2);
        return matrix_mul(b, b);
    }
}

vector<vector<int>> pow(vector<vector<int>>& a, int num) {
	vector<vector<int>> res(n, vector<int>(n, 0));
    for (int i = 0; i < n; i++) {
        res[i][i] = 1;            
    }

    for (int i = 0; i < num; ++i) {
    	res = matrix_mul(res, a);
    }
    return res;
}

signed main(signed argc, char *argv[]) {
    vector<vector<int>> g(n, vector<int>(n, 0));
    int x = stoi(static_cast<string>(argv[1]));

    for (int i = 0; i < n; ++i) {
    	for (int j = 0; j < n; ++j) {
    		g[i][j] = x++;
    	}
    }

    int step = 25000;
    vector<vector<int>> ans = pow(g, step);

    int sum = 0;

    for (int i = 0; i < n; ++i) {
    	for (int j = 0; j < n; ++j) {
    		sum = (sum + g[i][j]) % M;
    	}
    }

    cout << sum / g[0][0] << '\n';
    return 0;
}
