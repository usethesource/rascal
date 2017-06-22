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
import org.rascalmpl.uri.classloaders.PathConfigClassLoader;
import io.usethesource.vallang.IValue;

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
		try {
			Class<?> generatedClass = new ClassLoader(new PathConfigClassLoader(rex.getPathConfig(), getClass().getClassLoader())) {
			    // This is a oneshot classloader to facilitate garbage collection of older versions and re-initialisation of
		        // dependending classloaders. We pass a PathConfigClassLoader as parent such that the generated code and the
			    // builtins it depends on can find Java classes used by builtin functions.
                public Class<?> defineClass(String name, byte[] bytes) {
					return super.defineClass(name, bytes, 0, bytes.length);
				}
                
                public java.lang.Class<?> loadClass(String name) throws ClassNotFoundException {
                    if (name.equals(generatedClassName)) {
                        return super.loadClass(name);
                    }
                    
                    // essential to directly call getParent().loadClass and not
                    // super.loadClass() because this will call parent.loadClass(String,bool)
                    // which is not overridable and this will break the semantics of PathConfigClassLoader.
                    return getParent().loadClass(name);
                };
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
        
        MethodType funType = MethodType.methodType(Object.class, Frame.class);
        MethodType rtType = MethodType.methodType(Object.class, RVMonJVM.class, Frame.class);

        for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
            String fname = e.getKey();
            Integer findex = e.getValue();
            Function func = functionStore[findex];
            String methodName = BytecodeGenerator.rvm2jvmName(fname);
            MethodHandle mh = lookup.findVirtual(generatedClass, methodName, funType);
            func.handle = new ConstantCallSite(mh.asType(rtType)).dynamicInvoker();
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
	        throw new InternalCompilerError("Class " + className + " not found", e1);
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
