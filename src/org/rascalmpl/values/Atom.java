package org.rascalmpl.values;

import java.util.Iterator;

public abstract class Atom extends OrgString {


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
