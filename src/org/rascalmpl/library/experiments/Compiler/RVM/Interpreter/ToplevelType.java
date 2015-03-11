package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.values.uptr.Factory;

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
	
	private static final Integer listHashCode = "list".hashCode();
	private static final Integer mapHashCode = "map".hashCode();
	private static final Integer setHashCode = "set".hashCode();
	private static final Integer tupleHashCode = "tuple".hashCode();
	private static final Integer valueHashCode = "value".hashCode();
	
	public static int getFingerprint(final IValue v, final boolean useConcreteFingerprint){
		return v.getType().accept(new ITypeVisitor<Integer,RuntimeException>() {

			@Override
			public Integer visitReal(final Type type) throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitInteger(final Type type) throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitRational(final Type type) throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitList(final Type type) throws RuntimeException {
				return listHashCode;
			}

			@Override
			public Integer visitMap(final Type type) throws RuntimeException {
				return mapHashCode;
			}

			@Override
			public Integer visitNumber(final Type type) throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitAlias(final Type type) throws RuntimeException {
				throw new CompilerError("Alias cannot occur in fingerprint");
			}

			@Override
			public Integer visitSet(final Type type) throws RuntimeException {
				return setHashCode;
			}

			@Override
			public Integer visitSourceLocation(final Type type)
					throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitString(final Type type) throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitNode(final Type type) throws RuntimeException {
				return ((INode) v).getName().hashCode() << 2 + ((INode) v).arity();
			}

			@Override
			public Integer visitConstructor(final Type type) throws RuntimeException {
				IConstructor cons = (IConstructor) v;
				if(useConcreteFingerprint && cons.getName().equals("appl")){	// use name to be insensitive to annotations
					return cons.get(0).hashCode(); 
				}
				return cons.getName().hashCode() << 2 + cons.arity();
			}

			@Override
			public Integer visitAbstractData(final Type type) throws RuntimeException {
				IConstructor cons = (IConstructor) v;
				if(useConcreteFingerprint  && cons.getName().equals("appl")){	// use name to be insensitive to annotations
					return cons.get(0).hashCode(); 
				}
				return cons.getName().hashCode() << 2 + cons.arity();
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
			public Integer visitVoid(Type type) throws RuntimeException {
				throw new CompilerError("Void cannot occur in fingerprint");
			}

			@Override
			public Integer visitBool(final Type type) throws RuntimeException {
				return v.hashCode();
			}

			@Override
			public Integer visitParameter(final Type type) throws RuntimeException {
				throw new CompilerError("Parameter cannot occur in fingerprint");
			}

			@Override
			public Integer visitExternal(final Type type) throws RuntimeException {
				throw new CompilerError("External cannot occur in fingerprint");
			}

			@Override
			public Integer visitDateTime(final Type type) throws RuntimeException {
				return v.hashCode();
			}
		});
	}
}
