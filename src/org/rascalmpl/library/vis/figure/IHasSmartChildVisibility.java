/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure;

import java.util.List;

import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public interface IHasSmartChildVisibility {
	void drawVisibleChildrenSmart(
			List<IHasSWTElement> visibleSWTElements, GraphicsContext gc,
			Rectangle part) ;

	void getFiguresUnderMouseSmart(Coordinate c, List<Figure> result);

}
