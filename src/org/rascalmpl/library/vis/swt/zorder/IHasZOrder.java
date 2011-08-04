package org.rascalmpl.library.vis.swt.zorder;

import org.eclipse.swt.widgets.Control;

public interface IHasZOrder {
	
	void setZOrder(int depth);
	int getZOrder();
	Control getElement();
	int getStableOrder();
	void setVisible(boolean visible);
}
