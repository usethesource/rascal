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
	
	protected float x;               // x position of centre
	protected float y; 				// y position of centre
	protected float width = 0;		// width of element
	protected float height = 0;		// height picture
	
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
	
	public float max(float a, float b){
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
				System.err.println("translate: " + x + ", " + y);
				vlp.translate(x, y);
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
	
	protected int getHeightProperty(){
		return getIntProperty("height", -1);
	}
	
	protected int getWidthProperty(){
		return getIntProperty("width", -1);
	}
	
	protected int getSizeProperty(){
		return getIntProperty("size", -1);
	}
	
	protected int getGapProperty(){
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
	
	protected boolean isBottom(){
		return properties.get("bottom") != null;
	}
	
	protected boolean isLeft(){
		return properties.get("left") != null;
	}
	
	protected boolean isRight(){
		return properties.get("right") != null;
	}
	
	protected boolean isClosed(){
		return properties.get("closed") != null;
	}
	
	protected boolean isCurved(){
		return properties.get("curved") != null;
	}
	
	protected String getTextProperty(){
		IValue is =  properties.get("text");
		if(is != null)
			return ((IString) is).getValue();
		return "";
	}
	
	protected String getNameProperty(){
		IValue is =  properties.get("name");
		if(is != null)
			return ((IString) is).getValue();
		return "";
	}
	
	protected String getFontProperty(){
		IValue is =  properties.get("font");
		if(is != null)
			return ((IString) is).getValue();
		return "Helvetica";
	}
	
	protected int getFontSizeProperty(){
		IValue fs =  properties.get("fontSize");
		if(fs != null)
			return ((IInteger) fs).intValue();
		return 12;
	}
	
	protected int getTextAngleProperty(){
		IValue ta =  properties.get("textAngle");
		if(ta != null)
			return ((IInteger) ta).intValue();
		return 0;
	}
		
	abstract void draw(float x, float y);
	
	abstract BoundingBox bbox();

	public void mouseOver(int x, int y){
	}
	
}
