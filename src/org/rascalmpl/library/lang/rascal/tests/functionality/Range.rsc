 module lang::rascal::tests::functionality::Range
  
  /*******************************************************************************
   * Copyright (c) 2009-2015 CWI
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
  
test bool rangeInt1() = [1..1] == []; 
test bool rangeInt2() = [1..-1] == [1, 0]; 
test bool rangeInt3() = [1..0] == [1]; 
test bool rangeInt4() = [1..2] == [1]; 
test bool rangeInt5() = [1..5] == [1,2,3,4]; 
test bool rangeInt6() = [1, 3..10] == [1, 3, 5, 7, 9 ]; 
test bool rangeInt7() = [1, -2 .. -5] == [1, -2]; 

// rangeNum
  
test bool rangeNum1() {{num n1 = 1; return [n1..n1] == []; }}
test bool rangeNum2() {{num n1 = 1; num n2 = 2; return [n1..n2] == [1]; }}
test bool rangeNum3() {{num n1 = 1; num n5 = 5; return [n1..n5] == [1,2,3,4]; }}
test bool rangeNum4() {{num n1 = 1; num n3 = 3; num n10 = 10; return [n1, n3..n10] == [1, 3, 5, 7, 9 ]; }}
  
// rangeReals
  
test bool rangeReals1() = [1.0 .. .1] == [1.0]; 
test bool rangeReals2() = [1.0 .. 1.0] == []; 
test bool rangeReals3() = [1.0 .. 5.0] == [1.0, 2.0, 3.0, 4.0]; 
test bool rangeReals4() = [1.0 .. 5.5] == [1.0, 2.0, 3.0, 4.0, 5.0]; 
test bool rangeReals5() = [1.0,1.5 .. 2.0] == [1.0, 1.5]; 
test bool rangeReals6() = [1.0, -2.0 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; 
  	
// rangeMixed
  
test bool rangeMixed1() = [1 .. 1.0] == []; 

@ignoreCompiler{
Different behaviour
}
test bool rangeMixed2a() = [1 .. .1] == [1.]; 
@ignoreInterpreter{
Different behaviour
}
test bool rangeMixed2b() = [1 .. .1] == [1.0]; 

@ignoreCompiler{
Different behaviour
}
test bool rangeMixed3a() = [1 .. 5.0] ==  [1.,2.0,3.0,4.0]; 
@ignoreInterpreter{
Different behaviour
}
test bool rangeMixed3b() = [1 .. 5.0] ==  [1.0,2.0,3.0,4.0]; 

@ignoreCompiler{
Different behaviour
}
test bool rangeMixed4a() = [1 .. 5.5] == [1.,2.0,3.0,4.0,5.0];
@ignoreInterpreter{
Different behaviour
} 
test bool rangeMixed4b() = [1 .. 5.5] == [1.0,2.0,3.0,4.0,5.0]; 

@ignoreCompiler{
Different behaviour
}
test bool rangeMixed5a() = [1 ,1.5 .. 2.0] == [1., 1.5]; 
@ignoreInterpreter{
Different behaviour
} 
test bool rangeMixed5b() = [1 ,1.5 .. 2.0] == [1.0, 1.5]; 

test bool rangeMixed6() = [1 ,1.5 .. 3] == [1, 1.5, 2.0, 2.5]; 
test bool rangeMixed7() = [1.0, -2 .. -10.0] == [1.0, -2.0, -5.0, -8.0]; 
  	
alias nat = int;
  	
test bool aliased1() {
    nat x = 0; nat y = 3; return [i|int i <- [x..y]]==[0..3];
}
  
  
 
