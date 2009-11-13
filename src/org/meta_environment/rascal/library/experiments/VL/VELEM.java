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
import org.meta_environment.rascal.interpreter.result.RascalFunction;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

import processing.core.PApplet;

public abstract class VELEM {
	
	protected IEvaluatorContext ctx;
	
	protected IValueFactory vf;
	
	int bottom;                                 // bottom margin
	RascalFunction bottomFun = null;
	
	int left;									// left margin
	RascalFunction leftFun = null;
	
	int right;									// right marging
	RascalFunction rightFun = null;
	
	int gap;									// gap
	RascalFunction gapFun = null;
	
	int top;									// top margin
	RascalFunction topFun = null;
	
	boolean visible;							// is VELEM visible?
	
	String title;								// Title
	
	float values[];								// Data values
	RascalFunction valuesFun = null;
	
	int fillStyle;                              // Figure fill style
	RascalFunction fillStyleFun = null;
	
	int lineWidth;                              // Figure line width
	RascalFunction lineWidthFun = null;
	
	int strokeStyle;
	RascalFunction strokeStyleFun = null;
	
	
	VELEM(IEvaluatorContext ctx){
		this.ctx = ctx;
		vf = ValueFactoryFactory.getValueFactory();
	}
	
	protected IList getProps(IList props){
		IListWriter w = vf.listWriter(TypeFactory.getInstance().valueType());
		
		for(IValue v : props){
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			
			IValue arg = c.get(0);
			System.err.println("pname = " + pname + ", arg = " + arg);
			if(pname.equals("bottom")){
				if(arg instanceof RascalFunction)
					bottomFun = (RascalFunction) arg;
				else
					bottom = ((IInteger) arg).intValue();
			} else if(pname.equals("left")){
				System.err.println("arg = " + arg + ", type = " + arg.getType() +  " class = " + arg.getClass());
				if(arg instanceof RascalFunction)
					leftFun = (RascalFunction) arg;
				else
					left = ((IInteger) arg).intValue();
			} else if(pname.equals("right")){
					if(arg instanceof RascalFunction)
						rightFun = (RascalFunction) arg;
					else
						right = ((IInteger) arg).intValue();
			} else if(pname.equals("gap")){
				if(arg instanceof RascalFunction)
					gapFun = (RascalFunction) arg;
				else
					gap = ((IInteger) arg).intValue();
			} else if(pname.equals("top")){
					if(arg instanceof RascalFunction)
						topFun = (RascalFunction) arg;
					else
						top = ((IInteger) arg).intValue();
					
			} else if(pname.equals("fillStyle")){
				if(arg instanceof RascalFunction)
					fillStyleFun = (RascalFunction) arg;
				else
					fillStyle = ((IInteger) arg).intValue();
				
			} else if(pname.equals("lineWidth")){
				if(arg instanceof RascalFunction)
					lineWidthFun = (RascalFunction) arg;
				else
					lineWidth = ((IInteger) arg).intValue();
				
			} else if(pname.equals("strokeStyle")){
				if(arg instanceof RascalFunction)
					strokeStyleFun = (RascalFunction) arg;
				else
					strokeStyle = ((IInteger) arg).intValue();
			} else if(pname.equals("values")){
				if(arg instanceof RascalFunction)
					valuesFun = (RascalFunction) arg;
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
	
	protected static IValue[] argVals = new IValue[] { null };
	
	int getIntField(RascalFunction fun, int n, int field){
		if(fun != null){
			argVals[0] = vf.integer(n);
			Result<IValue> res = fun.call(argTypes, argVals);
			if(res.getType().isIntegerType())
				return ((IInteger) res.getValue()).intValue();
			else if(res.getType().isRealType())
				return (int) ((IReal) res.getValue()).floatValue();
			else
				throw RuntimeExceptionFactory.illegalArgument(res.getValue(), ctx.getCurrentAST(), ctx.getStackTrace());
		}
		
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
	
	protected int getGap(int n){
		return getIntField(gapFun, n, gap);
	}
	
	protected int getTop(int n){
		return getIntField(topFun, n, top);
	}
	
	protected int getLineWidth(int n){
		return getIntField(lineWidthFun, n, lineWidth);
	}
	
	protected int getFillStyle(int n){
		return getIntField(fillStyleFun, n, fillStyle);
	}
	
	protected int getStrokeStyle(int n){
		return getIntField(strokeStyleFun, n, strokeStyle);
	}
	
	protected float getValues(int n){
		if(valuesFun != null){
			argVals[0] = vf.integer(n);
			Result<IValue> res = valuesFun.call(argTypes, argVals);
			return ((IInteger) res.getValue()).intValue();
		}
		
		return values[n];		
	}
	
	protected boolean getVisible(){
		return visible;
	}
	
	protected String getTitle(){
		return title;
	}
	
	abstract boolean draw(PApplet pa, int i, int left, int bottom);
	
	abstract BoundingBox bbox();
	
	abstract BoundingBox bbox(int i);
}
