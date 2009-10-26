package org.meta_environment.rascal.library.experiments.Processing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TypedListener;

import processing.core.PApplet;

public class ProcessingComposite  extends Composite implements PaintListener, MouseListener, MouseMoveListener {

	private Canvas canvas;
	private PApplet papplet;

	public ProcessingComposite(Composite parent, int style, final PApplet papplet) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		 setLayout(new FillLayout());
		 this.papplet = papplet;
		 this.canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
		 
		 canvas.addPaintListener(new PaintListener() {
		      public void paintControl(PaintEvent e) {
		    	  papplet.init();
		    	  papplet.draw();
		        // Do some drawing
//		        org.eclipse.swt.graphics.Rectangle rect = ((Canvas) e.widget).getBounds();
//		        e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
//		        e.gc.drawFocus(5, 5, rect.width - 10, rect.height - 10);
//		        e.gc.drawText("You can draw text directly on a canvas", 60, 60);
		    	 e.gc.drawLine(50,50,100,100);
		      }
		    });
		// this.canvas.addPaintListener(this);
	    // this.canvas.addMouseListener(this);
	    // this.canvas.addMouseMoveListener(this);
	}
	
	public void addPaintListener(PaintListener listener) {
		checkWidget();
		if (listener == null) System.err.println ("PaintListener: " + SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener (listener);
		addListener(SWT.Paint,typedListener);
	}
	
	public void addMouseListener(MouseListener listener) {
		checkWidget();
		//if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener (listener);
		addListener(SWT.MouseDown,typedListener);
		addListener(SWT.MouseUp,typedListener);
		addListener(SWT.MouseDoubleClick,typedListener);
	}

	public void paintControl(PaintEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseMove(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
