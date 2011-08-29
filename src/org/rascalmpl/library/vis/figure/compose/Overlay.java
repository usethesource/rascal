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
import static org.rascalmpl.library.vis.properties.Properties.*;

import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureMath;
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
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
	  boolean closed = prop.getBool(SHAPE_CLOSED);
        boolean curved = prop.getBool(SHAPE_CURVED);
        boolean connected =  prop.getBool(SHAPE_CONNECTED) || closed || curved;
        // TODO: this curved stuff is unclear to me...
        if(connected){
            gc.beginShape();
        }
        if(!closed){
        	gc.noFill();
        }
        
        if(closed && connected && children.length >= 0){
        	gc.vertex(location.getX() + children[0].location.getX() + children[0].prop.getReal(HCONNECT) * children[0].size.getX(),
    				location.getY() + children[0].location.getY()   + children[0].prop.getReal(VCONNECT)  * children[0].size.getY()  );
        }
        if(connected){
	        for(int i = 0 ; i < children.length ; i++){
	        	if(curved ){
	        		gc.curveVertex(location.getX() + children[i].location.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
	        				location.getY() + children[i].location.getY()  + children[i].prop.getReal(VCONNECT)  * children[i].size.getY()  );
	        	} else {
	        		gc.vertex(location.getX() + children[i].location.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
	        				location.getY() + children[i].location.getY()  + children[i].prop.getReal(VCONNECT) * children[i].size.getY()  );
	        	} 
	        }
        }
        
        if(connected){
			if(closed){
				int i = children.length-1;
        		gc.vertex(location.getX() + children[i].location.getX() + children[i].prop.getReal(HCONNECT) * children[i].size.getX(),
        				location.getY() + children[i].location.getY()  + children[i].prop.getReal(VCONNECT) * children[i].size.getY()  );
				gc.endShape(FigureMath.CLOSE);
			} else 
				gc.endShape();
		}
	}
		
	

	public String toString(){
		return "Overlay: " + super.toString();
	}

}
