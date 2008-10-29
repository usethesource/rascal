package org.meta_environment.rascal.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.ASTFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Module;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.uptr.TreeAdapter;

/**
 * Uses reflection to construct an AST hierarchy from a 
 * UPTR parse tree of a rascal program.
 *
 */
public class ASTBuilder {
	private ASTFactory factory;
    private Class<? extends ASTFactory> clazz;
    
	public ASTBuilder(ASTFactory factory) {
		this.factory = factory;
		this.clazz = factory.getClass();
	}
	
	public Module buildModule(ITree parseTree) {
		return null;

	}
	
	public Expression buildExpression(ITree expression) {
		return null;

	}
	
	public Statement buildStatement(ITree statement) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private AbstractAST buildNode(ITree in) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		TreeAdapter tree = new TreeAdapter(in);
		
		String cons = tree.getConstructorName();
		String sort = tree.getProduction().getSortName();
		String Sort = capitalize(sort);
		String Cons = capitalize(cons);
		
		if (tree.isContextFree()) {
		  IList args = tree.getContextFreeArgs();
		  int arity = args.length() + 1;
		  Class<?> formals[] = new Class<?>[arity];
		  Object actuals[] = new AbstractAST[arity];
		  
		  formals[0] = tree.getClass();
		  actuals[0] = in;
		  
		  int i = 1;
		  for (IValue arg : args) {
			  actuals[i] = buildValue(arg); 
			  formals[i] = actuals[i].getClass();
			  i++;
		  }
		  
		  Method make = clazz.getMethod("make" + Sort + Cons, formals);
		  return (AbstractAST) make.invoke(factory, actuals);
		}
		
		// TODO implement other cases
		return null;
	}

	private AbstractAST buildValue(IValue arg) {
		// TODO Auto-generated method stub
		return null;
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
