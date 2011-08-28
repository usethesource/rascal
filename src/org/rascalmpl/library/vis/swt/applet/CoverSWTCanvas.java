package org.rascalmpl.library.vis.swt.applet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public class CoverSWTCanvas extends Canvas implements PaintListener, MouseMoveListener{

	FigureSWTApplet parent;
	Figure part;
	
	CoverSWTCanvas(FigureSWTApplet parent, Figure part) {
		super(parent,SWT.NORMAL);
		System.out.printf("Creating swt canvas\n");
		this.parent = parent;
		this.part = part;
		addMouseListener(parent.getInputHandler());
		addMouseTrackListener(parent.getInputHandler());
		addMouseMoveListener(this);
		addPaintListener(this);
		addKeyListener(parent.getInputHandler());
		setVisible(true);
		
	}

	public boolean overlapsWith(Rectangle r){
		return part.overlapsWith(r);
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		e.x+=part.location.getX();
		e.y+=part.location.getY();
		parent.mouseMove(e);
	}

	@Override
	public void paintControl(PaintEvent e) {
		//System.out.printf("Redrawing me %s\n",this);
		//e.gc.drawOval(0, 0, getSize().x, getSize().y);
		Image img = parent.getFigureImage();
		e.gc.drawImage(img,FigureMath.round(-part.location.getX()),FigureMath.round(-part.location.getY()));
	}

	public void relocate() {
		System.out.printf("Relocating me %s %s %s\n",this,part.size,part.location);
		setSize(FigureMath.round(part.size.getX()),FigureMath.round(part.size.getY()));
		setLocation(FigureMath.round(part.location.getX()),FigureMath.round(part.location.getY()));
	}

}
