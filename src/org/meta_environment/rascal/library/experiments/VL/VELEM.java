package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;


public class VELEM {
	
	protected IEvaluatorContext ctx;
	
	protected IValueFactory vf;
	
	int bottom;                                  // bottom margin
	OverloadedFunctionResult bottomFun = null;
	
	int left;
	OverloadedFunctionResult leftFun = null;
	
	int right;
	OverloadedFunctionResult rightFun = null;
	
	int top;
	OverloadedFunctionResult topFun = null;
	
	boolean visible;
	
	String title;
	
	float values[];
	OverloadedFunctionResult valuesFun = null;
	
	VELEM(IEvaluatorContext ctx){
		this.ctx = ctx;
		vf = ValueFactoryFactory.getValueFactory();
	}
	
	protected IList getProps(IList props){
		IListWriter w = vf.listWriter(TypeFactory.getInstance().valueType());
		
		for(IValue v : props){
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			System.err.println("pname = " + pname);
			IValue arg = c.get(0);
			if(pname.equals("bottom")){
				if(arg instanceof OverloadedFunctionResult)
					bottomFun = (OverloadedFunctionResult) arg;
				else
					bottom = ((IInteger) arg).intValue();
			} else if(pname.equals("left")){
				if(arg instanceof OverloadedFunctionResult)
					leftFun = (OverloadedFunctionResult) arg;
				else
					left = ((IInteger) arg).intValue();
			} else if(pname.equals("right")){
					if(arg instanceof OverloadedFunctionResult)
						rightFun = (OverloadedFunctionResult) arg;
					else
						right = ((IInteger) arg).intValue();
				} else if(pname.equals("top")){
					if(arg instanceof OverloadedFunctionResult)
						topFun = (OverloadedFunctionResult) arg;
					else
						top = ((IInteger) arg).intValue();
			} else if(pname.equals("values")){
				if(arg instanceof OverloadedFunctionResult)
					valuesFun = (OverloadedFunctionResult) arg;
				else {
					IList vals = (IList) c.get(0);
					values = new float[vals.length()];
					for(int i = 0; i < vals.length(); i++){
						IValue num = vals.get(i);
						if(num.getType().isIntegerType()){
							values[i] = ((IInteger) num).intValue();
						} else if(num.getType().isRealType()){
							values[i] = ((IReal) num).floatValue();
						} else {
							throw RuntimeExceptionFactory.illegalArgument(num, ctx.getCurrentAST(), ctx.getStackTrace());
						}
					}
				}
			} else {
				w.append(v);
			}
		}
		
		return w.done();
	}
	
	protected static Type[] argTypes = new Type[] {TypeFactory.getInstance().integerType()};
	protected static IValue[] argVals;
	
	int getIntField(OverloadedFunctionResult fun, int n, int field){
		if(fun != null){
			argVals[0] = vf.integer(n);
			Result<IValue> res = fun.call(argTypes, argVals);
			return ((IInteger) res.getValue()).intValue();
		} else
			return field;		
	}
	
	protected int getBottom(int n){
		return getIntField(bottomFun, n, bottom);
	}
	
	protected int getLeft(int n){
		return getIntField(leftFun, n, left);
	}
	
	protected int getRight(int n){
		return getIntField(rightFun, n, right);
	}
	
	protected int getTop(int n){
		return getIntField(topFun, n, top);
	}
	
	protected float getValues(int n){
		if(valuesFun != null){
			argVals[0] = vf.integer(n);
			Result<IValue> res = valuesFun.call(argTypes, argVals);
			return ((IInteger) res.getValue()).intValue();
		} else
			return values[n];		
	}
	
	protected boolean getVisible(){
		return visible;
	}
	
	protected String getTitle(){
		return title;
	}
	
}
