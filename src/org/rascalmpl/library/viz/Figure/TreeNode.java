package org.rascalmpl.library.viz.Figure;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

/**
 * A TreeNode is created for each "node" constructor that occurs in the tree.
 * 
 * @author paulk
 *
 */
public class TreeNode extends Figure {
	
	Figure figure;
	Tree tree;
	private ArrayList<TreeNode> children;
	private float leftPosition;
	private float rightPosition;
	private ArrayList<PropertyManager> edgeProperties;
	private static boolean debug = true;
	private boolean visible = true;
	
	public TreeNode(FigurePApplet fapplet, Tree tree, PropertyManager inheritedProps,
			IList props, Figure fig, IEvaluatorContext ctx) {
		super(fapplet, inheritedProps, props, ctx);
		this.tree = tree;
		figure = fig;
		children = new ArrayList<TreeNode>();
		edgeProperties = new ArrayList<PropertyManager>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
		edgeProperties.add(new PropertyManager(null, inheritedProps, props, ctx));
	}
	
	/**
	 * shapeTree places the current subtree on the raster
	 * 
	 * @param rootMidX	x coordinate of centre of the root figure
	 * @param rootTop	y coordinate of top of root figure
	 * @param raster	NodeRaster to be used
	 * @return the x position of the root
	 */
	float shapeTree(float rootMidX, float rootTop, TreeNodeRaster raster) {

		System.err.printf("shapeTree: rootMidX=%f, rootTop=%f\n", rootMidX, rootTop);
		figure.bbox();
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		float position = rootMidX; // x position of center of figure of this TreeNode!
		position = raster.leftMostPosition(position, rootTop, figure.width, figure.height, hgap);
		
		height = figure.height;
		width = figure.width;
		
		int nChildren = children.size();

		if(nChildren > 0){
			float widthDirectChildren = 0;
			
			float widthChildren = 0;
			float heightChildren = 0;
			
			for(TreeNode child : children){
				child.figure.bbox();
			}
			
			if(nChildren > 1){
				widthDirectChildren = (children.get(0).figure.width + children.get(nChildren-1).figure.width)/2 +
				        (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					widthDirectChildren += children.get(i).figure.width;
				}
			} else {
				widthDirectChildren = 0;
			}
			float branchPosition = position - widthDirectChildren/2;
			
			// Place leftmost child
			leftPosition = children.get(0).shapeTree(branchPosition, rootTop + figure.height + vgap, raster);
			
			System.err.printf("shapeTree(%f, %f) => branchPosition=%f, leftPosition=%f\n", rootMidX, rootTop, branchPosition, leftPosition);
			rightPosition = leftPosition;
			
			widthChildren = children.get(0).width;
			heightChildren = children.get(0).height;
			for(int i = 1; i < nChildren; i++){
				branchPosition += hgap + (children.get(i-1).figure.width + children.get(i).figure.width)/2;
				rightPosition = children.get(i).shapeTree(branchPosition, rootTop + figure.height + vgap, raster);
				widthChildren += hgap + children.get(i).width;
				heightChildren = max(heightChildren, children.get(i).height);
			}
			height += vgap + heightChildren;
			width = max(figure.width, widthChildren);
			position = (leftPosition + rightPosition)/2;
		}
	
		raster.add(position, rootTop, figure.width, figure.height);
		this.left = position - width/2;
		this.top = rootTop;
		System.err.printf("shapeTree(%f, %f) => position=%f, width=%f, height=%f\n", rootMidX, rootTop, position, width, height);
		return position;
	}
	
	@Override
	void bbox() {
		// TODO Auto-generated method stub
	}
	
	@Override
	void draw(float left, float top){
		this.left = left;
		this.top = top;
		left += leftDragged;
		top += topDragged;
		boolean squareStyle = true;
		
		applyProperties();
		float figLeft = left + width/2 - figure.width/2;
		figure.draw(figLeft, top);
		
		int n = children.size();
		
		if(n > 0 && visible){
			float figBottomX = left + width/2;;
			float figBottomY = top + figure.height;
			float hgap = getHGapProperty();
			float vgap = getVGapProperty();
			final float childTop = figBottomY + vgap;
			float horLineY = figBottomY + vgap/2;
		
			if(squareStyle){
				
				vlp.line(figBottomX, figBottomY, figBottomX, horLineY);
				
//				if(n > 1){
//					Figure leftFig = children.get(0).figure;
//					Figure rightFig = children.get(n-1).figure;
//					vlp.line(leftFig.getCurrentLeft() + leftFig.width/2, horLineY, rightFig.getCurrentLeft() + rightFig.width/2, horLineY);
//				}
				
			
			// TODO line style!
		
				float position = left + leftPosition;
				for(TreeNode child : children){
					if(!squareStyle)
						vlp.line(figBottomX, figBottomY, child.figure.left + child.figure.width/2, childTop);
					float midChild = child.figure.getCurrentLeft() + child.figure.width/2;
					
					vlp.line(midChild, child.figure.getCurrentTop(), midChild, horLineY);
					child.draw(position-child.width/2, childTop);
				    position += child.width + hgap;
				}
				
				if(n > 1)
					vlp.line(children.get(0).getCurrentMiddle(), horLineY, children.get(n-1).getCurrentMiddle(), horLineY);
			}

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
		vlp.stroke(255, 0,0);
		vlp.noFill();
		vlp.rect(left + leftDragged, top + topDragged, width, height);
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		if(debug)System.err.printf("TreeNode.mouseover: %d, %d\n", mousex, mousey);
		if(debug)System.err.printf("TreeNode.mouseover: left=%f, top=%f\n", left, top);
		if(figure.mouseOver(mousex, mousey))
			return true;
		for(TreeNode child : children)
			if(child.mouseOver(mousex, mousey))
				return true;
		return false;
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		for(TreeNode child : children)
			if(child.mousePressed(mousex, mousey))
				return true;
		if(mouseInside(mousex, mousey)){
			vlp.registerFocus(this);
			if(vlp.mouseButton == vlp.RIGHT)
				visible = false;
			else
				visible = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseDragged(int mousex, int mousey){
		if(debug)System.err.printf("TreeNode.mouseDragged: %d, %d\n", mousex, mousey);
		for(TreeNode child : children)
			if(child.mouseDragged(mousex, mousey))
				return true;
		if(debug)System.err.println("TreeNode.mouseDragged: children do not match\n");
		if(mouseInside(mousex, mousey)){
			vlp.registerFocus(this);
			drag(mousex, mousey);
			return true;
		}
		return false;
	}
}
