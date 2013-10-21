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
package org.rascalmpl.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.DataTarget;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.MidStringChars.Lexical;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringConstant;
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
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.OrgString;
import org.rascalmpl.values.OriginValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;
  
public class StringTemplateConverter {
	private static int labelCounter = 0;
	
	public StringTemplateConverter() {
		super();
	}
	
	private Statement surroundWithSingleIterForLoop(ISourceLocation loc, Name label, Statement body) {
		Name dummy = ASTBuilder.make("Name","Lexical",loc, "_");
		Expression var = ASTBuilder.make("Expression","QualifiedName",loc, ASTBuilder.make("QualifiedName", loc, Arrays.asList(dummy)));
		Expression truth = ASTBuilder.make("Expression","Literal",loc, ASTBuilder.make("Literal","Boolean",loc, ASTBuilder.make("BooleanLiteral","Lexical",loc, "true")));
		Expression list = ASTBuilder.make("Expression","List", loc, Arrays.asList(truth));
		Expression enumerator = ASTBuilder.make("Expression","Enumerator",loc, var, list);
		Statement stat = ASTBuilder.make("Statement","For",loc, ASTBuilder.make("Label","Default", loc, label), Arrays.asList(enumerator), body);
		return stat;
	}


	public Statement convert(org.rascalmpl.ast.StringLiteral str) {
		final Name label= ASTBuilder.make("Name","Lexical", str.getLocation(), "#" + labelCounter);
		labelCounter++;
		Statement stat = str.accept(new Visitor(label));
		return surroundWithSingleIterForLoop(str.getLocation(), label, stat);
	}
	
	private static class Visitor extends NullASTVisitor<Statement> {
		private final Name label;

		public Visitor(Name label) {
			this.label = label;
		}

		private Statement makeBlock(ISourceLocation src, Statement ...stats) {
			return makeBlock(src, Arrays.asList(stats));
		}
		
		private Statement makeBlock(ISourceLocation loc, List<Statement> stats) {
			return ASTBuilder.make("Statement","NonEmptyBlock",loc, ASTBuilder.make("Label", "Empty", loc),
					stats);
		}

		
		private class IndentingAppend extends org.rascalmpl.semantics.dynamic.Statement.Append {

			public IndentingAppend(ISourceLocation __param1, DataTarget __param2,
					Statement __param3) {
				super(null, __param2, __param3);
				setSourceLocation(__param1);
			} 
			
			@Override
			public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
				Accumulator target = getTarget(__eval);
				
				// TODO refactor this: pull up to Append
				Result<IValue> result = this.getStatement().interpret(__eval);
				IRascalValueFactory vf = ValueFactoryFactory.getValueFactory();
				IValue v = result.getValue();
				v = convertToString(v);
				java.lang.String fill = __eval.getCurrentIndent();
				if (v instanceof OrgString) {
					v = ((OrgString)v).replaceAll("\n", vf.string(src, "\n" + fill));
				}
				else {
					java.lang.String content = ((IString)v).getValue();
					content = content.replaceAll("\n", "\n" + fill);
					v = vf.string(content);
				}
				result = ResultFactory.makeResult(v.getType(), v, result.getEvaluatorContext());
				target.append(result);
				return result;
			}
			
			private IString convertToString(IValue value) {
				IRascalValueFactory vf = ValueFactoryFactory.getValueFactory();
				if (value.getType() == Factory.Tree && value.asAnnotatable().hasAnnotation("loc")) {
					return vf.string(TreeAdapter.getLocation((IConstructor) value),
					    org.rascalmpl.values.uptr.TreeAdapter.yield((IConstructor) value));
				}
				else if ((value.getType().isNode() || value.getType().isConstructor()) 
						&& value.asAnnotatable().hasAnnotation("location")) {
					return vf.string((ISourceLocation) value.asAnnotatable().getAnnotation("location"),
						    value.toString());
				}
				else if (value.getType().isSubtypeOf(Factory.Type)) {
					return vf.string(src, SymbolAdapter.toString((IConstructor) ((IConstructor) value).get("symbol"), false));
				}
				else if (value.getType().isString()) {
					// use the origin of the string, if any
					return (IString) value;
				} 
				else {
					// use the expression location as origin.
					return vf.string(src, value.toString());
				}
			}
			
