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
	protected VLPApplet vlp;
	
	protected IValueFactory vf;
	
	protected HashMap<String,IValue> properties;
	
	protected int left = 0;
	protected int bottom = 0;
	protected int width = 0;
	protected int height = 0;
	
	VELEM(VLPApplet vlp, IEvaluatorContext ctx){
		this.vlp = vlp;
		this.ctx = ctx;
		vf = ValueFactoryFactory.getValueFactory();
		properties = new HashMap<String,IValue>();
	}
	
	@SuppressWarnings("unchecked")
	VELEM(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx){
		this.vlp = vlp;
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
			if(pname.equals("fillColor") || pname.equals("lineColor")){
				if(arg.getType().isStringType()){
					IInteger cl = VL.colorNames.get(((IString)arg).getValue());
					if(cl != null)
						arg = cl;
					else
						throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
				}
			}
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
	
	protected float getFloat(IValue v){
		if(v instanceof IReal)
			return ((IReal)v).floatValue();
		return 0;
	}
	
	public void applyProperties(){
		
		for(String prop :properties.keySet()){
			if(prop.equals("fillColor"))
				vlp.fill(getInt(properties.get(prop)));
			if(prop.equals("lineColor"))
				vlp.stroke(getInt(properties.get(prop)));
			if(prop.equals("lineWidth"))
				vlp.strokeWeight(getInt(properties.get(prop)));
			if(prop.equals("fontSize"))
				vlp.textSize(getInt(properties.get(prop)));
			if(prop.equals("rotate")){
				System.err.println("translate: " + (left + width/2) + ", " + (bottom - height/2));
				vlp.translate(left + width/2, bottom - height/2);
				vlp.rotate(PApplet.radians(getInt(properties.get(prop))));
			}
		}
		//vlp.textFont(vlp.createFont(getFont(), getFontSize()));
	}
	
	public void printProperties(){
		for(String prop :properties.keySet()){
			System.err.println(prop + ": " + properties.get(prop));
		}
	}
	
	protected int getHeight(){
		return getIntProperty("height", -1);
	}
	
	protected int getHeight2(){
		return getIntProperty("height2", -1);
	}
	
	protected int getWidth(){
		return getIntProperty("width", -1);
	}
	
	protected int getSize(){
		return getIntProperty("size", -1);
	}
	
	protected int getGap(){
		return getIntProperty("gap", -1);
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
	
	protected String getName(){
		IValue is =  properties.get("name");
		if(is != null)
			return ((IString) is).getValue();
		return "";
	}
	
	protected String getFont(){
		IValue is =  properties.get("font");
		if(is != null)
			return ((IString) is).getValue();
		return "Helvetica";
	}
	
	protected int getFontSize(){
		IValue fs =  properties.get("fontSize");
		if(fs != null)
			return ((IInteger) fs).intValue();
		return 12;
	}
	
	protected int getTextAngle(){
		IValue ta =  properties.get("textAngle");
		if(ta != null)
			return ((IInteger) ta).intValue();
		return 0;
	}
	
	public int getX(){
		return left + width/2;
	}
	
	public int getY(){
		return bottom - height/2;
	}
		
	abstract void draw(int left, int bottom);
	
	abstract BoundingBox bbox();

	public void mouseOver(int x, int y){
	}
	
}
