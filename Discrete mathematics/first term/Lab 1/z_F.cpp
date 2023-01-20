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

int n, m;
vector <int> vars;
vector <vector <int>> v;
int used;

int find_single() {
    vars.clear();
    vars.resize(n, -1);
    int ans = 0;
    for (int i = 0; i < m; i++) {
        int cnt = 0, last, lasts;
        for (int j = 0; j < n; j++) {
            if (v[i][j] != -1) {
                cnt++;
                last = j;
                lasts = v[i][j];
            }
        }
        //cout << "haha " << i << ' ' << cnt << '\n';
        if (cnt == 1) {
            vars[last] = lasts;
            ans++;
        }
    }
    //cout << vars << '\n';
    return ans;
}

int get_f (int a, int b) {
    if (a == 1)
        return b;
    else 
        return ((b == 1) ? 0 : 1);
}

bool find_clause() {
    for (int i = 0; i < m; i++) {
        int y = 0, cnt = 0;
        for (int j = 0; j < n; j++) {
            if (v[i][j] != -1 && vars[j] != -1) {
                cnt++;
                y |= get_f(v[i][j], vars[j]);
            }
            else if (v[i][j] != -1 && vars[j] == -1) {
                y = 1;
                break;
            }
        }
        if (y == 0 && cnt != 0) {
            return 1;
        }
    }
    return 0;
}

void delete_single() {
    for (int i = 0; i < n; i++) {
        if (vars[i] != -1) {
            for (int j = 0; j < m; j++) {
                if (v[j][i] != -1 && get_f(v[j][i], vars[i]) == 1) {
                    //cout << "hehe " << j << ' ' << vars[i] << '\n';
                    for (int k = 0; k < n; k++)
                        v[j][k] = -1;
                }
            }
        }
    }
    for (int i = 0; i < n; i++) {
        if (vars[i] != -1) {
            for (int j = 0; j < m; j++) {
                v[j][i] = -1;
            }
        }
    }

    /*cout << '\n';
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++)
            cout << v[i][j] << ' ';
        cout << '\n';
    }
    cout << '\n';*/
}

signed main() {
    #ifdef _LOCAL
        clock_t Tsart = clock();
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    fast();

    cin >> n >> m;
    v.resize(m);
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            int a;
            cin >> a;
            v[i].pb(a);
        }
    }
    
    for (;;) {
        if (find_single() == 0)
            break;
        if (find_clause()) {
            cout << "YES" << '\n';
            return 0;
        }
        delete_single();
    }
    cout << "NO" << '\n';
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