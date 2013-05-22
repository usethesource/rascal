/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.test.library;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.rascalmpl.test.infrastructure.TestFramework;

public class ListRelationsTests extends TestFramework {
	// Tests related to the correctness of the dynamic types of list relations produced by the library functions;
	// incorrect dynamic types make pattern matching fail;

	@Test
	public void testDynamicTypes() {
		prepare("import List;");
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[int, int] _ := slice(lr, 1, 2); }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[int, int] _ := lr - <\"1\",\"1\">; }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value a, value b] lr = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[int, int] _ := lr - [<\"1\",\"1\">] && (lr - [<\"1\",\"1\">]).a == [2,3] && (lr - [<\"1\",\"1\">]).b == [2,3]; }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[int, int] _ := delete(lr, 0); }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[int, int] _ := drop(1, lr); }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<1,1>,<2,2>,<\"3\",\"3\">]; lrel[int, int] _ := head(lr, 2); }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<1,1>,<2,2>,<\"3\",\"3\">]; lrel[int, int] _ := prefix(lr); }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[int, int] _ := tail(lr); }"));
		assertTrue(runTestInSameEvaluator("{ lrel[value, value] lr = [<1,1>,<2,2>,<\"3\",\"3\">]; lrel[int, int] _ := take(2, lr); }"));
		
		assertTrue(runTestInSameEvaluator("{ [tuple[str,str] _, *tuple[int,int] _] := [<\"1\",\"1\">,<2,2>,<3,3>]; }"));
		
		assertTrue(runTestInSameEvaluator("{ lrel[value a, value b] lr1 = [<\"1\",\"1\">,<2,2>,<3,3>]; lrel[value a, value b] lr2 = [<2,2>,<3,3>]; lrel[int, int] _ := lr1 & lr2 && (lr1 & lr2).a == [2,3] && (lr2 & lr1).b == [2,3]; }"));
	}

}
