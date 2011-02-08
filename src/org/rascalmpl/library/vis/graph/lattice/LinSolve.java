package org.rascalmpl.library.vis.graph.lattice;

public class LinSolve {
	float y;
	float x;
	final float e1ToX, e1ToY, e2ToX, e2ToY;
	final float e1FromY, e1FromX, e2FromX, e2FromY;

	public LinSolve(LatticeGraphEdge ee1, LatticeGraphEdge ee2) {
		final LatticeGraphEdge e1=ee1, e2=ee2;
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
	
	public LinSolve(float e1FromX, float e1FromY, float e1ToX, float e1ToY, float e2FromX, float e2FromY, float e2ToX, float e2ToY){
		this.e1FromX = e1FromX;
		this.e1FromY= e1FromY;
		this.e1ToX = e1ToX;
		this.e1ToY = e1ToY;
		this.e2FromX = e2FromX;
		this.e2FromY = e2FromY;
		this.e2ToX = e2ToX;
		this.e2ToY = e2ToY;
		solve();
	}

	private void solve() {
		final float c1x = e1ToY - e1FromY, c1y = e1ToX - e1FromX;
		final float c2x = e2ToY - e2FromY, c2y = e2ToX - e2FromX;
		final float c1 = e1FromX * e1ToY - e1FromX * e1ToY - e1FromY * e1ToX
				+ e1FromY * e1FromX;
		final float c2 = e2FromX * e2ToY - e2FromX * e2ToY - e2FromY * e2ToX
				+ e2FromY * e2FromX;
		y = (c1 * c2x - c2 * c1x) / (-c1y * c2x + c2y * c1x);
		x = (c1 * c2y - c2 * c1y) / (c1x * c2y - c2x * c1y);
	}

	boolean isInside() {
		return y * y <= (e1FromY - e1ToY) * (e1FromY - e1ToY);
	}

	public static void main(String[] args) {
		LinSolve s = new LinSolve(0,0, 1, 1, 0,3, 3, 0);
		System.err.println("Solution:"+s.x+" "+s.y+" "+s.isInside());
	}
}
