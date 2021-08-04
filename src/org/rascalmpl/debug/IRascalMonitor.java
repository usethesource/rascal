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

import io.usethesource.vallang.ISourceLocation;

public interface IRascalMonitor {
	/**
	 * Register a job with a name, the amount this will contribute to the overall task,
	 * and a total amount of steps to do.
	 */
	public void jobStart(String name, int workShare, int totalWork);
	
	default void jobStart(String name) {
		jobStart(name, 1, 100);
	} 

	default void jobStart(String name, int totalWork) {
		jobStart(name, 1, totalWork);
	}

	/**
	 * Log the start of an event with the amount of work that will be done when it's finished.
	 * An event is finished when the next event is logged, or when endJob() is called.
	 */
	public void jobStep(String name, int workShare);
	
	default void jobStep(String name) {
		jobStep(name, 1);
	}
	
	/**
	 * This should always be called once for every startJob, unless an exception is thrown.
	 * @return The amount of work completed for this job (to help in future estimates)
	 */
	public int jobEnd(boolean succeeded);
	
	/**
	 * @return True if cancellation has been requested for this job
	 */
	public boolean jobIsCanceled();
	
	/**
	 * Set the estimated remaining work for the current (sub)job.
	 * 
	 * @param work Amount of work remaining to be done, or 0 for unknown.
	 */
	public void jobTodo(int work);
	
	/**
	 * Inform about a warning
	 */
	public void warning(String message, ISourceLocation src);
}
