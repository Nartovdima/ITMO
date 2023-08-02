#include <algorithm>
#include <iostream>
#include <iterator>
#include <ext/rope>
#include <iomanip>
#include <fstream>
#include <cassert>
#include <string>
#include <vector>
#include <bitset>
#include <cmath>
#include <map>
#include <set>

using namespace __gnu_cxx;
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
#define fir first
#define sec second
#define watch(x) cout << (#x) << "-" << x << '\n'
const int INF = 0x3f3f3f3f;
const ld EPS = 1e-8;
const ld PI = 3.14159265358979323;
/*const int MAX_MEM = 1e8;
int mpos = 0;
char mem[MAX_MEM];*/
#define fast(){ \
    ios_base::sync_with_stdio(0); \
    cin.tie(0); \
}
/*inline void * operator new ( size_t n ) {
    assert((mpos += n) <= MAX_MEM);
    return (void *)(mem + mpos - n);
}
inline void operator delete (void *) noexcept { }*/

vector <string> v;
string a[5], s;

signed main()
{
    fast();
    #ifdef _LOCAL
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    int k;
    cin >> k;
    a[0] = "edHs";
    a[1] = "fEHs";
    //string a[2] = "edIT";

    for(int i = 0; i < (1 << 10); i++){
        s.clear();
        for(int j = 0; j < 10; j++)
            if (i & (1 << j))
                s += a[0];
            else
                s += a[1];
        v.pb(s);
    }
    for(int i = 0; i < k; i++)
        cout << v[i] << '\n';
    return 0;
}