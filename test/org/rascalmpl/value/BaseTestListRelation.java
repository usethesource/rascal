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

import java.util.Iterator;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

import junit.framework.TestCase;

public abstract class BaseTestListRelation extends TestCase {
    private IValueFactory vf;
	private TypeFactory tf;
	private IValue[] integers;
	private ITuple[] integerTuples;
	private IList listOfIntegers;
	private IList integerListRelation;
	private IValue[] doubles;
	private IList listOfDoubles;
	private IList doubleListRelation;
	private ITuple[] doubleTuples;
    
	protected void setUp(IValueFactory factory) throws Exception {
		super.setUp();
		vf = factory;
		tf = TypeFactory.getInstance();
		
		integers = new IValue[5];
		IListWriter lw = vf.listWriter(tf.integerType());
		
		for (int i = 0; i < integers.length; i++) {
			IValue iv = vf.integer(i);
			integers[i] = iv;
			lw.insert(iv);
		}
		listOfIntegers = lw.done();
		
		doubles = new IValue[10];
		IListWriter lw2 = vf.listWriter(tf.realType());
		
		for (int i = 0; i < doubles.length; i++) {
			IValue iv = vf.real(i);
			doubles[i] = iv;
			lw2.insert(iv);
		}
		listOfDoubles = lw2.done();
		IListWriter rw = vf.listRelationWriter(tf.tupleType(tf.integerType(), tf.integerType()));
		integerTuples = new ITuple[integers.length * integers.length];
		
		for (int i = 0; i < integers.length; i++) {
			for (int j = 0; j < integers.length; j++) {
				ITuple t = vf.tuple(integers[i], integers[j]);
				integerTuples[i * integers.length + j] = t;
				rw.insert(t);
			}
		}
		integerListRelation = rw.done();
		
		IListWriter rw2 = vf.listRelationWriter(tf.tupleType(tf.realType(), tf.realType()));
		doubleTuples = new ITuple[doubles.length * doubles.length];
		
		for (int i = 0; i < doubles.length; i++) {
			for (int j = 0; j < doubles.length; j++) {
				ITuple t = vf.tuple(doubles[i], doubles[j]);
				doubleTuples[i * doubles.length + j] = t;
				rw2.insert(t);
			}
		}
		doubleListRelation = rw2.done();
	}

	public void testIsEmpty() {
		if (integerListRelation.isEmpty()) {
			fail("integerRelation is not empty");
		}
		
		if (!vf.listRelation(tf.tupleType(tf.integerType())).isEmpty()) {
			fail("this relation should be empty");
		}
		
		IList emptyRel = vf.listRelation();
		if (!emptyRel.isEmpty()) {
			fail("empty relation is not empty?");
		}
		if (!emptyRel.getType().isListRelation()) {
			fail("empty relation should have relation type");
		}
		
		
	}

	public void testSize() {
		if (integerListRelation.length() != integerTuples.length) {
			fail("relation size is not correct");
		}
	}

	public void testArity() {
		if (integerListRelation.asRelation().arity() != 2) {
			fail("arity should be 2");
		}
	}

	public void testProductIRelation() {
		IList prod = integerListRelation.product(integerListRelation);
		
		if (prod.asRelation().arity() != 2 ) {
			fail("arity of product should be 2");
		}
		
		if (prod.length() != integerListRelation.length() * integerListRelation.length()) {
			fail("size of product should be square of size of integerRelation");
		}
	}

	public void testProductIList() {
		IList prod = integerListRelation.product(listOfIntegers);
		
		if (prod.asRelation().arity() != 2) {
			fail("arity of product should be 2");
		}
		
		if (prod.length() != integerListRelation.length() * listOfIntegers.length()) {
			fail("size of product should be square of size of integerRelation");
		}
	}

	public void testClosure() {
		try {
			if (!integerListRelation.asRelation().closure().isEqual(integerListRelation)) {
				fail("closure adds extra tuples?");
			}
		} catch (FactTypeUseException e) {
			fail("integerRelation is reflexive, so why an error?");
		}
		
		try {
			ITuple t1 = vf.tuple(integers[0], integers[1]);
			IList rel = vf.listRelation(t1);
			
			rel.asRelation().closure();
		}
		catch (FactTypeUseException e) {
			fail("reflexivity with subtyping is allowed");
		}
		
		try {
			ITuple t1 = vf.tuple(integers[0], integers[1]);
			ITuple t2 = vf.tuple(integers[1], integers[2]);
			ITuple t3 = vf.tuple(integers[2], integers[3]);
			ITuple t4 = vf.tuple(integers[0], integers[2]);
			ITuple t5 = vf.tuple(integers[1], integers[3]);
			ITuple t6 = vf.tuple(integers[0], integers[3]);
			
			IList test = vf.listRelation(t1, t2, t3);
			IList closed = test.asRelation().closure();
			
			if (closed.asRelation().arity() != test.asRelation().arity()) {
				fail("closure should produce relations of same arity");
			}
			
			if (closed.length() != 6) {
				fail("closure contains too few elements");
			}
			
			if (!closed.intersect(test).isEqual(test)) {
				fail("closure should contain all original elements");
			}
			
			if (!closed.contains(t4) || !closed.contains(t5) || !closed.contains(t6)) {
				fail("closure does not contain required elements");
			}
		
		} catch (FactTypeUseException e) {
			fail("this should all be type correct");
		}
	}

