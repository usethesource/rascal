/*
 * @(#)GraphicalCompositeFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.*;
import org.jhotdraw.figures.*;
import java.awt.*;
import java.io.*;
import java.util.List;

/**
 * The GraphicalCompositeFigure fills in the gap between a CompositeFigure
 * and other figures which mainly have a presentation purpose. The
 * GraphicalCompositeFigure can be configured with any Figure which
 * takes over the task for rendering the graphical presentation for
 * a CompositeFigure. Therefore, the GraphicalCompositeFigure manages
 * contained figures like the CompositeFigure does, but delegates
 * its graphical presentation to another (graphical) figure which
 * purpose it is to draw the container for all contained figures.
 *
 * The GraphicalCompositeFigure adds to the {@link CompositeFigure CompositeFigure}
 * by containing a presentation figure by default which can not be removed.  Normally,
 * the {@link CompositeFigure CompositeFigure} can not be seen without containing a figure
 * because it has no mechanism to draw itself.  It instead relies on its contained
 * figures to draw themselves thereby giving the {@link CompositeFigure CompositeFigure} its
 * appearance.  However, the <b>GraphicalCompositeFigure</b>'s presentation figure
 * can draw itself even when the <b>GraphicalCompositeFigure</b> contains no other figures.
 * The <b>GraphicalCompositeFigure</b> also uses a {@link Layouter Layouter} or layout
 * its contained figures.
 *
 * @author	Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public class GraphicalCompositeFigure extends CompositeFigure implements Layoutable  {

	/**
	 * Figure which performs all presentation tasks for this
	 * CompositeFigure as CompositeFigures usually don't have
	 * an own presentation but present only the sum of all its
	 * children.
	 */
	private Figure	myPresentationFigure;

	/**
	 * A Layouter determines how the CompositeFigure should
	 * be laid out graphically.
	 */
	private Layouter myLayouter;

	private static final long serialVersionUID = 1265742491024232713L;

	/**
	 * Default constructor which uses a RectangleFigure as presentation
	 * figure. This constructor is needed by the Storable mechanism.
	 */
	public GraphicalCompositeFigure() {
		this(new RectangleFigure());
	}

	/**
	 * Constructor which creates a GraphicalCompositeFigure with
	 * a given graphical figure for presenting it.
	 *
	 * @param	newPresentationFigure	figure which renders the container
	 */
	public GraphicalCompositeFigure(Figure newPresentationFigure) {
		super();
		setPresentationFigure(newPresentationFigure);
		initialize();
	}

	/**
	 * This method performs additional initialization operations,
	 * in this case setting the Layouter.
	 * It is called from the constructors and the clone() method.
	 * A StandardLayouter is set.
	 */
	protected void initialize() {
		if (getLayouter() != null) {
			// use prototype to create new instance
			setLayouter(getLayouter().create(this));
		}
		else {
			setLayouter(new StandardLayouter(this));
		}
	}

	/**
	 * Clones a figure and initializes it
	 *
	 * @see Figure#clone
	 */
	public Object clone() {
		Object cloneObject = super.clone();
		((GraphicalCompositeFigure)cloneObject).initialize();
		return cloneObject;
	}

	/**
	 * Return the display area. This method is delegated to the encapsulated presentation figure.
	 */
	public Rectangle displayBox() {
		return getPresentationFigure().displayBox();
	}

	/**
	 * Standard presentation method which is delegated to the encapsulated presentation figure.
	 */
	public void basicDisplayBox(Point origin, Point corner) {
		Rectangle r = getLayouter().layout(origin, corner);
		 // Fix for bug request IDs 548000 and 548032
		 // Previously was
		 //     getPresentationFigure().basicDisplayBox(r.getLocation(), new Point(r.width, r.height));
		 // The corner transferred to the presentation figure is wrong as it transfers
		 // the dimension of the resulting rectangle from the layouter instead of the
		 // lower right corner
		getPresentationFigure().basicDisplayBox(r.getLocation(),
			new Point((int)r.getMaxX(), (int)r.getMaxY()));
	}

	/**
	 * Standard presentation method which is delegated to the encapsulated presentation figure.
	 * The presentation figure is moved as well as all contained figures.
	 */
	protected void basicMoveBy(int dx, int dy) {
		super.basicMoveBy(dx, dy);
		getPresentationFigure().moveBy(dx, dy);
	}

	/**
	 * Explicit update: an updated involves a layout for all contained figures.
	 */
	public void update() {
		willChange();
		layout();
		change();
		changed();
	}

	/**
	 * Draw the figure. This method is delegated to the encapsulated presentation figure.
	 */
	public void draw(Graphics g) {
		getPresentationFigure().draw(g);
		super.draw(g);
	}

	/**
	 * Return default handles from the presentation figure.
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		BoxHandleKit.addHandles(this, handles);
		return new HandleEnumerator(handles);
		//return getPresentationFigure().handles();
	}

	/**
	 * Delegate capabilities for storing and retrieving attributes to a
	 * CompositeFigure if the encapsulated presentation figure. If no
	 * presentation figure is found then the superclass' getAttribute()
	 * will be invoked (which currently returns always "null").
	 *
	 * @param	name	name of the attribute whose value should be returned
	 * @return	value of the attribute with the given name
	 *
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		if (getPresentationFigure() != null) {
			return getPresentationFigure().getAttribute(name);
		}
		else {
			return super.getAttribute(name);
		}
	}

	/**
	 * Delegate capabilities for storing and retrieving attributes to a
	 * CompositeFigure if the encapsulated presentation figure. If no
	 * presentation figure is found then the superclass' getAttribute()
	 * will be invoked (which currently returns always "null").
	 *
	 * @param	attributeConstant	attribute constant whose value should be returned
	 * @return	value of the attribute with the given name
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		if (getPresentationFigure() != null) {
			return getPresentationFigure().getAttribute(attributeConstant);
		}
		else {
			return super.getAttribute(attributeConstant);
		}
	}

	/**
	 * Delegate capabilities for storing and retrieving attributes to a
	 * CompositeFigure if the encapsulated presentation figure. If no
	 * presentation figure is found then the superclass' setAttribute()
	 * will be invoked (which currently does not set an attribute).
	 *
	 * @param	name	name of the attribute
	 * @param	value	value associated with this attribute
	 *
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
		if (getPresentationFigure() != null) {
			getPresentationFigure().setAttribute(name, value);
		}
		else {
			super.setAttribute(name, value);
		}
	}

	/**
	 * Delegate capabilities for storing and retrieving attributes to a
	 * CompositeFigure if the encapsulated presentation figure. If no
	 * presentation figure is found then the superclass' setAttribute()
	 * will be invoked (which currently does not set an attribute).
	 *
	 * @param	attributeConstant	attribute constant
	 * @param	value	value associated with this attribute
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
		if (getPresentationFigure() != null) {
			getPresentationFigure().setAttribute(attributeConstant, value);
		}
		else {
			super.setAttribute(attributeConstant, value);
		}
	}

	/**
	 * Set a figure which renders this CompositeFigure. The presentation
	 * tasks for the CompositeFigure are delegated to this presentation
	 * figure.
	 *
	 * @param	newPresentationFigure	figure takes over the presentation tasks
	 */
	public void setPresentationFigure(Figure newPresentationFigure) {
		myPresentationFigure = newPresentationFigure;
	}

	/**
	 * Get a figure which renders this CompositeFigure. The presentation
	 * tasks for the CompositeFigure are delegated to this presentation
	 * figure.
	 *
	 * @return	figure takes over the presentation tasks
	 */
	public Figure getPresentationFigure() {
		return myPresentationFigure;
	}

	/**
	 * A layout algorithm is used to define how the child components
	 * should be laid out in relation to each other. The task for
	 * layouting the child components for presentation is delegated
	 * to a Layouter which can be plugged in at runtime.
	 */
	public void layout() {
		if (getLayouter() != null) {
			Rectangle r = getLayouter().calculateLayout(displayBox().getLocation(), displayBox().getLocation());
			displayBox(r.getLocation(), new Point(r.x + r.width, r.y + r.height));
		}
	}

	/**
	 * Set a Layouter object which encapsulated a layout
	 * algorithm for this figure. Typically, a Layouter
	 * accesses the child components of this figure and arranges
	 * their graphical presentation. It is a good idea to set
	 * the Layouter in the protected initialize() method
	 * so it can be recreated if a GraphicalCompositeFigure is
	 * read and restored from a StorableInput stream.
	 *
	 * @param	newLayouter	encapsulation of a layout algorithm.
	 */
	public void setLayouter(Layouter newLayouter) {
		myLayouter = newLayouter;
	}

	/**
	 * Get a Layouter object which encapsulated a layout
	 * algorithm for this figure. Typically, a Layouter
	 * accesses the child components of this figure and arranges
	 * their graphical presentation.
	 *
	 * @return	layout strategy used by this figure
	 */
	public Layouter getLayouter() {
		return myLayouter;
	}

	/**
	 * Notify the registered change listener if an exlicit change
	 * to the component (or one of its child components has occurred).
	 */
	protected void change() {
		if (listener() != null) {
			listener().figureRequestUpdate(new FigureChangeEvent(this));
		}
	}

	/**
	 * Propagates the removeFromDrawing request up to the container.
	 */
	public void figureRequestRemove(FigureChangeEvent e) {
		if (listener() != null) {
			if (includes(e.getFigure())) {
				Rectangle r = invalidateRectangle(displayBox());
				listener().figureRequestRemove(new FigureChangeEvent(this, r, e));
			}
			else {
				super.figureRequestRemove(e);
			}
		}
	}

	/**
	 * Reads the contained figures from StorableInput. The
	 * figure responsible for graphical presentation is read
	 * together with all child components. The Layouter
	 * is not stored and therefore not read.
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		setPresentationFigure((Figure)dr.readStorable());
		setLayouter((Layouter)dr.readStorable());
	}

	/**
	 * Writes the contained figures to the StorableOutput. The
	 * figure responsible for graphical presentation is written
	 * together with all child components. The Layouter
	 * is not written.
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeStorable(getPresentationFigure());
		dw.writeStorable(getLayouter());
	}
}