package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

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
import org.rascalmpl.values.uptr.RascalValueFactory;

public class RascalRuntimeException {
	
	private static TypeFactory TF = TypeFactory.getInstance();
	private static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	public static final TypeStore TS = new TypeStore(RascalValueFactory.getStore()); //new TypeStore();
	public static final Type Exception = TF.abstractDataType(TS, "RuntimeException");
	
//	public static final Type StackOverflow = TF.constructor(TS, Exception, "StackOverflow");
    public static final Type IndexOutOfBounds = TF.constructor(TS, Exception, "IndexOutOfBounds", TF.integerType(), "index");
//	public static final Type AssertionFailed = TF.constructor(TS,Exception,"AssertionFailed");
	public static final Type LabeledAssertionFailed = TF.constructor(TS,Exception,"AssertionFailed", TF.stringType(), "label");
	public static final Type EmptyList = TF.constructor(TS,Exception,"EmptyList");
	public static final Type EmptySet = TF.constructor(TS,Exception,"EmptySet");
	public static final Type EmptyMap = TF.constructor(TS,Exception,"EmptyMap");
//	public static final Type NoSuchElement = TF.constructor(TS,Exception,"NoSuchElement",TF.valueType(), "v");
	public static final Type UnavailableInformation = TF.constructor(TS,Exception, "UnavailableInformation", TF.stringType(), "message");
	public static final Type InvalidArgument = TF.constructor(TS,Exception,"InvalidArgument",TF.valueType(), "v", TF.stringType(), "message");
//	public static final Type IllegalTypeArgument = TF.constructor(TS,Exception,"IllegalTypeArgument",TF.stringType(), "type", TF.stringType(), "message");

	public static final Type AnonymousInvalidArgument = TF.constructor(TS,Exception,"InvalidArgument");
	public static final Type IO = TF.constructor(TS,Exception,"IO",TF.stringType(), "message");
//	public static final Type PathNotFound = TF.constructor(TS,Exception,"PathNotFound",TF.sourceLocationType(), "location");
	
//	public static final Type LocationNotFound = TF.constructor(TS,Exception,"LocationNotFound",TF.sourceLocationType(), "location");
//	public static final Type PermissionDenied = TF.constructor(TS,Exception,"PermissionDenied",TF.stringType(), "message");
//	public static final Type AnonymousPermissionDenied = TF.constructor(TS,Exception,"PermissionDenied");
//	public static final Type ModuleNotFound = TF.constructor(TS, Exception, "ModuleNotFound", TF.stringType(), "name");
//	public static final Type MultipleKey = TF.constructor(TS, Exception, "MultipleKey", TF.valueType(), "key");
	public static final Type NoSuchKey = TF.constructor(TS, Exception, "NoSuchKey", TF.valueType(), "key");
	public static final Type NoSuchAnnotation = TF.constructor(TS, Exception, "NoSuchAnnotation", TF.stringType(), "label");
	public static final Type NoSuchField = TF.constructor(TS, Exception, "NoSuchField", TF.stringType(), "label");
	public static final Type ParseError = TF.constructor(TS, Exception, "ParseError", TF.sourceLocationType(), "location");
	public static final Type Ambiguity = TF.constructor(TS, Exception, "Ambiguity", TF.sourceLocationType(), "location", TF.stringType(), "nonterminal", TF.stringType(), "sentence");
//	public static final Type IllegalIdentifier = TF.constructor(TS, Exception, "IllegalIdentifier", TF.stringType(), "name");
//	public static final Type IllegalChar = TF.constructor(TS, Exception, "IllegalCharacter", TF.integerType(), "character");
//	public static final Type SchemeNotSupported = TF.constructor(TS, Exception, "SchemeNotSupported", TF.sourceLocationType(), "location");
	public static final Type InvalidURI = TF.constructor(TS, Exception, "InvalidURI", TF.stringType(), "invalidUri");
	public static final Type NoParent = TF.constructor(TS, Exception, "NoParent", TF.sourceLocationType(), "noParentUri");
//	public static final Type NameMismatch = TF.constructor(TS, Exception, "NameMismatch", TF.stringType(), "expectedName", TF.stringType(), "gotName");
//	public static final Type ArityMismatch = TF.constructor(TS, Exception, "ArityMismatch", TF.integerType(), "expectedArity", TF.integerType(), "gotArity");

