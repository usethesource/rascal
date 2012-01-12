package org.rascalmpl.interpreter.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.io.StandardTextWriter;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ReadEvalPrintDialogMessages {
	public static String toResult(Result<IValue> result) {
		String content;
		IValue value = result.getValue();
		
		if (value != null) {
			Type type = result.getType();
			
			if (type.isAbstractDataType() && type.isSubtypeOf(Factory.Tree)) {
				content = type.toString() + ": `" + TreeAdapter.yield((IConstructor) value, 1000) + "`\n";
				
				StandardTextWriter stw = new StandardTextWriter(false);
				LimitedResultWriter lros = new LimitedResultWriter(1000);
				try{
					stw.write(value, lros);
				}catch(IOLimitReachedException iolrex){
					// This is fine, ignore.
				}catch(IOException ioex){
					// This can never happen.
				}
				content += "Tree: " + lros.toString();
			} else {
				content = result.toString(4096);
			}
		} else {
			content = "ok";
		}
		return content;
	}

	public static String toParseError(String command, ParseError pe) {
		String content = "";
		if (pe.getLocation().getScheme().equals("eval")) {
			String[] commandLines = command.split("\n");
			int lastLine = commandLines.length;
			int lastColumn = commandLines[lastLine - 1].length();

			if (pe.getEndLine() + 1 == lastLine && lastColumn <= pe.getEndColumn()) { 
				content = "";
			} 
			else {
				content = "";
				int i = 0;
				for ( ; i < pe.getEndColumn() + "rascal>".length(); i++) {
					content += " ";
				}
				content += "^ ";
				content += "parse error here";
				if (i > 80) {
					content += "\nparse error at column " + pe.getEndColumn();
				}
			}
		}
		else {
			content = pe.toString();
			ByteArrayOutputStream trace = new ByteArrayOutputStream();
			pe.printStackTrace(new PrintStream(trace));
			content += "\n" + trace.toString();
		}
		return content;
	}

	public static String toInterruptedException(InterruptException i) {
		String content;
		content = i.getMessage();
		return content;
	}

	public static String toAmbiguous(Ambiguous e) {
		String content;
		content = e.getMessage();
		return content;
	}

	public static String toThrowable(Throwable e, String rascalTrace) {
		String content;
		content = "internal exception: " + e.toString();
		content += rascalTrace;
		ByteArrayOutputStream trace = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(trace));
		content += "\n" + trace.toString();
		return content;
	}

	public static String toThrow(Throw e) {
		String content;
		content = "exception:" + e.getException().toString() + "\n";
		String trace = e.getTrace();
		if (trace != null) {
			content += "stacktrace:" + e.getLocation() + "\n" + trace;
		}
		return content;
	}

	public static String toStaticError(StaticError e) {
		String content;
		content = e.getMessage();
		ByteArrayOutputStream trace = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(trace));
		content += "\n" + trace.toString();
		return content;
	}
}
