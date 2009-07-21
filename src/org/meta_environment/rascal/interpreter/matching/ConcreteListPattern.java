package org.meta_environment.rascal.interpreter.matching;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.IUPTRAstToSymbolConstructor;
import org.meta_environment.rascal.interpreter.IUPTRAstToSymbolConstructor.NonGroundSymbolException;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.asserts.NotYetImplemented;
import org.meta_environment.rascal.interpreter.env.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.SymbolAdapter;
import org.meta_environment.uptr.TreeAdapter;

class ConcreteListPattern extends AbstractPattern {
	private ListPattern pat;
	private CallOrTree callOrTree;

	public ConcreteListPattern(IValueFactory vf,
			EvaluatorContext ctx, CallOrTree x, List<MatchPattern> list) {
		super(vf, ctx);
		callOrTree = x;
		initListPatternDelegate(vf, ctx, list);
	}

	private void initListPatternDelegate(IValueFactory vf,
			EvaluatorContext ctx, List<MatchPattern> list) {
		Type type = getType(null);
		
		if (type instanceof ConcreteSyntaxType) {
			IConstructor sym = ((ConcreteSyntaxType) type).getSymbol();
			SymbolAdapter rhs = new SymbolAdapter(sym);

			if (rhs.isCf()) {	
				SymbolAdapter cfSym = rhs.getSymbol();
				if (cfSym.isIterPlus() || cfSym.isIterStar()) {
					pat = new ListPattern(vf, ctx, list, 2);
				}
				else if (cfSym.isIterPlusSep() || cfSym.isIterStarSep()) {
					pat = new ListPattern(vf, ctx, list, 4);
				}
			}
			else if (rhs.isLex()){
				SymbolAdapter lexSym = rhs.getSymbol();
				if (lexSym.isIterPlus() || lexSym.isIterStar()) {
					pat = new ListPattern(vf, ctx, list, 1);
				}
				else if (lexSym.isIterPlusSep() || lexSym.isIterStarSep()) {
					pat = new ListPattern(vf, ctx, list, 2);
				}
			}
			else {
				throw new ImplementationError("crooked production: non (cf or lex) list symbol: " + rhs);
			}
			return;
		}
		throw new ImplementationError("should not get here if we don't know that its a proper list");
	}

	@Override
	public void initMatch(IValue subject, Environment env) {
		super.initMatch(subject, env);
		if (subject.getType() != Factory.Tree) {
			hasNext = false;
			return;
		}
		TreeAdapter tree = new TreeAdapter((IConstructor) subject);
// Disabled because singletons should matched against list 
// variables: [|d <D+ Xs>|] := [|d d|];
// The variable will be a AbstractPatternList...
//		if (!tree.isCFList()) {
//			hasNext = false;
//			return;
//		}
//		if (!tree.getProduction().tree.isEqual(prod)) {
//			hasNext = false;
//			return;
//		}
		pat.initMatch(tree.getArgs(), env);
		hasNext = true;
	}
	
	@Override
	public Type getType(Environment env) {
		CallOrTree prod = (CallOrTree) callOrTree.getArguments().get(0);
		CallOrTree rhs = (CallOrTree) prod.getArguments().get(0);
		
		try {
			return new ConcreteSyntaxType(rhs.accept(new IUPTRAstToSymbolConstructor(vf)));
		}
		catch (NonGroundSymbolException e) {
			return Factory.Tree;
		}
	}

	@Override
	public boolean hasNext() {
		if (!hasNext) {
			return false;
		}
		return pat.hasNext();
	}
	
	@Override
	public boolean next() {
		if (!hasNext()) {
			return false;
		}
		return pat.next();
		
	}

	@Override
	public IValue toIValue(Environment env) {
		throw new NotYetImplemented("is this dead?");
	}
	
	@Override
	public java.util.List<String> getVariables() {
		return pat.getVariables();
	}
}