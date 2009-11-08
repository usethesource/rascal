package org.meta_environment.rascal.library.experiments.Processing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

import treemap.Mappable;
import treemap.SimpleMapItem;
import treemap.SimpleMapModel;
import treemap.Treemap;

public class TreeMap {
	
	private static final java.lang.String treemapCons = "treemap";
	
	public static INode treemap(IString title, IMap m, IList itemCallbacks, IList groupCallbacks, IEvaluatorContext ctx){

		SimpleMapModel myMap = new RascalSimpleMapModel(m,  Core.getCallBacks(itemCallbacks), ctx);

		Treemap myTreemap = new Treemap(myMap, 0, 0, 600, 600);
		
		RascalPApplet myPApplet = new RascalTreemapPApplet(title.getValue(), myTreemap, Core.getCallBacks(groupCallbacks));
		SketchSWT mySketch = new SketchSWT(myPApplet);
		
		return Core.addSketch(treemapCons, mySketch, ctx);
	}
	
	public static void draw(IConstructor PO, IEvaluatorContext ctx){
		SketchSWT s = Core.getSketch(treemapCons, PO, ctx);
		s.getApplet().draw();
	}
}

class RascalSimpleMapModel extends SimpleMapModel {
	
	public RascalSimpleMapModel(IMap m, HashMap<String,OverloadedFunctionResult> callbacks, IEvaluatorContext ctx){
		Mappable[] items = new Mappable[m.size()];
		
		Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
		int k = 0;
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			IValue key = entry.getKey();
			IValue val = entry.getValue();
		    items[k++] = new RascalSimpleMapItem((IString)key, (IInteger)val, callbacks, ctx);
		}
		setItems(items);
	}
}

class RascalSimpleMapItem extends SimpleMapItem {
	
	private IString key;
	private OverloadedFunctionResult myDraw;
	private IEvaluatorContext ctx;
	protected static Type[] argtypes = new Type[] {};
	protected static IValue[] argvals = new IValue[] {};
	
	private TypeFactory types = TypeFactory.getInstance();
	private ValueFactory values = ValueFactory.getInstance();
	private Type intType = types.integerType();
	private Type strType = types.stringType();
	
	public RascalSimpleMapItem(IString key, IInteger val, HashMap<String,OverloadedFunctionResult> callbacks, IEvaluatorContext ctx){
		this.key = key;
		setSize(val.intValue());
		myDraw = callbacks.get("draw");
		this.ctx = ctx;
	}
	
	@Override
	public void draw(){
		System.err.println("RascalSimpleMapItem.draw: about to call(" + key + ") ... " + myDraw);
		if(myDraw != null){
			System.err.println("arguments: " + x + ", " + y + ", " + h + ", " + w);
			Environment env = myDraw.getEvaluatorContext().getCurrentEnvt();
			env.storeVariable("x", ResultFactory.makeResult(intType, values.integer(Math.round(x)), ctx));
			env.storeVariable("y", ResultFactory.makeResult(intType, values.integer(Math.round(y)), ctx));
			env.storeVariable("h", ResultFactory.makeResult(intType, values.integer(Math.round(h)), ctx));
			env.storeVariable("w", ResultFactory.makeResult(intType, values.integer(Math.round(w)), ctx));
			env.storeVariable("key", ResultFactory.makeResult(strType, key, ctx));
			myDraw.call(argtypes, argvals);
		}
	}

}

class RascalTreemapPApplet extends RascalPApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6880697390030412081L;
	private Treemap treemap;

	RascalTreemapPApplet(String title, Treemap tm, HashMap<String,OverloadedFunctionResult> callbacks){
		super(title, callbacks);
		this.treemap = tm;
	}
	
	@Override
	public void draw(){
		Core.myPApplet = this;
		System.err.println("RascalTreemapPApplet.draw: about to call ... treemap.draw");
		treemap.draw();
	}
}


