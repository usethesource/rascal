package org.rascalmpl.values;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IString;

public abstract class Atom extends OrgString {
	protected final IString value;
	private Integer hashcode = null;
	
	public Atom(String s) {
		value = vf.baseString(s);
	}
	
	protected Atom(IString v) {
		value = v;
	}
	

	@Override
	public int length() {
		return value.length();
	}
	
	@Override
	public String getValue() {
		return value.getValue();
	}
	
	@Override
	public int hashCode() {
		if (hashcode == null) {
			hashcode = 0;
			String s = getValue();
			for (int i = 0; i < length(); i++) {
				hashcode += s.charAt(i);
			}
			return hashcode;
		}
		return hashcode;
	}
	
	@Override
	public int charAt(int index) {
		return value.getValue().charAt(index);
	}
	
	
	private boolean match(IString pat, int offset) {
		for (int i = 0; i < pat.length(); i++) {
			if (pat.charAt(i) != charAt(offset + i)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int indexOf(IString pat) {
		for (int i = 0; i < length() - pat.length() + 1; i++) {
			if (match(pat, i)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private int cursor = 0;

			@Override
			public boolean hasNext() {
				return cursor < length();
			}

			@Override
			public Integer next() {
				return charAt(cursor++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("no remove on strings");
			}
		};
	}
	
}
