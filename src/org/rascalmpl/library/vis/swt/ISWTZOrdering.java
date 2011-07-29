package org.rascalmpl.library.vis.swt;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.compose.Overlap;
public interface ISWTZOrdering {
	public void pushOverlap();
	public void popOverlap();
	public void register(Figure fig);
	public void registerOverlap(Overlap fig);
}
