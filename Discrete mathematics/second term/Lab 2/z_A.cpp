#include <map>
#include <algorithm>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;


#define pb push_back
#define eb emplace_back 


#define fast(){ \
    ios_base::sync_with_stdio(0); \
    cin.tie(0); \
}

/* --------------------------
    Solutions starts here!!!
   -------------------------- */

array <bool, 300> term_vert;
vector<vector<pair <int, char>>> gr;

/*bool check_word_validity(int vertex, string word) {
    if (word.empty()) {
        return (term_vert[vertex]);
    }
    char curr_sym = word[0];
    for (auto &[ver, sym] : gr[vertex]) {
        if (sym == curr_sym) {
            return check_word_validity(ver, word.substr(1, word.size() - 1));
        }
    }
    return false;
}*/

signed main() {
    fast();

    freopen("autamaton.in", "r", stdin);
    freopen("automaton.out", "w", stdout);

    
    map<char, int> dict;
    int n;
    char st;
    cin >> n >> st;
    int num = 0;
    dict[st] = num++;
    gr.resize(1);
    for (int i = 0; i < n; i++) {

        char a;
        string trash;
        string b;
        cin >> a >> trash >> b;

        if (dict.find(a) == dict.end()) {
            dict[a] = num++;
            gr.push_back(vector<pair<int, char>>(0));
        }

        if (b.size() == 1) {
            gr[dict[a]].pb({num++, b[0]});
            gr.push_back(vector<pair<int, char>>(0));
            term_vert[num - 1] = 1;
        } else {

            if (dict.find(b[1]) == dict.end()) {
                dict[b[1]] = num++;
                gr.push_back(vector<pair<int, char>>(0));
            }
            
            gr[dict[a]].pb({dict[b[1]], b[0]});
        }
    }

    /*for (int i = 0; i < gr.size(); i++) {
        for (int j = 0; j < gr[i].size(); j++) {
            cout << gr[i][j].first << ' ' << gr[i][j].second << ' ';
        }
        cout << '\n';
    }*/
    int m;
    cin >> m;
    while (m--) {
        string s;
        cin >> s;
        vector<bool> accepts1(num, 0);
        vector<bool> accepts2;
        accepts1[0] = 1;

        for (int i = 0; i < s.size(); i++) {
            accepts2.resize(num, 0);
            for (int j = 0; j < num; j++) {
                if (accepts1[j]) {
                    //cout << gr[j].size() << '\n';
                    for (auto &it : gr[j]) {
                        if (it.second == s[i]) {
                            accepts2[it.first] = 1;
                        }
                    }
                }
            }
            accepts1 = std::move(accepts2);
        }

        
        bool ans = 0;
        for (int i = 0; i < num; i++) {
            //cout << term_vert[i] << '\n';
            if (accepts1[i] && term_vert[i]) {
                ans = 1;
                break;
            }
        }

        cout << (ans ? "yes" : "no") << '\n';
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