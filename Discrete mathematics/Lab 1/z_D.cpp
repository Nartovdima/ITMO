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


struct element{
	int type, num_of_element;
	vector <int> input;

	element(int type, int inputs_num, vector <int> input) {
		this -> type = type;
		this -> num_of_element = num_of_element;
		this -> input = move(input);
	}
};

int curr_element_num;
vector <element> ans;
vector <int> sum_element;

void get_sum_element(string s) {
	vector <int> nots(s.size());
	int tmp_num = 0;
	for (int i = 0; i < s.size(); i++) {
		if (s[i] == '0') {
			ans.pb(element(1, curr_element_num, vector <int> {i + 1}));
			nots[i] = curr_element_num;
			curr_element_num++;
		}
	}

	int prev = ((s[0] == '0') ? nots[0] : 1);
	for (int i = 1; i < s.size(); i++) {
		ans.pb(element(
					2, 
					curr_element_num, 
					vector <int> {prev, ((s[i] == '0') ? nots[i] : i + 1)}
		));
		prev = curr_element_num;
		curr_element_num++;
	}
	sum_element.pb(curr_element_num - 1);
}



signed main() {
    #ifdef _LOCAL
        clock_t Tsart = clock();
        freopen("input.txt", "r", stdin);
        freopen("output.txt", "w", stdout);
    #endif
    fast();

    int n, b = 0;
    cin >> n;
    curr_element_num = n + 1;
    for (int i = 0; i < (1 << n); i++) {
    	string s; int a;
    	cin >> s >> a;
    	if (a == 1) {
    		get_sum_element(s);
    		b++;
    	}
    }
    if (sum_element.size() == 0) {
    	cout << n + 2 << '\n';
    	cout << 1 << ' ' << 1 << '\n';
    	cout << 2 << ' ' << 1 << ' ' << n + 1 << '\n';
    	return 0;
    }
    int prev = sum_element[0];
    for (int i = 1; i < sum_element.size(); i++) {
    	ans.pb(element(
    				3,
    				curr_element_num,
    				vector <int> {prev, sum_element[i]}
    			));
    	prev = curr_element_num;
    	curr_element_num++;
    }
    cout << ans.size() + n << '\n';
    for (int i = 0; i < ans.size(); i++) {
    	cout << ans[i].type << ' ' << ans[i].input << '\n';
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