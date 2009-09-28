package org.meta_environment.rascal.std;

import java.util.EnumMap;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.std.Processing.callback;

import processing.core.PApplet;

public class RascalProcessingApplet extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2654582294257404948L;
	
	private OverloadedFunctionResult setup;
	private OverloadedFunctionResult draw;
	private OverloadedFunctionResult mouseClicked;
	private OverloadedFunctionResult mouseDragged;
	private OverloadedFunctionResult mouseMoved;
	private OverloadedFunctionResult mousePressed;
	private OverloadedFunctionResult mouseReleased;
	
	private static Type[] argtypes = new Type[] {};
	private static IValue[] argvals = new IValue[] {};
	
	RascalProcessingApplet(EnumMap<callback,OverloadedFunctionResult> callbacks){
		this.setup         = callbacks.get(callback.setup);
		this.draw          = callbacks.get(callback.draw);
		this.mouseClicked  = callbacks.get(callback.mouseClicked);
		this.mouseDragged  = callbacks.get(callback.mouseDragged);
		this.mouseMoved    = callbacks.get(callback.mouseMoved);
		this.mousePressed  = callbacks.get(callback.mousePressed);
		this.mouseReleased = callbacks.get(callback.mouseReleased);
	}
	
	@Override
	public void setup(){
		
		// System.err.println("RascalProcessingApplet.setup: about to call ... " + setup);

		setup.call(argtypes, argvals);
	}
	
	@Override
	public void draw(){

		// System.err.println("RascalProcessingApplet.draw: about to call draw ... " + draw);

		draw.call(argtypes, argvals);
	}
	
	@Override
	public void mouseClicked(){
		if(mouseClicked != null){
			// System.err.println("mouseClicked: about to call mouseClicked ... " + mouseClicked);
			mouseClicked.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseDragged(){
		if(mouseDragged != null){
			// System.err.println("mouseDragged: about to call mouseDragged ... " + mouseDragged);
			mouseDragged.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseMoved(){
		if(mouseMoved != null){
			// System.err.println("mouseMoved: about to call mouseMoved ... " + mouseMoved);
			mouseMoved.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mousePressed(){
		if(mousePressed != null){
			// System.err.println("mousePressed: about to call mousePressed ... " + mousePressed);
			mousePressed.call(argtypes, argvals);
		}
	}
	
	@Override
	public void mouseReleased(){
		if(mouseReleased != null){
			// System.err.println("mouseReleased: about to call mouseReleased ... " + mouseReleased);
			mouseReleased.call(argtypes, argvals);
		}
	}
	
}