	public static final Type Java = TF.constructor(TS, Exception, "Java", TF.stringType(), "class", TF.stringType(), "message");
	public static final Type JavaWithCause = TF.constructor(TS, Exception, "Java", TF.stringType(), "class", TF.stringType(), "message", Exception, "cause");
  
//	public static final Type Subversion = TF.constructor(TS, Exception, "Subversion", TF.stringType(), "message");

	public static final Type InvalidUseOfDate = TF.constructor(TS, Exception, "InvalidUseOfDate", TF.dateTimeType(), "msg");
	public static final Type InvalidUseOfTime = TF.constructor(TS, Exception, "InvalidUseOfTime", TF.dateTimeType(), "msg");
//	public static final Type InvalidUseOfDateTime = TF.constructor(TS, Exception, "InvalidUseOfDateTime", TF.dateTimeType(), "msg");
	public static final Type InvalidUseOfLocation = TF.constructor(TS, Exception, "InvalidUseOfLocation", TF.stringType(), "message");
	public static final Type DateTimeParsingError = TF.constructor(TS, Exception, "DateTimeParsingError", TF.stringType(), "message");
	public static final Type DateTimePrintingError = TF.constructor(TS, Exception, "DateTimePrintingError", TF.stringType(), "message");
	public static final Type Timeout = TF.constructor(TS, Exception, "Timeout");
	public static final Type Figure = TF.constructor(TS, Exception, "Figure", TF.stringType(), "message", TF.valueType(), "figure");
	
	public static final Type ImplodeError = TF.constructor(TS, Exception, "ImplodeError", TF.stringType(), "message");

	public static final Type ArithmeticException = TF.constructor(TS, Exception, "ArithmeticException", TF.stringType(), "message");
	
	public static final Type UninitializedVariable = TF.constructor(TS, Exception, "UninitializedVariable", TF.stringType(), "name");

	public static final Type NoMainFunction = TF.constructor(TS, Exception, "NoMainFunction");
	
	public static final Type IncompletelyDefinedFunction = TF.constructor(TS, Exception, "IncompletelyDefinedFunction", TF.stringType(), "message");
	
	public static final Type RegExpSyntaxError = TF.constructor(TS, Exception, "RegExpSyntaxError", TF.stringType(), "message");

	public static final Type NotImplemented = TF.constructor(TS, Exception, "NotImplemented", TF.stringType(), "message");
	
	public static final Type Failed = TF.constructor(TS, Exception, "Failed", TF.sourceLocationType(), "caller", TF.listType(TF.valueType()), "arguments");
	
	public static Thrown ambiguity(ISourceLocation loc, IString type, IString string, Frame currentFrame) {
	    return Thrown.getInstance(VF.constructor(Ambiguity, loc, type, string), currentFrame);
	}
	
	public static Thrown failed(ISourceLocation loc, IList arguments, Frame currentFrame) {
	    return Thrown.getInstance(VF.constructor(Failed, loc, arguments), currentFrame);
	}
	
	public static Thrown arithmeticException(String msg, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(ArithmeticException, VF.string(msg)), currentFrame);
	}
	
	public static Thrown assertionFailed(IString msg, ISourceLocation loc, Frame currentFrame) {
	    return Thrown.getInstance(VF.constructor(LabeledAssertionFailed, msg), loc, currentFrame);
    }
	
	public static Thrown emptyList(Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(EmptyList), currentFrame);
	}
	
	public static Thrown emptyMap(Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(EmptyMap), currentFrame);
	}
	
	public static Thrown emptySet(Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(EmptySet), currentFrame);
	}

	public static Thrown indexOutOfBounds(IInteger i, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(IndexOutOfBounds, i), currentFrame);
	}
	
	public static Thrown invalidArgument(Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(AnonymousInvalidArgument), currentFrame);	
	}
	
	public static Thrown invalidArgument(IValue v, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(InvalidArgument, v), currentFrame);	
	}
	
	public static Thrown invalidArgument(IValue v, Frame currentFrame, String message) {
		return Thrown.getInstance(VF.constructor(InvalidArgument, v, VF.string(message)), currentFrame);	
	}
	
	public static Thrown invalidUseOfLocation(String msg, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(InvalidUseOfLocation, VF.string(msg)),  currentFrame);
	}   

	public static Thrown invalidURI(String uri, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(InvalidURI, VF.string(uri)), currentFrame);
	}

	public static Thrown invalidUseOfDate(String message, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(InvalidUseOfDate, VF.string(message)), currentFrame);
	}
	
