module experiments::constraints::MicroKanren

/*
Microkanren based on 
	@book{Hemann:2013vg,
	author = {Hemann, J and Friedman, D},
	title = {{microkanren: A minimal functional core for relational programming}},
	publisher = {Proceedings of the 2013 Workshop on Scheme and {\ldots}},
	year = {2013}
	}
*/

import IO;

data VAR = var(int n);

alias BINDING = tuple[VAR var, value val];

alias BINDINGS = list[BINDING];

data PAIR = cons(value car, value cdr)  | nil();

value walk(value v, BINDINGS s){
	if(VAR u := v){
	   for(<u, ass> <- s)
	   	return walk(ass, s);
	}
	return v;
}

BINDINGS ext_s(VAR x, value v, BINDINGS s) = <x, v> + s;

data STATE = state(BINDINGS bindings, int counter);

STATE empty_state = state([], 0);

alias UNI = tuple[bool unifiable, BINDINGS bindings];

UNI unify(value u, value v, BINDINGS s){
	u = walk(u, s);
	v = walk(v, s);
	if(VAR uvar := u && VAR vvar := v && uvar.n == vvar.n)
		return <true, s>;
	if(VAR uvar := u)
		return <true, ext_s(uvar, v, s)>;
	if(VAR vvar := v)
		return <true, ext_s(vvar, u, s)>;
	if(cons(u1, u2) := u && cons(v1, v2) := v){
		<b, s1> = unify(u1, v1, s);
		if(b){
			return unify(u2, v2, s1);
		}
		return <false, s>;
	}
	if(u == v){
		return <true, s>;
	}
	return <false, s>;
}

alias STREAM = list[STATE];

STREAM unit(STATE sc) = [sc];
STREAM mzero = [];

GOAL equiv(value u, value v)  =
	STREAM (STATE sc) {
		<b, s1> = unify(u, v, sc.bindings);
		return b ? unit(state(s1, sc.counter)) : mzero;
		};

alias GOAL = STREAM(STATE sc);

bool isNull(STREAM s) = s == [];
STATE fst(STREAM s) = s[0];
STREAM rest(STREAM s) = s[1..];

GOAL call_fresh(GOAL(value q) f) =
    STREAM (STATE sc){
    	c = sc.counter;
    	return f(var(c))(state(sc.bindings, c + 1));
    };

GOAL disj(GOAL g1, GOAL g2) = STREAM(STATE sc) { return mplus( g1(sc), g2(sc) ); };
GOAL conj(GOAL g1, GOAL g2) = STREAM(STATE sc) { return bind( g1(sc), g2); };

STREAM mplus(STREAM s1,STREAM s2) =
    isNull(s1) ? s2 : [fst(s1), *mplus(rest(s1), s2)];

STREAM bind(STREAM s, GOAL g) =
    isNull(s) ? mzero
              : mplus(g(fst(s)), bind(rest(s), g));

// Variant for infinite streams (does not yet work):   
//STREAM mplus(value s1, STREAM s2) =
//    isNull(s1) ? s2 
//               : (value() is1 := s1) ? STREAM() { return mplus(is1(), s2); }
//               : [fst(s1), *mplus(rest(s1), s2)];
//
//STREAM bind(value s, GOAL g) =
//    isNull(s) ? mzero
//     		  : (value() is := s) ? mplus(is(), s2)
//              : mplus(g(fst(s)), bind(rest(s), g));
//GOAL delay(GOAL g) =
//	STREAM (STATE sc) {
//		return value() { return g(sc); };
//	};
              
// Examples

GOAL q() = call_fresh( GOAL(value q) { return equiv(q, 5); });

GOAL a() = call_fresh( GOAL(value a) { return disj(equiv(a, 5), equiv(a, 6)); });

GOAL a_and_b1 () =
   conj( call_fresh( GOAL(value a) { return equiv(a, 7); }),
         call_fresh( GOAL(value b) { return equiv(b, 5); }));
         
GOAL a_and_b2 () =
   conj( call_fresh( GOAL(value a) { return equiv(a, 7); }),
         call_fresh( GOAL(value b) { return disj(equiv(b, 5), equiv(b, 6)); }));

//GOAL fives (value x) = 
//	disj(equiv(x, 5), 
//	     STREAM (STATE sc) {
//		 	return STREAM { return value() { return fives(x)(sc); }; }; });
//
//GOAL do_fives() = call_fresh(fives);
                  
value main(list[value] args){
    //return q()(empty_state);
    //return a()(empty_state);
    //return a_and_b1()(empty_state);
    return a_and_b2()(empty_state);
    //return do_fives()(empty_state);
}