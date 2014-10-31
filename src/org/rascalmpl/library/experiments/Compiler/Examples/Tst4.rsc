module experiments::Compiler::Examples::Tst4

import Prelude;
import demo::lang::Lisra::Syntax;
import demo::lang::Lisra::Runtime;

//public Lval parse(str txt) = build(parse(#LispExp, txt));                          /*1*/

// Build Abstract Synax Tree: Transform a LispExp to an Lval

//public Lval build((LispExp)`<IntegerLiteral il>`) = Integer(toInt("<il>"));        /*2*/
public Lval build((LispExp) `<AtomExp at>`)        = Atom("<at>");                  /*3*/
//public Lval build((LispExp)`( <LispExp* lst> )`)  = List([build(le) | le <- lst]); /*4*/

