package org.meta_environment.rascal.library.experiments.VL;

public class BoundingBox {

	private float width;
	private float height;

	BoundingBox(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	@Override
	public String toString(){
		return "bbox(" + width + ", " + height + ")";
	}
}
