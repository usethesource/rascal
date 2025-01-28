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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

public class BatchProgressMonitor implements IRascalMonitor {
	PrintWriter out;

	public BatchProgressMonitor() {
		this(new PrintWriter(System.err, true));
	}

	public BatchProgressMonitor(PrintStream out) {
		this(new PrintWriter(out, true));
	}

	public BatchProgressMonitor(PrintWriter out) {
		this.out = out;
	}

	@Override
	public int jobEnd(String name, boolean succeeded) {
		out.println("\tJob done: "  + name);
		return 0;
	}

	@Override
	public void jobStep(String name, String msg, int inc) {
		// not printing intermediate steps to keep the logs clean.
	}

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
		out.println("Job started: " + name);
		out.flush();
	}

	@Override
	public void jobTodo(String name, int work) {
	}

	@Override
	public boolean jobIsCanceled(String name) {
		return false;
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		out.println("Warning: " + message);
		out.flush();
	}

	@Override
	public void endAllJobs() {
		// ignore
	}
}
