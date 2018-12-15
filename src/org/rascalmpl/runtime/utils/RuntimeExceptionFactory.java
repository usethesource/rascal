/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.StackTrace;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * This class defines and implements all dynamic (run-time) exceptions that
 * can be thrown by the Rascal interpreter. It creates exceptions that can be
 * caught by Rascal code.
 * <br>
 * Static errors such as parse errors and type errors are something different.
 */
public class RuntimeExceptionFactory {
	private static TypeFactory TF = TypeFactory.getInstance();
	private static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	public static final TypeStore TS = new TypeStore();
	public static final Type Exception = TF.abstractDataType(TS, "RuntimeException");
	
	public static final Type StackOverflow = TF.constructor(TS, Exception, "StackOverflow");
    public static final Type IndexOutOfBounds = TF.constructor(TS, Exception, "IndexOutOfBounds", TF.integerType(), "index");
	public static final Type AssertionFailed = TF.constructor(TS,Exception,"AssertionFailed");
	public static final Type LabeledAssertionFailed = TF.constructor(TS,Exception,"AssertionFailed", TF.stringType(), "label");
	public static final Type EmptyList = TF.constructor(TS,Exception,"EmptyList");
	public static final Type EmptySet = TF.constructor(TS,Exception,"EmptySet");
	public static final Type EmptyMap = TF.constructor(TS,Exception,"EmptyMap");
	public static final Type CallFailed = TF.constructor(TS, Exception, "CallFailed", TF.sourceLocationType(), "caller", TF.listType(TF.valueType()), "arguments");
	public static final Type NoSuchElement = TF.constructor(TS,Exception,"NoSuchElement",TF.valueType(), "v");
	public static final Type UnavailableInformation = TF.constructor(TS,Exception, "UnavailableInformation");
	public static final Type IllegalArgument = TF.constructor(TS,Exception,"IllegalArgument",TF.valueType(), "v", TF.stringType(), "message");
	public static final Type IllegalTypeArgument = TF.constructor(TS,Exception,"IllegalTypeArgument",TF.stringType(), "type", TF.stringType(), "message");

	public static final Type AnonymousIllegalArgument = TF.constructor(TS,Exception,"IllegalArgument");
	public static final Type IO = TF.constructor(TS,Exception,"IO",TF.stringType(), "message");
	public static final Type PathNotFound = TF.constructor(TS,Exception,"PathNotFound",TF.sourceLocationType(), "location");
	
	public static final Type LocationNotFound = TF.constructor(TS,Exception,"LocationNotFound",TF.sourceLocationType(), "location");
	public static final Type PermissionDenied = TF.constructor(TS,Exception,"PermissionDenied",TF.stringType(), "message");
	public static final Type AnonymousPermissionDenied = TF.constructor(TS,Exception,"PermissionDenied");
	public static final Type ModuleNotFound = TF.constructor(TS, Exception, "ModuleNotFound", TF.stringType(), "name");
	public static final Type MultipleKey = TF.constructor(TS, Exception, "MultipleKey", TF.valueType(), "key", TF.valueType(), "first", TF.valueType(), "second");
	public static final Type NoSuchKey = TF.constructor(TS, Exception, "NoSuchKey", TF.valueType(), "key");
	public static final Type NoSuchAnnotation = TF.constructor(TS, Exception, "NoSuchAnnotation", TF.stringType(), "label");
	public static final Type NoSuchField = TF.constructor(TS, Exception, "NoSuchField", TF.stringType(), "label");
	public static final Type ParseError = TF.constructor(TS, Exception, "ParseError", TF.sourceLocationType(), "location");
	public static final Type Ambiguity = TF.constructor(TS, Exception, "Ambiguity", TF.sourceLocationType(), "location", TF.stringType(), "nonterminal", TF.stringType(), "sentence");
	public static final Type IllegalIdentifier = TF.constructor(TS, Exception, "IllegalIdentifier", TF.stringType(), "name");
	public static final Type IllegalChar = TF.constructor(TS, Exception, "IllegalCharacter", TF.integerType(), "character");
	public static final Type SchemeNotSupported = TF.constructor(TS, Exception, "SchemeNotSupported", TF.sourceLocationType(), "location");
	public static final Type MalFormedURI = TF.constructor(TS, Exception, "MalFormedURI", TF.stringType(), "malFormedUri");
	public static final Type NoParent = TF.constructor(TS, Exception, "NoParent", TF.sourceLocationType(), "noParentUri");
	public static final Type NameMismatch = TF.constructor(TS, Exception, "NameMismatch", TF.stringType(), "expectedName", TF.stringType(), "gotName");
	public static final Type ArityMismatch = TF.constructor(TS, Exception, "ArityMismatch", TF.integerType(), "expectedArity", TF.integerType(), "gotArity");

