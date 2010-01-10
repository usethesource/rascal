package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

@SuppressWarnings("serial")
public class VELEMFactory {
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static IList emptyList = vf.list();
	
	enum Primitives {
		ALIGN, 
		BOX, 
		EDGE, 
		ELLIPSE, 
		GRAPH, 
		GRID,
		HORIZONTAL, 
		OVERLAY, 
		PACK, 
		PIE,
		SHAPE,
		TEXT, 
		TREE,
		USE,
		VERTEX,
		VERTICAL}
					  
    static HashMap<String,Primitives> pmap = new HashMap<String,Primitives>() {
    {
    	put("align",		Primitives.ALIGN);	
    	put("box",			Primitives.BOX);
    	put("edge",			Primitives.EDGE);
    	put("ellipse",		Primitives.ELLIPSE);
    	put("graph",		Primitives.GRAPH);
    	put("grid",			Primitives.GRID);
    	put("horizontal",	Primitives.HORIZONTAL);
    	put("overlay",		Primitives.OVERLAY);	
    	put("pack",			Primitives.PACK);	
    	put("pie",			Primitives.PIE);
    	put("shape",		Primitives.SHAPE);
    	put("text",			Primitives.TEXT);	    		
    	put("tree",			Primitives.TREE);
    	put("use",			Primitives.USE);
    	put("vertex",		Primitives.VERTEX);
    	put("vertical",		Primitives.VERTICAL);
    }};
    
    static IList props;
	static IList elems;
	
	private static void getOneOrTwoArgs(IConstructor c){
		if(c.arity() >= 2){
			props = (IList) c.get(0);
			elems = (IList) c.get(1);
		} else {
			props = emptyList;
			elems = (IList) c.get(0);
		}
	}
	public static VELEM make(VLPApplet vlp, IConstructor c, PropertyManager inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
		System.err.println("ename = " + ename);
	
		switch(pmap.get(ename)){
		
		case ALIGN: 
			getOneOrTwoArgs(c); 
			return new Align(vlp, inheritedProps, props, elems, ctx);
			
		case BOX:
			if(c.arity() == 2)
				return new Box(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			else
				return new Box(vlp, inheritedProps, (IList) c.get(0), null, ctx);
		
		case EDGE:
			if(c.arity() == 3)
				return new GraphEdge(null,vlp, inheritedProps, (IList) c.get(0), (IString)c.get(1), (IString)c.get(2), ctx);
			
			return new GraphEdge(null,vlp, inheritedProps, emptyList, (IString)c.get(0), (IString)c.get(1), ctx);
		
		case ELLIPSE:
			if(c.arity() == 2)
				return new Ellipse(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			else
				return new Ellipse(vlp, inheritedProps, (IList) c.get(0), null, ctx);
		
		case GRAPH: 
			if(c.arity() == 3)
				return new Graph(vlp,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), ctx);
			
			return new Graph(vlp,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case GRID: 
			getOneOrTwoArgs(c); 
			return new Grid(vlp, inheritedProps, props, elems, ctx);

		case HORIZONTAL:
			getOneOrTwoArgs(c);
			return new Horizontal(vlp, inheritedProps, props, elems, ctx);
			
		case OVERLAY: 
			getOneOrTwoArgs(c); 
			return new Overlay(vlp, inheritedProps, props, elems, ctx);
			
		case PACK: 
			getOneOrTwoArgs(c); 
			return new Pack(vlp, inheritedProps, props, elems, ctx);
			
		case PIE: 
			getOneOrTwoArgs(c); 
			return new Pie(vlp, inheritedProps, props, elems, ctx);
			
		case SHAPE: 
			getOneOrTwoArgs(c); 
			return new Shape(vlp, inheritedProps, props, elems, ctx);
			
		case TEXT:
			if(c.arity() == 1)
				return new Text(vlp, inheritedProps, emptyList, (IString) c.get(0), ctx);
			else
				return new Text(vlp, inheritedProps,  (IList) c.get(0), (IString) c.get(1), ctx);
			
		case TREE: 
			if(c.arity() == 3)
				return new Tree(vlp,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), ctx);
			
			return new Tree(vlp,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), ctx);
	
		case USE:
			if(c.arity() == 2)
				return new Use(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			else
				return new Use(vlp, inheritedProps, emptyList, (IConstructor) c.get(0), ctx);
			
		case VERTEX:
			if(c.arity() == 3)
				return new Vertex(vlp, (IInteger) c.get(0), (IInteger) c.get(1), (IConstructor) c.get(2), ctx);
			
			return new Vertex(vlp, (IInteger) c.get(0), (IInteger) c.get(1), ctx);
			
				
		case VERTICAL:
			getOneOrTwoArgs(c);
			return new Vertical(vlp, inheritedProps, props, elems, ctx);
									
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	public static GraphEdge makeGraphEdge(Graph G, VLPApplet vlp, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		if(c.arity() == 3)
			return new GraphEdge(G, vlp, properties, (IList) c.get(0), (IString)c.get(1), (IString)c.get(2), ctx);
		return new GraphEdge(G, vlp, properties, emptyList, (IString)c.get(0), (IString)c.get(1), ctx);
	}

}
