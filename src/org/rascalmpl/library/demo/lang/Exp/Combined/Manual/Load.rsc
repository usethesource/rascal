module demo::lang::Exp::Combined::Manual::Load
import demo::lang::Exp::Concrete::WithLayout::Syntax;
import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Combined::Manual::Parse;

import ParseTree;

public demo::lang::Exp::Abstract::Syntax::Exp load(str txt) = 
        implode(#demo::lang::Exp::Abstract::Syntax, parse(#demo::lang::Exp::Concrete::WithLayout::Syntax::Exp, txt)); 
        
public Exp p2a((Exp)`<IntegerLiteral l>`) = con(toInt("<l>"));       
public Exp p2a((Exp)`<demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e1> * <demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e2>`) = mul(p2a(e1), p2a(e2));  
public Exp p2a((Exp)`<demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e1> + <demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e2>`) = add(p2a(e1), p2a(e2)); 
public Exp p2a((Exp)`( <demo::lang::Exp::Concrete::WithLayout::Syntax::Exp e> )`) = p2a(e);                    
