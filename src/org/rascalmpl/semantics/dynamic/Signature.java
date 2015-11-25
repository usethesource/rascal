/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TypeDeclarationEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public abstract class Signature extends org.rascalmpl.ast.Signature {

	private static FunctionModifiers addMod(FunctionModifiers base, FunctionModifier addition) {
		List<FunctionModifier> l = new ArrayList<>(base.getModifiers().size() + 1);
		l.addAll(base.getModifiers());
		l.add(addition);
		
		return ASTBuilder.make("FunctionModifiers", "Modifierlist", base.getLocation(), l);
	}
	
	static public class Test extends NoThrows {
		public Test(ISourceLocation src, IConstructor node, FunctionModifiers mods, Name name, Parameters parameters) {
			super(src, 
					node, 
					addMod(mods, ASTBuilder.make("FunctionModifier", "Test", src)),
					ASTBuilder.make("Type", "Basic", src,
							ASTBuilder.make("BasicType",  "Bool", src)
							), 
					name, 
					parameters);
		}
	}
	
	static public class Format extends NoThrows {
		public Format(ISourceLocation src, IConstructor node, FunctionModifiers mods, Parameters parameters) {
			super(src, 
					node, 
					mods, 
					ASTBuilder.make("Type", "Basic", src,
							ASTBuilder.make("BasicType",  "String", src)
							), 
					Names.toName("format", src), 
					parameters);
		}
	}
		
	static public class NoThrows extends org.rascalmpl.ast.Signature.NoThrows {

		public NoThrows(ISourceLocation __param1, IConstructor tree,FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				 Name __param4, Parameters __param5) {
			super(__param1, tree, __param3, __param2, __param4, __param5);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			RascalTypeFactory RTF = org.rascalmpl.interpreter.types.RascalTypeFactory
					.getInstance();
			Parameters parameters = getParameters();
			Type kwParams = TF.voidType();

			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				List<KeywordFormal> kwd = parameters.getKeywordFormals().getKeywordFormalList();
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kwd, eval);
			}

			return RTF.functionType(getType().typeOf(env, instantiateTypeParameters, eval), parameters.typeOf(env, instantiateTypeParameters, eval), kwParams);
		}
	}

	static public class WithThrows extends org.rascalmpl.ast.Signature.WithThrows {
		public WithThrows(ISourceLocation __param1, IConstructor tree, FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				Name __param4, Parameters __param5,
				List<org.rascalmpl.ast.Type> __param6) {
			super(__param1, tree, __param3, __param2, __param4, __param5, __param6);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			RascalTypeFactory RTF = RascalTypeFactory.getInstance();

			Type kwParams = TF.voidType();

			Parameters parameters = getParameters();

			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				List<KeywordFormal> kwd = parameters.getKeywordFormals().getKeywordFormalList();
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kwd, eval);
			}

			return RTF.functionType(getType().typeOf(env, instantiateTypeParameters, eval), getParameters()
					.typeOf(env, instantiateTypeParameters, eval), kwParams);
		}

	}

	public Signature(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
