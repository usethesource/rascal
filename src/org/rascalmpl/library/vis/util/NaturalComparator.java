package org.rascalmpl.library.vis.util;

import java.util.Comparator;

public class NaturalComparator<T extends Comparable<T>> implements Comparator<T>{

	@SuppressWarnings("rawtypes")
	public static final NaturalComparator instance = new NaturalComparator();
	
	@Override
	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	}
	
	

}
