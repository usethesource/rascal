module tests::functionality::AccumulatingTests
	
	public test bool testForWithAppend() {
		return {for (x <- [1,2,3]) append x; } == [1,2,3];
	}

 public test bool testForWithAppendAfterSomethingElse() {
		return { for (x <- [1,2,3]) { x += 1; append x; }} == [2,3,4];
	}

	
	public test bool testForWithAppendAndLabel() {
		return { y: for (x <- [1,2,3]) { append y: x; }} == [1,2,3];
	}

	
	public test bool testForWithAppendAndLabelOuter() {
		return { y: for (x <- [1,2,3]) { for (i <- [1,2,3]) append y: i; }} == [1,2,3,1,2,3,1,2,3];
	}

	
	public test bool testForWithAppendAndLabelOuterAndInner() {
		return { y: for (x <- [1,2,3]) { z: for (i <- [1,2,3]) append y: i; }} == [1,2,3,1,2,3,1,2,3];
	}

	
	public test bool testNestedAppend() {
		return { for (x <- [1,2,3]) append for (i <- [1,2,3]) append i; } == [[1,2,3],[1,2,3],[1,2,3]];
	}

	

	public test bool testSimpleNestedFor() {
		return {for (x <- [1,2,3]) append for (y <- [1,2,3]) append y; } 
			   == [[1,2,3],[1,2,3],[1,2,3]];
	}


	public test bool escapingClosureAppendToDevNull() {
		int() f() { for (x <- [1,2,3]) { int g() { append x; return 4; } return g; }}
		return f()() == 4;
	}
	

	public test bool testClosuresHaveAccessToLexicalScopeForAppend() {
		return {for (x <- [1,2,3]) { f = () { append x; }; f();}} 
		    == [1,2,3];
	}
		
	

	public test bool testWhileWithNoAppend() {
		return  {x = 3; while (x > 0) {x -= 1; }}== [];
	}

	public test bool testWhileWithAppend() {
		return {x = 3; while (x > 0) { append x; x -= 1; }}  == [3,2,1];
	}


	
	public test bool testDoWhileWithNoAppend() {
		return { x = 3; do { x -= 1; } while (x > 0);}  == [];
	}
	

	public test bool testDoWhileWithAppend() {
		return {x = 3; do { append x; x -= 1; } while (x > 0);} == [3,2,1];
	}
