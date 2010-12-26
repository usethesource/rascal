package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;

/**
 * Manage the properties of a figure.
 * 
 * @author paulk
 *
 */

public class PropertyManager implements IPropertyManager {
	
//	FigurePApplet fpa;
//	int doi;
//	int fillColor;
//	String font;
//	int fontColor;
//	int fontSize;
//	float fromAngle;
//	public float hanchor;	
//	float height;
//	float hgap;
//	String hint;
//	String id;
//	float innerRadius; 
//	int lineColor;
//	float lineWidth;
//	boolean shapeClosed;
//	boolean shapeConnected;
//	boolean shapeCurved;
//	float textAngle; 
//	float toAngle;
//	public float vanchor;
//	float vgap;
//	float width;
	
	// Interaction and mouse handling

	protected Figure mouseOverFigure = null;
	
	
	IPropertyValue values[];
	IPropertyManager inherited;
	
	private int countProperties(IList props){
		int n = 0;
		for (IValue v : props) {
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			switch (propertyNames.get(pname)) {
			case ANCHOR:
			case SIZE:
			case GAP:
				n += 2; break;
			case MOUSEOVER:
				break;
			default:
				n++;
			}
		}
		return n;
	}

	
	public PropertyManager(FigurePApplet fpa, IPropertyManager inherited, IList props, IEvaluatorContext ctx) {
		this.inherited = inherited;
		
		values = new IPropertyValue[countProperties(props)];
		int i = 0;
		
		for (IValue v : props) {
			IConstructor c = (IConstructor) v;
			String pname = c.getName();

			switch (propertyNames.get(pname)) {
			
			case ANCHOR:
				values[i++] = Utils.getRealArg(Property.HANCHOR, c, 0, fpa, ctx);
				//hanchor = hanchor < 0 ? 0 : (hanchor > 1 ? 1 : hanchor);
				
				values[i++] = Utils.getRealArg(Property.VANCHOR, c, 1, fpa, ctx);
				//vanchor = vanchor < 0 ? 0 : (vanchor > 1 ? 1 : vanchor);
				break;
				
			case DOI:
				values[i++] = Utils.getIntArg(Property.DOI, c, fpa, ctx); break;
			
			case FILLCOLOR:
				values[i++] = Utils.getColorArg(Property.FILLCOLOR, c, ctx); break;
				
			case FONT:
				values[i++] = Utils.getStrArg(Property.FONT, c, fpa, ctx); break;
				
			case FONTCOLOR:
				values[i++] = Utils.getColorArg(Property.FONTCOLOR, c, ctx); break;
				
			case FONTSIZE:
				values[i++] = Utils.getIntArg(Property.FONTSIZE, c, fpa, ctx); break;
				
			case FROMANGLE:
				values[i++] = Utils.getIntOrRealArg(Property.FROMANGLE, c, 0, fpa, ctx); break;
			
			case GAP:
				if(c.arity() == 1){
					values[i++] = Utils.getIntOrRealArg(Property.HGAP, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.VGAP, c, 0, fpa, ctx);
					
				} else {
					values[i++] = Utils.getIntOrRealArg(Property.HGAP, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.VGAP, c, 1, fpa, ctx);
				}
				break;
				
			case HANCHOR:
				values[i++] = Utils.getRealArg(Property.HANCHOR, c, 0, fpa, ctx);
				//hanchor = hanchor < 0 ? 0 : (hanchor > 1 ? 1 : hanchor);
				break;
				
			case HEIGHT:
				values[i++] = Utils.getIntOrRealArg(Property.HEIGHT, c, 0, fpa, ctx); break;
				
			case HGAP:
				values[i++] = Utils.getIntOrRealArg(Property.HGAP, c, 0, fpa, ctx); break;
				
			case HINT:
				values[i++] = Utils.getStrArg(Property.HINT, c, fpa, ctx); break;
				//hint = getStrArg(null, c).toLowerCase(); break;
				
			case ID:
				values[i++] = Utils.getStrArg(Property.ID, c, fpa, ctx); break;
				
			case INNERRADIUS:
				values[i++] = Utils.getIntOrRealArg(Property.INNERRADIUS, c, 0, fpa, ctx); break;
				
			case LINECOLOR:
				values[i++] = Utils.getColorArg(Property.LINECOLOR, c, ctx); break;
				
			case LINEWIDTH:
				values[i++] = Utils.getIntOrRealArg(Property.LINEWIDTH, c, 0, fpa, ctx); break;

			case MOUSEOVER:
				mouseOverFigure = FigureFactory.make(fpa, 
													 (IConstructor)c.get(0), 
													 new DefaultPropertyManager(fpa),
													 ctx);
				break;	
				
			case SHAPECLOSED:
				values[i++] = new ConstantBooleanProperty(Property.SHAPECLOSED, true); break;
				
			case SHAPECONNECTED:
				values[i++] = new ConstantBooleanProperty(Property.SHAPECONNECTED, true); break;	
				
			case SHAPECURVED:
				values[i++] = new ConstantBooleanProperty(Property.SHAPECURVED, true); break;	
			
			case SIZE:
				if(c.arity() == 1){
					values[i++] = Utils.getIntOrRealArg(Property.WIDTH, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.HEIGHT, c, 0, fpa, ctx);
				} else {
					values[i++] = Utils.getIntOrRealArg(Property.WIDTH, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.HEIGHT, c, 1, fpa, ctx);
				}
				break;
				
			case TEXTANGLE:
				values[i++] = Utils.getIntOrRealArg(Property.TEXTANGLE, c, 0, fpa, ctx); break;
				
			case TOANGLE:
				values[i++] = Utils.getIntOrRealArg(Property.TOANGLE, c, 0, fpa, ctx); break;
				
			case VANCHOR:
				values[i++] = Utils.getRealArg(Property.VANCHOR, c, 0, fpa, ctx);
				//vanchor = vanchor < 0 ? 0 : (vanchor > 1 ? 1 : vanchor);
				break;
				
			case VGAP:
				values[i++] = Utils.getIntOrRealArg(Property.VGAP, c, 0, fpa, ctx); break;
			
			case WIDTH:
				values[i++] = Utils.getIntOrRealArg(Property.WIDTH, c, 0, fpa, ctx); break;
				
			default:
				throw RuntimeExceptionFactory.illegalArgument(c, ctx
						.getCurrentAST(), ctx.getStackTrace());
			}
		}
	}
	
