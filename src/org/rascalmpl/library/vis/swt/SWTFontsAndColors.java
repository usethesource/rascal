package org.rascalmpl.library.vis.swt;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.rascalmpl.library.vis.graphics.FontStyle;
import org.rascalmpl.library.vis.util.FigureColorUtils;

public class SWTFontsAndColors {
	
	static private Device device = null;
	static private GC gc = null;


	private static void setGC(){
		if(device == null){
			device = Display.getCurrent();
			if(device == null){
				throw new Error("Trying to create get display from non-userinterfacethread!");
			}
		}
		if(gc == null || gc.isDisposed()){
			gc = new GC(device);
		}
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
		return new Color(device, FigureColorUtils.getRed(c),
				FigureColorUtils.getGreen(c), FigureColorUtils.getBlue(c));
	}

	public static Color getColor(int which) {
		setGC();
		return device.getSystemColor(which);
	}
}
