@contributor{Jurgen J. Vinju}
module demo::Mod17

// A fully symbolic specification of peano arithmatic modulo 17, not using builtin data-types
// Rewritten from the TOM version!

import IO;
import util::Benchmark;

data Bool 
  = TRUE() 
  | FALSE()
  ;

data Nat 
  = ZERO()
  | S(Nat s1)
  ;
  
data SNat
  = EXZERO() 
  | EXS(SNat s1)
  | EXPLUS(SNat s1, SNat s2) 
  | EXMULT(SNat s1, SNat s2) 
  | EXEXP(SNat s1, SNat s2) 
  | EXONE()
  ;
  
data TREE 
  = LEAF(Nat n1) 
  | NODE(Nat n1,Nat n2, TREE t3, TREE t4) 
  ;
  
Bool equal(Nat t1, t1) = TRUE() ;
default Bool equal(Nat _, Nat _) = FALSE();

SNat int2SNat(int n) = (EXZERO() | EXS(it) | _ <- [0..n]);

void run_evalsym17(int max) {
  SNat n = EXEXP(int2SNat(2),int2SNat(max));
  Nat t1 = EVAL17(n);
  Nat t2 = EVALSYM17(n);
  Bool res = equal(t1,t2);
  println("result evalsym17 <res>");
}

void run_evalexp17(int max) {
  SNat n = EXEXP(int2SNat(2),int2SNat(max));
  Nat t1 = EVAL17(n);
  Nat t2 = EVALEXP17(n);
  Bool res = equal(t1,t2);
  println("result evalexp17 <res>");
}

void run_evaltree17(int max) {
  Nat n = EVAL(int2SNat(max));
  Nat t1 = calctree17(n);
  Nat t2 = GETVAL(BUILDTREE(n,ZERO()));
  Bool res = equal(t1,t2);
  println("result evaltree17 <res>");
}

int main(int max) {
  println("evalsym17(<max>): <cpuTimeOf( () { run_evalsym17(max); }) / (1000 * 1000)> msec");
  println("evalexp17(<max>): <cpuTimeOf( () { run_evalexp17(max); })  / (1000 * 1000)> msec");
  println("evaltree17(<max>): <cpuTimeOf( () { run_evaltree17(max); })  / (1000 * 1000)> msec");
  return 0;
}
  
TREE BUILDTREE(ZERO(), Nat val) = LEAF(val);
TREE BUILDTREE(S(Nat x), Nat y) {
  TREE Left = BUILDTREE(x, y);  
  Nat Max2 = GETMAX(Left);
  TREE Right= BUILDTREE(x, SUCC17(Max2));
  Nat Val2 = GETVAL(Left);
  Nat Val3 = GETVAL(Right);
  Nat Val  = PLUS17(Val2, Val3);
  Nat Max  = GETMAX(Right);
  return NODE(Val, Max, Left, Right);
}

Nat PLUS(Nat x, ZERO()) = x;
Nat PLUS(Nat x, S(Nat y)) = S(PLUS(x,y));

Nat MULT(Nat x, ZERO()) = ZERO();
Nat MULT(Nat x, S(Nat y)) = PLUS(MULT(x,y),x);

Nat EXP(Nat _, ZERO()) = S(ZERO());
Nat Exp(Nat x, S(Nat y)) = MULT(x, EXP(x,y));
  
Nat SUCC17(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(ZERO()))))))))))))))))) = ZERO();
default Nat SUCC17(Nat x) = S(x);
  
Nat PRED17(S(Nat x)) = x ;
Nat PRED17(ZERO()) = S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(ZERO())))))))))))))))); 
  
Nat PLUS17(Nat x,ZERO()) = x; 
Nat PLUS17(Nat x,S(Nat y)) = SUCC17(PLUS17(x,y)) ;
  
Nat MULT17(Nat x,ZERO()) = ZERO();
Nat MULT17(Nat x,S(Nat y)) = PLUS17(x,MULT17(x,y)); 
  
Nat EXP17(Nat x,ZERO()) = SUCC17(ZERO()); 
Nat EXP17(Nat x,S(Nat y)) = MULT17(x,EXP17(x,y)); 
  
Nat EVAL(EXZERO()) = ZERO();
Nat EVAL(EXS(SNat Xs)) = S(EVAL(Xs)); 
Nat EVAL(EXPLUS(SNat Xs,SNat Ys)) = PLUS(EVAL(Xs), EVAL(Ys)); 
Nat EVAL(EXMULT(SNat Xs,SNat Ys)) = MULT(EVAL(Xs), EVAL(Ys)); 
Nat EVAL(EXEXP(SNat Xs,SNat Ys)) = EXP(EVAL(Xs), EVAL(Ys));
  
