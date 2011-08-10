package org.rascalmpl.library.vis.figure.keys;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class VAxis extends HAxis {

	public VAxis(String label,boolean bottom,  Figure inner,
			PropertyManager properties) {
		super(label,true, bottom,  inner, properties);
	}

	double minimumMajorTicksInterval(){
		return (getTextAscent() + getTextDescent()) * 3.0;
	}
	
	double axisHeight(){
		return majorTickHeight + textTickSpacing 
		+ borderSpacing +  labelWidth() * 1.2 + (label.equals("") ? 0 : labelSpacing + getTextAscent() + getTextDescent()); 
	}
	
	public Properties alignProp(){
		return Properties.VALIGN;
	}
	

	
	public void draw(GraphicsContext gc){
		double axisLeft ;
		if(bottom){
			axisLeft = innerFig.size.getWidth();
		} else {
			axisLeft = axisHeight();
		}
		double topOffset;
		if(innerFig instanceof HAxis && !((HAxis)innerFig).bottom){
				topOffset = ((HAxis)innerFig).axisHeight();
		} else {
			topOffset =0.0;
		}
		double outerSpace =outerSpace();
		Tick[] ticks = getTicks(minimumMajorTicksInterval()
				,0
				,spacing() * (1.0 - innerFig.getRealProperty(alignProp()))
				,spacing() *  (1.0 - innerFig.getRealProperty(alignProp())) + pixelSpace()
				,outerSpace
				,minVal,maxVal
				);

		applyProperties(gc);
		
		double direction = bottom ? 1.0f : -1.0f;
		gc.fill(255);
		//fpa.rect(getLeft(),getTop(), size.getWidth(),size.getHeight());
		
		String format =formatString();
		//System.out.print("format : " + format + "\n");
		for(Tick tick : ticks){
			double tickHeight = direction * (tick.major ? majorTickHeight : minorTickHeight);
			String label = String.format(format,tick.measurePos);
			if(tick.major){
				if(tick.measurePos == 0.0){
					gc.stroke(getColorProperty(Properties.LINE_COLOR));
				} else {
					gc.stroke(getColorProperty(Properties.GUIDE_COLOR));
				}
				gc.line( getLeft() + axisLeft , 
						getTop() + topOffset + outerSpace - tick.pixelPos,
						getLeft() +axisLeft  + innerFig.size.getWidth(),
						getTop() + topOffset +outerSpace- tick.pixelPos);
				
		
				gc.stroke(0);
				gc.text(label,  getLeft() + axisLeft + tickHeight + (bottom ? textTickSpacing : -(textTickSpacing + getTextWidth(label)) ), getTop() + topOffset +outerSpace- tick.pixelPos );
			}
			gc.line(getLeft() + axisLeft + tickHeight , 
					getTop() + topOffset +outerSpace- tick.pixelPos,
					getLeft() + axisLeft,
					getTop() + topOffset +outerSpace- tick.pixelPos);
		}
		if(!this.label.equals("")){
			gc.pushMatrix();
			gc.translate(getLeft() + axisLeft + direction * (majorTickHeight + textTickSpacing + labelSpacing + getTextDescent() + labelWidth()), getTop() + topOffset + 0.5 * (outerSpace - getTextWidth(this.label)));
			gc.rotate(0.5  * Math.PI);
			gc.text(this.label, 0,0);
			gc.popMatrix();
			
		}
		//System.out.printf("Innerfig %s\n",innerFig.size);
		innerFig.draw(gc);
		/*
		fpa.line(getLeft() + axisLeft,
				getTop() + innerFigLocation.getY(),
				getLeft() + axisLeft,
				getTop() + innerFigLocation.getY() + innerFig.size.getHeight());
		*/
		
	}
	
}
