package org.meta_environment.rascal.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;

public interface IStrategyContext {

	public void update(IValue oldvalue, IValue newvalue);

	public IValue getValue();

	public void setValue(IValue value);

	public List<IValue> getChildren(IValue value);
}
