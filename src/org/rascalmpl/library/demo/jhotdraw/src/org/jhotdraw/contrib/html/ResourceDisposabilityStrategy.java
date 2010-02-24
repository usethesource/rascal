/*
 * @(#)ResourceDisposabilityStrategy.java
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
 * ResourceDisposabilityStrategy defines the interface for resource disposal
 * strategies.<br>
 * The strategy defines the logic used to determine which and when a resource can
 * be efficiently disposed of. A smarter the strategy leads to few resource
 * regeneration.
 *
 * @author  Eduardo Francos - InContext
 * @created 2 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public interface ResourceDisposabilityStrategy {

	/**
	 * Sets the manager holding the resources for this strategy
	 *
	 * @param manager  The new manager value
	 */
	public void setManager(DisposableResourceManager manager);


	/**
	 * Gets the manager holding the resources for this strategy
	 *
	 * @return   The manager value
	 */
	public DisposableResourceManager getManager();

	/**
	 * Activates the strategy which starts disposing of resources as fitted
	 *
	 * @exception ResourceManagerNotSetException  Description of the Exception
	 */
	public void startDisposing() throws ResourceManagerNotSetException;

	/**
	 * Deactivates the strategy that stops automatic disposal of resource.<br>
	 * The millis parameters specifies in milliseconds the time to wait for
	 * the disposal to stop. After this time the method returns, but the
	 * deactivation request remain active.
	 *
	 * @param millis  time to wait for disposal to stop
	 */
	public void stopDisposing(long millis);
}
