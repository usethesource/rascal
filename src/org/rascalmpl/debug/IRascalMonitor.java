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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.rascalmpl.interpreter.ConsoleRascalMonitor;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.repl.IsTTY;
import org.rascalmpl.repl.TerminalProgressBarMonitor;

import io.usethesource.vallang.ISourceLocation;
import jline.Terminal;
import jline.TerminalFactory;

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
	 * This utility method is not to be implemented by clients. It's a convenience
	 * function that helps to guarantee jobs that are started, are always ended.
	 */
	default <T> T job(String name, int totalWork, Supplier<T> block) {
		boolean result = false;
		try {
			jobStart(name, totalWork);
			return block.get();
		}
		finally {
			jobEnd(name, result);
		}
	}
	
	/**
	 * This utility method is not to be implemented by clients. It's a convenience
	 * function that helps to guarantee jobs that are started, are always ended.
	 * Also it provides easy access to the name of the current job, such that 
	 * this "magic" constant does not need to be repeated or stored elsewhere.
	 */
	default <T> T job(String name, int totalWork, Function<String, T> block) {
		boolean result = false;
		try {
			jobStart(name, totalWork);
			return block.apply(name);
		}
		finally {
			jobEnd(name, result);
		}
	}

	/**
	 * This utility method is not to be implemented by clients. It's a convenience
	 * function that helps to guarantee jobs that are started, are always ended.
	 * Also it provides easy access to the name of the current job, such that 
	 * this "magic" constant does not need to be repeated or stored elsewhere.
	 * @param <T>        return type for the entire job
	 * @param name       name of the job to identify the progress bar
	 * @param totalWork  total work to be done
	 * @param block      lambda to execute. It will get the name as a parameter 
	 *                   and a `step` function to call with the current message and
	 *                   the amount of work that has been done.
	 * 
	 * Example:
	 * ```
	 * job("loading", 100, (n, step) -> {
	 *   for (int i = 0; i < 100; i+= 10) 
	 *     doSomething()
	 *     step("did " + i, 10);
	 *   }
	 * });
	 * 
	 * @return whatever the block returns is returned by the job
	 */
	default <T> T job(String name, int totalWork, BiFunction<String, BiConsumer<String, Integer>, T> block) {
		boolean result = false;
		try {
			jobStart(name, totalWork);
			return block.apply(name, (msg, worked) -> jobStep(name, msg, worked));
		}
		finally {
			jobEnd(name, result);
		}
	}

	/**
	 * Log the start of an event with the amount of work that will be done when it's finished.
	 * An event is finished when the next event is logged, or when endJob() is called.
	 * 
	 * jobSteps should be _ignored_ for jobs that have not started. This helps with modularizing
	 * the monitoring of steps acros complex reusable pieces algorithms. If the context registers
	 * a job, progress is monitored, otherwise it is not shown. 
	 */
	public void jobStep(String name, String message, int workShare);
	
	default void jobStep(String name, String message) {
		jobStep(name, message, 1);
	}
	
	/**
	 * This should always be called once for every startJob, unless an exception is thrown.
	 * @return The amount of work completed for this job (to help in future estimates)
	 */
	public int jobEnd(String name, boolean succeeded);
	
	/**
	 * @param label
	 * @return True if cancellation has been requested for this job
	 */
	public boolean jobIsCanceled(String name);
	
	/**
	 * Set the estimated remaining work for the current (sub)job.
	 * @param string
	 * 
	 * @param work Amount of work remaining to be done, or 0 for unknown.
	 */
	public void jobTodo(String name, int work);
	
	/**
	 * Remove all active jobs from the monitor
	 */
	public void endAllJobs();

	/**
	 * Inform (about a warning
	 */
	public void warning(String message, ISourceLocation src);

	/**
	 * Convenience method will produce a monitor with ANSI progress bars if possible,
	 * and otherwise default to a dumn terminal console progress logger.
	 * @return
	 */
	public static IRascalMonitor buildConsoleMonitor(InputStream in, OutputStream out) {
		return IsTTY.isTTY()
			? new TerminalProgressBarMonitor(out, in, TerminalFactory.get())
			: new ConsoleRascalMonitor(new PrintStream(out))
		;
	}

	/**
	 * Convenience method will produce a monitor that eats up all events without logging
	 * or reporting
	 */
	public default IRascalMonitor buildNullMonitor() {
		return new NullRascalMonitor();
	}
}
