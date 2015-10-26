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
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *
 * Based on code by:
 *
 *   * Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/
package org.rascalmpl.value.impl.func;

import java.util.Iterator;
import java.util.Random;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.util.collections.ShareableValuesHashSet;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public final class ListFunctions {

	private final static TypeFactory TF = TypeFactory.getInstance();

	public static IList sublist(IValueFactory vf, IList list1, int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > list1.length()) {
			throw new IndexOutOfBoundsException();
		}
		IListWriter w = vf.listWriter();
		for (int i = offset; i < offset + length; i++) {
			w.append(list1.get(i));
		}
		return w.done();
	}

	public static IList insert(IValueFactory vf, IList list1, IValue e) {
		IListWriter w = vf.listWriter();
		w.appendAll(list1);
		w.insert(e);

		return w.done();
	}

	public static IList put(IValueFactory vf, IList list1, int i, IValue e)
			throws IndexOutOfBoundsException {
		IListWriter w = vf.listWriter();
		w.appendAll(list1);
		w.replaceAt(i, e);
		return w.done();
	}

	// public static IList replace(IValueFactory vf, IList list1, int first,
	// int second, int end, IList repl) {
	// IListWriter w = vf.listWriter();
	// if (first < end) {
	// for (int i = 0; i < first; i++) {
	// w.append(list1.get(i));
	// }
	// w.appendAll(repl);
	// for (int i = end; i < list1.length(); i++) {
	// w.append(list1.get(i));
	// }
	// } else {
	// for (int i = list1.length() - 1; i > first; i--) {
	// w.insert(list1.get(i));
	// }
	// for (IValue v : repl) {
	// w.insert(v);
	// }
	// for (int i = end; i >= 0; i--) {
	// w.insert(list1.get(i));
	// }
	// }
	// return w.done();
	// }

	public static IList replace(IValueFactory vf, IList list1, int first,
			int second, int end, IList repl) throws FactTypeUseException,
			IndexOutOfBoundsException {
		IListWriter result = vf.listWriter();

		int rlen = repl.length();
		int increment = Math.abs(second - first);

		if (first < end) {
			int listIndex = 0;
			// Before begin
			while (listIndex < first) {
				result.append(list1.get(listIndex++));
			}
			int replIndex = 0;
			boolean wrapped = false;
			// Between begin and end
			while (listIndex < end) {
				result.append(repl.get(replIndex++));
				if (replIndex == rlen) {
					replIndex = 0;
					wrapped = true;
				}
				listIndex++; // skip the replaced element
				for (int j = 1; j < increment && listIndex < end; j++) {
					result.append(list1.get(listIndex++));
				}
			}
			if (!wrapped) {
				while (replIndex < rlen) {
					result.append(repl.get(replIndex++));
				}
			}
			// After end
			int dlen = list1.length();
			while (listIndex < dlen) {
				result.append(list1.get(listIndex++));
			}
		} else {
			// Before begin (from right to left)
			int listIndex = list1.length() - 1;
			while (listIndex > first) {
				result.insert(list1.get(listIndex--));
			}
			// Between begin (right) and end (left)
			int replIndex = 0;
			boolean wrapped = false;
			while (listIndex > end) {
				result.insert(repl.get(replIndex++));
				if (replIndex == repl.length()) {
					replIndex = 0;
					wrapped = true;
				}
				listIndex--; // skip the replaced element
				for (int j = 1; j < increment && listIndex > end; j++) {
					result.insert(list1.get(listIndex--));
				}
			}
			if (!wrapped) {
				while (replIndex < rlen) {
					result.insert(repl.get(replIndex++));
				}
			}
			// Left of end
			while (listIndex >= 0) {
				result.insert(list1.get(listIndex--));
			}
		}

		return result.done();
	}

	public static IList append(IValueFactory vf, IList list1, IValue e) {
		IListWriter w = vf.listWriter();
		w.appendAll(list1);
		w.append(e);

		return w.done();
	}

	public static boolean contains(IValueFactory vf, IList list1, IValue e) {
		for (IValue v : list1) {
			if (v.isEqual(e)) {
				return true;
			}
		}
		return false;
	}

	public static IList delete(IValueFactory vf, IList list1, IValue v) {
		IListWriter w = vf.listWriter();

		boolean deleted = false;
		for (Iterator<IValue> iterator = list1.iterator(); iterator.hasNext();) {
			IValue e = iterator.next();

			if (!deleted && e.isEqual(v)) {
				deleted = true; // skip first occurrence
			} else {
				w.append(e);
			}
		}
		return w.done();
	}

	public static IList delete(IValueFactory vf, IList list1, int index) {
		IListWriter w = vf.listWriter();

		int currentIndex = 0;
		boolean deleted = false;
		for (Iterator<IValue> iterator = list1.iterator(); iterator.hasNext(); currentIndex++) {
			IValue e = iterator.next();

			if (!deleted && index == currentIndex) {
				deleted = true; // skip first occurrence
			} else {
				w.append(e);
			}
		}
		return w.done();
	}

	public static IList reverse(IValueFactory vf, IList list1) {
		IListWriter w = vf.listWriter();
		for (IValue e : list1) {
			w.insert(e);
		}
		return w.done();
	}
	public static IList shuffle(IValueFactory vf, IList list1, Random rand) {
		IListWriter w = vf.listWriter();
		w.appendAll(list1); // add everything
		// we use Fisher–Yates shuffle (or Knuth shuffle)
		// unbiased and linear time (incase of random access)
		for (int i = list1.length() - 1; i >= 1; i--) {
			w.replaceAt(i, w.replaceAt(rand.nextInt(i + 1), w.get(i)));
		}
		return w.done();
	}

	public static IList concat(IValueFactory vf, IList list1, IList list2) {
		IListWriter w = vf.listWriter();
		w.appendAll(list1);
		w.appendAll(list2);
		return w.done();
	}

	public static int hashCode(IValueFactory vf, IList list1) {
		int hash = 0;

		Iterator<IValue> iterator = list1.iterator();
		while (iterator.hasNext()) {
			IValue element = iterator.next();
			hash = (hash << 1) ^ element.hashCode();
		}

		return hash;
	}

	public static boolean equals(IValueFactory vf, IList list1, Object other) {
		if (other == list1)
			return true;
		if (other == null)
			return false;

		if (other instanceof IList) {
			IList list2 = (IList) other;

			if (list1.isEmpty() && list2.isEmpty()) {
				return true;
			}
			
			if (list1.getType() != list2.getType())
				return false;

			if (hashCode(vf, list1) != hashCode(vf, list2))
				return false;

			
			
			if (list1.length() == list2.length()) {

				final Iterator<IValue> it1 = list1.iterator();
				final Iterator<IValue> it2 = list2.iterator();

				while (it1.hasNext() && it2.hasNext()) {
					// call to Object.equals(Object)
					if (it1.next().equals(it2.next()) == false)
						return false;
				}

				assert (!it1.hasNext() && !it2.hasNext());
				return true;
			}
		}

		return false;
	}

	public static boolean isEqual(IValueFactory vf, IList list1, IValue other) {
		// return equals(vf, list1, other);
		if (other == list1)
			return true;
		if (other == null)
			return false;

		if (other instanceof IList) {
			IList list2 = (IList) other;

			if (list1.length() == list2.length()) {

				final Iterator<IValue> it1 = list1.iterator();
				final Iterator<IValue> it2 = list2.iterator();

				while (it1.hasNext() && it2.hasNext()) {
					// call to IValue.isEqual(IValue)
					if (it1.next().isEqual(it2.next()) == false)
						return false;
				}

				assert (!it1.hasNext() && !it2.hasNext());
				return true;
			}
		}

		return false;
	}

	public static IList product(IValueFactory vf, IList list1, IList list2) {
		IListWriter w = vf.listWriter();

		for (IValue t1 : list1) {
			for (IValue t2 : list2) {
				IValue values[] = { t1, t2 };
				ITuple t3 = vf.tuple(values);
				w.insert(t3);
			}
		}

		return (IList) w.done();
	}

	public static IList intersect(IValueFactory vf, IList list1, IList list2) {
		IListWriter w = vf.listWriter();

		for (IValue v : list1) {
			if (list2.contains(v)) {
				w.append(v);
			}
		}

		return w.done();
	}

	public static IList subtract(IValueFactory vf, IList list1, IList list2) {
		IListWriter w = vf.listWriter();
		for (IValue v : list1) {
			if (list2.contains(v)) {
				list2 = list2.delete(v);
			} else
				w.append(v);
		}
		return w.done();
	}

	public static boolean isSubListOf(IValueFactory vf, IList list1, IList list2) {
		int j = 0;
		nextValue: for (IValue elm : list1) {
			while (j < list2.length()) {
				if (elm.isEqual(list2.get(j))) {
					j++;
					continue nextValue;
				} else
					j++;
			}
			return false;
		}
		return true;
	}

	public static IList closure(IValueFactory vf, IList list1) {
		// will throw exception if not binary and reflexive
		list1.getType().closure();

		IList tmp = list1;

		int prevCount = 0;

		ShareableValuesHashSet addedTuples = new ShareableValuesHashSet();
		while (prevCount != tmp.length()) {
			prevCount = tmp.length();
			IList tcomp = compose(vf, tmp, tmp);
			IListWriter w = vf.listWriter();
			for (IValue t1 : tcomp) {
				if (!tmp.contains(t1)) {
					if (!addedTuples.contains(t1)) {
						addedTuples.add(t1);
						w.append(t1);
					}
				}
			}
			tmp = tmp.concat(w.done());
			addedTuples.clear();
		}
		return tmp;
	}

	public static IList closureStar(IValueFactory vf, IList list1) {
		list1.getType().closure();
		// an exception will have been thrown if the type is not acceptable

		IListWriter reflex = vf.listWriter();

		for (IValue e : carrier(vf, list1)) {
			reflex.insert(vf.tuple(new IValue[] { e, e }));
		}

		return closure(vf, list1).concat(reflex.done());
	}

	public static IList compose(IValueFactory vf, IList list1, IList list2) {

		Type otherTupleType = list2.getType().getFieldTypes();

		if (list1.getElementType() == TF.voidType())
			return list1;
		if (otherTupleType == TF.voidType())
			return list2;

		if (list1.getElementType().getArity() != 2
				|| otherTupleType.getArity() != 2)
			throw new IllegalOperationException("compose",
					list1.getElementType(), otherTupleType);

		// Relaxed type constraint:
		if (!list1.getElementType().getFieldType(1)
				.comparable(otherTupleType.getFieldType(0)))
			throw new IllegalOperationException("compose",
					list1.getElementType(), otherTupleType);

		IListWriter w = vf.listWriter();

		for (IValue v1 : list1) {
			ITuple tuple1 = (ITuple) v1;
			for (IValue t2 : list2) {
				ITuple tuple2 = (ITuple) t2;

				if (tuple1.get(1).isEqual(tuple2.get(0))) {
					w.append(vf.tuple(tuple1.get(0), tuple2.get(1)));
				}
			}
		}
		return w.done();
	}

	public static IList carrier(IValueFactory vf, IList rel1) {
		IListWriter w = vf.listWriter();
		java.util.HashSet<IValue> cache = new java.util.HashSet<>();

		for (IValue v : rel1) {
			ITuple t = (ITuple) v;
			for (IValue e : t) {
				if (!cache.contains(e)) {
					cache.add(e);
					w.append(e);
				}
			}
		}

		return w.done();
	}

	public static IList domain(IValueFactory vf, IList rel1) {
		int columnIndex = 0;
		IListWriter w = vf.listWriter();
		java.util.HashSet<IValue> cache = new java.util.HashSet<>();

		for (IValue elem : rel1) {
			ITuple tuple = (ITuple) elem;
			IValue e = tuple.get(columnIndex);
			if (!cache.contains(e)) {
				cache.add(e);
				w.append(e);
			}
		}
		return w.done();
	}

	public static IList range(IValueFactory vf, IList rel1) {
		int columnIndex = rel1.getType().getArity() - 1;
		IListWriter w = vf.listWriter();
		java.util.HashSet<IValue> cache = new java.util.HashSet<>();

		for (IValue elem : rel1) {
			ITuple tuple = (ITuple) elem;
			IValue e = tuple.get(columnIndex);
			if (!cache.contains(e)) {
				cache.add(e);
				w.append(e);
			}
		}

		return w.done();
	}

	public static IList project(IValueFactory vf, IList list1, int... fields) {
		IListWriter w = vf.listWriter();

		for (IValue v : list1) {
			w.append(((ITuple) v).select(fields));
		}

		return w.done();
	}

	public static IList projectByFieldNames(IValueFactory vf, IList list1, String... fields) {
		int[] indexes = new int[fields.length];
		int i = 0;

		if (list1.getType().getFieldTypes().hasFieldNames()) {
			for (String field : fields) {
				indexes[i++] = list1.getType().getFieldTypes()
						.getFieldIndex(field);
			}

			return project(vf, list1, indexes);
		}

		throw new IllegalOperationException("select with field names",
				list1.getType());
	}

}