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
package org.rascalmpl.library.vis.figure;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.Box;
import org.rascalmpl.library.vis.figure.combine.containers.Ellipse;
import org.rascalmpl.library.vis.figure.combine.containers.Space;
import org.rascalmpl.library.vis.figure.compose.Grid;
import org.rascalmpl.library.vis.figure.compose.HVCat;
import org.rascalmpl.library.vis.figure.compose.Overlay;
import org.rascalmpl.library.vis.figure.compose.Pack;
import org.rascalmpl.library.vis.figure.compose.WidthDependsOnHeightWrapper;
import org.rascalmpl.library.vis.figure.graph.layered.LayeredGraph;
import org.rascalmpl.library.vis.figure.graph.layered.LayeredGraphEdge;
import org.rascalmpl.library.vis.figure.interaction.ComputeFigure;
import org.rascalmpl.library.vis.figure.interaction.FigureSwitch;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.figure.interaction.Timer;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Button;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Checkbox;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Choice;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Combo;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.Scrollable;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.TextField;
import org.rascalmpl.library.vis.figure.tree.Tree;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.properties.Types;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters;
import org.rascalmpl.library.vis.util.vector.Dimension;
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
		BOX,// scheduled for removal
		BUTTON,
		CHECKBOX,
		CHOICE,
		COMBO,
		COMPUTEFIGURE,
		EDGE, 
		ELLIPSE, // scheduled for removal
		GRAPH, 
		GRID,
		FIGURESWITCH,
		LEFTAXIS,
		RIGHTAXIS,
		TOPAXIS,
		BOTTOMAXIS,
		LEFTSCREEN,
		RIGHTSCREEN,
		BOTTOMSCREEN,
		TOPSCREEN,
		HVCAT,
		NOMINALKEY,
		MOUSEOVER,
		INTERVALKEY,
		OUTLINE,// scheduled for removal
		OVERLAY, 
		OVERLAP,
		PACK, 
		PLACE,// scheduled for removal
		PROJECTION,
		ROTATE,
		SCROLLABLE,
		SPACE,// scheduled for removal
		TEXT, 
		TEXTFIELD,
		TIMER,
		TREE,
		TREEMAP,
		WEDGE,// scheduled for removal...
		WIDTHDEPSHEIGHT,
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
    	put("_leftAxis",    Primitives.LEFTAXIS);    
    	put("_rightAxis",   Primitives.RIGHTAXIS);   
    	put("_topAxis",     Primitives.TOPAXIS);   
    	put("_bottomAxis",  Primitives.BOTTOMAXIS);
    	put("_fswitch"   ,  Primitives.FIGURESWITCH);
    	put("_leftScreen",  Primitives.LEFTSCREEN);
    	put("_rightScreen", Primitives.RIGHTSCREEN);
    	put("_topScreen",   Primitives.TOPSCREEN);
    	put("_bottomScreen",Primitives.BOTTOMSCREEN);
    	put("_hvcat",		Primitives.HVCAT);
    	put("_mouseOver",	Primitives.MOUSEOVER);
    	put("_nominalKey",  Primitives.NOMINALKEY);
    	put("_intervalKey", Primitives.INTERVALKEY);
      	put("_outline",		Primitives.OUTLINE);	
    	put("_overlay",		Primitives.OVERLAY);	
    	put("_overlap",     Primitives.OVERLAP);
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
    	put("_wedge",		Primitives.WEDGE);
    	put("_widthDepsHeight", Primitives.WIDTHDEPSHEIGHT);
    	put("_xaxis",		Primitives.XAXIS);
    }};
	
    
    public static Figure[] makeList(IFigureConstructionEnv env, IValue list, PropertyManager properties, IList childProps){
    	IList elems = (IList)list;
    	Figure[] result = new Figure[elems.length()];
    	for (int i = 0; i < elems.length(); i++) {
    		IConstructor c = (IConstructor)elems.get(i);
			result[i] = FigureFactory.make(env, c, properties, childProps);
		}
    	return result;
    }
    
    public static Figure[][] make2DList(IFigureConstructionEnv env, IValue list, PropertyManager properties, IList childProps){
    	IList elems = (IList)list;
    	Figure[][] result = new Figure[elems.length()][];
    	for (int i = 0; i < elems.length(); i++) {
    		IList c = (IList)elems.get(i);
			result[i] = makeList(env, c, properties, childProps);
		}
    	return result;
    }
    
    public static Figure makeChild(IFigureConstructionEnv env, IConstructor c, PropertyManager properties, IList childProps ){
    	if(c.arity() == 2 ){
    		return makeChild(0,env,c,properties,childProps);
    	} else {
    		return null;
    	}
    }
    
    public static Figure makeChild(int index,IFigureConstructionEnv env, IConstructor c, PropertyManager properties, IList childProps ){
    		return FigureFactory.make(env, (IConstructor)c.get(index), properties, childProps);
    }
    
    public static String[] makeStringList(IList list){
    	String[] result = new String[list.length()];
    	for(int i = 0 ; i < list.length() ; i++){
    		result[i] = ((IString)list.get(i)).getValue();
    	}
    	return result;
    }
    
    
    
	
	public static Figure make(IFigureConstructionEnv env, IConstructor c, PropertyManager properties, IList childProps){
		String ename = c.getName();
		properties = PropertyManager.extendProperties(env, c, properties, childProps);
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
			return new Box( makeChild(env,c,properties,childPropsNext), properties );
			
		case BUTTON:
			return new Button(env, ((IString) c.get(0)).getValue(),c.get(1), properties);
		
		case CHECKBOX:
			return new Checkbox(env,  ((IString) c.get(0)).getValue(),((IBool)c.get(1)).getValue(), c.get(2), properties);
			
		case CHOICE:
			return new Choice(env, makeStringList((IList) c.get(0)), c.get(1),  properties);
		
		case COMBO:
			return new Combo(env, makeStringList((IList)c.get(0)), c.get(1),  properties);					
			
		case COMPUTEFIGURE:
			return new ComputeFigure(env, properties,  c.get(0), childPropsNext);
			
	
		case ELLIPSE:
			return new Ellipse( makeChild(env,c,properties,childPropsNext), properties );
					
		case FIGURESWITCH:
			PropertyValue<Integer> choice = Properties.produceMaybeComputedValue(Types.INT,c.get(0),properties,env);
			children = makeList(env,c.get(1),properties,childPropsNext);
			return new FigureSwitch(choice, children, properties);
		case GRAPH:
			//if(properties.getStringProperty(Properties.HINT).contains("lattice"))
			//	return new LatticeGraph(env, properties, (IList) c.get(0), (IList)c.get(1));
			if(properties.getStr(Properties.HINT).contains("layered"))
				return new LayeredGraph(env, properties, (IList) c.get(0), (IList)c.get(1));
			//if(properties.getStringProperty(Properties.HINT).contains("leveled"))
			//	return new LeveledGraph(env, properties, (IList) c.get(0), (IList)c.get(1));
			//return new SpringGraph(env, properties, (IList) c.get(0), (IList)c.get(1));
			throw new Error("Graph temporarily out of order");
			

		/*
		case LEFTAXIS:
			return new VAxis(((IString) c.get(0)).getValue(),false, makeChild(1,env,c,properties,childPropsNext), properties );
		case RIGHTAXIS:
			return new VAxis(((IString) c.get(0)).getValue(),true, makeChild(1,env,c,properties,childPropsNext), properties );
		case TOPAXIS:
			return new HAxis(((IString) c.get(0)).getValue(),false, makeChild(1,env,c,properties,childPropsNext), properties );
		case BOTTOMAXIS:
			return new HAxis(((IString) c.get(0)).getValue(),true, makeChild(1,env,c,properties,childPropsNext), properties );
						
			
			
		case LEFTSCREEN:
			return new HScreen(true,false, makeChild(env,c,properties,childPropsNext), properties );
		case RIGHTSCREEN:
			return new HScreen(true,true, makeChild(env,c,properties,childPropsNext), properties );
		case TOPSCREEN:
			return new HScreen(false,true, makeChild(env,c,properties,childPropsNext), properties );
		case BOTTOMSCREEN:
			return new HScreen(false,true, makeChild(env,c,properties,childPropsNext), properties );
		
			*/
			
		case INTERVALKEY:
			//return new IntervalKey(env,c.get(0),c.get(1),properties,childProps);
		case NOMINALKEY:
			//return new NominalKey(env,(IList)c.get(0),c.get(1),properties,childProps);
			
		case HVCAT:
			children = makeList(env,c.get(0),properties,childPropsNext);
			return new HVCat(Dimension.X, children, properties);
			
		case GRID:
			Figure[][] elems = make2DList(env, c.get(0), properties, childPropsNext);
			return new Grid( elems, properties);
			
						
		case OUTLINE:
			throw new Error("Outline out of order");
			//return new Outline( properties, (IList)c.get(0), (IInteger) c.get(1));
		
	
		case OVERLAY: 
			children = makeList(env,c.get(0),properties,childPropsNext);
			return new Overlay( children, properties);
			
		case OVERLAP:
			 Figure under = makeChild(0,env,c,properties,childPropsNext);
			 Figure over =  makeChild(1,env,c,properties,childPropsNext);
			 return new Overlap(under, over, properties);
		case MOUSEOVER:
			 under = makeChild(0,env,c,properties,childPropsNext);
			 over =  makeChild(1,env,c,properties,childPropsNext);
			 return new MouseOver(under, over, properties);
		case PACK:  
			children = makeList(env,c.get(0),properties,childPropsNext);
			return new Pack(Dimension.X, children, properties);
			
		case PLACE:
			throw new Error("Place out of order..");
			//return new Place(env, properties, (IConstructor) c.get(0), (IString) c.get(1), (IConstructor) c.get(2), ctx);

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
			Figure projecton = makeChild(projectionIndex,env,c,properties,childPropsNext);
			Figure projection = makeChild(0,env,c,properties,childPropsNext);
			//return new Projection(env,name,projecton,projection,properties);
		case ROTATE:
			//TODO
			child =  makeChild(1,env,c,properties,childPropsNext);
			//double angle = PropertyParsers.parseNum(c.get(0));
			throw new Error("Rotate out of order..");
			//return new Rotate(env, angle, child, properties);
			

		case SCROLLABLE:
			return new Scrollable(((IBool)c.get(0)).getValue(), ((IBool)c.get(1)).getValue(), env, (IConstructor)c.get(2),  properties);
			
		case SPACE:
			return new Space( makeChild(env,c,properties,childPropsNext), properties );
			
		case TEXT:
			
			PropertyValue<String> txt = Properties.produceMaybeComputedValue(Types.STR,c.get(0),properties,env);

			//return new Text(env, properties,  (IString) c.get(0), ctx);	// TODO: check this
			return new Text(properties,txt);
						
		case TEXTFIELD:
			validate = null;
			if(c.arity() > 3) validate = c.get(2);
			return new TextField(env,  ((IString) c.get(0)).getValue(), c.get(1), validate, properties);
		case TIMER:
			return new Timer(env, c.get(0), c.get(1), makeChild(2,env,c,properties,childPropsNext), properties );
			
		case TREE: 			
			return new Tree(env,properties, (IList) c.get(0), (IList)c.get(1));

		case TREEMAP: 			
			//return new TreeMap(env,properties, (IList) c.get(0), (IList)c.get(1), ctx);
			throw new Error("Treemap temporarily out of order..");

		case WIDTHDEPSHEIGHT:
			return new WidthDependsOnHeightWrapper(Dimension.X, env, (IConstructor)c.get(0),  properties);
		case WEDGE:			
			//return new Wedge( makeChild(env,c,properties,childPropsNext), properties );
			throw new Error("Wedge is gone to the eternal bitfields");
			
		}
		throw RuntimeExceptionFactory.illegalArgument(c, env.getRascalContext().getCurrentAST(),  env.getRascalContext().getStackTrace());
	}
	
	
