set_min_div(Num, Div) :-
	min_div(Num, A), !.
	
set_min_div(Num, Div) :-
	assert(min_div(Num, Div)).


init(MAX_N) :-
	generate_composite_numbers_list(2, MAX_N).

	
generate_composite_numbers_list(N, MAX_N) :-
	N =< MAX_N,
	\+ (composite_numbers_list(N)),
	set_min_div(N, N),
	Tmp_N is N * N,
	add_composite_numbers(Tmp_N, N, MAX_N).

generate_composite_numbers_list(N, MAX_N) :-
	N =< MAX_N,
	N1 is N + 1,
	generate_composite_numbers_list(N1, MAX_N).

add_composite_numbers(N, Step, MAX_N) :-
	N =< MAX_N,
	assert(composite_numbers_list(N)),
	set_min_div(N, Step),
	N1 is N + Step,
	add_composite_numbers(N1, Step, MAX_N).


prime(N) :-
	N > 1,
	\+ (composite_numbers_list(N)).

composite(N) :-
	N > 1,
	composite_numbers_list(N).

prime_divisors(1, []) :- !.

prime_divisors(N, [F | T]) :-
	number(N),
	!,
	min_div(N, F),
	N1 is div(N, F),
	prime_divisors(N1, T).

calculate_number([], 1).

calculate_number([F], F) :- 
	prime(F).

calculate_number([F, S | T], R) :-
	prime(F),
	prime(S),
	F =< S,
	calculate_number([S | T], R1),
	R is R1 * F.
		
prime_divisors(N, Divisors) :-
	var(N),
	!,
	calculate_number(Divisors, N).

divs_num(N, Div, R) :-
	A is mod(N, Div),
	A \= 0, 
	!,
	R is 0.
	
divs_num(N, Div, R) :-
	0 is mod(N, Div),
	N1 is div(N, Div),
	divs_num(N1, Div, R1),
	R is R1 + 1.
	
compact_prime_divisors(1, []) :- !.

compact_prime_divisors(N, [(Base, Exp) | T]) :-
	number(N),
	!,
	min_div(N, Base),
	divs_num(N, Base, Exp),
	N1 is div(N, round(Base ** Exp)),
	compact_prime_divisors(N1, T).


short_notation_number_calc([], 1).

short_notation_number_calc([(Base, Exp)], R) :-
	prime(Base),
	R is round(Base ** Exp).

short_notation_number_calc([(Base1, Exp1), (Base2, Exp2) | T], R) :-
	prime(Base1),
	prime(Base2),
	Base1 < Base2,
	short_notation_number_calc([(Base2, Exp2) | T], R1),	
	Tmp_R is round(Base1 ** Exp1),
	R is Tmp_R * R1.
	
compact_prime_divisors(N, CDs) :-
	var(N),
	short_notation_number_calc(CDs, N).
