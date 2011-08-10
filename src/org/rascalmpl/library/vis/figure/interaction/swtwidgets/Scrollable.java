package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.FigureSWTApplet;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;

public class Scrollable extends SWTWidgetFigure<FigureSWTApplet> {

	public Figure innerFig;
	boolean hscroll, vscroll;
	
	public Scrollable(boolean hscroll,boolean vscroll,IFigureConstructionEnv env, IConstructor inner, PropertyManager properties) {
		super(env,  properties);
		System.out.printf("Scrollable %s \n",inner);
		widget = makeWidget(env.getSWTParent(), env,inner);
		innerFig = widget.getFigure();
		widget.setVisible(false);
	}
	
	FigureSWTApplet makeWidget(Composite comp, IFigureConstructionEnv env,IConstructor inner) {
		return new FigureSWTApplet(comp, inner,env.getFigureExecEnv(),hscroll,vscroll);
	}
	
	public void bbox(){
		super.bbox();
		innerFig.bbox();
		if(!hscroll){
			minSize.setWidth(innerFig.minSize.getWidth() + FigureSWTApplet.scrollbarSize.getWidth());
		}
		if(!vscroll){
			minSize.setHeight(innerFig.minSize.getHeight() + FigureSWTApplet.scrollbarSize.getHeight());
		}
		setResizable();
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
