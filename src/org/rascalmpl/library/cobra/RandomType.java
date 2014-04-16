/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IKeywordParameterInitializer;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class RandomType {

	private final TypeFactory tf = TypeFactory.getInstance();
	private final RascalTypeFactory rtf = RascalTypeFactory.getInstance();
	private final LinkedList<Type> atomicTypes;
	private final Random random;
	
	private int cntRecursiveTypes = 4; // list, set, map, relation, list relation, tuple

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

	public void plusRascalTypes(boolean yes) {
		if(yes) {
			cntRecursiveTypes = 7;
		} else {
			cntRecursiveTypes = 4;
		}
	}
	
	public Type getType(int maxDepth) {
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
		switch (random.nextInt(cntRecursiveTypes)) {
		case 0:
			return tf.listType(getType(maxDepth));
		case 1:
			return tf.setType(getType(maxDepth));
		case 2:
			return tf.mapType(getType(maxDepth), getType(maxDepth));
		case 3:
			return getTupleType(maxDepth);
		case 4:
			return getFunctionType(maxDepth);
		case 5:
			return getOverloadedFunctionType(maxDepth);
		case 6:
			return getReifiedType(maxDepth);
		}
		return null;
	}

	private Type getTupleType(int maxDepth) {
		List<Type> l = getTypeList(maxDepth, 1);
		return tf.tupleType(l.toArray(new Type[l.size()]));
	}
	
	public Type getFunctionType(int maxDepth) {
	  // TODO: add keyword parameter generation
		return rtf.functionType(getType(maxDepth), getTupleType(maxDepth), tf.voidType(), Collections.<String,IKeywordParameterInitializer>emptyMap());
	}
	
	public Type getOverloadedFunctionType(int maxDepth) {
	  // TODO: add keyword parameter generation
		Type returnType = getType(maxDepth);
		List<Type> l = getTypeList(maxDepth, 2);
		Set<FunctionType> alternatives = new HashSet<FunctionType>();
		for(Type t : l) {
			if(t.isTuple()) {
				alternatives.add((FunctionType)rtf.functionType(returnType, t, tf.voidType(), Collections.<String,IKeywordParameterInitializer>emptyMap()));
			} else {
				alternatives.add((FunctionType)rtf.functionType(returnType, tf.tupleType(t), tf.voidType(), Collections.<String,IKeywordParameterInitializer>emptyMap()));
			}
		}
		return rtf.overloadedFunctionType(alternatives);
	}
	
	public Type getReifiedType(int maxDepth) {
		return rtf.reifiedType(getType(maxDepth));
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



