package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyParsers;
import org.rascalmpl.library.vis.util.Key;


public class HAxis extends WithInnerFig implements Key<Double> {
	

	final static double majorTickHeight = 5;
	final static double minorTickHeight = 2;
	final static double textTickSpacing = 2;
	final static double borderSpacing = 3;
	final static double labelSpacing = 25;
	
	double labelY;
	double axisY;
	double scale;
	static final boolean debug = true;
	boolean flip, bottom;
	double minVal, maxVal;
	double minOffset,maxOffset;
	double inheritedSpacing;
	String label;
	
	public HAxis(String label,boolean flip, boolean bottom,IFigureApplet fpa, Figure inner,PropertyManager properties) {
		super(fpa,inner , properties);
		this.flip = flip;
		this.bottom = bottom;
		this.label = label;
	}
	
	public HAxis(String label,boolean bottom,IFigureApplet fpa, Figure inner,PropertyManager properties) {
		this(label,false,bottom,fpa,inner,properties);
	}
	
	public void init(){
		minVal = Double.MAX_VALUE;
		maxVal = Double.MIN_VALUE;
		super.init();
	}
	
	@Override
	public void registerValue(Properties prop,Object val) {
		//System.out.printf("Registering at axis %s\n",val);
		if(val instanceof IValue && (((IValue)val).getType().isNumberType() || ((IValue)val).getType().isIntegerType() || ((IValue)val).getType().isRealType())){
			double pval = PropertyParsers.parseNum((IValue)val);
			//System.out.printf("Processed %f %s\n", pval,getIdProperty());
			minVal = Math.min(minVal,pval);
			maxVal = Math.max(maxVal,pval);
		}
	}

	@Override
	public Double scaleValue(Object val) {
		if(val instanceof IValue && (((IValue)val).getType().isNumberType() || ((IValue)val).getType().isIntegerType() || ((IValue)val).getType().isRealType())){
			double pval = PropertyParsers.parseNum((IValue)val);
			//System.out.printf("Converted %f\n", scale * pval);
			//System.out.printf("%f -> %f %f\n",  pval, scale * (pval-minVal) ,minVal);
			return scale * (pval-minVal)  ;
		}
		return 0.0;
	}
	
	void computeScale(double pixelSpace){
		scale = pixelSpace / ((maxVal - minVal) );
	}
	
	@Override
	public void bbox() {
		innerFig.bbox();
		//System.out.printf("axissize %s %s\n",size,minSize);
		for(boolean flip : BOTH_DIMENSIONS){
			minSize.setWidth(flip,innerFig.minSize.getWidth(flip) * getHGrowProperty(flip));
		}
		resizableX = innerFig.resizableX;
		resizableY = innerFig.resizableY;
		setResizable();
		minSize.addHeight(flip,axisHeight());
		
	}
	
	double pixelSpace(){
		if(innerFig instanceof HAxis && ((HAxis)innerFig).flip != flip){
			return size.getWidth(flip) / getHGrowProperty(flip) - ((HAxis)innerFig).axisHeight();
		} else {
			return size.getWidth(flip) / getHGrowProperty(flip);
		}
	}
	
	double spacing(){
		return  size.getWidth(flip) * (1.0 - (1.0 /getHGrowProperty(flip))) ;
	}
	
	double outerSpace(){
		if(innerFig instanceof HAxis && ((HAxis)innerFig).flip != flip){
			return size.getWidth(flip)  - ((VAxis)innerFig).axisHeight();
		} else {
			return size.getWidth(flip) ;
		}
	}
	
	public Properties alignProp(){
		return Properties.HALIGN;
	}
	
	public void layout(){
		computeScale(pixelSpace());
		double axisHeight = axisHeight();
		double spacing ;
		if(innerFig instanceof HAxis && ((HAxis)innerFig).flip != flip){
			spacing = 0;
		} else {
			spacing = spacing() ;
		}
		innerFig.takeDesiredWidth(flip, size.getWidth(flip) - spacing);
		
		innerFig.takeDesiredHeight(flip, size.getHeight(flip) - axisHeight - inheritedSpacing);
		innerFigLocation.setX(flip,spacing * innerFig.getRealProperty(alignProp()) );
		
		if(bottom){
			innerFigLocation.setY(flip,inheritedSpacing);
		} else {
			innerFigLocation.setY(flip,axisHeight + inheritedSpacing);
		}
		for(boolean flip : BOTH_DIMENSIONS){
			innerFig.globalLocation.setX(flip, globalLocation.getX(flip) + innerFigLocation.getX(flip));
		}
		
		
		innerFig.layout();
		if(innerFig instanceof HAxis && ((HAxis)innerFig).flip != flip){
			((HAxis)innerFig).inheritedSpacing = spacing() * innerFig.getRealProperty(alignProp());
		}
	}
	
