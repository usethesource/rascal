package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;

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
	private static Type NoSuchElement = TF.constructor(TS,E,"NoSuchElement",TF.valueType(), "value");
	private static Type IllegalArgument = TF.constructor(TS,E,"IllegalArgument",TF.valueType(), "value");
	private static Type AnonymousIllegalArgument = TF.constructor(TS,E,"IllegalArgument");
	private static Type IO = TF.constructor(TS,E,"IO",TF.stringType(), "message");
	private static Type FileNotFound = TF.constructor(TS,E,"FileNotFound",TF.stringType(), "filename");
	private static Type LocationNotFound = TF.constructor(TS,E,"LocationNotFound",TF.sourceLocationType(), "location");
	private static Type PermissionDenied = TF.constructor(TS,E,"PermissionDenied",TF.stringType(), "message");
	private static Type AnonymousPermissionDenied = TF.constructor(TS,E,"PermissionDenied");
	private static Type ModuleNotFound = TF.constructor(TS, E, "ModuleNotFound", TF.stringType(), "module");
	private static Type NoSuchKey = TF.constructor(TS, E, "NoSuchKey", TF.valueType(), "key");
	private static Type NoSuchAnnotation = TF.constructor(TS, E, "NoSuchAnnotation", TF.stringType(), "label");
	
	private static Type Java = TF.constructor(TS, E, "Java", TF.stringType(), "message");

    public static Throw indexOutOfBounds(IInteger i) {
    	return new Throw(IndexOutOfBounds.make(VF, i));
    }
    
    public static Throw assertionFailed(IString msg) {
    	return new Throw(LabeledAssertionFailed.make(VF, msg));
    }
    
    public static Throw assertionFailed() {
    	return new Throw(AssertionFailed.make(VF));
    }

	public static Throw emptyList() {
		return new Throw(EmptyList.make(VF));
	}
	
	public static Throw emptySet() {
		return new Throw(EmptySet.make(VF));
	}
	
	public static Throw emptyMap() {
		return new Throw(EmptyMap.make(VF));
	}

	public static Throw noSuchElement(IValue v) {
		return new Throw(NoSuchElement.make(VF,v));	
	}
	
	public static Throw illegalArgument(IValue v) {
		return new Throw(IllegalArgument.make(VF,v));	
	}
	
	public static Throw illegalArgument() {
		return new Throw(AnonymousIllegalArgument.make(VF));	
	}
	
	public static Throw fileNotFound(IString name) {
		return new Throw(FileNotFound.make(VF, name));
	}
	
	public static Throw locationNotFound(ISourceLocation loc) {
		return new Throw(LocationNotFound.make(VF, loc));
	}
	
	public static Throw permissionDenied() {
		return new Throw(AnonymousPermissionDenied.make(VF));
	}
	
	public static Throw permissionDenied(IString msg) {
		return new Throw(PermissionDenied.make(VF, msg));
	}
	
	public static Throw io(IString msg) {
		return new Throw(IO.make(VF, msg));
	}
	
	public static Throw moduleNotFound(IString module) {
		return new Throw(ModuleNotFound.make(VF, module));
	}

	public static Throw noSuchKey(IValue v) {
		return new Throw(NoSuchKey.make(VF, v));
	}
	
	public static Throw noSuchAnnotation(String label) {
		return new Throw(NoSuchAnnotation.make(VF, VF.string(label)));
	}

	public static Throw javaException(String message) {
		return new Throw(Java.make(VF, VF.string(message)));
	}
}
