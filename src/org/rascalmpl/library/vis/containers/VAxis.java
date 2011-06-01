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
			minSize.setWidth(getWidthProperty());
		} else {
			minSize.setWidth(desiredWidth);
		}
		if(isHeightPropertySet() || desiredHeight == AUTO_SIZE){
			minSize.setHeight(getHeightProperty());
		} else {
			minSize.setHeight(desiredHeight);
		}
	}
	
	@Override
	void addAxisToBBox() {
		axisY = getHAlignProperty() * innerFig.minSize.getWidth();
		textWidth = Math.max(fpa.textWidth(range.getMinimum() + ""),fpa.textWidth(range.getMaximum() + ""));
		if(getHAlignProperty() > 0.5f){
			labelY = axisY+ getHGapProperty() +MAJOR_TICK_WIDTH;
			minSize.setWidth(Math.max(labelY + textWidth, innerFig.minSize.getWidth()));
		} else {
			labelY = axisY-getHGapProperty() - textWidth - MAJOR_TICK_WIDTH ;
			if(labelY < 0.0f){
				innerFigLocation.setX(-(labelY));
				axisY+= innerFigLocation.getX();
				labelY= 0.0f;
				minSize.setWidth(minSize.getWidth() + innerFigLocation.getX());
			}
		}
	}
	
	@Override
	public void draw(double left, double top) {
		
		setLeft(left);
		setTop(top);
		Tick[] ticks = getTicks(50.0f,top + innerFigLocation.getY() + innerFig.getVerticalBorders().getMinimum()
								,top + offsets.getMinimum()
								,top + offsets.getMaximum()
								,top + innerFigLocation.getY() + innerFig.getVerticalBorders().getMaximum()
								,range.getMinimum(),range.getMaximum()
								);
		
		applyProperties();
		applyFontProperties();
		double bottom = top + innerFigLocation.getY() + innerFig.minSize.getHeight();
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
				fpa.line( left + innerFigLocation.getX() ,
						top + innerFigLocation.getY() + bottom - pixelPos,
						 left + innerFigLocation.getX() + innerFig.minSize.getWidth(),
						 top + innerFigLocation.getY() +  bottom - pixelPos);
				fpa.stroke(0);
				double labelYPos ;
				if(getHAlignProperty() > 0.5f){
					labelYPos = labelY;
				} else {
					labelYPos = labelY + textWidth - fpa.textWidth(label);
				}
				fpa.text(label, left + labelYPos , top + innerFigLocation.getY() + bottom -  pixelPos);
			}
			fpa.line(left + axisY + tickWidth,
					top + innerFigLocation.getY() +  bottom - pixelPos ,
					left + axisY,
					top + innerFigLocation.getY() + bottom -pixelPos);
		}
		innerFig.draw(left + innerFigLocation.getX(), top + innerFigLocation.getY());
	
	}
	
	@Override
	public Extremes getHorizontalBorders(){
		return new Extremes(innerFigLocation.getX(),innerFigLocation.getX() + innerFig.minSize.getWidth());
	}
	
	@Override
	public Extremes getVerticalBorders(){
		return new Extremes(0,minSize.getHeight());
	}
	
}
