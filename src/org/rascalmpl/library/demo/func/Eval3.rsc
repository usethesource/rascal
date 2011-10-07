module Eval3

// pointers into the stack

import AST;


import List;
import IO;


alias Address = int;
alias Mem = list[int];
alias Env = map[str, Address];
alias PEnv = map[str, Func];

alias Result = tuple[Mem, int];

public Address push(Mem mem) {
  return size(mem);
}

public tuple[Mem, Address] alloc(Mem mem, int v) {
  mem += [v];
  return <mem, size(mem) - 1>;
}

public Mem pop(Mem mem, Address scope) {
  return slice(mem, 0, scope);
}

public Result eval(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  mem = [];
  <mem, env> = bind(f.formals, args, mem); 
  println("env = <env>");
  println("mem = <mem>");
  return eval(f.body, env, penv, mem);
}

public tuple[Mem, Env] bind(list[str] fs, list[int] args, Mem mem) {
  env = ();
  for (i <- domain(fs)) {
    <mem, a> = alloc(mem, args[i]);
    env[fs[i]] = a;
  }
  return <mem, env>;
}

public Result eval(nat(int nat), Env env, PEnv penv, Mem mem) = <mem, nat>;
 
public Result eval(var(str name), Env env, PEnv penv, Mem mem) = <mem, mem[env[name]]>;
       
       
public Result eval(mul(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, x * y>;
} 
      
public Result eval(div(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, x / y>;
} 
      
public Result eval(add(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, x + y>;
} 
      
public Result eval(min(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, x - y>;
} 
      
public Result eval(gt(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, (x > y) ? 1 : 0>;
} 
  
public Result eval(lt(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, (x < y) ? 1 : 0>;
} 
  
public Result eval(geq(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, (x >= y) ? 1 : 0>;
} 
  
public Result eval(leq(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval(lhs, env, penv, mem);
  <mem, y> = eval(rhs, env, penv, mem);
  return <mem, (x <= y) ? 1 : 0>;
} 
  
public Result eval(cond(Exp cond, Exp then, Exp otherwise), Env env, PEnv penv, Mem mem) {
  <mem, c> = eval(cond, env, penv, mem);
  return (c != 0) ? eval(then, env, penv, mem) : eval(otherwise, env, penv, mem);
}
  
public Result eval(call(str name, list[Exp] args), Env env, PEnv penv, Mem mem) {
   f = penv[name];
   scope = push(mem);
   vs = for (a <- args) {
     <mem, v> = eval(a, env, penv, mem);
     append v;
   }
   <mem, env> = bind(f.formals, vs, mem);
   <mem, v> = eval(f.body, env, penv, mem);
   return <pop(mem, scope), v>; 
}
    
public Result eval(address(str var), Env env, PEnv penv, Mem mem) = <mem, env[var]>;
  
public Result eval(deref(Exp exp), Env env, PEnv penv, Mem mem) {
  <mem, v> = eval(exp, env, penv, mem);
  return <mem, mem[v]>; 
}
     
public Result eval(let(list[Binding] bindings, Exp exp), Env env, PEnv penv, Mem mem) {
   scope = push(mem);
   for (b <- bindings) {
     <mem, v> = eval(b.exp, env, penv, mem);
     <mem, a> = alloc(mem, v);
     env[b.var] = a;
   }
   <mem, v> = eval(exp, env, penv, mem);
   return <pop(mem, scope), v>;
} 

public Result eval(seq(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, _> = eval(lhs, env, penv, mem);
  return eval(rhs, env, penv, mem);
}

public Result eval(assign(var(str name), Exp e), Env env, PEnv penv, Mem mem) {
  <mem, v> = eval(e, env, penv, mem);
  mem[env[name]] = v;
  return <mem, v>;
}

public Result eval(assign(deref(Exp lvalue), Exp e), Env env, PEnv penv, Mem mem) {
  <mem, addr> = eval(lvalue, env, penv, mem);
  <mem, v> = eval(e, env, penv, mem);
  mem[addr] = v;
  return <mem, v>;
}



