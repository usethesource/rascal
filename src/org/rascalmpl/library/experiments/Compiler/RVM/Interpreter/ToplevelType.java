package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.type.Type;

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
	VALUE			(17);
	
	private final int toplevelType;
	
	private static ToplevelType[] values = ToplevelType.values();

	public static ToplevelType fromInteger(int prim){
		return values[prim];
	}
	
	ToplevelType(int n){
		this.toplevelType = n;
	}
	
	public int getToplevelType(){
		return toplevelType;
	}
	
	public static int getToplevelType(Type t){
		// Composite types
		if(t.isConstructor())
			return CONSTRUCTOR.getToplevelType();
		if(t.isNode())
			return NODE.getToplevelType();
		if(t.isListRelation())
			return LREL.getToplevelType();
		if(t.isList())
			return LIST.getToplevelType();
		if(t.isMap())
			return MAP.getToplevelType();
		if(t.isRelation())
			return REL.getToplevelType();
		if(t.isSet())
			return SET.getToplevelType();
		if(t.isTuple())
			return TUPLE.getToplevelType();
		// Primitive types
		if(t.isBool())
			return BOOL.getToplevelType();
		if(t.isInteger())
			return INT.getToplevelType();
		if(t.isReal())
			return REAL.getToplevelType();
		if(t.isRational())
			return RAT.getToplevelType();
		if(t.isNumber())
			return NUM.getToplevelType();
		if(t.isString())
			return STR.getToplevelType();
		if(t.isSourceLocation())
			return LOC.getToplevelType();
		if(t.isDateTime())
			return DATETIME.getToplevelType();
		if(t.isTop())
			return VALUE.getToplevelType();
		if(t.isBottom())
			return VOID.getToplevelType();
		
		throw new RuntimeException("Unknown type: " + t);
	}
}
