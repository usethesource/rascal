package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.RascalExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class $RascalModule {
	public final static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	public final static TypeFactory $TF = TypeFactory.getInstance();
	public final static TypeStore $TS = new TypeStore();

	public static IInteger aint_divide_aint(IInteger a, IInteger b) {
		try {
			return a.divide(b);
		} catch(ArithmeticException e) {
			throw RascalExceptionFactory.arithmeticException("divide by zero");
		}
	}
}
