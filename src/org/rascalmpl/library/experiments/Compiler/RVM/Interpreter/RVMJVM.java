/**
 * 
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.rascalmpl.value.IValue;

public class RVMJVM extends RVMCore {

	final RVMExecutable rvmExec;
	final byte[] generatedByteCode;
	final String generatedClassName;
	RVMonJVM generatedClassInstance;

	/**
	 * @param rvmExec
	 * @param rex
	 */
	public RVMJVM(RVMExecutable rvmExec, RascalExecutionContext rex) {
		super(rvmExec, rex);

		generatedByteCode = rvmExec.getJvmByteCode();
		generatedClassName = rvmExec.getFullyQualifiedDottedName();

		this.rvmExec = rvmExec;
		
		createGeneratedClassInstance();
	}

	private void createGeneratedClassInstance() {
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
			}.defineClass(generatedClassName, generatedByteCode);

			Constructor<?>[] cons = generatedClass.getConstructors();

			generatedClassInstance = (RVMonJVM) cons[0].newInstance(rvmExec, rex);
			// make sure that the moduleVariables in this RVM and in the generated class are the same.
			this.moduleVariables = generatedClassInstance.moduleVariables;

		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}
	
	/************************************************************************************/
	/*		Implementation of abstract methods in RVMCore for RVMJVM					*/
	/*		(simply reroute to RVMonJVM)												*/
	/************************************************************************************/

	@Override
	public Object executeRVMFunction(Function func, IValue[] posArgs, Map<String, IValue> kwArgs) {
		return generatedClassInstance.executeRVMFunction(func, posArgs, kwArgs);
	}

	@Override
	public IValue executeRVMFunction(FunctionInstance func, IValue[] posAndKwArgs) {
		return generatedClassInstance.executeRVMFunction(func, posAndKwArgs);
	}

	@Override
	public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] posAndKwArgs) {
		// TODO Auto-generated method stub
		return generatedClassInstance.executeRVMFunction(func, posAndKwArgs);
	}

	@Override
	public IValue executeRVMFunctionInVisit(Frame root) {
		// TODO Auto-generated method stub
		return generatedClassInstance.executeRVMFunctionInVisit(root);
	}

	@Override
	public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] posArgs, Map<String, IValue> kwArgs) {
		// TODO Auto-generated method stub
		return generatedClassInstance.executeRVMProgram(moduleName, uid_main, posArgs, kwArgs);
	}
}
