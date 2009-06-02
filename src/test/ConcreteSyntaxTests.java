package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConcreteSyntaxTests extends TestFramework {
	
	@Test
	public void singleA(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|a|] := [|a|];"));
	}
	
	@Test
	public void singleAspaces1(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[| a |] := [|a|];"));
	}
	
	@Test
	public void singleAspaces2(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|a|] := [| a |];"));
	}
	
	@Test
	public void AB(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|a b|] := [|a b|];"));
	}
	
	@Test
	public void ABspaces(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|a b|] := [|a   b|];"));
	}
	
	@Test
	public void varA(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|<someA*>|] := [|a|];"));
	}
	
	@Test
	public void varAtyped(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|<A someA>|] := [|a|];"));
	}
	
	@Test
	public void ABvars1(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|a <someB>|] := [|a b|];"));
	}
	
	@Test
	public void ABvars2(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|<someA> <someB>|] := [|a b|];"));
	}
	
	@Test
	public void D1(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|d|] := [|d|];"));
	}
	
	@Test
	public void D2(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|d d|] := [|d d|];"));
	}
	@Test
	public void Dvars(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|<Xs>|] := [|d d|];"));
	}
	
	@Test
	public void E1(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|e|] := [|e|];"));
	}
	
	@Test
	public void E2(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|e, e|] := [|e, e|];"));
	}
	
	@Test
	public void E2spaces1(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|e, e|] := [|e , e|];"));
	}
	
	@Test
	public void E2spaces2(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|e, e|] := [|e ,  e|];"));
	}
	
	@Test
	public void Evars(){
		prepare("import src::test::GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("[|<Xs>|] := [|e, e|];"));
	}
	
	
	private String QmoduleM = "module M\n" +
	                         "import languages::pico::syntax::Pico;\n" +
	                         "import basic::Whitespace;\n" +
	                         "public Tree t1 = [| begin declare x: natural; x := 10 end |];\n";
	
	private String UQmoduleM = "module M\n" +
                              "import languages::pico::syntax::Pico;\n" +
                              "import basic::Whitespace;\n" +
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
