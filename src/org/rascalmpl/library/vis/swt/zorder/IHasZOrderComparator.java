package org.rascalmpl.library.vis.swt.zorder;

import java.util.Comparator;

class IHasZOrderComparator implements Comparator<IHasZOrder>{

	public static final IHasZOrderComparator instance = new IHasZOrderComparator();
	
	@Override
	public int compare(IHasZOrder o1, IHasZOrder o2) {
		return o1.getZOrder() - o2.getZOrder();
	}
	
}