package org.rascalmpl.library.vis.util;

public final class ForBothDimensions <T>{
	
	T forX, forY;
	
	public ForBothDimensions(T forX, T forY) {
		this.forX = forX;
		this.forY = forY;
	}
	
	public ForBothDimensions(T forX){
		this(forX,forX);
	}
	
	public T getForX(boolean flip){
		if(flip) return forY;
		else return forX;
	}
	
	public T getForY(boolean flip){
		if(flip) return forX;
		else return forY;
	}
	
	public T getForX(){
		return forX;
	}
	
	public T getForY(){
		return forY;
	}

	public void setForX(T forX){
		this.forX = forX;
	}
	
	public void setForY(T forY){
		this.forY = forY;
	}	
	
	public void setForX(boolean flip, T forX){
		if(flip) this.forY = forX;
		else this.forX = forX;
	}
}
