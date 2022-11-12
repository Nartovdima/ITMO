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

#define int long long

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

vector <int> a;
int n, y, b;
const int one = 1;

bool get_bit(int a, int pos) {
	//cout << "hehe " << (1 << pos) << '\n';
	if ((a & (one << pos)))
		return 1;
	else
		return 0;
}

bool check_func() {
	vector <int> vars(64, -1);
	for (int i = 0; i < y; i++) {

		int tmp = 0;
		for (int j = 0; j < n; j++) {
			tmp = 2 * tmp + get_bit(a[j], i);
		}
		//cout << get_bit(b, i) << ' ' << vars[tmp] << '\n';
		if (vars[tmp] != -1 && vars[tmp] != get_bit(b, i)) {
			return 0;
		}
		vars[tmp] = get_bit(b, i);
	}
	return 1;
}

string get_clause(int pos) {
	string res;
	for (int i = 0; i < n; i++) {
		if (get_bit(a[i], pos) == 1) {
			if (i == 0)
				res += to_string(i + 1);
			else
				res += "&" + to_string(i + 1);
		} else {
			if (i == 0)
				res += "~" + to_string(i + 1);
			else
				res += "&~" + to_string(i + 1);
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

    cin >> n;
    a.resize(n);
    cin >> a;
   	cin >> b;
   	y = -INF;
   	for (int i = 0; i < n; i++) {
   		y = max(y, __lg(a[i]));
   	}
    y = max(y, __lg(b));
    y += 2;
    if (b == 0) {
    	cout << "1&~1" << '\n';
    	return 0;
    }
    //cout << y << '\n';
    if (!check_func()) {
    	cout << "Impossible" << '\n';
    	return 0;
    }
    string ans;
    int cnt = 0;
    //cout << get_bit(b, 33) << '\n';
    for (int i = 0; i < y; i++) {
    	if (get_bit(b, i) == 1) {
    		//cout << i << '\n';
    		if (cnt == 0)
    			ans += get_clause(i);
    		else	
    			ans += "|" + get_clause(i);
    		cnt++;
    	}
    	
    }
    cout << ans << '\n';
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