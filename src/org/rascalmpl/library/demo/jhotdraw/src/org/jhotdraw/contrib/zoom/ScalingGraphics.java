/*
 * @(#)ScalingGraphics.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib.zoom;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * A graphics context that can scale to an arbitrary factor.
 *
 * Note: this class is only needed for a JDK1.1 compliant implementation
 *
 * @author Andre Spiegel <spiegel@gnu.org>
 * @version <$CURRENT_VERSION$>
 */
public class ScalingGraphics extends java.awt.Graphics {

	/**
	 * The scale used for all drawing operations.
	 */
	private double scale = 1.0;

	/**
	 * The actual graphics context to which drawing is delegated.
	 */
	private Graphics real;

	/**
	 * The font with which the user thinks he is drawing.
	 * On the real graphics context, a scaled font is substituted
	 * for it (which may or may not be precisely to scale).
	 */
	private Font userFont;

	/**
	 * The current clipping rectangle, in user coordinates.
	 * Cached here to avoid unnecessary scaling back and forth.
	 */
	private Rectangle userClip;


	public ScalingGraphics(Graphics realGraphics) {
		real = realGraphics;
	}

	/**
	 * Sets the scale to be used for any subsequent drawing operations.
	 * All coordinates are multiplied by this value in both x- and
	 * y-direction before drawing.  Thus, a value of 1.0 means no
	 * scaling, smaller values shrink the picture, larger ones enlarge
	 * it.
	 */
	public void setScale(double newScale) {
		scale = newScale;
	}

	/**
	 * Returns the scale factor currently used for drawing operations.
	 * @see #setScale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Returns the font that should be substituted for the
	 * given font at the given scale.
	 */
	private static Font scaledFont(Font f, double scale) {
		int size = f.getSize();
		int scaledSize = (int) (size * scale);
		//if (scaledSize < 6) scaledSize = 6;
		return new Font(f.getFamily(), f.getStyle(), scaledSize);
	}

	/**
	 * Scales a shape to the given scale.
	 */
	private static Shape scaledShape(Shape s, double scale) {
		if (s instanceof Rectangle) {
			Rectangle r = (Rectangle) s;
			return new Rectangle((int) (r.x * scale), (int) (r.y * scale),
					(int) (r.width * scale), (int) (r.height * scale));
		}
		else {
			throw new RuntimeException("Cannot scale shape: " + s.getClass().getName());
		}
	}

	// delegating implementations below this line

	public Graphics create() {
		Graphics realCopy = real.create();
		ScalingGraphics result = new ScalingGraphics(realCopy);
		result.setScale(getScale());
		return result;
	}

	public void translate(int x, int y) {
		real.translate((int) (x * scale), (int) (y * scale));
	}

	public Color getColor() {
		return real.getColor();
	}

	public void setColor(Color c) {
		real.setColor(c);
	}

	public void setPaintMode() {
		real.setPaintMode();
	}

	public void setXORMode(Color c1) {
		real.setXORMode(c1);
	}

	public Font getFont() {
		// returns the font with which the user thinks he is drawing
		if (userFont == null)
			userFont = real.getFont();
		return userFont;
	}

	public void setFont(Font font) {
		userFont = font;
		real.setFont(scaledFont(font, scale));
	}

	public FontMetrics getFontMetrics() {
		return new ScalingFontMetrics(userFont, real.getFontMetrics());
	}

	public FontMetrics getFontMetrics(Font f) {
		// returns a ScalingFontMetrics object that measures distances
		// on the real font, and scales them back to user coordinates
		return new ScalingFontMetrics(f,
				real.getFontMetrics(scaledFont(f, scale)));
	}

	public Rectangle getClipBounds() {
		return userClip;
	}

	public void clipRect(int x, int y, int width, int height) {
		if (userClip == null)
			userClip = new Rectangle(x, y, width, height);
		else
			userClip = userClip.intersection(new Rectangle(x, y, width, height));
		real.clipRect((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale));
	}

	public void setClip(int x, int y, int width, int height) {
		userClip = new Rectangle(x, y, width, height);
		real.setClip((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale));
	}

	public Shape getClip() {
		return userClip;
	}

