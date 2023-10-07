package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;

public class DescendantDescriptorAlwaysTrue implements IDescendantDescriptor {
	private final boolean concreteMatch;
	
	public DescendantDescriptorAlwaysTrue(IBool concreteMatch){
		this.concreteMatch = concreteMatch.getValue();
	}
	@Override
	public boolean isConcreteMatch() {
		return concreteMatch;
	}

	@Override
	public boolean isAllwaysTrue() {
		return true;
	}

	@Override
	public IBool shouldDescentInAbstractValue(IValue subject) {
		return TRUE;
	}

	@Override
	public IBool shouldDescentInConcreteValue(ITree subject) {
		return TRUE;
	}

}
