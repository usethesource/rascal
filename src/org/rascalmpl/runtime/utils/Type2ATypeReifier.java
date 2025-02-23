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
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.ATypeFactory;
import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.InternalCompilerError;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.DefaultTypeVisitor;
import io.usethesource.vallang.type.Type;

/*** NOT USED ANYMORE ***/
public class Type2ATypeReifier extends ATypeFactory {
	
	protected IString empty = $VF.string("");
	 
	public IConstructor reify2atype(final Type t, IString label){
		return t.accept(new DefaultTypeVisitor<IConstructor, RuntimeException>(null) {
			@Override
			public IConstructor visitVoid(Type type) throws RuntimeException {
				return label.length() == 0 ? $avoid() : $avoid(label);
			}
			@Override
			public IConstructor visitBool(Type type) throws RuntimeException {
				return label.length() == 0 ? $abool() : $abool(label);
			}
			@Override
			public IConstructor visitInteger(Type type) throws RuntimeException {
				return label.length() == 0 ? $aint() : $aint(label);
			}
			@Override
			public IConstructor visitReal(Type type) throws RuntimeException {
				return label.length() == 0 ? $areal() : $areal(label);
			}
			
			@Override
			public IConstructor visitRational(Type type) throws RuntimeException {
				return label.length() == 0 ? $arat() : $arat(label);
			}
			@Override
			public IConstructor visitNumber(Type type) throws RuntimeException {
				return label.length() == 0 ? $anum() : $anum(label);
			}
			@Override
			public IConstructor visitString(Type type) throws RuntimeException {
				return label.length() == 0 ? $astr() : $astr(label);
			}
			@Override
			public IConstructor visitSourceLocation(Type type) throws RuntimeException {
				return label.length() == 0 ? $aloc() : $aloc(label);
			}
			@Override
			public IConstructor visitDateTime(Type type) throws RuntimeException {
				return label.length() == 0 ? $adatetime() : $adatetime(label);
			}
			@Override
			public IConstructor visitList(Type type) throws RuntimeException {
				IConstructor elmType = reify2atype(type.getElementType(), empty);
				return label.length() == 0 ? $alist(elmType) : $alist(elmType, label);
			}
			
			// bag
			
			@Override
			public IConstructor visitSet(Type type) throws RuntimeException {
				IConstructor elmType = reify2atype(type.getElementType(), empty);
				return label.length() == 0 ? $aset(elmType) : $aset(elmType, label);
			}
			
			@Override
			public IConstructor visitTuple(Type type) throws RuntimeException {
				Type fieldTypes = type.getFieldTypes();
				String[] fieldNames = type.hasFieldNames() ? type.getFieldNames() : null;
				int arity = type.getArity();
				IConstructor fieldATypes[] = new IConstructor[arity];
				for(int i = 0; i <arity; i++) {
					fieldATypes[i] = reify2atype(fieldTypes.getFieldType(i), fieldNames == null ? empty : $VF.string(fieldNames[i]));
				}
				return label.length() == 0 ? $atuple(fieldATypes) : $atuple(fieldATypes, label);
			}
			
			@Override
			public IConstructor visitMap(Type type) throws RuntimeException {
				boolean hasNames = type.hasFieldNames();
				String keyLabel = hasNames ? type.getKeyLabel() : "";
				IConstructor keyType = reify2atype(type.getKeyType(), keyLabel.isEmpty() ? empty : $VF.string(keyLabel));
				
				String valLabel = hasNames ? type.getValueLabel() : "";
				IConstructor valType = reify2atype(type.getValueType(), valLabel.isEmpty() ? empty : $VF.string(valLabel));
				return label.length() == 0 ? $amap(keyType, valType) : $amap(keyType, valType, label);
			}
			
			@Override
			public IConstructor visitParameter(Type type) throws RuntimeException {
				String pname = type.getName();
				IConstructor boundType = reify2atype(type.getBound(), empty);
				return $aparameter($VF.string(pname), boundType);
			}
			
			@Override
			public IConstructor visitNode(Type type) throws RuntimeException {
				return label.length() == 0 ? $anode() : $anode(label); 
			}
			
			@Override
			public IConstructor visitAbstractData(Type type) throws RuntimeException {
				String adtName = t.getName();
				Type parameters = t.getTypeParameters();
				int arity = parameters.getArity();
				IListWriter w = $VF.listWriter();
				for(int i = 0; i <arity; i++) {
					w.append(reify2atype(parameters.getFieldType(i), empty));
				}
				return label.length() == 0 ? $aadt($VF.string(adtName), w.done(), dataSyntax)
						                   : $aadt($VF.string(adtName), w.done(), dataSyntax, label);
			}
			
			@Override
			public IConstructor visitConstructor(Type type) throws RuntimeException {
				String consName = t.getName();
				Type adt = t.getAbstractDataType();
				Type fields = type.getFieldTypes();
				int arity = fields.getArity();
				IListWriter w = $VF.listWriter();
				for(int i = 0; i <arity; i++) {
					w.append(reify2atype(fields.getFieldType(i), empty));
				}
				return $acons(reify2atype(adt, empty), w.done(), $VF.listWriter().done(), $VF.string(consName));
			}
			
			@Override
			public IConstructor visitValue(Type type) throws RuntimeException {
				return $avalue();
			}
			
			@Override
			public IConstructor visitAlias(Type type) throws RuntimeException {
				throw new InternalCompilerError("Alias not implemented: " + type);
			}
			
			@Override
			public IConstructor visitExternal(Type type) throws RuntimeException {
				throw new InternalCompilerError("External not implemented: " + type);
			}
		});
	}
	
	@SuppressWarnings("deprecation")
    public static void main(String [] args) {
		Type2ATypeReifier reifier = new Type2ATypeReifier();
		IValue one = reifier.$VF.integer(1);
		IValue two = reifier.$VF.integer(2);
		System.err.println(reifier.reify2atype(one.getType(), reifier.empty));
		System.err.println(reifier.reify2atype(one.getType(), reifier.$VF.string("intLabel")));
		
		System.err.println(reifier.reify2atype(reifier.$VF.set(one).getType(), reifier.empty));
		System.err.println(reifier.reify2atype(reifier.$VF.set(one).getType(), reifier.$VF.string("set Label")));
		
		System.err.println(reifier.reify2atype(reifier.$VF.tuple(one,two).getType(), reifier.empty));
		
		System.err.println(reifier.reify2atype(reifier.$TF.tupleType(reifier.$TF.integerType(), "abc"), reifier.empty));
		
		System.err.println(reifier.reify2atype(reifier.$VF.list(reifier.$VF.tuple(one,two)).getType(), reifier.empty));
		
		Type D = reifier.$TF.abstractDataType(reifier.$TS, "D");
		Type D_d = reifier.$TF.constructor(reifier.$TS, D, "d");
		
		System.err.println(reifier.reify2atype(D, reifier.empty));
		System.err.println(reifier.reify2atype(D_d, reifier.empty));
		
	}
	
	
}
