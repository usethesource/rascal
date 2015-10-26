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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.TypeFactory;

import junit.framework.TestCase;

public abstract class BaseTestList extends TestCase {
    private IValueFactory vf;
    private TypeFactory tf = TypeFactory.getInstance();
    
    private IValue[] integers;
    private IList integerList;
    private IList emptyIntegerList;
    
	protected void setUp(IValueFactory factory) throws Exception {
		super.setUp();
		vf = factory;
		
		integers = new IValue[20];
		IListWriter w = vf.listWriter(tf.integerType());
		
		for (int i = 0; i < integers.length; i++) {
			integers[i] = vf.integer(i);
		}
		
		for (int i = integers.length - 1; i >= 0; i--) {
			w.insert(vf.integer(i));
		}
		
		integerList = w.done();
	
		emptyIntegerList = vf.listWriter(tf.integerType()).done();
	}

	public void testGetElementType() {
		if (!integerList.getElementType().isSubtypeOf(tf.integerType())) {
			fail("funny getElementType");
		}
	}

	public void testAppend() {
		try {
			IValue newValue = vf.integer(integers.length);
			IList longer = integerList.append(newValue);
			
			if (longer.length() != integerList.length() + 1) {
				fail("append failed");
			}
			
			if (!longer.get(integerList.length()).isEqual(newValue)) {
				fail("element was not appended");
			}
			
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
		
		try {
			if (!integerList.append(vf.real(2)).getElementType().equivalent(tf.numberType())) {
			  fail("append should lub the element type");
			}
		} catch (FactTypeUseException e) {
			// this should happen
		}
	}

	public void testGet() {
		for (int i = 0; i < integers.length; i++) {
			if (!integerList.get(i).isEqual(integers[i])) {
				fail("get failed");
			}
		}
	}

	public void testInsert() {
		try {
			IValue newValue = vf.integer(integers.length);
			IList longer = integerList.insert(newValue);
			
			if (longer.length() != integerList.length() + 1) {
				fail("append failed");
			}
			
			if (!longer.get(0).isEqual(newValue)) {
				fail("element was not insrrted");
			}
			
		} catch (FactTypeUseException e) {
			fail("the above should be type correct");
		}
		
		try {
			if (!integerList.insert(vf.real(2)).getElementType().equivalent(tf.numberType())) {
			  fail("insert should lub the element type");
			}
		} catch (FactTypeUseException e) {
			// this should happen
		}
	}

	public void testLength() {
		if (vf.list(tf.integerType()).length() != 0) {
			fail("empty list should be size 0");
		}
		
		if (integerList.length() != integers.length) {
			fail("length does not count amount of elements");
		}
	}

	public void testReverse() {
		IList reverse = integerList.reverse();
		
		if (reverse.getType() != integerList.getType()) {
			fail("reverse should keep type");
		}
		
		if (reverse.length() != integerList.length()) {
			fail("length of reverse is different");
		}
		
		for (int i = 0; i < integers.length; i++) {
			if (!reverse.get(i).isEqual(integers[integers.length - i - 1])) {
				fail("reverse did something funny: " + reverse + " is not reverse of " + integerList);
			}
		}
	}
	
	public void testShuffle() {
		IList shuffle = integerList.shuffle(new Random());
		
		if (shuffle.getType() != integerList.getType()) {
			fail("shuffle should keep type");
		}
		
		if (shuffle.length() != integerList.length()) {
			fail("length after shuffle is different");
		}
	}
	// doesn't completly test distribution, but at least protects against some cases
	public void testShuffleFirstLast() {
		Set<IValue> first = new HashSet<>();
		Set<IValue> last = new HashSet<>();
		Random r = new Random();
		for (int i=0; i < 20 * integerList.length(); i++) {
			IList shuffled = integerList.shuffle(r);
			first.add(shuffled.get(0));
			last.add(shuffled.get(shuffled.length() - 1));
		}
		for (IValue v: integerList) {
			if (!first.contains(v)) {
				fail("The shuffle doesn't shuffle the first index correctly");
			}
			if (!last.contains(v)) {
				fail("The shuffle doesn't shuffle the last index correctly");
			}
		}
	}
	
	public void testReverseEmpty() {
		IList reverse = emptyIntegerList.reverse();
		
		if (reverse.getType() != emptyIntegerList.getType()) {
			fail("reverse should keep type");
		}
		
		if (reverse.length() != emptyIntegerList.length()) {
			fail("length of reverse is different");
		}
	}	

	public void testIterator() {
		Iterator<IValue> it = integerList.iterator();
		
		int i;
		for (i = 0; it.hasNext(); i++) {
			IValue v = it.next();
			if (!v.isEqual(integers[i])) {
				fail("iterator does not iterate in order");
			}
		}
	}
	
	// NOTE: This is not a very good test, but sufficient for it's purpose.
	public void testSubList(){
		// Front
		IListWriter flw = vf.listWriter(tf.integerType());
		for(int i = 0; i < 20; i++){
			flw.append(vf.integer(i));
		}
		IList fList = flw.done();
		
		// Back
		IListWriter blw = vf.listWriter(tf.integerType());
		for(int i = 19; i >= 0; i--){
			blw.insert(vf.integer(i));
		}
		IList bList = blw.done();
		
		// Overlap
		IListWriter olw = vf.listWriter(tf.integerType());
		for(int i = 9; i >= 0; i--){
			olw.insert(vf.integer(i));
		}
		for(int i = 10; i < 20; i++){
			olw.append(vf.integer(i));
		}
		IList oList = olw.done();
		
		IList fSubList = fList.sublist(0, 5);
		IList bSubList = bList.sublist(0, 5);
		IList oSubList = oList.sublist(0, 5);
		checkSubListEquality(fSubList, bSubList, oSubList);
		
		fSubList = fList.sublist(1, 5);
		bSubList = bList.sublist(1, 5);
		oSubList = oList.sublist(1, 5);
		checkSubListEquality(fSubList, bSubList, oSubList);
		
		fSubList = fList.sublist(0, 15);
		bSubList = bList.sublist(0, 15);
		oSubList = oList.sublist(0, 15);
		checkSubListEquality(fSubList, bSubList, oSubList);
		
		fSubList = fList.sublist(1, 15);
		bSubList = bList.sublist(1, 15);
		oSubList = oList.sublist(1, 15);
		checkSubListEquality(fSubList, bSubList, oSubList);
		
		fSubList = fList.sublist(5, 5);
		bSubList = bList.sublist(5, 5);
		oSubList = oList.sublist(5, 5);
		checkSubListEquality(fSubList, bSubList, oSubList);
		
		fSubList = fList.sublist(5, 10);
		bSubList = bList.sublist(5, 10);
		oSubList = oList.sublist(5, 10);
		checkSubListEquality(fSubList, bSubList, oSubList);
		
		fSubList = fList.sublist(15, 5);
		bSubList = bList.sublist(15, 5);
		oSubList = oList.sublist(15, 5);
		checkSubListEquality(fSubList, bSubList, oSubList);
	}
	
	private static void checkSubListEquality(IList fList, IList bList, IList oList){
		if(!fList.isEqual(bList) || !bList.isEqual(oList)) fail("IList#subList is broken: "+fList+" "+bList+" "+oList);
	}
	
	public void testIsSubListOf(){
		IListWriter w = vf.listWriter(tf.integerType());
		
		for (int i = integers.length - 1; i >= 0; i -= 2) {
			w.insert(vf.integer(i));
		}
		
		IList even = w.done();
		
		w = vf.listWriter(tf.integerType());
		
		for (int i = integers.length - 2; i >= 0; i -= 2) {
			w.insert(vf.integer(i));
		}
		
		IList odd = w.done();
		if(!integerList.isSubListOf(integerList))
			fail("integerList should be sublist of integerList");
		if(!even.isSubListOf(integerList))
			fail("even should be sublist of integerList");
		if(!odd.isSubListOf(integerList))
			fail("odd should be sublist of integerList");
		
		if(integerList.isSubListOf(even))
			fail("integerList cannot be sublist of even");
		if(integerList.isSubListOf(odd))
			fail("integerList cannot be sublist of odd");
		if(even.isSubListOf(odd))
			fail("even cannot be sublist of odd");
		if(odd.isSubListOf(even))
			fail("odd cannot be sublist of even");
		
		IList L123 = vf.list(integers[1], integers[2], integers[3]);
		IList L918273 = vf.list(integers[9], integers[1], integers[8],integers[2], integers[7], integers[3]);
		IList L918372 = vf.list(integers[9], integers[1], integers[8],integers[3], integers[7], integers[2]);
		
		if(!L123.isSubListOf(L918273))
			fail("123 is sublist of 918273");
		if(L123.isSubListOf(L918372))
			fail("123 is not a sublist of 918372");
	}
	
	public void testSubtract(){
		IList L12312 = vf.list(integers[1], integers[2], integers[3],  integers[1], integers[2]);
		IList L123 = vf.list(integers[1], integers[2], integers[3]);
		IList L12 = vf.list(integers[1], integers[2]);
		IList L321321 = vf.list(integers[3], integers[2], integers[1],integers[3], integers[2], integers[1]);
		
		if(!checkListEquality(L12312.subtract(L123), L12))
			fail("12312 subtract 123 should be 12");
		if(!L12312.subtract(L321321).isEmpty())
			fail("12312 subtract 123213213 should be empty");
	}
	
	private boolean checkListEquality(IList lst1, IList lst2){
		return lst1.isSubListOf(lst2) && lst2.isSubListOf(lst2);
		
	}
}
