/*
 * @(#)StandardDisposableResourceManager.java
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

import java.util.WeakHashMap;

/**
 * StandardDisposableResourceManager implements disposable resource management
 * using a client supplied strategy.<br>
 *
 * @author  Eduardo Francos - InContext
 * @created 2 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class StandardDisposableResourceManager implements DisposableResourceManager {

	/** The registered resources */
	private WeakHashMap resources;

	/** The disposing strategy */
	private ResourceDisposabilityStrategy strategy;

	/**
	 *Constructor for the StandardDisposableResourceManager object
	 *
	 * @param strategy  Description of the Parameter
	 */
	public StandardDisposableResourceManager(ResourceDisposabilityStrategy newStrategy) {
		resources = new WeakHashMap();
		setStrategy(newStrategy);
		getStrategy().setManager(this);
	}

	/**
	 * Registers a resource to be automatically disposed of
	 *
	 * @param resource  the resource
	 */
	public synchronized void registerResource(DisposableResourceHolder resource) {
		resources.put(resource, resource);
	}

	/**
	 * Unregisters a resource so it is not automatically GCed.<br>
	 * If does nothing if the resource was not registered with this manager
	 *
	 * @param resource  the resource
	 */
	public synchronized void unregisterResource(DisposableResourceHolder resource) {
		resources.remove(resource);
	}

	/**
	 * Gets an iterator on the managed resources
	 *
	 * @return   The iterator
	 */
	public Iterator getResources() {
		return resources.values().iterator();
	}

	/**
	 * Description of the Method
	 *
	 * @param resource  the resource
	 * @return          True if the resource is registered with this manager
	 */
	public synchronized boolean managesResource(DisposableResourceHolder resource) {
		return resources.containsValue(resource);
	}

	/**
	 * Gets the strategy attribute of the StandardDisposableResourceManager object
	 *
	 * @return   The strategy value
	 */
	public ResourceDisposabilityStrategy getStrategy() {
		return strategy;
	}

	/**
	 * Sets the strategy attribute of the StandardDisposableResourceManager object
	 *
	 * @param newStrategy  The new strategy value
	 */
	public void setStrategy(ResourceDisposabilityStrategy newStrategy) {
		strategy = newStrategy;
	}

	/**
	 * Activates the strategy which starts disposing of resources as fitted
	 *
	 * @exception ResourceManagerNotSetException  Description of the Exception
	 */
	public void startDisposing() throws ResourceManagerNotSetException {
		getStrategy().startDisposing();
	}

	/**
	 * Deactivates the strategy that stops automatic disposal of resource.<br>
	 * The millis parameters specifies in milliseconds the time to wait for
	 * the disposal to stop. After this time the method returns, but the
	 * deactivation request remain active.
	 *
	 * @param millis  time to wait for disposal to stop
	 */
	public void stopDisposing(long millis) {
		getStrategy().stopDisposing(millis);
	}
}
