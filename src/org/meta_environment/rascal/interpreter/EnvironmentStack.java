package org.meta_environment.rascal.interpreter;

import java.util.Stack;

import org.eclipse.imp.pdb.facts.type.TupleType;
import org.meta_environment.rascal.ast.FunctionDeclaration;

public class EnvironmentStack extends Environment {
  private final Stack<Environment> stack = new Stack<Environment>();
  private final TypeEvaluator types = new TypeEvaluator();
  
  public EnvironmentStack() {
	  stack.push(new Environment());
  }
  
  public void push() {
	  stack.push(new Environment());
  }
  
  public void pop() {
	stack.pop();
  }
  
  @Override
  public FunctionDeclaration getFunction(String name, TupleType actuals) {
	  for (int i = stack.size() - 1; i >= 0; i--) {
		  FunctionDeclaration result = stack.get(i).getFunction(name, actuals);
		  
		  if (result != null) {
			  return result;
		  }
	  }
	  
	  throw new RascalTypeError("Call to undefined function " + name + " with argument types " + actuals);
  }

  @Override
  public EvalResult getVariable(String name) {
	  for (int i = stack.size() - 1; i >= 0; i--) {
		  EvalResult result = stack.get(i).getVariable(name);
		  
		  if (result != null) {
			  return result;
		  }
	  }
	  
	  throw new RascalTypeError("Reference to undefined variable " + name);
  }

  @Override
  public void storeFunction(String name, FunctionDeclaration function) {
	  TupleType formals = (TupleType) function.getSignature().accept(types);
	  int i;
	  for (i = stack.size() - 1; i >= 0; i--) {
		  FunctionDeclaration result = stack.get(i).getFunction(name, formals);
		  
		  if (result != null) {
			  break;
		  }
	  }
	  
	  if (i == -1) {
	    stack.peek().storeFunction(name, function);
	  }
	  else {
		  stack.get(i).storeFunction(name, function);
	  }
  }

  @Override
  public void storeVariable(String name, EvalResult value) {
	  int i;
	  for (i = stack.size() - 1; i >= 0; i--) {
		  EvalResult result = stack.get(i).getVariable(name);
		  
		  if (result != null) {
			  break;
		  }
	  }
	  
	  if (i == -1) {
	    stack.peek().storeVariable(name, value);
	  }
	  else {
		stack.get(i).storeVariable(name, value);
	  }
  }


}