	public static final Type Java = TF.constructor(TS, Exception, "Java", TF.stringType(), "class", TF.stringType(), "message");
	public static final Type JavaWithCause = TF.constructor(TS, Exception, "Java", TF.stringType(), "class", TF.stringType(), "message", Exception, "cause");
  
	public static final Type Subversion = TF.constructor(TS, Exception, "Subversion", TF.stringType(), "message");

	public static final Type InvalidUseOfDate = TF.constructor(TS, Exception, "InvalidUseOfDate", TF.dateTimeType(), "msg");
	public static final Type InvalidUseOfTime = TF.constructor(TS, Exception, "InvalidUseOfTime", TF.dateTimeType(), "msg");
	public static final Type InvalidUseOfDateTime = TF.constructor(TS, Exception, "InvalidUseOfDateTime", TF.dateTimeType(), "msg");
	public static final Type InvalidUseOfLocation = TF.constructor(TS, Exception, "InvalidUseOfLocation", TF.stringType(), "message");
	public static final Type DateTimeParsingError = TF.constructor(TS, Exception, "DateTimeParsingError", TF.stringType(), "message");
	public static final Type DateTimePrintingError = TF.constructor(TS, Exception, "DateTimePrintingError", TF.stringType(), "message");
	public static final Type Timeout = TF.constructor(TS, Exception, "Timeout");
	public static final Type Figure = TF.constructor(TS, Exception, "Figure", TF.stringType(), "message", TF.valueType(), "figure");
	
	public static final Type ImplodeError = TF.constructor(TS, Exception, "ImplodeError", TF.stringType(), "message");

	public static final Type ArithmeticException = TF.constructor(TS, Exception, "ArithmeticException", TF.stringType(), "message");
	
	// The "official" exceptions that a Rascal program can catch (alphabetical order)
	
	public static Throw ambiguity(ISourceLocation loc, IString type, IString string, AbstractAST ast, StackTrace trace) {
	  return new Throw(VF.constructor(Ambiguity, loc, type, string), ast, trace);
	}
	
	public static Throw callFailed(ISourceLocation loc, IList arguments, AbstractAST ast, StackTrace trace) {
	    return new Throw(VF.constructor(CallFailed, loc, arguments), ast, trace);
	}
	            
