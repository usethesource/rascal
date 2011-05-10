package org.rascalmpl.library.vis.containers;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


public class HAxis extends Figure {
	
	Figure innerFig;
	double innerFigX, innerFigY;
	Extremes offsets;
	Extremes range;
	double labelY;
	double axisY;
	double scale;
	static final boolean debug = true;
	
	
	public HAxis(IConstructor innerCons, IFigureApplet fpa, PropertyManager properties,  IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.innerFig = FigureFactory.make(fpa, innerCons, this.properties, childProps, ctx);
	}

	boolean horizontal(){
		return true;
	}
	
	@Override
	public void bbox(double desiredWidth, double desiredHeight) {
		range = innerFig.getExtremesForAxis(getIdProperty(), 0, horizontal());
		if(debug) System.out.printf("range for %s : %f %f %s\n",getIdProperty(),range.getMinimum(),range.getMaximum(),this);
		if(axisScales == null){
			axisScales = new HashMap<String, Double>();
		}
		setWidthHeight(desiredWidth,desiredHeight);
		double rangeInterval = range.getMaximum() - range.getMinimum(); 
		if(range.gotData()){
			scale = getWidth() / (rangeInterval);
		} else {
			scale = 1.0f;
		}
		if(debug) System.out.printf("width %f heihgt %f %s\n",width,height,this);
		if(debug) System.out.printf("scale for %s becomes %f\n",getIdProperty(),scale);
		axisScales.put(getIdProperty(), scale);
		innerFig.propagateScaling(1.0f, 1.0f,axisScales);
		innerFig.bbox(width, height);
		
		double startOffset = innerFig.getOffsetForAxis(getIdProperty(), 0.0f, horizontal());
		offsets = new Extremes(
				startOffset, 
				startOffset + getWidth());
		width = innerFig.width;
		height = innerFig.height;
		//System.out.printf("Minimum offset %f maximum offset %f\n",offsets.getMinimum(),offsets.getMaximum());
		addAxisToBBox();
	}

	double getWidth() {
		return width;
	}

	void setWidthHeight(double desiredWidth,double desiredHeight) {
		if(isWidthPropertySet() || desiredWidth == AUTO_SIZE){
			width = getWidthProperty();
		} else {
			width = desiredWidth;
		}
		if(isHeightPropertySet()){
			height = getHeightProperty();
		} else {
			height = desiredHeight;
		}
	}

	void addAxisToBBox() {
		axisY = getVAlignProperty() * innerFig.height;
		applyFontProperties();
		if(getVAlignProperty() > 0.5f){
			labelY = axisY + getVGapProperty() + fpa.textAscent() ;
			double bottomLabelY = labelY + fpa.textDescent();
			height = max(height,bottomLabelY);
		} else {
			labelY = axisY -  getVGapProperty() - fpa.textAscent() - fpa.textDescent() ;
			double topLabelY = labelY - fpa.textDescent() ;
			innerFigY = max(0,-topLabelY);
			axisY+=innerFigY ;
			labelY+=innerFigY ;
			height+=innerFigY;
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
		Tick[] ticks = getTicks(65.0f, left + innerFigX + innerFig.getHorizontalBorders().getMinimum()
								,left + offsets.getMinimum()
								,left + offsets.getMaximum()
								,left + innerFigX + innerFig.getHorizontalBorders().getMaximum()
								,range.getMinimum(),range.getMaximum()
								);
		
		applyProperties();
		applyFontProperties();
		
		double direction = getVAlignProperty() > 0.5f ? 1.0f : -1.0f;
		fpa.fill(255);
		fpa.rect(left,top, width,height);
		fpa.textAlign(FigureApplet.CENTER, FigureApplet.CENTER);
		for(Tick tick : ticks){
			double tickHeight = direction * (tick.major ? 7 : 3);
			String label = tick.measurePos + "";
			if(tick.major){
				fpa.stroke(230);
				fpa.line( (double)tick.pixelPos ,
						top + innerFigY,
						 (double)tick.pixelPos,
						top + innerFigY + innerFig.height);
				fpa.stroke(0);
				fpa.text(label,  (double)tick.pixelPos , top + labelY );
			}
			fpa.line((double)tick.pixelPos ,
					top + axisY + tickHeight,
					(double)tick.pixelPos,
					top + axisY );
		}
		innerFig.draw(left + innerFigX, top + innerFigY);
		fpa.line(left + innerFig.getHorizontalBorders().getMinimum(),
				top + axisY,
				left + innerFig.getHorizontalBorders().getMaximum(),
				top + axisY);
		
	

	}
	
	public Extremes getHorizontalExtremes(){
		return offsets;
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		if(innerFig!=null){
			innerFig.gatherProjections(left + innerFigX, top + innerFigY, projections, first, screenId, horizontal);
		}
	}
	
	public void propagateScaling(double scaleX,double scaleY,HashMap<String,Double> axisScales){
		super.propagateScaling(scaleX, scaleY,axisScales);
		if(innerFig != null){
			innerFig.propagateScaling(scaleX, scaleY,axisScales);
		}
	}
	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		Extremes result = super.getExtremesForAxis(axisId, offset, horizontal);
		if(result.gotData()){
			return result;
		} else {
			return innerFig.getExtremesForAxis(axisId, offset, horizontal);
		}
	}
	
	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		double result = super.getOffsetForAxis(axisId, offset, horizontal);
		if(result != Double.MAX_VALUE){
			return result;
		} else {
			double off = 0.0f;
			if(horizontal){
				off = innerFigX;
			} else {
				off = innerFigY;
			}
			return innerFig.getOffsetForAxis(axisId, offset + off, horizontal);
		}
	}
	
	@Override
	public Extremes getVerticalBorders(){
		//System.out.printf("vertical haxis borders %f %f \n", innerFigY,innerFigY + innerFig.height);
		return new Extremes(innerFigY,innerFigY + innerFig.height);
	}
	
}
