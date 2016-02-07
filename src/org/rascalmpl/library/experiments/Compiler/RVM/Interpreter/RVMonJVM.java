package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import javax.xml.bind.DatatypeConverter;

import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.DescendantDescriptor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.DIRECTION;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.FIXEDPOINT;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.PROGRESS;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse.Traverse.REBUILD;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
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

	private boolean debug = true;

	// Function overloading
	
	protected OverloadedFunction[] overloadedStore;

	private TypeStore typeStore = RascalValueFactory.getStore();

	// Management of active coroutines
	//protected Stack<Coroutine> activeCoroutines = new Stack<>();
	//protected Frame ccf = null; 	// The start frame of the current active coroutine
									// (coroutine's main function)
	//protected Frame cccf = null; 	// The candidate coroutine's start frame; used
									// by the guard semantics


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

		Object o = dynRun(func.function.funId, root);

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
//		this.overloadedStore = overloadedStore2;
		this.constructorStore = constructorStore2;
		this.typeStore = typeStore2;
		this.functionMap = functionMap2;
	}


	public Object insnLOADLOCREF(Object[] stack, int pos) {
		return new Reference(stack, pos);
	}
	
	public int insnPUSHLOCREF(Object[] stack, int lsp, int args1) {
		stack[lsp++] = new Reference(stack, args1);
		return lsp;
	}

	public int insnLOADTYPE(Object[] stack, int sp, Frame cf, int arg1) {
		stack[sp++] = cf.function.typeConstantStore[arg1];
		return sp;
	}

	public Object insnLOADLOCDEREF(Object[] stack, int loc) {
		Reference ref = (Reference) stack[loc];
		return ref.stack[ref.pos];
	}
	
	public int insnPUSHLOCDEREF(Object[] stack, int sp, int loc) {
		Reference ref = (Reference) stack[loc];
		stack[sp++] = ref.stack[ref.pos];
		return sp;
	}

	public int insnUNWRAPTHROWNLOC(Object[] stack, int sp, int target) {
		stack[target] = ((Thrown) stack[--sp]).value;
		return sp;
	}

	public void insnSTORELOCDEREF(Object[] stack, int sp, int loc) {
		Reference ref = (Reference) stack[loc];
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

//	public int insnUNWRAPTHROWNVAR(Object[] stack, int sp, Frame cf, int scopeid, int pos, boolean maxarg2) {
//		if (maxarg2) {
//			IValue mvar = cf.function.constantStore[scopeid];
//			moduleVariables.put(mvar, (IValue) stack[sp - 1]);
//			return sp;
//		}
//		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
//			if (fr.scopeId == scopeid) {
//				// TODO: We need to re-consider how to guarantee safe use of
//				// both Java objects and IValues
//				fr.stack[pos] = ((Thrown) stack[--sp]).value;
//				return sp;
//			}
//		}
//		throw new RuntimeException("UNWRAPTHROWNVAR cannot find matching scope: " + scopeid);
//	}

//	@SuppressWarnings("unchecked")
//	public int insnCALLCONSTR(Object[] stack, int sp, int constrctr, int arity) {
//		Type constructor = constructorStore.get(constrctr);
//
//		IValue[] args = new IValue[constructor.getArity()];
//
//		java.util.Map<String, IValue> kwargs;
//		Type type = (Type) stack[--sp];
//		if (type.getArity() > 0) {
//			// Constructors with keyword parameters
//			kwargs = (java.util.Map<String, IValue>) stack[--sp];
//		} else {
//			kwargs = new HashMap<String, IValue>();
//		}
//
//		for (int i = 0; i < constructor.getArity(); i++) {
//			args[constructor.getArity() - 1 - i] = (IValue) stack[--sp];
//		}
//		stack[sp++] = vf.constructor(constructor, args, kwargs);
//
//		return sp;
//	}

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

	public int insnAPPLY(Object[] lstack, int lsp, int function, int arity) {
		FunctionInstance fun_instance;
		Function fun = functionStore.get(function);
		assert arity <= fun.nformals;
		assert fun.scopeIn == -1;
		fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, lstack, lsp);
		lsp = lsp - arity;
		lstack[lsp++] = fun_instance;
		return lsp;
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

//	public int insnPRINTLN(Object[] stack, int sp, int arity) {
//		StringBuilder w = new StringBuilder();
//		for (int i = arity - 1; i >= 0; i--) {
//			String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp - 1 - i]).toString() : asString(stack[sp - 1 - i]);
//			w.append(str).append(" ");
//		}
//		stdout.println(w.toString());
//		sp = sp - arity + 1;
//		return sp;
//	}
	
//	@SuppressWarnings("unchecked")
//	public int insnPUSHLOCKWP(Object[] stack, int sp, Frame cf, int iname) {
//		IString name = (IString) cf.function.codeblock.getConstantValue(iname);
//		Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
//		Map.Entry<Type, IValue> defaultValue = defaults.get(name.getValue());
//		Frame f = cf;
//		
//		// TODO: UNCOMMENT TO GET KEYWORD PARAMETER PROPAGATION
//		//for(Frame f = cf; f != null; f = f.previousCallFrame) {
//			int nf = f.function.nformals;
//			if(nf > 0){								// Some generated functions have zero args, i.e. EQUIVALENCE
//				Object okargs = f.stack[nf - 1];
//				if(okargs instanceof Map<?,?>){	// Not all frames provide kwargs, i.e. generated PHI functions.
//					Map<String, IValue> kargs = (Map<String,IValue>) okargs;
//					if(kargs.containsKey(name)) {
//						IValue val = kargs.get(name);
//						if(val.getType().isSubtypeOf(defaultValue.getKey())) {
//							stack[sp++] = val;
//							return sp;
//						}
//					}
//				}
//			}
//		//}				
//		stack[sp++] = defaultValue.getValue();
//		return sp;
//	}

	// / JVM Helper methods
	public Object dynRun(String fname, IValue[] args) {
		int n = functionMap.get(fname);
		Function func = functionStore.get(n);
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);

		root.stack[0] = vf.list(args); // pass the program argument to
		root.stack[1] = vf.mapWriter().done();
		root.sp = func.getNlocals();

		Object result = dynRun(n, root);
		return result;
	}

	public Object dynRun(int n, Frame cf) {
		System.out.println("Unimplemented Base called !");
		return PANIC;
	}

	public Object return0Helper(Object[] st0ck, int spp, Frame cof) {

		Object rval = null;

		boolean returns = cof.isCoroutine;
		if (returns) {
			rval = Rascal_TRUE;
		}

		if (cof == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		if (returns) {
			cof.previousCallFrame.stack[cof.previousCallFrame.sp++] = rval;
		}
		return rval;
	}

	public Object return1Helper(Object[] lstack, int sop, Frame cof, int arity) {
		Object rval = null;
		if (cof.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cof.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cof.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) lstack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = lstack[--sop];
			}
		} else {
			rval = lstack[sop - 1];
		}
		if (cof.previousCallFrame != null) {
			cof.previousCallFrame.stack[cof.previousCallFrame.sp++] = rval;
		}
		return rval;
	}
	
