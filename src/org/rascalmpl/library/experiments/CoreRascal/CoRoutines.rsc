module experiments::CoreRascal::CoRoutines

// Co-routines

// The lambda expression part

public data Exp = 
            label(str name) 
          | nil() 
          | lambda(str id, Exp exp)
          | id(str name)
          | apply(Exp exp1, Exp exp2)
          | assign(str id, Exp exp)
          | ifelse(Exp exp1, Exp exp2, Exp exp3)
          | equ(Exp exp1, Exp exp2)
          | C(Exp exp)									// context constructor: used to define the focal point of reduction
          ;
          
// The co-routine part

public data Exp =
            labeled(str name, Exp exp)
          | creat(Exp exp)
          | resume(Exp exp1, Exp exp2)
          | yield(Exp exp)
          ;  

bool isValue(nil()) = true;
bool isValue(label(str name)) = true;
bool isValue(lambda(str id, Exp exp)) = true;
default bool isValue(Exp e) = false;

set[str] FV(Exp e) = {};

set[str] LB(Exp e) = {};

alias Store = map[str, Exp];

tuple[Exp, Store] reduce(Exp e) {
    es = <C(e), ()>;
    solve(es) {
      es = step(es);
    }
    return es;
}


tuple[Exp,Store] step(tuple[Exp, Store] es){

  return innermost visit(es){

   //case <C(id(str id)), S> 							=> <C(S[x]), S>
   
  // case <C(apply(e1, e2)), S>						=> <apply(e1, C(e2)), S>
   
  // case <apply(e1, C(v)), S> 						=> <apply(C(e1),v), S> when isValue(v)

  // case <C(apply(lambda(id, e), v)), Store S> 	: { z = fresh(Store); insert <C(subst(e, z, x)), bind(S, z, v)>; } when isValue(V)
   
  // case <C(assign(x, e)), S>  						=> <assign(x, C(e)), S>

  // case <assign(x, C(v)), S>  						=> <C(v), repl(S, x, v)> when isValue(v) && x in domain(S)

   case <C(ifelse(Exp e0, Exp e1, Exp e2)), S> 		=> <ifelse(C(e0), e1, e2), S>
   
   case <ifelse(C(Exp v), Exp e1, Exp e2), S> 		=> <C(e1), S> when (isValue(v) &&  v != nil())

   case <ifelse(C(nil()), e1, e2), S> 				=> <C(e2), S>

   case <C(equ(l1, l2)), S> 						=> <equ(l1, C(l2)), S>
   
   case <equ(l1, C(v)), S> 							=> <equ(C(l1), v), S> when isValue(v)
   
   case <equ(C(l), l), S> 							=> <C(l), S> when isValue(l)

   case <equ(C(l1), l2), S> 						=> <C(nil()), S> when isValue(l1) && isValue(l2) && l1 != l2
  }
}

public Exp L  = label("L");
public Exp L1 = label("L1");
public Exp t2 = equ(L, L);
public Exp t3 = equ(L, L1);
public Exp t4 = ifelse(t2, L, L1);
public Exp t5 = ifelse(t3, L, L1);

test bool tst1() = reduce(t2) == <C(label("L")), ()>;
test bool tst2() = reduce(t3) == <C(nil()), ()>;
test bool tst3() = reduce(t4) == <C(label("L")), ()>;  // two tests fail: ifelse does not reduce properly.
test bool tst4() = reduce(t5) == <C(label("L1")), ()>;
