/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.swt;

import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.swt.applet.FigureSWTApplet;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.BoundingBox;

public class FigureExecutionEnvironment implements ICallbackEnv{

	private FigureSWTApplet appletRoot;
	private IEvaluatorContext ctx;
	private boolean callbackBatch;
	private boolean batchEmpty;
	private boolean computing;
	private Composite swtRoot;
	private long rascalTime = 0;
	private long startTime = 0;
	public static boolean profile = false;
	private NameResolver resolver;
	private int computeClock; 

	
	
	public FigureExecutionEnvironment(Composite parent, IConstructor cfig,IEvaluatorContext ctx) {
		this.ctx = ctx;
		// ctx.registerComputationFinishedListener(this)!!!!
		callbackBatch = false;
		computing = false;
		this.swtRoot = parent;
		computeClock = 0;
		resolver = new NameResolver(getRascalContext());
		appletRoot = new FigureSWTApplet(parent, cfig, this);


		appletRoot.setLocation(0, 0);
		appletRoot.pack();
		computing = true;
		appletRoot.triggerRecompute();
		computing = false;
	}
	
	public boolean isBatchEmpty(){
		return batchEmpty;
	}
	
	public void computeFigures(){
		computing = true;
		long startTime = System.currentTimeMillis();
		appletRoot.triggerRecompute();
		if(profile){
			long elapsedTime = System.currentTimeMillis() - startTime;
			System.out.printf("Recomputing took %d rascalTime %d %f \n",  elapsedTime, rascalTime / 1000000, (double)(rascalTime / 1000000.0) / (double) elapsedTime );
		}
		rascalTime = 0;
		computing = false;
	}
	
	public void beginCallbackBatch(){
		computeClock++;
		startTime = System.nanoTime();
		rascalTime = 0;
		callbackBatch = true;
		batchEmpty = true;
	}
	
	public void endCallbackBatch(){
		endCallbackBatch(false);
		
	}
	
	public void setSize(int x,int y){
		appletRoot.setSize(x + appletRoot.getBorderWidth() , y + appletRoot.getBorderWidth() );
	}
	
	public BoundingBox getMinViewingSize(){
		BoundingBox box = appletRoot.getFigure().minSize;
		Rectangle r = appletRoot.computeTrim(0, 0, FigureMath.round(box.getX()), FigureMath.round(box.getY()));
		return new BoundingBox(r.width, r.height);
	}
	
	// dontRecompute param is currently neseccary because of non-memoization of nullary closures
	// holding the mouse over a computed figure, with associated mouseOver handler will 
	// otherwise result in infinite loop
	public void endCallbackBatch(boolean dontRecompute){
		callbackBatch = false;
		if(profile && !batchEmpty){
			double figTime = (System.nanoTime() - startTime) / 1000000.0;
			System.out.printf("Callback batch took %f rascalTime %d %f \n",  figTime, rascalTime / 1000000, (double)(rascalTime / 1000000.0) / (double) figTime );
			rascalTime = 0;
		}
		if(!batchEmpty && !dontRecompute){
			computeFigures();
			
		}

		if(dontRecompute && !batchEmpty){
			swtRoot.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					computeFigures();
				}
			});
		}
	}

	public IEvaluatorContext getRascalContext() {
		return ctx;
	}
	

	public void checkIfIsCallBack(IValue fun) {
		if (!(fun.getType().isExternalType() && ((fun instanceof RascalFunction) || (fun instanceof OverloadedFunction)))) {
			throw RuntimeExceptionFactory.illegalArgument(fun,
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	public IConstructor executeRascalFigureCallBack(IValue callback,
			Type[] argTypes, IValue[] argVals) {
		Result<IValue> e = executeRascalCallBack(callback, argTypes, argVals);
		if(e == null){
			return null;
		} else {
			IConstructor c = (IConstructor)e.getValue();
			IEvaluator<Result<IValue>> evaluator = ctx.getEvaluator();
			return (IConstructor)evaluator.call(getRascalContext(),"vis::Figure", "normalize", c);
		}

	}

	public Result<IValue> executeRascalCallBack(IValue callback,
			Type[] argTypes, IValue[] argVals) {
		long startTime = System.nanoTime();
		Result<IValue> result = null;
		try {
			synchronized (ctx) {
				result = ((ICallableValue) callback).call(argTypes, argVals, null);
			}
		} catch (Throw e) {
			e.printStackTrace(ctx.getStdErr());
			ctx.getStdErr().printf("Callback error: " + e.getMessage() + ""
					+ e.getTrace());
		}
		catch (StaticError e) {
			e.printStackTrace(ctx.getStdErr());
			ctx.getStdErr().printf("Callback error: " + e.getMessage());
		}
		if(profile) rascalTime += System.nanoTime() - startTime;
		if(!computing){
			if(callbackBatch){
				batchEmpty = false;
			} 
		} 

		return result;
	}
	
	@Override
	public void fakeRascalCallBack() {
		batchEmpty = false;
	}

	public Result<IValue> executeRascalCallBackWithoutArguments(IValue callback) {
		Type[] argTypes = {};
		IValue[] argVals = {};

		return executeRascalCallBack(callback, argTypes, argVals);
	}

	public Result<IValue> executeRascalCallBackSingleArgument(IValue callback,
			Type type, IValue arg) {
		Type[] argTypes = { type };
		IValue[] argVals = { arg };
		return executeRascalCallBack(callback, argTypes, argVals);
	}
	
	public FigureSWTApplet getRootApplet(){
		return appletRoot;
	}
	
	public void dispose(){
		appletRoot.dispose();
	}
	

	
	public NameResolver getNameResolver(){
		return resolver;
	}
	
	public int getComputeClock(){
		return computeClock;
	}

	@Override
	public void signalRecompute() {
		computeClock++;
		computeFigures();
	}

	@Override
	public long getAndResetRascalTime() {
		long ret = rascalTime;
		rascalTime = 0;
		return ret;
	}

	public void writeScreenshot(OutputStream s){
		appletRoot.writeScreenshot(s);
	}

	@Override
	public void registerAnimation(Animation a) {
		appletRoot.registerAnimation(a);
		
	}

	@Override
	public void unregisterAnimation(Animation a) {
		appletRoot.unregisterAnimation(a);
		
	}


}
