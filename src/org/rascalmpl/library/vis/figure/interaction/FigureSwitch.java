package org.rascalmpl.library.vis.figure.interaction;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.compose.Compose;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;

public class FigureSwitch extends Compose{

	IValue callback;
	int choice ;
	
	public FigureSwitch(IValue callback, Figure[] figures, PropertyManager properties) {
		super(figures, properties);
		this.callback = callback;
		choice = 0;
	}
	

	public void computeFiguresAndProperties(ICallbackEnv env) {
		super.computeFiguresAndProperties(env);
		int newChoice = ((IInteger)(env.executeRascalCallBackWithoutArguments(callback).getValue())).intValue();
		if(newChoice >= figures.length || newChoice < 0){
			newChoice = 0;
		}
		if(newChoice != choice){
			choice = newChoice;
		}
		
	}
	
	public void bbox(){
		for(Figure fig : figures){
			fig.bbox();
			for(boolean flip : BOTH_DIMENSIONS){
				minSize.setWidth(flip, Math.max(minSize.getWidth(flip),fig.minSize.getWidth(flip)/ fig.getHShrinkProperty(flip)));
				setResizableX(flip, getResizableX(flip) || fig.getResizableX(flip));
			}
		}
	}
	
	public void layout(){
		figures[choice].globalLocation.set(globalLocation);
		for(boolean flip : BOTH_DIMENSIONS){
				figures[choice].takeDesiredWidth(flip, size.getWidth(flip) * figures[choice].getHShrinkProperty(flip));
				figures[choice].globalLocation.addX(flip, (size.getWidth(flip) - figures[choice].size.getWidth(flip)) * figures[choice].getHAlignProperty(flip));
		}
		figures[choice].layout();
		
	}

	public void draw(GraphicsContext gc){
		figures[choice].draw(gc);
	}
	
	public boolean isVisible(){
		return figures[choice].isVisible();
	}
	
}
