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
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import junit.framework.TestCase;

// TODO: this class could use more tests
public abstract class BaseTestEquality extends TestCase {
    private IValueFactory vf;
    private TypeFactory tf = TypeFactory.getInstance();
    
	protected void setUp(IValueFactory factory) throws Exception {
		super.setUp();
		vf = factory;
	}
	
	public void testInteger() {
		assertTrue(vf.integer(0).isEqual(vf.integer(0)));
		assertFalse(vf.integer(0).isEqual(vf.integer(1)));
	}
	
	public void testDouble() {
		assertTrue(vf.real(0.0).isEqual(vf.real(0.0)));
		assertTrue(vf.real(1.0).isEqual(vf.real(1.00000)));
		assertFalse(vf.real(0.0).isEqual(vf.real(1.0)));
	}
	
	public void testString() {
		assertTrue(vf.string("").isEqual(vf.string("")));
		assertTrue(vf.string("a").isEqual(vf.string("a")));
		assertFalse(vf.string("a").isEqual(vf.string("b")));
	}
	
	public void testEmptyCollectionsAreVoid() {
	  assertTrue(vf.list(tf.integerType()).getElementType().isSubtypeOf(tf.voidType()));
	  assertTrue(vf.set(tf.integerType()).getElementType().isSubtypeOf(tf.voidType()));
	  assertTrue(vf.map(tf.integerType(),tf.integerType()).getKeyType().isSubtypeOf(tf.voidType()));
	  assertTrue(vf.map(tf.integerType(),tf.integerType()).getValueType().isSubtypeOf(tf.voidType()));
	  assertTrue(vf.relation(tf.tupleType(tf.integerType(),tf.integerType())).getElementType().isSubtypeOf(tf.voidType()));
	  
	  assertTrue(vf.listWriter(tf.integerType()).done().getElementType().isSubtypeOf(tf.voidType()));
    assertTrue(vf.setWriter(tf.integerType()).done().getElementType().isSubtypeOf(tf.voidType()));
    assertTrue(vf.mapWriter(tf.integerType(),tf.integerType()).done().getKeyType().isSubtypeOf(tf.voidType()));
    assertTrue(vf.mapWriter(tf.integerType(),tf.integerType()).done().getValueType().isSubtypeOf(tf.voidType()));
    assertTrue(vf.relationWriter(tf.tupleType(tf.integerType(),tf.integerType())).done().getElementType().isSubtypeOf(tf.voidType()));
	}
	
	public void testList() {
		assertTrue("element types are comparable", vf.list(tf.voidType()).isEqual(vf.list(tf.integerType()))); 
		assertTrue("empty lists are always equal", vf.list(tf.realType()).isEqual(vf.list(tf.integerType())));
		
		assertTrue(vf.list(vf.integer(1)).isEqual(vf.list(vf.integer(1))));
		assertFalse(vf.list(vf.integer(1)).isEqual(vf.list(vf.integer(0))));
		
		assertTrue(vf.list(vf.list(tf.voidType())).isEqual(vf.list(vf.list(tf.integerType()))));
		assertTrue(vf.list(vf.list(tf.realType())).isEqual(vf.list(vf.list(tf.integerType()))));
	}
	
	public void testSet() {
		assertTrue("element types are comparable", vf.set(tf.voidType()).isEqual(vf.set(tf.integerType()))); 
		assertTrue("empty sets are always equal", vf.set(tf.realType()).isEqual(vf.set(tf.integerType())));
		
		assertTrue(vf.set(vf.integer(1)).isEqual(vf.set(vf.integer(1))));
		assertFalse(vf.set(vf.integer(1)).isEqual(vf.set(vf.integer(0))));
		
		assertTrue(vf.set(vf.set(tf.voidType())).isEqual(vf.set(vf.set(tf.integerType()))));
		assertTrue(vf.set(vf.set(tf.realType())).isEqual(vf.set(vf.set(tf.integerType()))));
	}
	
	/**
	 * Documenting the current relationship between Node and Constructor in
	 * terms of equality and hash codes.
	 */
	public void testConstructorIsEqualToConstructor() {
		final INode n = vf.node("constructorComparableName", vf.integer(1), vf.integer(2));
		
		final TypeStore ts = new TypeStore();
		final Type adtType = tf.abstractDataType(ts, "adtTypeNameThatIsIgnored");
		final Type constructorType = tf.constructor(ts, adtType, "constructorComparableName", tf.integerType(), tf.integerType());
		
		final IConstructor c = vf.constructor(constructorType, vf.integer(1), vf.integer(2));

		// they are not the same
		assertFalse(n.equals(c));
		assertFalse(c.equals(n));
		/*
		 * TODO: what is the general contract between isEqual() and hashCode()?
		 */
		assertFalse(n.hashCode() == c.hashCode());
		
		// unidirectional: n -> c = false
		assertFalse(n.isEqual(c));		
		
		// unidirectional: c -> n = false
		assertFalse(c.isEqual(n));	
	}
}