	public void testCompose() {
		try {
			IList comp = integerListRelation.asRelation().compose(integerListRelation.asRelation());
			
			if (comp.asRelation().arity() != integerListRelation.asRelation().arity() * 2 - 2) {
				fail("composition is a product with the last column of the first relation and the first column of the last relation removed");
			}
			
			if (comp.length() != integerListRelation.length() * integers.length) {
				fail("number of expected tuples is off");
			}
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
		
		try {
			ITuple t1 = vf.tuple(integers[0], doubles[0]);
			ITuple t2 = vf.tuple(integers[1], doubles[1]);
			ITuple t3 = vf.tuple(integers[2], doubles[2]);
			IList rel1 = vf.listRelation(t1, t2, t3);

			ITuple t4 = vf.tuple(doubles[0], integers[0]);
			ITuple t5 = vf.tuple(doubles[1], integers[1]);
			ITuple t6 = vf.tuple(doubles[2], integers[2]);
			IList rel2 = vf.listRelation(t4, t5, t6);
			
			ITuple t7 = vf.tuple(integers[0], integers[0]);
			ITuple t8 = vf.tuple(integers[1], integers[1]);
			ITuple t9 = vf.tuple(integers[2], integers[2]);
			IList rel3 = vf.listRelation(t7, t8, t9);
			
			try {
			  vf.listRelation(vf.tuple(doubles[0],doubles[0])).asRelation().compose(rel1.asRelation());
			  fail("relations should not be composable");
			}
			catch (FactTypeUseException e) {
				// this should happen
			}
			
			IList comp = rel1.asRelation().compose(rel2.asRelation());
			
			if (!comp.isEqual(rel3)) {
				fail("composition does not produce expected result");
			}
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
	}

	public void testContains() {
		try {
			for (ITuple t : integerTuples) {
				if (!integerListRelation.contains(t)) {
					fail("contains returns false instead of true");
				}
			}
		} catch (FactTypeUseException e) {
			fail("this should be type correct");
		}
	}

	public void testInsert() {
		try {
//			IList rel = integerListRelation.insert(vf.tuple(vf.integer(0),vf.integer(0)));
//			
//			if (!rel.isEqual(integerListRelation)) {
//				fail("insert into a relation of an existing tuple should not change the relation");
//			}
			
			IListWriter relw3 = vf.listRelationWriter(tf.tupleType(tf.integerType(), tf.integerType()));
			relw3.insertAll(integerListRelation);
			IList rel3 = relw3.done();
			 
			final ITuple tuple = vf.tuple(vf.integer(100), vf.integer(100));
			IList rel4 = rel3.insert(tuple);
			
			if (rel4.length() != integerListRelation.length() + 1) {
				fail("insert failed");
			}
			
			if (!rel4.contains(tuple)) {
				fail("insert failed");
			}
			
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
	}

	public void testIntersectIRelation() {
		IList empty1 = vf.listRelation(tf.tupleType(tf.integerType()));
		IList empty2 = vf.listRelation(tf.tupleType(tf.realType()));
		
		try {
			final IList intersection = empty1.intersect(empty2);
			if (!intersection.isEmpty()) {
				fail("empty intersection failed");
			}
			
			Type type = intersection.getType();
			if (!type.getFieldType(0).isBottom()) {
				fail("intersection should produce lub types");
			}
		} catch (FactTypeUseException e) {
		    fail("intersecting types which have a lub should be possible");
		}
		
		try {
			if (!integerListRelation.intersect(doubleListRelation).isEmpty()) {
				fail("non-intersecting relations should produce empty intersections");
			}

			IList oneTwoThree = vf.listRelation(integerTuples[0],
					integerTuples[1], integerTuples[2]);
			IList threeFourFive = vf.listRelation(integerTuples[2],
					integerTuples[3], integerTuples[4]);
			IList result = vf.listRelation(integerTuples[2]);

			if (!oneTwoThree.intersect(threeFourFive).isEqual(result)) {
				fail("intersection failed");
			}
			if (!threeFourFive.intersect(oneTwoThree).isEqual(result)) {
				fail("intersection should be commutative");
			}
			
			if (!oneTwoThree.intersect(vf.listRelation(tf.tupleType(tf.integerType(),tf.integerType()))).isEmpty()) {
				fail("intersection with empty set should produce empty");
			}

		} catch (FactTypeUseException e) {
			fail("the above should all be type safe");
		} 
	}

	public void testIntersectIList() {
		IList empty1 = vf.listRelation(tf.tupleType(tf.integerType()));
		IList empty2 = vf.list(tf.tupleType(tf.realType()));
		
		try {
			final IList intersection = empty1.intersect(empty2);
			if (!intersection.isEmpty()) {
				fail("empty intersection failed");
			}
			
			Type type = intersection.getType();
			if (!type.getFieldType(0).isBottom()) {
				fail("empty intersection should produce void type");
			}
		} catch (FactTypeUseException e) {
		    fail("intersecting types which have a lub should be possible");
		}
		
		try {
			if (!integerListRelation.intersect(doubleListRelation).isEmpty()) {
				fail("non-intersecting relations should produce empty intersections");
			}

			IList oneTwoThree = vf.listRelation(integerTuples[0],
					integerTuples[1], integerTuples[2]);
			IList threeFourFive = vf.list(integerTuples[2],
					integerTuples[3], integerTuples[4]);
			IList result = vf.listRelation(integerTuples[2]);

			if (!oneTwoThree.intersect(threeFourFive).isEqual(result)) {
				fail("intersection failed");
			}
			if (!threeFourFive.intersect(oneTwoThree).isEqual(result)) {
				fail("intersection should be commutative");
			}
			
			if (!oneTwoThree.intersect(vf.listRelation(tf.tupleType(tf.integerType(),tf.integerType()))).isEmpty()) {
				fail("intersection with empty list should produce empty");
			}

		} catch (FactTypeUseException e) {
			fail("the above should all be type safe");
		} 
	}

	public void testConcatIListRelation() {
		IList empty1 = vf.listRelation(tf.tupleType(tf.integerType()));
		IList empty2 = vf.listRelation(tf.tupleType(tf.realType()));
		
		try {
			final IList concat = (IList) empty1.concat(empty2);
			if (!concat.isEmpty()) {
				fail("empty concat failed");
			}
			
			Type type = concat.getType();
			if (!type.getFieldType(0).isBottom()) {
				fail("concat should produce void type");
			}
		} catch (FactTypeUseException e) {
		    fail("concat types which have a lub should be possible");
		}
		
		try {
			if (integerListRelation.concat(doubleListRelation).length() != integerListRelation.length() + doubleListRelation.length())  {
				fail("non-intersecting non-intersectiopn relations should produce relation that is the sum of the sizes");
			}

			IList oneTwoThree = vf.listRelation(integerTuples[0],
					integerTuples[1], integerTuples[2]);
			IList threeFourFive = vf.listRelation(integerTuples[3], integerTuples[4]);
			IList result1 = vf.listRelation(integerTuples[0],
					integerTuples[1], integerTuples[2], integerTuples[3], integerTuples[4]);
			IList result2 = vf.listRelation(integerTuples[3],
					integerTuples[4], integerTuples[0], integerTuples[1], integerTuples[2]);

			if (!oneTwoThree.concat(threeFourFive).isEqual(result1)) {
				fail("concat 1 failed");
			}
			if (!threeFourFive.concat(oneTwoThree).isEqual(result2)) {
				fail("concat 2 failed");
			}
			
			if (!oneTwoThree.concat(vf.listRelation(tf.tupleType(tf.integerType(),tf.integerType()))).isEqual(oneTwoThree)) {
				fail("concat with empty set should produce same set");
			}

		} catch (FactTypeUseException e) {
			fail("the above should all be type safe");
		} 
	}

	public void testIterator() {
		try {
			Iterator<IValue> it = integerListRelation.iterator();

			int i;
			for (i = 0; it.hasNext(); i++) {
				ITuple t = (ITuple) it.next();

				if (!integerListRelation.contains(t)) {
					fail("iterator produces strange elements?");
				}
			}
			
			if (i != integerListRelation.length()) {
				fail("iterator skipped elements");
			}
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
	}

	public void testCarrier() {
		IList carrier = integerListRelation.asRelation().carrier();
		
		if (!carrier.isEqual(listOfIntegers)) {
			fail("carrier should be equal to this set");
		}
	
		try {
			ITuple t1 = vf.tuple(integers[0], doubles[0]);
			ITuple t2 = vf.tuple(integers[1], doubles[1]);
			ITuple t3 = vf.tuple(integers[2], doubles[2]);
			IList rel1 = vf.listRelation(t1, t2, t3);
			
			IList carrier1 = rel1.asRelation().carrier();
			
			if (carrier1.getElementType() != tf.numberType()) {
				fail("expected number type on carrier");
			}
			
			if (carrier1.length() != 6) {
				fail("carrier does not contain all elements");
			}
			
			if (carrier1.intersect(listOfIntegers).length() != 3) {
				fail("integers should be in there still");
			}
			
			if (carrier1.intersect(listOfDoubles).length() != 3) {
				fail("doubles should be in there still");
			}
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
		
	}
}
