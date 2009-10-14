package org.meta_environment.rascal.library.experiments.Processing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import processing.core.PApplet;

public class ProcessingComposite  extends Composite {

	private Canvas canvas;

	public ProcessingComposite(Composite parent, int style, PApplet pa) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		 setLayout(new FillLayout());
		 this.canvas = new Canvas(this, SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
	}
	

}
