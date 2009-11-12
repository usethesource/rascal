package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

import processing.core.PApplet;


public class VLPApplet extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074377218243765483L;
	
	private  IEvaluatorContext ctx;
	private int width = 200;
	private int height = 200;
	private ArrayList<VELEM> velems;

	VLPApplet(IConstructor panel, IEvaluatorContext ctx){
		this.ctx = ctx;
		IList props = (IList) panel.get(0);
		IList elms = (IList) panel.get(1);
		getProps(props);
		getElems(elms);
	}

	private void getProps(IList props){
		for(IValue v : props){
			IConstructor c = (IConstructor) v;
			String pname = c.getName();
			System.err.println("pname = " + pname);
			if(pname.equals("width")){
				width = ((IInteger) c.get(0)).intValue();
			} else if(pname.equals("height")){
				height = ((IInteger) c.get(0)).intValue();
			} else {
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			}
		}
	}

	private void getElems(IList elms){
		velems = new ArrayList<VELEM>();
		for(IValue v : elms){
			IConstructor c = (IConstructor) v;
			String ename = c.getName();
			System.err.println("ename = " + ename);
			if(ename.equals("bar")){
				IList props = (IList) c.get(0);
				VELEM ve = new Bar(props, ctx);
				velems.add(ve);
			} else {
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			}
		}
	}

	@Override
	public void setup(){
		size(width, height);
		//noLoop();
		
	}
	
	boolean hcomposition = true;
	int max = 10;
	int gap = 2;
	
	@Override
	public void draw(){
		int left = 0;
		int bottom = height;
		rectMode(CORNERS);
		for(int i = 0; i < max; i++){
			for(VELEM ve : velems){
				ve.draw(this, i, left, bottom);
				if(hcomposition)
					left += ve.hmove();
				else
					bottom += ve.vmove();
			}
		}

		

	}
/*
	@Override
	public void draw(){
		//stroke(255);
		//background(192, 64, 0);
		//rect(25, 0, 50, 80);
		//line(150, 25, mouseX, mouseY);
		for(VELEM ve : velems){
			ve.draw(this);
		}
	}
*/
}