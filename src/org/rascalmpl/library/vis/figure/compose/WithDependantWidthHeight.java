package org.rascalmpl.library.vis.figure.compose;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Scrollable;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.BoundingBox;

public class WithDependantWidthHeight extends Scrollable{
	
	BoundingBox realSize;

	public WithDependantWidthHeight(boolean widthMajor,IConstructor inner, PropertyManager properties,IFigureConstructionEnv env) {
		super(!widthMajor,widthMajor, env,inner,properties);
	}
	
	public BoundingBox getMinViewingSize() {
		return innerFig.size;
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
