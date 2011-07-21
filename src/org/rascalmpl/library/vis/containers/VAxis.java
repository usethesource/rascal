package org.rascalmpl.library.vis.containers;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class VAxis extends HAxis {

	public VAxis(String label,boolean bottom, IFigureApplet fpa, Figure inner,
			PropertyManager properties) {
		super(label,true, bottom, fpa, inner, properties);
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
	

	
	public void draw(double left, double top, GraphicsContext gc){
		setLeft(left);
		setTop(top);
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
		//fpa.rect(left,top, size.getWidth(),size.getHeight());
		
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
				gc.line( left + axisLeft , 
						top + topOffset + outerSpace - tick.pixelPos,
						left +axisLeft  + innerFig.size.getWidth(),
						top + topOffset +outerSpace- tick.pixelPos);
				
		
				gc.stroke(0);
				gc.text(label,  left + axisLeft + tickHeight + (bottom ? textTickSpacing : -(textTickSpacing + getTextWidth(label)) ), top + topOffset +outerSpace- tick.pixelPos );
			}
			gc.line(left + axisLeft + tickHeight , 
					top + topOffset +outerSpace- tick.pixelPos,
					left + axisLeft,
					top + topOffset +outerSpace- tick.pixelPos);
		}
		if(!this.label.equals("")){
			gc.pushMatrix();
			gc.translate(left + axisLeft + direction * (majorTickHeight + textTickSpacing + labelSpacing + getTextDescent() + labelWidth()), top + topOffset + 0.5 * (outerSpace - getTextWidth(this.label)));
			gc.rotate(0.5  * Math.PI);
			gc.text(this.label, 0,0);
			gc.popMatrix();
			
		}
		//System.out.printf("Innerfig %s\n",innerFig.size);
		innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY(), gc);
		/*
		fpa.line(left + axisLeft,
				top + innerFigLocation.getY(),
				left + axisLeft,
				top + innerFigLocation.getY() + innerFig.size.getHeight());
		*/
		
	}
	
}
