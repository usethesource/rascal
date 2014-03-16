package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.eclipse.imp.pdb.facts.IInteger;

public class RVMRunBody extends RVMRun {

	public RVMRunBody(IValueFactory vf, IEvaluatorContext ctx, boolean debug, boolean profile) {
		super(vf, ctx, debug, profile);
		// TODO Auto-generated constructor stub
	}
	void nop() {
		
	}
	void POP( ) {
		nop() ;
		sp-- ;
		nop() ;
	}
	public void insnJMPTRUE(int target) {
		nop() ;
		sp--;
		if (stack[sp].equals(TRUE) || stack[sp].equals(Rascal_TRUE)) {
			pc = target;
		}
		nop() ;
	}

	public void insnJMPFALSE(int target) {
		nop() ;
		sp--;
		if (stack[sp].equals(FALSE) || stack[sp].equals(Rascal_FALSE)) {
			pc = target;
		}
		nop() ;
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
		nop( );
		return FAILRETURN ;
	}
	
	public Object functionTemplate() {
		nop();
		IValue v = null  ;
		return v;
	}
}