//	public static Thrown invalidUseOfDateTime(String message, Frame currentFrame) {
//      return Thrown.getInstance(VF.constructor(InvalidUseOfDateTime, VF.string(message)),  currentFrame);
//    }

	public static Thrown invalidUseOfTime(String message, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(InvalidUseOfTime, VF.string(message)),  currentFrame);
	}
	
	public static Thrown io(IString msg, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(IO, msg), currentFrame);
	}
	
	private static Thrown javaException(String clazz, String message, IValue cause, ISourceLocation loc, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(Java, VF.string(clazz), VF.string(message), cause), loc, currentFrame);
	}

	private static Thrown javaException(String clazz, String message, ISourceLocation loc, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(Java, VF.string(clazz), VF.string(message)), loc, currentFrame);
	}
	
	public static Thrown javaException(Throwable targetException, ISourceLocation loc, Frame currentFrame) throws InternalCompilerError {
		String clazz = targetException.getClass().getSimpleName();
		String msg = targetException.getMessage();
		//List<Frame> trace = buildTrace(targetException, currentFrame);
		Throwable cause = targetException.getCause();

		if (cause != null && cause != targetException) {
			Thrown throwCause = cause instanceof Thrown ? (Thrown) cause : javaException(cause, loc, currentFrame);
			return javaException(clazz, msg != null ? msg : "", throwCause.getValue(), loc, currentFrame);
		}
		else {
			return javaException(clazz, msg != null ? msg : "", loc, currentFrame);
		}
	}

//	private static List<Frame> buildTrace(Throwable targetException, Frame currentFrame) throws IOException {
//		StackTraceElement[] elements = targetException.getStackTrace();
//		List<Frame> trace = new ArrayList<Frame>();
//		for(StackTraceElement elem : elements) {
//			if(elem.getMethodName().equals("invoke")) {
//				break;
//			}
//			Function function = new Function(elem.getClassName() + "." + elem.getMethodName(), null, null, -1, -1, null, -1, null, null);
//			trace.add(new Frame(-1, null, -1, function));
//		}
//		trace.addAll(stacktrace);
//		return trace;
//	}
	
