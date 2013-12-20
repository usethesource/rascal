package org.rascalmpl.values;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.asserts.ImplementationError;

public class Concat extends OrgString {

	private final OrgString lhs;
	private final OrgString rhs;
	private Integer length = null;

	public Concat(OrgString lhs, OrgString rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public OrgString getLhs() {
		return lhs;
	}
	
	public OrgString getRhs() {
		return rhs;
	}

	@Override
	public int length() {
		if (length == null) {
		  length = lhs.length() + rhs.length();
		}
		return length;
	}

	@Override
	public IString reverse() {
		return ((OrgString)rhs.reverse()).concat(lhs.reverse());
	}

	@Override
	public int charAt(int index) {
		if (index < lhs.length()) {
			return lhs.charAt(index);
		}
		return rhs.charAt(index - lhs.length());
	}

	@Override
	public IString substring(int start, int end) {
		if (end < lhs.length()) {
			return lhs.substring(start, end);
		}
		if (start >= lhs.length()) {
			return rhs.substring(start - lhs.length(), end - lhs.length());
		}
		return lhs.substring(start)
				.concat(rhs.substring(0, end - lhs.length()));
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
			// for a string abcd we partition as
			// a bcd, ab cd, abc d
			String before = str.substring(0, i);
			String after = str.substring(i, str.length());
			int lind = lhs.indexOf(before);
			int rind = rhs.indexOf(after);
			if (lind == str.length() - i && rind == 0) {
				return lind;
			}
		}
		ind = rhs.indexOf(str);
		if (ind != -1) {
			return lhs.length() + ind;
		}
		return -1;
	}

	@Override
	public int hashCode() {
		return lhs.hashCode() + rhs.hashCode();
	}

	@Override
	public void serialize(StringBuilder b) {
		lhs.serialize(b);
		rhs.serialize(b);
	}
	
	public OrgString capitalize() {
		assert lhs.length() > 0;
		return (OrgString) lhs.capitalize().concat(rhs);
	}

}
