 module lang::rascal::tests::library::Number
  /*******************************************************************************
   * Copyright (c) 2009-2015 CWI
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
 
// compare
  
test bool compare1() = 1r1 == 1;
test bool compare2() = 1r1 == 1.0;
test bool compare3() =  -1r1 == -1;
  
test bool compare4() = 1r2 < 1;
test bool compare5() = 1r2 <= 1;
test bool compare6() = 1r1 <= 1;
test bool compare7() = 3r2 > 1;
test bool compare8() = 3r2 >= 1;
test bool compare9() = 3r1 >= 1;
  
test bool compare10() = 1r2 < 1.0;
test bool compare11() = 1r2 <= 1.0;
test bool compare12() = 1r1 <= 1.0;
test bool compare13() = 3r2 > 1.0;
test bool compare14() = 3r2 >= 1.0;
test bool compare15() = 3r1 >= 1.0;
  		
test bool compare16() = 1r2 < 2r2;
test bool compare17() = 1r2 <= 2r2;
test bool compare18() = 1r1 <= 2r2;
test bool compare19() = 3r2 > 2r2;
test bool compare20() = 3r2 >= 2r2;
test bool compare21() = 3r1 >= 2r2;
  
// arithPromotion
  
test bool arithPromotion1() = 2r4 + 1r2 == 1r;
test bool arithPromotion2() = 2r4 - 1r2 == 0r;
test bool arithPromotion3() = 2r4 * 1r2 == 1r4;
test bool arithPromotion4() = 2r4 / 1r2 == 1r;
  
test bool arithPromotion5() = 2r4 + 2 == 5r2;
test bool arithPromotion6() = 2r4 - 2 == -3r2;
test bool arithPromotion7() = 2r4 * 2 == 1r;
test bool arithPromotion8() = 2r4 / 2 == 1r4;
  
test bool arithPromotion9() = 2r4 + 2.0 == 2.5;
test bool arithPromotion10() = 2r4 - 2.0 == -1.5;
test bool arithPromotion11() = 2r4 * 2.0 == 1.0;
test bool arithPromotion12() = 2r4 / 2.0 == 0.25;
  
  		
test bool arithPromotion13() = 2 + 1r2 == 5r2;
test bool arithPromotion14() = 2 - 1r2 == 3r2;
test bool arithPromotion15() = 2 * 1r2 == 1r;
test bool arithPromotion16() = 2 / 1r2 == 4r;
  
test bool arithPromotion17() = 2.0 + 1r2 == 2.5;
test bool arithPromotion18() = 2.0 - 1r2 == 1.5;
test bool arithPromotion19() = 2.0 * 1r2 == 1.0;
test bool arithPromotion20() = 2.0 / 1r2 == 4.0;
  

  	
  
 
