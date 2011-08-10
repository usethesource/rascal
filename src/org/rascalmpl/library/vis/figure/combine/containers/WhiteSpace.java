package org.rascalmpl.library.vis.figure.combine.containers;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.FigureColorUtils;

public class WhiteSpace extends Container{

	public WhiteSpace(Figure inner, PropertyManager properties) {
		super(inner, properties);
	}

	@Override
	void drawContainer(GraphicsContext gc) {
		gc.fill(FigureColorUtils.colorNames.get("white").intValue());
		gc.stroke(FigureColorUtils.colorNames.get("white").intValue());
		gc.rect(getLeft(), getTop() , size.getWidth(), size.getHeight());
		
	}

	@Override
	String containerName() {
		return "Whitespace";
	}

}
