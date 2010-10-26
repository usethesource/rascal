package org.rascalmpl.library;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
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
	
	private final IInteger duration;
	
	public Scripting(IValueFactory values){
		super();
		this.values = values;
		duration = values.integer(1000); // default duration for eval and shell
	}

	public IValue shell(IList lines, IInteger duration) throws IOException {
		StringBuffer sb = new StringBuffer();
		for(IValue line : lines){
			sb.append(((IString)line).getValue());
			sb.append("\n");
		}
		return shell(values.string(sb.toString()), duration);
	}
	
	public IValue shell(IList lines) throws IOException {
		return shell(lines, duration);
	}
	
	public IValue shell(IString input, IInteger duration) throws IOException {
	
		java.lang.String is = input.getValue();
		
		if(is.charAt(is.length()-1) != '\n'){
			is = is + "\n";
		}
		InputStream in = new ByteArrayInputStream(is.getBytes());
		
		StringWriter os = new StringWriter();
		PrintWriter out = new PrintWriter(os);
		
		StringWriter es = new StringWriter();
		PrintWriter err = new PrintWriter(es);
		
		try {
			RascalShell shell = new RascalShell(in, err, out);
			Timer timer = new ShellTimer(shell, duration.intValue());
			timer.start();
			shell.run();
			timer.cancel();
			
			out.close();
			err.close();
			java.lang.String result = os.toString();
			java.lang.String prompt = "rascal>";
			if(result.endsWith(prompt))
					result = result.substring(0, result.length()- prompt.length());
			if(timer.hasExpired()){
				System.err.println("Timeout");
				// Remove stack trace due to killing the shell
				int k = result.lastIndexOf("Unexpected exception");
				result = result.substring(0, k);
			    result = result.concat("\n*** Rascal killed after timeout ***\n");
			}
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
	
	public IValue shell(IString input) throws IOException {
		return shell(input, duration);
	}

	public IValue eval (IString input, IInteger duration) throws IOException {
		return doEval(ValueFactoryFactory.getValueFactory().list(input), duration).getValue();
	}
	
	public IValue eval (IString input) throws IOException {
		return eval(input, duration);
	}

	public IValue eval (IList commands, IInteger duration) throws IOException {
		return doEval(commands, duration).getValue();
	}
	
	public IValue eval (IList commands) throws IOException {
		return eval(commands, duration);
	}
	
	public IValue evalType (IString input, IInteger duration) throws IOException {
		Result<IValue> result =  doEval(values.list(input), duration);
		// Make sure redundant spaces are removed from the type.
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IString input) throws IOException {
		return evalType(input, duration);
	}
	
	public IValue evalType (IList commands, IInteger duration) throws IOException {
		Result<IValue> result = doEval(commands, duration);
		return values.string(result.getType().toString());
	}
	
	public IValue evalType (IList commands) throws IOException {
		return evalType(commands, duration);
	}
	
	private Result<IValue> doEval (IList commands, IInteger duration) throws IOException {
		PrintWriter err = new PrintWriter(System.err);
		PrintWriter out = new PrintWriter(System.out);
		
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("***scripting***"));
		Evaluator evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), err, out, root, heap);
		EvalTimer timer = new EvalTimer(evaluator, duration.intValue());
		timer.start();
		
		Result<IValue> result = null;
		if(!timer.hasExpired() && commands.length() > 0){
			for(IValue command : commands){
				result = evaluator.eval(((IString) command).getValue(), URI.create("stdin:///"));
			}
			timer.cancel();
			if(timer.hasExpired())
			  throw RuntimeExceptionFactory.timeout(null, null);
			return result;
		}
		
		throw RuntimeExceptionFactory.illegalArgument(commands, null, null);
	}

	class Timer extends Thread {
		private final int timeout;
		private int elapsed;
		private final int sample;
		private volatile boolean running;

		public Timer(int timeout) {
			super();
			this.elapsed = 0;
			this.timeout = timeout;
			this.sample = timeout / 10;
			running = true;
		}

		public void run() {
			running = true;
			elapsed = 0;
			while (running) {
				try {
					sleep(sample);
				} catch (InterruptedException e) {
					// ignore
				}
				synchronized (this) {
					elapsed += sample;
					if (elapsed > timeout && running) {
						running = false;
						timeout();
					}
				}
			}
		}

		public synchronized void cancel() {
			running = false;
		}

		public boolean hasExpired() {
			return elapsed > timeout;
		}

		public void timeout(){
			System.err.println("Timeout!");
		}
	}

	private class ShellTimer extends Timer {
		private final RascalShell shell;

		public ShellTimer(RascalShell shell, int timeout) {
			super(timeout);
			this.shell = shell;
		}
		public void timeout(){
			shell.stop();
		}
	}

	private class EvalTimer extends Timer {
		private Evaluator eval;

		public EvalTimer(Evaluator eval, int timeout) {
			super(timeout);
			this.eval = eval;
		}
		
		public void timeout(){
			eval.interrupt();
		}
	}
}

