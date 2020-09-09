package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class GenerateActuals {
	static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	final static TypeFactory $TF = TypeFactory.getInstance();
	final int maxDepth ;
	final int maxWidth;
	final int tries;
	final Random random;

	public GenerateActuals(int maxDepth, int maxWidth, int tries){
		this.maxDepth = maxDepth;
		this.maxWidth = maxWidth;
		this.tries = tries;
		this.random = new Random();
	}
	
	public Stream<IValue[]> generateActuals(Type[] formals, TypeStore $TS) {
		Type[] types = formals;
		Map<Type, Type> tpbindings = new HashMap<>();
		
		Type[] actualTypes = new Type[types.length];
		for(int j = 0; j < types.length; j ++) {
			actualTypes[j] = types[j].instantiate(tpbindings);
		}

		Stream<IValue[]> s = 
				Stream.generate(() -> 
				{ IValue[] values = new IValue[formals.length];
				for (int n = 0; n < values.length; n++) {
//					System.err.print("n = " + n + ", types[n] = " + types[n]);
					values[n] = types[n].randomValue(random, $VF, $TS, tpbindings, maxDepth, maxWidth);
//					System.err.println(", values[n] = " + values[n]);
				}
				return values;
				});
		return s.limit(tries);
	}
}
