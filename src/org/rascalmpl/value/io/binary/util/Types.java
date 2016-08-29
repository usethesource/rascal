/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.value.io.binary.util;

import org.rascalmpl.value.io.binary.IValueKinds;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;

public class Types {

    private static final ITypeVisitor<Boolean, RuntimeException> typeToCompound = new ITypeVisitor<Boolean, RuntimeException>() {
        @Override
        public Boolean visitReal(Type type) throws RuntimeException {
            return IValueKinds.REAL_COMPOUND;
        }

        @Override
        public Boolean visitInteger(Type type) throws RuntimeException {
            return IValueKinds.INTEGER_COMPOUND;
        }

        @Override
        public Boolean visitRational(Type type) throws RuntimeException {
            return IValueKinds.RATIONAL_COMPOUND;
        }

        @Override
        public Boolean visitList(Type type) throws RuntimeException {
            return IValueKinds.LIST_COMPOUND;
        }

        @Override
        public Boolean visitMap(Type type) throws RuntimeException {
            return IValueKinds.MAP_COMPOUND;
        }

        @Override
        public Boolean visitNumber(Type type) throws RuntimeException {
            return IValueKinds.NUMBER_COMPOUND;
        }

        @Override
        public Boolean visitAlias(Type type) throws RuntimeException {
            return IValueKinds.ALIAS_COMPOUND;
        }

        @Override
        public Boolean visitSet(Type type) throws RuntimeException {
            return IValueKinds.SET_COMPOUND;
        }

        @Override
        public Boolean visitSourceLocation(Type type) throws RuntimeException {
            return IValueKinds.SOURCELOCATION_COMPOUND;
        }

        @Override
        public Boolean visitString(Type type) throws RuntimeException {
            return IValueKinds.STRING_COMPOUND;
        }

        @Override
        public Boolean visitNode(Type type) throws RuntimeException {
            return IValueKinds.NODE_COMPOUND_TYPE;
        }

        @Override
        public Boolean visitConstructor(Type type) throws RuntimeException {
            return IValueKinds.CONSTRUCTOR_COMPOUND;
        }

        @Override
        public Boolean visitAbstractData(Type type) throws RuntimeException {
            return IValueKinds.ADT_COMPOUND;
        }

        @Override
        public Boolean visitTuple(Type type) throws RuntimeException {
            return IValueKinds.TUPLE_COMPOUND;
        }

        @Override
        public Boolean visitValue(Type type) throws RuntimeException {
            return IValueKinds.VALUE_COMPOUND;
        }

        @Override
        public Boolean visitVoid(Type type) throws RuntimeException {
            return IValueKinds.VOID_COMPOUND;
        }

        @Override
        public Boolean visitBool(Type type) throws RuntimeException {
            return IValueKinds.BOOLEAN_COMPOUND;
        }

        @Override
        public Boolean visitParameter(Type type) throws RuntimeException {
            return IValueKinds.PARAMETER_COMPOUND;
        }

        @Override
        public Boolean visitExternal(Type type) throws RuntimeException {
            return IValueKinds.EXTERNAL_COMPOUND;
        }

        @Override
        public Boolean visitDateTime(Type type) throws RuntimeException {
            return IValueKinds.DATETIME_COMPOUND;
        }
    };

    public static boolean isCompound(Type tp) {
        return tp.accept(typeToCompound);
    }
}
