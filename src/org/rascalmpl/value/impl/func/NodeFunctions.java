/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.func;

import java.util.ArrayList;
import java.util.Iterator;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;

public class NodeFunctions {
	
	/*
     * TODO: merge with ListFunctions.replace(...). Algorithm is exactly the same, the only difference is
     * that difference interfaces are used (IList, INode).
	 */
	public static INode replace(IValueFactory vf, INode node1, int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		ArrayList<IValue> newChildren = new ArrayList<>();
		int rlen = repl.length();
		int increment = Math.abs(second - first);
		if (first < end) {
			int childIndex = 0;
			// Before begin
			while (childIndex < first) {
				newChildren.add(node1.get(childIndex++));
			}
			int replIndex = 0;
			boolean wrapped = false;
			// Between begin and end
			while (childIndex < end) {
				newChildren.add(repl.get(replIndex++));
				if (replIndex == rlen) {
					replIndex = 0;
					wrapped = true;
				}
				childIndex++; //skip the replaced element
				for (int j = 1; j < increment && childIndex < end; j++) {
					newChildren.add(node1.get(childIndex++));
				}
			}
			if (!wrapped) {
				while (replIndex < rlen) {
					newChildren.add(repl.get(replIndex++));
				}
			}
			// After end
			int dlen = node1.arity();
			while (childIndex < dlen) {
				newChildren.add(node1.get(childIndex++));
			}
		} else {
			// Before begin (from right to left)
			int childIndex = node1.arity() - 1;
			while (childIndex > first) {
				newChildren.add(0, node1.get(childIndex--));
			}
			// Between begin (right) and end (left)
			int replIndex = 0;
			boolean wrapped = false;
			while (childIndex > end) {
				newChildren.add(0, repl.get(replIndex++));
				if (replIndex == repl.length()) {
					replIndex = 0;
					wrapped = true;
				}
				childIndex--; //skip the replaced element
				for (int j = 1; j < increment && childIndex > end; j++) {
					newChildren.add(0, node1.get(childIndex--));
				}
			}
			if (!wrapped) {
				while (replIndex < rlen) {
					newChildren.add(0, repl.get(replIndex++));
				}
			}
			// Left of end
			while (childIndex >= 0) {
				newChildren.add(0, node1.get(childIndex--));
			}
		}

		IValue[] childArray = new IValue[newChildren.size()];
		newChildren.toArray(childArray);
		return vf.node(node1.getName(), childArray);
	}

	public static boolean isEqual(IValueFactory vf, INode node1, IValue value) {
		if(value == node1) return true;
		if(value == null) return false;

		if (node1.getType() != value.getType()) {
			return false;
		}

		if (value instanceof INode) {
			INode node2 = (INode) value;

			// Object equality ('==') is not applicable here
			// because value is cast to {@link INode}.
			if (!node1.getName().equals(node2.getName())) {
				return false;
			}

			if (node1.arity() != node2.arity()) {
				return false;
			}

			Iterator<IValue> it1 = node1.iterator();
			Iterator<IValue> it2 = node2.iterator();

			while (it1.hasNext()) {
				if (!it1.next().isEqual(it2.next())) {
					return false;
				}
			}

			if (node1.mayHaveKeywordParameters() && node2.mayHaveKeywordParameters()) {
				return node1.asWithKeywordParameters().equalParameters(node2.asWithKeywordParameters());
			}

			if (node1.mayHaveKeywordParameters() && node1.asWithKeywordParameters().hasParameters()) {
				return false;
			}

			if (node2.mayHaveKeywordParameters() && node2.asWithKeywordParameters().hasParameters()) {
				return false;
			}

			return true;
		}

		return false;
	}

}
