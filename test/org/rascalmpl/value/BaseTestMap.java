/*******************************************************************************
 * Copyright (c) 2012 Centrum Wiskunde en Informatica (CWI)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Anya Helene Bagge (University of Bergen) - implementation
 *    Arnold Lankamp - base implementation (from TestBinaryIO.java)
 *******************************************************************************/
package org.rascalmpl.value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.BinaryReader;
import org.rascalmpl.value.io.binary.BinaryWriter;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import junit.framework.TestCase;

/**
 * @author Anya Helene Bagge
 */
public abstract class BaseTestMap extends TestCase {
	protected final TypeStore ts = new TypeStore();
	protected final TypeFactory tf = TypeFactory.getInstance();
	private final Type fromToMapType = tf.mapType(tf.stringType(), "from", tf.stringType(), "to");
	private final Type fromToValueMapType = tf.mapType(tf.valueType(), "from", tf.valueType(), "to");
	private final Type keyValueMapType = tf.mapType(tf.stringType(), "key", tf.stringType(), "value");
	private final Type unlabeledMapType = tf.mapType(tf.stringType(), tf.stringType());
	enum Kind { BINARY };
	protected IValueFactory vf;
	private Type a;
	private Type b;
	private TestValue[] testValues;
	private IMap[] testMaps;
	private StringPair[] keyValues;
	
	protected void setUp(IValueFactory factory) throws Exception {
		vf = factory;
		a = tf.abstractDataType(ts, "A");
		b = tf.abstractDataType(ts, "B");
		testValues = new TestValue[]{
				new TestValue(this, "Bergen", "Amsterdam", "from", "to"),
				new TestValue(this, "New York", "London", null, null),
				new TestValue(this, "Banana", "Fruit", "key", "value"),
		};
		
		testMaps = new IMap[] {
				vf.map(fromToMapType),
				vf.map(keyValueMapType),
				vf.map(unlabeledMapType),
				vf.map(fromToMapType).put(vf.string("Bergen"), vf.string("Amsterdam")),
				vf.map(fromToValueMapType).put(vf.string("Bergen"), vf.string("Amsterdam")).put(vf.string("Mango"), vf.string("Yummy")),
				vf.map(fromToMapType).put(vf.string("Bergen"), vf.string("Amsterdam")).put(vf.string("Amsterdam"), vf.string("Frankfurt")),
				vf.map(fromToMapType).put(vf.string("Bergen"), vf.string("Amsterdam")).put(vf.string("Amsterdam"), vf.string("Frankfurt")).put(vf.string("Frankfurt"), vf.string("Moscow")),
				vf.map(keyValueMapType).put(vf.string("Bergen"), vf.string("Rainy")).put(vf.string("Helsinki"), vf.string("Cold")),
				vf.map(unlabeledMapType).put(vf.string("Mango"), vf.string("Sweet")).put(vf.string("Banana"), vf.string("Yummy")),
		};
		
		String[] strings = new String[] { "Bergen", "Amsterdam", "Frankfurt", "Helsinki", "Moscow", "Rainy", "Cold", "Mango", "Banana", "Sweet", "Yummy" };
		List<String> list1 = Arrays.asList(strings);
		List<String> list2 = Arrays.asList(strings);
		Collections.shuffle(list1);
		Collections.shuffle(list2);
		keyValues = new StringPair[strings.length];
		for(int i = 0; i < strings.length; i++) {
			keyValues[i] = new StringPair(vf.string(list1.get(i)), vf.string(list2.get(i)));
		}
	}

	public void testNoLabels() {
		// make a non-labeled map type, and the labels should be null
		Type type = tf.mapType(a, b);

		assertNull(type.getKeyLabel());
		assertNull(type.getValueLabel());
	}

	public void testLabels() {
		// make a labeled map type, and the labels should match
		Type type = tf.mapType(a, "apple", b, "banana");

		assertEquals("apple", type.getKeyLabel());
		assertEquals("banana", type.getValueLabel());
	}