			private void appendToString(IValue value, StringBuilder b) {
				if (value.getType() == Factory.Tree) {
					b.append(org.rascalmpl.values.uptr.TreeAdapter.yield((IConstructor) value));
				}
				else if (value.getType().isSubtypeOf(Factory.Type)) {
					b.append(SymbolAdapter.toString((IConstructor) ((IConstructor) value).get("symbol"), false));
				}
				else if (value.getType().isString()) {
					b.append(((IString) value).getValue());
				} 
				else {
					b.append(value.toString());
				}
			}
			
		}
		
		private static class ConstAppend extends org.rascalmpl.semantics.dynamic.Statement.Append {
			private static final Pattern MARGIN = Pattern.compile("^[ \t]*'", Pattern.MULTILINE);
			protected final IString str;

			public ConstAppend(ISourceLocation __param1, DataTarget __param2, String arg) {
				super(null, __param2, null);
				setSourceLocation(__param1);
				str = initString(preprocess(arg));
			}
			
			protected IString initString(String preprocessedString) {
				return makeValue(removeMargins(preprocessedString));
			}
			
			private IString makeValue(String arg) {
				IRascalValueFactory vf = ValueFactoryFactory.getValueFactory();
				// Consts always have " <, or > ", or > <.
				ISourceLocation loc = vf.sourceLocation(src, src.getOffset() + 1, 
						src.getLength() - 2, 
						src.getBeginLine(), 
						src.getEndLine(), 
						src.getBeginColumn() + 1,
						src.getEndColumn() - 1);
				return vf.string(loc, arg);
			}
			
			private String removeMargins(String arg) {
				arg = MARGIN.matcher(arg).replaceAll("");
				return org.rascalmpl.interpreter.utils.StringUtils.unescapeSingleQuoteAndBackslash(arg);
			}
			
			private String preprocess(String arg) {
				arg = org.rascalmpl.interpreter.utils.StringUtils.unquote(arg);
				// don't unescape ' yet
				arg = org.rascalmpl.interpreter.utils.StringUtils.unescapeBase(arg);
				return arg;
			}
			
