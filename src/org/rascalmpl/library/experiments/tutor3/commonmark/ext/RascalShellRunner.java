package org.rascalmpl.library.experiments.tutor3.commonmark.ext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.DebugREPLFrameObserver;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;


public class RascalShellRunner extends AbstractVisitor {
	ISourceLocation screenInputLocation;
	CommandExecutor shell;
	StringWriter sout;
	PrintWriter pout;
	String conceptName = "";
	PrintWriter err;
	private StringWriter fout;

	public RascalShellRunner(PrintWriter err){
		String consoleInputPath = "/ConsoleInput.rsc";
		try {
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			screenInputLocation = vf.sourceLocation("home", "", consoleInputPath);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot initialize: " + e.getMessage());
		}

		this.err = err;
		sout = new StringWriter();
		pout = new PrintWriter(sout);
		shell = new CommandExecutor(pout, pout);
		fout = new StringWriter();
		try {
			shell.setDebugObserver(new DebugREPLFrameObserver(System.in, System.out, true, true, null, null, null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setConceptName(String name){
		conceptName = name;
	}
	
	private String complete(String line){
		return line + (line.endsWith(";") ? "\n" : ";\n");
	}

	@Override public void visit(FencedCodeBlock fcblock){
		String info = fcblock.getInfo();
		if(info.startsWith("rascal-shell")){
			if(!info.contains("continue")){
				shell.reset();
			}
			sout.getBuffer().setLength(0);
			String[] lines = fcblock.getLiteral().split("\n");
			for(String line : lines){
				fout.append("rascal>").append(line).append("\n");
				
				IValue result = shell.eval(complete(line), screenInputLocation);
				pout.flush();
				fout.flush();
				String messages = sout.toString();
				sout.getBuffer().setLength(0);
				if(result != null){
					fout.append(messages);
					fout.append(result.getType().toString()).append(": ").append(result.toString()).append("\n");
				} else  if(info.contains("error")){
					fout.append(messages);
				} else {
					err.println("* __" + conceptName + "__:");
					err.println("```");
					err.println(messages.trim());
					err.println("```");
				}
			}
			pout.flush();
			fout.flush();
			fcblock.setLiteral(fout.toString());
			fout.getBuffer().setLength(0);
		}
	}
}