	public void testTwoLabels1() {
		// make two map types with same key/value types but different labels,
		// and the labels should be kept distinct
		Type type1 = tf.mapType(a, "apple", b, "banana");
		Type type2 = tf.mapType(a, "orange", b, "mango");
		Type type3 = tf.mapType(a, b);

		assertEquals("apple", type1.getKeyLabel());
		assertEquals("banana", type1.getValueLabel());
		assertEquals("orange", type2.getKeyLabel());
		assertEquals("mango", type2.getValueLabel());
		assertNull(type3.getKeyLabel());
		assertNull(type3.getValueLabel());
	}

	public void testTwoLabels2() {
		Type type1 = tf.mapType(a, "apple", b, "banana");
		Type type2 = tf.mapType(a, "orange", b, "mango");

		assertTrue("Two map types with different labels should be equivalent", type1.equivalent(type2));
		assertTrue("Two map types with different labels should be equivalent", type2.equivalent(type1));
		assertFalse("Two map types with different labels should not be equals", type1.equals(type2));
		assertFalse("Two map types with different labels should not be equals", type2.equals(type1));

		Type type3 = tf.mapType(a, b);
		assertTrue("Labeled and unlabeled maps should be equivalent", type1.equivalent(type3));
		assertTrue("Labeled and unlabeled maps should be equivalent", type3.equivalent(type1));
		assertTrue("Labeled and unlabeled maps should be equivalent", type2.equivalent(type3));
		assertTrue("Labeled and unlabeled maps should be equivalent", type3.equivalent(type2));
		assertFalse("Labeled and unlabeled maps should not be equals", type1.equals(type3));
		assertFalse("Labeled and unlabeled maps should not be equals", type3.equals(type1));
		assertFalse("Labeled and unlabeled maps should not be equals", type2.equals(type3));
		assertFalse("Labeled and unlabeled maps should not be equals", type3.equals(type2));
	}

	/**
	 * Check basic properties of put()
	 */
	public void testPut() {
		for(IMap map : testMaps) {
			for(StringPair p : keyValues) {
				IMap newMap = map.put(p.a, p.b);
				assertTrue(newMap.containsKey(p.a));
				assertEquals(p.b, newMap.get(p.a));
				assertEquals(map.getType().getKeyLabel(), newMap.getType().getKeyLabel());
				assertEquals(map.getType().getValueLabel(), newMap.getType().getValueLabel());
				assertTrue(map.getType().isSubtypeOf(newMap.getType()));
			}
		}
	}
	
	/**
	 * Check that putting doesn't modify original map, and doesn't modify other elements.
	 */
	public void testPutModification() {
		for(IMap map : testMaps) {
			for(StringPair p : keyValues) { // testing with an arbitrary element of map is sufficient
				if(map.containsKey(p.a)) {
					IValue val = map.get(p.a);
					for(StringPair q : keyValues) {
						IMap newMap = map.put(q.a, q.b);
						assertEquals(val, map.get(p.a)); // original is never modified
						if(!p.a.isEqual(q.a))
							assertEquals(val, newMap.get(p.a)); // only element q.a is modified
					}
				}
					
			}
		}
	}
	
	public void testCommon() {
		for(IMap map1 : testMaps) {
			for(IMap map2 : testMaps) {
				IMap map3 = map1.common(map2);
				// all common values are present
				for(IValue key : map1) {
					if(map1.get(key).equals(map2.get(key))) {
						assertEquals(map1.get(key), map3.get(key));
					}
				}
				// type is lub of map1 and map2 types
				if(!map3.isEmpty()) {
					assertTrue(map1.getType().toString() + " <: " + map3.getType(), map1.getType().isSubtypeOf(map3.getType()));
					assertTrue(map2.getType().toString() + " <: " + map3.getType(), map2.getType().isSubtypeOf(map3.getType()));
				}
				
				// check labels
				if(!map2.getType().hasFieldNames()) {
					assertEquals(map1.getType().getKeyLabel(), map3.getType().getKeyLabel());
					assertEquals(map1.getType().getValueLabel(), map3.getType().getValueLabel());
				}
				if(!map1.getType().hasFieldNames()) {
					assertEquals(map2.getType().getKeyLabel(), map3.getType().getKeyLabel());
					assertEquals(map2.getType().getValueLabel(), map3.getType().getValueLabel());
				}
			}

		}
	}

