package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class VLPApplet extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074377218243765483L;
	
	private int width = 600;
	private int height = 600;
	private VELEM  velem;
	private VELEM mouseOver = null;
	private HashMap<String,GraphNode> registered;

	public VLPApplet(IConstructor elem, IEvaluatorContext ctx){
		registered = new HashMap<String,GraphNode>();
		this.velem = VELEMFactory.make(this, elem, null, ctx);
	}
	
	public void register(String name, GraphNode nd){
		registered.put(name, nd);
	}
	
	public GraphNode getRegistered(String name){
		return registered.get(name);
	}
	
	public float textHeight(int fontSize){
		textSize(fontSize);
		return 0.3f * (textAscent() + textDescent());
	}
	
	public void registerMouse(VELEM v){
		mouseOver = v;
	}
	
	public boolean isRegisteredMouse(VELEM v){
		return mouseOver == v;
	}

	@Override
	public void setup(){
		size(width, height);
		textFont(createFont("Helvetica", 12));
		smooth();
		noLoop();
		//if(!velem.hasInteraction())
		//	noLoop();
	}
	
	@Override
	public void draw(){
		background(255);
		velem.bbox();
		velem.draw(0, 0);
	}
	
	@Override
	public void mouseMoved(){
		System.err.println("mouseMoved: " + mouseX + ", " + mouseY);
		if(mouseOver != null){
			mouseOver.properties.setMouseOver(false);
			mouseOver = null;
		}
		velem.mouseOver(mouseX, mouseY);
		redraw();
	}
	
	@Override
	public void mousePressed(){
		//System.err.println("mousePressed: " + mouseX + ", " + mouseY);
		velem.mouseOver(mouseX, mouseY);
		redraw();
	}
}