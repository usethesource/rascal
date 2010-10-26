package org.rascalmpl.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.ASTFactory;
import org.rascalmpl.ast.ASTStatistics;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Header;
import org.rascalmpl.ast.LanguageAction;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.Symbols;
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
	private ASTFactory factory;
    private Class<? extends ASTFactory> clazz;
    
    private final Expression dummyEmptyTree;
    
    private PointerEqualMappingsCache<INode, AbstractAST> ambCache = new PointerEqualMappingsCache<INode, AbstractAST>();
    private PointerEqualMappingsCache<INode, AbstractAST> sortCache = new PointerEqualMappingsCache<INode, AbstractAST>();
    private PointerEqualMappingsCache<INode, AbstractAST> lexCache = new PointerEqualMappingsCache<INode, AbstractAST>();
    
    private PointerEqualMappingsCache<IValue, Expression> matchCache = new PointerEqualMappingsCache<IValue, Expression>();
    private PointerEqualMappingsCache<IValue, Expression> constructorCache = new PointerEqualMappingsCache<IValue, Expression>();
    private ISourceLocation lastSuccess = null;
    
	public ASTBuilder(ASTFactory factory) {
		this.factory = factory;
		this.clazz = factory.getClass();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		
		// this tree should never appear in "nature", so we can use it as a dummy
		this.dummyEmptyTree = factory.makeExpressionAmbiguity(
				(INode) Factory.Tree_Amb.make(vf, vf.list()), 
				Collections.<Expression>emptyList());
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
				return factory.makeModuleDefault(tree, header, factory.makeBodyToplevels((INode) moduleArgs.get(2), Collections.<Toplevel>emptyList())); 
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
				if (sortName(t).equals(MODULE_SORT)) {
					// t must be an appl so call buildValue directly
					return (Module) buildValue(t);
				}
				else if (sortName(t).equals(PRE_MODULE_SORT)) {
					// TODO temporary solution while bootstrapping (if we regenerate the ast hierarchy this can be solved more elegantly)
					throw new Ambiguous(parseTree);
//					IList startArgs = (IList) ((IConstructor) val).get(1);
//					IConstructor moduleTop = (IConstructor) startArgs.get(1);
//					IList moduleArgs = (IList) moduleTop.get(1);
//					IConstructor headerTree = (IConstructor) moduleArgs.get(0);
//					Header header = (Header) buildValue(headerTree);
//					return new Module.Default(tree, header, 
//							new Body.Toplevels((INode) moduleArgs.get(2), 
//									Collections.<Toplevel>emptyList()));
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
	
	public LanguageAction buildAction(IConstructor parseTree) {
		return buildSort(parseTree, "LanguageAction");
	}
	
	public Command buildCommand(IConstructor parseTree) {
		return buildSort(parseTree, "Command");
	}
	
	@SuppressWarnings("unchecked")
	private <T extends AbstractAST> T buildSort(IConstructor parseTree, String sort) {
		IConstructor top = parseTree;
		
		if (TreeAdapter.isAppl(top)) {
			IConstructor tree = (IConstructor) TreeAdapter.getArgs(top).get(1);
			
			if (sortName(tree).equals(sort)) {
				return (T) buildValue(tree);
			}
		}
		else if (TreeAdapter.isAmb(top)) {
			for (IValue alt : TreeAdapter.getAlternatives(top)) {
				IConstructor tree = (IConstructor) alt;

				if (sortName(tree).equals(sort)) {
					AbstractAST value = buildValue(tree);
					if (value != null) {
						return (T) value;
					}
				}
			}
			throw new SyntaxError(sort, TreeAdapter.getLocation(top)); // TODO Always @ offset = 0?
		}
		
		throw new ImplementationError("This is not a " + sort +  ": " + top);
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
			throw new ImplementationError("All Rascal productions should have a constructor name: " + ProductionAdapter.getTree(TreeAdapter.getProduction(tree)));
		}
		
		String cons = capitalize(constructorName);
		String sort = sortName(tree);
		
		if (sort.length() == 0) {
			throw new ImplementationError("Could not retrieve sort name for " + tree);
		}
		sort = sort.equalsIgnoreCase("pattern") ? "Expression" : capitalize(sort); 

		IList args = getASTArgs(tree);
		int arity = args.length() + 1;
		Class<?> formals[] = new Class<?>[arity];
		Object actuals[] = new Object[arity];

		formals[0] = INode.class;
		actuals[0] = tree;

		ASTStatistics total = new ASTStatistics();

		int i = 1;
		for (IValue arg : args) {
			IConstructor argTree = (IConstructor) arg;

			if (TreeAdapter.isList(argTree)) {
				actuals[i] = buildList((IConstructor) arg);
				formals[i] = List.class;

				if (actuals[i] == null) { // filtered
					return null;
				}

				for (Object ast : ((java.util.List<?>) actuals[i])) {
					total.add(((AbstractAST) ast).getStats());
				}
			}
			else if (TreeAdapter.isAmbiguousList(argTree)) {
				actuals[i] = filterList(argTree);
				formals[i] = List.class;

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
				formals[i] = actuals[i].getClass().getSuperclass();


				ASTStatistics stats = ((AbstractAST) actuals[i]).getStats();
				total.add(stats);
			}
			i++;
		}

		AbstractAST ast = callMakerMethod(sort, cons, formals, actuals);
		
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
		Class<?> formals[] = new Class<?>[] { INode.class, String.class };
		Object actuals[] = new Object[] { tree, new String(TreeAdapter.yield(tree)) };

		AbstractAST result = callMakerMethod(sort, "Lexical", formals, actuals);
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

		Class<?> formals[] = new Class<?>[]  { INode.class, List.class };
		Object actuals[] = new Object[] { tree, altsOut };

		AbstractAST ast = callMakerMethod(sort, "Ambiguity", formals, actuals);
		
		ast.setStats(ref != null ? ref : new ASTStatistics());
		
		ambCache.putUnsafe(tree, ast);
		return ast;
	}

	private <T extends AbstractAST> ASTStatistics filter(java.util.List<T> altsOut,
			T ast, ASTStatistics ref) {
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
	private Expression filterNestedPattern(IConstructor antiQuote, IConstructor pattern) {
		ISet alternatives = TreeAdapter.getAlternatives(pattern);
		List<Expression> result = new ArrayList<Expression>(alternatives.size());
		 
		IConstructor expected = ProductionAdapter.getRhs(TreeAdapter.getProduction(antiQuote));

		// any alternative that is a typed variable must be parsed using a 
		// MetaVariable that produced exactly the same type as is declared inside
		// the < > brackets.
		for (IValue alt : alternatives) {
			if (isEmbedding((IConstructor) alt)) {
				continue; // filter direct nesting
			}
			
			Expression exp = (Expression) buildValue(alt);
		
			if (exp != null && correctlyNestedPattern(expected, exp)) {
				result.add(exp);
			}
		}
		
		if (result.size() == 1) {
			return result.get(0);
		}
		
		if (result.size() == 0) {
			return null;
		}
		
		return factory.makeExpressionAmbiguity(antiQuote, result);
	}

	private AbstractAST lift(IConstructor tree, boolean match) {
		AbstractAST cached = (match ? matchCache : constructorCache).get(tree);
		if (cached != null) {
			if (cached == dummyEmptyTree) {
				return null;
			}
			return cached;
		}
		
		
		if (TreeAdapter.isEpsilon(tree)) {
			matchCache.putUnsafe(tree, dummyEmptyTree);
			constructorCache.putUnsafe(tree, dummyEmptyTree);
			return null;
		}
		
		IConstructor pattern = getConcretePattern(tree);
		Expression ast = lift(pattern, pattern, match, false);
		
		if (ast != null) {
			ASTStatistics stats = ast.getStats();
			stats.setConcreteFragmentCount(1);
			stats.setConcreteFragmentSize(TreeAdapter.getLocation(pattern).getLength());
			
			if (stats.isAmbiguous()) {
				throw new Ambiguous((IConstructor) ast.getTree());
			}
		}
		
		if (match) {
			matchCache.putUnsafe(tree, ast);
		} else {
			constructorCache.putUnsafe(tree, ast);
		}
		return ast;
	}

	private Expression lift(IValue pattern, IConstructor source, boolean match, boolean inlist) {
		Expression cached = (match ? matchCache : constructorCache).get(pattern);
		if (cached != null) {
			return cached;
		}
		
		Type type = pattern.getType();
		if (type.isNodeType()) {
			INode node = (INode) pattern;
			ASTStatistics stats = new ASTStatistics();
			boolean isAmb = false;
			ISourceLocation loc = null;
			Type nonterminalType = null;
			
			if (type.isAbstractDataType()) {
				IConstructor tree = (IConstructor) pattern;

				if (TreeAdapter.isAppl(tree)) {
					loc = TreeAdapter.getLocation(tree);
					
					if (TreeAdapter.isList(tree)) {
						inlist = true;
					}
					
					// normal variables
					String cons = TreeAdapter.getConstructorName(tree);
					if (cons != null && (cons.equals("MetaVariable")
							// TODO: TypedMetaVariable does not exist in grammar
							|| cons.equals("TypedMetaVariable"))) {
						Expression result = liftVariable(tree);
						// Set the nonterminal type of metavariables in concrete syntax patterns
						if (result != null && result.hasType()) {
							result._setType(RascalTypeFactory.getInstance().nonTerminalType(result.getType()));
						}
						(match ? matchCache : constructorCache).putUnsafe(pattern, result);
						return result;
					}
					
					if (match && SymbolAdapter.isCfOptLayout(ProductionAdapter.getRhs(TreeAdapter.getProduction(tree)))) {
						Expression result = wildCard(tree);
						(match ? matchCache : constructorCache).putUnsafe(pattern, result);
						return result;
					}
					
					if (!TreeAdapter.isLexical(tree) || TreeAdapter.isInjectionOrSingleton(tree)) {
						stats.setInjections(1);
					}
					else if (TreeAdapter.isNonEmptyStarList(tree)) {
						stats.setInjections(1);
					}
					else {
						stats.setInjections(0); // bug
					}

					source = tree;
					IConstructor sym =  ProductionAdapter.getRhs(TreeAdapter.getProduction(tree));
					nonterminalType = RascalTypeFactory.getInstance().nonTerminalType(sym);
				}
				else if (TreeAdapter.isAmb(tree)) {
					isAmb = true;
					source = tree;
					IConstructor sym = ProductionAdapter.getRhs(TreeAdapter.getProduction((IConstructor) TreeAdapter.getAlternatives(tree).iterator().next()));
					nonterminalType = RascalTypeFactory.getInstance().nonTerminalType(sym);
				}
				else if (TreeAdapter.isChar(tree)) {
					source = tree;
				}
			}
			

			String name = node.getName();
			List<Expression> args = new ArrayList<Expression>(node.arity());

			for (IValue child : node) {
				Expression ast = lift(child, source, match, inlist);
				if (ast == null) {
					return null;
				}
				args.add(ast);
				stats.add(ast.getStats());
			}
			
			if (isAmb && ((Expression.Set)args.get(0)).getElements().size() == 1) {
				Expression result = ((Expression.Set)args.get(0)).getElements().get(0);
				(match ? matchCache : constructorCache).putUnsafe(pattern, result);
				return result;
			}
			
			if (isAmb) {
				stats.setAmbiguous(true);
			}

			// this generates a node instead of a constructor for the cons name, to match the representation
			// that is produced by SGLR
			Expression func;
			if (!name.equals("cons")) {
				func = makeQualifiedName(source, name);
			}
			else {
				func = makeStringExpression(source, name);
			}
			Expression ast = factory.makeExpressionCallOrTree(source, func, args);
			ast._setType(nonterminalType);

			if (loc != null && !match) {
				ast = addLocationAnnotationSetterExpression(source, loc, ast);
			}
			
			ast.setStats(stats);
			
			(match ? matchCache : constructorCache).putUnsafe(pattern, ast);
			return ast;
		}
		else if (type.isListType()) {
			IList list = (IList) pattern;
			List<Expression> result = new ArrayList<Expression>(list.length());
			ASTStatistics stats = new ASTStatistics();
			
			if (list.length() == 1) {
				stats.setInjections(1); 
			}
			
			for (IValue arg: list) {
				Expression ast = lift(arg, source, match, false);
				
				if (ast == null) {
					return null;
				}
				
				// TODO: this does not deal with directly nested lists
				if (inlist && isListAppl(ast)) {
					// splicing can be necessary if filtering was successful
					List<Expression> elements = ast.getArguments().get(1).getElements();
					for (Expression elem : elements) {
						stats.add(elem.getStats());
						result.add(elem);
					}
				}
				else {
					stats.add(ast.getStats());
					result.add(ast);
				}
			}
			Expression.List ast = factory.makeExpressionList(source, result);
			ast.setStats(stats);
			(match ? matchCache : constructorCache).putUnsafe(pattern, ast);
			return ast;
		}
		else if (type.isStringType()) {
			Expression result = factory.makeExpressionLiteral(source, 
									factory.makeLiteralString(source,
											factory.makeStringLiteralNonInterpolated(source, 
													factory.makeStringConstantLexical(source, pattern.toString()))));
			matchCache.putUnsafe(pattern, result);
			constructorCache.putUnsafe(pattern, result);
			return result;
		}
		else if (type.isIntegerType()) {
			Expression result = factory.makeExpressionLiteral(source, factory.makeLiteralInteger(source, factory.makeIntegerLiteralDecimalIntegerLiteral(source, factory.makeDecimalIntegerLiteralLexical(source, pattern.toString()))));
			matchCache.putUnsafe(pattern, result);
			constructorCache.putUnsafe(pattern, result);
			return result;
		}
		else if (type.isSetType()) {
			// this code depends on the fact that only amb nodes can contain sets
			ISet set = (ISet) pattern;
			
			List<Expression> result = new ArrayList<Expression>(set.size());
			ASTStatistics ref = null;
			
			for (IValue elem : set) {
				Expression ast = lift(elem, source, match, false);
				
				if (ast != null) {
					if (ref == null) {
						ref = ast.getStats();
						result.add(ast);
					}
					else {
						ref = filter(result, ast, ref);
					}
				}
			}
			
			if (result.size() == 0) {
				return null; // all alts filtered
			}
			
			Expression.Set ast = factory.makeExpressionSet(source, result);
			ast.setStats(ref != null ? ref : new ASTStatistics());
			(match ? matchCache : constructorCache).putUnsafe(pattern, ast);
			return ast;
		}
		else {
			throw new ImplementationError("Illegal value encountered while lifting a concrete syntax pattern:" + pattern);
		}
	}

	private org.rascalmpl.ast.Expression.Literal makeStringExpression(
			IConstructor source, String name) {
		return factory.makeExpressionLiteral(source, factory.makeLiteralString(source, factory.makeStringLiteralNonInterpolated(source, factory.makeStringConstantLexical(source, "\""+  name + "\""))));
	}

	private Expression addLocationAnnotationSetterExpression(
			IConstructor source, ISourceLocation loc, Expression ast) {
		List<Expression> positions = new ArrayList<Expression>(4);
		positions.add(createIntegerExpression(source, loc.getOffset()));
		positions.add(createIntegerExpression(source, loc.getLength()));
		
		List<Expression> begin = new ArrayList<Expression>(2);
		begin.add(createIntegerExpression(source, loc.getBeginLine()));
		begin.add(createIntegerExpression(source, loc.getBeginColumn()));
		positions.add(factory.makeExpressionTuple(source, begin));
		
		List<Expression> end = new ArrayList<Expression>(2);
		end.add(createIntegerExpression(source, loc.getEndLine()));
		end.add(createIntegerExpression(source, loc.getEndColumn()));
		positions.add(factory.makeExpressionTuple(source, end));
		
		String host = loc.getURI().getAuthority();
		String uriPath = loc.getURI().getPath();
		String path = host != null ? host : "" + "/" + uriPath != null ? uriPath : "";
		ast = factory.makeExpressionSetAnnotation(source, ast, Names.toName("loc"), 
				factory.makeExpressionCallOrTree(source, factory.makeExpressionLiteral(source, 
						factory.makeLiteralLocation(source, 
								factory.makeLocationLiteralDefault(source, 
										factory.makeProtocolPartNonInterpolated(source, 
												factory.makeProtocolCharsLexical(source, "|" + loc.getURI().getScheme() + "://")
										), 
										factory.makePathPartNonInterpolated(source, 
												factory.makePathCharsLexical(source, path + "|"))))),
												positions
												));
//		ast._setType(ast._getType());
		return ast;
	}

	private org.rascalmpl.ast.Expression.Literal createIntegerExpression(
			IConstructor source, int offset) {
		return factory.makeExpressionLiteral(source, factory.makeLiteralInteger(source, factory.makeIntegerLiteralDecimalIntegerLiteral(source, factory.makeDecimalIntegerLiteralLexical(source, Integer.toString(offset)))));
	}

	// TODO: optimize, this can be really slowing things down
	private boolean isListAppl(Expression ast) {
		if (!ast.isCallOrTree()) {
			return false;
		}
		
		if(!ast.getExpression().isQualifiedName()) {
			return false;
		}
		
		CallOrTree call = (CallOrTree) ast;
		
		String name = Names.name(Names.lastName(call.getExpression().getQualifiedName()));
		
		if (!name.equals("appl")) {
			return false;
		}
		
		CallOrTree prod = (CallOrTree) ast.getArguments().get(0);
		name = Names.name(Names.lastName(prod.getExpression().getQualifiedName()));
		
		return name.equals("list");
	}

	private Expression liftVariable(IConstructor tree) {
		String cons = TreeAdapter.getConstructorName(tree);
		
		if (cons.equals("MetaVariable")) {
			IConstructor arg = (IConstructor) getASTArgs(tree).get(0);
			
			if (arg.getConstructorType() == Factory.Tree_Amb) {
				return filterNestedPattern(tree, arg); 
			}
			Expression result = (Expression) buildValue(arg);
		
			if (result != null && correctlyNestedPattern(ProductionAdapter.getRhs(TreeAdapter.getProduction(tree)), result)) {
				return result;
			}
			return null;
		}
		throw new ImplementationError("Unexpected meta variable while lifting pattern");
	}


	private org.rascalmpl.ast.Expression makeQualifiedName(IConstructor node, String name) {
		Name simple = factory.makeNameLexical(node, name);
		List<Name> list = new ArrayList<Name>(1);
		list.add(simple);
		return factory.makeExpressionQualifiedName(node, factory.makeQualifiedNameDefault(node, list));
	}

	private boolean correctlyNestedPattern(IConstructor expected, Expression exp) {
		if (exp.isTypedVariable()) {
			Expression.TypedVariable var = (Expression.TypedVariable) exp;
			IValue type = Symbols.typeToSymbol(var.getType());

			// the declared type inside the pattern must match the produced type outside the brackets
			// "<" Pattern ">" -> STAT in the grammar and "<STAT t>" in the pattern. STAT == STAT.
			if (type.equals(expected) ) {
				return true;
			}
			
			if (SymbolAdapter.isAnyList((IConstructor) type)) {
				
				IConstructor elem = SymbolAdapter.getSymbol((IConstructor) type);
				return elem.equals(expected);
			}
			return false;
		}
		else if (exp.isGuarded()) {
			Expression.Guarded var = (Expression.Guarded) exp;
			IValue type = Symbols.typeToSymbol(var.getType());

			// the declared type inside the pattern must match the produced type outside the brackets
			// "<" [Type] Pattern ">" -> STAT in the grammar and "<[STAT] pattern>" in the pattern. STAT == STAT.
			if (type.equals(expected) ) {
				return true;
			}
			return false;
		}
		
		return true;
	}

	private IConstructor getConcretePattern(IConstructor tree) {
		String cons = TreeAdapter.getConstructorName(tree);
		
		if (cons.equals("ConcreteQuoted")) {
			return (IConstructor) getASTArgs(tree).get(0);
		}
		
		if (cons.equals("ConcreteUnquoted")) {
			return (IConstructor) getASTArgs(tree).get(0);
		}
		
		if (cons.equals("ConcreteTypedQuoted")) {
			 return (IConstructor) TreeAdapter.getArgs(tree).get(8);
		}
		
		throw new ImplementationError("Unexpected embedding syntax");
	}

	private IList getASTArgs(IConstructor tree) {
//		if (!TreeAdapter.isContextFree(tree)) {
//			throw new ImplementationError("This is not a context-free production: "
//					+ tree);
//		}
	
		IList children = TreeAdapter.getArgs(tree);
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory.getValueFactory());
	
		for (int i = 0; i < children.length(); i++) {
			IConstructor kid = (IConstructor) children.get(i);
			if (!TreeAdapter.isLiteral(kid) && !TreeAdapter.isCILiteral(kid) && !isRascalLiteral(kid)) {
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

	private Expression wildCard(IConstructor node) {
		return makeQualifiedName(node, "_");
	}

	private ImplementationError unexpectedError(Throwable e) {
		return new ImplementationError("Unexpected error in AST construction: " + e, e);
	}

	private boolean isEmbedding(IConstructor tree) {
		String name = TreeAdapter.getConstructorName(tree);
		return name.equals("ConcreteQuoted") 
		|| name.equals("ConcreteUnquoted") 
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
			IConstructor rhs = ProductionAdapter.getRhs(prod);
			
			if (SymbolAdapter.isParameterizedSort(rhs) && SymbolAdapter.getName(rhs).equals("_WrappedLiteral")) {
				return true;
			}
		}
		return false;
	}

	private boolean isRascalSort(String sort) {
		return sort.startsWith(RASCAL_SORT_PREFIX);
	}

	private AbstractAST callMakerMethod(String sort, String cons, Class<?> formals[], Object actuals[]) {
		try {
			Method make = clazz.getMethod("make" + sort + cons, formals);
			AbstractAST ast = (AbstractAST) make.invoke(factory, actuals);
			return ast;
		} catch (SecurityException e) {
			throw unexpectedError(e);
		} catch (NoSuchMethodException e) {
			throw unexpectedError(e);
		} catch (IllegalArgumentException e) {
			throw unexpectedError(e);
		} catch (IllegalAccessException e) {
			throw unexpectedError(e);
		} catch (InvocationTargetException e) {
			throw unexpectedError(e);
		}
	}

}
