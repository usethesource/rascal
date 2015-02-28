
module experiments::Compiler::Examples::Tst2


//import Prelude;
import ParseTree;
import demo::lang::Lisra::Syntax;
import demo::lang::Lisra::Runtime;
import demo::lang::Lisra::Parse;

//public Lval parse(str txt) = build(parse(#LispExp, txt));                          /*1*/

// Build Abstract Synax Tree: Transform a LispExp to an Lval

//public Lval build((LispExp)`<IntegerLiteral il>`) = Integer(toInt("<il>"));        /*2*/
//public Lval build((LispExp)`<AtomExp at>`)        = Atom("<at>");                  /*3*/
//public Lval build((LispExp)`( <LispExp* lst> )`)  = List([build1(le) | le <- lst]); /*4*/


public test bool b01() = build((LispExp) `42`) == Integer(42);
public test bool b02() = build((LispExp) `abc`) == Atom("abc");
public test bool b03() = build((LispExp) `(abc 42)`) == List([Atom("abc"), Integer(42)]);

//test bool parse1() = parse("123") == Integer(123);
//test bool parse2() = parse("abc") == Atom("abc");
//test bool parse3() = parse("()") == List([]);
//test bool parse4() = parse("(123)") == List([Integer(123)]);
//test bool parse5() = parse("(123 abc)") == List([Integer(123), Atom("abc")]);

value main(list[value] args) = build((LispExp) `(abc 42)`);
