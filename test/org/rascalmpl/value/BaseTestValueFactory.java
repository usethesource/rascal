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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.io.StandardTextReader;
import org.rascalmpl.value.io.StandardTextWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import junit.framework.TestCase;

public abstract class BaseTestValueFactory extends TestCase {
    private IValueFactory ff;
    private TypeFactory ft = TypeFactory.getInstance();
    private IValue[] integers;
	
	protected void setUp(IValueFactory factory) throws Exception {
		ff = factory;
		
		integers = new IValue[100];
		for (int i = 0; i < integers.length; i++) {
			integers[i] = ff.integer(i);
		}
	}

	public void testRelationNamedType() {
		try {
			Type type = ft.aliasType(new TypeStore(), "myType2", ft.relType(ft.integerType(), ft.integerType()));
			ISet r = ff.set(type.getElementType());
			
			if (!r.getType().isRelation()) {
				fail("relation does not have a relation type");
			}
		} catch (FactTypeUseException e) {
			fail("type error on the construction of a valid relation: " + e);
		}
	}

	public void testRealZeroDotFromString() {
		assertTrue(ff.real("0.").isEqual(ff.real("0")));
	}
	
	public void testZeroRealRepresentation() {
		IReal real = ff.real("0");
		
		assertTrue(real.toString().equals("0."));
	}
	
	
	
	public void testRelationTupleType() {
		ISet r = ff.relation(ft.tupleType(ft.integerType()));

		if (r.size() != 0) {
			fail("empty set is not empty");
		}

		if (!r.getType().isSubtypeOf(ft.relTypeFromTuple(ft.tupleType(ft.integerType())))) {
			fail("should be a rel of unary int tuples");
		}
	}

	public void testRelationWith() {
		ISet[] relations = new ISet[7];
		ITuple[] tuples = new ITuple[7];
		
		for (int i = 0; i < 7; i++) {
			tuples[i] = ff.tuple(ff.integer(i), ff.real(i));
		}

		try {
			relations[0] = ff.relation(tuples[0]);
			relations[1] = ff.relation(tuples[0], tuples[1]);
			relations[2] = ff.relation(tuples[0], tuples[1], tuples[2]);
			relations[3] = ff.relation(tuples[0], tuples[1], tuples[2],
					tuples[3]);
			relations[4] = ff.relation(tuples[0], tuples[1], tuples[2],
					tuples[3], tuples[4]);
			relations[5] = ff.relation(tuples[0], tuples[1], tuples[2],
					tuples[3], tuples[4], tuples[5]);
			relations[6] = ff.relation(tuples[0], tuples[1], tuples[2],
					tuples[3], tuples[4], tuples[5], tuples[6]);

			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < i; j++) {
					if (!relations[i].contains(tuples[j])) {
						fail("tuple creation is weird");
					}
				}
			}
		} catch (FactTypeUseException e) {
			System.err.println(e);
			fail("this should all be type correct");
		}
	}

	public void testSetNamedType() {
		ISet l;
		try {
			TypeStore typeStore = new TypeStore();
			l = ff.set(ff.integer(1));

			if (!l.getType().isSubtypeOf(ft.aliasType(typeStore, "mySet", ft.setType(ft.integerType())))) {
				fail("named types should be aliases");
			}

			if (!l.getElementType().isSubtypeOf(ft.integerType())) {
				fail("elements should be integers");
			}

			if (l.size() != 1) {
				fail("??");
			}
		} catch (FactTypeUseException e1) {
			fail("this was a correct type");
		}
	}

	public void testSetType() {
        ISet s = ff.set(ft.realType());
		
		if (s.size() != 0) {
			fail("empty set is not empty");
		}
		
		if (!s.getType().isSubtypeOf(ft.setType(ft.realType()))) {
			fail("should be a list of reals");
		}

		if (!s.getElementType().isSubtypeOf(ft.realType())) {
			fail("should be a list of reals");
		}
	}

	public void testSetWith() {
        ISet[] sets = new ISet[7];
		
		sets[0] = ff.set(integers[0]);
		sets[1] = ff.set(integers[0],integers[1]);
		sets[2] = ff.set(integers[0],integers[1],integers[2]);
		sets[3] = ff.set(integers[0],integers[1],integers[2],integers[3]);
		sets[4] = ff.set(integers[0],integers[1],integers[2],integers[3],integers[4]);
		sets[5] = ff.set(integers[0],integers[1],integers[2],integers[3],integers[4],integers[5]);
		sets[6] = ff.set(integers[0],integers[1],integers[2],integers[3],integers[4],integers[5],integers[6]);

		try {
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j <= i; j++) {
					if (!sets[i].contains(integers[j])) {
						fail("set creation is weird");
					}
				}
				for (int j = 8; j < 100; j++) {
					if (sets[i].contains(integers[j])) {
						fail("set creation contains weird values");
					}
				}
			}
		} catch (FactTypeUseException e) {
			System.err.println(e);
			fail("this should all be type correct");
		}
	}

	public void testListNamedType() {
		IList l;
		try {
			TypeStore ts = new TypeStore();
			l = ff.list(ff.integer(1));

			if (!l.getType().isSubtypeOf(ft.aliasType(ts, "myList", ft.listType(ft
					.integerType())))) {
				fail("named types should be aliases");
			}

			if (!l.getElementType().isSubtypeOf(ft.integerType())) {
				fail("elements should be integers");
			}

			if (l.length() != 1) {
				fail("???");
			}
		} catch (FactTypeUseException e1) {
			fail("this was a correct type");
		}
	}

	public void testListType() {
		IList l = ff.list(ft.realType());
		
		if (l.length() != 0) {
			fail("empty list is not empty");
		}

		if (!l.getElementType().isSubtypeOf(ft.realType())) {
			fail("should be a list of reals");
		}
	}

	public void testListWith() {
		IList[] lists = new IList[7];
		
		lists[0] = ff.list(integers[0]);
		lists[1] = ff.list(integers[0],integers[1]);
		lists[2] = ff.list(integers[0],integers[1],integers[2]);
		lists[3] = ff.list(integers[0],integers[1],integers[2],integers[3]);
		lists[4] = ff.list(integers[0],integers[1],integers[2],integers[3],integers[4]);
		lists[5] = ff.list(integers[0],integers[1],integers[2],integers[3],integers[4],integers[5]);
		lists[6] = ff.list(integers[0],integers[1],integers[2],integers[3],integers[4],integers[5],integers[6]);

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j <= i; j++) {
				if (lists[i].get(j) != integers[j]) {
					fail("list creation is weird");
				}
			}
		}
		
	}

	public void testTupleIValue() {
		ITuple[] tuples = new ITuple[7];
		
		tuples[0] = ff.tuple(integers[0]);
		tuples[1] = ff.tuple(integers[0],integers[1]);
		tuples[2] = ff.tuple(integers[0],integers[1],integers[2]);
		tuples[3] = ff.tuple(integers[0],integers[1],integers[2],integers[3]);
		tuples[4] = ff.tuple(integers[0],integers[1],integers[2],integers[3],integers[4]);
		tuples[5] = ff.tuple(integers[0],integers[1],integers[2],integers[3],integers[4],integers[5]);
		tuples[6] = ff.tuple(integers[0],integers[1],integers[2],integers[3],integers[4],integers[5],integers[6]);

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j <= i; j++) {
				if (tuples[i].get(j) != integers[j]) {
					fail("tuple creation is weird");
				}
			}
		}
	}

	public void testInteger() {
		assertTrue(ff.integer(42).toString().equals("42"));
	}

	public void testDubble() {
		assertTrue(ff.real(84.5).toString().equals("84.5"));
	}

	public void testString() {
		assertTrue(ff.string("hello").getValue().equals("hello"));
		assertTrue(ff.string(0x1F35D).getValue().equals("🍝"));
		assertTrue(ff.string(new int[] {0x1F35D,0x1F35D}).getValue().equals("🍝🍝"));
	}

