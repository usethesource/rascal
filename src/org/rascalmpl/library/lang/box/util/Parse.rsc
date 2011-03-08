module lang::box::util::Parse
import lang::box::util::Box;
import lang::box::util::BoxFormat;
import IO;
import List;
import String;

str pr(list[Box] bl) {
     str r = "";
     for (Box b <- bl) r+= toString(b);
     return r;
     }

str getAnno(Box b) {
      str r = "";
      if ((b@hs)?) {
           r+="hs=<b@hs>";
           }
      if ((b@vs)?) {
           r+="vs=<b@vs>";
           }
      if ((b@is)?) {
           r+="is=<b@is>";
           }
      /*
      if ((b@ts)?) {
           r+="ts=<b@ts>";
           }
      */
      return r;
      }

public str toString(Box c) {   
switch(c) {
         case L(str s): {return "\"<replaceAll(s,"\"","\\\"")>\"";}
         case  H(list[Box] bl): {return "H<getAnno(c)>[<pr(bl)>]"; }
         case  V(list[Box] bl): {return "V<getAnno(c)>[<pr(bl)>]";}
         case  I(list[Box] bl):{return "I<getAnno(c)>[<pr(bl)>]";}
         case  WD(list[Box] bl):{return "WD<getAnno(c)>[<pr(bl)>]";}
         case  HOV(list[Box] bl):{return "HOV<getAnno(c)>[<pr(bl)>]";}
         case  HV(list[Box] bl):{return "HV<getAnno(c)>[<pr(bl)>]";}
         case  A(list[Box] bl):{return "A[<pr(bl)>]";}
         case  R(list[Box] bl):{return "R[<pr(bl)>]";}
         case KW(Box a):{return "KW[<toString(a)>]";}
         case VAR(Box a):{return "VAR[<toString(a)>]";}
         case NM(Box a):{return "NUM[<toString(a)>]";}
         case STRING(Box a):{return "STRING[<toString(a)>]";}
         case COMM(Box a):{return "COMM[<toString(a)>]";}
         case MATH(Box a):{return "MATH[<toString(a)>]";}
         case ESC(Box a):{return "ESC[<toString(a)>]";}
     }
}


public Box unparse(Box b) {
   return BoxToBox(toString(b));
   }