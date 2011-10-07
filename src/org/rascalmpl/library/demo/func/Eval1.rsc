module demo::func::Eval1

// using env, allowing let

import demo::func::AST;

import List;
import IO;

alias Env = map[str, int];
alias PEnv = map[str, Func];

public int eval1(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  env = ( f.formals[i] : args[i] | i <- domain(f.formals) ); 
  return eval1(f.body, env, penv);
}


public int eval1(nat(int nat), PEnv penv)  = nat;

public int eval1(mul(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) * eval1(rhs, penv);
    
public int eval1(div(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) / eval1(rhs, penv);
    
public int eval1(add(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) + eval1(rhs, penv);
    
public int eval1(sub(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) - eval1(rhs, penv);
    
public int eval1(gt(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) > eval1(rhs, penv) ? 1 : 0;
    
public int eval1(lt(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) < eval1(rhs, penv) ? 1 : 0;
    
public int eval1(geq(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) >= eval1(rhs, penv) ? 1 : 0;
    
public int eval1(leq(Exp lhs, Exp rhs), PEnv penv) = eval1(lhs, penv) <= eval1(rhs, penv) ? 1 : 0;
  
public int eval1(cond(Exp cond, Exp then, Exp otherwise), PEnv penv) = (eval1(cond, penv) != 0) ? eval1(then, penv) : eval1(otherwise, penv);
                 
public int eval1(call(str name, list[Exp] args), PEnv penv) {
   f = penv[name];
   env =  ( f.formals[i]: eval1(args[i], env, penv) | i <- domain(f.formals) );
   return eval1(f.body, env, penv);
}
         
public int eval1(let(list[Binding] bindings, Exp exp), PEnv penv) {
   env += ( b.var : eval1(b.exp, env, penv) | b <- bindings );  
   return eval1(exp, env, penv);  
}


