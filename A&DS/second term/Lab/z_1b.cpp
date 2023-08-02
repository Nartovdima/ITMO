#include <iostream>
#include <vector>
#include <cmath>
#include <string>
#include <algorithm>
#include <iomanip>
#include <map>
#include <set>
#include <bitset>
#include <fstream>
#include <unordered_set>
#include <unordered_map>

using namespace std;

/*#pragma GCC optimize("Ofast")
#pragma GCC optimize("no-stack-protector")
#pragma GCC optimize("unroll-loops")
#pragma GCC target("sse,sse2,sse3,ssse3,popcnt,abm,mmx,tune=native")
#pragma GCC optimize("fast-math")*/
     
 
#define eb emplace_back
#define pb push_back
#define int long long
#define ld long double 
#define f first
#define s second
#define deb(a) cerr << #a << " = " << a << '\n';
#define fast() { \
    ios_base::sync_with_stdio(0); \
    cin.tie(0); \
    cout << fixed << setprecision(10); \
    cerr << fixed << setprecision(10); \
}
const int INF = 1e18 + 7;
const ld EPS = 1e-8;
const int MAXI = 20000;
const int MOD = 16714589;
const int MAXST = 2000000;
const int P = 40;
const ld PI = 3.14159265358979323;
const int MAXN = 1e5 + 10;

int a[MAXN];
int t[4 * MAXN];
int t2[4 * MAXN];

void build(int v, int tl , int tr)
{
    if (tl == tr) {
        t[v] = a[tl];
        t2[v] = 1;
    } else {
        int tm = (tl + tr) / 2;
        build(v * 2, tl, tm);
        build(v * 2 + 1, tm + 1, tr);
        if (t[v * 2] == t[v * 2 + 1]) {
            t[v] = t[v * 2];
            t2[v] = t2[v * 2] + t2[v * 2 + 1];
        } else if (t[v * 2] > t[v * 2 + 1]) {
            t[v] = t[v * 2];
            t2[v] = t2[v * 2];
        } else {
            t[v] = t[v * 2 + 1];
            t2[v] = t2[v * 2 + 1];
        }
    }
}

pair <int, int> fm(int v, int tl, int tr, int l, int r)
{
    if (l > r)
        return {-INF, -1};
    if (tl == l && tr == r)
        return {t[v], t2[v]};
    int tm = (tl + tr) / 2;
    pair <int, int> first_query = fm(v * 2, tl, tm, l, min(r, tm));
    pair <int, int> second_query = fm(v * 2 + 1, tm + 1, tr, max(l, tm + 1), r);
    if (first_query.first == second_query.first) {
        return {first_query.first, first_query.second + second_query.second};
    } else if (first_query.first > second_query.first) {
        return first_query;
    } else {
        return second_query;
    }
    
}

signed main()
{
    fast();
    #ifdef LOCAL
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    int n;
    cin >> n;
    for(int i = 0; i < n; i++)
        cin >> a[i];
    build(1, 0, n - 1);
    int q;
    cin >> q;
    for(int i = 0; i < q; i++)
    {
        int l, r;
        cin >> l >> r;
        l--;
        r--;
        auto [mx, id] = fm(1, 0, n - 1, l, r);
        cout << mx << ' ' << id << '\n';
    }
    /*for (int i = 0; i < pow(2, n); i++)
        cout << t2[i] << ' ';*/
    return 0;
}

