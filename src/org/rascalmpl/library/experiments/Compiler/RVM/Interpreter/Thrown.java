package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.values.uptr.IRascalValueFactory;

public class Thrown extends RuntimeException {
	
	private static final long serialVersionUID = 5789848344801944419L;
	
	// TODO: this is not thread safe
	private static Thrown instance = new Thrown();
	
	public IValue value;
	ISourceLocation loc;
	Throwable cause;
	
	Frame currentFrame;
	
	private Thrown() {
		super();
		this.value = null;
		this.currentFrame = null;
		this.cause = null;
	}
	
	public static Thrown getInstance(IValue value, ISourceLocation loc, Frame currentFrame) {
		instance.value = value;
		instance.loc = loc;
		instance.currentFrame = currentFrame;
		return instance;
	}
	
	public static Thrown getInstance(Throwable cause, ISourceLocation loc, Frame currentFrame) {
		instance.cause = cause;
		IRascalValueFactory vf = IRascalValueFactory.getInstance();
		return getInstance(vf.node(cause.getClass().getCanonicalName(), vf.string(cause.getMessage())), loc, currentFrame);
	}
	
	public static Thrown getInstance(IValue value, Frame currentFrame) {
		return getInstance(value,  currentFrame != null ? currentFrame.src : null, currentFrame);
	}

	public String toString() {
		return value.toString();
	}
	
	public String getAdvice(){
		if(value.getType().isConstructor()){
			String prefix = "http://tutor.rascal-mpl.org/Errors/Dynamic/";
			String cn = ((IConstructor) value).getName();
			return "Advice: |" + prefix + cn + "/" + cn + ".html|";
		}
		return "";
	}
	
	public void printStackTrace(PrintWriter stdout) {
		stdout.println(this.toString() + ((loc != null) ? " at " + loc : "") );
		
		while (cause != null) {
			stdout.println("Caused by (most recent first):");
			for (StackTraceElement e : cause.getStackTrace()) {
				if (e.getMethodName().equals("invoke0")) {
					break;
				}
				String location = "|file:///" + e.getFileName() + "|(0,1,<" + e.getLineNumber() + ",1>,<" +  e.getLineNumber() + ",1>)";
				stdout.println("\t"+location +":" + e.getClassName() + "." + e.getMethodName() + "()");
			}
			
			cause = cause.getCause() != cause ? getCause() : null;
		}
		
		if(currentFrame != null){
			stdout.println("Call stack (most recent first):");

			for(Frame f = currentFrame; f != null; f = f.previousCallFrame) {
				//stdout.println("at " + f.function.name);
				stdout.println("\t" + f);
			}
		} else {
			stdout.println("No call stack available");
		}
		stdout.println(getAdvice());
	}
	
}
