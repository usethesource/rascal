/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
*******************************************************************************/
package org.rascalmpl.debug;

import org.rascalmpl.value.ISourceLocation;

public interface IRascalMonitor {
	/**
	 * Used to indicate an unknown amount of work to be done.
	 */
	public final int TODO_UNKNOWN = 0;
	
	/**
	 * The standard size of a unit of work. 
	 */
	public final int TODO_DEFAULT = 10;
	
	/**
	 * Register a job with a name, a default amount of work contributed to the overall task,
	 * and an unknown amount of steps to do.
	 */
	public void startJob(String name);
	
	/**
	 * Register a job with a name and a total amount of steps to do (this will also be the amount
	 * of work contributed to the parent job, if any).
	 */
	public void startJob(String name, int totalWork);

	/**
	 * Register a job with a name, the amount this will contribute to the overall task,
	 * and a total amount of steps to do.
	 */
	public void startJob(String name, int workShare, int totalWork);
	
	/**
	 * Log the <bold>start</bold> of an event. 
	 */
	public void event(String name);
	
	/**
	 * Log the start of an event with the amount of work that will be done when it's finished.
	 * An event is finished when the next event is logged, or when endJob() is called.
	 */
	public void event(String name, int inc);
	
	/**
	 * Log the start of an event with the amount of work that will be done when it's finished.
	 * An event is finished when the next event is logged, or when endJob() is called.
	 */
	public void event(int inc);
	
	/**
	 * This should always be called once for every startJob, unless an exception is thrown.
	 * @return The amount of work completed for this job (to help in future estimates)
	 */
	public int endJob(boolean succeeded);
	
	/**
	 * @return True if cancellation has been requested for this job
	 */
	public boolean isCanceled();
	
	/**
	 * Set the estimated remaining work for the current (sub)job.
	 * 
	 * @param work Amount of work remaining to be done, or 0 for unknown.
	 */
	public void todo(int work);
	
	/**
	 * Inform about a warning
	 */
	public void warning(String message, ISourceLocation src);
}
