/*
 * @(#)DisposableResourceHolder.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

/**
 * DisposableResourceHolder defines the interface for objects holding
 * disposable resources.<br>
 * A disposable resource is any kind of object that can be disposed of
 * according to a given disposal strategy.<BR>
 * DisposableResourceHolder keeps track of the last time the resource is
 * accessed to support ellapsed time ResourceDisposabilityStrategies.<br>
 * For example, if you are handling large images that you can regenerate
 * at will, the DisposableResourceHolder will hold the image, and if not
 * accessed for let's say 60 secs then it discards the image
 * (sets its reference to null) so that it can be GCed.<br>
 * Within the context of JHotDraw, HTMLTextAreaFigure uses
 * DisposableResourceHolders to handle the HTML generated image, but if the
 * figure is not displayed (out of the display area or in a background view) then
 * the image is disposed of after a while and the HTMLTextAreaFigure will regenerate
 * it the next time it is displayed.<br>
 * A resource may be locked so that its user can be sure it will not be
 * disposed of while in use. After finished using it it should be unlocked so as
 * to reintegrate it to the disposal strategy.
 *
 * @author    Eduardo Francos - InContext
 * @created   2 mai 2002
 * @version   <$CURRENT_VERSION$>
 * @see       DisposableResourceManager
 * @see       StandardDisposableResourceManager
 * @see       ResourceDisposabilityStrategy
 * @see       DisposableResourceManagerFactory
 * @see       StandardDisposableResourceHolder
 */
public interface DisposableResourceHolder {

	/**
	 * Gets the resource
	 *
	 * @return                          The resource value
	 * @exception NullPointerException  If the resource was disposed of
	 */
	public Object getResource() throws NullPointerException;

	/**
	 * Makes a clone the this
	 *
	 * @return   the clone
	 */
	public Object clone();

	/**
	 * Sets the resource
	 *
	 * @param resource  The new resource value
	 */
	public void setResource(Object resource);

	/**
	 * Sets the disposableDelay
	 *
	 * @param millis  The new disposableDelay value
	 */
	public void setDisposableDelay(long millis);

	/**
	 * Gets the disposableDelay
	 *
	 * @return   The disposableDelay value
	 */
	public long getDisposableDelay();

	/**
	 * Resets the disposing delay so as to restart the time counter
	 */
	public void resetDelay();

	/**
	 * Gets the lastTimeAccessed attribute of the DisposableResourceHolder object
	 *
	 * @return   The lastTimeAccessed value
	 */
	public long getLastTimeAccessed();

	/** Disposes of the resource immediately. */
	public void dispose();

	/**
	 * Returns true if the resource is still available
	 *
	 * @return   The resourceDirty value
	 */
	public boolean isAvailable();

	/**
	 * Locks the resource so it cannot be automatically disposed of until unlock
	 * is called.<br>
	 * Explicit disposing by calling dispose() is still possible though.
	 */
	public void lock();

	/**
	 * Unlocks the resource so it can be automatically disposed of again.<br>
	 * Explicit disposing by calling dispose() is still possible though.
	 */
	public void unlock();

	/**
	 * True if the resource is locked
	 *
	 * @return   The locked status
	 */
	public boolean isLocked();
}
