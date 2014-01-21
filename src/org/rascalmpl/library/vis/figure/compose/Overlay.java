/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.compose;


import static org.rascalmpl.library.vis.properties.Properties.HCONNECT;
import static org.rascalmpl.library.vis.properties.Properties.SHAPE_CLOSED;
import static org.rascalmpl.library.vis.properties.Properties.SHAPE_CONNECTED;
import static org.rascalmpl.library.vis.properties.Properties.SHAPE_CURVED;
import static org.rascalmpl.library.vis.properties.Properties.VCONNECT;
import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.POS;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.graphics.Interpolation;
/**
 * 
 * Overlay elements by stacking them
 * 
 * @author paulk
 *
 */
import org.rascalmpl.library.vis.graphics.TypedPoint;


public class Overlay extends Compose{
	
	public Overlay(Figure[] children, PropertyManager properties) {
		super(children, properties);

	}
	
	@Override
	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		for(int i = 0; i < children.length ; i++){
			if(swtSeen){
				env.addAboveSWTElement(children[i]);
			}
			swtSeen = swtSeen || children[i].init(env, resolver,mparent, swtSeen, visible);
		}
		return swtSeen;
	}
	
	@Override
	public void computeMinSize() {
		minSize.set(0,0);
		for(Figure fig : children){
			for(Dimension d : HOR_VER){
				if(fig.prop.is2DPropertySet(d, POS)){
					minSize.setMax(d,
							(fig.prop.get2DReal(d, POS) + (1.0 - fig.prop.get2DReal(d, ALIGN)) * fig.prop.get2DReal(d, SHRINK)) * fig.minSize.get(d));
					minSize.setMax(d, fig.minSize.get(d) );
				} else {
					minSize.setMax(d, fig.minSize.get(d) / fig.prop.get2DReal(d, SHRINK));
				}
			}
		}
	}

	@Override
	public void resizeElement(Rectangle view) {
		for(Figure fig : children){
			for(Dimension d : HOR_VER){
				fig.size.set(d,size.get(d)*fig.prop.get2DReal(d, SHRINK));
				if(fig.prop.is2DPropertySet(d, POS)){
					fig.localLocation.set(d,size.get(d) * (fig.prop.get2DReal(d, POS) - fig.prop.get2DReal(d,ALIGN) * fig.prop.get2DReal(d, SHRINK)));
				} else {
					fig.localLocation.set(d,(size.get(d) - fig.size.get(d)) * fig.prop.get2DReal(d, ALIGN));
				}
			}
		}
	}
	
	@Override
	public void getFiguresUnderMouse(Coordinate c,List<Figure> result){
		if(!mouseInside(c)){
			return;
		}
		if(handlesInput()){
			Point2D.Double d = new Point2D.Double(c.getX(), c.getY());
			if(makePath().contains(d)){
				result.add(this);
			}
		}
		for(int i = children.length - 1 ; i >= 0 ; i--){
			children[i].getFiguresUnderMouse(c, result);
		}
		
	}

	
	
	
	public Path2D.Double makePath(){
	
		  boolean closed = prop.getBool(SHAPE_CLOSED);
	        boolean curved = prop.getBool(SHAPE_CURVED);
	        boolean connected =  prop.getBool(SHAPE_CONNECTED) || closed || curved;
        	Path2D.Double p = new Path2D.Double();
	        if(connected && closed && children.length >= 0){

	        	if(curved) {
	        	ArrayList<TypedPoint> res = new ArrayList<TypedPoint>();
	        	for(int i = 0 ; i < children.length ; i++){
	        		res.add(new TypedPoint(children[i].globalLocation.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
    				children[i].globalLocation.getY()   + children[i].prop.getReal(VCONNECT)  * children[i].size.getY(),TypedPoint.kind.CURVED));
	        	}
	        	
	        		Interpolation.solve(res, true);
	        		p.moveTo(Interpolation.P0[0].x, Interpolation.P0[0].y);
	        		int n = Interpolation.P0.length;
	        		for (int i = 0; i < n; i++)
	        			p.curveTo(
	        			Interpolation.P1[i].x,
	        					Interpolation.P1[i].y,
	        					Interpolation.P2[i].x,
	        					 Interpolation.P2[i].y,
	        					 Interpolation.P3[i].x,
	        					Interpolation.P3[i].y);
	        	} else{
	        		p.moveTo(children[0].globalLocation.getX() + children[0].prop.getReal(HCONNECT) * children[0].size.getX(),
		    				children[0].globalLocation.getY()   + children[0].prop.getReal(VCONNECT)  * children[0].size.getY());
	        		for(int i = 0 ; i < children.length ; i++){
		        		p.lineTo(children[i].globalLocation.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
	    				children[i].globalLocation.getY()   + children[i].prop.getReal(VCONNECT)  * children[i].size.getY());
		        	}
	        	}
	        	p.closePath();
	        }	
	        	
	        return p;
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
	  boolean closed = prop.getBool(SHAPE_CLOSED);
        boolean curved = prop.getBool(SHAPE_CURVED);
        boolean connected =  prop.getBool(SHAPE_CONNECTED) || closed || curved;
        if(connected){
            gc.beginShape();
        }
        if(!closed){
        	gc.noFill();
        }
        
        if(closed && connected && children.length >= 0){
        	gc.vertex( children[0].globalLocation.getX() + children[0].prop.getReal(HCONNECT) * children[0].size.getX(),
    				children[0].globalLocation.getY()   + children[0].prop.getReal(VCONNECT)  * children[0].size.getY()  );
        }
        if(connected){
	        for(int i = 0 ; i < children.length ; i++){
	        	if(curved ){
	        		gc.curveVertex( children[i].globalLocation.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
	        				 children[i].globalLocation.getY()  + children[i].prop.getReal(VCONNECT)  * children[i].size.getY()  );
	        	} else {
	        		gc.vertex(children[i].globalLocation.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
	        				 children[i].globalLocation.getY()  + children[i].prop.getReal(VCONNECT) * children[i].size.getY()  );
	        	} 
	        	//System.out.printf("child %s\n",children[i].globalLocation);
	        }
        }
        
        if(connected){
			if(closed){
				int i = children.length-1;
        		gc.vertex(children[i].globalLocation.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
        				children[i].globalLocation.getY()  + children[i].prop.getReal(VCONNECT) * children[i].size.getY()  );
				gc.endShape(FigureMath.CLOSE);
			} else 
				gc.endShape();
		}
	}
		
	

	public String toString(){
		return "Overlay: " + super.toString();
	}

}
