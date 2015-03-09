package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;


// Interface to allow multiple implementations of the RVM.
public interface IRVM {
	public IValue executeProgram(String string, String uid_module_init, IValue[] arguments);
	public IValue executeFunction(String uid_main, IValue[] args) ;
	public IValue executeFunction(FunctionInstance functionInstance, IValue[] args);

	public RascalExecutionContext getRex() ;
	public Type symbolToType(IConstructor symbol) ;
	public void declare(Function f) ;
	public void declareConstructor(String name, IConstructor symbol) ;
	public void addResolver(IMap resolver) ;
	public void fillOverloadedStore(IList overloadedStore) ;
	
	public PrintWriter getStdErr();
	public IEvaluatorContext getEvaluatorContext();
	public IValueFactory     getValueFactory() ;
	public void resetLocationCollector();
	public void setLocationCollector(ILocationCollector collector);
	public PrintWriter getStdOut();
	public void validateInstructionAdressingLimits();
}
