/*
 * @(#)HTMLTextAreaFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import org.jhotdraw.contrib.TextAreaFigure;
import org.jhotdraw.figures.RectangleFigure;
import org.jhotdraw.framework.*;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;

/**
 * An HTMLTextAreaFigure contains HTML formatted text.<br>
 * Formatting is done internally by a JLabel component, so all display features
 * and constrains that apply for a JLabel apply also for an HTMLTextAreaFigure,
 * including text and images, as in any Web browser, even allowing for contents
 * residing on external Web sources. But don't forget that this is <b>NOT</b> a Web
 * browser, so HTML contents cannot be standard Web pages with headers, stylesheets,
 * javascript and who knows what else, just plain down to earth HTML code.
 * <p>
 * In order to automatically integrate "external" attributes like FillColor,
 * FontName, etc, HTMLTextAreaFigure encapsulates the text the user types in the
 * editor within a table with a single cell occupying the whole area.<br>
 * Here is what the HTML code passed to the JLabel looks like:<br>
 * <code>
 * &lt;html&gt;<br>
 * &lt;table border='0' width='area.width' height='area.height'
 * cellpadding='0' cellspacing='0' bgcolor='&FillColor;'&gt;<br>
 * &lt;tr&gt;<br>
 * &lt;td width='100%'&gt;<br>
 * &lt;font face='&FontName;' color='&TextColor;' size='&FontSize;'&gt;<br>
 * &lt;!-- add italic and bold attributes if required--&gt;<br>
 * &lt;i&gt;<br>
 * &lt;b&gt;<br>
 * ============&gt> User's contents go here &lt;============<br>
 * &lt;!-- close italic and bold attributes if required --&gt;<br>
 * &lt;/b&gt;<br>
 * &lt;/i&gt;<br>
 * &lt;/font&gt;<br>
 * &lt;/td&gt;<br>
 * &lt;/tr&gt;<br>
 * &lt;/table&gt;<br>
 * &lt;/html&gt;<br>
 * </code><br>
 * It is possible to write <i>raw</i> HTML code by calling
 * <code>setRawHTML(true)</code>. In that case no tags are issued.<br>
 * The user is then responsible for applying the figure attributes and in
 * general is responsible for the whole display.
 * This setting can be dynamically toggled as needed.<br>
 * Note that JLabel resets the font to its own default whenever it encounters
 * an HTML structure, like a table or a header tag. I couldn't find a workaround
 * for what can/should be called a bug. Normal browsers do not behave like this.<p>
 *
 * Internal attributes like FillColor or FontName are exposed as special SGML
 * entities using the standard SGML entity notation, ex: <code>&FillColor;</code>.<br>
 * Any attribute associated to the figure can be used and will be replaced with
 * an appropriate value from a ContentsProducer (see below) or its
 * toString() value if no specific ContentProducer is defined.<p>
 *
 * The HTML display and layouting can be time consuming, quite fast in most cases,
 * unless the HTML structure is complicated. This can become a serious penalty
 * when working with a large number of complicated figures.<br>
 * To help in this issue HTMLTextAreaFigure offers two display modes, DirectDraw,
 * where the HTML layout logic is executed every time the figure is displayed, and
 * BufferedDraw, where HTMLTextAreaFigure creates an in-memory image of the
 * resulting layout and uses the image for fast display until a change requires
 * to regenerate the image.<br>
 * The BufferedDraw mode is as fast as an image display can be, but it consumes
 * more memory than the DirectDraw mode, which in turn is slower.<br>
 * The setting is specific to each figure instance and it can be dynamically
 * toggled at any time, so it is possible to fine tune when and which figures
 * use either one of the drawing modes.<p>
 *
 * Remember the attributes based SGML entities?<br>
 * If you set the figure to be read only, so not allowing the user to directly
 * edit the HTML contens, then it is possible to use HTMLTextAreaFigures to
 * produce very elaborate and complex information layout.<br>
 * You create HTML templates for each figure layout you want to use and set them
 * as the text of the figure. Within the template text you place field names
 * wherever needed as you would for a Web page, then each figure using the template
 * associates the field values as attributes of the figure. The attribute exposure
 * feature will substitute the entity names with the current attribute's value.<br>
 * Please refer to the accompanying sample program to see in detail the multiple
 * ways this feature can enhance your drawings.<p>
 *
 * <b>ContentProducers</b><br>
 * As stated above, entities referenced in the HTML template code are replaced by
 * their current value in the drawn figure. The values themselves are provided
 * by ContentProducers.<br>
 * For a detailed description of ContentProducers please refer to their
 * own documentation, but to make it simple, a ContentProducer is an object that
 * implements the method <code>getContent</code> and is registered to produce
 * content for either specific entities, or entity classes.<br>
 * An entity class is the class of the attribute containing its value, ie: an
 * attribute containing a URL has class URL (<code>attribute.getClass()</code>),
 * and an URLContentProducer can be associated to it so that when the layout
 * needs the entity's value, the producer's getContent() method is called and the
 * returned value (ex: contents from a Web page, FTP file or disk file) is used
 * to replace the entity in the displayed figure.<br>
 * The ContentProducer can return either a String, in which case it is used
 * <b>as is</b>, or another Object. In the later case HTMLTextAreaFigure will
 * continue calling registered ContentProviders depending on the class of the
 * returned Object until it either gets a final String, or null. If null then
 * the entity is considered as unknown and left as is in the displayed text. To
 * make it dissapear alltogether the producer should return an empty String.<p>
 * HTMLTextAreaFigure registers default ContentProducers:
 * AttributeFigureContentProducer for the intrinsic attributes of the figure
 * (height, width, font name, etc.), URLContentProducer for URL attributes,
 * HTMLColorContentProducer for HTML color encoding and for embedded
 * TextAreaFigure and HTMLTextAreaFigure classes. That's right, you can embed
 * a TextAreaFigure or HTMLTextAreaFigure contents inside an HTMLTextAreaFigure
 * recursively for as many levels as your CPU and memory will allow.<br>
 * For instance, the main figure can consists of an HTML table where each
 * cell's contents come from a different HTMLTextAreaFigure.
 *
 * @author    Eduardo Francos - InContext
 * @created   7 May 2002
 * @version   <$CURRENT_VERSION$>
 */
