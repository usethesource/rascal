// tag::module[]
module demo::lang::Lisra::Pretty

import demo::lang::Lisra::Runtime;

// Pretty print: transform an Lval to a string
str pretty(Integer(n))  = "<n>";
str pretty(Atom(name))  = name;
str pretty(List(list[Lval] elms)) = "( <for(Lval e <- elms){><pretty(e)> <}>)";
str pretty(Closure(fn)) = "Closure(<fn>)";

// end::module[]
test bool pretty1() = pretty(Integer(123)) == "123";
test bool pretty2() = pretty(Atom("abc")) == "abc";
test bool pretty3() = pretty(List([])) == "( )";
test bool pretty4() = pretty(List([Integer(123)])) == "( 123 )";
test bool pretty5() = pretty(List([Integer(123), Atom("abc")])) == "( 123 abc )";
