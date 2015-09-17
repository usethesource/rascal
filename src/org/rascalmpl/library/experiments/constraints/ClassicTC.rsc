module experiments::constraints::ClassicTC

import experiments::constraints::LambdaSub;

data TYPE = var(str x)
          | errorType()
          ;

default TYPE lub(TYPE t1, TYPE2) = subtype(t1, t2) ? t2 : (subtype(t2, t1) ? t1 : errorType());

// Create a fresh type var
int ntypeVar = 0;
TYPE newTypeVar() { ntypeVar += 1; return var("S<ntypeVar>"); }

// A Type environment is a list of bindings

alias BINDING = tuple[TYPE t1, TYPE t1];
alias TENV = list[BINDING];

TYPE lookup(TYPE x, TENV env) { 
	for(<x, TYPE tp> <- env) 
		return tp;
    return errorType();
}

alias UNIF = tuple[bool unifiable, TENV env];

// Unify a single type equality
UNIF unify(TYPE t, errorType(), TENV env) = < false, env >;
UNIF unify(errorType(), TYPE t, TENV env) = < false, env >;

UNIF unify(tv:var(x), b:base(_), TENV env) = < true, <tv, b> + env >;
UNIF unify(b:base(_), tv:var(x), TENV env) = < true, <tv, b> + env >;

UNIF unify(tvx:var(x), tvy:var(y), TENV env) = tvx == tvy ? < true, env > : < true, <tvx, tvy> + env >;

UNIF unify(func(TYPE t1, TYPE t2), func(TYPE t3, TYPE t4), TENV env) = unify([ teq(t1, t3), teq(t2, t4) ], env);

default UNIF unify (TYPE t1, TYPE t2, TENV env) = < t1 == t2, env >;

test bool uni1() = unify(base(intType()), base(intType()), []) == < true, [] >;
test bool uni2() = unify(base(intType()), base(realType()), []) == < false, [] >;
test bool uni3() = unify(var("x"), var("x"), []) == < true, [] >;
test bool uni4() = unify(var("x"), var("y"), []) == < true, [<var("x"), var("y")>] >;

// Unify  list of type equalities

data TEQ = teq(TYPE t1, TYPE t2);

UNIF unify([ ], TENV env) = < true, env >;
UNIF unify([teq(TYPE t1, TYPE t2), *TEQ teqs], TENV env) {
	<b1, env1> = unify(t1, t2, env);
	<b2, env2> = unify(teqs, env1);
	if(b1 && b2) {
	   return < true, env2 >;
	}
	return < false, env >;
}

// A type checker result consists of a type and a type env
alias TC = tuple[TYPE mainType, TENV env];

// The actual type checking rules

TC tc(exp:\int(_), TENV env) = < base(intType()), env >;

TC tc(exp:\real(_), TENV env) = < base(realType()), env>;

TC tc(exp: var(x), TENV env) = < lookup(var(x), env), env >;

TC tc(exp:apply(E e1, E e2), TENV env) {
     <func(tp1, tp), env1> = tc(e1, env);
     <tp2, env2> = tc(e2, env1);
     if(tp1 == errorType() || tp2 == erorType() || !subtype(tp2, tp1)){
     	return < errorType(), env >;
     }
     return < tp, env3 >;
}
          
TC tc(exp:lambda(EXP x, TYPE t1, EXP e), TENV env) {
	<tp, env1> = tc(e, <x, t1> + env);
	if(tp == errorType()){
		return < errorType(), [] >;
	}
	return < tp, env1 >;
}
     
TC tc(exp:\if(EXP e1, EXP e2, EXP e3), TENV env) {
     <tp1, env1> = tc(e1, env);
     <tp2, env2> = tc(e2, env2);
     <tp3, env3> = tc(e3, env2);
     <unifiable, env4> = unify([ teq(tp1, base(boolType())),
                                 teq(tp2, tp3) ], env3);
     lb = lub(t2, tp3);
     if(tp1 == errorType() || tp2 == errorType() || tp3 == errorType() || !unifiable || lb == errorType()){
     	return < errorType(), env >;
     }
     return <lb, env4 >;
}

TC tc(op(OP _, E e1, E e2), TENV env) {
     <tp1, env1> = tc(e1, env);
     <tp2, env2> = tc(e2, env1);
     <unifiable, env3> = unify(tp1, tp2, env2);
     lb = lub(tp1, tp2);
     if(tp1 == errorType() || tp2 == errorType() || !unifiable || lb == errorType()){
     	return < errorType(), env >;
     }
     return < lb, env3 >;
}