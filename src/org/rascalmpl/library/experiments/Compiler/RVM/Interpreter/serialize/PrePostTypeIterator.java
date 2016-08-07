package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;


import java.io.IOException;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.value.type.Type;

public class PrePostTypeIterator extends PrePostIterator<Type, TypeIteratorKind>  {

    public PrePostTypeIterator(Type root) throws IOException {
        this(root, 1024);
    }

    public PrePostTypeIterator(Type root, int stackSize) throws IOException {
        super(Type.class, TypeIteratorKind.class, stackSize);
        stack.push(root, TypeIteratorKind.getKind(root), true);
        beginning = false; // start out at fake end
    }

    public TypeIteratorKind next() throws IOException {
        if (beginning) {
            if (kind.isCompound()) {
                
                stack.push(value, kind, false);
                
                switch(kind){
                    case ADT: {
                        Type typeParameters = value.getTypeParameters();
                        
                        stack.push(typeParameters, TypeIteratorKind.getKind(typeParameters), true);
                        break;
                    }
                    
                    case ALIAS: {
                        Type aliased = value.getAliased();
                        Type typeParameters = value.getTypeParameters();
                        
                        stack.push(typeParameters, TypeIteratorKind.getKind(typeParameters), true);
                        stack.push(aliased, TypeIteratorKind.getKind(aliased), true);
                        break;
                    }
                    
                    case CONSTRUCTOR: {
                        Type adt = value.getAbstractDataType();
                        Type type = value.getFieldTypes();
                        
                        stack.push(type, TypeIteratorKind.getKind(type),  true);
                        stack.push(adt, TypeIteratorKind.getKind(adt),  true); 
                        break;
                    }
                    
                    case FUNCTION: {
                        FunctionType ft = (FunctionType) value;
                        Type returnType = ft.getReturnType();
                        Type argumentTypes = ft.getArgumentTypes();
                        Type kwparamTypes = ft.getKeywordParameterTypes();
                        
                        stack.push(kwparamTypes, TypeIteratorKind.getKind(kwparamTypes),  true); 
                        stack.push(argumentTypes, TypeIteratorKind.getKind(argumentTypes),  true); 
                        stack.push(returnType, TypeIteratorKind.getKind(returnType),  true); 
                        
                        break;
                    }
                   
                    case LIST:{
                        Type elemType = value.getElementType();
                        
                        stack.push(elemType,  TypeIteratorKind.getKind(elemType),  true); 
                        break;
                    }
             
                    case MAP: {
                        Type keyType = value.getKeyType();
                        Type valType = value.getValueType();
                        
                        stack.push(valType,  TypeIteratorKind.getKind(valType),  true); 
                        stack.push(keyType,  TypeIteratorKind.getKind(keyType),  true);
                        break;
                    }
                    
                    case NONTERMINAL: {
                        NonTerminalType nt = (NonTerminalType) value;
                        
                        break;
                    }
                   
                    case OVERLOADED:{
                        Set<FunctionType> alternatives = ((OverloadedFunctionType) value).getAlternatives();
                        for(FunctionType ft : alternatives){
                            stack.push(ft,  TypeIteratorKind.getKind(ft),  true);
                        }
                        break;
                    }
                        
                    case PARAMETER:{
                        Type bound = value.getBound();
                        stack.push(bound,  TypeIteratorKind.getKind(bound),  true);
                        break;
                    }
                   
                    case REIFIED: {
                        Type typeParameters = value.getTypeParameters();
                        stack.push(typeParameters,  TypeIteratorKind.getKind(typeParameters),  true);
                        break;
                    }
                        
                    case SET: {
                        Type elemType = value.getElementType();
                        
                        stack.push(elemType,  TypeIteratorKind.getKind(elemType),  true); 
                        break;
                    }
                    
                    case TUPLE: {
                        for(int i = value.getArity() - 1; i >= 0; i--){
                            Type elemType = value.getFieldType(i);
                            stack.push(elemType,  TypeIteratorKind.getKind(elemType),  true); 
                        }
                        break;
                    }
                    
                    default:
                        throw new RuntimeException("Cannot happen");
                
                }
            }
        }
        value = stack.currentIValue();
        kind = stack.currentKind();
        beginning = stack.currentBeginning();
        stack.pop();
        return kind;
    }

}