//	public void testSourceLocation() {
//		ISourceLocation sl;
//		try {
//			sl = ff.sourceLocation(new URL("file:///dev/null"), 1, 2, 3, 4, 5, 6);
//			if (!sl.getURL().getPath().equals("/dev/null")) {
//				fail("source location creation is weird");
//			}
//			
//			if (sl.getStartOffset() != 1 || sl.getLength() != 2
//					|| sl.getStartColumn() != 5 || sl.getStartLine() != 3
//					|| sl.getEndLine() != 4 || sl.getEndColumn() != 6) {
//				fail("source range creation is weird");
//			}
//		} catch (MalformedURLException e) {
//			fail();
//		}
//		
//	}

	public void testToString() {
		// first we create a lot of values, and
		// then we check whether toString does the same
		// as StandardTextWriter
		ISetWriter extended;
		try {
			extended = createSomeValues();

			StandardTextWriter w = new StandardTextWriter();

			for (IValue o : extended.done()) {
				StringWriter out = new StringWriter();
				try {
					w.write(o, out);
					if(!out.toString().equals(o.toString())) {
						fail(out.toString() + " != " + o.toString());
					}
				} catch (IOException e) {
					fail(e.toString());
					e.printStackTrace();
				}
			}

		} catch (FactTypeUseException | MalformedURLException e1) {
			fail(e1.toString());
		}
    }
	
	public void testStandardReaderWriter() {
		StandardTextWriter w = new StandardTextWriter();
		StandardTextReader r = new StandardTextReader();
		
		try {
			for (IValue o : createSomeValues().done()) {
				StringWriter out = new StringWriter();
				w.write(o, out);
				StringReader in = new StringReader(out.toString());
				IValue read = r.read(ff, in);
				if (!o.isEqual(read)) {
					fail(o + " != " + read);
				}
			}
		} catch (IOException e) {
			fail();
		} 
	}

	private ISetWriter createSomeValues() throws FactTypeUseException, MalformedURLException {
		ISetWriter basicW = ff.setWriter(ft.valueType());
		
		// TODO add tests for locations and constructors again
		basicW.insert(ff.integer(0),
				ff.real(0.0),
//				ff.sourceLocation(new URL("file:///dev/null"), 0, 0, 0, 0, 0, 0),
				ff.bool(true),
				ff.bool(false),
				ff.node("hello"));
		
		ISet basic = basicW.done();
		ISetWriter extended = ff.setWriter(ft.valueType());
		
//		TypeStore ts = new TypeStore();
//		Type adt = ft.abstractDataType(ts, "E");
//		Type cons0 = ft.constructor(ts, adt, "cons");
//		Type cons1 = ft.constructor(ts, adt, "cons", ft.valueType(), "value");

		extended.insertAll(basic);
		for (IValue w : basic) {
			extended.insert(ff.list());
			extended.insert(ff.list(w));
			extended.insert(ff.set());
			extended.insert(ff.set(w));
			IMap map = ff.map(w.getType(), w.getType());
			extended.insert(map.put(w,w));
			ITuple tuple = ff.tuple(w,w);
			extended.insert(tuple);
			extended.insert(ff.relation(tuple, tuple));
			extended.insert(ff.node("hi", w));
//			extended.insert(ff.constructor(cons0));
//			extended.insert(ff.constructor(cons1, w));
		}
		return extended;
	}
}
