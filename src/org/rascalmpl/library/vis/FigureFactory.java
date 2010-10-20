package org.rascalmpl.library.vis;

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
		TREEMAP,
		USE,
		VCAT,
		VERTEX,
		WEDGE
		}
					  
    static HashMap<String,Primitives> pmap = new HashMap<String,Primitives>() {
    {
    	put("Fbox",			Primitives.BOX);
    	put("Fedge",			Primitives.EDGE);
    	put("Fellipse",		Primitives.ELLIPSE);
    	put("Fgraph",		Primitives.GRAPH);
    	put("Fgrid",			Primitives.GRID);
    	put("Fhcat",			Primitives.HCAT);
    	put("Fhvcat",		Primitives.HVCAT);	
      	put("Foutline",		Primitives.OUTLINE);	
    	put("Foverlay",		Primitives.OVERLAY);	
    	put("Fpack",			Primitives.PACK);	
    	put("rotate",       Primitives.ROTATE);
    	put("scale",		Primitives.SCALE);
    	put("Fshape",		Primitives.SHAPE);
    	put("Fspace",		Primitives.SPACE);
    	put("Ftext",			Primitives.TEXT);	    		
    	put("Ftree",			Primitives.TREE);
       	put("Ftreemap",		Primitives.TREEMAP);
    	put("Fuse",			Primitives.USE);
    	put("Fvcat",			Primitives.VCAT);
    	put("vertex",		Primitives.VERTEX);
    	put("Fwedge",		Primitives.WEDGE);
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
	public static Figure make(FigurePApplet fpa, IConstructor c, PropertyManager inheritedProps, IEvaluatorContext ctx){
		String ename = c.getName();
	
		switch(pmap.get(ename)){
			
		case BOX:
			if(c.arity() == 2)
				return new Box(fpa, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Box(fpa, inheritedProps, (IList) c.get(0), null, ctx);
		
		case EDGE:
			if(c.arity() == 3)
				return new GraphEdge(null,fpa, inheritedProps, (IList) c.get(0), (IString)c.get(1), (IString)c.get(2), ctx);
			
			return new GraphEdge(null,fpa, inheritedProps, emptyList, (IString)c.get(0), (IString)c.get(1), ctx);
		
		case ELLIPSE:
			if(c.arity() == 2)
				return new Ellipse(fpa, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Ellipse(fpa, inheritedProps, (IList) c.get(0), null, ctx);
		
		case GRAPH: 
			if(c.arity() == 3)
				return new Graph(fpa,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), ctx);
			
			return new Graph(fpa,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), ctx);
			
		case GRID: 
			getOneOrTwoArgs(c); 
			return new Grid(fpa, inheritedProps, props, elems, ctx);

		case HCAT:
			getOneOrTwoArgs(c);
			return new HCat(fpa, inheritedProps, props, elems, ctx);
			
		case HVCAT: 
			getOneOrTwoArgs(c); 
			return new HVCat(fpa, inheritedProps, props, elems, ctx);
			
		case OUTLINE: 
			if(c.arity() == 2)
				return new Outline(fpa, inheritedProps, (IList) c.get(0), (IMap)c.get(1), ctx);
			return new Outline(fpa, inheritedProps, emptyList, (IMap)c.get(0), ctx);
			
		case OVERLAY: 
			getOneOrTwoArgs(c); 
			return new Overlay(fpa, inheritedProps, props, elems, ctx);
			
		case PACK: 
			getOneOrTwoArgs(c); 
			return new Pack(fpa, inheritedProps, props, elems, ctx);
			
		case ROTATE:
			return new Rotate(fpa, inheritedProps, c.get(0), (IConstructor) c.get(1), ctx);
			
		case SCALE:
			if(c.arity() == 2)
				return new Scale(fpa, inheritedProps, c.get(0), c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Scale(fpa, inheritedProps, c.get(0), c.get(1), (IConstructor) c.get(2), ctx);
		case SHAPE: 
			getOneOrTwoArgs(c); 
			return new Shape(fpa, inheritedProps, props, elems, ctx);
			
		case SPACE:
			if(c.arity() == 2)
				return new Space(fpa, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Space(fpa, inheritedProps, (IList) c.get(0), null, ctx);
			
		case TEXT:
			if(c.arity() == 1)
				return new Text(fpa, inheritedProps, emptyList, (IString) c.get(0), ctx);
			
			return new Text(fpa, inheritedProps,  (IList) c.get(0), (IString) c.get(1), ctx);
			
		case TREE: 
			if(c.arity() == 3)
				return new Tree(fpa,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), ctx);
			
			return new Tree(fpa,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), ctx);

		case TREEMAP: 
			if(c.arity() == 3)
				return new TreeMap(fpa,inheritedProps, (IList) c.get(0), (IList) c.get(1), (IList)c.get(2), ctx);
			
			return new TreeMap(fpa,inheritedProps, emptyList, (IList) c.get(0), (IList)c.get(1), ctx);

			
		case USE:
			if(c.arity() == 2)
				return new Use(fpa, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Use(fpa, inheritedProps, emptyList, (IConstructor) c.get(0), ctx);
			
		case VCAT:
			getOneOrTwoArgs(c);
			return new VCat(fpa, inheritedProps, props, elems, ctx);
			
		case VERTEX:
			if(c.arity() == 3)
				return new Vertex(fpa, c.get(0), c.get(1), (IConstructor) c.get(2), ctx);
			
			return new Vertex(fpa, c.get(0), c.get(1), ctx);
			
		case WEDGE:
			if(c.arity() == 2)
				return new Wedge(fpa, inheritedProps, (IList) c.get(0), (IConstructor) c.get(1), ctx);
			
			return new Wedge(fpa, inheritedProps, (IList) c.get(0), null, ctx);
									
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
