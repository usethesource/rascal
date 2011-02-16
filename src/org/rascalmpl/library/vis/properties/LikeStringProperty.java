package org.rascalmpl.library.vis.properties;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;

public class LikeStringProperty implements IStringPropertyValue {
	final private Property property;
	final private Figure fig;

	public LikeStringProperty(Property prop, String id, FigurePApplet fpa, IEvaluatorContext ctx){
		this.property = prop;
		this.fig = fpa.getRegisteredId(id);
		if(this.fig == null)
			throw RuntimeExceptionFactory.figureException("Cannot be the same as not (yet) existing figure", ctx.getValueFactory().string(id), ctx.getCurrentAST(),
				ctx.getStackTrace());
	}
	
	public Property getProperty(){
		return property;
	}
	
	public String getValue() {
		return fig.properties.getStringProperty(property);
	}

	public boolean usesTrigger() {
		return false;
	}

}
