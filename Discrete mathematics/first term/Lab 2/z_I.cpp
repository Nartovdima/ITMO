
#include <iostream>

#include <string>
#include <vector>


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

const int M = 1e9;
const int MAXN = 2 * 1e5;


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



signed main() {
    //fast();

    int flag;
    cin >> flag;
    string s;
    cin >> s;
    if (flag == 1) {
        int n = s.size();
        vector <int> message;
        message.clear();
        message.pb(0);
        int m = 0;
        for (int i = 1, j = 0; j < n; i++) {
            if (i == (1 << __lg(i))) {
                message.pb(0);
                m++;
            } else {
                message.pb((int)(s[j++] - '0'));
            }
        }

        int k = message.size();
        for (int q = 0; q < m; q++) {
            for (int i = (1 << q), j = 0; i < k;) {
                if (j == (1 << q)) {
                    j = 0;
                    i += (1 << q);
                    continue;
                }
                message[(1 << q)] = message[(1 << q)] ^ message[i];
                i++;
                j++;
            }
        }
        string tmp;
        for (int i = 1; i < k; i++) {
            tmp += message[i] + '0';
        }
        /*if (tmp == "00000000000000") {
            for(;;);
        }*/
        //cout << "00000000000000" << '\n';
        cout << tmp << '\n';
        return 0;
    } else {
    	vector <int> message;
        message.clear();
        message.pb(0);
        for (int i = 0; i < s.size(); i++) {
            message.pb((int)(s[i] - '0'));
        }
        int m = __lg(s.size()) + 1;
        int k = message.size();
        vector <int> message2(k, 0);
        for (int q = 0; q < m; q++) {
            for (int i = (1 << q) + 1, j = 1; i < k;) {
                if (j == (1 << q)) {
                    j = 0;
                    i += (1 << q);
                    continue;
                }
                message2[(1 << q)] = message2[(1 << q)] ^ message[i];
                i++;
                j++;
            }
        }
        int sum = 0;
        for (int i = 0; i < m; i++) {
            if (message[(1 << i)] != message2[(1 << i)]) {
                sum += (1 << i);
            }
        }
        string tmp;
        if (message[sum] == 1)
            message[sum] = 0;
        else
            message[sum] = 1;


        for (int i = 1; i < k; i++) {
            if (i != (1 << __lg(i)))
                tmp += message[i] + '0';
        }
        cout << tmp << '\n';
        return 0;
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