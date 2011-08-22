package org.rascalmpl.library.vis.swt;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.widgets.Composite;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.swt.applet.FigureSWTApplet;
import org.rascalmpl.library.vis.util.NameResolver;

public class FigureExecutionEnvironment implements ICallbackEnv{

	private FigureSWTApplet appletRoot;
	private IEvaluatorContext ctx;
	private boolean callbackBatch;
	private boolean batchEmpty;
	private boolean computing;
	private Composite swtRoot;
	private long rascalTime = 0;
	public static boolean profile = false;
	private NameResolver resolver;
	private int computeClock; 

	
	
	public FigureExecutionEnvironment(Composite parent, IConstructor cfig,IEvaluatorContext ctx) {
		this.ctx = ctx;
		// ctx.registerComputationFinishedListener(this)!!!!
		callbackBatch = false;
		computing = false;
		this.swtRoot = parent;
		appletRoot = new FigureSWTApplet(parent, cfig, this);
		computeClock = 0;
		resolver = new NameResolver(getRascalContext());
		computeFigures();
	}
	
	public boolean isBatchEmpty(){
		return batchEmpty;
	}
	
	public void computeFigures(){
		computing = true;
		appletRoot.triggerRecompute();
		computing = false;
	}
	
	public void beginCallbackBatch(){
		computeClock++;
		callbackBatch = true;
		batchEmpty = true;
	}
	
	public void endCallbackBatch(){
		endCallbackBatch(false);
		
	}
	
	// dontRecompute param is currently neseccary because of non-memoization of nullary closures
	// holding the mouse over a computed figure, with associated mouseOver handler will 
	// otherwise result in infinite loop
	public void endCallbackBatch(boolean dontRecompute){
		long startTime = System.nanoTime();
		callbackBatch = false;
		if(!batchEmpty && !dontRecompute){
			computeFigures();
			if(profile){
				double figTime = (System.nanoTime() - startTime) / 1000000.0;
				double rascalTimeD = rascalTime / 1000000.0;
				System.out.printf("Compute figures took %f rascal time: %f rascal portion %f\n", figTime, rascalTimeD, rascalTimeD / figTime);
				rascalTime = 0;
			}
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
		if (!(fun.getType().isExternalType() && ((fun instanceof RascalFunction) || (fun instanceof OverloadedFunctionResult)))) {
			throw RuntimeExceptionFactory.illegalArgument(fun,
					ctx.getCurrentAST(), ctx.getStackTrace());
		}
	}
	
	public IConstructor executeRascalFigureCallBack(IValue callback,
			Type[] argTypes, IValue[] argVals) {
		IConstructor c = (IConstructor)executeRascalCallBack(callback, argTypes, argVals).getValue();
		return (IConstructor)ctx.getEvaluator().call(getRascalContext(), "normalize", c);
	}

	public Result<IValue> executeRascalCallBack(IValue callback,
			Type[] argTypes, IValue[] argVals) {
		long startTime = System.nanoTime();
		Result<IValue> result = null;
		try {
			result = ((ICallableValue) callback).call(argTypes, argVals);
		} catch (Throw e) {
			e.printStackTrace();
			System.err.printf("Callback error: " + e.getMessage() + ""
					+ e.getTrace());
		}
		if(!computing){
			if(callbackBatch){
				batchEmpty = false;
			} 
		}
		if(profile) rascalTime += System.nanoTime() - startTime;
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


}
