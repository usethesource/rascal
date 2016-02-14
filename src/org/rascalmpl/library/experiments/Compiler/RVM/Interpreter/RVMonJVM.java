package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;

public class RVMonJVM extends RVM {

	/*
	 * The following instance variables are only used by executeProgram
	 */
	public Frame root; // Root frame of a program
	Thrown thrown;

	// TODO : ccf, cccf and activeCoroutines needed to allow exception handling in coroutines. :(
	
	public static IInteger Rascal_MONE;  // -1
	public static IInteger Rascal_ZERO;
	public static IInteger Rascal_ONE;
	public static IInteger Rascal_TWO;

	protected final IString NONE;
	protected final IString YIELD;
	protected final IString FAILRETURN;
	protected final IString PANIC;
	
	protected Object returnValue = null;

	private boolean debug = true;

	// Function overloading
	
	protected OverloadedFunction[] overloadedStore;

	private TypeStore typeStore = RascalValueFactory.getStore();

	public RVMonJVM(RVMExecutable rvmExec, RascalExecutionContext rex) {
		super(rvmExec, rex);

		this.rex = rex;
		rex.setRVM(this);

		Rascal_MONE = vf.integer(-1);
		Rascal_ZERO = vf.integer(0);
		Rascal_ONE = vf.integer(1);
		Rascal_TWO = vf.integer(2);

		// Return types used in code generator
		NONE = vf.string("$nothing$");
		YIELD = vf.string("$yield0$");
		FAILRETURN = vf.string("$failreturn$");
		PANIC = vf.string("$panic$");
	}

	@Override
	public IValue executeFunction(FunctionInstance func, IValue[] args) {

		Thrown oldthrown = thrown;

		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		root.sp = func.function.getNlocals();

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			root.stack[i] = args[i];
		}

		dynRun(func.function.funId, root);

		//Object o = root.stack[root.sp-1];
		
		Object o = returnValue;
		
		thrown = oldthrown;

		if (o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}
	
	public void inject(ArrayList<Function> functionStore2, ArrayList<Type> constructorStore2, TypeStore typeStore2,
			Map<String, Integer> functionMap2) {
		// TODO check if we can generate code for them.
		this.functionStore = functionStore2;
		this.constructorStore = constructorStore2;
		this.typeStore = typeStore2;
		this.functionMap = functionMap2;
	}
	
	static boolean verbose = true;
	
	private static void printFrameAndStackAndAccu(Frame cf, int sp, Object accu){
		System.err.println(cf);
		for(int i = 0; i < sp; i++){
			String isLocal = i < cf.function.getNlocals() ? "*" : " ";
			System.err.println("\t" + isLocal + i + ": " + asString(cf.stack[i]));
		}

		System.err.println("\tacc: " + asString(accu));
	}
	
	public static void debugINSTRUCTION(String insName, Frame cf, int sp, Object accu){
		if(verbose){
			printFrameAndStackAndAccu(cf, sp, accu);
			System.err.println(insName +"\n");
		}
	}
	
	public static void debugINSTRUCTION1(String insName, int arg1, Frame cf, int sp, Object accu){
		if(verbose){
			printFrameAndStackAndAccu(cf, sp, accu);
			System.err.println(insName + " " + arg1 + "\n");
		}
	}
	
	public static void debugINSTRUCTION2(String insName, String arg1, int arg2, Frame cf, int sp, Object accu){
		if(verbose){
			printFrameAndStackAndAccu(cf, sp, accu);
			System.err.println(insName + " " + arg1 + ", " + arg2 + "\n");
		}
	}

	public Object insnLOADLOCREF(Object[] stack, int pos) {
		return new Reference(stack, pos);
	}
	
	public int insnPUSHLOCREF(Object[] stack, int sp, int pos) {
		stack[sp++] = new Reference(stack, pos);
		return sp;
	}

	public int insnLOADTYPE(Object[] stack, int sp, Frame cf, int arg1) {
		stack[sp++] = cf.function.typeConstantStore[arg1];
		return sp;
	}

	public Object insnLOADLOCDEREF(Object[] stack, int pos) {
		Reference ref = (Reference) stack[pos];
		return ref.stack[ref.pos];
	}
	
