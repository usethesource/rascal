package org.rascalmpl.interpreter;

import java.io.PrintWriter;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.utils.Names;

public class PatternEvaluator extends org.rascalmpl.ast.NullASTVisitor<org.rascalmpl.interpreter.matching.IMatchingResult> implements org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.matching.IMatchingResult> {
	private final org.rascalmpl.interpreter.IEvaluatorContext ctx;
	private boolean debug = false;
	private static final org.eclipse.imp.pdb.facts.type.TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	
	public PatternEvaluator(org.rascalmpl.interpreter.IEvaluatorContext ctx){
		this.ctx = ctx;
	}

	public static org.eclipse.imp.pdb.facts.type.TypeFactory __getTf() {
		return tf;
	}

	public void __setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean __getDebug() {
		return debug;
	}

	public org.rascalmpl.interpreter.IEvaluatorContext __getCtx() {
		return ctx;
	}

	public org.eclipse.imp.pdb.facts.IValue call(java.lang.String name, org.eclipse.imp.pdb.facts.IValue... args) {
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("should not call call");
	}
	
	public java.lang.String getValueAsString(java.lang.String varName){
		org.rascalmpl.interpreter.env.Environment env = this.__getCtx().getCurrentEnvt();
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> res = env.getVariable(varName);
		if(res != null && res.getValue() != null){
			if(res.getType().isStringType()) return ((org.eclipse.imp.pdb.facts.IString)res.getValue()).getValue(); 
			
			return res.getValue().toString();	
		}
		
		throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError(varName, this.__getCtx().getCurrentAST());  
	}

	/*
	 * Interpolate all occurrences of <X> by the value of X
	 */
	public java.lang.String interpolate(java.lang.String re){
		java.util.regex.Pattern replacePat = java.util.regex.Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)>");
		java.util.regex.Matcher m = replacePat.matcher(re);
		java.lang.StringBuffer result = new java.lang.StringBuffer();
		int start = 0;
		while(m.find()){
			result.append(re.substring(start, m.start(0))).
			append(this.getValueAsString(m.group(1))); // TODO: escape special chars?
			start = m.end(0);
		}
		result.append(re.substring(start,re.length()));

