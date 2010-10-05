package org.rascalmpl.library.vis;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

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
	private static boolean debug = true;
	private boolean saveFigure = true;
	private String file;
	private float scale = 1.0f;
	private int left = 0;
	private int top = 0;

	private PGraphics canvas;

	private PFont stdFont;

	private int lastMouseX;

	private int lastMouseY;

	public FigurePApplet(IConstructor elem, ISourceLocation sloc, IEvaluatorContext ctx){
		registered = new HashMap<String,GraphNode>();
		saveFigure = true;
		try {
			this.file = ctx.getResolverRegistry().absolutePath(sloc.getURI());
			System.err.println("will save drawing to " + file);
			this.figure = FigureFactory.make(this, elem, null, ctx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FigurePApplet(IConstructor elem, IEvaluatorContext ctx){
		registered = new HashMap<String,GraphNode>();
		saveFigure = false;
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
		System.err.println("setup called");
		if(saveFigure)
			canvas = createGraphics(round(figure.width + 10), round(figure.height + 10), P2D);
		else
			size(width, height);
		noLoop();
		figure.bbox();
	}
	
	@Override
	public void draw(){
		stdFont = createFont("Helvetica", 12);
		if(saveFigure){
			canvas = createGraphics(round(figure.width + 10), round(figure.height + 10), P2D);
			canvas.beginDraw();
			canvas.background(255);
			canvas.textFont(stdFont);
			canvas.smooth();
			canvas.strokeJoin(MITER);
			figure.draw(5, 5);
			canvas.endDraw();
			canvas.save(file);
		} else {
			background(255);
			textFont(stdFont);
			smooth();
			strokeJoin(MITER);
			pushMatrix();
			scale(scale);
			figure.draw(left, top);
				
			if(focus != null){
				if(focusSelected)
					focus.drawFocus();
				else
					focus.drawMouseOverFigure();
			}	
			popMatrix();
		}
	}
	
	// ---------------------
	
	// Temporary switch for all Processing methods that we use:
	// write to canvas when saving and to parent (PApplet) otherwise.
	// This gives the best quality on the screen.
	// Replace by two version of FigurePApplet:
	// - FigurePApplet (current one)
	// - FigurePAppletSave (the saving version)
	
	@Override
	public void line(float arg0, float arg1, float arg2, float arg3) {
		if(saveFigure)
			canvas.line(arg0, arg1, arg2, arg3);
		else
			super.line(arg0, arg1, arg2, arg3);
	}
	
	@Override
	public void rect(float arg0, float arg1, float arg2, float arg3) {
		if(saveFigure)
			canvas.rect(arg0, arg1, arg2, arg3);
		else
			super.rect(arg0, arg1, arg2, arg3);
	}
	@Override
	public void ellipse(float arg0, float arg1, float arg2, float arg3) {
		if(saveFigure)
			canvas.ellipse(arg0, arg1, arg2, arg3);
		else
			super.ellipse(arg0, arg1, arg2, arg3);
	}
	
	@Override
	public void rectMode(int arg0) {
		if(saveFigure)
			canvas.rectMode(arg0);
		else
			super.rectMode(arg0);
	}
	
	@Override
	public void ellipseMode(int arg0) {
		if(saveFigure)
			canvas.ellipseMode(arg0);
		else
			super.ellipseMode(arg0);
	}
	
	@Override
	public void fill(int arg0) {
		if(saveFigure)
			canvas.fill(arg0);
		else
			super.fill(arg0);
	}
	
	
	@Override public void stroke(int arg0) { 
		if(saveFigure)
			canvas.stroke(arg0);
		else
			super.stroke(arg0);
	}
	
	@Override
	public void strokeWeight(float arg0) {
		if(saveFigure)
			canvas.strokeWeight(arg0);
		else
			super.strokeWeight(arg0);
	}
	
	@Override
	public void textSize(float arg0) {
		if(saveFigure)
			canvas.textSize(arg0);
		else
			super.textSize(arg0);
	}
	
	@Override
	public void textAlign(int arg0, int arg1) {
		if(saveFigure)
			canvas.textAlign(arg0, arg1);
		else
			super.textAlign(arg0, arg1);
	}
	
	@Override
	public void text(char arg0, float arg1, float arg2, float arg3) {
		if(saveFigure)
			canvas.text(arg0, arg1, arg2, arg3);
		else
			super.text(arg0, arg1, arg2, arg3);
	}
	
	@Override
	public void pushMatrix() {
		if(saveFigure)
			canvas.pushMatrix();
		else
			super.pushMatrix();
	}
	
	@Override
	public void popMatrix() {
		if(saveFigure)
			canvas.popMatrix();
		else
			super.popMatrix();
	}
	
	@Override
	public void rotate(float arg0) {
		if(saveFigure)
			canvas.rotate(arg0);
		else
			super.rotate(arg0);
	}
	
	@Override
	public void translate(float arg0, float arg1) {
		if(saveFigure)
			canvas.translate(arg0, arg1);
		else
			super.translate(arg0, arg1);
	}
	
	@Override
	public void scale(float arg0, float arg1) {
		if(saveFigure)
			canvas.scale(arg0, arg1);
		else
			super.scale(arg0, arg1);
	}
	
	@Override
	public void bezierVertex(float arg0, float arg1, float arg2, float arg3,
			float arg4, float arg5) {
		if(saveFigure)
			canvas.bezierVertex(arg0, arg1, arg2, arg3, arg4, arg5);
		else
			super.bezierVertex(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	
	@Override
	public void arc(float arg0, float arg1, float arg2, float arg3, float arg4,
			float arg5) {
		if(saveFigure)
			canvas.arc(arg0, arg1, arg2, arg3, arg4, arg5);
		else
			super.arc(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	
	@Override
	public void beginShape() {
		if(saveFigure)
			canvas.beginShape();
		else
			super.beginShape();
	}
	
	@Override
	public void endShape() {
		if(saveFigure)
			canvas.endShape();
		else
			super.beginShape();
	}
	
	//-----------------------
	
	private void moveBy(int dx, int dy){
		left += dx;
		top += dy;
		redraw();
	}
	
	@Override
	public void keyPressed(){
		if(key == CODED){
			if(keyCode == UP)
				moveBy(0, 20);
			if(keyCode == DOWN)
				moveBy(0, -20);
			if(keyCode == LEFT)
				moveBy(20, 0);
			if(keyCode == RIGHT)
				moveBy(-20, 0);
			return;
		} 
		if(key == '+' || key == '=')
			scale += 0.1;
		if(key == '-' || key == '_')
			scale -= 0.1;
		redraw();
	}
	
	@Override
	public void mouseReleased(){
		focusSelected = false;
	}
	
	@Override
	public void mouseDragged(){
		if(debug)System.err.println("mouseDragged: " + mouseX + ", " + mouseY);
		if(keyPressed && key == SHIFT){
			cursor(HAND);
			left += mouseX - lastMouseX;
			top += mouseY - lastMouseY;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		}
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
				focus.drag(mouseX/scale, mouseY/scale);
		else if(figure.mouseOver(round(mouseX/scale), round(mouseY/scale))){
			/* do nothing */
		} else
			unRegisterFocus();
		redraw();
	}
	
	@Override
	public void mousePressed(){
		if(debug)System.err.println("mousePressed: " + mouseX + ", " + mouseY);
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		if(figure.mousePressed(round(mouseX/scale), round(mouseY/scale)))
			focusSelected = true;
		else
			unRegisterFocus();
		redraw();
	}
}