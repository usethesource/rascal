package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public enum ToplevelType {
	VOID			(0),
	BOOL			(1),
	INT				(2),
	REAL			(3),
	RAT				(4),
	NUM				(5),
	STR				(6),
	LOC				(7),
	DATETIME		(8),
	LIST			(9),
	NODE			(10),
	CONSTRUCTOR		(11),
	LREL			(12),
	MAP				(13),
	SET				(14),
	REL				(15),
	TUPLE			(16),
	ADT				(17),
	VALUE			(18);
	
	private final int toplevelType;
	
	private static ToplevelType[] values = ToplevelType.values();

	public static ToplevelType fromInteger(int prim){
		return values[prim];
	}
	
	ToplevelType(int n){
		this.toplevelType = n;
	}
	
	public int getToplevelTypeAsInt(){
		return toplevelType;
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
				throw new RuntimeException("Alias cannot occur as toplevel type");
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
				throw new RuntimeException("Parameter cannot occur as toplevel type");
			}

			@Override
			public ToplevelType visitExternal(Type type)
					throws RuntimeException {
				throw new RuntimeException("External cannot occur as toplevel type");
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
