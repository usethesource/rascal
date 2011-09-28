package org.rascalmpl.library.vis.figure.combine.containers;

import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureColorUtils;

public class WhiteSpace extends Container{

	public WhiteSpace(Figure inner, PropertyManager properties) {
		super(inner, properties);
	}

	@Override
	String containerName() {
		return "Whitespace";
	}

	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements) {
		gc.fill(FigureColorUtils.WHITE);
		gc.stroke(FigureColorUtils.WHITE);
		gc.rect(localLocation.getX(), localLocation.getY() , size.getX(), size.getY());
	}

}