	public void setClip(Shape clip) {
		userClip = (Rectangle) clip;
		if (clip != null)
		// Scale the Shape before applying it.
			real.setClip(scaledShape(clip, scale));
		else
			real.setClip(null);
	}

	public void copyArea(int x, int y, int width, int height,
						 int dx, int dy) {
		real.copyArea((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale),
				(int) (dx * scale), (int) (dy * scale));
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		real.drawLine((int) (x1 * scale), (int) (y1 * scale),
				(int) (x2 * scale), (int) (y2 * scale));
	}

	public void fillRect(int x, int y, int width, int height) {
		real.fillRect((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale));
	}

	public void clearRect(int x, int y, int width, int height) {
		real.clearRect((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale));
	}

	public void drawRoundRect(int x, int y, int width, int height,
							  int arcWidth, int arcHeight) {
		real.drawRoundRect((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale),
				(int) (arcWidth * scale), (int) (arcHeight * scale));
	}

	public void fillRoundRect(int x, int y, int width, int height,
							  int arcWidth, int arcHeight) {
		real.fillRoundRect((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale),
				(int) (arcWidth * scale), (int) (arcHeight * scale));
	}

	public void drawOval(int x, int y, int width, int height) {
		real.drawOval((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale));
	}

	public void fillOval(int x, int y, int width, int height) {
		real.fillOval((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale));
	}

	public void drawArc(int x, int y, int width, int height,
						int startAngle, int arcAngle) {
		real.drawArc((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale),
				startAngle, arcAngle);
	}

	public void fillArc(int x, int y, int width, int height,
						int startAngle, int arcAngle) {
		real.fillArc((int) (x * scale), (int) (y * scale),
				(int) (width * scale), (int) (height * scale),
				startAngle, arcAngle);
	}

	public void drawPolyline(int xPoints[], int yPoints[],
							 int nPoints) {
		int[] realXPoints = new int[nPoints];
		int[] realYPoints = new int[nPoints];
		for (int i = 0; i < nPoints; i++) {
			realXPoints[i] = (int) (xPoints[i] * scale);
			realYPoints[i] = (int) (yPoints[i] * scale);
		}
		real.drawPolyline(realXPoints, realYPoints, nPoints);
	}

	public void drawPolygon(int xPoints[], int yPoints[],
							int nPoints) {
		int[] realXPoints = new int[nPoints];
		int[] realYPoints = new int[nPoints];
		for (int i = 0; i < nPoints; i++) {
			realXPoints[i] = (int) (xPoints[i] * scale);
			realYPoints[i] = (int) (yPoints[i] * scale);
		}
		real.drawPolygon(realXPoints, realYPoints, nPoints);
	}

	public void fillPolygon(int xPoints[], int yPoints[],
							int nPoints) {
		int[] realXPoints = new int[nPoints];
		int[] realYPoints = new int[nPoints];
		for (int i = 0; i < nPoints; i++) {
			realXPoints[i] = (int) (xPoints[i] * scale);
			realYPoints[i] = (int) (yPoints[i] * scale);
		}
		real.fillPolygon(realXPoints, realYPoints, nPoints);
	}

	public void drawString(String str, int x, int y) {
		real.drawString(str, (int) (x * scale), (int) (y * scale));
	}

	// drop this method if using jdk 1.1
	public void drawString(java.text.AttributedCharacterIterator iterator,
						   int x, int y) {
		real.drawString(iterator, (int) (x * scale), (int) (y * scale));
	}

	public boolean drawImage(Image img, int x, int y,
							 ImageObserver observer) {
		// DoubleBufferImages must not be scaled.
		if (img instanceof DoubleBufferImage)
			return real.drawImage(((DoubleBufferImage) img).getRealImage(),
					x, y, observer);
		else
			return real.drawImage(img, (int) (x * scale), (int) (y * scale),
					(int) (img.getWidth(observer) * scale),
					(int) (img.getHeight(observer) * scale),
					observer);
	}

