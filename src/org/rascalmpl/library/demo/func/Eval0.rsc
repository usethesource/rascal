module demo::func::Eval0

// No let

import demo::func::AST;
import List;

alias PEnv = map[str, Func];

public value eval0(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  return eval0(subst(f.body, f.formals, args), penv);
}


public Exp subst(Exp exp, list[str] vars, list[int] values) {
  env = ( vars[i]: values[i] | i <- domain(vars) );
  return visit (exp) {
    case var(str name) => nat(env[name])
  };
}

public int eval0(nat(int nat), PEnv penv)  = nat;

public int eval0(mul(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) * eval0(rhs, penv);
    
public int eval0(div(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) / eval0(rhs, penv);
    
public int eval0(add(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) + eval0(rhs, penv);
    
public int eval0(sub(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) - eval0(rhs, penv);
    
public int eval0(gt(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) > eval0(rhs, penv) ? 1 : 0;
    
public int eval0(lt(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) < eval0(rhs, penv) ? 1 : 0;
    
public int eval0(geq(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) >= eval0(rhs, penv) ? 1 : 0;
    
public int eval0(leq(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) <= eval0(rhs, penv) ? 1 : 0;
  
public int eval0(cond(Exp cond, Exp then, Exp otherwise), PEnv penv) = (eval0(cond, penv) != 0) ? eval0(then, penv) : eval0(otherwise, penv);
          
       
public int eval0(call(str name, list[Exp] args), PEnv penv) = 
    eval0(subst(penv[name].body, penv[name].formals, [ eval0(a, penv) | a <- args]), penv);


