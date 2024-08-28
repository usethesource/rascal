/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

package org.rascalmpl.parser.gtd.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.rascalmpl.parser.gtd.IGTD;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;

/**
 * To offer backwards compatibility for generated parsers that do not yet have the "getFreeStackNodeId" method yet,
 * this class uses reflection to find that method and otherwise improvises by just using a ridiculously high starting number.
 */
public class StackNodeIdDispenser implements IdDispenser {
    private IGTD<IConstructor, ITree, ISourceLocation> parser;
    private Method dispenseMethod;
    private int nextNodeIdBackup = (Integer.MAX_VALUE/4)*3;

    public StackNodeIdDispenser(IGTD<IConstructor, ITree, ISourceLocation> parser) {
        try {
            dispenseMethod = parser.getClass().getMethod("getFreeStackNodeId");
        } catch (NoSuchMethodException e) {
            // Custom IGTB implementation without "getFreeStackNodeId" method. No biggy, we just use nextNodeIdBackup.
        }
    }
    
    @Override
    public int dispenseId() {
        if (dispenseMethod != null) {
            try {
                return (Integer)dispenseMethod.invoke(parser);
            }
            catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof UnsupportedOperationException) {
                    // We are dealing with a parser class that has no generated "getFreeStackNodeId" method (yet),
                    // for backwards compatibility we fall back on "nextNodeIdBackup".
                    dispenseMethod = null; // No reason to try again.
                } else {
                    throw new RuntimeException(e);
                }
            }
            catch (IllegalAccessException | IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        return nextNodeIdBackup++;
    }

}

