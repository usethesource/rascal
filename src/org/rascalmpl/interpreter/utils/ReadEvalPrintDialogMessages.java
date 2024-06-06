/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Davy Landman  - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;

import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;

public class ReadEvalPrintDialogMessages {
    public static final String PROMPT = "rascal>";
	public static final String CONTINUE_PROMPT = ">>>>>>>";
	public static final String CANCELLED = "cancelled";
	
	
	public static String resultMessage(Result<IValue> result) {
		String content;
		IValue value = result.getValue();
		
		if (value != null) {
			Type type = result.getStaticType();
			
			if (type.isAbstractData() && type.isSubtypeOf(RascalValueFactory.Tree)) {
				content = type.toString() + ": `" + TreeAdapter.yield((IConstructor) value, 1000) + "`\n";
				
				StandardTextWriter stw = new StandardTextWriter(false);
				LimitedResultWriter lros = new LimitedResultWriter(1000);
				try{
					stw.write(value, lros);
				}catch(/*IOLimitReachedException*/ RuntimeException iolrex){
					// This is fine, ignore.
				}catch(IOException ioex){
					// This can never happen.
				}
				content += "Tree: " + lros.toString();
			} else {
				content = result.toString(4096) + "\n";
			}
		} else {
			content = "ok\n";
		}
		return content;
	}

	public static void parseErrorMessage(PrintWriter content, String command, String scheme, ParseError pe, StandardTextWriter writer) {
		if (pe.getLocation().getScheme().equals(scheme)) {
			String[] commandLines = command.split("\n");
			int lastLine = commandLines.length;
			int lastColumn = commandLines[lastLine - 1].length();

			if (!(pe.getEndLine() + 1 == lastLine && lastColumn <= pe.getEndColumn())) { 
				int i = 0;
				for ( ; i < pe.getEndColumn() + PROMPT.length(); i++) {
					content.append(' ');
				}
				content.append('^');
				content.append(' ');
				content.append("Parse error here");
				if (i > 80) {
					content.append("\nParse error at column ");
					content.append(""+pe.getEndColumn());
				}
				
				content.append("\n");
			}
		}
		else {
		    printSourceLocation(content, pe.getLocation(), writer);
			content.println(": Parse error");
		}
		
		content.flush();
	}

	public static String interruptedExceptionMessage(InterruptException i) {
		String content;
		content = i.getMessage();
		return content;
	}

	public static String ambiguousMessage(Ambiguous e) {
		StringBuilder content = new StringBuilder();
		content.append(e.getLocation());
		content.append(':');
		content.append(' ');
		content.append(e.getMessage());
		content.append('\t');
		IConstructor tree = e.getTree();
		if (tree != null) {
			content.append("diagnose using: ");
			content.append(getValueString(e.getTree()));
		}
		content.append('\n');
		return content.toString();
	}
	
	private static String getValueString(IConstructor tree) {
		String val = tree.toString();
		val = val.replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
		val = val.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		val = val.replaceAll("<", Matcher.quoteReplacement("\\<"));
		val = val.replaceAll(">", Matcher.quoteReplacement("\\>"));
		return "\"" + val + "\"";
	}

	public static void throwableMessage(PrintWriter out, Throwable e, StackTrace rascalTrace, StandardTextWriter prettyPrinter) {
	    out.println(e.toString());
		out.println("(internal error)");
		try {
            rascalTrace.prettyPrintedString(out, prettyPrinter);
            out.println();
        }
        catch (IOException e1) {
            out.println("Error printing stack trace");
        }
		e.printStackTrace(out);
		out.println();
		out.flush();
	}
	
	public static void parseOrStaticOrThrowMessage(PrintWriter out, RuntimeException e, StandardTextWriter prettyPrinter) {
		if (e instanceof ParseError) {
			parseErrorMessage(out, "unknown", "unknown", (ParseError)e, prettyPrinter);
		}
		else if (e instanceof StaticError)  {
			staticErrorMessage(out, (StaticError)e, prettyPrinter);
        }
        else if (e instanceof Throw) {
                throwMessage(out, (Throw)e, prettyPrinter);
        }
        else {
            out.write("Not a rascal exception: " + e.toString());
        }
	}
	public static void throwMessage(PrintWriter out, Throw e, StandardTextWriter prettyPrinter) {
		LimitedResultWriter lros = new LimitedResultWriter(1000);
		try {
		    if (e.getException() != null) {
                prettyPrinter.write(e.getException(), lros);
            }
		}
		catch(/*IOLimitReachedException*/ RuntimeException iolrex){
			// This is fine, ignore.
		}
		catch(IOException ioex){
			// This can/should never happen.
		}
		
		ISourceLocation loc = e.getLocation();
		if (loc != null) {
		    printSourceLocation(out, loc, prettyPrinter);
	          out.print(": ");
		}
		else {
		    out.print("unknown location: ");
		}
		out.println(lros.getBuffer().toString());
		
		StackTrace trace = e.getTrace();
		if (trace != null) {
		    try {
                trace.prettyPrintedString(out, prettyPrinter);
            }
            catch (IOException e1) {
                out.println("Error printing stacktrace");
            }
		}
	}

    private static void printSourceLocation(PrintWriter out, ISourceLocation l, StandardTextWriter prettyPrinter) {
        try {
		    prettyPrinter.write(l, out);
		} catch (IOException ex) {
		    out.print("Error printing location");
		}
    }
	
	public static void staticErrorMessage(PrintWriter out, StaticError e, StandardTextWriter writer)  {
		printSourceLocation(out, e.getLocation(), writer);
	    out.print(": ");
	    out.println(e.getMessage());
	    out.flush();
	}
}
