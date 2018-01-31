package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import org.rascalmpl.values.uptr.IRascalValueFactory;

public class Thrown extends RuntimeException {
	
	private static final long serialVersionUID = 5789848344801944419L;
	
	public IValue value;
	private ISourceLocation loc;
	private Throwable cause;
	
	Frame currentFrame;
	
	private Thrown() {
		super();
		this.setValue(null);
		this.currentFrame = null;
		this.cause = null;
	}
	
	public static Thrown getInstance(IValue value, ISourceLocation loc, Frame currentFrame) {
	    Thrown instance = new Thrown();
		instance.setValue(value);
		instance.loc = loc;
		instance.currentFrame = currentFrame;
		return instance;
	}
	
	@Override
	public synchronized Throwable getCause() {
	    return cause;
	}
	
	public ISourceLocation getLocation() {
        return loc;
    }
	
	public static Thrown getInstance(Throwable cause, ISourceLocation loc, Frame currentFrame) {
	    Thrown instance = new Thrown();
		instance.cause = cause;
		IRascalValueFactory vf = IRascalValueFactory.getInstance();
		return getInstance(vf.node(cause.getClass().getCanonicalName(), vf.string(cause.getMessage())), loc, currentFrame);
	}
	
	public static Thrown getInstance(IValue value, Frame currentFrame) {
		return getInstance(value,  currentFrame != null ? currentFrame.src : null, currentFrame);
	}
	
	 public IValue getValue() {
	    return value;
	  }

	  public void setValue(IValue value) {
	    this.value = value;
	  }
	
	public Frame getFrame(){
	  return currentFrame;
	}

	public String toString() {
		return getValue().toString();
	}
	
	
	public String getAdvice(){
		if(getValue().getType().isConstructor()){
			String prefix = "http://tutor.rascal-mpl.org/Errors/Dynamic/";
			String cn = ((IConstructor) getValue()).getName();
			return "Advice: |" + prefix + cn + "/" + cn + ".html|";
		}
		return "";
	}
	
	public void printStackTrace(PrintWriter stdout) {
	  if(currentFrame != null){
	    if(!currentFrame.isConsoleMainFrame()){
	      stdout.println(this.toString() + ((loc != null) ? " at " + loc : "") );
	    }

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
	      if(!currentFrame.isConsoleMainFrame()){
	        stdout.println("Call stack (most recent first):");

	        for(Frame f = currentFrame; f != null; f = f.previousCallFrame) {
	          if(f.isConsoleMainFrame()){
	            stdout.println("\tinput from console");
	          } else {
	            stdout.println("\t" + f);
	          }
	        }
	        stdout.println(getAdvice());
	      }
	    } else {
	      stdout.println("No call stack available");
	    }
	  }
	}

 
	
	
	
}
