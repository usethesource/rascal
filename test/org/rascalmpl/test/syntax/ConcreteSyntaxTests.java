/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.syntax;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPattern;
import org.rascalmpl.test.infrastructure.TestFramework;

public class ConcreteSyntaxTests extends TestFramework {
	
	@Test
	public void parseDS(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("parse(#DS, \"d d d\") == (DS)`d d d`;"));
	}

	@Test
	public void parseDSInModule(){
		prepareModule("M", "module M " +
				"import GrammarABCDE;" +
				"import ParseTree;" +
				"public DS ds = (DS)`d d d`;" +
				"public DS parseDS(str input) { return parse(#DS, input); }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("parseDS(\"d d d\") == ds;"));
	}

	
	@Test
	public void parseDSfromFile(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("parse(#DS, |testdata:///DS.trm|) == (DS)`d d d`;"));
	}

	@Test @Ignore("not supported")
	public void singleA(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a` := `a`;"));
	}
	
	@Test @Ignore("not supported")
	public void singleATyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("(A)`a` := `a`;"));
	}
	
	@Test @Ignore("not supported")
	public void singleAUnquoted1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("a := `a`;"));
	}
	
	@Test(expected=UndeclaredVariable.class) @Ignore("not supported")
	public void singleAUnquoted2(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("a := a;"));
	}
	
	@Test @Ignore("not supported")
	public void AB(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a b` := `a b`;"));
	}
	
	@Test @Ignore("not supported")
	public void ABspaces1(){ 
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a b` := `a   b`;"));
	}
	
	@Test(expected=UnsupportedPattern.class) @Ignore("not supported")
	public void varAQuoted(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		runTestInSameEvaluator("`<someA>` := `a`;");
	}
	
	@Test @Ignore("not supported")
	public void varAassign(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		runTestInSameEvaluator("{someA := `a` && someA == `a`;}");
	}
	
	@Test @Ignore("not supported")
	public void varAQuotedTyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`<A someA>` := `a`;"));
	}
	
	@Test @Ignore("not supported")
	public void varAQuotedDeclaredBefore(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{A someA; (A)`<someA>` := `a`;}"));
	}
	
	@Test @Ignore("not supported") 
	public void VarATypedInsertAmbiguous(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<A someA>` := `a` && someA == `a`; }"));
	}

	@Test @Ignore("not supported")
	public void VarATypedInsert(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<A someA>` := `a` && someA == `a`; }"));
	}
	
	@Test @Ignore("not supported")
	public void ABvars1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a <someB>` := `a b`;"));
	}
	
	@Test @Ignore("not supported")
	public void ABvars1Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a <B someB>` := `a b`;"));
	}
	
	@Test @Ignore("not supported")
	public void ABvars2(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`<someA> <someB>` := `a b`;"));
	}

	@Test @Ignore("not supported")
	public void ABvars2Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`<A someA> <B someB>` := `a b`;"));
	}
	
	@Test @Ignore("not supported")
	public void ABvars2TypedEq(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{`<A someA> <B someB>` := `a b` && someA ==`a` && someB == `b`;}"));
	}
	
	@Test @Ignore("not supported")
	public void ABvars2TypedInsertWithoutTypes(){ 
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<A someA><B someB>` := `a b` &&  `<someA><someB>` == `a b`;}"));
	}
	
	@Test @Ignore("not supported")
	public void ABvars2TypedInsertWithTypes(){ 
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<A someA><B someB>` := `a b` && (C)`<someA><someB>` == `a b`;}"));
	}
	
	@Test @Ignore("not supported")
	public void ABequal1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a b` == `a b`;"));
	}
	
	@Test @Ignore("not supported")
	public void ABequal5(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`a b` == `a  b`;"));
	}
	
	@Test @Ignore("not supported")
	public void D1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`d` := `d`;"));
	}
	
	@Test @Ignore("not supported")
	public void D2(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`d d` := `d d`;"));
	}

	@Test @Ignore("not supported")
	public void D3(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("(DS)`d d` := `d d`;"));
	}

	@Test @Ignore("not supported")
	public void D4(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("`d d` := (DS)`d d`;"));
	}

	@Test
	public void D5(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("(DS)`d d` := (DS)`d d`;"));
	}

	
	@Test(expected=UnsupportedPattern.class) @Ignore("not supported")
	public void Dvars(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`<Xs>` := `d d`;"));
	}
	
	@Test @Ignore("not supported")
	public void DvarsTyped(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ D+ Xs := (D+) `d d` && Xs == (D+) `d d`; }"));
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

	
	@Test(expected=StaticError.class) @Ignore("not supported")
	public void DvarsTypedInsert2Untyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<D+ Xs>` := `d`; }"));
	}
	
	@Test @Ignore("not supported")
	public void DvarsTypedInsert32(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<D+ Xs>` := (D+) `d d`; }"));
	}
	
	@Test @Ignore("not supported")
	public void DvarsTypedInsert4UnTyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("(`d <D+ Xs>` := `d d`)  && `d <D+ Xs>` == `d d`;"));
	}
	
	@Test
	public void DvarsTypedInsert4(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("(DS)`d <D+ Xs>` := (DS)`d d` && (DS)`d <D+ Xs>` == (DS)`d d`;"));
	}
	
	@Test @Ignore("not supported")
	public void DvarsTypedInsert5Untyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `d <D+ Xs>` := `d d d` && `d <D+ Xs>` == `d d d`; }"));
	}

	@Test 
	public void DvarsTypedInsert5(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ (DS)`d <D+ Xs>` := (DS)`d d d` && (DS)`d <D+ Xs>` == (DS)`d d d`; }"));
	}

	@Test @Ignore("not supported")
	public void E1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`e` := `e`;"));
	}
	
	@Test @Ignore("not supported")
	public void E2(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`e, e` := `e, e`;"));
	}
	
	@Test @Ignore("not supported")
	public void E2spaces1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`e, e` := `e , e`;"));
	}
	
	@Test @Ignore("not supported")
	public void E2spaces2(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`e, e` := `e ,  e`;"));
	}
	
	@Test @Ignore("not supported")
	public void Evars1(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ Xs := `e, e` && Xs == `e, e`;}"));
	}
	
	@Test 
	@Ignore("Functionality subject to future/current change")
	public void Evar1Typed(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ {E \",\"}+ Xs := ({E \",\"}+) `e, e` && Xs == ({E \",\"}+)  `e, e`;}"));
	}
	
	@Test @Ignore("not supported") // (expected=AmbiguousConcretePattern.class)
	public void Evars2(){
		prepare("import GrammarABCDE;");
		assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e, e` && Xs == `e`;}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void NoStarSubjectToPlusVar(){
		prepare("import GrammarABCDE;");
		assertFalse(runTestInSameEvaluator("{E \",\"}+ Xs := ({E \",\"}*) ``;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void plusListShouldNotMatchEmptyList() {
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertFalse(runTestInSameEvaluator("({E \",\"}+) `e, <{E \",\"}+ Es>` := ({E \",\"}+) `e`;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void starListPatternShouldMatchPlusListSubject() {
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{E \",\"}* Zs := ({E \",\"}+) `e, e`;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void plusListPatternShouldMatchPStarListSubjectIfNotEmpty() {
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{E \",\"}+ Zs := ({E \",\"}*) `e, e`;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void plusListPatternShouldNotMatchPStarListSubjectIfEmpty() {
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertFalse(runTestInSameEvaluator("{E \",\"}+ Zs := ({E \",\"}*) ``;"));
	}
	
	@Test @Ignore("not supported")
	public void emptyListVariablePatternShouldBeSplicedInbetweenSeparators() {
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`e, <{E \",\"}* Xs>, e` := `e, e`;"));
	}

	@Test
	@Ignore("Functionality subject to future/current change")
	public void emptyListVariablePatternShouldBeSplicedInbetweenSeparatorsAndBindToEmptyList() {
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("`e, <{E \",\"}* Xs>, e` := `e, e` && Xs == ({E \",\"}*) ``;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void emptySepListShouldSpliceCorrectly(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{E \",\"}* Xs := ({E \",\"}*) `` && `e, <{E \",\"}* Xs>, e` == `e, e`;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Evars2Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e` && Xs == ({E \",\"}+) `e`;}"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	@Ignore("not supported")
	// @Ignore("needs to be reinstated when we have a type checker")
	public void Evars3(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e, e` &&  Xs == `e` && `e, <Xs>` == `e, e`; }"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Evars3Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e` && Xs == ({E \",\"}+) `e` && ({E \",\"}+) `e, <{E \",\"}+ Xs>` == `e, e`; }"));
	}
	
	@Test // (expected=AmbiguousConcretePattern.class)
	@Ignore("needs to be reinstated when we have a type checker")
	public void Evars4(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e` && Xs == ` ` && ` e, <Xs> ` == `e`; }"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void EvarsTyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{E \",\"}+ Xs := `e, e`;"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void EvarsTypedInsert1(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void EvarsTypedInsert1Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void EvarsTypedInsert2(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
	}
	
	@Test @Ignore("not supported")
	public void EvarsTypedInsert3(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
	}
	
	@Test
	public void sortsInGrammar(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{A vA; B vB; C vC; D vD; DS vDS; E vE; ES vES; {E \",\"}+ vES2; true;}"));
	}
	
	
    @Test @Ignore("can not know that `d` should be a D+ list without a type checker")
	public void enumeratorDs1Untyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `d` ]; L == [`d`];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorDs1Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | D X <- `d` ]; L == [ `d` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorDsUnyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `d d d` ]; L == [`d`, `d`, `d`];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorDsTyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | D X <- `d d d` ]; L == [`d`, `d`, `d`];}"));
	}
	
	@Test @Ignore("Can not know that `e` should be an E* or E+ list without a type-checker")
	public void enumeratorEs1Untyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `e` ]; L == [ `e` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorEs1Typed(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | E X <- `e` ]; L == [ `e` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorEsUntyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `e, e, e` ]; L == [`e`, `e`, `e`];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorEsTyped(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | E X <- `e, e, e` ]; L == [`e`, `e`, `e`];}"));
	}

	@Test
	@Ignore("Functionality subject to future/current change")
	public void EvarsTypedInsert3Empty(){
		prepare("import GrammarABCDE;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("` e, <{E \",\"}* Xs> ` := ({E \",\"}+) `e`;"));
	}

	@Test
	public void Pico1(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{t1 = (Program) `begin declare x: natural; x := 10 end`;true;}"));
	}
	
	@Test
	public void Pico2(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{Program P := (Program) `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	public void Pico3(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{(Program) `<Program P>` := (Program) `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico4(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{`begin <decls> <stats> end` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico5(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{`begin <Decls decls> <{Statement \";\"}* stats> end` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico6(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{Decls decls; {Statement \";\"}* stats; `begin <decls> <stats> end` := `begin declare x: natural; x := 10 end`;}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico7a(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{`begin <Decls decls> <{Statement \";\"}+ stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
				                          "(decls == `declare x: natural;`);}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico7b(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{`begin <Decls decls> <{Statement \";\"}+ stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
				                          "(decls == `declare x: natural;`) && (stats == ({Statement \";\"}+)`x := 1; x := 2`);}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico7c(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{`begin <Decls decls> <{Statement \";\"}* stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
				                          "(decls == `declare x: natural;`) && (stats == ({Statement \";\"}*)`x := 1; x := 2`);}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void Pico8(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{ bool B;" +
				                          "  if(`begin <Decls decls> <{Statement \";\"}* stats> end` := `begin declare x: natural; x := 1; x := 2 end`){" +
				                          "            B = (decls == `declare x: natural;`);" +
				                          "  } else" +
				                          "     B = false; " +
				                          "  B;" +
				                          "}"));
	}
	
	private String QmoduleM = "module M\n" +
	                         "import lang::pico::syntax::Main;\n" +
	                         "import ParseTree;\n" +
	                         "public Tree t1 = (Program) `begin declare x: natural; x := 10 end`;\n" +
	                         "public Tree t2 = (Decls) `declare x : natural;`;\n";
	
	@Test
	public void PicoQuoted0() {
		prepareModule("M", QmoduleM + "public bool match1() { return (Program) `<Program program>` := t1; }\n");
	}
	
	@Test
	public void PicoQuoted1(){
		prepareModule("M", QmoduleM + "public bool match1() { return (Program) `<Program program>` := t1; }\n");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match1();"));
	}
	
	@Test
	public void PicoQuoted2(){
		prepareModule("M", QmoduleM + "public bool match2() { return Program program := t1; }\n");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match2();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void PicoQuoted3(){
		prepareModule("M", QmoduleM + "public bool match3() { return (Program) `begin <Decls decls> <{Statement \";\"}* stats> end` := t1; }\n");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match3();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void PicoQuoted4(){
		prepareModule("M", QmoduleM + "public bool match4() { return `begin <Decls decls> <stats> end` := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match4();"));
	}
	
	@Test
	public void PicoQuoted5(){
		prepareModule("M", QmoduleM + "public bool match5() { return (Program) `begin <Decls decls> <{Statement \";\"}* stats> end` := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match5();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void PicoQuoted6(){
		prepareModule("M", QmoduleM + "public bool match6() { return (Program) `begin <Decls decls> <{Statement \";\"}* stats> end` := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match6();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void PicoQuoted7(){
		prepareModule("M", QmoduleM + "public bool match7() { return ` begin declare <{IdType \",\" }* decls>; <{Statement \";\"}* Stats> end ` := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match7();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void PicoQuoted8(){
		prepareModule("M", QmoduleM + "public bool match8() { return ` declare <{IdType \",\" }* decls>; ` := t2; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match8();"));
	}
	
	private String UQmoduleM = "module M\n" +
    "import lang::pico::syntax::Main;\n" +
    "import ParseTree;" +
    "public Tree t1 = begin declare x: natural; x := 10 end;\n";

	@Test(expected=StaticError.class) // Directly antiquoting without quotes not allowed.
	@Ignore("Functionality subject to future/current change")
	public void PicoUnQuoted1(){
		prepareModule("M", UQmoduleM + "public bool match1() { return <Program program> := t1; }\n");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match1();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void PicoUnQuoted2(){
		prepareModule("M", UQmoduleM + "public bool match2() { return Program program := t1; }\n");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match2();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted3(){
		prepareModule("M", UQmoduleM + "public bool match3() { return begin <decls> <stats> end := t1; }\n");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match3();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted4(){
		prepareModule("M", UQmoduleM + "public bool match4() { return begin <Decls decls> <stats> end := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match4();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted5(){
		prepareModule("M", UQmoduleM + "public bool match5() { return begin <decls> <{Statement \";\"}* stats> end := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match5();"));
	}
	
	@Test @Ignore
	public void PicoUnQuoted6(){
		prepareModule("M", UQmoduleM + "public bool match6() { return begin <Decls decls> <{Statement \";\"}* stats> end := t1; }");
		prepareMore("import ParseTree;");
		prepareMore("import M;");
		assertTrue(runTestInSameEvaluator("match6();"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatement1Untyped(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `a:=1` ]; L == [ `a:=1` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatement1Typed(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | Statement X <- `a:=1` ]; L == [ `a:=1` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatementsUntyped(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatementsTyped(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | Statement X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatementsConcretePattern1(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | `<\\PICO-ID X>:=1` <- `a:=1;b:=2;c:=1` ]; L == [ `a`, `c` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatementsConcretePattern2(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | /`b:=<EXP X>` <- `a:=1;b:=2;c:=3` ]; L == [ (EXP)`2` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatementsConcretePattern3(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [Id | /`<\\PICO-ID Id> : <TYPE Tp>` <- `x : natural, y : string` ]; L == [ `x`, `y` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void enumeratorPicoStatementsConcretePattern4(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = []; for(/`<\\PICO-ID Id> : <TYPE Tp>` <- `x : natural, y : string`){L += Id;} L == [ `x`, `y` ];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void forPicoStatementsTyped1(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | /Statement X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	public void forPicoStatementsTyped2(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
//		assertTrue(runTestInSameEvaluator("{L = [X | /Statement X <- `begin declare a : natural; a:=1;a:=2;a:=3 end` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
	}
	
	@Test
	@Ignore("Functionality subject to future/current change")
	public void forPicoStatementsTyped3(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{L = [X | /EXP X <- `begin declare a : natural; a:=1;b:=2;c:=3 end` ]; L == [(EXP)`1`, (EXP)`2`, (EXP)`3` ];}"));
	}
	
	@Test
	public void PicoStringDoesNotOverrideRascalString1(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{str s = \"abc\"; s == \"abc\";}"));
	}
	
	@Test
	public void PicoStringDoesNotOverrideRascalString2(){
		prepare("import lang::pico::syntax::Main;");
		prepareMore("import ParseTree;");
		assertTrue(runTestInSameEvaluator("{int n = 3; s = \"abc<n>\"; s == \"abc3\";}"));
	}
	
}
