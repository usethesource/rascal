package org.rascalmpl.library.vis.containers;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class EmptyFigure extends Figure{

	public static final EmptyFigure instance = new EmptyFigure(); 
	
	private EmptyFigure(){
		super(new PropertyManager());
	}
	
	@Override
	public void bbox() {
		minSize.set(0,0);
		resizableX = resizableY = true;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
	}

	@Override
	public void layout() {
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void suspend() {
		
	}

}