public class HTMLTextAreaFigure extends TextAreaFigure
		 implements HTMLContentProducerContext, FigureChangeListener {

	/** Start marker for embedded attribute values */
	public final static char START_ENTITY_CHAR = '&';

	/** End marker for embedded attribute values */
	public final static char END_ENTITY_CHAR = ';';

	/** Marker escape character */
	public final static char ESCAPE_CHAR = '\\';

	/** holder for the image used for the display */
	private transient DisposableResourceHolder fImageHolder;

	/** The label used for in-memory display */
	private transient JLabel fDisplayDelegate;

	/** True if using direct drawing, false if using the memory image */
	private boolean fUseDirectDraw = false;

	/** True if the memory image should be regenerated */
	private boolean fIsImageDirty = true;

	/** Description of the Field */
	private boolean fRawHTML = false;

	/** Supplier for intrinsic data */
	private transient ContentProducer fIntrinsicContentProducer;

	/** Description of the Field */
	private static ContentProducerRegistry fDefaultContentProducers = new ContentProducerRegistry();
	// initialize the default content producers for HTMLTextAreaFigure figures
	static {
		fDefaultContentProducers.registerContentProducer(TextAreaFigure.class, new TextHolderContentProducer());
		fDefaultContentProducers.registerContentProducer(Color.class, new HTMLColorContentProducer());
	}

	/** Description of the Field */
	private transient ContentProducerRegistry fContentProducers = null;

	/** The figure used to draw the frame of the area */
	private Figure fFrameFigure = null;

	// make sure required default attributes are set
	static {
		initDefaultAttribute("XAlignment", new Integer(SwingConstants.LEFT));
		initDefaultAttribute("YAlignment", new Integer(SwingConstants.TOP));
		initDefaultAttribute("LeftMargin", new Float(5));
		initDefaultAttribute("RightMargin", new Float(5));
		initDefaultAttribute("TopMargin", new Float(5));
		initDefaultAttribute("BottomMargin", new Float(5));
		initDefaultAttribute("TabSize", new Float(8));
	}

	/** Constructor for the HTMLTextAreaFigure object */
	public HTMLTextAreaFigure() {
		initialize();
	}

	/**
	 * Clones a figure and initializes it
	 *
	 * @return   Description of the Return Value
	 * @see      Figure#clone
	 */
	public Object clone() {
		Object cloneObject = super.clone();
		((HTMLTextAreaFigure)cloneObject).initialize();
		return cloneObject;
	}

	/**
	 * Sets the display box for the figure
	 *
	 * @param origin  origin point
	 * @param corner  corner point
	 * @see           Figure
	 */
	public void basicDisplayBox(Point origin, Point corner) {
		super.basicDisplayBox(origin, corner);
		getFrameFigure().displayBox(displayBox());
	}

	/**
	 * Returns an iterator of standard sizing handles to manipulate the figure
	 *
	 * @return   Description of the Return Value
	 */
	public HandleEnumeration handles() {
		return getFrameFigure().handles();
//		List handles = CollectionsFactory.current().createList();
//		BoxHandleKit.addHandles(this, handles);
//		return new HandleEnumerator(handles);
	}

	/**
	 * True if the figure contains the point. The call is relayed to the frame figure
	 *
	 * @param x  Description of the Parameter
	 * @param y  Description of the Parameter
	 * @return   Description of the Return Value
	 */
	public boolean containsPoint(int x, int y) {
		return getFrameFigure().containsPoint(x, y);
	}

	/**
	 * Moves the figure by the specified displacement
	 *
	 * @param dx  Description of the Parameter
	 * @param dy  Description of the Parameter
	 */
	public void moveBy(int dx, int dy) {
		super.moveBy(dx, dy);
		getFrameFigure().moveBy(dx, dy);
	}

	/** Initializes the figure */
	protected void initialize() {
		fImageHolder = DisposableResourceManagerFactory.createStandardHolder(null);
		setFrameFigure(new RectangleFigure());

		// initialize the content producers
		setIntrinsicContentProducer(new HTMLContentProducer());
		fContentProducers = new ContentProducerRegistry(fDefaultContentProducers);

		markSizeDirty();
		markImageDirty();
		markTextDirty();
		markFontDirty();

		setAttribute(FigureAttributeConstant.POPUP_MENU, createPopupMenu());
	}

	/**
	 * Called whenever the something changes that requires size recomputing
	 */
	protected void markSizeDirty() {
		markImageDirty();
		super.markSizeDirty();
	}

	/**
	 * Called whenever the something changes that requires text recomputing
	 */
	protected void markTextDirty() {
		markImageDirty();
		super.markTextDirty();
	}

	/**
	 * Called whenever the something changes that requires font recomputing
	 */
	protected void markFontDirty() {
		markImageDirty();
		super.markFontDirty();
	}

	/**
	 * Draws the figure in the given graphics. Draw is a template
	 * method calling drawBackground followed by drawText then drawFrame.<br>
	 * HTMLTextAreaFigure displays in a different order tahn most figures to avoid
	 * smearing of the border when enclosed in a weird frame figure.<br>
	 * Also, there is no such thing as a transparent background so we always draw it.
	 *
	 * @param g  Description of the Parameter
	 * @todo     check possibility of clipping the contents from the background to have a
	 * transparent figure
	 */
	public void draw(Graphics g) {
		Color fill = getFillColor();
		g.setColor(fill);
		drawBackground(g);

		// we draw the text then the rame to avoid smearing
		drawText(g, displayBox());

		Color frame = getFrameColor();
		g.setColor(frame);
		drawFrame(g);
	}

	/**
	 * Draws the frame around the text. It gets the shape of the frame from the
	 * enclosing figure
	 *
	 * @param g  The graphics to use for the drawing
	 */
	public void drawFrame(Graphics g) {
		((Graphics2D)g).draw(getClippingShape());
	}

	/**
	 * Draws the background for the figure. It gets the shape of the frame from the
	 * enclosing figure
	 *
	 * @param g  The graphics to use for the drawing
	 */
	public void drawBackground(Graphics g) {
		((Graphics2D)g).fill(getClippingShape());
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
		Shape savedClip = null;

		if (g != null) {
			g2 = (Graphics2D)g;
			savedClip = g2.getClip();
		}

		Rectangle drawingBox = makeDrawingBox(displayBox);

		// drawing an empty displayBox is not possible
		if (drawingBox.isEmpty()) {
			return drawingBox.height;
		}

		if (g != null) {
			g2.clip(getClippingShape());
		}

		if (usesDirectDraw()) {
			drawTextDirect(g2, drawingBox);
		}
		else {
			fImageHolder.lock();
			if (isImageDirty()) {
				generateImage(drawingBox);
				setSizeDirty(false);
			}

			if (g2 != null) {
				g2.drawImage(getImage(), drawingBox.x, drawingBox.y, null);
			}
			fImageHolder.unlock();
		}
		if (g != null) {
			g2.setClip(savedClip);
		}

		// redraw the border to prevent smearing
		drawFrame(g);
		return displayBox.height;
	}

	/**
	 * Generates the HTML image to be used for fast BufferedDrawing
	 *
	 * @param drawingBox  Description of the Parameter
	 */
	protected void generateImage(Rectangle drawingBox) {
		// create the image and get its Graphics
		createImage(drawingBox.width, drawingBox.height);
		Graphics2D g2 = (Graphics2D)getImage().getGraphics();

		Rectangle finalBox = new Rectangle(drawingBox);
		finalBox.setLocation(0, 0);
		renderText(g2, finalBox);
		g2.dispose();
	}

	/**
	 * Draws the text directly onto the drawing, without using the cached figure
	 *
	 * @param g2          Description of the Parameter
	 * @param drawingBox  Description of the Parameter
	 */
	protected void drawTextDirect(Graphics2D g2, Rectangle drawingBox) {
		Shape savedClipArea = null;
		Color savedFontColor = null;
		//Font savedFont = null;
		//Rectangle2D clipRect = null;
		RenderingHints savedRenderingHints = null;

		if (g2 != null) {
			savedRenderingHints = g2.getRenderingHints();
			savedClipArea = g2.getClip();
			//savedFont = g2.getFont();
			savedFontColor = g2.getColor();
			g2.clip(drawingBox);
		}

		//float finalHeight = renderText(g2, drawingBox);

		// restore saved graphic attributes
		if (g2 != null) {
			g2.setClip(savedClipArea);
			g2.setColor(savedFontColor);
			g2.setRenderingHints(savedRenderingHints);
		}
	}

	/**
	 * Renders the HTML formatted text onto the supplied Graphics.<br>
	 * Rendering involves entity substitution and HTML contents preparation suitable
	 * for display by a JLabel.
	 *
	 * @param g2          Description of the Parameter
	 * @param drawingBox  Description of the Parameter
	 * @return            Description of the Return Value
	 * @todo              look for other HTML display providers as JLabel is kind of
	 * lousy at it
	 */
	protected float renderText(Graphics2D g2, Rectangle drawingBox) {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		// fill with background color
		g2.setBackground(getFillColor());
		g2.setColor(getFillColor());
		g2.clearRect(drawingBox.x, drawingBox.y, drawingBox.width, drawingBox.height);
		g2.fillRect(drawingBox.x, drawingBox.y, drawingBox.width, drawingBox.height);

		// get the text. Either in raw format or prepared for HTML display
		String text;
		if (isRawHTML()) {
			text = getText();
		}
		else {
			text = getHTMLText(getText(), getFont(),
					(String)getContentProducer(Color.class).getContent(this, FigureAttributeConstant.TEXT_COLOR_STR, getTextColor()),
					(String)getContentProducer(Color.class).getContent(this, FigureAttributeConstant.FILL_COLOR_STR, getFillColor()),
					drawingBox
					);
		}

		// perform entity keyword substitution
		text = substituteEntityKeywords(text);

		// create the JLabel used as delegate for drawing
		JLabel displayDelegate = getDisplayDelegate();
		displayDelegate.setText(text);
		displayDelegate.setBackground(getFillColor());

		// ensure the label covers the whole area
		displayDelegate.setLocation(0, 0);
		displayDelegate.setSize(drawingBox.width, drawingBox.height);
		displayDelegate.setHorizontalAlignment(((Integer)getAttribute(FigureAttributeConstant.XALIGNMENT)).intValue());
		displayDelegate.setVerticalAlignment(((Integer)getAttribute(FigureAttributeConstant.YALIGNMENT)).intValue());

		// finally display it
		SwingUtilities.paintComponent(
				g2,
				displayDelegate,
				getContainerPanel(displayDelegate, drawingBox),
				drawingBox.x,
				drawingBox.y,
				drawingBox.width,
				drawingBox.height);

		return drawingBox.height;
	}

	/**
	 * Builds the drawing box using the margins
	 *
	 * @param displayBox  Description of the Parameter
	 * @return            The drawing box
	 */
	protected Rectangle makeDrawingBox(Rectangle displayBox) {
		// get alignment information
		float leftMargin = ((Float)getAttribute(FigureAttributeConstant.LEFT_MARGIN)).floatValue();
		float rightMargin = ((Float)getAttribute(FigureAttributeConstant.RIGHT_MARGIN)).floatValue();
		float topMargin = ((Float)getAttribute(FigureAttributeConstant.TOP_MARGIN)).floatValue();
		float bottomMargin = ((Float)getAttribute(FigureAttributeConstant.BOTTOM_MARGIN)).floatValue();

		// inset the drawing box by 1 on every side so as not to overwrite
		// the border
		Rectangle drawingBox = new Rectangle(displayBox);
		drawingBox.grow(-1, -1);
		// adjust for margins
		drawingBox.x += leftMargin;
		drawingBox.width -= (leftMargin + rightMargin);
		drawingBox.y += topMargin;
		drawingBox.height -= topMargin + bottomMargin;

		return drawingBox;
	}

	/**
	 * Gets the displayDelegate attribute of the HTMLTextAreaFigure object
	 *
	 * @return   The displayDelegate value
	 */
	protected JLabel getDisplayDelegate() {
		if (fDisplayDelegate == null) {
			fDisplayDelegate = new JLabel();
			fDisplayDelegate.setBorder(null);
		}
		return fDisplayDelegate;
	}

	/**
	 * Creates the cached image, unless there is already one and it is
	 * compatible with new request, in which case we reuse it
	 *
	 * @param width   Description of the Parameter
	 * @param height  Description of the Parameter
	 */
	protected void createImage(int width, int height) {
		// if current image is compatible reuse it
		fImageHolder.lock();
		if (!fImageHolder.isAvailable() ||
				((BufferedImage)fImageHolder.getResource()).getWidth() != width ||
				((BufferedImage)fImageHolder.getResource()).getHeight() != height) {
			fImageHolder.setResource(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
		}
		// we don't unlock the image, it's the responsibility of
		// the caller to do so, this in oreder to ensure that calling createImage
		// will always return with a valid image
	}

	/**
	 * Builds the container for the drawing delegate
	 *
	 * @param drawingDelegate  The delegate
	 * @param displayBox       The bounding box
	 * @return                 The container
	 */
	protected JPanel getContainerPanel(Component drawingDelegate, Rectangle displayBox) {
		JPanel panel = new JPanel();
		return panel;
	}

	/**
	 * Returns a string that is valid HTML contents for a JLabel.<br>
	 * Valid HTML contents is text enclosed in <html> </html> tags.<br>
	 * We enclose the supplied text into a table with a single cell so that we
	 * can also set the external alignment and font attributes
	 *
	 * @param text        The text
	 * @param font        The font
	 * @param textColor   The text color HTML code
	 * @param backColor   The background's color HTML code
	 * @param displayBox  Description of the Parameter
	 * @return            The final HTML encoded text
	 */
	protected String getHTMLText(String text, Font font, String textColor,
			String backColor, Rectangle displayBox) {

		StringBuffer htmlText = new StringBuffer();
		// add an <HTML>
		htmlText.append("<html>");

		// add a table with width=100%, background color, and no borders with
		// a single cell
		htmlText.append(
				"<table border='0' width='" +
				displayBox.width +
				"' height='" + displayBox.height +
				"' cellpadding='0' cellspacing='0'" +
				"bgcolor='&FillColor;'>");
		htmlText.append("<tr><td width='100%'>");
		// set the font
		htmlText.append("<font face='&FontName;' color='&TextColor;' size='&FontSize;'>");
		// add alignment if required
		if (((Integer)getAttribute(FigureAttributeConstant.XALIGNMENT)).intValue() == SwingConstants.CENTER) {
			htmlText.append("<center>");
		}
		// add italic and bold attributes if required
		if (font.isItalic()) {
			htmlText.append("<i>");
		}
		if (font.isBold()) {
			htmlText.append("<b>");
		}

		// add the text itself
		htmlText.append(text);

		// close italic and bold attributes if required
		if (font.isBold()) {
			htmlText.append("</b>");
		}
		if (font.isItalic()) {
			htmlText.append("</i>");
		}
		// close alignment if required
		if (((Integer)getAttribute(FigureAttributeConstant.XALIGNMENT)).intValue() == SwingConstants.CENTER) {
			htmlText.append("</center>");
		}
		// close the font tag
		htmlText.append("</font>");
		// close the cell, row and table
		htmlText.append("</td></tr></table>");

		// close the html tag
		htmlText.append("</html>");

		return htmlText.toString();
	}

	/**
	 * Returns a new String with the entity keywords replaced by their
	 * current attribute value.<br>
	 * The text is scanned for entity keywords delimited by the START_ENTITY_CHAR
	 * and END_ENTITY_CHAR characters as in<br>
	 * <code>&gt;font face='&amp;FontName;' color='&amp;FillColor;'&lt;</code><br>
	 * A keyword is replaced if and only if an attribute with the given name is
	 * found, otherwise the text is left as is.
	 *
	 * @param template  The template text
	 * @return          The resulting string with its attributes replaced
	 */
	protected String substituteEntityKeywords(String template) {
		int endPos;
		StringBuffer finalText = new StringBuffer();

		int startPos = 0;
		int chunkEnd = startPos;
		try {
			while ((startPos = template.indexOf(START_ENTITY_CHAR, startPos)) != -1) {
				if (startPos != 0 && template.charAt(startPos - 1) == ESCAPE_CHAR) {
					// found an escaped parameter starter
					startPos++;
					continue;
				}

				// get the end of the parameter
				endPos = startPos + 1;
				while ((endPos = template.indexOf(END_ENTITY_CHAR, endPos)) != -1) {
					if (endPos == 0 || template.charAt(endPos - 1) != ESCAPE_CHAR) {
						// found a valid non escaped group stopper
						break;
					}
					// invalid entity, error? probably not, anyway we consider
					// this as not being an attribute replacement
					throw new InvalidAttributeMarker();
				}

				// OK, we now have an attribute
				String attrName = template.substring(startPos + 1, endPos);

				// replace it if present, otherwise leave as is
				String attrValue = getEntityHTMLRepresentation(attrName);
				if (attrValue != null) {
					finalText.append(template.substring(chunkEnd, startPos));
					// append the entity's value after performing
					// entity keyword substitution on its contents
					finalText.append(substituteEntityKeywords(attrValue));
					startPos = endPos + 1;
					chunkEnd = startPos;
				}
				else {
					startPos++;
				}
			}
		}
		catch (InvalidAttributeMarker ex) {
			// invalid marker, ignore
		}

		// append whatever is left
		finalText.append(template.substring(chunkEnd));

		// and return it
		return finalText.toString();
	}

	/**
	 * Returns a string representation of the attribute according to its type
	 *
	 * @param attrName  The name of the attribute
	 * @return          The attribute's HTML representation
	 */
	protected String getEntityHTMLRepresentation(String attrName) {
		// get the attribute's raw value
		Object attrValue = getIntrinsicContentProducer().getContent(this, attrName, null);

		// no such attribute?
		if (attrValue == null) {
			return null;
		}

		// found something
		// keep requesting value expansion until we get a String
		while (attrValue != null && !(attrValue instanceof String)) {
			// handle explicit ContentProducers
			if (attrValue instanceof ContentProducer) {
				attrValue = ((ContentProducer)attrValue).getContent(this, attrName, attrValue);
				continue;
			}

			// not a specific producer, try a default one
			ContentProducer defaultProducer = getContentProducer(attrValue.getClass());
			if (defaultProducer != null) {
				attrValue = defaultProducer.getContent(this, attrName, attrValue);
				continue;
			}

			// no specific producer,
			// all classes without an explicit default producer
			// get their value from their toString() method
			attrValue = attrValue.toString();
		}

		return (String)attrValue;
	}

	/**
	 * Gets the image.
	 *
	 * @return   The image value
	 */
	protected BufferedImage getImage() {
		if (fImageHolder.isAvailable()) {
			return (BufferedImage)fImageHolder.getResource();
		}
		return null;
	}

	/**
	 * Sets the image attribute of the HTMLTextAreaFigure object
	 *
	 * @param newImage  The new image value
	 */
	protected void setImage(BufferedImage newImage) {
		fImageHolder.setResource(newImage);
	}

	/**
	 * Factory method to create a popup menu which allows to set options
	 *
	 * @return   newly created popup menu
	 */
	protected JPopupMenu createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		addPopupMenuItems(popupMenu);
		popupMenu.setLightWeightPopupEnabled(true);
		return popupMenu;
	}

	/**
	 * Adds items to the popup menu
	 *
	 * @param popupMenu  The popup menu to add items to
	 */
	protected void addPopupMenuItems(JPopupMenu popupMenu) {
		ButtonGroup drawingPopupGroup;

		JRadioButtonMenuItem rbOption;

		drawingPopupGroup = new ButtonGroup();
		rbOption = new JRadioButtonMenuItem(
			new AbstractAction("Direct drawing") {
				public void actionPerformed(ActionEvent event) {
					setUseDirectDraw(true);
				}
			});

		drawingPopupGroup.add(rbOption);
		if (usesDirectDraw()) {
			drawingPopupGroup.setSelected(rbOption.getModel(), true);
		}
		popupMenu.add(rbOption);

		rbOption = new JRadioButtonMenuItem(
			new AbstractAction("Buffered drawing") {
				public void actionPerformed(ActionEvent event) {
					setUseDirectDraw(false);
				}
			});
		drawingPopupGroup.add(rbOption);
		if (usesBufferedDraw()) {
			drawingPopupGroup.setSelected(rbOption.getModel(), true);
		}
		popupMenu.add(rbOption);

		popupMenu.addSeparator();

		drawingPopupGroup = new ButtonGroup();
		rbOption = new JRadioButtonMenuItem(
			new AbstractAction("Normal HTML") {
				public void actionPerformed(ActionEvent event) {
					setRawHTML(false);
				}
			});

		drawingPopupGroup.add(rbOption);
		drawingPopupGroup.setSelected(rbOption.getModel(), true);
		popupMenu.add(rbOption);

		rbOption =
				new JRadioButtonMenuItem(
			new AbstractAction("Raw HTML") {
				public void actionPerformed(ActionEvent event) {
					setRawHTML(true);
				}
			});
		drawingPopupGroup.add(rbOption);
		popupMenu.add(rbOption);

	}

	/**
	 * Gets the usesDirectDraw status of the HTMLTextAreaFigure object
	 *
	 * @return   True if currently doing direct drawing, ie: not using a cached image
	 */
	public boolean usesDirectDraw() {
		return fUseDirectDraw;
	}

	/**
	 * Sets the useDirectDraw attribute of the HTMLTextAreaFigure object
	 *
	 * @param newUseDirectDraw  The new useDirectDraw value
	 */
	public void setUseDirectDraw(boolean newUseDirectDraw) {
		fUseDirectDraw = newUseDirectDraw;
		setAttribute(FigureAttributeConstant.POPUP_MENU, createPopupMenu());
		markSizeDirty();
	}

	/**
	 * Sets the useBufferedDraw attribute of the HTMLTextAreaFigure object
	 *
	 * @param newUseBufferedDraw  The new useBufferedDraw value
	 */
	public void setUseBufferedDraw(boolean newUseBufferedDraw) {
		setUseDirectDraw(!newUseBufferedDraw);
	}

	/**
	 * Gets the usesBufferedDraw attribute of the HTMLTextAreaFigure object
	 *
	 * @return   True if currently using buffered draw, ie: the cached image
	 */
	public boolean usesBufferedDraw() {
		return !usesDirectDraw();
	}

	/**
	 * Disposes of the image so it will be regenerated next time it is displayed
	 */
	protected void markImageDirty() {
		fImageHolder.dispose();
	}

	/**
	 * True if the image should be regenerated
	 *
	 * @return   The imageDirty value
	 */
	protected boolean isImageDirty() {
		return !fImageHolder.isAvailable();
	}

	/**
	 * Reads the figure from StorableInput
	 *
	 * @param dr            Description of the Parameter
	 * @throws IOException  the inout storable
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);

		setFrameFigure((Figure)dr.readStorable());
		setUseDirectDraw(dr.readBoolean());
		setRawHTML(dr.readBoolean());
//		setIntrinsicContentProducer((ContentProducer)dr.readStorable());
//		fContentProducers.read(dr);

//        // finally add the popup menu
//        setAttribute(Figure.POPUP_MENU, createPopupMenu());

	}

	/**
	 * Writes the figure to StorableOutput
	 *
	 * @param dw  the output storable
	 */
	public void write(StorableOutput dw) {
		super.write(dw);

		dw.writeStorable(getFrameFigure());
		dw.writeBoolean(usesDirectDraw());
		dw.writeBoolean(isRawHTML());
//		dw.writeStorable(getIntrinsicContentProducer());
//		fContentProducers.write(dw);
	}

	/**
	 * A text area figure uses the "LeftMargin", "RightMargin",
	 * "TopMargin", "BottomMargin", "TabSize", "FontSize", "FontStyle", and "FontName"
	 * attributes
	 *
	 * @param name   The new attribute name
	 * @param value  The new attribute value
	 */
	public void setAttribute(FigureAttributeConstant name, Object value) {
		super.setAttribute(name, value);
		markImageDirty();
	}

	/**
	 * Gets the rawHTML attribute of the HTMLTextAreaFigure object.<br>
	 * In RawHTML mode, the figure does not add any HTML formatting information so it's
	 * the user's responsibility to handle the whole displayed contents
	 *
	 * @return   The rawHTML value
	 */
	public boolean isRawHTML() {
		return fRawHTML;
	}

	/**
	 * Sets the rawHTML attribute of the HTMLTextAreaFigure object
	 *
	 * @param newRawHTML  The new rawHTML value
	 */
	public void setRawHTML(boolean newRawHTML) {
		fRawHTML = newRawHTML;
		setAttribute(FigureAttributeConstant.POPUP_MENU, createPopupMenu());
	}

	/**
	 * Gets the IntrinsicContentProducer attribute of the HTMLTextAreaFigure object.<br>
	 * The intrinsic producer produces contents for the basic figure's attributes
	 *
	 * @return   The IntrinsicContentProducer value
	 */
	protected ContentProducer getIntrinsicContentProducer() {
		return fIntrinsicContentProducer;
	}

	/**
	 * Sets the IntrinsicContentProducer attribute of the HTMLTextAreaFigure object
	 *
	 * @param newIntrinsicContentProducer  The new IntrinsicContentProducer value
	 */
	public void setIntrinsicContentProducer(ContentProducer newIntrinsicContentProducer) {
		fIntrinsicContentProducer = newIntrinsicContentProducer;
	}

	/**
	 * Registers a specific content producer for the target class
	 *
	 * @param targetClass  the target class
	 * @param producer     the producer
	 * @return             the previously registered producer. May be null
	 */
	public ContentProducer registerContentProducer(Class targetClass, ContentProducer producer) {
		return fContentProducers.registerContentProducer(targetClass, producer);
	}

	/**
	 * Unregisters a registered content producer.
	 *
	 * @param producer     Description of the Parameter
	 * @param targetClass  Description of the Parameter
	 */
	public void unregisterContentProducer(Class targetClass, ContentProducer producer) {
		fContentProducers.unregisterContentProducer(targetClass, producer);
	}

	/**
	 * Retrieves a suitable content producer for the target class
	 *
	 * @param targetClass  the target class
	 * @return             The ContentProducer
	 */
	protected ContentProducer getContentProducer(Class targetClass) {
		return fContentProducers.getContentProducer(targetClass);
	}

	/**
	 * Makes a polygon with the same shape and dimensions as the current figure
	 *
	 * @return   Description of the Return Value
	 */
	public Polygon getPolygon() {
		Polygon polygon = new Polygon();

		// make an AffineTransofmr that does nothing
		AffineTransform at = AffineTransform.getScaleInstance(1, 1);
		// and get an iterator on the segments
		FlatteningPathIterator pIter = new FlatteningPathIterator(
				getClippingShape().getPathIterator(at),
				1);

		double[] coords = new double[6];
		//int pointType;
		// iterate on the segments adding the points to the polygon
		while (!pIter.isDone()) {
			//pointType = pIter.currentSegment(coords);
			pIter.currentSegment(coords);
			polygon.addPoint((int)coords[0], (int)coords[1]);
			pIter.next();
		}

		return polygon;
	}

	/**
	 * Gets the frameFigure attribute of the HTMLTextAreaFigure object
	 *
	 * @return   The frameFigure value
	 */
	protected Figure getFrameFigure() {
		return fFrameFigure;
	}

	/**
	 * Sets the frameFigure attribute of the HTMLTextAreaFigure object
	 *
	 * @param newFrameFigure  The new frameFigure value
	 */
	public void setFrameFigure(Figure newFrameFigure) {
		if (fFrameFigure != null) {
			fFrameFigure.removeFigureChangeListener(this);
		}
		fFrameFigure = newFrameFigure;
		fFrameFigure.addFigureChangeListener(this);
	}

	/**
	 * Gets the clippingShape attribute of the HTMLTextAreaFigure object
	 *
	 * @return   The clippingShape value
	 */
	protected Shape getClippingShape() {
		Figure frame = getFrameFigure();
		if (frame instanceof GeometricFigure) {
			return ((GeometricFigure)frame).getShape();
		}
		return frame.displayBox();
	}

	/**
	 * handles frame figure's invalidated events
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureInvalidated(FigureChangeEvent e) { }

	/**
	 * handles frame figure's changed events.<br>
	 * It updates the displayBox to match
	 * the frame figure's
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureChanged(FigureChangeEvent e) {
		willChange();
		super.basicDisplayBox(e.getFigure().displayBox().getLocation(), Geom.corner(e.getFigure().displayBox()));
		changed();
	}

	/**
	 * handles frame figure's invalidatedremoved events.<br>
	 * Never happens because the frame figure is not part of the drawing
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureRemoved(FigureChangeEvent e) { }

	/**
	 * handles frame figure's remove requests events.<br>
	 * Never happens because the frame figure is not part of the drawing
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureRequestRemove(FigureChangeEvent e) { }

	/**
	 * handles frame figure's update requests events.<br>
	 * Never happens because the frame figure is not part of the drawing
	 *
	 * @param e  Description of the Parameter
	 */
	public void figureRequestUpdate(FigureChangeEvent e) { }

	/**
	 * Thrown when an entity reference is not correctly encoded
	 *
	 * @author    gualo
	 * @created   1 mai 2002
	 */
	private class InvalidAttributeMarker extends Exception {
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
