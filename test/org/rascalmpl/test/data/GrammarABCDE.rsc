@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module GrammarABCDE

layout Whitespace = [\ \t\n]*;

start syntax A = "a";
start syntax B = "b";
start syntax C = A B;
start syntax D = "d";
start syntax DS = D+;
start syntax E = "e";
start syntax ES = {E ","}+;