	public void testJoin() {
		for(IMap map1 : testMaps) {
			for(IMap map2 : testMaps) {
				IMap map3 = map1.join(map2);
				// should contain all values from map2...
				for(IValue key : map2) {
					assertEquals(map2.get(key), map3.get(key));
				}
				// ...and all values from map1 unless the keys are in map2
				for(IValue key : map1) {
					if(!map2.containsKey(key)) {
						assertEquals(map1.get(key), map3.get(key));
					}
				}
				
				// type is lub of map1 and map2 types
				if(!map3.isEmpty()) {
					assertTrue(map1.getType().toString() + " <: " + map3.getType(), map1.getType().isSubtypeOf(map3.getType()));
					assertTrue(map2.getType().toString() + " <: " + map3.getType(), map2.getType().isSubtypeOf(map3.getType()));
				}
				
				// check labels
				if(!map2.getType().hasFieldNames()) {
					assertEquals(map1.getType().getKeyLabel(), map3.getType().getKeyLabel());
					assertEquals(map1.getType().getValueLabel(), map3.getType().getValueLabel());
				}
				if(!map1.getType().hasFieldNames()) {
					assertEquals(map2.getType().getKeyLabel(), map3.getType().getKeyLabel());
					assertEquals(map2.getType().getValueLabel(), map3.getType().getValueLabel());
				}
			}

		}
	}

	public void testCompose() {
		for(IMap map1 : testMaps) {
			for(IMap map2 : testMaps) {
				IMap map3 = map1.compose(map2);
				// should map keys in map1 to values in map2
				for(IValue key : map1) {
					if(map2.containsKey(map1.get(key)))
						assertEquals(map2.get(map1.get(key)), map3.get(key));
					else
						assertNull(map3.get(key));
				}
				
				// type is key type of map1 and value type of map2
				if(!map3.isEmpty()) {
					assertEquals(map1.getType().getKeyType(), map3.getType().getKeyType());
					assertEquals(map2.getType().getValueType(), map3.getType().getValueType());
				}
				
				// check labels
				if(map1.getType().hasFieldNames() && map2.getType().hasFieldNames()) {
					assertEquals(map1.getType().getKeyLabel(), map3.getType().getKeyLabel());
					assertEquals(map2.getType().getValueLabel(), map3.getType().getValueLabel());
				}
				else {
					assertFalse(map3.getType().hasFieldNames());
				}
			}

		}
	}

	public void testRemove() {
		for(IMap map1 : testMaps) {
			for(IMap map2 : testMaps) {
				IMap map3 = map1.remove(map2);
				for(IValue key : map2) {
					assertFalse("Key " + key + " should not exist", map3.containsKey(key));
				}
				
				// type is same as map1
				if(!map3.isEmpty()) {
					assertEquals(map1.getType(), map3.getType());
				}
				
				// labels are same as map1
				if(map1.getType().hasFieldNames()) {
					assertEquals(map1.getType().getKeyLabel(), map3.getType().getKeyLabel());
					assertEquals(map1.getType().getValueLabel(), map3.getType().getValueLabel());
				}
			}

		}
	}
	
	public void testLabelsIO(){
		try{
			for(int i = 0; i < testValues.length; i++){
				for(Kind k : Kind.values()) {
					TestValue testValue = testValues[i];

					assertEquals(testValue.keyLabel, testValue.value.getType().getKeyLabel());
					assertEquals(testValue.valueLabel, testValue.value.getType().getValueLabel());
					
					System.out.println(testValue + " : " + testValue.value.getType()); // Temp

					IValue result = doIO(testValue.value, k);
					System.out.println(result + " : " + result.getType()); // Temp
					System.out.println(); // Temp

					if(!testValue.value.isEqual(result)){
						String message = "Not equal: \n\t"+testValue+" : "+testValue.value.getType()+"\n\t"+result+" : "+result.getType();
						System.err.println(message);
						fail(message);
					}

					Type resultType = result.getType();
					assertEquals("Labels should be preserved by " + k.name() + " IO: ", testValue.keyLabel, resultType.getKeyLabel());
					assertEquals("Labels should be preserved by " + k.name() + " IO: ", testValue.valueLabel, resultType.getValueLabel());
				}
			}
		}catch(IOException ioex){
			ioex.printStackTrace();
			fail(ioex.getMessage());
		}
	}

