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

graph g;
vector <int> used, up, tin;
int timer = 0;
set <int> st;
map <ii, int> mp;
map<int, bool> extra_edges;
map <int, pair<int, int>> edge_by_num;

void dfs(int v, int p = -1) {
    used[v] = 1;
    tin[v] = up[v] = timer++;
    for (auto u : g[v]) {
        if (u == p)
            continue;
        if (used[u])
            up[v] = min(up[v], tin[u]);
        else {
            dfs(u, v);
            up[v] = min(up[v], up[u]);
            if (up[u] > tin[v] && !extra_edges[mp[{v, u}]]) {
                st.insert(mp[{v, u}]);
            }
        }
    }
}

void dfs2(int v, vector<vector<int>>& g, vector<int>& used, vector <int>& comp, int color) {
    used[v] = 1;
    comp[v] = color;
    for (auto u : g[v]) {
        if (!used[u]) {
            dfs2(u, g, used, comp, color);
        }
    }
}

signed main() {
    fast();

    int n, m;
    cin >> n >> m;
    g.resize(n);
    for (int i = 0; i < m; i++) {
        int a, b;
        cin >> a >> b;
        a--; b--;
        g[a].pb(b);
        g[b].pb(a);
        auto it = mp.find({a, b});
        if (it != mp.end()) {
            extra_edges[it -> second] = 1;
            extra_edges[i] = 1;
        }
        mp[{a, b}] = i;
        mp[{b, a}] = i;
        edge_by_num[i] = {a, b};
    }
    
    used.resize(n, 0); tin.resize(n, 0); up.resize(n, 0);
    for (int i = 0; i < n; i++)
        if (!used[i])
            dfs(i);


    for (auto brigde : st) {
        auto [v, u] = edge_by_num[brigde];
        for (int i = 0; i < g[v].size(); ++i) {
            if (g[v][i] == u) {
                swap(g[v][i], g[v][g[v].size() - 1]);
                g[v].pop_back();
            }
        }
        for (int i = 0; i < g[u].size(); ++i) {
            if (g[u][i] == v) {
                swap(g[u][i], g[u][g[u].size() - 1]);
                g[u].pop_back();
            }
        }
    }

    used.clear();
    used.resize(n, 0);
    vector <int> comp(n);
    int color = 1;
    for (int i = 0; i < n; ++i) {
        if (!used[i]) {
            dfs2(i, g, used, comp, color);
            color++;
        }
    }

    cout << color - 1 << '\n';
    for (auto it : comp) {
        cout << it << ' ';
    }  
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