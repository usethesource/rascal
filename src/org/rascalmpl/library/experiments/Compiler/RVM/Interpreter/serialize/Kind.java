package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;

public enum Kind {

	/**
	 * The typess distinguished during (de)serialization.
	 */
	BOOL, DATETIME, INT, LOC, NUMBER, PARAMETER, 
	RATIONAL, REAL, STR, VALUE, VOID, 
	
	ADT, ALIAS, CONSTRUCTOR, FUNCTION, LIST, MAP, NODE, 
	NONTERMINAL, OVERLOADED, REIFIED, SET, TUPLE;
	
	
	public byte ordinal(Kind kind) {
		return (byte) kind.ordinal();
	}
	
	static boolean isCompound(Kind kind){
		switch(kind){
		case BOOL:
		case DATETIME:
		case INT:
		case LOC:
		case NUMBER:
		case PARAMETER:
		case RATIONAL:
		case REAL:
		case STR:
		case VALUE:
		case VOID:
			return false;
			
		case ADT:	
		case ALIAS:
		case CONSTRUCTOR:
		case FUNCTION:
		case LIST:
		case MAP:
		case NODE:
		case NONTERMINAL:
		case OVERLOADED:
		case REIFIED:
		case SET:
		case TUPLE:
			return true;
		
		default:
			throw new RuntimeException("Missing case");
		
		}
	}
	
	public  static Kind  getKind(IValue v) throws IOException {
		 return v.getType().accept(new ITypeVisitor<Kind,IOException>() {

			// Atomic types
			
			@Override
			public Kind visitBool(Type type) throws IOException {
				return BOOL;
			}
			
			@Override
			public Kind visitDateTime(Type type) throws IOException {
				return DATETIME;
			}
			
			@Override
			public Kind visitInteger(Type type) throws IOException {
				return INT;
			}
			
			@Override
			public Kind visitNode(Type type) throws IOException {
				return NODE;
			}
			
			@Override
			public Kind visitNumber(Type type) throws IOException {
				return NUMBER;
			}
			
			@Override
			public Kind visitRational(Type type) throws IOException {
				return RATIONAL;
			}
			
			
			@Override
			public Kind visitReal(Type type) throws IOException {
				return REAL;
			}
			
			@Override
			public Kind visitSourceLocation(Type type) throws IOException {
				return LOC;
			}
			
			@Override
			public Kind visitString(Type type) throws IOException {
				return STR;
			}
			
			@Override
			public Kind visitValue(Type type) throws IOException {
				return VALUE;
			}

			@Override
			public Kind visitVoid(Type type) throws IOException {
				return VOID;
			}
			
			// Composite types
			
			@Override
			public Kind visitAbstractData(Type type) throws IOException {
				return v instanceof IConstructor ? CONSTRUCTOR : ADT;
			}
			
			@Override
			public Kind visitAlias(Type type) throws IOException {
				return ALIAS;
			}
			
			@Override
			public Kind visitConstructor(Type type) throws IOException {
				return CONSTRUCTOR;
			}
			
			@Override
			public Kind visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					return FUNCTION;
				} else if(type instanceof ReifiedType){
					return REIFIED;
				} else if(type instanceof OverloadedFunctionType){
					return OVERLOADED;
				} else if(type instanceof NonTerminalType){
					return NONTERMINAL;
				} else {
					throw new RuntimeException("External type not supported: " + type);
				}
			}

			@Override
			public Kind visitList(Type type) throws IOException {
				return LIST;
			}

			@Override
			public Kind visitMap(Type type) throws IOException {
				return MAP;
			}
			
			@Override
			public Kind visitParameter(Type type) throws IOException {
				return PARAMETER;
			}

			@Override
			public Kind visitSet(Type type) throws IOException {
				return SET;
			}

			@Override
			public Kind visitTuple(Type type) throws IOException {
				return TUPLE;
			}
		});
	}
}
