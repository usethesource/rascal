package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.RascalFunction;

import processing.core.PApplet;

public class Bar extends VELEM {
	int height;
	RascalFunction heightFun = null;
	
	int width;
	RascalFunction widthFun = null;

	public Bar(IList props, IEvaluatorContext ctx) {
		super(ctx);
		//IList unknown = getProps(props);
		//if(unknown.length() != 0)
		//	throw RuntimeExceptionFactory.noSuchElement(unknown, ctx.getCurrentAST(), ctx.getStackTrace());
	}

	@Override
	BoundingBox draw(PApplet pa, int left, int bottom) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	protected IList getProps(IList props){
//
//		IList myProps = super.getProps(props);
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
//			if(pname.equals("width")){
//				if(arg instanceof RascalFunction)
//					widthFun = (RascalFunction) arg;
//				else
//					width = ((IInteger) arg).intValue();
//			} else if(pname.equals("height")){
//				if(arg instanceof RascalFunction)
//					heightFun = (RascalFunction) arg;
//				else
//					height = ((IInteger) arg).intValue();
//			} else {
//				w.append(v);
//			}
//		}
//		return w.done();
//	}
//
//	protected int getHeight(int n){
//		return getIntField(heightFun, n, height);
//	}
//
//	protected int getWidth(int n){
//		return getIntField(widthFun, n, width);
//	}
//	
//	@Override
//	BoundingBox draw(PApplet pa, int left, int bottom) {
//		int l = left;
//		int bbh = 0;
//		for(int index = 0; index < values.length; index++){
//			int gap = getGap(index);
//			
//			l = left + gap;
//			int h = getHeight(index);
//			int w = getWidth(index);
//			int b = bottom - getBottom(index);
//			
//			int right = left + gap + w;
//			int t = b - h;
//			
//			bbh = max(bbh, t - b);
//					
//			pa.fill(getFillStyle(index));
//			pa.stroke(getStrokeStyle(index));
//			pa.strokeWeight(getLineWidth(index));
//			pa.rectMode(pa.CORNERS);
//			pa.rect(l, t, right, b);
//		}
//		
//		return new BoundingBox(right - left, bbh);
//	}
//
//	@Override
//	BoundingBox draw(PApplet pa, int i, int left, int bottom) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
//
//	@Override
//	boolean draw(PApplet pa, int index, int left, int bottom) {
//		if(index < values.length){
//			System.err.println("index = " + index + ", left = " + left + ", bottom = " + bottom);
//			int gap = getGap(index);
//			
//			int l = left + gap;
//			int h = getHeight(index);
//			int w = getWidth(index);
//			int b = bottom - getBottom(index);
//			
//			int r = left + gap + w;
//			int t = b - h;
//			
//			System.err.println("index = " + index + ", l = " + l + ", t = " + t + ", r = " + r + ", b = " + b);
//		
//			pa.fill(getFillStyle(index));
//			pa.stroke(getStrokeStyle(index));
//			pa.strokeWeight(getLineWidth(index));
//			pa.rectMode(pa.CORNERS);
//			pa.rect(l, t, r, b);
//			return true;
//		} else {
//			return false;
//		}
//	}

//	@Override
//	void draw(PApplet pa) {
//		for(int index = 0; index < values.length; index++){
//			//System.err.println("d = " + index);
//			float x = getLeft(index);
//			float h = getHeight(index);
//			float y = pa.getHeight() - (h + getBottom(index));
//			float w = getWidth(index);
//			pa.fill(getFillStyle(index));
//			pa.stroke(getStrokeStyle(index));
//			pa.strokeWeight(getLineWidth(index));
//			pa.rect(x, y, w, h);
//		}
//	}
}