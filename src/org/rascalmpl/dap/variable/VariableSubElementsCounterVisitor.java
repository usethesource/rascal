/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
package org.rascalmpl.dap.variable;

import io.usethesource.vallang.*;
import io.usethesource.vallang.visitors.IValueVisitor;

/**
    Visitor used to get the number of sub elements of a variable
 **/
public class VariableSubElementsCounterVisitor implements IValueVisitor<VariableSubElementsCounter, RuntimeException> {

    @Override
    public VariableSubElementsCounter visitString(IString o) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitReal(IReal o) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitRational(IRational o) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitList(IList o) throws RuntimeException {
        return new VariableSubElementsCounter(0, o.length());
    }

    @Override
    public VariableSubElementsCounter visitSet(ISet o) throws RuntimeException {
        return new VariableSubElementsCounter(0, o.size());
    }

    @Override
    public VariableSubElementsCounter visitSourceLocation(ISourceLocation o) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitTuple(ITuple o) throws RuntimeException {
        return new VariableSubElementsCounter(0, o.arity());
    }

    @Override
    public VariableSubElementsCounter visitNode(INode o) throws RuntimeException {
        return new VariableSubElementsCounter(0, o.arity());
    }

    @Override
    public VariableSubElementsCounter visitConstructor(IConstructor o) throws RuntimeException {
        return new VariableSubElementsCounter((o.mayHaveKeywordParameters() ? o.asWithKeywordParameters().getParameters().size() : 0)+o.arity(), 0);
    }

    @Override
    public VariableSubElementsCounter visitInteger(IInteger o) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitMap(IMap o) throws RuntimeException {
        return new VariableSubElementsCounter(0, o.size());
    }

    @Override
    public VariableSubElementsCounter visitBoolean(IBool boolValue) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitExternal(IExternalValue externalValue) throws RuntimeException {
        return new VariableSubElementsCounter(0, 0);
    }

    @Override
    public VariableSubElementsCounter visitDateTime(IDateTime o) throws RuntimeException {
        if(o.isDateTime()){
            return new VariableSubElementsCounter(9, 0);
        }
        else if(o.isDate()){
            return new VariableSubElementsCounter(3, 0);
        }
        else{
            return new VariableSubElementsCounter(6, 0);
        }
    }
}
