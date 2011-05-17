package org.rascalmpl.library.vis.compose;

import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.DimensionalProp;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;

public class HCat extends Compose {

	double gapSize;
	double numberOfGaps;
	double minTopAnchor = Double.MAX_VALUE;
	private double maxTopAnchor;
	private static boolean debug = true;
	boolean flip ;

	
	public HCat(IFigureApplet fpa, Figure[] figures, PropertyManager properties) {
		super(fpa,figures, properties);
		flip = false;
	}
	
	
	public
	void bbox(double desiredWidth, double desiredHeight){
		bboxActual(new BoundingBox(desiredWidth, desiredHeight));
	}
		
	public void bboxActual(BoundingBox desiredBBox){
		
		gapSize = getHGapProperty(flip);
		numberOfGaps = (figures.length - 1);
		if(getStartGapProperty()){numberOfGaps+=0.5f;} 
		if(getEndGapProperty()){numberOfGaps+=0.5f;}
		double desiredWidth,desiredHeight;
		desiredWidth = desiredBBox.getWidth(flip);
		desiredHeight = desiredBBox.getHeight(flip);
		if(isWidthPropertySet()) desiredWidth = getWidthProperty() ;
		if(isHeightPropertySet())  desiredHeight = getHeightProperty() ;
		double desiredWidthPerElement, desiredWidthOfElements, desiredHeightPerElement, gapsSize;
		desiredWidthPerElement = 0.0;
		desiredHeightPerElement = 0.0;
		desiredWidthOfElements = 0.0;
		gapsSize = 0.0; // stops compiler from whining
		if(isHGapPropertySet(flip) && !isHGapFactorPropertySet(flip)){
			gapsSize = getHGapProperty(flip) ;
		}
		// determine desired width of elements 
		if(desiredWidth == Figure.AUTO_SIZE){
			// FIXME: do this more efficiently
			for(Figure fig : figures){
				fig.bbox();
				desiredWidthPerElement = Math.max(desiredWidthPerElement, fig.getWidth(flip));
			}
		} else {
			if(isHGapFactorPropertySet(flip) || !isHGapPropertySet(flip)){
				gapsSize = desiredWidth* getHGapFactorProperty(flip);
			}
			desiredWidthOfElements = desiredWidth- gapsSize;
			desiredWidthPerElement = desiredWidthOfElements / figures.length;
		}
		// deterine desired height of elements
		if(desiredHeight == Figure.AUTO_SIZE){
			for(Figure fig : figures){
				fig.bbox();
				desiredHeightPerElement = Math.max(desiredHeightPerElement,  fig.getHeight(flip));
			}
		} else {
			double minTopAnchorProp, maxTopAnchorProp;
			minTopAnchorProp = Double.MAX_VALUE;
			maxTopAnchorProp = 0.0f;
			for(Figure fig : figures){
				minTopAnchorProp = Math.min(minTopAnchorProp,fig.topAlign(flip));
				maxTopAnchorProp = Math.max(maxTopAnchorProp,fig.topAlign(flip));
			}
			double maxDiffTopAnchorProp = maxTopAnchorProp - minTopAnchorProp;
			// first assume all elements will get desiredheight
			// rewrote : desiredHeight = desiredHeightPerElement + maxDiffTopAnchorProp* desiredHeightPerElement
			desiredHeightPerElement = desiredHeight  / (1 + maxDiffTopAnchorProp);
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
				BoundingBox desiredBoundingBoxPerElement = new BoundingBox(desiredWidthPerElement,desiredHeightPerElement,flip); 
				if(mayBeResized[i]){
					figures[i].bbox(desiredBoundingBoxPerElement);
					if(desiredWidthPerElement != Figure.AUTO_SIZE 
							&& figures[i].getWidthProperty(flip) != desiredWidthPerElement ){
						totalNonResizeableWidth+=figures[i].getWidthProperty(flip);
						numberOfNonResizeableWidthElements++;
						mayBeResized[i]=false;
						fixPointReached = false;
					} 
				}
			}
			// recompute width of resizeable elements
			if(numberOfNonResizeableWidthElements > 0 && desiredBBox.getWidth(flip)!=Figure.AUTO_SIZE){
				desiredWidthPerElement = (desiredWidthOfElements - totalNonResizeableWidth) 
				                              / (figures.length - numberOfNonResizeableWidthElements);
			}
		} while(!fixPointReached);
		// Fixpoint for height depending on alignment?
		if(desiredBBox.getHeight() != AUTO_SIZE){
			double maxTopAnchor, maxBottomAnchor;
			double maxTopAnchorR, maxBottomAnchorR;
			maxTopAnchor = maxBottomAnchor = maxTopAnchorR = maxBottomAnchorR =  0.0f;
			fixPointReached = true;
			for(int i = 0 ; i < figures.length ; i++){
				mayBeResized[i] = figures[i].height == desiredHeightPerElement;
				if(!mayBeResized[i]){
					maxTopAnchor = Math.max(maxTopAnchor,figures[i].topAlign(flip));
					maxBottomAnchor = Math.max(maxBottomAnchor,figures[i].bottomAlign(flip));
					fixPointReached = false;
				} else {
					maxTopAnchorR =  Math.max(maxTopAnchorR,figures[i].topAlign(flip));
					maxBottomAnchorR = Math.max(maxBottomAnchorR,figures[i].bottomAlign(flip));
				}
			}
			while(!fixPointReached){
				fixPointReached = true;
				double spaceForResize = desiredHeight - (maxTopAnchor + maxBottomAnchor);
				double totalHeightNow = Math.max(maxTopAnchor,maxTopAnchorR) + Math.max(maxBottomAnchor,maxBottomAnchorR);
				double topExtraSpacePart, bottomExtraSpacePart;
				
				topExtraSpacePart = Math.max(0,(maxTopAnchorR - maxTopAnchor) / totalHeightNow);
				bottomExtraSpacePart = Math.max(0,(maxBottomAnchorR - maxBottomAnchor) / totalHeightNow);
				if(topExtraSpacePart + bottomExtraSpacePart == 0){
					// cannot fit!
					break;
				}
				double topCap = (topExtraSpacePart / (topExtraSpacePart + bottomExtraSpacePart)) * spaceForResize + maxTopAnchor;
				double bottomCap = (bottomExtraSpacePart / (topExtraSpacePart + bottomExtraSpacePart)) * spaceForResize + maxBottomAnchor;
				for(int i = 0 ; i < figures.length ; i++){
					if(mayBeResized[i]){
						double topAdjust = Math.min(figures[i].topAlign(flip), topCap);
						double bottomAdjust = Math.min(figures[i].bottomAlign(flip), bottomCap);
						double desiredHeightNow;
						if(figures[i].getVAlignProperty(flip)  == 0.0f){
							desiredHeightNow = bottomAdjust /( 1 - figures[i].getVAlignProperty(flip));
						} else if (figures[i].getVAlignProperty(flip) == 1.0f){
							desiredHeightNow = topAdjust / figures[i].getVAlignProperty(flip);
						} else {
							desiredHeightNow = Math.min(topAdjust / figures[i].getVAlignProperty(flip),
												  bottomAdjust /( 1 - figures[i].getVAlignProperty(flip)));
						}
						BoundingBox desiredBoundingBoxPerElement = new BoundingBox(desiredWidthPerElement,desiredHeightNow,flip);
						figures[i].bbox(desiredBoundingBoxPerElement);
						mayBeResized[i] = figures[i].height == desiredHeightNow;
						if(!mayBeResized[i]){
							maxTopAnchor = Math.max(maxTopAnchor,figures[i].getVAlignProperty(flip));
							maxBottomAnchor = Math.max(maxBottomAnchor,figures[i].getVAlignProperty(flip));
							fixPointReached = false;
						} else {
							maxTopAnchorR =  Math.max(maxTopAnchorR,figures[i].getVAlignProperty(flip));
							maxBottomAnchorR = Math.max(maxBottomAnchorR,figures[i].getVAlignProperty(flip));
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
			totalElementsWidth += fig.getWidth(flip);
			maxBottomAnchor = Math.max(maxBottomAnchor,fig.bottomAlign(flip));
			maxTopAnchor = Math.max(maxTopAnchor,fig.topAlign(flip));
			minTopAnchor = Math.min(minTopAnchor,fig.topAlign(flip));
		}
		if(desiredWidth != Figure.AUTO_SIZE && 
		   (int)(totalElementsWidth + gapsSize + 0.5f) > (int)(desiredWidth + 0.5f)){ // prevent round-off cannot fit error
			if(debug) if(debug)System.err.printf("Cannot fit!");
			gapsSize = Math.max(desiredWidth -totalElementsWidth,0.0f);
		}
		// compute gap if auto-size and ratio
		if(desiredWidth==Figure.AUTO_SIZE && (isHGapFactorPropertySet(flip) || !isHGapPropertySet(flip))){
			// the next formula can be obtained by rewriting hGapFactor = gapsSize / (totalElementsWidth + gapsSize) 
			gapsSize = totalElementsWidth /  (1/getHGapFactorProperty(flip) - 1);
		}
		width = totalElementsWidth + gapsSize;
		gapSize = gapsSize / numberOfGaps;
		
		height =  maxTopAnchor + maxBottomAnchor;
		//System.out.printf("height %f maxTopAnchor %f maxBottomAnchor %f\n", height,maxTopAnchor, maxBottomAnchor);
		determinePlacement();
		BoundingBox bbox = new BoundingBox(width, height,flip);
		width = bbox.getWidth();
		height = bbox.getHeight();
		//System.out.printf("Done bbox!\n");
	}

	private void determinePlacement() {
		double left, top;
		left = top = 0.0f;
		if(getStartGapProperty()){
			left+=0.5*gapSize;
		}
			// Draw from left to right
		for(int i = 0 ; i < figures.length ; i++){
			Coordinate location = new Coordinate(left, top - figures[i].topAlign(flip) + maxTopAnchor,flip);
			xPos[i] = location.getX();
			yPos[i] = location.getY();
			left += figures[i].getWidth(flip) + gapSize;
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
