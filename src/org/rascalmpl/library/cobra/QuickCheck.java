/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI - added type parameters
 *   * Davy Landman - davy.landman@swat.engineering - SWAT.engineering
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.random.RandomValueGenerator;
import io.usethesource.vallang.random.util.TypeParameterBinder;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeStore;

public class QuickCheck {

    private final String EXPECT_TAG = "expected";
    private final Random stRandom;

    private static class InstanceHolder {
        public static final QuickCheck sInstance = new QuickCheck();
    }
    
    private QuickCheck() {
        stRandom = new Random();
    }


    public static QuickCheck getInstance() {
        return InstanceHolder.sInstance;
    }


    public boolean quickcheck(AbstractFunction function, int maxDepth, int maxWidth,
        int tries, boolean verbose, PrintWriter out) {

        String fname = function.getEnv().getName() + "::" + function.getName();

        Environment declEnv = function.getEnv();
        IValueFactory vf = function.getEval().getValueFactory();
        Type formals = function.getFormals();
        String expected = null;

        if(function.hasTag(EXPECT_TAG)){
            expected = ((IString) function.getTag(EXPECT_TAG)).getValue();
        }


        Type[] types = new Type[formals.getArity()];
        IValue[] values = new IValue[formals.getArity()];

        for (int n = 0; n < formals.getArity(); n++) {
            types[n] = formals.getFieldType(n);
        }
        
        Map<Type, Type> tpbindings = new TypeParameterBinder().bind(formals);
        Type[] actualTypes = new Type[types.length];
        for(int j = 0; j < types.length; j ++) {
            actualTypes[j] = types[j].instantiate(tpbindings);
        }
 
        if (formals.getArity() == 0) {
            tries = 1;
        }

        RandomValueGenerator generator = new RandomValueGenerator(vf, stRandom, maxDepth, maxWidth);
        for (int i = 0; i < tries; i++) {
            for (int n = 0; n < values.length; n++) {
                values[n] = generator.generate(types[n], declEnv.getRoot().getStore(), tpbindings);
            }
            
            boolean expectedThrown = false;
            try {

                IValue result = function.call(actualTypes, values, null).getValue();
                function.getEval().getStdOut().flush();

                if (expected != null) {
                    // no exception at all is thrown
                    return reportMissingException(fname, expected, out);
                }

                if (!((IBool) result).getValue()) {
                    // reducing to smallest case
                    values = findSmallestInput(function, expected, maxDepth, maxWidth, tries, declEnv, vf, types, values, tpbindings, actualTypes);
                    reportFailed(fname, "test returns false", tpbindings, formals, values, out);
                    return false;
                } else if (verbose && formals.getArity() > 0) {
                    out.println((i + 1) + ": Checked with " + Arrays.toString(values) + ": true");
                }
            } 
            catch (Throwable e) {
                if (!(e instanceof Throw)) {
                    e.printStackTrace();
                }
                if (expected != null) {
                    expectedThrown = e.getClass().toString().endsWith("." + expected)
                        || (e instanceof Throw && ((IConstructor)((Throw)e).getException()).getName().equals(expected));
                }
                if (!expectedThrown) {
                    values = findSmallestInput(function, expected, maxDepth, maxWidth, tries, declEnv, vf, types, values, tpbindings, actualTypes);
                    return reportFailed(fname, e.getMessage(), tpbindings, formals, values, out);
                }
            }

            if(expected != null && !expectedThrown){
                return reportMissingException(fname, expected, out);
            }
        }

        out.println("Test " + fname + (formals.getArity() > 0 ? " not refuted after " + tries + " tries with maximum depth " + maxDepth
            : " succeeded"));

        return true;
    }


    private IValue[] findSmallestInput(AbstractFunction function, String expected, int maxDepth, int maxWidth, int tries, Environment declEnv,
        IValueFactory vf, Type[] types, IValue[] values, Map<Type, Type> tpbindings, Type[] actualTypes) {
        if (types.length == 0) {
            return values;
        }
        values = findSmallestFailing(
            generateAlternatives(types, vf, stRandom, tries, maxDepth, maxWidth, declEnv.getRoot().getStore(), tpbindings),
            (IValue[] args) -> {
                try {
                    return ((IBool)(function.call(actualTypes, args, null).getValue())).getValue();
                } catch (Throwable e) {
                    if (!(e instanceof Throw)) {
                        e.printStackTrace();
                    }
                    if (expected != null) {
                        return e.getClass().toString().endsWith("." + expected)
                            || (e instanceof Throw && ((IConstructor)((Throw)e).getException()).getName().equals(expected));
                    }
                    return false;
                }
            }, 
            values );
        return values;
    }
    
    private IValue[] findSmallestFailing(Iterator<IValue[]> values, Function<IValue[], Boolean> test, IValue[] originalFailure) {
        while (values.hasNext()) {
            IValue[] currentValues = values.next();
            if (test.apply(currentValues)) {
                return currentValues;
            }
        }
        return originalFailure;
    }
    
    
    public static Iterator<IValue[]> generateAlternatives(Type[] types, IValueFactory vf, Random random, int maxTries, int maxDepth, int maxWidth, TypeStore store, Map<Type, Type> parameterBindings) {
        return new Iterator<IValue[]>() {
            private int currentDepth = 1;
            private int currentWidth = 1;
            private int tries = 0;
            private RandomValueGenerator generator = new RandomValueGenerator(vf, random, currentDepth, currentWidth);

            @Override
            public boolean hasNext() {
                return tries < maxTries || currentDepth <= maxDepth || currentWidth <= maxWidth;
            }

            @Override
            public IValue[] next() {
                if (tries == maxTries) {
                    if (currentWidth == maxWidth) {
                        currentDepth++;
                        currentWidth = 1;
                    }
                    else {
                        currentWidth++;
                    }
                    generator = new RandomValueGenerator(vf, random, currentDepth, currentWidth);
                    tries = 0;
                }
                tries++;
                IValue[] result = new IValue[types.length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = generator.generate(types[i], store, parameterBindings); 
                }
                return result;
            }
        };
        
    }

    private boolean reportFailed(String name, String msg, Map<Type, Type> tpbindings, Type formals, IValue[] values, PrintWriter out){
        out.println("Test " + name + " failed due to\n\t" + msg + "\n");
        if(tpbindings.size() > 0){
            out.println("Type parameters:");
            for(Type key : tpbindings.keySet()){
                out.println("\t" + key + " => " + tpbindings.get(key));
            }
        }
        out.println("Actual parameters:");
        for (int i = 0; i < formals.getArity(); i++) {
            IValue arg = values[i];
            out.println("\t" + formals.getFieldType(i) + " " + "=>" + arg);
        }
        out.println();
        return false;
    }

    private boolean reportMissingException(String name, String msg, PrintWriter out){
        out.println("Test " + name + " failed due to\n\tmissing exception: " + msg + "\n");
        return false;
    }

}
