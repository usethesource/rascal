package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureSWTApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.IFigureExecutionEnvironment;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Scrollable extends Figure {

	// TODO: fixme!
	
	ScrolledComposite window;
	//Composite parent;
	Canvas canvas;
	IFigureApplet fpaNest;
	
	public Scrollable(IFigureExecutionEnvironment fpa, IConstructor inner, IEvaluatorContext ctx,
			PropertyManager properties) {
		super(fpa,  properties);
		//parent = new Composite(fpa.getComp(), SWT.NONE);
		window  = new ScrolledComposite(fpa.getComp(), SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		window.setAlwaysShowScrollBars(false);
		window.setExpandHorizontal(false);
		window.setExpandVertical(false);
		canvas = new Canvas(window, SWT.NONE);
		window.setBackground(new Color(fpa.getComp().getDisplay(),255,255,255));
		fpaNest = new FigureSWTApplet(canvas, "Scrollable",
				inner, ctx);
		window.setContent(canvas);
	}

	@Override
	public void bbox() {
		/* TODO:
		if(!isWidthPropertySet() && desiredWidth != AUTO_SIZE){
			width = desiredWidth;
		} else {
			width = getWidthProperty();
		}
		if(!isHeightPropertySet() && desiredHeight != AUTO_SIZE){
			height = desiredHeight;
		} else {
			height = getHeightProperty();
		}
		int w, h;
		w = (int)Math.round(width);
		h = (int)Math.round(height);
		System.out.printf("Setting canvas size: %d %d\n",fpa.getFigureWidth(), fpa.getFigureHeight());
		canvas.setSize(
				fpaNest.getFigureWidth()+1 , 
				fpaNest.getFigureHeight()+1);
		window.setSize(w,h);
		*/
		setResizable();
		super.bbox();

	}

	@Override
	public void draw(double left, double top, GraphicsContext gc) {
		// drawing is handled by swt, a new paintevent should 
		// be handled by the nested figureswtapplet...
		window.setLocation((int)Math.round(left), (int)Math.round(top));
		window.setSize((int)size.getWidth(), (int)size.getHeight());
	}

	@Override
	public void layout() {
		
	}

}
