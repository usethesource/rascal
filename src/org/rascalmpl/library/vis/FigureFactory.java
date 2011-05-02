/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.compose.Grid;
import org.rascalmpl.library.vis.compose.HCat;
import org.rascalmpl.library.vis.compose.HVCat;
import org.rascalmpl.library.vis.compose.Overlay;
import org.rascalmpl.library.vis.compose.Pack;
import org.rascalmpl.library.vis.compose.Place;
import org.rascalmpl.library.vis.compose.VCat;
import org.rascalmpl.library.vis.containers.Box;
import org.rascalmpl.library.vis.containers.HAxis;
import org.rascalmpl.library.vis.containers.HScreen;
import org.rascalmpl.library.vis.containers.Ellipse;
import org.rascalmpl.library.vis.containers.Projection;
import org.rascalmpl.library.vis.containers.Space;
import org.rascalmpl.library.vis.containers.VAxis;
import org.rascalmpl.library.vis.containers.VScreen;
import org.rascalmpl.library.vis.containers.Wedge;
import org.rascalmpl.library.vis.graph.lattice.LatticeGraph;
import org.rascalmpl.library.vis.graph.lattice.LatticeGraphEdge;
import org.rascalmpl.library.vis.graph.layered.LayeredGraph;
import org.rascalmpl.library.vis.graph.layered.LayeredGraphEdge;
import org.rascalmpl.library.vis.graph.spring.SpringGraph;
import org.rascalmpl.library.vis.graph.spring.SpringGraphEdge;
import org.rascalmpl.library.vis.interaction.Button;
import org.rascalmpl.library.vis.interaction.Checkbox;
import org.rascalmpl.library.vis.interaction.Choice;
import org.rascalmpl.library.vis.interaction.ComputeFigure;
import org.rascalmpl.library.vis.interaction.TextField;
import org.rascalmpl.library.vis.properties.IPropertyValue;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyParsers;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;
import org.rascalmpl.library.vis.tree.Tree;
import org.rascalmpl.library.vis.tree.TreeMap;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * 
 * FigureFactory: factory for creating visual elements.
 * 
 * @author paulk
 *
 */
@SuppressWarnings("serial")
public class FigureFactory {
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static IList emptyList = vf.list();
	
	enum Primitives {
		BOX,
		BUTTON,
		CHECKBOX,
		CHOICE,
		COMPUTEFIGURE,
		CONTROLON,
		CONTROLOFF,
		EDGE, 
		ELLIPSE, 
		GRAPH, 
		GRID,
		HAXIS,
		HCAT, 
		HSCREEN,
		HVCAT,
		OUTLINE,
		OVERLAY, 
		PACK, 
		PLACE,
		PROJECTION,
		ROTATE,
		SCALE,
		SHAPE,
		SPACE,
		TEXT, 
		TEXTFIELD,
		TREE,
		TREEMAP,
		USE,
		VAXIS,
		VCAT,
		VERTEX,
		VSCREEN,
		WEDGE,
		XAXIS
		}
					  
