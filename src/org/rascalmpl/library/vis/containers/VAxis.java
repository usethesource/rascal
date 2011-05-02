package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Extremes;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

import processing.core.PApplet;

public class VAxis extends HAxis {
	
	private static final int MINOR_TICK_WIDTH = 3;
	private static final int MAJOR_TICK_WIDTH = 7;
	private float textWidth;

	public VAxis(IConstructor innerCons, IFigureApplet fpa, PropertyManager properties,  IList childProps, IEvaluatorContext ctx) {
		super(innerCons, fpa, properties,  childProps, ctx);
	}
	
	boolean horizontal(){
		return false;
	}
	
	void setWidthHeight(float desiredWidth,float desiredHeight) {
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
		textWidth = max(fpa.textWidth(range.getMinimum() + ""),fpa.textWidth(range.getMaximum() + ""));
		if(getHAlignProperty() > 0.5f){
			labelY = axisY+ getHGapProperty() +MAJOR_TICK_WIDTH;
			width= max(labelY + textWidth, innerFig.width);
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
	
	float getWidth() {
		return height;
	}
	@Override
	public void draw(float left, float top) {
		
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
		float bottom = top + innerFigY + innerFig.height;
		fpa.line(left + axisY,
				top +  innerFig.getVerticalBorders().getMinimum(),
				left + axisY,
				top + innerFig.getVerticalBorders().getMaximum());
		float direction = getHAlignProperty() > 0.5f ? 1.0f : -1.0f;

		fpa.textAlign(PApplet.LEFT, PApplet.CENTER);
		for(Tick tick : ticks){
			float tickWidth = direction * (tick.major ? MAJOR_TICK_WIDTH : MINOR_TICK_WIDTH);
			String label = tick.measurePos + "";
			if(tick.major){
				fpa.stroke(230);
				fpa.line( left + innerFigX ,
						top + innerFigY + bottom - tick.pixelPos,
						 left + innerFigX + innerFig.width,
						 top + innerFigY +  bottom - tick.pixelPos);
				fpa.stroke(0);
				float labelYPos ;
				if(getHAlignProperty() > 0.5f){
					labelYPos = labelY;
				} else {
					labelYPos = labelY + textWidth - fpa.textWidth(label);
				}
				fpa.text(label, left + labelYPos , top + innerFigY + bottom -  tick.pixelPos);
			}
			fpa.line(left + axisY + tickWidth,
					top + innerFigY +  bottom - tick.pixelPos ,
					left + axisY,
					top + innerFigY + bottom - tick.pixelPos );
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
