package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * 
 * Vertical composition of elements using their horizontal anchor for alignment.
 * 
 * @author paulk
 *
 */
public class VCat extends Compose {
	
	float vgap;
	float leftAnchor = 0;
	float rightAnchor = 0;
	private static boolean debug = false;

	public VCat(FigurePApplet fpa, IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	@Override
	public
	void bbox(){

		width = 0;
		height = 0;
		leftAnchor = 0;
		rightAnchor = 0;
		vgap = getVGapProperty();
		if(debug)System.err.printf("vertical.bbox: vgap=%f\n", vgap);
		for(Figure fig : figures){
			fig.bbox();
			leftAnchor = max(leftAnchor, fig.leftAnchor());
			rightAnchor = max(rightAnchor, fig.rightAnchor());
			height = height + fig.height;
		}
		
		width = leftAnchor + rightAnchor;
		int ngaps = (figures.length - 1);
		
		height += ngaps * vgap;
		if(debug)System.err.printf("vcat: width=%f, height=%f, leftAnchor=%f, rightAnchor=%f\n", width, height, leftAnchor, rightAnchor);
	}
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);
		
		applyProperties();

		float bottom = top + height;

		// Draw from top to bottom
		for(int i = figures.length-1; i >= 0; i--){
			if(debug)System.err.printf("vertical.draw: i=%d, vgap=%f, bottom=%f\n", i, vgap, bottom);
			Figure fig = figures[i];
			float h = fig.height;
			fig.draw(left + leftAnchor - fig.leftAnchor(), bottom - h);
			bottom -= h + vgap;
		}
	}
	
	@Override
	public float leftAnchor(){
		return leftAnchor;
	}
	
	@Override
	public float rightAnchor(){
		return rightAnchor;
	}
}
