package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;


import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class $RascalModule {
	public final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	public final static TypeFactory $TF = TypeFactory.getInstance();
	public final static TypeStore $TS = new TypeStore();
}
