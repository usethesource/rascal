package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
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
	
	protected HashMap<String,IValue> properties;
	
	float values[];								// Data values
	RascalFunction valuesFun = null;
	
	VELEM(IEvaluatorContext ctx){
		this.ctx = ctx;
		vf = ValueFactoryFactory.getValueFactory();
		properties = new HashMap<String,IValue>();
	}
	
	@SuppressWarnings("unchecked")
	VELEM(HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx){
		this.ctx = ctx;
		vf = ValueFactoryFactory.getValueFactory();
		if(inheritedProps != null)
			properties = (HashMap<String, IValue>) inheritedProps.clone();
		else
			properties = new HashMap<String,IValue>();
		getProps(props);
	}
	
	protected void getProps(IList props){
		
		for(IValue v : props){
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			IValue arg = c.get(0);
			System.err.println("pname = " + pname + ", arg = " + arg);
			if(pname.equals("values")){
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
			} else
				properties.put(pname, arg);
		}
	}
	
	public int max(int a, int b){
		return a > b ? a : b;
	}
	
	protected static Type[] argTypes = new Type[] {TypeFactory.getInstance().integerType()};
	
	protected static IValue[] argVals = new IValue[] { null };
	
	protected int getIntProperty(String prop, int n){
		Object propVal = properties.get(prop);
		if(propVal != null){
			if(propVal instanceof RascalFunction){
				RascalFunction fun = (RascalFunction) propVal;
				argVals[0] = vf.integer(n);
				Result<IValue> res = fun.call(argTypes, argVals);
				if(res.getType().isIntegerType())
					return ((IInteger) res.getValue()).intValue();
				else if(res.getType().isRealType())
					return (int) ((IReal) res.getValue()).floatValue();
				else
					throw RuntimeExceptionFactory.illegalArgument(res.getValue(), ctx.getCurrentAST(), ctx.getStackTrace());	
			} else if(propVal instanceof IInteger){
				return ((IInteger)propVal).intValue();
			}
		}
		return 0;
	}
	
	protected int getBottom(int n){
		return getIntProperty("bottom", n);
	}
	
	protected int getLeft(int n){
		return getIntProperty("left", n);
	}
	
	protected int getRight(int n){
		return getIntProperty("right", n);
	}
	
	protected int getHeight(int n){
		return getIntProperty("height", n);
	}


	protected int getWidth(int n){
		return getIntProperty("width", n);
	}
	
	protected int getGap(int n){
		return getIntProperty("gap", n);
	}
	
	protected int getOffset(int n){
		return getIntProperty("offset", n);
	}
	
	protected int getTop(int n){
		return getIntProperty("top", n);
	}
	
	protected int getLineWidth(int n){
		return getIntProperty("lineWidth", n);
	}
	
	protected int getFillStyle(int n){
		return getIntProperty("fillStyle", n);
	}
	
	protected int getStrokeStyle(int n){
		return getIntProperty("strokeStyle", n);
	}
	
	protected boolean isVertical(){
		return properties.get("vertical") != null;
	}
	
	protected boolean isHorizontal(){
		return properties.get("horizontal") != null ||  properties.get("vertical") == null;
	}
	
	protected int getNumberOfValues(){
		return values.length;
	}
	
	protected float getValue(int n){
		if(valuesFun != null){
			argVals[0] = vf.integer(n);
			Result<IValue> res = valuesFun.call(argTypes, argVals);
			return ((IInteger) res.getValue()).intValue();
		}
		return values[n];		
	}
	
	abstract BoundingBox draw(PApplet pa, int i, int left, int bottom);
	
}
