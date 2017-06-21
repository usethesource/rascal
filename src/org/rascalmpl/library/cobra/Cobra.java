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
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;

public class Cobra {

    public static final String TRIES = "tries";
    public static final String MAXDEPTH = "maxDepth";
    public static final String MAXWIDTH = "maxWidth";

    public static Type reifyType(IValue type) {
        Type reified = type.getType();
        if (!(reified instanceof ReifiedType)) {
            throw RuntimeExceptionFactory.illegalArgument(type, null, null,
                "A reified type is required instead of " + reified);
        }
        return reified.getTypeParameters().getFieldType(0);
    }


    final IValueFactory vf;


    public Cobra(IValueFactory vf) {
        this.vf = vf;
    }

    public static int readIntTag(AbstractFunction test, String key, int defaultVal) {
        if (test.hasTag(key)) {
            int result = Integer.parseInt(((IString) test.getTag(key)).getValue());
            if (result < 1) {
                throw new IllegalArgumentException(key + " smaller than 1");
            }
            return result;
        } else {
            return defaultVal;
        }
    }


}