package org.rascalmpl.library.util;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

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
