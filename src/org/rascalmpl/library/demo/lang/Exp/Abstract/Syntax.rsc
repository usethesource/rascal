// tag::module[]
module demo::lang::Exp::Abstract::Syntax

data Exp = con(int n)          // <1>
         | mul(Exp e1, Exp e2) // <2>
         | add(Exp e1, Exp e2) // <3>
         ;
// end::module[]
