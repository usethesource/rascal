package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class RVMJVMExecutable extends RVMExecutable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6074410394887825412L;

	public byte[] jvmByteCode;

	public RVMJVMExecutable(String module_name, IMap tags, IMap symbol_definitions, Map<String, Integer> functionMap, ArrayList<Function> functionStore,
			Map<String, Integer> constructorMap, ArrayList<Type> constructorStore, Map<String, Integer> resolver, ArrayList<OverloadedFunction> overloadedStore,
			ArrayList<String> initializers, ArrayList<String> testsuites, String uid_module_init, String uid_module_main, String uid_module_main_testsuite, TypeStore ts,
			IValueFactory vfactory) {
		super(module_name, tags, symbol_definitions, functionMap, functionStore, constructorMap, constructorStore, resolver, overloadedStore, initializers, testsuites,
				uid_module_init, uid_module_main, uid_module_main_testsuite, ts, vfactory);
		
		buildRunnerByteCode(false);
	}
	public void buildRunnerByteCode(boolean profile) {
			try {
				// TODO; in the future create classes with the same name as a Rascal module
				String packageName = "org.rascalmpl.library.experiments.Compiler.RVM.Interpreter";
				String className = "RVMRunner";

				BytecodeGenerator codeEmittor = new BytecodeGenerator(packageName, className, functionStore, overloadedStore, functionMap, constructorMap, resolver);

				codeEmittor.buildClass() ;
				
				jvmByteCode = codeEmittor.finalizeCode();
				codeEmittor.dump("/Users/ferryrietveld/tmp/Running.class");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
