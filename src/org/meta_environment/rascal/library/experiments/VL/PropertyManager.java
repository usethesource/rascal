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
		WIDTH, HEIGHT, SIZE, GAP, HORIZONTAL, VERTICAL, TOP, CENTER, BOTTOM, LEFT, RIGHT, 
		LINE_WIDTH, LINE_COLOR, FILL_COLOR, FROM_ANGLE, TO_ANGLE, INNER_RADIUS, TEXT, FONT, 
		FONT_SIZE, TEXT_ANGLE, MOUSE_OVER, NAME, CLOSED, CURVED
	};

	static final HashMap<String, Property> propertyNames = new HashMap<String, Property>() {
		{
			put("width", Property.WIDTH);
			put("height", Property.HEIGHT);
			put("size", Property.SIZE);
			put("gap", Property.GAP);
			put("lineWidth", Property.LINE_WIDTH);
			put("lineColor", Property.LINE_COLOR);
			put("fillColor", Property.FILL_COLOR);
			put("fromAngle", Property.FROM_ANGLE);
			put("toAngle", Property.TO_ANGLE);
			put("innerRadius", Property.INNER_RADIUS);

			put("text", Property.TEXT);
			put("font", Property.FONT);
			put("fontSize", Property.FONT_SIZE);
			put("textAngle", Property.TEXT_ANGLE);
			put("mouseOver", Property.MOUSE_OVER);
			put("name", Property.NAME);

			put("horizontal", Property.HORIZONTAL);
			put("vertical", Property.VERTICAL);
			put("top", Property.TOP);
			put("center", Property.CENTER);
			put("bottom", Property.BOTTOM);
			put("left", Property.LEFT);
			put("right", Property.RIGHT);
			put("closed", Property.CLOSED);
			put("curved", Property.CURVED);
		}
	};

	EnumMap<Property, Integer> intProperties;
	EnumMap<Property, String> strProperties;
	EnumSet<Property> boolProperties;
	EnumSet<Property> defined;
	IList origMouseOverProperties = null;
	boolean mouseOver = false;
	PropertyManager mouseOverproperties = null;
	
	private int getIntArg(IConstructor c){
		return ((IInteger) c.get(0)).intValue();
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
			else
				throw RuntimeExceptionFactory.illegalArgument(c, ctx
						.getCurrentAST(), ctx.getStackTrace());
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
	
//	@Override
//	public PropertyManager clone(){
//		
//		PropertyManager pm = new PropertyManager();
//		pm.intProperties = intProperties.clone();
//		pm.boolProperties = boolProperties.clone();
//		pm.strProperties = strProperties.clone();
//		if(mouseOverproperties != null)
//			pm.mouseOverproperties = mouseOverproperties.clone();
//		return pm;
//	}
	
	PropertyManager(){
		
	}

	PropertyManager(PropertyManager inherited, IList props, IEvaluatorContext ctx) {
		
		defined = EnumSet.noneOf(Property.class);
		if(inherited != null){
			intProperties = inherited.intProperties.clone();
			boolProperties = inherited.boolProperties.clone();
			strProperties = inherited.strProperties.clone();
			//if(inherited.mouseOverproperties != null)
			//	mouseOverproperties = inherited.mouseOverproperties.clone();
		} else {
			intProperties = new EnumMap<Property, Integer>(Property.class);
			strProperties = new EnumMap<Property, String>(	Property.class);
			boolProperties = EnumSet.of(Property.HORIZONTAL,
					Property.VERTICAL, Property.TOP, Property.CENTER, Property.BOTTOM,
					Property.LEFT, Property.RIGHT, Property.CLOSED, Property.CURVED);
			setDefaults();
		}
		
		for (IValue v : props) {
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			System.err.println("property: " + pname);

			switch (propertyNames.get(pname)) {
			case WIDTH:
				defInt(Property.WIDTH, getIntArg(c)); break;
			case HEIGHT:
				defInt(Property.HEIGHT, getIntArg(c));	break;
			case SIZE:
				defInt(Property.SIZE, getIntArg(c)); break;
			case GAP:
				defInt(Property.GAP, getIntArg(c)); break;
			case LINE_WIDTH:
				defInt(Property.LINE_WIDTH, getIntArg(c));	break;
			case FROM_ANGLE:
				defInt(Property.FROM_ANGLE, getIntArg(c));	break;
			case TO_ANGLE:
				defInt(Property.TO_ANGLE, getIntArg(c)); break;
			case INNER_RADIUS:
				defInt(Property.INNER_RADIUS, getIntArg(c)); break;

			case LINE_COLOR:
				defColor(Property.LINE_COLOR, c, ctx); break;
			case FILL_COLOR:
				defColor(Property.FILL_COLOR, c, ctx); 	break;

			case TEXT:
				defStr(Property.TEXT, getStrArg(c)); break;
			case FONT:
				defStr(Property.FONT, getStrArg(c)); break;
			case NAME:
				defStr(Property.NAME, getStrArg(c)); break;

			case FONT_SIZE:
				defInt(Property.FONT_SIZE,  getIntArg(c)); break;
			case TEXT_ANGLE:
				defInt(Property.TEXT_ANGLE,  getIntArg(c)); break;

			case MOUSE_OVER:
				origMouseOverProperties = (IList) c.get(0);
				mouseOverproperties = new PropertyManager(this, (IList) c.get(0), ctx); break;

			case HORIZONTAL:
				defBool(Property.HORIZONTAL, true); defBool(Property.VERTICAL, false);break;
			case VERTICAL:
				defBool(Property.VERTICAL, true); defBool(Property.HORIZONTAL, false);	break;
			case TOP:
				defBool(Property.TOP, true); defBool(Property.BOTTOM, true); break;
			case CENTER:
				defBool(Property.CENTER, true);	break;
			case BOTTOM:
				defBool(Property.BOTTOM, true); defBool(Property.TOP, false); break;
			case LEFT:
				defBool(Property.LEFT, true);	defBool(Property.RIGHT, false); break;
			case RIGHT:
				defBool(Property.RIGHT, true); defBool(Property.LEFT, false); break;
			case CLOSED:
				defBool(Property.CLOSED, true); break;
			case CURVED:
				defBool(Property.CURVED, true); break;
			default:
				throw RuntimeExceptionFactory.illegalArgument(c, ctx
						.getCurrentAST(), ctx.getStackTrace());
			}
		}
		if(inherited != null && origMouseOverProperties == null && inherited.mouseOverproperties != null)
			mouseOverproperties = new PropertyManager(this, inherited.origMouseOverProperties, ctx);
	}
	
	private void setDefaults() {
		defInt(Property.WIDTH, 0);
		defInt(Property.HEIGHT, 0);
		defInt(Property.SIZE, 0);
		defInt(Property.GAP, 0);
		defInt(Property.LINE_WIDTH, 1);
		defInt(Property.FROM_ANGLE, 0);
		defInt(Property.TO_ANGLE, 360);
		defInt(Property.INNER_RADIUS, 0);
		
		intProperties.put(Property.LINE_COLOR,0);	defined.add(Property.LINE_COLOR);
		intProperties.put(Property.FILL_COLOR,255);	defined.add(Property.FILL_COLOR);
		
		defStr(Property.TEXT, "");
		defStr(Property.FONT, "Helvetica");
		defStr(Property.NAME, "");
		defInt(Property.FONT_SIZE, 12);
		defInt(Property.TEXT_ANGLE, 0);
		defBool(Property.HORIZONTAL,true);
		defBool(Property.VERTICAL, false);
		defBool(Property.TOP, false);
		defBool(Property.CENTER, true);
		defBool(Property.BOTTOM, false);
		defBool(Property.LEFT, false);
		defBool(Property.RIGHT, false);
		defBool(Property.CLOSED, false);
		defBool(Property.CURVED, false);
	}
	
	public int getInt(Property p){
		return intProperties.get(p);
	}
	
	public String getStr(Property p){
		return strProperties.get(p);
	}
	
	public boolean getBool(Property p){
		return boolProperties.contains(p);
	}
	
	public void applyProperties(VLPApplet vlp){
		if(mouseOver && mouseOverproperties != null)
			mouseOverproperties.applyProperties(vlp);
		else {
			vlp.fill(intProperties.get(Property.FILL_COLOR));
			vlp.stroke(intProperties.get(Property.LINE_COLOR));
			vlp.strokeWeight(intProperties.get(Property.LINE_WIDTH));
			vlp.textSize(intProperties.get(Property.FONT_SIZE));
		}
	}
	
	public void setMouseOver(boolean on){
		mouseOver = on;
	}

}
