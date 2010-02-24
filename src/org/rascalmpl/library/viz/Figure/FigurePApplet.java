package org.rascalmpl.library.viz.Figure;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PApplet;

/**
 * 
 * FigurePApplet: wrapper that adapts Processing's PApplet to our needs.
 * 
 * @author paulk
 *
 */
public class FigurePApplet extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6074377218243765483L;
	
	private int width = 600;
	private int height = 600;
	private Figure  figure;
	private Figure mouseOver = null;
	private HashMap<String,GraphNode> registered;
	private static boolean debug = false;

	public FigurePApplet(IConstructor elem, IEvaluatorContext ctx){
		registered = new HashMap<String,GraphNode>();
		this.figure = FigureFactory.make(this, elem, null, ctx);
	}
	
	//TODO move these methods to Graph
	public void register(String name, GraphNode nd){
		registered.put(name, nd);
	}
	
	public GraphNode getRegistered(String name){
		return registered.get(name);
	}
	
	public void registerMouse(Figure v){
		mouseOver = v;
	}
	
	public void unRegisterMouse(){
		mouseOver = null;
	}
	
	public boolean isRegisteredAsMouseOver(Figure v){
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
		figure.bbox();
		figure.draw(0f, 0f);
		if(mouseOver != null)
			mouseOver.draw();
	}
	
	@Override
	public void mouseMoved(){
		if(debug)System.err.println("mouseMoved: " + mouseX + ", " + mouseY);
		figure.mouseOver(mouseX, mouseY);
		redraw();
	}
	
	@Override
	public void mousePressed(){
		if(debug)System.err.println("mousePressed: " + mouseX + ", " + mouseY);
		figure.mouseOver(mouseX, mouseY);
		redraw();
	}
}