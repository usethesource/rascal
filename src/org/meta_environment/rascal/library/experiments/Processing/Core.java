package org.meta_environment.rascal.library.experiments.Processing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.fast.Constructor;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

import processing.core.PApplet;
import processing.core.PFont;

public class Core {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	static PApplet myPApplet;
	//static RascalFrame myFrame;
	
	public static IInteger getX(){
		return values.integer(myPApplet.getX());
	}
	
	public static IInteger getY(){
		return values.integer(myPApplet.getY());
	}
	
	public static IInteger height(){
		return values.integer(myPApplet.height);
	}
	
	public static IInteger width(){
		return values.integer(myPApplet.width);
	}
	
	public static void size(IInteger x, IInteger y){
		 myPApplet.size(x.intValue(), y.intValue());
	}
	
	// background

	public static void background(IInteger rgb){
		myPApplet.background(rgb.intValue());
	}
	
	public static void background(IInteger rgb, IReal alpha){
		myPApplet.background(rgb.intValue(), alpha.floatValue());
	}
	public static void background(IReal grey){
		myPApplet.background(grey.floatValue());
	}
	
	public static void background(IReal gray, IReal alpha){
		myPApplet.background(gray.floatValue(),alpha.floatValue());
	}
	
	public static void background(IInteger red, IInteger green, IInteger blue){
		myPApplet.background(red.intValue(),green.intValue(), blue.intValue());
	}
	
	public static void background(IReal red, IReal green, IReal blue){
		myPApplet.background(red.floatValue(),green.floatValue(), blue.floatValue());
	}
	
	public static void background(IInteger red, IInteger green, IInteger blue, IReal alpha){
		myPApplet.background(red.intValue(),green.intValue(), blue.intValue(), alpha.floatValue());
	}
	
	public static void background(IReal red, IReal green, IReal blue, IReal alpha){
		myPApplet.background(red.floatValue(),green.floatValue(), blue.floatValue(), alpha.floatValue());
	}
	
	// fill
	
	public static void fill(IInteger rgb){
		myPApplet.fill(rgb.intValue());
	}
	
	public static void fill(IInteger rgb, IReal alpha){
		myPApplet.fill(rgb.intValue(), alpha.floatValue());
	}
	
	public static void fill(IReal grey){
		myPApplet.fill(grey.floatValue());
	}
	
	public static void fill(IReal gray, IReal alpha){
		myPApplet.fill(gray.floatValue(),alpha.floatValue());
	}
	
	public static void fill(IReal red, IReal green, IReal blue){
		myPApplet.fill(red.floatValue(),green.floatValue(), blue.floatValue());
	}
	
	public static void fill(IReal red, IReal green, IReal blue, IReal alpha){
		myPApplet.fill(red.floatValue(),green.floatValue(), blue.floatValue());
	}
	
	// ---- noFill ----
	
	public static void noFill(){
		noFill();
	}
	
	// ---- stroke ----
	
	public static void stroke(IInteger rgb){
		myPApplet.stroke(rgb.intValue());
	}
	
	public static void stroke(IInteger rgb, IReal alpha){
		myPApplet.stroke(rgb.intValue(), alpha.floatValue());
	}
	
	public static void stroke(IReal grey){
		myPApplet.stroke(grey.floatValue());
	}
	
	public static void stroke(IReal gray, IReal alpha){
		myPApplet.stroke(gray.floatValue(),alpha.floatValue());
	}
	
	public static void stroke(IReal red, IReal green, IReal blue){
		myPApplet.stroke(red.floatValue(),green.floatValue(), blue.floatValue());
	}
	
	public static void stroke(IReal red, IReal green, IReal blue, IReal alpha){
		myPApplet.stroke(red.floatValue(),green.floatValue(), blue.floatValue());
	}
	
	// ---- noStroke ----
	
	public static void noStroke(){
	    myPApplet.noStroke();
	}
	
	/*
	 * 2D primitives
	 */
	 
	// ---- arc ----
	
	public static void arc(IInteger x, IInteger y, IInteger width, IInteger height, IInteger start, IInteger stop){
	    myPApplet.arc(x.intValue(), y.intValue(), width.intValue(), height.intValue(), start.intValue(), stop.intValue());
	}
	
