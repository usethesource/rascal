 module tests::functionality::RangeTests
  
  /*******************************************************************************
   * Copyright (c) 2009-2011 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
  *******************************************************************************/
  
  // rangeInt
  
  		public test bool rangeInt1() = [1..1] == []; 
  		public test bool rangeInt2() = [1..-1] == [1, 0]; 
  		public test bool rangeInt3() = [1..0] == [1]; 
  		public test bool rangeInt4() = [1..2] == [1]; 
  		public test bool rangeInt5() = [1..5] == [1,2,3,4]; 
  		public test bool rangeInt6() = [1, 3..10] == [1, 3, 5, 7, 9 ]; 
  		public test bool rangeInt7() = [1, -2 .. -5] == [1, -2]; 

  	
  // rangeNum
  
  		public test bool rangeNum1() {{num n1 = 1; return [n1..n1] == []; }}
  		public test bool rangeNum2() {{num n1 = 1; num n2 = 2; return [n1..n2] == [1]; }}
  		public test bool rangeNum3() {{num n1 = 1; num n5 = 5; return [n1..n5] == [1,2,3,4]; }}
  		public test bool rangeNum4() {{num n1 = 1; num n3 = 3; num n10 = 10; return [n1, n3..n10] == [1, 3, 5, 7, 9 ]; }}
  
  // rangeReals
  
  		public test bool rangeReals1() = [1.0 .. .1] == [1.0]; 
  		public test bool rangeReals2() = [1.0 .. 1.0] == []; 
  		public test bool rangeReals3() = [1.0 .. 5.0] == [1.0, 2.0, 3.0, 4.0]; 
  		public test bool rangeReals4() = [1.0 .. 5.5] == [1.0, 2.0, 3.0, 4.0, 5.0]; 
  		public test bool rangeReals5() = [1.0,1.5 .. 2.0] == [1.0, 1.5]; 
  		public test bool rangeReals6() = [1.0, -2.0 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; 
  	
  // rangeMixed
  
  		public test bool rangeMixed1() = [1 .. .1] == [1]; 
  		public test bool rangeMixed2() = [1 .. 1.0] == []; 
  		@ignore{not allowed in compiler}public test bool rangeMixed3() = [1 .. 5.0] == [1, 2.0, 3.0, 4.0]; 
  		@ignore{not allowed in compiler}public test bool rangeMixed4() = [1 .. 5.5] == [1, 2.0, 3.0, 4.0, 5.0]; 
  		@ignore{not allowed in compiler}public test bool rangeMixed5() = [1 ,1.5 .. 2.0] == [1.0, 1.5]; 
  		@ignore{not allowed in compiler}public test bool rangeMixed6() = [1 ,1.5 .. 3] == [1.0, 1.5, 2.0, 2.5]; 
  		public test bool rangeMixed7() = [1.0, -2 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; 
  	
  alias nat = int;
  	
  	public test bool aliased1() {
  	   nat x = 0; nat y = 3; return [i|int i <- [x..y]]==[0..3];
  	}
  
  
 