	private IValue doIO(IValue val, Kind kind) throws IOException {
		switch(kind) {
		case BINARY: {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BinaryWriter binaryWriter = new BinaryWriter(val, baos, ts);
			binaryWriter.serialize();

			byte[] data = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			BinaryReader binaryReader = new BinaryReader(vf, ts, bais);
			System.out.print("data: ");
			printBytes(data); // Temp
			return binaryReader.deserialize();
		}
		/*// Doesn't work, but should, perhaps?
		case XML: {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLWriter writer = new XMLWriter();
			writer.write(val, new OutputStreamWriter(baos), ts);

			byte[] data = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			XMLReader reader = new XMLReader();
			printBytes(data); // Temp
			return reader.read(vf, new InputStreamReader(bais));
		}
		*/
		
		/* TEXT IO shouldn't work, since the labels aren't present in the standard text representation */

		/* // Doesn't work, but should, perhaps?
		case ATERM: {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ATermWriter writer = new ATermWriter();
			writer.write(val, new OutputStreamWriter(baos), ts);

			byte[] data = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			ATermReader reader = new ATermReader();
			printBytes(data); // Temp
			return reader.read(vf, bais);
		}
		*/
		default:
			throw new RuntimeException("Missing case: " + kind.name());
		}
	}

	private final static String[] HEX = new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

	// May be handy when debugging.
	private static void printBytes(byte[] bytes){
		for(int i = 0; i < bytes.length; i++){
			byte b = bytes[i];
			int higher = (b & 0xf0) >> 4;
		int lower = b & 0xf;
		System.out.print("0x");
		System.out.print(HEX[higher]);
		System.out.print(HEX[lower]);
		System.out.print(" ");
		}
		System.out.println();
	}

	static class TestValue {
		Type type;
		IValue value;
		String keyLabel;
		String valueLabel;

		TestValue(BaseTestMap baseTestMap, String key, String value, String keyLabel, String valueLabel) {
			TypeFactory tf = baseTestMap.tf;
			IValueFactory vf = baseTestMap.vf;
			this.keyLabel = keyLabel;
			this.valueLabel = valueLabel;
			if(keyLabel != null && valueLabel != null)
				type = tf.mapType(tf.stringType(), keyLabel, tf.stringType(), valueLabel);
			else
				type = tf.mapType(tf.stringType(), tf.stringType());
			this.value = vf.map(type).put(vf.string(key), vf.string(value));
		}
		
		public String toString() {
			return value.toString();
		}
	}
	
	static class StringPair {
		IString a;
		IString b;
		
		StringPair(IString a, IString b) {
			this.a = a;
			this.b = b;
		}
		
		@Override 
		public String toString() {
			return String.format("(%s,%s)", a, b);
		}
	}

	public void testPutReplaceGet() {
		final IMap m1 = vf.mapWriter().done()
				.put(vf.integer(1), vf.integer(1))
				.put(vf.integer(1), vf.integer(2));
	
		assertEquals(1, m1.size());
		assertEquals(vf.integer(2), m1.get(vf.integer(1)));
	}

	public void testDynamicTypesAfterMapUpdatesGrow() {
		final IMap m1 = vf.mapWriter().done()
				.put(vf.integer(1), vf.integer(1))
				.put(vf.integer(1), vf.real(1));
	
		assertEquals(1, m1.size());
		assertEquals(tf.integerType(), m1.getType().getKeyType());
		assertEquals(tf.realType(), m1.getType().getValueType());
	}
	
	public void testDynamicTypesAfterMapWriterUpdatesGrow() {
		final IMapWriter w1 = vf.mapWriter();
		w1.put(vf.integer(1), vf.integer(1));
		w1.put(vf.integer(1), vf.real(1));

		final IMap m1 = w1.done();
		
		assertEquals(1, m1.size());
		assertEquals(tf.integerType(), m1.getType().getKeyType());
		assertEquals(tf.realType(), m1.getType().getValueType());
	}	
	
