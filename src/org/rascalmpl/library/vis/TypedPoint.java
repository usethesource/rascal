package org.rascalmpl.library.vis;

class TypedPoint {
	
	enum kind {
		CURVED, NORMAL, BEZIER;
	}
	final float x, y;
	final kind curved;

	TypedPoint(float x, float y, kind curved) {
		this.x = x;
		this.y = y;
		this.curved = curved;
	}
}
