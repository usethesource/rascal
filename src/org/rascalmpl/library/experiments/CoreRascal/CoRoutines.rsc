module experiments::CoreRascal::CoRoutines

// Co-routines

data Exp = 
            label(str name) 
          | nil() 
          | lambda(str id, Exp exp)
          | id(str name)
          | apply(Exp exp1, Exp exp2)
          | assign(str id, Exp exp)
          | ifelse(Exp exp1, Exp exp2, Exp exp3)
          | eq(Exp exp1, Exp exp2)
          | C(Exp exp)
          ;

bool isValue(nil()) = true;
bool isValue(label(str name)) = true;
bool isValue(lambda(str id, Exp exp)) = true;

set[str] FV(Exp e) = {};

set[str] LB(Exp e) = {};

alias Store = map[str, Exp];

public Exp t1 = label("L");
public Exp t2 = eq(label("L"), label("L"));


Exp reduce(Exp e){

  return innermost visit(<C(e), ()>){

   case <C(id(str id)), S> => <C(S[x]), S>
   
   case <C(apply(exp1, exp2)), S> => <apply(exp1, C(exp2)), S>
   
   case <apply(exp1, C(v)), S> => <apply(C(exp1),v), S> when isValue(v)

   case <C(apply(lambda(id, exp), v)), Store S> => { z = fresh(Store); insert <C(subst(e, z, x)), bind(S, z, v)>; } when isValue(V)
   
   case <C(assign(x, e)), S>  => <assign(x, C(e)), S>

   case <assign(x, C(v)), S>  => <C(v), repl(S, x, v)> when isValue(v) && x in domain(S)

   case <C(ifelse(e0, e1,e2)), S> => <ifelse(C(e0), e1,e2), S>
   
   case <ifelse(C(v), e1,e2), S> => <C(e1), S> when isValue(v) && v != nil()

   case <ifelse(C(nil()), e1,e2), S> => <C(e2), S>

   case <C(eq(l1, l2)), S> => <eq(l1, C(l2)), S>
   
   case <C(eq(l1, C(v))), S> => <eq(C(l1), v), S> when isValue(v)
   
   case <eq(C(l), l), S> => <l, S> when isValue(l)

   case <eq(C(l1), l2), S> => <nil(), S> when isValue(l1) && isValue(l2) && l1 != l2
  }
}
