@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module box::box::Basic
import ParseTree;
import basic::StrCon;
import box::Box;
import box::Concrete;
import box::box::Input;
import IO;

public Box getBasic(Tree q) {
if (<str key, str val, list[Tree] t> := selectBasic(q)) {
     list[Box] b = [evPt(a)|a<-t];
     switch (key) {
         case "L": return L(val);
         case "H": return H(b);
         case "V": return V(b);
         case "HV": return HV(b);
         case "HOV": return HOV(b);
         case "I": return I(b);
         }
     }
return NULL();
}


/*
if (BoxOperator a:=q) 
switch(a) {
	case `H <SpaceOption* options> `: return NULL();
	case `V <SpaceOption* options> `: return NULL();
	case `HV <SpaceOption* options> `: return NULL();
	case `HOV <SpaceOption* options> `: return NULL();
	case `I <SpaceOption* options> `: return NULL();
	case `WD `: return NULL();
}
if (SpaceOption a:=q) 
switch(a) {
	case `= `: return NULL();
}
if (SpaceSymbol a:=q) 
switch(a) {
	case `hs `: return NULL();
	case `vs `: return NULL();
	case `is `: return NULL();
	case `ts `: return NULL();
}
*/

