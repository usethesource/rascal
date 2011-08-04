package org.rascalmpl.library.vis.swt.zorder;

import java.util.Comparator;

public class IHasZOrderStableComparator implements Comparator<IHasZOrder>{

	public static final IHasZOrderStableComparator instance = new IHasZOrderStableComparator();
	
	@Override
	public int compare(IHasZOrder o1, IHasZOrder o2) {
		return o2.getStableOrder() - o1.getStableOrder();
	}
	
}
