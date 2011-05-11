package org.rascalmpl.library.vis.compose;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.DimensionalProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;

public class HCat extends Compose {

	double gapSize;
	double numberOfGaps;
	double minTopAnchor = Double.MAX_VALUE;
	private double maxTopAnchor;
	private static boolean debug = true;
	
	boolean isWidthPropertySet, isHeightPropertySet, isHGapPropertySet, isHGapFactorPropertySet;
	double getWidthProperty, getHeightProperty, getHGapProperty, getHGapFactorProperty, getValignProperty;

	
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
	
	double getFigureWidth(Figure fig){ return fig.width; }
	double getFigureHeight(Figure fig){return fig.height;}
	double getTopAnchor(Figure fig){return fig.topAlign();}
	double getTopAnchorProperty(Figure fig){return fig.getRealProperty(RealProp.VALIGN);}
	double getBottomAnchor(Figure fig){return fig.bottomAlign();}
	void  drawFigure(Figure fig,double left,double top,double leftBase,double topBase){
		fig.draw(leftBase + left, topBase + top);
	}
	void  bboxOfFigure(Figure fig,double desiredWidth,double desiredHeight){ fig.bbox(desiredWidth, desiredHeight);}
	double getHeight(){return height;}
	
	public
	void bbox(double desiredWidth, double desiredHeight){
		width = height = 0;
		gapSize = getHGapProperty;
		numberOfGaps = (figures.length - 1);
		setProperties();
		if(getStartGapProperty()){numberOfGaps+=0.5f;} 
		if(getEndGapProperty()){numberOfGaps+=0.5f;} 
		if(isWidthPropertySet) desiredWidth = getWidthProperty ;
		if(isHeightPropertySet) desiredHeight = getHeightProperty ;
		double desiredWidthPerElement, desiredWidthOfElements, desiredHeightPerElement, gapsSize;
		gapsSize = 0.0f; // stops compiler from whining
		if(isHGapPropertySet && !isHGapFactorPropertySet){
			gapsSize = getHGapProperty ;
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
			double minTopAnchorProp, maxTopAnchorProp;
			minTopAnchorProp = Double.MAX_VALUE;
			maxTopAnchorProp = 0.0f;
			for(Figure fig : figures){
				minTopAnchorProp = min(minTopAnchorProp,getTopAnchorProperty(fig));
				maxTopAnchorProp = max(maxTopAnchorProp,getTopAnchorProperty(fig));
			}
			double maxDiffTopAnchorProp = maxTopAnchorProp - minTopAnchorProp;
			// first assume all elements will get desiredheight
			// rewrote : desiredHeight = desiredHeightPerElement + maxDiffTopAnchorProp* desiredHeightPerElement
			desiredHeightPerElement = desiredHeight / (1 + maxDiffTopAnchorProp);
		}
		// determine width and bounding box elements and which are non-resizeable
		int numberOfNonResizeableWidthElements = 0;
		double totalNonResizeableWidth = 0;
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
		// Fixpoint for height depending on alignment?
		if(desiredHeight != AUTO_SIZE){
			double maxTopAnchor, maxBottomAnchor;
			double maxTopAnchorR, maxBottomAnchorR;
			maxTopAnchor = maxBottomAnchor = maxTopAnchorR = maxBottomAnchorR =  0.0f;
			fixPointReached = true;
			for(int i = 0 ; i < figures.length ; i++){
				mayBeResized[i] = figures[i].height == desiredHeightPerElement;
				if(!mayBeResized[i]){
					maxTopAnchor = max(maxTopAnchor,getTopAnchor(figures[i]));
					maxBottomAnchor = max(maxBottomAnchor,getBottomAnchor(figures[i]));
					fixPointReached = false;
				} else {
					maxTopAnchorR =  max(maxTopAnchorR,getTopAnchor(figures[i]));
					maxBottomAnchorR = max(maxBottomAnchorR,getBottomAnchor(figures[i]));
				}
			}
			while(!fixPointReached){
				fixPointReached = true;
				double spaceForResize = desiredHeight - (maxTopAnchor + maxBottomAnchor);
				double totalHeightNow = max(maxTopAnchor,maxTopAnchorR) + max(maxBottomAnchor,maxBottomAnchorR);
				double topExtraSpacePart, bottomExtraSpacePart;
				
				topExtraSpacePart = max(0,(maxTopAnchorR - maxTopAnchor) / totalHeightNow);
				bottomExtraSpacePart = max(0,(maxBottomAnchorR - maxBottomAnchor) / totalHeightNow);
				if(topExtraSpacePart + bottomExtraSpacePart == 0){
					// cannot fit!
					break;
				}
				double topCap = (topExtraSpacePart / (topExtraSpacePart + bottomExtraSpacePart)) * spaceForResize + maxTopAnchor;
				double bottomCap = (bottomExtraSpacePart / (topExtraSpacePart + bottomExtraSpacePart)) * spaceForResize + maxBottomAnchor;
				for(int i = 0 ; i < figures.length ; i++){
					if(mayBeResized[i]){
						double topAdjust = min(getTopAnchor(figures[i]), topCap);
						double bottomAdjust = min(getBottomAnchor(figures[i]), bottomCap);
						double desiredHeightNow;
						if(getTopAnchorProperty(figures[i]) == 0.0f){
							desiredHeightNow = bottomAdjust /( 1 - getTopAnchorProperty(figures[i]));
						} else if (getTopAnchorProperty(figures[i]) == 1.0f){
							desiredHeightNow = topAdjust / getTopAnchorProperty(figures[i]);
						} else {
							desiredHeightNow = min(topAdjust / getTopAnchorProperty(figures[i]),
												  bottomAdjust /( 1 - getTopAnchorProperty(figures[i])));
						}
						bboxOfFigure(figures[i],desiredWidthPerElement,desiredHeightNow);
						mayBeResized[i] = figures[i].height == desiredHeightNow;
						if(!mayBeResized[i]){
							maxTopAnchor = max(maxTopAnchor,getTopAnchor(figures[i]));
							maxBottomAnchor = max(maxBottomAnchor,getBottomAnchor(figures[i]));
							fixPointReached = false;
						} else {
							maxTopAnchorR =  max(maxTopAnchorR,getTopAnchor(figures[i]));
							maxBottomAnchorR = max(maxBottomAnchorR,getBottomAnchor(figures[i]));
						}
					}
				}
				
			}
		}
		double totalElementsWidth = 0;
		double maxBottomAnchor = 0.0f;
		maxTopAnchor = 0.0f;
		minTopAnchor = Double.MAX_VALUE;
		for(Figure fig : figures){
			totalElementsWidth += getFigureWidth(fig);
			maxBottomAnchor = max(maxBottomAnchor,getBottomAnchor(fig));
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
		System.out.printf("height %f maxTopAnchor %f maxBottomAnchor %f\n", height,maxTopAnchor, maxBottomAnchor);
		determinePlacement();
		System.out.printf("Done bbox!\n");
	}

	private void determinePlacement() {
		double left, top;
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
	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		if(horizontal && getMeasureProperty(DimensionalProp.WIDTH).axisName.equals(axisId)){
			double val = getMeasureProperty(DimensionalProp.WIDTH).value;
			return new Extremes(offset - getHAlignProperty() * val, offset + (1-getHAlignProperty()) * val);
		} else if( !horizontal && getMeasureProperty(DimensionalProp.HEIGHT).axisName.equals(axisId)){
			double val = getMeasureProperty(DimensionalProp.HEIGHT).value;
			return new Extremes(offset - getVAlignProperty() * val, offset + (1-getVAlignProperty()) * val);
		} else {
			Extremes[] extremesList = new Extremes[figures.length];
			for(int i = 0 ; i < figures.length ; i++){
				extremesList[i] = figures[i].getExtremesForAxis(axisId, offset, horizontal);
				System.out.printf("Got extreme %s %f %f\n", extremesList[i], extremesList[i].getMinimum(), extremesList[i].getMaximum());
				if(correctOrientation(horizontal) && gapSize == 0 && extremesList[i].gotData()){
					offset += extremesList[i].getMaximum();
				}
			}
			return Extremes.merge(extremesList);
		}
	}
	
	protected boolean correctOrientation(boolean horizontal){
		return horizontal;
	}
	

	
}