	public int insnPUSHLOCDEREF(Object[] stack, int sp, int pos) {
		Reference ref = (Reference) stack[pos];
		stack[sp++] = ref.stack[ref.pos];
		return sp;
	}

	public int insnUNWRAPTHROWNLOC(Object[] stack, int sp, int target) {
		stack[target] = ((Thrown) stack[--sp]).value;
		return sp;
	}

	public void insnSTORELOCDEREF(Object[] stack, int sp, int pos) {
		Reference ref = (Reference) stack[pos];
		ref.stack[ref.pos] = stack[sp - 1];
	}

	public int insnPUSH_ROOT_FUN(Object[] stack, int sp, int fun) {
		stack[sp++] = new FunctionInstance(functionStore.get(fun), root, this);
		return sp;
	}

	public int insnPUSH_NESTED_FUN(Object[] stack, int sp, Frame cf, int fun, int scopeIn) {
		stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(fun), cf, scopeIn, this);
		return sp;
	}

	public int insnPUSHOFUN(Object[] stack, int sp, Frame cf, int ofun) {
		OverloadedFunction of = overloadedStore[ofun];
		stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this) : OverloadedFunctionInstance
				.computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
		return sp;
	}

	public int insnPUSHCONSTR(Object[] stack, int sp, int construct) {
		Type constructor = constructorStore.get(construct);
		stack[sp++] = constructor;
		return sp;
	}

	public int insnCALLJAVA(Object[] stack, int sp, Frame cf, int m, int c, int p, int k, int r) {
		int newsp = sp;
		String methodName = ((IString) cf.function.constantStore[m]).getValue();
		String className = ((IString) cf.function.constantStore[c]).getValue();
		Type parameterTypes = cf.function.typeConstantStore[p];
		Type keywordTypes = cf.function.typeConstantStore[k];
		int reflect = r;
		int arity = parameterTypes.getArity();
		try {
			newsp = callJavaMethod(methodName, className, parameterTypes, keywordTypes, reflect, stack, sp);
		} catch (Throw e) {
			//stacktrace.add(cf);
			thrown = Thrown.getInstance(e.getException(), e.getLocation(), cf);
			// postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
		} catch (Thrown e) {
			//stacktrace.add(cf);
			thrown = e;
			// postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
		} catch (Exception e) {
			e.printStackTrace(stderr);
			stderr.flush();
			throw new CompilerError("Exception in CALLJAVA: " + className + "." + methodName + "; message: " + e.getMessage() + e.getCause(), cf);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new CompilerError("Throwable in CALLJAVA: " + className + "." + methodName + "; message: " + e.getMessage() + e.getCause(), cf);
		}
		return newsp;
	}

	public int insnAPPLY(Object[] stack, int sp, int function, int arity) {
		FunctionInstance fun_instance;
		Function fun = functionStore.get(function);
		assert arity <= fun.nformals;
		assert fun.scopeIn == -1;
		fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}

	public int insnAPPLYDYN(Object[] stack, int sp, int arity) {
		FunctionInstance fun_instance;
		Object src = stack[--sp];
		if (src instanceof FunctionInstance) {
			fun_instance = (FunctionInstance) src;
			assert arity + fun_instance.next <= fun_instance.function.nformals;
			fun_instance = fun_instance.applyPartial(arity, stack, sp);
		} else {
			throw new RuntimeException("Unexpected argument type for APPLYDYN: " + asString(src));
		}
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}

	public Object insnSUBSCRIPTARRAY(Object arg_2, Object arg_1) {
		return ((Object[]) arg_2)[((Integer) arg_1)];
	}

	public Object insnSUBSCRIPTLIST(Object arg_2, Object arg_1) {
		return ((IList) arg_2).get((Integer) arg_1);
	}

	public Object insnLESSINT(Object arg_2, Object arg_1) {
		return ((Integer) arg_2) < ((Integer) arg_1) ? Rascal_TRUE : Rascal_FALSE;
	}

	public Object insnGREATEREQUALINT(Object arg_2, Object arg_1) {
		return ((Integer) arg_2) >= ((Integer) arg_1) ? Rascal_TRUE : Rascal_FALSE;
	}

	public Object insnADDINT(Object arg_2, Object arg_1) {
		return  ((Integer) arg_2) + ((Integer) arg_1);
	}

	public Object insnSUBTRACTINT(Object arg_2, Object arg_1) {
		return ((Integer) arg_2) - ((Integer) arg_1);
	}

	public Object insnANDBOOL(Object arg_2, Object arg_1) {
		return ((IBool) arg_2).and((IBool) arg_1);
	}

	public Object insnTYPEOF(Object arg_1) {
		if (arg_1 instanceof HashSet<?>) { // For the benefit of set
													// matching
			@SuppressWarnings("unchecked")
			HashSet<IValue> mset = (HashSet<IValue>) arg_1;
			if (mset.isEmpty()) {
				return tf.setType(tf.voidType());
			} else {
				IValue v = mset.iterator().next();
				return tf.setType(v.getType());
			}
		} else {
			return ((IValue) arg_1).getType();
		}
	}

	public Object insnSUBTYPE(Object arg_2, Object arg_1) {
		return vf.bool(((Type) arg_2).isSubtypeOf((Type) arg_1));
	}

	public Object insnCHECKARGTYPEANDCOPY(Object[] lstack, int lsp, Frame cof, int loc, int type, int toLoc) {
		Type argType = ((IValue) lstack[loc]).getType();
		Type paramType = cof.function.typeConstantStore[type];

		if (argType.isSubtypeOf(paramType)) {
			lstack[toLoc] = lstack[loc];
			return Rascal_TRUE;
		} else {
			return Rascal_FALSE;
		}
	}

	public void insnLABEL() {
		throw new RuntimeException("label instruction at runtime");
	}

	public void insnHALT(Object[] stack, int sp) {
		if (debug) {
			stdout.println("Program halted:");
			for (int i = 0; i < sp; i++) {
				stdout.println(i + ": " + stack[i]);
			}
		}
		return; // TODO stack[sp - 1];
	}
	
	
	// / JVM Helper methods
	public Object dynRun(String fname, IValue[] args) {
		
		int n = functionMap.get(fname);
		Function func = functionStore.get(n);
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);

		root.stack[0] = vf.list(args); // pass the program argument to
		root.stack[1] = vf.mapWriter().done();
		root.sp = func.getNlocals();

		return dynRun(n, root);
	}

	public Object dynRun(int n, Frame cf) {
		System.out.println("Unimplemented Base called !");
		return PANIC;
	}
	
	public void coreturn0Helper(final Frame cof) {
		if (cof == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		returnValue = Rascal_TRUE;
	}
	
	public void coreturn1Helper(final Object[] lstack, int sop, final Frame cof, final int arity) {
		int[] refs = cof.function.refs;
		if (arity != refs.length) {
			throw new RuntimeException("Coroutine " + cof.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
		}
		for (int i = 0; i < arity; i++) {
			Reference ref = (Reference) lstack[refs[arity - 1 - i]];
			ref.stack[ref.pos] = lstack[--sop];
		}

		returnValue = Rascal_TRUE;
	}
	
	public Object jvmCREATE(Object[] stack, int sp, Frame cf, int fun, int arity) {
		cccf = cf.getCoroutineFrame(functionStore.get(fun), root, arity, sp);
		cccf.previousCallFrame = cf;

		// lcf.sp = modified by getCoroutineFrame.
		dynRun(fun, cccf); // Run untill guard, leaves coroutine instance in stack.
		return returnValue;
	}

	public Object jvmCREATEDYN(Object[] stack, int sp, Frame cf, int arity) {
		FunctionInstance fun_instance;

		Object src = stack[--sp];

		if (!(src instanceof FunctionInstance)) {
			throw new RuntimeException("Unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
		}

		// In case of partial parameter binding
		fun_instance = (FunctionInstance) src;
		cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
		sp = cf.sp;
		cccf.previousCallFrame = cf;

		// cf.sp = modified by getCoroutineFrame.
		dynRun(fun_instance.function.funId, cccf);
		return returnValue;
	}

	public int typeSwitchHelper(final Object accu) { // stackpointer calc is done in the inline part.
		IValue val = (IValue) accu;
		Type t = null;
		if (val instanceof IConstructor) {
			t = ((IConstructor) val).getConstructorType();
		} else {
			t = val.getType();
		}
		return ToplevelType.getToplevelTypeAsInt(t);
	}

	public int switchHelper(final Object accu, final boolean useConcreteFingerprint) {
		IValue val = (IValue) accu;
		IInteger fp = vf.integer(ToplevelType.getFingerprint(val, useConcreteFingerprint));
		int toReturn = fp.intValue();
		return toReturn;
	}

	public boolean guardHelper(final Object accu) {
		boolean precondition;
		if (accu instanceof IBool) {
			precondition = ((IBool) accu).getValue();
		} else {
			throw new RuntimeException("Guard's expression has to be boolean!");
		}
		return precondition;
	}

	public void yield1Helper(Frame cf, Object[] stack, int sp, int arity, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield1 does the return

		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		returnValue = Rascal_TRUE;
		int[] refs = cf.function.refs;

		for (int i = 0; i < arity; i++) {
			Reference ref = (Reference) stack[refs[arity - 1 - i]];
			ref.stack[ref.pos] = stack[--sp];
		}

		cf.hotEntryPoint = ep;
		cf.sp = sp;

		coroutine.frame = cf;
		coroutine.suspended = true;
	}

	public void yield0Helper(Frame lcf, Object[] lstack, int lsp, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield0 does the return

		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		returnValue = Rascal_TRUE;
		
		lcf.hotEntryPoint = ep;
		lcf.sp = lsp;

		coroutine.frame = lcf;
		coroutine.suspended = true;
	}

	public Object callHelper(Object[] lstack, int lsp, Frame lcf, int funid, int arity, int ep) {
		Frame tmp;
		Function fun;
		Object rval;

		if (lcf.hotEntryPoint != ep) {
			fun = functionStore.get(funid);
			// In case of partial parameter binding
			if (arity < fun.nformals) {
				FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, lstack, lsp);
				lsp = lsp - arity;
				returnValue = fun_instance;
				lcf.sp = lsp;
				return NONE;
			}
			tmp = lcf.getFrame(fun, root, arity, lsp);
			lcf.nextFrame = tmp;
		} else {
			tmp = lcf.nextFrame;
			fun = tmp.function;
		}
		tmp.previousCallFrame = lcf;

		rval = dynRun(fun.funId, tmp); // In a full inline version we can call the
										// function directly (name is known).
		if (rval == YIELD) {
			// drop my stack
			lcf.hotEntryPoint = ep;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			lcf.hotEntryPoint = 0;
			lcf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public Object jvmNEXT0(Frame cf, Object accu) {
		
		Coroutine coroutine = (Coroutine) accu;
		
		if (!coroutine.hasNext()) {
			return Rascal_FALSE;
		} 
		// put the coroutine onto the stack of active coroutines
		activeCoroutines.push(coroutine);
		ccf = coroutine.start;
		coroutine.next(cf);

		// Push something on the stack of the prev yielding function
		coroutine.frame.stack[coroutine.frame.sp++] = null;

		coroutine.frame.previousCallFrame = cf;

		dynRun(coroutine.entryFrame.function.funId, coroutine.entryFrame);

		return returnValue;
	}

	public Object exhaustHelper(Object[] stack, int sp, Frame cf) {
		
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		if (cf.previousCallFrame == null) {
			return Rascal_FALSE;
		}
		
		returnValue = Rascal_FALSE;
		return NONE;// i.e., signal a failure;
	}

	public Object jvmOCALL(Object[] stack, int sp, Frame cf, int ofun, int arity) {
		cf.sp = sp;

		OverloadedFunctionInstanceCall ofun_call = null;
		OverloadedFunction of = overloadedStore[ofun];
	    
		Object arg0 = stack[sp - arity];
		ofun_call = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), cf, null, arity)  // changed root to cf
				                     : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), of.scopeIn, null, arity);
		
		Frame frame = ofun_call.nextFrame(functionStore);

		while (frame != null) {	
			Object rsult = dynRun(frame.function.funId, frame);
			if (rsult == NONE) {
				return returnValue; // Alternative matched.
			}
			frame = ofun_call.nextFrame(functionStore);
		}
		Type constructor = ofun_call.nextConstructor(constructorStore);
		
		sp = sp - arity;
		
		cf.sp = sp;
		returnValue = vf.constructor(constructor, ofun_call.getConstructorArguments(constructor.getArity()));
		return returnValue;
	}

	public int jvmOCALLDYN(Object[] lstack, int sop, Frame lcf, int typesel, int arity) {
		Object funcObject = lstack[--sop];
		OverloadedFunctionInstanceCall ofunCall = null;
		lcf.sp = sop;

		// Get function types to perform a type-based dynamic
		// resolution
		Type types = lcf.function.codeblock.getConstantType(typesel);
		// Objects of two types may appear on the stack:
		// 1. FunctionInstance due to closures whom will have no overloading
		if (funcObject instanceof FunctionInstance) {
			FunctionInstance fun_instance = (FunctionInstance) funcObject;
			Frame frame = lcf.getFrame(fun_instance.function, fun_instance.env, arity, sop);
			frame.previousCallFrame = lcf;
			
			dynRun(frame.function.funId, frame);
			return lcf.sp;
		}
		// 2. OverloadedFunctionInstance due to named Rascal
		// functions
		OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
		ofunCall = new OverloadedFunctionInstanceCall(lcf, of_instance.getFunctions(), of_instance.getConstructors(), of_instance.env, types, arity);

		boolean stackPointerAdjusted = false;
		Frame frame = ofunCall.nextFrame(functionStore);
		while (frame != null) {
			stackPointerAdjusted = true; // See text at OCALL
			Object rsult = dynRun(frame.function.funId, frame);
			if (rsult == NONE) {
				return lcf.sp; // Alternative matched.
			}
			frame = ofunCall.nextFrame(functionStore);
		}
		Type constructor = ofunCall.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false) {
			sop = sop - arity;
		}
		returnValue = vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
		lcf.sp = sop;
		return sop;
	}

	public Object calldynHelper(Object[] lstack, int lsp, final Frame lcf, int arity, int ep) {
		Frame tmp;
		Object rval;

		if (lcf.hotEntryPoint != ep) {
			if (lstack[lsp - 1] instanceof Type) {
				Type constr = (Type) lstack[--lsp];
				arity = constr.getArity();
				IValue[] args = new IValue[arity];
				for (int i = arity - 1; i >= 0; i--) {
					args[i] = (IValue) lstack[lsp - arity + i];
				}
				lsp = lsp - arity;
				returnValue = vf.constructor(constr, args);
				lcf.sp = lsp;
				return NONE; // DO not return continue execution
			}

			if (lstack[lsp - 1] instanceof FunctionInstance) {
				FunctionInstance fun_instance = (FunctionInstance) lstack[--lsp];
				// In case of partial parameter binding
				if (fun_instance.next + arity < fun_instance.function.nformals) {
					fun_instance = fun_instance.applyPartial(arity, lstack, lsp);
					lsp = lsp - arity;
					returnValue = fun_instance;
					lcf.sp = lsp;
					return NONE;
				}
				tmp = lcf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, lsp);
				lcf.nextFrame = tmp;
			} else {
				throw new RuntimeException("Unexpected argument type for CALLDYN: " + asString(lstack[lsp - 1]));
			}
		} else {
			tmp = lcf.nextFrame;
		}

		tmp.previousCallFrame = lcf;

		rval = dynRun(tmp.function.funId, tmp); // In a inline version we can call the
												// function directly.
		if (rval == YIELD) {
			// Save reentry point
			lcf.hotEntryPoint = ep;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			lcf.hotEntryPoint = 0;
			lcf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public Thrown thrownHelper(Frame cf, Object[] stack, int sp) {
		Object obj = stack[sp];
		Thrown thrown = null;
		if (obj instanceof IValue) {
			thrown = Thrown.getInstance((IValue) obj, null, cf);
		} else {
			thrown = (Thrown) obj;
		}
		return thrown;
	}

	public static Object anyDeserialize(String s) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(s));
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
