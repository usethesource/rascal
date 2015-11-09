package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.values.ValueFactoryFactory;

public class CoverageFrameObserver implements IFrameObserver {

	private final PrintWriter stdout;
	private final HashSet<ISourceLocation> data;
	private boolean collecting;
	
	public CoverageFrameObserver(PrintWriter stdout){
		this.stdout = stdout;
		this.data = new HashSet<ISourceLocation>();
		collecting = true;
	}
	
	@Override
	public void observe(Frame frame) {
		if(collecting){
			//System.err.println("observe: " + frame.src);
			data.add(frame.src);
			//System.err.println("data:"  + data);
		}
	}
	
	@Override
	public IList getData(){
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter();
		System.err.println("getData: " + data);
		for (ISourceLocation src : data) {
			//System.err.println("getData: " + src);
			w.insert(src);
		}
		return w.done();
	}

	@Override
	public void report(IList data) {
		Iterator<IValue> iter = data.iterator();
		stdout.println(data.length() > 0 ? "COVERAGE:" : "NO COVERAGE DATA");
		while(iter.hasNext()){
			stdout.printf("%s\n",  iter.next());
		}
		stdout.flush();
	}
	
	@Override
	public void report() {
		stdout.println(data.size() > 0 ? "COVERAGE:" : "NO COVERAGE DATA");
		for(ISourceLocation src : data){
			stdout.printf("%s\n",  src);
		}
		stdout.flush();
	}

}
