package org.rascalmpl.library.vis.figure.interaction.swtwidgets;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;



public class Timer extends LayoutProxy {

	ExecuteTimer t;
	
	public Timer(IFigureConstructionEnv env, int delay, IValue callback, Figure inner, PropertyManager properties){
		super(inner,properties);
		t = new ExecuteTimer(env.getSWTParent(),delay,env.getCallBackEnv(),callback);
	}
	
	static class ExecuteTimer implements Runnable{

		ICallbackEnv cbenv;
		IValue callback;
		boolean cancel;
		Control c;
		ExecuteTimer(Control c,int delay,ICallbackEnv cbenv, IValue callback){
			this.c = c;
			this.cbenv = cbenv;
			this.callback = callback;
			cancel = false;
			Display.getCurrent().timerExec(delay, this);
		}
		
		public void run() {
			if(cancel || c.isDisposed()) return;
			long beginTime = System.currentTimeMillis();
			System.out.printf("Executing timer!\n");
			IInteger result = (IInteger)cbenv.executeRascalCallBackWithoutArguments(callback).getValue();
			cbenv.signalRecompute();
			long elapsed = System.currentTimeMillis() - beginTime;
			int newDelay = Math.max(0,result.intValue() - (int)elapsed);
			if(newDelay != 0){
				Display.getCurrent().timerExec(newDelay, this);
			}
		}
	}
}
