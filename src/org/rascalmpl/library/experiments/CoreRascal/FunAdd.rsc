module experiments::CoreRascal::FunAdd

data Term =     // Terms + addition
            id(str name)
          | number(int n)
          | lambda(str id, Term t)
          | apply(Term t1, Term t2)
          | add(Term t1, Term t2)
          ; 
 
bool isValue(number(n)) = true;
bool isValue( lambda(str id, Term t)) = true;
default bool isValue(Term t) = false;

Term subst(id(x), str y, Term v) = x == y ? v : id(x);
Term subst(number(n), str y, Term v) = number(n);
Term subst(lambda(str x, Term t), str y, Term v) = (x == y) ? lambda(x, t) :  lambda(x, subst(t, y, v));
Term subst(add(Term t1, Term t2), str y, Term v) = add(subst(t1, y, v), subst(t2, y, v));  

/*
data K = kempty() | kapply(K k, Term t) | kapply(Value v, K k) | kadd(K k, Term t) | kadd(Value v, K k);

Term plug(t, kempty()) = t;
Term plug(t, kapply(K k, Term t1)) = apply(plug(t, k), t1);
Term plug(t, kapply(Value v, K k)) = apply(v, plug(t, k));
Term plug(t, kadd(K k, Term t2)) = add(plug(t, k), t2);
Term plug(t, kadd(Value v, K k)) = add(v, plug(t1, k));

Term reduce(plug(apply(lambda(AId id, Term t), Value v), K k)) = plug(subst(t, id, v), k);

Term reduce(plug(add(number(n1), number(n2)), K k)) = plug(number(n1 + n2), k);

default Term reduce(Term t) = t;
*/

public Term t1 = add(number(1), number(2));
public Term t2 = add(add(number(1), number(2)),number(3));
public Term t3 = apply(lambda("x", add(number(1), id("x"))), number(2));

Term reducecbv(Term t){ // call-by-value

  return innermost visit(t){
     case apply(lambda(str id, Term t), Term v) => subst(t, id, v) when isValue(v)
     case add(number(n1), number(n2))           => number(n1 + n2)
  }
}

Term reducecbn(Term t){  // call-by-name

  return innermost visit(t){
     case apply(lambda(str id, Term t), Term v) => subst(t, id, v)
     case add(number(n1), number(n2))           => number(n1 + n2)
  }
}

test bool tst1a() = reducecbv(t1) == number(3);
test bool tst1b() = reducecbn(t1) == number(3);

test bool tst2a() = reducecbv(t2) == number(6);
test bool tst2b() = reducecbn(t2) == number(6);

test bool tst3a() = reducecbv(t3) == number(3);
test bool tst3b() = reducecbn(t3) == number(3);



