package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import org.rascalmpl.values.ValueFactoryFactory;

public class CoverageFrameObserver implements IFrameObserver {

	private final PrintWriter stdout;
	private final HashSet<ISourceLocation> data;
	private boolean collecting;
	
	public CoverageFrameObserver(RascalExecutionContext rex){
		this.stdout = rex.getStdOut();
		this.data = new HashSet<ISourceLocation>();
		collecting = true;
	}
	
	@Override
	public boolean observe(Frame frame) {
		if(collecting){
			//System.err.println("observe: " + frame.src);
			data.add(frame.src);
			//System.err.println("data:"  + data);
		}
		return true;
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
