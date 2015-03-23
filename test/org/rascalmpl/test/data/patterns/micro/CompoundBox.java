package org.rascalmpl.test.data.patterns.micro;

public class CompoundBox {

	private Canopy c;
	private int i;

	public CompoundBox(Canopy c, int i) {
		this.c = c;
		this.i = i;
	}
	
	public Canopy toCanopy() {
		return c;
	}
	
}
