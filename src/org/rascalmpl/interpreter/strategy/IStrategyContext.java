package org.rascalmpl.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;

public interface IStrategyContext {

	public void update(IValue oldvalue, IValue newvalue);

	public IValue getValue();

	public List<IValue> getChildren(IValue value);
}
