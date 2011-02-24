package org.rascalmpl.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.DataTarget;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.MidStringChars.Lexical;
import org.rascalmpl.ast.StringLiteral.NonInterpolated;
import org.rascalmpl.ast.StringMiddle.Interpolated;
import org.rascalmpl.ast.StringMiddle.Mid;
import org.rascalmpl.ast.StringMiddle.Template;
import org.rascalmpl.ast.StringTail.Post;
import org.rascalmpl.ast.StringTemplate.DoWhile;
import org.rascalmpl.ast.StringTemplate.For;
import org.rascalmpl.ast.StringTemplate.IfThen;
import org.rascalmpl.ast.StringTemplate.IfThenElse;
import org.rascalmpl.ast.StringTemplate.While;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.AppendWithoutLoop;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.OriginValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
  
public class StringTemplateConverter {
	private static int labelCounter = 0;
	private final ASTBuilder factory;
	
	public StringTemplateConverter(ASTBuilder builder) {
		this.factory = builder;
	}
	
	private Statement surroundWithSingleIterForLoop(ISourceLocation src, Name label, Statement body) {
		Name dummy = factory.make("Name","Lexical",src, "_");
		Expression var = factory.make("Expression","QualifiedName",src, factory.make("QualifiedName", src, Arrays.asList(dummy)));
		Expression truth = factory.make("Expression","Literal",src, factory.make("Literal","Boolean",src, factory.make("BooleanLiteral","Lexical",src, "true")));
		Expression list = factory.make("Expression","List", src, Arrays.asList(truth));
		Expression enumerator = factory.make("Expression","Enumerator",src, var, list);
		Statement stat = factory.make("Statement","For",src, factory.make("Label","Default", src, label), Arrays.asList(enumerator), body);
		return stat;
	}


	public Statement convert(org.rascalmpl.ast.StringLiteral str) {
		final Name label= factory.make("Name","Lexical", str.getLocation(), "#" + labelCounter);
		labelCounter++;
		return surroundWithSingleIterForLoop(str.getLocation(), label, str.accept(new Visitor(label, factory)));
	}
	
	private static class Visitor extends NullASTVisitor<Statement> {
		private final Name label;
		private final ASTBuilder factory;

		public Visitor(Name label, ASTBuilder factory) {
			this.label = label;
			this.factory = factory;
		}

		private Statement makeBlock(ISourceLocation src, Statement ...stats) {
			return makeBlock(src, Arrays.asList(stats));
		}
		
		private Statement makeBlock(ISourceLocation src, List<Statement> stats) {
			return factory.make("Statement","NonEmptyBlock",src, factory.make("Label", "Empty", src),
					stats);
		}

		
		private class MyAppend extends org.rascalmpl.semantics.dynamic.Statement.Append {

			public MyAppend(ISourceLocation __param1, DataTarget __param2,
					Statement __param3) {
				super(__param1, __param2, __param3);
			}
			
			
			// Ugh, this is the same as normal Statement.Append
			// but wraps already converts to origined strings
			@Override
			public Result<IValue> interpret(Evaluator __eval) {
				Accumulator target = null;
				if (__eval.__getAccumulators().empty()) {
					throw new AppendWithoutLoop(this);
				}
				if (!this.getDataTarget().isEmpty()) {
					String label = org.rascalmpl.interpreter.utils.Names.name(this.getDataTarget().getLabel());
					for (Accumulator accu : __eval.__getAccumulators()) {
						if (accu.hasLabel(label)) {
							target = accu;
							break;
						}
					}
					if (target == null) {
						throw new AppendWithoutLoop(this); // TODO: better error
															// message
					}
				} else {
					target = __eval.__getAccumulators().peek();
				}
				Result<IValue> result = this.getStatement().interpret(__eval);
				IValueFactory vf = ValueFactoryFactory.getValueFactory();
				IValue v = result.getValue();
				if (!(v instanceof IString)) {
					if (vf instanceof OriginValueFactory) {
						v = ((OriginValueFactory)vf).expression(getLocation(), result.getValue().toString());
					}
					else {
						// Ensure that values that are trees are yielding the appropriate string value
						StringBuilder sb = new StringBuilder(500);
						__eval.appendToString(v, sb);
						v = vf.string(sb.toString());
					}
				}
				result = ResultFactory.makeResult(v.getType(), v, result.getEvaluatorContext());
				target.append(result);
				return result;

			}

			
		}
		
		private Statement makeAppend(Expression exp) {
			return new MyAppend(exp.getLocation(), factory.<DataTarget>make("DataTarget","Labeled", null, label),
					factory.<Statement>make("Statement","Expression", exp.getLocation(), exp)); 
		}
		
