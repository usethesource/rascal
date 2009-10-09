package org.meta_environment.rascal.std.Processing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

import treemap.Mappable;
import treemap.SimpleMapItem;
import treemap.SimpleMapModel;
import treemap.Treemap;

public class TreeMap {
	
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final java.lang.String treemapCons = "treemap";
	
	static Treemap myTreemap;
	private static final HashMap<INode, Treemap> treemaps = new HashMap<INode, Treemap>();
	private static int tmCnt = 0;
	
	public static INode treemap(IMap m, IInteger x, IInteger y, IInteger width, IInteger height, 
			IValue draw, IEvaluatorContext ctx){

		Core.checkRascalFunction(draw, ctx);
		SimpleMapModel myMap = new RascalSimpleMapModel(m,  (OverloadedFunctionResult) draw, true);

		myTreemap = new Treemap(myMap, x.intValue(), y.intValue(), width.intValue(), height.intValue());
		IValue args[] = new IValue[1];
		args[0] = values.integer(tmCnt++);
		INode nd = values.node(treemapCons, args);
		treemaps.put(nd, myTreemap);
		
		return nd;
	}
	
	public static void treemap(IMap m, IReal x, IReal y, IReal width, IReal height, 
			                     IValue draw, IEvaluatorContext ctx){
		
		Core.checkRascalFunction(draw, ctx);
		SimpleMapModel myMap = new RascalSimpleMapModel(m, (OverloadedFunctionResult)  draw, false);
			
		myTreemap = new Treemap(myMap, x.floatValue(), y.floatValue(), width.floatValue(), height.floatValue());
	}
	
	private static Treemap getTreemap(INode PO, IEvaluatorContext ctx){
		if(!PO.getName().equals(treemapCons))
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), ctx.getStackTrace());
		Treemap tm = treemaps.get(PO);
		if(tm == null)
			throw RuntimeExceptionFactory.noSuchElement(PO, ctx.getCurrentAST(), ctx.getStackTrace());
		return tm;
	}
	
	public static void draw(INode PO, IEvaluatorContext ctx){
		Treemap tm = getTreemap(PO, ctx);
		tm.draw();
	}
	
}

class RascalSimpleMapModel extends SimpleMapModel {
	public RascalSimpleMapModel(IMap m, OverloadedFunctionResult drawItem, boolean intArgs){
		Mappable[] items = new Mappable[m.size()];
		
		Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
		int k = 0;
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			IValue key = entry.getKey();
			IValue val = entry.getValue();
		    items[k++] = new RascalSimpleMapItem((IString)key, (IInteger)val, drawItem, intArgs);
		}
		setItems(items);
	}
}

class RascalSimpleMapItem extends SimpleMapItem {
	
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();

	private static OverloadedFunctionResult myDraw;
	
	/*
	 * Represent argument types of:
	 *  	drawItem(int x, int y, int w, int h, str word)
	 * or
	 * 		drawItem(real x, real y, real w, real h, str word)
	 */
	
	private static Type[] intargtypes = new Type[]  {types.integerType(), types.integerType(), types.integerType(), types.integerType(), types.stringType()};
	private static Type[] realargtypes = new Type[] {types.realType(),    types.realType(),    types.realType(),    types.realType(),    types.stringType()};

	private static IValue[] argvals = new IValue[5];
	
	private IString key;
	private boolean intArgs;
	
	public RascalSimpleMapItem(IString key, IInteger val, OverloadedFunctionResult draw, boolean intArgs){
		this.key = key;
		setSize(val.intValue());
		myDraw = draw;
		this.intArgs = intArgs;
	}
	
	@Override
	public void draw(){
		// System.err.println("RascalSimpleMapItem.draw: about to call ... " + myDraw);
		if(intArgs){
			argvals[0] = values.integer(Math.round(x));
			argvals[1] = values.integer(Math.round(y));
			argvals[2] = values.integer(Math.round(w));
			argvals[3] = values.integer(Math.round(h));
			argvals[4] = key;
			
			myDraw.call(intargtypes, argvals);
			
		} else {
			argvals[0] = values.real(x);
			argvals[1] = values.real(y);
			argvals[2] = values.real(w);
			argvals[3] = values.real(h);
			argvals[4] = key;
			
			myDraw.call(realargtypes, argvals);
		}
	}
}