	double minimumMajorTicksInterval(){
		applyFontProperties();
		return labelWidth() * 7.0 ;
	}
	
	double axisHeight(){
		applyFontProperties();
		return majorTickHeight + textTickSpacing 
		+ borderSpacing + (label.equals("") ? fpa.textAscent() + fpa.textDescent() : labelSpacing + 2 *  (fpa.textAscent() + fpa.textDescent()) )  ; 
	}
	
	int standardExtraPrecision(){
		return 3;
	}
	
	int highestPrecision(){
		return (int)Math.ceil(Math.max(Math.log10(maxVal),Math.log10(minVal)));
	}
	
	int lowestPrecision(){
		return (int)Math.ceil(Math.log10(maxVal-minVal)) - standardExtraPrecision();
	}
	
	double labelWidth(){
		int length = highestPrecision() + Math.max(0,-lowestPrecision()) + 1;
		return fpa.textWidth(String.format("%0" + length + "d", 0));
	}
	
	String formatString(){
		return "%" + String.format("%d.%df",
				Math.max(1, highestPrecision()),Math.max(0, -lowestPrecision()));
	}
	
	
	
	public void draw(double left, double top){
		setLeft(left);
		setTop(top);
		System.out.printf("innersize %s\n",innerFig.size);
		double axisTop ;
		if(bottom){
			axisTop = innerFig.size.getHeight(flip);
		} else {
			axisTop = axisHeight();
		}
		
		double pixelSpace = pixelSpace(); 
		double leftOffset ;
		if(innerFig instanceof VAxis && !((VAxis)innerFig).bottom){
			System.out.printf("Data dan weer wel\n");
				leftOffset = ((VAxis)innerFig).axisHeight();
		} else {
				leftOffset =0.0;
		}
		double spacing = spacing();
		double outerSpace = outerSpace();
		System.out.printf("left offset %f\n",leftOffset);
		Tick[] ticks = getTicks(minimumMajorTicksInterval(), 
				left + leftOffset
				, left + leftOffset + spacing * innerFig.getRealProperty(alignProp())
				,left + leftOffset + spacing * innerFig.getRealProperty(alignProp()) + pixelSpace
				,left +  leftOffset + outerSpace
				,minVal,maxVal
				);

		applyProperties(false);
		applyFontProperties();
		
		double direction = bottom ? 1.0f : -1.0f;
		fpa.fill(255);
		//fpa.rect(left,top, size.getWidth(),size.getHeight());
		fpa.textAlign(FigureApplet.CENTER, FigureApplet.CENTER);
		String format = formatString();
		for(Tick tick : ticks){
			double tickHeight = direction * (tick.major ? majorTickHeight : minorTickHeight);
			String label = String.format(format,tick.measurePos );
			
			//System.out.printf("tick %f",tick.measurePos);
			if(tick.major){
				if(tick.measurePos == 0.0){
					fpa.stroke(getColorProperty(Properties.LINE_COLOR));
				} else {
					fpa.stroke(getColorProperty(Properties.GUIDE_COLOR));
				}
				fpa.line( tick.pixelPos ,
						top + axisTop,
						 tick.pixelPos,
						top + axisTop + -direction * innerFig.size.getHeight());
			
		
				applyProperties(false);
				fpa.text(label,  tick.pixelPos , top + axisTop + tickHeight + (bottom ? fpa.textAscent() : -fpa.textDescent()) );
			}
			fpa.line(tick.pixelPos ,
					top + axisTop + tickHeight,
					tick.pixelPos,
					top + axisTop );
		}
		applyFontProperties();
		if(!this.label.equals("")){
			fpa.text(this.label, left + leftOffset + (0.5 * (innerFig.size.getWidth() - fpa.textWidth(this.label))), top + axisTop + direction* (majorTickHeight +  textTickSpacing 
					+ borderSpacing + fpa.textAscent() + fpa.textDescent()) + (bottom ? fpa.textAscent() : fpa.textDescent()));
		}
		//System.out.printf("Innerfig %s %s\n",this,innerFigLocation);
		
		innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY());
		/*fpa.line(left + innerFigLocation.getX(),
				top + axisTop,
				left + innerFigLocation.getX() + innerFig.size.getWidth(),
				top + axisTop);*/
		
		
	}


	class Tick{
		double pixelPos;
		double measurePos;
		boolean major;
	}
	
	Tick[] getTicks(double majorTickPixelsInteval, double leftBorder, double leftInnerBorder, double rightInnerBorder,double rightBorder, double leftVal, double rightVal){
		//if(debug)System.out.printf("left %f leftInner %f rightInner %f right %f",leftBorder,leftInnerBorder,rightInnerBorder,rightBorder);
		//double pixelsWidth = rightBorder - leftBorder;
		// TODO: this is sometimes beyond the actual range
		double pixelsInnerWidth = rightInnerBorder - leftInnerBorder;
		double rangeInterval = rightVal - leftVal;
		double nrOfInnerMajorTickIntervals = pixelsInnerWidth / (majorTickPixelsInteval / 2.5);
		double tickInterval = rangeInterval / nrOfInnerMajorTickIntervals;
		int numberOfDigits =  (int)Math.floor(Math.log10(tickInterval));
		double closest10fold = Math.pow(10.0, numberOfDigits);
		double tenMultiple = (int)(tickInterval / closest10fold);
		int nrMinorTicks;
		double closestRoundedNumber;
		if(tenMultiple < 2.5){
			closestRoundedNumber = closest10fold * 2.5;
			nrMinorTicks=2;
		} else if(tenMultiple < 5){
			closestRoundedNumber = closest10fold * 5.0;
			nrMinorTicks = 5;
		} else if(tenMultiple < 7.5){
			closestRoundedNumber = closest10fold * 7.5;
			nrMinorTicks = 4;
		} else {
			closestRoundedNumber = closest10fold * 10.0;
			nrMinorTicks = 10;
		}
		
		
		
		double widthPixelsPerMajorTick = closestRoundedNumber * scale; 
		double widthPixelsPerMinorTick = widthPixelsPerMajorTick / nrMinorTicks;
		double startOffset = Math.signum(leftVal) *
							(Math.ceil(Math.abs(leftVal) / closestRoundedNumber)) * closestRoundedNumber;

		double startOffsetPixels = leftInnerBorder + (startOffset - leftVal)* scale; 
		int startOffsetTickIndex = (int)((startOffsetPixels - leftBorder ) / widthPixelsPerMinorTick);
		//int startOffsetTickIndex = PApplet.floor((startOffsetPixels - leftBorder) / widthPixelsPerMinorTick);
		//if(debug) System.out.printf("\nstartOffsetTickIndex %f %d\n", (startOffsetPixels - leftBorder ) / widthPixelsPerMinorTick, startOffsetTickIndex);
		int numberOfTicks = startOffsetTickIndex + (int)((rightBorder - startOffsetPixels) / widthPixelsPerMinorTick) + 1;
		Tick[] result = new Tick[numberOfTicks];
		double measurePerTick = closestRoundedNumber / nrMinorTicks;
		double measureHere = startOffset - startOffsetTickIndex * measurePerTick;
		double measureHereMajor = startOffset - (startOffsetTickIndex / nrMinorTicks) * closestRoundedNumber;
		for(int i = 0 ; i < numberOfTicks ; i++){
			result[i] = new Tick();
			result[i].measurePos = measureHere ;
			result[i].pixelPos = startOffsetPixels + (i - startOffsetTickIndex) * widthPixelsPerMinorTick ;
			result[i].major = (i - startOffsetTickIndex) % nrMinorTicks == 0;
			
			measureHere += measurePerTick;
			if((i + 1 - startOffsetTickIndex) % nrMinorTicks == 0){
				measureHereMajor += closestRoundedNumber;
				measureHere = measureHereMajor;
				//System.out.printf("closest rounded %f\n", measureHere);
			}
			//if(debug) System.out.printf("Tick %d measure %f pixels %f major %s\n",i - startOffsetTickIndex,result[i].measurePos,result[i].pixelPos,result[i].major);
		}
		
		return result;
	}

	@Override
	public String getId() {
		return getIdProperty();
	}

	@Override
	public void registerOffset(double offset) {
		//System.out.printf("Registering offset %f",offset);
		minOffset = Math.min(minOffset, offset);
		maxOffset = Math.max(maxOffset, offset);
		
	}
	
}
