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

int a[4 * MAXN];
int n;
vector <int> values;

void recalc(int v) {
	a[v] = a[v * 2] + a[v * 2 + 1];
}

void build(int v, int l, int r) {
	if (r - l == 1) {
		a[v] = (values[l] == 0 ? 1 : 0);
		return;
	}
	int m = (l + r) / 2;
	build(v * 2, l, m);
	build(v * 2 + 1, m, r);
	recalc(v);
}

void update_element(int v, int l, int r, int pos, int val) {
	if (pos == l && r - l == 1) {
		a[v] = (val == 0 ? 1 : 0);
		values[pos] = val;
		return;
	}
	int m = (l + r) / 2;
	if (pos < m) {
		update_element(v * 2, l, m, pos, val);
	} else {
		update_element(v * 2 + 1, m, r, pos, val);
	}
	recalc(v);
}

int count_zeros(int v, int l, int r, int ql, int qr) {
	if (qr <= l || r <= ql)
		return 0;
	if (ql <= l && r <= qr)
		return a[v];
	int m = (l + r) / 2;
	return count_zeros(v * 2, l, m, ql, qr) + count_zeros(v * 2 + 1, m, r, ql, qr);

}

int find_nth_zero(int v, int l, int r, int val, int ql, int qr) {
	//cout << v << ' ' << val << '\n';
	if (r <= ql || qr <= l) {
		return -1;
	}
	if (r - l == 1 && val == 1 && values[l] == 0) {
		return l + 1;
	} else if (r - l == 1 && (val != 1 || values[l] != 0)) {
		return -1;
	} 
	int m = (l + r) / 2;
	if (val <= a[v * 2]) {
		return find_nth_zero(v * 2, l, m, val, ql, qr);
	} else {
		return find_nth_zero(v * 2 + 1, m, r, val - a[v * 2], ql, qr);
	}
}

void print(int v, int l, int r) {
	cout << v << " [" << l << "; " << r << ") " << a[v] << '\n';
	if (r - l == 1)
		return;
	int m = (l + r) / 2;
	print(v * 2, l, m);
	print(v * 2 + 1, m, r);
}


signed main() {
    #ifdef _LOCAL
        clock_t Tsart = clock();
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    fast();

    cin >> n;
    values.resize(n);

    cin >> values;
    build(1, 0, n);
    //print(1, 0, n);
    int m;
    cin >> m;
    while (m--) {
    	char q;
    	cin >> q; 
    	if (q == 'u') {
    		int pos, val;
    		cin >> pos >> val;
    		pos--;
    		update_element(1, 0, n, pos, val);
    	} else {
    		int l, r, val;
    		cin >> l >> r >> val;
    		l--;
    		int del = count_zeros(1, 0, n, 0, l); 
    		//cout << "haha " << del << '\n';
    		cout << find_nth_zero(1, 0, n, val + del, l, r) << ' ';
    	}
    }
    //print(1, 0, n);
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