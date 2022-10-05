module demo::lang::Lisra::Eval

import Prelude;
import demo::lang::Lisra::Parse;
import demo::lang::Lisra::Runtime;

@synopsis{Parse and Evaluate an expression in an empty environment}
Result eval(str exp) = eval(parse(exp),  [()]);

@synopsis{Evaluate an Lval in an empty environment}
Lval eval(Lval x) = eval(x, [()]).val;

@synopsis{Evaluate an Lval in a given environment and return a Result}
Result eval(Integer(int x), Env e) = <Integer(x), e>; // <1>

Result eval(var:Atom(str name), Env e) { // <2>
  n = find(var, e);
  return <(n < 0) ? var : e[n][var], e>;
}

Result eval(List([Atom("quote"), *Lval exps]), Env e) = // <3>
  <size(exps) == 1 ? exps[0] : List(exps), e>;

Result eval(List([Atom("set!"), var, exp]), Env e) { // <4>
  val = eval(exp, e).val;
  n = find(var, e);
  if(n < 0) e[0][var] = val; else e[n][var] = val;
  return <val, e>;
}
                                                            
Result eval(List([Atom("if"), Lval tst, Lval conseq, Lval alt]), Env e) = // <5>
  eval(tst, e).val != FALSE ? eval(conseq, e) : eval(alt, e);
       
                                                           
Result eval(List([Atom("begin"), *Lval exps]) , Env e) { // <6>
  val = FALSE;
  for(Lval exp <- exps){
      <val, e> = eval(exp, e);
  }
  return <val, e>;
}
                                                           
Result eval(List([Atom("define"), var, exp]), Env e){ // <7>
   e[0][var] = eval(exp, e).val;
   return <FALSE, e>;
}
                                                            
Result eval(List([Atom("lambda"), List(list[Lval] vars), exp]), Env defEnv) = // <8>
  <Closure(Result(list[Lval] args, Env callEnv) { 
                 return eval(exp, makeEnv(vars, args, tail(callEnv, size(defEnv))));
           }),
   defEnv>;

default Result eval(List([ *Lval exps ]), Env e) { // <9>
  if(isEmpty(exps))
     return <List([]), e>;
  vals = [ eval(exp, e).val | exp <- exps ];
  return apply(head(vals), tail(vals), e);
}

//default Result eval(Lval exp, Env e) = <exp, e>;

                                                            
// Apply an Lval to a list of arguments and return a Result
Result apply(Closure(Result(list[Lval] args, Env env) fn), list[Lval] args, Env e) { // <10>
  return <fn(args, e).val, e>;
}

// <11>

Result apply(Atom("+"),      [Integer(x), Integer(y)],      Env e) = <Integer(x + y), e>;
Result apply(Atom("-"),      [Integer(x), Integer(y)],      Env e) = <Integer(x - y), e>;
Result apply(Atom("*"),      [Integer(x), Integer(y)],      Env e) = <Integer(x * y), e>;
Result apply(Atom("\<"),     [Lval x, Lval y],              Env e) = <x < y ? TRUE : FALSE, e>;
Result apply(Atom("\>"),     [Lval x, Lval y],              Env e) = <x >= y ? TRUE : FALSE, e>;
Result apply(Atom("equal?"), [Lval x, Lval y],              Env e) = <x == y ? TRUE : FALSE, e>;
Result apply(Atom("null?"),  [List(list[Lval] x)],          Env e) = <isEmpty(x) ? TRUE : FALSE, e>;
Result apply(Atom("cons"),   [Lval x, List(list[Lval] y)],  Env e) = <List([x, *y]), e>;
Result apply(Atom("append"), [List(list[Lval] x), Lval y],  Env e) = <List([*x, y]), e>;
Result apply(Atom("car"),    [List(list[Lval] x)],          Env e) = <head(x), e>;
Result apply(Atom("cdr"),    [List(list[Lval] x)],          Env e) = <List(tail(x)), e>;
Result apply(Atom("list"),   list[Lval] x,                  Env e) = <List(x), e>;

default Result apply(Lval a,     list[Lval] b, Env e) { // <12>
  println("Cannot apply <a> to <b> using <e>");
  return <FALSE, e>;
}
