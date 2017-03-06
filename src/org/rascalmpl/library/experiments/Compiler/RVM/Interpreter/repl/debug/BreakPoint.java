package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import io.usethesource.vallang.ISourceLocation;

public abstract class BreakPoint {
	protected boolean enabled;
	protected int id;
	protected int ignore = 0;
	
	public BreakPoint(int id){
		this.id = id;
		this.enabled = true;
	}
	
	int getId() { return id; }
	
	void setId(int id){
		this.id = id;
	}
	
	void setIgnore(int ignore){
		this.ignore = ignore;
	}
	
	boolean isEnabled(){
		return enabled;
	}
	
	void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	boolean containedIn(ISourceLocation requiredSrc, ISourceLocation currentSrc){
		if(requiredSrc.getPath().equals(currentSrc.getPath())){
			int aBeginLine = requiredSrc.getBeginLine();
			int aEndLine = requiredSrc.getEndLine();
			
			if(aBeginLine >= currentSrc.getBeginLine() && aEndLine <= currentSrc.getEndLine()){
				if (aBeginLine == aEndLine){					
					return requiredSrc.getBeginColumn() >= currentSrc.getBeginColumn() && requiredSrc.getEndColumn() <= currentSrc.getEndColumn();
				}
				return true;
			}
		}
		return false;
	}
	
	boolean shouldBreakAt(String path, int lino, ISourceLocation currentSrc){
		return path.equals(currentSrc.getPath()) && currentSrc.getBeginLine() == lino;
	}
	
	boolean shouldBreakAt(int lino, ISourceLocation currentSrc){
		return lino == currentSrc.getBeginLine();
	}
	
	boolean ignoreOrBreak(boolean breakCondition){
		if(breakCondition){
			if(ignore == 0){
				return true;
			}
			ignore--;
			return false;
		}
		return false;
	}
	
    void println(PrintWriter stdout) { stdout.println("println should be redefined"); }
    
	@SuppressWarnings("unused")
    boolean matchOnObserve(Frame frame) { return false; }
	
	@SuppressWarnings("unused")
    boolean matchOnEnter(Frame frame) { return false; }
	
	@SuppressWarnings("unused")
    boolean matchOnLeave(Frame frame) { return false; }
	
	@SuppressWarnings("unused")
    boolean matchOnException(Frame frame, Thrown thrown) { return true; }
	
	abstract public String toString();
}
