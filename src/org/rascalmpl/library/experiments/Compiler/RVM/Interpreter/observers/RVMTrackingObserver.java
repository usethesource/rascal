package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;

public class RVMTrackingObserver implements IFrameObserver {
	
	private final PrintWriter stdout;

	public RVMTrackingObserver(PrintWriter stdout){
		this.stdout = stdout;
	}

	@Override
	public boolean observeRVM(RVM rvm, Frame frame, int pc, Object[] stack, int sp) {
		stdout.printf("[%03d] %s, scope %d\n", pc, frame.function.getName(), frame.scopeId);
		
		for (int i = 0; i < sp; i++) {
			stdout.println("\t   " + (i < frame.function.getNlocals() ? "*" : " ") + i + ": " + rvm.asString(stack[i], 40));
		}
		stdout.printf("%5s %s\n" , "", frame.function.codeblock.toString(pc));
		stdout.flush();
		return true;
	}

}