//	void jvmRESETVAR(int varScope, int pos, Frame cf){
//		if(CodeBlock.isMaxArg2(pos)){
//			IValue mvar = cf.function.constantStore[varScope];
//			moduleVariables.put(mvar, null);
//			return;
//		}
//		for (Frame fr = cf.previousScope; fr != null; fr = fr.previousScope) {
//			if (fr.scopeId == varScope) {
//				// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
//				fr.stack[pos] = null;
//				return;
//			}
//		}
//		throw new CompilerError("RESETVAR cannot find matching scope: " + varScope + " from scope " + cf.scopeId, cf);
//	}

	public int jvmCREATE(Object[] stock, int lsp, Frame lcf, int fun, int arity) {
		cccf = lcf.getCoroutineFrame(functionStore.get(fun), root, arity, lsp);
		cccf.previousCallFrame = lcf;

		// lcf.sp = modified by getCoroutineFrame.
		dynRun(fun, cccf); // Run untill guard, leaves coroutine instance in stack.
		return lcf.sp;
	}

	public int jvmCREATEDYN(Object[] lstack, int lsp, Frame lcf, int arity) {
		FunctionInstance fun_instance;

		Object src = lstack[--lsp];

		if (!(src instanceof FunctionInstance)) {
			throw new RuntimeException("Unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
		}

		// In case of partial parameter binding
		fun_instance = (FunctionInstance) src;
		cccf = lcf.getCoroutineFrame(fun_instance, arity, lsp);
		cccf.previousCallFrame = lcf;

		// lcf.sp = modified by getCoroutineFrame.
		dynRun(fun_instance.function.funId, cccf);
		return lcf.sp;
	}

	public int typeSwitchHelper(Object[] lstack, int lsp) { // stackpointer calc is done in the inline part.
		IValue val = (IValue) lstack[lsp];
		Type t = null;
		if (val instanceof IConstructor) {
			t = ((IConstructor) val).getConstructorType();
		} else {
			t = val.getType();
		}
		return ToplevelType.getToplevelTypeAsInt(t);
	}

	public int switchHelper(Object[] stack, int spp, boolean useConcreteFingerprint) {
		IValue val = (IValue) stack[spp];
		IInteger fp = vf.integer(ToplevelType.getFingerprint(val, useConcreteFingerprint));
		int toReturn = fp.intValue();
		return toReturn;
	}

	public boolean guardHelper(Object[] stack, int sp) {
		Object rval = stack[sp - 1];
		boolean precondition;
		if (rval instanceof IBool) {
			precondition = ((IBool) rval).getValue();
		} else {
			throw new RuntimeException("Guard's expression has to be boolean!");
		}
		return precondition;
	}

	public void yield1Helper(Frame lcf, Object[] lstack, int lsp, int arity2, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield1 does the return

		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		coroutine.start.previousCallFrame.stack[coroutine.start.previousCallFrame.sp++] = Rascal_TRUE;

		int[] refs = lcf.function.refs;

		for (int i = 0; i < arity2; i++) {
			Reference ref = (Reference) lstack[refs[arity2 - 1 - i]];
			ref.stack[ref.pos] = lstack[--lsp];
		}

		lcf.hotEntryPoint = ep;
		lcf.sp = lsp;

		coroutine.frame = lcf;
		coroutine.suspended = true;
	}

	public void yield0Helper(Frame lcf, Object[] lstack, int lsp, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield0 does the return

		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		coroutine.start.previousCallFrame.stack[coroutine.start.previousCallFrame.sp++] = Rascal_TRUE;

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
				lstack[lsp++] = fun_instance;
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

		if (rval.equals(YIELD)) {
			// drop my stack
			lcf.hotEntryPoint = ep;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			lcf.hotEntryPoint = 0;
			lcf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public int jvmNEXT0(Object[] lstack, int spp, Frame lcf) {
		Coroutine coroutine = (Coroutine) lstack[--spp];
		// Merged the hasNext and next semantics
		if (!coroutine.hasNext()) {
			lstack[spp++] = Rascal_FALSE;
			return spp;
		}
		// put the coroutine onto the stack of active coroutines
		activeCoroutines.push(coroutine);
		ccf = coroutine.start;
		coroutine.next(lcf);

		// Push something on the stack of the prev yielding function
		coroutine.frame.stack[coroutine.frame.sp++] = null;

		lcf.sp = spp;

		coroutine.frame.previousCallFrame = lcf;

		dynRun(coroutine.entryFrame.function.funId, coroutine.entryFrame);

		return lcf.sp;
	}

	public Object exhaustHelper(Object[] lstack, int sop, Frame cof) {
		if (cof == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		if (cof.previousCallFrame == null) {
			return Rascal_FALSE;
		}
		cof.previousCallFrame.stack[cof.previousCallFrame.sp++] = Rascal_FALSE; // 'Exhaust' has to always return FALSE,

		return NONE;// i.e., signal a failure;
	}

	// jvmOCALL has an issue
	// There are 3 possible ways to reset the stack pointer sp
	// 1: Done by nextFrame
	// 2: Not done by nextFrame (there is no frame)
	// 3: todo after the constructor call.
	// Problem there was 1 frame and the function failed.
	public int jvmOCALL(Object[] stack, int sp, Frame cf, int ofun, int arity) {
		boolean stackPointerAdjusted = false;

		cf.sp = sp;

		OverloadedFunctionInstanceCall ofun_call = null;
		OverloadedFunction of = overloadedStore[ofun];
	    
		Object arg0 = stack[sp - arity];
		ofun_call = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), cf, null, arity)  // changed root to cf
				                            : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), of.scopeIn, null, arity);
		
		Frame frame = ofun_call.nextFrame(functionStore);

		while (frame != null) {
			stackPointerAdjusted = true; // See text

			// System.err.println("Function ID : " + frame.function.funId);			
			Object rsult = dynRun(frame.function.funId, frame);
			if (rsult.equals(NONE)) {
				return cf.sp; // Alternative matched.
			}
			frame = ofun_call.nextFrame(functionStore);
		}
		Type constructor = ofun_call.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false) {
			sp = sp - arity;
		}
		stack[sp++] = vf.constructor(constructor, ofun_call.getConstructorArguments(constructor.getArity()));
		cf.sp = sp;
		return sp;
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
			// stack = cf.stack;
			// sp = cf.sp;
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
			if (rsult.equals(NONE)) {
				return lcf.sp; // Alternative matched.
			}
			frame = ofunCall.nextFrame(functionStore);
		}
		Type constructor = ofunCall.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false) {
			sop = sop - arity;
		}
		lstack[sop++] = vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
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
				lstack[lsp++] = vf.constructor(constr, args);
				lcf.sp = lsp;
				return NONE; // DO not return continue execution
			}

			if (lstack[lsp - 1] instanceof FunctionInstance) {
				FunctionInstance fun_instance = (FunctionInstance) lstack[--lsp];
				// In case of partial parameter binding
				if (fun_instance.next + arity < fun_instance.function.nformals) {
					fun_instance = fun_instance.applyPartial(arity, lstack, lsp);
					lsp = lsp - arity;
					lstack[lsp++] = fun_instance;
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
		if (rval.equals(YIELD)) {
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


	static boolean silent = false;
	public static void debugPOP(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC0(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC1(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC2(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC3(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC4(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC5(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC6(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC7(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC8(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC9(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOC(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugRESETLOCS(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADBOOL(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADINT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADCON(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOCREF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugJMP(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugJMPTRUE(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugJMPFALSE(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugTYPESWITCH(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSWITCH(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADTYPE(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOCDEREF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSTORELOC(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugUNWRAPTHROWNLOC(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSTORELOCDEREF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugPUSH_ROOT_FUN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugPUSH_NESTED_FUN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugPUSHOFUN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugPUSHCONSTR(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADVAR(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADVARREF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADVARDEREF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSTOREVAR(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugUNWRAPTHROWNVAR(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSTOREVARDEREF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCALLCONSTR(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCALLDYN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCALL(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugOCALLDYN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(lcf.function.name + " : " +  insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugOCALL(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(lcf.function.name + " : " +  insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCHECKARGTYPEANDCOPY(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugFAILRETURN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugFILTERRETURN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugRETURN0(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugRETURN1(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCALLJAVA(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCREATE(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugCREATEDYN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugGUARD(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugAPPLY(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugAPPLYDYN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugNEXT0(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugNEXT1(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugYIELD0(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugYIELD1(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugEXHAUST(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSUBSCRIPTARRAY(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSUBSCRIPTLIST(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLESSINT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugGREATEREQUALINT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugADDINT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSUBTRACTINT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugANDBOOL(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugTYPEOF(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSUBTYPE(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLABEL(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugHALT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugPRINTLN(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugTHROW(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADLOCKWP(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADVARKWP(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSTORELOCKWP(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSTOREVARKWP(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugLOADCONT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugRESET(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static void debugSHIFT(String insName, Frame lcf, int lsp) {
		if (!silent) {
			System.out.println(insName);
			if (lcf == null)
				throw new RuntimeException();
		}
	}

	public static Object anyDeserialize(String s) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(s));
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		ois.close();
		return o;
	}
}
