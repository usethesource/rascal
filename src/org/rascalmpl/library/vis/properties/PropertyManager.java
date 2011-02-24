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
			case ALIGN:
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
			
			case ALIGN_ANCHORS:
//				values[i++] = new ConstantBooleanProperty(Property.ALIGNASCHILD, true); break;
				values[i++] = Utils.getBooleanArg(Property.ALIGN_ANCHORS, c, fpa, ctx); break;
				
			case ALIGN:
				values[i++] = Utils.getRealArg(Property.HALIGN, c, 0, fpa, ctx);  // TODO check range?
				values[i++] = Utils.getRealArg(Property.VALIGN, c, 1, fpa, ctx);
				break;
				
			case ANCHOR:
				values[i++] = Utils.getRealArg(Property.HANCHOR, c, 0, fpa, ctx);	// TODO check range?
				values[i++] = Utils.getRealArg(Property.VANCHOR, c, 1, fpa, ctx);
				break;
				
			case DOI:
				values[i++] = Utils.getIntArg(Property.DOI, c, fpa, ctx); break;
			
			case FILL_COLOR:
				values[i++] = Utils.getColorArg(Property.FILL_COLOR, c, fpa, ctx); break;
				
			case FONT:
				values[i++] = Utils.getStrArg(Property.FONT, c, fpa, ctx); break;
				
			case FONT_COLOR:
				values[i++] = Utils.getColorArg(Property.FONT_COLOR, c, fpa, ctx); break;
				
			case FONT_SIZE:
				values[i++] = Utils.getIntArg(Property.FONT_SIZE, c, fpa, ctx); break;
				
			case FROM_ANGLE:
				values[i++] = Utils.getIntOrRealArg(Property.FROM_ANGLE, c, 0, fpa, ctx); break;
			
			case GAP:
				if(c.arity() == 1){
					values[i++] = Utils.getIntOrRealArg(Property.HGAP, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.VGAP, c, 0, fpa, ctx);
					
				} else {
					values[i++] = Utils.getIntOrRealArg(Property.HGAP, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.VGAP, c, 1, fpa, ctx);
				}
				break;
				
			case HALIGN:
				values[i++] = Utils.getRealArg(Property.HALIGN, c, 0, fpa, ctx);
				//hanchor = hanchor < 0 ? 0 : (hanchor > 1 ? 1 : hanchor);
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
				
			case LINE_COLOR:
				values[i++] = Utils.getColorArg(Property.LINE_COLOR, c, fpa, ctx); break;
				
			case LINE_WIDTH:
				values[i++] = Utils.getIntOrRealArg(Property.LINE_WIDTH, c, 0, fpa, ctx); break;

			case MOUSEOVER:
				mouseOverFigure = FigureFactory.make(fpa, 
													 (IConstructor)c.get(0), 
													 new DefaultPropertyManager(fpa),
													 ctx);
				break;	
				
			case ONCLICK:
				onClickHandler = c.get(0);
				break;
				
			case SHAPE_CLOSED:
				//values[i++] = new ConstantBooleanProperty(Property.SHAPECLOSED, true); break;
				values[i++] = Utils.getBooleanArg(Property.SHAPE_CLOSED, c, fpa, ctx); break;
				
			case SHAPE_CONNECTED:
				//values[i++] = new ConstantBooleanProperty(Property.SHAPECONNECTED, true); break;
				values[i++] = Utils.getBooleanArg(Property.SHAPE_CONNECTED, c, fpa, ctx); break;
				
			case SHAPE_CURVED:
				//values[i++] = new ConstantBooleanProperty(Property.SHAPECURVED, true); break;
				values[i++] = Utils.getBooleanArg(Property.SHAPE_CURVED, c, fpa, ctx); break;
			
			case SIZE:
				if(c.arity() == 1){
					values[i++] = Utils.getIntOrRealArg(Property.WIDTH, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.HEIGHT, c, 0, fpa, ctx);
				} else {
					values[i++] = Utils.getIntOrRealArg(Property.WIDTH, c, 0, fpa, ctx);
					values[i++] = Utils.getIntOrRealArg(Property.HEIGHT, c, 1, fpa, ctx);
				}
				break;
				
			case TEXT_ANGLE:
				values[i++] = Utils.getIntOrRealArg(Property.TEXT_ANGLE, c, 0, fpa, ctx); break;
				
			case TO_ANGLE:
				values[i++] = Utils.getIntOrRealArg(Property.TO_ANGLE, c, 0, fpa, ctx); break;
				
			case VALIGN:
				values[i++] = Utils.getRealArg(Property.VALIGN, c, 0, fpa, ctx);
				break;
				
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
	
	public boolean getAlignAnchors(){
		return getBooleanProperty(Property.ALIGN_ANCHORS);
	}
	
	public int getDOI(){
		return getIntegerProperty(Property.DOI);
	}

	public int getFillColor() {
		return getIntegerProperty(Property.FILL_COLOR);
	}

	public String getFont() {
		return getStringProperty(Property.FONT);
	}

	public int getFontColor() {
		return getIntegerProperty(Property.FONT_COLOR);
	}

	public int getFontSize() {
		return getIntegerProperty(Property.FONT_SIZE);
	}

	public float getFromAngle() {
		return getRealProperty(Property.FROM_ANGLE);
	}
	
	public float getHalign() {
		return getRealProperty(Property.HALIGN);
	}
	
	public float getHanchor() {
		return getRealProperty(Property.HANCHOR);
	}

	public float getHGap() {
		return getRealProperty(Property.HGAP);
	}

	public float getHeight() {
		return getRealProperty(Property.HEIGHT);
	}

	public String getHint() {
		return getStringProperty(Property.HINT);
	}

	public String getId() {
		return getStringProperty(Property.ID);
	}

	public float getInnerRadius() {
		return getRealProperty(Property.INNERRADIUS);
	}

	public int getLineColor() {
		return getIntegerProperty(Property.LINE_COLOR);
	}

	public float getLineWidth() {
		return getRealProperty(Property.LINE_WIDTH);
	}

	public Figure getMouseOver() {
		if(mouseOverFigure != null)
			return mouseOverFigure;
		return inherited.getMouseOver();
	}
	
	public IValue getOnClick(){
		if(onClickHandler != null)
			return onClickHandler;
		return inherited.getOnClick();
	}

	public float getTextAngle() {
		return getRealProperty(Property.TEXT_ANGLE);
	}

	public float getVGap() {
		return getRealProperty(Property.VGAP);
	}
	
	public float getValign() {
		return getRealProperty(Property.VALIGN);
	}

	public float getVanchor() {
		return getRealProperty(Property.VANCHOR);
	}

	public float getWidth() {
		return getRealProperty(Property.WIDTH);
	}

	public boolean isShapeClosed() {
		return getBooleanProperty(Property.SHAPE_CLOSED);
	}

	public boolean isShapeConnected() {
		return getBooleanProperty(Property.SHAPE_CONNECTED);
	}

	public boolean isShapeCurved() {
		return getBooleanProperty(Property.SHAPE_CURVED);
	}

	public float getToAngle() {
		return getRealProperty(Property.TO_ANGLE);
	}
	
	public boolean isDraggable(){
		return draggable;
	}
	
	public boolean getBooleanProperty(Property property) {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == property)
				return ((IBooleanPropertyValue) pv).getValue();
		}
		return inherited.getBooleanProperty(property);
	}

	public int getIntegerProperty(Property property) {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == property)
				return ((IIntegerPropertyValue) pv).getValue();
		}
		return inherited.getIntegerProperty(property);
	}
	
	public float getRealProperty(Property property) {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == property)
				return ((IRealPropertyValue) pv).getValue();
		}
		return inherited.getRealProperty(property);
	}
	
	public String getStringProperty(Property property) {
		for(IPropertyValue pv : values){
			if(pv.getProperty() == property)
				return ((IStringPropertyValue) pv).getValue();
		}
		return inherited.getStringProperty(property);
	}
}
