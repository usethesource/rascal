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

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeDeclarationException;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import junit.framework.TestCase;

public abstract class BaseTestAnnotations extends TestCase {
    private IValueFactory vf;
    private TypeFactory tf = TypeFactory.getInstance();
    private TypeStore ts = new TypeStore();
    private Type E;
    private Type N;
    
	protected void setUp(IValueFactory factory) throws Exception {
		super.setUp();
		vf = factory;
		E = tf.abstractDataType(ts, "E");
		N = tf.constructor(ts, E, "n", tf.integerType());
		ts.declareAnnotation(E, "x", tf.integerType());
	}
	
	public void testDeclarationOnNonAllowedType() {
		try {
			ts.declareAnnotation(tf.integerType(), "a", tf.integerType());
		}
		catch (FactTypeDeclarationException e) {
			// this should happen
		}
		try {
			ts.declareAnnotation(tf.realType(), "a", tf.integerType());
		}
		catch (FactTypeDeclarationException e) {
			// this should happen
		}
	}
	
	public void testDoubleDeclaration() {
		try {
			ts.declareAnnotation(E, "size", tf.integerType());
		}
		catch (FactTypeDeclarationException | FactTypeUseException e) {
			fail(e.toString());
		}

        try {
			ts.declareAnnotation(E, "size", tf.realType());
			fail("double declaration is not allowed");
		}
		catch (FactTypeDeclarationException e) {
			// this should happen
		}
	}
	
	public void testSetAnnotation() {
		IConstructor n = vf.constructor(N, vf.integer(0));
		ts.declareAnnotation(E, "size", tf.integerType());
		
		try {
			n.asAnnotatable().setAnnotation("size", vf.integer(0));
		}
		catch (FactTypeDeclarationException | FactTypeUseException e) {
			fail(e.toString());
		}
    }
	
	public void testGetAnnotation() {
		IConstructor n = vf.constructor(N, vf.integer(0));
		ts.declareAnnotation(E, "size", tf.integerType());
		
		try {
			if (n.asAnnotatable().getAnnotation("size") != null) {
				fail("annotation should be null");
			}
		} catch (FactTypeUseException e) {
			fail(e.toString());
		}
		
		IConstructor m = n.asAnnotatable().setAnnotation("size", vf.integer(1));
		IValue b = m.asAnnotatable().getAnnotation("size");
		if (!b.isEqual(vf.integer(1))) {
			fail();
		}
	}
	
	public void testImmutability() {
		IConstructor n = vf.constructor(N, vf.integer(0));
		ts.declareAnnotation(E, "size", tf.integerType());
		
		IConstructor m = n.asAnnotatable().setAnnotation("size", vf.integer(1));
		
		if (m == n) {
			fail("annotation setting should change object identity");
		}
		
		assertTrue(m.isEqual(n));
	}
	
	public void testDeclaresAnnotation() {
		IConstructor n = vf.constructor(N,  vf.integer(0));
		ts.declareAnnotation(E, "size", tf.integerType());
		
		if (!n.declaresAnnotation(ts, "size")) {
			fail();
		}
		
		if (n.declaresAnnotation(ts, "size2")) {
			fail();
		}
	}
	
	public void testEqualityNode() {
		final INode n = vf.node("hello");
		final INode na = n.asAnnotatable().setAnnotation("audience", vf.string("world"));		
	
		assertEqualityOfValueWithAndWithoutAnnotations(n, na);
	}
	
	public void testEqualityConstructor() {
		final IConstructor n = vf.constructor(N, vf.integer(1));
		final IConstructor na = n.asAnnotatable().setAnnotation("x", vf.integer(1));
			
		assertEqualityOfValueWithAndWithoutAnnotations(n, na);
	}
	
