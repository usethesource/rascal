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

import java.io.IOException;
import java.io.Writer;

import org.rascalmpl.interpreter.utils.LimitedResultWriter;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

public class RascalVariableUtils {

    private static final int MAX_SIZE_STRING_NAME = 128;

    // copied from Rascal Eclipse debug.core.model.RascalValue
    public static String getDisplayString(IValue value) {
        if(value == null) {
            return "null";
        }
        Writer w = new LimitedResultWriter(MAX_SIZE_STRING_NAME);
        try {
            new StandardTextWriter(true, 2).write(value, w);
            return w.toString();
        } catch (LimitedResultWriter.IOLimitReachedException e) {
            return w.toString();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "error during serialization...";
        }
    }
}
