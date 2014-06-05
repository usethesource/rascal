module lang::rascal::tests::functionality::ScopeTests

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
	
	test bool  localRedeclarationError10(){
		int n; L = [n | int n <- [1 .. 10]]; return L == [1 .. 10];
	}
	*/

	
	
test bool RedeclaredLocal(){
	int n = 1; {int m = 2;}; int m = 3; return n == 1 && m == 3;
}
	
test bool  formalsToGlobalsLeak() {
	int x = 0;
	void f(int x) { x += 1;}
	f(1); 
	return x == 0;
}

