package org.rascalmpl.library.vis.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.graphics.SWTGraphicsContext;
import org.rascalmpl.library.vis.util.Rectangle;

public class OverlapCanvas extends Canvas implements PaintListener, MouseMoveListener {

	private Figure fig;
	private Rectangle overlap;
	private FigureSWTApplet parent;
	
	public OverlapCanvas(FigureSWTApplet parent,Composite floor) {
		super(floor, SWT.NORMAL);
		this.addPaintListener(this);
		this.parent = parent;
		addPaintListener(this);
		addMouseMoveListener(this);
		addMouseListener(parent);
		addKeyListener(parent);
	}
	
	public void setOverlap(Figure fig,Rectangle overlap){
		this.overlap = overlap;
		this.fig = fig;
		setSize(FigureApplet.round(overlap.getWidth()),FigureApplet.round(overlap.getHeight()));
		setLocation(FigureApplet.round(overlap.getX()),FigureApplet.round(overlap.getY()));
	}
	

	@Override
	public void paintControl(PaintEvent e) {
		GraphicsContext gc = new SWTGraphicsContext(e.gc);
		gc.translate( -overlap.getX(),-overlap.getY());
		fig.drawPart(overlap, gc);
		e.gc.dispose();
	}

	@Override
	public void mouseMove(MouseEvent e) {
		e.x+=fig.globalLocation.getX();
		e.y+=fig.globalLocation.getY();
		parent.mouseMove(e);
	}
	
	

}