	public void testDynamicTypesAfterMapUpdatesShrink() {
		final IMap m1 = vf.mapWriter().done()
				.put(vf.integer(1), vf.integer(1))
				.put(vf.integer(1), vf.real(1))
				.put(vf.integer(1), vf.integer(1));
	
		assertEquals(1, m1.size());
		assertEquals(tf.integerType(), m1.getType().getKeyType());
		assertEquals(tf.integerType(), m1.getType().getValueType());
	}
	
	public void testDynamicTypesAfterMapWriterUpdatesShrink() {
		final IMapWriter w1 = vf.mapWriter();
		w1.put(vf.integer(1), vf.integer(1));
		w1.put(vf.integer(1), vf.real(1));
		w1.put(vf.integer(1), vf.integer(1));
	
		final IMap m1 = w1.done();
		
		assertEquals(1, m1.size());
		assertEquals(tf.integerType(), m1.getType().getKeyType());
		assertEquals(tf.integerType(), m1.getType().getValueType());
	}	
	
	public void testPutReplaceWithAnnotations_Map() { 
		final Type E = tf.abstractDataType(ts, "E");
		final Type N = tf.constructor(ts, E, "n", tf.integerType());
		ts.declareAnnotation(E, "x", tf.integerType());
		
		final IConstructor n = vf.constructor(N, vf.integer(1));
		final IConstructor na = n.asAnnotatable().setAnnotation("x", vf.integer(1));
		
		final IMap m1 = vf.mapWriter().done()
				.put(n, vf.integer(1))
				.put(na, vf.integer(1));
	
		assertEquals(1, m1.size());
		assertEquals(vf.integer(1), m1.get(n));
		assertEquals(vf.integer(1), m1.get(na));
	}

	public void testPutReplaceWithAnnotationsValue_Map() { 
		final Type E = tf.abstractDataType(ts, "E");
		final Type N = tf.constructor(ts, E, "n", tf.integerType());
		ts.declareAnnotation(E, "x", tf.integerType());
		
		final IConstructor n = vf.constructor(N, vf.integer(1));
		final IConstructor na = n.asAnnotatable().setAnnotation("x", vf.integer(1));
		
		final IMap m1 = vf.mapWriter().done()
				.put(vf.integer(1), n)
				.put(vf.integer(1), na);
	
		assertEquals(1, m1.size());
		assertEquals(na, m1.get(vf.integer(1)));
	}
	
	public void testPutReplaceWithAnnotations_MapWriter() { 
		final Type E = tf.abstractDataType(ts, "E");
		final Type N = tf.constructor(ts, E, "n", tf.integerType());
		ts.declareAnnotation(E, "x", tf.integerType());
		
		final IConstructor n = vf.constructor(N, vf.integer(1));
		final IConstructor na = n.asAnnotatable().setAnnotation("x", vf.integer(1));
		
		final IMapWriter w1 = vf.mapWriter();
		w1.put(n, vf.integer(1));
		w1.put(na, vf.integer(1));
	
		final IMap m1 = w1.done();
		
		assertEquals(1, m1.size());
		assertEquals(vf.integer(1), m1.get(n));
		assertEquals(vf.integer(1), m1.get(na));
	}

	public void testPutReplaceWithAnnotationsValue_MapWriter() { 
		final Type E = tf.abstractDataType(ts, "E");
		final Type N = tf.constructor(ts, E, "n", tf.integerType());
		ts.declareAnnotation(E, "x", tf.integerType());
		
		final IConstructor n = vf.constructor(N, vf.integer(1));
		final IConstructor na = n.asAnnotatable().setAnnotation("x", vf.integer(1));
		
		final IMapWriter w1 = vf.mapWriter();
		w1.put(vf.integer(1), n);
		w1.put(vf.integer(1), na);
	
		final IMap m1 = w1.done();
		
		assertEquals(1, m1.size());
		assertEquals(na, m1.get(vf.integer(1)));
	}
		
}
