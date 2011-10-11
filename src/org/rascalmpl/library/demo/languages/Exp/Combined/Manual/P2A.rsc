module demo::languages::Exp::Combined::P2A
import demo::languages::Exp::Concrete::WithLayout::Syntax;
import demo::languages::Exp::Abstract::Syntax;
import String;
import ParseTree;

public Exp p2a(str txt) = p2a(parse(#demo::languages::Exp::Concrete::WithLayout::Syntax::Exp, txt)); 
     
public Exp p2a((Exp)`<IntegerLiteral l>`) = con(toInt("<l>"));       
public Exp p2a((Exp)`<Exp e1> * <Exp e2>`) = mul(p2a(e1), p2a(e2));  
public Exp p2a((Exp)`<Exp e1> + <Exp e2>`) = add(p2a(e1), p2a(e2)); 
public Exp p2a((Exp)`( <Exp e> )`) = p2a(e);                    