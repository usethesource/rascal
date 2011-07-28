package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.FigureSWTApplet;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;

public class Scrollable extends SWTWidgetFigure<FigureSWTApplet> {

	Figure innerFig;
	
	public Scrollable(IFigureConstructionEnv env, IConstructor inner, PropertyManager properties) {
		super(env,  properties);
		widget = makeWidget(env.getSWTParent(), env,inner);
		innerFig = widget.getFigure();
	}
	
	FigureSWTApplet makeWidget(Composite comp, IFigureConstructionEnv env,IConstructor inner) {
		return new FigureSWTApplet(comp, inner,env.getFigureExecEnv(),true,true);
	}
	
	public void bbox(){
		super.bbox();
		innerFig.bbox();
	}

	public void init(){
		super.init();
		innerFig.init();
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		super.computeFiguresAndProperties(env);
		innerFig.computeFiguresAndProperties(env);
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		innerFig.registerNames(resolver);
	}
	
	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		innerFig.registerValues(resolver);
	}

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		innerFig.getLikes(resolver);
	}
	
	public void finalize(){
		super.finalize();
		innerFig.finalize();
	}
	
	public void destroy(){
		super.destroy();
		innerFig.destroy();
	}

}
