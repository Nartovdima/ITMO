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
const int MAXN = 1e6 + 2;

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

struct Node {
    int max_seq;
    int left_seq;
    int right_seq;

    Node() = default;

    Node(int a, int b, int c) : max_seq(a), left_seq(b), right_seq(c) {}
};

string a;
Node tree[4 * MAXN];

Node evaluator(Node left, Node right, int l, int r) {
    return Node(left.max_seq + right.max_seq + min(left.left_seq, right.right_seq), left.left_seq + right.left_seq - min(left.left_seq, right.right_seq), left.right_seq + right.right_seq - min(left.left_seq, right.right_seq));
}

void recalc(int v, int l, int r) {
    auto & parent = tree[v];
    auto & left_child = tree[v * 2];
    auto & right_child = tree[v * 2 + 1];
    parent = evaluator(left_child, right_child, l, r); 
}

void build(int v, int l, int r) {
    if (r - l == 1) {
        if (a[l] == '(') {
            tree[v] = Node(0, 1, 0);
        } else {
            tree[v] = Node(0, 0, 1);
        }
        return;
    }
    int m = (l + r) / 2;
    build(v * 2, l, m);
    build(v * 2 + 1, m, r);
    recalc(v, l, r);
}

Node calc_length(int v, int l, int r, int ql, int qr) {
    if (r <= ql || qr <= l) {
        return Node(0, 0, 0);
    }
    if (ql <= l && r <= qr) {
        return tree[v];
    }
    int m = (l + r) / 2;
    return evaluator(calc_length(v * 2, l, m, ql, qr), calc_length(v * 2 + 1, m, r, ql, qr), l, r);
}

void print (int v, int l, int r) {
    cout << v << ' ' << l << ' ' << r << " (" << tree[v].max_seq << ' ' << tree[v].left_seq << ' ' << tree[v].right_seq << ")" << '\n';
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

    string s;
    cin >> a;
    int n = a.size();
    build(1, 0, n);
    int q;
    cin >> q;

    while (q--) {
        int l;
        int r;
        cin >> l >> r;
        l--;
        cout << calc_length(1, 0, n, l, r).max_seq * 2 << '\n';
    }

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