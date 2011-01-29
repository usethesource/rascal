package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * Outline element: a rectangle with colored horizontal lines
 * 
 * @author paulk
 *
 */
public class Outline extends Figure {

	private boolean debug = false;
	private IMap coloredLines;
	private float topAnchor = 0;
	private float bottomAnchor = 0;

	public Outline(FigurePApplet fpa, IPropertyManager properties, IMap coloredLines, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.coloredLines = coloredLines;
	}

	@Override
	public
	void bbox(){
		float lw = getLineWidthProperty();
		width = getWidthProperty();
		height = getHeightProperty();
		width += 2*lw;
		height += 2*lw;
		if(debug) System.err.println("Outline.bbox => " + width + ", " + height);
	}
	
	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		
		float lw = getLineWidthProperty();
		applyProperties();
		if(debug) System.err.println("Outline.draw => " + width + ", " + height);
		if(height > 0 && width > 0){
			fpa.rect(left, top, width, height);
			for(IValue key : coloredLines){
				int lino = ((IInteger) key).intValue();
				int lineCol = ((IInteger) coloredLines.get(key)).intValue();
				fpa.stroke(lineCol);
				fpa.line(left + lw, top+lino, left + width - lw, top+lino);
			}
		}
	}
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}
}
