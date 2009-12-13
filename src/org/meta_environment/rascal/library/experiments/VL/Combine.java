package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Combine extends Compose {

	Combine(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		width = 0;
		height = 0;
		for(VELEM ve : velems){
			ve.bbox();
			if(isHorizontal()){
				width += ve.width;
				height = max(height, ve.height);
			} else {
				width = max(width, ve.width);
				height = height + ve.height;
			}
		} 
		int ngaps = (velems.length - 1);
		if(isHorizontal())
			width += ngaps * getHGapProperty();
		else
			height += ngaps * getVGapProperty();
	}
	
	@Override
	void draw(){
		
		applyProperties();

		if(isHorizontal()){
			int hgap = getHGapProperty();
			float bottom = top + height;
			float veTop;
			for(VELEM ve : velems){
				if(isTopAligned())
					veTop = top;
				else if(isBottomAligned())
					veTop = bottom - ve.height;
				else
					veTop = top + (height - ve.height)/2;
				
				ve.draw(left, veTop);
				left += ve.width + hgap;
			}
		} else {
			int vgap = getVGapProperty();
			float right = left + width;
			float bottom = top + height;
			float veLeft;
			for(VELEM ve : velems){
				if(isRightAligned())
					veLeft = right - ve.width;
				else if(isLeftAligned())
					veLeft = left;
				else
					veLeft = left + (width - ve.width)/2;
			
				float h = ve.height;
				ve.draw(veLeft, bottom - h);
				bottom -= h + vgap;
			}
		}
	}

	@Override
	void bbox() {
		bbox(0,0);
		
	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		draw();
	}
	

}
