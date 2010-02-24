/*
 * @(#)TextFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.ColorMap;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

import java.awt.*;
import java.io.*;
import java.util.List;

/**
 * A text figure.
 *
 * @see TextTool
 *
 * @version <$CURRENT_VERSION$>
 */
public class TextFigure
	extends AttributeFigure
	implements FigureChangeListener, TextHolder {

	private int fOriginX;
	private int fOriginY;

	// cache of the TextFigure's size
	transient private boolean fSizeIsDirty = true;
	transient private int fWidth;
	transient private int fHeight;

	private String fText;
	private Font fFont;
	private boolean fIsReadOnly;

	private Figure fObservedFigure = null;
	private OffsetLocator fLocator = null;

	private static String fgCurrentFontName = "Helvetica";
	private static int fgCurrentFontSize = 12;
	private static int fgCurrentFontStyle = Font.PLAIN;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 4599820785949456124L;
	private int textFigureSerializedDataVersion = 1;

	public TextFigure() {
		fOriginX = 0;
		fOriginY = 0;
		fFont = createCurrentFont();
		setAttribute(FigureAttributeConstant.FILL_COLOR, ColorMap.color("None"));
		fText = "";
		fSizeIsDirty = true;
	}

	/**
	 * @see org.jhotdraw.framework.Figure#moveBy(int, int)
	 */
	public void moveBy(int x, int y) {
		willChange();
		basicMoveBy(x, y);
		if (getLocator() != null) {
			getLocator().moveBy(x, y);
		}
		changed();
	}

	protected void basicMoveBy(int x, int y) {
		fOriginX += x;
		fOriginY += y;
	}

	/**
	 * @see org.jhotdraw.framework.Figure#basicDisplayBox(java.awt.Point, java.awt.Point)
	 */
	public void basicDisplayBox(Point newOrigin, Point newCorner) {
		fOriginX = newOrigin.x;
		fOriginY = newOrigin.y;
	}

	/**
	 * @see org.jhotdraw.framework.Figure#displayBox()
	 */
	public Rectangle displayBox() {
		Dimension extent = textExtent();
		return new Rectangle(fOriginX, fOriginY, extent.width, extent.height);
	}

	/**
	 * @see org.jhotdraw.standard.TextHolder#textDisplayBox()
	 */
	public Rectangle textDisplayBox() {
		return displayBox();
	}

	/**
	 * Tests whether this figure is read only.
	 */
	public boolean readOnly() {
		return fIsReadOnly;
	}

	/**
	 * Sets the read only status of the text figure.
	 */
	public void setReadOnly(boolean isReadOnly) {
		fIsReadOnly = isReadOnly;
	}

	/**
	 * Gets the font.
	 * @see org.jhotdraw.standard.TextHolder#getFont()
	 */
	public Font getFont() {
		return fFont;
	}

	/**
	 * Usually, a TextHolders is implemented by a Figure subclass. To avoid casting
	 * a TextHolder to a Figure this method can be used for polymorphism (in this
	 * case, let the (same) object appear to be of another type).
	 * Note, that the figure returned is not the figure to which the TextHolder is
	 * (and its representing figure) connected.
	 * @return figure responsible for representing the content of this TextHolder
	 * @see org.jhotdraw.standard.TextHolder#getRepresentingFigure()
	 */
	public Figure getRepresentingFigure() {
		return this;
	}

	/**
	 * Sets the font.
	 */
	public void setFont(Font newFont) {
		willChange();
		fFont = newFont;
		markDirty();
		changed();
	}

	/**
	 * Updates the location whenever the figure changes itself.
	 * @see org.jhotdraw.framework.Figure#changed()
	 */
	public void changed() {
		super.changed();
		//updateLocation();
	}

	/**
	 * A text figure understands the "FontSize", "FontStyle", and "FontName"
	 * attributes.
	 *
	 * @see org.jhotdraw.framework.Figure#getAttribute(java.lang.String)
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		return getAttribute(FigureAttributeConstant.getConstant(name));
	}

	/**
	 * A text figure understands the "FontSize", "FontStyle", and "FontName"
	 * attributes.
	 * @see org.jhotdraw.framework.Figure#getAttribute(org.jhotdraw.framework.FigureAttributeConstant)
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		Font font = getFont();
		if (attributeConstant.equals(FigureAttributeConstant.FONT_SIZE)) {
			return new Integer(font.getSize());
		}
		if (attributeConstant.equals(FigureAttributeConstant.FONT_STYLE)) {
			return new Integer(font.getStyle());
		}
		if (attributeConstant.equals(FigureAttributeConstant.FONT_NAME)) {
			return font.getName();
		}
		return super.getAttribute(attributeConstant);
	}

	/**
	 * A text figure understands the "FontSize", "FontStyle", and "FontName"
	 * attributes.
	 *
	 * @see org.jhotdraw.framework.Figure#setAttribute(java.lang.String, java.lang.Object)
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
		setAttribute(FigureAttributeConstant.getConstant(name), value);
	}

	/**
	 * A text figure understands the "FontSize", "FontStyle", and "FontName"
	 * attributes.
	 * @see org.jhotdraw.framework.Figure#setAttribute(org.jhotdraw.framework.FigureAttributeConstant, java.lang.Object)
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
		Font font = getFont();
		if (attributeConstant.equals(FigureAttributeConstant.FONT_SIZE)) {
			Integer s = (Integer) value;
			setFont(new Font(font.getName(), font.getStyle(), s.intValue()));
		}
		else if (attributeConstant.equals(FigureAttributeConstant.FONT_STYLE)) {
			Integer s = (Integer) value;
			int style = font.getStyle();
			if (s.intValue() == Font.PLAIN) {
				style = Font.PLAIN;
			}
			else {
				style = style ^ s.intValue();
			}
			setFont(new Font(font.getName(), style, font.getSize()));
		}
		else if (attributeConstant.equals(FigureAttributeConstant.FONT_NAME)) {
			String n = (String) value;
			setFont(new Font(n, font.getStyle(), font.getSize()));
		}
		else {
			super.setAttribute(attributeConstant, value);
		}
	}

	/**
	 * Gets the text shown by the text figure.
	 * @see org.jhotdraw.standard.TextHolder#getText()
	 */
	public String getText() {
		return fText;
	}

	/**
	 * Sets the text shown by the text figure.
	 * @see org.jhotdraw.standard.TextHolder#setText(java.lang.String)
	 */
	public void setText(String newText) {
		if (newText == null || !newText.equals(fText)) {
			willChange();
			fText = newText;
			markDirty();
			changed();
		}
	}

	/**
	 * Tests whether the figure accepts typing.
	 * @see org.jhotdraw.standard.TextHolder#acceptsTyping()
	 */
	public boolean acceptsTyping() {
		return !fIsReadOnly;
	}

	/**
	 * @see org.jhotdraw.figures.AttributeFigure#drawBackground(java.awt.Graphics)
	 */
	public void drawBackground(Graphics g) {
		Rectangle r = displayBox();
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	/**
	 * @see org.jhotdraw.figures.AttributeFigure#drawFrame(java.awt.Graphics)
	 */
	public void drawFrame(Graphics g) {
		g.setFont(fFont);
		g.setColor((Color) getAttribute(FigureAttributeConstant.TEXT_COLOR));
		FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(fFont);
		Rectangle r = displayBox();
		g.drawString(getText(), r.x, r.y + metrics.getAscent());
	}

	protected Dimension textExtent() {
		if (!fSizeIsDirty) {
			return new Dimension(fWidth, fHeight);
		}
		FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(fFont);
		fWidth = metrics.stringWidth(getText());
		fHeight = metrics.getHeight();
		fSizeIsDirty = false;
		return new Dimension(fWidth, fHeight);
	}

	protected void markDirty() {
		fSizeIsDirty = true;
	}

	/**
	 * Gets the number of columns to be overlaid when the figure is edited.
	 * @see org.jhotdraw.standard.TextHolder#overlayColumns()
	 */
	public int overlayColumns() {
		int length = getText().length();
		int columns = 20;
		if (length != 0) {
			columns = getText().length() + 3;
		}
		return columns;
	}

	/**
	 * @see org.jhotdraw.framework.Figure#handles()
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		handles.add(new NullHandle(this, RelativeLocator.northWest()));
		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		handles.add(new FontSizeHandle(this, RelativeLocator.southWest()));
		return new HandleEnumerator(handles);
	}

	/**
	 * @see org.jhotdraw.util.Storable#write(org.jhotdraw.util.StorableOutput)
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		Rectangle r = displayBox();
		dw.writeInt(r.x);
		dw.writeInt(r.y);
		dw.writeString(getText());
		dw.writeString(fFont.getName());
		dw.writeInt(fFont.getStyle());
		dw.writeInt(fFont.getSize());
		dw.writeBoolean(fIsReadOnly);
		dw.writeStorable(getObservedFigure());
		dw.writeStorable(getLocator());
	}

	/**
	 * @see org.jhotdraw.util.Storable#read(org.jhotdraw.util.StorableInput)
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		markDirty();
		basicDisplayBox(new Point(dr.readInt(), dr.readInt()), null);
		setText(dr.readString());
		fFont = new Font(dr.readString(), dr.readInt(), dr.readInt());
		fIsReadOnly = dr.readBoolean();

		setObservedFigure((Figure) dr.readStorable());
		if (getObservedFigure() != null) {
			getObservedFigure().addFigureChangeListener(this);
		}
		setLocator((OffsetLocator) dr.readStorable());
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
		s.defaultReadObject();

		if (getObservedFigure() != null) {
			getObservedFigure().addFigureChangeListener(this);
		}
		markDirty();
	}

	/**
	 * @see org.jhotdraw.standard.TextHolder#connect(org.jhotdraw.framework.Figure)
	 */
	public void connect(Figure figure) {
		if (getObservedFigure() != null) {
			getObservedFigure().removeFigureChangeListener(this);
		}

		setObservedFigure(figure);
		setLocator(new OffsetLocator(getObservedFigure().connectedTextLocator(this)));
		getObservedFigure().addFigureChangeListener(this);
		willChange();
		updateLocation();
		changed();
	}

	/**
	 * @see org.jhotdraw.framework.FigureChangeListener#figureChanged(org.jhotdraw.framework.FigureChangeEvent)
	 */
	public void figureChanged(FigureChangeEvent e) {
		willChange();
		updateLocation();
		changed();
	}

	/**
	 * @see org.jhotdraw.framework.FigureChangeListener#figureRemoved(org.jhotdraw.framework.FigureChangeEvent)
	 */
	public void figureRemoved(FigureChangeEvent e) {
		if (listener() != null) {
			Rectangle rect = invalidateRectangle(displayBox());
			listener().figureRemoved(new FigureChangeEvent(this, rect, e));
		}
	}

	/**
	 * @see org.jhotdraw.framework.FigureChangeListener#figureRequestRemove(org.jhotdraw.framework.FigureChangeEvent)
	 */
	public void figureRequestRemove(FigureChangeEvent e) {
	}

	/**
	 * @see org.jhotdraw.framework.FigureChangeListener#figureInvalidated(org.jhotdraw.framework.FigureChangeEvent)
	 */
	public void figureInvalidated(FigureChangeEvent e) {
	}

	/**
	 * @see org.jhotdraw.framework.FigureChangeListener#figureRequestUpdate(org.jhotdraw.framework.FigureChangeEvent)
	 */
	public void figureRequestUpdate(FigureChangeEvent e) {
	}

	/**
	 * Updates the location relative to the connected figure.
	 * The TextFigure is centered around the located point.
	 */
	protected void updateLocation() {
		if (getLocator() != null) {
			Point p = getLocator().locate(getObservedFigure());

			p.x -= size().width / 2 + fOriginX;
			p.y -= size().height / 2 + fOriginY;
			if (p.x != 0 || p.y != 0) {
				//willChange();
				basicMoveBy(p.x, p.y);
				//changed();
			}
		}
	}

	/**
	 * @see org.jhotdraw.framework.Figure#release()
	 */
	public void release() {
		super.release();
		disconnect(getObservedFigure());
	}

	/**
	 * Disconnects a text holder from a connect figure.
	 * @see org.jhotdraw.standard.TextHolder#disconnect(org.jhotdraw.framework.Figure)
	 */
	public void disconnect(Figure disconnectFigure) {
		if (disconnectFigure != null) {
			disconnectFigure.removeFigureChangeListener(this);
		}
		setLocator(null);
		setObservedFigure(null);
	}

	protected void setObservedFigure(Figure newObservedFigure) {
		fObservedFigure = newObservedFigure;
	}

	public Figure getObservedFigure() {
		return fObservedFigure;
	}

	protected void setLocator(OffsetLocator newLocator) {
		fLocator = newLocator;
	}

	protected OffsetLocator getLocator() {
		return fLocator;
	}

	/**
	 * @see org.jhotdraw.framework.Figure#getTextHolder()
	 */
	public TextHolder getTextHolder() {
		return this;
	}

	/**
	 * Creates the current font to be used for new text figures.
	 */
	static public Font createCurrentFont() {
		return new Font(fgCurrentFontName, fgCurrentFontStyle, fgCurrentFontSize);
	}

	/**
	 * Sets the current font name
	 */
	static public void setCurrentFontName(String name) {
		fgCurrentFontName = name;
	}

	/**
	 * Sets the current font size.
	 */
	static public void setCurrentFontSize(int size) {
		fgCurrentFontSize = size;
	}

	/**
	 * Sets the current font style.
	 */
	static public void setCurrentFontStyle(int style) {
		fgCurrentFontStyle = style;
	}
}
