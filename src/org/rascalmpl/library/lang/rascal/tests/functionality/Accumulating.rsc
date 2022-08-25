module lang::rascal::tests::functionality::Accumulating
	
test bool testForWithNoAppend(){
    return { for(x <- [1,2,3]) { ; } } == [];
}

test bool testForWithAppend() {
	return {for (x <- [1,2,3]) append x; } == [1,2,3];
}

test bool testForWithAppendAfterSomethingElse() {
	return { for (x <- [1,2,3]) { x += 1; append x; }} == [2,3,4];
}

test bool testForWithAppendAndLabel() {
	return { y: for (x <- [1,2,3]) { append y: x; }} == [1,2,3];
}

test bool testForWithAppendAndLabelOuter() {
	return { y: for (_ <- [1,2,3]) { for (i <- [1,2,3]) append y: i; }} == [1,2,3,1,2,3,1,2,3];
}

test bool testForWithAppendAndLabelOuterAndInner() {
    // unused label for testing purposes
	return { y: for (_ <- [1,2,3]) { z: for (i <- [1,2,3]) append y: i; }} == [1,2,3,1,2,3,1,2,3];
}
	
test bool testNestedAppend() {
	return { for (_ <- [1,2,3]) append for (i <- [1,2,3]) append i; } == [[1,2,3],[1,2,3],[1,2,3]];
}

test bool testSimpleNestedFor() {
	return {for (_ <- [1,2,3]) append for (y <- [1,2,3]) append y; } == [[1,2,3],[1,2,3],[1,2,3]];
}
		
test bool testWhileWithNoAppend() {
	return  {x = 3; while (x > 0) {x -= 1; }} == [];
}

test bool testWhileWithAppend() {
	return {x = 3; while (x > 0) { append x; x -= 1; }}  == [3,2,1];
}

test bool testDoWhileWithNoAppend() {
	return { x = 3; do { x -= 1; } while (x > 0);}  == [];
}
	
test bool testDoWhileWithAppend() {
	return {x = 3; do { append x; x -= 1; } while (x > 0);} == [3,2,1];
}
