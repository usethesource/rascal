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
