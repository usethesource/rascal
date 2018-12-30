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

import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * This class defines and implements all dynamic (run-time) exceptions that
 * can be generated by Rascal code. It creates exceptions that can be
 * caught by Rascal code.
 * <br>
 * Static errors such as parse errors and type errors are handled before execution of the Rascal code.
 */
public class RascalExceptionFactory {
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
	
	public static final Type InvalidArgument = TF.constructor(TS,Exception,"InvalidArgument",TF.valueType(), "v", TF.stringType(), "message");
	public static final Type AnonymousInvalidArgument = TF.constructor(TS,Exception,"InvalidArgument");
	
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
	
	public static RascalException ambiguity(ISourceLocation loc, IString type, IString string) {
	  return new RascalException(VF.constructor(Ambiguity, loc, type, string));
	}
	
	public static RascalException callFailed(ISourceLocation loc, IList arguments) {
	    return new RascalException(VF.constructor(CallFailed, loc, arguments));
	}
	            
	public static RascalException arithmeticException(String msg) {
		return new RascalException(VF.constructor(ArithmeticException, VF.string(msg)));
	}
	
	public static RascalException assertionFailed() {
		return new RascalException(VF.constructor(AssertionFailed));
	}
	
	public static RascalException assertionFailed(IString msg) {
    	return new RascalException(VF.constructor(LabeledAssertionFailed, msg));
    }
	
	public static RascalException emptyList() {
		return new RascalException(VF.constructor(EmptyList));
	}
	
	public static RascalException emptyMap() {
	  return new RascalException(VF.constructor(EmptyMap));
	}
	
	public static RascalException emptySet() {
		return new RascalException(VF.constructor(EmptySet));
	}
	
	public static RascalException illegalArgument() {
		return new RascalException(VF.constructor(AnonymousIllegalArgument));	
	}
	
	public static RascalException illegalArgument(IValue v) {
		return new RascalException(VF.constructor(IllegalArgument));	
	}
	
	public static RascalException illegalArgument(IValue v, String message) {
		return new RascalException(VF.constructor(IllegalArgument, v, VF.string(message)));	
	}
	
	public static RascalException invalidArgument() {
		return new RascalException(VF.constructor(AnonymousInvalidArgument));	
	}
	
	public static RascalException invalidArgument(IValue v) {
		return new RascalException(VF.constructor(InvalidArgument));	
	}
	
	public static RascalException invalidrgument(IValue v, String message) {
		return new RascalException(VF.constructor(InvalidArgument, v, VF.string(message)));	
	}
	
	public static RascalException indexOutOfBounds(IInteger i) {
    	return new RascalException(VF.constructor(IndexOutOfBounds, i));
    }
	
	public static RascalException io(IString msg) {
		return new RascalException(VF.constructor(IO, msg));
	}
	
	public static RascalException moduleNotFound(IString module) {
		return new RascalException(VF.constructor(ModuleNotFound, module));
	}
	
	public static RascalException noSuchAnnotation(String label) {
		return new RascalException(VF.constructor(NoSuchAnnotation, VF.string(label)));
	}

	public static RascalException noSuchKey(IValue v) {
		return new RascalException(VF.constructor(NoSuchKey, v));
	}
	
	public static RascalException parseError(ISourceLocation loc) {
		return new RascalException(VF.constructor(ParseError, loc));
	}
	
	public static RascalException pathNotFound(ISourceLocation loc) {
		return new RascalException(VF.constructor(PathNotFound, loc));
	}
	
	public static RascalException stackOverflow() {
		return new RascalException(VF.constructor(StackOverflow));
	}
	
	// The status of the following exceptions is to be determined (alphabetical order)
	
	public static RascalException arityMismatch(int expected, int got) {
		return new RascalException(VF.constructor(ArityMismatch, VF.integer(expected), VF.integer(got)));
	}
	
	public static RascalException dateTimeParsingError(String message) {
		return new RascalException(VF.constructor(DateTimeParsingError, VF.string(message)));
	}
	
	public static RascalException dateTimePrintingError(String message) {
		return new RascalException(VF.constructor(DateTimePrintingError, VF.string(message)));
	}	
	
	public static RascalException figureException(String message, IValue v) {
		return new RascalException(VF.constructor(Figure, VF.string(message), v));
	}
	
	public static RascalException illegalCharacter(IInteger i) {
		return new RascalException(VF.constructor(IllegalChar, i));
	}
	
//	public static RascalException illegalIdentifier(String name,
//			) {
//		return new RascalException(IllegalIdentifier.make(VF, VF.string(name)));
//	}
	
	public static RascalException illegalTypeArgument(String type, String message){
		return new RascalException(VF.constructor(IllegalTypeArgument,VF.string(type),VF.string(message)));	
	}
	
	public static RascalException illegalTypeArgument(String type){
		return new RascalException(VF.constructor(IllegalTypeArgument,VF.string(type), VF.string("")));	
	}
	
	public static RascalException implodeError(String msg) {
		return new RascalException(VF.constructor(ImplodeError, VF.string(msg)));
	}

	public static RascalException invalidUseOfLocation(String msg) {
		return new RascalException(VF.constructor(InvalidUseOfLocation, VF.string(msg)));
	}	
	
	public static RascalException invalidUseOfDateException(String message) {
		return new RascalException(VF.constructor(InvalidUseOfDate, VF.string(message)));
	}
	
	public static RascalException invalidUseOfTimeException(String message) {
		return new RascalException(VF.constructor(InvalidUseOfTime, VF.string(message)));
	}
	
	public static RascalException invalidUseOfDateTimeException(String message) {
		return new RascalException(VF.constructor(InvalidUseOfDateTime, VF.string(message)));
	}
	
//	public static RascalException locationNotFound(ISourceLocation loc) {
//		return new RascalException(LocationNotFound.make(VF, loc));
//	}
	
	public static RascalException malformedURI(String uri) {
		return new RascalException(VF.constructor(MalFormedURI, VF.string(uri)));
	}
	
	public static RascalException MultipleKey(IValue v, IValue first, IValue second) {
		return new RascalException(VF.constructor(MultipleKey, v, first, second));
	}
	
	public static RascalException nameMismatch(String expected, String got) {
		return new RascalException(VF.constructor(NameMismatch, VF.string(expected), VF.string(got)));
	}
	
	public static RascalException noParent(ISourceLocation loc) {
		return new RascalException(VF.constructor(NoParent, loc));
	}
	
	public static RascalException noSuchElement(IValue v) {
		return new RascalException(VF.constructor(NoSuchElement,v));	
	}

	public static RascalException noSuchField(String name) {
		return new RascalException(VF.constructor(NoSuchField, VF.string(name)));
	}
	
	public static RascalException permissionDenied() {
		return new RascalException(VF.constructor(AnonymousPermissionDenied));
	}
	
	public static RascalException permissionDenied(IString msg) {
		return new RascalException(VF.constructor(PermissionDenied, msg));
	}

	public static RascalException unavailableInformation(){
		return new RascalException(VF.constructor(UnavailableInformation));	
	}

	public static RascalException schemeNotSupported(ISourceLocation file) {
		return new RascalException(VF.constructor(SchemeNotSupported, file));
	}
	
	public static RascalException timeout() {
    	return new RascalException(VF.constructor(Timeout));
    }
	
}
