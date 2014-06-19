package org.rascalmpl.library.experiments.vis2;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class FigureUtils {

	static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	static BufferedImage buffer;

	public FigureUtils(IValueFactory factory) {
		vf = factory;
	}

	public static ITuple textSize(IString text, IString fontName, IInteger fontSize, IBool bold, IBool italic){
		Dimension dim = textSize(text.getValue(), fontName.getValue(), fontSize.intValue(), bold.getValue(), italic.getValue());
		return vf.tuple(vf.integer(dim.width), vf.integer(dim.height));
	}

	private static Dimension textSize(String text, String fontName, int fontSize, boolean bold, boolean italic){
		int textHeight = fontLineHeight(fontName, fontSize, bold, italic);

		String[] lines = text.split("\n");
		int[] indents = new int[lines.length];
		int width = 0;
		for(int i = 0 ; i < lines.length ; i++){
			//lines[i] = Util.tabs2spaces(TAB_WIDTH,lines[i]);
			indents[i] = textLineWidth(lines[i], fontName, fontSize, bold, italic);
			width = Math.max(width,indents[i]);
		}
		//double innerAlign = prop.getReal(INNER_ALIGN);
		for(int i = 0 ; i < indents.length ; i++){
			indents[i] = (width - indents[i]);// * innerAlign;
		}
		int height = lines.length * textHeight;
		return new Dimension(width, height);
	}

	public static IInteger fontAscent(IString fontName, IInteger fontSize, IBool bold, IBool italic) {
		return vf.integer(getFontMetrics(fontName.getValue(), fontSize.intValue(), bold.getValue(), italic.getValue()).getMaxAscent());
	}

	public static IInteger fontDescent(IString fontName, IInteger fontSize, IBool bold, IBool italic) {
		return vf.integer(getFontMetrics(fontName.getValue(), fontSize.intValue(), bold.getValue(), italic.getValue()).getMaxDescent());
	}

	private static int fontLineHeight(String fontName, int fontSize, boolean bold, boolean italic) {
		return getFontMetrics(fontName, fontSize, bold, italic).getHeight();	
	}

	private static int textLineWidth(String text, String fontName, int fontSize, boolean bold, boolean italic) {
		return getFontMetrics(fontName, fontSize, bold, italic).stringWidth(text);	
	}

	private static java.awt.FontMetrics getFontMetrics(String fontName, int fontSize, boolean bold, boolean italic) {
		if(buffer == null){
			buffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		}
		Graphics g = buffer.getGraphics();

		Font f = new Font(fontName, getFontStyle(bold, italic), fontSize);
		g.setFont(f);
		java.awt.FontMetrics fm = g.getFontMetrics();
		return fm;
	}
	
	private static int getFontStyle(boolean bold, boolean italic){
		if(bold && italic){
			return Font.BOLD | Font.ITALIC;
			
		} else if(bold){
			return Font.BOLD;
		} else if(italic){
			return Font.ITALIC;
		} else {
			return Font.PLAIN;
		}
	}

	private static String[] webSafeFonts = {
		"Arial", 
		"Arial Black", 
		"Book Antiqua",
		"Charcoal", 
		"Comic Sans MS", 
		"Courier", 
		"Courier New", 
		"Gadget",
		"Geneva",
		"Georgia", 
		"Helvetica", 
		"Impact",
		"Lucida Console",
		"Lucida Grande",
		"Lucida Sans Unicode",
		"Monaco",
		"Palatino",
		"Palatino Linotype", 
		"Tahoma", 
		"Times", 
		"Times New Roman", 
		"Trebuchet MS", 
		"Verdana"
	};

	public static IList fontNames(){
		IListWriter w = vf.listWriter();
		for(int i = 0; i < webSafeFonts.length; i++){
			w.append(vf.string(webSafeFonts[i]));
		}
		return w.done();
	}
}

