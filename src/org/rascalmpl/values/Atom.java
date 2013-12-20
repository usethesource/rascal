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
		return value.charAt(index);
	}
	
	@Override
	public int indexOf(String str) {
		return getValue().indexOf(str);
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
