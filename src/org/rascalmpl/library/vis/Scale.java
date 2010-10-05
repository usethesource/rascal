package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class Scale extends Figure {
	float xscale;
	float yscale;
	Figure figure;

	public Scale(FigurePApplet fpa, PropertyManager inheritedProps, IValue xs,
			IValue ys, IConstructor c, IEvaluatorContext ctx) {
		super(fpa, ctx);
		xscale = xs.getType().isIntegerType() ? ((IInteger) xs).intValue()
				                              : ((IReal) xs).floatValue();
		
		yscale = ys.getType().isIntegerType() ? ((IInteger) ys).intValue()
                							  : ((IReal) ys).floatValue();
		
		figure = FigureFactory.make(fpa, c, properties, ctx);
	}

	@Override
	void bbox() {
		figure.bbox();
		width = xscale * figure.width;
		height = yscale * figure.height;
	}

	@Override
	void draw(float left, float top) {
		fpa.pushMatrix();
		fpa.translate(left, top);
		fpa.scale(xscale, yscale);
		figure.draw(0,0);
		fpa.popMatrix();
	}

}
