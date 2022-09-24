module demo::lang::Func::Eval0

// No let

import demo::lang::Func::AST;
import List;

alias PEnv = map[str, Func]; // <1>

value eval0(str main, list[int] args, Prog prog) { // <2>
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  return eval0(subst(f.body, f.formals, args), penv);
}


Exp subst(Exp exp, list[str] vars, list[int] values) { // <3>
  env = ( vars[i]: values[i] | i <- index(vars) );
  return visit (exp) {
    case var(str name) => nat(env[name])
  };
}

int eval0(nat(int nat), PEnv penv)  = nat; // <4>

int eval0(mul(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) * eval0(rhs, penv);
    
int eval0(div(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) / eval0(rhs, penv);
    
int eval0(add(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) + eval0(rhs, penv);
    
int eval0(sub(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) - eval0(rhs, penv);
    
int eval0(gt(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) > eval0(rhs, penv) ? 1 : 0;
    
int eval0(lt(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) < eval0(rhs, penv) ? 1 : 0;
    
int eval0(geq(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) >= eval0(rhs, penv) ? 1 : 0;
    
int eval0(leq(Exp lhs, Exp rhs), PEnv penv) = eval0(lhs, penv) <= eval0(rhs, penv) ? 1 : 0;
  
int eval0(cond(Exp cond, Exp then, Exp otherwise), PEnv penv) =  // <5>
    (eval0(cond, penv) != 0) ? eval0(then, penv) : eval0(otherwise, penv);
               
int eval0(call(str name, list[Exp] args), PEnv penv) =  // <6>
    eval0(subst(penv[name].body, penv[name].formals, [ eval0(a, penv) | a <- args]), penv);