	public void assertEqualityOfValueWithAndWithoutAnnotations(IValue n, IValue na) {
				
		assertIsEqualButNotEquals(n, na);
		
		assertIsEqualButNotEquals(vf.set(n), vf.set(na));
		assertIsEqualButNotEquals(vf.set(vf.set(n)), vf.set(vf.set(na)));
				
		assertIsEqualButNotEquals(vf.list(n), vf.list(na));
		assertIsEqualButNotEquals(vf.list(vf.list(n)), vf.list(vf.list(na)));
					
		// check: with keys
		{
			final IMap mapN = createMap(n, vf.integer(1));
			final IMap mapNA = createMap(na, vf.integer(1));
			final IMap mapMapN = createMap(mapN, vf.integer(1));
			final IMap mapMapNA = createMap(mapNA, vf.integer(1));

			assertIsEqualButNotEquals(mapN, mapNA);
			assertIsEqualButNotEquals(mapMapN, mapMapNA);
		}
		
		// check: with values
		{
			final IMap mapN = createMap(vf.integer(1), n);
			final IMap mapNA = createMap(vf.integer(1), na);
			final IMap mapMapN = createMap(vf.integer(1), mapN);
			final IMap mapMapNA = createMap(vf.integer(1), mapNA);

			assertIsEqualButNotEquals(mapN, mapNA);
			assertIsEqualButNotEquals(mapMapN, mapMapNA);
		}

		assertIsEqualButNotEquals(vf.node("nestingInNode", n), vf.node("nestingInNode", na));
		
		final TypeStore ts = new TypeStore();
		final Type adtType = tf.abstractDataType(ts, "adtTypeNameThatIsIgnored");
		final Type constructorType = tf.constructor(ts, adtType, "nestingInConstructor", tf.valueType());

		assertIsEqualButNotEquals(vf.constructor(constructorType, n), vf.constructor(constructorType, na));
	}

	/**
	 * Create a @IMap from a variable argument list.
	 * 
	 * @param keyValuePairs
	 *            a sequence of alternating keys and values
	 * @return an new @IMap instance
	 */
	public IMap createMap(IValue... keyValuePairs) {
		assert (keyValuePairs.length % 2 == 0);

		IMapWriter w = vf.mapWriter();

		for (int i = 0; i < keyValuePairs.length / 2; i++) {
			w.put(keyValuePairs[i], keyValuePairs[i+1]);
		}
		
		return w.done();
	}
	
	/**
	 * Asserting the current implementation w.r.t. hash codes and different
	 * equalities. Note, that this does not reflect the envisioned design that
	 * we are working towards (= structurally where annotations contribute to
	 * equality and hash code).
	 * 
	 * @param a
	 *            an object that does not use annotations
	 * @param b
	 *            a structurally equal object to {@literal a} with annotations
	 */
	public void assertIsEqualButNotEquals(IValue a, IValue b) {
		assertFalse(a.equals(b));
		
		assertTrue(a.isEqual(b));		
		assertTrue(a.hashCode() == b.hashCode());		
	}
	

	public void testNoKeywordParametersOnAnnotatedNode() {
	   try {
	     vf.node("hallo")
	     .asAnnotatable()
	     .setAnnotation("a", vf.integer(1))
	     .asWithKeywordParameters()
	     .setParameter("b", vf.integer(2));
	   }
	   catch (UnsupportedOperationException e) {
	     assertTrue(true);
	   }
	}
	
	public void testAnnotationsOnNodeWithKeywordParameters() {
    try {
      vf.node("hallo")
      .asWithKeywordParameters()
      .setParameter("b", vf.integer(2))
      .asAnnotatable()
      .setAnnotation("a", vf.integer(1));
    }
    catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
 }
	
	public void testNodeAnnotation() {
		ts.declareAnnotation(tf.nodeType(), "foo", tf.boolType());
		INode n = vf.node("hello");
		INode na = n.asAnnotatable().setAnnotation("foo", vf.bool(true));
		
		assertTrue(na.asAnnotatable().getAnnotation("foo").getType().isBool());
		
		// annotations on node type should be propagated
		assertTrue(ts.getAnnotationType(tf.nodeType(), "foo").isBool());
		assertTrue(ts.getAnnotations(E).containsKey("foo"));
		
		// annotations sets should not collapse into one big set
		ts.declareAnnotation(E, "a", tf.integerType());
		ts.declareAnnotation(N, "b", tf.boolType());
		assertTrue(!ts.getAnnotations(E).equals(ts.getAnnotations(N)));
	}
	
	
	public void testNodeKeywordParameter() {
    INode n = vf.node("hello");
    INode na = n.asWithKeywordParameters().setParameter("foo", vf.bool(true));
    
    assertTrue(na.asWithKeywordParameters().getParameter("foo").getType().isBool());
    assertTrue(na.asWithKeywordParameters().getParameter("foo").equals(vf.bool(true)));
  }
	
	public void testConstructorKeywordParameter() {
	  TypeStore ts = new TypeStore();
	  Type adt = tf.abstractDataType(ts, "adt");
	  Type cons = tf.constructorFromTuple(ts, adt, "cons", tf.tupleEmpty());

	  IConstructor n1 = vf.constructor(cons);
	  
	  // overrides work
	  IConstructor n2 = n1.asWithKeywordParameters().setParameter("foo", vf.bool(false));
	  assertTrue(n2.asWithKeywordParameters().getParameter("foo").isEqual(vf.bool(false)));

	  // keywordparameters work on equality:
	  assertFalse(n1.isEqual(n2));
  }
	
}
