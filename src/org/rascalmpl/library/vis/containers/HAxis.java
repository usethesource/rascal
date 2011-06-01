package org.rascalmpl.library.vis.containers;

import java.util.HashMap;

import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


public class HAxis extends WithInnerFig {
	
	Extremes offsets;
	Extremes range;
	double labelY;
	double axisY;
	double scale;
	static final boolean debug = true;
	
	
	public HAxis(IFigureApplet fpa, Figure inner,PropertyManager properties) {
		super(fpa,inner , properties);
	}

	boolean horizontal(){
		return true;
	}
	
	@Override
	public void bbox() {
		range = innerFig.getExtremesForAxis(getIdProperty(), 0, horizontal());
		if(debug) System.out.printf("range for %s : %f %f %s\n",getIdProperty(),range.getMinimum(),range.getMaximum(),this);
		if(axisScales == null){
			axisScales = new HashMap<String, Double>();
		}
		// TODO: setWidthHeight();
		double rangeInterval = range.getMaximum() - range.getMinimum(); 
		if(range.gotData()){
			scale = getWidth() / (rangeInterval);
		} else {
			scale = 1.0f;
		}
		if(debug) System.out.printf("width %f heihgt %f %s\n",minSize.getWidth(),minSize.getHeight(),this);
		if(debug) System.out.printf("scale for %s becomes %f\n",getIdProperty(),scale);
		axisScales.put(getIdProperty(), scale);
		innerFig.propagateScaling(1.0f, 1.0f,axisScales);
		innerFig.bbox();
		
		double startOffset = innerFig.getOffsetForAxis(getIdProperty(), 0.0f, horizontal());
		offsets = new Extremes(
				startOffset, 
				startOffset + getWidth());
		minSize.setWidth(innerFig.minSize.getWidth());
		minSize.setHeight(innerFig.minSize.getHeight());
		//System.out.printf("Minimum offset %f maximum offset %f\n",offsets.getMinimum(),offsets.getMaximum());
		addAxisToBBox();
		setNonResizable();
		super.bbox();
	}


	void setWidthHeight(double desiredWidth,double desiredHeight) {
		if(isWidthPropertySet() || desiredWidth == AUTO_SIZE){
			minSize.setWidth(getWidthProperty());
		} else {
			minSize.setWidth(desiredWidth);
		}
		if(isHeightPropertySet()){
			minSize.setHeight(getHeightProperty());
		} else {
			minSize.setHeight(desiredHeight);
		}
	}

	void addAxisToBBox() {
		axisY = getVAlignProperty() * innerFig.minSize.getHeight();
		applyFontProperties();
		if(getVAlignProperty() > 0.5f){
			labelY = axisY + getVGapProperty() + fpa.textAscent() ;
			double bottomLabelY = labelY + fpa.textDescent();
			minSize.setHeight(Math.max(minSize.getHeight(),bottomLabelY));
		} else {
			labelY = axisY -  getVGapProperty() - fpa.textAscent() - fpa.textDescent() ;
			double topLabelY = labelY - fpa.textDescent() ;
			innerFigLocation.setY(Math.max(0,-topLabelY));
			axisY+=innerFigLocation.getY() ;
			labelY+=innerFigLocation.getY() ;
			minSize.setHeight(minSize.getHeight() + innerFigLocation.getY());
		}
	}

	double getScale(double width) {
		//System.out.printf("Max %f min %f range axis %s",range.getMaximum(),range.getMinimum(),getIdProperty());
		double scale ;
		if(range.gotData()){
			scale = width / (range.getMaximum() - range.getMinimum());
		} else {
			scale = 1.0f;
		}
		return scale;
	}

	class Tick{
		double pixelPos;
		double measurePos;
		boolean major;
	}
	

