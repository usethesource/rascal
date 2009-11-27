package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Vertex extends VELEM {
	VELEM marker;
	int deltax;
	int deltay;

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
		System.err.println("Point with : " + marker);
	}

	@Override
	BoundingBox bbox(){
		if(marker != null){
			BoundingBox bb = marker.bbox();
			width = bb.getWidth();
			height = deltay + bb.getHeight()/2;
		} else {
			width = deltax;
			height = deltay;
		}
		System.err.printf("bbox.point: %f, %f)\n", width, height);
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y) {
		this.x = x;
		this.y = y;
		applyProperties();
		System.err.println("Point: marker = " + marker);
		System.err.printf("Point: marker at %f, %f\n", x + deltax/2, y + deltay/2);
		if(marker != null){
			marker.bbox();
			marker.draw(x, y);
		}
	}

}
