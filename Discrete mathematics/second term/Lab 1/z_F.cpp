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
const int MAXN = 1e5;

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

char terminal_nodes1[MAXN];
char terminal_nodes2[MAXN];
char used[MAXN];
int match[MAXN];

vector<set<pair<int, int>>> gr1, gr2;

bool dfs(int v, int u) {
	used[v] = 1;
	if (terminal_nodes1[v] != terminal_nodes2[u]) {
		return false;
	}
	match[v] = u;

	
	if (gr1[v].size() != gr2[u].size()) {
		return false;
	}

	auto it1 = gr1[v].begin();
	auto it2 = gr2[u].begin();


	bool res = 1;
	for (;it1 != gr1[v].end(); ++it1, ++it2) {
		auto [symb1, to1] = *it1;
		auto [symb2, to2] = *it2;
        //cout << to1 << ' ' << symb1 << '\n' << to2 << ' ' << symb2 << '\n' << '\n';
		if (symb1 != symb2) {
			return false;
		}
		if (used[to1] == 1) {
			res = res & (match[to1] == to2);			 
		} else {
			res = res & dfs(to1, to2);
		}
	}

	return res;

}

signed main() {
    #ifdef _LOCAL
        clock_t Tsart = clock();
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    fast();
    freopen("isomorphism.in", "r", stdin);
    freopen("isomorphism.out", "w", stdout);
    int n, m, k;
    cin >> n >> m >> k;
    for (int i = 0; i < k; i++) {
    	int a;
    	cin >> a;
    	terminal_nodes1[--a] = 1;
    }

   	gr1.resize(n); 
    for (int i = 0; i < m; i++) {
    	int a, b;
    	char c;
    	cin >> a >> b >> c;
    	gr1[--a].insert({c, --b});
    }

    int n2, m2, k2;
    cin >> n2 >> m2 >> k2;

    for (int i = 0; i < k2; i++) {
    	int a;
    	cin >> a;
    	terminal_nodes2[--a] = 1;
    }

    gr2.resize(n2);    
    for (int i = 0; i < m2; i++) {
    	int a, b;
    	char c;
    	cin >> a >> b >> c;
    	gr2[--a].insert({c, --b});
    }

    cout << (dfs(0, 0) == 1 ? "YES" : "NO") << '\n';
    #ifdef _LOCAL
        cerr << "Runtime: " << (ld)(clock() - Tsart) / CLOCKS_PER_SEC << '\n';
    #endif      
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