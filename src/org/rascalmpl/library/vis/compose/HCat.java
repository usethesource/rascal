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
	private boolean useChildAnchors = false;
	private static boolean debug = true;

	public HCat(FigurePApplet fpa, IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
	}
	
	@Override
	public
	void bbox(){
		if(useChildAnchors)
			bbox1();
		else
			bbox2();
	}
	
	public
	void bbox1(){
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
	
	public
	void bbox2(){
		width = 0;
		height = 0;
		
		float vanchor = getVanchor();
		
		hgap = getHGapProperty();
		for(Figure fig : figures){
			fig.bbox();
			width += fig.width;
			height = max(height, fig.height);
			if(debug)System.err.printf("hcat (loop): topAnchor=%f, bottomAnchor=%f\n", topAnchor, bottomAnchor);
		} 
		int ngaps = (figures.length - 1);
		width += ngaps * hgap;
		topAnchor = vanchor * height;
		bottomAnchor = (1 - vanchor) * height;
		
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);
	
		applyProperties();
		if(useChildAnchors){
			// Draw from left to right
			for(Figure fig : figures){
				fig.draw(left, top + topAnchor - fig.topAnchor());
				left += fig.width + hgap;
			}
		} else {
			float vanchor = getVanchor();
			for(Figure fig : figures){
				float vpad = vanchor * (height - fig.height);
				fig.draw(left, top + vpad);
				left += fig.width + hgap;
			}
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