			@Override
			public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
				Result<IValue> result = ResultFactory.makeResult(str.getType(), str, __eval);
				getTarget(__eval).append(result);
				return result;
			}
			
		}
		
		
		private abstract static class IndentingStringFragmentAppend extends ConstAppend {
			private static final Pattern INDENT = Pattern.compile("(?<![\\\\])'([ \t]*)([^']*)$");
			private String indent;
			
			public IndentingStringFragmentAppend(ISourceLocation __param1, DataTarget __param2, String arg) {
				super(__param1, __param2, arg);
			}
			
			@Override
			protected IString initString(String arg) {
				indent = computeIndent(arg);
				return super.initString(arg);
			}

			private String computeIndent(String arg) {
				Matcher m = INDENT.matcher(arg);
				if (m.find()) {
					return m.group(1) + replaceEverythingBySpace(m.group(2));
				}
				return "";
			}
			
			private static final Pattern NONSPACE = Pattern.compile("[^ \t]");
			private String replaceEverythingBySpace(String input) {
				return NONSPACE.matcher(input).replaceAll(" ");
			}

			protected String getIndent() {
				return indent;
			}
			
		}

		private static class PreAppend extends IndentingStringFragmentAppend {
			public PreAppend(ISourceLocation __param1, DataTarget __param2, String arg) {
				super(__param1, __param2, arg);
			}
			
			@Override
			public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
				__eval.indent(getIndent());
				return super.interpret(__eval);
			}
		
		}

		private static class MidAppend extends IndentingStringFragmentAppend {

			public MidAppend(ISourceLocation __param1, DataTarget __param2, String arg) {
				super(__param1, __param2, arg);
			}
			
			@Override
			public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
				__eval.unindent();
				__eval.indent(getIndent());
				return super.interpret(__eval);
			}
		
		}

		private static class PostAppend extends ConstAppend {
			public PostAppend(ISourceLocation __param1, DataTarget __param2, String arg) {
				super(__param1, __param2, arg);
			}
			
			@Override
			public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
				__eval.unindent();
				return super.interpret(__eval);
			}

			
		}		
		
		
		private Statement makeConstAppend(ISourceLocation tree, String str) {
			return new ConstAppend(tree, ASTBuilder.<DataTarget>make("DataTarget","Labeled", tree, label), str); 
		}

		private Statement makePostAppend(ISourceLocation tree, String str) {
			return new PostAppend(tree, ASTBuilder.<DataTarget>make("DataTarget","Labeled", tree, label), str); 
		}

		private Statement makePreAppend(ISourceLocation tree, String str) {
			return new PreAppend(tree, ASTBuilder.<DataTarget>make("DataTarget","Labeled", tree, label), str); 
		}

		private Statement makeMidAppend(ISourceLocation tree, String str) {
			return new MidAppend(tree, ASTBuilder.<DataTarget>make("DataTarget","Labeled", tree, label), str); 
		}

		private Statement makeIndentingAppend(Expression exp) {
			ISourceLocation loc = exp.getLocation();
			return new IndentingAppend(exp.getLocation(), ASTBuilder.<DataTarget>make("DataTarget","Labeled", loc, label),
					ASTBuilder.<Statement>make("Statement","Expression", loc, exp)); 
		}
		
		private  Statement combinePreBodyPost(ISourceLocation src, List<Statement> pre, Statement body, List<Statement> post) {
			List<Statement> stats = new ArrayList<Statement>();
			stats.addAll(pre);
			stats.add(body);
			stats.addAll(post);
			return makeBlock(src, stats);
		}
		
		@Override
		public Statement visitStringLiteralInterpolated(
				org.rascalmpl.ast.StringLiteral.Interpolated x) {
			Statement pre = x.getPre().accept(this);
			Statement exp = makeIndentingAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), pre, exp, tail);
		}
		
		@Override
		public Statement visitStringLiteralNonInterpolated(NonInterpolated x) {
			return makeConstAppend(x.getLocation(), ((StringConstant.Lexical)x.getConstant()).getString());
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
			return ASTBuilder.makeStat("DoWhile", x.getLocation(), ASTBuilder.make("Label","Empty", x.getLocation()), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()) , x.getCondition());
		}

		@Override
		public Statement visitStringTemplateFor(For x) {
			Statement body = x.getBody().accept(this);
			return ASTBuilder.makeStat("For", x.getLocation(), ASTBuilder.make("Label","Empty", x.getLocation()), x.getGenerators(), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringTemplateIfThen(IfThen x) {
			Statement body = x.getBody().accept(this);
			return ASTBuilder.makeStat("IfThen", x.getLocation(), ASTBuilder.make("Label", "Empty", x.getLocation()), x.getConditions(), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringTemplateIfThenElse(IfThenElse x) {
			Statement t = x.getThenString().accept(this);
			Statement e = x.getElseString().accept(this);
			return ASTBuilder.makeStat("IfThenElse", x.getLocation(), ASTBuilder.make("Label","Empty",x.getLocation()), 
					x.getConditions(), 
						combinePreBodyPost(x.getLocation(), x.getPreStatsThen(), t, x.getPostStatsThen()),
						combinePreBodyPost(x.getLocation(), x.getPreStatsElse(), e, x.getPostStatsElse()));
		}

		@Override
		public Statement visitStringTemplateWhile(While x) {
			Statement body = x.getBody().accept(this);
			return ASTBuilder.makeStat("While", x.getLocation(), ASTBuilder.make("Label","Empty", x.getLocation()), Collections.singletonList(x.getCondition()), 
					combinePreBodyPost(x.getLocation(), x.getPreStats(), body, x.getPostStats()));
		}

		@Override
		public Statement visitStringMiddleInterpolated(Interpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = makeIndentingAppend(x.getExpression());
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
			return makeMidAppend(x.getLocation(), x.getString());
		}

		@Override
		public Statement visitPreStringCharsLexical(
				org.rascalmpl.ast.PreStringChars.Lexical x) {
			return makePreAppend(x.getLocation(), x.getString());
		}
		
		@Override
		public Statement visitPostStringCharsLexical(
				org.rascalmpl.ast.PostStringChars.Lexical x) {
			return makePostAppend(x.getLocation(), x.getString());
		}

		@Override
		public Statement visitStringTailMidInterpolated(
				org.rascalmpl.ast.StringTail.MidInterpolated x) {
			Statement mid = x.getMid().accept(this);
			Statement exp = makeIndentingAppend(x.getExpression());
			Statement tail = x.getTail().accept(this);
			return makeBlock(x.getLocation(), mid, exp, tail);
		}

		@Override
		public Statement visitStringConstantLexical(
				org.rascalmpl.ast.StringConstant.Lexical x) {
			return makeConstAppend(x.getLocation(), x.getString());
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
