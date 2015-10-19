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

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class MonitorCompiled {
	private final IValueFactory vf;

	public MonitorCompiled(IValueFactory vf) {
		this.vf = vf;
	}

	public void startJob(IString name,  RascalExecutionContext rex) {
		rex.startJob(name.getValue());
	}

	public void startJob(IString name, IInteger totalWork,  RascalExecutionContext rex) {
		rex.startJob(name.getValue(), totalWork.intValue());
	}

	public void startJob(IString name, IInteger workShare, IInteger totalWork,
			 RascalExecutionContext rex) {
		rex.startJob(name.getValue(), workShare.intValue(),
				totalWork.intValue());
	}

	public void event(IString name, RascalExecutionContext rex) {
		rex.event(name.getValue());
	}

	public void event(IString name, IInteger inc,  RascalExecutionContext rex) {
		rex.event(name.getValue(), inc.intValue());
	}

	public void event(IInteger inc,  RascalExecutionContext rex) {
		 rex.event(inc.intValue());
	}

	public IValue endJob(IBool succeeded,  RascalExecutionContext rex) {
		return vf.integer(rex.endJob(succeeded.getValue()));
	}

	public void todo(IInteger work,  RascalExecutionContext rex) {
		rex.todo(work.intValue());
	}
}
