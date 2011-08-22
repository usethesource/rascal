package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.FigureSWTApplet;

public class Scrollable extends SWTWidgetFigure<FigureSWTApplet> {

	public Figure innerFig;
	boolean hscroll, vscroll;
	
	public Scrollable(boolean hscroll,boolean vscroll,IFigureConstructionEnv env, IConstructor inner, PropertyManager properties) {
		super(env,  properties);
		System.out.printf("Scrollable %s \n",inner);
		widget = makeWidget(env.getSWTParent(), env,inner);
		env.getSWTParent().registerChild(widget);
		innerFig = widget.getFigure();
		widget.setVisible(false);
	}
	
	FigureSWTApplet makeWidget(Composite comp, IFigureConstructionEnv env,IConstructor inner) {
		return new FigureSWTApplet(comp, inner,env.getFigureExecEnv(),hscroll,vscroll);
	}
	

}
