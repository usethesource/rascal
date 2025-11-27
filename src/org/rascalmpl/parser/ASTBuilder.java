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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Commands;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.parser.gtd.util.PointerKeyedHashMap;
import org.rascalmpl.semantics.dynamic.Tree;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse node of a rascal program.
 */
public class ASTBuilder {
	private static final String MODULE_SORT = "Module";

	private final PointerKeyedHashMap<IValue, Expression> constructorCache = new PointerKeyedHashMap<IValue, Expression>();
	private final static HashMap<String, Constructor<?>> astConstructors = new HashMap<String,Constructor<?>>();
	private final static HashMap<String, Class<?>> astClasses = new HashMap<String,Class<?>>();
	
	private final static ClassLoader classLoader = ASTBuilder.class.getClassLoader();

	public static <T extends AbstractAST> T make(String sort, ISourceLocation src, Object... args) {
		return make(sort, "Default", src, args);
	}

	public static  <T extends Expression> T makeExp(String cons, ISourceLocation src, Object... args) {
		return make("Expression", cons, src, args);
	}

	public static <T extends Statement> T makeStat(String cons, ISourceLocation src, Object... args) {
		return make("Statement", cons, src, args);
	}

	public static <T extends AbstractAST> T makeLex(String sort, ISourceLocation src, Object... args) {
		return make(sort, "Lexical", src, args);
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractAST> T make(String sort, String cons, ISourceLocation src, Object... args) {
		Object[] newArgs = new Object[args.length + 2];
		System.arraycopy(args, 0, newArgs, 2, args.length);
		newArgs[0] = src;
		return (T) callMakerMethod(sort, cons, newArgs, null);
	}
 
	public Module buildModule(org.rascalmpl.values.parsetrees.ITree tree) throws FactTypeUseException {
		if (TreeAdapter.isAppl(tree)) {
	 		if (sortName(tree).equals(MODULE_SORT)) {
				// t must be an appl so call buildValue directly
				return (Module) buildValue(tree);
			}
			return buildSort(tree, MODULE_SORT);
		}

		if (TreeAdapter.isAmb(tree)) {
			throw new Ambiguous(tree);
		}

		throw new ImplementationError("Parse of module returned invalid tree.");
	}

	public Expression buildExpression(org.rascalmpl.values.parsetrees.ITree parseTree) {
		return buildSort(parseTree, "Expression");
	}

	public Statement buildStatement(org.rascalmpl.values.parsetrees.ITree parseTree) {
		return buildSort(parseTree, "Statement");
	}

	public Command buildCommand(org.rascalmpl.values.parsetrees.ITree parseTree) {
		return buildSort(parseTree, "Command");
	}

	public Sym buildSym(org.rascalmpl.values.parsetrees.ITree parseTree) {
		return buildSort(parseTree, "Sym");
	}

	public Commands buildCommands(org.rascalmpl.values.parsetrees.ITree parseTree) {
		return buildSort(parseTree, "Commands");
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractAST> T buildSort(org.rascalmpl.values.parsetrees.ITree parseTree, String sort) {
		if (TreeAdapter.isAppl(parseTree)) {
			org.rascalmpl.values.parsetrees.ITree tree = TreeAdapter.getStartTop(parseTree);

			if (sortName(tree).equals(sort)) {
				return (T) buildValue(tree);
			}
		} 
		else if (TreeAdapter.isAmb(parseTree)) {
			throw new Ambiguous(parseTree);
		}

		throw new ImplementationError("This is not a " + sort +  ": " + parseTree);
	}

	public AbstractAST buildValue(IValue arg)  {
		org.rascalmpl.values.parsetrees.ITree tree = (org.rascalmpl.values.parsetrees.ITree) arg;

		if (TreeAdapter.isList(tree)) {
			throw new ImplementationError("buildValue should not be called on a list");
		}

		if (TreeAdapter.isAmb(tree)) {
			throw new Ambiguous(tree);
		}

		if (!TreeAdapter.isAppl(tree)) {
			throw new UnsupportedOperationException();
		}	

		if (isLexical(tree)) {
			if (TreeAdapter.isRascalLexical(tree)) {
				return buildLexicalNode(tree);
			}
			return buildLexicalNode((org.rascalmpl.values.parsetrees.ITree) ((IList) ((org.rascalmpl.values.parsetrees.ITree) arg).get("args")).get(0));
		}
		
		if (TreeAdapter.isOpt(tree)) {
			IList args = TreeAdapter.getArgs(tree);
			if (args.isEmpty()) {
			    return null;
			}
			return buildValue(args.get(0));
		    
		}

		if (sortName(tree).equals("Pattern")) {
			if (isNewEmbedding(tree)) {
				return newLift(tree, true);
			}
		}

		if (sortName(tree).equals("Expression")) {
			if (isNewEmbedding(tree)) {
				return newLift(tree, false);
			}
		}

		return buildContextFreeNode((org.rascalmpl.values.parsetrees.ITree) arg);
	}

	private List<AbstractAST> buildList(org.rascalmpl.values.parsetrees.ITree in)  {
		List<AbstractAST> result = new ArrayList<AbstractAST>(TreeAdapter.getListLength(in));
		
		IList args = in.getArgs();
		int seps = TreeAdapter.getSeparatorCount(in);

		for (int i = 0; i < args.length(); i++) {
			ITree arg = (ITree) args.get(i);
			result.add(buildValue(arg));
			i += seps;
		}
			
		return result;
	}

	private AbstractAST buildContextFreeNode(org.rascalmpl.values.parsetrees.ITree tree)  {
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

		switch(sort){
		case "Mapping":
			sort = "Mapping_Expression"; break;
		case "KeywordArgument":
			sort = "KeywordArgument_Expression"; break;
		case "KeywordArguments":
			sort = "KeywordArguments_Expression"; break;
		}

		// Here we see how precisely the constructors of the generated AST hierarchy
		// are connected to the shape of the grammar rules for the Rascal syntax.
		// The hierarchy is generated from the same grammar that these parse trees
		// come from, and that's why this works.
		Constructor<?> constructor = getConstructor(sort, cons);
		int parameterCount = constructor.getParameterCount();
		Object[] actuals = new Object[parameterCount]; 
		int i = 0;
		actuals[i++] = TreeAdapter.getLocation(tree);
		actuals[i++] = tree;

		IList args = tree.getArgs();

		for (int j = 0; j < args.length(); j += 2 /* skipping layout */) {
			ITree argTree = (ITree) args.get(j);
			
			if (!TreeAdapter.isLiteral(argTree) && !TreeAdapter.isCILiteral(argTree) && !TreeAdapter.isEmpty(argTree)) {
				if (TreeAdapter.isList(argTree)) {
					actuals[i++] = buildList((org.rascalmpl.values.parsetrees.ITree) argTree);
				}
				else {
					actuals[i++] = buildValue(argTree);
				}
			}
		}

		return callMakerMethod(constructor, actuals, null);
	}

	private AbstractAST buildLexicalNode(org.rascalmpl.values.parsetrees.ITree tree) {
		String sort = capitalize(sortName(tree));

		if (sort.length() == 0) {
			throw new ImplementationError("could not retrieve sort name for " + tree);
		}
		Object actuals[] = new Object[] { TreeAdapter.getLocation(tree), tree, new String(TreeAdapter.yield(tree)) };

		return callMakerMethod(sort, "Lexical", actuals, null);
	}

	private String getPatternLayout(org.rascalmpl.values.parsetrees.ITree tree) {
		IConstructor prod = TreeAdapter.getProduction(tree);
		String cons = ProductionAdapter.getConstructorName(prod);

		if (cons.equals("concrete")) {
			// TODO
			return "???";
		}

		throw new ImplementationError("Unexpected embedding syntax:" + prod);
	}

	private Expression cache(IConstructor in, Expression out) {
		constructorCache.putUnsafe(in, out);
		return out;
	}

	private Expression liftRec(org.rascalmpl.values.parsetrees.ITree tree, boolean lexicalFather, String layoutOfFather) {
		Expression cached = constructorCache.get(tree);
		if (cached != null) {
			return cached;
		}

		if (layoutOfFather == null) {
			throw new ImplementationError("layout is null");
		}

		if (TreeAdapter.isAppl(tree)) {
			String cons = TreeAdapter.getConstructorName(tree);

			if (cons != null && (cons.equals("hole"))) { // TODO: this is unsafe, what if somebody named their own production "hole"??
				return liftHole(tree);
			}

			boolean lex = lexicalFather ? !TreeAdapter.isSort(tree) : TreeAdapter.isLexical(tree);

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
				Expression ast = liftRec((org.rascalmpl.values.parsetrees.ITree) arg, lex, layout);
				if (ast == null) {
					return null;
				}
				kids.add(ast);
			}

			if (TreeAdapter.isList(tree)) {
				// TODO: splice element lists (can happen in case of ambiguous lists)
				return cache(tree, new Tree.List(TreeAdapter.getProduction(tree), TreeAdapter.getLocation(tree), kids));
			}
			else if (TreeAdapter.isOpt(tree)) {
				return cache(tree, new Tree.Optional(TreeAdapter.getProduction(tree), TreeAdapter.getLocation(tree), kids));
			}
			else { 
				return cache(tree, new Tree.Appl(TreeAdapter.getProduction(tree), TreeAdapter.getLocation(tree), kids));
			}
		}
		else if (TreeAdapter.isCycle(tree)) {
			return new Tree.Cycle(TreeAdapter.getLocation(tree), TreeAdapter.getCycleType(tree), TreeAdapter.getCycleLength(tree));
		}
		else if (TreeAdapter.isAmb(tree)) {
			ISet args = TreeAdapter.getAlternatives(tree);
			java.util.List<Expression> kids = new ArrayList<Expression>(args.size());

			for (IValue arg : args) {
				kids.add(liftRec((org.rascalmpl.values.parsetrees.ITree) arg, lexicalFather, layoutOfFather));
			}

			if (kids.size() == 0) {
				return null;
			}

			if (kids.size() == 1) {
				return kids.get(0);
			}

			return cache(tree, new Tree.Amb(TreeAdapter.getLocation(tree), tree, kids));
		}
		else {
			if (!TreeAdapter.isChar(tree)) {
				throw new ImplementationError("unexpected tree type: " + tree);
			}
			return cache(tree, new Tree.Char(TreeAdapter.getLocation(tree), tree)); 
		}
	}

    private Expression liftHole(org.rascalmpl.values.parsetrees.ITree tree) {
		assert tree.asWithKeywordParameters().hasParameter("holeType");
		IConstructor type = (IConstructor) tree.asWithKeywordParameters().getParameter("holeType");
		tree = (org.rascalmpl.values.parsetrees.ITree) TreeAdapter.getArgs(tree).get(0);
		IList args = TreeAdapter.getArgs(tree);
		IConstructor nameTree = (IConstructor) args.get(4);
		ISourceLocation src = TreeAdapter.getLocation(tree);
		Expression result = new Tree.MetaVariable(src, tree, type, TreeAdapter.yield(nameTree));
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

	private String sortName(org.rascalmpl.values.parsetrees.ITree tree) {
		if (TreeAdapter.isAppl(tree)) { 
			return TreeAdapter.getSortName(tree);
		}
		if (TreeAdapter.isAmb(tree)) {
			// all alternatives in an amb cluster have the same sort
			return sortName((org.rascalmpl.values.parsetrees.ITree) TreeAdapter.getAlternatives(tree).iterator().next());
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

	private boolean isNewEmbedding(org.rascalmpl.values.parsetrees.ITree tree) {
		String name = TreeAdapter.getConstructorName(tree);
		assert name != null;

		if (name.equals("concrete")) {
			tree = (org.rascalmpl.values.parsetrees.ITree) TreeAdapter.getArgs(tree).get(0);
			name = TreeAdapter.getConstructorName(tree);

			if (name.equals("$parsed")) {
				return true;
			}
		}
		return false;
	}

	private boolean isLexical(org.rascalmpl.values.parsetrees.ITree tree) {
		return TreeAdapter.isRascalLexical(tree);
	}

	private AbstractAST newLift(org.rascalmpl.values.parsetrees.ITree tree, boolean match) {
		org.rascalmpl.values.parsetrees.ITree concrete = (org.rascalmpl.values.parsetrees.ITree) TreeAdapter.getArgs(tree).get(0);
		org.rascalmpl.values.parsetrees.ITree fragment = (org.rascalmpl.values.parsetrees.ITree) TreeAdapter.getArgs(concrete).get(7);
		return liftRec(fragment, false,  getPatternLayout(tree));
	}

	private static Class<?> loadClass(String name) throws ClassNotFoundException {
		if (astClasses.containsKey(name)) {
			return astClasses.get(name);
		}

		Class<?> result = null;

		try {
			result = classLoader.loadClass("org.rascalmpl.semantics.dynamic." + name);
		}
		catch (ClassNotFoundException e) {
			result = classLoader.loadClass("org.rascalmpl.ast." + name);
		}

		astClasses.put(name, result);
		return result;
	}

	private static Constructor<?> getConstructor(String sort, String cons) {
		try {
			String name = sort + '$' + cons;
			Constructor<?> constructor = astConstructors.get(name);

			if (constructor == null) {
				Class<?> clazz = loadClass(name);

				constructor = clazz.getConstructors()[0];
				constructor.setAccessible(true);
				astConstructors.put(name, constructor);
			}
			
			return constructor;
		} catch (SecurityException e) {
			throw unexpectedError(e);
		} catch (IllegalArgumentException e) {
			throw unexpectedError(e);
		} catch (ClassNotFoundException e) {
			throw unexpectedError(e);
		}
	}

	private static AbstractAST callMakerMethod(String sort, String cons, Object actuals[], Object keywordActuals[]) {
		try {
			return (AbstractAST) getConstructor(sort, cons).newInstance(actuals);
		} catch (SecurityException e) {
			throw unexpectedError(e);
		} catch (IllegalArgumentException e) {
			throw unexpectedError(e);
		} catch (IllegalAccessException e) {
			throw unexpectedError(e);
		} catch (InvocationTargetException e) {
			throw unexpectedError(e);
		} catch (InstantiationException e) {
			throw unexpectedError(e);
		}
	}

	private static AbstractAST callMakerMethod(Constructor<?> constructor, Object actuals[], Object keywordActuals[]) {
		try {
			return (AbstractAST) constructor.newInstance(actuals);
		} catch (SecurityException e) {
			throw unexpectedError(e);
		} catch (IllegalArgumentException e) {
			throw unexpectedError(e);
		} catch (IllegalAccessException e) {
			throw unexpectedError(e);
		} catch (InvocationTargetException e) {
			throw unexpectedError(e);
		} catch (InstantiationException e) {
			throw unexpectedError(e);
		}
	}

}