//    private static ISourceLocation robustSourceLocation(String path, int offset, int length, int beginLine, int endLine, int beginCol, int endCol) {
//    	if (path == null) {
//    		path = "UNKNOWN_FILENAME";
//    	}
//    	if (!path.startsWith("/")) {
//			path = "/" + path;
//		}
//    	URI uri = null;
//		try {
//			uri = new URI("file", "", path, null);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//		}
//    	
//    	if (offset < 0) 
//    		offset = 0;
//		if (length < 0) 
//			length = 0;
//		if (beginLine < 0) 
//			beginLine = 0;
//		if (beginCol < 0) 
//			beginCol = 0;
//		if (endCol < 0) 
//			endCol = 0;
//		if (endLine < beginLine) 
//			endLine = beginLine;
//		if (endLine == beginLine && endCol < beginCol) 
//			endCol = beginCol;
//		return VF.sourceLocation(uri, offset, length, beginLine, endLine, beginCol, endCol);
//    }
	
	public static Thrown noMainFunction(Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(NoMainFunction), currentFrame);
	}
	
	public static Thrown incompletelyDefinedFunction(String message, Frame currentFrame){
	    return Thrown.getInstance(VF.constructor(IncompletelyDefinedFunction, VF.string(message)), currentFrame);
	}
	
	public static Thrown noParent(ISourceLocation noparentloc, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(NoParent, noparentloc), currentFrame);
	}
	
	public static Thrown noSuchAnnotation(String label, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(NoSuchAnnotation, VF.string(label)), currentFrame);
	}
	
	public static Thrown noSuchField(String name, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(NoSuchField, VF.string(name)), currentFrame);
	}

	public static Thrown noSuchKey(IValue v, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(NoSuchKey, v),  currentFrame);
	}
	
	public static Thrown notImplemented(String msg, ISourceLocation loc, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(NotImplemented, VF.string(msg)), loc, currentFrame);
	}
	
	public static Thrown parseError(ISourceLocation parseloc, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(ParseError, parseloc), currentFrame);
	}

	public static Thrown parseError(ISourceLocation parseloc, IString nt, IString s, Frame currentFrame) {
		return Thrown.getInstance(VF.constructor(Ambiguity, parseloc, nt, s), currentFrame);
	}
	
	public static Thrown uninitializedVariable(String name, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(UninitializedVariable, VF.string(name)), currentFrame);
	}

	public static Thrown RegExpSyntaxError(String message, Frame currentFrame) {
	  return Thrown.getInstance(VF.constructor(RegExpSyntaxError, VF.string(message)), currentFrame);
	}

	public static Thrown unavailableInformation(String message, Frame currentFrame){
	  return Thrown.getInstance(VF.constructor(UnavailableInformation, VF.string(message)),  currentFrame);   
	}

// Obsolete:
//	public static Thrown pathNotFound(ISourceLocation parseloc, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(PathNotFound, parseloc), loc, stacktrace);
//	}
	
//	public static Thrown stackOverflow(ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(StackOverflow), loc, stacktrace);
//	}
	
//	public static Thrown arityMismatch(int expected, int got, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(ArityMismatch, VF.integer(expected), VF.integer(got)), loc, stacktrace);
//	}
	
//	public static Thrown dateTimeParsingError(String message, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(DateTimeParsingError, VF.string(message)), loc, stacktrace);
//	}
	
//	public static Thrown dateTimePrintingError(String message, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(DateTimePrintingError, VF.string(message)), loc, stacktrace);
//	}	
	
//	public static Thrown figureException(String message, IValue v, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(Figure, VF.string(message), v), loc, stacktrace);
//	}
	
//	public static Thrown illegalCharacter(IInteger i, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(IllegalChar, i), loc, stacktrace);
//	}
	
//	public static Thrown illegalTypeArgument(String type, String message, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(IllegalTypeArgument,VF.string(type),VF.string(message)), loc, stacktrace);	
//	}
	
//	public static Thrown illegalTypeArgument(String type, ISourceLocation loc, List<Frame> stacktrace){
//		return Thrown.getInstance(VF.constructor(IllegalTypeArgument,VF.string(type)), loc, stacktrace);	
//	}
	
//	public static Thrown implodeError(String msg, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(ImplodeError, VF.string(msg)), loc, stacktrace);
//	}
	
//	public static Thrown MultipleKey(IValue v, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(MultipleKey, v), loc, stacktrace);
//	}
	
//	public static Thrown nameMismatch(String expected, String got, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(NameMismatch, VF.string(expected), VF.string(got)), loc, stacktrace);
//	}
	
//	public static Thrown noSuchElement(IValue v, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(NoSuchElement,v), loc, stacktrace);	
//	}

//	public static Thrown permissionDenied(ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(AnonymousPermissionDenied), loc, stacktrace);
//	}
	
//	public static Thrown permissionDenied(IString msg, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(PermissionDenied, msg), loc, stacktrace);
//	}
	
	

	
//	public static Thrown schemeNotSupported(ISourceLocation file, ISourceLocation loc, List<Frame> stacktrace) {
//		return Thrown.getInstance(VF.constructor(SchemeNotSupported, file), loc, stacktrace);
//	}
	
//	public static Thrown timeout(ISourceLocation loc, List<Frame> stacktrace) {
//    	return Thrown.getInstance(VF.constructor(Timeout), loc, stacktrace);
//    }
	
	
}
