package org.rascalmpl.library.cobra;

import java.util.HashMap;

import io.usethesource.vallang.type.DefaultTypeVisitor;
import io.usethesource.vallang.type.Type;

public class TypeParameterVisitor extends DefaultTypeVisitor<Void,RuntimeException> {

    private HashMap<Type, Type> typeParameters;
    private final RandomType randomType;

    public TypeParameterVisitor(){
        super(null);
        randomType = new RandomType();
    }

    public HashMap<Type, Type> bindTypeParameters(Type type){
        typeParameters = new HashMap<Type, Type>();
        type.accept(this);
        return typeParameters;
    }

    @Override
    public Void visitParameter(Type parameterType) {
        Type type = typeParameters.get(parameterType);
        if(type == null){
            Type bound = parameterType.getBound();
            while (bound.isOpen()){
                bound = typeParameters.get(bound.getName());
            }

            do {
                type = randomType.getType(5);
            } while (bound != null && !type.isSubtypeOf(bound));
            typeParameters.put(parameterType,  type);
        }
        return null;
    }

    @Override
    public Void visitTuple(Type type) {
        for(int i = 0; i < type.getArity(); i++){
            type.getFieldType(i).accept(this);
        }
        return null;
    }

    @Override
    public Void visitList(Type type) {
        type.getElementType().accept(this);
        return null;
    }

    @Override
    public Void visitMap(Type type) {
        type.getKeyType().accept(this);
        type.getValueType().accept(this);
        return null;
    }

    @Override
    public Void visitSet(Type type) {
        type.getElementType().accept(this);
        return null;
    }
}
