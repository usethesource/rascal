/*
 * @(#)DisposableResourceManagerFactory.java
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
 * DisposableResourceManagerFactory creates DisposableResourceHolders on behalf
 * of requesting clients. It automatically registers the holders with its singleton
 * standard resource manager
 *
 * @author  Eduardo Francos - InContext
 * @created 2 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public abstract class DisposableResourceManagerFactory {

	/** The default periodicity for resource disposal */
	public static long DEFAULT_DISPOSAL_PERIODICITY = 60000;

	/** The singleton current resource manager */
	protected static DisposableResourceManager currentManager = null;

	protected static ResourceDisposabilityStrategy currentStrategy = null;

	protected static DisposableResourceHolder holderPrototype = null;

	/**
	 * Gets the current manager.
	 *
	 * @return   The currentManager
	 */
	public static DisposableResourceManager getManager() {
		return currentManager;
	}

	/**
	 * Sets the strategy of the DisposableResourceManagerFactory
	 * class. This is a one shot thing that should be initialized before using it
	 * so if there is already a strategy this returns without further notice.
	 *
	 * @param strategy  The new disposalStrategy
	 */
	public static void setStrategy(ResourceDisposabilityStrategy strategy) {
		currentStrategy = strategy;
	}

	/**
	 * Returns a new standard resource holder already registered with the default
	 * resource manager
	 *
	 * @param resource  Description of the Parameter
	 * @return          the newly created resource holder
	 */
	public static DisposableResourceHolder createStandardHolder(Object resource) {
		// ensure the manager is set and running
		initManager();

		// now create the holder and register it
		DisposableResourceHolder holder = (DisposableResourceHolder)holderPrototype.clone();
		holder.setResource(resource);
		getManager().registerResource(holder);

		return holder;
	}

	/** Initializes the standard resource disposal manager */
	protected static void initManager() {
		if (currentManager == null) {
			// ensure we have a holder prototype
			if (holderPrototype == null) {
				holderPrototype = new StandardDisposableResourceHolder();
			}
			// ensure we have a strategy
			if (currentStrategy == null) {
				currentStrategy = new ETSLADisposalStrategy(DEFAULT_DISPOSAL_PERIODICITY);
			}
			// ensure we have a manager
			if (currentManager == null) {
			   currentManager = new StandardDisposableResourceManager(currentStrategy);
			}

			// start the whole thing
			try {
				currentManager.startDisposing();
			}
			catch (ResourceManagerNotSetException ex) {
				// we set it so we shouldn't get here
			}
		}
	}
}
