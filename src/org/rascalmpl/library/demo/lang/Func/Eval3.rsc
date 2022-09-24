module demo::lang::Func::Eval3

// pointers into the stack

import demo::lang::Func::AST;

import List;

alias Env = map[str, Address];
alias PEnv = map[str, Func];

alias Result3 = tuple[Mem, int];

alias Address = int;
alias Mem = list[int];

Address push(Mem mem) {
  return size(mem);
}

tuple[Mem, Address] alloc(Mem mem, int v) {
  mem += [v];
  return <mem, size(mem) - 1>;
}

Mem pop(Mem mem, Address scope) {
  return slice(mem, 0, scope);
}

Result3 eval3(str main, list[int] args, Prog prog) {
  penv = ( f.name: f | f <- prog.funcs );
  f = penv[main];
  mem = [];
  <mem, env> = bind(f.formals, args, mem); 
  return eval3(f.body, env, penv, mem);
}

tuple[Mem, Env] bind(list[str] fs, list[int] args, Mem mem) {
  env = ();
  for (i <- index(fs)) {
    <mem, a> = alloc(mem, args[i]);
    env[fs[i]] = a;
  }
  return <mem, env>;
}

Result3 eval3(nat(int nat), Env env, PEnv penv, Mem mem) = <mem, nat>;
 
Result3 eval3(var(str name), Env env, PEnv penv, Mem mem) = <mem, mem[env[name]]>;
       
       
Result3 eval3(mul(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, x * y>;
} 
      
Result3 eval3(div(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, x / y>;
} 
      
Result3 eval3(add(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, x + y>;
} 
      
Result3 eval3(sub(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, x - y>;
} 
      
Result3 eval3(gt(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, (x > y) ? 1 : 0>;
} 
  
Result3 eval3(lt(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, (x < y) ? 1 : 0>;
} 
  
Result3 eval3(geq(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, (x >= y) ? 1 : 0>;
} 
  
Result3 eval3(leq(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, x> = eval3(lhs, env, penv, mem);
  <mem, y> = eval3(rhs, env, penv, mem);
  return <mem, (x <= y) ? 1 : 0>;
} 
  
Result3 eval3(cond(Exp cond, Exp then, Exp otherwise), Env env, PEnv penv, Mem mem) {
  <mem, c> = eval3(cond, env, penv, mem);
  return (c != 0) ? eval3(then, env, penv, mem) : eval3(otherwise, env, penv, mem);
}
  
Result3 eval3(call(str name, list[Exp] args), Env env, PEnv penv, Mem mem) {
   f = penv[name];
   scope = push(mem);
   vs = for (a <- args) {
     <mem, v> = eval3(a, env, penv, mem);
     append v;
   }
   <mem, env> = bind(f.formals, vs, mem);
   <mem, v> = eval3(f.body, env, penv, mem);
   return <pop(mem, scope), v>; 
}
    
Result3 eval3(address(str var), Env env, PEnv penv, Mem mem) = <mem, env[var]>;
  
Result3 eval3(deref(Exp exp), Env env, PEnv penv, Mem mem) {
  <mem, v> = eval3(exp, env, penv, mem);
  return <mem, mem[v]>; 
}
     
Result3 eval3(let(list[Binding] bindings, Exp exp), Env env, PEnv penv, Mem mem) {
   scope = push(mem);
   for (b <- bindings) {
     <mem, v> = eval3(b.exp, env, penv, mem);
     <mem, a> = alloc(mem, v);
     env[b.var] = a;
   }
   <mem, v> = eval3(exp, env, penv, mem);
   return <pop(mem, scope), v>;
} 

Result3 eval3(seq(Exp lhs, Exp rhs), Env env, PEnv penv, Mem mem) {
  <mem, _> = eval3(lhs, env, penv, mem);
  return eval3(rhs, env, penv, mem);
}

Result3 eval3(assign(var(str name), Exp e), Env env, PEnv penv, Mem mem) {
  <mem, v> = eval3(e, env, penv, mem);
  mem[env[name]] = v;
  return <mem, v>;
}

Result3 eval3(assign(deref(Exp lvalue), Exp e), Env env, PEnv penv, Mem mem) {
  <mem, addr> = eval3(lvalue, env, penv, mem);
  <mem, v> = eval3(e, env, penv, mem);
  mem[addr] = v;
  return <mem, v>;
}


