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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.BooleanLiteral;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Commands;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.KeywordArguments_Expression;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.RationalLiteral;
import org.rascalmpl.ast.RealLiteral;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.StringConstant.Lexical;
import org.rascalmpl.ast.StringLiteral;
import org.rascalmpl.ast.StringLiteral.NonInterpolated;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.parser.gtd.util.PointerKeyedHashMap;
import org.rascalmpl.semantics.dynamic.Expression.CallOrTree;
import org.rascalmpl.semantics.dynamic.Expression.Set;
import org.rascalmpl.semantics.dynamic.Expression.TypedVariable;
import org.rascalmpl.semantics.dynamic.IntegerLiteral;
import org.rascalmpl.semantics.dynamic.Literal;
import org.rascalmpl.semantics.dynamic.LocationLiteral;
import org.rascalmpl.semantics.dynamic.Name;
import org.rascalmpl.semantics.dynamic.PathChars;
import org.rascalmpl.semantics.dynamic.PathPart;
import org.rascalmpl.semantics.dynamic.ProtocolChars;
import org.rascalmpl.semantics.dynamic.ProtocolPart;
import org.rascalmpl.semantics.dynamic.QualifiedName;
import org.rascalmpl.semantics.dynamic.QualifiedName.Default;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.semantics.dynamic.Type.User;
import org.rascalmpl.semantics.dynamic.UserType;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;

/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse node of a rascal program.
 */
public class ASTBuilder {
	private static final String MODULE_SORT = "Module";

	private final PointerKeyedHashMap<IValue, Expression> constructorCache = new PointerKeyedHashMap<IValue, Expression>();

	private final static HashMap<String, Constructor<?>> astConstructors = new HashMap<String,Constructor<?>>();
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
 
