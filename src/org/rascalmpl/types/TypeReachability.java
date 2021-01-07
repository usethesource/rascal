/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.types;

import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.interpreter.env.Environment;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class TypeReachability {
    public static boolean mayOccurIn(Type small, Type large, Environment env) {
        return mayOccurIn(small, large, new HashSet<Type>(), env);
    }

    static private boolean mayOccurIn(final Type small, final Type large, final Set<Type> seen, final Environment env) {
        if (small.isBottom() && !large.isBottom()) {
            return false;
        }

        if (small.comparable(large)) {
            return true;
        }

        return large.accept(new DefaultRascalTypeVisitor<Boolean, RuntimeException>(false) {
            @Override
            public Boolean visitList(Type type)  {
                return mayOccurIn(small, large.getElementType(), seen, env);
            }

            @Override
            public Boolean visitSet(Type type)  {
                return mayOccurIn(small, large.getElementType(), seen, env);
            }

            @Override
            public Boolean visitMap(Type type)  {
                return mayOccurIn(small, large.getKeyType(), seen, env)
                    ||  mayOccurIn(small, large.getValueType(), seen, env);
            }

            @Override
            public Boolean visitTuple(Type type)  {
                for (int i = 0; i < large.getArity(); i++) {
                    if (mayOccurIn(small, large.getFieldType(i), seen, env)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Boolean visitFunction(Type type)  {
                return false;
            }

            @Override
            public Boolean visitReified(RascalType type) throws RuntimeException {
                // TODO: we can be more precise here because we know only SymbolFactory and Definitions can occur in 
                // reified types
                return true;
            }

            @Override
            public Boolean visitNonTerminal(RascalType type) throws RuntimeException {
                // TODO: Until we have more precise info about the types in the
                // concrete syntax
                // we just return true here.
                return true;
            }

            @Override
            public Boolean visitConstructor(Type type)  {
                for (int i = 0; i < type.getArity(); i++) {
                    if (mayOccurIn(small, type.getFieldType(i), seen, env)) {
                        return true;
                    }
                }
                return false;
            }


            @Override
            public Boolean visitAbstractData(Type type)  {
                if (small.equivalent(TypeFactory.getInstance().nodeType())) {
                    return true;
                }

                seen.add(large);

                for (Type alt : env.lookupAlternatives(large)) {
                    for (int i = 0; i < alt.getArity(); i++) {
                        Type fType = alt.getFieldType(i);
                        if (seen.add(fType) && mayOccurIn(small, fType, seen, env)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

}