	public boolean drawImage(Image img, int x, int y,
							 int width, int height,
							 ImageObserver observer) {
		if (img instanceof DoubleBufferImage)
			return real.drawImage(((DoubleBufferImage) img).getRealImage(),
					x, y, width, height, observer);
		else
			return real.drawImage(img, (int) (x * scale), (int) (y * scale),
					(int) (width * scale), (int) (height * scale),
					observer);
	}

	public boolean drawImage(Image img, int x, int y,
							 Color bgcolor,
							 ImageObserver observer) {
		if (img instanceof DoubleBufferImage)
			return real.drawImage(((DoubleBufferImage) img).getRealImage(),
					x, y, bgcolor, observer);
		else
			return real.drawImage(img, (int) (x * scale), (int) (y * scale),
					(int) (img.getWidth(observer) * scale),
					(int) (img.getHeight(observer) * scale),
					bgcolor, observer);
	}

	public boolean drawImage(Image img, int x, int y,
							 int width, int height,
							 Color bgcolor,
							 ImageObserver observer) {
		if (img instanceof DoubleBufferImage)
			return real.drawImage(((DoubleBufferImage) img).getRealImage(),
					x, y, width, height, bgcolor, observer);
		else
			return real.drawImage(img, (int) (x * scale), (int) (y * scale),
					(int) (width * scale), (int) (height * scale),
					bgcolor, observer);
	}

	public boolean drawImage(Image img,
							 int dx1, int dy1, int dx2, int dy2,
							 int sx1, int sy1, int sx2, int sy2,
							 ImageObserver observer) {
		if (img instanceof DoubleBufferImage)
			return real.drawImage(((DoubleBufferImage) img).getRealImage(),
					dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
					observer);
		else
			return real.drawImage(img, (int) (dx1 * scale), (int) (dy1 * scale),
					(int) (dx2 * scale), (int) (dy2 * scale),
					(int) (sx1 * scale), (int) (sy1 * scale),
					(int) (sx2 * scale), (int) (sy2 * scale),
					observer);
	}

	public boolean drawImage(Image img,
							 int dx1, int dy1, int dx2, int dy2,
							 int sx1, int sy1, int sx2, int sy2,
							 Color bgcolor,
							 ImageObserver observer) {
		if (img instanceof DoubleBufferImage)
			return real.drawImage(((DoubleBufferImage) img).getRealImage(),
					dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
					bgcolor, observer);
		else
			return real.drawImage(img, (int) (dx1 * scale), (int) (dy1 * scale),
					(int) (dx2 * scale), (int) (dy2 * scale),
					(int) (sx1 * scale), (int) (sy1 * scale),
					(int) (sx2 * scale), (int) (sy2 * scale),
					bgcolor, observer);
	}

	public void dispose() {
		real.dispose();
	}

	/**
	 * A scaling extension of the FontMetrics class.  Measurements
	 * are performed on the actual, scaled font used on the screen,
	 * and then scaled back into user space.  The object pretends
	 * to be measuring the font specified by the user when obtaining
	 * this FontMetrics object.
	 */
	private class ScalingFontMetrics extends FontMetrics {

		/**
		 * A FontMetrics object on the real, scaled font.  All queries
		 * are forwarded to this object, and the results scaled back
		 * into user space.
		 */
		private FontMetrics real;

		/**
		 * The font which the user thinks he is asking about.
		 */
		private Font userFont;

		public ScalingFontMetrics(Font newUserFont, FontMetrics newReal) {
			super(null);
			userFont = newUserFont;
			real = newReal;
		}

		// Delegating methods below this line.  Only those methods which
		// the man page suggests as a minimal subset are implemented.

		public Font getFont() {
			return userFont;
		}

		public int getAscent() {
			return (int) (real.getAscent() / ScalingGraphics.this.getScale());
		}

		public int getLeading() {
			return (int) (real.getLeading() / ScalingGraphics.this.getScale());
		}

		public int getMaxAdvance() {
			return (int) (real.getMaxAdvance() / ScalingGraphics.this.getScale());
		}

		public int charWidth(char ch) {
			return (int) (real.charWidth(ch) / ScalingGraphics.this.getScale());
		}

		public int charsWidth(char[] data, int off, int len) {
			return (int) (real.charsWidth(data, off, len) /
					ScalingGraphics.this.getScale());
		}
	}

}
