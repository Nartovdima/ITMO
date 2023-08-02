%	Map via AVL-Tree.
%	Author: Nartovdima.

node(K, V, L, R, H).

get_node_height(nil, 0) :- !.
get_node_height(node(_, _, _, _, H), H).

max(A, B, R) :- 
	A >= B,
	!,
	R is A.
max(A, B, R)	 :-
	R is B.
	
calc_heights(node(K, V, L, R, _), node(K, V, L, R, New_H)) :-
	get_node_height(L, H1),
	get_node_height(R, H2),
	max(H1, H2, Tmp_H),
	New_H is Tmp_H + 1.



left_rotation(node(K, V, L, node(K1, V1, L1, R1, _), _), Res) :-
	calc_heights(node(K, V, L, L1, _), Tmp_Res),
	calc_heights(node(K1, V1, Tmp_Res, R1, _), Res).
	
right_rotation(node(K, V, node(K1, V1, L1, R1, _), R, _), Res) :-
	calc_heights(node(K, V, R1, R, _), Tmp_Res),
	calc_heights(node(K1, V1, L1, Tmp_Res, _), Res).
	
big_left_rotation(node(K, V, L, node(K1, V1, node(K2, V2, L2, R2, _), R1, _), _), Res) :-
	right_rotation(node(K1, V1, node(K2, V2, L2, R2, _), R1, _), Tmp_R),
	left_rotation(node(K, V, L, Tmp_R, _), Res).

big_right_rotation(node(K, V, node(K1, V1, L1, node(K2, V2, L2, R2, _), _), R, _), Res) :-
	left_rotation(node(K1, V1, L1, node(K2, V2, L2, R2, _), _), Tmp_L),
	right_rotation(node(K, V, Tmp_L, R, _), Res).

calc_diff(Node1, Node2, Diff) :-
	get_node_height(Node1, Node1_H),
	get_node_height(Node2, Node2_H),
	Diff is Node1_H - Node2_H.



balance(node(K, V, L, R, H), Res) :-
	calc_diff(L, R, Diff),
	Diff is -2,
	!,
	choose_left_rotation(node(K, V, L, R, H), Res).
	
choose_left_rotation(node(K, V, L, node(K2, V2, L2, R2, H2), H), Res) :-
	calc_diff(L2, R2, Diff),
	Diff is 1,
	!,
	big_left_rotation(node(K, V, L, node(K2, V2, L2, R2, H2), H), Res).
	
choose_left_rotation(node(K, V, L, node(K2, V2, L2, R2, H2), H), Res) :-
	calc_diff(L2, R2, Diff),
	(Diff is 0; Diff is -1),
	left_rotation(node(K, V, L, node(K2, V2, L2, R2, H2), H), Res).
	
balance(node(K, V, L, R, H), Res) :-
	calc_diff(L, R, Diff),
	Diff is 2,
	!,
	choose_right_rotation(node(K, V, L, R, H), Res).
	
choose_right_rotation(node(K, V, node(K2, V2, L2, R2, H2), R, H), Res)	:-
	calc_diff(L2, R2, Diff),
	Diff is -1,
	!,
	big_right_rotation(node(K, V, node(K2, V2, L2, R2, H2), R, H), Res).
	
choose_right_rotation(node(K, V, node(K2, V2, L2, R2, H2), R, H), Res) :-
	calc_diff(L2, R2, Diff),
	(Diff is 1; Diff is 0),
	right_rotation(node(K, V, node(K2, V2, L2, R2, H2), R, H), Res).

balance(Tree, Tree).



map_put(node(Key, _, L, R, H), Key, Value, node(Key, Value, L, R, H)) :- !.
map_put(nil, Key, Value, node(Key, Value, nil, nil, 1)) :- !.
map_put(node(K, V, L, R, H), Key, Value, Result) :-
	K > Key,
	!,
	map_put(L, Key, Value, Tmp_Res),
	Tmp_Res1 = node(K, V, Tmp_Res, R, H),
	calc_heights(Tmp_Res1, Tmp_Res2),
	balance(Tmp_Res2, Result).
	
map_put(node(K, V, L, R, H), Key, Value, Result) :-
	K < Key,
	!,
	map_put(R, Key, Value, Tmp_Res),
	Tmp_Res1 = node(K, V, L, Tmp_Res, H),
	calc_heights(Tmp_Res1, Tmp_Res2),
	balance(Tmp_Res2, Result).

map_build([], nil) :- !.
map_build([(Key, Value) | T], TreeMap) :-
	map_build(T, Tmp_TreeMap),
	map_put(Tmp_TreeMap, Key, Value, TreeMap).


map_get(node(K, V, _, _, _), K, V) :- !.
map_get(node(K, _, L, R, _), Key, Value) :-
	K > Key,
	!,
	map_get(L, Key, Value).

map_get(node(K, _, L, R, _), Key, Value) :-
	K < Key,
	!,
	map_get(R, Key, Value).


map_remove(node(K, V, nil, nil, _), K, nil) :- !.
map_remove(node(K, V, L, R, H), K, Result) :-
	!,
	swap_with_leaf(node(K, V, L, R, H), Tmp_Res),
	calc_heights(Tmp_Res, Tmp_Res1),
	balance(Tmp_Res1, Result).
	
map_remove(node(K, V, L, R, H), Key, Result) :-
	K > Key,
	!,
	map_remove(L, Key, Tmp_Res),
	Tmp_Res1 = node(K, V, Tmp_Res, R, H),
	calc_heights(Tmp_Res1, Tmp_Res2),
	balance(Tmp_Res2, Result).
	
map_remove(node(K, V, L, R, H), Key, Result) :-
	K < Key, 
	!,
	map_remove(R, Key, Tmp_Res),
	Tmp_Res1 = node(K, V, L, Tmp_Res, H),
	calc_heights(Tmp_Res1, Tmp_Res2),
	balance(Tmp_Res2, Result).
	
swap_with_leaf(node(K, V, L, R, H), Res) :-
	get_node_height(L, L_H),
	get_node_height(R, R_H),
	L_H >= R_H,
	!,
	find_max(L, New_K, New_V),
	map_remove(L, New_K, Tmp_Res),
	Res = node(New_K, New_V, Tmp_Res, R, H).
	
swap_with_leaf(node(K, V, L, R, H), Res) :-
	get_node_height(L, L_H),
	get_node_height(R, R_H),
	L_H < R_H,
	!,
	find_min(R, New_K, New_V),
	map_remove(R, New_K, Tmp_Res),
	Res = node(New_K, New_V, L, Tmp_Res, H).
	
find_min(node(K, V, nil, _, _), K, V) :- !.
find_min(node(_, _, L, _, _), Key, Value) :-
	find_min(L, Key, Value).

find_max(node(K, V, _, nil, _), K, V) :- !.
find_max(node(_, _, _, R, _), Key, Value) :-
	find_max(R, Key, Value).

map_remove(TreeMap, K, TreeMap).


map_putIfAbsent(TreeMap, Key, Value, Result) :-
	\+ map_get(TreeMap, Key, _), 
	!,
	map_put(TreeMap, Key, Value, Result).

map_putIfAbsent(TreeMap, Key, Value, TreeMap).