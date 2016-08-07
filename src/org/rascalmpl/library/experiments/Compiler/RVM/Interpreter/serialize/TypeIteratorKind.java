package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;

public enum TypeIteratorKind implements IteratorKind {

	/**
	 * The static types distinguished during (de)serialization.
	 * 
	 * TypeKInd    (isComposite == contains embedded type)
	 */
    
	BOOL           (false),
	
	DATETIME       (false),
	INT            (false),
	LOC            (false),
	NUMBER         (false),
	PARAMETER      (true),
	RATIONAL       (true),
	REAL           (false),
	STR            (false),
	VALUE          (false),
	VOID           (false), 
	
	ADT            (true),
	ALIAS          (true),
	CONSTRUCTOR    (true),
	FUNCTION       (true),
	LIST           (true),
	MAP            (true),
	NODE           (false), 
	NONTERMINAL    (false),
	OVERLOADED     (true), 
	REIFIED        (true), 
	SET            (true), 
	TUPLE          (true);
	
    private boolean compound;
    
    public boolean isCompound(){
        return compound;
    }

    TypeIteratorKind(boolean isCompound){
        this.compound = isCompound;
    }
	
	public byte ordinal(TypeIteratorKind kind) {
		return (byte) kind.ordinal();
	}
	
	public  static TypeIteratorKind  getKind(Type t) throws IOException {
        return t.accept(new ITypeVisitor<TypeIteratorKind,IOException>() {

           // Atomic types
           
           @Override
           public TypeIteratorKind visitBool(Type type) throws IOException {
               return BOOL;
           }
           
           @Override
           public TypeIteratorKind visitDateTime(Type type) throws IOException {
               return DATETIME;
           }
           
           @Override
           public TypeIteratorKind visitInteger(Type type) throws IOException {
               return INT;
           }
           
           @Override
           public TypeIteratorKind visitNode(Type type) throws IOException {
               return NODE;
           }
           
           @Override
           public TypeIteratorKind visitNumber(Type type) throws IOException {
               return NUMBER;
           }
           
           @Override
           public TypeIteratorKind visitRational(Type type) throws IOException {
               return RATIONAL;
           }
           
           
           @Override
           public TypeIteratorKind visitReal(Type type) throws IOException {
               return REAL;
           }
           
           @Override
           public TypeIteratorKind visitSourceLocation(Type type) throws IOException {
               return LOC;
           }
           
           @Override
           public TypeIteratorKind visitString(Type type) throws IOException {
               return STR;
           }
           
           @Override
           public TypeIteratorKind visitValue(Type type) throws IOException {
               return VALUE;
           }

           @Override
           public TypeIteratorKind visitVoid(Type type) throws IOException {
               return VOID;
           }
           
           // Composite types
           
           @Override
           public TypeIteratorKind visitAbstractData(Type type) throws IOException {
               return ADT;
           }
           
           @Override
           public TypeIteratorKind visitAlias(Type type) throws IOException {
               return ALIAS;
           }
           
           @Override
           public TypeIteratorKind visitConstructor(Type type) throws IOException {
               return CONSTRUCTOR;
           }
           
           @Override
           public TypeIteratorKind visitExternal(Type type) throws IOException {
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
           public TypeIteratorKind visitList(Type type) throws IOException {
               return LIST;
           }

           @Override
           public TypeIteratorKind visitMap(Type type) throws IOException {
               return MAP;
           }
           
           @Override
           public TypeIteratorKind visitParameter(Type type) throws IOException {
               return PARAMETER;
           }

           @Override
           public TypeIteratorKind visitSet(Type type) throws IOException {
               return SET;
           }

           @Override
           public TypeIteratorKind visitTuple(Type type) throws IOException {
               return TUPLE;
           }
       });
   }
}
