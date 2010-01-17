package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Vertex extends VELEM {
	VELEM marker;
	int deltax;
	int deltay;
	private static boolean debug = false;

	public Vertex(VLPApplet vlp, IInteger dx, IInteger dy, IEvaluatorContext ctx) {
		super(vlp, ctx);
		deltax = dx.intValue();
		deltay = dy.intValue();
	}
	
	public Vertex(VLPApplet vlp, IInteger dx, IInteger dy, IConstructor marker, IEvaluatorContext ctx) {
		super(vlp, ctx);
		deltax = dx.intValue();
		deltay = dy.intValue();
		this.marker = VELEMFactory.make(vlp, marker, properties, ctx);
		if(debug)System.err.println("Point with : " + marker);
	}

	@Override
	void bbox(int left, int top){
		this.left = left;
		this.top = top;
		if(marker != null){
			//TODO this is wrong
			marker.bbox();
			width = marker.width;
			height = deltay + marker.height/2;
		} else {
			width = deltax;
			height = deltay;
		}
		if(debug)System.err.printf("bbox.point: %f, %f)\n", width, height);
	}
	
	@Override
	void draw() {
		
		applyProperties();
		if(debug){
			System.err.println("Point: marker = " + marker);
			System.err.printf("Point: marker at %d, %d\n", left, top);
		}
		if(marker != null){
			marker.bbox();
			marker.draw(left-marker.width/2, top-marker.height/2);
		}
	}

}
