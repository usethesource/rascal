/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

package org.rascalmpl.value;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	// TODO: this test suite tests the basic functionality of sets, relations and lists;
	// it also checks the functionality of the type factory and the computation of 
	// the least upperbound of types and the isSubtypeOf method. It needs more tests
	// for named types and the way they are checked and produced by the implementations
	// of IRelation, ISet and IList.
	
	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.imp.pdb");
	
		suite.addTestSuite(TestType.class);
		suite.addTestSuite(TestTypeFactory.class);
		suite.addTestSuite(TestIO.class);
		suite.addTestSuite(TestBinaryIO.class);

		// addReferenceTests(suite); // msteindorfer: broken, thus ignored
		addFastTests(suite);
		addPersistentTests(suite);
		
		return suite;
	}

	private static void addReferenceTests(TestSuite suite) {
		suite.addTestSuite(org.rascalmpl.value.reference.TestAnnotations.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestBasicValues.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestEquality.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestList.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestListRelation.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestMap.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestRandomValues.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestRelation.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestSet.class);
		suite.addTestSuite(org.rascalmpl.value.reference.TestValueFactory.class);
	}

	private static void addFastTests(TestSuite suite) {
		suite.addTestSuite(org.rascalmpl.value.fast.TestAnnotations.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestBasicValues.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestEquality.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestList.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestListRelation.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestMap.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestRandomValues.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestRelation.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestSet.class);
		suite.addTestSuite(org.rascalmpl.value.fast.TestValueFactory.class);
	}
	
	private static void addPersistentTests(TestSuite suite) {
		suite.addTestSuite(org.rascalmpl.value.persistent.TestAnnotations.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestBasicValues.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestEquality.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestList.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestListRelation.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestMap.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestRandomValues.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestRelation.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestSet.class);
		suite.addTestSuite(org.rascalmpl.value.persistent.TestValueFactory.class);
	}
	
}
