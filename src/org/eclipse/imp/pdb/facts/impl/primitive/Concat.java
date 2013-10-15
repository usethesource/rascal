package org.eclipse.imp.pdb.facts.impl.primitive;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class Concat extends OrgString {

	private final OrgString lhs;
	private final OrgString rhs;
	private final int length;

	public Concat(OrgString lhs, OrgString rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.length = lhs.length() + rhs.length();
	}
	
	public OrgString getLhs() {
		return lhs;
	}
	
	public OrgString getRhs() {
		return rhs;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public String getValue() {
		return lhs.getValue() + rhs.getValue();
	}

	@Override
	public IString reverse() {
		/*
		 * abcd --> dcba
		 * ab cd --> dc ba
		 * ab cde --> edc ba
		 * What happens with locations?
		 */
		return new Concat((OrgString)rhs.reverse(), (OrgString)lhs.reverse());
	}

	@Override
	public int charAt(int index) {
		if (index < lhs.length()) {
			return lhs.charAt(index);
		}
		return rhs.charAt(index);
	}

	@Override
	public IString substring(int start, int end) {
		if (end < lhs.length()) {
			return lhs.substring(start, end);
		}
		if (start >= lhs.length()) {
			return rhs.substring(start - lhs.length(), end - lhs.length());
		}
		return new Concat((OrgString)lhs.substring(start),
				(OrgString)rhs.substring(0, end - lhs.length()));
	}

	
	@Override
	public IString replace(int first, int second, int end, IString repl) {
		throw new ImplementationError("Not yet implemented");
	}

	@Override
	public int compare(IString other) {
		return getValue().compareTo(other.getValue());
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private Iterator<Integer> iterL = lhs.iterator();
			private Iterator<Integer> iterR = rhs.iterator();
			boolean switched = false;

			@Override
			public boolean hasNext() {
				if (!switched) {
					if (iterL.hasNext()) {
						return true;
					}
					switched = true;
					return iterR.hasNext();
				}
				return iterR.hasNext();
			}

			@Override
			public Integer next() {
				if (!switched) {
					return iterL.next();
				}
				return iterR.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("no remove on strings");
			}
		};
	}


	@Override
	public int indexOf(String str) {
		int ind = lhs.indexOf(str);
		if (ind != -1) {
			return ind;
		}
		// test for overlapping
		for (int i = 1; i < str.length(); i++) {
			String before = str.substring(0, i);
			String after = str.substring(i, str.length());
			int lind = lhs.indexOf(before);
			int rind = rhs.indexOf(after);
			if (lind == str.length() - i && rind == 0) {
				return lind;
			}
		}
		return rhs.indexOf(str);
	}


}