		if(this.__getDebug())System.err.println("interpolate: " + re + " -> " + result);
		return result.toString();
	}

	public boolean isConcreteSyntaxAppl(org.rascalmpl.ast.Expression.CallOrTree tree){
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(tree.getExpression().getQualifiedName())).equals("appl") && tree._getType() instanceof org.rascalmpl.interpreter.types.NonTerminalType;
	}

	public boolean isConcreteSyntaxAmb(org.rascalmpl.ast.Expression.CallOrTree tree){
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(tree.getExpression().getQualifiedName())).equals("amb") && tree._getType() instanceof org.rascalmpl.interpreter.types.NonTerminalType;
	}
	

	public boolean isConcreteSyntaxList(org.rascalmpl.ast.Expression.CallOrTree tree){
		return this.isConcreteSyntaxAppl(tree) && this.isConcreteListProd((org.rascalmpl.ast.Expression.CallOrTree) tree.getArguments().get(0)) && tree._getType() instanceof org.rascalmpl.interpreter.types.NonTerminalType;
	}
	
	public boolean isConcreteSyntaxOptional(org.rascalmpl.ast.Expression.CallOrTree tree){
		return this.isConcreteSyntaxAppl(tree) && this.isConcreteOptionalProd((org.rascalmpl.ast.Expression.CallOrTree) tree.getArguments().get(0)) && tree._getType() instanceof org.rascalmpl.interpreter.types.NonTerminalType;
	}

	private boolean isConcreteListProd(org.rascalmpl.ast.Expression.CallOrTree prod){
		if (!prod.getExpression().isQualifiedName()) {
			return false;
		}
		java.lang.String name = org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(prod.getExpression().getQualifiedName()));
		// TODO: note how this code breaks if we start using regular for other things besides lists...
		if (name.equals("regular")) {
			Expression sym = prod.getArguments().get(0);
			if (Names.name(Names.lastName(sym.getExpression().getQualifiedName())).startsWith("iter")) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isConcreteOptionalProd(org.rascalmpl.ast.Expression.CallOrTree prod){
		if (!prod.getExpression().isQualifiedName()) {
			return false;
		}
		java.lang.String name = org.rascalmpl.interpreter.utils.Names.name(org.rascalmpl.interpreter.utils.Names.lastName(prod.getExpression().getQualifiedName()));
		// TODO: note how this code breaks if we start using regular for other things besides lists...
		if (name.equals("regular")) {
			Expression sym = prod.getArguments().get(0);
			if (Names.name(Names.lastName(sym.getExpression().getQualifiedName())).equals("opt")) {
				return true;
			}
		}
		
		return false;
	}

	public java.util.List<org.rascalmpl.interpreter.matching.IMatchingResult> visitArguments(org.rascalmpl.ast.Expression.CallOrTree x){
		java.util.List<org.rascalmpl.ast.Expression> elements = x.getArguments();
		return this.visitElements(elements);
	}

	public java.util.List<org.rascalmpl.interpreter.matching.IMatchingResult> visitConcreteLexicalArguments(org.rascalmpl.ast.Expression.CallOrTree x){
        org.rascalmpl.ast.Expression args = x.getArguments().get(1);
        
		java.util.List<org.rascalmpl.ast.Expression> elements = args.getElements();
		return this.visitElements(elements);
	}

	
	public java.util.List<org.rascalmpl.interpreter.matching.IMatchingResult> visitConcreteArguments(org.rascalmpl.ast.Expression.CallOrTree x){
        org.rascalmpl.ast.Expression args = x.getArguments().get(1);
        
		java.util.List<org.rascalmpl.ast.Expression> elements = args.getElements();
		return this.visitConcreteElements(elements);
	}
	
	private java.util.List<org.rascalmpl.interpreter.matching.IMatchingResult> visitConcreteElements(java.util.List<org.rascalmpl.ast.Expression> elements){
		int n = elements.size();
		java.util.ArrayList<org.rascalmpl.interpreter.matching.IMatchingResult> args = new java.util.ArrayList<org.rascalmpl.interpreter.matching.IMatchingResult>((n + 1) / 2);

		for (int i = 0; i < n; i += 2) { // skip layout elements
			org.rascalmpl.ast.Expression e = elements.get(i);
			args.add(e.__evaluate(this));
		}
		return args;
	}


	public java.util.List<org.rascalmpl.interpreter.matching.IMatchingResult> visitElements(java.util.List<org.rascalmpl.ast.Expression> elements){
		java.util.ArrayList<org.rascalmpl.interpreter.matching.IMatchingResult> args = new java.util.ArrayList<org.rascalmpl.interpreter.matching.IMatchingResult>(elements.size());

		int i = 0;
		for(org.rascalmpl.ast.Expression e : elements){
			args.add(i++, e.__evaluate(this));
		}
		return args;
	}

	public org.rascalmpl.ast.AbstractAST getCurrentAST() {
		return this.__getCtx().getCurrentAST();
	}

	public org.rascalmpl.interpreter.env.Environment getCurrentEnvt() {
		return this.__getCtx().getCurrentEnvt();
	}

	public org.rascalmpl.interpreter.Evaluator getEvaluator() {
		return this.__getCtx().getEvaluator();
	}

	public org.rascalmpl.interpreter.env.GlobalEnvironment getHeap() {
		return this.__getCtx().getHeap();
	}

	public java.lang.String getStackTrace() {
		return this.__getCtx().getStackTrace();
	}

	public void pushEnv() {
		this.__getCtx().pushEnv();		
	}

	public boolean runTests() {
		return this.__getCtx().runTests();
	}

	public void setCurrentEnvt(org.rascalmpl.interpreter.env.Environment environment) {
		this.__getCtx().setCurrentEnvt(environment);
	}

	public void unwind(org.rascalmpl.interpreter.env.Environment old) {
		this.__getCtx().unwind(old);
	}

	public void setCurrentAST(org.rascalmpl.ast.AbstractAST ast) {
		this.__getCtx().setCurrentAST(ast);
	}

	public org.eclipse.imp.pdb.facts.IValueFactory getValueFactory() {
		return this.__getCtx().getValueFactory();
	}

	public org.rascalmpl.interpreter.strategy.IStrategyContext getStrategyContext() {
		return this.__getCtx().getStrategyContext();
	}

	public void pushStrategyContext(org.rascalmpl.interpreter.strategy.IStrategyContext strategyContext) {
		this.__getCtx().pushStrategyContext(strategyContext);
	}

	public void popStrategyContext() {
		this.__getCtx().popStrategyContext();
	}

	public java.util.Stack<org.rascalmpl.interpreter.Accumulator> getAccumulators() {
		return this.__getCtx().getAccumulators();
	}

	public void setAccumulators(java.util.Stack<org.rascalmpl.interpreter.Accumulator> accumulators) {
		this.__getCtx().setAccumulators(accumulators);
	}

	public org.rascalmpl.uri.URIResolverRegistry getResolverRegistry() {
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
