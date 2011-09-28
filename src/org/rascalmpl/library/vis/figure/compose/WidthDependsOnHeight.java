package org.rascalmpl.library.vis.figure.compose;

import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Dimension;

public abstract class WidthDependsOnHeight extends Compose{

	Dimension major, minor;
	
	public static final double MINOR_MINSIZE = 30;
	
	protected WidthDependsOnHeight(Dimension major, Figure[] figures, PropertyManager properties) {
		super(figures, properties);
		this.major = major;
		minor = major.other();
	}
	
	@Override
	public boolean widthDependsOnHeight(){
		return true;
	}
	
	public Dimension getMajorDimension(){
		return major;
	}

	@Override
	public void computeMinSize() {
		double minWidth = 0;
		for(Figure fig : children){
			minWidth = Math.max(minWidth,fig.minSize.get(major) );
		}
		minSize.set(major, Math.ceil(minWidth) + 1);
		minSize.set(minor, MINOR_MINSIZE);
	}



	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		gc.rect(globalLocation.getX()-3, globalLocation.getY()-3, size.getX()+3, size.getY()+3);
	}
	
	
}
