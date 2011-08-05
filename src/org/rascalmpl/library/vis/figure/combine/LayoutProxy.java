package org.rascalmpl.library.vis.figure.combine;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;

public abstract class LayoutProxy extends WithInnerFig {

	// Figure which is merely a wrapper for the inner figure from a layout perspective
	
	public LayoutProxy(Figure inner, PropertyManager properties) {
		super(inner, properties);
	}
	
	@Override
	public void bbox(){
		innerFig.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			setResizableX(flip, innerFig.getResizableX(flip));
		}
		minSize.set(innerFig.minSize);
	}
	
	
	@Override
	public void layout(){
		for(boolean flip : BOTH_DIMENSIONS){
			innerFig.takeDesiredWidth(flip, size.getWidth(flip));
		}
		innerFig.layout();
	}
	
	public boolean isHShrinkPropertySet(){
		return innerFig.isHShrinkPropertySet();
	}
	
	public boolean isVShrinkPropertySet(){
		return innerFig.isVShrinkPropertySet();
	}
	
	public boolean isHGrowPropertySet(){
		return innerFig.isHGrowPropertySet();
	}
	
	public boolean isVGrowPropertySet(){
		return innerFig.isVGrowPropertySet();
	}
	

	public boolean isHLocPropertySet(){
		return innerFig.isHLocPropertySet();
	}
	
	public boolean isVLocPropertySet(){
		return innerFig.isVLocPropertySet();
	}

	public double getHShrinkProperty() {
		return innerFig.getHShrinkProperty();
	}
	
	public double getVShrinkProperty() {
		return innerFig.getVShrinkProperty();
	}
	
	public double getHGrowProperty() {
		return innerFig.getHGrowProperty();
	}
	
	public double getVGrowProperty() {
		return innerFig.getVGrowProperty();
	}
	

	public double getHLocProperty(){
		return innerFig.getHLocProperty();
	}
	
	public double getVLocProperty(){
		return innerFig.getVLocProperty();
	}
	

	public double getHAlignProperty() {
		return innerFig.getHAlignProperty();
	}

	public double getVAlignProperty() {
		return innerFig.getVAlignProperty();
	}

}
