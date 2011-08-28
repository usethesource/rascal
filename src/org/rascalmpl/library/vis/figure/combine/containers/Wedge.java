/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.combine.containers;





/**
 * A wedge is a vee-shaped figure mostly used in pie charts. 
 * With a non-null inner radius wedge becomes a shape that can be used in ring structures.
 * 
 * Relevant properties:
 * - height			radius
 * - innerRadius
 * - fromAngle		starting angle of wedge
 * - toAngle		end angle of wedge
 * 
 * @author paulk
 * 
 * TODO: fix me for resizing! specialize connectFrom
 *
 */
//
//public class Wedge extends Container {
//	private double fromAngle;
//	private double toAngle;
//	private double radius;
//	private double innerRadius;
//	private double leftAnchor;
//	private double rightAnchor;
//	private double topAnchor;
//	private double bottomAnchor;
//	
//	private double centerX;
//	private double centerY;
//	
//	private double IX;	// center of inner element, relative to (centerX, centerY)
//	private double IY;
//	
//	double Ax;	// start of outer arc (relative to center)
//	double Ay;
//	
//	double Bx;	// end of outer arc
//	double By;
//	
//	double Cx;	// start of inner arc
//	double Cy;
//	
//	double Dx;	// end of inner arc
//	double Dy;
//	
//	int qFrom;	// Quadrant of fromAngle;
//	int qTo;	// Quadrant of toAngle;
//	
//	private static boolean debug = false;
//
//	public Wedge(Figure inside, PropertyManager properties) {
//		super(inside, properties);
//	}
//	
//	// Determine quadrant of angle according to numbering scheme:
//	//  3  |  4
//	//  2  |  1
//	
//	private int quadrant(double angle){
//		if(debug)System.err.printf("angle 1 = %f\n", angle);
//		if(angle < 0)
//			return quadrant(angle +2 * FigureMath.PI);
//		if( angle <=  FigureMath.PI/2)
//			return 1;
//		if( angle <=  FigureMath.PI)
//			return 2;
//		if( angle <=  1.5 * FigureMath.PI)
//			return 3;
//		if(angle <= 2 * FigureMath.PI)
//			return 4;
//		return quadrant(angle - 2 * FigureMath.PI);
//	}
//
//	@Override
//	public
//	void bbox(){
//		radius = getHeightProperty();
//		double lw = getLineWidthProperty();
//		innerRadius = getInnerRadiusProperty();
//
//		fromAngle = FigureMath.radians(getFromAngleProperty());
//		toAngle= FigureMath.radians(getToAngleProperty());
//		
//		if(toAngle < fromAngle)
//			toAngle += 2 * FigureMath.PI;
//		
//		
//		if(innerFig != null)	// Compute bounding box of inside object.
//			innerFig.bbox();
//		
//		double sinFrom = FigureMath.sin(fromAngle);
//		double cosFrom = FigureMath.cos(fromAngle);
//		double sinTo = FigureMath.sin(toAngle);
//		double cosTo = FigureMath.cos(toAngle);
//		
//		double rsinFrom = radius * Math.abs(sinFrom);
//		double rcosFrom = radius * Math.abs(cosFrom);
//		
//		double rsinTo = radius * Math.abs(sinTo);
//		double rcosTo = radius * Math.abs(cosTo);
//		
//		Ax = radius*cosFrom;  // start of outer arc
//		Ay = radius*sinFrom;
//		
//		Bx = radius*cosTo;    // end of outer arc
//		By = radius*sinTo;
//		
//		Cx = innerRadius*cosTo;  // start of inner arc
//		Cy = innerRadius*sinTo;
//		
//		Dx = innerRadius*cosFrom; // end of inner arc
//		Dy = innerRadius*sinFrom;
//		
//		// Compute center and max (approximate) width of inner element
//		
//		double middleAngle = fromAngle + (toAngle-fromAngle)/2;
//		
//		double raux = innerRadius + 0.5f*(radius-innerRadius);
//
//		switch(quadrant(middleAngle)){
//		case 1:	IX = raux * FigureMath.cos(middleAngle);
//				IY = raux * FigureMath.sin(middleAngle);
//				break;
//		case 2:
//				IX = -raux * FigureMath.cos(FigureMath.PI - middleAngle);
//				IY = raux * FigureMath.sin(FigureMath.PI - middleAngle);
//				break;
//		case 3:	IX = -raux * FigureMath.cos(middleAngle - FigureMath.PI);
//				IY = -raux * FigureMath.sin(middleAngle - FigureMath.PI);
//				break;
//		case 4:	IX = raux * FigureMath.cos(2 *  FigureMath.PI - middleAngle);
//				IY = -raux * FigureMath.sin(2 *  FigureMath.PI - middleAngle);
//				break;
//		}
//		
//		if(debug)System.err.printf("Quadrant=%d,AX=%f,AY=%f, BX=%f,BY=%f,CX=%f,CY=%f,DX=%f,DY=%f,IX=%f,IY=%f\n",
//									quadrant(middleAngle),Ax,Ay,Bx,By,Cx,Cy,Dx, Dy,IX,IY);
//		
//		qFrom = quadrant(fromAngle);
//		qTo = quadrant(toAngle);
//		
//		if(debug)System.err.printf("qFrom=%d, qTo=%d\n", qFrom, qTo);
//		
//		/*
//		 * Perform a case analysis to determine the anchor values.
//		 * TODO Is there a nicer way to express this?
//		 */
//		if(qFrom == qTo && qTo < qFrom){
//			leftAnchor = rightAnchor = topAnchor = bottomAnchor = radius;
//		} else {
//		
//			switch(qFrom){
//			case 1:
//				switch(qTo){
//				case 1:	leftAnchor = 0; 		rightAnchor = rcosFrom; topAnchor = 0; 		bottomAnchor = rsinTo; break;
//				case 2:	leftAnchor = rcosTo; 	rightAnchor = rcosFrom; topAnchor = 0; 		bottomAnchor = radius; break;
//				case 3:	leftAnchor = radius;	rightAnchor = rcosFrom;	topAnchor = rsinTo;	bottomAnchor = radius; break;
//				case 4:	leftAnchor = radius;	rightAnchor = Math.max(
//															  rcosFrom, 
//						                                      rcosTo);	topAnchor = radius;	bottomAnchor = radius; break;
//				}
//				break;
//			case 2:
//				switch(qTo){
//				case 2:	leftAnchor = rcosTo;	rightAnchor = 0; 		topAnchor = 0; 		bottomAnchor = rsinFrom; break;
//				case 3:	leftAnchor = radius; 	rightAnchor = 0; 		topAnchor = rsinTo; bottomAnchor = rsinFrom; break;
//				case 4:	leftAnchor = radius;	rightAnchor = rcosTo;	topAnchor = radius;	bottomAnchor = rsinFrom; break;
//				case 1:	leftAnchor = radius;	rightAnchor = radius;	topAnchor = radius;	bottomAnchor = Math.max(rsinFrom, rsinTo); break;
//				}
//				break;
//			case 3:
//				switch(qTo){
//				case 3:	leftAnchor = rcosFrom;	rightAnchor = 0; 		topAnchor = rsinTo; bottomAnchor = 0; break;
//				case 4:	leftAnchor = rcosFrom; 	rightAnchor = rcosTo; 	topAnchor = radius; bottomAnchor = 0; break;
//				case 1:	leftAnchor = rcosFrom;	rightAnchor = radius;	topAnchor = radius;	bottomAnchor = rsinTo; break;
//				case 2:	leftAnchor = Math.max(
//									 rcosFrom, 
//									 rcosTo);	rightAnchor = radius;	topAnchor = radius;	bottomAnchor = radius; break;
//				}
//				break;
//	
//			case 4:
//				switch(qTo){
//				case 4:	leftAnchor = 0;			rightAnchor = rcosTo; 		topAnchor = rsinFrom; 		bottomAnchor = 0; break;
//				case 1:	leftAnchor = 0; 		rightAnchor = radius; 		topAnchor = rsinFrom; 		bottomAnchor = rsinTo; break;
//				case 2:	leftAnchor = rcosTo;	rightAnchor = radius;		topAnchor = rsinFrom;		bottomAnchor = radius; break;
//				case 3:	leftAnchor = radius;	rightAnchor = radius;		topAnchor = Math.max(rsinFrom, 
//																								rsinTo);	bottomAnchor = radius; break;
//				}
//				break;
//			}
//		}
//			
//		leftAnchor += lw/2;
//		rightAnchor += lw/2;
//		topAnchor += lw/2;
//		bottomAnchor += lw/2;
//		
//		minSize.setWidth(leftAnchor + rightAnchor);
//		minSize.setHeight(topAnchor + bottomAnchor);
//		if(debug)System.err.printf("bbox.wedge: fromAngle=%f, toAngle=%f, leftAnChor=%f, rightAnchor=%f, topAnchor=%f, bottomAnchor =%f, %f, %f)\n", 
//				fromAngle, toAngle, leftAnchor, rightAnchor, topAnchor, bottomAnchor, minSize.getWidth(), minSize.getHeight());
//		setNonResizable();
//		super.bbox();
//	}
//	
//	/**
//	 * arcVertex: draw an arc as a bezierVertex that is part of a beginShape() ... endShape() sequence
//	 * @param r				radius
//	 * @param fromAngle		begin angle
//	 * @param toAngle		end angle
//	 */
//	void arcVertex(double r, double fromAngle, double toAngle, GraphicsContext gc){
//		if(debug)System.err.printf("arcVertex: fromAngle=%f, toAngle=%f\n", fromAngle, toAngle);
//	    if(Math.abs(toAngle - fromAngle) < FigureMath.PI/2){
//			double middleAngle = (toAngle - fromAngle)/2;		// fromAngle + middleAngle == middle of sector
//			double middleR = Math.abs(r / FigureMath.cos(middleAngle));	// radius of control point M
//			
//			double Mx = centerX + middleR * FigureMath.cos(fromAngle + middleAngle);	// coordinates of M
//			double My = centerY + middleR * FigureMath.sin(fromAngle + middleAngle);
//			
//			double Fx = centerX + r * FigureMath.cos(fromAngle);	// coordinates of start point
//			double Fy = centerY + r * FigureMath.sin(fromAngle);
//			
//			double Tx = centerX + r * FigureMath.cos(toAngle);		// coordinates of end point
//			double Ty = centerY + r * FigureMath.sin(toAngle);
//			if (debug){
//				System.err.printf("arcVertex: fromAngle=%f, middleAngle=%f, toAngle=%f, r=%f, middleR=%f\n", 
//								fromAngle, middleAngle, toAngle, r, middleR);
//				System.err.printf("arcVertex: Fx=%f, Fy=%f, Mx=%f, My=%f, Tx=%f, Ty=%f\n",
//									Fx, Fy, Mx, My, Tx, Ty);
//			}
//			/*
//			 * Add a bezierVertex between (Fx,Fy) and (Tx,Ty) using (Mx,My) as control point
//			 */
//			gc.bezierVertex(Fx, Fy, Mx, My, Tx, Ty);
//	    } else {
//	    	/*
//	    	 * Split when difference is larger than PI/2
//	    	 */
//	    	double medium = (toAngle - fromAngle)/2;
//	    	arcVertex(r, fromAngle, fromAngle + medium,gc);
//	    	arcVertex(r, fromAngle + medium, toAngle,gc);
//	    }
//	}
//	
//	@Override
//	void drawContainer(GraphicsContext gc) {
//		centerX = getLeft() + leftAnchor;
//		centerY = getTop() + topAnchor;
//		
//		if(debug)System.err.printf("wedge.drawContainer: %f, %f\n", centerX, centerY);
//		
//		applyProperties(gc);
//		drawActualContainer(gc);
//	}
//	
//	private void drawActualContainer(GraphicsContext gc){
//		gc.beginShape();
//		gc.vertex(centerX + Ax, centerY + Ay);
//		arcVertex(radius, fromAngle, toAngle,gc);
//		gc.vertex(centerX + Cx, centerY + Cy);
//		arcVertex(innerRadius, toAngle, fromAngle,gc);
//		gc.vertex(centerX + Ax, centerY + Ay);
//		gc.endShape();
//	}
//	
//	@Override
//	String containerName(){
//		return "wedge";
//	}
//	
//	@Override
//	public
//	void draw(GraphicsContext gc) {
//	
//		applyProperties(gc);
//		//if(debug)System.err.printf("%s.draw: left=%f, top=%f, width=%f, height=%f, hanchor=%f, vanchor=%f\n", containerName(), left, top, width, height, getHAlignProperty(), getVAlignProperty());
//
//		if(minSize.getHeight() > 0 && minSize.getWidth() > 0){
//			drawContainer(gc);
//			if(innerFig != null){
//				//if(debug)System.err.printf("%s.draw2:  inside.width=%f\n",  containerName(), innerFig.width);
//				if(innerFits()) {
//					innerDraw(gc);
//				}
//			}
//		}
//	}
//	
//	boolean innerFits(){
//		if(debug)System.err.printf("Wedge.insideFits\n");
//		return innerFig.minSize.getHeight() < radius - innerRadius && innerFig.minSize.getWidth() < minSize.getWidth(); //TODO was h1()
//	}
//	
//	/**
//	 * If the inside  element fits or during a mouseOver, draw it.
//	 */
//	void innerDraw(GraphicsContext gc){
//		innerFig.draw(gc);
//	}
//	
//	@Override
//	public double leftAlign(){
//		return leftAnchor;
//	}
//	
//	@Override
//	public double rightAlign(){
//		return rightAnchor;
//	}
//	
//	@Override
//	public double topAlign(){
//		return topAnchor;
//	}
//	
//	@Override
//	public double bottomAlign(){
//		return bottomAnchor;
//	}
//	
//	@Override
//	public boolean mouseInside(double mousex, double mousey){
//		double dx = mousex - centerX;
//		double dy = mousey - centerY;
//		double dist = FigureMath.sqrt(dx*dx + dy*dy);
//		double angle;
//		if(dx > 0 && dy > 0)
//			angle = FigureMath.asin(dy/dist);
//		else if(dx < 0 && dy > 0)
//			angle = FigureMath.PI  - FigureMath.asin(dy/dist);
//		else if (dx < 0 && dy < 0)
//			angle = FigureMath.PI + FigureMath.asin(-dy/dist);
//		else 
//			angle = 2 * FigureMath.PI - FigureMath.asin(-dy/dist);
//		
//		return dist > innerRadius && dist <  radius &&
//		       angle > fromAngle && angle < toAngle;
//	}
//	
////	@Override
////	public void drawMouseOverFigure(int mouseX, int mouseY){
////		if(isVisible()){
////			if(hasMouseOverFigure()){
////				Figure mo = getMouseOverFigure();
////				mo.bbox();
////				mo.draw(centerX + IX - mo.width/2, centerY + IY - mo.height/2);
////			} else if(innerFig != null){
////				innerDraw();
////			}
////		}
////	}
//	
//}
