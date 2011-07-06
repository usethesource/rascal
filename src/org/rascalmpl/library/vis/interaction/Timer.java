package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;



public class Timer extends org.rascalmpl.library.vis.containers.WithInnerFig {

	ExecuteTimer t;
	
	public Timer(IFigureApplet fpa, int delay, IValue callback, Figure inner, PropertyManager properties){
		super(fpa,inner,properties);
		t = new ExecuteTimer(fpa,callback);
		fpa.getComp().getDisplay().timerExec(delay, t);
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

		IFigureApplet fpa;
		IValue callback;
		boolean cancel;
		ExecuteTimer(IFigureApplet fpa, IValue callback){
			this.fpa = fpa;
			this.callback = callback;
			cancel = false;
		}
		
		@Override
		public void run() {
			if(cancel) return;
			if(fpa.getComp().isDisposed()) return;
			IInteger result = (IInteger)fpa.executeRascalCallBackWithoutArguments(callback).getValue();
			fpa.setComputedValueChanged();
			fpa.redraw();
			int newDelay = result.intValue();
			if(newDelay != 0){
				fpa.getComp().getDisplay().timerExec(newDelay, this);
			}
		}
		
		
	}
	
	@Override
	public void draw(double left, double top) {
		innerFig.draw(left, top);
	}
	
	public void executeKeyDownHandlers(IValue keySym, IMap modifiers){
	}
	
	public void executeKeyUpHandlers(IValue keySym, IMap modifiers){
	}

	public void executeMouseOverOffHandlers(Properties prop) {
	}

}
