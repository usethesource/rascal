package org.rascalmpl.semantics.dynamic;

import java.lang.StringBuilder;
import java.util.List;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.BooleanLiteral;
import org.rascalmpl.ast.DateTimeLiteral;
import org.rascalmpl.ast.IntegerLiteral;
import org.rascalmpl.ast.LocationLiteral;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.RealLiteral;
import org.rascalmpl.ast.RegExpLiteral;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringConstant.Lexical;
import org.rascalmpl.ast.StringLiteral;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.values.OriginValueFactory;

public abstract class Literal extends org.rascalmpl.ast.Literal {

	public Literal(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Literal.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Literal> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Real extends org.rascalmpl.ast.Literal.Real {

		public Real(INode __param1, RealLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			java.lang.String str = this.getRealLiteral().toString();
			if (str.toLowerCase().endsWith("d")) {
				str = str.substring(0, str.length() - 1);
			}
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().realType(), __eval.__getVf().real(str), __eval);

		}

	}

	static public class Integer extends org.rascalmpl.ast.Literal.Integer {

		public Integer(INode __param1, IntegerLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getIntegerLiteral().interpret(__eval);

		}

	}

	static public class RegExp extends org.rascalmpl.ast.Literal.RegExp {

		public RegExp(INode __param1, RegExpLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return this.getRegExpLiteral().buildMatcher(__eval);

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new SyntaxError("regular expression. They are only allowed in a pattern (left of <- and := or in a case statement).", this.getLocation());

		}

	}

	static public class Boolean extends org.rascalmpl.ast.Literal.Boolean {

		public Boolean(INode __param1, BooleanLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			java.lang.String str = this.getBooleanLiteral().toString();
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), __eval.__getVf().bool(str.equals("true")), __eval);

		}

	}

	static public class DateTime extends org.rascalmpl.ast.Literal.DateTime {

		public DateTime(INode __param1, DateTimeLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getDateTimeLiteral().interpret(__eval);

		}

	}

	static public class Location extends org.rascalmpl.ast.Literal.Location {

		public Location(INode __param1, LocationLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getLocationLiteral().interpret(__eval);

		}

	}

	static public class String extends org.rascalmpl.ast.Literal.String {

		public String(INode __param1, StringLiteral __param2) {
			super(__param1, __param2);
		}


		@Override
		public IMatchingResult buildMatcher(PatternEvaluator __eval) {

			return new LiteralPattern(__eval.__getCtx(), this, this.interpret(__eval.__getCtx().getEvaluator()).getValue());

		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			StringLiteral lit = this.getStringLiteral();
			IValueFactory vf = __eval.__getVf();
			
			// To prevent infinite recursion detect non-interpolated strings
			// first. TODO: design flaw?
			if (lit.isNonInterpolated()) {
				java.lang.String str = org.rascalmpl.interpreter.utils.StringUtils.unescape(((Lexical) lit.getConstant()).getString());

				
				IValue v;
				if (vf instanceof OriginValueFactory) {
					OriginValueFactory of = (OriginValueFactory)vf;

					ISourceLocation loc = ((Lexical)lit.getConstant()).getLocation();

					// Remove quotes from location
					loc = of.sourceLocation(loc.getURI(), 
							loc.getOffset() + 1, 
							loc.getLength() - 2,
							loc.getBeginLine(), loc.getEndLine(), 
							loc.getBeginColumn() + 1, 
							loc.getEndColumn() - 1);
					v = of.literal(loc, str);
				}
				else {
					v = vf.string(str);
				}
				return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), v, __eval);
					
			} else {
				Statement stat = org.rascalmpl.interpreter.StringTemplateConverter.convert(lit);
				Result<IValue> value = stat.interpret(__eval);
				if (!value.getType().isListType()) {
					throw new ImplementationError("template eval returns non-list");
				}
				IList list = (IList) value.getValue();
				
				// list is always non-empty
				IString s = (IString)list.get(0);
				for (int i = 1; i < list.length(); i++) {
					IValue x = list.get(i);
					s = s.concat((IString)list.get(i));
				}
				return org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().stringType(), s, __eval);
			}


		}

	}
}
