/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.library.vis.util;

import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.swt.graphics.RGB;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class FigureColorUtils {

	public static final int OPAQUE = 255;
	
	static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public FigureColorUtils(IValueFactory factory) {
		// TODO: this code is weird; should vf be non-static, perhaps?
		vf = factory;
	}

	public static final HashMap<String, IInteger> colorNames = new HashMap<String, IInteger>() {
		/**
			 * 
			 */
		private static final long serialVersionUID = -28776310678947259L;

		{
			put("aliceblue", rgb(240, 248, 255));
			put("antiquewhite", rgb(250, 235, 215));
			put("aqua", rgb(0, 255, 255));
			put("aquamarine", rgb(127, 255, 212));
			put("azure", rgb(240, 255, 255));
			put("beige", rgb(245, 245, 220));
			put("bisque", rgb(255, 228, 196));
			put("black", rgb(0, 0, 0));
			put("blanchedalmond", rgb(255, 235, 205));
			put("blue", rgb(0, 0, 255));
			put("blueviolet", rgb(138, 43, 226));
			put("brown", rgb(165, 42, 42));
			put("burlywood", rgb(222, 184, 135));
			put("cadetblue", rgb(95, 158, 160));
			put("chartreuse", rgb(127, 255, 0));
			put("chocolate", rgb(210, 105, 30));
			put("coral", rgb(255, 127, 80));
			put("cornflowerblue", rgb(100, 149, 237));
			put("cornsilk", rgb(255, 248, 220));
			put("crimson", rgb(220, 20, 60));
			put("cyan", rgb(0, 255, 255));
			put("darkblue", rgb(0, 0, 139));
			put("darkcyan", rgb(0, 139, 139));
			put("darkgoldenrod", rgb(184, 134, 11));
			put("darkgray", rgb(169, 169, 169));
			put("darkgreen", rgb(0, 100, 0));
			put("darkgrey", rgb(169, 169, 169));
			put("darkkhaki", rgb(189, 183, 107));
			put("darkmagenta", rgb(139, 0, 139));
			put("darkolivegreen", rgb(85, 107, 47));
			put("darkorange", rgb(255, 140, 0));
			put("darkorchid", rgb(153, 50, 204));
			put("darkred", rgb(139, 0, 0));
			put("darksalmon", rgb(233, 150, 122));
			put("darkseagreen", rgb(143, 188, 143));
			put("darkslateblue", rgb(72, 61, 139));
			put("darkslategray", rgb(47, 79, 79));
			put("darkslategrey", rgb(47, 79, 79));
			put("darkturquoise", rgb(0, 206, 209));
			put("darkviolet", rgb(148, 0, 211));
			put("deeppink", rgb(255, 20, 147));
			put("deepskyblue", rgb(0, 191, 255));
			put("dimgray", rgb(105, 105, 105));
			put("dimgrey", rgb(105, 105, 105));
			put("dodgerblue", rgb(30, 144, 255));
			put("firebrick", rgb(178, 34, 34));
			put("floralwhite", rgb(255, 250, 240));
			put("forestgreen", rgb(34, 139, 34));
			put("fuchsia", rgb(255, 0, 255));
			put("gainsboro", rgb(220, 220, 220));
			put("ghostwhite", rgb(248, 248, 255));
			put("gold", rgb(255, 215, 0));
			put("goldenrod", rgb(218, 165, 32));
			put("gray", rgb(128, 128, 128));
			put("green", rgb(0, 128, 0));
			put("greenyellow", rgb(173, 255, 47));
			put("grey", rgb(128, 128, 128));
			put("honeydew", rgb(240, 255, 240));
			put("hotpink", rgb(255, 105, 180));
			put("indianred", rgb(205, 92, 92));
			put("indigo", rgb(75, 0, 130));
			put("ivory", rgb(255, 255, 240));
			put("khaki", rgb(240, 230, 140));
			put("lavender", rgb(230, 230, 250));
			put("lavenderblush", rgb(255, 240, 245));
			put("lawngreen", rgb(124, 252, 0));
			put("lemonchiffon", rgb(255, 250, 205));
			put("lightblue", rgb(173, 216, 230));
			put("lightcoral", rgb(240, 128, 128));
			put("lightcyan", rgb(224, 255, 255));
			put("lightgoldenrodyellow", rgb(250, 250, 210));
			put("lightgray", rgb(211, 211, 211));
			put("lightgreen", rgb(144, 238, 144));
			put("lightgrey", rgb(211, 211, 211));
			put("lightpink", rgb(255, 182, 193));
			put("lightsalmon", rgb(255, 160, 122));
			put("lightseagreen", rgb(32, 178, 170));
			put("lightskyblue", rgb(135, 206, 250));
			put("lightslategray", rgb(119, 136, 153));
			put("lightslategrey", rgb(119, 136, 153));
			put("lightsteelblue", rgb(176, 196, 222));
			put("lightyellow", rgb(255, 255, 224));
			put("lime", rgb(0, 255, 0));
			put("limegreen", rgb(50, 205, 50));
			put("linen", rgb(250, 240, 230));
			put("magenta", rgb(255, 0, 255));
			put("maroon", rgb(128, 0, 0));
			put("mediumaquamarine", rgb(102, 205, 170));
			put("mediumblue", rgb(0, 0, 205));
			put("mediumorchid", rgb(186, 85, 211));
			put("mediumpurple", rgb(147, 112, 219));
			put("mediumseagreen", rgb(60, 179, 113));
			put("mediumslateblue", rgb(123, 104, 238));
			put("mediumspringgreen", rgb(0, 250, 154));
			put("mediumturquoise", rgb(72, 209, 204));
			put("mediumvioletred", rgb(199, 21, 133));
			put("midnightblue", rgb(25, 25, 112));
			put("mintcream", rgb(245, 255, 250));
			put("mistyrose", rgb(255, 228, 225));
			put("moccasin", rgb(255, 228, 181));
			put("navajowhite", rgb(255, 222, 173));
			put("navy", rgb(0, 0, 128));
			put("oldlace", rgb(253, 245, 230));
			put("olive", rgb(128, 128, 0));
			put("olivedrab", rgb(107, 142, 35));
			put("orange", rgb(255, 165, 0));
			put("orangered", rgb(255, 69, 0));
			put("orchid", rgb(218, 112, 214));
			put("palegoldenrod", rgb(238, 232, 170));
			put("palegreen", rgb(152, 251, 152));
			put("paleturquoise", rgb(175, 238, 238));
			put("palevioletred", rgb(219, 112, 147));
			put("papayawhip", rgb(255, 239, 213));
			put("peachpuff", rgb(255, 218, 185));
			put("peru", rgb(205, 133, 63));
			put("pink", rgb(255, 192, 203));
			put("plum", rgb(221, 160, 221));
			put("powderblue", rgb(176, 224, 230));
			put("purple", rgb(128, 0, 128));
			put("red", rgb(255, 0, 0));
			put("rosybrown", rgb(188, 143, 143));
			put("royalblue", rgb(65, 105, 225));
			put("saddlebrown", rgb(139, 69, 19));
			put("salmon", rgb(250, 128, 114));
			put("sandybrown", rgb(244, 164, 96));
			put("seagreen", rgb(46, 139, 87));
			put("seashell", rgb(255, 245, 238));
			put("sienna", rgb(160, 82, 45));
			put("silver", rgb(192, 192, 192));
			put("skyblue", rgb(135, 206, 235));
			put("slateblue", rgb(106, 90, 205));
			put("slategray", rgb(112, 128, 144));
			put("slategrey", rgb(112, 128, 144));
			put("snow", rgb(255, 250, 250));
			put("springgreen", rgb(0, 255, 127));
			put("steelblue", rgb(70, 130, 180));
			put("tan", rgb(210, 180, 140));
			put("teal", rgb(0, 128, 128));
			put("thistle", rgb(216, 191, 216));
			put("tomato", rgb(255, 99, 71));
			put("turquoise", rgb(64, 224, 208));
			put("violet", rgb(238, 130, 238));
			put("wheat", rgb(245, 222, 179));
			put("white", rgb(255, 255, 255));
			put("whitesmoke", rgb(245, 245, 245));
			put("yellow", rgb(255, 255, 0));
			put("yellowgreen", rgb(154, 205, 50));
		}

	};
	
	
	public static final int WHITE = colorNames.get("white").intValue();
	public static final int BLACK = colorNames.get("black").intValue();
	public static final int LIGHTGRAY = colorNames.get("lightgray").intValue();

	public static IInteger rgb(int r, int g, int b) {
		return vf.integer(figureColor(r, g, b));
	}

	public static int figureColor(int r, int g, int b) {
		return figureColor(r, g, b, 1.0f);
	}

	public static int figureColor(int r, int g, int b, float alpha) {

		if (r > 255)
			r = 255;
		else if (r < 0)
			r = 0;
		if (g > 255)
			g = 255;
		else if (g < 0)
			g = 0;
		if (b > 255)
			b = 255;
		else if (b < 0)
			b = 0;

		if (alpha > 1)
			alpha = 1;
		else if (alpha < 0)
			alpha = 0;

		return (((int) (alpha * 255) << 24) | ((r) << 16) | ((g) << 8) | (b));
	}

	public static int figureColor(int c1, float alpha) {
		float r1 = (c1 >> 16) & 0xff;
		float g1 = (c1 >> 8) & 0xff;
		float b1 = c1 & 0xff;

		if (alpha > 1)
			alpha = 1;
		else if (alpha < 0)
			alpha = 0;

		return (((int) (alpha * 255) << 24) | ((int) (r1) << 16)
				| ((int) (g1) << 8) | ((int) (b1)));
	}

	public static int myLerpColor(int c1, int c2, float amt) {
		float a1 = ((c1 >> 24) & 0xff);
		float r1 = (c1 >> 16) & 0xff;
		float g1 = (c1 >> 8) & 0xff;
		float b1 = c1 & 0xff;
		float a2 = (c2 >> 24) & 0xff;
		float r2 = (c2 >> 16) & 0xff;
		float g2 = (c2 >> 8) & 0xff;
		float b2 = c2 & 0xff;

		return (((int) (a1 + (a2 - a1) * amt) << 24)
				| ((int) (r1 + (r2 - r1) * amt) << 16)
				| ((int) (g1 + (g2 - g1) * amt) << 8) | ((int) (b1 + (b2 - b1)
				* amt)));
	}

	public static int getRed(int c) {
		return (c >> 16) & 0xff;
	}

	public static int getGreen(int c) {
		return (c >> 8) & 0xff;
	}

	public static int getBlue(int c) {
		return c & 0xff;
	}

	public static int getAlpha(int c) {
		return (c >> 24) & 0xff;
	}
	
	public static int withoutAlpha(int c){
		return c & 0xffffff; 
	}

	public static RGB toRGB(int c) {
		return new RGB(getRed(c), getGreen(c), getBlue(c));
	}

	public IInteger interpolateColor(IInteger from, IInteger to, IReal amt) {
		int fromColor = from.intValue();
		int toColor = to.intValue();
		float percentage = amt.floatValue();
		return vf.integer(myLerpColor(fromColor, toColor, percentage));
	}

	public IList colorSteps(IInteger from, IInteger to, IInteger n) {
		int fromColor = from.intValue();
		int toColor = to.intValue();
		int max = n.intValue();
		IListWriter w = vf.listWriter(from.getType());
		for (int i = 0; i < max; i++) {
			// System.err.println("i = " + i + ": " + fromColor + " " + toColor
			// + " " + (1.0f * i)/max);
			w.append(vf.integer(myLerpColor(fromColor, toColor, (1.0f * i)
					/ max)));
		}
		return w.done();
	}

	public IInteger gray(IInteger r) {
		int g = r.intValue();
		return vf.integer(figureColor(g, g, g));
	}

	public IInteger gray(IReal r) {
		int g = (int)Math.round(255 * r.doubleValue());
		return vf.integer(figureColor(g, g, g));
	}

	public IInteger gray(IInteger r, IReal alpha) {
		int g = r.intValue();
		return vf.integer(figureColor(g, g, g, alpha.floatValue()));
	}

	public IInteger gray(IReal r, IReal alpha) {
		int g = (int)Math.round(255 * r.doubleValue());
		return vf.integer(figureColor(g, g, g, alpha.floatValue()));
	}

	public IInteger rgb(IInteger r, IInteger g, IInteger b) {
		return vf.integer(figureColor(r.intValue(), g.intValue(), b.intValue(),
				1.0f));
	}

	public IInteger rgb(IInteger r, IInteger g, IInteger b, IReal alpha) {
		return vf.integer(figureColor(r.intValue(), g.intValue(), b.intValue(),
				alpha.floatValue()));
	}

	public IInteger color(IString colorName, IEvaluatorContext ctx) {
		IInteger c = colorNames.get(colorName.getValue().toLowerCase());
		if (c != null)
			return c;
		throw RuntimeExceptionFactory.illegalArgument(colorName, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}

	public IInteger color(IString colorName, IReal alpha, IEvaluatorContext ctx) {
		IInteger c = colorNames.get(colorName.getValue().toLowerCase());
		if (c != null) {
			int ci = c.intValue();
			return vf.integer(figureColor(ci, alpha.floatValue()));
		}
		throw RuntimeExceptionFactory.illegalArgument(colorName, ctx.getCurrentAST(),
				ctx.getStackTrace());
	}

	/**
	 * @return list of built-in color names
	 */
	public IList colorNames() {
		TypeFactory types = TypeFactory.getInstance();
		IListWriter w = vf.listWriter(types.stringType());
		String strings[] = new String[colorNames.size()];
		Arrays.sort(colorNames.keySet().toArray(strings));
		for (String s : strings) {
			w.append(vf.string(s));
		}
		return w.done();
	}

	/**
	 * List of default colors for errors
	 */
	protected static final int errorColors[] = { figureColor(65, 105, 225), // royalblue
			figureColor(246, 211, 87), // gold (like)
			figureColor(255, 0, 0) // red
	};

	/**
	 * Define a list of custom colors for errors in editor and outline
	 * 
	 * @param colors
	 *            the list of colors
	 */
	public static void setErrorColors(final IList colors) {
		for (int i = 0; i < colors.length() && i < errorColors.length; i++) {
			int color = ((IInteger) colors.get(i)).intValue();
			errorColors[i] = color;
		}
	}

	/**
	 * Get an error color
	 * 
	 * @param n
	 *            the n-th color (0 <= n < 3)
	 * @return the corresponding color
	 */
	public static int getErrorColor(int n) {
		if (n >= 0 && n < errorColors.length)
			return errorColors[n];
		return errorColors[2];
	}

	/**
	 * List of default colors for highlights
	 */

	public static final int highlightColors[] = { figureColor(255, 192, 203), // pink
			figureColor(255, 255, 0), // yellow
			figureColor(255, 0, 255), // magenta
			figureColor(0, 255, 255), // aqua
			figureColor(0, 255, 0), // lime
			figureColor(192, 192, 192), // silver
			figureColor(218, 165, 32), // golden rod
			figureColor(240, 128, 128), // light coral
			figureColor(135, 206, 250), // lightskyblue
			figureColor(0, 128, 128) // teal
	};

	/**
	 * Define a list of custom colors for line highlights in editor and outline
	 * 
	 * @param colors
	 *            The list of colors
	 */
	public static void setHighlightColors(final IList colors) {
		for (int i = 0; i < colors.length() && i < highlightColors.length; i++) {
			int color = ((IInteger) colors.get(i)).intValue();
			highlightColors[i] = color;
		}
	}

	/**
	 * Get a highlight color
	 * 
	 * @param n
	 *            the n-th color (0 <= n < highlightColors.length)
	 * @return the corresponding color
	 */
	public static int getHighlightColor(int n) {
		if (n >= 0 && n < highlightColors.length)
			return highlightColors[n];
		return highlightColors[0];
	}
	
	public static int dropShadowColor() {
		return figureColor(colorNames.get("black").intValue(), 0.3f);
	}
	
	public static int noColor() {
		return figureColor(colorNames.get("black").intValue(), 0.1f);
	}
}
