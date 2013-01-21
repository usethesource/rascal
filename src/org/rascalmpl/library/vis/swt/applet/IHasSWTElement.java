/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
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
