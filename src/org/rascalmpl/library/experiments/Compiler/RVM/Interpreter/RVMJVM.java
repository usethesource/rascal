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
	byte[] generatedByteCode = null;
	String generatedClassName = null;
	RVMonJVM generatedClassInstance = null;

	/**
	 * @param rvmExec
	 * @param rex
	 */
	public RVMJVM(RVMExecutable rvmExec, RascalExecutionContext rex) {
		super(rvmExec, rex);

		generatedByteCode = rvmExec.getJvmByteCode();
		generatedClassName = rvmExec.getFullyQualifiedDottedName();

		this.rvmExec = rvmExec;
		this.rex = rex;
		try {
			createGeneratedClassInstance();
		}
		catch(Exception e) {
			e.printStackTrace() ;
		}
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public IValue executeProgram(String moduleName, String uid_main, IValue[] args, HashMap<String,IValue> kwArgs) {

		rex.setCurrentModuleName(moduleName);

		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw new RuntimeException("PANIC: No function " + uid_main + " found");
		}

		rex.getStdErr().println("Running RVMJVM.executeProgram: " + uid_main);
		
		generatedClassInstance.dynRun(uid_main, args);
		Object o = generatedClassInstance.returnValue;
		if (o != null && o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}

	protected Object executeProgram(Frame root, Frame cf) {
		generatedClassInstance.dynRun(root.function.funId, root);
		return generatedClassInstance.returnValue;
	}
	
	@Override
	public IValue executeFunction(OverloadedFunctionInstance func, IValue[] args){
		Function firstFunc = functionStore.get(func.getFunctions()[0]); // TODO: null?
		int arity = args.length;
		int scopeId = func.env.scopeId;
		Frame root = new Frame(scopeId, null, func.env, arity+2, firstFunc);
		root.sp = arity;
		
		OverloadedFunctionInstanceCall c_ofun_call_next = 
				scopeId == -1 ? new OverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), root, null, arity)  // changed root to cf
        					  : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), scopeId, null, arity);
				
		Frame cf = c_ofun_call_next.nextFrame(functionStore);
		// Pass the program arguments to func
		for(int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i]; 
		}
		cf.sp = args.length;
		cf.previousCallFrame = null;		// ensure that func will retrun here
		Object o = null; // = executeProgram(root, cf, /*arity,*/ /*cf.function.codeblock.getInstructions(),*/ c_ofun_call_next);
		
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o); 
	}
}
