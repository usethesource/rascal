package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.value.ISourceLocation;

public abstract class BreakPoint {
	protected boolean enabled;
	protected int id;
	
	public BreakPoint(int id){
		this.id = id;
		this.enabled = true;
	}
	
	int getId() { return id; }
	
	void setId(int id){
		this.id = id;
	}
	
	boolean isEnabled(){
		return enabled;
	}
	void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	boolean containedIn(ISourceLocation a, ISourceLocation b	){
		if(a.getPath().equals(b.getPath())){
			int aBeginLine = a.getBeginLine();
			int aEndLine = a.getEndLine();
			
			if(aBeginLine >= b.getBeginLine() && aEndLine <= b.getEndLine()){
				if (aBeginLine == aEndLine){					
					return a.getBeginColumn() >= b.getBeginColumn() && a.getEndColumn() <= b.getEndColumn();
				}
				return true;
			}
		}
		return false;
	}
	
	abstract void println(PrintWriter stdout);
	abstract boolean matchOnObserve(Frame frame);
	abstract boolean matchOnEnter(Frame frame);
	abstract boolean matchOnLeave(Frame frame);
}
