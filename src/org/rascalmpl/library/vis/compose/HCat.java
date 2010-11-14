package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;

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
	private static boolean debug = false;

	public HCat(FigurePApplet fpa, PropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	@Override
	public
	void bbox(){
		width = 0;
		height = 0;
		topAnchor = 0;
		bottomAnchor = 0;
		hgap = getHGapProperty();
		for(Figure fig : figures){
			fig.bbox();
			width += fig.width;
			topAnchor = max(topAnchor, fig.topAnchor());
			bottomAnchor = max(bottomAnchor, fig.bottomAnchor());
		} 
		int ngaps = (figures.length - 1);
		width += ngaps * hgap;
		height = topAnchor + bottomAnchor;
		
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	@Override
	public
	void draw(float left, float top){
		this.left = left;
		this.top =  top;
	
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
