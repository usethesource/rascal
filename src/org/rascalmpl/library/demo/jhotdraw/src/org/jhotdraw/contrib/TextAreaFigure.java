/*
 *  @(#)TextAreaFigure.java
 *
 *  Project:		JHotdraw - a GUI framework for technical drawings
 *  http://www.jhotdraw.org
 *  http://jhotdraw.sourceforge.net
 *  Copyright:	? by the original author(s) and all contributors
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.util.*;
import java.util.List;

import org.jhotdraw.figures.AttributeFigure;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.FigureChangeEvent;
import org.jhotdraw.framework.FigureChangeListener;
import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.*;

/**
 * A TextAreaFigure contains formatted text.<br>
 * It automatically rearranges the text to fit its allocated display area,
 * breaking the lines at word boundaries whenever possible.<br>
 * The text can contain either LF or CRLF sequences to separate paragraphs,
 * as well as tab characters for table like formatting and alignment.<br>
 * Currently the tabs are distributed at regular intervals as determined by
 * the TabSize property. Tabs align correctly with either fixed
 * or variable fonts.<br>
 * If, when resizing, the vertical size of the display box is not enough to
 * display all the text, TextAreaFigure displays a dashed red line at the
 * bottom of the figure to indicate there is hidden text.<br>
 * TextAreFigure uses all standard attributes for the area rectangle,
 * ie: FillColor, PenColor for the border, FontSize, FontStyle, and FontName,
 * as well as four additional attributes LeftMargin, RightMargin, TopMargin,
 * and TabSize.<br>
 *
 * @author    Eduardo Francos - InContext
 * @created   26 avril 2002
 * @version   <$CURRENT_VERSION$>
 */

