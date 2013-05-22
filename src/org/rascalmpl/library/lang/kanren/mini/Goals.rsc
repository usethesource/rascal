module lang::kanren::mini::Goals

import lang::kanren::mini::MiniKanren;

value(Subst) nullo(value x) = equ(x, []);
value(Subst) conso(value x, value d, value p)
  = equ([x, d], p);
  
value(Subst) pairo(value p) {
  a = fresh();
  d = fresh();
  return conso(a, d, p);
}

value(Subst) cdro(value p, value d) {
  a = fresh();
  return conso(a, d, p);
}

value(Subst) caro(value p, value a) {
  d = fresh();
  return cons(a, d, p);
}


value(Subst) listo(value l) {
  d = fresh();
  return \any([
    \all([nullo(l), succeed]),
    \all([pairo(l), cdro(l, d), defer1(listo, d)])]);
}

value(Subst) equ(value u, value v) = 
  value(Subst s) {
    //println("calling unify in equ: s = <s>");
    return unify(u, v, s);
  };

public value(Subst) succeed = 
  value(Subst s) { return s; };

public value(Subst) failure = 
  value(Subst s) {
    //println("FAILURE"); 
    return nil(); 
  };

value(Subst) \all(list[value(Subst)] goals) {
  if (goals == []) {
    return succeed;
  }
  return value(Subst s) {
    value x = ( s | bind(it, goal) | goal <- goals );
    return x;
    //if (Subst s2 := x) {
    //  return s2;
    //}
    //throw "Cannot happen";
  };
}

value(Subst) \any(list[value(Subst)] goals) {
  if (goals == []) {
    return succeed;
  }
  return value(Subst s) {
    return value() { return mplusAll(goals, s); };
  };
}

value(Subst) defer1(value(Subst)(value) func, value arg) 
  = value(Subst s) { return func(arg)(s); }; 


value(Subst) deferGoal(value(Subst) (value(Subst)) func, value(Subst) goal) 
  = value(Subst s) { return func(goal)(s); };

value(Subst) defer(value(Subst) (&T) func, &T arg)
  = value(Subst s) { return func(arg)(s); };

value(Subst) anyo(value(Subst) goal)  
  = \any([goal, defer(anyo, goal)]); 


public value(Subst) alwayso 
  = anyo(equ(false, false));

public value(Subst) nevero
  = anyo(equ(false, true));
  

