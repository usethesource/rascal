/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.runtime.utils;

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
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;

public class GenerateActuals {
	static IValueFactory $VF = ValueFactoryFactory.getValueFactory();
	final static TypeFactory $TF = TypeFactory.getInstance();
	final int maxDepth ;
	final int maxWidth;
	final int tries;
	final Random random;
	final RandomTypesConfig typesConfig;

	public GenerateActuals(int maxDepth, int maxWidth, int tries){
		this.maxDepth = maxDepth;
		this.maxWidth = maxWidth;
		this.tries = tries;
		this.random = new Random();
		this.typesConfig = RandomTypesConfig.defaultConfig(random).withoutRandomAbstractDatatypes();
	}
	
	public Stream<IValue[]> generateActuals(Type[] formals, TypeStore $TS) {
		Type[] types = formals;
		Map<Type, Type> tpbindings = new HashMap<>();
		
		Type[] actualTypes = new Type[types.length];
		for(int j = 0; j < types.length; j ++) {
			actualTypes[j] = types[j].instantiate(tpbindings);
		}

		Stream<IValue[]> s = 
				Stream.generate(() -> { 
					IValue[] values = new IValue[formals.length];

					for (int n = 0; n < values.length; n++) {
						if (n > 1 && types[n-1] == types[n] && random.nextInt(4) == 0 /* p=25% */) {
							// once in a while duplicate a parameter, but only if it fits the same type
							values[n] = values[n-1];
							continue;
						}
						
						values[n] = types[n].randomValue(random, typesConfig, $VF, $TS, tpbindings, maxDepth, maxWidth);
					}

					return values;
				});
		return s.limit(tries);
	}
}
