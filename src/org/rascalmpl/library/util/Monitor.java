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

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class Monitor {
	private final IValueFactory values;
    private final IRascalMonitor services;

	public Monitor(IValueFactory vf, IRascalMonitor monitor) {
		this.values = vf;
		this.services = monitor;
	}

	public void jobStart(IString name, IInteger work, IInteger totalWork) {
        services.jobStart(name.getValue(), work.intValue(), totalWork.intValue());
    }
	
	public void jobStep(IString name, IString message, IInteger inc) {
        services.jobStep(name.getValue(), message.getValue(), inc.intValue());
    }
	
	public IInteger jobEnd(IString name, IBool succeeded) {
        return values.integer(services.jobEnd(name.getValue(), succeeded.getValue()));
    }
	
	public IBool jobIsCancelled(IString name) {
        return values.bool(services.jobIsCanceled(name.getValue()));
    }
	
	public void jobTodo(IString name, IInteger work) {
        services.jobTodo(name.getValue(), work.intValue());
    }

	public void jobWarning(IString message, ISourceLocation src) {
        services.warning(message.getValue(), src);
    }
}
