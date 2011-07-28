package org.rascalmpl.library.vis.swt;

import java.util.Vector;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;

public class FiguresOverlay {
	
	private Vector<Figure> figures;
	
	public FiguresOverlay(){
		figures = new Vector<Figure>();
	}
	
	public void add(Figure fig){
		figures.add(fig);
	}
	
	public void clear(){
		figures.clear();
	}
	
	private boolean rectanglesOverlap(	double xlow1, double ylow1, double xhigh1, double yhigh1,
											double xlow2, double ylow2, double xhigh2, double yhigh2){
		return !(ylow1 > yhigh2 || yhigh1 < ylow2 || xlow1 > xhigh2 || xhigh1 < xlow2); 
	
	}
	
	public void renderOn(GraphicsContext gc, Control control){
		Point p = control.getLocation();
		Point s = control.getSize();
		gc.translate(-p.x, -p.y);
		for(Figure f : figures){
			System.err.printf("Drawing %s on %s\n",f,control);
			if(rectanglesOverlap(p.x,p.y,p.x +s.x,p.y+s.y,
					f.globalLocation.getX(),f.globalLocation.getY(), f.globalLocation.getX() +f.size.getWidth(),f.globalLocation.getY()+f.size.getHeight())){
				f.draw(gc);
			} else {
				System.out.printf("Non overlapping! %s\n",f);
			}
		}
		gc.translate(p.x, p.y);
	}
}
