/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.swt;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.library.vis.graphics.FontStyle;
import org.rascalmpl.library.vis.util.FigureColorUtils;
import org.rascalmpl.values.ValueFactoryFactory;

public class SWTFontsAndColors {
	
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private static TypeFactory tf = TypeFactory.getInstance();

	public SWTFontsAndColors(IValueFactory factory) {
		vf = factory;
	}
	
	static private Device device = null;
	static private GC gc = null;


	private static void setGC(){
		if(device == null){
			device = Display.getCurrent();
			if(device == null){
				throw new Error("Trying to create get display from non-user interface thread!");
			}
		}
		if(gc == null || gc.isDisposed()){
			gc = new GC(device);
		}
	}
	

	static IList res = null;
	public static IList fontNames(){
		System.err.println("fontNames");
		
		 Display.getDefault().syncExec(new Runnable(){

			@Override
			public void run() {
				FontData[] fd =  Display.getDefault().getFontList(null, true);
				IListWriter w = vf.listWriter(tf.stringType());
				for(int i = 0; i < fd.length; i++){
					System.err.println("adding " + fd[i].getName());
					w.append(vf.string(fd[i].getName()));
				}
				res= w.done();
				
			}
			
		});
		 return res;
	
	}
	
	public static double textAscent(String fontName, int fontSize,
			FontStyle... styles) {
		// TODO: optimize this
		FontData fd = new FontData(fontName, fontSize, FontStyle.toStyleMask(styles));
		Font f = new Font(device, fd);
		setGC();
		gc.setFont(f);
		double result = gc.getFontMetrics().getAscent();
		f.dispose();
		return result;
	}

	public static double textDescent(String fontName, int fontSize,
			FontStyle... styles) {
		// TODO: optimize this
		FontData fd = new FontData(fontName, fontSize, FontStyle.toStyleMask(styles));
		Font f = new Font(device, fd);
		setGC();
		gc.setFont(f);
		double result = gc.getFontMetrics().getDescent();
		f.dispose();
		return result;
	}

	public static double textWidth(String s, String fontName, int fontSize,
			FontStyle... styles) {
		// TODO: optimize this
		FontData fd = new FontData(fontName, fontSize, FontStyle.toStyleMask(styles));
		Font f = new Font(device, fd);
		setGC();
		gc.setFont(f);
		double result = gc.textExtent(s).x;
		f.dispose();
		return result;
	}

	public static Color getRgbColor(int c) {
		setGC();
		return getRgbColor(device, c);
	}
	public static Color getRgbColor(Device dc, int c) {
		return new Color(dc, FigureColorUtils.getRed(c),
				FigureColorUtils.getGreen(c), FigureColorUtils.getBlue(c));
	}

	public static Color getColor(int which) {
		setGC();
		return device.getSystemColor(which);
	}
}
