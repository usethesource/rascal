package org.meta_environment.rascal.library.experiments.VL;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

@SuppressWarnings("serial")
public class PropertyManager implements Cloneable {

	enum Property {
		BOTTOM, 
		CENTER,
		CLOSED, 
		CONNECTED,
		CURVED,
		FILLCOLOR, 
		FONT, 
		FONTCOLOR, 
		FONTSIZE, 
		FROMANGLE,
		GAP, 
		HCENTER, 
		HEIGHT, 
		HGAP, 
		ID, 
		INNERRADIUS, 
		LEFT,
		LINECOLOR, 
		LINEWIDTH, 
		MOUSEOVER, 
		RIGHT, 
		SIZE, 
		TEXTANGLE, 
		TOANGLE,
		TOP, 
		VCENTER, 
		VGAP, 
		WIDTH
	}

	static final HashMap<String, Property> propertyNames = new HashMap<String, Property>() {
		{
			put("bottom", Property.BOTTOM);
			put("center", Property.CENTER);
			put("closed", Property.CLOSED);
			put("connected", Property.CONNECTED);
			put("curved", Property.CURVED);
			put("fillColor", Property.FILLCOLOR);
			put("font", Property.FONT);
			put("fontColor", Property.FONTCOLOR);
			put("fontSize", Property.FONTSIZE);
			put("fromAngle", Property.FROMANGLE);
			put("gap", Property.GAP);	
			put("hcenter", Property.HCENTER);
			put("height", Property.HEIGHT);
			put("hgap", Property.HGAP);                  // Only used internally
			put("id", Property.ID);
			put("innerRadius", Property.INNERRADIUS);
			put("left", Property.LEFT);
			put("lineColor", Property.LINECOLOR);
			put("lineWidth", Property.LINEWIDTH);
			put("mouseOver", Property.MOUSEOVER);
			put("right", Property.RIGHT);
			put("size", Property.SIZE);
			put("textAngle", Property.TEXTANGLE);
			put("toAngle", Property.TOANGLE);
			put("top", Property.TOP);
			put("vcenter", Property.VCENTER);
			put("vgap", Property.VGAP);                 // Only used internally
			put("width", Property.WIDTH);
		}
	};

	EnumMap<Property, Integer> intProperties;
	EnumMap<Property, String> strProperties;
	EnumSet<Property> boolProperties;
	EnumSet<Property> defined;
	float hanchor;
	float vanchor;
	IList origMouseOverProperties = null;
	boolean mouseOver = false;
	PropertyManager mouseOverproperties = null;
	protected VELEM mouseOverVElem = null;
	private VLPApplet vlp;
	
	private int getIntArg(IConstructor c){
		return ((IInteger) c.get(0)).intValue();
	}
	
	private int getIntArg(IConstructor c, int i){
		return ((IInteger) c.get(i)).intValue();
	}
	
	private String getStrArg(IConstructor c){
		return ((IString) c.get(0)).getValue();
	}