	public FigurePApplet getFPA() {
		return inherited.getFPA();
	}
	
	public int getDOI(){
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.DOI)
				return ((IIntegerPropertyValue) pv).getValue();
		}
		return inherited.getDOI();
	}

	public int getFillColor() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.FILLCOLOR)
				return ((IIntegerPropertyValue) pv).getValue();
		}
		return inherited.getFillColor();
	}

	public String getFont() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.FONT)
				return ((IStringPropertyValue) pv).getValue();
		}
		return inherited.getFont();
	}

	public int getFontColor() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.FONTCOLOR)
				return ((IIntegerPropertyValue) pv).getValue();
		}
		return inherited.getFontColor();
	}

	public int getFontSize() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.FONTSIZE)
				return ((IIntegerPropertyValue) pv).getValue();
		}
		return inherited.getFontSize();
	}

	public float getFromAngle() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.FROMANGLE)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getFromAngle();
	}
	
	public float getHanchor() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.HANCHOR)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getHanchor();
	}

	public float getHGap() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.HGAP)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getHGap();
	}

	public float getHeight() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.HEIGHT)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getHeight();
	}

	public String getHint() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.HINT)
				return ((IStringPropertyValue) pv).getValue();
		}
		return inherited.getHint();
	}

	public String getId() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.ID)
				return ((IStringPropertyValue) pv).getValue();
		}
		return inherited.getId();
	}

	public float getInnerRadius() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.INNERRADIUS)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getInnerRadius();
	}

	public int getLineColor() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.LINECOLOR)
				return ((IIntegerPropertyValue) pv).getValue();
		}
		return inherited.getLineColor();
	}

	public float getLineWidth() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.LINEWIDTH)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getLineWidth();
	}

	public Figure getMouseOver() {
		// TODO Auto-generated method stub
		return mouseOverFigure;
	}

	public float getTextAngle() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.TEXTANGLE)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getTextAngle();
	}

	public float getVGap() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.VGAP)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getVGap();
	}

	public float getVanchor() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.VANCHOR)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getVanchor();
	}

	public float getWidth() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.WIDTH){
				return ((IRealPropertyValue) pv).getValue();
			}
		}
		return inherited.getWidth();
	}

	public boolean isShapeClosed() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.SHAPECLOSED)
				return ((IBooleanPropertyValue) pv).getValue();
		}
		return inherited.isShapeClosed();
	}

	public boolean isShapeConnected() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.SHAPECONNECTED)
				return ((IBooleanPropertyValue) pv).getValue();
		}
		return inherited.isShapeConnected();
	}

	public boolean isShapeCurved() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.SHAPECURVED)
				return ((IBooleanPropertyValue) pv).getValue();
		}
		return inherited.isShapeCurved();
	}

	public float getToAngle() {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == Property.TOANGLE)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getToAngle();
	}
	
//	private void importProperties(PropertyManager inh) {
//		doi = inh.doi;
//		fillColor = inh.fillColor;
//		font = inh.font;
//		fontColor = inh.fontColor;
//		fontSize = inh.fontSize;
//		fromAngle = inh.fromAngle;
//		hanchor = inh.hanchor;
//		height = inh.height;
//		hgap = inh.hgap;
//		hint = inh.hint;
//		id = inh.id;
//		innerRadius = inh.innerRadius;
//		lineColor = inh.lineColor;
//		lineWidth = inh.lineWidth;
//		shapeClosed = inh.shapeClosed;
//		shapeConnected = inh.shapeConnected;
//		shapeCurved = inh.shapeCurved;
//		textAngle = inh.textAngle;
//		toAngle = inh.toAngle;
//		vanchor = inh.vanchor;
//		vgap = inh.vgap;
//		width = inh.width;
//	}
	
//	private void setDefaults() {
//		doi = 1000000;
//		fillColor = 255;
//		font = "Helvetica";
//		fontColor = 0;
//		fontSize = 12;
//		fromAngle = 0;
//		hanchor = 0.5f;
//		height = 0;
//		hgap = 0;
//		hint = "";
//		id = "";
//		innerRadius = 0;
//		lineColor = 0;
//		lineWidth = 1;
//		shapeClosed = false;
//		shapeConnected = false;
//		shapeCurved = false;
//		textAngle = 0;
//		toAngle = 0;
//		vanchor = 0.5f;
//		vgap = 0;
//		width = 0;
//		mouseOverFigure = null;
//	}
}
