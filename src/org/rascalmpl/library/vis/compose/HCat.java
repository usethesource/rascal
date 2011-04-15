package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class HCat extends Compose {

	float gapSize;
	float numberOfGaps;
	float topAnchor = 0;
	float bottomAnchor = 0;
	private boolean alignAnchors = false;
	private static boolean debug = true;
	
	boolean isWidthPropertySet, isHeightPropertySet, isHGapPropertySet, isHGapFactorPropertySet;
	float getWidthProperty, getHeightProperty, getHGapProperty, getHGapFactorProperty, getValignProperty;
	
	public HCat(IFigureApplet fpa, PropertyManager properties, IList elems, IEvaluatorContext ctx) {
		super(fpa, properties, elems, ctx);
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
		getValignProperty = getValignProperty();
	}
	
	float getFigureWidth(Figure fig){ return fig.width; }
	float getFigureHeight(Figure fig){return fig.height;}
	float getTopAnchor(Figure fig){return fig.topAnchor();}
	float getBottomAnchor(Figure fig){return fig.bottomAnchor();}
	void  drawFigure(Figure fig,float left,float top,float leftBase,float topBase){
		fig.draw(leftBase + left, topBase + top);
	}
	void  bboxOfFigure(Figure fig,float desiredWidth,float desiredHeight){ fig.bbox(desiredWidth, desiredHeight);}
	float getHeight(){return height;}
	
	public
	void bbox(float desiredWidth, float desiredHeight){
		width = height = 0;
		topAnchor = bottomAnchor = 0;
		gapSize = getHGapProperty;
		numberOfGaps = (figures.length - 1);
		setProperties();
		if(getStartGapProperty()){numberOfGaps+=0.5f;} 
		if(getEndGapProperty()){numberOfGaps+=0.5f;} 
		
		alignAnchors = getAlignAnchorsProperty();
		if(alignAnchors)
			bboxAlignAnchors();
		else
			bboxStandard(desiredWidth,desiredHeight);
	}
	
	public
	void bboxAlignAnchors(){
		for(Figure fig : figures){
			fig.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
			width += getFigureWidth(fig);
			topAnchor = max(topAnchor, getTopAnchor(fig));
			bottomAnchor = max(bottomAnchor, getBottomAnchor(fig));
			//if(debug)System.err.printf("hcat (loop): topAnchor=%f, bottomAnchor=%f\n", topAnchor, bottomAnchor);
		} 
		width += numberOfGaps * gapSize;
		height = topAnchor + bottomAnchor;
		
		//if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f\n", width, height, topAnchor, bottomAnchor);
	}	
	
	
	
	public
	void bboxStandard(float desiredWidth, float desiredHeight){
		float valign = getValignProperty;
		if(isWidthPropertySet) desiredWidth = getWidthProperty;
		if(isHeightPropertySet) desiredHeight = getHeightProperty;
		float desiredWidthPerElement, desiredWidthOfElements, gapsSize;
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
		// determine width and bounding box elements and which are non-resizeable
		int numberOfNonResizeableElements = 0;
		float totalNonResizeableWidth = 0;
		boolean[] mayBeResized = new boolean[figures.length];
		for(int i = 0 ; i < figures.length ; i++){
			mayBeResized[i] = true;
		}
		boolean fixPointReached;
		do{
			fixPointReached = true;
			for(int i = 0 ; i < figures.length; i++){
				if(mayBeResized[i]){
					bboxOfFigure(figures[i],desiredWidthPerElement,desiredHeight);
					//figures[i].bbox(desiredWidthPerElement, desiredHeight);
					if(getFigureWidth(figures[i]) != desiredWidthPerElement && desiredWidthPerElement != Figure.AUTO_SIZE){
						totalNonResizeableWidth+=getFigureWidth(figures[i]);
						numberOfNonResizeableElements++;
						mayBeResized[i]=false;
						fixPointReached = false;
					} 
				}
			}
			// recompute width of resizeable elements
			if(numberOfNonResizeableElements > 0 && desiredWidth!=Figure.AUTO_SIZE){
				desiredWidthPerElement = (desiredWidthOfElements - totalNonResizeableWidth) 
				                              / (figures.length - numberOfNonResizeableElements);
			}
		} while(!fixPointReached);
		float totalElementsWidth = 0;
		// compute total width and height
		for(Figure fig : figures){
			totalElementsWidth += getFigureWidth(fig);
			height = max(height,getFigureHeight(fig));
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
		gapSize = gapsSize / numberOfGaps;
		topAnchor = valign * height;
		bottomAnchor = (1 - valign) * height;
		//if(debug)System.err.printf("hcat: width=%f, height=%f, topAnchor=%f, bottomAnchor=%f hgap=%f\n", width, height, topAnchor, bottomAnchor,hgap);
	}	
	
	public
	void draw(float leftBase, float topBase){
		this.setLeft(leftBase);
		this.setTop(topBase);
		float left, top;
		left = top = 0.0f;
		applyProperties();
		if(getStartGapProperty()){
			left+=0.5*gapSize;
		}
		float valign = getValignProperty;
			// Draw from left to right
		for(Figure fig : figures){
			float topOffset;
			if(alignAnchors){
				topOffset =  topAnchor - getTopAnchor(fig);
			} else {
				topOffset =valign * (getHeight() - getFigureHeight(fig));
			}
			drawFigure(fig,left,top + topOffset,leftBase,topBase);
			left += getFigureWidth(fig) + gapSize;
		}
	}
	
	
	
	public float topAnchor(){ return topAnchor; }
	public float bottomAnchor(){ return bottomAnchor; }
	
	
}
