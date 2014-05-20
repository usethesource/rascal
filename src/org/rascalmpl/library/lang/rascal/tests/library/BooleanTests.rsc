 module tests::library::BooleanTests
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
  
  import Boolean;
  
  // arb
  
  		public test bool arb1(){
  		      bool B = Boolean::arbBool(); 
  		      return (B == true) || (B == false);
  		      }
  		public test bool arb2(){
  		     bool B = arbBool(); 
  		     return (B == true) || (B == false);
  		     }
  
  // toInt
  
  		public test bool toInt1()=Boolean::toInt(false) == 0;
  		public test bool toInt2()=Boolean::toInt(true) == 1;
  
  		public test bool toInt3()=toInt(false) == 0;
  		public test bool toInt4()=toInt(true) == 1;
  
  // toReal
  
  		public test bool toReal1()=Boolean::toReal(false) == 0.0;
  		public test bool toReal2()=Boolean::toReal(true) == 1.0;
  
  		public test bool toReal3()=toReal(false) == 0.0;
  		public test bool toReal4()=toReal(true) == 1.0;
  
  // testToString
  
  		public test bool testToString1()= Boolean::toString(false) == "false";
  		public test bool testToString2()= Boolean::toString(true) == "true";
  		public test bool testToString3()= toString(false) == "false";
  		public test bool testToString4()= toString(true) == "true";
 