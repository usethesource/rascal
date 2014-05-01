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
	
	List<Frame> stacktrace;
	
	private Thrown() {
		super();
		this.value = null;
		this.stacktrace = null;
	}
	
	public static Thrown getInstance(IValue value, ISourceLocation loc, List<Frame> stacktrace) {
		instance.value = value;
		instance.loc = loc;
		instance.stacktrace = stacktrace;
		return instance;
	}
	
	public static Thrown getInstance(IValue value, List<Frame> stacktrace) {
		return getInstance(value, null, stacktrace);
//		instance.value = value;
//		instance.loc = null;
//		instance.stacktrace = stacktrace;
//		return instance;
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
		stdout.println(this.toString() + ((loc !=null) ? " at " + loc : "") );
		if(stacktrace != null){
			for(Frame cf : stacktrace) {
				for(Frame f = cf; f != null; f = f.previousCallFrame) {
					//stdout.println("at " + f.function.name);
					stdout.println(f);
				}
			}
		}
		stdout.println(getAdvice());
		
	}
	
}
