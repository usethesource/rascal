package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BreakPointManager {
	private final List<BreakPoint> breakpoints;
	int uid;
	
	BreakPointManager(){
		breakpoints = new ArrayList<>();
		uid = 1;
	}
	
	void add(BreakPoint breakpoint){
		breakpoint.setId(uid++);
		breakpoints.add(breakpoint);
	}
	
	void printBreakPoints(PrintWriter stdout){
		stdout.println("Id\tEnabled\tDetails");
		for(BreakPoint breakpoint : breakpoints){
			breakpoint.println(stdout);
		}
	}

}
