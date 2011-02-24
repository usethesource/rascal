package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;


/**
 * Place elements on fixed grid positions. The width is determined by the width property, height is
 * determined by number of elements. Each element is positioned 
 * - with its anchor on the grid point, when alignAnchors==true
 * - aligned relative to grid point using current alignment values, otherwise.
 * 
 * @author paulk
 *
 */
public class Grid extends Compose {
	
	float leftFig[];
	float topFig[];
	
	float extTop = 0;
	float extBot = 0;
	float extLeft = 0;
	float extRight = 0;
	
	private boolean alignAnchors = false;
	private static boolean debug = true;
	

	public Grid(FigurePApplet fpa, IPropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
		leftFig = new float[elems.length()];
		topFig = new float[elems.length()];
	}
	
	@Override
	public void bbox(){
		alignAnchors = getAlignAnchorsProperty();
		if(alignAnchors)
			bboxAlignAnchors();
		else
			bboxStandard();
	}
	
	public void bboxAlignAnchors(){
		
		width = getWidthProperty();
		height = 0;
		float w = 0;
		int nrow = 0;
		
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		
		int lastRow = (hgap == 0) ? 0 : figures.length / (1 + (int) (width / hgap)) - 1;
		if(debug)System.err.printf("lastRow = %d\n", lastRow);
		
		extTop = 0;
		extBot = 0;
		extLeft = 0;
		extRight = 0;
		
		for(int i = 0; i < figures.length; i++){
			if(w > width){
				nrow++;
				height += vgap;
				w = 0;
			}
			
			Figure fig = figures[i];
			fig.bbox();
			
			if(w == 0)
				extLeft = max(extLeft, fig.leftAnchor());
			if(w + hgap >= width)
				extRight = max(extRight, fig.rightAnchor());
			if(nrow == 0)
				extTop = max(extTop, fig.topAnchor());
			if(nrow == lastRow){
				extBot = max(extBot, fig.bottomAnchor());
			}
			
			if(debug)System.err.printf("i=%d, row=%d, w=%f, extLeft=%f, extRight=%f, extTop=%f, extBot=%f\n", i, nrow, w, extLeft, extRight, extTop, extBot);
			
			leftFig[i] = w;
			topFig[i] = height;
			w += hgap;
		}
		width += extLeft + extRight;
		height += extTop + extBot;
		if(debug)System.err.printf("grid.bbox: %f, %f\n", width, height);
	}
	
	public void bboxStandard(){
		
		width = getWidthProperty();
		height = 0;
		float w = 0;
		int nrow = 0;
		
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		
		float halign = getHalignProperty();
		float valign = getValignProperty();
		
		int lastRow = (hgap == 0) ? 0 : figures.length / (1 + (int) (width / hgap)) - 1;
		if(debug)System.err.printf("lastRow = %d\n", lastRow);
		
		extTop = 0;
		extBot = 0;
		extLeft = 0;
		extRight = 0;
		
		for(int i = 0; i < figures.length; i++){
			if(w > width){
				nrow++;
				height += vgap;
				w = 0;
			}
			
			Figure fig = figures[i];
			fig.bbox();
			
			if(w == 0)
				extLeft = max(extLeft, halign * fig.width);
			if(w + hgap >= width)
				extRight = max(extRight, (1 - halign) * fig.width);
			if(nrow == 0)
				extTop = max(extTop, valign * fig.height);
			if(nrow == lastRow){
				extBot = max(extBot, (1 - valign) * fig.height);
			}
			
			if(debug)System.err.printf("i=%d, row=%d, w=%f, extLeft=%f, extRight=%f, extTop=%f, extBot=%f\n", i, nrow, w, extLeft, extRight, extTop, extBot);
			
			leftFig[i] = w;
			topFig[i] = height;
			w += hgap;
		}
		width += extLeft + extRight;
		height += extTop + extBot;
		if(debug)System.err.printf("grid.bbox: %f, %f\n", width, height);
	}
	
	@Override
	public
	void draw(float left, float top){
		setLeft(left);
		setTop(top);
	
		applyProperties();
		if(alignAnchors){
			for(int i = 0; i < figures.length; i++){
				Figure fig = figures[i];
				if(debug)System.err.printf("i=%d: %f, %f, left=%f, top=%f\n", i, leftFig[i], topFig[i], left, top);
				fig.draw(left + extLeft + leftFig[i] - fig.leftAnchor(), top + extTop + topFig[i] - fig.topAnchor());
			}
		} else {
			
			float hgap = getHGapProperty();
			float vgap = getVGapProperty();
			
			float halign = getHalignProperty();
			float valign = getValignProperty();
			
			System.err.printf("hanchor=%f, vanchor=%f\n", halign, valign);
			
			for(int i = 0; i < figures.length; i++){
				Figure fig = figures[i];
				if(debug)System.err.printf("i=%d: %f, %f, left=%f, top=%f\n", i, leftFig[i], topFig[i], left, top);
				fig.draw(left + extLeft + leftFig[i] - halign * fig.width, 
						 top  + extTop  + topFig[i]  - valign * fig.height);
			}
		}
	}
}
