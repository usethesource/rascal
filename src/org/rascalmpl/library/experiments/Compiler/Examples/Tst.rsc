module experiments::Compiler::Examples::Tst

import ParseTree;
import demo::lang::Exp::Concrete::WithLayout::Syntax;
import demo::lang::Exp::Abstract::Syntax; 

demo::lang::Exp::Concrete::WithLayout::Syntax::Exp v = (Exp) `1`;

//import demo::lang::Exp::Concrete::WithLayout::Syntax;  /*1*/
//import demo::lang::Exp::Abstract::Syntax;              /*2*/
//import demo::lang::Exp::Combined::Manual::Parse;       /*3*/
//import String;
//
//public Exp load(str txt) = load(parse(txt));           /*4*/
//
//public Exp load((Exp)`<IntegerLiteral l>`)             /*5*/
//       = con(toInt("<l>"));       
//public Exp load((Exp)`<Exp e1> * <Exp e2>`) 
//       = mul(load(e1), load(e2));  
//public Exp load((Exp)`<Exp e1> + <demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e2>`)
//       = add(load(e1), load(e2)); 
//public Exp load((Exp)`( <demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e> )`) 
//       = load(e);