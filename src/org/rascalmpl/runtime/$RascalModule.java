package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;


import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class $RascalModule {
	protected final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	protected final static TypeFactory $TF = TypeFactory.getInstance();
	protected final static TypeStore $TS = new TypeStore();
}
