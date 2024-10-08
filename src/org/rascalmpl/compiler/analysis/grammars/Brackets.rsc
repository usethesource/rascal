@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}

module analysis::grammars::Brackets

import Grammar;
import Node;
import lang::rascal::grammar::definition::Priorities;
import ParseTree;
import List;

DoNotNest prioritiesOf(type[&T] t) = doNotNest(grammar({}, t.definitions));

default &T parens(DoNotNest prios, node parent, node kid, &T x,  &T(&T x) parenizer) = x;

&T parens(DoNotNest prios, node parent, node kid, &T x,  &T(&T x) parenizer) = parenizer(x)
  when 
     <pprod, pos, kprod> <- prios,
     pprod.def has name,
     kprod.def has name, 
     pprod.def.name == getName(parent), 
     kprod.def.name == getName(kid),
     parent[astPosition(pos, pprod)] == kid;

private int astPosition(int pos, Production p)
  = ( -1 | it + 1 | i <- [0,1..pos], isASTsymbol(p.symbols[i]) );

bool isASTsymbol(\layouts(_)) = false; 
bool isASTsymbol(\keywords(str name)) = false;
bool isASTsymbol(\lit(str string)) = false;
bool isASTsymbol(\cilit(str string)) = false;
//bool isASTsymbol(\conditional(_, _)) = false;
bool isASTsymbol(\empty()) = false;
default bool isASTsymbol(Symbol _) = true;

