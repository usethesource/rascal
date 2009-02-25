package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.meta_environment.rascal.interpreter.errors.*;

public class AnnotationTests extends TestFramework{
	
	
	@Test(expected=NoSuchAnnotationError.class)
	public void annotationNotAllowed(){
		prepare("data POS = pos(int n);");
		runTestInSameEvaluator("1 @ (pos:3);");
	}
	
	@Test(expected=NoSuchAnnotationError.class)
	public void annotationNotAllowed2(){
		runTest("1 @ pos;");
	}
	
	@Test(expected=TypeError.class)
	public void annotationNotAllowed3(){
		prepare("data F = f | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int pos on F;");
		runTestInSameEvaluator("f @ (pos:true);");
	}
	
	@Test(expected=NoSuchAnnotationError.class)
	public void annotationNotAllowed4(){
		prepare("data F = f | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int pos on F;");
		runTestInSameEvaluator("f @ (wrongpos:true);");
	}
	
	@Test
	public void annotations(){
		prepare("data F = f | f(int n) | g(int n) | deep(F f);");
		prepareMore("anno int pos on F;");
		
		assertTrue(runTestInSameEvaluator("f @ (pos:1) == f;"));
		assertTrue(runTestInSameEvaluator("f @ (pos:1) @ pos == 1;"));
		assertTrue(runTestInSameEvaluator("f @ (pos:1) @(pos:2) @ pos == 2;"));
		
		assertTrue(runTestInSameEvaluator("f(5) @ (pos:1) == f(5);"));
		assertTrue(runTestInSameEvaluator("f(5) @ (pos:1) @ pos == 1;"));
		assertTrue(runTestInSameEvaluator("f(5) @ (pos:1) @(pos:2) @ pos == 2;"));
		
		assertTrue(runTestInSameEvaluator("deep(f(5) @ (pos:1)) == deep(f(5));"));
		assertTrue(runTestInSameEvaluator("f(5) @ (pos:1) == f(5) @ (pos:2);"));	
	}
}

