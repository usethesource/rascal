
package org.rascalmpl.library.vis;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PConstants;

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
	
	public TreeNode(FigurePApplet fpa, Tree tree, PropertyManager inheritedProps,
			IList props, Figure fig, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);
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
	 * @param rootMidX	x coordinate of center of the root figure
	 * @param rootTop	y coordinate of top of root figure
	 * @param raster	NodeRaster to be used
	 * @return the x position of the center of the root
	 */
	float shapeTree(float rootMidX, float rootTop, TreeNodeRaster raster) {
        String id = figure.getIdProperty();
		System.err.printf("shapeTree: id=%s, rootMidX=%f, rootTop=%f\n", id, rootMidX, rootTop);
		figure.bbox();
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		
		// Initial placement of figure of this TreeNode
		float position = rootMidX; 
		position = raster.leftMostPosition(position, rootTop, figure.width, figure.height, hgap);
		
		height = figure.height;
		width = figure.width;
		
		int nChildren = children.size();

		if(nChildren > 0){
			for(TreeNode child : children){
				child.figure.bbox();
			}
			
			float branchPosition = position;
			
			if(nChildren > 1){
				float widthDirectChildren = (children.get(0).figure.width + children.get(nChildren-1).figure.width)/2 +
				        (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					widthDirectChildren += children.get(i).figure.width;
				}
				branchPosition = position - widthDirectChildren/2; // Position of leftmost child
			}
			
			float childTop = rootTop + figure.height + vgap;         // Top of all children
			
			// Place leftmost child
			leftPosition = children.get(0).shapeTree(branchPosition, childTop, raster);
			
			System.err.printf("shapeTree(%s, %f, %f) => branchPosition=%f, leftPosition=%f\n", id, rootMidX, rootTop, branchPosition, leftPosition);
			rightPosition = leftPosition;
			
			float heightChildren = children.get(0).height;
			for(int i = 1; i < nChildren; i++){
				branchPosition += hgap + (children.get(i-1).figure.width + children.get(i).figure.width)/2;
				rightPosition = children.get(i).shapeTree(branchPosition, childTop, raster);
				heightChildren = max(heightChildren, children.get(i).height);
			}
			height += vgap + heightChildren;
			width = rightPosition - leftPosition + children.get(0).width/2 + children.get(nChildren-1).width/2;
			width = max(figure.width, width);
			position = (leftPosition + rightPosition)/2;
		} else {
			leftPosition = rightPosition = width/2;
		}
	
		// After placing all children, we can finally add the current root figure to the raster.
		raster.add(position, rootTop, figure.width, figure.height);
		this.left = position - width/2;
		this.top = rootTop;
		System.err.printf("shapeTree(%s, %f, %f) => position=%f, left=%f, top=%f, width=%f, height=%f\n", id, rootMidX, rootTop, position, left, top, width, height);
		return position;
	}
	
	@Override
	void bbox() {
		// TODO Auto-generated method stub
	}
	
	@Override
	void draw(float left, float top){
		String id = figure.getIdProperty();
		boolean squareStyle = true;
		int nChildren = children.size();
		
		applyProperties();
		
		if(nChildren == 0){
			figure.draw(left + this.left, top + this.top);
			return;
		}
	
		float absMiddleXofFig = left + leftPosition + leftDragged + (rightPosition - leftPosition)/2;
		float absLeftRootFig = absMiddleXofFig - figure.width/2;
		System.err.printf("draw figure %s at %f, %f", id, absLeftRootFig, top + this.top);
		System.err.printf(", left = %f, this.left=%f, leftPosition=%f, rightPosition=%f\n", left, this.left, leftPosition, rightPosition);
//		figure.draw(figMiddleX - figure.width/2, this.top);
		figure.draw(absLeftRootFig, top + this.top + topDragged);
		
		if(nChildren > 0 && visible){
			float absFigBottomY = top + this.top + topDragged + figure.height;
			float vgap = getVGapProperty();
			final float absChildTop = absFigBottomY + vgap;
			float absHorLineY = absFigBottomY + vgap/2;
		
			if(squareStyle){
				// Vertical line from bottom of current figure to horizontal line
				System.err.printf("%s: absFigMiddleX=%f, absFigBottomY=%f\n", id, absMiddleXofFig, absFigBottomY);
				fpa.line(absMiddleXofFig, absFigBottomY, absMiddleXofFig, absHorLineY);
			
			// TODO line style!
		
				for(TreeNode child : children){
					if(!squareStyle)
						fpa.line(absMiddleXofFig, absFigBottomY, child.figure.left + child.figure.width/2, absChildTop);
					float absMidChild = left + leftDragged + child.getRealMiddle();
					
					System.err.printf("%s: absMidChild=%f, childCurrentTop=%f\n", id, absMidChild,  child.getRealTop());
					
					// Vertical line from horizontal line to top of this child
					fpa.line(absMidChild, top + topDragged + child.getRealTop(), absMidChild, absHorLineY);
					child.draw(left + leftDragged, top + topDragged);
				}
				
				if(nChildren> 1)
					fpa.line(left + children.get(0).getRealMiddle(), absHorLineY, left + children.get(nChildren-1).getRealMiddle(), absHorLineY);
			}

		}
	}
	
