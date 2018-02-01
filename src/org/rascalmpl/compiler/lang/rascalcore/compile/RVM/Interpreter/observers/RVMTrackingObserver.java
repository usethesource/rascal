package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;

public class RVMTrackingObserver implements IFrameObserver {
	
	private final PrintWriter stdout;

	public RVMTrackingObserver(RascalExecutionContext rex){
		this.stdout = rex.getStdOut();
	}

	@Override
	public boolean observeRVM(RVMCore rvm, Frame frame, int pc, Object[] stack, int sp, Object accu) {
		stdout.printf("[%03d] %s, scope %d\n", pc, frame.function.getName(), frame.scopeId);

		for (int i = 0; i < sp; i++) {
			if(stack[i] != null){
				stdout.println("\t " + (i < frame.function.getNlocals() ? "*" : " ") + i + ": " + rvm.asString(stack[i], 40));
			}
		}
		stdout.println("\tacc: " + rvm.asString(accu, 40));
		stdout.printf("%5s %s\n" , "", frame.function.codeblock.toString(pc));
		stdout.flush();
		return true;
	}

}
