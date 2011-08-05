package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;



public class Timer extends org.rascalmpl.library.vis.figure.combine.WithInnerFig {

	ExecuteTimer t;
	
	public Timer(IFigureConstructionEnv env, int delay, IValue callback, Figure inner, PropertyManager properties){
		super(inner,properties);
		t = new ExecuteTimer(delay,env.getCallBackEnv(),callback);
		this.properties = inner.properties;
	}
	
	@Override
	public void destroy(){
		t.cancel = true;
		super.destroy();
	}
	
	@Override
	public void bbox(){
		innerFig.bbox();
	}
	
	@Override
	public void takeDesiredWidth(double width){
		innerFig.takeDesiredWidth(width);
		size.setWidth(innerFig.size.getWidth());
	}
	
	@Override
	public void takeDesiredHeight(double height){
		innerFig.takeDesiredHeight(height);
		size.setHeight(innerFig.size.getHeight());
	}
	
	@Override
	public void layout(){
		innerFig.globalLocation = globalLocation;
		innerFig.layout();
	}
	
	static class ExecuteTimer implements Runnable{

		ICallbackEnv cbenv;
		IValue callback;
		boolean cancel;
		ExecuteTimer(int delay,ICallbackEnv cbenv, IValue callback){
			this.cbenv = cbenv;
			this.callback = callback;
			cancel = false;
			Display.getCurrent().timerExec(delay, this);
		}
		
		public void run() {
			if(cancel) return;
			IInteger result = (IInteger)cbenv.executeRascalCallBackWithoutArguments(callback).getValue();
			int newDelay = result.intValue();
			if(newDelay != 0){
				Display.getCurrent().timerExec(newDelay, this);
			}
		}
		
		
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		innerFig.draw(gc);
	}
}