//	void xxxdraw(float left, float top){
//		String id = figure.getIdProperty();
//		
//		System.err.printf("draw(%s, %f,%f)\n", id, this.left, this.top);
//	//	this.left = left;
//	//	this.top = top;
//	//	left += leftDragged;
//	//	top += topDragged;
//		boolean squareStyle = true;
//		
//		applyProperties();
//		float figMiddleX = left + this.left + leftPosition + (rightPosition - leftPosition)/2;
//		System.err.printf("draw figure %s at %f, %f", id, figMiddleX - figure.width/2, top);
//		System.err.printf(", left = %f, leftPosition=%f, rightPosition=%f\n", left, leftPosition, rightPosition);
////		figure.draw(figMiddleX - figure.width/2, this.top);
//		figure.draw(this.left + left, this.top + top);
//		
//		int nChildren = children.size();
//		
//		if(nChildren > 0 && visible){
//			float figBottomY = this.top + figure.height;
//			float vgap = getVGapProperty();
//			final float childTop = figBottomY + vgap;
//			float horLineY = figBottomY + vgap/2;
//		
//			if(squareStyle){
//				System.err.printf("%s: figMiddleX=%f, figBottomY=%f\n", id, figMiddleX, figBottomY);
//				vlp.line(figMiddleX, figBottomY, figMiddleX, horLineY);
//			
//			// TODO line style!
//		
//				for(TreeNode child : children){
//					if(!squareStyle)
//						vlp.line(figMiddleX, figBottomY, child.figure.left + child.figure.width/2, childTop);
//					float midChild = this.left + child.getRealMiddle();
//					
//					System.err.printf("%s: midChild=%f, childCurrentTop=%f\n", id, midChild,  child.getRealTop());
//					
//					vlp.line(midChild, child.getRealTop(), midChild, horLineY);
//					child.draw(left, top);
//				
//				}
//				
//				if(nChildren> 1)
//					vlp.line(this.left + children.get(0).getRealMiddle(), horLineY, this.left + children.get(nChildren-1).getRealMiddle(), horLineY);
//			}
//
//		}
//	}
	
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
		if(debug)System.err.printf("TreeNode.mouseDragged: %d, %d\n", mousex, mousey);
		for(TreeNode child : children)
			if(child.mouseDragged(mousex, mousey))
				return true;
		if(debug)System.err.println("TreeNode.mouseDragged: children do not match\n");
		if(mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			drag(mousex, mousey);
			return true;
		}
		return false;
	}
}
