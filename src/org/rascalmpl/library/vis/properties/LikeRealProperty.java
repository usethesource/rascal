package org.rascalmpl.library.vis.properties;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;

public class LikeRealProperty implements IRealPropertyValue {
	final private Property property;
	final private Figure fig;

	public LikeRealProperty(Property prop, String id, FigurePApplet fpa, IEvaluatorContext ctx){
		this.property = prop;
		this.fig = fpa.getRegisteredId(id);
		if(this.fig == null)
			throw RuntimeExceptionFactory.figureException("Cannot be the same as not (yet) existing figure", ctx.getValueFactory().string(id), ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	public Property getProperty(){
		return property;
	}
	
	public float getValue() {
		return fig.properties.getRealProperty(property);
	}

	public boolean isCallBack() {
		return false;
	}

}