	public static Throw arithmeticException(String msg, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(ArithmeticException, VF.string(msg)), ast, trace);
	}
	
	public static Throw assertionFailed(AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(AssertionFailed), ast, trace);
	}
	
	public static Throw assertionFailed(IString msg, AbstractAST ast, StackTrace trace) {
    	return new Throw(VF.constructor(LabeledAssertionFailed, msg), ast, trace);
    }
	
	public static Throw emptyList(AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(EmptyList), ast, trace);
	}
	
	public static Throw emptyMap(AbstractAST ast, StackTrace trace) {
	  return new Throw(VF.constructor(EmptyMap), ast, trace);
	}
	
	public static Throw emptySet(AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(EmptySet), ast, trace);
	}
	
	public static Throw illegalArgument(AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(AnonymousIllegalArgument), ast, trace);	
	}
	
	public static Throw illegalArgument(IValue v, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(IllegalArgument), ast, trace);	
	}
	
	public static Throw illegalArgument(IValue v, AbstractAST ast, StackTrace trace, String message) {
		return new Throw(VF.constructor(IllegalArgument, v, VF.string(message)), ast, trace);	
	}
	
	public static Throw indexOutOfBounds(IInteger i, AbstractAST ast, StackTrace trace) {
    	return new Throw(VF.constructor(IndexOutOfBounds, i), ast, trace);
    }
	
	public static Throw io(IString msg, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(IO, msg), ast, trace);
	}
	
	private static Throw javaException(String clazz, String message, IValue cause, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(Java, VF.string(clazz), VF.string(message), cause), ast, trace);
	}

	private static Throw javaException(String clazz, String message, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(Java, VF.string(clazz), VF.string(message)), ast, trace);
	}
	
	public static Throw javaException(Throwable targetException, AbstractAST ast, StackTrace rascalTrace) throws ImplementationError {
		try {
			String clazz = targetException.getClass().getSimpleName();
			String msg = targetException.getMessage();
			StackTrace trace = buildTrace(targetException, rascalTrace);
			Throwable cause = targetException.getCause();

			if (cause != null && cause != targetException) {
				Throw throwCause = cause instanceof Throw ? (Throw) cause : javaException(cause, ast, rascalTrace);
				return javaException(clazz, msg != null ? msg : "", throwCause.getException(), ast, trace);
			}
			else {
				return javaException(clazz, msg != null ? msg : "", ast, trace);
			}
		} catch (IOException e1) {
			throw new ImplementationError("Could not create stack trace", e1);
		}
	}
	
	 /*
     * Robust version of sourceLocation, to be called only from error situations to avoid nested
     * error traces.
     */
	private static ISourceLocation robustSourceLocation(String path, int offset, int length, int beginLine, int endLine, int beginCol, int endCol) {
	  if (path == null) {
	    path = "UNKNOWN_FILENAME";
	  }
	  if (!path.startsWith("/")) {
	    path = "/" + path;
	  }
	  ISourceLocation locRoot;
	  try {
	    locRoot = VF.sourceLocation("unknown", "", path);
	  } catch (URISyntaxException e) {
	    locRoot = VF.sourceLocation(path); // fall back, shouldn't happen.
	  }

	  if (offset < 0) 
	    offset = 0;
	  if (length < 0) 
	    length = 0;
	  if (beginLine < 0) 
	    beginLine = 0;
	  if (beginCol < 0) 
	    beginCol = 0;
	  if (endCol < 0) 
	    endCol = 0;
	  if (endLine < beginLine) 
	    endLine = beginLine;
	  if (endLine == beginLine && endCol < beginCol) 
	    endCol = beginCol;
	  return VF.sourceLocation(locRoot, offset, length, beginLine, endLine, beginCol, endCol);
	}

	private static StackTrace buildTrace(Throwable targetException, StackTrace rascalTrace) throws IOException {
		StackTraceElement[] stackTrace = targetException.getStackTrace();
		StackTrace newTrace = new StackTrace();
		for (StackTraceElement elem : stackTrace) {
			if (elem.getMethodName().equals("invoke")) {
				break;
			}
			newTrace.add(robustSourceLocation(elem.getFileName(), 0, 0, elem.getLineNumber(), elem.getLineNumber(), 0, 0), elem.getClassName() + "." + elem.getMethodName());
		}
		newTrace.addAll(rascalTrace);
		return newTrace.freeze();
	}
	
	public static Throw moduleNotFound(IString module, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(ModuleNotFound, module), ast, trace);
	}
	
	public static Throw noSuchAnnotation(String label, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(NoSuchAnnotation, VF.string(label)), ast, trace);
	}


	public static Throw noSuchKey(IValue v, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(NoSuchKey, v), ast, trace);
	}
	
	public static Throw parseError(ISourceLocation loc, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(ParseError, loc), ast, trace);
	}
	
	public static Throw pathNotFound(ISourceLocation loc, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(PathNotFound, loc), ast, trace);
	}
	
	public static Throw stackOverflow(AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(StackOverflow), ast, trace);
	}
	
	// The status of the following exceptions is to be determined (alphabetical order)
	
	public static Throw arityMismatch(int expected, int got, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(ArityMismatch, VF.integer(expected), VF.integer(got)), ast, trace);
	}
	
	public static Throw dateTimeParsingError(String message, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(DateTimeParsingError, VF.string(message)), ast, trace);
	}
	
	public static Throw dateTimePrintingError(String message, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(DateTimePrintingError, VF.string(message)), ast, trace);
	}	
	
	public static Throw figureException(String message, IValue v, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(Figure, VF.string(message), v), ast, trace);
	}
	
	public static Throw illegalCharacter(IInteger i, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(IllegalChar, i), ast, trace);
	}
	
