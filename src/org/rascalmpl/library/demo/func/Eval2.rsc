module Eval2

// local side effects, returning env

import AST;

import List;
import IO;

alias Env = map[str, int];
alias PEnv = map[str, Func];

alias Result = tuple[Env, int];

public Result eval(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  env = ( f.formals[i] : args[i] | i <- domain(f.formals) ); 
  return eval(f.body, env, penv);
}

public Result eval(nat(int nat), Env env, PEnv penv) = <env, nat>;
 
public Result eval(var(str name), Env env, PEnv penv) = <env, env[name]>;       
       
public Result eval(mul(Exp lhs, Exp rhs), Env env, PEnv penv) {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, x * y>;
} 
      
public Result eval(div(Exp lhs, Exp rhs), Env env, PEnv penv) {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, x / y>;
} 
      
public Result eval(add(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, x + y>;
} 
      
public Result eval(min(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, x - y>;
} 
      
public Result eval(gt(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, (x > y) ? 1 : 0>;
} 
      
public Result eval(lt(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, (x < y) ? 1 : 0>;
} 
      
public Result eval(geq(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, (x >= y) ? 1 : 0>;
} 
      
public Result eval(leq(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, x> = eval(lhs, env, penv);
  <env, y> = eval(rhs, env, penv);
  return <env, (x <= y) ? 1 : 0>;
} 
  
public Result eval(cond(Exp cond, Exp then, Exp otherwise), Env env, PEnv penv)  {
  <env, c> = eval(cond, env, penv);
  return (c != 0) ? eval(then, env, penv) : eval(otherwise, env, penv);
}
      
public Result eval(call(str name, list[Exp] args), Env env, PEnv penv)  {
   f = penv[name];
   for (i <- domain(f.formals)) {
     <env, v> = eval(args[i], env, penv);
     env[f.formals[i]] = v;
   }
   return eval(f.body, env, penv);
}
         
public Result eval(let(list[Binding] bindings, Exp exp), Env env, PEnv penv)  {
   for (b <- bindings) {
     <env, x> = eval(b.exp, env, penv);
     env[b.var] = x;
   }
   return eval(exp, env, penv);
} 
    
public Result eval(seq(Exp lhs, Exp rhs), Env env, PEnv penv)  {
  <env, _> = eval(lhs, env, penv);
  return eval(rhs, env, penv);
}
    
public Result eval(assign(var(str name), Exp exp), Env env, PEnv penv)  {
  <env, v> = eval(exp, env, penv);
  env[name] = v;
  return <env, v>;
}



