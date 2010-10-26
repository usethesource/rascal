package org.rascalmpl.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.ASTFactoryFactory;
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

public class StringTemplateConverter {
	private static int labelCounter = 0;
	
	private static Statement surroundWithSingleIterForLoop(INode src, Name label, Statement body) {
		ASTFactory factory = ASTFactoryFactory.getASTFactory();
		Name dummy = factory.makeNameLexical(src, "_");
		Expression var = factory.makeExpressionQualifiedName(src, factory.makeQualifiedNameDefault(src, Arrays.asList(dummy)));
		Expression truth = factory.makeExpressionLiteral(src, factory.makeLiteralBoolean(src, factory.makeBooleanLiteralLexical(src, "true")));
		Expression list = factory.makeExpressionList(src, Arrays.asList(truth));
		Expression enumerator = factory.makeExpressionEnumerator(src, var, list);
		Statement stat = factory.makeStatementFor(src, factory.makeLabelDefault(src, label), Arrays.asList(enumerator), body);
		return stat;
	}


	public static Statement convert(org.rascalmpl.ast.StringLiteral str) {
		final Name label= ASTFactoryFactory.getASTFactory().makeNameLexical(null, "#" + labelCounter);
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
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			return factory.makeStatementNonEmptyBlock(src, factory.makeLabelEmpty(src),
					stats);
		}

		
		private Statement makeAppend(Expression exp) {
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			return factory.makeStatementAppend(exp.getTree(), factory.makeDataTargetLabeled(null, label),
					factory.makeStatementExpression(exp.getTree(), exp)); 
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
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			return factory.makeExpressionLiteral(src, 
					factory.makeLiteralString(src, 
							factory.makeStringLiteralNonInterpolated(src, 
									factory.makeStringConstantLexical(src, str))));
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
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			Statement body = x.getBody().accept(this);
			return factory.makeStatementDoWhile(x.getTree(), factory.makeLabelEmpty(x.getTree()), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()) , x.getCondition());
		}


		@Override
		public Statement visitStringTemplateFor(For x) {
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			Statement body = x.getBody().accept(this);
			return factory.makeStatementFor(x.getTree(), factory.makeLabelEmpty(x.getTree()), x.getGenerators(), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringTemplateIfThen(IfThen x) {
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			Statement body = x.getBody().accept(this);
			return factory.makeStatementIfThen(x.getTree(), factory.makeLabelEmpty(x.getTree()), x.getConditions(), 
					combinePreBodyPost(x.getTree(), x.getPreStats(), body, x.getPostStats()), null);
		}

		@Override
		public Statement visitStringTemplateIfThenElse(IfThenElse x) {
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			Statement t = x.getThenString().accept(this);
			Statement e = x.getElseString().accept(this);
			return factory.makeStatementIfThenElse(x.getTree(), factory.makeLabelEmpty(x.getTree()), 
					x.getConditions(), 
						combinePreBodyPost(x.getTree(), x.getPreStatsThen(), t, x.getPostStatsThen()),
						combinePreBodyPost(x.getTree(), x.getPreStatsElse(), e, x.getPostStatsElse()));
		}

		@Override
		public Statement visitStringTemplateWhile(While x) {
			ASTFactory factory = ASTFactoryFactory.getASTFactory();
			Statement body = x.getBody().accept(this);
			return factory.makeStatementWhile(x.getTree(), factory.makeLabelEmpty(x.getTree()), Collections.singletonList(x.getCondition()), 
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
