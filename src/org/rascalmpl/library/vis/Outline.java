package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

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

	public Outline(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IMap coloredLines, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);
		this.coloredLines = coloredLines;
	}

	@Override
	void bbox(){
		int lw = getLineWidthProperty();
		width = getWidthProperty();
		height = getHeightProperty();
		width += 2*lw;
		height += 2*lw;
		if(debug) System.err.println("Outline.bbox => " + width + ", " + height);
	}
	
	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		
		left += leftDragged;
		top += topDragged;
		int lw = getLineWidthProperty();
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
