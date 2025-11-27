 module lang::rascal::tests::library::Boolean
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
  
import Boolean;
  
// arb
  
test bool arb1(){
    bool B = Boolean::arbBool(); 
  	return (B == true) || (B == false);
}

test bool arb2(){
    bool B = arbBool(); 
  	return (B == true) || (B == false);
}
  
// toInt
  
test bool toInt1()=Boolean::toInt(false) == 0;
test bool toInt2()=Boolean::toInt(true) == 1;
  
test bool toInt3()=toInt(false) == 0;
test bool toInt4()=toInt(true) == 1;
  
// toReal
  
test bool toReal1()=Boolean::toReal(false) == 0.0;
test bool toReal2()=Boolean::toReal(true) == 1.0;
  
test bool toReal3()=toReal(false) == 0.0;
test bool toReal4()=toReal(true) == 1.0;
  
// testToString
  
test bool testToString1()= Boolean::toString(false) == "false";
test bool testToString2()= Boolean::toString(true) == "true";
test bool testToString3()= toString(false) == "false";
test bool testToString4()= toString(true) == "true";
 
