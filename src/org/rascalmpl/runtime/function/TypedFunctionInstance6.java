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
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.function;

import java.util.Map;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class TypedFunctionInstance6<R,A,B,C,D,E,F> extends TypedFunctionInstance {
	
	private final TypedFunction6<R,A,B,C,D,E,F> function;

	public TypedFunctionInstance6(TypedFunction6<R,A,B,C,D,E,F> function, Type ftype){
		super(ftype);
		this.function = function;
		assert ftype.isFunction() && ftype.getArity() == 6;
	}
	
	public R typedCall(A a, B b, C c, D d, E e, F f) {
		return function.typedCall(a, b, c, d, e, f);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a, IValue b, IValue c, IValue d, IValue e, IValue f) {
		return function.typedCall((A)a, (B)b, (C)c, (D)d, (E)e, (F) f);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
        return (T)function.typedCall((A)parameters[0], (B)parameters[1], (C)parameters[2], (D)parameters[3], (E)parameters[4], (F)parameters[5]);
    }
}
