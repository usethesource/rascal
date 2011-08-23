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
package org.rascalmpl.library.vis.figure.compose;


import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.POS;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.Mutable;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
/**
 * 
 * Overlay elements by stacking them
 * 
 * @author paulk
 *
 */
public class Overlay extends Compose{
	
	public Overlay(Figure[] figures, PropertyManager properties) {
		super(figures, properties);
	}
	
	@Override
	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen) {
		for(int i = 0; i < children.length ; i++){
			if(swtSeen){
				env.addAboveSWTElement(children[i]);
			}
			swtSeen = swtSeen || children[i].init(env, resolver,mparent, swtSeen);
		}
		return swtSeen;
	}
	
	@Override
	public void computeMinSize() {
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
					fig.location.set(d,size.get(d) * (fig.prop.get2DReal(d, POS) - fig.prop.get2DReal(d,ALIGN) * fig.prop.get2DReal(d, SHRINK)));
				} else {
					fig.location.set(d,(size.get(d) - fig.size.get(d)) * fig.prop.get2DReal(d, ALIGN));
				}
			}
		}
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){}
	

	public String toString(){
		return "Overlay: " + super.toString();
	}
	
	/*
	private void drawShape(GraphicsContext gc) {
		applyProperties(gc);
        boolean closed = getClosedProperty();
        boolean curved = getCurvedProperty();
        boolean connected = getConnectedProperty() || closed || curved;
        // TODO: this curved stuff is unclear to me...
        if(connected){
            gc.beginShape();
        }
        if(!closed){
        	gc.noFill();
        }
        
        if(closed && connected && figures.length >= 0){
        	gc.vertex(getLeft() + pos[0].getX() + figures[0].getHConnectProperty() * figures[0].size.getWidth(),
    				getTop() + pos[0].getY()  + figures[0].getVConnectProperty() * figures[0].size.getHeight()  );
        }
        if(connected){
	        for(int i = 0 ; i < figures.length ; i++){
	        	if(curved ){
	        		gc.curveVertex(getLeft() + pos[i].getX() + figures[i].getHConnectProperty() * figures[i].size.getWidth(),
	        				getTop() + pos[i].getY()  + figures[i].getVConnectProperty() * figures[i].size.getHeight()  );
	        	} else {
	        		gc.vertex(getLeft() + pos[i].getX() + figures[i].getHConnectProperty() * figures[i].size.getWidth(),
	        				getTop() + pos[i].getY()  + figures[i].getVConnectProperty() * figures[i].size.getHeight()  );
	        	} 
	        }
        }
        
        if(connected){
			if(closed){
				gc.vertex(getLeft()  + pos[figures.length-1].getX() + figures[figures.length-1].getHConnectProperty() * figures[figures.length-1].size.getWidth(),
						getTop() + pos[figures.length-1].getY()  + figures[figures.length-1].getVConnectProperty() * figures[figures.length-1].size.getHeight()  );
				gc.endShape(FigureMath.CLOSE);
			} else 
				gc.endShape();
		}
	}
	*/

}
