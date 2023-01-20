#include <unordered_set>
#include <unordered_map>
//#include <ext/rope>
#include <algorithm>
#include <iostream>
#include <iterator>
#include <iomanip>
#include <fstream>
#include <cassert>
#include <chrono>
#include <string>
#include <vector>
#include <bitset>
#include <cstdio>
#include <random>
#include <stack>
#include <cmath>
#include <queue>
#include <ctime>
#include <map>
#include <set>

//using namespace __gnu_cxx;
using namespace std;

/*#pragma GCC optimize("Ofast")
#pragma GCC optimize("no-stack-protector")
#pragma GCC optimize("unroll-loops")
#pragma GCC target("sse,sse2,sse3,ssse3,popcnt,abm,mmx,tune=native")
#pragma GCC optimize("fast-math")*/

typedef long long ll;   
typedef long double ld;
typedef pair <int, int> ii;
typedef pair <ll, ll> dl;
typedef vector <vector <int> > graph;

#define pb push_back
#define eb emplace_back 
#define ff first
#define ss second
#define show(x) cerr << (#x) << "=" << x << '\n'
#define ptree_node tree_node*
#define sqr(a) ((a) * (a))
#define all(x) (x).begin(), (x).end()
 
const int INF = 1e9;
const ld EPS = 1e-8;
const ld PI = atan2(0.0, -1.0);
const int M = 1e9;
const int MAXN = 2 * 1e5;

#ifdef _LOCAL
    mt19937 rnd(223);
    mt19937_64 rndll(231);
#else
    mt19937 rnd(chrono::high_resolution_clock::now().time_since_epoch().count());
    mt19937_64 rndll(chrono::high_resolution_clock::now().time_since_epoch().count());
#endif

template<typename T>             istream& operator>>(istream& is,  vector<T> &v){for (auto& i : v) is >> i;                       return is;}
template<typename T>             ostream& operator<<(ostream& os,  vector<T>  v){for (auto& i : v) os << i << ' ';                return os;}
template<typename T, typename U> istream& operator>>(istream& is, pair<T, U> &p){is >> p.first >> p.second;                       return is;}
template<typename T, typename U> ostream& operator<<(ostream& os, pair<T, U>  p){os << '(' << p.first << "; " << p.second << ')'; return os;}

/*const int MAX_MEM = 1e8; 
int mpos = 0; 
char mem[MAX_MEM]; 
inline void * operator new ( size_t n ) { 
    assert((mpos += n) <= MAX_MEM); 
    return (void *)(mem + mpos - n); 
} 
inline void operator delete (void *) noexcept { } */

#define fast(){ \
    ios_base::sync_with_stdio(0); \
    cin.tie(0); \
}

/* --------------------------
    Solutions starts here!!!
   -------------------------- */

vector <int> NotPostClasses;

int binpow (int a, int b) {
    int res = 1;
    while (b) {
        if (b & 1)
            res = res * a;
        a = a * a;
        b >>= 1;
    }
    return res;
}

int IsKeepingZero(vector <int> &truthTable) {
	return (truthTable[0] == 0);
}

int IsKeepingOne(vector <int> &truthTable) {
	return (truthTable[truthTable.size() - 1] == 1);
}

/*int IsMonotonic(vector <int> &truthTable) {
	int n = __lg(truthTable.size()); 
	for (int i = 0; i < n; i++) { // 2
		int m = binpow(2, i);
		for (int j = 0; j < m; j++) { // 2
			int r = binpow(2, n - i - 1);
			for (int k = 0; k < r; k++) { // 1
				if (truthTable[r * j * 2 + k] > truthTable[r * (j * 2 + 1) + k])
					return 0;
			}
		}
	}
	return 1;
}*/

int IsMonotonic(vector <int> &truthTable) {
	for (int i = 0; i < truthTable.size(); i++) {
		for (int j = 0; j < __lg(truthTable.size()); j++) {
			//cout << "heh " << i << ' ' << j << ' ' << ((i & (1 << j)) == 0) << ' ' << truthTable[i] << ' ' << truthTable[i | (1 << j)] << ' ' << (truthTable[i] > truthTable[i | (1 << j)]) << '\n';
			if ((i & (1 << j)) == 0 && truthTable[i] > truthTable[i | (1 << j)])
				return 0;
		}
	}
	return 1;
}

/*int IsSelfDual(vector <int> &truthTable) {
	for (int i = 0; i < truthTable.size() / 2; i++) 
		if (truthTable[i] == truthTable[truthTable.size() - i - 1])
			return 0;
	return 1;
}*/

int IsSelfDual(vector <int> &truthTable) {
	for (int i = 0; i < truthTable.size(); i++) 
		if (truthTable[i] == truthTable[i ^ (truthTable.size() - 1)])
			return 0;
	return 1;
}

int IsLinear(vector <int> &truthTable) {
	vector <vector <int>> tmp;
	tmp.resize(truthTable.size());

	for (int i = 0; i < truthTable.size(); i++) {
		tmp[i].resize(truthTable.size() - i);
		tmp[i][0] = truthTable[i];
	}

	for (int i = 1; i < truthTable.size(); i++) {
		for (int j = 0; j < truthTable.size() - i; j++) {
			tmp[j][i] = tmp[j][i - 1] ^ tmp[j + 1][i - 1];
		}
	}

	int y = 1;
	for (int i = 1; i < truthTable.size(); i++) {
		if (i != y && tmp[0][i] == 1)
			return 0; 
		if (i == y)
			y <<= 1;
	}
	return 1;
}

void checkFunction(vector <int> &truthTable) {
	NotPostClasses[0] = min(IsKeepingZero(truthTable), NotPostClasses[0]);
	NotPostClasses[1] = min(IsKeepingOne(truthTable), NotPostClasses[1]);
	NotPostClasses[2] = min(IsMonotonic(truthTable), NotPostClasses[2]);
	NotPostClasses[3] = min(IsSelfDual(truthTable), NotPostClasses[3]);
	NotPostClasses[4] = min(IsLinear(truthTable), NotPostClasses[4]);
	//cout << IsKeepingZero(truthTable) << ' ' << IsKeepingOne(truthTable) << ' ' << IsMonotonic(truthTable) << ' ' << IsSelfDual(truthTable) << ' ' << IsLinear(truthTable) << '\n'; 
}

signed main() {
    fast();

    NotPostClasses.resize(5, 1);
    int n;
    cin >> n;
    for (int i = 0; i < n; i++) {
    	int t;
    	cin >> t;
    	vector <int> truthTable(binpow(2, t));
    	string s;
    	cin >> s;
    	for (int j = 0; j < binpow(2, t); j++) {
    		truthTable[j] = s[j] - '0';
    	}
    	checkFunction(truthTable);
    }

    for (int i = 0; i < 5; i++) {
    	if (NotPostClasses[i] == 1) {
    		cout << "NO" << '\n';
    		return 0;
    	}
    }

    cout << "YES" << '\n';      
    return 0;
}
 
/*     .^----^.
      (= o  O =)
       (___V__)
        _|==|_
   ___/' |--| |
  / ,._| |  | '
 | \__ |__}-|__}
  \___)`*/