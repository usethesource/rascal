package org.rascalmpl.library.vis;

import java.util.HashMap;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

/**
 * Manage the properties of an element.
 * 
 * @author paulk
 *
 */

@SuppressWarnings("serial")
public class PropertyManager {

	enum Property {
		ANCHOR,
		CONTENTSHIDDEN,
		CONTENTSVISIBLE,
		DOI,
		FILLCOLOR, 
		FONT, 
		FONTCOLOR, 
		FONTSIZE, 
		FROMANGLE,
		GAP, 
		HANCHOR,
		HEIGHT, 
		HGAP, 
		HINT,
		ID, 
		INNERRADIUS, 
		LINECOLOR, 
		LINEWIDTH, 
		MOUSEOVER, 
		PINNED,
		SHAPECLOSED, 
		SHAPECONNECTED,
		SHAPECURVED,
		SIZE, 
		TEXTANGLE, 
		TOANGLE,
		VANCHOR,
		VGAP, 
		WIDTH
	}

	static final HashMap<String, Property> propertyNames = new HashMap<String, Property>() {
		{
			put("anchor", Property.ANCHOR);
			put("contentsHidden", Property.CONTENTSHIDDEN);
			put("contentsVisible", Property.CONTENTSVISIBLE);
			put("doi",       Property.DOI);
			put("fillColor", Property.FILLCOLOR);
			put("font", Property.FONT);
			put("fontColor", Property.FONTCOLOR);
			put("fontSize", Property.FONTSIZE);
			put("fromAngle", Property.FROMANGLE);
			put("gap", Property.GAP);
			put("hanchor", Property.HANCHOR);
			put("height", Property.HEIGHT);
			put("hgap", Property.HGAP);                  // Only used internally
			put("hint", Property.HINT);
			put("id", Property.ID);
			put("innerRadius", Property.INNERRADIUS);
			put("lineColor", Property.LINECOLOR);
			put("lineWidth", Property.LINEWIDTH);
			put("mouseOver", Property.MOUSEOVER);
			put("pinned", Property.PINNED);
			put("shapeClosed", Property.SHAPECLOSED);
			put("shapeConnected", Property.SHAPECONNECTED);
			put("shapeCurved", Property.SHAPECURVED);
			put("size", Property.SIZE);
			put("textAngle", Property.TEXTANGLE);
			put("toAngle", Property.TOANGLE);
			put("vanchor", Property.VANCHOR);
			put("vgap", Property.VGAP);                 // Only used internally
			put("width", Property.WIDTH);
		}
	};
	
	boolean contentsVisible;
	int doi;
	int fillColor;
	String font;
	int fontColor;
	int fontSize;
	float fromAngle;
	float hanchor;	
	float height;
	float hgap;
	String hint;
	String id;
	float innerRadius; 
	int lineColor;
	float lineWidth;
	boolean pinned;
	boolean shapeClosed;
	boolean shapeConnected;
	boolean shapeCurved;
	float textAngle; 
	float toAngle;
	float vanchor;
	float vgap;
	float width;
	
	// Interaction and mouse handling
//	IList origMouseOverProperties = null;
//	boolean mouseOver = false;
//	PropertyManager mouseOverproperties = null;
	protected Figure mouseOverFigure = null;
	
	private int getIntArg(IConstructor c){
		return ((IInteger) c.get(0)).intValue();
	}
	
	private String getStrArg(IConstructor c){
		return ((IString) c.get(0)).getValue();
	}
	
	private float getRealArg(IConstructor c, int i){
		return ((IReal) c.get(i)).floatValue();
	}
	
	protected static float getIntOrRealArg(IConstructor c, int i){
		if(c.get(i).getType().isIntegerType())
			return  ((IInteger) c.get(i)).intValue();
		
		return ((IReal) c.get(i)).floatValue();
	}

