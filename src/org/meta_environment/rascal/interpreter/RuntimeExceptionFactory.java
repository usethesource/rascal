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
import org.meta_environment.rascal.ast.AbstractAST;
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
	private static Type NoSuchElement = TF.constructor(TS,E,"NoSuchElement",TF.valueType(), "v");
	private static Type IllegalArgument = TF.constructor(TS,E,"IllegalArgument",TF.valueType(), "v");
	private static Type AnonymousIllegalArgument = TF.constructor(TS,E,"IllegalArgument");
	private static Type IO = TF.constructor(TS,E,"IO",TF.stringType(), "message");
	private static Type FileNotFound = TF.constructor(TS,E,"FileNotFound",TF.stringType(), "filename");
	private static Type LocationNotFound = TF.constructor(TS,E,"LocationNotFound",TF.sourceLocationType(), "location");
	private static Type PermissionDenied = TF.constructor(TS,E,"PermissionDenied",TF.stringType(), "message");
	private static Type AnonymousPermissionDenied = TF.constructor(TS,E,"PermissionDenied");
	private static Type ModuleNotFound = TF.constructor(TS, E, "ModuleNotFound", TF.stringType(), "name");
	private static Type NoSuchKey = TF.constructor(TS, E, "NoSuchKey", TF.valueType(), "key");
	private static Type NoSuchAnnotation = TF.constructor(TS, E, "NoSuchAnnotation", TF.stringType(), "label");
	private static Type NoSuchField = TF.constructor(TS, E, "NoSuchField", TF.stringType(), "label");
	
	
	private static Type Java = TF.constructor(TS, E, "Java", TF.stringType(), "message");

    public static Throw indexOutOfBounds(IInteger i, AbstractAST ast) {
    	return new Throw(IndexOutOfBounds.make(VF, i), ast);
    }
    
    public static Throw assertionFailed(IString msg, AbstractAST ast) {
    	return new Throw(LabeledAssertionFailed.make(VF, msg), ast);
    }
    
    public static Throw assertionFailed(AbstractAST ast) {
    	return new Throw(AssertionFailed.make(VF), ast);
    }

	public static Throw emptyList(AbstractAST ast) {
		return new Throw(EmptyList.make(VF), ast);
	}
	
	public static Throw emptySet(AbstractAST ast) {
		return new Throw(EmptySet.make(VF), ast);
	}
	
	public static Throw emptyMap(AbstractAST ast) {
		return new Throw(EmptyMap.make(VF), ast);
	}

	public static Throw noSuchElement(IValue v, AbstractAST ast) {
		return new Throw(NoSuchElement.make(VF,v), ast);	
	}
	
	public static Throw illegalArgument(IValue v, AbstractAST ast) {
		return new Throw(IllegalArgument.make(VF,v), ast);	
	}
	
	public static Throw illegalArgument(AbstractAST ast) {
		return new Throw(AnonymousIllegalArgument.make(VF), ast);	
	}
	
	public static Throw fileNotFound(IString name, AbstractAST ast) {
		return new Throw(FileNotFound.make(VF, name), ast);
	}
	
	public static Throw locationNotFound(ISourceLocation loc, AbstractAST ast) {
		return new Throw(LocationNotFound.make(VF, loc), ast);
	}
	
	public static Throw permissionDenied(AbstractAST ast) {
		return new Throw(AnonymousPermissionDenied.make(VF), ast);
	}
	
	public static Throw permissionDenied(IString msg, AbstractAST ast) {
		return new Throw(PermissionDenied.make(VF, msg), ast);
	}
	
	public static Throw io(IString msg, AbstractAST ast) {
		return new Throw(IO.make(VF, msg), ast);
	}
	
	public static Throw moduleNotFound(IString module, AbstractAST ast) {
		return new Throw(ModuleNotFound.make(VF, module), ast);
	}

	public static Throw noSuchKey(IValue v, AbstractAST ast) {
		return new Throw(NoSuchKey.make(VF, v), ast);
	}
	
	public static Throw noSuchAnnotation(String label, AbstractAST ast) {
		return new Throw(NoSuchAnnotation.make(VF, VF.string(label)), ast);
	}

	public static Throw javaException(String message, AbstractAST ast) {
		return new Throw(Java.make(VF, VF.string(message)), ast);
	}

	public static Throw noSuchField(String name, AbstractAST ast) {
		return new Throw(NoSuchField.make(VF, VF.string(name)), ast);
	}
}
