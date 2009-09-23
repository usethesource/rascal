package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.ValueFactoryFactory;

import processing.core.PApplet;

public class Processing {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	//private static final TypeFactory types = TypeFactory.getInstance();
	//private static final Random random = new Random();
	
	static PApplet myPApplet = new PApplet();
	static boolean init = false;
	
	private static void doInit(){
		if(!init)
			myPApplet.init();
		init = true;
	}
	
	public static IInteger height(){
		doInit();
		return values.integer(myPApplet.height);
	}
	
	public static IInteger width(){
		doInit();
		return values.integer(myPApplet.width);
	}
	
	public static IInteger mouseX(){
		doInit();
		return values.integer(myPApplet.mouseX);
	}
	
	public static IInteger mouseY(){
		doInit();
		return values.integer(myPApplet.mouseY);
	}
	
	public static void size(IInteger x, IInteger y){
		doInit();
		try {
		 myPApplet.size(x.intValue(), y.intValue());
		} catch (Exception e) {
			System.err.println("size: caught exception: " + e.getMessage());
		}
	}
	
	public static void background(IInteger n){
		doInit();
		myPApplet.background(n.intValue());
	}
	
	public static void fill(IInteger gray){
		doInit();
		myPApplet.fill(gray.intValue());
	}
	
	public static void fill(IInteger gray, IInteger alpha){
		doInit();
		myPApplet.fill(gray.intValue(),alpha.intValue());
	}
	
	public static void fill(IInteger red, IInteger green, IInteger blue){
		doInit();
		myPApplet.fill(red.intValue(),green.intValue(), blue.intValue());
	}
	
	public static void noStroke(){
		doInit();
	    noStroke();
	}
	
	public static void line(IInteger bx, IInteger by, IInteger ex, IInteger ey){
		doInit();
		myPApplet.line(0,1,2,3);
	}
	
	public static void rect(IInteger x, IInteger y, IInteger w, IInteger h){
		doInit();
		myPApplet.rect(0,1,2,3);
	}
	
	public static void show(){
		myPApplet.show();
		
		//PApplet.main(new String[] { "--present", "MyProcessingSketch" });
	}
}