//	public static Throw illegalIdentifier(String name,
//			AbstractAST ast, StackTrace trace) {
//		return new Throw(IllegalIdentifier.make(VF, VF.string(name)), ast, trace);
//	}
	
	public static Throw illegalTypeArgument(String type, AbstractAST ast, StackTrace trace, String message){
		return new Throw(VF.constructor(IllegalTypeArgument,VF.string(type),VF.string(message)), ast, trace);	
	}
	
	public static Throw illegalTypeArgument(String type, AbstractAST ast, StackTrace trace){
		return new Throw(VF.constructor(IllegalTypeArgument,VF.string(type), VF.string("")), ast, trace);	
	}
	
	public static Throw implodeError(String msg, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(ImplodeError, VF.string(msg)), ast, trace);
	}

	public static Throw invalidUseOfLocation(String msg, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(InvalidUseOfLocation, VF.string(msg)), ast, trace);
	}	
	
	public static Throw invalidUseOfDateException(String message, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(InvalidUseOfDate, VF.string(message)), ast, trace);
	}
	
	public static Throw invalidUseOfTimeException(String message, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(InvalidUseOfTime, VF.string(message)), ast, trace);
	}
	
	public static Throw invalidUseOfDateTimeException(String message, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(InvalidUseOfDateTime, VF.string(message)), ast, trace);
	}
	
//	public static Throw locationNotFound(ISourceLocation loc, AbstractAST ast, StackTrace trace) {
//		return new Throw(LocationNotFound.make(VF, loc), ast, trace);
//	}
	
	public static Throw malformedURI(String uri, AbstractAST x, StackTrace trace) {
		return new Throw(VF.constructor(MalFormedURI, VF.string(uri)), x, trace);
	}
	
	public static Throw MultipleKey(IValue v, IValue first, IValue second, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(MultipleKey, v, first, second), ast, trace);
	}
	
	public static Throw nameMismatch(String expected, String got, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(NameMismatch, VF.string(expected), VF.string(got)), ast, trace);
	}
	
	public static Throw noParent(ISourceLocation loc, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(NoParent, loc), ast, trace);
	}
	
	public static Throw noSuchElement(IValue v, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(NoSuchElement,v), ast, trace);	
	}

	public static Throw noSuchField(String name, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(NoSuchField, VF.string(name)), ast, trace);
	}
	
	public static Throw permissionDenied(AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(AnonymousPermissionDenied), ast, trace);
	}
	
	public static Throw permissionDenied(IString msg, AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(PermissionDenied, msg), ast, trace);
	}

	public static Throw unavailableInformation(AbstractAST ast, StackTrace trace){
		return new Throw(VF.constructor(UnavailableInformation), ast, trace);	
	}

//	public static Throw subversionException(String message, AbstractAST ast, StackTrace trace) {
//		return new Throw(Subversion.make(VF, VF.string(message)), ast, trace);
//	}
	
	public static Throw schemeNotSupported(ISourceLocation file,
			AbstractAST ast, StackTrace trace) {
		return new Throw(VF.constructor(SchemeNotSupported, file), ast, trace);
	}
	
	public static Throw timeout(AbstractAST ast, StackTrace trace) {
    	return new Throw(VF.constructor(Timeout), ast, trace);
    }
	
}
