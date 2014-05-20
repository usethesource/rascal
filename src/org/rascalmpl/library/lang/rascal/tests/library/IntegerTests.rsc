 module tests::library::IntegerTests
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
  		public test bool abs2() = abs(-1) == 1;
  		public test bool abs3() = abs(1) == 1;
  
  // arbInt
  
  		public test bool arbInt1() {
  		    int N = arbInt(10); 
  		    return (N >= 0) && (N < 10);
  		    }
  		public test bool arbInt2() {
  		    int N = arbInt(); 
  		    return true;
  		    }
  
  // max
  
  		public test bool max1() = max(3, 10) == 10;
  		public test bool max2() = max(10, 10) == 10;
  
  // min
  
  		public test bool min1() = min(3, 10) == 3;
  		public test bool min2() = min(10, 10) == 10;
  
  // toReal
  
  		public test bool toReal1() =  toReal(3) == 3.0;
  
  // testToString
  
  		public test bool testToString1() = toString(314) == "314";
 