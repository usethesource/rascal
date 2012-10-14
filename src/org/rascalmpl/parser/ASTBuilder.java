/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.rascalmpl.ast.ASTStatistics;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Commands;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Header;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.utils.Symbols;
import org.rascalmpl.parser.gtd.util.PointerKeyedHashMap;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse node of a rascal program.
 */
public class ASTBuilder {
	private static final String RASCAL_SORT_PREFIX = "_";
	private static final String MODULE_SORT = "Module";
	private static final String PRE_MODULE_SORT = "PreModule";

	// this tree should never appear in "nature", so we can use it as a dummy
    private static Expression dummyEmptyTree;
    
    private final PointerKeyedHashMap<IConstructor, AbstractAST> ambCache = new PointerKeyedHashMap<IConstructor, AbstractAST>();
    private final PointerKeyedHashMap<IConstructor, AbstractAST> sortCache = new PointerKeyedHashMap<IConstructor, AbstractAST>();
    private final PointerKeyedHashMap<IConstructor, AbstractAST> lexCache = new PointerKeyedHashMap<IConstructor, AbstractAST>();
    
    private final PointerKeyedHashMap<IValue, Expression> constructorCache = new PointerKeyedHashMap<IValue, Expression>();
    private ISourceLocation lastSuccess = null;
    
    private final static HashMap<String, Constructor<?>> astConstructors = new HashMap<String,Constructor<?>>();
	private final static ClassLoader classLoader = ASTBuilder.class.getClassLoader();
    
	public ASTBuilder(){
		super();
		
		if(dummyEmptyTree == null){
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			dummyEmptyTree = makeAmb("Expression", vf.sourceLocation("/dev/null"), Collections.<Expression>emptyList());
		}
	}
	
	public static <T extends AbstractAST> T make(String sort, ISourceLocation src, Object... args) {
		return make(sort, "Default", src, args);
	}
	
	public static  <T extends Expression> T makeExp(String cons, ISourceLocation src, Object... args) {
		return make("Expression", cons, src, args);
	}
	
	public static <T extends Statement> T makeStat(String cons, ISourceLocation src, Object... args) {
		return make("Statement", cons, src, args);
	}
	
	public static <T extends AbstractAST> T makeAmb(String sort, ISourceLocation src, Object... args) {
		return make(sort, "Ambiguity", src, args);
	}
	
