package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

// This file is only a tool to aid the writing
// of jvm bytecode and is not used in the program 
// DELETE FROM PROJECT 


public class RVMRunBody extends RVMRun {


	void nop() {
	}

	void POP() {
		nop();
		sp--;
		nop();
	}


	public void insnRETURN0() {
		globalReturnValue = null;

		// Overloading specific
		
		Object rval = null;
		boolean returns = cf.isCoroutine;
		if (returns) {
			rval = Rascal_TRUE;
		} else {
			rval = stack[sp - 1];
		}

		// if the current frame is the frame of a top active coroutine,
		// then pop this coroutine from the stack of active coroutines
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}
		cf = cf.previousCallFrame;
		if (cf == null) {
			if (returns) {
				globalReturnValue = rval;
				return; // TODO rval;
			} else {
				globalReturnValue = NONE;
				return; // TODO NONE;
			}
		}
		stack = cf.stack;
		sp = cf.sp;
		if (returns) {
			stack[sp++] = rval;
		}
	}

	public Object fret() {
		return NONE;
	}

	public Object failreturn() {
		nop();
		return FAILRETURN;
	}

	public Object functionTemplate() {
		nop();
		IValue v = null;
		return v;
	}

	public Object return1Helper() {
		Object rval = null;
		if (cf.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cf.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cf.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) stack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = stack[--sp];
			}
		} else {
			rval = stack[sp - 1];
		}
		return rval;
	}


	public void name(int b, String g) {
		name(b, g);
	}

	public Object wat() {
		Object p;

		nop();

		p = fret();

		nop();

		return p;
	}

	

	public Object ocallOID() {
		String p = "ehhd do maar";
		int scope = 120399393;
		int[] fnctions = new int[0];
		int[] cons = new int[0];
		Object rval;
		Function func;
		Frame root;

		cons[800] = 19990;
		cons[900] = 29999;

		nop();

		cf.sp = sp;

		nop();

		func = functionStore.get(fnctions[777]);

		nop();

		root = cf.getFrame(func, null, func.nformals, sp);

		// root = new Frame(scope, cf, func.maxstack, func);

		cf = root;

		nop();

		stack = cf.stack;

		nop();

		sp = func.nlocals;

		nop();

		rval = fret();
		if (rval.equals(NONE))
			return rval;

		nop();

		return p + scope;
	}

	public void insnLOADCON(int arg1) {
		nop();
		stack[sp++] = cf.function.constantStore[50];
		nop();
	}

	public void insnLOADLOC3() {
		// postOp = 0 ;
		// if (stack[3] != null) {
		stack[sp++] = stack[3];
		// } else {
		// postOp = Opcode.POSTOP_CHECKUNDEF;
		// }
	}

	public void insnSTORELOC(Object rval, boolean precondition, Coroutine coroutine, Frame prev) {
		stack[544] = stack[sp - 1];
	}

	public void insnLOADTYPE(Object rval, boolean precondition, Coroutine coroutine, Frame prev) {
		stack[sp++] = cf.function.typeConstantStore[544];
		return;
	}

	public void entry() {
		switch (cf.hotEntryPoint) {
		case 0:
			fret();
		case 1:
			fret();
		}
	}

	public Object dynRun(String fname, IValue[] args) {
		int n = functionMap.get(fname);

		Function func = functionStore.get(n);

		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		cf = root;

		stack = cf.stack;

		cf.stack[0] = vf.list(args); // pass the program argument to
		cf.stack[1] = vf.mapWriter().done();

		sp = func.nlocals;
		cf.sp = this.sp;

		return dynRun(n);
	}

	public Object dynRun(int n) {
		switch (n) {
		case 0:
			return fret();
		case 1:
			return fret();
		}
		return vf.bool(false);
	}

	public boolean guardHelper() {
		// TODO Auto-generated method stub
		return false;
	}

	public void insnTYPESWITCH(Object rval, boolean precondition, Coroutine coroutine, Frame prev) {
		switch (typeSwitchHelper()) {
		case 0:
			fret();
			break;
		case 1:
			fret();
			break;
		default:
			return;
		}
	}


	public Object jvmGUARD(Object rval, boolean precondition, Coroutine coroutine, Frame prev) {
		precondition = guardHelper();
		
		cf.hotEntryPoint = 909090990;

		if (cf == cccf) {
			coroutine = null;

			prev = cf.previousCallFrame;
			if (precondition) {
				coroutine = new Coroutine(cccf);
				coroutine.isInitialized = true;
				coroutine.entryFrame = cf ;
				coroutine.suspend(cf);
			}
			cccf = null;
			--sp;
			cf.sp = sp;
			cf = prev;
			stack = cf.stack;
			sp = cf.sp;
			stack[sp++] = precondition ? coroutine : exhausted;

			return NONE;
		}

		if (!precondition) {
			cf.sp = sp;
			cf = cf.previousCallFrame;
			stack = cf.stack;
			sp = cf.sp;
			stack[sp++] = Rascal_FALSE;
			return NONE;
		}

		// Skip nop and code below nop.
		nop();
		return NONE;
	}


	public Object jvmCallTemplate(Object rval, boolean precondition, Coroutine coroutine, Frame prev, int[] refs) {
		
		
		if ( callHelper(1111, 2222, 3333).equals(YIELD) ) {
			return YIELD ;
		}

		// Do not return ;
		nop() ;
		return NONE ;
	}
	

	public Object EXHAUST() {
		return exhaustHelper() ;
	}
	public void call(int a , int b , boolean g) {
		insnLOADVARREF(1111, 2222, true);
		insnLOADVARREF(1111, 2222, false);
		
	}

	public Object jvmYIELD1(Object rval, boolean precondition, Coroutine coroutine, Frame prev, int[] refs, int i) {
		
		yield1Helper(11111,9999) ;
		if (cf == null) {
			return Rascal_TRUE; // TODO rval;
		}
		return YIELD;
	}
	
	public Object jvmYIELD0(Object rval, boolean precondition, Coroutine coroutine, Frame prev, int[] refs, int i) {
		
		yield0Helper(11111) ;
		if (cf == null) {
			return Rascal_TRUE; // TODO rval;
		}
		return YIELD;
	}
	public void dopop() {
		insnSTORELOC(1) ;
	}
	public void ainsnLOADCON(int arg1) {
		stack[sp++] = cf.function.constantStore[arg1];
	}

	public Object calldynHelper(int arity, int ep) {
		// In case of CALLDYN, the stack top value of type 'Type'
		// leads to a constructor call

		// This instruction is a monstrosity it should be split in three.

		Frame tmp = null;
		Object rval;

		if (cf.hotEntryPoint != ep) {
			if (stack[sp - 1] instanceof Type) {
				Type constr = (Type) stack[--sp];
				arity = constr.getArity();
				IValue[] args = new IValue[arity];
				for (int i = arity - 1; i >= 0; i--) {
					args[i] = (IValue) stack[sp - arity + i];
				}
				sp = sp - arity;
				stack[sp++] = vf.constructor(constr, args);
				return NONE; // DO not return continue execution
			}

			// Specific to delimited continuations (experimental)
			if (stack[sp - 1] instanceof Coroutine) {
				// Coroutine coroutine = (Coroutine) stack[--sp];
				// // Merged the hasNext and next semantics
				// activeCoroutines.push(coroutine);
				// ccf = coroutine.start;
				// coroutine.next(cf);
				// instructions =
				// coroutine.frame.function.codeblock.getInstructions();
				// coroutine.frame.stack[coroutine.frame.sp++] = arity == 1 ?
				// stack[--sp] : null;
				// cf.pc = pc;
				// cf.sp = sp;
				// cf = coroutine.frame;
				// stack = cf.stack;
				// sp = cf.sp;
				// pc = cf.pc;
				return PANIC;
			}
			if (stack[sp - 1] instanceof FunctionInstance) {
				FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
				// In case of partial parameter binding
				if (fun_instance.next + arity < fun_instance.function.nformals) {
					fun_instance = fun_instance.applyPartial(arity, stack, sp);
					sp = sp - arity;
					stack[sp++] = fun_instance;
					return NONE;
				}
				tmp = cf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
				cf.nextFrame = tmp;
			} else {
//				throw new RuntimeException("Unexpected argument type for CALLDYN: " + asString(stack[sp - 1]));
			}
		} else {
			tmp = cf.nextFrame;
		}

		tmp.previousCallFrame = cf;

		this.cf = tmp;
		this.stack = cf.stack;
		this.sp = cf.sp;

		rval = dynRun(cf.function.funId); // In a inline version we can call the
											// function directly.

		if (rval.equals(YIELD)) {
			// drop my stack
			cf.hotEntryPoint = ep;
			cf.sp = sp;

			cf = cf.previousCallFrame;
			sp = cf.sp;
			stack = cf.stack;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			cf.hotEntryPoint = 0;
			cf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}
	public void jvmJMPINDEXED(int i) {
		switch(  ((IInteger) stack[--sp]).intValue()) {
		case 0 : fret() ;
		case 2 : fret() ;
		};
	}
	public void insnLOADINT(int i) {
		stack[sp++] = 1000000;
	}
	
	public Object doreturn0() {
		Object rval = return0Helper();
		if (cf == null) {
			return rval;
		}
		return NONE;
	}
	public Object doreturn1() {
		Object rval = return1Helper();
		if (cf == null) {
			return rval;
		}
		return NONE;
	}
	public int intint(int val) {
		int p ;
		switch(val) {
		case 0: p = 0 ; 
		case 1: p = 1 ;
		case 2: p = 2 ;
		case 3: p = 3 ;
		case 4: p = 4 ;
		case 5: p = 5 ;
		case 6: p = 6 ;
		case 7: p = 7 ;
		case 8: p = 8 ;
		case 9: p = 9 ;
		default : p = 10 ;
		}
		return p;
	}
	public void insnJMPFALSE(int target) {
		nop();
		sp--;
		if (!((IBool) stack[sp - 1]).getValue()) {
			//pc = target;
		}
		nop();
	}

	public void insnJMPTRUE(int target) {
		nop();
		sp--;
		if (((IBool) stack[sp]).getValue()) {
			//pc = target;
		}
		nop();
	}
	public RVMRunBody(RascalExecutionContext rascalExecutionContext) {
		super(rascalExecutionContext);
	}
	
	int spp() {
		return sp++ ;
	}
	
	public void insnLOADIN2T(int i) {
		insnPOP();
	}
	
	public void mup(MuPrimitive muprim) {
		sp = RascalPrimitive.adt_update.execute(stack, sp, arity, stacktrace);
	}
	public void CHK() {
		sp--;
		Type argType =  ((IValue) stack[sp - 1]).getType();
		Type paramType = ((Type) stack[sp]);
		stack[sp - 1] = vf.bool(argType.isSubtypeOf(paramType));
	}
	
	public Object f() {
		Frame    lframe = cf ;
		Object[] stack = lframe.stack ;
		int      sp = lframe.sp ;
		
		
		return NONE ;
	}
	public Object  F(Frame cf)  throws Exception {
		//Object[] stack ;
		//int      sp ;
		//Frame    lcf ;
		
		cf = cf ;
		stack  = cf.stack ;
		sp = cf.sp ;
		
		nop() ;
		
		stack[sp++] = cf.function.constantStore[1];
		
		return NONE;	
	}

	public Object nfi(Frame ccf) {
		int sp = cf.sp  ;
		Object[] stack = cf.stack ;
		
		nfi(cf) ;
		
		stack[sp++] = Rascal_FALSE;
		

//mv.visitVarInsn(ALOAD, 2);  // stack
//mv.visitVarInsn(ILOAD, 1);  // sp 
//mv.visitIincInsn(1, 1);     // sp++
//mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRunBody", "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");
//mv.visitInsn(AASTORE);

		return null ;

	}
	public void insnLOADBOOLTRUE(Frame cf) {
		int nosp = cf.sp ;
		Object[] stack = cf.stack ;
		
		nop() ;
	
		nosp = insnLOADCON(stack, nosp, 1) ;
		
		nop() ;
		
		stack[sp++] = stack[0] ;
	}

	void callOne(Frame cf) {
		int nosp = cf.sp ;
		Object[] stack= cf.stack; 
		
		sp = insnCHECKARGTYPE(stack, sp) ;
	
	}

	public Object drun(Frame cf) {
		int spp = cf.sp ;
		Object[] stack = cf.stack ;
		
		//this.sp = insnLOADCON(stack, this.sp, cf, 25) ;
		
		this.sp = insnPOP(this.sp) ;
		
		return null ;
	}
	
}
