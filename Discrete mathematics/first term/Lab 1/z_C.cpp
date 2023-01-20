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

graph circuit;
vector <vector <int>> truth_table;
vector <int> vars, vertex_power;
vector <int> tmp_circuit;
int n;

int next_boolean_string(vector <int> &num) {
	int num_of_ones = num.size() - 1;

	while (num[num_of_ones] == 1) {
		num[num_of_ones] = 0;
		num_of_ones--;
	}

	if (num_of_ones != -1)
		num[num_of_ones] = 1;

	return (num.size() - num_of_ones);

}

int bin2dec(vector <int> &binary_num) {
	int res = 0;
	for (int i = 0; i < binary_num.size(); i++) 
		res = 2 * res + binary_num[i];

	return res;
	
}

void recalculate_circuit() {
	for (int i = 0; i < n; i++) {
		vector <int> tmp_num(circuit[i].size());
		if (circuit[i].size() == 0)
			continue;
		for (int j = 0; j < circuit[i].size(); j++) {
			tmp_num[j] = tmp_circuit[circuit[i][j]];
		}

		int id = bin2dec(tmp_num);
		//cout << "hehe " << i << ' ' << id << '\n';
		tmp_circuit[i] = truth_table[i][id];
	}
}

signed main() {
    #ifdef _LOCAL
        clock_t Tsart = clock();
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    fast();

    
    cin >> n;

    circuit.resize(n);
    vertex_power.resize(n);
    truth_table.resize(n);

    for (int i = 0; i < n; i++) {
    	int m;
    	cin >> m;
    	if (m == 0) {
    		vars.pb(i);
    		vertex_power[i] = 0;
    	} else {
	    	for (int j = 0; j < m; j++) {
	    		int a;
	    		cin >> a;
	    		a--;
	    		circuit[i].pb(a);
	    		vertex_power[i] = max(vertex_power[a] + 1, vertex_power[i]);
	    	}
	    	truth_table[i].resize((1 << m));
	    	for (int j = 0; j < (1 << m); j++) {
	    		cin >> truth_table[i][j];
	    	}
	    }
    }
    
    cout << vertex_power[n - 1] << '\n';
    vector <int> binary_num(vars.size(), 0);
    int recalculation_num = vars.size();
    tmp_circuit.resize(n, 0);
    for (;;) {
    	for (int i = vars.size() - 1; i > (int)(vars.size()) - recalculation_num - 1; i--) {
    		tmp_circuit[vars[i]] = binary_num[i];
    	}
    	/*cout << vars.size() - recalculation_num - 1 << '\n';
    	cout << binary_num << '\n';
    	cout << tmp_circuit << '\n';*/
    	recalculate_circuit();
    	cout << tmp_circuit[n - 1];
		recalculation_num = next_boolean_string(binary_num);
		if (recalculation_num > vars.size())
			return 0;
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