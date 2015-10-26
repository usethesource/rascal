package org.rascalmpl.library.cobra;

import java.util.HashMap;

import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;

public class TypeParameterVisitor implements ITypeVisitor<IValue,RuntimeException> {
	
	private HashMap<Type, Type> typeParameters;
	private final RandomType randomType;
	
	public TypeParameterVisitor(){
		randomType = new RandomType();
	}
	
	public HashMap<Type, Type> bindTypeParameters(Type type){
		typeParameters = new HashMap<Type, Type>();
		type.accept(this);
		return typeParameters;
	}

	@Override
	public IValue visitParameter(Type parameterType) {
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
	public IValue visitTuple(Type type) {
		for(int i = 0; i < type.getArity(); i++){
			type.getFieldType(i).accept(this);
		}
		return null;
	}

	@Override
	public IValue visitReal(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitInteger(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitRational(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitList(Type type) {
		type.getElementType().accept(this);
		return null;
	}

	@Override
	public IValue visitMap(Type type) {
		type.getKeyType().accept(this);
		type.getValueType().accept(this);
		return null;
	}

	@Override
	public IValue visitNumber(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitAlias(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitSet(Type type) {
		type.getElementType().accept(this);
		return null;
	}

	@Override
	public IValue visitSourceLocation(Type type) {
		return null;
	}

	@Override
	public IValue visitString(Type type) {
		return null;
	}

	@Override
	public IValue visitNode(Type type) {
		return null;
	}

	@Override
	public IValue visitConstructor(Type type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitAbstractData(Type type) {
		return null;
	}

	@Override
	public IValue visitValue(Type type) {
		return null;
	}

	@Override
	public IValue visitVoid(Type type) {
		return null;
	}

	@Override
	public IValue visitBool(Type boolType) {
		return null;
	}

	@Override
	public IValue visitExternal(Type externalType) {
		return null;
	}

	@Override
	public IValue visitDateTime(Type type) {
		return null;
	}

}
