module lang::rascal::tests::functionality::Scoping

	/* TODO: inert interpreter test */
	/*The following tests have been removed since we no longer support shadowing
	
	test bool localShadowing(){
		int n = 2; return int n := 3;
	}	

	test bool  localRedeclarationInt1(){
		int n ; return int n := 3 && n == 3;
	}
	
	test bool localRedeclarationInt2(){
		int n; return [int n] := [3] && n == 3;
	}

	test bool localShadowing2(){
		int n; return [*int n] := [1,2,3] && n == [1,2,3];
	}
	
	test bool  localShadowingListMatch(){
		list[int] n = [10,20]; return [*int n] := [1,2,3] && n == [1,2,3];
	}
	
	test bool  localRedeclarationList(){
		list[int] n; return [*int n] := [1,2,3] && n == [1,2,3];
	}
	
	test bool  localRedeclarationError9(){
		int n; return /<n:[0-9]*>/ := "123";
	}
	
	test bool  localComprehensionShadowing(){
		int n = 5; L = [n | int n <- [1 .. 10]]; return n==5;
	}
	
	test bool  localReunexpectedDeclaration10(){
		int n; L = [n | int n <- [1 .. 10]]; return L == [1 .. 10];
	}
	*/

	
	
//test bool RedeclaredLocal(){
//	int n = 1; {int m = 2;}; int m = 3; return n == 1 && m == 3;
//}
//	
//test bool  formalsToGlobalsLeak() {
//	int x = 0;
//	void f(int x) { x += 1;}
//	f(1); 
//	return x == 0;
//}
//
//// Nested function declarations and non-local variables
//
//
//int inc0(){
//	int n = 13;
//	int inc0(){
//		return n;
//	}
//	return inc0();
//}
//
//test bool tstInc0() = inc0() == 13;
//
//int inc1(int n){
//	int inc1(){
//		return n;
//	}
//	return inc1();
//}
//
//test bool tstInc1() = inc1(13) == 13;
//
//int inc2(int n){
//	int inc2(){
//		n += 1;
//		return n;
//	}
//	inc2();
//	return n;
//}
//
//test bool tstInc2() = inc2(13) == 14;
//
//int inc3(int n){
//	int inc3(){
//		int inc3(){
//			return n;
//		}
//		return inc3();
//	}
//	inc3();
//	return n;
//}
//
//test bool tstInc3() = inc3(13) == 13;
//
//int inc4(int n){
//	int inc4(){
//		int inc4(){
//			n += 1;
//			return n;
//		}
//		return inc4();
//	}
//	inc4();
//	return n;
//}
//
//test bool tstInc4() = inc4(13) == 14;
//
//int (int) f() { 
//	int n = 100; 
//	return int (int i) { return i + n; }; 
//}
//
//test bool tstF() = f()(11) == 111;
//
//
//// Closures
//
//int (int) g(int n) { 
//	return int (int i) { return i + n; }; 
//}
//
//test bool tstG1() = g(100)(11) == 111;
//
//test bool tstG2() { int n = 1000; return g(100)(11) == 111 && g(200)(11) == 211; }
//
//test bool tstG2() { 
//	g100 = g(100); 
//	int k(int n) = g100(n);
//	return g(100)(11) == k(11);
//}
//
//int (int) h(int n1) { 
//	int n2 = 50; 
//	int k(int i) { return n1 + n2 + i; } 
//	return k; 
//} 
//
//test bool tstH() = h(1)(2) == 53;

