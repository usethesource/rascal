package org.meta_environment.rascal.library.experiments.VL;

public class BoundingBox {

	private int width;
	private int height;

	BoundingBox(int width, int height){
		this.width = width;
		this.height = height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	@Override
	public String toString(){
		return "bbox(" + width + ", " + height + ")";
	}
}