	public static <T extends AbstractAST> T makeLex(String sort, ISourceLocation src, Object... args) {
		return make(sort, "Lexical", src, args);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractAST> T make(String sort, String cons, ISourceLocation src, Object... args) {
		Object[] newArgs = new Object[args.length + 1];
		System.arraycopy(args, 0, newArgs, 1, args.length);
		return (T) callMakerMethod(sort, cons, src, newArgs);
	}

	public ISourceLocation getLastSuccessLocation() {
		return lastSuccess;
	}
	
	public Module buildModule(IConstructor parseTree) throws FactTypeUseException {
		IConstructor tree =  parseTree;
		
		if (TreeAdapter.isAppl(tree)) {
			if (sortName(tree).equals(MODULE_SORT)) {
				// t must be an appl so call buildValue directly
				return (Module) buildValue(tree);
			}
			else if (sortName(tree).equals(PRE_MODULE_SORT)) {
				// TODO temporary solution while bootstrapping (if we regenerate the ast hierarchy this can be solved more elegantly)
				IList moduleArgs = (IList) tree.get(1);
				IConstructor headerTree = (IConstructor) moduleArgs.get(0);
				Header header = (Header) buildValue(headerTree);
				return make("Module", TreeAdapter.getLocation(tree), header, make("Body","Toplevels", (ISourceLocation) ((IConstructor) moduleArgs.get(2)).getAnnotation("loc"), Collections.<Toplevel>emptyList())); 
			}
			return buildSort(parseTree, MODULE_SORT);
		}
		if (TreeAdapter.isAmb(tree)) {
			ISet alts = TreeAdapter.getAlternatives(tree);
			for (IValue val: alts) {
				IConstructor t = (IConstructor) TreeAdapter.getArgs((IConstructor)val).get(1);
				// This *prefers* the first Rascal Module it encounters in the set of alts.
				// So if the Rascal syntax for modules itself would be ambiguous
				// you get just one of them (unknown which).
				if (sortName((IConstructor) val).equals(MODULE_SORT)) {
					// t must be an appl so call buildValue directly
					return (Module) buildValue(t);
				}
				else if (sortName((IConstructor) val).equals(PRE_MODULE_SORT)) {
					throw new Ambiguous(parseTree);
				}
			}
		}
		throw new ImplementationError("Parse of module returned invalid tree.");
	}
	
	public Expression buildExpression(IConstructor parseTree) {
		return buildSort(parseTree, "Expression");
	}
	
	public Statement buildStatement(IConstructor parseTree) {
		return buildSort(parseTree, "Statement");
	}
	
	public Command buildCommand(IConstructor parseTree) {
		return buildSort(parseTree, "Command");
	}
	
	public Commands buildCommands(IConstructor parseTree) {
		return buildSort(parseTree, "Commands");
	}
	
	@SuppressWarnings("unchecked")
	private <T extends AbstractAST> T buildSort(IConstructor parseTree, String sort) {
		if (TreeAdapter.isAppl(parseTree)) {
			IConstructor tree = (IConstructor) TreeAdapter.getArgs(parseTree).get(1);
			
			if (TreeAdapter.isAmb(tree)) {
				throw new Ambiguous(tree);
			}
			
			if (sortName(tree).equals(sort)) {
				return (T) buildValue(tree);
			}
		} else if (TreeAdapter.isAmb(parseTree)) {
			for (IValue alt : TreeAdapter.getAlternatives(parseTree)) {
				IConstructor tree = (IConstructor) alt;

				if (sortName(tree).equals(sort)) {
					AbstractAST value = buildValue(tree);
					if (value != null) {
						return (T) value;
					}
				}
			}
			throw new SyntaxError(sort, TreeAdapter.getLocation(parseTree)); // TODO Always @ offset = 0?
		}
		
		throw new ImplementationError("This is not a " + sort +  ": " + parseTree);
	}
	
	public AbstractAST buildValue(IValue arg)  {
		IConstructor tree = (IConstructor) arg;
		
		if (TreeAdapter.isList(tree)) {
			throw new ImplementationError("buildValue should not be called on a list");
		}
		
		if (TreeAdapter.isAmb(tree)) {
			return filter(tree);
		}
		
		if (!TreeAdapter.isAppl(tree)) {
			throw new UnsupportedOperationException();
		}	
		
		if (isLexical(tree)) {
			if (TreeAdapter.isRascalLexical(tree)) {
				return buildLexicalNode(tree);
			}
			return buildLexicalNode((IConstructor) ((IList) ((IConstructor) arg).get("args")).get(0));
		}
		
		if (sortName(tree).equals("Pattern") && isEmbedding(tree)) {
			return lift(tree, true);
		}

		if (sortName(tree).equals("Expression")) {
			if (isEmbedding(tree)) {
				return lift(tree, false);
			}
		}
		
		return buildContextFreeNode((IConstructor) arg);
	}

	private List<AbstractAST> buildList(IConstructor in)  {
		IList args = TreeAdapter.getListASTArgs(in);
		List<AbstractAST> result = new ArrayList<AbstractAST>(args.length());
		for (IValue arg: args) {
			IConstructor tree = (IConstructor) arg;

			if (TreeAdapter.isAmbiguousList(tree)) {
				// unflattened list due to nested ambiguity
				List<AbstractAST> elems = filterList(tree);
				
				if (elems != null) {
					result.addAll(elems);
				}
				else {
					return null;
				}
			}
			else {
				AbstractAST elem = buildValue(arg);

				if (elem == null) {
					return null; // filtered
				}
				result.add(elem);
			}
		}
		return result;
	}

	private AbstractAST buildContextFreeNode(IConstructor tree)  {
		AbstractAST cached = sortCache.get(tree);
		
		if (cached != null) {
			return cached;
		}
		
		String constructorName = TreeAdapter.getConstructorName(tree);
		if (constructorName == null) {
			throw new ImplementationError("All Rascal productions should have a constructor name: " + TreeAdapter.getProduction(tree));
		}
		
		String cons = capitalize(constructorName);
		String sort = sortName(tree);
		
		if (sort.length() == 0) {
			throw new ImplementationError("Could not retrieve sort name for " + tree);
		}
		sort = sort.equalsIgnoreCase("pattern") ? "Expression" : capitalize(sort); 
		
		if (sort.equals("Mapping")) {
			sort = "Mapping_Expression";
		}

		IList args = getASTArgs(tree);
		int arity = args.length();
		Object actuals[] = new Object[arity+1];
		actuals[0] = tree;
		ASTStatistics total = new ASTStatistics();

		int i = 1;
		for (IValue arg : args) {
			IConstructor argTree = (IConstructor) arg;

			if (TreeAdapter.isList(argTree)) {
				actuals[i] = buildList((IConstructor) arg);

				if (actuals[i] == null) { // filtered
					return null;
				}

				for (Object ast : ((java.util.List<?>) actuals[i])) {
					total.add(((AbstractAST) ast).getStats());
				}
			}
			else if (TreeAdapter.isAmbiguousList(argTree)) {
				actuals[i] = filterList(argTree);

				if (actuals[i] == null) { // filtered
					return null;
				}

				for (Object ast : ((java.util.List<?>) actuals[i])) {
					ASTStatistics stats = ((AbstractAST) ast).getStats();
					total.add(stats);
				}
			}
			else {
				actuals[i] = buildValue(arg);
				if (actuals[i] == null) { // filtered
					return null;
				}

				ASTStatistics stats = ((AbstractAST) actuals[i]).getStats();
				total.add(stats);
			}
			i++;
		}

		AbstractAST ast = callMakerMethod(sort, cons, tree.getAnnotations(), actuals);
		
		// TODO: This is a horrible hack. The pattern Statement s : `whatever` should
		// be a concrete syntax pattern, but is not recognized as such because of the
		// Statement s in front (the "concrete-ness" is nested inside). This propagates
		// the pattern type up to this level. It would be good to find a more principled
		// way to do this.
		if (ast instanceof org.rascalmpl.ast.Expression.TypedVariableBecomes || ast instanceof org.rascalmpl.ast.Expression.VariableBecomes) {
			org.rascalmpl.ast.Expression astExp = (org.rascalmpl.ast.Expression)ast;
			if (astExp.hasPattern() && astExp.getPattern()._getType() != null) {
				astExp._setType(astExp.getPattern()._getType());
			}
		}
		
		ast.setStats(total);
		sortCache.putUnsafe(tree, ast);
		lastSuccess = ast.getLocation();
		return ast;
	}
	
	private AbstractAST buildLexicalNode(IConstructor tree) {
		AbstractAST cached = lexCache.get(tree);
		if (cached != null) {
			return cached;
		}
		String sort = capitalize(sortName(tree));

		if (sort.length() == 0) {
			throw new ImplementationError("could not retrieve sort name for " + tree);
		}
		Object actuals[] = new Object[] { tree, new String(TreeAdapter.yield(tree)) };

		AbstractAST result = callMakerMethod(sort, "Lexical", tree.getAnnotations(), actuals);
		lexCache.putUnsafe(tree, result);
		return result;
	}
	
	private AbstractAST filter(IConstructor tree) {
		AbstractAST cached = ambCache.get(tree);
		if (cached != null) {
			return cached;
		}
		ISet altsIn = TreeAdapter.getAlternatives(tree);
		java.util.List<AbstractAST> altsOut = new ArrayList<AbstractAST>(altsIn.size());
		String sort = "";
		ASTStatistics ref = null;
		Ambiguous lastCaughtACP = null;
		
		for (IValue alt : altsIn) {
			sort = sortName((IConstructor) alt);
			AbstractAST ast = null;
			try {
				ast = buildValue(alt);
			} catch (Ambiguous acp) {
				lastCaughtACP = acp;
			}
			
			if (ast == null) {
				continue;
			}
			
			if (ref == null) {
				ref = ast.getStats();
				altsOut.add(ast);
			}
			else {
				ref = filter(altsOut, ast, ref);
			}
		}
		
		if (altsOut.size() == 0) {
			if (null != lastCaughtACP) {
				throw lastCaughtACP;
			}
			return null; // this could happen in case of nested ambiguity
//			throw new SyntaxError("concrete syntax pattern", tree.getLocation());
		}
		
		if (altsOut.size() == 1) {
			return altsOut.iterator().next();
		}

		// Concrete syntax is lifted to Expression
		sort = sort.equalsIgnoreCase("pattern") ? "Expression" : capitalize(sort); 

		Object actuals[] = new Object[] {  tree, altsOut };

		AbstractAST ast = callMakerMethod(sort, "Ambiguity", tree.getAnnotations(), actuals);
		
		ast.setStats(ref != null ? ref : new ASTStatistics());
		
		ambCache.putUnsafe(tree, ast);
		return ast;
	}

	private <T extends AbstractAST> ASTStatistics filter(java.util.List<T> altsOut, T ast, ASTStatistics ref) {
		ASTStatistics stats = ast.getStats();
		return filter(altsOut, ast, ref, stats);
	}

	private <T> ASTStatistics filter(java.util.List<T> altsOut, T ast, ASTStatistics ref, ASTStatistics stats) {
		switch(ref.compareTo(stats)) {
		case 1:
			ref = stats;
			altsOut.clear();
			altsOut.add(ast);
			break;
		case 0:
			altsOut.add(ast);
			break;
		case -1:
			// do nothing
		}
		return ref;
	}

	private List<AbstractAST> filterList(IConstructor argTree) {
		ISet alts = TreeAdapter.getAlternatives(argTree);
		ASTStatistics ref = new ASTStatistics();
		List<List<AbstractAST>> result = new ArrayList<List<AbstractAST>>(/* size unknown */);
	
		for (IValue alt : alts) {
			List<AbstractAST> list = buildList((IConstructor) alt);
			
			if (list == null) {
				continue;
			}
			
			ASTStatistics listStats = new ASTStatistics();
			
			for (AbstractAST ast : list) {
				ASTStatistics stats = ast.getStats();
				listStats.add(stats);
			}
			
			if (ref == null) {
				ref = listStats;
				result.add(list);
			}
			else {
				ref = filter(result, list, ref, listStats);
			}
		}
		
		switch(result.size()) {
		case 1:
			return result.get(0);
		case 0: 
			return null;
//			throw new ImplementationError("Accidentally all ambiguous derivations of a list have been filtered", argTree.getLocation());
		default:
			throw new Ambiguous(argTree);
		}
	}

	/**
	 * Removes patterns like <PROGRAM p> where the <...> hole is not nested in a place
	 * where a PROGRAM is expected. Also, patterns that directly nest concrete syntax patterns
	 * again, like `<`...`>` are filtered.
	 */
	private Expression filterNestedPattern(IConstructor antiQuote, IConstructor pattern, boolean lexicalFather, String layout) {
		ISet alternatives = TreeAdapter.getAlternatives(pattern);
		List<Expression> result = new ArrayList<Expression>(alternatives.size());
		 
		IConstructor expected = TreeAdapter.getType(antiQuote);

		// any alternative that is a typed variable must be parsed using a 
		// MetaVariable that produced exactly the same type as is declared inside
		// the < > brackets.
		for (IValue alt : alternatives) {
			if (isEmbedding((IConstructor) alt)) {
				continue; // filter direct nesting
			}
			
			Expression exp = (Expression) buildValue(alt);
		
			if (exp != null && correctlyNestedPattern(expected, exp, lexicalFather, layout)) {
				result.add(exp);
			}
		}
		
		if (result.size() == 1) {
			return result.get(0);
		}
		
		if (result.size() == 0) {
			return null;
		}
		
		return makeAmb("Expression", TreeAdapter.getLocation(antiQuote), result);
	}

	private AbstractAST lift(IConstructor tree, boolean match) {
		AbstractAST cached = constructorCache.get(tree);
		if (cached != null) {
			if (cached == dummyEmptyTree) {
				return null;
			}
			return cached;
		}
		
		if (TreeAdapter.isEpsilon(tree)) {
			constructorCache.putUnsafe(tree, dummyEmptyTree);
			return null;
		}
		
		IConstructor pattern = getConcretePattern(tree);
		Expression ast = liftRec(pattern, false,  getPatternLayout(tree));
		
		if (ast != null) {
			ASTStatistics stats = ast.getStats();
			stats.setConcreteFragmentCount(1);
			ISourceLocation location = TreeAdapter.getLocation(pattern);
			stats.setConcreteFragmentSize(location.getLength());
			
//			if (stats.isAmbiguous()) {
//				throw new Ambiguous(ast.getTree());
//			}
		}
		
		constructorCache.putUnsafe(tree, ast);
		return ast;
	}

	private String getPatternLayout(IConstructor tree) {
		IConstructor prod = TreeAdapter.getProduction(tree);
		String cons = ProductionAdapter.getConstructorName(prod);
		
		IList symbols = ProductionAdapter.getSymbols(prod);
		
		if (cons.equals("ConcreteQuoted")) {
			IConstructor sym = (IConstructor) symbols.get(1);
			if (!SymbolAdapter.isLayouts(sym)) {
				throw new ImplementationError("?? expected layout symbol but got " + sym);
			}
			return SymbolAdapter.getName(sym);
		}
		
		if (cons.equals("ConcreteTypedQuoted")) {
			// the type name is an arbitrary length list of literals, so we better start from the end
			IConstructor sym = (IConstructor) symbols.get(symbols.length() - 2);
			if (!SymbolAdapter.isLayouts(sym)) {
				throw new ImplementationError("?? expected layout symbol but got " + sym);
			}
			
			return SymbolAdapter.getName(sym);
		}
		
		throw new ImplementationError("Unexpected embedding syntax:" + prod);
	}

	private Expression stats(IConstructor in, Expression out, ASTStatistics a) {
		constructorCache.putUnsafe(in, out);
		if (out != null) {
			out.setStats(a);
		}
		return out;
	}
	
	private Expression liftRec(IConstructor tree, boolean lexicalFather, String layoutOfFather) {
		Expression cached = constructorCache.get(tree);
		if (cached != null) {
			return cached;
		}
		
		if (layoutOfFather == null) {
			throw new ImplementationError("layout is null");
		}
		
		ASTStatistics stats = new ASTStatistics();
		Expression result;
		
		if (TreeAdapter.isAppl(tree)) {
			String cons = TreeAdapter.getConstructorName(tree);
			
			if (cons != null && (cons.equals("MetaVariable") || cons.equals("TypedMetaVariable"))) {
				
				result = liftVariable(tree, lexicalFather, layoutOfFather);
				stats.setNestedMetaVariables(1);
				return stats(tree, result, stats);
			}

			boolean lex = lexicalFather ? !TreeAdapter.isSort(tree) : TreeAdapter.isLexical(tree);
			boolean inj = TreeAdapter.isInjectionOrSingleton(tree);
			boolean star = TreeAdapter.isNonEmptyStarList(tree);
			
			if (!lex || inj) {
				stats.setInjections(1);
			}
			else if (star) {
				stats.setInjections(1);
			}
			else {
				stats.setInjections(0);
			}
				
			
			IList args = TreeAdapter.getArgs(tree);
			String layout = layoutOfFather;
			
			if (cons != null && !lex) { 
				String newLayout = getLayoutName(TreeAdapter.getProduction(tree));
				
				// this approximation is possibly harmfull. Perhaps there is a chain rule defined in another module, which nevertheless
				// switched the applicable layout. Until we have a proper type-analysis there is nothing we can do here.
				if (newLayout != null) {
					layout = newLayout;
				}
			}
			
			java.util.List<Expression> kids = new ArrayList<Expression>(args.length());
			for (IValue arg : args) {
				Expression ast = liftRec((IConstructor) arg, lex, layout);
				if (ast == null) {
					return null;
				}
				kids.add(ast);
				stats.add(ast.getStats());
			}

			if (TreeAdapter.isLexical(tree)) {
				return stats(tree, new Tree.Lexical(tree, kids), stats);
			}
			else if (TreeAdapter.isList(tree)) {
				// TODO: splice element lists (can happen in case of ambiguous lists)
				return stats(tree, new Tree.List(tree, kids), stats);
			}
			else if (TreeAdapter.isOpt(tree)) {
				return stats(tree, new Tree.Optional(tree, kids), stats);
			}
			else { 
				return stats(tree, new Tree.Appl(tree, kids), stats);
			}
		}
		else if (TreeAdapter.isCycle(tree)) {
			return new Tree.Cycle(TreeAdapter.getCycleType(tree), TreeAdapter.getCycleLength(tree));
		}
		else if (TreeAdapter.isAmb(tree)) {
			ISet args = TreeAdapter.getAlternatives(tree);
			java.util.List<Expression> kids = new ArrayList<Expression>(args.size());
			
			ASTStatistics ref = null;
			
			for (IValue arg : args) {
				Expression ast = liftRec((IConstructor) arg, lexicalFather, layoutOfFather);
				if (ast != null) {
					if (ref == null) {
						ref = ast.getStats();
						kids.add(ast);
					}
					else {
						ref = filter(kids, ast, ref);
					}
				}
			}
			
			if (kids.size() == 0) {
				return null;
			}
			
			stats = ref != null ? ref : new ASTStatistics();
			if (kids.size() == 1) {
				return kids.get(0);
			}
			
			stats.setAmbiguous(true);
			return stats(tree, new Tree.Amb(tree, kids), stats);
		}
		else {
			if (!TreeAdapter.isChar(tree)) {
				throw new ImplementationError("unexpected tree type: " + tree);
			}
			return stats(tree, new Tree.Char(tree), new ASTStatistics()); 
		}
	}

	// TODO: this is the new one to construct variables, which we'll use when the meta syntax for variables has been simplified
	private Expression liftVariable(IConstructor tree) {
		IConstructor type = TreeAdapter.getType(tree);
		IList args = TreeAdapter.getArgs(tree);
		IConstructor nameTree = (IConstructor) args.get(args.length() - 3);
		ISourceLocation src = TreeAdapter.getLocation(tree);
		Expression result = new Tree.MetaVariable(tree, type, TreeAdapter.yield(nameTree));
		result.setSourceLocation(src);
		return result;
	}
	
	private String getLayoutName(IConstructor production) {
		if (ProductionAdapter.isDefault(production)) {
			for (IValue sym : ProductionAdapter.getSymbols(production)) {
				if (SymbolAdapter.isLayouts(SymbolAdapter.delabel((IConstructor) sym))) {
					return SymbolAdapter.getName((IConstructor) sym);
				}
			}
		}
		
		return null;
	}

	private Expression liftVariable(IConstructor tree, boolean lexicalFather, String layout) {
		String cons = TreeAdapter.getConstructorName(tree);
		
		if (cons.equals("MetaVariable")) {
			IConstructor arg = (IConstructor) getASTArgs(tree).get(0);
			
			if (arg.getConstructorType() == Factory.Tree_Amb) {
				return filterNestedPattern(tree, arg, lexicalFather, layout); 
			}
			Expression result = (Expression) buildValue(arg);
		
			if (result != null && correctlyNestedPattern(TreeAdapter.getType(tree), result, lexicalFather, layout)) {
				return result;
			}
			return null;
		}
		throw new ImplementationError("Unexpected meta variable while lifting pattern");
	}


	private boolean correctlyNestedPattern(IConstructor expected, Expression exp, boolean lexicalFather, String layout) {
		if (exp.isTypedVariable()) {
			IConstructor expressionType = Symbols.typeToSymbol(exp.getType(), lexicalFather, layout);

			
			// the declared type inside the pattern must match the produced type outside the brackets
			// "<" Pattern ">" -> STAT in the grammar and "<STAT t>" in the pattern. STAT == STAT.
			if (SymbolAdapter.isEqual(expressionType, expected)) {
				return true;
			}
			
			if (SymbolAdapter.isAnyList(expressionType) || SymbolAdapter.isOpt(expressionType)) {
				IConstructor elem = SymbolAdapter.getSymbol(expressionType);
				return SymbolAdapter.isEqual(elem, expected);
			}
			
			return false;
		}else if (exp.isAsType()) {
			IConstructor expressionType = Symbols.typeToSymbol(exp.getType(), lexicalFather, layout);

			// the declared type inside the pattern must match the produced type outside the brackets
			// "<" [Type] Pattern ">" -> STAT in the grammar and "<[STAT] pattern>" in the pattern. STAT == STAT.
			return (SymbolAdapter.isEqual(expressionType, expected));
		}
		
		return true;
	}

	private IConstructor getConcretePattern(IConstructor tree) {
		IList args = TreeAdapter.getArgs(tree);
		return  (IConstructor) args.get(args.length() - 3);
	}

	private IList getASTArgs(IConstructor tree) {
		IList children = TreeAdapter.getArgs(tree);
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory.getValueFactory());
	
		for (int i = 0; i < children.length(); i++) {
			IConstructor kid = (IConstructor) children.get(i);
			if (!TreeAdapter.isLiteral(kid) && !TreeAdapter.isCILiteral(kid) && !isRascalLiteral(kid) && !TreeAdapter.isEmpty(kid)) {
				writer.append(kid);	
			} 
			// skip layout
			i++;
		}
		
		return writer.done();
	}

