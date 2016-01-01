/**
 * 
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.uri.classloaders.PathConfigClassLoader;
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
		System.err.println("RVMJVM (" + rvmExec.getModuleName() + "): " + generatedByteCode.length + " bytes generatedByteCode");
		createGeneratedClassInstance();
	}

	private void createGeneratedClassInstance() {
		try {
			Class<?> generatedClass = new ClassLoader(new PathConfigClassLoader(rex.getPathConfig(), getClass().getClassLoader())) {
			    // This is a oneshot classloader to facilitate garbage collection of older versions and re-initialisation of
		        // dependending classloaders. We pass a PathConfigClassLoader as parent such that the generated code and the
			    // builtins it depends on can find Java classes used by builtin functions.
                public Class<?> defineClass(String name, byte[] bytes) {
					return super.defineClass(name, bytes, 0, bytes.length);
				}
			}.defineClass(generatedClassName, generatedByteCode);

			Constructor<?>[] cons = generatedClass.getConstructors();

			generatedClassInstance = (RVMonJVM) cons[0].newInstance(rvmExec, rex);
			// make sure that the moduleVariables in this RVM and in the generated class are the same.
			this.moduleVariables = generatedClassInstance.moduleVariables;
			generatedClassInstance.frameObserver = this.frameObserver = rex.getFrameObserver();

		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}
	
	@Override
	public Class<?> getJavaClass(String className) {
	    Class<?> clazz = classCache.get(className);
	    if(clazz != null){
	        return clazz;
	    }

	    try {
	        clazz = generatedClassInstance.getClass().getClassLoader().loadClass(className);
	        classCache.put(className, clazz);
	        return clazz;
	    } 
	    catch(ClassNotFoundException | NoClassDefFoundError e1) {
	        throw new CompilerError("Class " + className + " not found", e1);
	    }
	}
	
	@Override
	public void setFrameObserver(IFrameObserver observer){
	    generatedClassInstance.frameObserver = this.frameObserver = observer;
	    this.rex.setFrameObserver(observer);
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
	public IValue executeRVMFunction(FunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs) {
		return generatedClassInstance.executeRVMFunction(func, posArgs, kwArgs);
	}

	@Override
	public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs) {
		return generatedClassInstance.executeRVMFunction(func, posArgs, kwArgs);
	}

	@Override
	public IValue executeRVMFunctionInVisit(Frame root) {
		return generatedClassInstance.executeRVMFunctionInVisit(root);
	}

	@Override
	public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] posArgs, Map<String, IValue> kwArgs) {
		return generatedClassInstance.executeRVMProgram(moduleName, uid_main, posArgs, kwArgs);
	}
}
