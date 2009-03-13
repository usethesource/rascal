package org.meta_environment.rascal.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Command;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.JavaFunctionBody;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.asserts.Ambiguous;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.uptr.TreeAdapter;


/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse node of a rascal program.
 *
 */
public class ASTBuilder {
	private ASTFactory factory;
    private Class<? extends ASTFactory> clazz;
    
	public ASTBuilder(ASTFactory factory) {
		this.factory = factory;
		this.clazz = factory.getClass();
	}
	
	public Module buildModule(IConstructor parseTree) throws FactTypeUseException {
		return buildSort(parseTree, "Module");
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

		
		if (treeAdapter.getSortName().equals(sort)) {
			return (T) buildValue(tree);
		} else {
			throw new ImplementationError("This is not a" + sort +  ": "
					+ new TreeAdapter(tree).yield());
		}
	}
	
	private List<AbstractAST> buildList(IConstructor in)  {
		IList args = new TreeAdapter(in).getListASTArgs();
		List<AbstractAST> result = new ArrayList<AbstractAST>();
		for (IValue arg: args) {
			result.add(buildValue(arg));
		}
		return result;
	}

	private AbstractAST buildContextFreeNode(IConstructor in)  {
		try {
			TreeAdapter tree = new TreeAdapter(in);

			String cons = tree.getConstructorName();
			String sort = tree.getProduction().getSortName();
			sort = sort.equalsIgnoreCase("pattern") ? "Expression" : capitalize(sort); 
			cons = capitalize(cons);
			
			IList args = tree.getASTArgs();
			int arity = args.length() + 1;
			Class<?> formals[] = new Class<?>[arity];
			Object actuals[] = new Object[arity];

			formals[0] = INode.class;
			actuals[0] = in;

			int i = 1;
			for (IValue arg : args) {
				TreeAdapter argTree = new TreeAdapter((IConstructor) arg);
				if (argTree.isList()) {
					actuals[i] = buildList((IConstructor) arg);
					formals[i] = List.class;
				}
				else {
					actuals[i] = buildValue(arg);
					formals[i] = actuals[i].getClass().getSuperclass();
				}
				i++;
			}

			Method make = clazz.getMethod("make" + sort + cons, formals);
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
	
	private AbstractAST buildAmbNode(INode node, ISet alternatives) {
		try {
			String sort = null;
			List<AbstractAST> alts = new ArrayList<AbstractAST>();

			for (IValue elem : alternatives) {
				TreeAdapter alt = new TreeAdapter((IConstructor) elem);

				if (alt.isList()) {
					// TODO add support for ambiguous lists
					throw new Ambiguous("Can not deal with ambiguous list: " + 
							node);
				}
				else if (sort == null) {
					sort = alt.getSortName();
				}
				
				alts.add(buildValue(elem));
			}
			
			if (alts.size() == 0) {
				throw new ImplementationError("bug: Ambiguity without children!?! " + node);
			}

			sort = capitalize(sort);
			Class<?> formals[] = new Class<?>[]  { INode.class, List.class };
			Object actuals[] = new Object[] { node, alts };

			Method make = clazz.getMethod("make" + sort + "Ambiguity", formals);
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
	
	private AbstractAST buildLexicalNode(IConstructor in) {
		try {
			TreeAdapter tree = new TreeAdapter(in);

			String sort = tree.getProduction().getSortName();
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
		return new ImplementationError("Unexpected error in AST construction", e);
	}

	private AbstractAST buildValue(IValue arg)  {
		TreeAdapter tree = new TreeAdapter((IConstructor) arg);
		
		if (tree.isAmb()) {
			return buildAmbNode((INode) arg, tree.getAlternatives());
		}
		
		if (!tree.isAppl()) {
			throw new UnsupportedOperationException();
		}	
		
		if (tree.isLexToCf()) {
			return buildLexicalNode((IConstructor) ((IList) ((IConstructor) arg).get("args")).get(0));
		}
		else if (tree.getSortName().equals("FunctionBody") && tree.getConstructorName().equals("Java")) {
			return new JavaFunctionBody((INode) arg, tree.yield());
		}
			
		return buildContextFreeNode((IConstructor) arg);
	}



	private String capitalize(String sort) {
		if (sort.length() > 1) {
		  return Character.toUpperCase(sort.charAt(0)) + sort.substring(1);
		}
		else {
			return sort.toUpperCase();
		}
	}

}
