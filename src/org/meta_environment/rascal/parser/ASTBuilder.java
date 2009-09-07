package org.meta_environment.rascal.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.ASTStatistics;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.IntegerLiteral;
import org.meta_environment.rascal.ast.JavaFunctionBody;
import org.meta_environment.rascal.ast.Literal;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.StringLiteral;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.utils.Names;
import org.meta_environment.rascal.interpreter.utils.Symbols;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ParsetreeAdapter;
import org.meta_environment.uptr.ProductionAdapter;
import org.meta_environment.uptr.SymbolAdapter;
import org.meta_environment.uptr.TreeAdapter;


/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse node of a rascal program.
 *
 */
public class ASTBuilder {
	private static final String RASCAL_SORT_PREFIX = "_";
	private ASTFactory factory;
    private Class<? extends ASTFactory> clazz;
    
	public ASTBuilder(ASTFactory factory) {
		this.factory = factory;
		this.clazz = factory.getClass();
	}
	
	public Module buildModule(IConstructor parseTree) throws FactTypeUseException {
		IConstructor tree = ParsetreeAdapter.getTop(parseTree);
		if (TreeAdapter.isAppl(tree)) {
			return buildSort(parseTree, "Module");
		}
		throw new ImplementationError("Ambiguous module?");
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
	
	@SuppressWarnings("unchecked")
	private <T extends AbstractAST> T buildSort(IConstructor parseTree, String sort) {
		IConstructor top = (IConstructor) parseTree.get("top");
		
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
	
	private AbstractAST buildValue(IValue arg)  {
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
			return buildLexicalNode((IConstructor) ((IList) ((IConstructor) arg).get("args")).get(0));
		}
		
		if (sortName(tree).equals("FunctionBody") && TreeAdapter.getConstructorName(tree).equals("Java")) {
			JavaFunctionBody javaAST = new JavaFunctionBody((INode) arg, TreeAdapter.yield(tree));
			javaAST.getStats().setAvoided(true);
			return javaAST;
		}

		if (sortName(tree).equals("Pattern") && isEmbedding(tree)) {
			return lift(tree, true);
		}

		if (sortName(tree).equals("Expression") && isEmbedding(tree)) {
			return lift(tree, false);
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
		String constructorName = TreeAdapter.getConstructorName(tree);
		if (constructorName == null) {
			throw new ImplementationError("All Rascal productions should have a constructor name: " + ProductionAdapter.getTree(TreeAdapter.getProduction(tree)));
		}
		
		String cons = capitalize(constructorName);
		String sort = sortName(tree);
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
				total.setAvoided(stats.isAvoided());
				total.setPreferred(stats.isPreferred());
					
			}
			i++;
		}

		AbstractAST ast = callMakerMethod(sort, cons, formals, actuals);

		if (arity > 2) { // is not an injection so kill accumulation of prefer and avoid
			total.setAvoided(false);
			total.setPreferred(false);
		}
		
		if (TreeAdapter.hasPreferAttribute(tree)) {
			total.setAvoided(false);
			total.setPreferred(true);
		}
		if (TreeAdapter.hasAvoidAttribute(tree)) {
			total.setAvoided(true);
			total.setPreferred(false);
		}
		
		ast.setStats(total);
		return ast;
	}
	
	private AbstractAST buildLexicalNode(IConstructor tree) {
		String sort = capitalize(sortName(tree));

		Class<?> formals[] = new Class<?>[] { INode.class, String.class };
		Object actuals[] = new Object[] { tree, new String(TreeAdapter.yield(tree)) };

		return callMakerMethod(sort, "Lexical", formals, actuals);
	}
	
	private AbstractAST filter(IConstructor tree) {
		ISet altsIn = TreeAdapter.getAlternatives(tree);
		java.util.List<AbstractAST> altsOut = new ArrayList<AbstractAST>(altsIn.size());
		String sort = "";
		ASTStatistics ref = null;
		
		for (IValue alt : altsIn) {
			AbstractAST ast = buildValue(alt);
			
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
			return null; // this could happen in case of nested ambiguity
//			throw new SyntaxError("concrete syntax pattern", tree.getLocation());
		}
		
		if (altsOut.size() == 1) {
			return altsOut.iterator().next();
		}
		
		if (isRascalSort(sort)) {
			sort = capitalize(sort.substring(1));
		}
		else {
			// concrete syntax is lifted to Expression
			sort = "Expression";
		}

		Class<?> formals[] = new Class<?>[]  { INode.class, List.class };
		Object actuals[] = new Object[] { tree, altsOut };

		AbstractAST ast = callMakerMethod(sort, "Ambiguity", formals, actuals);
		
		ast.setStats(ref != null ? ref : new ASTStatistics());
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
			throw new ImplementationError("Unexpected ambiguous list after filtering", TreeAdapter.getLocation(argTree));
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
		
			if (correctlyNestedPattern(expected, exp)) {
				result.add(exp);
			}
		}
		
		if (result.size() == 1) {
			return result.get(0);
		}
		
		if (result.size() == 0) {
			return null;
		}
		
