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

public class TypedFunctionInstance1<R extends IValue,A> extends TypedFunctionInstance {
	
	private final TypedFunction1<R,A> function;

	public TypedFunctionInstance1(TypedFunction1<R,A> function, Type ftype){
		super(ftype);
		this.function = function;
		assert ftype.isFunction() && ftype.getArity() == 1;
	}
	
	public R typedCall(A a) {
		return function.typedCall(a);
	}
	
	@SuppressWarnings("unchecked")
	public R call(IValue a) {
		return function.typedCall((A)a);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    return (T)function.typedCall((A)parameters[0]);
	}
}