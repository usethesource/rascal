package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;

public enum ToplevelType {
	VOID			(0, "void"),
	BOOL			(1, "bool"),
	INT				(2, "int"),
	REAL			(3, "real"),
	RAT				(4, "rat"),
	NUM				(5, "num"),
	STR				(6, "str"),
	LOC				(7, "loc"),
	DATETIME		(8, "datetime"),
	LIST			(9, "list"),
	NODE			(10, "node"),
	CONSTRUCTOR		(11, "constructor"),
	LREL			(12, "lrel"),
	MAP				(13, "map"),
	SET				(14, "set"),
	REL				(15, "rel"),
	TUPLE			(16, "tuple"),
	ADT				(17, "adt"),
	VALUE			(18, "value");
	
	private final int toplevelType;
	private final String representation;
	
	private static ToplevelType[] values = ToplevelType.values();

	public static ToplevelType fromInteger(int prim){
		return values[prim];
	}
	
	ToplevelType(int n, String repr){
		this.toplevelType = n;
		this.representation = repr;
	}
	
	public int getToplevelTypeAsInt(){
		return toplevelType;
	}
	public String getToplevelTypeAsString(){
		return representation;
	}
	public static String getToplevelTypeAsString(Type t){
		return getToplevelType(t).representation;
	}
	
	public static ToplevelType getToplevelType(Type t){
		return t.accept(new ITypeVisitor<ToplevelType,RuntimeException>() {

			@Override
			public ToplevelType visitReal(Type type) throws RuntimeException {
				return REAL;
			}

			@Override
			public ToplevelType visitInteger(Type type) throws RuntimeException {
				return INT;
			}

			@Override
			public ToplevelType visitRational(Type type)
					throws RuntimeException {
				return RAT;
			}

			@Override
			public ToplevelType visitList(Type type) throws RuntimeException {
				return LIST;
			}

			@Override
			public ToplevelType visitMap(Type type) throws RuntimeException {
				return MAP;
			}

			@Override
			public ToplevelType visitNumber(Type type) throws RuntimeException {
				return NUM;
			}

			@Override
			public ToplevelType visitAlias(Type type) throws RuntimeException {
				throw new CompilerError("Alias cannot occur as toplevel type");
			}

			@Override
			public ToplevelType visitSet(Type type) throws RuntimeException {
				return SET;
			}

			@Override
			public ToplevelType visitSourceLocation(Type type)
					throws RuntimeException {
				return LOC;
			}

			@Override
			public ToplevelType visitString(Type type) throws RuntimeException {
				return STR;
			}

			@Override
			public ToplevelType visitNode(Type type) throws RuntimeException {
				return NODE;
			}

			@Override
			public ToplevelType visitConstructor(Type type)
					throws RuntimeException {
				return CONSTRUCTOR;
			}

			@Override
			public ToplevelType visitAbstractData(Type type)
					throws RuntimeException {
				return ADT;
			}

			@Override
			public ToplevelType visitTuple(Type type) throws RuntimeException {
				return TUPLE;
			}

			@Override
			public ToplevelType visitValue(Type type) throws RuntimeException {
				return VALUE;
			}

			@Override
			public ToplevelType visitVoid(Type type) throws RuntimeException  {
				return VOID;
			}

			@Override
			public ToplevelType visitBool(Type type) throws RuntimeException {
				return BOOL;
			}

			@Override
			public ToplevelType visitParameter(Type type)
					throws RuntimeException {
				throw new CompilerError("Parameter cannot occur as toplevel type");
			}

			@Override
			public ToplevelType visitExternal(Type type)
					throws RuntimeException {
				throw new CompilerError("External cannot occur as toplevel type");
			}

			@Override
			public ToplevelType visitDateTime(Type type)
					throws RuntimeException {
				return DATETIME;
			}});
	}
	
	public static int getToplevelTypeAsInt(Type t){
		return getToplevelType(t).getToplevelTypeAsInt();
	}
}
