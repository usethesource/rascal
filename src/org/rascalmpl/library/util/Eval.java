/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.ByteArrayInputStream;
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
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.RascalShell;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class Eval {
	private static final TypeFactory types = TypeFactory.getInstance();
	private final IValueFactory values;
	
	private final IInteger duration;
	
	public Eval(IValueFactory values){
		super();
		this.values = values;
		duration = values.integer(1000*100); // default duration for eval and shell
	}

	public IValue shell(IList lines, IInteger duration, IEvaluatorContext ctx) {
		StringBuffer sb = new StringBuffer();
		for(IValue line : lines){
			sb.append(((IString)line).getValue());
			sb.append("\n");
		}
		return shell(values.string(sb.toString()), duration, ctx);
	}
	
	public IValue shell(IList lines, IEvaluatorContext ctx) {
		return shell(lines, duration, ctx);
	}
	
	public IValue shell(IString input, IInteger duration, IEvaluatorContext ctx) {
	
		java.lang.String is = input.getValue();
		
		if(is.charAt(is.length()-1) != '\n'){
			is = is + "\n";
		}
		InputStream in = new ByteArrayInputStream(is.getBytes());
		
		StringWriter os = new StringWriter();
		PrintWriter out = new PrintWriter(os);
		
		StringWriter es = new StringWriter();
		PrintWriter err = new PrintWriter(es);
		RascalShell shell = null;
		Timer timer = null;
		
		try {
			shell = new RascalShell(in, err, out, ((Evaluator) ctx).getClassLoaders(), ((Evaluator) ctx).getRascalResolver());
			timer = new ShellTimer(shell, duration.intValue());
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
				if (k != -1) {
					result = result.substring(0, k);
				}
			    result = result.concat("\n*** Rascal killed after timeout ***\n");
			}
			java.lang.String lines[] = result.split("[\r\n]");
			IListWriter w = values.listWriter(types.stringType());
			for(java.lang.String line : lines)
				w.append(values.string(line));
			return w.done();
		} catch (Throwable e) {
			System.err.println("unexpected error: " + e.getMessage());
			if (shell != null) {
				System.err.println(shell.getEvaluator().getStackTrace());
				shell.stop();
			}
			if(timer != null){
				timer.cancel();
			}
			System.err.println("caused by:");
			e.printStackTrace();
			return values.string("unexpected error: " + e.getMessage());
		} finally {
			out.close();
			err.close();
			if(shell != null){
				shell.stop();
			}
			if(timer != null){
				timer.cancel();
			}
		}
	}
	
	public IValue shell(IString input, IEvaluatorContext ctx) {
		return shell(input, duration, ctx);
	}

	public IValue eval (IString input, IInteger duration, IEvaluatorContext ctx) {
		return doEval(ValueFactoryFactory.getValueFactory().list(input), duration, ctx).getValue();
	}
	
	public IValue eval (IString input, IEvaluatorContext ctx) {
		return eval(input, duration, ctx);
	}

	public IValue eval (IList commands, IInteger duration, IEvaluatorContext ctx) {
		return doEval(commands, duration, ctx).getValue();
	}
	
	public IValue eval (IList commands, IEvaluatorContext ctx) {
		return eval(commands, duration, ctx);
	}
	
	public IValue evalType (IString input, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result =  doEval(values.list(input), duration, ctx);
		// Make sure redundant spaces are removed from the type.
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IString input, IEvaluatorContext ctx) {
		return evalType(input, duration, ctx);
	}
	
	public IValue evalType (IList commands, IInteger duration, IEvaluatorContext ctx) {
		Result<IValue> result = doEval(commands, duration, ctx);
		return values.string(result.getType().toString().replaceAll(" ", ""));
	}
	
	public IValue evalType (IList commands, IEvaluatorContext ctx) {
		return evalType(commands, duration, ctx);
	}
	
	private Result<IValue> doEval (IList commands, IInteger duration, IEvaluatorContext ctx) {
		PrintWriter err = new PrintWriter(System.err); // TODO ?
		PrintWriter out = new PrintWriter(System.out);
		
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment root = heap.addModule(new ModuleEnvironment("___scripting___", ctx.getHeap()));
		Evaluator eval = (Evaluator) ctx;

		Evaluator evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), err, out, root, heap, eval.getClassLoaders(), eval.getRascalResolver());
		EvalTimer timer = new EvalTimer(evaluator, duration.intValue());
		timer.start();
		
		Result<IValue> result = null;
		if(!timer.hasExpired() && commands.length() > 0){
			for(IValue command : commands){
				result = evaluator.eval(null, ((IString) command).getValue(), URI.create("stdin:///"));
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
				
				elapsed += sample;
				if (elapsed > timeout && running) {
					running = false;
					timeout();
				}
			}
		}

		public void cancel() {
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

