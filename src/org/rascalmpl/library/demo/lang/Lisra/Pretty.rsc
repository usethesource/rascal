module demo::lang::Lisra::Pretty

import demo::lang::Lisra::Runtime;

// Pretty print: transform an Lval to a string
public str pretty(Integer(n))  = "<n>";
public str pretty(Atom(name))  = name;
public str pretty(List(list[Lval] elms)) = "( <for(Lval e <- elms){><pretty(e)> <}>)";
public str pretty(Closure(Result(list[Lval] args, Env env))) = "Closure(<args>)";

test bool pretty1() = pretty(Integer(123)) == "123";
test bool pretty2() = pretty(Atom("abc")) == "abc";
test bool pretty3() = pretty(List([])) == "( )";
test bool pretty4() = pretty(List([Integer(123)])) == "( 123 )";
test bool pretty5() = pretty(List([Integer(123), Atom("abc")])) == "( 123 abc )";