@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Uninit
import Relation;
import  analysis::graphs::Graph;

// Find the unitialized variables in a program

alias expr = int;

alias varname = str;

public expr ROOT = 1;

public Graph[expr] PRED = { <1,3>, <3,4>, <4,5>, <5,6>, <5,8>, 
                        <6,10>, <8,10> 
                      };

public rel[varname,expr] DEFS = {<"x", 3>, <"p", 4>, <"z", 6>, <"x", 8>, <"y", 10>};

public rel[varname, expr] USES =  {<"q", 5>, <"y", 6>, <"x", 6>, <"z", 10>};

public rel[varname,expr] UNINIT = 
   { <V, E> | <varname V, expr E> <- USES, 
              E in reachX(PRED, {ROOT}, DEFS[V])
   };
   
public set[varname] UNUSED = domain(DEFS) - domain(USES);
   
public test bool t1() = UNINIT == {<"q", 5>, <"y", 6>, <"z", 10>};
public test bool t2() = UNUSED == {"p"};