	private String sortName(IConstructor tree) {
		if (TreeAdapter.isAppl(tree)) {
			String sortName = TreeAdapter.getSortName(tree);
			
			if (isRascalSort(sortName)) {
				sortName = sortName.substring(1);
			}
			
			return sortName;
		}
		if (TreeAdapter.isAmb(tree)) {
			// all alternatives in an amb cluster have the same sort
			return sortName((IConstructor) TreeAdapter.getAlternatives(tree).iterator().next());
		}
		return "";
	}

	private String capitalize(String sort) {
		if (sort.length() == 0) {
			return sort;
		}
		if (sort.length() > 1) {
			return Character.toUpperCase(sort.charAt(0)) + sort.substring(1);
		}
		
		return sort.toUpperCase();
	}

	private static ImplementationError unexpectedError(Throwable e) {
		return new ImplementationError("Unexpected error in AST construction: " + e, e);
	}

	private boolean isEmbedding(IConstructor tree) {
		String name = TreeAdapter.getConstructorName(tree);
		return name.equals("ConcreteQuoted") 
//		|| name.equals("ConcreteUnquoted") 
		|| name.equals("ConcreteTypedQuoted");
	}

	private boolean isLexical(IConstructor tree) {
		if (TreeAdapter.isRascalLexical(tree)) {
			return true;
		}
		return false;
	}

