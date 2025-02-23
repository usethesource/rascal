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
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime;

import org.rascalmpl.types.DefaultRascalTypeVisitor;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class Fingerprint {
	
	private static final int boolHashCode = "bool".hashCode();
	private static final int intHashCode = "num".hashCode();
	private static final int realHashCode = "num".hashCode();
	private static final int ratHashCode = "num".hashCode();
	private static final int numHashCode = "num".hashCode();
	private static final int strHashCode = "str".hashCode();
	private static final int locHashCode = "loc".hashCode();
	private static final int datetimeHashCode = "datetime".hashCode();
	
	private static final int listHashCode = "list".hashCode();
	private static final int mapHashCode = "map".hashCode();
	private static final int setHashCode = "set".hashCode();
	private static final int tupleHashCode = "tuple".hashCode();
	private static final int valueHashCode = "value".hashCode();
	
	public static int getFingerprint(final IValue v){
		int res = v.getType().accept(new DefaultRascalTypeVisitor<Integer,RuntimeException>(v.hashCode()) {
			
//			@Override
//			public Integer visitBool(final Type type) throws RuntimeException {
//				return boolHashCode;
//			}
//			
//			@Override
//			public Integer visitInteger(final Type type) throws RuntimeException {
//				return intHashCode;
//			}
//			
//			@Override
//			public Integer visitReal(final Type type) throws RuntimeException {
//				return realHashCode;
//			}
//			
//			@Override
//			public Integer visitRational(final Type type) throws RuntimeException {
//				return ratHashCode;
//			}
//			
//			@Override
//			public Integer visitNumber(final Type type) throws RuntimeException {
//				return numHashCode;
//			}
//			@Override
//			public Integer visitString(final Type type) throws RuntimeException {
//				return strHashCode;
//			}
//			
//			@Override
//			public Integer visitSourceLocation(final Type type) throws RuntimeException {
//				return locHashCode;
//			}
//			
//			@Override
//			public Integer visitDateTime(final Type type) throws RuntimeException {
//				return datetimeHashCode;
//			}
			
			@Override
			public Integer visitList(final Type type) throws RuntimeException {
				return listHashCode;
			}

			@Override
			public Integer visitMap(final Type type) throws RuntimeException {
				return mapHashCode;
			}

			@Override
			public Integer visitSet(final Type type) throws RuntimeException {
				return setHashCode;
			}

			@Override
			public Integer visitNode(final Type type) throws RuntimeException {
				return ((INode) v).getName().hashCode() << 2 + ((INode) v).arity();
			}

			@Override
			public Integer visitConstructor(final Type type) throws RuntimeException {
				IConstructor cons = (IConstructor) v;
				return cons.getName().hashCode() << 2 + cons.arity();
			}

			@Override
			public Integer visitAbstractData(final Type type) throws RuntimeException {
				return visitConstructor(type);
			}

			@Override
			public Integer visitTuple(final Type type) throws RuntimeException {
				return tupleHashCode << 2 + ((ITuple) v).arity();
			}

			@Override
			public Integer visitValue(final Type type) throws RuntimeException {
				return valueHashCode;
			}

			@Override
			public Integer visitReified(RascalType type)
					throws RuntimeException {
				// TODO: this might work; need to check
				return visitConstructor(type);
			}

			@Override
			public Integer visitNonTerminal(RascalType type) throws RuntimeException {
				return visitAbstractData(type.asAbstractDataType());
			}
		});
		return res;
	}
	
	public static int getConcreteFingerprint(final IValue v) {
		if(v instanceof ITree && ((ITree) v).isAppl()){
			return ((ITree) v).getProduction().hashCode(); 
		}
		return getFingerprint(v);
	}
}
