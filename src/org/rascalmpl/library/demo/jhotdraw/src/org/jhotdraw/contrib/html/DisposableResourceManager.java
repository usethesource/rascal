/*
 * @(#)DisposableResourceManager.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.util.Iterator;

/**
 * DisposableResourceManager defines the interface for managers of disposable
 * resources.<br>
 * A DisposableResourceManager implements a strategy for efficient disposal
 * of seldom used resources.
 *
 * @author  Eduardo Francos - InContext
 * @created 2 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public interface DisposableResourceManager {

	/**
	 * Registers a resource with the manager so as to be disposed
	 *
	 * @param resource  the resource
	 */
	public void registerResource(DisposableResourceHolder resource);

	/**
	 * Unregisters a resource so it is not automatically GCed.<br>
	 *
	 * @param resource  the resource
	 */
	public void unregisterResource(DisposableResourceHolder resource);

	/**
	 * Returns an iterator on the managed resources
	 *
	 * @return   The resources iterator
	 */
	public Iterator getResources();

	/**
	 * True if the resource is registered with this manager
	 *
	 * @param resource  The resource
	 * @return          True if the resource is registered with this manager
	 */
	public boolean managesResource(DisposableResourceHolder resource);

	/**
	 * Activates the strategy which starts disposing of resources as fitted
	 *
	 * @exception ResourceManagerNotSetException  Description of the Exception
	 */
	public void startDisposing() throws ResourceManagerNotSetException;

	/**
	 * Deactivates the strategy that stops automatic disposal of resource.<br>
	 * The millis parameters specifies in milliseconds the time to wait for
	 * the disposal to stop. After this time the method returns whether the
	 * disposal was stopped or not, but the desactivation request remains active.
	 *
	 * @param millis  time to wait for disposal to stop
	 */
	public void stopDisposing(long millis);
}
