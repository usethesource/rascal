package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;

public class RVMrunBody extends RVMRun {

	public RVMrunBody(RascalExecutionContext rex) {
		super(rex);
		// TODO Auto-generated constructor stub
	}

	public void insnCallMUPRIM(Object[] stack, int sp, int i, int j) {
		sp = MuPrimitive.addition_mint_mint.execute(stack, sp, i);
	}

	public void setLocCol(Frame cf, int loc) {
		cf.src = (ISourceLocation) cf.function.constantStore[loc];
		locationCollector.registerLocation(cf.src);
	}

	public void insnCallPRIM(int i) {

		//setLocCol(cf, i);
		sp = RascalPrimitive.adt_field_update.execute(stack, sp, arity, cf);
		// try {
		// } catch (Exception exception) {
		// if (!(exception instanceof Thrown)) {
		// throw exception;
		// }
		// thrown = (Thrown) exception;
		// // thrown.stacktrace.add(cf);
		// sp = sp - arity;
		// postOp = Opcode.POSTOP_HANDLEEXCEPTION;
		// }
	}

}
