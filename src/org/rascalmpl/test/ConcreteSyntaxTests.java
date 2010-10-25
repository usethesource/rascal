package org.rascalmpl.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;

public class ConcreteSyntaxTests extends TestFramework {
	
	@Test
	public void parseDS(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("parse(#DS, \"d d d\") == (DS)`d d d`;"));
	}

	@Test
	public void parseDSInModule(){
		prepareModule("M", "module M " +
				"import GrammarABCDE;" +
				"public DS ds = (DS)`d d d`;" +
				"public DS parseDS(str input) { return parse(#DS, input); }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("parseDS(\"d d d\") == ds;"));
	}

	
	@Test
	public void parseDSfromFile(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("parse(#DS, |cwd:///src/org/rascalmpl/test/data/DS.trm|) == (DS)`d d d`;"));
	}

	@Test
	public void singleA(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a` := `a`;"));
	}
	
	@Test
	public void singleAspaces1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("` a ` := `a`;"));
	}
	
	@Test
	public void singleAspaces2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a` := ` a `;"));
	}
	
	@Test
	public void singleATyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("(A)`a` := `a`;"));
	}
	
	@Test
	public void singleAUnquoted1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("a := `a`;"));
	}
	
	@Test(expected=UndeclaredVariableError.class)
	public void singleAUnquoted2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("a := a;"));
	}
	
	@Test
	public void AB(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` := `a b`;"));
	}
	
	@Test
	public void ABspaces1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` := `a   b`;"));
	}
	
	@Test
	public void ABspaces2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` := `  a   b  `;"));
	}
	
	@Test(expected=Ambiguous.class)
	public void varAQuoted(){
		prepare("import GrammarABCDE;");
		runTestInSameEvaluator("`<someA>` := `a`;");
	}
	
	@Test
	public void varAassign(){
		prepare("import GrammarABCDE;");
		runTestInSameEvaluator("{someA := `a` && someA == `a`;}");
	}
	
	@Test
	public void varAQuotedTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`<A someA>` := `a`;"));
	}
	
	@Test
	public void varAQuotedDeclaredBefore(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{A someA; (A)`<someA>` := `a`;}"));
	}
	
	public void VarATypedInsertAmbiguous(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<A someA>` := `a` && someA == `a`; }"));
	}
	
	public void VarATypedInsert(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<A someA>` := `a` && someA == `a`; }"));
	}
	
	@Test
	public void ABvars1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a <someB>` := `a b`;"));
	}
	
	@Test
	public void ABvars1Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a <B someB>` := `a b`;"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	public void ABvars2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`<someA> <someB>` := `a b`;"));
	}
	
	public void ABvars2Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`<A someA> <B someB>` := `a b`;"));
	}
	
	@Test
	public void ABvars2TypedEq(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{`<A someA> <B someB>` := `a b` && someA ==`a` && someB == `b`;}"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	public void ABvars2TypedInsertWithoutTypes(){ 
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<A someA><B someB>` := `a b` &&  `<someA><someB>` == `a b`;}"));
	}
	
	@Test
	public void ABvars2TypedInsertWithTypes(){ 
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<A someA><B someB>` := `a b` && (C)`<someA><someB>` == `a b`;}"));
	}
	
	@Test
	public void ABequal1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` == `a b`;"));
	}
	
	@Test
	public void ABequal2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` == ` a b`;"));
	}
	
	@Test
	public void ABequal3(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` == `a b `;"));
	}
	
	@Test
	public void ABequal4(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` == ` a b `;"));
	}
	
	@Test
	public void ABequal5(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` == `a  b`;"));
	}
	
	@Test
	public void ABequal6(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`a b` == ` a  b `;"));
	}
	
	@Test
	public void D1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`d` := `d`;"));
	}
	
	@Test
	public void D2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`d d` := `d d`;"));
	}

	public void D3(){
		prepare("import GrammarABCDE;");
		assertFalse(runTestInSameEvaluator("(DS)`d d` := `d d`;"));
	}

	public void D4(){
		prepare("import GrammarABCDE;");
		assertFalse(runTestInSameEvaluator("`d d` := (DS)`d d`;"));
	}

	@Test
	public void D5(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("(DS)`d d` := (DS)`d d`;"));
	}

	
	@Test(expected=Ambiguous.class)
	public void Dvars(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`<Xs>` := `d d`;"));
	}
	
	@Test
	public void DvarsTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ D+ Xs := `d d` && Xs == ` d d `; }"));
	}
	
	@Test
	public void DvarsTypedInsert1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ D+ Xs := `d d` && Xs == ` d d `; }"));
	}

	
	@Test
	public void DvarsTypedInsert2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ (DS)`<D+ Xs>` := (DS)`d`; }"));
	}
	
	@Test
	public void DvarsTypedInsert3(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ (DS)`<D+ Xs>` := (DS)`d d`; }"));
	}

	
	@Test(expected=StaticError.class)
	public void DvarsTypedInsert2Untyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<D+ Xs>` := `d`; }"));
	}
	
	@Test
	public void DvarsTypedInsert3Untyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<D+ Xs>` := `d d`; }"));
	}

	
	@Test
	public void DvarsTypedInsert4UnTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("(`d <D+ Xs>` := `d d`)  && ` d <D+ Xs> ` == ` d d `;"));
	}
	
	@Test
	public void DvarsTypedInsert4(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("(DS)`d <D+ Xs>` := (DS)`d d` && (DS)` d <D+ Xs> ` == (DS)` d d `;"));
	}
	
	
	@Test
	public void DvarsTypedInsert5Untyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `d <D+ Xs>` := `d d d` && ` d <D+ Xs> ` == `d d d`; }"));
	}

	@Test 
	public void DvarsTypedInsert5(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ (DS)`d <D+ Xs>` := (DS)`d d d` && (DS)` d <D+ Xs> ` == (DS)`d d d`; }"));
	}

	@Test
	public void E1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`e` := `e`;"));
	}
	
	@Test
	public void E2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`e, e` := `e, e`;"));
	}
	
	@Test
	public void E2spaces1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`e, e` := `e , e`;"));
	}
	
	@Test
	public void E2spaces2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`e, e` := `e ,  e`;"));
	}
	
	@Test
	public void Evars1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ Xs := `e, e` && Xs == ` e, e`;}"));
	}
	
	@Test
	public void Evar1Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ {E \",\"}+ Xs := `e, e` && Xs == ` e, e`;}"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	public void Evars2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e, e` && Xs == ` e `;}"));
	}
	
	@Test
	public void NoStarSubjectToPlusVar(){
		prepare("import GrammarABCDE;");
		assertFalse(runTestInSameEvaluator("{E \",\"}+ Xs := ({E \",\"}*) ` `;"));
	}
	
	public void plusListShouldNotMatchEmptyList() {
		prepare("import GrammarABCDE;");
		assertFalse(runTestInSameEvaluator("` e, <{E \",\"}+ Es> ` := ({E \",\"}+) ` e `;"));
	}
	
	@Test
	public void starListPatternShouldMatchPlusListSubject() {
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{E \",\"}* Zs := ({E \",\"}+) ` e, e `;"));
	}
	
	@Test
	public void plusListPatternShouldMatchPStarListSubjectIfNotEmpty() {
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{E \",\"}+ Zs := ({E \",\"}*) ` e, e `;"));
	}
	
	@Test
	public void plusListPatternShouldNotMatchPStarListSubjectIfEmpty() {
		prepare("import GrammarABCDE;");
		assertFalse(runTestInSameEvaluator("{E \",\"}+ Zs := ({E \",\"}*) ` `;"));
	}
	
	@Test
	public void emptyListVariablePatternShouldBeSplicedInbetweenSeparators() {
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`e, <{E \",\"}* Xs>, e` := ` e, e `;"));
	}

	@Test
	public void emptyListVariablePatternShouldBeSplicedInbetweenSeparatorsAndBindToEmptyList() {
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`e, <{E \",\"}* Xs>, e` := ` e, e ` && Xs == ({E \",\"}*) ` `;"));
	}
	
	@Test
	public void emptySepListShouldSpliceCorrectly(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{E \",\"}* Xs := ({E \",\"}*) ` ` && `e, <{E \",\"}* Xs>, e ` == ` e, e `;"));
	}
	
	@Test
	public void Evars2Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e` && Xs == ({E \",\"}+) ` e `;}"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	// @Ignore("needs to be reinstated when we have a type checker")
	public void Evars3(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e, e` &&  Xs == ` e ` && ` e, <Xs> ` == ` e, e`; }"));
	}
	
	@Test
	public void Evars3Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e` && Xs == ({E \",\"}+) ` e ` && ({E \",\"}+) ` e, <{E \",\"}+ Xs> ` == ` e, e`; }"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	@Ignore("needs to be reinstated when we have a type checker")
	public void Evars4(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e` && Xs == ` ` && ` e, <Xs> ` == ` e `; }"));
	}
	
	@Test
	public void EvarsTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{E \",\"}+ Xs := `e, e`;"));
	}
	
	@Test
	public void EvarsTypedInsert1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && ` e, <{E \",\"}+ Xs> ` == ` e, e, e `; }"));
	}
	
	@Test
	public void EvarsTypedInsert1Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && ` e, <{E \",\"}+ Xs> ` == ` e, e, e `; }"));
	}
	
	@Test
	public void EvarsTypedInsert2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && ` e, <{E \",\"}+ Xs> ` == ` e, e, e `; }"));
	}
	
	@Test
	public void EvarsTypedInsert3(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ ` e, <{E \",\"}+ Xs> ` := `e, e, e` && ` e, <{E \",\"}+ Xs> ` == ` e, e, e `; }"));
	}
	
	@Test
	public void sortsInGrammar(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{A vA; B vB; C vC; D vD; DS vDS; E vE; ES vES; {E \",\"}+ vES2; true;}"));
	}
	
	
    @Test @Ignore("can not know that `d` should be a D+ list without a type checker")
	public void enumeratorDs1Untyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `d` ]; L == [`d`];}"));
	}
	
	@Test
	public void enumeratorDs1Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | D X <- `d` ]; L == [ `d` ];}"));
	}
	
	@Test
	public void enumeratorDsUnyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `d d d` ]; L == [`d`, `d`, `d`];}"));
	}
	
	@Test
	public void enumeratorDsTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | D X <- `d d d` ]; L == [`d`, `d`, `d`];}"));
	}
	
	@Test @Ignore("Can not know that `e` should be an E* or E+ list without a type-checker")
	public void enumeratorEs1Untyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `e` ]; L == [ `e` ];}"));
	}
	
	@Test
	public void enumeratorEs1Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | E X <- `e` ]; L == [ `e` ];}"));
	}
	
	@Test
	public void enumeratorEsUntyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `e, e, e` ]; L == [`e`, `e`, `e`];}"));
	}
	
	@Test
	public void enumeratorEsTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{L = [X | E X <- `e, e, e` ]; L == [`e`, `e`, `e`];}"));
	}

	@Test
	public void EvarsTypedInsert3Empty(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("` e, <{E \",\"}* Xs> ` := ({E \",\"}+) `e`;"));
	}

	@Test
	public void Pico1(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{t1 = `begin declare x: natural; x := 10 end`;true;}"));
	}
	
	@Test
	public void Pico2(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{PROGRAM P := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	public void Pico3(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{`<PROGRAM P>` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	public void Pico4(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{`begin <decls> <stats> end` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	public void Pico5(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{`begin <DECLS decls> <{STATEMENT \";\"}* stats> end` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	public void Pico6(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{DECLS decls; {STATEMENT \";\"}* stats; `begin <decls> <stats> end` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	public void Pico7a(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{`begin <DECLS decls> <{STATEMENT \";\"}+ stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
				                          "(decls == `declare x: natural;`);}"));
	}
	
	@Test
	public void Pico7b(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{`begin <DECLS decls> <{STATEMENT \";\"}+ stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
				                          "(decls == `declare x: natural;`) && (stats == ({STATEMENT \";\"}+)`x := 1; x := 2`);}"));
	}
	
	@Test
	public void Pico7c(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{`begin <DECLS decls> <{STATEMENT \";\"}* stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
				                          "(decls == `declare x: natural;`) && (stats == ({STATEMENT \";\"}*)`x := 1; x := 2`);}"));
	}
	
	@Test
	public void Pico8(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{ bool B;" +
				                          "  if(`begin <DECLS decls> <{STATEMENT \";\"}* stats> end` := `begin declare x: natural; x := 1; x := 2 end`){" +
				                          "            B = (decls == `declare x: natural;`);" +
				                          "  } else" +
				                          "     B = false; " +
				                          "  B;" +
				                          "}"));
	}
	
	private String QmoduleM = "module M\n" +
	                         "import languages::pico::syntax::Pico;\n" +
	                         "public Tree t1 = `begin declare x: natural; x := 10 end`;\n" +
	                         "public Tree t2 = `declare x : natural;`;\n";
	
	@Test
	public void PicoQuoted0() {
		prepareModule("M", QmoduleM + "public bool match1() { return `<PROGRAM program>` := t1; }\n");
	}
	
	@Test
	public void PicoQuoted1(){
		prepareModule("M", QmoduleM + "public bool match1() { return `<PROGRAM program>` := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match1();"));
	}
	
	@Test
	public void PicoQuoted2(){
		prepareModule("M", QmoduleM + "public bool match2() { return PROGRAM program := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match2();"));
	}
	
	@Test
	public void PicoQuoted3(){
		prepareModule("M", QmoduleM + "public bool match3() { return `begin <decls> <stats> end` := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match3();"));
	}
	
	@Test
	public void PicoQuoted4(){
		prepareModule("M", QmoduleM + "public bool match4() { return `begin <DECLS decls> <stats> end` := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match4();"));
	}
	
	@Test
	public void PicoQuoted5(){
		prepareModule("M", QmoduleM + "public bool match5() { return `begin <decls> <{STATEMENT \";\"}* stats> end` := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match5();"));
	}
	
	@Test
	public void PicoQuoted6(){
		prepareModule("M", QmoduleM + "public bool match6() { return `begin <DECLS decls> <{STATEMENT \";\"}* stats> end` := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match6();"));
	}
	
	@Test
	public void PicoQuoted7(){
		prepareModule("M", QmoduleM + "public bool match7() { return ` begin declare <{\\ID-TYPE \",\" }* decls>; <{STATEMENT \";\"}* Stats> end ` := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match7();"));
	}
	
	@Test
	public void PicoQuoted8(){
		prepareModule("M", QmoduleM + "public bool match8() { return ` declare <{\\ID-TYPE \",\" }* decls>; ` := t2; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match8();"));
	}
	
	private String UQmoduleM = "module M\n" +
    "import languages::pico::syntax::Pico;\n" +
    "public Tree t1 = begin declare x: natural; x := 10 end;\n";

	@Test(expected=StaticError.class) // Directly antiquoting without quotes not allowed.
	public void PicoUnQuoted1(){
		prepareModule("M", UQmoduleM + "public bool match1() { return <PROGRAM program> := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match1();"));
	}
	
	@Test
	public void PicoUnQuoted2(){
		prepareModule("M", UQmoduleM + "public bool match2() { return PROGRAM program := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match2();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted3(){
		prepareModule("M", UQmoduleM + "public bool match3() { return begin <decls> <stats> end := t1; }\n");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match3();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted4(){
		prepareModule("M", UQmoduleM + "public bool match4() { return begin <DECLS decls> <stats> end := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match4();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted5(){
		prepareModule("M", UQmoduleM + "public bool match5() { return begin <decls> <{STATEMENT \";\"}* stats> end := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match5();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted6(){
		prepareModule("M", UQmoduleM + "public bool match6() { return begin <DECLS decls> <{STATEMENT \";\"}* stats> end := t1; }");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match6();"));
	}
	
	@Test
	public void enumeratorPicoStatement1Untyped(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `a:=1` ]; L == [ `a:=1` ];}"));
	}
	
	@Test
	public void enumeratorPicoStatement1Typed(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | STATEMENT X <- `a:=1` ]; L == [ `a:=1` ];}"));
	}
	
	@Test
	public void enumeratorPicoStatementsUntyped(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	public void enumeratorPicoStatementsTyped(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | STATEMENT X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	public void enumeratorPicoStatementsConcretePattern1(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | `<\\PICO-ID X>:=1` <- `a:=1;b:=2;c:=1` ]; L == [ `a`, `c` ];}"));
	}
	
	@Test
	public void enumeratorPicoStatementsConcretePattern2(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | /`b:=<EXP X>` <- `a:=1;b:=2;c:=3` ]; L == [ (EXP)`2` ];}"));
	}
	
	@Test
	public void enumeratorPicoStatementsConcretePattern3(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [Id | /`<\\PICO-ID Id> : <TYPE Tp>` <- `x : natural, y : string` ]; L == [ `x`, `y` ];}"));
	}
	
	@Test
	public void enumeratorPicoStatementsConcretePattern4(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = []; for(/`<\\PICO-ID Id> : <TYPE Tp>` <- `x : natural, y : string`){L += Id;} L == [ `x`, `y` ];}"));
	}
	
	@Test
	public void forPicoStatementsTyped1(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | /STATEMENT X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	public void forPicoStatementsTyped2(){
		prepare("import languages::pico::syntax::Pico;");
//		assertTrue(runTestInSameEvaluator("{L = [X | /STATEMENT X <- `begin declare a : natural; a:=1;a:=2;a:=3 end` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	public void forPicoStatementsTyped3(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{L = [X | /EXP X <- `begin declare a : natural; a:=1;b:=2;c:=3 end` ]; L == [(EXP)`1`, (EXP)`2`, (EXP)`3` ];}"));
	}
	
	@Test
	public void PicoStringDoesNotOverrideRascalString1(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{str s = \"abc\"; s == \"abc\";}"));
	}
	
	@Test
	public void PicoStringDoesNotOverrideRascalString2(){
		prepare("import languages::pico::syntax::Pico;");
		assertTrue(runTestInSameEvaluator("{int n = 3; s = \"abc<n>\"; s == \"abc3\";}"));
	}
	
}
