package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;

import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;

public enum ValueIteratorKind implements IteratorKind {

	/**
	 * The runtime value kinds distinguished during (de)serialization.
	 * 
	 * ValueKind   (isCompound == contains embedded value)
	 */
	BOOL           (false),
	DATETIME       (false), 
	INT            (false), 
	LOC            (false), 
	NUMBER         (false), 
	RATIONAL       (true), 
	REAL           (false), 
	STR            (false),  
	
	CONSTRUCTOR    (true), 
	LIST           (true), 
	MAP            (true), 
	NODE           (true), 
	SET            (true), 
	TUPLE          (true),
	
	RVM_FUNCTION   (true),
	RVM_OVERLOADED_FUNCTION 
	               (true),
	RVM_CODEBLOCK  (true),
	RVM_EXECUTABLE (true);
	
	private boolean compound;

    public byte ordinal(ValueIteratorKind kind) {
		return (byte) kind.ordinal();
	}
	
    ValueIteratorKind(boolean isCompound){
        this.compound = isCompound;
    }
	
    @Override
    public boolean isCompound() {
        return compound;
    }
    private static final ITypeVisitor<ValueIteratorKind,RuntimeException> typeMapper = new ITypeVisitor<ValueIteratorKind,RuntimeException>() {

			// Atomic types
			@Override
			public ValueIteratorKind visitBool(Type type) throws RuntimeException {
				return BOOL;
			}
			
			@Override
			public ValueIteratorKind visitDateTime(Type type) throws RuntimeException {
				return DATETIME;
			}
			
			@Override
			public ValueIteratorKind visitInteger(Type type) throws RuntimeException {
				return INT;
			}
			
			@Override
			public ValueIteratorKind visitNode(Type type) throws RuntimeException {
				return NODE;
			}
			
			@Override
			public ValueIteratorKind visitNumber(Type type) throws RuntimeException {
				return NUMBER;
			}
			
			@Override
			public ValueIteratorKind visitRational(Type type) throws RuntimeException {
				return RATIONAL;
			}
			
			
			@Override
			public ValueIteratorKind visitReal(Type type) throws RuntimeException {
				return REAL;
			}
			
			@Override
			public ValueIteratorKind visitSourceLocation(Type type) throws RuntimeException {
				return LOC;
			}
			
			@Override
			public ValueIteratorKind visitString(Type type) throws RuntimeException {
				return STR;
			}
			
			@Override
			public ValueIteratorKind visitValue(Type type) throws RuntimeException {
			    throw new RuntimeException("Value type not allowed as runtime type");
			}

			@Override
			public ValueIteratorKind visitVoid(Type type) throws RuntimeException {
			    throw new RuntimeException("Void type not allowed as runtime type");
			}
			
			// Composite types
			
			@Override
			public ValueIteratorKind visitAbstractData(Type type) throws RuntimeException {
			    return CONSTRUCTOR;
				//return v instanceof IConstructor ? CONSTRUCTOR : ADT;
			}
			
			@Override
			public ValueIteratorKind visitAlias(Type type) throws RuntimeException {
			    throw new RuntimeException("Alias type not allowed as runtime type");
			}
			
			@Override
			public ValueIteratorKind visitConstructor(Type type) throws RuntimeException {
				return CONSTRUCTOR;
			}
			
			@Override
			public ValueIteratorKind visitExternal(Type type) throws RuntimeException {
			    if(type instanceof NonTerminalType){
			        return CONSTRUCTOR;
			    }
				throw new RuntimeException("External type not allowed as runtime type: " + type);
			}

			@Override
			public ValueIteratorKind visitList(Type type) throws RuntimeException {
				return LIST;
			}

			@Override
			public ValueIteratorKind visitMap(Type type) throws RuntimeException {
				return MAP;
			}
			
			@Override
			public ValueIteratorKind visitParameter(Type type) throws RuntimeException {
			    throw new RuntimeException("Parameter type not allowed as runtime type");
			}

			@Override
			public ValueIteratorKind visitSet(Type type) throws RuntimeException {
				return SET;
			}

			@Override
			public ValueIteratorKind visitTuple(Type type) throws RuntimeException {
				return TUPLE;
			}
		};
	
	public  static ValueIteratorKind  getKind(IValue v) {
		 return v.getType().accept(typeMapper);
	}
}
