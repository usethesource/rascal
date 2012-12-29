/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public class RandomType {

	private final TypeFactory tf = TypeFactory.getInstance();
	private final LinkedList<Type> atomicTypes;
	private final Random random;

	public RandomType() {
		atomicTypes = new LinkedList<Type>();
		atomicTypes.add(tf.realType());
		atomicTypes.add(tf.integerType());
		atomicTypes.add(tf.rationalType());
		atomicTypes.add(tf.numberType());
		atomicTypes.add(tf.sourceLocationType());
		atomicTypes.add(tf.stringType());
		atomicTypes.add(tf.nodeType());
		atomicTypes.add(tf.boolType());
		atomicTypes.add(tf.dateTimeType());
		this.random = new Random();
	}

	// Vanuit value
	public Type getType(int maxDepth) {
		int cntRecursiveTypes = 5; // list, set, map, relation, tuple
		int cntAtomicTypes = atomicTypes.size();

		if (maxDepth <= 0
				|| random.nextInt(cntAtomicTypes + cntRecursiveTypes) < cntAtomicTypes) {
			return getAtomicType();
		} else {
			return getRecursiveType(maxDepth - 1);
		}

	}

	private Type getRecursiveType(int maxDepth) {
		// list, set, map, relation, list relation, tuple
		switch (random.nextInt(5)) {
		case 0:
			return tf.listType(getType(maxDepth));
		case 1:
			return tf.setType(getType(maxDepth));
		case 2:
			return tf.mapType(getType(maxDepth), getType(maxDepth));
		case 3:
			return getRelationType(maxDepth);
		case 4:
			return getListRelationType(maxDepth);
		case 5:
			return getTupleType(maxDepth);
		}
		return null;
	}

	private Type getTupleType(int maxDepth) {
		List<Type> l = getTypeList(maxDepth, 1);
		return tf.tupleType(l.toArray(new Type[l.size()]));
	}

	private Type getRelationType(int maxDepth) {
		List<Type> l = getTypeList(maxDepth, 1);
		return tf.relType(l.toArray(new Type[l.size()]));
	}
	
	private Type getListRelationType(int maxDepth) {
		List<Type> l = getTypeList(maxDepth, 1);
		return tf.lrelType(l.toArray(new Type[l.size()]));
	}

	private List<Type> getTypeList(int maxLength, int minLength) {
		if (random.nextInt(2) == 0 || maxLength <= 0) {
			LinkedList<Type> l = new LinkedList<Type>();
			for (int i = 0; i < minLength; i++) {
				l.add(getType(maxLength - 1));
			}
			return l;
		} else {
			List<Type> l = getTypeList(maxLength - 1,
					Math.max(0, minLength - 1));
			l.add(getType(maxLength - 1));
			return l;
		}

	}

	private Type getAtomicType() {
		return this.atomicTypes.get(random.nextInt(atomicTypes.size()));
	}
}



