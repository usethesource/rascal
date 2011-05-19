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
import org.eclipse.imp.pdb.facts.IValue;
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
import org.rascalmpl.library.vis.containers.Ellipse;
import org.rascalmpl.library.vis.containers.HAxis;
import org.rascalmpl.library.vis.containers.HScreen;
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
import org.rascalmpl.library.vis.interaction.Combo;
import org.rascalmpl.library.vis.interaction.ComputeFigure;
import org.rascalmpl.library.vis.interaction.TextField;
import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyParsers;
import org.rascalmpl.library.vis.properties.PropertyValue;
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
		COMBO,
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
    	put("_combo", 		Primitives.COMBO);
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
	
    
    public static Figure[] makeList(IFigureApplet fpa, IValue list, PropertyManager properties, IList childProps, IEvaluatorContext ctx){
    	IList elems = (IList)list;
    	Figure[] result = new Figure[elems.length()];
    	for (int i = 0; i < elems.length(); i++) {
    		IConstructor c = (IConstructor)elems.get(i);
			result[i] = FigureFactory.make(fpa, c, properties, childProps, ctx);
		}
    	return result;
    }
    
    public static Figure makeChild(IFigureApplet fpa, IConstructor c, PropertyManager properties, IList childProps, IEvaluatorContext ctx ){
    	if(c.arity() == 2 ){
    		return makeChild(0,fpa,c,properties,childProps,ctx);
    	} else {
    		return null;
    	}
    }
    
    public static Figure makeChild(int index,IFigureApplet fpa, IConstructor c, PropertyManager properties, IList childProps, IEvaluatorContext ctx ){
    		return FigureFactory.make(fpa, (IConstructor)c.get(index), properties, childProps, ctx);
    }
    
    public static String[] makeStringList(IList list){
    	String[] result = new String[list.length()];
    	for(int i = 0 ; i < list.length() ; i++){
    		result[i] = ((IString)list.get(i)).getValue();
    	}
    	return result;
    }
    
    
    
	
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
		Figure[] children;
		IValue validate;
		Figure child;
		switch(pmap.get(ename)){
			
		case BOX:
			return new Box(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case BUTTON:
			return new Button(fpa, ((IString) c.get(0)).getValue(),c.get(1),ctx, properties);
		
		case CHECKBOX:
			return new Checkbox(fpa,  ((IString) c.get(0)).getValue(),c.get(1),ctx, properties);
			
		case CHOICE:
			return new Choice(fpa, makeStringList((IList) c.get(0)), c.get(1), ctx, properties);
		
		case COMBO:
			validate = null;
			if(c.arity() > 4) validate = c.get(3);
			
			return new Combo(fpa,((IString) c.get(0)).getValue(), makeStringList((IList)c.get(1)), c.get(2), validate, ctx, properties);					
			
		case COMPUTEFIGURE:
			return new ComputeFigure(fpa, properties,  c.get(0), childPropsNext, ctx);
			
	
		case ELLIPSE:
			return new Ellipse(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
					
		case GRAPH:
			if(properties.getStringProperty(Properties.HINT).contains("lattice"))
				return new LatticeGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			if(properties.getStringProperty(Properties.HINT).contains("layered"))
				return new LayeredGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			return new SpringGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case GRID: 
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new Grid(fpa, children , properties);

		case HAXIS:
			return new HAxis(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case HCAT:
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new HCat(fpa, children, properties);
			
		case HSCREEN:
			return new HScreen(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case HVCAT:
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new HVCat(fpa, children, properties);
						
		case OUTLINE: 
			return new Outline(fpa, properties, (IList)c.get(0), (IInteger) c.get(1));
			
		case OVERLAY: 
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new Overlay(fpa, children, properties);
			
		case PACK:  
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new Pack(fpa, children, properties);
			
		case PLACE:
			return new Place(fpa, properties, (IConstructor) c.get(0), (IString) c.get(1), (IConstructor) c.get(2), ctx);

		case PROJECTION:
			String name;
			int projectionIndex;
			if(c.arity() == 4) {
				name = ((IString) c.get(1)).getValue();
				projectionIndex = 2;
			}
			else  {
				name = "";
				projectionIndex = 1;
			}
			Figure projecton = makeChild(projectionIndex,fpa,c,properties,childPropsNext,ctx);
			Figure projection = makeChild(0,fpa,c,properties,childPropsNext,ctx);
			return new Projection(fpa,name,projecton,projection,properties);
		case ROTATE:
			//TODO
			child =  makeChild(1,fpa,c,properties,childPropsNext,ctx);
			double angle = PropertyParsers.parseNum(c.get(0));
			return new Rotate(fpa, angle, child, properties);
			
		case SCALE:
			//TODO
			int childIndex,scaleYIndex;
			if(c.arity() == 4){
				childIndex = 2;
				scaleYIndex=1;
			} else {
				childIndex = 1;
				scaleYIndex = 0;
			}
			double scaleX = PropertyParsers.parseNum(c.get(0));
			double scaleY = PropertyParsers.parseNum(c.get(scaleYIndex));
			child = makeChild(childIndex,fpa,c,properties,childPropsNext,ctx);
			return new Scale(fpa,scaleX,scaleY,child,properties);
			
		case SHAPE: 
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new Shape(fpa, children, properties);
			
		case SPACE:
			return new Space(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case TEXT:
			PropertyValue<String> txt = new PropertyParsers.StringArgParser(Properties.TEXT).parseProperty( c, null, 0, fpa, ctx);

			//return new Text(fpa, properties,  (IString) c.get(0), ctx);	// TODO: check this
			return new Text(fpa, properties,txt);
						
		case TEXTFIELD:
			validate = null;
			if(c.arity() > 3) validate = c.get(2);
			return new TextField(fpa,  ((IString) c.get(0)).getValue(), c.get(1), null, ctx, properties);
			
		case TREE: 			
			return new Tree(fpa,properties, (IList) c.get(0), (IList)c.get(1), ctx);

		case TREEMAP: 			
			return new TreeMap(fpa,properties, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case USE:			
			return new Use(fpa,makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case VAXIS:
			return new VAxis(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case VCAT:
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new VCat(fpa, children, properties);
		
		case VSCREEN:
			return new VScreen(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case VERTEX:			
			Measure dx = PropertyParsers.parseMeasure(c.get(0));
			Measure dy = PropertyParsers.parseMeasure(c.get(1));
			if(c.arity() == 4){
				child = makeChild(2,fpa,c,properties,childPropsNext,ctx);
			} else {
				child = null;
			}
			return new Vertex(fpa, dx,dy, child, properties);
			
		case WEDGE:			
			return new Wedge(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
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
//		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
//		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
		return new LayeredGraphEdge(G, fpa, properties, from, to, ctx);
	}
	
	public static LatticeGraphEdge makeLatticeGraphEdge(LatticeGraph G, IFigureApplet fpa, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
		return new LatticeGraphEdge(G, fpa, properties, from, to,  ctx);
	}

}
