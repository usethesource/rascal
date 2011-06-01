package org.rascalmpl.library.vis;

import org.rascalmpl.library.vis.properties.PropertyManager;

public class EmptyFigure extends Figure{

	public static final EmptyFigure instance = new EmptyFigure(); 
	
	private EmptyFigure(){
		super(null, new PropertyManager());
	}
	
	@Override
	public void bbox() {
		minSize.set(0,0);
		resizableX = resizableY = true;
	}

	@Override
	public void draw(double left, double top) {
		
	}

	@Override
	public void layout() {

		
	}

}
