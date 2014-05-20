 module tests::functionality::DataDeclarationTests
  /*******************************************************************************
   * Copyright (c) 2009-2011 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/
  
  
  data Bool = btrue() | bfalse() | band(Bool left, Bool right) | bor(Bool left, Bool right);
  data Exp = let(str name, Exp exp1, Exp exp2) | var(str name) | \int(int intVal);
  data Exp1[&T] = tval(&T tval) | tval2(&T tval1, &T tval2) | ival(int x);
  alias Var2 = str;
  data Exp2 = let(Var2 var, Exp2 exp1, Exp2 exp2) | var2(Var2 var) | \int2(int intVal);
  data Maybe[&T] = None() | Some(&T t);

  // bool
  
  		public test bool bool1() {Bool b = btrue(); return b == Bool::btrue();}
  		public test bool bool2() {Bool b = bfalse(); return b == Bool::bfalse();}
  		public test bool bool3() {Bool b = band(btrue(),bfalse());  return b == Bool::band(Bool::btrue(),Bool::bfalse());}
  		public test bool bool4() {Bool b = bor(btrue(),bfalse()); return b == bor(btrue(),bfalse());}
  		public test bool bool5() = band(btrue(),bfalse()).left == btrue();
  		public test bool bool6() = band(btrue(),bfalse()).right == bfalse();
  		public test bool bool7() = bor(btrue(),bfalse()).left == btrue();
  		public test bool bool8() = bor(btrue(),bfalse()).right == bfalse();
  		public test bool bool9() {Bool b = band(btrue(),bfalse()).left; return b == btrue();}
  		public test bool bool10() {Bool b = band(btrue(),bfalse()).right; return b == bfalse();}
  		
  		@expected{NoSuchField}
  		public test bool bool11() {Bool b = btrue(); return b.left == btrue(); }
  	
  // boolFieldUpdate
  
  		 public test bool boolFieldUpdate1() {
  		 Bool b = bor(btrue(),bfalse()); return b[left=bfalse()] == bor(bfalse(),bfalse());
  		 }
  		public test bool boolFieldUpdate2() {Bool b = bor(btrue(),bfalse()); return b[right=btrue()] == bor(btrue(),btrue());}
  		public test bool boolFieldUpdate3() {Bool b = bor(btrue(),bfalse());  b.left=bfalse();return b == bor(bfalse(),bfalse());}
  		public test bool boolFieldUpdate4() {Bool b = bor(btrue(),bfalse());b.right=btrue();  return b == bor(btrue(),btrue());}
  		public test bool boolFieldUpdate5() {Bool b = bor(bfalse(),bfalse()); b.left=btrue(); b.right=btrue(); return b == bor(btrue(),btrue());}
  		
  		@expected{NoSuchField}
  		public test bool boolFieldUpdate6() {Bool b = btrue(); return b.left == btrue();}
  		
  		
  
  // let
  
  		public test bool let1() {Exp e = \int(1); return e == \int(1);}
  		public test bool let2() {Exp e = var("a"); return e == var("a");}
  		public test bool let3() {Exp e = let("a",\int(1),var("a")); return e ==  let("a",\int(1),var("a"));}
  		
  // parameterized
  		
  		public test bool parameterized1() {Exp1[int] e = tval(1); return e == tval(1);}
  		public test bool parameterized2(){Exp1[str] f = tval("abc"); return f == tval("abc");}
  		public test bool parameterized3() {set[Exp1[value]] g = {tval(1),tval("abc")}; return g == {tval(1), tval("abc")};}
  		
  		// if the parameter is not bound by a constructor, the instantiated type equals the bound of the parameter, 
  		// any smaller types, like Exp1[int] would result in a type error
  		public test bool parameterized4() {a = tval(1); return a == tval(1);}
  		public test bool parameterized4a() {b = tval("abc"); b == tval("abc");}
  		
  		public test bool parameterized5() {Exp1[int] e = tval(1); return e == tval(1);}
  		public test bool parameterized6(){Exp1[str] f = tval("abc"); return f == tval("abc");}
  		public test bool parameterized7() {set[Exp1[value]] g = {tval(1),tval("abc")}; return g == {tval(1), tval("abc")};}
  		
  		// if the parameter is not bound by a constructor, the instantiated type equals the bound of the parameter, 
  		// any smaller types, like Exp1[int] would result in a type error
  		public test bool parameterized8() {Exp1[value] h = ival(3); return h == ival(3);}
  		
  		public test bool parameterized9(){j = tval2("abc", "def"); return j == tval2("abc", "def");}
  		public test bool parameterized10(){k = tval2("abc", "def"); return k.tval1 == "abc";}
  		public test bool parameterized11(){l = tval2("abc", "def"); return l.tval2 == "def";}
  		public test bool parameterized12(){m = tval2("abc", "def"); str s2 = m.tval2; return s2 == "def";}	
  		public test bool parameterized13() {Exp1[value] h = ival(3); return h == ival(3);}
  		
  		public test bool parameterized14(){j = tval2("abc", "def"); return j == tval2("abc", "def");}
  		public test bool parameterized15(){k = tval2("abc", "def"); return k.tval1 == "abc";}
  		public test bool parameterized16(){l = tval2("abc", "def"); return l.tval2 == "def";}
  		public test bool parameterized17(){m = tval2("abc", "def"); str s2 = m.tval2; return s2 == "def";}	
  
  // parameterizedErrorTest
  
  		public test bool parameterizedErrorTest1() {Exp1[int] h = ival(3); return h == ival(3);}
  
  // unboundTypeVar
  
  		public test bool unboundTypeVar1() { Maybe[void] x = None(); return x == None();}
  		public test bool unboundTypeVar2() { x = None(); x = Some(0); return x == Some(0);}
  	
  	public test bool unequalParameterType1(){
  		Exp1[value] x = tval2(3, "abc"); return true;
  	}
  
  //  let
  
  		public test bool let4() {Exp2 e = \int2(1); return e == \int2(1);}
  		public test bool let5(){Exp2 e = var2("a"); return e == var2("a");}
  		public test bool let6(){Exp2 e = let("a",\int2(1),var2("a")); return e ==  let("a",\int2(1),var2("a"));}
  		public test bool let7()=Var2 var2 := "a";
  	
  
  
