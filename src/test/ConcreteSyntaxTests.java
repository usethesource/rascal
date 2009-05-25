package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConcreteSyntaxTests extends TestFramework {
	
	@Test
	public void Pico(){
		
		prepareModule("module M\n" +
				 "import languages::pico::syntax::Pico;\n" +
				 "import basic::Whitespace;\n" +
				 "public Tree t1 = [| begin declare x: natural; x := 10 end |];\n" +
				 "public Tree t2 = begin declare x: natural; x := 10 end;\n" +
		         "public bool match1() { return [| begin <decls> <stats> end |] := t1; }\n" +
		         "public bool match2() { return begin <decls> <stats> end := t2; }");
		
		assertTrue(runTestInSameEvaluator("match1();"));
		assertTrue(runTestInSameEvaluator("match2();"));
	}

}
