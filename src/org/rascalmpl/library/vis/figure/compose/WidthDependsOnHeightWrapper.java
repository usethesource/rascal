package org.rascalmpl.library.vis.figure.compose;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Scrollable;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.ViewPortHandler;
import org.rascalmpl.library.vis.util.vector.Dimension;

public class WidthDependsOnHeightWrapper extends Scrollable{

	Dimension major;

	public WidthDependsOnHeightWrapper(Dimension major, IFigureConstructionEnv env, IConstructor inner, PropertyManager properties) {
		super(major != Dimension.X, major != Dimension.Y, env,inner,properties);
		this.major = major;

	}
	
	@Override
	public void computeMinSize(){
		super.computeMinSize();
		minSize.set(major,
				(widget.getFigure()).minSize.get(major) + ViewPortHandler.scrollbarSize.get(major));
	}
	
	@Override
	public boolean widthDependsOnHeight(){
		return true;
	}

}