	private int getColorArg(IConstructor c, IEvaluatorContext ctx) {
		IValue arg = c.get(0);
		if (arg.getType().isStringType()) {
			IInteger cl = FigureLibrary.colorNames.get(((IString) arg).getValue().toLowerCase());
			if (cl != null)
				return cl.intValue();
			
			throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		return ((IInteger) arg).intValue();
	}

	PropertyManager(FigurePApplet fpa, PropertyManager inherited, IList props, IEvaluatorContext ctx) {
		if(inherited != null)
			importProperties(inherited);
		else
			setDefaults();
		
		for (IValue v : props) {
			IConstructor c = (IConstructor) v;
			String pname = c.getName();

			switch (propertyNames.get(pname)) {
			
			case ANCHOR:
				hanchor = getRealArg(c, 0);
				hanchor = hanchor < 0 ? 0 : (hanchor > 1 ? 1 : hanchor);
				
				vanchor = getRealArg(c, 1);
				vanchor = vanchor < 0 ? 0 : (vanchor > 1 ? 1 : vanchor);
				System.err.printf("anchor: %f, %f\n", hanchor, vanchor);
				break;
				
			case CONTENTSHIDDEN:
				contentsVisible = false; break;
				
			case CONTENTSVISIBLE:
				contentsVisible = true; break;
				
			case DOI:
				doi = getIntArg(c); break;
			
			case FILLCOLOR:
				fillColor = getColorArg(c, ctx); break;
				
			case FONT:
				font = getStrArg(c); break;
				
			case FONTCOLOR:
				fontColor = getColorArg(c, ctx); break;
				
			case FONTSIZE:
				fontSize = getIntArg(c); break;
				
			case FROMANGLE:
				fromAngle = getIntOrRealArg(c, 0); break;
			
			case GAP:
				if(c.arity() == 1){
					hgap =  vgap = getIntOrRealArg(c, 0);
					
				} else {
					hgap = getIntOrRealArg(c, 0);
					vgap = getIntOrRealArg(c, 1);
				}
				break;
				
			case HANCHOR:
				hanchor = getRealArg(c, 0);
				hanchor = hanchor < 0 ? 0 : (hanchor > 1 ? 1 : hanchor);
				break;
				
			case HEIGHT:
				height =  getIntOrRealArg(c, 0); break;
				
			case HGAP:
				hgap = getIntOrRealArg(c, 0); break;
				
			case HINT:
				hint = getStrArg(c).toLowerCase(); break;
				
			case ID:
				id = getStrArg(c); break;
				
			case INNERRADIUS:
				innerRadius = getIntOrRealArg(c, 0); break;
				
			case LINECOLOR:
				lineColor = getColorArg(c, ctx); break;
				
			case LINEWIDTH:
				lineWidth = getIntOrRealArg(c, 0); break;

			case MOUSEOVER:
				//origMouseOverProperties = (IList) c.get(0);
				//mouseOverproperties = new PropertyManager(vlp, this, (IList) c.get(0), ctx);
				//if(c.arity() == 2){
					//mouseOverFigure = FigureFactory.make(vlp, (IConstructor)c.get(1), mouseOverproperties, ctx);
					mouseOverFigure = FigureFactory.make(fpa, (IConstructor)c.get(0), null, ctx);
				//}
				break;	
			case PINNED:
				pinned = true; break;
				
			case SHAPECLOSED:
				shapeClosed = true; break;
				
			case SHAPECONNECTED:
				shapeConnected = true; break;	
				
			case SHAPECURVED:
				shapeCurved = true; break;
			
			case SIZE:
				if(c.arity() == 1){
					width = height =  getIntOrRealArg(c, 0);
				} else {
					width =  getIntOrRealArg(c, 0);
					height =  getIntOrRealArg(c, 1);
				}
				break;
				
			case TEXTANGLE:
				textAngle = getIntOrRealArg(c, 0); break;
				
			case TOANGLE:
				toAngle = getIntOrRealArg(c, 0); break;
				
			case VANCHOR:
				vanchor = getRealArg(c, 0);
				vanchor = vanchor < 0 ? 0 : (vanchor > 1 ? 1 : vanchor);
				break;
				
			case VGAP:
				vgap = getIntOrRealArg(c, 0); break;
			
			case WIDTH:
				width = getIntOrRealArg(c, 0); break;
				
			default:
				throw RuntimeExceptionFactory.illegalArgument(c, ctx
						.getCurrentAST(), ctx.getStackTrace());
			}
		}
		//if(inherited != null && origMouseOverProperties == null && inherited.mouseOverproperties != null)
		//	mouseOverproperties = new PropertyManager(vlp, this, inherited.origMouseOverProperties, ctx);
	}
	
	private void importProperties(PropertyManager inh) {
		contentsVisible = inh.contentsVisible;
		doi = inh.doi;
		fillColor = inh.fillColor;
		font = inh.font;
		fontColor = inh.fontColor;
		fontSize = inh.fontSize;
		fromAngle = inh.fromAngle;
		hanchor = inh.hanchor;
		height = inh.height;
		hgap = inh.hgap;
		hint = inh.hint;
		id = inh.id;
		innerRadius = inh.innerRadius;
		lineColor = inh.lineColor;
		lineWidth = inh.lineWidth;
		pinned = inh.pinned;
		shapeClosed = inh.shapeClosed;
		shapeConnected = inh.shapeConnected;
		shapeCurved = inh.shapeCurved;
		textAngle = inh.textAngle;
		toAngle = inh.toAngle;
		vanchor = inh.vanchor;
		vgap = inh.vgap;
		width = inh.width;
	}
	
	private void setDefaults() {
		contentsVisible = true;
		doi = 1000000;
		fillColor = 255;
		font = "Helvetica";
		fontColor = 0;
		fontSize = 12;
		fromAngle = 0;
		hanchor = 0.5f;
		height = 0;
		hgap = 0;
		hint = "";
		id = "";
		innerRadius = 0;
		lineColor = 0;
		lineWidth = 1;
		pinned = false;
		shapeClosed = false;
		shapeConnected = false;
		shapeCurved = false;
		textAngle = 0;
		toAngle = 0;
		vanchor = 0.5f;
		vgap = 0;
		width = 0;
		mouseOverFigure = null;
	}
	

}