public class TextAreaFigure extends AttributeFigure
		 implements FigureChangeListener, TextHolder {

	/** True if the paragraph's cache needs to be reconstructed */
	protected boolean fTextIsDirty = true;
	/** True if the sizing needs to be recalculated */
	protected transient boolean fSizeIsDirty = true;

	/** The current display box for the figure */
	private Rectangle fDisplayBox;

	/** Paragraph cache resulting from splitting the text */
	protected List fParagraphs;

	/** The text */
	protected String fText;
	/** The current font */
	protected Font fFont;
	/**
	 * True if the font has changed and font related calculations need to be remade
	 */
	protected boolean fFontIsDirty = true;

	/** The width of the current font */
	protected float fFontWidth;
	/**
	 * Map of attributes for the AttributedString used for the figure's text.
	 * Currently it just uses one single attribute with the figure's current font.
	 */
	protected Hashtable attributesMap = new Hashtable();

	/** True if the figure is read only */
	protected boolean fIsReadOnly;

	/** A connected figure */
	protected Figure fObservedFigure = null;
	/** Description of the Field */
	protected OffsetLocator fLocator = null;

	final static long serialVersionUID = 4993631445423148845L;

	// make sure required default attributes are set
	static {
		initDefaultAttribute("LeftMargin", new Float(5));
		initDefaultAttribute("RightMargin", new Float(5));
		initDefaultAttribute("TopMargin", new Float(5));
		initDefaultAttribute("TabSize", new Float(8));
	}


	/** Constructor for the TextAreaFigure object */
	public TextAreaFigure() {
		fParagraphs = CollectionsFactory.current().createList();
		fDisplayBox = new Rectangle(0, 0, 30, 15);
		fFont = createFont();
		fText = new String("");

		fSizeIsDirty = true;
		fTextIsDirty = true;
		fFontIsDirty = true;
	}


	/**
	 * Gets the text of the figure
	 *
	 * @return   The text value
	 */
	public String getText() {
		return fText;
	}


	/**
	 * Sets the text of the figure
	 *
	 * @param newText  The new text value
	 */
	public void setText(String newText) {
		if (newText == null || !newText.equals(fText)) {
			markTextDirty();
			fText = newText;
			changed();
		}
	}

	/**
	 * Returns the display box for the text
	 *
	 * @return   Description of the Return Value
	 */
	public Rectangle textDisplayBox() {
		return displayBox();
	}

	/**
	 * Creates the font from current attributes.
	 *
	 * @return   Description of the Return Value
	 */
	public Font createFont() {
		return new Font(
				(String)getAttribute("FontName"),
				((Integer)getAttribute("FontStyle")).intValue(),
				((Integer)getAttribute("FontSize")).intValue());
	}

	public boolean isReadOnly()
	{
		return fIsReadOnly;
	}

	public void setReadOnly(boolean newReadOnly)
	{
		fIsReadOnly = newReadOnly;
	}

	/**
	 * Tests whether the figure accepts typing.
	 *
	 * @return   Description of the Return Value
	 */
	public boolean acceptsTyping() {
		return !isReadOnly();
	}


	/**
	 * Called whenever the something changes that requires text recomputing
	 */
	protected void markTextDirty() {
		setTextDirty(true);
	}


	/**
	 * Sets the textDirty attribute of the TextAreaFigure object
	 *
	 * @param newTextDirty  The new textDirty value
	 */
	protected void setTextDirty(boolean newTextDirty) {
		fTextIsDirty = newTextDirty;
	}


	/**
	 * Gets the textDirty attribute of the TextAreaFigure object
	 *
	 * @return   The textDirty value
	 */
	public boolean isTextDirty() {
		return fTextIsDirty;
	}


	/**
	 * Called whenever the something changes that requires size recomputing
	 */
	protected void markSizeDirty() {
		setSizeDirty(true);
	}


	/**
	 * Called to set the dirty status of the size
	 *
	 * @param newSizeIsDirty  The new sizeDirty value
	 */
	public void setSizeDirty(boolean newSizeIsDirty) {
		fSizeIsDirty = newSizeIsDirty;
	}


	/**
	 * Returns the current size dirty status
	 *
	 * @return   The sizeDirty value
	 */
	public boolean isSizeDirty() {
		return fSizeIsDirty;
	}

	/**
	 * Gets the font.
	 *
	 * @return   The font value
	 */
	public Font getFont() {
		return fFont;
	}

	/**
	 * Sets the font.
	 *
	 * @param newFont  The new font value
	 */
	public void setFont(Font newFont) {
		if(newFont == null) {
			throw new IllegalArgumentException();
		}
		willChange();
		fFont = newFont;
		markSizeDirty();
		markFontDirty();
		attributesMap = new Hashtable(1);
		attributesMap.put(TextAttribute.FONT, newFont);
		changed();
	}

	/**
	 * Gets the number of columns to be overlaid when the figure is edited.<br>
	 * This method is mandatory by the TextHolder interface but is not
	 * used by the TextAreaFigure/TextAreaTool couple because the overlay always
	 * covers the text area display box
	 *
	 * @return   the number of overlay columns
	 */
	public int overlayColumns() {
		return 0;
	}

	/**
	 * Sets the display box for the figure
	 *
	 * @param origin  origin point
	 * @param corner  corner point
	 * @see           Figure
	 */
	public void basicDisplayBox(Point origin, Point corner) {
		Dimension prevSize = fDisplayBox.getSize();
		fDisplayBox = new Rectangle(origin);
		fDisplayBox.add(corner);
		if (!fDisplayBox.getSize().equals(prevSize)){
		   markSizeDirty();
		}
	}

	/**
	 * Returns an iterator of standard sizing handles to manipulate the figure
	 *
	 * @return   Description of the Return Value
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		BoxHandleKit.addHandles(this, handles);
		return new HandleEnumerator(handles);
	}

	/**
	 * Returns the current display box for the figure
	 *
	 * @return   Description of the Return Value
	 */
	public Rectangle displayBox() {
		return new Rectangle(
				fDisplayBox.x,
				fDisplayBox.y,
				fDisplayBox.width,
				fDisplayBox.height);
	}


	/**
	 * Moves the figure the supplied offset
	 *
	 * @param x  x displacement
	 * @param y  y displacement
	 */
	public void moveBy(int x, int y) {
		willChange();
		basicMoveBy(x, y);
		if (fLocator != null) {
			fLocator.moveBy(x, y);
		}
		changed();
	}

	/**
	 * Moves the figure the supplied offset
	 *
	 * @param x  x displacement
	 * @param y  y displacement
	 */
	protected void basicMoveBy(int x, int y) {
		fDisplayBox.translate(x, y);
	}

	/**
	 * Draws the background for the figure. Called by the superclass with the colors
	 * set from the current attribute values
	 *
	 * @param g  The graphics to use for the drawing
	 */
	public void drawBackground(Graphics g) {
		Rectangle r = displayBox();
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	/**
	 * Draws the figure. Overriden so as to draw the text once everything
	 * else has been drawn
	 *
	 * @param g  The graphics to use for the drawing
	 */
	public void draw(Graphics g) {
		super.draw(g);
		drawText(g, displayBox());
	}

	/**
	 * Draws the frame around the text
	 *
	 * @param g  The graphics to use for the drawing
	 */
	public void drawFrame(Graphics g) {
		Rectangle r = displayBox();
		g.setColor((Color)getAttribute("FrameColor"));
		g.drawRect(r.x, r.y, r.width, r.height);
	}


	/**
	 * Formats and draws the text for the figure
	 *
	 * @param g           the graphics for the drawing. It can be null when
	 * called just to compute the size
	 * @param displayBox  the display box  within which the text should be formatted and drawn
	 * @return            Description of the Return Value
	 */
	protected float drawText(Graphics g, Rectangle displayBox) {
		Graphics2D g2 = null;
		Shape savedClipArea = null;
		Color savedFontColor = null;
		Rectangle2D clipRect = null;
		RenderingHints savedRenderingHints = null;

		if (g != null) {
			g2 = (Graphics2D)g;
			savedRenderingHints = g2.getRenderingHints();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			//Font savedFont = g2.getFont();
			savedFontColor = g2.getColor();
			savedClipArea = g2.getClip();
			if(savedClipArea != null) {
				clipRect = displayBox.createIntersection((Rectangle2D)savedClipArea);
			}
			else {
				clipRect = displayBox;
			}
			g2.setClip(clipRect);
			Color textColor = getTextColor();
			if (!ColorMap.isTransparent(textColor)) {
				g2.setColor(textColor);
			}
			g2.setFont(getFont());
		}
		FontRenderContext fontRenderCtx = new FontRenderContext(null, false, false);

		// split the text into paragraphs
		prepareText();

		float leftMargin = displayBox.x + ((Float)getAttribute("LeftMargin")).floatValue();
		float rightMargin = displayBox.x + displayBox.width - ((Float)getAttribute("RightMargin")).floatValue();
		float topMargin = displayBox.y + ((Float)getAttribute("TopMargin")).floatValue();

		/**
		 * @todo   we prepare stops for 40 tabs which should be enough to handle
		 * all normal cases, but a better means should/could be implemented
		 */
		float[] tabStops = new float[40];
		// tabSize is in pixels
		float tabSize = ((Float)getAttribute("TabSize")).floatValue() * getFontWidth();
		float tabPos = tabSize;
		for (int tabCnt = 0; tabCnt < 40; tabCnt++) {
			tabStops[tabCnt] = tabPos + leftMargin;
			tabPos += tabSize;
		}

		/** Iterate on the paragraphs displaying each one in turn */
		float verticalPos = topMargin;
		Iterator paragraphs = fParagraphs.iterator();
		while (paragraphs.hasNext()) {
			String paragraphText = (String)paragraphs.next();

			// prepare tabs. Here we build an array with the character positions
			// of the tabs within the paragraph
			AttributedString attrText = new AttributedString(paragraphText);
			AttributedCharacterIterator paragraphIter = attrText.getIterator();
			int[] tabLocations = new int[paragraphText.length()];
			int tabCount = 0;
			for (char c = paragraphIter.first(); c != CharacterIterator.DONE; c = paragraphIter.next()) {
				if (c == '\t') {
					tabLocations[tabCount++] = paragraphIter.getIndex();
				}
			}
			tabLocations[tabCount] = paragraphIter.getEndIndex() - 1;

			// tabs done. Replace tab characters with spaces. This to avoid
			// a strange behaviour where the layout is a lot slower and
			// the font get's changed. If anybody knows why this so please
			// tell me.
			paragraphText = paragraphText.replace('\t', ' ');
			attrText = new AttributedString(paragraphText, attributesMap);
			paragraphIter = attrText.getIterator();

			// Now tabLocations has an entry for every tab's offset in
			// the text.  For convenience, the last entry in tabLocations
			// is the offset of the last character in the text.

			LineBreakMeasurer measurer = new LineBreakMeasurer(paragraphIter, fontRenderCtx);
			int currentTab = 0;

			while (measurer.getPosition() < paragraphIter.getEndIndex()) {
				// Lay out and draw each line.  All segments on a line
				// must be computed before any drawing can occur, since
				// we must know the largest ascent on the line.
				// TextLayouts are computed and stored in a collection;
				// their horizontal positions are stored in a parallel
				// collection.

				// lineContainsText is true after first segment is drawn
				boolean lineContainsText = false;
				boolean lineComplete = false;
				float maxAscent = 0;
				float maxDescent = 0;
				float horizontalPos = leftMargin;
				List layouts = CollectionsFactory.current().createList(1);
				List penPositions = CollectionsFactory.current().createList(1);

				while (!lineComplete) {
					float wrappingWidth = rightMargin - horizontalPos;
					// ensure wrappingWidth is at least 1
					wrappingWidth = Math.max(1, wrappingWidth);
					TextLayout layout =
							measurer.nextLayout(wrappingWidth,
							tabLocations[currentTab] + 1,
							lineContainsText);

					// layout can be null if lineContainsText is true
					if (layout != null) {
						layouts.add(layout);
						penPositions.add(new Float(horizontalPos));
						horizontalPos += layout.getAdvance();
						maxAscent = Math.max(maxAscent, layout.getAscent());
						maxDescent = Math.max(maxDescent,
								layout.getDescent() + layout.getLeading());
					}
					else {
						lineComplete = true;
					}

					lineContainsText = true;

					if (measurer.getPosition() == tabLocations[currentTab] + 1) {
						currentTab++;
					}

					if (measurer.getPosition() == paragraphIter.getEndIndex()) {
						lineComplete = true;
					}
					else if (horizontalPos >= tabStops[tabStops.length - 1]) {
						lineComplete = true;
					}

					if (!lineComplete) {
						// move to next tab stop
						int j;
						for (j = 0; horizontalPos >= tabStops[j]; j++) {
						}
						horizontalPos = tabStops[j];
					}
				}

				// set the vertical position for the line
				verticalPos += maxAscent;

				// now iterate through layouts and draw them
				Iterator layoutEnum = layouts.iterator();
				Iterator positionEnum = penPositions.iterator();
				while (layoutEnum.hasNext()) {
					TextLayout nextLayout = (TextLayout)layoutEnum.next();
					Float nextPosition = (Float)positionEnum.next();
					if (g2 != null) {
						nextLayout.draw(g2, nextPosition.floatValue(), verticalPos);
					}
				}

				// keep track of the highest (actually lowest) position for the
				// next iteration
				verticalPos += maxDescent;
			}
		}

		// if the last displayed line is not visible because the displayBox is
		// too small then draw a dashed red line at the bottom
		if (g2 != null && verticalPos > clipRect.getMaxY() && clipRect.getMaxY() == displayBox.getMaxY()) {
			Stroke savedStroke = g2.getStroke();
			float[] dash = new float[2];
			dash[0] = 2f;
			dash[1] = 4f;
			g2.setStroke(new BasicStroke(
					1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
					1f, dash, 0f));
			g2.setColor(Color.red);
			g2.drawLine((int)clipRect.getMinX() + 1, (int)clipRect.getMaxY() - 1,
					(int)clipRect.getMaxX() - 1, (int)clipRect.getMaxY() - 1);
			g2.setStroke(savedStroke);
		}

		// restore saved graphic attributes
		if (g2 != null) {
			if(savedClipArea != null) {
			g2.setClip(savedClipArea);
			}
			g2.setColor(savedFontColor);
			g2.setRenderingHints(savedRenderingHints);
		}

		// and return the final text height
		return verticalPos;
	}

	/**
	 * Splits the text into paragraphs. A paragraph is delimited by a LF or CRLF.
	 * If the paragraph is empty it returns a single space so the display logic has
	 * something to work with
	 */
	protected void prepareText() {
		if (!isTextDirty()) {
			return;
		}

		fParagraphs = CollectionsFactory.current().createList();
		String paragraphText;
		Point pos = new Point(-1, -1);

		while ((paragraphText = getNextParagraph(fText, pos)) != null) {
			if (paragraphText.length() == 0) {
				paragraphText = " ";
			}
			fParagraphs.add(paragraphText);
		}
		setTextDirty(false);
	}

	/**
	 * Gets the next paragraph in the supplied string<br>
	 * Paragraphs are defined by a LF or CRLF sequence<br>
	 * Scanning starts from the next characters as given by the pos.y value
	 *
	 * @param text  the text to break into paragraphs
	 * @param pos   a point where pos.x is the position of the first character of the paragraph in
	 * the string and pos.y is the last
	 * @return      The text for the paragraph
	 */
	protected String getNextParagraph(String text, Point pos) {
		int start = pos.y + 1;

		if (start >= text.length()) {
			return null;
		}
		pos.x = start;

		int end = text.indexOf('\n', start);
		if (end == -1) {
			end = text.length();
		}
		pos.y = end;
		// check for "\r\n" sequence
		if (text.charAt(end - 1) == '\r') {
			return text.substring(start, end - 1);
		}
		else {
			return text.substring(start, end);
		}
	}

	/**
	 * A text area figure uses the "LeftMargin", "RightMargin", "TopMargin",
	 * "TabSize", "FontSize", "FontStyle", and "FontName" attributes
	 *
	 * @param name the attribute's name
	 * @return     the attribute value
	 * @deprecated use getAttribute(FigureAttributeConstant)
	 */
	public Object getAttribute(String name) {
		return super.getAttribute(name);
	}


	/**
	 * A text area figure uses the "LeftMargin", "RightMargin",
	 * "TopMargin", "TabSize", "FontSize", "FontStyle", and "FontName"
	 * attributes
	 *
	 * @param name  the new attribute name
	 * @param value the new attribute value
	 * @deprecated use setAttribute(FigureAttributeConstant, Object)
	 */
	public void setAttribute(String name, Object value) {
		// we need to create a new font if one of the font attributes
		Font font = getFont();
		if (name.equals("FontSize")) {
			Integer s = (Integer)value;
			setFont(new Font(font.getName(), font.getStyle(), s.intValue()));
			// store the attribute
			super.setAttribute(name, value);
		}
		else if (name.equals("FontStyle")) {
			Integer s = (Integer)value;
			int style = font.getStyle();
			if (s.intValue() == Font.PLAIN) {
				style = Font.PLAIN;
			}
			else {
				style = style ^ s.intValue();
			}
			setFont(new Font(font.getName(), style, font.getSize()));
			// store the attribute
			super.setAttribute(name, new Integer(style));
		}
		else if (name.equals("FontName")) {
			String n = (String)value;
			setFont(new Font(n, font.getStyle(), font.getSize()));
			// store the attribute
			super.setAttribute(name, value);
		}
		else {
			// store the attribute
			super.setAttribute(name, value);
		}
	}

	/**
	 * Writes the figure to StorableOutput
	 *
	 * @param dw  the output storable
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fDisplayBox.x);
		dw.writeInt(fDisplayBox.y);
		dw.writeInt(fDisplayBox.width);
		dw.writeInt(fDisplayBox.height);
		dw.writeString(fText);
		dw.writeBoolean(fIsReadOnly);
		dw.writeStorable(fObservedFigure);
		dw.writeStorable(fLocator);
	}

	/**
	 * Reads the figure from StorableInput
	 *
	 * @param dr            Description of the Parameter
	 * @throws IOException  the inout storable
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);

		markSizeDirty();
		markTextDirty();
		markFontDirty();

		fDisplayBox.x = dr.readInt();
		fDisplayBox.y = dr.readInt();
		fDisplayBox.width = dr.readInt();
		fDisplayBox.height = dr.readInt();
		fText = dr.readString();
		fIsReadOnly = dr.readBoolean();

		fObservedFigure = (Figure)dr.readStorable();
		if (fObservedFigure != null) {
			fObservedFigure.addFigureChangeListener(this);
		}
		fLocator = (OffsetLocator)dr.readStorable();

		setFont(createFont());
	}

	/**
	 * Reads the figure from an object stream
	 *
	 * @param s                        the input stream
	 * @throws ClassNotFoundException  thrown by called methods
	 * @throws IOException             thrown by called methods
	 */
	protected void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		if (fObservedFigure != null) {
			fObservedFigure.addFigureChangeListener(this);
		}
		markSizeDirty();
		markTextDirty();
		markFontDirty();
	}

	/**
	 * Connects the figure to another figure
	 *
	 * @param figure  the connecting figure
	 */
	public void connect(Figure figure) {
		if (fObservedFigure != null) {
			fObservedFigure.removeFigureChangeListener(this);
		}

		fObservedFigure = figure;
		fLocator = new OffsetLocator(figure.connectedTextLocator(this));
		fObservedFigure.addFigureChangeListener(this);
		updateLocation();
	}

	/**
	 * Disconnects a text holder from a connect figure.
	 *
	 * @param disconnectFigure  the disconnecting figure
	 */
	public void disconnect(Figure disconnectFigure) {
		if (disconnectFigure != null) {
			disconnectFigure.removeFigureChangeListener(this);
		}
		fLocator = null;
	}

	/**
	 * Description of the Method
	 * @todo   Implement this org.jhotdraw.framework.FigureChangeListener method
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureInvalidated(FigureChangeEvent e) {
	}

	/**
	 * A connected figure has changed, update the figure's location
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureChanged(FigureChangeEvent e) {
		updateLocation();
	}

	/**
	 * Updates the location relative to the connected figure.
	 * The TextAreaFigure is centered around the located point.
	 */
	protected void updateLocation() {
		if (fLocator != null) {
			Point p = fLocator.locate(fObservedFigure);

			p.x -= size().width / 2 + fDisplayBox.x;
			p.y -= size().height / 2 + fDisplayBox.y;
			if (p.x != 0 || p.y != 0) {
				willChange();
				basicMoveBy(p.x, p.y);
				changed();
			}
		}
	}

	/**
	 * The figure is about to be removed from another composite figure
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureRemoved(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRemoved(new FigureChangeEvent(this));
		}
	}

	/**
	 * A request to remove the figure from another composite figure
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureRequestRemove(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRequestRemove(new FigureChangeEvent(this));
		}
	}

	/**
	 * @param e  Description of the Parameter
	 */
	public void figureRequestUpdate(FigureChangeEvent e) {
		// @todo:   Implement this org.jhotdraw.framework.FigureChangeListener method
	}

	/**
	 * Gets the font width for the active font. This is by convention the width of
	 * the 'W' character, the widest one
	 *
	 * @return   The fontWidth value
	 */
	protected float getFontWidth() {
		updateFontInfo();
		return fFontWidth;
	}

	/** Retrieve all Font information needed */
	protected void updateFontInfo() {
		if (!isFontDirty()) {
			return;
		}
		fFontWidth = (int) getFont().getMaxCharBounds(new FontRenderContext(null, false, false)).getWidth(); 

		setFontDirty(false);
	}

	/**
	 * Gets the text color of a figure. This is a convenience
	 * method.
	 *
	 * @return   The textColor value
	 * @see      #getAttribute
	 */
	public Color getTextColor() {
		return (Color)getAttribute("TextColor");
	}

	/**
	 * Gets the empty attribute of the figure. True if there is no text
	 *
	 * @return   The empty value
	 */
	public boolean isEmpty() {
		return (fText.length() == 0);
	}

	/**
	 * Called whenever the something changes that requires font recomputing
	 */
	protected void markFontDirty() {
		setFontDirty(true);
	}

	/**
	 * Gets the fontDirty attribute of the TextAreaFigure object
	 *
	 * @return   The fontDirty value
	 */
	public boolean isFontDirty() {
		return fFontIsDirty;
	}

	/**
	 * Sets the fontDirty attribute of the TextAreaFigure object
	 *
	 * @param newFontIsDirty  The new fontDirty value
	 */
	public void setFontDirty(boolean newFontIsDirty) {
		fFontIsDirty = newFontIsDirty;
	}

	/**
	 * Usually, a TextHolders is implemented by a Figure subclass. To avoid casting
	 * a TextHolder to a Figure this method can be used for polymorphism (in this
	 * case, let the (same) object appear to be of another type).
	 * Note, that the figure returned is not the figure to which the TextHolder is
	 * (and its representing figure) connected.
	 * @return figure responsible for representing the content of this TextHolder
	 */
	public Figure getRepresentingFigure() {
		return this;
	}
}
