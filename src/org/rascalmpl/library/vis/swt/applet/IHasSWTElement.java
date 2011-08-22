package org.rascalmpl.library.vis.swt.applet;

import java.util.Comparator;

import org.eclipse.swt.widgets.Control;


public interface IHasSWTElement {
	Control getControl();
	void setVisible(boolean visible);
	int getStableOrder();
	
	
	public static class ICanBeInvisibleComparator implements Comparator<IHasSWTElement>{

		public static final ICanBeInvisibleComparator instance = new ICanBeInvisibleComparator();
		
		@Override
		public int compare(IHasSWTElement o1, IHasSWTElement o2) {
			return o2.getStableOrder() - o1.getStableOrder();
		}
	}
}
