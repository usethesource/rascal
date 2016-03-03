/**
 * 
 */
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public class RVMJVM extends RVMInterpreter {

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
			// make sure that the moduleVariables in this RVM and in the generated class are the same.
			this.moduleVariables = generatedClassInstance.moduleVariables;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object executeRVMFunction(Function func, IValue[] posArgs, Map<String,IValue> kwArgs){
		// Assumption here is that the function called is not a nested one
		// and does not use global variables
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < posArgs.length; i++){
			cf.stack[i] = posArgs[i]; 
		}
		cf.stack[func.nformals-1] =  kwArgs; // new HashMap<String, IValue>();
		cf.sp = func.getNlocals();
		//cf.stack[func.nformals] = kwArgs == null ? new HashMap<String, IValue>() : kwArgs;
		generatedClassInstance.dynRun(func.funId, cf);
		
		Object returnValue = generatedClassInstance.returnValue;
		if(returnValue instanceof Thrown){
			throw (Thrown) returnValue;
		}
		return returnValue;
	}

	// Implements abstract function for RVMonJVM
	@Override
	public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] args){		
		Function firstFunc = functionStore.get(func.getFunctions()[0]); // TODO: null?
		int arity = args.length;
		int scopeId = func.env.scopeId;
		Frame root = new Frame(scopeId, null, func.env, arity+2, firstFunc);

		// Pass the program arguments to func
		for(int i = 0; i < args.length; i++) {
			root.stack[i] = args[i]; 
		}
		root.sp = args.length;
		root.previousCallFrame = null;

		OverloadedFunctionInstanceCall ofunCall = new OverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), func.env, null, arity);

		Frame frame = ofunCall.nextFrame(functionStore);
		while (frame != null) {
			Object rsult = generatedClassInstance.dynRun(frame.function.funId, frame);
			if (rsult == NONE) {
				return narrow(generatedClassInstance.returnValue); // Alternative matched.
			}
			frame = ofunCall.nextFrame(functionStore);
		}
		Type constructor = ofunCall.nextConstructor(constructorStore);

		return vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
	}

	// Implements abstract function for RVMonJVM
	@Override
	public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] args, HashMap<String,IValue> kwArgs) {

		rex.setCurrentModuleName(moduleName);

		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw new RuntimeException("PANIC: No function " + uid_main + " found");
		}

		//Thrown oldthrown = generatedClassInstance.thrown;	// <===
		
		generatedClassInstance.dynRun(uid_main, args);
		
		//generatedClassInstance.thrown = oldthrown;
		
		Object returnValue = generatedClassInstance.returnValue;
		if (returnValue != null && returnValue instanceof Thrown) {
			throw (Thrown) returnValue;
		}
		return narrow(returnValue);
	}
}
