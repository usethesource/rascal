package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
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
			IValue arg = (c.arity() > 0) ? c.get(0) : vf.bool(true);
			System.err.println("pname = " + pname + ", arg = " + arg);
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
	
	protected int getInt(IValue v){
		if(v instanceof IInteger)
			return ((IInteger)v).intValue();
		return 0;
	}
	
	public void applyProperties(PApplet pa){
		for(String prop :properties.keySet()){
			System.err.println("applyProperties: " + prop);
			if(prop.equals("fillStyle"))
				pa.fill(getInt(properties.get(prop)));
			if(prop.equals("strokeStyle"))
				pa.stroke(getInt(properties.get(prop)));
			if(prop.equals("lineWidth"))
				pa.strokeWeight(getInt(properties.get(prop)));
		}
	}
	
	public void printProperties(){
		for(String prop :properties.keySet()){
			System.err.println(prop + ": " + properties.get(prop));
		}
	}
	
	protected int getHeight(int n){
		return getIntProperty("height", n);
	}
	
	protected int getHeight(){
		return getIntProperty("height", -1);
	}
	
	protected int getHeight2(){
		return getIntProperty("height2", -1);
	}

	protected int getWidth(int n){
		return getIntProperty("width", n);
	}
	
	protected int getWidth(){
		return getIntProperty("width", -1);
	}
	
	protected int getSize(){
		return getIntProperty("size", -1);
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
	
	protected boolean isCenter(){
		return properties.get("center") != null;
	}
	
	protected boolean isTop(){
		System.err.println("isTop: " + properties.get("top"));
		return properties.get("top") != null;
	}
	
	protected boolean isRight(){
		return properties.get("right") != null;
	}
	
	protected String getText(){
		IValue is =  properties.get("text");
		if(is != null)
			return ((IString) is).getValue();
		return "";
	}
	
	abstract BoundingBox draw(PApplet pa, int left, int bottom);
	
	abstract BoundingBox bbox();
	
}
