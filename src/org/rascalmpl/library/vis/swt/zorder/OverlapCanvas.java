package org.rascalmpl.library.vis.swt.zorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.graphics.SWTGraphicsContext;
import org.rascalmpl.library.vis.swt.FigureSWTApplet;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.Rectangle;

public class OverlapCanvas extends Canvas implements PaintListener, MouseMoveListener, IHasZOrder{

	private Figure fig;
	private Rectangle overlap;
	private FigureSWTApplet parent;
	private int zorder;
	private int sequenceNr;
	
	public OverlapCanvas(FigureSWTApplet parent,Composite floor) {
		super(floor, SWT.NORMAL);
		this.addPaintListener(this);
		this.parent = parent;
		addPaintListener(this);
		addMouseMoveListener(this);
		addMouseListener(parent);
		addKeyListener(parent);
		sequenceNr = Figure.sequencer;
		Figure.sequencer++;
	}
	
	public void setOverlap(Figure fig,Rectangle overlap){
		this.overlap = overlap;
		this.fig = fig;
		setSize(FigureMath.round(overlap.getWidth()+1),FigureMath.round(overlap.getHeight() +1));
		setLocation(FigureMath.round(overlap.getX()),FigureMath.round(overlap.getY()));
	}
	

	@Override
	public void paintControl(PaintEvent e) {
		if(e.gc.isDisposed()) {
			return;
		}
		GraphicsContext gc = new SWTGraphicsContext(e.gc,parent.getVisibleSWTElementsVector());
		setVisible(true);
		gc.registerSWTElement(this);
		gc.translate( -overlap.getX(),-overlap.getY());
		parent.drawPart(overlap, gc);
		e.gc.dispose();
	}

	@Override
	public void mouseMove(MouseEvent e) {
		e.x+=fig.globalLocation.getX();
		e.y+=fig.globalLocation.getY();
		parent.mouseMove(e);
	}

	@Override
	public void setZOrder(int depth) {
		zorder = depth;
		
	}

	@Override
	public int getZOrder() {
		return zorder;
	}

	@Override
	public Control getElement() {
		return this;
	}

	@Override
	public int getStableOrder() {
		return sequenceNr;
	}

	@Override
	public void setVisible(boolean visible) {
		this.setVisible(visible);
	}
	
	

}
