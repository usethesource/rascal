/**
 * 
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
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
		// Oneshot classloader
		try {
			Class<?> generatedClass = new ClassLoader(RVMJVM.class.getClassLoader()) {
				public Class<?> defineClass(String name, byte[] bytes) {
					return super.defineClass(name, bytes, 0, bytes.length);
				}

				public Class<?> loadClass(String name) throws ClassNotFoundException {
				    try { 
				        return super.loadClass(name); 
				    } catch (ClassNotFoundException e) {
				        // then its a library class we need
				    }

				    // let's try the classloaders as configured in pathConfig:
				    for (ClassLoader l : classLoaders) {
				        try {
				            // TODO: group URLClassLoaders into a single instance 
				            // to enhance class loading performance
				            return l.loadClass(name);
				        }
				        catch (ClassNotFoundException e) {
				            // this is normal, try next loader
				            continue;
				        }
				    }
				    
				    throw new ClassNotFoundException(name);
				}
			}.defineClass(generatedClassName, generatedByteCode);

			Constructor<?>[] cons = generatedClass.getConstructors();

			generatedClassInstance = (RVMonJVM) cons[0].newInstance(rvmExec, rex);
			// make sure that the moduleVariables in this RVM and in the generated class are the same.
			this.moduleVariables = generatedClassInstance.moduleVariables;
			generatedClassInstance.frameObserver = this.frameObserver = rex.getFrameObserver();
			setupInvokeDynamic(generatedClass);
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	}
	
	private void setupInvokeDynamic(Class<?> generatedClass) throws NoSuchMethodException, IllegalAccessException{
        MethodHandles.Lookup lookup = MethodHandles.lookup(); 
        
        MethodType methodType = MethodType.methodType(Object.class, Frame.class);

        for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
            String fname = e.getKey();
            Integer findex = e.getValue();
            String methodName = BytecodeGenerator.rvm2jvmName(fname);
            MethodHandle mh = lookup.findVirtual(generatedClass, methodName, methodType);
            functionStore[findex].handle = new ConstantCallSite(mh).dynamicInvoker();
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
