package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class XAxis extends Container {

	public XAxis(IFigureApplet fpa, PropertyManager properties, IConstructor inside, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties, inside, childProps, ctx);
	}

	@Override
	void drawContainer() {
		fpa.line(getLeft(), getTop() + height, getLeft() + width,  getTop() + height);

	}

	@Override
	String containerName() {
		return "xaxis";
	}

}
