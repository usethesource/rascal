/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
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
