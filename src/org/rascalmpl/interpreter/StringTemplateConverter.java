package org.rascalmpl.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.BooleanLiteral;
import org.rascalmpl.ast.DataTarget;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Label;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.StringLiteral;
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

public class StringTemplateConverter {
	private static int labelCounter = 0;
	
	private static Statement surroundWithSingleIterForLoop(INode src, Name label, Statement body) {
		Name dummy = new Name.Lexical(src, "_");
		Expression var = new Expression.QualifiedName(src, new QualifiedName.Default(src, Arrays.asList(dummy)));
		Expression truth = new Expression.Literal(src, new org.rascalmpl.ast.Literal.Boolean(src, new BooleanLiteral.Lexical(src, "true")));
		Expression list = new Expression.List(src, Arrays.asList(truth));
		Expression enumerator = new Expression.Enumerator(src, var, list);
		Statement stat = new Statement.For(src, new Label.Default(src, label), Arrays.asList(enumerator), body);
		return stat;
	}


	public static Statement convert(org.rascalmpl.ast.StringLiteral str) {
		final Name label= new Name.Lexical(null, "#" + labelCounter);
		labelCounter++;
		return surroundWithSingleIterForLoop(str.getTree(), label, str.accept(new Visitor(label)));
	}
	
	private static class Visitor extends NullASTVisitor<Statement> {
		
		private final Name label;

		public Visitor(Name label) {
			this.label = label;
		}

		private static Statement makeBlock(INode src, Statement ...stats) {
			return makeBlock(src, Arrays.asList(stats));
		}
		
		private static Statement makeBlock(INode src, List<Statement> stats) {
			return new Statement.NonEmptyBlock(src, new Label.Empty(src),
					stats);
		}

		
		private Statement makeAppend(Expression exp) {
			return new Statement.Append(exp.getTree(), new DataTarget.Labeled(null, label),
					new Statement.Expression(exp.getTree(), exp)); 
		}
		
		private static Statement combinePreBodyPost(INode src, List<Statement> pre, Statement body, List<Statement> post) {
			List<Statement> stats = new ArrayList<Statement>();
			stats.addAll(pre);
			stats.add(body);
			stats.addAll(post);
			return makeBlock(src, stats);
		}
		
		
		private static Expression makeLit(INode src, String str) {
			// Note: we don't unescape here this happens
			// in the main evaluator; also, we pretend 
			// "...< etc. to be "..." stringliterals...
			return new Expression.Literal(src, 
					new org.rascalmpl.ast.Literal.String(src, 
							new StringLiteral.NonInterpolated(src, 
									new StringConstant.Lexical(src, str))));
		}
		
		
		@Override
		public Statement visitStringLiteralInterpolated(
				org.rascalmpl.ast.StringLiteral.Interpolated x) {
			Statement pre = x.getPre().accept(this);
			Statement exp = makeAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), pre, exp, tail);
		}
		
		@Override
		public Statement visitStringLiteralNonInterpolated(NonInterpolated x) {
			return makeAppend(makeLit(x.getTree(), ((StringConstant.Lexical)x.getConstant()).getString()));
		}
		
		@Override
		public Statement visitStringLiteralTemplate(
				org.rascalmpl.ast.StringLiteral.Template x) {
			Statement pre = x.getPre().accept(this);
			Statement template = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), pre, template, tail);
		}
		
	
		@Override
		public Statement visitStringTemplateDoWhile(DoWhile x) {
			Statement body = x.getBody().accept(this);
			return new Statement.DoWhile(x.getTree(), new Label.Empty(x.getTree()), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()) , x.getCondition());
		}


		@Override
		public Statement visitStringTemplateFor(For x) {
			Statement body = x.getBody().accept(this);
			return new Statement.For(x.getTree(), new Label.Empty(x.getTree()), x.getGenerators(), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringTemplateIfThen(IfThen x) {
			Statement body = x.getBody().accept(this);
			return new Statement.IfThen(x.getTree(), new Label.Empty(x.getTree()), x.getConditions(), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()), null);
		}

		@Override
		public Statement visitStringTemplateIfThenElse(IfThenElse x) {
			Statement t = x.getThenString().accept(this);
			Statement e = x.getElseString().accept(this);
			return new Statement.IfThenElse(x.getTree(), new Label.Empty(x.getTree()), 
					x.getConditions(), 
						combinePreBodyPost(x.getTree(), x.getPreStatsThen(), t, x.getPostStatsThen()),
						combinePreBodyPost(x.getTree(), x.getPreStatsElse(), e, x.getPostStatsElse()));
		}

		@Override
		public Statement visitStringTemplateWhile(While x) {
			Statement body = x.getBody().accept(this);
			return new Statement.While(x.getTree(), new Label.Empty(x.getTree()), Collections.singletonList(x.getCondition()), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringMiddleInterpolated(Interpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = makeAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), mid, exp, tail);
		}

		@Override
		public Statement visitStringMiddleTemplate(Template x) {
			Statement mid = x.getMid().accept(this);
			Statement tmp = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), mid, tmp, tail);
		}
		
		@Override
		public Statement visitStringMiddleMid(Mid x) {
			return x.getMid().accept(this);
		}

		@Override
		public Statement visitMidStringCharsLexical(Lexical x) {
			return makeAppend(makeLit(x.getTree(), x.getString()));
		}

		@Override
		public Statement visitPreStringCharsLexical(
				org.rascalmpl.ast.PreStringChars.Lexical x) {
			return makeAppend(makeLit(x.getTree(), x.getString()));
		}
		
		@Override
		public Statement visitPostStringCharsLexical(
				org.rascalmpl.ast.PostStringChars.Lexical x) {
			return makeAppend(makeLit(x.getTree(), x.getString()));
		}

		@Override
		public Statement visitStringTailMidInterpolated(
				org.rascalmpl.ast.StringTail.MidInterpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = makeAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), mid, exp, tail);
		}

		@Override
		public Statement visitStringConstantLexical(
				org.rascalmpl.ast.StringConstant.Lexical x) {
			return makeAppend(makeLit(x.getTree(), x.getString()));
		}
		
		@Override
		public Statement visitStringTailMidTemplate(
				org.rascalmpl.ast.StringTail.MidTemplate x) {
			Statement mid = x.getMid().accept(this);
			Statement template = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), mid, template, tail);
		}
		
		@Override
		public Statement visitStringTailPost(Post x) {
			return x.getPost().accept(this);
		}
		
	
	}
}
