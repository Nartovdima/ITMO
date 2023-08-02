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
const int M = 1e9 + 7;
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

pair<int, int> _lis_data[4 * MAXN]; // 1st - max, 2nd - nums

pair<int, int> calc_func(pair<int, int> a, pair<int, int> b) {
    pair <int, int> ans = a;
    if (a.ff == b.ff) {
        ans.ss = (ans.ss + b.ss) % M;
    }
    if (b.ff > a.ff) {
        ans = b;
    }
    return ans;
}

void recalc(int v) {
    _lis_data[v] = calc_func(_lis_data[v * 2], _lis_data[v * 2 + 1]);
}

void set_value(int v, int l, int r, int pos, pair<int, int> & vals) {
    if (r - l == 1 && l == pos) {
        _lis_data[v] = calc_func(vals, _lis_data[v]);
        return;
    }
    int m = (l + r) / 2;
    if (pos < m) {
        set_value(v * 2, l, m, pos, vals);
    } else {
        set_value(v * 2 + 1, m, r, pos, vals);
    }
    recalc(v);
}

pair<int, int> get_values(int v, int l, int r, int ql, int qr) {
    if (r <= ql || qr <= l) {
        return {0, 0};
    }
    if (ql <= l && r <= qr) {
        return _lis_data[v];
    }
    int m = (l + r) / 2;
    return calc_func(get_values(v * 2, l, m, ql, qr), get_values(v * 2 + 1, m, r, ql, qr));
}

void print(int v, int l, int r) {
    cout << v << " [" << l << "; " << r << ") " << _lis_data[v] << '\n';
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

    int n;
    cin >> n;
    vector <int> a(n);
    cin >> a;
    map <int, int> new_values;
    for (int i = 0; i < n; i++) {
        new_values.insert({a[i], 0});
    }
    int num = 0;
    for (auto &it : new_values) {
        it.second = num++;
    }
    for (auto &it : a) {
        it = new_values[it];
    }
    vector <int> dp(n, 0);
    vector <int> dp2(n, 0);
    dp[0] = 1;
    dp2[0] = 1;
    pair <int, int> curr = {1, 1};
    set_value(1, 0, n, a[0], curr);
    
    for (int i = 1; i < n; i++) {
        auto tmp = get_values(1, 0, n, 0, a[i]);
        dp[i] = tmp.ff + 1;
        if (tmp.ss == 0) {
            dp2[i] = 1;
        } else {
            dp2[i] = (dp2[i] + tmp.ss) % M;
        }
        curr = {dp[i], dp2[i]};
        set_value(1, 0, n, a[i], curr);
    }

    int ans = dp[0];
    for (int i = 1; i < n; i++) {
        ans = max(ans, dp[i]);
    }
    int true_ans = 0;
    for (int i = 0; i < n; i++) {
        if (dp[i] == ans) {
            true_ans = (true_ans + dp2[i]) % M;
        }
    }

    cout << true_ans << '\n';
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