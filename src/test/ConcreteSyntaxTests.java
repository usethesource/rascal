package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConcreteSyntaxTests extends TestFramework {
	private String QmoduleM = "module M\n" +
	                         "import languages::pico::syntax::Pico;\n" +
	                         "public Tree t1 = [| begin declare x: natural; x := 10 end |];\n";
	
	private String UQmoduleM = "module M\n" +
                              "import languages::pico::syntax::Pico;\n" +
                              "public Tree t1 = begin declare x: natural; x := 10 end;\n";
	
	@Test
	public void PicoQuoted1(){
		prepareModule(QmoduleM + "public bool match1() { return [| <PROGRAM program> |] := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match1();"));
	}
	
	@Test
	public void PicoQuoted2(){
		prepareModule(QmoduleM + "public bool match2() { return PROGRAM program := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match2();"));
	}
	
	@Test
	public void PicoQuoted3(){
		prepareModule(QmoduleM + "public bool match3() { return [| begin <decls> <stats> end |] := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match3();"));
	}
	
	@Test
	public void PicoQuoted4(){
		prepareModule(QmoduleM + "public bool match4() { return [| begin <DECLS decls> <stats> end |] := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match4();"));
	}
	
	@Test
	public void PicoQuoted5(){
		prepareModule(QmoduleM + "public bool match5() { return [| begin <decls> <{STATEMENT \";\"}* stats> end |] := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match5();"));
	}
	
	@Test
	public void PicoQuoted6(){
		prepareModule(QmoduleM + "public bool match6() { return [| begin <DECLS decls> <{STATEMENT \";\"}* stats> end |] := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match6();"));
	}
	
	@Test
	public void PicoUnQuoted1(){
		prepareModule(UQmoduleM + "public bool match1() { return <PROGRAM program> := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match1();"));
	}
	
	@Test
	public void PicoUnQuoted2(){
		prepareModule(UQmoduleM + "public bool match2() { return PROGRAM program := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match2();"));
	}
	
	@Test
	public void PicoUnQuoted3(){
		prepareModule(UQmoduleM + "public bool match3() { return begin <decls> <stats> end := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match3();"));
	}
	
	@Test
	public void PicoUnQuoted4(){
		prepareModule(UQmoduleM + "public bool match4() { return begin <DECLS decls> <stats> end := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match4();"));
	}
	
	@Test
	public void PicoUnQuoted5(){
		prepareModule(UQmoduleM + "public bool match5() { return begin <decls> <{STATEMENT \";\"}* stats> end := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match5();"));
	}
	
	@Test
	public void PicoUnQuoted6(){
		prepareModule(UQmoduleM + "public bool match6() { return begin <DECLS decls> <{STATEMENT \";\"}* stats> end := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match6();"));
	}
}
