package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.BooleanLiteral;
import org.rascalmpl.ast.DateTimeLiteral;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.LocationLiteral;
import org.rascalmpl.ast.RealLiteral;
import org.rascalmpl.ast.RegExpLiteral;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringLiteral;
import org.rascalmpl.ast.StringConstant.Lexical;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.StringTemplateConverter;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.values.OriginValueFactory;

public abstract class Literal extends org.rascalmpl.ast.Literal {

	static public class Boolean extends org.rascalmpl.ast.Literal.Boolean {

		public Boolean(ISourceLocation __param1, BooleanLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(
					__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			java.lang.String str = this.getBooleanLiteral().toString();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().boolType(),
					__eval.__getVf().bool(str.equals("true")), __eval);

		}

		@Override
		public Type typeOf(Environment env) {
			return TF.boolType();
		}

	}

	static public class DateTime extends org.rascalmpl.ast.Literal.DateTime {

		public DateTime(ISourceLocation __param1, DateTimeLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getDateTimeLiteral().interpret(__eval);

		}

		@Override
		public Type typeOf(Environment env) {
			return TF.dateTimeType();
		}

	}

	static public class Integer extends org.rascalmpl.ast.Literal.Integer {

		public Integer(ISourceLocation __param1, IntegerLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {
			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(
					__eval.__getCtx().getEvaluator()).getValue());
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return this.getIntegerLiteral().interpret(__eval);
		}

		@Override
		public Type typeOf(Environment env) {
			return TF.integerType();
		}

	}

	static public class Location extends org.rascalmpl.ast.Literal.Location {

		public Location(ISourceLocation __param1, LocationLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getLocationLiteral().interpret(__eval);

		}

		@Override
		public Type typeOf(Environment env) {
			return TF.sourceLocationType();
		}

	}

	static public class Real extends org.rascalmpl.ast.Literal.Real {

		public Real(ISourceLocation __param1, RealLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(
					__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			java.lang.String str = this.getRealLiteral().toString();
			if (str.toLowerCase().endsWith("d")) {
				str = str.substring(0, str.length() - 1);
			}
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(
					org.rascalmpl.interpreter.Evaluator.__getTf().realType(),
					__eval.__getVf().real(str), __eval);

		}

		@Override
		public Type typeOf(Environment env) {
			return TF.realType();
		}
	}

	static public class RegExp extends org.rascalmpl.ast.Literal.RegExp {

		public RegExp(ISourceLocation __param1, RegExpLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return this.getRegExpLiteral().buildMatcher(__eval);

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new SyntaxError(
					"regular expression. They are only allowed in a pattern (left of <- and := or in a case statement).",
					this.getLocation());

		}

		@Override
		public Type typeOf(Environment env) {
			return TF.stringType();
		}

	}

	static public class String extends org.rascalmpl.ast.Literal.String {

		public String(ISourceLocation __param1, StringLiteral __param2) {
			super(__param1, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(
					__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			StringLiteral lit = this.getStringLiteral();
			IValueFactory vf = __eval.__getVf();

			// To prevent infinite recursion detect non-interpolated strings
			// first. TODO: design flaw?
			if (lit.isNonInterpolated()) {
				java.lang.String str = org.rascalmpl.interpreter.utils.StringUtils
						.unescape(((Lexical) lit.getConstant()).getString());

				IValue v;
				if (vf instanceof OriginValueFactory) {
					OriginValueFactory of = (OriginValueFactory) vf;

					ISourceLocation loc = ((Lexical) lit.getConstant())
							.getLocation();

					// Remove quotes from location
					loc = of.sourceLocation(loc.getURI(), loc.getOffset() + 1,
							loc.getLength() - 2, loc.getBeginLine(), loc
									.getEndLine(), loc.getBeginColumn() + 1,
							loc.getEndColumn() - 1);
					v = of.literal(loc, str);
				} else {
					v = vf.string(str);
				}
				return org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(org.rascalmpl.interpreter.Evaluator
								.__getTf().stringType(), v, __eval);

			} else {
				Statement stat = new StringTemplateConverter(__eval
						.getBuilder()).convert(lit);
				Result<IValue> value = stat.interpret(__eval);
				if (!value.getType().isListType()) {
					throw new ImplementationError(
							"template eval returns non-list");
				}
				IList list = (IList) value.getValue();

				// list is always non-empty
				Result<IValue> s = ResultFactory.makeResult(TF.stringType(),
						list.get(0), __eval);

				// lazy concat!
				for (int i = 1; i < list.length(); i++) {
					s = s.add(ResultFactory.makeResult(TF.stringType(), list
							.get(i), __eval));
				}

				return s;
			}

		}

		@Override
		public Type typeOf(Environment env) {
			return TF.stringType();
		}

	}

	public Literal(ISourceLocation __param1) {
		super(__param1);
	}
}
