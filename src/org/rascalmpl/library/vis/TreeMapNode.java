
package org.rascalmpl.library.vis;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PConstants;

/**
 * A TreeMapNode is created for each "node" constructor that occurs in the tree.
 * 
 * @author paulk
 *
 */
public class TreeMapNode extends Figure {
	
	Figure figure;
	TreeMap treemap;
	private ArrayList<TreeMapNode> children;
	private boolean visible;
	private static boolean debug = true;
	
	public TreeMapNode(FigurePApplet fpa, TreeMap treeMap, PropertyManager inheritedProps,
			IList props, Figure fig, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);
		this.treemap = treeMap;
		figure = fig;
		children = new ArrayList<TreeMapNode>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeMapNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
	}
	
	public void place(float left, float top, float width, float height, boolean hor) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		
		float hgap = getHGapProperty();
		float vgap = getHGapProperty();
		
		String id = figure.getIdProperty();
		System.err.printf("%s: %f,%f,%f,%f,%s\n", id, left,top,width,height, hor? "hor":"vert");
		
		int n = children.size();
		float ratio[] = new float[n];
		float chsurf = 0;
		float awidth = width - (n+1) * hgap;
		float aheight = height - (n+1) * vgap;
		for(int i = 0; i < n; i++){
			TreeMapNode child = children.get(i);
			child.bbox();
			chsurf += child.width * child.height;
		}
		for(int i = 0; i < n; i++){
			TreeMapNode child = children.get(i);
			ratio[i] = (child.width * child.height) /chsurf;
			System.err.printf("%s: ratio = %f\n", child.figure.getIdProperty(), ratio[i]);
		}
		if(hor){
			float x = left + hgap;
			for(int i = 0; i < n; i++){	
				TreeMapNode child = children.get(i);
				float dw = ratio[i] * awidth;
				child.place(x, top + vgap, dw, height - 2* vgap, !hor);
				x += dw + hgap;
			}
		} else {
			float y = top + vgap;
			for(int i = 0; i < n; i++){	
				TreeMapNode child = children.get(i);
				float dh =  ratio[i] * aheight;
				child.place(left + hgap, y, width - 2 * hgap, dh, !hor);
				y += dh + vgap;
			}
		}
       return;
	}
	
	@Override
	void bbox() {
		figure.bbox();
		width = figure.width;
		height = figure.height;
	}
	
	@Override
	void draw(float left, float top){
		System.err.printf("draw: %s at %f, %f\n", figure.getIdProperty(), left + this.left + leftDragged,  top + this.top + topDragged);
		figure.applyProperties();
		fpa.rect(left + this.left + leftDragged, top + this.top + topDragged, width, height);
		
		for(TreeMapNode child : children){
			child.draw(left, top);
		}
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey){
		float l = left + leftDragged;
		float t = top + topDragged;
	
		return mousex > l && mousex < l + width &&
				mousey > t && mousey < t + height;
		
	}
	
	@Override
	public void drawFocus(){
		fpa.stroke(255, 0,0);
		fpa.noFill();
		fpa.rect(left + leftDragged, top + topDragged, width, height);
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		if(debug)System.err.printf("TreeMapNode.mouseover: %d, %d\n", mousex, mousey);
		if(debug)System.err.printf("TreeMapNode.mouseover: left=%f, top=%f\n", left, top);
		if(figure.mouseOver(mousex, mousey))
			return true;
		for(TreeMapNode child : children)
			if(child.mouseOver(mousex, mousey))
				return true;
		return false;
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		for(TreeMapNode child : children)
			if(child.mousePressed(mousex, mousey))
				return true;
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			if(fpa.mouseButton == PConstants.RIGHT)
				visible = false;
			else
				visible = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseDragged(int mousex, int mousey){
		if(debug)System.err.printf("TreeMapNode.mouseDragged: %d, %d\n", mousex, mousey);
		for(TreeMapNode child : children)
			if(child.mouseDragged(mousex, mousey))
				return true;
		if(debug)System.err.println("TreeMapNode.mouseDragged: children do not match\n");
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			drag(mousex, mousey);
			return true;
		}
		return false;
	}
}
