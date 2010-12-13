package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.lang.String;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIResolverRegistry;

public class PatternEvaluator extends NullASTVisitor<IMatchingResult> implements IEvaluator<IMatchingResult> {
	private final IEvaluatorContext ctx;
	private boolean debug = false;
	private static final TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();

	public PatternEvaluator(IEvaluatorContext ctx) {
		this.ctx = ctx;
	}

	public static TypeFactory __getTf() {
		return tf;
	}

	public void __setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean __getDebug() {
		return debug;
	}

	public IEvaluatorContext __getCtx() {
		return ctx;
	}

	public IValue call(String name, IValue... args) {
		throw new ImplementationError("should not call call");
	}

	public String getValueAsString(String varName) {
		Environment env = this.__getCtx().getCurrentEnvt();
		Result<IValue> res = env.getVariable(varName);
		if (res != null && res.getValue() != null) {
			if (res.getType().isStringType())
				return ((IString) res.getValue()).getValue();

			return res.getValue().toString();
		}

		throw new UninitializedVariableError(varName, this.__getCtx().getCurrentAST());
	}

	/*
	 * Interpolate all occurrences of <X> by the value of X
	 */
	public String interpolate(String re) {
		Pattern replacePat = java.util.regex.Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)>");
		Matcher m = replacePat.matcher(re);
		StringBuffer result = new StringBuffer();
		int start = 0;
		while (m.find()) {
			result.append(re.substring(start, m.start(0))).append(this.getValueAsString(m.group(1))); // TODO:
																										// escape
																										// special
																										// chars?
			start = m.end(0);
		}
		result.append(re.substring(start, re.length()));

		if (this.__getDebug())
			System.err.println("interpolate: " + re + " -> " + result);
		return result.toString();
	}

	public boolean isConcreteSyntaxAppl(CallOrTree tree) {
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(tree.getExpression().getQualifiedName())).equals("appl")
				&& tree._getType() instanceof NonTerminalType;
	}

	public boolean isConcreteSyntaxAmb(CallOrTree tree) {
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(tree.getExpression().getQualifiedName())).equals("amb")
				&& tree._getType() instanceof NonTerminalType;
	}

	public boolean isConcreteSyntaxList(CallOrTree tree) {
		return this.isConcreteSyntaxAppl(tree) && this.isConcreteListProd((CallOrTree) tree.getArguments().get(0)) && tree._getType() instanceof NonTerminalType;
	}

	public boolean isConcreteSyntaxOptional(CallOrTree tree) {
		return this.isConcreteSyntaxAppl(tree) && this.isConcreteOptionalProd((CallOrTree) tree.getArguments().get(0)) && tree._getType() instanceof NonTerminalType;
	}

	private boolean isConcreteListProd(CallOrTree prod) {
		if (!prod.getExpression().isQualifiedName()) {
			return false;
		}
		String name = org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(prod.getExpression().getQualifiedName()));
		// TODO: note how this code breaks if we start using regular for other
		// things besides lists...
		if (name.equals("regular")) {
			Expression sym = prod.getArguments().get(0);
			if (Names.name(Names.lastName(sym.getExpression().getQualifiedName())).startsWith("iter")) {
				return true;
			}
		}

		return false;
	}

	private boolean isConcreteOptionalProd(CallOrTree prod) {
		if (!prod.getExpression().isQualifiedName()) {
			return false;
		}
		String name = org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(prod.getExpression().getQualifiedName()));
		// TODO: note how this code breaks if we start using regular for other
		// things besides lists...
		if (name.equals("regular")) {
			Expression sym = prod.getArguments().get(0);
			if (Names.name(Names.lastName(sym.getExpression().getQualifiedName())).equals("opt")) {
				return true;
			}
		}

		return false;
	}

	public List<IMatchingResult> visitArguments(CallOrTree x) {
		List<Expression> elements = x.getArguments();
		return this.visitElements(elements);
	}

	public List<IMatchingResult> visitConcreteLexicalArguments(CallOrTree x) {
		Expression args = x.getArguments().get(1);

		List<Expression> elements = args.getElements();
		return this.visitElements(elements);
	}

	public List<IMatchingResult> visitConcreteArguments(CallOrTree x) {
		Expression args = x.getArguments().get(1);

		List<Expression> elements = args.getElements();
		return this.visitConcreteElements(elements);
	}

	private List<IMatchingResult> visitConcreteElements(List<Expression> elements) {
		int n = elements.size();
		ArrayList<IMatchingResult> args = new ArrayList<IMatchingResult>((n + 1) / 2);

		for (int i = 0; i < n; i += 2) { // skip layout elements
			Expression e = elements.get(i);
			args.add(e.__evaluate(this));
		}
		return args;
	}

	public List<IMatchingResult> visitElements(List<Expression> elements) {
		ArrayList<IMatchingResult> args = new ArrayList<IMatchingResult>(elements.size());

		int i = 0;
		for (Expression e : elements) {
			args.add(i++, e.__evaluate(this));
		}
		return args;
	}

	public AbstractAST getCurrentAST() {
		return this.__getCtx().getCurrentAST();
	}

	public Environment getCurrentEnvt() {
		return this.__getCtx().getCurrentEnvt();
	}

	public Evaluator getEvaluator() {
		return this.__getCtx().getEvaluator();
	}

	public GlobalEnvironment getHeap() {
		return this.__getCtx().getHeap();
	}

	public String getStackTrace() {
		return this.__getCtx().getStackTrace();
	}

	public void pushEnv() {
		this.__getCtx().pushEnv();
	}

	public boolean runTests() {
		return this.__getCtx().runTests();
	}

	public void setCurrentEnvt(Environment environment) {
		this.__getCtx().setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		this.__getCtx().unwind(old);
	}

	public void setCurrentAST(AbstractAST ast) {
		this.__getCtx().setCurrentAST(ast);
	}

	public IValueFactory getValueFactory() {
		return this.__getCtx().getValueFactory();
	}

	public IStrategyContext getStrategyContext() {
		return this.__getCtx().getStrategyContext();
	}

	public void pushStrategyContext(IStrategyContext strategyContext) {
		this.__getCtx().pushStrategyContext(strategyContext);
	}

	public void popStrategyContext() {
		this.__getCtx().popStrategyContext();
	}

	public Stack<Accumulator> getAccumulators() {
		return this.__getCtx().getAccumulators();
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		this.__getCtx().setAccumulators(accumulators);
	}

	public URIResolverRegistry getResolverRegistry() {
		return this.__getCtx().getResolverRegistry();
	}

	public void interrupt() {

	}

	public boolean isInterrupted() {
		return false;
	}

	public PrintWriter getStdErr() {
		return null;
	}

	public PrintWriter getStdOut() {
		return null;
	}

}
