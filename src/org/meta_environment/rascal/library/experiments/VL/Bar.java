package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.RascalFunction;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

import processing.core.PApplet;

public class Bar extends VELEM {
	int height;
	RascalFunction heightFun = null;
	
	int width;
	RascalFunction widthFun = null;

	public Bar(IList props, IEvaluatorContext ctx) {
		super(ctx);
		IList unknown = getProps(props);
		if(unknown.length() != 0)
			throw RuntimeExceptionFactory.noSuchElement(unknown, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	@Override
	protected IList getProps(IList props){

		IList myProps = super.getProps(props);

		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		IListWriter w = vf.listWriter(TypeFactory.getInstance().valueType());
		for(IValue v : myProps){
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			
			IValue arg = c.get(0);
			
			System.err.println("pname = " + pname + ", arg = " + arg);
			if(pname.equals("width")){
				if(arg instanceof RascalFunction)
					widthFun = (RascalFunction) arg;
				else
					width = ((IInteger) arg).intValue();
			} else if(pname.equals("height")){
				if(arg instanceof RascalFunction)
					heightFun = (RascalFunction) arg;
				else
					height = ((IInteger) arg).intValue();
			} else {
				w.append(v);
			}
		}
		return w.done();
	}

	protected int getHeight(int n){
		return getIntField(heightFun, n, height);
	}

	protected int getWidth(int n){
		return getIntField(widthFun, n, width);
	}

	@Override
	void draw(PApplet pa) {
		for(int index = 0; index < values.length; index++){
			//System.err.println("d = " + index);
			float x = getLeft(index);
			float h = getHeight(index);
			float y = pa.getHeight() - (h + getBottom(index));
			float w = getWidth(index);
			//pa.fill(getFillStyle(index));
			//pa.stroke(getStrokeStyle(d));
			//pa.strokeWeight(getLineWidth(d));
			pa.rect(x, y, w, h);
		}
	}
}