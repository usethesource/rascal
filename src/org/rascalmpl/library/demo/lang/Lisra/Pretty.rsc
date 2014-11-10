module demo::lang::Lisra::Pretty

import demo::lang::Lisra::Runtime;

// Pretty print: transform an Lval to a string
public str pretty(Integer(n))  = "<n>";
public str pretty(Atom(name))  = name;
public str pretty(List(list[Lval] elms)) = "( <for(Lval e <- elms){><pretty(e)> <}>)";
public str pretty(Closure(Result(list[Lval] args, Env env))) = "Closure(<args>)";