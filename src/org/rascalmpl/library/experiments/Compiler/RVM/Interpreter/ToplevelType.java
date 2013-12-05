package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

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
	VALUE			(17);
	
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
	
	/*
	 * TODO: This function is an obvious performance hog; This should be built-in to the PDB.
	 */
	
	public static ToplevelType getToplevelType(Type t){
		// Composite types
		if(t.isConstructor())
			return CONSTRUCTOR;
		if(t.isNode())
			return NODE;
		if(t.isListRelation())
			return t.getElementType().equivalent(TypeFactory.getInstance().voidType()) ? LIST : LREL;
		if(t.isList())
			return LIST;
		if(t.isMap())
			return MAP;
		if(t.isRelation())
			return t.getElementType().equivalent(TypeFactory.getInstance().voidType()) ? SET : REL;
		if(t.isSet())
			return SET;
		if(t.isTuple())
			return TUPLE;
		// Primitive types
		if(t.isBool())
			return BOOL;
		if(t.isInteger())
			return INT;
		if(t.isReal())
			return REAL;
		if(t.isRational())
			return RAT;
		if(t.isNumber())
			return NUM;
		if(t.isString())
			return STR;
		if(t.isSourceLocation())
			return LOC;
		if(t.isDateTime())
			return DATETIME;
		if(t.isTop())
			return VALUE;
		if(t.isBottom())
			return VOID;
		
		throw new RuntimeException("Unknown type: " + t);
	}
	
	public static int getToplevelTypeAsInt(Type t){
		return getToplevelType(t).getToplevelTypeAsInt();
	}
}
