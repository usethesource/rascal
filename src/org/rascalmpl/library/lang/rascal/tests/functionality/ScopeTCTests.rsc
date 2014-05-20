module tests::functionality::ScopeTCTests

import StaticTestingUtils;

public test bool noEscapeFromToplevelMatch() = undeclaredVariable("bool a := true; a;");	

public test bool localRedeclarationError1() = redeclaredVariable("int n; int n;");	

public test bool localRedeclarationError2() = redeclaredVariable("int n = 1; int n;");	
	
public test bool localRedeclarationError3() = redeclaredVariable("int n = 1; int n = 2;");	

public test bool ifNoLeak1() = undeclaredVariable("if (int n := 3) {n == 3;} else  {n != 3;} n == 3;");	

public test bool ifNoLeak2() = undeclaredVariable("if(int n \<- [1 .. 3], n\>=3){n == 3;}else{n != 3;} n == 3;");	
	
public test bool blockNoLeak1() = undeclaredVariable("int n = 1; {int m = 2;} n == 1 && m == 2;");	

public test bool innerImplicitlyDeclared() = undeclaredVariable("int n = 1; {m = 2;}; return n == 1 && m == 2;");	
	
public test bool varsInEnumeratorExpressionsShouldNotLeak() = undeclaredVariable("int n \<- [1,2]; n == 1;");	
	