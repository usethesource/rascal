package org.rascalmpl.library.util;

import org.rascalmpl.types.DefaultRascalTypeVisitor;
import org.rascalmpl.types.RascalType;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

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
	
	private static final ToplevelType[] values = ToplevelType.values();

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
	public static String getToplevelTypeAsString(final Type t){
		return getToplevelType(t).representation;
	}
	
	public static ToplevelType getToplevelType(final Type t){
		return t.accept(new DefaultRascalTypeVisitor<ToplevelType,RuntimeException>(VOID) {

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
				throw new RuntimeException("External cannot occur as toplevel type: " + type);
			}
			
			@Override
			public ToplevelType visitNonTerminal(RascalType type)
					throws RuntimeException {
				// TODO: @paulk review
				return CONSTRUCTOR;
			}
			
			@Override
			public ToplevelType visitReified(RascalType type)
					throws RuntimeException {
				// TODO: @paulk review
				return CONSTRUCTOR;
			}
			
			@Override
			public ToplevelType visitFunction(Type type)
					throws RuntimeException {
				// TODO: @paulk review
				return VALUE;
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
	
	private static final Integer listHashCode = "list".hashCode();
	private static final Integer mapHashCode = "map".hashCode();
	private static final Integer setHashCode = "set".hashCode();
	private static final Integer tupleHashCode = "tuple".hashCode();
	private static final Integer valueHashCode = "value".hashCode();
	
	public static int getFingerprintNode(INode nd){
		//System.err.println("getFingerprintNode: " + nd.hashCode() + " for " + nd);
		return nd.hashCode();
	}
	
	public static int getFingerprint(final IValue v, final boolean useConcreteFingerprint){
		int res = v.getType().accept(new DefaultRascalTypeVisitor<Integer,RuntimeException>(v.hashCode()) {
			@Override
			public Integer visitList(final Type type) throws RuntimeException {
				return listHashCode;
			}

			@Override
			public Integer visitMap(final Type type) throws RuntimeException {
				return mapHashCode;
			}

			@Override
			public Integer visitSet(final Type type) throws RuntimeException {
				return setHashCode;
			}

			@Override
			public Integer visitNode(final Type type) throws RuntimeException {
				return ((INode) v).getName().hashCode() << 2 + ((INode) v).arity();
			}

			@Override
			public Integer visitConstructor(final Type type) throws RuntimeException {
				IConstructor cons = (IConstructor) v;
				return cons.getName().hashCode() << 2 + cons.arity();
			}

			@Override
			public Integer visitAbstractData(final Type type) throws RuntimeException {
				return visitConstructor(type);
			}

			@Override
			public Integer visitTuple(final Type type) throws RuntimeException {
				return tupleHashCode << 2 + ((ITuple) v).arity();
			}

			@Override
			public Integer visitValue(final Type type) throws RuntimeException {
				return valueHashCode;
			}

			@Override
			public Integer visitReified(RascalType type)
					throws RuntimeException {
				// TODO: this might work; need to check
				return visitConstructor(type);
			}

			@Override
			public Integer visitNonTerminal(RascalType type) throws RuntimeException {
				assert v instanceof ITree;
				if (useConcreteFingerprint && ((ITree) v).isAppl()) {	
					return ((ITree) v).getProduction().hashCode(); 
				}
				
				return visitAbstractData(type.asAbstractDataType());
			}
		});
		return res;
	}
}
