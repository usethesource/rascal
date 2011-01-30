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

	protected Figure mouseOverFigure = null; // Interaction and mouse handling
	protected IValue onClickHandler = null;
	
	IPropertyValue values[];
	IPropertyManager inherited;

//	private boolean usesTrigger;
	private boolean draggable;
	
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
			case ONCLICK:
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
		
		draggable = false;
		
		for (IValue v : props) {
			
			IConstructor c = (IConstructor) v;
			String pname = c.getName();

			switch (propertyNames.get(pname)) {
			
			case ANCHOR:
				values[i++] = Utils.getRealArg(Property.HANCHOR, c, 0, fpa, ctx);
				//hanchor = hanchor < 0 ? 0 : (hanchor > 1 ? 1 : hanchor);
				
				values[i++] = Utils.getRealArg(Property.VANCHOR, c, 1, fpa, ctx);
				//vanchor = vanchor < 0 ? 0 : (vanchor > 1 ? 1 : vanchor);
				
				if(values[i-1].usesTrigger() || values[i-1].usesTrigger())
					draggable = true;
				break;
				
			case DOI:
				values[i++] = Utils.getIntArg(Property.DOI, c, fpa, ctx); break;
			
			case FILLCOLOR:
				values[i++] = Utils.getColorArg(Property.FILLCOLOR, c, fpa, ctx); break;
				
			case FONT:
				values[i++] = Utils.getStrArg(Property.FONT, c, fpa, ctx); break;
				
			case FONTCOLOR:
				values[i++] = Utils.getColorArg(Property.FONTCOLOR, c, fpa, ctx); break;
				
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
				if(values[i-1].usesTrigger())
					draggable = true;
				break;
				
			case HEIGHT:
				values[i++] = Utils.getIntOrRealArg(Property.HEIGHT, c, 0, fpa, ctx); break;
				
			case HGAP:
				values[i++] = Utils.getIntOrRealArg(Property.HGAP, c, 0, fpa, ctx); break;
				
			case HINT:
				values[i++] = Utils.getStrArg(Property.HINT, c.get(0), fpa, ctx); break;
				//hint = getStrArg(null, c).toLowerCase(); break;
				
			case ID:
				values[i++] = Utils.getStrArg(Property.ID, c.get(0), fpa, ctx); break;
				
			case INNERRADIUS:
				values[i++] = Utils.getIntOrRealArg(Property.INNERRADIUS, c, 0, fpa, ctx); break;
				
			case LINECOLOR:
				values[i++] = Utils.getColorArg(Property.LINECOLOR, c, fpa, ctx); break;
				
			case LINEWIDTH:
				values[i++] = Utils.getIntOrRealArg(Property.LINEWIDTH, c, 0, fpa, ctx); break;

			case MOUSEOVER:
				mouseOverFigure = FigureFactory.make(fpa, 
													 (IConstructor)c.get(0), 
													 new DefaultPropertyManager(fpa),
													 ctx);
				break;	
				
			case ONCLICK:
				onClickHandler = c.get(0);
				System.err.println("onClickHandler = " + onClickHandler);
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
				if(values[i-1].usesTrigger())
					draggable = true;
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
		
//		for(IPropertyValue v : values){
//			if(v.usesTrigger()){
//				this.usesTrigger = true;
//				break;
//			}
//		}
			
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
		return mouseOverFigure;
	}
	
	public IValue getOnClick(){
		return onClickHandler;
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
	
//	public boolean usesTrigger(){
//		return usesTrigger;
//	}
	
	public boolean isDraggable(){
		return draggable;
	}
}
