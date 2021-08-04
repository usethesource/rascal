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

public class ConsoleRascalMonitor implements IRascalMonitor {
	PrintWriter out;

	public ConsoleRascalMonitor() {
		this.out = new PrintWriter(System.err);
	}

	public ConsoleRascalMonitor(PrintStream out) {
		this.out = new PrintWriter(out);
	}

	@Override
	public int jobEnd(boolean succeeded) {
		return 0;
	}

	@Override
	public void jobStep(String name, int inc) {
		//out.println(name);
		//out.flush();
	}

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
		out.println(name);
		out.flush();
	}

	@Override
	public void jobTodo(int work) {
	}

	@Override
	public boolean jobIsCanceled() {
		return false;
	}

	@Override
	public void warning(String message, ISourceLocation src) {
		out.println("Warning: " + message);
		out.flush();
	}
}
