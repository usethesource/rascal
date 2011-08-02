package org.rascalmpl.library.vis.swt.zorder;

import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.containers.Overlap;
import org.rascalmpl.library.vis.interaction.MouseOver;
import org.rascalmpl.library.vis.swtwidgets.SWTWidgetFigure;
public interface ISWTZOrdering {
	public void pushOverlap();
	public void popOverlap();
	public void register(Figure fig);
	public void registerOverlap(Overlap nonLocalFigure);
	public void registerMouseOver(MouseOver mouseOver);
	public void registerControl(SWTWidgetFigure c);
}
