/*
 * @(#)FigureChangeEventMulticaster.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.*;
import java.awt.*;
import java.util.*;

/**
 * Manages a list of FigureChangeListeners to be notified of
 * specific FigureChangeEvents.
 *
 * @version <$CURRENT_VERSION$>
 */
public class FigureChangeEventMulticaster extends
	AWTEventMulticaster implements FigureChangeListener {

	public FigureChangeEventMulticaster(EventListener newListenerA, EventListener newListenerB) {
		super(newListenerA, newListenerB);
	}

	public void figureInvalidated(FigureChangeEvent e) {
		((FigureChangeListener)a).figureInvalidated(e);
		((FigureChangeListener)b).figureInvalidated(e);
	}

	public void figureRequestRemove(FigureChangeEvent e) {
		((FigureChangeListener)a).figureRequestRemove(e);
		((FigureChangeListener)b).figureRequestRemove(e);
	}

	public void figureRequestUpdate(FigureChangeEvent e) {
		((FigureChangeListener)a).figureRequestUpdate(e);
		((FigureChangeListener)b).figureRequestUpdate(e);
	}

	public void figureChanged(FigureChangeEvent e) {
		((FigureChangeListener)a).figureChanged(e);
		((FigureChangeListener)b).figureChanged(e);
	}

	public void figureRemoved(FigureChangeEvent e) {
		((FigureChangeListener)a).figureRemoved(e);
		((FigureChangeListener)b).figureRemoved(e);
	}

	public static FigureChangeListener add(FigureChangeListener a, FigureChangeListener b) {
		return (FigureChangeListener)addInternal(a, b);
	}


	public static FigureChangeListener remove(FigureChangeListener l, FigureChangeListener oldl) {
		return (FigureChangeListener) removeInternal(l, oldl);
	}

	protected EventListener remove(EventListener oldl)
	{
		if (oldl == a) {
			return b;
		}
		if (oldl == b) {
			return a;
		}
		EventListener a2 = removeInternal(a, oldl);
		EventListener b2 = removeInternal(b, oldl);
		if (a2 == a && b2 == b) {
			return this;
		}
		else {
			return addInternal((FigureChangeListener)a2, (FigureChangeListener)b2);
		}
	}

	protected static EventListener addInternal(FigureChangeListener a, FigureChangeListener b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		return new FigureChangeEventMulticaster(a, b);
	}

	protected static EventListener removeInternal(EventListener l, EventListener oldl) {
		if (l == oldl || l == null) {
			return null;
		}
		else if (l instanceof FigureChangeEventMulticaster) {
			return ((FigureChangeEventMulticaster)l).remove(oldl);
		}
		else {
			return l;		// it's not here
		}
	}

}
