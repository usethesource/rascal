@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module lang::box::util::Parse

import lang::box::\syntax::Box;
import lang::box::util::Box;
// import lang::box::util::BoxFormat;
// import lang::box::util::Concrete;
import IO;
import List;
import String;
import ParseTree;

Box annotateBox(Box b,list[Tree] tl) {
  for (Tree t<-tl) {
    if (SpaceOption so:=t) {
      if ((SpaceOption) `<SpaceSymbol sp> = <NatCon n>`:=so) {
        switch (sp) {
          case hs: b@hs = toInt("<n>");
          case vs: b@vs = toInt("<n>");
          case is: b@is = toInt("<n>");
          case ts: b@ts = toInt("<n>");
          }
        }
      }
    }
  return b;
  }

Box getUserDefined(Tree q) {
  if (StrCon a:=q) { return L("<a>"); }
  if ((Boxx) `<BoxOperator op> [ <Boxx* boxs> ]`:=q) {
    switch (op) {
      case (BoxOperator)`H <SpaceOption* s>`: return annotateBox(H(getArgs(boxs)),getA(s));
      case (BoxOperator)`V <SpaceOption* s>`: return annotateBox(V(getArgs(boxs)),getA(s));
      case (BoxOperator)`HV <SpaceOption* s>`: return annotateBox(HV(getArgs(boxs)),getA(s));
      case (BoxOperator)`HOV <SpaceOption* s>`: return annotateBox(HOV(getArgs(boxs)),getA(s));
      case (BoxOperator)`WD`: return WD(getArgs(boxs));
      case (BoxOperator)`I <SpaceOption* s>`: return annotateBox(HOV(getArgs(boxs)),getA(s));
      case (BoxOperator)`A <AlignmentOptions alignments> <SpaceOption* s>`:
           if ((AlignmentOptions) `( <{AlignmentOption ","}* at> )`:=alignments) {
             list[Tree] as = getA(at);
             Box b = annotateBox(A(getArgs(boxs)),getA(s));
             b@\format = ["<al>"|Tree t<-as,AlignmentOption al:=t];
             return b;
             }
      case (BoxOperator) `R`: return R(getArgs(boxs));
      }
    }
  if ((Boxx) `<FontOperator op> [ <Boxx* boxs> ]`:=q) {
    list[Box] bs = getArgs(boxs);
    switch (op) {
      case (FontOperator) `KW`: return KW(bs);
      case (FontOperator)`VAR`: return VR(bs);
      case (FontOperator)`NUM`: return NM(bs);
      case (FontOperator)`STRING`: return SG(bs);
      case (FontOperator)`ESC`: return SC(bs);
      case (FontOperator)`COMM`: return CT(bs);
      case (FontOperator)`MATH`: return MT(bs);
      }
    }
  return NULL();
  }

str pr(list[Box] bl) { str r = ""; for (Box b<-bl) r += toString(b); return r; }

str getAnno(Box b) {
  str r = "";
  if ((b@hs)?) { r += "hs=<b@hs>"; }
  if ((b@vs)?) { r += "vs=<b@vs>"; }
  if ((b@is)?) { r += "is=<b@is>"; }
  return r;
  }

str toString(Box c) {
  switch (c) {
    case L(str s): { return "\"<replaceAll(s,"\"","\\\"")>\""; }
    case H(list[Box] bl): { return "H<getAnno(c)>[<pr(bl)>]"; }
    case V(list[Box] bl): { return "V<getAnno(c)>[<pr(bl)>]"; }
    case I(list[Box] bl): { return "I<getAnno(c)>[<pr(bl)>]"; }
    case WD(list[Box] bl): { return "WD<getAnno(c)>[<pr(bl)>]"; }
    case HOV(list[Box] bl): { return "HOV<getAnno(c)>[<pr(bl)>]"; }
    case HV(list[Box] bl): { return "HV<getAnno(c)>[<pr(bl)>]"; }
    case A(list[Box] bl): { return "A[<pr(bl)>]"; }
    case R(list[Box] bl): { return "R[<pr(bl)>]"; }
    case KW(Box a): { return "KW[<toString(a)>]"; }
    case VAR(Box a): { return "VAR[<toString(a)>]"; }
    case NM(Box a): { return "NUM[<toString(a)>]"; }
    case STRING(Box a): { return "STRING[<toString(a)>]"; }
    case COMM(Box a): { return "COMM[<toString(a)>]"; }
    case MATH(Box a): { return "MATH[<toString(a)>]"; }
    case ESC(Box a): { return "ESC[<toString(a)>]"; }
    }
  }

public Box unparse(Box b) { return BoxToBox(toString(b)); }

public Box parse(loc src) {
  Main a = parse(#Main,src);
  setUserDefined(getUserDefined);
  return treeToBox(a);
  }
