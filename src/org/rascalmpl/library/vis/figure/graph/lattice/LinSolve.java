/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.lattice;

/**
 * @author bertl
 * 
 */
public class LinSolve {
	double y;
	double x;
	double e1ToX, e1ToY, e2ToX, e2ToY;
	double e1FromY, e1FromX, e2FromX, e2FromY;
	final double eps = 0.1;
	final double eps1 = 10;
	final boolean debug = false;

	class LineEquation {
		final double c;
		final double cx;
		final double cy;

		LineEquation(double eFromX, double eFromY, double eToX, double eToY) {
			cx = eToY - eFromY;
			cy = -(eToX - eFromX);
			c = eFromX * eToY - eFromY * eToX;
			if (debug) {
				double c1 = cx * eFromX + cy * eFromY;
				double c2 = cx * eToX + cy * eToY;
				System.err.println(" c1=" + c1 + " c2=" + c2);
			}
		}
		
		private boolean isOnEdge(LatticeGraphNode n) {
		  final double d = Math.hypot(cx, cy);
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

	public void solve(double e1FromX, double e1FromY, double e1ToX, double e1ToY,
			double e2FromX, double e2FromY, double e2ToX, double e2ToY) {
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
		final double det = (eq1.cx * eq2.cy - eq2.cx * eq1.cy);
		x = (eq1.c * eq2.cy - eq2.c * eq1.cy) / det;
		y = (-eq1.c * eq2.cx + eq2.c * eq1.cx) / det;
		if (debug) {
			final double c1t = eq1.cx * x + eq1.cy * y, c2t = eq2.cx * x
					+ eq2.cy * y;
			System.err.println("c1t=" + c1t + " " + eq1.c);
			assert (Math.abs(eq1.c - c1t) < eps);
			System.err.println("c2t=" + c2t + " " + eq2.c);
			assert (Math.abs(eq2.c - c2t) < eps);
		}
	}

	private boolean isInInterval(double b1, double b2, double x) {
		final double m = Math.min(b1, b2), M = Math.max(b1, b2);
		return m < x && x < M;
	}
	
	private boolean isInSegment(double b1, double b2, double x) {
		final double m = Math.min(b1, b2), M = Math.max(b1, b2);
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
	public double getX() {
		return x;
	}

	/**
	 * @return the y coordinate of cutting point
	 */
	public double getY() {
		return y;
	}

	public static void main(String[] args) {
		LinSolve s = new LinSolve();
		s.solve(0, -1, 4.2f, 3.7f, 1, 2, 0, 3);
		System.err.println("Solution:" + s.x + " " + s.y + " " + s.isInside());
	}
}
