package org.meta_environment.rascal.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.staticErrors.AppendWithoutFor;


public class AccumulatingTests extends TestFramework {

	@Test(expected=AppendWithoutFor.class)
	public void appendWithoutFor() {
		runTest("append 3;");
	}
	
	@Test
	public void testForWithAppend() {
		assertTrue(runTest("{ for (x <- [1,2,3]) append x; } == [1,2,3];"));
	}

	@Test
	public void testForWithAppendAfterSomethingElse() {
		assertTrue(runTestInSameEvaluator("{ for (x <- [1,2,3]) { x += 1; append x; }} == [2,3,4];"));
	}

	@Test
	public void testForWithAppendAndLabel() {
		assertTrue(runTestInSameEvaluator("{ y: for (x <- [1,2,3]) { append y: x; }} == [1,2,3];"));
	}

	@Test
	public void testForWithAppendAndLabelOuter() {
		assertTrue(runTestInSameEvaluator("{ y: for (x <- [1,2,3]) { for (i <- [1,2,3]) append y: i; }} == [1,2,3,1,2,3,1,2,3];"));
	}

	@Test
	public void testForWithAppendAndLabelOuterAndInner() {
		assertTrue(runTestInSameEvaluator("{ y: for (x <- [1,2,3]) { z: for (i <- [1,2,3]) append y: i; }} == [1,2,3,1,2,3,1,2,3];"));
	}

	@Test
	public void testNestedAppend() {
		assertTrue(runTestInSameEvaluator("{ for (x <- [1,2,3]) append for (i <- [1,2,3]) append i; } == [[1,2,3],[1,2,3],[1,2,3]];"));
	}

	
}
