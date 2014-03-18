package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.eclipse.imp.pdb.facts.IInteger;

public class RVMRunBody extends RVMRun {

	public RVMRunBody(IValueFactory vf, IEvaluatorContext ctx, boolean debug, boolean profile) {
		super(vf, ctx, debug, profile);
	}

	void nop() {

	}

	void POP() {
		nop();
		sp--;
		nop();
	}

	public void insnJMPTRUE(int target) {
		nop();
		sp--;
		if (stack[sp].equals(TRUE) || stack[sp].equals(Rascal_TRUE)) {
			pc = target;
		}
		nop();
	}

	public void insnJMPFALSE(int target) {
		nop();
		sp--;
		if (stack[sp].equals(FALSE) || stack[sp].equals(Rascal_FALSE)) {
			pc = target;
		}
		nop();
	}

	public void insnRETURN0() {
		globalReturnValue = null;

		// Overloading specific
		if (c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
			ocalls.pop();
			c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
		}

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
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
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

	public Object doreturn1() {
		Object rval = return1Helper();
		cf = cf.previousCallFrame;
		if (cf == null) {
			return rval; // TODO rval;
		}
		stack = cf.stack;
		sp = cf.sp;
		stack[sp++] = rval;
		return NONE;
	}

	@Override
	public Object dynRun(String fname, IValue[] args) {

		System.out.println(fname);
		
		int n = functionMap.get(fname);
		
		Function func = functionStore.get(n);
		
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		cf = root;
		
		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i];
		}
		
		switch (n) {
		case 0:
			return functionTemplate();
		case 1:
			return fret();
		case 2:
			return fret();
		case 3:
			return fret();
		}
		return vf.bool(false);
	}
}
