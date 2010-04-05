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
	
	private int width = 1000;
	private int height = 1000;
	private Figure  figure;
	private Figure focus = null;
	private boolean focusSelected = false;
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
	
	public void registerFocus(Figure v){
		focus = v;
	}
	
	public void unRegisterFocus(){
		focus = null;
		focusSelected = false;
	}
	
	public boolean isRegisteredAsFocus(Figure v){
		return focus == v;
	}

	@Override
	public void setup(){
		//size(width, height, PDF, "tmp.pdf");
		size(width, height);
		textFont(createFont("Helvetica", 12));
		smooth();
		noLoop();
		figure.bbox();
		//if(!velem.hasInteraction())
		//	noLoop();
	}
	
	@Override
	public void draw(){
		background(255);
	//	figure.bbox();
		figure.draw();
		if(focus != null){
			if(focusSelected)
				focus.drawFocus();
			else
				focus.drawMouseOverFigure();
	//		focus.draw();
		}
	}
	
	@Override
	public void mouseReleased(){
		focusSelected = false;
	}
	
	@Override
	public void mouseDragged(){
		if(debug)System.err.println("mouseDragged: " + mouseX + ", " + mouseY);
		if(focus != null){
			if(debug) System.err.println("update current focus:" + focus);
			focusSelected = true;
			focus.drag(mouseX, mouseY);
			
		} else {
			if(debug) System.err.println("searching for new focus");
			if(figure.mouseDragged(mouseX, mouseY))
				focusSelected = true;
			else
				unRegisterFocus();
		}
		redraw();
	}
	
	@Override
	public void mouseMoved(){
		if(debug)System.err.println("mouseMoved: " + mouseX + ", " + mouseY);
		if(focus != null && focusSelected)
				focus.drag(mouseX,mouseY);
		else if(figure.mouseInside(mouseX, mouseY)){
			/* do nothing */
		} else
			unRegisterFocus();
		redraw();
	}
	
	@Override
	public void mousePressed(){
		if(debug)System.err.println("mousePressed: " + mouseX + ", " + mouseY);
		if(figure.mousePressed(mouseX, mouseY))
			focusSelected = true;
		else
			unRegisterFocus();
		redraw();
	}
}