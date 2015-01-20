/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.test.demo;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class AllDemoTests extends TestFramework {
	
	@Ignore @Test 
	public void AbstractPicoAssembly(){
		assertTrue(runRascalTests("import demo::AbstractPico::Assembly;"));
	}

	@Ignore @Test
	public void AbstractPicoCommonSubexpression() {
		assertTrue(runRascalTests("import demo::AbstractPico::CommonSubexpression;"));
	}
	
	@Ignore @Test
	public void AbstractPicoConstantPropagation() {
		assertTrue(runRascalTests("import demo::AbstractPico::ConstantPropagation;"));
	}
	
	@Ignore @Test
	public void AbstractPicoControlflow() {
		assertTrue(runRascalTests("import demo::AbstractPico::Controlflow;"));
	}
	
	@Ignore @Test
	public void AbstractPicoEval() {
		assertTrue(runRascalTests("import demo::AbstractPico::Eval;"));
	}

	@Ignore @Test
	public void AbstractPicoPrograms() {
		assertTrue(runRascalTests("import demo::AbstractPico::Programs;"));
	}

	@Ignore @Test
	public void AbstractPicoTypecheck() {
		assertTrue(runRascalTests("import demo::AbstractPico::Typecheck;"));
	}

	@Ignore @Test
	public void AbstractPicoUninit() {
		assertTrue(runRascalTests("import demo::AbstractPico::Uninit;"));
	}

	@Ignore @Test
	public void AbstractPicoUseDef() {
		assertTrue(runRascalTests("import demo::AbstractPico::UseDef;"));
	}

	@Test
	public void Ackermann() {
		assertTrue(runRascalTests("import demo::basic::Ackermann;"));
	}

	@Test
	public void Bubble() {
		assertTrue(runRascalTests("import demo::basic::BubbleTest;"));
	}

	@Test
	public void Calls() {
		assertTrue(runRascalTests("import demo::common::Calls;"));
	}
	
	@Test
	public void ColoredTrees() {
		assertTrue(runRascalTests("import demo::common::ColoredTrees;"));
	}

	@Ignore @Test
	public void ConcretePicoEval() {
		assertTrue(runRascalTests("import demo::ConcretePico::Eval;"));
	}
	
	@Ignore @Test
	public void ConcretePicoTypecheck() {
		assertTrue(runRascalTests("import demo::ConcretePico::Typecheck;"));
	}

	@Test
	public void Cycles() {
		assertTrue(runRascalTests("import demo::common::Cycles;"));
	}
	
	@Test
	public void Dominators() {
		assertTrue(runRascalTests("import demo::Dominators;"));
	}
	
	@Test
	public void Factorial() {
		assertTrue(runRascalTests("import demo::basic::Factorial;"));
	}
	
	@Test
	public void Hello() {
		assertTrue(runRascalTests("import demo::basic::Hello;"));
	}
	
	@Test
	public void Lift() {
		assertTrue(runRascalTests("import  demo::common::Lift;"));
	}
	
	@Test
	public void McCabe() {
		assertTrue(runRascalTests("import   demo::McCabe;"));
	}

	@Ignore @Test
	public void ModelTransformationsBook2Publication() {
		assertTrue(runRascalTests("import  experiments::ModelTransformations::Book2Publication;"));
	}

	@Ignore @Test
	public void ModelTransformationsFamilies2Persons() {
		assertTrue(runRascalTests("import experiments::ModelTransformations::Families2Persons;"));
	}
	
	@Ignore @Test
	public void ModelTransformationsTree2List() {
		assertTrue(runRascalTests("import experiments::ModelTransformations::Tree2List;"));
	}

	@Test
	public void Queens() {
		assertTrue(runRascalTests("import demo::Queens;"));
	}
	
	@Test
	public void ReachingDefs() {
		assertTrue(runRascalTests("import demo::ReachingDefs;"));
	}

	@Test
	public void Slicing() {
		assertTrue(runRascalTests("import demo::Slicing;"));
	}
	
	@Test
	public void StringTemplate() {
		assertTrue(runRascalTests("import demo::common::StringTemplateTest;"));
	}
	
	@Test
	public void Squares() {
		assertTrue(runRascalTests("import demo::basic::Squares;"));
	}
	
	@Test
	public void Uninit() {
		assertTrue(runRascalTests("import demo::Uninit;"));
	}
	
	@Test
	public void WordCount() {
		assertTrue(runRascalTests("import demo::common::WordCount::WordCount;"));
	}
	
	@Test
	public void WordReplacement() {
		assertTrue(runRascalTests("import demo::common::WordReplacement;"));
	}

}