Nat EVALSYM17(EXZERO()) = ZERO();
Nat EVALSYM17(EXS(SNat Xs)) = SUCC17(EVALSYM17(Xs)); 
Nat EVALSYM17(EXPLUS(SNat Xs,SNat Ys)) = PLUS17(EVALSYM17(Xs),EVALSYM17(Ys)) ;
Nat EVALSYM17(EXMULT(SNat Xs,EXZERO())) = ZERO();
Nat EVALSYM17(EXMULT(SNat Xs,EXS(SNat Ys))) = EVALSYM17(EXPLUS(EXMULT(Xs,Ys),Xs)) ;
Nat EVALSYM17(EXMULT(SNat Xs,EXPLUS(SNat Ys,SNat Zs))) = EVALSYM17(EXPLUS(EXMULT(Xs,Ys),EXMULT(Xs,Zs))) ;
Nat EVALSYM17(EXMULT(SNat Xs,EXMULT(SNat Ys,SNat Zs))) = EVALSYM17(EXMULT(EXMULT(Xs,Ys),Zs)) ;
Nat EVALSYM17(EXMULT(SNat Xs,EXEXP(SNat Ys,SNat Zs))) = EVALSYM17(EXMULT(Xs,DEC(EXEXP(Ys,Zs)))) ;
Nat EVALSYM17(EXEXP(SNat Xs,EXZERO())) = SUCC17(ZERO()) ;
Nat EVALSYM17(EXEXP(SNat Xs,EXS(SNat Ys))) = EVALSYM17(EXMULT(EXEXP(Xs,Ys),Xs)) ;
Nat EVALSYM17(EXEXP(SNat Xs,EXPLUS(SNat Ys,SNat Zs))) = EVALSYM17(EXMULT(EXEXP(Xs,Ys),EXEXP(Xs,Zs))) ;
Nat EVALSYM17(EXEXP(SNat Xs,EXMULT(SNat Ys,SNat Zs))) = EVALSYM17(EXEXP(EXEXP(Xs,Ys),Zs)) ;
Nat EVALSYM17(EXEXP(SNat Xs,EXEXP(SNat Ys,SNat Zs))) = EVALSYM17(EXEXP(Xs,DEC(EXEXP(Ys,Zs))))  ;

Nat EVALEXP17(SNat Xs) = EVAL17(EXPAND(Xs));
  
SNat DEC(EXEXP(SNat Xs,EXZERO())) = EXS(EXZERO());
SNat DEC(EXEXP(SNat Xs,EXS(SNat Ys))) = EXMULT(EXEXP(Xs,Ys),Xs);
SNat DEC(EXEXP(SNat Xs,EXPLUS(SNat Ys,SNat Zs))) = EXMULT(EXEXP(Xs,Ys),EXEXP(Xs,Zs));
SNat DEC(EXEXP(SNat Xs,EXMULT(SNat Ys,SNat Zs))) = DEC(EXEXP(EXEXP(Xs,Ys),Zs));
SNat DEC(EXEXP(SNat Xs,EXEXP(SNat Ys,SNat Zs))) = DEC(EXEXP(Xs, DEC(EXEXP(Ys,Zs)))); 

Nat EVAL17(EXONE()) = S(ZERO()); 
Nat EVAL17(EXZERO()) = ZERO();
Nat EVAL17(EXS(SNat Xs)) = SUCC17(EVAL17(Xs));
Nat EVAL17(EXPLUS(SNat Xs,SNat Ys)) = PLUS17(EVAL17(Xs), EVAL17(Ys));
Nat EVAL17(EXMULT(SNat Xs,SNat Ys)) = MULT17(EVAL17(Xs), EVAL17(Ys));
Nat EVAL17(EXEXP(SNat Xs,SNat Ys)) = EXP17(EVAL17(Xs), EVAL(Ys));

SNat EXPAND(EXZERO()) = EXZERO() ;
SNat EXPAND(EXONE()) = EXONE() ;
SNat EXPAND(EXS(SNat Xs)) = EXPLUS(EXONE(),EXPAND(Xs)) ;
SNat EXPAND(EXPLUS(SNat Xs,SNat Ys)) = EXPLUS(EXPAND(Xs),EXPAND(Ys)) ;
SNat EXPAND(EXMULT(SNat Xs,EXZERO())) = EXZERO() ;
SNat EXPAND(EXMULT(SNat Xs,EXONE())) = EXPAND(Xs) ;
SNat EXPAND(EXMULT(SNat Xs,EXPLUS(SNat Ys,SNat Zs))) = EXPAND(EXPLUS(EXMULT(Xs,Ys),EXMULT(Xs,Zs))) ;
SNat EXPAND(EXMULT(SNat Xs,SNat Ys)) = EXPAND(EXMULT(Xs,EXPAND(Ys))) ;
SNat EXPAND(EXEXP(SNat Xs,EXZERO())) = EXONE() ;
SNat EXPAND(EXEXP(SNat Xs,EXONE())) = EXPAND(Xs) ;
SNat EXPAND(EXEXP(SNat Xs,EXPLUS(SNat Ys,SNat Zs))) = EXPAND(EXMULT(EXEXP(Xs,Ys),EXEXP(Xs,Zs))) ;
SNat EXPAND(EXEXP(SNat Xs,SNat Ys)) = EXPAND(EXEXP(Xs, EXPAND(Ys))) ;

Nat GETVAL(LEAF(Nat val)) = val;
Nat GETVAL(NODE(Nat val,Nat _,TREE _, TREE _)) = val;

Nat GETMAX(LEAF(val)) = val;
Nat GETMAX(NODE(Nat _, Nat max, TREE _, TREE _)) = max;

Nat calctree17(Nat X) = MULT17(EXP17(S(S(ZERO())), PRED17(X)),PRED17(EXP17(S(S(ZERO())),X)));
