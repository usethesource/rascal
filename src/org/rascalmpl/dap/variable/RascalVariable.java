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

import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class RascalVariable {
    private int referenceID;
    private final Type type;
    private final String name;
    private final IValue value;
    private final String displayValue;
    private int namedVariables = 0;
    private int indexedVariables = 0;

    public RascalVariable(Type type, String name, IValue value, IDEServices services) {
        this.referenceID = -1;
        this.type = type;
        this.name = name;
        this.value = value;
        this.displayValue = RascalVariableUtils.getDisplayString(value, services);
    }

    public void setNamedVariables(int namedVariables) {
        this.namedVariables = namedVariables;
    }

    public void setIndexedVariables(int indexedVariables) {
        this.indexedVariables = indexedVariables;
    }

    public int getReferenceID(){
        return referenceID;
    }

    public Type getType(){
        return type;
    }

    public void setReferenceID(int referenceID) {
        this.referenceID = referenceID;
    }

    public String getName() {
        return name;
    }

    public IValue getValue() {
        return value;
    }

    public String getDisplayValue(){
        return displayValue;
    }

    public boolean hasSubFields(){
        if(type == null) {
            return false;
        }

        return type.isList() || type.isMap() || type.isSet() || type.isAliased() || type.isNode() || type.isConstructor() || type.isRelation() || type.isTuple() || type.isDateTime() || (type.isExternalType() && type.isSubtypeOf(RascalValueFactory.Tree));
    }

    public int getNamedVariables() {
        return namedVariables;
    }

    public int getIndexedVariables() {
        return indexedVariables;
    }
}
