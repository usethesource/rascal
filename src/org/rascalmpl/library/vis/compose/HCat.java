package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;

public class HCat extends Compose {

	float gapSize;
	float numberOfGaps;
	float topAnchor = 0;
	float bottomAnchor = 0;
	float minTopAnchor = Float.MAX_VALUE;
	private float maxTopAnchor;
	private static boolean debug = true;
	
	boolean isWidthPropertySet, isHeightPropertySet, isHGapPropertySet, isHGapFactorPropertySet;
	float getWidthProperty, getHeightProperty, getHGapProperty, getHGapFactorProperty, getValignProperty;

	
	public HCat(IFigureApplet fpa, PropertyManager properties, IList elems,  IList childProps,  IEvaluatorContext ctx) {
		super(fpa, properties, elems, childProps, ctx);
	}
	
	void setProperties(){
		isWidthPropertySet = isWidthPropertySet();
		isHeightPropertySet = isHeightPropertySet();
		isHGapPropertySet = isHGapPropertySet();
		isHGapFactorPropertySet = isHGapFactorPropertySet();
		
		getWidthProperty = getWidthProperty();
		getHeightProperty = getHeightProperty();
		getHGapProperty = getHGapProperty();
		getHGapFactorProperty = getHGapFactorProperty();
	}
	
	float getFigureWidth(Figure fig){ return fig.width; }
	float getFigureHeight(Figure fig){return fig.height;}
	float getTopAnchor(Figure fig){return fig.topAlign();}
	float getTopAnchorProperty(Figure fig){return fig.getRealProperty(RealProp.VALIGN);}
	float getBottomAnchor(Figure fig){return fig.bottomAlign();}
	void  drawFigure(Figure fig,float left,float top,float leftBase,float topBase){
		fig.draw(leftBase + left, topBase + top);
	}
	void  bboxOfFigure(Figure fig,float desiredWidth,float desiredHeight){ fig.bbox(desiredWidth, desiredHeight);}
	float getHeight(){return height;}
	
	public
	void bbox(float desiredWidth, float desiredHeight){
		width = height = 0;
		gapSize = getHGapProperty;
		numberOfGaps = (figures.length - 1);
		setProperties();
		if(getStartGapProperty()){numberOfGaps+=0.5f;} 
		if(getEndGapProperty()){numberOfGaps+=0.5f;} 
		if(isWidthPropertySet) desiredWidth = getWidthProperty;
		if(isHeightPropertySet) desiredHeight = getHeightProperty;
		float desiredWidthPerElement, desiredWidthOfElements, desiredHeightPerElement, gapsSize;
		gapsSize = 0.0f; // stops compiler from whining
		if(isHGapPropertySet && !isHGapFactorPropertySet){
			gapsSize = getHGapProperty;
		}
		// determine desired width of elements 
		if(desiredWidth == Figure.AUTO_SIZE){
			desiredWidthOfElements = desiredWidthPerElement = Figure.AUTO_SIZE;
		} else {
			if(isHGapFactorPropertySet || !isHGapPropertySet){
				gapsSize = desiredWidth * getHGapFactorProperty;
			}
			desiredWidthOfElements = desiredWidth - gapsSize;
			desiredWidthPerElement = desiredWidthOfElements / figures.length;
		}
		// deterine desired height of elements
		if(desiredHeight == Figure.AUTO_SIZE){
			desiredHeightPerElement = Figure.AUTO_SIZE;
		} else {
			float minTopAnchorProp, maxTopAnchorProp;
			minTopAnchorProp = Float.MAX_VALUE;
			maxTopAnchorProp = 0.0f;
			for(Figure fig : figures){
				minTopAnchorProp = min(minTopAnchorProp,getTopAnchorProperty(fig));
				maxTopAnchorProp = max(maxTopAnchorProp,getTopAnchorProperty(fig));
			}
			float maxDiffTopAnchorProp = maxTopAnchorProp - minTopAnchorProp;
			// first assume all elements will get desiredheight
			// rewrote : desiredHeight = desiredHeightPerElement + maxDiffTopAnchorProp* desiredHeightPerElement
			desiredHeightPerElement = desiredHeight / (1 + maxDiffTopAnchorProp);
		}
		// determine width and bounding box elements and which are non-resizeable
		int numberOfNonResizeableWidthElements = 0;
		float totalNonResizeableWidth = 0;
		boolean[] mayBeResized = new boolean[figures.length];
		for(int i = 0 ; i < figures.length ; i++){
			mayBeResized[i] = true;
		}
		
		// assume width and height are independent(!)
		boolean fixPointReached;
		// fixpoint computation to set width of figures...
		do{
			fixPointReached = true;
			for(int i = 0 ; i < figures.length; i++){
				if(mayBeResized[i]){
					bboxOfFigure(figures[i],desiredWidthPerElement,desiredHeightPerElement);
					if(desiredWidthPerElement != Figure.AUTO_SIZE 
							&& getFigureWidth(figures[i]) != desiredWidthPerElement ){
						totalNonResizeableWidth+=getFigureWidth(figures[i]);
						numberOfNonResizeableWidthElements++;
						mayBeResized[i]=false;
						fixPointReached = false;
					} 
				}
			}
			// recompute width of resizeable elements
			if(numberOfNonResizeableWidthElements > 0 && desiredWidth!=Figure.AUTO_SIZE){
				desiredWidthPerElement = (desiredWidthOfElements - totalNonResizeableWidth) 
				                              / (figures.length - numberOfNonResizeableWidthElements);
			}
		} while(!fixPointReached);
		// TODO: Fixpoint for height depending on alignment?
		float totalElementsWidth = 0;
		float maxBottomAnchor = 0.0f;
		maxTopAnchor = 0.0f;
		minTopAnchor = Float.MAX_VALUE;
		for(Figure fig : figures){
			totalElementsWidth += getFigureWidth(fig);
			maxBottomAnchor = max(height,getBottomAnchor(fig));
			maxTopAnchor = max(maxTopAnchor,getTopAnchor(fig));
			minTopAnchor = min(minTopAnchor,getTopAnchor(fig));
		}
		if(desiredWidth != Figure.AUTO_SIZE && 
		   (int)(totalElementsWidth + gapsSize + 0.5f) > (int)(desiredWidth + 0.5f)){ // prevent round-off cannot fit error
			if(debug) if(debug)System.err.printf("Cannot fit!");
			gapsSize = max(desiredWidth -totalElementsWidth,0.0f);
		}
		// compute gap if auto-size and ratio
		if(desiredWidth==Figure.AUTO_SIZE && (isHGapFactorPropertySet || !isHGapPropertySet)){
			// the next formula can be obtained by rewriting hGapFactor = gapsSize / (totalElementsWidth + gapsSize) 
			gapsSize = totalElementsWidth /  (1/getHGapFactorProperty - 1);
		}
		width = totalElementsWidth + gapsSize;
		gapSize = gapsSize / numberOfGaps;
		height =  maxTopAnchor + maxBottomAnchor;
		if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f minTopAnchor %f maxTopAnchor %f\n", width, height, topAnchor, bottomAnchor,minTopAnchor,maxTopAnchor);
		determinePlacement();
	}

	private void determinePlacement() {
		float left, top;
		left = top = 0.0f;
		if(getStartGapProperty()){
			left+=0.5*gapSize;
		}
			// Draw from left to right
		for(int i = 0 ; i < figures.length ; i++){
			xPos[i] = left;
			yPos[i] = top - getTopAnchor(figures[i]) + maxTopAnchor;
			left += getFigureWidth(figures[i]) + gapSize;
		}
	}	
}
