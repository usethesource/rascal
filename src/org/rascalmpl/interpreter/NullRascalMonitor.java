/*******************************************************************************
 * Copyright (c) 2009-2013 CWI All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 * * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 * * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.interpreter;

import org.rascalmpl.debug.IRascalMonitor;

import io.usethesource.vallang.ISourceLocation;

public class NullRascalMonitor implements IRascalMonitor {
	@Override
	public int jobEnd(String name, boolean succeeded) {
		return 0;
	}

	@Override
	public void jobStep(String name, String msg, int inc) {
	}

	@Override
	public void jobStart(String name, int workShare, int totalWork) {
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
		return;
	}
}