    static HashMap<String,Primitives> pmap = new HashMap<String,Primitives>() {
    {
    	put("_box",			Primitives.BOX);
    	put("_button", 		Primitives.BUTTON);
    	put("_checkbox",	Primitives.CHECKBOX);
    	put("_choice", 		Primitives.CHOICE);
    	put("_computeFigure",Primitives.COMPUTEFIGURE);
    	put("_edge",		Primitives.EDGE);
    	put("_ellipse",		Primitives.ELLIPSE);
    	put("_graph",		Primitives.GRAPH);
    	put("_grid",		Primitives.GRID);
    	put("_haxis",       Primitives.HAXIS);      
    	put("_hcat",		Primitives.HCAT);
    	put("_hscreen",      Primitives.HSCREEN);
    	put("_hvcat",		Primitives.HVCAT);
      	put("_outline",		Primitives.OUTLINE);	
    	put("_overlay",		Primitives.OVERLAY);	
    	put("_pack",		Primitives.PACK);	
    	put("_place",		Primitives.PLACE);
    	put("_projection",	Primitives.PROJECTION);
    	put("_rotate",      Primitives.ROTATE);
    	put("_scale",		Primitives.SCALE);
    	put("_shape",		Primitives.SHAPE);
    	put("_space",		Primitives.SPACE);
    	put("_text",		Primitives.TEXT);		
    	put("_textfield",	Primitives.TEXTFIELD);
    	put("_tree",		Primitives.TREE);
       	put("_treemap",		Primitives.TREEMAP);
    	put("_use",			Primitives.USE);
    	put("_vaxis",       Primitives.VAXIS);  
    	put("_vcat",		Primitives.VCAT);
    	put("_vscreen",      Primitives.VSCREEN);
    	put("_vertex",		Primitives.VERTEX);
    	put("_wedge",		Primitives.WEDGE);
    	put("_xaxis",		Primitives.XAXIS);
    }};
	
	
	@SuppressWarnings("incomplete-switch")
	public static Figure make(IFigureApplet fpa, IConstructor c, PropertyManager properties, IList childProps, IEvaluatorContext ctx){
		String ename = c.getName();
		properties = PropertyManager.extendProperties(fpa, c, properties, childProps, ctx);
		IList childPropsNext = PropertyManager.getChildProperties((IList) c.get(c.arity()-1));
		if(childProps != null){
			IList childchildProps = PropertyManager.getChildProperties(childProps);
			if(childchildProps != null){
				if(childPropsNext != null){
					childPropsNext = childchildProps.concat(childPropsNext);
				} else {
					childPropsNext = childchildProps;
				}
			}
		}
		switch(pmap.get(ename)){
			
		case BOX:
			return new Box(fpa, properties, c.arity() == 2 ? (IConstructor) c.get(0) : null, childPropsNext, ctx);
			
		case BUTTON:
			return new Button(fpa, properties, (IString) c.get(0), c.get(1), ctx);
		
		case CHECKBOX:
			return new Checkbox(fpa, properties, (IString) c.get(0), c.get(1), ctx);
			
		case CHOICE:
			return new Choice(fpa, properties, (IList) c.get(0), c.get(1), ctx);
			
		case COMPUTEFIGURE:
			return new ComputeFigure(fpa, properties,  c.get(0), ctx);
							
		case ELLIPSE:
			return new Ellipse(fpa, properties, c.arity() == 2 ? (IConstructor) c.get(0) : null, childPropsNext,ctx);
					
		case GRAPH:
			if(properties.getStringProperty(StrProp.HINT).contains("lattice"))
				return new LatticeGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			if(properties.getStringProperty(StrProp.HINT).contains("layered"))
				return new LayeredGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			return new SpringGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case GRID: 
			return new Grid(fpa, properties, (IList) c.get(0), childPropsNext, ctx);

		case HAXIS:
			return new HAxis(c.arity() == 2 ? (IConstructor) c.get(0) : null,fpa, properties, childPropsNext, ctx);	
			
		case HCAT:
			return new HCat(fpa, properties, (IList) c.get(0), childPropsNext, ctx);
			
		case HSCREEN:
			return new HScreen(c.arity() == 2 ? (IConstructor) c.get(0) : null,fpa, properties, childPropsNext, ctx);	
			
		case HVCAT:
			return new HVCat(fpa, properties, (IList) c.get(0), childPropsNext, ctx);
						
		case OUTLINE: 
			return new Outline(fpa, properties, (IList)c.get(0), (IInteger) c.get(1));
			
		case OVERLAY: 
			return new Overlay(fpa, properties, (IList) c.get(0), childPropsNext, ctx);
			
		case PACK:  
			return new Pack(fpa, properties, (IList) c.get(0), childPropsNext, ctx);
			
		case PLACE:
			return new Place(fpa, properties, (IConstructor) c.get(0), (IString) c.get(1), (IConstructor) c.get(2), ctx);

		case PROJECTION:
			if(c.arity() == 4)
				return new Projection(((IString) c.get(1)).getValue(),(IConstructor) c.get(2), fpa, properties, (IConstructor) c.get(0), childPropsNext, ctx);
			else 
				return new Projection("",(IConstructor) c.get(1), fpa, properties, (IConstructor) c.get(0), childPropsNext, ctx);
		case ROTATE:
			//TODO
			return new Rotate(fpa, properties, c.get(0), (IConstructor) c.get(1), ctx);
			
		case SCALE:
			//TODO
			if(c.arity() == 3)
				return new Scale(fpa, properties, c.get(0), c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Scale(fpa, properties, c.get(0), c.get(1), (IConstructor) c.get(2), ctx);
			
		case SHAPE: 
			return new Shape(fpa, properties, (IList) c.get(0), childPropsNext, ctx);
			
		case SPACE:
			return new Space(fpa, properties, c.arity() == 2 ? (IConstructor) c.get(0) : null, childPropsNext, ctx);
			
		case TEXT:
			IPropertyValue<String> txt = new PropertyParsers.StringArgParser().parseProperty(StrProp.TEXT, c, null, 0, fpa, ctx);

			//return new Text(fpa, properties,  (IString) c.get(0), ctx);	// TODO: check this
			return new Text(fpa, properties,txt);
						
		case TEXTFIELD:
			if(c.arity() > 3)
				return new TextField(fpa, properties, (IString) c.get(0), c.get(1), c.get(2), ctx);
			return new TextField(fpa, properties, (IString) c.get(0), c.get(1), null, ctx);
			
		case TREE: 			
			return new Tree(fpa,properties, (IList) c.get(0), (IList)c.get(1), ctx);

		case TREEMAP: 			
			return new TreeMap(fpa,properties, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case USE:			
			return new Use(fpa, properties, (IConstructor) c.get(0), ctx);
			
		case VAXIS:
			return new VAxis(c.arity() == 2 ? (IConstructor) c.get(0) : null,fpa, properties, childPropsNext, ctx);	
			
		case VCAT:
			return new VCat(fpa, properties, (IList) c.get(0), childPropsNext, ctx);
		
		case VSCREEN:
			return new VScreen( c.arity() == 2 ? (IConstructor) c.get(0) : null,fpa, properties, childPropsNext, ctx);	
			
		case VERTEX:			
			return new Vertex(fpa, properties, c.get(0), c.get(1), c.arity() == 4 ? (IConstructor) c.get(2) : null, ctx);
			
		case WEDGE:			
			return new Wedge(fpa, properties, c.arity() == 2 ? (IConstructor) c.get(0) : null, childPropsNext, ctx);
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	public static SpringGraphEdge makeSpringGraphEdge(SpringGraph G, IFigureApplet fpa, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
		return new SpringGraphEdge(G, fpa, properties, from, to, toArrow, fromArrow,ctx);
	}
	
	public static LayeredGraphEdge makeLayeredGraphEdge(LayeredGraph G, IFigureApplet fpa, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
		return new LayeredGraphEdge(G, fpa, properties, from, to, toArrow, fromArrow, ctx);
	}
	
	public static LatticeGraphEdge makeLatticeGraphEdge(LatticeGraph G, IFigureApplet fpa, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
		return new LatticeGraphEdge(G, fpa, properties, from, to,  ctx);
	}

}
