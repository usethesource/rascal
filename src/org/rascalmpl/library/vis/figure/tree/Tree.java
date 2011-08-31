/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Atze J. van der Ploeg - atze.van.der.ploeg@cwi.nl - CWI
*******************************************************************************/

package org.rascalmpl.library.vis.figure.tree;

import static org.rascalmpl.library.vis.properties.Properties.MANHATTAN_LINES;
import static org.rascalmpl.library.vis.properties.TwoDProperties.GAP;

import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.compose.Compose;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public class Tree extends Compose {

	Figure root;
	Dimension major;
	Dimension minor;
	double rootMinor;
	double childrenMajor;
	double[] childrenMinor;
	Outline leftOutline, rightOutline;
	static boolean isRoot = false;
	
	public Tree(Dimension major,Figure[] figures, PropertyManager properties) {
		super(figures, properties);
		this.root = figures[0];
		childrenMinor = new double[children.length-1];
		this.major = major;
		this.minor = major.other();
		leftOutline = new Outline(true);
		rightOutline = new Outline(false);
		
	}
	
	@Override
	public void computeMinSize() {
		setMajorDimension();
		double minorOffset = 0;
		childrenMajor = root.minSize.get(major) + prop.get2DReal(major, GAP);
		leftOutline.clear();
		rightOutline.clear();
		double rootMiddle = 0;
		int nrChildren = children.length-1;
		boolean nrChildrenEven = nrChildren% 2 == 0;
		for(int i = 1 ; i < children.length ; i++){
			Outline leftChildOutline = getLeftOutline(children[i]);
			Outline rightChildOutline = getRightOutline(children[i]);
			if(i == 1){
				childrenMinor[i-1] = 0;
			} else {
				leftChildOutline.move(0,-children[i].minSize.get(minor));
				childrenMinor[i-1] = getSeperation(rightOutline,leftChildOutline)  - children[i].minSize.get(minor) ;
				minorOffset = Math.max(minorOffset, -childrenMinor[i-1]);
				leftChildOutline.move(0, childrenMinor[i-1] + children[i].minSize.get(minor));
				rightChildOutline.move(0,childrenMinor[i-1]);
			}
			rootMiddle = setRootMiddle(rootMiddle, nrChildren, nrChildrenEven,
					i, leftChildOutline, rightChildOutline);
			leftOutline = leftOutline.merge(leftChildOutline);
			rightOutline = rightOutline.merge(rightChildOutline);
		}
		rootMinor = rootMiddle - root.minSize.get(minor)/2.0;
		minorOffset = Math.max(minorOffset,-rootMinor);
		moveMinor(minorOffset);
		leftOutline = leftOutline.merge(getRootLeftOutline(root, rootMinor));
		rightOutline = rightOutline.merge(getRootRightOutline(root, rootMinor));
		setMinSize();
	}

	private void setMajorDimension() {
		if(prop.getBool(Properties.MAJOR_X)){
			major = Dimension.X;
		} else {
			major = Dimension.Y;
		}
		this.minor = major.other();
	}

	private void moveMinor(double minorOffset) {
		rootMinor+=minorOffset;
		for(int i = 0 ; i < children.length-1 ; i++){
			childrenMinor[i]+=minorOffset;
		}
		leftOutline.move(childrenMajor,minorOffset);
		rightOutline.move(childrenMajor,minorOffset);
	}

	private double setRootMiddle(double rootMiddle, int nrChildren,
			boolean nrChildrenEven, int i, Outline leftChildOutline,
			Outline rightChildOutline) {
		if((nrChildrenEven && (i*2 == nrChildren || (i-1)*2 == nrChildren))
			|| (!nrChildrenEven && i*2-1 == nrChildren)){
			double l = leftChildOutline.getMinor(0);
			double r = rightChildOutline.getMinor(0);
			double newRootMiddle = ((r - prop.get2DReal(minor, GAP)) - l)/2.0 + l;
			if(nrChildrenEven && ((i-1)*2 == nrChildren)){
				newRootMiddle = (newRootMiddle - rootMiddle)/2.0 + rootMiddle;
			} 
			rootMiddle = newRootMiddle;
		}
		return rootMiddle;
	}

	private void setMinSize() {
		double minSizeMajor = 0;
		double minSizeMinor = rootMinor + root.minSize.get(minor);
		for(int i = 1 ; i < children.length ; i++){
			minSizeMajor = Math.max(minSizeMajor, children[i].minSize.get(major));
			minSizeMinor = Math.max(minSizeMinor, childrenMinor[i-1] +  children[i].minSize.get(minor));
		}
		minSize.set(major,minSizeMajor + childrenMajor);
		minSize.set(minor,minSizeMinor);
	}

	@Override
	public void resizeElement(Rectangle view) {
		root.location.set(minor,rootMinor );
		root.location.set(major,0);
		root.size.set(root.minSize);
		for(int i = 1 ; i < children.length ; i++){
			children[i].location.set(major,childrenMajor);
			children[i].location.set(minor,childrenMinor[i-1]);
		}
		for(Figure fig : children){
			fig.size.set(fig.minSize);
		}
	}
	
	public void drawLine(GraphicsContext gc,Coordinate from,Coordinate to){
		gc.line(from.getX(), from.getY(), to.getX() ,to.getY());
	}
	
	Coordinate getChildCenter(int i){
		if(children[i] instanceof Tree){
			Tree subTree = (Tree)children[i];
			return new Coordinate(major,children[i].location.get(major),
					subTree.root.location.get(minor) +  subTree.root.minSize.get(minor)/2.0);
			
		} else {
			return new Coordinate(major,children[i].location.get(major),
					children[i].location.get(minor) + children[i].size.get(minor)/2.0);
		}
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		if(children.length == 1) return;
		double hg = prop.get2DReal(major, GAP)/2.0;
		Coordinate fromRoot = getChildCenter(0);
		fromRoot.add(major,root.size.get(major));
		
		if(prop.getBool(MANHATTAN_LINES)){
			Coordinate toCenter = new Coordinate(fromRoot);
			toCenter.add(major,hg);
			drawLine(gc,fromRoot,toCenter);
			Coordinate from = getChildCenter(1);
			Coordinate to = getChildCenter(children.length-1);
			from.add(major,-hg);
			to.add(major,-hg);
			drawLine(gc,from,to);
		}
		for(int i = 1; i < children.length ; i++){
			Coordinate child = getChildCenter(i);
			Coordinate from;
			if(prop.getBool(MANHATTAN_LINES)){
				from = new Coordinate(child);
				from.add(major,-hg);
			} else {
				from = fromRoot;
			}
			drawLine(gc,from,child);
		}
	}
	
	Outline getRootLeftOutline(Figure fig, double rootMinor){
		Outline result = new Outline(true);
		double hg = prop.get2DReal(major, GAP);
		result.add(0, rootMinor);
		result.add(fig.minSize.get(major)+hg, Double.POSITIVE_INFINITY);
		return result;
	}
	
	Outline getRootRightOutline(Figure fig, double rootMinor){
		Outline result = new Outline(false);
		double hg = prop.get2DReal(major, GAP);
		double vg = prop.get2DReal(minor, GAP);
		result.add(0, rootMinor + fig.minSize.get(minor) + vg);
		result.add(fig.minSize.get(major) + hg, Double.NEGATIVE_INFINITY);
		return result;
	}
	
	Outline getLeftOutline(Figure fig){
		if(fig instanceof Tree){
			Tree subTree = (Tree)fig;
			return subTree.leftOutline;
		} else {
			Outline result = new Outline(true);
			double hg = prop.get2DReal(major, GAP);
			result.add(0, 0);
			result.add(fig.minSize.get(major)+hg, Double.POSITIVE_INFINITY);
			return result;
		}
	}
	
	Outline getRightOutline(Figure fig){
		if(fig instanceof Tree){
			Tree subTree = (Tree)fig;
			return subTree.rightOutline;
		} else {
			Outline result = new Outline(false);
			double hg = prop.get2DReal(major, GAP);
			double vg = prop.get2DReal(minor, GAP);
			result.add(0, fig.minSize.get(minor) + vg);
			result.add(fig.minSize.get(major) + hg, Double.NEGATIVE_INFINITY);
			return result;
		}
	}
	
	static class Outline{
		
		ArrayList<Coordinate> outline;
		boolean left;
		Outline(boolean left){
			this.left = left;
			outline = new ArrayList<Coordinate>();
		}
		
		void clear(){
			outline.clear();
		}
		
		double getMinorMin(){
			if(left){
				return Double.POSITIVE_INFINITY;
			} else {
				return Double.NEGATIVE_INFINITY;
			}
		}
		
		double getMinor(int i){
			if(i < 0 || i >= outline.size()){
				return getMinorMin();
			} else {
				return outline.get(i).getY();
			}
		}
		
		double getMajor(int i){
			if(i < 0 || i >= outline.size()){
				return Double.POSITIVE_INFINITY;
			} else {
				return outline.get(i).getX();
			}
		}
		
		private double getMajorMin(int i) {
			if(i < 0 || i >= outline.size()){
				return Double.NEGATIVE_INFINITY;
			} else {
				return outline.get(i).getX();
			}
		}
		
		int nrElements(){
			return outline.size();
		}
		
		void move(double majorOffset, double minorOffset){
			for(Coordinate c : outline){
				c.add(Dimension.X,majorOffset);
				c.add(Dimension.Y,minorOffset);
			}
		}
		
		double getMax(double l,double r){
			if(left){
				return Math.min(l, r);
			} else {
				return Math.max(l, r);
			}
		}
		
		void add(double major, double minor){
			outline.add(new Coordinate(major,minor));
		}
		
		Outline merge(Outline other){
			if(outline.isEmpty()) return other;
			else if(other.outline.isEmpty()) return this;
			Outline result = new Outline(left);
			double prevMinor = getMinorMin();
			int i, j;
			i = j = -1;
			while(i < nrElements() || j < other.nrElements()){
				double nextMajorL, nextMajorR;
				nextMajorL = getMajor(i+1);
				nextMajorR = other.getMajor(j+1);
				if(nextMajorL < nextMajorR){
					i++;
				} else if(nextMajorL > nextMajorR){
					j++;
				} else {
					i++; j++;
				}
				double curMinor = getMax(getMinor(i), other.getMinor(j));
				if(curMinor != prevMinor){
					result.add(Math.max(getMajorMin(i),other.getMajorMin(j)), curMinor);
					prevMinor = curMinor;
				}
			}
			return result;
		}
	}
	
	static double getSeperation(Outline rightOutline,Outline leftOutline){
		double result = Double.NEGATIVE_INFINITY;
		int i, j;
		i = j = -1;
		while(i < leftOutline.nrElements() && j < rightOutline.nrElements()){
			double nextMajorL, nextMajorR;
			nextMajorL = leftOutline.getMajor(i+1);
			nextMajorR = rightOutline.getMajor(j+1);
			if(nextMajorL < nextMajorR){
				i++;
			} else if(nextMajorL > nextMajorR){
				j++;
			} else {
				i++; j++;
			}
			result = Math.max(result, rightOutline.getMinor(j) - leftOutline.getMinor(i));
		}
		return result;
	}
}
