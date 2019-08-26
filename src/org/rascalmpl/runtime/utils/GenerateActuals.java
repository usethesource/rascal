package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.random.RandomValueGenerator;
import io.usethesource.vallang.random.util.TypeParameterBinder;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class GenerateActuals {
	static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	final static TypeFactory $TF = TypeFactory.getInstance();
	final int maxDepth ;
	final int maxWidth;
	final int tries;
	final RandomValueGenerator generator;

	public GenerateActuals(int maxDepth, int maxWidth, int tries){
		this.maxDepth = maxDepth;
		this.maxWidth = maxWidth;
		this.tries = tries;
		this.generator = new RandomValueGenerator($VF, new Random(), maxDepth, maxWidth, true);
	}
	
	public Stream<IValue[]> generateActuals(Type[] formals) {
		Type[] types = formals;
		Map<Type, Type> tpbindings = new TypeParameterBinder().bind($TF.tupleType(formals));
		
		Type[] actualTypes = new Type[types.length];
		for(int j = 0; j < types.length; j ++) {
			actualTypes[j] = types[j].instantiate(tpbindings);
		}

		Stream<IValue[]> s = 
				Stream.generate(() -> 
				{ IValue[] values = new IValue[formals.length];
				for (int n = 0; n < values.length; n++) {
					values[n] = generator.generate(types[n], $RascalModule.$TS, tpbindings);
				}
				return values;
				});
		return s.limit(tries);
	}
}
