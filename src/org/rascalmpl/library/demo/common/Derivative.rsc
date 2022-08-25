// tag::module[]
module demo::common::Derivative

data Exp = con(int n) // <1>
         | var(str name)
         | mul(Exp e1, Exp e2)
         | add(Exp e1, Exp e2)
         ;
         
public Exp E = add(mul(con(3), var("y")), mul(con(5), var("x"))); // <2>

Exp dd(con(n), var(V))              = con(0); // <3>
Exp dd(var(V1), var(V2))            = con((V1 == V2) ? 1 : 0);
Exp dd(add(Exp e1, Exp e2), var(V)) = add(dd(e1, var(V)), dd(e2, var(V)));
Exp dd(mul(Exp e1, Exp e2), var(V)) = add(mul(dd(e1, var(V)), e2), mul(e1, dd(e2, var(V))));
 
Exp simp(add(con(n), con(m))) = con(n + m); // <4>
Exp simp(mul(con(n), con(m))) = con(n * m);

Exp simp(mul(con(1), Exp e))  = e;
Exp simp(mul(Exp e, con(1)))  = e;
Exp simp(mul(con(0), Exp e))  = con(0);
Exp simp(mul(Exp e, con(0)))  = con(0);

Exp simp(add(con(0), Exp e))  = e;
Exp simp(add(Exp e, con(0)))  = e;

default Exp simp(Exp e)       = e; // <5>

Exp simplify(Exp e){ // <6>
  return bottom-up visit(e){
           case Exp e1 => simp(e1)
         }
}
// end::module[]

test bool tstSimplity1() = simplify(mul(var("x"), add(con(3), con(5)))) == mul(var("x"), con(8));
test bool tstSimplity2() = simplify(dd(E, var("x"))) == con(5);
