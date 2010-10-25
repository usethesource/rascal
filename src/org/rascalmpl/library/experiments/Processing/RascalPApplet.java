package org.rascalmpl.library.experiments.Processing;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;

import processing.core.PApplet;

public class RascalPApplet extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2654582294257404948L;
	
	private String title;
	private OverloadedFunctionResult setup;
	private OverloadedFunctionResult draw;
	private OverloadedFunctionResult mouseClicked;
	private OverloadedFunctionResult mouseDragged;
	private OverloadedFunctionResult mouseMoved;
	private OverloadedFunctionResult mousePressed;
	private OverloadedFunctionResult mouseReleased;
	
	protected static Type[] argtypes = new Type[] {};
	protected static IValue[] argvals = new IValue[] {};
	
	RascalPApplet(String title, HashMap<String,OverloadedFunctionResult> callbacks){
		this.title         = title;
		this.setup         = callbacks.get("setup");
		this.draw          = callbacks.get("draw");
		this.mouseClicked  = callbacks.get("mouseClicked");
		this.mouseDragged  = callbacks.get("mouseDragged");
		this.mouseMoved    = callbacks.get("mouseMoved");
		this.mousePressed  = callbacks.get("mousePressed");
		this.mouseReleased = callbacks.get("mouseReleased");
	}
	
	private void defaultSetup(){
		size(1024, 768);
		noLoop();
	}
	
	@Override
	public void setup(){
		if(setup == null)
			defaultSetup();
		else {
			// System.err.println("RascalProcessingApplet.setup: about to call ... " + setup);
			Core.myPApplet = this;
			setup.call(argtypes, argvals);
		}
	}
	
	@Override
	public void draw(){
		if(draw != null){
			// System.err.println("RascalProcessingApplet.draw: about to call draw ... " + draw);
			Core.myPApplet = this;
			draw.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseClicked(){
		if(mouseClicked != null){
			Core.myPApplet = this;
			System.err.println("mouseClicked: about to call mouseClicked ... " + mouseClicked);
			mouseClicked.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseDragged(){
		if(mouseDragged != null){
			Core.myPApplet = this;
			// System.err.println("mouseDragged: about to call mouseDragged ... " + mouseDragged);
			mouseDragged.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseMoved(){
		if(mouseMoved != null){
			Core.myPApplet = this;
			// System.err.println("mouseMoved: about to call mouseMoved ... " + mouseMoved);
			mouseMoved.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mousePressed(){
		if(mousePressed != null){
			Core.myPApplet = this;
			// System.err.println("mousePressed: about to call mousePressed ... " + mousePressed);
			mousePressed.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseReleased(){
		if(mouseReleased != null){
			Core.myPApplet = this;
			// System.err.println("mouseReleased: about to call mouseReleased ... " + mouseReleased);
			mouseReleased.call(argtypes, argvals);
		}
	}
	
}
