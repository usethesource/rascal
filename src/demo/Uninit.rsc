module demo::Uninit
import Relation;
import Graph;

alias expr = int;

alias varname = str;

public expr ROOT = 1;

public graph[expr] PRED = { <1,3>, <3,4>, <4,5>, <5,6>, <5,8>, 
                        <6,10>, <8,10> 
                      };

public rel[varname,expr] DEFS = {<"x", 3>, <"p", 4>, <"z", 6>, <"x", 8>, <"y", 10>};

public rel[varname, expr] USES =  {<"q", 5>, <"y", 6>, <"x", 6>, <"z", 10>};

public rel[varname,expr] UNINIT = 
   { <V, E> | <varname V, expr E> <- USES, 
              E in reachX(PRED, {ROOT}, DEFS[V])
   };
   
public set[varname] UNUSED = domain(DEFS) - domain(USES);
   
public bool test(){
   return UNINIT == {<"q", 5>, <"y", 6>, <"z", 10>} &&
          UNUSED == {"p"};
}