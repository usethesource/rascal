package org.rascalmpl.library.vis.graph.lattice;

public class LinSolve {
	float y;
	float x;
	final float e1ToX, e1ToY, e2ToX, e2ToY;
	final float e1FromY, e1FromX, e2FromX, e2FromY;
	final float eps = 0.001f;

	public LinSolve(LatticeGraphEdge ee1, LatticeGraphEdge ee2) {
		final LatticeGraphEdge e1 = ee1, e2 = ee2;
		e1ToY = e1.getTo().y;
		e1ToX = e1.getTo().x;
		e2ToX = e2.getTo().x;
		e2ToY = e2.getTo().y;
		e1FromY = e1.getFrom().y;
		e1FromX = e1.getFrom().x;
		e2FromX = e2.getFrom().x;
		e2FromY = e2.getFrom().y;
		solve();
	}

	public LinSolve(float e1FromX, float e1FromY, float e1ToX, float e1ToY,
			float e2FromX, float e2FromY, float e2ToX, float e2ToY) {
		this.e1FromX = e1FromX;
		this.e1FromY = e1FromY;
		this.e1ToX = e1ToX;
		this.e1ToY = e1ToY;
		this.e2FromX = e2FromX;
		this.e2FromY = e2FromY;
		this.e2ToX = e2ToX;
		this.e2ToY = e2ToY;
		solve();
	}

	/*
	 * Eq line through (x0, y0) and (y1, x1)
	 * x(y1-y0)-y(x1-x0)=x0(y1-y0)-y0(x1-x0)
	 */
	private void solve() {
		final float c1x = e1ToY - e1FromY, c1y = e1ToX - e1FromX;
		final float c2x = e2ToY - e2FromY, c2y = e2ToX - e2FromX;
		final float c1 = e1FromX * e1ToY - e1FromX * e1FromY - e1FromY * e1ToX
				+ e1FromY * e1FromX;
		final float c2 = e2FromX * e2ToY - e2FromX * e2FromY - e2FromY * e2ToX
				+ e2FromY * e2FromX;
		/*
		System.err.println("c1x="+c1x);
		System.err.println("c1y="+c1y);
		System.err.println("c2x="+c2x);
		System.err.println("c2y="+c2y);
		System.err.println("c1="+c1);
		System.err.println("c2="+c2);
		*/
		if (Math.abs(c1x) < eps && Math.abs(c2x) < eps || Math.abs(c1x) < eps && Math.abs(c2x) < eps) {
			x = 0;
			y = -1;
			return;
		}
		if (Math.abs(c2x) > eps && Math.abs(c2y) > eps
				&& Math.abs(c1x / c2x - c1y / c2y) < eps) {
			x = 0;
			y = -1;
			return;
		}
		if (Math.abs(c1x) > eps && Math.abs(c1y) > eps
				&& Math.abs(c2x / c1x - c2y / c1y) < eps) {
			x = 0;
			y = -1;
			return;
		}	
		y = (c1 * c2x - c2 * c1x) / (-c1y * c2x + c2y * c1x);
		x = (c1 * c2y - c2 * c1y) / (c1x * c2y - c2x * c1y);
	}

	boolean isInside() {
		return y < e1FromY && y > e1ToY;
	}

	public static void main(String[] args) {
		LinSolve s = new LinSolve(0, -1, 4, 3, 1, 2, 0, 3);
		System.err.println("Solution:" + s.x + " " + s.y + " " + s.isInside());
	}
}