	private boolean isRascalLiteral(IConstructor tree) {
		if (TreeAdapter.isAppl(tree)) {
			IConstructor prod = TreeAdapter.getProduction(tree);
			IConstructor rhs = ProductionAdapter.getType(prod);
			
			if (SymbolAdapter.isParameterizedSort(rhs) && SymbolAdapter.getName(rhs).equals("_WrappedLiteral")) {
				return true;
			}
		}
		return false;
	}

	private boolean isRascalSort(String sort) {
		return sort.startsWith(RASCAL_SORT_PREFIX);
	}

	private static AbstractAST callMakerMethod(String sort, String cons, Map<String, IValue> annotations, Object actuals[]) {
		return callMakerMethod(sort, cons, TreeAdapter.getLocation((IConstructor) actuals[0]), annotations, actuals);
	}
	
	/**
	 * @deprecated Does not propagate <code>attributes</code>. 
	 *             Use the following instead:
	 *             {@link ASTBuilder#callMakerMethod(String, String, ISourceLocation, ISet, Object[]).
	 */
	@Deprecated
	private static AbstractAST callMakerMethod(String sort, String cons, ISourceLocation src, Object actuals[]) {
		return callMakerMethod(sort, cons, src, null, actuals);
	}
	
	private static AbstractAST callMakerMethod(String sort, String cons, ISourceLocation src, Map<String, IValue> annotations, Object actuals[]) {
		try {
			String name = sort + '$' + cons;
			Constructor<?> constructor = astConstructors.get(name);
			
			if (constructor == null) {
				Class<?> clazz = null;
				
				try {
					clazz = classLoader.loadClass("org.rascalmpl.semantics.dynamic." + name);
				}
				catch (ClassNotFoundException e) {
					// it happens
				}
				
				if (clazz == null) {
					clazz = classLoader.loadClass("org.rascalmpl.ast." + name);
				}
				
				constructor = clazz.getConstructors()[0];
				constructor.setAccessible(true);
				astConstructors.put(name, constructor);
			}

			AbstractAST result = (AbstractAST) constructor.newInstance(actuals);
			if (src != null) {
				result.setSourceLocation(src);
			}
			if (annotations != null && !annotations.isEmpty()) {
				result.setAnnotations(annotations);
			}
			return result;
		} catch (SecurityException e) {
			throw unexpectedError(e);
		} catch (IllegalArgumentException e) {
			throw unexpectedError(e);
		} catch (IllegalAccessException e) {
			throw unexpectedError(e);
		} catch (InvocationTargetException e) {
			throw unexpectedError(e);
		} catch (ClassNotFoundException e) {
			throw unexpectedError(e);
		} catch (InstantiationException e) {
			throw unexpectedError(e);
		}
	}
		
}
