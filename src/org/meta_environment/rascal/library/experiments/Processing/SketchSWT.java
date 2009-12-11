package org.meta_environment.rascal.library.experiments.Processing;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import processing.core.PApplet;

public class SketchSWT   {
	
	private PApplet applet;
	//private static Display display = new Display();

	public SketchSWT (final PApplet pa){
		this. applet = pa;
		Display display = new Display();
		Shell shell = new Shell(display);

		shell.setSize(600, 600);
		shell.setLayout(new FillLayout());
		shell.setText("Rascal Visualization");
		
		Composite composite = new Composite(shell, SWT.DOUBLE_BUFFERED | SWT.EMBEDDED);

		Frame frame = SWT_AWT.new_Frame(composite); 
		frame.setLocation(0,0);
		frame.add(pa);
		pa.init();
		frame.setVisible(true);
		frame.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		pa.destroy();
	}
	
	public PApplet getApplet(){
		return applet;
	}
}
