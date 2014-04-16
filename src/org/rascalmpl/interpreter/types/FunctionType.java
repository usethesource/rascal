/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IKeywordParameterInitializer;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

/**
 * Function types are an extension of the pdb's type system, especially tailored to Rascal's functions 
 */
public class FunctionType extends RascalType {
	private final Type returnType;
	private final Type argumentTypes;
	private final Type keywordParameters;
	private final Map<String, IKeywordParameterInitializer> defaultParameters;
	
	private static final TypeFactory TF = TypeFactory.getInstance();
	private static final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	
	/*package*/ FunctionType(Type returnType, Type argumentTypes, Type keywordParameters, Map<String, IKeywordParameterInitializer> defaultParameters) {
		this.argumentTypes = argumentTypes.isTuple() ? argumentTypes : TF.tupleType(argumentTypes);
		this.returnType = returnType;
		this.keywordParameters = keywordParameters;
		this.defaultParameters = defaultParameters;
	}
	
	@Override
	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
	  return visitor.visitFunction(this);
	}
	
	public Type getReturnType() {
		return returnType;
	}

	public Type getArgumentTypes() {
		return argumentTypes;
	}
	
	@Override
	public int getArity() {
		return argumentTypes.getArity();
	}
	
	@Override
	public IKeywordParameterInitializer getKeywordParameterInitializer(
			String label) {
		return defaultParameters.get(label);
	}
	
	@Override
	public Map<String, IKeywordParameterInitializer> getKeywordParameterInitializers() {
		return defaultParameters;
	}
	
	public Type getKeywordParameterTypes() {
		return keywordParameters;
	}
	
	@Override
	public Type getKeywordParameterType(String label) {
	  return keywordParameters.getFieldType(label);
	}
	
	@Override
	public boolean hasKeywordParameter(String label) {
	  return keywordParameters.hasField(label);
	}
	
	@Override
	public boolean hasKeywordParameters() {
	  return keywordParameters != null && !keywordParameters.isBottom();
	}
	
	@Override
	protected boolean isSupertypeOf(RascalType type) {
	  return type.isSubtypeOfFunction(this);
	}
	
	@Override
	protected Type lub(RascalType type) {
	  return type.lubWithFunction(this);
	}
	
	@Override
	protected Type glb(RascalType type) {
		return type.glbWithFunction(this);
	}
	
	@Override
	public boolean isSubtypeOfFunction(RascalType other) {
		// Rascal functions are co-variant in the return type position and
		// contra-variant in the argument positions, such that a sub-function
		// can safely simulate a super function.
	  FunctionType otherType = (FunctionType) other;

	  if (getReturnType().isSubtypeOf(otherType.getReturnType())) {
	    if (otherType.getArgumentTypes().isSubtypeOf(getArgumentTypes())) {
	      return true;
	    }

	    // type parameterized functions are never sub-types before instantiation
	    // because the argument types are co-variant. This would be weird since
	    // instantiated functions are supposed to be substitutable for their generic
	    // counter parts. So, we try to instantiate first, and then check again.
	    Map<Type,Type> bindings = new HashMap<Type,Type>();

	    if (!otherType.match(this, bindings)) {
	      return false;
	    }
	    if (bindings.size() != 0) {
	      return isSubtypeOf(otherType.instantiate(bindings));
	    }
	  }
	  
	  return false;
	}

	@Override
	protected Type lubWithFunction(RascalType type) {
	  if(this == type) {
	    return this;
	  } 
	  
	  FunctionType f = (FunctionType) type;
	  
	  Type returnType = getReturnType().lub(f.getReturnType());
	  Type argumentTypes = getArgumentTypes().glb(f.getArgumentTypes());
	  
	  if (argumentTypes.isTuple()) {
	    // TODO: figure out what lub means for keyword parameters!
	    return RTF.functionType(returnType, argumentTypes, TF.voidType(), Collections.<String,IKeywordParameterInitializer>emptyMap());
	  }
	  
	  return TF.valueType();
	}
	
	@Override
	protected Type glbWithFunction(RascalType type) {
	  if(this == type) {
		return this;
	  }
		
	  FunctionType f = (FunctionType) type;
		
	  Type returnType = getReturnType().glb(f.getReturnType());
	  Type argumentTypes = getArgumentTypes().lub(f.getArgumentTypes());
		
	  if(argumentTypes.isTuple()) {
	    // TODO: figure out what glb means for keyword parameters
	    return RTF.functionType(returnType, argumentTypes, TF.voidType(), Collections.<String,IKeywordParameterInitializer>emptyMap());
	  }
		
	  return TF.voidType();
	}
		
	@Override
	protected boolean isSubtypeOfOverloadedFunction(RascalType type) {
	  OverloadedFunctionType function = (OverloadedFunctionType) type;
	  for (FunctionType f : function.getAlternatives()) {
	    if (!this.isSubtypeOf(f)) {
		  return false;
	    }
	  }
	  
	  return true;
	}

	@Override
	protected Type lubWithOverloadedFunction(RascalType type) {
	  return type.lubWithFunction(this);
	}
	
	@Override 
	protected Type glbWithOverloadedFunction(RascalType type) {
		return type.glbWithFunction(this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(returnType);
		sb.append(' ');
		sb.append('(');
		int i = 0;
		for (Type arg : argumentTypes) {
			if (i++ > 0) {
				sb.append(", ");
			}
			sb.append(arg.toString());
		}
		sb.append(')');
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return 19 + 19 * returnType.hashCode() + 23 * argumentTypes.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionType) {
			FunctionType other = (FunctionType) o;
			return returnType == other.returnType 
			    && argumentTypes == other.argumentTypes
			    && keywordParameters.equals(other.keywordParameters)
			    && defaultParameters.values().equals(other.defaultParameters.values());
		}
		return false;
	}
	
	@Override
	public Type instantiate(Map<Type, Type> bindings) {
		return RTF.functionType(returnType.instantiate(bindings), argumentTypes.instantiate(bindings), keywordParameters, defaultParameters);
	}
	
	@Override
	public boolean match(Type matched, Map<Type, Type> bindings)
			throws FactTypeUseException {
//		super.match(matched, bindings); match calls isSubTypeOf which calls match, watch out for infinite recursion
		if (matched.isBottom()) {
			return returnType.match(matched, bindings);
		} else {
			// Fix for cases where we have aliases to function types, aliases to aliases to function types, etc
			while (matched.isAliased()) {
				matched = matched.getAliased();
			}
	
			if (matched instanceof OverloadedFunctionType) {
				OverloadedFunctionType of = (OverloadedFunctionType) matched;
				// at least one needs to match (also at most one can match)
				
				for (Type f : of.getAlternatives()) {
					if (this.match(f, bindings)) {
						return true;
					}
				}
				
				return false;
			}
			else if (matched instanceof FunctionType) {
				return argumentTypes.match(((FunctionType) matched).getArgumentTypes(), bindings)
						&& returnType.match(((FunctionType) matched).getReturnType(), bindings);
			}
			else {
				return false;
			}
		}
	}
	
	@Override
	public Type compose(Type right) {
		if (right.isBottom()) {
			return right;
		}
		Set<FunctionType> newAlternatives = new HashSet<FunctionType>();
		
		if(right instanceof FunctionType) {
			if(TF.tupleType(((FunctionType) right).returnType).isSubtypeOf(this.argumentTypes)) {
				return RTF.functionType(this.returnType, ((FunctionType) right).getArgumentTypes(), ((FunctionType) right).keywordParameters, ((FunctionType) right).defaultParameters);
			}
		} else if(right instanceof OverloadedFunctionType) {
			for(FunctionType ftype : ((OverloadedFunctionType) right).getAlternatives()) {
				if(TF.tupleType(ftype.getReturnType()).isSubtypeOf(this.argumentTypes)) {
					newAlternatives.add((FunctionType) RTF.functionType(this.returnType, ftype.getArgumentTypes(), ftype.keywordParameters, ftype.defaultParameters));
				}
			}
		} else {
			throw new IllegalOperationException("compose", this, right);
		}
		if(!newAlternatives.isEmpty()) 
			return RTF.overloadedFunctionType(newAlternatives);
		return TF.voidType();
	}
}