		private  Statement combinePreBodyPost(ISourceLocation src, List<Statement> pre, Statement body, List<Statement> post) {
			List<Statement> stats = new ArrayList<Statement>();
			stats.addAll(pre);
			stats.add(body);
			stats.addAll(post);
			return makeBlock(src, stats);
		}
		
		
		private Expression makeLit(ISourceLocation src, String str) {
			// Note: we don't unescape here this happens
			// in the main evaluator; also, we pretend 
			// "...< etc. to be "..." stringliterals...
			return factory.makeExp("Literal",src, 
					factory.make("Literal","String", src, 
							factory.make("StringLiteral", "NonInterpolated", src, 
									factory.make("StringConstant","Lexical", src, str))));
		}
		
		
		@Override
		public Statement visitStringLiteralInterpolated(
				org.rascalmpl.ast.StringLiteral.Interpolated x) {
			Statement pre = x.getPre().accept(this);
			Statement exp = makeAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), pre, exp, tail);
		}
		
		@Override
		public Statement visitStringLiteralNonInterpolated(NonInterpolated x) {
			return makeAppend(makeLit(x.getLocation(), ((StringConstant.Lexical)x.getConstant()).getString()));
		}
		
		@Override
		public Statement visitStringLiteralTemplate(
				org.rascalmpl.ast.StringLiteral.Template x) {
			Statement pre = x.getPre().accept(this);
			Statement template = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), pre, template, tail);
		}
		
	
		@Override
		public Statement visitStringTemplateDoWhile(DoWhile x) {
			Statement body = x.getBody().accept(this);
			return factory.makeStat("DoWhile", x.getLocation(), factory.make("Label","Empty", x.getLocation()), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()) , x.getCondition());
		}


		@Override
		public Statement visitStringTemplateFor(For x) {
			Statement body = x.getBody().accept(this);
			return factory.makeStat("For", x.getLocation(), factory.make("Label","Empty", x.getLocation()), x.getGenerators(), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringTemplateIfThen(IfThen x) {
			Statement body = x.getBody().accept(this);
			return factory.makeStat("IfThen", x.getLocation(), factory.make("Label", "Empty", x.getLocation()), x.getConditions(), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()), factory.make("NoElseMayFollow", x.getLocation()));
		}

		@Override
		public Statement visitStringTemplateIfThenElse(IfThenElse x) {
			Statement t = x.getThenString().accept(this);
			Statement e = x.getElseString().accept(this);
			return factory.makeStat("IfThenElse", x.getLocation(), factory.make("Label","Empty",x.getLocation()), 
					x.getConditions(), 
						combinePreBodyPost(x.getLocation(), x.getPreStatsThen(), t, x.getPostStatsThen()),
						combinePreBodyPost(x.getLocation(), x.getPreStatsElse(), e, x.getPostStatsElse()));
		}

		@Override
		public Statement visitStringTemplateWhile(While x) {
			Statement body = x.getBody().accept(this);
			return factory.makeStat("While", x.getLocation(), factory.make("Label","Empty", x.getLocation()), Collections.singletonList(x.getCondition()), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringMiddleInterpolated(Interpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = makeAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), mid, exp, tail);
		}

		@Override
		public Statement visitStringMiddleTemplate(Template x) {
			Statement mid = x.getMid().accept(this);
			Statement tmp = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), mid, tmp, tail);
		}
		
		@Override
		public Statement visitStringMiddleMid(Mid x) {
			return x.getMid().accept(this);
		}

		@Override
		public Statement visitMidStringCharsLexical(Lexical x) {
			return makeAppend(makeLit(x.getLocation(), x.getString()));
		}

		@Override
		public Statement visitPreStringCharsLexical(
				org.rascalmpl.ast.PreStringChars.Lexical x) {
			return makeAppend(makeLit(x.getLocation(), x.getString()));
		}
		
		@Override
		public Statement visitPostStringCharsLexical(
				org.rascalmpl.ast.PostStringChars.Lexical x) {
			return makeAppend(makeLit(x.getLocation(), x.getString()));
		}

		@Override
		public Statement visitStringTailMidInterpolated(
				org.rascalmpl.ast.StringTail.MidInterpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = makeAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), mid, exp, tail);
		}

		@Override
		public Statement visitStringConstantLexical(
				org.rascalmpl.ast.StringConstant.Lexical x) {
			return makeAppend(makeLit(x.getLocation(), x.getString()));
		}
		
		@Override
		public Statement visitStringTailMidTemplate(
				org.rascalmpl.ast.StringTail.MidTemplate x) {
			Statement mid = x.getMid().accept(this);
			Statement template = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), mid, template, tail);
		}
		
		@Override
		public Statement visitStringTailPost(Post x) {
			return x.getPost().accept(this);
		}
		
	
	}
}
