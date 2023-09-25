package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.TypeFactory;

public interface IDescendantDescriptor {
	static TypeFactory TF = TypeFactory.getInstance();
	static IBool TRUE = IRascalValueFactory.getInstance().bool(true);
	static IBool FALSE = IRascalValueFactory.getInstance().bool(false);
	
	boolean isConcreteMatch();

	boolean isAllwaysTrue();

	IBool shouldDescentInAbstractValue(IValue subject);

	IBool shouldDescentInConcreteValue(ITree subject);

}