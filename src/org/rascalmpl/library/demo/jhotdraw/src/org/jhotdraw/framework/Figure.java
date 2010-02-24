/*
 * @(#)Figure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import org.jhotdraw.util.*;
import org.jhotdraw.standard.TextHolder;

import java.awt.*;
import java.io.Serializable;

/**
 * The interface of a graphical figure. A figure knows
 * its display box and can draw itself. A figure can be
 * composed of several figures. To interact and manipulate
 * with a figure it can provide Handles and Connectors.<p>
 * A figure has a set of handles to manipulate its shape or attributes.
 * A figure has one or more connectors that define how
 * to locate a connection point.<p>
 * Figures can have an open ended set of attributes.
 * An attribute is identified by a string.<p>
 * Default implementations for the Figure interface are provided
 * by AbstractFigure.<p>
 *
 * Figures can have <a name="dependent_figure">dependent figure</a>s. The existence od dependent
 * figures depend on another figure. This is the case for figures
 * such as ConnectedTextFigures and LineDecoration. Thus, they are
 * "externally" dependent on a figure in contrast to (internally)
 * contained figures. This means, "normal" figures (figures that
 * are not containers) can still have dependent figures. Dependent
 * figures are especially important if the figure which the depend
 * on is deleted because they should be removed as well (cascading delete).
 *
 * @see Handle
 * @see Connector
 * @see org.jhotdraw.standard.AbstractFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public interface Figure
				extends Storable, Cloneable, Serializable {

	/**
	 * Constant that allows to identify a popup menu assigned
	 * as an attribute.
	 */
	public static String POPUP_MENU = "POPUP_MENU";

	/**
	 * Moves the Figure to a new location.
	 * @param dx the x delta
	 * @param dy the y delta
	 */
	public void moveBy(int dx, int dy);

	/**
	 * Changes the display box of a figure. This method is
	 * always implemented in figure subclasses. It only changes
	 * the displaybox and does not announce any changes. It
	 * is usually not called by the client. Clients typically call
	 * displayBox to change the display box.
	 * @param origin the new origin
	 * @param corner the new corner
	 * @see #displayBox
	 */
	public void basicDisplayBox(Point origin, Point corner);

	/**
	 * Changes the display box of a figure. Clients usually
	 * invoke this method. It changes the display box
	 * and announces the corresponding changes.
	 * @param origin the new origin
	 * @param corner the new corner
	 * @see #displayBox
	 */
	public void displayBox(Point origin, Point corner);

	/**
	 * Gets the display box of a figure
	 * @see #basicDisplayBox
	 */
	public Rectangle displayBox();

	/**
	 * Draws the figure.
	 * @param g the Graphics to draw into
	 */
	public void draw(Graphics g);

	/**
	 * Returns the handles used to manipulate
	 * the figure. Handles is a Factory Method for
	 * creating handle objects.
	 *
	 * @return an type-safe iterator of handles
	 * @see Handle
	 */
	public HandleEnumeration handles();

	/**
	 * Gets the size of the figure
	 */
	public Dimension size();

	/**
	 * Gets the figure's center
	 */
	public Point center();

	/**
	 * Checks if the Figure should be considered as empty.
	 */
	public boolean isEmpty();

	/**
	 * Returns an Enumeration of the figures contained in this figure
	 */
	public FigureEnumeration figures();

	/**
	 * Returns the figure that contains the given point.
	 */
	public Figure findFigureInside(int x, int y);

	/**
	 * Checks if a point is inside the figure.
	 */
	public boolean containsPoint(int x, int y);

	/**
	 * Returns a Clone of this figure
	 */
	public Object clone();

	/**
	 * Changes the display box of a figure. This is a
	 * convenience method. Implementors should only
	 * have to override basicDisplayBox
	 * @see #displayBox
	 */
	public void displayBox(Rectangle r);

	/**
	 * Checks whether the given figure is contained in this figure.
	 */
	public boolean includes(Figure figure);

	/**
	 * Decomposes a figure into its parts. A figure is considered
	 * as a part of itself.
	 */
	public FigureEnumeration decompose();

	/**
	 * Sets the Figure's container and registers the container
	 * as a figure change listener. A figure's container can be
	 * any kind of FigureChangeListener. A figure is not restricted
	 * to have a single container.
	 */
	public void addToContainer(FigureChangeListener c);

	/**
	 * Removes a figure from the given container and unregisters
	 * it as a change listener.
	 */
	public void removeFromContainer(FigureChangeListener c);

	/**
	 * Add a <a href="#dependent_figure">dependent figure</a>.
	 */
	public void addDependendFigure(Figure newDependendFigure);

	/**
	 * Remove a <a href="#dependent_figure">dependent figure</a>.
	 */
	public void removeDependendFigure(Figure oldDependendFigure);

	/**
	 * Get an enumeration of all <a href="#dependent_figure">dependent figures</a>.
	 */
	public FigureEnumeration getDependendFigures();

	/**
	 * Gets the Figure's listeners.
	 */
	public FigureChangeListener listener();

	/**
	 * Adds a listener for this figure.
	 */
	public void addFigureChangeListener(FigureChangeListener l);

	/**
	 * Removes a listener for this figure.
	 */
	public void removeFigureChangeListener(FigureChangeListener l);

	/**
	 * Releases a figure's resources. Release is called when
	 * a figure is removed from a drawing. Informs the listeners that
	 * the figure is removed by calling figureRemoved.
	 */
	public void release();

	/**
	 * Invalidates the figure. This method informs its listeners
	 * that its current display box is invalid and should be
	 * refreshed.
	 */
	public void invalidate();

	/**
	 * Informes that a figure is about to change such that its
	 * display box is affected.
	 * Here is an example of how it is used together with changed()
	 * <pre>
	 * public void move(int x, int y) {
	 *      willChange();
	 *      // change the figure's location
	 *      changed();
	 *  }
	 * </pre>
	 * @see #invalidate
	 * @see #changed
	 */
	public void willChange();

	/**
	 * Informes that a figure has changed its display box.
	 * This method also triggers an update call for its
	 * registered observers.
	 * @see #invalidate
	 * @see #willChange
	 *
	 */
	public void changed();

	/**
	 * Checks if this figure can be connected
	 */
	public boolean canConnect();

	/**
	 * Gets a connector for this figure at the given location.
	 * A figure can have different connectors at different locations.
	 */
	public Connector connectorAt(int x, int y);

	/**
	 * Sets whether the connectors should be visible.
	 * Connectors can be optionally visible. Implement
	 * this method and react on isVisible to turn the
	 * connectors on or off.
	 */
	public void connectorVisibility(boolean isVisible, ConnectionFigure connection);

	/**
	 * Returns the connection inset. This is only a hint that
	 * connectors can use to determine the connection location.
	 * The inset defines the area where the display box of a
	 * figure should not be connected.
	 *
	 */
	public Insets connectionInsets();

	/**
	 * Returns the locator used to located connected text.
	 */
	public Locator connectedTextLocator(Figure text);

	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * All figures support the attribute names
	 * FillColor and FrameColor
	 *
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name);

	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * All figures support the attribute names
	 * FillColor and FrameColor
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant);

	/**
	 * Sets the named attribute to the new value
	 *
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value);

	/**
	 * Sets the named attribute to the new value
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value);

	/**
	 * Gets the z value (back-to-front ordering) of this figure.
	 * Z values are not guaranteed to not skip numbers.
	 */
	public int getZValue();

	/**
	 * Sets the z value (back-to-front ordering) of this figure.
	 * Z values are not guaranteed to not skip numbers.
	 */
	public void setZValue(int z);

	public void visit(FigureVisitor visitor);

	/**
	 * Some figures have the ability to hold text. This method returns
	 * the adjunctant TextHolder.
	 * @return
	 */
	public TextHolder getTextHolder();

	/**
	 * Get the underlying figure in case the figure has been decorated.
	 * If the figure has not been decorated the figure itself is returned.
	 * The DecoratorFigure does not release the the decorated figure but
	 * just returns it (in contrast to {@link org.jhotdraw.standard.DecoratorFigure.peelDecoration}).
	 *
	 * @return underlying, "real" without DecoratorFigure
	 * @see org.jhotdraw.standard.DecoratorFigure
	 */
	public Figure getDecoratedFigure();
}
