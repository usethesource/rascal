package org.rascalmpl.library.vis.swt;

public interface Animation {
	// returns true on more frames, false else
	boolean moreFrames();
	void animate();
}
