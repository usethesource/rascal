package org.rascalmpl.library.vis.graph.lattice;

/**
 * @author bertl
 * 
 */
public class LinSolve {
	float y;
	float x;
	float e1ToX, e1ToY, e2ToX, e2ToY;
	float e1FromY, e1FromX, e2FromX, e2FromY;
	final float eps = 0.1f;
	final float eps1 = 10f;
	final boolean debug = false;

	class LineEquation {
		final float c;
		final float cx;
		final float cy;

		LineEquation(float eFromX, float eFromY, float eToX, float eToY) {
			cx = eToY - eFromY;
			cy = -(eToX - eFromX);
			c = eFromX * eToY - eFromY * eToX;
			if (debug) {
				float c1 = cx * eFromX + cy * eFromY;
				float c2 = cx * eToX + cy * eToY;
				System.err.println(" c1=" + c1 + " c2=" + c2);
			}
		}
		
		private boolean isOnEdge(LatticeGraphNode n) {
		  final float d = (float) Math.hypot(cx, cy);
		  return Math.abs(cx * n.x + cy * n.y - c) < eps1*d;
		}
	}
	
    private boolean isInsideEdge(LatticeGraphEdge e, LatticeGraphNode n) {
//		System.err.println("" + e.getFrom().x + "<" + n.x + "<" + e.getTo().x);
//		System.err.println("" + e.getFrom().y + "<" + n.y + "<" + e.getTo().y);
    	return isInSegment(e.getFrom().x, e.getTo().x, n.x) && 
    	isInSegment(e.getFrom().y, e.getTo().y, n.y);
	}

	public boolean isOnEdge(LatticeGraphEdge e, LatticeGraphNode n) {
		LineEquation eq = new LineEquation(e.getFrom().x, e.getFrom().y,
				e.getTo().x, e.getTo().y);
		return eq.isOnEdge(n)  && isInsideEdge(e, n);
	}

	public void solve(LatticeGraphEdge e1, LatticeGraphEdge e2) {
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

	public void solve(float e1FromX, float e1FromY, float e1ToX, float e1ToY,
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

	/**
	 * Solves cx1*x+cy1*y=c1 and Solves cx2*x+cy2*y=c2
	 * cx1 = (y1-y0) cy1 = (x1-x0) c1 = x0*y1-y0*x1
	 */
	private void solve() {
		final LineEquation eq1 = new LineEquation(e1FromX, e1FromY, e1ToX,
				e1ToY), eq2 = new LineEquation(e2FromX, e2FromY, e2ToX, e2ToY);
		final float det = (eq1.cx * eq2.cy - eq2.cx * eq1.cy);
		x = (eq1.c * eq2.cy - eq2.c * eq1.cy) / det;
		y = (-eq1.c * eq2.cx + eq2.c * eq1.cx) / det;
		if (debug) {
			final float c1t = eq1.cx * x + eq1.cy * y, c2t = eq2.cx * x
					+ eq2.cy * y;
			System.err.println("c1t=" + c1t + " " + eq1.c);
			assert (Math.abs(eq1.c - c1t) < eps);
			System.err.println("c2t=" + c2t + " " + eq2.c);
			assert (Math.abs(eq2.c - c2t) < eps);
		}
	}

	private boolean isInInterval(float b1, float b2, float x) {
		final float m = Math.min(b1, b2), M = Math.max(b1, b2);
		return m < x && x < M;
	}
	
	private boolean isInSegment(float b1, float b2, float x) {
		final float m = Math.min(b1, b2), M = Math.max(b1, b2);
		return m <= x && x <= M;
	}
	
	public boolean isCuttingPointInside(LatticeGraphEdge e1, LatticeGraphEdge e2) {
		solve(e1, e2);
		return isInside();
	}
	
	
	

	private boolean isInside() {
		// System.err.println(" x="+e1FromX+"<"+x+"<"+e1ToX);
		// System.err.println(" x="+e2FromX+"<"+x+"<"+e2ToX);
		// if (e1FromY < e1ToY)
		// System.err.println(" y=" + e1FromY + "<" + y + "<" + e1ToY);
		if (isInInterval(e1FromX, e1ToX, x) && isInInterval(e2FromX, e2ToX, x)
				&& isInInterval(e1FromY, e1ToY, y)
				&& isInInterval(e2FromY, e2ToY, y))
			return true;
		return false;
	}

	/**
	 * @return the x coordinate of cutting point
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y coordinate of cutting point
	 */
	public float getY() {
		return y;
	}

	public static void main(String[] args) {
		LinSolve s = new LinSolve();
		s.solve(0, -1, 4.2f, 3.7f, 1, 2, 0, 3);
		System.err.println("Solution:" + s.x + " " + s.y + " " + s.isInside());
	}
}