	Tick[] getTicks(double majorTickPixelsInteval, double leftBorder, double leftInnerBorder, double rightInnerBorder,double rightBorder, double leftVal, double rightVal){
		//if(debug)System.out.printf("left %f leftInner %f rightInner %f right %f",leftBorder,leftInnerBorder,rightInnerBorder,rightBorder);
		//double pixelsWidth = rightBorder - leftBorder;
		double pixelsInnerWidth = rightInnerBorder - leftInnerBorder;
		double rangeInterval = rightVal - leftVal;
		double nrOfInnerMajorTickIntervals = pixelsInnerWidth / majorTickPixelsInteval;
		double tickInterval = rangeInterval / nrOfInnerMajorTickIntervals;
		int numberOfDigits =  (int)Math.floor(Math.log10(tickInterval));
		double closest10fold = Math.pow(10.0, numberOfDigits);
		double tenMultiple = (int)(tickInterval / closest10fold);
		int nrMinorTicks;
		double closestRoundedNumber;
		if(tenMultiple < 1.25){
			closestRoundedNumber = closest10fold * 1.0;
			nrMinorTicks=10;
		} else if(tenMultiple < 3.75){
			closestRoundedNumber = closest10fold * 2.5;
			nrMinorTicks = 2;
		} else if(tenMultiple < 6.25){
			closestRoundedNumber = closest10fold * 5.0;
			nrMinorTicks = 5;
		} else if(tenMultiple < 8.75){
			closestRoundedNumber = closest10fold * 7.5;
			nrMinorTicks = 4;
		} else {
			closestRoundedNumber = closest10fold * 10.0;
			nrMinorTicks = 10;
		}
		double widthPixelsPerMajorTick = closestRoundedNumber * scale; 
		double widthPixelsPerMinorTick = widthPixelsPerMajorTick / nrMinorTicks;
		double startOffset = Math.signum(range.getMinimum()) *
							(Math.ceil(Math.abs(leftVal) / closestRoundedNumber)) * closestRoundedNumber;

		double startOffsetPixels = leftInnerBorder + (startOffset - range.getMinimum())* scale; 
		int startOffsetTickIndex = (int)((startOffsetPixels - leftBorder ) / widthPixelsPerMinorTick);
		//int startOffsetTickIndex = PApplet.floor((startOffsetPixels - leftBorder) / widthPixelsPerMinorTick);
		//if(debug) System.out.printf("\nstartOffsetTickIndex %f %d\n", (startOffsetPixels - leftBorder ) / widthPixelsPerMinorTick, startOffsetTickIndex);
		int numberOfTicks = startOffsetTickIndex + (int)((rightBorder - startOffsetPixels) / widthPixelsPerMinorTick) + 1;
		Tick[] result = new Tick[numberOfTicks];
		double measurePerTick = closestRoundedNumber / nrMinorTicks;
		for(int i = 0 ; i < numberOfTicks ; i++){
			result[i] = new Tick();
			result[i].measurePos = startOffset + (i - startOffsetTickIndex) * measurePerTick ;
			result[i].pixelPos = startOffsetPixels + (i - startOffsetTickIndex) * widthPixelsPerMinorTick ;
			result[i].major = (i - startOffsetTickIndex) % nrMinorTicks == 0;
			//if(debug) System.out.printf("Tick %d measure %f pixels %f major %s\n",i - startOffsetTickIndex,result[i].measurePos,result[i].pixelPos,result[i].major);
		}
		return result;
	}
	
	@Override
	public void draw(double left, double top) {
		setLeft(left);
		setTop(top);
		Tick[] ticks = getTicks(65.0f, left + innerFigLocation.getX() + innerFig.getHorizontalBorders().getMinimum()
								,left + offsets.getMinimum()
								,left + offsets.getMaximum()
								,left + innerFigLocation.getX() + innerFig.getHorizontalBorders().getMaximum()
								,range.getMinimum(),range.getMaximum()
								);
		
		applyProperties();
		applyFontProperties();
		
		double direction = getVAlignProperty() > 0.5f ? 1.0f : -1.0f;
		fpa.fill(255);
		fpa.rect(left,top, minSize.getWidth(),minSize.getHeight());
		fpa.textAlign(FigureApplet.CENTER, FigureApplet.CENTER);
		for(Tick tick : ticks){
			double tickHeight = direction * (tick.major ? 7 : 3);
			String label = tick.measurePos + "";
			if(tick.major){
				fpa.stroke(230);
				fpa.line( (double)tick.pixelPos ,
						top + innerFigLocation.getY(),
						 (double)tick.pixelPos,
						top + innerFigLocation.getY() + innerFig.minSize.getHeight());
				fpa.stroke(0);
				fpa.text(label,  (double)tick.pixelPos , top + labelY );
			}
			fpa.line((double)tick.pixelPos ,
					top + axisY + tickHeight,
					(double)tick.pixelPos,
					top + axisY );
		}
		innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY());
		fpa.line(left + innerFig.getHorizontalBorders().getMinimum(),
				top + axisY,
				left + innerFig.getHorizontalBorders().getMaximum(),
				top + axisY);
		
	

	}
	
	public Extremes getHorizontalExtremes(){
		return offsets;
	}
	
	@Override
	public Extremes getVerticalBorders(){
		//System.out.printf("vertical haxis borders %f %f \n", innerFigY,innerFigY + innerFig.height);
		return new Extremes(innerFigLocation.getY(),innerFigLocation.getY() + innerFig.minSize.getHeight());
	}
	
}
