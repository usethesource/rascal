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

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

public class Monitor {
	private final IValueFactory vf;

	public Monitor(IValueFactory vf) {
		this.vf = vf;
	}

	public void startJob(IString name, IEvaluatorContext ctx) {
		ctx.getEvaluator().startJob(name.getValue());
	}

	public void startJob(IString name, IInteger totalWork, IEvaluatorContext ctx) {
		ctx.getEvaluator().startJob(name.getValue(), totalWork.intValue());
	}

	public void startJob(IString name, IInteger workShare, IInteger totalWork,
			IEvaluatorContext ctx) {
		ctx.getEvaluator().startJob(name.getValue(), workShare.intValue(),
				totalWork.intValue());
	}

	public void event(IString name, IEvaluatorContext ctx) {
		ctx.getEvaluator().event(name.getValue());
	}

	public void event(IString name, IInteger inc, IEvaluatorContext ctx) {
		ctx.getEvaluator().event(name.getValue(), inc.intValue());
	}

	public void event(IInteger inc, IEvaluatorContext ctx) {
		ctx.getEvaluator().event(inc.intValue());
	}

	public IValue endJob(IBool succeeded, IEvaluatorContext ctx) {
		return vf.integer(ctx.getEvaluator().endJob(succeeded.getValue()));
	}

	public void todo(IInteger work, IEvaluatorContext ctx) {
		ctx.getEvaluator().todo(work.intValue());
	}
}
