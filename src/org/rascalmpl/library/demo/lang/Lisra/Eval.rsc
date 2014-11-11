module demo::lang::Lisra::Eval

import Prelude;
import demo::lang::Lisra::Parse;
import demo::lang::Lisra::Runtime;

public Lval eval(Lval x) = eval(x, [()]).val;

// Evaluate an Lval in a given environment and return a Result.

public Result eval(str exp) = eval(parse(exp),  [()]);

public Result eval(Integer(int x), Env e) = <Integer(x), e>; /*1*/

public Result eval(var:Atom(str name), Env e) {              /*2*/
  n = find(var, e);
  return <(n < 0) ? var : e[n][var], e>;
}

public Result eval(List([Atom("quote"), exp*]), Env e) =     /*3*/
  <size(exp) == 1 ? exp[0] : List(exp), e>;

public Result eval(List([Atom("set!"), var, exp]), Env e) {  /*4*/
  val = eval(exp, e).val;
  n = find(var, e);
  if(n < 0) e[0][var] = val; else e[n][var] = val;
  return <val, e>;
}
                                                             /*5*/
public Result eval(List([Atom("if"), Lval tst, Lval conseq, Lval alt]), Env e) = 
       eval(tst, e).val != FALSE ? eval(conseq, e) : eval(alt, e);
       
                                                             /*6*/
public Result eval(List([Atom("begin"), *Lval exps]) , Env e) {
  val = FALSE;
  for(Lval exp <- exps){
      <val, e> = eval(exp, e);
  }
  return <val, e>;
}
                                                             /*7*/
public Result eval(List([Atom("define"), var, exp]), Env e){
   e[0][var] = eval(exp, e).val;
   return <FALSE, e>;
}
                                                             /*8*/
public Result eval(List([Atom("lambda"), List(list[Lval] vars), exp]), Env defEnv) =
  <Closure(Result(list[Lval] args, Env callEnv) { return eval(exp, makeEnv(vars, args, tail(callEnv, size(defEnv))));}),
   defEnv>;

public default Result eval(List([ exps* ]), Env e) {         /*9*/
  if(isEmpty(exps))
     return <FALSE, e>;
  vals = [ eval(exp, e).val | exp <- exps ];
  return apply(head(vals), tail(vals), e);
}

//public default Result eval(Lval exp, Env e) = <exp, e>;

                                                             /*10*/
// Apply an Lval to a list of arguments and return a Result
public Result apply(Closure(Result(list[Lval] args, Env env) fn), list[Lval] args, Env e) {
  return <fn(args, e).val, e>;
}
                                                             /*11*/

public Result apply(Atom("+"),      [Integer(x), Integer(y)],      Env e) = <Integer(x + y), e>;
public Result apply(Atom("-"),      [Integer(x), Integer(y)],      Env e) = <Integer(x - y), e>;
public Result apply(Atom("*"),      [Integer(x), Integer(y)],      Env e) = <Integer(x * y), e>;
public Result apply(Atom("\<"),     [Lval x, Lval y],              Env e) = <x < y ? TRUE : FALSE, e>;
public Result apply(Atom("\>"),     [Lval x, Lval y],              Env e) = <x >= y ? TRUE : FALSE, e>;
public Result apply(Atom("equal?"), [Lval x, Lval y],              Env e) = <x == y ? TRUE : FALSE, e>;
public Result apply(Atom("null?"),  [List(list[Lval] x)],          Env e) = <isEmpty(x) ? TRUE : FALSE, e>;
public Result apply(Atom("cons"),   [Lval x, List(list[Lval] y)],  Env e) = <List([x, *y]), e>;
public Result apply(Atom("append"), [List(list[Lval] x), Lval y],  Env e) = <List([*x, *y]), e>;
public Result apply(Atom("car"),    [List(list[Lval] x)],          Env e) = <head(x), e>;
public Result apply(Atom("cdr"),    [List(list[Lval] x)],          Env e) = <List(tail(x)), e>;
public Result apply(Atom("list"),   list[Lval] x,                  Env e) = <List(x), e>;

default Result apply(Lval a,     list[Lval] b, Env e) {      /*12*/
  println("Cannot apply <a> to <b> using <e>");
  return <FALSE, e>;
}
