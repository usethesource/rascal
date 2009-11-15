package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public abstract class Compose extends VELEM {

	protected ArrayList<VELEM> velems;

	Compose(HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(inheritedProps, props, ctx);		
		velems = new ArrayList<VELEM>();
		for(IValue v : elems){
			IConstructor c = (IConstructor) v;
			System.err.println("Compose, elem = " + c.getName());
			velems.add(VELEMFactory.make(c, this.properties, ctx));
		}
	}
	
	@Override
	protected int getNumberOfValues(){
		int n = 0;
		for(VELEM v : velems)
			n = max(n, v.getNumberOfValues());
		return n;
	}
	
//	enum align {LEFT, CENTER, RIGHT, TOP, BOTTOM};
//	
//	@Override
//	protected void getProps(IList props){
//
//		super.getProps(props);
//
//		IValueFactory vf = ValueFactoryFactory.getValueFactory();
//		IListWriter w = vf.listWriter(TypeFactory.getInstance().valueType());
//		for(IValue v : myProps){
//			IConstructor c = (IConstructor) v;
//			String pname = c.getName();
//			
//			IValue arg = c.get(0);
//			
//			System.err.println("pname = " + pname + ", arg = " + arg);
//			if(pname.equals("gap")){
//				if(arg instanceof RascalFunction)
//					gapFun = (RascalFunction) arg;
//				else
//					gap = ((IInteger) arg).intValue();
//			} else if(pname.equals("align")){
//				if(arg instanceof RascalFunction)
//					alignFun = (RascalFunction) arg;
//				else
//					align = ((IInteger) arg).intValue();
//			} else {
//				w.append(v);
//			}
//		}
//		return w.done();
//	}
//	
//	@Override
//	protected int getGap(int n){
//		return getIntField(gapFun, n, gap);
//	}
//	
//	protected int getAlign(int n){
//		return getIntField(alignFun, n, align);
//	}

}
