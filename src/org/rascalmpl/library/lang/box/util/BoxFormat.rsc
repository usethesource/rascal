@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module lang::box::util::BoxFormat
import ParseTree;
// import lang::box::util::Concrete;
import lang::box::util::Box;
import IO;
import List;

import lang::box::\syntax::Box;

/*
alias UserDefinedFilter = Box(Tree t) ;

list[Box(Tree t)] userDefinedFilters = [ 
       ];
*/
list[Box] adjoin(list[Box] bs) {
        list[Box] v = [];
        list[Box] r = [];
        for (Box b<-bs) {
           if (L(_):=b) {
             v += b;
             }
           else {
            if (size(v)>1) r += H(0, v);
            else if (size(v)==1) r+=v[0];
            v=[];
            r+=b;
            }
         }
     if (size(v)>1) r += H(0, v);
     else if (size(v)==1) r+=v[0];
     return r;  
     }
     
Box makeBody(Tree body) {
   list[Box] bs = getArgs(body);
   bs = [b | Box b<-bs, COMM(_)!:=b];
   bs = adjoin(bs);
   /* Size(bs) is uneven */
   Box b = V(0, [I([bs[i]])|int i <-[0,1..size(bs)]]);
   return b;
   }
   

public Box getUserDefined(Tree q) {
      if (Boxx a:=q) {
          if ((Boxx) `<FontOperator op> [ <Boxx* boxs> ]`:=a) {
          list[Box] bs = getArgs(boxs);
          return   H(0, [evPt(op), L("["), H(1, bs), L("]")]);
         }
         if ((Boxx) `<BoxOperator op> [ <Boxx* boxs> ]`:=a) {
          return   V(0, [H(0, [evPt(op), L("[")]), makeBody(boxs), L("]")]);
         }
      }
      return NULL();
      }
      

list[int] isIndent(list[Symbol] q) {
     if (isScheme(q , ["N", "[", "N", "]"])) return [2];
     return [];
     }
     
     
bool isKeyword(Symbol a) {
     if (\lit(str s):=a) {
         if (s=="V" || s == "H" || s == "I" || s == "HV" || s == "HOV"
            || s == "WD" || s == "KW" || s == "VAR" || s == "NUM" || s=="STRING") return true;
         }
     return false;
     }
     
void setUserRules() {
    // setIndent(isIndent);
    // setCompact(isCompact);
    setKeyword(isKeyword);
    setUserDefined(getUserDefined);
    }
     
public Box BoxToBox(str src) {
    Tree a = parse(#Main, src);
    setUserRules();
    return treeToBox(a);
    }



/* --- Interface -- */  
     
public Box toBox(loc src) {
    Tree a = parse(#Main, src);
    setUserRules();
    return treeToBox(a);
    }
    

