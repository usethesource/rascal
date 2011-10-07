module Eval1

// using env, allowing let

import AST;

import List;
import IO;

alias Env = map[str, int];
alias PEnv = map[str, Func];

public int eval(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  env = ( f.formals[i] : args[i] | i <- domain(f.formals) ); 
  return eval(f.body, env, penv);
}


public int eval(nat(int nat), PEnv penv)  = nat;

public int eval(mul(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) * eval(rhs, penv);
    
public int eval(div(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) / eval(rhs, penv);
    
public int eval(add(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) + eval(rhs, penv);
    
public int eval(min(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) - eval(rhs, penv);
    
public int eval(gt(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) > eval(rhs, penv) ? 1 : 0;
    
public int eval(lt(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) < eval(rhs, penv) ? 1 : 0;
    
public int eval(geq(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) >= eval(rhs, penv) ? 1 : 0;
    
public int eval(leq(Exp lhs, Exp rhs), PEnv penv) = eval(lhs, penv) <= eval(rhs, penv) ? 1 : 0;
  
public int eval(cond(Exp cond, Exp then, Exp otherwise), PEnv penv) = (eval(cond, penv) != 0) ? eval(then, penv) : eval(otherwise, penv);
                 
public int eval(call(str name, list[Exp] args), PEnv penv) {
   f = penv[name];
   env =  ( f.formals[i]: eval(args[i], env, penv) | i <- domain(f.formals) );
   return eval(f.body, env, penv);
}
         
public int eval(let(list[Binding] bindings, Exp exp), PEnv penv) {
   env += ( b.var : eval(b.exp, env, penv) | b <- bindings );  
   return eval(exp, env, penv);  
}


