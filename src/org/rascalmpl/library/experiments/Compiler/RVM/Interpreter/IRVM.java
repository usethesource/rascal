package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public interface IRVM {
	public IValue executeProgram(String uid_main, IValue[] args) ;
	public IValue executeFunction(String uid_main, IValue[] args) ;
	public RascalExecutionContext getRex() ;
	public IValue executeFunction(FunctionInstance functionInstance, IValue[] args);
	public Type symbolToType(IConstructor symbol) ;
	public void declare(Function f) ;
	public void declareConstructor(String name, IConstructor symbol) ;
	public void addResolver(IMap resolver) ;
	public void fillOverloadedStore(IList overloadedStore) ;
}
