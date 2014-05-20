 module tests::library::NumberTests
  /*******************************************************************************
   * Copyright (c) 2009-2011 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/
  import util::Math;
  
  // abs
  
  		public test bool abs1() = abs(0) == 0;
  		public test bool abs2() = abs(0r) == 0r;
  		public test bool abs3() = abs(-1) == 1;
  		public test bool abs4() = abs(-1r1) == 1r1;
  		public test bool abs5() = abs(1) == 1;
  		public test bool abs6() = abs(1.5) == 1.5;
  		public test bool abs7() = abs(3r2) == 3r2;
  		public test bool abs8() = abs(-1.5) == 1.5;
  		public test bool abs9() = abs(-3r2) == 3r2;
  
  // arbInt
  
  		public test bool absInt1() {int N = arbInt(10); return (N >= 0) && (N < 10);}
  		public test bool absInt2() {int N = arbInt(); return true;}

  // compare
  
  		public test bool compare1() = 1r1 == 1;
  		public test bool compare2() = 1r1 == 1.0;
  		public test bool compare3() =  -1r1 == -1;
  
  		public test bool compare4() = 1r2 < 1;
  		public test bool compare5() = 1r2 <= 1;
  		public test bool compare6() = 1r1 <= 1;
  		public test bool compare7() = 3r2 > 1;
  		public test bool compare8() = 3r2 >= 1;
  		public test bool compare9() = 3r1 >= 1;
  
  		public test bool compare10() = 1r2 < 1.0;
  		public test bool compare11() = 1r2 <= 1.0;
  		public test bool compare12() = 1r1 <= 1.0;
  		public test bool compare13() = 3r2 > 1.0;
  		public test bool compare14() = 3r2 >= 1.0;
  		public test bool compare15() = 3r1 >= 1.0;
  		
  		public test bool compare16() = 1r2 < 2r2;
  		public test bool compare17() = 1r2 <= 2r2;
  		public test bool compare18() = 1r1 <= 2r2;
  		public test bool compare19() = 3r2 > 2r2;
  		public test bool compare20() = 3r2 >= 2r2;
  		public test bool compare21() = 3r1 >= 2r2;
  
  // arithPromotion
  
  		public test bool arithPromotion1() = 2r4 + 1r2 == 1r;
  		public test bool arithPromotion2() = 2r4 - 1r2 == 0r;
  		public test bool arithPromotion3() = 2r4 * 1r2 == 1r4;
  		public test bool arithPromotion4() = 2r4 / 1r2 == 1r;
  
  		public test bool arithPromotion5() = 2r4 + 2 == 5r2;
  		public test bool arithPromotion6() = 2r4 - 2 == -3r2;
  		public test bool arithPromotion7() = 2r4 * 2 == 1r;
  		public test bool arithPromotion8() = 2r4 / 2 == 1r4;
  
  		public test bool arithPromotion9() = 2r4 + 2.0 == 2.5;
  		public test bool arithPromotion10() = 2r4 - 2.0 == -1.5;
  		public test bool arithPromotion11() = 2r4 * 2.0 == 1.0;
  		public test bool arithPromotion12() = 2r4 / 2.0 == 0.25;
  
  		
  		public test bool arithPromotion13() = 2 + 1r2 == 5r2;
  		public test bool arithPromotion14() = 2 - 1r2 == 3r2;
  		public test bool arithPromotion15() = 2 * 1r2 == 1r;
  		public test bool arithPromotion16() = 2 / 1r2 == 4r;
  
  		public test bool arithPromotion17() = 2.0 + 1r2 == 2.5;
  		public test bool arithPromotion18() = 2.0 - 1r2 == 1.5;
  		public test bool arithPromotion19() = 2.0 * 1r2 == 1.0;
  		public test bool arithPromotion20() = 2.0 / 1r2 == 4.0;
  
  // arbReal
  
  		public test bool arbReal1() {real D = arbReal(); return (D >= 0.0) && (D <= 1.0);}
  
  // max
  
  		public test bool testToString1() = max(3, 10) == 10;
  		public test bool testToString2() = max(10, 10) == 10;
  		public test bool testToString3() = max(3.0, 10.0) == 10.0;
  		public test bool testToString4() = max(10.0, 10.0) == 10.0;
  		
  		public test bool testToString5() = max(3.5, 10) == 10;
  		public test bool testToString6() = max(3, 10.5) == 10.5;
  
  // min
  
  		public test bool min1() = min(3, 10) == 3;
  		public test bool min2() = min(10, 10) == 10;
  		
  		public test bool min3() = min(3.0, 10.0) == 3.0;
  		public test bool min4() = min(3.0, 10.0) == 3.0;
  		public test bool min5() = min(10.0, 10.0) == 10.0;
  		
  		public test bool min6() = min(3.5, 10) == 3.5;
  		public test bool min7() = min(3, 10.5) == 3;
  	
  // toInt
  
  		public test bool toInt1() = toInt(3) == 3;
  		public test bool toInt2() = toInt(3.14) == 3;
  		public test bool toInt3() = toInt(3r2) == 1;
  		public test bool toInt4() = toInt(4r2) == 2;
  
  // toReal
  
  		public test bool toReal1() =  toReal(3) == 3.0;
  		public test bool toReal2() = toReal(3.14) == 3.14;
  
  // testToString
  
  		public test bool testToString7() = toString(314) == "314";
  		public test bool testToString8() = toString(3.14) == "3.14";
  		public test bool testToString9() = toString(4r8) == "1r2";
  
 