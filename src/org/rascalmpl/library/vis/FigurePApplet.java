package org.rascalmpl.library.vis;

import java.io.IOException;
import java.util.LinkedList;

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
	private LinkedList<Figure> zoomStack = null;

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

	private float rootWidth;
	private float rootHeight;
	
	private int depth = 0;

	public FigurePApplet(IConstructor elem, ISourceLocation sloc, IEvaluatorContext ctx){
		saveFigure = true;
		try {
			this.file = ctx.getResolverRegistry().absolutePath(sloc.getURI());
			this.figure = FigureFactory.make(this, elem, null, ctx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FigurePApplet(IConstructor elem, IEvaluatorContext ctx){
		saveFigure = false;
		this.figure = FigureFactory.make(this, elem, null, ctx);
		zoomStack = new LinkedList<Figure>();
		//zoomStack.push(figure);
	}

	@Override
	public void setup(){
		System.err.println("setup called");
		if(saveFigure){
			canvas = createGraphics(width, height, JAVA2D);
			figure.bbox();
			rootWidth = figure.width;
			rootHeight = figure.height;
			canvas = createGraphics(round(rootWidth + 2), round(rootHeight + 2), JAVA2D);
		} else {
			//width = round(max(width, figure.width + 10));
		    //height = round(max(height, figure.height + 10));
			size(width, height);
		}
		noLoop();
		figure.bbox();
		rootWidth = figure.width;
		rootHeight = figure.height;
	}
	
	@Override
	public void draw(){
		stdFont = createFont("Helvetica", 15);
//		System.err.printf("postscriptName %s\n", stdFont.getPostScriptName());
//		System.err.printf("Available fonts: ");
//		for(String s : stdFont.list()){
//			System.err.printf("%s ", s);
//		}
//		System.err.printf("\n");
		
		textFont(stdFont);
		if(saveFigure){
			canvas = createGraphics(round(rootWidth + 2), round(rootHeight + 2), JAVA2D);
			canvas.hint(ENABLE_NATIVE_FONTS);
			canvas.beginDraw();
			canvas.background(255);
			canvas.textFont(stdFont);
			canvas.textMode(MODEL);
			canvas.smooth();
			canvas.strokeJoin(MITER);
			figure.draw(1, 1);
			canvas.endDraw();
			canvas.save(file);
		} else {
			
			background(255);
			textFont(stdFont);
			smooth();
			strokeJoin(MITER);
			depth = 0;
			int deltah = 0;
			
			if(true){ // !zoomStack.isEmpty()){
				deltah = 20;
				fill(255);
				rect(left, top, left + rootWidth, top + deltah);
				textAlign(CENTER, CENTER);
				fill(0);
				text("Zoom level = " + zoomStack.size(), left + rootWidth/2, top + deltah/2);
			}
			pushMatrix();
			scale(scale);
			figure.draw(left, top + deltah/scale);
				
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
	public void textFont(PFont arg0){
		if(saveFigure)
			canvas.textFont(arg0);
			//return;
		else
			super.textFont(arg0);
	}
	
	@Override
	public void textFont(PFont arg0, float arg1){
		if(saveFigure)
			canvas.textFont(arg0, arg1);
			//return;
		else
			super.textFont(arg0, arg1);
	}
	@Override
	public float textWidth(String txt){
		if(saveFigure)
			return canvas.textWidth(txt);
		
		return super.textWidth(txt);
	}
	
	@Override
	public float textAscent(){
		if(saveFigure)
			return canvas.textAscent();
		
		return super.textAscent();
	}
	
	@Override
	public float textDescent(){
		if(saveFigure)
			return canvas.textDescent();
		
		return super.textDescent();
	}
	
	@Override
	public void text(String arg0, float arg1, float arg2) {
		if(saveFigure)
			canvas.text(arg0, arg1, arg2);
		else
			super.text(arg0, arg1, arg2);
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
	public void vertex(float arg0, float arg1){
		if(saveFigure)
			canvas.vertex(arg0, arg1);
		else
			super.vertex(arg0, arg1);
	}
	
	@Override
	public void curveVertex(float arg0, float arg1){
		if(saveFigure)
			canvas.curveVertex(arg0, arg1);
		else
			super.curveVertex(arg0, arg1);
	}
	
	@Override
	public void noFill(){
		if(saveFigure)
			canvas.noFill();
		else
			super.noFill();
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
	public void beginShape(int arg0) {
		if(saveFigure)
			canvas.beginShape(arg0);
		else
			super.beginShape(arg0);
	}
	
	@Override
	public void endShape() {
		if(saveFigure)
			canvas.endShape();
		else
			super.endShape();
	}
	
	
	@Override
	public void endShape(int arg0 ) {
		if(saveFigure)
			canvas.endShape(arg0);
		else
			super.endShape(arg0);
	}
	
	//-----------------------
	
	private void moveBy(int dx, int dy){
		left += dx;
		top += dy;
		redraw();
	}
	
	/*
	 * Interaction: handle mouse and key events.
	 */
	
	public void incDepth(){
		depth += 1;
	}
	
	public void decDepth(){
		depth -= 1;
	}
	
	public boolean isVisible(int d){
		return depth <= d;
	}
	
	public void registerFocus(Figure f){
		focus = f;
		System.err.println("registerFocus:" + f);
	}
	
	public void unRegisterFocus(){
		focus = null;
		focusSelected = false;
	}
	
	public boolean isRegisteredAsFocus(Figure f){
		return focus == f;
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
	
//	@Override
//	public void mouseDragged(){
//		
//		if(debug)System.err.println("mouseDragged: " + mouseX + ", " + mouseY);
//		if(keyPressed && key == SHIFT){
//			cursor(HAND);
//			left += mouseX - lastMouseX;
//			top += mouseY - lastMouseY;
//			lastMouseX = mouseX;
//			lastMouseY = mouseY;
//		}
//		if(focus != null){
//			if(debug) System.err.println("update current focus:" + focus);
//			focusSelected = true;
//			focus.drag(mouseX, mouseY);
//			
//		} else {
//			if(debug) System.err.println("searching for new focus");
//			if(figure.mouseDragged(mouseX, mouseY))
//				focusSelected = true;
//			else
//				unRegisterFocus();
//		}
//		redraw();
//		
//	}
	
	@Override
	public void mouseMoved(){
//		
//		if(debug)System.err.println("mouseMoved: " + mouseX + ", " + mouseY);
//		if(focus != null && focusSelected)
//				focus.drag(mouseX/scale, mouseY/scale);
//		else if(figure.mouseOver(round(mouseX/scale), round(mouseY/scale))){
//			/* do nothing */
//		} else
//			unRegisterFocus();
//		redraw();
	}
	
	@Override
	public void mousePressed(){
		if(debug)System.err.println("mousePressed: " + mouseX + ", " + mouseY);
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		if(figure.mousePressed(round(mouseX/scale), round(mouseY/scale))){
			focusSelected = true;
			if(keyPressed && key == CODED && keyCode == SHIFT){
				if(mouseButton == LEFT){
					if(debug)System.err.println("mousePressed: zoomin, focus=" + focus);
					if(focus != figure){
						zoomStack.push(figure);
						figure = focus;
						if(focus.width > focus.height)
							scale = rootWidth/focus.width;
						else
							scale = rootHeight/focus.height;
					}
				} else if(mouseButton == RIGHT){
					if(debug)System.err.println("mousePressed: zoomout");
					if(!zoomStack.isEmpty()){
						focus = figure = zoomStack.pop();
						if(focus.width > focus.height)
							scale = rootWidth/focus.width;
						else
							scale = rootHeight/focus.height;
					}
				}
			}
		} else
			unRegisterFocus();
		
		redraw();
	}
}