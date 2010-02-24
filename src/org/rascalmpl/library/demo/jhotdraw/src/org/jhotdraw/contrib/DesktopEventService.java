/*
 * @(#)DesktopEventService.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.standard.NullDrawingView;
import org.jhotdraw.util.CollectionsFactory;

import java.util.List;
import java.util.ListIterator;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;
import java.awt.*;

/**
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class DesktopEventService {

	/**
	 * Current usage of this List is not thread safe, nor should it need to be.
	 * If it ever does we can synchronize on the List itself to provide safety.
	 */
	private java.util.List listeners;
	private DrawingView mySelectedView;
	private Container myContainer;
	private Desktop myDesktop;

	public DesktopEventService(Desktop newDesktop, Container newContainer) {
		listeners = CollectionsFactory.current().createList();
		setDesktop(newDesktop);
		setContainer(newContainer);
		getContainer().addContainerListener(createComponentListener());
	}

	private void setDesktop(Desktop newDesktop) {
		myDesktop = newDesktop;
	}

	protected Desktop getDesktop() {
		return myDesktop;
	}

	private void setContainer(Container newContainer) {
		myContainer = newContainer;
	}

	protected Container getContainer() {
		return myContainer;
	}

	public void addComponent(Component newComponent) {
		getContainer().add(newComponent);
	}

	public void removeComponent(DrawingView dv) {
		Component[] comps = getContainer().getComponents();
		for (int x = 0; x < comps.length; x++) {
			if (dv == Helper.getDrawingView(comps[x])) {
				getContainer().remove(comps[x]);
			    break;
			}
		}
	}

	public void removeAllComponents() {
		getContainer().removeAll();
	}

	public void addDesktopListener(DesktopListener dpl) {
		listeners.add(dpl);
	}

	public void removeDesktopListener(DesktopListener dpl) {
		listeners.remove(dpl);
	}

	protected void fireDrawingViewAddedEvent(final DrawingView dv) {
		ListIterator li= listeners.listIterator(listeners.size());
		DesktopEvent dpe = createDesktopEvent(getActiveDrawingView(), dv);
		while (li.hasPrevious()) {
			DesktopListener dpl = (DesktopListener)li.previous();
			dpl.drawingViewAdded(dpe);
		}
	}

	protected void fireDrawingViewRemovedEvent(final DrawingView dv) {
		ListIterator li= listeners.listIterator(listeners.size());
		DesktopEvent dpe = createDesktopEvent(getActiveDrawingView(), dv);
		while (li.hasPrevious()) {
			DesktopListener dpl = (DesktopListener)li.previous();
			dpl.drawingViewRemoved(dpe);
		}
	}

	/**
	 * This method is only called if the selected drawingView has actually changed
	 */
	protected void fireDrawingViewSelectedEvent(final DrawingView oldView, final DrawingView newView) {
		ListIterator li= listeners.listIterator(listeners.size());
		DesktopEvent dpe = createDesktopEvent(oldView, newView);
		while (li.hasPrevious()) {
			DesktopListener dpl = (DesktopListener)li.previous();
			dpl.drawingViewSelected(dpe);
		}
	}

	/**
	 * @param oldView previous active drawing view (may be null because not all events require this information)
	 */
	protected DesktopEvent createDesktopEvent(DrawingView oldView, DrawingView newView) {
		return new DesktopEvent(getDesktop(), newView, oldView);
	}

	public DrawingView[] getDrawingViews(Component[] comps) {
		List al = CollectionsFactory.current().createList();
		for (int x = 0; x < comps.length; x++) {
			DrawingView dv = Helper.getDrawingView(comps[x]);
			if (dv != null) {
				al.add(dv);
			}
		}
		DrawingView[] dvs = new DrawingView[al.size()];
		al.toArray(dvs);
		return dvs;
	}

	public DrawingView getActiveDrawingView() {
		return mySelectedView;
	}

	protected void setActiveDrawingView(DrawingView newActiveDrawingView) {
		mySelectedView = newActiveDrawingView;
	}
	
	protected ContainerListener createComponentListener() {
		return new ContainerAdapter() {
			/**
			 * If the dv is null assert
			 * @todo does adding a component always make it the selected view?
			 *  Yes so far because this is only being used on single view Desktops.
			 *  If it is to work on multipleView desktops, the we need to think further.
			 */
            public void componentAdded(ContainerEvent e) {
				DrawingView dv = Helper.getDrawingView((java.awt.Container)e.getChild());
				DrawingView oldView = getActiveDrawingView();
				if (dv != null) {
					fireDrawingViewAddedEvent(dv);
					setActiveDrawingView(dv);
					fireDrawingViewSelectedEvent(oldView, getActiveDrawingView());
				}
            }

		    /**
			 * If dv is null assert
             * dv will only be null if something thats not a drawingView was
			 * added to the desktop.  it would be simpler if we forbade that.
			 */
            public void componentRemoved(ContainerEvent e) {
				DrawingView dv = Helper.getDrawingView((java.awt.Container)e.getChild());
				if (dv != null) {
					DrawingView oldView = getActiveDrawingView();
					setActiveDrawingView(NullDrawingView.getManagedDrawingView(oldView.editor()));
					fireDrawingViewSelectedEvent(oldView, getActiveDrawingView());
					fireDrawingViewRemovedEvent(dv);
				}
            }
        };
	}
}
