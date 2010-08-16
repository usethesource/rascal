package org.rascalmpl.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.fast.ListWriter;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.RascalShell;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Scripting {
	private static final TypeFactory types = TypeFactory.getInstance();
	private final IValueFactory values;
	
	public Scripting(IValueFactory values){
		super();
		this.values = values;
	}

	public IValue shell(IList lines) throws IOException {
		StringBuffer sb = new StringBuffer();
		for(IValue line : lines){
			sb.append(((IString)line).getValue());
			sb.append("\n");
		}
		return shell(values.string(sb.toString()));
	}
	
	public IValue shell(IString input) throws IOException {
	
		java.lang.String is = input.getValue();
		
		if(is.charAt(is.length()-1) != '\n'){
			is = is + "\n";
		}
		InputStream in = new StringBufferInputStream(is);
		StringWriter os = new StringWriter();
		
		PrintWriter out = new PrintWriter(os);
		StringWriter es = new StringWriter();
		PrintWriter err = new PrintWriter(es);
		
		try {
			new RascalShell(in, err, out).run();
			out.close();
			err.close();
			java.lang.String result = os.toString();
			java.lang.String prompt = "rascal>";
			if(result.endsWith(prompt))
					result = result.substring(0, result.length()- prompt.length());
			java.lang.String lines[] = result.split("[\r\n]+");
			IListWriter w = values.listWriter(types.stringType());
			for(java.lang.String line : lines)
				w.append(values.string(line));
			return w.done();
		} catch (IOException e) {
			System.err.println("unexpected error: " + e.getMessage());
			return values.string("unexpected error: " + e.getMessage());
		} 
	}
/*	
	public IValue eval (IString input) throws IOException {
		PrintWriter err = new PrintWriter(System.err);
		PrintWriter out = new PrintWriter(System.out);
		
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***scripting***"));
		Evaluator evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), err, out, root, heap);
		Result<IValue> result = evaluator.eval(input.getValue(), URI.create("stdin:///"));
		return result.getValue();
	}
*/	
	public IValue eval (IString input) throws IOException {
		return doEval(ValueFactoryFactory.getValueFactory().list(input)).getValue();
	}
	
	public IValue evalType (IString input) throws IOException {
		Result<IValue> result =  doEval(values.list(input));
		return values.string(result.getType().toString());
	}
	
	public IValue eval (IList commands) throws IOException {
		return doEval(commands).getValue();
	}
	
	public IValue evalType (IList commands) throws IOException {
		Result<IValue> result = doEval(commands);
		return values.string(result.getType().toString());
	}
	
	private Result<IValue> doEval (IList commands) throws IOException {
		PrintWriter err = new PrintWriter(System.err);
		PrintWriter out = new PrintWriter(System.out);
		
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***scripting***"));
		Evaluator evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), err, out, root, heap);
		
		Result<IValue> result = null;
		if(commands.length() > 0){
			for(IValue command : commands){
				result = evaluator.eval(((IString) command).getValue(), URI.create("stdin:///"));
			}
			return result;
		} else
			throw RuntimeExceptionFactory.illegalArgument(commands, null, null);
	}
	
/**** Work in progress
	private static class TheTerminator implements Runnable{
		private final int timeout;
		private final RascalShell shell;
		
		private final NotifiableLock lock;
		private volatile boolean stopped;
		
		public TheTerminator(RascalShell shell, int timeout){
			super();
			
			this.shell = shell;
			this.timeout = timeout;
			
			lock = new NotifiableLock();
			stopped = false;
		}
		
		public void run(){
			lock.block(timeout);
			
			if(!stopped){
				System.err.println("Killing!");
				shell.stop();
			}
		}
		
		public void illBeBack(){
			stopped = true;
			lock.wakeUp();
		}
		
		private static class NotifiableLock{
			private volatile boolean notified = false;
			
			public synchronized void wakeUp(){
				notified = true;
				notify();
			}
			
			public synchronized void block(int timeout){
				long endTime = System.currentTimeMillis() + timeout;
				while(!notified && endTime > System.currentTimeMillis()){
					long waitFor = endTime - System.currentTimeMillis();
					try{
						wait(waitFor);
					}catch(InterruptedException irex){
						// Don't care.
					}
				}
				notified = false;
			}
		}
		
		public static TheTerminator killedInTMinus(int xSeconds, OutputInterpreterConsole console, DummyDebugger dd){
			TheTerminator t = new TheTerminator(console, dd, xSeconds * 1000);
			new Thread(t).start();
			return t;
		}
	}
}
***/
	
}
