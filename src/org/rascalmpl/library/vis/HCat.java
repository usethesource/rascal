package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * Horizontal composition of elements, using their vertical anchor for alignment
 * 
 * @author paulk
 *
 */
public class HCat extends Compose {
	
	float hgap;
	float topAnchor = 0;
	float bottomAnchor = 0;
	private static boolean debug = true;

	HCat(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(){
		width = 0;
		height = 0;
		topAnchor = 0;
		bottomAnchor = 0;
		hgap = getHGapProperty();
		for(Figure ve : figures){
			ve.bbox();
			width += ve.width;
			topAnchor = max(topAnchor, ve.topAnchor());
			bottomAnchor = max(bottomAnchor, ve.bottomAnchor());
		} 
		int ngaps = (figures.length - 1);
		width += ngaps * hgap;
		height = topAnchor + bottomAnchor;
		
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	@Override
	void draw(float left, float top){
		this.left = left;
		this.top =  top;
		left += leftDragged;
		top  += topDragged;
		applyProperties();

		// Draw from left to right
		for(Figure ve : figures){
			ve.draw(left, top + topAnchor - ve.topAnchor());
			left += ve.width + hgap;
		}
	}
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}
}
