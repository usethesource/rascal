package org.rascalmpl.library.viz.Figure;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
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
		EDGE, 
		ELLIPSE, 
		GRAPH, 
		GRID,
		HCAT, 
		HVCAT,
		OUTLINE,
		OVERLAY, 
		PACK, 
		ROTATE,
		SCALE,
		SHAPE,
		SPACE,
		TEXT, 
		TREE,
		USE,
		VCAT,
		VERTEX,
		WEDGE
		}
					  
    static HashMap<String,Primitives> pmap = new HashMap<String,Primitives>() {
    {
    	put("box",			Primitives.BOX);
    	put("edge",			Primitives.EDGE);
    	put("ellipse",		Primitives.ELLIPSE);
    	put("graph",		Primitives.GRAPH);
    	put("grid",			Primitives.GRID);
    	put("hcat",			Primitives.HCAT);
    	put("hvcat",		Primitives.HVCAT);	
      	put("outline",		Primitives.OUTLINE);	
    	put("overlay",		Primitives.OVERLAY);	
    	put("pack",			Primitives.PACK);	
    	put("rotate",       Primitives.ROTATE);
    	put("scale",		Primitives.SCALE);
    	put("shape",		Primitives.SHAPE);
    	put("space",		Primitives.SPACE);
    	put("text",			Primitives.TEXT);	    		
    	put("tree",			Primitives.TREE);
    	put("use",			Primitives.USE);
    	put("vcat",			Primitives.VCAT);
    	put("vertex",		Primitives.VERTEX);
    	put("wedge",		Primitives.WEDGE);
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
	public static Figure make(FigurePApplet vlp, IConstructor c, PropertyManager inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
	
		switch(pmap.get(ename)){
			
		case BOX:
			if(c.arity() == 2)
				return new Box(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Box(vlp, inheritedProps, (IList) c.get(0), null, ctx);
		
		case EDGE:
			if(c.arity() == 3)
				return new GraphEdge(null,vlp, inheritedProps, (IList) c.get(0), (IString)c.get(1), (IString)c.get(2), ctx);
			
			return new GraphEdge(null,vlp, inheritedProps, emptyList, (IString)c.get(0), (IString)c.get(1), ctx);
		
		case ELLIPSE:
			if(c.arity() == 2)
				return new Ellipse(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Ellipse(vlp, inheritedProps, (IList) c.get(0), null, ctx);
		
		case GRAPH: 
			if(c.arity() == 3)
				return new Graph(vlp,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), ctx);
			
			return new Graph(vlp,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case GRID: 
			getOneOrTwoArgs(c); 
			return new Grid(vlp, inheritedProps, props, elems, ctx);

		case HCAT:
			getOneOrTwoArgs(c);
			return new HCat(vlp, inheritedProps, props, elems, ctx);
			
		case HVCAT: 
			getOneOrTwoArgs(c); 
			return new HVCat(vlp, inheritedProps, props, elems, ctx);
			
		case OUTLINE: 
			if(c.arity() == 2)
				return new Outline(vlp, inheritedProps, (IList) c.get(0), (IMap)c.get(1), ctx);
			return new Outline(vlp, inheritedProps, emptyList, (IMap)c.get(0), ctx);
			
		case OVERLAY: 
			getOneOrTwoArgs(c); 
			return new Overlay(vlp, inheritedProps, props, elems, ctx);
			
		case PACK: 
			getOneOrTwoArgs(c); 
			return new Pack(vlp, inheritedProps, props, elems, ctx);
			
		case ROTATE:
			return new Rotate(vlp, inheritedProps, c.get(0), (IConstructor) c.get(1), ctx);
			
		case SCALE:
			if(c.arity() == 2)
				return new Scale(vlp, inheritedProps, c.get(0), c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Scale(vlp, inheritedProps, c.get(0), c.get(1), (IConstructor) c.get(2), ctx);
		case SHAPE: 
			getOneOrTwoArgs(c); 
			return new Shape(vlp, inheritedProps, props, elems, ctx);
			
		case SPACE:
			if(c.arity() == 2)
				return new Space(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Space(vlp, inheritedProps, (IList) c.get(0), null, ctx);
			
		case TEXT:
			if(c.arity() == 1)
				return new Text(vlp, inheritedProps, emptyList, (IString) c.get(0), ctx);
			
			return new Text(vlp, inheritedProps,  (IList) c.get(0), (IString) c.get(1), ctx);
			
		case TREE: 
			if(c.arity() == 4)
				return new Tree(vlp,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), (IString) c.get(3), ctx);
			
			return new Tree(vlp,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), (IString) c.get(2), ctx);
	
		case USE:
			if(c.arity() == 2)
				return new Use(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Use(vlp, inheritedProps, emptyList, (IConstructor) c.get(0), ctx);
			
		case VCAT:
			getOneOrTwoArgs(c);
			return new VCat(vlp, inheritedProps, props, elems, ctx);
			
		case VERTEX:
			if(c.arity() == 3)
				return new Vertex(vlp, c.get(0), c.get(1), (IConstructor) c.get(2), ctx);
			
			return new Vertex(vlp, c.get(0), c.get(1), ctx);
			
		case WEDGE:
			if(c.arity() == 2)
				return new Wedge(vlp, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Wedge(vlp, inheritedProps, (IList) c.get(0), null, ctx);
									
		}
		throw RuntimeExceptionFactory.illegalArgument(c, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	public static GraphEdge makeGraphEdge(Graph G, FigurePApplet vlp, IConstructor c,
			PropertyManager properties, IEvaluatorContext ctx) {
		if(c.arity() == 3)
			return new GraphEdge(G, vlp, properties, (IList) c.get(0), (IString)c.get(1), (IString)c.get(2), ctx);
		return new GraphEdge(G, vlp, properties, emptyList, (IString)c.get(0), (IString)c.get(1), ctx);
	}

}
