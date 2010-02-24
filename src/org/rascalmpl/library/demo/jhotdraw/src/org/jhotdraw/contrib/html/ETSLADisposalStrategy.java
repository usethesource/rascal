/*
 * @(#)ETSLADisposalStrategy.java
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
 * ETSLADisposalStrategy implements an Ellapsed Time Since Last Access disposal
 * strategy.<bt>
 * It checks the last time a resource was accessed and if greater than
 * the resource's disposalDelay it disposes of the resource so that it can
 * be GCed.<br>
 * Disposal activity must be explicitely started using the startDisposing method
 *
 * @author    Eduardo Francos - InContext
 * @created   2 mai 2002
 * @version   1.0
 */
public class ETSLADisposalStrategy implements ResourceDisposabilityStrategy {

	/** The associated resource manager */
	private DisposableResourceManager manager;

	/** The periodicity at wich disposal is checked */
	private long gcPeriodicity = 60000;

	/** the thread that calls this periodically */
	private DisposalThread disposalThread = null;

	/** True if disposal is active and running */
	private boolean disposingActive = false;


	/**
	 * Constructor for the ETSLADisposalStrategy object
	 */
	public ETSLADisposalStrategy() { }

	/**
	 *Constructor for the ETSLADisposalStrategy object
	 *
	 * @param periodicity  the periodicity at which to check for disposable resources
	 */
	public ETSLADisposalStrategy(long periodicity) {
		this(null, periodicity);
	}

	/**
	 *Constructor for the ETSLADisposalStrategy object
	 *
	 * @param manager      the manager
	 * @param periodicity  the periodicity at which to check for disposable resources
	 */
	public ETSLADisposalStrategy(DisposableResourceManager newManager, long newPeriodicity) {
		setManager(newManager);
		setPeriodicity(newPeriodicity);
		initDisposalThread();
	}

	/**
	 * Sets the manager holding the resources for this strategy
	 *
	 * @param manager  The new manager value
	 */
	public synchronized void setManager(DisposableResourceManager newManager) {
		// if new manager is null the stop disposing
		if (getManager() == null) {
			stopDisposing(Long.MAX_VALUE);
		}
		manager = newManager;
	}

	/**
	 * Gets the manager holding the resources for this strategy
	 *
	 * @return   The manager value
	 */
	public DisposableResourceManager getManager() {
		return manager;
	}

	/**
	 * Activates the strategy which starts disposing of resources as fitted
	 *
	 * @exception ResourceManagerNotSetException  thrown if the manager has not
	 * been set, so impossible to run
	 */
	public void startDisposing() throws ResourceManagerNotSetException {
		if (getManager() == null) {
			throw new ResourceManagerNotSetException();
		}

		// do nothing if already active
		if (disposingActive) {
			return;
		}

		disposingActive = true;
		disposalThread.start();
	}

	/**
	 * Deactivates the strategy that stops automatic disposal of resources.<br>
	 * The millis parameters specifies in milliseconds the time to wait for
	 * the disposal to stop. After this time the method returns, but the
	 * deactivation request remains active.
	 *
	 * @param millis  time to wait for disposal to stop
	 */
	public void stopDisposing(long millis) {
		// do nothing if not disposing
		if (!disposingActive) {
			return;
		}
		// request the thread to stop and wait for it
		try {
			disposalThread.interruptDisposalPending = true;
			disposalThread.join(millis);
		}
		catch (InterruptedException ex) {
			// ignore
		}
		finally {
			disposingActive = false;
		}
	}

	/**
	 * Initializes the disposal thread if not alrady done.
	 */
	protected void initDisposalThread() {
		if (disposalThread != null) {
			return;
		}

		disposalThread = new DisposalThread(this, getPeriodicity());
	}

	/**
	 * Dispose of all the resources whose dispose delay has expired and
	 * are not locked
	 */
	protected synchronized void dispose() {
		synchronized (getManager()) {
			long currentTime = System.currentTimeMillis();
			Iterator resourceIter = getManager().getResources();
			DisposableResourceHolder resource;

			while (resourceIter.hasNext()) {
				resource = (DisposableResourceHolder)resourceIter.next();
				synchronized (resource) {
					if (!resource.isLocked() && (resource.getLastTimeAccessed() + resource.getDisposableDelay()) < currentTime) {
						resource.dispose();
					}
				}
			}
		}
	}

	/**
	 * Gets the periodicity attribute of the ETSLADisposalStrategy object
	 *
	 * @return   The periodicity value
	 */
	public long getPeriodicity() {
		return gcPeriodicity;
	}

	/**
	 * Sets the periodicity attribute of the ETSLADisposalStrategy object
	 *
	 * @param newPeriodicity  The new periodicity value
	 */
	public void setPeriodicity(long newPeriodicity) {
		gcPeriodicity = newPeriodicity;
		if (disposalThread != null) {
			disposalThread.setPeriodicity(newPeriodicity);
		}
	}
}

/**
 * Description of the Class
 *
 * @author    Eduardo Francos - InContext
 * @created   2 mai 2002
 */
class DisposalThread extends Thread {

	private ETSLADisposalStrategy strategy;

	/** The periodicity at wich disposal is checked */
	private long periodicity = 60000;

	/** Description of the Field */
	boolean interruptDisposalPending = false;

	/**
	 *Constructor for the DisposalThread object
	 *
	 * @param strategy     the strategy
	 * @param periodicity  the periodicity at which the strategy should be called
	 */
	DisposalThread(ETSLADisposalStrategy newStrategy, long newPeriodicity) {
		strategy = newStrategy;
		periodicity = newPeriodicity;
	}

	/**
	 * Main processing method for the DisposalThread object
	 */
	public void run() {
		interruptDisposalPending = false;
		while (!interruptDisposalPending) {
			try {
				sleep(periodicity);
			}
			catch (Exception ex) {
				// just exit
				break;
			}
			strategy.dispose();
		}
		interruptDisposalPending = false;
	}

	/**
	 * Gets the periodicity attribute of the ETSLADisposalStrategy object
	 *
	 * @return   The periodicity value
	 */
	public long getPeriodicity() {
		return periodicity;
	}

	/**
	 * Sets the periodicity attribute of the ETSLADisposalStrategy object
	 *
	 * @param newPeriodicity  The new periodicity value
	 */
	public void setPeriodicity(long newPeriodicity) {
		periodicity = newPeriodicity;
	}

	/** Description of the Method */
	public void interruptDisposal() {
		interruptDisposalPending = true;
	}
}
