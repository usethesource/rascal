package org.rascalmpl.library.vis.figure.compose;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.swt.graphics.Rectangle;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Scrollable;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.FigureSWTApplet;
import org.rascalmpl.library.vis.swt.applet.ViewPortHandler;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.library.vis.util.vector.Dimension;

public class WidthDependsOnHeightWrapper extends Scrollable{

	Dimension major;

	public WidthDependsOnHeightWrapper(Dimension major, IFigureConstructionEnv env, IConstructor inner, PropertyManager properties) {
		super(major != Dimension.X, major != Dimension.Y, env,inner,properties);
		this.major = major;

	}
	
	@Override
	public void computeMinSize(){
		//super.computeMinSize();
		BoundingBox iminSize = widget.getFigure().minSize;
		Rectangle r = widget.computeTrim(0, 0, FigureMath.round(iminSize.getX()), FigureMath.round(iminSize.getY()));
		minSize.set(r.width,r.height);
		Dimension minor = major.other();
		minSize.set(minor, iminSize.get(minor) );
	}
	
	@Override
	public boolean widthDependsOnHeight(){
		return true;
	}

}
