/*
 * @(#)StandardDisposableResourceHolder.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.io.Serializable;

/**
 * StandardDisposableResourceHolder is a standard implementation of the
 * DisposableResourceHolder interface
 *
 * @author  Eduardo Francos - InContext
 * @created 2 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class StandardDisposableResourceHolder implements DisposableResourceHolder, Serializable {

	/** The holded resource object */
	private Object resource = null;

	/** The dispose delay, default to 60 seconds */
	private long disposeDelay = 60000;

	/**
	 * The last time the resource was accessed as returned by
	 * <code>System.currentTimeMillis()</code>
	 */
	private long lastTimeAccessed = 0;

	/** True if the resource is locked */
	private boolean isLocked = false;

	/**
	 * Constructor for the StandardDisposableResourceHolder object
	 */
	public StandardDisposableResourceHolder() { }

	/**
	 * Constructor for the StandardDisposableResourceHolder object
	 *
	 * @param resource  Description of the Parameter
	 */
	public StandardDisposableResourceHolder(Object newResource) {
		resource = newResource;
		resetDelay();
	}

	/**
	 * Makes a clone of this
	 *
	 * @return   the clone
	 */
	public Object clone() {
		StandardDisposableResourceHolder clone = new StandardDisposableResourceHolder();
		clone.setDisposableDelay(this.getDisposableDelay());
		return clone;
	}

	/**
	 * Gets the resource attribute of the StandardDisposableResourceHolder object
	 *
	 * @return                          The resource value
	 * @exception NullPointerException  Description of the Exception
	 */
	public Object getResource() throws NullPointerException {
		if (resource != null) {
			resetDelay();
			return resource;
		}
		throw new NullPointerException();
	}

	/**
	 * Sets the resource attribute of the StandardDisposableResourceHolder object
	 *
	 * @param resource  The new resource value
	 */
	public void setResource(Object newResource) {
		resource = newResource;
		resetDelay();
	}

	/**
	 * Sets the disposableDelay attribute of the StandardDisposableResourceHolder object
	 *
	 * @param millis  The new disposableDelay value
	 */
	public void setDisposableDelay(long millis) {
		disposeDelay = millis;
	}

	/**
	 * Gets the disposableDelay attribute of the StandardDisposableResourceHolder object
	 *
	 * @return   The disposableDelay value
	 */
	public long getDisposableDelay() {
		return disposeDelay;
	}

	/** Disposes of the resource */
	public void dispose() {
		resource = null;
	}

	/**
	 * Gets the available attribute of the StandardDisposableResourceHolder object
	 *
	 * @return   The available value
	 */
	public boolean isAvailable() {
		return (resource != null);
	}

	/**
	 * Locks the resource so it cannot be automatically disposed of until unlock
	 * is called.<br>
	 * Explicit disposing by calling dispose() is still possible though.
	 */
	public void lock() {
		isLocked = true;
	}

	/**
	 * Unlocks the resource so it can be automatically disposed of again.<br>
	 * Explicit disposing by calling dispose() is still possible though.
	 */
	public void unlock() {
		resetDelay();
		isLocked = false;
	}

	/**
	 * True if the resource is locked
	 *
	 * @return   The locked status
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Gets the lastTimeAccessed attribute of the DisposableResourceHolder object
	 *
	 * @return   The lastTimeAccessed value
	 */
	public long getLastTimeAccessed() {
		return lastTimeAccessed;
	}

	/** Resets the disposing delay so as to restart the time counter */
	public void resetDelay() {
		lastTimeAccessed = System.currentTimeMillis();
	}
}