//	
//	public static SpringGraphEdge makeSpringGraphEdge(SpringGraph G, IFigureConstructionEnv env, IConstructor c,
//			PropertyManager properties) {
//		IString from = (IString)c.get(0);
//		IString to = (IString)c.get(1);
//		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
//		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
//		return new SpringGraphEdge(G, env, properties, from, to, toArrow, fromArrow);
//	}
	
	public static LayeredGraphEdge makeLayeredGraphEdge(LayeredGraph G, IFigureConstructionEnv env, IConstructor c,
			PropertyManager properties) {
		IString from = (IString)c.get(0);
		IString to = (IString)c.get(1);
//		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
//		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
		return new LayeredGraphEdge(G, env, properties, from, to);
	}
//
//	public static LeveledGraphEdge makeLeveledGraphEdge(LeveledGraph G, IFigureConstructionEnv env, IConstructor c,
//			PropertyManager properties) {
//		IString from = (IString)c.get(0);
//		IString to = (IString)c.get(1);
////		IConstructor toArrow = c.arity() > 3 ? (IConstructor) c.get(2) : null;
////		IConstructor fromArrow = c.arity() > 4 ? (IConstructor)  c.get(3) : null;
//		return new LeveledGraphEdge(G, env, properties, from, to);
//	}
//	
//	public static LatticeGraphEdge makeLatticeGraphEdge(LatticeGraph G, IFigureConstructionEnv env, IConstructor c,
//			PropertyManager properties) {
//		IString from = (IString)c.get(0);
//		IString to = (IString)c.get(1);
//		return new LatticeGraphEdge(G, env, properties, from, to);
//	}
//	*/

}
