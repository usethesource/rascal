package org.rascalmpl.library.viz.Figure;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class Scale extends Figure {
	float xscale;
	float yscale;
	Figure velem;

	public Scale(FigurePApplet vlp, PropertyManager inheritedProps, IValue xs,
			IValue ys, IConstructor c, IEvaluatorContext ctx) {
		super(vlp, ctx);
		xscale = xs.getType().isIntegerType() ? ((IInteger) xs).intValue()
				                              : ((IReal) xs).floatValue();
		
		yscale = ys.getType().isIntegerType() ? ((IInteger) ys).intValue()
                							  : ((IReal) ys).floatValue();
		
		velem = FigureFactory.make(vlp, c, properties, ctx);
	}

	@Override
	void bbox() {
		velem.bbox();
		width = xscale * velem.width;
		height = yscale * velem.height;
	}

	@Override
	void draw(float left, float top) {
		vlp.pushMatrix();
		vlp.translate(left, top);
		vlp.scale(xscale, yscale);
		velem.draw(0,0);
		vlp.popMatrix();
	}

}
