package org.rascalmpl.library.vis.swt;

import org.rascalmpl.library.vis.Figure;
public interface ISWTZOrdering {
	public void pushOverlap();
	public void popOverlap();
	public void register(Figure fig);
	public void registerOverlap(Figure fig);
}
