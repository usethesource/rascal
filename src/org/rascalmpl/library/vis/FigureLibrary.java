/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.vis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class FigureLibrary {

	IValueFactory values;
	private ScrolledComposite sc;
	private IFigureApplet fpa;

	public FigureLibrary(IValueFactory values) {
		super();
		this.values = values;
	}

	private void displayFigure(String name, IConstructor fig,
			IEvaluatorContext ctx) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		final MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("&File");
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);
		final MenuItem item = new MenuItem(submenu, SWT.PUSH);
		item.setText("&Print");
		item.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				PrintDialog dialog = new PrintDialog(shell, SWT.PRIMARY_MODAL);
				final PrinterData data = dialog.open();
				if (data == null)
					return;
				final Printer printer = new Printer(data);
				if (printer.startJob("Figure")) {
					if (fpa != null)
						fpa.print(printer);
					printer.endJob();
				}
			}
		});

		sc = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc.setAlwaysShowScrollBars(true);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		final Canvas canvas = new Canvas(sc, SWT.NONE);
		fpa = new FigureSWTApplet(canvas, name, fig, ctx);
		sc.setContent(canvas);
		sc.setMinSize(fpa.getFigureWidth(), fpa.getFigureHeight());
		sc.pack();
		shell.setText(name);
		shell.setSize(Math.min(fpa.getFigureWidth() + 10, 400),
				Math.min(fpa.getFigureHeight() + 100, 400));
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		shell.dispose();
		display.dispose();
	}

	public void render(IConstructor fig, IEvaluatorContext ctx) {
		displayFigure("Figure", fig, ctx);
	}

	public void renderSave(IConstructor fig, ISourceLocation sloc,
			IEvaluatorContext ctx) {
		OutputStream out = null;
		final Display display = new Display();
		final Shell shell = new Shell(display);
		try {
			int mode = SWT.IMAGE_JPEG;
			URI uri = sloc.getURI();
			String path = uri.getPath();
			if (path.endsWith(".png"))
				mode = SWT.IMAGE_PNG;
			else if (path.endsWith(".bmp"))
				mode = SWT.IMAGE_BMP;
//			else if (path.endsWith(".gif"))
//				mode = SWT.IMAGE_GIF;
			else if (path.endsWith(".ico"))
				mode = SWT.IMAGE_ICO;
			else if (path.endsWith(".jpg"))
				mode = SWT.IMAGE_JPEG;
			out = ctx.getResolverRegistry().getOutputStream(uri, false);
			fpa = new FigureSWTApplet(shell, sloc.getURI().toString(), fig, ctx);
			fpa.write(out, mode);
			fpa.dispose();
		} catch (FileNotFoundException fnfex) {
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		} catch (IOException ioex) {
			throw RuntimeExceptionFactory.io(values.string(ioex.getMessage()),
					null, null);
		} finally {
			if (out != null) {
				try {
					out.close();
					shell.dispose();
					display.dispose();					
				} catch (IOException ioex) {
					throw RuntimeExceptionFactory.io(
							values.string(ioex.getMessage()), null, null);
				}
			}
		}
	}

	public void render(IString name, IConstructor fig, IEvaluatorContext ctx) {
		displayFigure(name.getValue(), fig, ctx);
	}

}
