module demo::lang::Lisra::Runtime

import Prelude;

public data Lval       /*1*/
     = Integer(int n)   
     | Atom(str name)
     | List(list[Lval] elms)
     | Closure(Result(list[Lval] args, Env env))
     ;
         
public alias Scope  = map[Lval,Lval]; /*2*/
public alias Env    = list[Scope];

public Env emptyEnv = [()];

public Env makeEnv(list[Lval] vars, list[Lval] values, Env outer) = /*3*/
   [(vars[i] : values[i] | i <- index(vars))] + outer;

public int find(Lval sym, Env e){ /*4*/
   for(n <- index(e))
       if(e[n][sym]?)
          return n;
   return -1;
}

public Lval TRUE  = Atom("#t");   /*5*/
public Lval FALSE = Atom("#f");

public alias Result = tuple[Lval val, Env env]; /*6*/

     