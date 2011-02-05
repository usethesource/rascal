
package org.rascalmpl.library.vis.tree;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

/**
 * A TreeMapNode is created for each "node" constructor that occurs in the TreeMap.
 * 
 * @author paulk
 *
 */
public class TreeMapNode extends Figure {
	
	Figure rootFigure;
	TreeMap treemap;
	private ArrayList<TreeMapNode> children;
	private float[] childLeft;
	private float[] childTop;
	private static boolean debug = true;
	
	public TreeMapNode(FigurePApplet fpa, TreeMap treeMap, IPropertyManager properties,
			Figure fig, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.treemap = treeMap;
		rootFigure = fig;
		children = new ArrayList<TreeMapNode>();
	}
	
	public void addChild(IPropertyManager inheritedProps, IList props,
			TreeMapNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
	}
	
	public void place(float width, float height, boolean hor) {
		this.width = width;
		this.height = height;
		
		float hgap = getHGapProperty();
		float vgap = getHGapProperty();
		
		String id = rootFigure.getIdProperty();
		if(debug)System.err.printf("%s: %f,%f,%s\n", id, width,height, hor? "hor":"vert");
		
		int n = children.size();
		
		childLeft = new float[n];
		childTop = new float[n];
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
			ratio[i] = (child.width * child.height) / chsurf;
			if(debug)System.err.printf("%s: ratio = %f\n", child.rootFigure.getIdProperty(), ratio[i]);
		}
		if(hor){
			float x = hgap;
			for(int i = 0; i < n; i++){	
				TreeMapNode child = children.get(i);
				float dw = ratio[i] * awidth;
				child.place(dw, height - 2* vgap, !hor);
				childLeft[i] = x;
				childTop[i] = vgap;
				x += dw + hgap;
			}
		} else {
			float y = vgap;
			for(int i = 0; i < n; i++){	
				TreeMapNode child = children.get(i);
				float dh =  ratio[i] * aheight;
				child.place(width - 2 * hgap, dh, !hor);
				childLeft[i] = hgap;
				childTop[i] = y;
				y += dh + vgap;
			}
		}
       return;
	}
	
	@Override
	public
	void bbox() {
		rootFigure.bbox();
		width = rootFigure.width;
		height = rootFigure.height;
	}
	
	@Override
	public
	void draw(float left, float top){
		this.setLeft(left);
		this.setTop(top);
		if(debug)System.err.printf("draw: %s at %f, %f (%s)\n", 
				          rootFigure.getIdProperty(), left,  top,
				          isVisible() ? "visible" : "invisible");
		if(!isVisible())
			return;
		
		rootFigure.applyProperties();
		fpa.rect(left, top, width, height);
		
		if(isNextVisible()){
			fpa.incDepth();
			int n = children.size();
			for(int i = 0; i < n; i++){
				TreeMapNode child = children.get(i);
				child.draw(left + childLeft[i], top + childTop[i]);
			}
			fpa.decDepth();
		}
	}
	
	@Override
	public void drawFocus(){
		if(debug)System.err.printf("TreeMapNode.drawFocus: %s, %f, %f\n", rootFigure.getIdProperty(), getLeft(), getTop());
		fpa.stroke(255, 0,0);
		fpa.noFill();
		fpa.rect(getLeft(), getTop(), width, height);
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent){
		if(debug)System.err.printf("TreeMapNode.mouseover: %s, %d, %d\n", rootFigure.getIdProperty(), mousex, mousey);
		if(debug)System.err.printf("TreeMapNode.mouseover: left=%f, top=%f\n", getLeft(), getTop());
		if(!isVisible())
			return false;
		if(rootFigure.mouseOver(mousex, mousey, centerX, centerY, false))
			return true;
		if(isNextVisible()){
			for(TreeMapNode child : children)
				if(child.mouseOver(mousex, mousey, centerX, centerY, mouseInParent))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e){
		if(debug)System.err.printf("TreeMapNode.mousePressed: %s, %d, %d\n", rootFigure.getIdProperty(), mousex, mousey);
		if(!isVisible())
			return false;
		if(isNextVisible()){
			for(TreeMapNode child : children)
				if(child.mousePressed(mousex, mousey, e))
					return true;
		}
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			return true;
		}
		return false;
	}
	
//	@Override
//	public boolean mouseDragged(int mousex, int mousey){
//		if(debug)System.err.printf("TreeMapNode.mouseDragged: %d, %d\n", mousex, mousey);
//		for(TreeMapNode child : children)
//			if(child.mouseDragged(mousex, mousey))
//				return true;
//		if(debug)System.err.println("TreeMapNode.mouseDragged: children do not match\n");
//		if(mouseInside(mousex, mousey)){
//			fpa.registerFocus(this);
//			drag(mousex, mousey);
//			return true;
//		}
//		return false;
//	}
}
