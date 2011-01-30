package org.rascalmpl.library.vis.properties;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;

@SuppressWarnings("serial")
public interface IPropertyManager {
	
	static final HashMap<String, Property> propertyNames = new HashMap<String, Property>() {
		{
			put("anchor",			Property.ANCHOR);
			put("doi",       		Property.DOI);
			put("fillColor", 		Property.FILLCOLOR);
			put("font", 			Property.FONT);
			put("fontColor", 		Property.FONTCOLOR);
			put("fontSize", 		Property.FONTSIZE);
			put("fromAngle", 		Property.FROMANGLE);
			put("gap", 				Property.GAP);
			put("hanchor", 			Property.HANCHOR);
			put("height", 			Property.HEIGHT);
			put("hgap",				Property.HGAP);                  // Only used internally
			put("hint", 			Property.HINT);
			put("id", 				Property.ID);
			put("innerRadius", 		Property.INNERRADIUS);
			put("lineColor", 		Property.LINECOLOR);
			put("lineWidth", 		Property.LINEWIDTH);
			put("mouseOver", 		Property.MOUSEOVER);
			put("onClick",			Property.ONCLICK);
			put("shapeClosed", 		Property.SHAPECLOSED);
			put("shapeConnected", 	Property.SHAPECONNECTED);
			put("shapeCurved", 		Property.SHAPECURVED);
			put("size", 			Property.SIZE);
			put("textAngle", 		Property.TEXTANGLE);
			put("toAngle", 			Property.TOANGLE);
			put("vanchor", 			Property.VANCHOR);
			put("vgap", 			Property.VGAP);                 // Only used internally
			put("width", 			Property.WIDTH);
		}
	};
	
	public FigurePApplet getFPA();
	
	public int getDOI();
	public int getFillColor();
	public String getFont();
	public int getFontColor();
	public int getFontSize();
	public float getFromAngle();
	public float getHanchor();
	public float getHGap();
	public float getHeight();
	public String getHint();
	public String getId();
	public float getInnerRadius();
	public int getLineColor();
	public float getLineWidth();
	public Figure getMouseOver();
	public IValue getOnClick();
	public boolean isShapeClosed();
	public boolean isShapeConnected();
	public boolean isShapeCurved();
	public float getTextAngle();
	public float getToAngle();
	public float getVanchor();
	public float getVGap();
	public float getWidth();
	
	public boolean isDraggable();
}
