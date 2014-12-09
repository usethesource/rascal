package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;

public class Thrown extends RuntimeException {
	
	private static final long serialVersionUID = 5789848344801944419L;
	
	private static Thrown instance = new Thrown();
	
	IValue value;
	ISourceLocation loc;
	
	Frame currentFrame;
	
	private Thrown() {
		super();
		this.value = null;
		this.currentFrame = null;
	}
	
	public static Thrown getInstance(IValue value, ISourceLocation loc, Frame currentFrame) {
		instance.value = value;
		instance.loc = loc;
		instance.currentFrame = currentFrame;
		return instance;
	}
	
	public static Thrown getInstance(IValue value, Frame currentFrame) {
		return getInstance(value,  currentFrame.src, currentFrame);
	}

	public String toString() {
		return value.toString();
	}
	
	public String getAdvice(){
		if(value.getType().isConstructor()){
			String prefix = "http://tutor.rascal-mpl.org/Errors/Dynamic/";
			String cn = ((IConstructor) value).getName();
			return "\uE007[Advice](" + prefix + cn + "/" + cn + ".html)";
		}
		return "";
	}
	
	public void printStackTrace(PrintWriter stdout) {
		
		stdout.println(this.toString() + ((loc != null) ? " at " + loc : "") );
		stdout.println("Call stack (most recent first):");
		
		for(Frame f = currentFrame; f != null; f = f.previousCallFrame) {
			//stdout.println("at " + f.function.name);
			stdout.println("\t" + f);
		}
		stdout.println(getAdvice());
	}
	
}
