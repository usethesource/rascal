/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.ast.BooleanLiteral;
import org.rascalmpl.ast.DateTimeLiteral;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.LocationLiteral;
import org.rascalmpl.ast.RationalLiteral;
import org.rascalmpl.ast.RealLiteral;
import org.rascalmpl.ast.RegExpLiteral;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringLiteral;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.StringTemplateConverter;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class Literal extends org.rascalmpl.ast.Literal {

	static public class Boolean extends org.rascalmpl.ast.Literal.Boolean {

		public Boolean(ISourceLocation __param1, IConstructor tree, BooleanLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new LiteralPattern(eval, this, interpret(eval.getEvaluator()).getValue());
		}
		
		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			java.lang.String str = ((BooleanLiteral.Lexical) this.getBooleanLiteral()).getString();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().boolType(),
					__eval.__getVf().bool(str.equals("true")), __eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.boolType();
		}

	}

	static public class DateTime extends org.rascalmpl.ast.Literal.DateTime {

		public DateTime(ISourceLocation __param1, IConstructor tree, DateTimeLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			return this.getDateTimeLiteral().interpret(__eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.dateTimeType();
		}

	}

	static public class Integer extends org.rascalmpl.ast.Literal.Integer {

		public Integer(ISourceLocation __param1, IConstructor tree, IntegerLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new LiteralPattern(eval, this, interpret(eval.getEvaluator()).getValue());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return this.getIntegerLiteral().interpret(__eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.integerType();
		}

	}

	static public class Location extends org.rascalmpl.ast.Literal.Location {

		public Location(ISourceLocation __param1, IConstructor tree, LocationLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			return this.getLocationLiteral().interpret(__eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.sourceLocationType();
		}

	}

	static public class Real extends org.rascalmpl.ast.Literal.Real {

		public Real(ISourceLocation __param1, IConstructor tree, RealLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new LiteralPattern(eval, this, interpret(eval.getEvaluator()).getValue());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			java.lang.String str = ((RealLiteral.Lexical) this.getRealLiteral()).getString();
			if (str.toLowerCase().endsWith("d")) {
				str = str.substring(0, str.length() - 1);
			}
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().realType(),
					__eval.__getVf().real(str), __eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.realType();
		}
	}
	
	static public class Rational extends org.rascalmpl.ast.Literal.Rational {

		public Rational(ISourceLocation __param1, IConstructor tree, RationalLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new LiteralPattern(eval, this, interpret(eval.getEvaluator()).getValue());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			java.lang.String str = ((RationalLiteral.Lexical) this.getRationalLiteral()).getString();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().rationalType(),
					__eval.__getVf().rational(str), __eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.rationalType();
		}
	}

	static public class RegExp extends org.rascalmpl.ast.Literal.RegExp {

		public RegExp(ISourceLocation __param1, IConstructor tree, RegExpLiteral __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval, boolean bindTypeParameters) {

			return this.getRegExpLiteral().buildMatcher(__eval, bindTypeParameters);

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			throw new SyntaxError(
					"regular expression. They are only allowed in a pattern (left of <- and := or in a case statement).",
					this.getLocation());

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.stringType();
		}

	}

	static public class String extends org.rascalmpl.ast.Literal.String {

		private final Statement stat;

		public String(ISourceLocation __param1, IConstructor tree, StringLiteral __param2) {
			super(__param1, tree, __param2);
			this.stat = new StringTemplateConverter().convert(this.getStringLiteral());
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new LiteralPattern(eval, this, interpret(eval.getEvaluator()).getValue());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> value = stat.interpret(__eval);
			if (!value.getStaticType().isList()) {
				throw new ImplementationError(
						"template eval returns non-list");
			}
			IList list = (IList) value.getValue();
			if (list.length() == 0) {
				System.out.println("bla");
			}

			if (!list.get(0).getType().isString()) {
				throw new ImplementationError(
						"template eval returns list with non-string");
			}
			
			return ResultFactory.makeResult(TF.stringType(), list.get(0), __eval);
			
//			// todo: concat not needed anymore, because of appendString in Accumulator.
//			// list is always non-empty
//			Result<IValue> s = ResultFactory.makeResult(TF.stringType(),
//					list.get(0), __eval);
//
//			// lazy concat! 
//			for (int i = 1; i < list.length(); i++) {
//				IString str = (IString) list.get(i);
//				s = s.add(ResultFactory.makeResult(TF.stringType(), str, __eval));
//			}

//			return s;
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.stringType();
		}

	}

	public Literal(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
