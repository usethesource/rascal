package org.rascalmpl.library.vis.containers;

import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;


public class VAxis extends HAxis {
	
	private static final int MINOR_TICK_WIDTH = 3;
	private static final int MAJOR_TICK_WIDTH = 7;
	private double textWidth;

	public VAxis(IFigureApplet fpa, Figure inner, PropertyManager properties) {
		super(fpa, inner, properties);
	}
	
	boolean horizontal(){
		return false;
	}
	
	void setWidthHeight(double desiredWidth,double desiredHeight) {
		if(isWidthPropertySet()){
			width = getWidthProperty();
		} else {
			width = desiredWidth;
		}
		if(isHeightPropertySet() || desiredHeight == AUTO_SIZE){
			height = getHeightProperty();
		} else {
			height = desiredHeight;
		}
	}
	
	@Override
	void addAxisToBBox() {
		axisY = getHAlignProperty() * innerFig.width;
		textWidth = Math.max(fpa.textWidth(range.getMinimum() + ""),fpa.textWidth(range.getMaximum() + ""));
		if(getHAlignProperty() > 0.5f){
			labelY = axisY+ getHGapProperty() +MAJOR_TICK_WIDTH;
			width= Math.max(labelY + textWidth, innerFig.width);
		} else {
			labelY = axisY-getHGapProperty() - textWidth - MAJOR_TICK_WIDTH ;
			if(labelY < 0.0f){
				innerFigX = -(labelY);
				axisY+= innerFigX;
				labelY= 0.0f;
				width+=innerFigX;
			}
		}
	}
	
	@Override
	public void draw(double left, double top) {
		
		setLeft(left);
		setTop(top);
		Tick[] ticks = getTicks(50.0f,top + innerFigY + innerFig.getVerticalBorders().getMinimum()
								,top + offsets.getMinimum()
								,top + offsets.getMaximum()
								,top + innerFigY + innerFig.getVerticalBorders().getMaximum()
								,range.getMinimum(),range.getMaximum()
								);
		
		applyProperties();
		applyFontProperties();
		double bottom = top + innerFigY + innerFig.height;
		fpa.line(left + axisY,
				top +  innerFig.getVerticalBorders().getMinimum(),
				left + axisY,
				top + innerFig.getVerticalBorders().getMaximum());
		double direction = getHAlignProperty() > 0.5f ? 1.0f : -1.0f;

		fpa.textAlign(FigureApplet.LEFT, FigureApplet.CENTER);
		for(Tick tick : ticks){
			double tickWidth = direction * (tick.major ? MAJOR_TICK_WIDTH : MINOR_TICK_WIDTH);
			String label = tick.measurePos + "";
			double pixelPos = (double)tick.pixelPos;
			if(tick.major){
				
				fpa.stroke(230);
				fpa.line( left + innerFigX ,
						top + innerFigY + bottom - pixelPos,
						 left + innerFigX + innerFig.width,
						 top + innerFigY +  bottom - pixelPos);
				fpa.stroke(0);
				double labelYPos ;
				if(getHAlignProperty() > 0.5f){
					labelYPos = labelY;
				} else {
					labelYPos = labelY + textWidth - fpa.textWidth(label);
				}
				fpa.text(label, left + labelYPos , top + innerFigY + bottom -  pixelPos);
			}
			fpa.line(left + axisY + tickWidth,
					top + innerFigY +  bottom - pixelPos ,
					left + axisY,
					top + innerFigY + bottom -pixelPos);
		}
		innerFig.draw(left + innerFigX, top + innerFigY);
	
	}
	
	@Override
	public Extremes getHorizontalBorders(){
		return new Extremes(innerFigX,innerFigX + innerFig.width);
	}
	
	@Override
	public Extremes getVerticalBorders(){
		return new Extremes(0,height);
	}
	
}
