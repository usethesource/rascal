package org.meta_environment.rascal.interpreter;

import java.util.Arrays;

import org.eclipse.imp.pdb.facts.INode;
import org.meta_environment.rascal.ast.BooleanLiteral;
import org.meta_environment.rascal.ast.DataTarget;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Label;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.StringConstant;
import org.meta_environment.rascal.ast.StringLiteral;
import org.meta_environment.rascal.ast.MidStringChars.Lexical;
import org.meta_environment.rascal.ast.StringLiteral.NonInterpolated;
import org.meta_environment.rascal.ast.StringMiddle.Interpolated;
import org.meta_environment.rascal.ast.StringMiddle.Mid;
import org.meta_environment.rascal.ast.StringMiddle.Template;
import org.meta_environment.rascal.ast.StringTail.Post;
import org.meta_environment.rascal.ast.StringTemplate.DoWhile;
import org.meta_environment.rascal.ast.StringTemplate.For;
import org.meta_environment.rascal.ast.StringTemplate.IfThen;
import org.meta_environment.rascal.ast.StringTemplate.IfThenElse;
import org.meta_environment.rascal.ast.StringTemplate.While;
import org.meta_environment.rascal.interpreter.utils.Utils;

public class StringTemplateConverter {
	private static final Name OUTER_FOR_LOOP_LABEL = new Name.Lexical(null, "#");
	
	private static Statement surroundWithSingleIterForLoop(INode src, Statement body) {
		Name dummy = new Name.Lexical(src, "_");
		Expression var = new Expression.QualifiedName(src, new QualifiedName.Default(src, Arrays.asList(dummy)));
		Expression truth = new Expression.Literal(src, new org.meta_environment.rascal.ast.Literal.Boolean(src, new BooleanLiteral.Lexical(src, "true")));
		Expression list = new Expression.List(src, Arrays.asList(truth));
		Expression enumerator = new Expression.Enumerator(src, var, list);
		Statement stat = new Statement.For(src, new Label.Default(src, OUTER_FOR_LOOP_LABEL), Arrays.asList(enumerator), body);
		return stat;
	}


	public static Statement convert(org.meta_environment.rascal.ast.StringLiteral str) {
		return surroundWithSingleIterForLoop(str.getTree(), str.accept(new Visitor()));
	}
	
	private static class Visitor extends NullASTVisitor<Statement> {

		private static Statement makeBlock(INode src, Statement ...stats) {
			return new Statement.NonEmptyBlock(src, new Label.Empty(src),
					Arrays.asList(stats));
		}
		
		private static Statement makeAppend(Expression exp) {
			return new Statement.Append(exp.getTree(), new DataTarget.Labeled(null, OUTER_FOR_LOOP_LABEL),
					new Statement.Expression(exp.getTree(), exp)); 
		}
		
		private static Expression makeLit(INode src, String str) {
			// Note: we don't unescape here this happens
			// in the main evaluator; also, we pretend 
			// "...< etc. to be "..." stringliterals...
			return new Expression.Literal(src, 
					new org.meta_environment.rascal.ast.Literal.String(src, 
							new StringLiteral.NonInterpolated(src, 
									new StringConstant.Lexical(src, str))));
		}
		
		
		@Override
		public Statement visitStringLiteralInterpolated(
				org.meta_environment.rascal.ast.StringLiteral.Interpolated x) {
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
				org.meta_environment.rascal.ast.StringLiteral.Template x) {
			Statement pre = x.getPre().accept(this);
			Statement template = x.getTemplate().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), pre, template, tail);
		}
		
		@Override
		public Statement visitStringTemplateDoWhile(DoWhile x) {
			Statement body = x.getBody().accept(this);
			return new Statement.DoWhile(x.getTree(), new Label.Empty(x.getTree()), body , x.getCondition());
		}

		@Override
		public Statement visitStringTemplateFor(For x) {
			Statement body = x.getBody().accept(this);
			return new Statement.For(x.getTree(), new Label.Empty(x.getTree()), x.getGenerators(), body);
		}

		@Override
		public Statement visitStringTemplateIfThen(IfThen x) {
			Statement body = x.getBody().accept(this);
			return new Statement.IfThen(x.getTree(), new Label.Empty(x.getTree()), x.getConditions(), body, null);
		}

		@Override
		public Statement visitStringTemplateIfThenElse(IfThenElse x) {
			Statement t = x.getThenString().accept(this);
			Statement e = x.getElseString().accept(this);
			return new Statement.IfThenElse(x.getTree(), new Label.Empty(x.getTree()), 
					x.getConditions(), t, e);
		}

		@Override
		public Statement visitStringTemplateWhile(While x) {
			Statement body = x.getBody().accept(this);
			return new Statement.While(x.getTree(), new Label.Empty(x.getTree()), x.getCondition(), body);
		}

		@Override
		public Statement visitStringMiddleInterpolated(Interpolated x) {
			INode src = x.getTree();
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
				org.meta_environment.rascal.ast.PreStringChars.Lexical x) {
			return makeAppend(makeLit(x.getTree(), x.getString()));
		}
		
		@Override
		public Statement visitPostStringCharsLexical(
				org.meta_environment.rascal.ast.PostStringChars.Lexical x) {
			return makeAppend(makeLit(x.getTree(), x.getString()));
		}

		@Override
		public Statement visitStringTailMidInterpolated(
				org.meta_environment.rascal.ast.StringTail.MidInterpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = x.getExpression().accept(this);
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getTree(), mid, exp, tail);
		}

		@Override
		public Statement visitStringTailMidTemplate(
				org.meta_environment.rascal.ast.StringTail.MidTemplate x) {
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
