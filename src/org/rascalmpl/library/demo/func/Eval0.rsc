module Eval0

// No let

import AST;
import List;

alias PEnv = map[str, Func];

public value eval(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  return eval(subst(f.body, f.formals, args), penv);
}


public Exp subst(Exp exp, list[str] vars, list[int] values) {
  env = ( vars[i]: values[i] | i <- domain(vars) );
  return visit (exp) {
    case var(str name) => nat(env[name])
  };
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
          
       
public int eval(call(str name, list[Exp] args), PEnv penv) = 
    eval(subst(penv[name].body, penv[name].formals, [ eval(a, penv) | a <- args]), penv);