	public static void arc(IReal x, IReal y, IReal width, IReal height, IReal start, IReal stop){
	    myPApplet.arc(x.floatValue(), y.floatValue(), width.floatValue(), height.floatValue(), start.floatValue(), stop.floatValue());
	}

	// ---- ellipse

	public static void ellipse(IInteger x, IInteger y, IInteger width, IInteger height){
	    myPApplet.ellipse(x.intValue(), y.intValue(), width.intValue(), height.intValue());
	}
	
	public static void ellipse(IReal x, IReal y, IReal width, IReal height){
	    myPApplet.ellipse(x.floatValue(), y.floatValue(), width.floatValue(), height.floatValue());
	}
	
	// ---- line
	
	public static void line(IInteger x1, IInteger y1, IInteger x2, IInteger y2){
		myPApplet.line(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
	}
	
	public static void line(IReal x1, IReal y1, IReal x2, IReal y2){
		myPApplet.line(x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue());
	}
	
	public static void line(IInteger x1, IInteger y1, IInteger z1, IInteger x2, IInteger y2, IInteger z2){
		myPApplet.line(x1.intValue(), y1.intValue(), z1.intValue(), x2.intValue(), y2.intValue(), y2.intValue());
	}
	
	public static void line(IReal x1, IReal y1, IReal z1, IReal x2, IReal y2, IReal z2){
		myPApplet.line(x1.floatValue(), y1.floatValue(), z1.floatValue(), x2.floatValue(), y2.floatValue(), y2.floatValue());
	}
	
	// ---- Point ----

	public static void point(IInteger x, IInteger y){
		myPApplet.point(x.intValue(), y.intValue());
	}

	public static void point(IReal x, IReal y){
		myPApplet.point(x.floatValue(), y.floatValue());
	}

	public static void point(IInteger x, IInteger y, IInteger z){
		myPApplet.point(x.intValue(), y.intValue(), z.intValue());
	}

	public static void point(IReal x, IReal y, IReal z){
		myPApplet.point(x.floatValue(), y.floatValue(), z.floatValue());
	}
	
	// ---- Quad ----
	
	public static void quad(IInteger x1, IInteger y1, IInteger x2, IInteger y2, IInteger x3, IInteger y3, IInteger x4, IInteger y4){
		myPApplet.quad(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue(), x3.intValue(), y3.intValue(), x4.intValue(), y4.intValue());
	}
	
	public static void quad(IReal x1, IReal y1, IReal x2, IReal y2, IReal x3, IReal y3, IReal x4, IReal y4){
		myPApplet.quad(x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue(), x3.floatValue(), y3.floatValue(), x4.floatValue(), y4.floatValue());
	}
	
	// triangle
	
	public static void triangle(IInteger x1, IInteger y1, IInteger x2, IInteger y2, IInteger x3, IInteger y3){
		myPApplet.triangle(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue(), x3.intValue(), y3.intValue());
	}
	
	public static void triangle(IReal x1, IReal y1, IReal x2, IReal y2, IReal x3, IReal y3){
		myPApplet.triangle(x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue(), x3.floatValue(), y3.floatValue());
	}
	
	// ---- rectangle 
	
	public static void rect(IInteger x, IInteger y, IInteger w, IInteger h){
		myPApplet.rect(x.intValue(), y.intValue(), w.intValue(), h.intValue());
	}
	
	public static void rect(IReal x, IReal y, IReal w, IReal h){
		myPApplet.rect(x.floatValue(), y.floatValue(), w.floatValue(), h.floatValue());
	}
	
	/*
	 * Curve primitives
	 */
	
	/* 
	 * 3D primitives
	 */
	
	/*
	 * Attributes
	 */
	
	public static void ellipseMode(IInteger mode){
		myPApplet.ellipseMode(mode.intValue());
	}
	
	public static void noSmooth(){
		myPApplet.noSmooth();
	}
	
	public static void rectMode(IInteger mode){
		myPApplet.rectMode(mode.intValue());
	}
	
	public static void smooth(){
		myPApplet.smooth();
	}
	
	public static void strokeCap(IInteger cap){
		myPApplet.strokeCap(cap.intValue());
	}
	
	public static void strokeJoin(IInteger join){
		myPApplet.strokeJoin(join.intValue());
	}
	
	public static void strokeWeight(IReal weight){
		myPApplet.strokeWeight(weight.floatValue());
	}
	
	
	// text
	
	public static void textAlign(IInteger align){
		myPApplet.textAlign(align.intValue());
	}
	
	public static void textAlign(IInteger alignX, IInteger alignY){
		myPApplet.textAlign(alignX.intValue(), alignY.intValue());
	}
	
	public static IReal textAscent(){
		return values.real(myPApplet.textAscent());
	}
	
	public static IReal textDescent(){
		return values.real(myPApplet.textAscent());
	}
	
	public static void text(IString s){
		myPApplet.text(s.getValue());
	}
	
	public static void text(IString s, IInteger x, IInteger y){
		myPApplet.text(s.getValue(), x.intValue(), y.intValue());
	}
	
	public static void text(IString s, IReal x, IReal y){
		myPApplet.text(s.getValue(), x.floatValue(), y.floatValue());
	}
	
	public static void text(IString s, IInteger x, IInteger y, IInteger z){
		myPApplet.text(s.getValue(), x.intValue(), y.intValue(), z.intValue());
	}
	
	public static void text(IString s, IReal x, IReal y, IReal z){
		myPApplet.text(s.getValue(), x.floatValue(), y.floatValue(), z.floatValue());
	}
	
	public static void text(IString s, IInteger x1, IInteger y1, IInteger x2, IInteger y2){
		myPApplet.text(s.getValue(), x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
	}
	
	public static void text(IString s, IReal x1, IReal y1, IReal x2, IReal y2){
		myPApplet.text(s.getValue(), x1.floatValue(), y1.floatValue(), x2.floatValue(), y2.floatValue());
	}
	
	// ----
	
	public static void textSize(IInteger size){
		myPApplet.textSize(size.intValue());
	}
	
	public static void textSize(IReal size){
		myPApplet.textSize(size.floatValue());
	}

	public static IInteger textWidth(IString s){
		int w = Math.round(myPApplet.textWidth(s.getValue()));
		return values.integer(w);
	}

	public static void textMode(IInteger mode){
		myPApplet.textMode(mode.intValue());
	}
	
	// ----
	
	private static LinkedList<PFont> fonts = new LinkedList<PFont>();
	
	public static IInteger createFont(IString name, IInteger size){
		fonts.add(myPApplet.createFont(name.getValue(), size.intValue()));
		return values.integer(fonts.size() - 1);
	}
	
	public static IInteger createFont(IString name, IReal size){
		fonts.add(myPApplet.createFont(name.getValue(), size.floatValue()));
		return values.integer(fonts.size() - 1);
	}
	
	public static IInteger createFont(IString name, IInteger size, IBool smooth){
		fonts.add(myPApplet.createFont(name.getValue(), size.intValue(), smooth.getValue()));
		return values.integer(fonts.size() - 1);
	}

	public static IInteger createFont(IString name, IReal size, IBool smooth){
		fonts.add(myPApplet.createFont(name.getValue(), size.floatValue(), smooth.getValue()));
		return values.integer(fonts.size() - 1);
	}

	public static void textFont(IInteger font, IEvaluatorContext ctx){
		int n = font.intValue();
		if(n >= 0 && n < fonts.size()){
			myPApplet.textFont(fonts.get(n));
		} else
			RuntimeExceptionFactory.illegalArgument(font, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	/*
	 * Input
	 */
	/*
	 * Mouse
	 */
	
	public static IInteger mouseButton(){
		return  values.integer(myPApplet.mouseButton);
	}
	
	public static IBool mousePressed(){
		return  values.bool(myPApplet.mousePressed);
	}
	
	public static IInteger mouseX(){
		return values.integer(myPApplet.mouseX);
	}
	
	public static IInteger mouseY(){
		return values.integer(myPApplet.mouseY);
	}
	
	public static IInteger pmouseX(){
		return values.integer(myPApplet.pmouseX);
	}
	
	public static IInteger pmouseY(){
		return values.integer(myPApplet.pmouseY);
	}
	
	// -- Triogonometry
	
	public static IReal radians(IInteger angle){
		return values.real(PApplet.radians(angle.intValue()));
	}
	
	public static IReal radians(IReal angle){
		return values.real(PApplet.radians(angle.floatValue()));
	}
	
	// and many more ...
	
	// ----- start/stop Processing visualization
	
	protected static void checkRascalFunction(IValue f, IEvaluatorContext ctx){
		if(f.getType().isExternalType() && (f instanceof OverloadedFunctionResult))
			return;
		throw  RuntimeExceptionFactory.illegalArgument(f, ctx.getCurrentAST(), ctx.getStackTrace());
		
	}
	
	public static HashSet<String>  callbackNames = new HashSet<String>();
	
	public static void registerCallbackName(String name){
		if(!callbackNames.contains(name)){
			callbackNames.add(name);
		}
	}
	
	private static boolean isCallbackName(String name){
		return callbackNames.contains(name);
	}

	private static HashMap<INode, SketchSWT> sketches = new HashMap<INode, SketchSWT>();
	private static java.lang.String sketchCons = "sketch";
	private static int sketchCnt = 0;
	
	public static HashMap<String,OverloadedFunctionResult> getCallBacks(IList V){
		HashMap<String,OverloadedFunctionResult> callbacks = new HashMap<String,OverloadedFunctionResult>();
		registerCallbackName("setup");
		registerCallbackName("draw");
		registerCallbackName("mouseClicked");
		registerCallbackName("mouseDragged");
		registerCallbackName("mouseMoved");
		registerCallbackName("mousePressed");
		registerCallbackName("mouseReleased");

		Iterator<IValue> valueIterator = V.iterator();
		while(valueIterator.hasNext()){
			Constructor cons = (Constructor) valueIterator.next();
			String cname = cons.getName();
			System.err.println("cons = " + cons);
			OverloadedFunctionResult fn = (OverloadedFunctionResult) cons.get(0);

			if(isCallbackName(cname)) {
				callbacks.put(cname, fn);
			} else {
				System.err.println("TODO: add exception");
			}

		}
		return callbacks;
	}
	
	public static INode sketch(IString title, IList V, IEvaluatorContext ctx){
		System.err.println("entering Sketch ...");
		
		myPApplet = new RascalPApplet(title.getValue(), getCallBacks(V));
		SketchSWT mySketch = new SketchSWT(myPApplet);
		
		return addSketch(sketchCons, mySketch, ctx);
	}
	
	public static void draw(INode PO, IEvaluatorContext ctx){
		SketchSWT s = getSketch(sketchCons, PO, ctx);
		s.getApplet().draw();
	}
	
	protected static INode addSketch(String type, SketchSWT sketch, IEvaluatorContext ctx){
		IValue args[] = new IValue[1];
		args[0] = values.integer(sketchCnt++);
		INode nd = values.node(type, args);
		sketches.put(nd, sketch);
		return nd;
	}
	
	static SketchSWT getSketch(String type, INode PO, IEvaluatorContext ctx){
		if(!PO.getName().equals(type))
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), ctx.getStackTrace());
		SketchSWT sketch = sketches.get(PO);
		if(sketch == null)
			throw RuntimeExceptionFactory.noSuchElement(PO, ctx.getCurrentAST(), ctx.getStackTrace());
		return sketch;
	}
	
	public static void stop(INode PO, IEvaluatorContext ctx){
		SketchSWT sketch = getSketch(sketchCons, PO, ctx);
		//sketch.dispose();
		sketches.remove(PO);
	}
	
	public static void noLoop(){
		myPApplet.noLoop();
	}
}

class RascalFrameAWT extends Frame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3251377159535885219L;

	RascalFrameAWT (PApplet pa){
		super("Rascal Visualization");
		setLayout(new BorderLayout());
		add(pa, BorderLayout.CENTER);
		pa.init();
		pack();
		setLocation(100,100);
		setVisible(true);
	}
}