	public Module buildModule(org.rascalmpl.values.uptr.ITree tree) throws FactTypeUseException {
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

	public Expression buildExpression(org.rascalmpl.values.uptr.ITree parseTree) {
		return buildSort(parseTree, "Expression");
	}

	public Statement buildStatement(org.rascalmpl.values.uptr.ITree parseTree) {
		return buildSort(parseTree, "Statement");
	}

	public Command buildCommand(org.rascalmpl.values.uptr.ITree parseTree) {
		return buildSort(parseTree, "Command");
	}

	public Command buildSym(org.rascalmpl.values.uptr.ITree parseTree) {
		return buildSort(parseTree, "Sym");
	}

	public Commands buildCommands(org.rascalmpl.values.uptr.ITree parseTree) {
		return buildSort(parseTree, "Commands");
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractAST> T buildSort(org.rascalmpl.values.uptr.ITree parseTree, String sort) {
		if (TreeAdapter.isAppl(parseTree)) {
			org.rascalmpl.values.uptr.ITree tree = TreeAdapter.getStartTop(parseTree);

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
		org.rascalmpl.values.uptr.ITree tree = (org.rascalmpl.values.uptr.ITree) arg;

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
			return buildLexicalNode((org.rascalmpl.values.uptr.ITree) ((IList) ((org.rascalmpl.values.uptr.ITree) arg).get("args")).get(0));
		}

		if (sortName(tree).equals("Pattern")) {
		    if (isExternalEmbedding(tree)) {
		        return liftExternal(tree, true);
		    }
			if (isInternalEmbedding(tree)) {
				return liftInternal(tree, true);
			}
		}

		if (sortName(tree).equals("Expression")) {
		    if (isExternalEmbedding(tree)) {
		        return liftExternal(tree, false);
            }
		    if (isInternalEmbedding(tree)) {
				return liftInternal(tree, false);
			}
		}

		return buildContextFreeNode((org.rascalmpl.values.uptr.ITree) arg);
	}

	private List<AbstractAST> buildList(org.rascalmpl.values.uptr.ITree in)  {
		IList args = TreeAdapter.getListASTArgs(in);
		List<AbstractAST> result = new ArrayList<AbstractAST>(args.length());
		for (IValue arg: args) {
			result.add(buildValue(arg));
		}
		return result;
	}

	private AbstractAST buildContextFreeNode(org.rascalmpl.values.uptr.ITree tree)  {
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

		IList args = getASTArgs(tree);
		int arity = args.length();
		Object actuals[] = new Object[arity+2];
		actuals[0] = TreeAdapter.getLocation(tree);
		actuals[1] = tree;

		int i = 2;
		for (IValue arg : args) {
			org.rascalmpl.values.uptr.ITree argTree = (org.rascalmpl.values.uptr.ITree) arg;

			if (TreeAdapter.isList(argTree)) {
				actuals[i] = buildList((org.rascalmpl.values.uptr.ITree) arg);
			}
			else {
				actuals[i] = buildValue(arg);
			}
			i++;
		}

		return callMakerMethod(sort, cons, actuals, null);
	}

	private AbstractAST buildLexicalNode(org.rascalmpl.values.uptr.ITree tree) {
		String sort = capitalize(sortName(tree));

		if (sort.length() == 0) {
			throw new ImplementationError("could not retrieve sort name for " + tree);
		}
		Object actuals[] = new Object[] { TreeAdapter.getLocation(tree), tree, new String(TreeAdapter.yield(tree)) };

		return callMakerMethod(sort, "Lexical", actuals, null);
	}

	private String getPatternLayout(org.rascalmpl.values.uptr.ITree tree) {
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

	private Expression liftRec(org.rascalmpl.values.uptr.ITree tree, boolean lexicalFather, String layoutOfFather) {
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

				// this approximation is possibly harmful. Perhaps there is a chain rule defined in another module, which nevertheless
				// switched the applicable layout. Until we have a proper type-analysis there is nothing we can do here.
				if (newLayout != null) {
					layout = newLayout;
				}
			}

			java.util.List<Expression> kids = new ArrayList<Expression>(args.length());
			for (IValue arg : args) {
				Expression ast = liftRec((org.rascalmpl.values.uptr.ITree) arg, lex, layout);
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
				kids.add(liftRec((org.rascalmpl.values.uptr.ITree) arg, lexicalFather, layoutOfFather));
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

	private Expression liftHole(org.rascalmpl.values.uptr.ITree tree) {
		assert tree.asAnnotatable().hasAnnotation("holeType");
		IConstructor type = (IConstructor) tree.asAnnotatable().getAnnotation("holeType");
		tree = (org.rascalmpl.values.uptr.ITree) TreeAdapter.getArgs(tree).get(0);
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

	private IList getASTArgs(org.rascalmpl.values.uptr.ITree tree) {
		IList children = TreeAdapter.getArgs(tree);
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();
		
                for (int i = 0; i < children.length(); i++) {
			org.rascalmpl.values.uptr.ITree kid = (org.rascalmpl.values.uptr.ITree) children.get(i);
			if (!TreeAdapter.isLiteral(kid) && !TreeAdapter.isCILiteral(kid) && !TreeAdapter.isEmpty(kid)) {
				writer.append(kid);	
			} 
			// skip layout
			i++;
		}

		return writer.done();
	}

	private String sortName(org.rascalmpl.values.uptr.ITree tree) {
		if (TreeAdapter.isAppl(tree)) { 
			return TreeAdapter.getSortName(tree);
		}
		if (TreeAdapter.isAmb(tree)) {
			// all alternatives in an amb cluster have the same sort
			return sortName((org.rascalmpl.values.uptr.ITree) TreeAdapter.getAlternatives(tree).iterator().next());
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
	
	private boolean isExternalEmbedding(org.rascalmpl.values.uptr.ITree tree) {
	    String name = TreeAdapter.getConstructorName(tree);
        assert name != null;

        if (name.equals("concrete") && ((ITree) TreeAdapter.getArgs(tree).get(0)).isQuote())
            return true;
        return false;
	}

	private boolean isInternalEmbedding(org.rascalmpl.values.uptr.ITree tree) {
		String name = TreeAdapter.getConstructorName(tree);
		assert name != null;

		if (name.equals("concrete")) {
			tree = (org.rascalmpl.values.uptr.ITree) TreeAdapter.getArgs(tree).get(0);
			name = TreeAdapter.getConstructorName(tree);

			if (name.equals("$parsed")) {
				return true;
			}
		}
		return false;
	}

	private boolean isLexical(org.rascalmpl.values.uptr.ITree tree) {
		if (TreeAdapter.isRascalLexical(tree)) {
			return true;
		}
		return false;
	}
	
    private AbstractAST liftExternal(org.rascalmpl.values.uptr.ITree tree, boolean match) {
        ITree quote = (ITree) TreeAdapter.getArgs(tree).get(0);
        assert TreeAdapter.isQuote(quote);
        return liftExternalRec((IConstructor) quote.get("quoted"), match, getPatternLayout(tree));
    }

    private AbstractAST liftExternalRec(IValue value, boolean lexicalParent, String layoutOfParent) {
        if (layoutOfParent == null)
            throw new ImplementationError("layout is null");

        ISourceLocation loc = ValueFactoryFactory.getValueFactory().sourceLocation("unknown:///");

        if (value instanceof IBool) {
            BooleanLiteral.Lexical booleanLiteral =
                new BooleanLiteral.Lexical(loc, (ITree) value, ((IBool) value).getValue() ? "true" : "false");
            return new Literal.Boolean(loc, (ITree) value, booleanLiteral);
        }

        if (value instanceof INumber) {
            if (value instanceof IInteger) {
                IInteger iinteger = (IInteger) value;
                String intValue = iinteger.getStringRepresentation();
                if (intValue.startsWith("0x")) {
                    org.rascalmpl.ast.HexIntegerLiteral hexLiteral =
                        new org.rascalmpl.ast.HexIntegerLiteral.Lexical(loc, null, intValue);
                    return new IntegerLiteral.HexIntegerLiteral(loc, null, hexLiteral);
                }
                if (intValue.startsWith("0")) {
                    org.rascalmpl.ast.OctalIntegerLiteral octalLiteral =
                        new org.rascalmpl.ast.OctalIntegerLiteral.Lexical(loc, null, intValue);
                    return new IntegerLiteral.OctalIntegerLiteral(loc, null, octalLiteral);
                }
                org.rascalmpl.ast.DecimalIntegerLiteral decimalLiteral =
                    new org.rascalmpl.ast.DecimalIntegerLiteral.Lexical(loc, null, intValue);
                return new IntegerLiteral.DecimalIntegerLiteral(loc, (ITree) value, decimalLiteral);
            }
            if (value instanceof IRational) {
                IRational irational = (IRational) value;
                RationalLiteral.Lexical rationalLexical =
                    new RationalLiteral.Lexical(loc, null, irational.getStringRepresentation());
                return new Literal.Rational(loc, (ITree) value, rationalLexical);
            }
            if (value instanceof IReal) {
                IReal ireal = (IReal) value;
                RealLiteral.Lexical realLexical = new RealLiteral.Lexical(loc, null, ireal.getStringRepresentation());
                return new Literal.Real(loc, (ITree) value, realLexical);
            }
        }

        if (value instanceof ISourceLocation) {
            ISourceLocation location = (ISourceLocation) value;
            ProtocolPart.NonInterpolated protocolPart = new ProtocolPart.NonInterpolated(loc, (ITree) value,
                new ProtocolChars.Lexical(loc, null, "|" + location.getScheme()));
            PathPart.NonInterpolated pathPart = new PathPart.NonInterpolated(location, null,
                new PathChars.Lexical(loc, null, location.getAuthority() + location.getPath()));
            return new Literal.Location(loc, (ITree) value,
                new LocationLiteral.Default(loc, null, protocolPart, pathPart));
        }

        if (value instanceof IString) {
            IString string = (IString) value;
            StringConstant.Lexical constant = new Lexical(loc, null, "\"" + string.getValue() + "\"");
            NonInterpolated nonInterpolated = new StringLiteral.NonInterpolated(loc, null, constant);
            Literal.String stringLiteral = new Literal.String(loc, null, nonInterpolated);
            org.rascalmpl.semantics.dynamic.Expression.Literal literalExpression =
                new org.rascalmpl.semantics.dynamic.Expression.Literal(loc, null, stringLiteral);
            return literalExpression;
        }

        if (value instanceof ITree) { // found a hole
            IList args = TreeAdapter.getArgs((ITree) value);
            IList subArgs = TreeAdapter.getArgs((ITree) args.get(0));
            String variableType = TreeAdapter.yield((ITree) subArgs.get(2));
            String variableName = TreeAdapter.yield((ITree) subArgs.get(4));

            Name.Lexical typeNameLexical = new Name.Lexical(loc, null, variableType);
            Default def = new Default(loc, null, Arrays.asList(typeNameLexical));
            UserType.Name userType_Name = new UserType.Name(loc, null, def);
            User user = new User(loc, null, userType_Name);
            Name.Lexical nameLexical = new Name.Lexical(loc, null, variableName);

            TypedVariable typedVariable = new TypedVariable(loc, (ITree) value, user, nameLexical);
            return typedVariable;
        }

        if (value instanceof ISet) {
            List<Expression> elements = new ArrayList<>();
            for (IValue element : (ISet) value) {
                elements.add((Expression) liftExternalRec(element, lexicalParent, layoutOfParent));
            }
            return new Set(loc, (ITree) value, elements);
        }

        if (value instanceof IList) {
            IList list = (IList) value;

            List<Expression> elements = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                elements.add((Expression) liftExternalRec(list.get(i), lexicalParent, layoutOfParent));
            }

            return new org.rascalmpl.semantics.dynamic.Expression.List(loc, null, elements);
        }

        if (value instanceof IConstructor) {
            IConstructor constructor = (IConstructor) value;
            Type type = constructor.getConstructorType();

            String constructorName = type.getName();

            Name.Lexical constructorNameLexical = new Name.Lexical(loc, constructor, constructorName);

            QualifiedName.Default qualifiedName = new Default(loc, constructor, Arrays.asList(constructorNameLexical));
            org.rascalmpl.semantics.dynamic.Expression.QualifiedName qualifiedNameExpression =
                new org.rascalmpl.semantics.dynamic.Expression.QualifiedName(loc, constructor, qualifiedName);

            List<Expression> args = new ArrayList<>();
            for (int i = 0; i < type.getArity(); i++) {
                args.add((Expression) liftExternalRec(constructor.get(i), lexicalParent, layoutOfParent));
            }

            return new CallOrTree(loc, constructor, qualifiedNameExpression, args,
                new KeywordArguments_Expression.None(loc, constructor));
        }

        throw new IllegalArgumentException(
            "Trying to liftExternalRec " + value.toString() + " (" + value.getClass().getSimpleName() + " )");
    }

	private AbstractAST liftInternal(org.rascalmpl.values.uptr.ITree tree, boolean match) {
		org.rascalmpl.values.uptr.ITree concrete = (org.rascalmpl.values.uptr.ITree) TreeAdapter.getArgs(tree).get(0);
		org.rascalmpl.values.uptr.ITree fragment = (org.rascalmpl.values.uptr.ITree) TreeAdapter.getArgs(concrete).get(7);
		return liftRec(fragment, false, getPatternLayout(tree));
	}

	private static AbstractAST callMakerMethod(String sort, String cons, Object actuals[], Object keywordActuals[]) {
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
			
			return (AbstractAST) constructor.newInstance(actuals);
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
