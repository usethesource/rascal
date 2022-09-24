module demo::lang::Lisra::Parse

import Prelude;
import demo::lang::Lisra::Syntax;
import demo::lang::Lisra::Runtime;

Lval parse(str txt) = build(parse(#LispExp, txt)); // <1>

@synopsis{Build Abstract Syntax Tree: Transform a LispExp to an Lval}
Lval build((LispExp)`<IntegerLiteral il>`) = Integer(toInt("<il>"));      // <2>
Lval build((LispExp)`<AtomExp at>`)        = Atom("<at>");                // <3>
Lval build((LispExp)`( <LispExp* lst> )`)  = List([build(l) | l <- lst]); // <4>

test bool build1() = build((LispExp) `42`) == Integer(42);
test bool build2() = build((LispExp) `abc`) == Atom("abc");
test bool build3() = build((LispExp) `(abc 42)`) == List([Atom("abc"), Integer(42)]);

test bool parse1() = parse("123") == Integer(123);
test bool parse2() = parse("abc") == Atom("abc");
test bool parse3() = parse("()") == List([]);
test bool parse4() = parse("(123)") == List([Integer(123)]);
test bool parse5() = parse("(123 abc)") == List([Integer(123), Atom("abc")]);

