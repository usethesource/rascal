package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.type.Type;

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


	@Override
	public Object dynRun(String fname, IValue[] args) {
		// 0 this
		// 1 fname
		// 2 args
		// 3 int n
		// 4 Function Func
		// 5 Frame root

		int n ;
		Function func ;
		Frame root ;
		
		n = functionMap.get(fname);

		func = functionStore.get(n);

		root = new Frame(func.scopeId, null, func.maxstack, func);
		cf = root;

		this.stack = cf.stack;
		
		cf.stack[0] = vf.list(args); // pass the program argument to
		cf.stack[1] = vf.mapWriter().done();
		
		this.sp = func.nlocals ;
		cf.sp = this.sp  ;

		nop();
		
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
	
	int[]  dodo ;
	public void insnOCALL(int ofun, int arity) {
		cf.sp = sp;
		cf.pc = pc;

		OverloadedFunction of = overloadedStore.get(CodeBlock.fetchArg1(instruction));
		c_ofun_call = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf, of.functions, of.constructors, root, null, arity) : 
										 OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.functions, of.constructors, of.scopeIn, null, arity);

		ocalls.push(c_ofun_call);

		Frame frame = c_ofun_call.nextFrame(functionStore);

		if (frame != null) {
			cf = frame;
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
		} else {
			Type constructor = c_ofun_call.nextConstructor(constructorStore);
			sp = sp - arity;
			stack[sp++] = vf.constructor(constructor, c_ofun_call.getConstructorArguments(constructor.getArity()));
		}
		
	}
	public Object EXHAUST() {
		cf = cf.previousCallFrame;
		if (cf == null) {
			return  Rascal_FALSE ; 
		}
		stack = cf.stack;
		sp = cf.sp;
		stack[sp++] = Rascal_FALSE; 
		return NONE;
	}


	
	
	public Object wat() {
		Object p ; 
		
		nop() ;
		
		p = fret() ;
		
		nop() ;
		
		return p ;
	}
	public Object doreturn1() {
		Object rval = return1Helper();
		if (cf == null) {
			return rval; // TODO rval;
		}
		stack = cf.stack;
		sp = cf.sp;
		stack[sp++] = rval;
		return NONE;
	}
	public Object ocallOID() {
		String p = "ehhd do maar" ;
		int    scope = 120399393 ; 
		int[] fnctions = new int[0];
		int[] cons = new int[0] ;
		Object rval ;
		Function func ;
		Frame root ;
				
		cons[800] = 19990 ;
		cons[900] = 29999 ;
		
		nop() ;
		
		cf.sp = sp ;
		
		nop() ;
		
		func = functionStore.get(fnctions[777]);
		
		nop() ;
		
		root = cf.getFrame(func, null, func.nformals, sp) ;
		
		//root = new Frame(scope, cf, func.maxstack, func);
		
		cf = root;
		
		nop() ;
		
		stack = cf.stack ;
		
		nop() ;
		
		sp = func.nlocals ; 
		
		nop() ;
		
		rval = fret() ;
		if (rval.equals(NONE)) return rval ;
		
		nop() ;
		
		return p + scope  ;
	}
	public void insnLOADCON(int arg1) {
		nop() ;
		stack[sp++] = cf.function.constantStore[50];
		nop() ;
	}

	public void insnLOADLOC3() {
//		postOp = 0 ;
//		if (stack[3] != null) {
			stack[sp++] = stack[3];
//		} else {
//			postOp = Opcode.POSTOP_CHECKUNDEF;
//		}
	}
	public void insnSTORELOC(int target) {
		stack[544] = stack[sp - 1];
	}
	public void insnLOADTYPE(int i) {
		stack[sp++] = cf.function.typeConstantStore[544];
		return;
	}



}
