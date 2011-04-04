@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module box::box::Input

import ParseTree;
import languages::box::syntax::Box;
import box::Concrete;
import IO;
import String;


public Tree inPut(loc asf) {
     Tree a = parse(#Box, asf);
     return a;
     }
     
public tuple[str, str, list[Tree]] selectBasic(Tree q) {
if (StrCon v:=q) {
     str g = "<v>";
     return <"L", "<substring(g, 1, size(g)-1)>", []>;
     }
if (Box a:=q) 
switch(a) {
	case `<StrCon s>`: return <"L", s, []>;
	case `<BoxOperator operator> [ <Box* lst> ] `: {
	     switch (operator) {
	         case `H <SpaceOption* options>`: return <"H", "", getA(lst)>;
	         case `V <SpaceOption* options>`: return <"V", "", getA(lst)>;
	         case `HV <SpaceOption* options>`: return <"HV", "", getA(lst)>;
	         case `HOV <SpaceOption* options>`: return <"HOV", "", getA(lst)>;
	         case `I <SpaceOption*  options>`: return <"I", "", getA(lst)>;
	         // case `WD <options>`: return <"WD", "", getA(lst)>;
	     };
      }
}
return <"", "", []>;
}
