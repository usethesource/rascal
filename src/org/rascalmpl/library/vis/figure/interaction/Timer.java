/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.interaction;

import static org.rascalmpl.library.vis.Timer.TimerAction_noChange;
import static org.rascalmpl.library.vis.Timer.TimerAction_restart;
import static org.rascalmpl.library.vis.Timer.TimerAction_restart_delay;
import static org.rascalmpl.library.vis.Timer.TimerAction_stop;
import static org.rascalmpl.library.vis.Timer.TimerInfo;
import static org.rascalmpl.library.vis.Timer.TimerInfo_running;
import static org.rascalmpl.library.vis.Timer.TimerInfo_stopped;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.values.ValueFactoryFactory;


public class Timer extends LayoutProxy {
	private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
	@SuppressWarnings("unused")
	private static final boolean debug = true;
	ExecuteTimer t;
	ICallbackEnv cbenv;
	IValue timerInit;
	IValue callback;
	Control c;
	long elapsedAtHide;
	boolean hidden;
	IConstructor timerAction;
	boolean firstDraw = true;
	
	public Timer(IFigureConstructionEnv env, IValue timerInit, IValue callback, Figure inner, PropertyManager properties){
		super(inner,properties);
		hidden = true;
		elapsedAtHide = 0;
		this.timerInit = timerInit;
		this.callback = callback;
		this.cbenv = env.getCallBackEnv();
		c = env.getSWTParent();
		t = null;
	}
	
	public void hideElement(IFigureConstructionEnv env) {
		if(!hidden && t != null){
			t.cancel = true;
			elapsedAtHide = System.currentTimeMillis() - t.beginTime;
			hidden = true;
		}
	}


	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		if(!visible) {
			return;
		}
		
		IValue timerInfo = getTimerInfo();
		//if(debug)System.out.printf("timerInit %s\n", timerInfo);
		Result<IValue> result = cbenv.executeRascalCallBackSingleArgument(timerInit, TimerInfo, timerInfo);
		
		if (result != null && result.getValue() != null) {
			timerAction = (IConstructor) result.getValue();
			executeTimerAction(timerAction);
			hidden = false;
		}

	}
	
	private void executeTimerAction(IConstructor timerAction){
		Type type = timerAction.getConstructorType();
		if(type == TimerAction_noChange){
			if(hidden){
				if(t !=null){
					int newDelay = (int)(t.delay - elapsedAtHide);
					t = new ExecuteTimer(newDelay);
				}
			} else {
				return;
			}
		} else if(type == TimerAction_stop){
			if(t !=null){
				t.cancel = true;
				t.stopped = true;
			}
		} else if(type == TimerAction_restart){
			if(t != null){
				t.cancel = true;
			}
			
			t = new ExecuteTimer(TimerAction_restart_delay(timerAction));
			//System.out.printf("Restarting time %d %s\n", TimerAction_restart_delay(timerAction),t);
		} else {
			System.err.printf("Unknown timerAction type %s %s!\n ", type, timerAction);
		}
	}
	
	private IValue getTimerInfo(){
		if(t == null){
			return vf.constructor(TimerInfo_stopped, vf.integer(0));
		} else {
			if(t.stopped){
				return vf.constructor(TimerInfo_stopped, vf.integer(System.currentTimeMillis() - t.stopTime));
			} else {
				return vf.constructor(TimerInfo_running, vf.integer(t.delay - (System.currentTimeMillis() - t.beginTime)));
			}
		}
	}
	
	class ExecuteTimer implements Runnable{
		boolean cancel;
		long beginTime;
		long stopTime;
		boolean stopped;
		int delay;
		
		ExecuteTimer(int delay){
			cancel = false;
			stopped = false;
			beginTime = System.currentTimeMillis();
			if(delay <= 0){
				delay = 1;
			}
			this.delay = delay;
				//if(debug)System.out.printf("Executing timer delay %s\n",delay);
			Display.getCurrent().timerExec(delay, this);			
		}
		
		public void run() {
			//if(debug)System.out.printf("Timer callbakc!");
			if(cancel || c.isDisposed()) {
				//System.out.printf("Cancelled! %d %s \n",delay, this);
				return;
			}
			//System.out.printf("Executing! %d %s \n",delay, this);
			
			cbenv.executeRascalCallBackWithoutArguments(callback);
			stopped =true;
			stopTime = System.currentTimeMillis();
			cbenv.signalRecompute();
		}
	}
}
