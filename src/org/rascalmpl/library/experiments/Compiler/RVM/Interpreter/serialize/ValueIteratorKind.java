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
	TUPLE          (true);
	
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
	
	public  static ValueIteratorKind  getKind(IValue v) throws IOException {
		 return v.getType().accept(new ITypeVisitor<ValueIteratorKind,IOException>() {

			// Atomic types
			
			@Override
			public ValueIteratorKind visitBool(Type type) throws IOException {
				return BOOL;
			}
			
			@Override
			public ValueIteratorKind visitDateTime(Type type) throws IOException {
				return DATETIME;
			}
			
			@Override
			public ValueIteratorKind visitInteger(Type type) throws IOException {
				return INT;
			}
			
			@Override
			public ValueIteratorKind visitNode(Type type) throws IOException {
				return NODE;
			}
			
			@Override
			public ValueIteratorKind visitNumber(Type type) throws IOException {
				return NUMBER;
			}
			
			@Override
			public ValueIteratorKind visitRational(Type type) throws IOException {
				return RATIONAL;
			}
			
			
			@Override
			public ValueIteratorKind visitReal(Type type) throws IOException {
				return REAL;
			}
			
			@Override
			public ValueIteratorKind visitSourceLocation(Type type) throws IOException {
				return LOC;
			}
			
			@Override
			public ValueIteratorKind visitString(Type type) throws IOException {
				return STR;
			}
			
			@Override
			public ValueIteratorKind visitValue(Type type) throws IOException {
			    throw new RuntimeException("Value type not allowed as runtime type");
			}

			@Override
			public ValueIteratorKind visitVoid(Type type) throws IOException {
			    throw new RuntimeException("Void type not allowed as runtime type");
			}
			
			// Composite types
			
			@Override
			public ValueIteratorKind visitAbstractData(Type type) throws IOException {
			    return CONSTRUCTOR;
				//return v instanceof IConstructor ? CONSTRUCTOR : ADT;
			}
			
			@Override
			public ValueIteratorKind visitAlias(Type type) throws IOException {
			    throw new RuntimeException("Alias type not allowed as runtime type");
			}
			
			@Override
			public ValueIteratorKind visitConstructor(Type type) throws IOException {
				return CONSTRUCTOR;
			}
			
			@Override
			public ValueIteratorKind visitExternal(Type type) throws IOException {
			    if(type instanceof NonTerminalType){
			        return CONSTRUCTOR;
			    }
				throw new RuntimeException("External type not allowed as runtime type: " + type);
			}

			@Override
			public ValueIteratorKind visitList(Type type) throws IOException {
				return LIST;
			}

			@Override
			public ValueIteratorKind visitMap(Type type) throws IOException {
				return MAP;
			}
			
			@Override
			public ValueIteratorKind visitParameter(Type type) throws IOException {
			    throw new RuntimeException("Parameter type not allowed as runtime type");
			}

			@Override
			public ValueIteratorKind visitSet(Type type) throws IOException {
				return SET;
			}

			@Override
			public ValueIteratorKind visitTuple(Type type) throws IOException {
				return TUPLE;
			}
		});
	}
}
