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
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.Symbols;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.AmbiguousConcretePattern;
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
		TreeAdapter tree = new ParsetreeAdapter(parseTree).getTop();
		if (tree.isAppl()) {
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
		TreeAdapter start = new TreeAdapter(top);
		IConstructor tree = (IConstructor) start.getArgs().get(1);
		TreeAdapter treeAdapter = new TreeAdapter(tree); 

		if (sortName(treeAdapter).equals(sort)) {
			return (T) buildValue(tree);
		}
		throw new ImplementationError("This is not a" + sort +  ": " + new TreeAdapter(tree).yield());
	}
	
	private String sortName(TreeAdapter tree) {
		if (tree.isAppl()) {
			String sortName = tree.getSortName();

			if (isRascalSort(sortName)) {
				sortName = sortName.substring(1);
			}

			return sortName;
		}
		return "";
	}
	
	private List<AbstractAST> buildList(IConstructor in)  {
		IList args = new TreeAdapter(in).getListASTArgs();
		List<AbstractAST> result = new ArrayList<AbstractAST>(args.length());
		for (IValue arg: args) {
			AbstractAST elem = buildValue(arg);
			
			if (elem == null) {
				return null; // filtered
			}
			result.add(elem);
		}
		return result;
	}

	public IList getASTArgs(TreeAdapter tree) {
		if (!tree.isContextFree()) {
			throw new ImplementationError("This is not a context-free production: "
					+ tree);
		}

		IList children = tree.getArgs();
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory.getValueFactory());

		for (int i = 0; i < children.length(); i++) {
			IValue kid = children.get(i);
			TreeAdapter treeAdapter = new TreeAdapter((IConstructor) kid);
			if (!treeAdapter.isLiteral() && !treeAdapter.isCILiteral() && !isRascalLiteral(treeAdapter)) {
				writer.append(kid);	
			} 
			// skip layout
			i++;
		}
		
		return writer.done();
	}
	
	private AbstractAST buildContextFreeNode(IConstructor in)  {
		try {
			TreeAdapter tree = new TreeAdapter(in);

			String cons = tree.getConstructorName();
			String sort = sortName(tree);
			sort = sort.equalsIgnoreCase("pattern") ? "Expression" : capitalize(sort); 
			cons = capitalize(cons);
			
			IList args = getASTArgs(tree);
			int arity = args.length() + 1;
			Class<?> formals[] = new Class<?>[arity];
			Object actuals[] = new Object[arity];

			formals[0] = INode.class;
			actuals[0] = in;

			ASTStatistics total = new ASTStatistics();
			
			int i = 1;
			for (IValue arg : args) {
				TreeAdapter argTree = new TreeAdapter((IConstructor) arg);
				
				if (argTree.isList()) {
					actuals[i] = buildList((IConstructor) arg);
					formals[i] = List.class;
					
					if (actuals[i] == null) { // filtered
						return null;
					}
					
					for (Object ast : ((java.util.List<?>) actuals[i])) {
						total.add(((AbstractAST) ast).getStats());
					}
				}
				else if (argTree.isAmbiguousList()) {
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

			Method make = clazz.getMethod("make" + sort + cons, formals);
			AbstractAST ast = (AbstractAST) make.invoke(factory, actuals);
			ast.setStats(total);
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
	
	private List<AbstractAST> filterList(TreeAdapter argTree) {
		ISet alts = argTree.getAlternatives();
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
		
		if (result.size() == 1) {
			return result.get(0);
		}
		// we don't support ambiguous lists in Rascal AST's.
		// if filtering was not successful we have to bail out
		// the hypothesis is that this can never happen
		throw new ImplementationError("Unexpected ambiguous list after filtering");
	}

	private AbstractAST buildLexicalNode(IConstructor in) {
		try {
			TreeAdapter tree = new TreeAdapter(in);

			String sort = sortName(tree);
			String Sort = capitalize(sort);

			Class<?> formals[] = new Class<?>[] { INode.class, String.class };
			Object actuals[] = new Object[] { in, new String(new TreeAdapter(in).yield()) };

			Method make = clazz.getMethod("make" + Sort + "Lexical", formals);
			return (AbstractAST) make.invoke(factory, actuals);
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
	
	private ImplementationError unexpectedError(Throwable e) {
		return new ImplementationError("Unexpected error in AST construction: " + e, e);
	}

	private boolean isEmbedding(TreeAdapter tree) {
		String name = tree.getConstructorName();
		return name.equals("ConcreteQuoted") 
		|| name.equals("ConcreteUnquoted") 
		|| name.equals("ConcreteTypedQuoted");
	}
		
	private AbstractAST buildValue(IValue arg)  {
		TreeAdapter tree = new TreeAdapter((IConstructor) arg);
		
		if (tree.isAmb()) {
			return filter(tree);
		}
		
		if (!tree.isAppl()) {
			throw new UnsupportedOperationException();
		}	
		
		if (isLexical(tree)) {
			return buildLexicalNode((IConstructor) ((IList) ((IConstructor) arg).get("args")).get(0));
		}
		else if (sortName(tree).equals("FunctionBody") && tree.getConstructorName().equals("Java")) {
			return new JavaFunctionBody((INode) arg, tree.yield());
		}
		else if (sortName(tree).equals("Pattern") && isEmbedding(tree)) {
			return lift(tree, true);
		}
		else if (sortName(tree).equals("Expression") && isEmbedding(tree)) {
			return lift(tree, false);
		}

		return buildContextFreeNode((IConstructor) arg);
	}

	private boolean isLexical(TreeAdapter tree) {
		if (tree.isLexToCf()) {
			return !isRascalLiteral(tree);
		}
		return false;
	}

	private boolean isRascalLiteral(TreeAdapter tree) {
		if (tree.isAppl()) {
			ProductionAdapter prod = tree.getProduction();
			SymbolAdapter rhs = prod.getRhs();
			
			if (rhs.isCf()) {
				rhs = rhs.getSymbol();
			}
			if (rhs.isParameterizedSort() && rhs.getName().equals("_Literal")) {
				return true;
			}
		}
		return false;
	}

	private AbstractAST filter(TreeAdapter tree) {
		ISet altsIn = tree.getAlternatives();
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
			throw new ImplementationError("Accidentally all ambiguous alternatives were removed");
		}
		else if (altsOut.size() == 1) {
			return altsOut.iterator().next();
		}
		
		try {
			if (isRascalSort(sort)) {
				sort = sort.substring(1);
				sort = capitalize(sort);
			}
			else {
				// concrete syntax is lifted to Expression
				sort = "Expression";
			}
			
			Class<?> formals[] = new Class<?>[]  { INode.class, List.class };
			Object actuals[] = new Object[] { tree.getTree(), altsOut };

			Method make = clazz.getMethod("make" + sort + "Ambiguity", formals);
			AbstractAST ast = (AbstractAST) make.invoke(factory, actuals);
			ast.setStats(ref != null ? ref : new ASTStatistics());
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

	private <T extends AbstractAST> ASTStatistics filter(java.util.List<T> altsOut,
			T ast, ASTStatistics ref) {
		ASTStatistics stats = ast.getStats();
		return filter(altsOut, ast, ref, stats);
	}

	private <T> ASTStatistics filter(java.util.List<T> altsOut,
			T ast, ASTStatistics ref, ASTStatistics stats) {
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

	private boolean isRascalSort(String sort) {
		return sort.startsWith(RASCAL_SORT_PREFIX);
	}

	private AbstractAST lift(TreeAdapter tree, boolean match) {
		IConstructor pattern = getConcretePattern(tree);
		Expression ast = lift(pattern, pattern, match);
		
		ast = filterNonEmptyStarLists(ast);
		
		if (ast != null) {
			ASTStatistics stats = ast.getStats();
			stats.setConcreteFragmentCount(1);
			stats.setConcreteFragmentSize(new TreeAdapter(pattern).yield().length());
		}
		
		return ast;
	}

	String getCallName(Expression.CallOrTree call) {
		return Names.name(Names.lastName(call.getQualifiedName()));
	}
	
	/** [| a a |] is a non-empty list of 'A*'. However there is "[|" A* "|]" -> Pattern
	 * and  "[|" A+ "|]" -> Pattern both always present at the same time. This is always
	 * ambiguous. This filter removes the * list interpretation in favor of the + interpretation.
	 */
	private Expression filterNonEmptyStarLists(Expression ast) {
		// unfortunately, I don't know when to do this on the parse tree level, so
		// we have to understand parse trees on the lifted AST level here :-(
		
		
		// TODO: this code is highly experimental Tijs, I don't know if it should be kept.
		// it smells wrong to be doing this on this level. Also, it did not solve the problem
		// I had.
		
		if(ast == null)
			return null;
		if (ast.isCallOrTree()) {
			Expression arg0 = ast.getArguments().get(0);
			if(!(arg0 instanceof CallOrTree)){
				if(arg0.isSet())
					throw new AmbiguousConcretePattern(arg0);
				
				throw new ImplementationError("Unexpected AST node");
			}
			
			Expression.CallOrTree prod = (CallOrTree) arg0;
			Expression.List args = (org.meta_environment.rascal.ast.Expression.List) ast.getArguments().get(1);

			if (getCallName(prod).equals("list")) {
				Expression.CallOrTree rhs = (CallOrTree) prod.getArguments().get(0);

				if (getCallName(rhs).equals("cf")) {
					rhs = (CallOrTree) rhs.getArguments().get(0);

					if (getCallName(rhs).equals("iter-star") || getCallName(rhs).equals("iter-star-sep")) { 
						// its a * list!
						if (args.getElements().size() > 0) {
							// it does have elements, so + list is good too, so filter this one
							return null;
						}
					}
				}
			}
		}
		return ast;
	}

	private Expression lift(IValue pattern, IConstructor source, boolean match) {
		Type type = pattern.getType();
		if (type.isNodeType()) {
			INode node = (INode) pattern;
			ASTStatistics stats = new ASTStatistics();
			
			if (type.isAbstractDataType()) {
				IConstructor constr = (IConstructor) pattern;

				if (constr.getConstructorType() == Factory.Tree_Appl) {
					TreeAdapter tree = new TreeAdapter(constr);
					String cons = tree.getConstructorName();
					if (cons != null && (cons.equals("MetaVariable") 
							|| cons.equals("TypedMetaVariable"))) {
						return liftVariable(tree);
					}

					if (match && tree.getProduction().getRhs().isCfOptLayout()) {
						return wildCard(constr);
					}
					
					if (tree.isContextFreeInjectionOrSingleton()) {
						stats.setInjections(1);
					}
					else {
						stats.setInjections(0); // bug
					}

					source = constr;
				}
			}

			String name = node.getName();
			List<Expression> args = new ArrayList<Expression>(node.arity());

			for (IValue child : node) {
				Expression ast = lift(child, source, match);
				if (ast == null) {
					return null;
				}
				args.add(ast);
				stats.add(ast.getStats());
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
				Expression ast = lift(arg, source, match);
				
				if (ast == null) {
					return null;
				}
				
				stats.add(ast.getStats());
				result.add(ast);
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
				Expression ast = lift(elem, source, match);
				
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

	private Expression wildCard(IConstructor node) {
		return new Expression.QualifiedName(node, makeQualifiedName(node, "_"));
	}

	private QualifiedName makeQualifiedName(IConstructor node, String name) {
		Name simple = new Name.Lexical(node, name);
		List<Name> list = new ArrayList<Name>(1);
		list.add(simple);
		return new QualifiedName.Default(node, list);
	}

	private Expression liftVariable(TreeAdapter tree) {
		String cons = tree.getConstructorName();
		
		if (cons.equals("MetaVariable")) {
			IConstructor arg = (IConstructor) getASTArgs(tree).get(0);
			
			if (arg.getConstructorType() == Factory.Tree_Amb) {
				return filterNestedPattern(tree, new TreeAdapter(arg)); 
			}
			Expression result = (Expression) buildValue(arg);
		
			if (correctlyNestedPattern(tree.getProduction().getRhs(), result)) {
				return result;
			}
			return null;
		}
		throw new ImplementationError("Unexpected meta variable while lifting pattern");
	}

	/**
	 * Removes patterns like <PROGRAM p> where the <...> hole is not nested in a place
	 * where a PROGRAM is expected. Also, patterns that directly nest concrete syntax patterns
	 * again, like [| <[| ... |]> |] are filtered.
	 */
	private Expression filterNestedPattern(TreeAdapter antiQuote, TreeAdapter pattern) {
		ISet alternatives = pattern.getAlternatives();
		List<Expression> result = new ArrayList<Expression>(alternatives.size());
		 
		SymbolAdapter expected = antiQuote.getProduction().getRhs();
		
		// any alternative that is a typed variable must be parsed using a 
		// MetaVariable that produced exactly the same type as is declared inside
		// the < > brackets.
		for (IValue alt : alternatives) {
			if (isEmbedding(new TreeAdapter((IConstructor) alt))) {
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
		return new Expression.Ambiguity(antiQuote.getTree(), result);
	}

	private boolean correctlyNestedPattern(SymbolAdapter expected, Expression exp) {
		if (exp.isTypedVariable()) {
			Expression.TypedVariable var = (Expression.TypedVariable) exp;
			IValue type = Symbols.typeToSymbol(var.getType());

			// the declared type inside the pattern must match the produced type outside the brackets
			// "<" Pattern ">" -> STAT in the grammar and "<STAT t>" in the pattern. STAT == STAT.
			if (type.equals(expected.getTree())) {
				return true;
			}
			return false;
		}
		
		return true;
	}

	private IConstructor getConcretePattern(TreeAdapter tree) {
		String cons = tree.getConstructorName();
		IConstructor pattern;
		
		if (cons.equals("ConcreteQuoted")) {
			pattern = (IConstructor) getASTArgs(tree).get(0);
		}
		else if (cons.equals("ConcreteUnquoted")) {
			pattern = (IConstructor) getASTArgs(tree).get(0);
		}
		else if (cons.equals("ConcreteTypedQuoted")) {
			System.err.println("0: " + getASTArgs(tree).get(0));
			// TODO PK: Was: pattern = (IConstructor) getASTArgs(tree).get(1);
			// The type has disappeared!
			 pattern = (IConstructor) getASTArgs(tree).get(0);
		}
		else {
			throw new ImplementationError("Unexpected embedding syntax");
		}
		
		return pattern;
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

}
