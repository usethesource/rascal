/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
package org.rascalmpl.interpreter.utils;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.LocationLiteral.Default;
import org.rascalmpl.interpreter.control_exceptions.Throw;
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
	public static final Type MultipleKey = TF.constructor(TS, Exception, "MultipleKey", TF.valueType(), "key");
	public static final Type NoSuchKey = TF.constructor(TS, Exception, "NoSuchKey", TF.valueType(), "key");
	public static final Type NoSuchAnnotation = TF.constructor(TS, Exception, "NoSuchAnnotation", TF.stringType(), "label");
	public static final Type NoSuchField = TF.constructor(TS, Exception, "NoSuchField", TF.stringType(), "label");
	public static final Type ParseError = TF.constructor(TS, Exception, "ParseError", TF.sourceLocationType(), "location");
	public static final Type IllegalIdentifier = TF.constructor(TS, Exception, "IllegalIdentifier", TF.stringType(), "name");
	public static final Type IllegalChar = TF.constructor(TS, Exception, "IllegalCharacter", TF.integerType(), "character");
	public static final Type SchemeNotSupported = TF.constructor(TS, Exception, "SchemeNotSupported", TF.sourceLocationType(), "location");
	public static final Type MalFormedURI = TF.constructor(TS, Exception, "MalFormedURI", TF.stringType(), "uri");
	public static final Type NoParent = TF.constructor(TS, Exception, "NoParent", TF.sourceLocationType(), "uri");
	public static final Type NameMismatch = TF.constructor(TS, Exception, "NameMismatch", TF.stringType(), "expected", TF.stringType(), "got");
	public static final Type ArityMismatch = TF.constructor(TS, Exception, "ArityMismatch", TF.integerType(), "expected", TF.integerType(), "got");

	public static final Type Java = TF.constructor(TS, Exception, "Java", TF.stringType(), "message");
	public static final Type Subversion = TF.constructor(TS, Exception, "Subversion", TF.stringType(), "message");
	public static final Type JavaBytecodeError = TF.constructor(TS, Exception, "JavaBytecodeError", TF.stringType(), "message");

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

	public static Throw stackOverflow(AbstractAST ast, String trace) {
		return new Throw(StackOverflow.make(VF), ast, trace);
	}
	
    public static Throw indexOutOfBounds(IInteger i, AbstractAST ast, String trace) {
    	return new Throw(IndexOutOfBounds.make(VF, i), ast, trace);
    }
    
    public static Throw assertionFailed(IString msg, AbstractAST ast, String trace) {
    	return new Throw(LabeledAssertionFailed.make(VF, msg), ast, trace);
    }
    
    public static Throw assertionFailed(AbstractAST ast, String trace) {
    	return new Throw(AssertionFailed.make(VF), ast, trace);
    }

	public static Throw emptyList(AbstractAST ast, String trace) {
		return new Throw(EmptyList.make(VF), ast, trace);
	}
	
	public static Throw emptySet(AbstractAST ast, String trace) {
		return new Throw(EmptySet.make(VF), ast, trace);
	}
	
	public static Throw emptyMap(AbstractAST ast, String trace) {
		return new Throw(EmptyMap.make(VF), ast, trace);
	}

	public static Throw noSuchElement(IValue v, AbstractAST ast, String trace) {
		return new Throw(NoSuchElement.make(VF,v), ast, trace);	
	}
	
	public static Throw illegalArgument(IValue v, AbstractAST ast, String trace, String message) {
		return new Throw(IllegalArgument.make(VF,v,VF.string(message)), ast, trace);	
	}
	
	public static Throw illegalArgument(IValue v, AbstractAST ast, String trace) {
		return new Throw(IllegalArgument.make(VF,v), ast, trace);	
	}
	
	public static Throw illegalTypeArgument(String type, AbstractAST ast, String trace, String message){
		return new Throw(IllegalTypeArgument.make(VF,VF.string(type),VF.string(message)), ast, trace);	
	}
	
	public static Throw illegalTypeArgument(String type, AbstractAST ast, String trace){
		return new Throw(IllegalTypeArgument.make(VF,VF.string(type)), ast, trace);	
	}
	
	public static Throw unavailableInformation(AbstractAST ast, String trace){
		return new Throw(UnavailableInformation.make(VF), ast, trace);	
	}
	
	public static Throw illegalArgument(AbstractAST ast, String trace) {
		return new Throw(AnonymousIllegalArgument.make(VF), ast, trace);	
	}
	
	public static Throw pathNotFound(ISourceLocation loc, AbstractAST ast, String trace) {
		return new Throw(PathNotFound.make(VF, loc), ast, trace);
	}
	
	public static Throw locationNotFound(ISourceLocation loc, AbstractAST ast, String trace) {
		return new Throw(LocationNotFound.make(VF, loc), ast, trace);
	}
	
	public static Throw permissionDenied(AbstractAST ast, String trace) {
		return new Throw(AnonymousPermissionDenied.make(VF), ast, trace);
	}
	
	public static Throw permissionDenied(IString msg, AbstractAST ast, String trace) {
		return new Throw(PermissionDenied.make(VF, msg), ast, trace);
	}
	
	public static Throw io(IString msg, AbstractAST ast, String trace) {
		return new Throw(IO.make(VF, msg), ast, trace);
	}
	
	public static Throw moduleNotFound(IString module, AbstractAST ast, String trace) {
		return new Throw(ModuleNotFound.make(VF, module), ast, trace);
	}

	public static Throw noSuchKey(IValue v, AbstractAST ast, String trace) {
		return new Throw(NoSuchKey.make(VF, v), ast, trace);
	}
	
	public static Throw MultipleKey(IValue v, AbstractAST ast, String trace) {
		return new Throw(MultipleKey.make(VF, v), ast, trace);
	}
	
	public static Throw noSuchAnnotation(String label, AbstractAST ast, String trace) {
		return new Throw(NoSuchAnnotation.make(VF, VF.string(label)), ast, trace);
	}

	public static Throw javaException(String message, AbstractAST ast, String trace) {
		return new Throw(Java.make(VF, VF.string(message)), ast, trace);
	}

	public static Throw noSuchField(String name, AbstractAST ast, String trace) {
		return new Throw(NoSuchField.make(VF, VF.string(name)), ast, trace);
	}
	
	public static Throw parseError(ISourceLocation loc, AbstractAST ast, String trace) {
		return new Throw(ParseError.make(VF, loc), ast, trace);
	}

	public static Throw illegalIdentifier(String name,
			AbstractAST ast, String trace) {
		return new Throw(IllegalIdentifier.make(VF, VF.string(name)), ast, trace);
	}

	public static Throw schemeNotSupported(ISourceLocation file,
			AbstractAST ast, String trace) {
		return new Throw(SchemeNotSupported.make(VF, file), ast, trace);
	}

	public static Throw malformedURI(String uri, Default x, String trace) {
		return new Throw(MalFormedURI.make(VF, VF.string(uri)), x, trace);
	}
	
	public static Throw subversionException(String message, AbstractAST ast, String trace) {
		return new Throw(Subversion.make(VF, VF.string(message)), ast, trace);
	}
	
	public static Throw javaBytecodeError(String message, AbstractAST ast, String trace) {
		return new Throw(JavaBytecodeError.make(VF, VF.string(message)), ast, trace);
	}

	public static Throw invalidUseOfDateException(String message, AbstractAST ast, String trace) {
		return new Throw(InvalidUseOfDate.make(VF, VF.string(message)), ast, trace);
	}
	
	public static Throw invalidUseOfTimeException(String message, AbstractAST ast, String trace) {
		return new Throw(InvalidUseOfTime.make(VF, VF.string(message)), ast, trace);
	}
	
	public static Throw invalidUseOfDateTimeException(String message, AbstractAST ast, String trace) {
		return new Throw(InvalidUseOfDateTime.make(VF, VF.string(message)), ast, trace);
	}
	
	public static Throw dateTimeParsingError(String message, AbstractAST ast, String trace) {
		return new Throw(DateTimeParsingError.make(VF, VF.string(message)), ast, trace);
	}
	
	public static Throw dateTimePrintingError(String message, AbstractAST ast, String trace) {
		return new Throw(DateTimePrintingError.make(VF, VF.string(message)), ast, trace);
	}	
	
	public static Throw nameMismatch(String expected, String got, AbstractAST ast, String trace) {
		return new Throw(NameMismatch.make(VF, VF.string(expected), VF.string(got)), ast, trace);
	}
	
	public static Throw arityMismatch(int expected, int got, AbstractAST ast, String trace) {
		return new Throw(ArityMismatch.make(VF, VF.integer(expected), VF.integer(got)), ast, trace);
	}

	public static Throw noParent(ISourceLocation loc, AbstractAST ast, String trace) {
		return new Throw(NoParent.make(VF, loc), ast, trace);
	}
	
	public static Throw timeout(AbstractAST ast, String trace) {
    	return new Throw(Timeout.make(VF), ast, trace);
    }
	
	public static Throw figureException(String message, IValue v, AbstractAST ast, String trace) {
		return new Throw(Figure.make(VF, VF.string(message), v), ast, trace);
	}

	public static Throw illegalCharacter(IInteger i, AbstractAST ast, String trace) {
		return new Throw(IllegalChar.make(VF, i), ast, trace);
	}

	public static Throw invalidUseOfLocation(String msg, AbstractAST ast, String trace) {
		return new Throw(InvalidUseOfLocation.make(VF, VF.string(msg)), ast, trace);
	}
	
	public static Throw implodeError(String msg, AbstractAST ast, String trace) {
		return new Throw(ImplodeError.make(VF, VF.string(msg)), ast, trace);
	}
	
	public static Throw arithmeticException(String msg, AbstractAST ast, String trace) {
		return new Throw(ArithmeticException.make(VF, VF.string(msg)), ast, trace);
	}
}
