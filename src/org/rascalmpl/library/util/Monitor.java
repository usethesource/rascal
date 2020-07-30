/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.util;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Monitor {
	private final IValueFactory vf;
    private final IRascalMonitor monitor;

	public Monitor(IValueFactory vf, IRascalMonitor monitor) {
		this.vf = vf;
		this.monitor = monitor;
	}

	public void startJob(IString name) {
		monitor.startJob(name.getValue());
	}

	public void startJob(IString name, IInteger totalWork) {
		monitor.startJob(name.getValue(), totalWork.intValue());
	}

	public void startJob(IString name, IInteger workShare, IInteger totalWork,
			IEvaluatorContext ctx) {
		monitor.startJob(name.getValue(), workShare.intValue(),
				totalWork.intValue());
	}

	public void event(IString name) {
		monitor.event(name.getValue());
	}

	public void event(IString name, IInteger inc) {
		monitor.event(name.getValue(), inc.intValue());
	}

	public void event(IInteger inc) {
		monitor.event(inc.intValue());
	}

	public IValue endJob(IBool succeeded) {
		return vf.integer(monitor.endJob(succeeded.getValue()));
	}

	public void todo(IInteger work) {
		monitor.todo(work.intValue());
	}
}
