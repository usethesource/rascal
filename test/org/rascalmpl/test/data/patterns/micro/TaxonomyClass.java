package org.rascalmpl.test.data.patterns.micro;

import java.util.*;

public class TaxonomyClass<T> extends ArrayList<T> {
	public TaxonomyClass() {
		super();
	}
	
	public TaxonomyClass(Collection<? extends T> c) {
		super(c);
	}

}
