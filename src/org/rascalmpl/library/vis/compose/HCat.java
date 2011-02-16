package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

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

	public HCat(FigurePApplet fpa, IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
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
			if(debug)System.err.printf("hcat (loop): topAnchor=%f, bottomAnchor=%f\n", topAnchor, bottomAnchor);
		} 
		int ngaps = (figures.length - 1);
		width += ngaps * hgap;
		height = topAnchor + bottomAnchor;
		
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);
	
		applyProperties();

		// Draw from left to right
		for(Figure fig : figures){
			fig.draw(left, top + topAnchor - fig.topAnchor());
			left += fig.width + hgap;
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
