/**
 * 
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.TypeOf;

public class RVMJVM extends RVM {

	RVMExecutable rrs;
	RascalExecutionContext rex;
	byte[] generatedRunner = null;
	RVMRun runner = null;

	/*
	 * 
	 */
	private static final long serialVersionUID = -3447489546163673435L;

	/**
	 * @param rrs
	 * @param rex
	 */
	public RVMJVM(RVMExecutable rrs, RascalExecutionContext rex) {
		super(rrs, rex);
		if (rrs instanceof RVMJVMExecutable) {
			generatedRunner = ((RVMJVMExecutable) rrs).jvmByteCode;
		}
		this.rrs = rrs;
		this.rex = rex;
		try {
			createRunner();
		}
		catch(Exception e) {
			e.printStackTrace() ;
		}

	}

	private void createRunner() {
		// Oneshot classloader
		try {
			Class<?> generatedClass = new ClassLoader(RVMJVM.class.getClassLoader()) {
				public Class<?> defineClass(String name, byte[] bytes) {
					return super.defineClass(name, bytes, 0, bytes.length);
				}

				public Class<?> loadClass(String name) {
					try {
						return super.loadClass(name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					return null;
				}
			}.defineClass("org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMRunner", generatedRunner);

			Constructor<?>[] cons = generatedClass.getConstructors();

			runner = (RVMRun) cons[0].newInstance(rrs, rex);
			// Inject is obsolete the constructor holds rrs.
			runner.inject(rrs.functionStore, rrs.constructorStore, RVMExecutable.store, rrs.functionMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean useRVMInterpreter = false;
	public IValue executeProgram(String moduleName, String uid_main, IValue[] args) {
		if (useRVMInterpreter) {
			return super.executeProgram(moduleName, uid_main, args);
		} else {
			rex.setCurrentModuleName(moduleName);

			Function main_function = functionStore.get(functionMap.get(uid_main));

			if (main_function == null) {
				throw new RuntimeException("PANIC: No function " + uid_main + " found");
			}

			if (main_function.nformals != 2) { // List of IValues and empty map of
												// keyword parameters
				throw new RuntimeException("PANIC: function " + uid_main + " should have two arguments");
			}

			Object o = null;

			o = runner.dynRun(uid_main, args);
			if (o != null && o instanceof Thrown) {
				throw (Thrown) o;
			}
			return narrow(o);
		}
	}

	protected Object executeProgram(Frame root, Frame cf) {
		// return super.executeProgram(root, cf) ;
		return runner.dynRun(root.function.funId, root);
	}
}
