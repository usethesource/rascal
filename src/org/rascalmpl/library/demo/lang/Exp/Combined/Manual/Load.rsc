// tag::module[]
module demo::lang::Exp::Combined::Manual::Load

import demo::lang::Exp::Concrete::WithLayout::Syntax; // <1>
import demo::lang::Exp::Abstract::Syntax; // <2>
import demo::lang::Exp::Combined::Manual::Parse; // <3>
import String;

demo::lang::Exp::Abstract::Syntax::Exp loadExp(str txt) = load(parseExp(txt)); // <4>
     
demo::lang::Exp::Abstract::Syntax::Exp load((Exp)`<IntegerLiteral l>`) // <5>
       = con(toInt("<l>"));       
demo::lang::Exp::Abstract::Syntax::Exp load((Exp)`<Exp e1> * <Exp e2>`) 
       = mul(load(e1), load(e2));  
demo::lang::Exp::Abstract::Syntax::Exp load((Exp)`<Exp e1> + <Exp e2>`)
       = add(load(e1), load(e2)); 
demo::lang::Exp::Abstract::Syntax::Exp load((Exp)`( <Exp e> )`) 
       = load(e);                    
// end::module[]
