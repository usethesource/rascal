package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class Scale extends VELEM {
	float xscale;
	float yscale;
	VELEM velem;

	public Scale(VLPApplet vlp, PropertyManager inheritedProps, IValue xs,
			IValue ys, IConstructor c, IEvaluatorContext ctx) {
		super(vlp, ctx);
		xscale = xs.getType().isIntegerType() ? ((IInteger) xs).intValue()
				                              : ((IReal) xs).floatValue();
		
		yscale = ys.getType().isIntegerType() ? ((IInteger) ys).intValue()
                							  : ((IReal) ys).floatValue();
		
		velem = VELEMFactory.make(vlp, c, properties, ctx);
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
