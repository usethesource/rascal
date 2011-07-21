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

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.compose.HStack;
import org.rascalmpl.library.vis.compose.HVCat;
import org.rascalmpl.library.vis.compose.Grid;
import org.rascalmpl.library.vis.compose.Overlay;
import org.rascalmpl.library.vis.compose.Pack;
import org.rascalmpl.library.vis.containers.Box;
import org.rascalmpl.library.vis.containers.Ellipse;
import org.rascalmpl.library.vis.containers.HAxis;
import org.rascalmpl.library.vis.containers.HScreen;
import org.rascalmpl.library.vis.containers.IntervalKey;
import org.rascalmpl.library.vis.containers.NominalKey;
import org.rascalmpl.library.vis.containers.Projection;
import org.rascalmpl.library.vis.containers.Scrollable;
import org.rascalmpl.library.vis.containers.Space;
import org.rascalmpl.library.vis.containers.VAxis;
import org.rascalmpl.library.vis.containers.Wedge;
import org.rascalmpl.library.vis.graph.lattice.LatticeGraph;
import org.rascalmpl.library.vis.graph.lattice.LatticeGraphEdge;
import org.rascalmpl.library.vis.graph.layered.LayeredGraph;
import org.rascalmpl.library.vis.graph.layered.LayeredGraphEdge;
import org.rascalmpl.library.vis.graph.spring.SpringGraph;
import org.rascalmpl.library.vis.graph.spring.SpringGraphEdge;
import org.rascalmpl.library.vis.graph.leveled.LeveledGraph;
import org.rascalmpl.library.vis.graph.leveled.LeveledGraphEdge;
import org.rascalmpl.library.vis.interaction.Button;
import org.rascalmpl.library.vis.interaction.Checkbox;
import org.rascalmpl.library.vis.interaction.Choice;
import org.rascalmpl.library.vis.interaction.Combo;
import org.rascalmpl.library.vis.interaction.ComputeFigure;
import org.rascalmpl.library.vis.interaction.TextField;
import org.rascalmpl.library.vis.interaction.Timer;
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
		LEFTAXIS,
		RIGHTAXIS,
		TOPAXIS,
		BOTTOMAXIS,
		LEFTSCREEN,
		RIGHTSCREEN,
		BOTTOMSCREEN,
		TOPSCREEN,
		HSTACK,
		VSTACK,
		HVCAT,
		NOMINALKEY,
		INTERVALKEY,
		OUTLINE,
		OVERLAY, 
		PACK, 
		PLACE,
		PROJECTION,
		ROTATE,
		SCROLLABLE,
		SPACE,
		TEXT, 
		TEXTFIELD,
		TIMER,
		TREE,
		TREEMAP,
		USE,
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
    	put("_leftAxis",       Primitives.LEFTAXIS);    
    	put("_rightAxis",       Primitives.RIGHTAXIS);   
    	put("_topAxis",       Primitives.TOPAXIS);   
    	put("_bottomAxis",       Primitives.BOTTOMAXIS);   
    	put("_leftScreen",  Primitives.LEFTSCREEN);
    	put("_rightScreen", Primitives.RIGHTSCREEN);
    	put("_topScreen",   Primitives.TOPSCREEN);
    	put("_bottomScreen",Primitives.BOTTOMSCREEN);
    	put("_hvcat",		Primitives.HVCAT);
    	put("_hstack",      Primitives.HSTACK);
    	put("_vstack",      Primitives.VSTACK);
    	put("_nominalKey",  Primitives.NOMINALKEY);
    	put("_intervalKey", Primitives.INTERVALKEY);
      	put("_outline",		Primitives.OUTLINE);	
    	put("_overlay",		Primitives.OVERLAY);	
    	put("_pack",		Primitives.PACK);	
    	put("_place",		Primitives.PLACE);
    	put("_projection",	Primitives.PROJECTION);
    	put("_rotate",      Primitives.ROTATE);
    	put("_scrollable",  Primitives.SCROLLABLE);
    	put("_space",		Primitives.SPACE);
    	put("_text",		Primitives.TEXT);		
    	put("_textfield",	Primitives.TEXTFIELD);
    	put("_timer",		Primitives.TIMER);
    	put("_tree",		Primitives.TREE);
       	put("_treemap",		Primitives.TREEMAP);
    	put("_use",			Primitives.USE);
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
    
    public static Figure[][] make2DList(IFigureApplet fpa, IValue list, PropertyManager properties, IList childProps, IEvaluatorContext ctx){
    	IList elems = (IList)list;
    	Figure[][] result = new Figure[elems.length()][];
    	for (int i = 0; i < elems.length(); i++) {
    		IList c = (IList)elems.get(i);
			result[i] = makeList(fpa, c, properties, childProps, ctx);
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
    
    
    
	
	public static Figure make(IFigureApplet fpa, IConstructor c, PropertyManager properties, IList childProps, IEvaluatorContext ctx){
		String ename = c.getName();
		//System.out.printf("Creating %s\n",ename);
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
			return new Checkbox(fpa,  ((IString) c.get(0)).getValue(),((IBool)c.get(1)).getValue(), c.get(2),ctx, properties);
			
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
			if(properties.getStringProperty(Properties.HINT).contains("leveled"))
				return new LeveledGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			return new SpringGraph(fpa, properties, (IList) c.get(0), (IList)c.get(1), ctx);
			

		case LEFTAXIS:
			return new VAxis(((IString) c.get(0)).getValue(),false,fpa, makeChild(1,fpa,c,properties,childPropsNext,ctx), properties );
		case RIGHTAXIS:
			return new VAxis(((IString) c.get(0)).getValue(),true,fpa, makeChild(1,fpa,c,properties,childPropsNext,ctx), properties );
		case TOPAXIS:
			return new HAxis(((IString) c.get(0)).getValue(),false,fpa, makeChild(1,fpa,c,properties,childPropsNext,ctx), properties );
		case BOTTOMAXIS:
			return new HAxis(((IString) c.get(0)).getValue(),true,fpa, makeChild(1,fpa,c,properties,childPropsNext,ctx), properties );
						
			
			
		case LEFTSCREEN:
			return new HScreen(true,false,fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
		case RIGHTSCREEN:
			return new HScreen(true,true,fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
		case TOPSCREEN:
			return new HScreen(false,true,fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
		case BOTTOMSCREEN:
			return new HScreen(false,true,fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
		
			
		case INTERVALKEY:
			return new IntervalKey(fpa,c.get(0),c.get(1),properties,childProps,ctx);
		case NOMINALKEY:
			return new NominalKey(fpa,(IList)c.get(0),c.get(1),properties,childProps,ctx);
			
		case HVCAT:
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new HVCat(fpa, children, properties);
			
		case HSTACK:
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new HStack(false,fpa, children, properties,ctx);
		case VSTACK:
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new HStack(true,fpa, children, properties,ctx);
			
		case GRID:
			Figure[][] elems = make2DList(fpa, c.get(0), properties, childPropsNext, ctx);
			return new Grid(fpa, elems, properties);
			
						
		case OUTLINE: 
			return new Outline(fpa, properties, (IList)c.get(0), (IInteger) c.get(1));
			
		case OVERLAY: 
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new Overlay(fpa, children, properties,ctx);
			
		case PACK:  
			children = makeList(fpa,c.get(0),properties,childPropsNext,ctx);
			return new Pack(fpa, children, properties);
			
		case PLACE:
			throw new Error("Place out of order..");
			//return new Place(fpa, properties, (IConstructor) c.get(0), (IString) c.get(1), (IConstructor) c.get(2), ctx);

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
			return new Projection(fpa,name,projecton,projection,properties,ctx);
		case ROTATE:
			//TODO
			child =  makeChild(1,fpa,c,properties,childPropsNext,ctx);
			double angle = PropertyParsers.parseNum(c.get(0));
			throw new Error("Rotate out of order..");
			//return new Rotate(fpa, angle, child, properties);
			

		case SCROLLABLE:
			//throw new Error("Scrollable temporary out of order");
			return new Scrollable(fpa, (IConstructor)c.get(0), ctx, properties);
			
		case SPACE:
			return new Space(fpa, makeChild(fpa,c,properties,childPropsNext,ctx), properties );
			
		case TEXT:
			PropertyValue<String> txt = new PropertyParsers.StringArgParser(Properties.TEXT).parseProperty( c, null, 0, fpa, ctx);

			//return new Text(fpa, properties,  (IString) c.get(0), ctx);	// TODO: check this
			return new Text(fpa, properties,txt);
						
		case TEXTFIELD:
			validate = null;
			if(c.arity() > 3) validate = c.get(2);
			return new TextField(fpa,  ((IString) c.get(0)).getValue(), c.get(1), validate, ctx, properties);
		case TIMER:
			return new Timer(fpa, (int)PropertyParsers.parseNum(c.get(0)), c.get(1), makeChild(2,fpa,c,properties,childPropsNext,ctx), properties );
			
		case TREE: 			
			return new Tree(fpa,properties, (IList) c.get(0), (IList)c.get(1), ctx);

		case TREEMAP: 			
			return new TreeMap(fpa,properties, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case USE:	
			throw new Error("Use temporary out of order..");
			//return new Use(fpa,makeChild(fpa,c,properties,childPropsNext,ctx), properties );

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

	public static LeveledGraphEdge makeLeveledGraphEdge(LeveledGraph G, IFigureApplet fpa, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
//		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
//		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
		return new LeveledGraphEdge(G, fpa, properties, from, to, ctx);
	}
	
	public static LatticeGraphEdge makeLatticeGraphEdge(LatticeGraph G, IFigureApplet fpa, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
		return new LatticeGraphEdge(G, fpa, properties, from, to,  ctx);
	}

}
