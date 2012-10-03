/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.Arrays;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.FunctionBody;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.ast.Visibility;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.JavaMethod;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.JavaMethodLinkError;
import org.rascalmpl.interpreter.staticErrors.MissingModifierError;
import org.rascalmpl.parser.ASTBuilder;

public abstract class FunctionDeclaration extends
		org.rascalmpl.ast.FunctionDeclaration {

	static public class Abstract extends
			org.rascalmpl.ast.FunctionDeclaration.Abstract {

		public Abstract(IConstructor __param1, Tags __param2, Visibility __param3,
				Signature __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			boolean varArgs = this.getSignature().getParameters().isVarArgs();

			if (!hasJavaModifier(this)) {
				throw new MissingModifierError("java", this);
			}

			AbstractFunction lambda = new JavaMethod(__eval, this, varArgs,
					__eval.getCurrentEnvt(), __eval.__getJavaBridge());
			String name = org.rascalmpl.interpreter.utils.Names.name(this
					.getSignature().getName());
			__eval.getCurrentEnvt().storeFunction(name, lambda);
			__eval.getCurrentEnvt().markNameFinal(lambda.getName());
			__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

			lambda.setPublic(this.getVisibility().isPublic());
			return lambda;

		}

	}

	static public class Default extends
			org.rascalmpl.ast.FunctionDeclaration.Default {

		public Default(IConstructor __param1, Tags __param2, Visibility __param3,
				Signature __param4, FunctionBody __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			AbstractFunction lambda;
			boolean varArgs = this.getSignature().getParameters().isVarArgs();

			if (hasJavaModifier(this)) {
				throw new JavaMethodLinkError(
						"may not use java modifier with a function that has a body",
						null, this, null);
			}

			if (!this.getBody().isDefault()) {
				throw new MissingModifierError("java", this);
			}

			lambda = new RascalFunction(__eval, this, varArgs, __eval
					.getCurrentEnvt(), __eval.__getAccumulators());

			__eval.getCurrentEnvt().storeFunction(lambda.getName(), lambda);
			__eval.getCurrentEnvt().markNameFinal(lambda.getName());
			__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

			lambda.setPublic(this.getVisibility().isPublic());
			return lambda;

		}

		@Override
		public Type typeOf(Environment __eval) {
			return this.getSignature().typeOf(__eval);
		}
	}

	static public class Expression extends
			org.rascalmpl.ast.FunctionDeclaration.Expression {

		public Expression(IConstructor node, Tags tags, Visibility visibility,
				Signature signature, org.rascalmpl.ast.Expression expression) {
			super(node, tags, visibility, signature, expression);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			AbstractFunction lambda;
			boolean varArgs = this.getSignature().getParameters().isVarArgs();

			if (hasJavaModifier(this)) {
				throw new JavaMethodLinkError(
						"may not use java modifier with a function that has a body",
						null, this, null);
			}

			lambda = new RascalFunction(__eval, this, varArgs, __eval
					.getCurrentEnvt(), __eval.__getAccumulators());
			
			lambda.setPublic(this.getVisibility().isPublic());
			__eval.getCurrentEnvt().markNameFinal(lambda.getName());
			__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());
			
			__eval.getCurrentEnvt().storeFunction(lambda.getName(), lambda);
			
			return lambda;
		}

	}
	
	static public class Conditional extends
	org.rascalmpl.ast.FunctionDeclaration.Conditional {

		public Conditional(IConstructor node, Tags tags, Visibility visibility,
				Signature signature, org.rascalmpl.ast.Expression expression, java.util.List<org.rascalmpl.ast.Expression> conditions) {
			super(node, tags, visibility, signature, expression, conditions);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
					
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);	
			
			AbstractFunction lambda;
			boolean varArgs = this.getSignature().getParameters().isVarArgs();

			if (hasJavaModifier(this)) {
				throw new JavaMethodLinkError(
						"may not use java modifier with a function that has a body",
						null, this, null);
			}

			ISourceLocation src = this.getLocation();
			AbstractAST ret = ASTBuilder.makeStat("Return", src, ASTBuilder.makeStat("Expression", src, getExpression()));
			AbstractAST fail = ASTBuilder.makeStat("Fail", src, ASTBuilder.make("Target", "Labeled", src, getSignature().getName()));
			AbstractAST ite = ASTBuilder.makeStat("IfThenElse", src, ASTBuilder.make("Label", "Empty", src), getConditions(), ret, fail);
			List<AbstractAST> sl = Arrays.<AbstractAST>asList(ite);
			AbstractAST body = ASTBuilder.make("FunctionBody", "Default", src, sl);
			FunctionDeclaration.Default func = ASTBuilder.make("FunctionDeclaration", "Default", src, getTags(), getVisibility(), getSignature(), body);
			
			lambda = new RascalFunction(__eval, func, varArgs, __eval
					.getCurrentEnvt(), __eval.__getAccumulators());

			__eval.getCurrentEnvt().storeFunction(lambda.getName(), lambda);
			__eval.getCurrentEnvt().markNameFinal(lambda.getName());
			__eval.getCurrentEnvt().markNameOverloadable(lambda.getName());

			lambda.setPublic(this.getVisibility().isPublic());
			return lambda;
		}

}

	public static boolean hasJavaModifier(
			org.rascalmpl.ast.FunctionDeclaration func) {
		List<FunctionModifier> mods = func.getSignature().getModifiers()
				.getModifiers();
		for (FunctionModifier m : mods) {
			if (m.isJava()) {
				return true;
			}
		}

		return false;
	}
	
	public static boolean hasTestModifier(
			org.rascalmpl.ast.FunctionDeclaration func) {
		List<FunctionModifier> mods = func.getSignature().getModifiers()
				.getModifiers();
		for (FunctionModifier m : mods) {
			if (m.isTest()) {
				return true;
			}
		}

		return false;
	}

	public FunctionDeclaration(IConstructor __param1) {
		super(__param1);
	}
}