		return new Expression.Ambiguity(antiQuote, result);
	}

	private AbstractAST lift(IConstructor tree, boolean match) {
		if (TreeAdapter.isEpsilon(tree)) {
			return null;
		}
		
		IConstructor pattern = getConcretePattern(tree);
		Expression ast = lift(pattern, pattern, match, false);
		
		if (ast != null) {
			ASTStatistics stats = ast.getStats();
			stats.setConcreteFragmentCount(1);
			stats.setConcreteFragmentSize(TreeAdapter.yield(pattern).length());
		}
		
		return ast;
	}

	private Expression lift(IValue pattern, IConstructor source, boolean match, boolean inlist) {
		Type type = pattern.getType();
		if (type.isNodeType()) {
			INode node = (INode) pattern;
			ASTStatistics stats = new ASTStatistics();
			boolean isAmb = false;
			
			if (type.isAbstractDataType()) {
				IConstructor tree = (IConstructor) pattern;

				if (tree.getConstructorType() == Factory.Tree_Appl) {
					if (TreeAdapter.isList(tree)) {
						inlist = true;
					}
					
					// list variables
					if (TreeAdapter.isList(tree) && TreeAdapter.getArgs(tree).length() == 1) {
					   IConstructor child = (IConstructor) TreeAdapter.getArgs(tree).get(0);
					   
					   if (TreeAdapter.isAppl(child)) {
						   String cons = TreeAdapter.getConstructorName(child);
						   if (cons != null && (cons.equals("MetaVariable")
								   // TODO: TypedMetaVariable does not exist in grammar
								   || cons.equals("TypedMetaVariable"))) {
							   return liftVariable(child);
						   }
					   }
					}
					
					// normal variables
					String cons = TreeAdapter.getConstructorName(tree);
					if (cons != null && (cons.equals("MetaVariable")
							// TODO: TypedMetaVariable does not exist in grammar
							|| cons.equals("TypedMetaVariable"))) {
						return liftVariable(tree);
					}
					
					if (match && SymbolAdapter.isCfOptLayout(ProductionAdapter.getRhs(TreeAdapter.getProduction(tree)))) {
						return wildCard(tree);
					}
					
					if (TreeAdapter.isContextFreeInjectionOrSingleton(tree)) {
						stats.setInjections(1);
					}
					else if (TreeAdapter.isNonEmptyStarList(tree)) {
						stats.setInjections(1);
					}
					else {
						stats.setInjections(0); // bug
					}

					source = tree;
				}
				else if (tree.getConstructorType() == Factory.Tree_Amb) {
					isAmb = true;
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
				return ((Expression.Set)args.get(0)).getElements().get(0);
			}

			Expression.CallOrTree ast = new Expression.CallOrTree(source, makeQualifiedName(source, name), args);
			ast.setStats(stats);
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
			Expression.List ast = new Expression.List(source, result);
			ast.setStats(stats);
			return ast;
		}
		else if (type.isStringType()) {
			return new Expression.Literal(source, new Literal.String(source, new StringLiteral.Lexical(source, pattern.toString())));
		}
		else if (type.isIntegerType()) {
			return new Expression.Literal(source, new Literal.Integer(source, new IntegerLiteral.DecimalIntegerLiteral(source, new DecimalIntegerLiteral.Lexical(source, pattern.toString()))));
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
			
			Expression.Set ast = new Expression.Set(source, result);
			ast.setStats(ref != null ? ref : new ASTStatistics());
			return ast;
		}
		else {
			throw new ImplementationError("Illegal value encountered while lifting a concrete syntax pattern:" + pattern);
		}
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
		
			if (correctlyNestedPattern(ProductionAdapter.getRhs(TreeAdapter.getProduction(tree)), result)) {
				return result;
			}
			return null;
		}
		throw new ImplementationError("Unexpected meta variable while lifting pattern");
	}


	private org.meta_environment.rascal.ast.Expression makeQualifiedName(IConstructor node, String name) {
		Name simple = new Name.Lexical(node, name);
		List<Name> list = new ArrayList<Name>(1);
		list.add(simple);
		return new Expression.QualifiedName(node, new QualifiedName.Default(node, list));
	}

	private boolean correctlyNestedPattern(IConstructor expected, Expression exp) {
		if (exp.isTypedVariable()) {
			Expression.TypedVariable var = (Expression.TypedVariable) exp;
			IValue type = Symbols.typeToSymbol(var.getType());

			// the declared type inside the pattern must match the produced type outside the brackets
			// "<" Pattern ">" -> STAT in the grammar and "<STAT t>" in the pattern. STAT == STAT.
			if (type.equals(expected)) {
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
			 return (IConstructor) TreeAdapter.getArgs(tree).get(4);
		}
		
		throw new ImplementationError("Unexpected embedding syntax");
	}

	private IList getASTArgs(IConstructor tree) {
		if (!TreeAdapter.isContextFree(tree)) {
			throw new ImplementationError("This is not a context-free production: "
					+ tree);
		}
	
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
		if (TreeAdapter.isLexToCf(tree)) {
			return !isRascalLiteral(tree);
		}
		return false;
	}

	private boolean isRascalLiteral(IConstructor tree) {
		if (TreeAdapter.isAppl(tree)) {
			IConstructor prod = TreeAdapter.getProduction(tree);
			IConstructor rhs = ProductionAdapter.getRhs(prod);
			
			if (SymbolAdapter.isCf(rhs)) {
				rhs = SymbolAdapter.getSymbol(rhs);
			}
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