	private int getColorArg(IConstructor c, IEvaluatorContext ctx) {
		IValue arg = c.get(0);
		if (arg.getType().isStringType()) {
			IInteger cl = VL.colorNames.get(((IString) arg).getValue());
			if (cl != null)
				return cl.intValue();
			
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		return ((IInteger) arg).intValue();
	}
	
	private void defInt(Property p, int n){
		intProperties.put(p, n);
		defined.add(p);
	}
	
	private void defColor(Property p, IConstructor c, IEvaluatorContext ctx){
		intProperties.put(p, getColorArg(c, ctx));
		defined.add(p);
	}
	
	private void defStr(Property p, String s){
		strProperties.put(p,s);
		defined.add(p); 
	}
	
	private void defBool(Property p, boolean isTrue){
		if(isTrue)
			boolProperties.add(p);
		else
			boolProperties.remove(p);
		defined.add(p);
	}
	

	PropertyManager(VLPApplet vlp, PropertyManager inherited, IList props, IEvaluatorContext ctx) {
		this.vlp = vlp;
		defined = EnumSet.noneOf(Property.class);
		if(inherited != null){
			intProperties = inherited.intProperties.clone();
			boolProperties = inherited.boolProperties.clone();
			strProperties = inherited.strProperties.clone();
			hanchor = inherited.hanchor;
			vanchor = inherited.vanchor;
		} else {
			intProperties = new EnumMap<Property, Integer>(Property.class);
			strProperties = new EnumMap<Property, String>(	Property.class);
			boolProperties = EnumSet.of(
					Property.BOTTOM,
					Property.CLOSED, 
					Property.CONNECTED, 
					Property.CURVED,
					Property.HCENTER, 
					Property.LEFT, 
					Property.TOP, 
					Property.RIGHT, 
					Property.VCENTER);
			setDefaults();
		}
		
		for (IValue v : props) {
			IConstructor c = (IConstructor) v;
			String pname = c.getName();

			switch (propertyNames.get(pname)) {
			
			case BOTTOM:
				defBool(Property.BOTTOM, true); 
				defBool(Property.TOP, false); 
				defBool(Property.VCENTER, false);
				vanchor = 1.0f;
				break;
				
			case CENTER:
				defBool(Property.HCENTER, true);	
				defBool(Property.LEFT, false);	
				defBool(Property.RIGHT, false);
				
				defBool(Property.VCENTER, true);	
				defBool(Property.TOP, false);	
				defBool(Property.BOTTOM, false);
				hanchor = vanchor = 0.5f;
				break;
				
			case CLOSED:
				defBool(Property.CLOSED, true); break;
				
			case CONNECTED:
				defBool(Property.CONNECTED, true); break;
				
			case CURVED:
				defBool(Property.CURVED, true); break;
			
			case FILLCOLOR:
				defColor(Property.FILLCOLOR, c, ctx); 	break;
				
			case FONT:
				defStr(Property.FONT, getStrArg(c)); break;
				
			case FONTCOLOR:
				defColor(Property.FONTCOLOR, c, ctx); 	break;
				
			case FONTSIZE:
				defInt(Property.FONTSIZE,  getIntArg(c)); break;
				
			case FROMANGLE:
				defInt(Property.FROMANGLE, getIntArg(c));	break;
				
			case GAP:
				if(c.arity() == 1){
					defInt(Property.HGAP, getIntArg(c, 0));
					defInt(Property.VGAP, getIntArg(c, 0));
				} else {
					defInt(Property.HGAP, getIntArg(c, 0));
					defInt(Property.VGAP, getIntArg(c, 1));
				}
				break;
				
			case HCENTER:
				defBool(Property.HCENTER, true);	
				defBool(Property.LEFT, false);	
				defBool(Property.RIGHT, false); 
				hanchor = 0.5f;
				break;
				
			case HEIGHT:
				defInt(Property.HEIGHT, getIntArg(c));	break;
				
			case ID:
				defStr(Property.ID, getStrArg(c)); break;
				
			case INNERRADIUS:
				defInt(Property.INNERRADIUS, getIntArg(c)); break;
				
			case LEFT:
				defBool(Property.LEFT, true);	
				defBool(Property.RIGHT, false); 
				defBool(Property.HCENTER, false);
				hanchor = 0f;
				break;
				
			case LINECOLOR:
				defColor(Property.LINECOLOR, c, ctx); break;
				
			case LINEWIDTH:
				defInt(Property.LINEWIDTH, getIntArg(c));	break;

			case MOUSEOVER:
				origMouseOverProperties = (IList) c.get(0);
				mouseOverproperties = new PropertyManager(vlp, this, (IList) c.get(0), ctx);
				if(c.arity() == 2){
					mouseOverVElem = VELEMFactory.make(vlp, (IConstructor)c.get(1), mouseOverproperties, ctx);
				}
				break;	
				
			case RIGHT:
				defBool(Property.RIGHT, true); 
				defBool(Property.LEFT, false); 
				defBool(Property.HCENTER, false);
				hanchor = 1f;
				break;
				
			case SIZE:
				if(c.arity() == 1){
					defInt(Property.WIDTH, getIntArg(c, 0));
					defInt(Property.HEIGHT, getIntArg(c, 0));
				} else {
					defInt(Property.WIDTH, getIntArg(c, 0));
					defInt(Property.HEIGHT, getIntArg(c, 1));
				}
				break;
				
			case TEXTANGLE:
				defInt(Property.TEXTANGLE,  getIntArg(c)); break;
				
			case TOANGLE:
				defInt(Property.TOANGLE, getIntArg(c)); break;
				
			case TOP:
				defBool(Property.TOP, true); 
				defBool(Property.VCENTER, false);
				defBool(Property.BOTTOM, false); 
				vanchor = 0f;
				break;
			
			case VCENTER:
				defBool(Property.VCENTER, true);	
				defBool(Property.TOP, false);	
				defBool(Property.BOTTOM, false); 
				vanchor = 0.5f;
				break;
			
			case WIDTH:
				defInt(Property.WIDTH, getIntArg(c)); break;
				
			default:
				throw RuntimeExceptionFactory.illegalArgument(c, ctx
						.getCurrentAST(), ctx.getStackTrace());
			}
		}
		if(inherited != null && origMouseOverProperties == null && inherited.mouseOverproperties != null)
			mouseOverproperties = new PropertyManager(vlp, this, inherited.origMouseOverProperties, ctx);
	}
	
	private void setDefaults() {
		defInt(Property.WIDTH, 0);
		defInt(Property.HEIGHT, 0);
		defInt(Property.HGAP, 0);
		defInt(Property.VGAP, 0);
		defInt(Property.LINEWIDTH, 1);
		defInt(Property.FROMANGLE, 0);
		defInt(Property.TOANGLE, 360);
		defInt(Property.INNERRADIUS, 0);
		
		intProperties.put(Property.LINECOLOR,0);	defined.add(Property.LINECOLOR);
		intProperties.put(Property.FILLCOLOR,255);	defined.add(Property.FILLCOLOR);
		intProperties.put(Property.FONTCOLOR,0);	defined.add(Property.FONTCOLOR);
		
		defStr(Property.FONT, "Helvetica");
		defStr(Property.ID, "");
		
		defInt(Property.FONTSIZE, 12);
		defInt(Property.TEXTANGLE, 0);
		
		defBool(Property.BOTTOM, false);
		defBool(Property.CLOSED, false);
		defBool(Property.CONNECTED, false);
		defBool(Property.CURVED, false);
		defBool(Property.HCENTER, true);
		defBool(Property.LEFT, false);
		defBool(Property.RIGHT, false);
		defBool(Property.TOP, false);
		defBool(Property.VCENTER, true);
	}
	
	public int getInt(Property p){
		return intProperties.get(p).intValue();
	}
	
	public String getStr(Property p){
		return strProperties.get(p);
	}
	
	public boolean getBool(Property p){
		return boolProperties.contains(p);
	}
	
	public void applyProperties(){
		if(mouseOver && mouseOverproperties != null)
			mouseOverproperties.applyProperties();
		else {
			vlp.fill(getInt(Property.FILLCOLOR));
			vlp.stroke(getInt(Property.LINECOLOR));
			vlp.strokeWeight(getInt(Property.LINEWIDTH));
			vlp.textSize(getInt(Property.FONTSIZE));
		}
	}
	
	public void applyFontColorProperty(){
		if(mouseOver && mouseOverproperties != null)
			mouseOverproperties.applyProperties();
		else {
			vlp.fill(getInt(Property.FONTCOLOR));
		}
	}
	
	public void setMouseOver(boolean on){
		mouseOver = on;
	}

}
