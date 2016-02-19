/**
 * 
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.rascalmpl.value.IValue;

public class RVMJVM extends RVM {

	RVMExecutable rvmExec;
	RascalExecutionContext rex;
	byte[] generatedRunner = null;
	String generatedName = null;
	RVMonJVM runner = null;

	/**
	 * @param rvmExec
	 * @param rex
	 */
	public RVMJVM(RVMExecutable rvmExec, RascalExecutionContext rex) {
		super(rvmExec, rex);

		generatedRunner = rvmExec.getJvmByteCode();
		generatedName = rvmExec.getFullyQualifiedDottedName();

		this.rvmExec = rvmExec;
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
			}.defineClass(generatedName, generatedRunner);

			Constructor<?>[] cons = generatedClass.getConstructors();

			runner = (RVMonJVM) cons[0].newInstance(rvmExec, rex);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IValue executeProgram(String moduleName, String uid_main, IValue[] args, HashMap<String,IValue> kwArgs) {

		rex.setCurrentModuleName(moduleName);

		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw new RuntimeException("PANIC: No function " + uid_main + " found");
		}

		runner.dynRun(uid_main, args);
		Object o = runner.returnValue;
		if (o != null && o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}

	protected Object executeProgram(Frame root, Frame cf) {
		runner.dynRun(root.function.funId, root);
		return runner.returnValue;
	}
}
