package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredFunctionError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFunctionError;

public class CalleeCandidatesResult extends Lambda implements Iterable<Lambda> {
	private final List<Lambda> candidates;
	private final String name;

	public CalleeCandidatesResult(String name, List<Lambda> candidates) {
		super(null, null, null, name, null, false, null, null);
		this.name = name;
		this.candidates = candidates;
	}

	public CalleeCandidatesResult(String name) {
		super(null, null, null, name, null, false, null, null);
		this.name = name;
		this.candidates = new LinkedList<Lambda>();
	}

	@Override
	public Result<?> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		Type tuple = getTypeFactory().tupleType(argTypes);
		
		for (Lambda candidate : candidates) {
			if (candidate.match(tuple)) {
				return candidate.call(argTypes, argValues, ctx);
			}
		}
		
		throw new UndeclaredFunctionError(name, ctx.getCurrentAST());
	}
	
	public CalleeCandidatesResult join(CalleeCandidatesResult other) {
		List<Lambda> joined = new LinkedList<Lambda>();
		joined.addAll(candidates);
		joined.addAll(0, other.candidates);
		return new CalleeCandidatesResult(name,joined);
	}
	
	public void add(Lambda candidate) {
		for (Lambda other : this) {
			if (!other.equals(candidate) && candidate.isAmbiguous(other)) {
				throw new RedeclaredFunctionError(candidate.getHeader(), other.getHeader(), candidate.getAst());
			}
		}
		
		candidates.add(0, candidate);
	}

	public int size() {
		return candidates.size();
	}

	public Iterator<Lambda> iterator() {
		return candidates.iterator();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalleeCandidatesResult) {
			return candidates.equals(((CalleeCandidatesResult) obj).candidates);
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		for (Lambda l : this) {
			b.append('\t');
			b.append(l.toString());
			b.append('\n');
		}
		b.append("]");
		
		return b.toString();
	}
}
