package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;


/**
 * HVCat elements on consecutive rows. Width is determined by the width property, height is
 * determined by the number and size of the elements. This is similar to aligning words in
 * a text but is opposed to composition in a grid, where the elements are placed on fixed
 * grid positions.
 * 
 * @author paulk
 *
 */
public class HVCat extends Compose {
	
	float leftElem[];
	float topRowElem[];
	float rowHeight[];
	float rowWidth[];
	int inRow[];
	static boolean debug = false;

	public HVCat(FigurePApplet fpa, PropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
		leftElem = new float[elems.length()];
		topRowElem = new float[elems.length()];
		rowHeight = new float[elems.length()];
		rowWidth = new float[elems.length()];
		inRow = new int[elems.length()];
	}
	
	@Override
	public
	void bbox(){
		
		width = getWidthProperty();
		height = 0;
		float w = 0;
		float hrow = 0;
		float toprow = 0;
		int nrow = 0;
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		for(int i = 0; i < figures.length; i++){
			Figure ve = figures[i];
			ve.bbox();
			if(w + hgap + ve.width > width){
				if(w == 0){
					width = ve.width;
				} else {
					rowHeight[nrow] = hrow;
					rowWidth[nrow] = w;
					nrow++;
					height += hrow + vgap;
					toprow = height;
					w = hrow = 0;
				}
			}
			leftElem[i] = w;
			topRowElem[i] = toprow;
			inRow[i] = nrow;
			w += ve.width + hgap;
			hrow = max(hrow, ve.height);
	
		}
		rowHeight[nrow] = hrow;
		rowWidth[nrow] = w;
		height += hrow;
		if(nrow == 0)
			width = w - hgap;
		if(debug)System.err.printf("HVCat.bbox: width=%f, height=%f\n", width, height);
	}
	
	@Override
	public
	void draw(float left, float top){
		this.left = left;
		this.top = top;
		
		applyProperties();

		for(int i = 0; i < figures.length; i++){
			
			Figure fig = figures[i];
			float hrow = rowHeight[inRow[i]];
			float rfiller = width - rowWidth[inRow[i]];
			
			fig.draw(left + leftElem[i] + fig.properties.hanchor*rfiller,
                    top + topRowElem[i] + fig.properties.vanchor *(hrow - fig.height));                  
		}
	}
}
