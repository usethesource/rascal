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
	private static TypeStore TS = new TypeStore();
	private static Type E = TF.abstractDataType(TS, "RuntimeException"); 
    private static Type IndexOutOfBounds = TF.constructor(TS, E, "IndexOutOfBounds", TF.integerType(), "index");
	private static Type AssertionFailed = TF.constructor(TS,E,"AssertionFailed");
	private static Type LabeledAssertionFailed = TF.constructor(TS,E,"AssertionFailed", TF.stringType(), "label");
	private static Type EmptyList = TF.constructor(TS,E,"EmptyList");
	private static Type EmptySet = TF.constructor(TS,E,"EmptySet");
	private static Type EmptyMap = TF.constructor(TS,E,"EmptyMap");
	private static Type NoSuchElement = TF.constructor(TS,E,"NoSuchElement",TF.valueType(), "v");
	private static Type IllegalArgument = TF.constructor(TS,E,"IllegalArgument",TF.valueType(), "v");
	private static Type AnonymousIllegalArgument = TF.constructor(TS,E,"IllegalArgument");
	private static Type IO = TF.constructor(TS,E,"IO",TF.stringType(), "message");
	private static Type PathNotFound = TF.constructor(TS,E,"PathNotFound",TF.sourceLocationType(), "location");
	
	private static Type LocationNotFound = TF.constructor(TS,E,"LocationNotFound",TF.sourceLocationType(), "location");
	private static Type PermissionDenied = TF.constructor(TS,E,"PermissionDenied",TF.stringType(), "message");
	private static Type AnonymousPermissionDenied = TF.constructor(TS,E,"PermissionDenied");
	private static Type ModuleNotFound = TF.constructor(TS, E, "ModuleNotFound", TF.stringType(), "name");
	private static Type MultipleKey = TF.constructor(TS, E, "MultipleKey", TF.valueType(), "key");
	private static Type NoSuchKey = TF.constructor(TS, E, "NoSuchKey", TF.valueType(), "key");
	private static Type NoSuchAnnotation = TF.constructor(TS, E, "NoSuchAnnotation", TF.stringType(), "label");
	private static Type NoSuchField = TF.constructor(TS, E, "NoSuchField", TF.stringType(), "label");
	private static Type ParseError = TF.constructor(TS, E, "ParseError", TF.sourceLocationType(), "location");
	private static Type IllegalIdentifier = TF.constructor(TS, E, "IllegalIdentifier", TF.stringType(), "name");
	private static Type SchemeNotSupported = TF.constructor(TS, E, "SchemeNotSupported", TF.sourceLocationType(), "location");
	private static Type MalFormedURI = TF.constructor(TS, E, "MalFormedURI", TF.stringType(), "uri");
	private static Type NameMismatch = TF.constructor(TS, E, "NameMismatch", TF.stringType(), "expected", TF.stringType(), "got");
	private static Type ArityMismatch = TF.constructor(TS, E, "ArityMismatch", TF.integerType(), "expected", TF.integerType(), "got");

	private static Type Java = TF.constructor(TS, E, "Java", TF.stringType(), "message");
	private static Type Subversion = TF.constructor(TS, E, "Subversion", TF.stringType(), "message");
	
	private static Type InvalidUseOfDate = TF.constructor(TS, E, "InvalidUseOfDate", TF.dateTimeType(), "msg");
	private static Type InvalidUseOfTime = TF.constructor(TS, E, "InvalidUseOfTime", TF.dateTimeType(), "msg");
	private static Type InvalidUseOfDateTime = TF.constructor(TS, E, "InvalidUseOfDateTime", TF.dateTimeType(), "msg");
	private static Type DateTimeParsingError = TF.constructor(TS, E, "DateTimeParsingError", TF.stringType(), "message");
	private static Type DateTimePrintingError = TF.constructor(TS, E, "DateTimePrintingError", TF.stringType(), "message");

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
	
	public static Throw illegalArgument(IValue v, AbstractAST ast, String trace) {
		return new Throw(IllegalArgument.make(VF,v), ast, trace);	
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
}
