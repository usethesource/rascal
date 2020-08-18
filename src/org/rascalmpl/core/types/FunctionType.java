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
package org.rascalmpl.core.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.rascalmpl.core.values.uptr.RascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.IllegalOperationException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;
import io.usethesource.vallang.type.TypeFactory.TypeReifier;
import io.usethesource.vallang.type.TypeStore;

/**
 * Function types are an extension of the pdb's type system, especially tailored to Rascal's functions 
 */
public class FunctionType extends RascalType {
	private final Type returnType;
	private final Type argumentTypes;
	private final Type keywordParameters;
	
	private static final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	
	/*package*/ FunctionType(Type returnType, Type argumentTypes, Type keywordParameters) {
		this.argumentTypes = argumentTypes.isBottom() ? TF.tupleEmpty() : argumentTypes;
		this.returnType = returnType;
		this.keywordParameters = keywordParameters == null ? null : keywordParameters.isBottom() || (keywordParameters.isTuple() && keywordParameters.getArity() == 0) ? null : keywordParameters;
	}
	
	public static class Reifier implements TypeReifier {
	    @Override
	    public Type getSymbolConstructorType() {
	        throw new UnsupportedOperationException();
	    }
	    
        @Override
        public Set<Type> getSymbolConstructorTypes() {
            return Arrays.stream(new Type[] { 
                    normalFunctionSymbol(),
                    oldNormalFunctionSymbol(),
                    // TODO: remove this deprecated representation. A prod type is the same as a function type
                    prodFunctionSymbol()
            }).collect(Collectors.toSet());
        }

        private Type prodFunctionSymbol() {
            return symbols().typeSymbolConstructor("prod", symbols().symbolADT(),  "sort", TF.stringType(), "name", TF.listType(symbols().symbolADT()), "parameters", TF.setType(symbols().attrADT()), "attributes");
        }

        private Type normalFunctionSymbol() {
            return symbols().typeSymbolConstructor("func", symbols().symbolADT(), "ret", TF.listType(symbols().symbolADT()), "parameters", TF.listType(symbols().symbolADT()), "kwTypes");
        }
        
        private Type oldNormalFunctionSymbol() {
            return symbols().typeSymbolConstructor("func", symbols().symbolADT(), "ret", TF.listType(symbols().symbolADT()), "parameters");
        }

        @Override
        public Type fromSymbol(IConstructor symbol, TypeStore store, Function<IConstructor, Set<IConstructor>> grammar) {
            if (symbol.getConstructorType() == prodFunctionSymbol()) {
                // TODO remove support for deprecated representation after bootstrap
                Type returnType = symbols().fromSymbol((IConstructor) symbol.get("sort"), store, grammar);
                Type parameters = symbols().fromSymbols((IList) symbol.get("parameters"), store, grammar);
                
                return RTF.functionType(returnType, parameters, TF.tupleEmpty());
            } else {
                Type returnType = symbols().fromSymbol((IConstructor) symbol.get("ret"), store, grammar);
                Type parameters = symbols().fromSymbols((IList) symbol.get("parameters"), store, grammar);
                
                if (symbol.getConstructorType()  == oldNormalFunctionSymbol()) {
                    return RTF.functionType(returnType, parameters, TF.tupleEmpty());
                }
                
                Type kwTypes = symbols().fromSymbols((IList) symbol.get("kwTypes"), store, grammar);

                // TODO: while merging the other branch had tf.voidType()...    
                return RTF.functionType(returnType, parameters, kwTypes);
            }
        }
        
        @Override
        public boolean isRecursive() {
            return true;
        }

        @Override
        public Type randomInstance(Supplier<Type> next, TypeStore store, RandomTypesConfig rnd) {
            return RascalTypeFactory.getInstance().functionType(next.get(), randomTuple(next, store, rnd), rnd.nextBoolean() ? tf().voidType() : randomTuple(next, store, rnd));
        }
        
        @Override
        public void asProductions(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar,
                Set<IConstructor> done) {
            ((FunctionType) type).getReturnType().asProductions(vf, store, grammar, done);

            for (Type arg : ((FunctionType) type).getArgumentTypes()) {
                arg.asProductions(vf, store, grammar, done);
            }
        }
        
        @Override
        public IConstructor toSymbol(Type type, IValueFactory vf, TypeStore store,  ISetWriter grammar, Set<IConstructor> done) {
            IListWriter w = vf.listWriter();
            
            int i = 0;
            Type args = ((FunctionType) type).getArgumentTypes();
            for (Type arg : args) {
                IConstructor sym = arg.asSymbol(vf, store, grammar, done);
                if (args.hasFieldNames()) {
                    sym = symbols().labelSymbol(vf, sym, args.getFieldName(i));
                }
                i++;
                w.append(sym);
            }
            
            IListWriter kw = vf.listWriter();
            
            i = 0;
            Type kwArgs = ((FunctionType) type).getKeywordParameterTypes();
            if (kwArgs != null && !kwArgs.isBottom()) {
                for (Type arg : kwArgs) {
                    IConstructor sym = arg.asSymbol(vf, store, grammar, done);
                    if (kwArgs.hasFieldNames()) {
                        sym = symbols().labelSymbol(vf, sym, kwArgs.getFieldName(i));
                    }
                    i++;
                    kw.append(sym);
                }
            }
            
            
            return vf.constructor(normalFunctionSymbol(), ((FunctionType) type).getReturnType().asSymbol(vf, store, grammar, done), w.done(), kw.done());
        }
	}
	
	@Override
	public TypeReifier getTypeReifier() {
	    return new Reifier();
	}
	
	@Override
	public boolean isFunction() {
		return true;
	}
	
	@Override
	public Type asAbstractDataType() {
		return RascalValueFactory.Production;
	}
	
	
	
	@Override
	public Type getFieldType(int i) {
		return argumentTypes.getFieldType(i);
	}
	
	@Override
	public Type getFieldType(String fieldName) throws FactTypeUseException {
		return argumentTypes.getFieldType(fieldName);
	}
	
	@Override
	public int getFieldIndex(String fieldName) {
		return argumentTypes.getFieldIndex(fieldName);
	}
	
	@Override
	public String getFieldName(int i) {
		return argumentTypes.getFieldName(i);
	}
	
	@Override
	public String[] getFieldNames() {
		return argumentTypes.getFieldNames();
	}
	
	@Override
	public Type getFieldTypes() {
		return argumentTypes;
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
	
	public Type getKeywordParameterTypes() {
		return keywordParameters == null ? TypeFactory.getInstance().voidType() : keywordParameters;
	}
	
	
	public Type getKeywordParameterType(String label) {
	  return keywordParameters != null ? keywordParameters.getFieldType(label) : null;
	}
	

	public boolean hasKeywordParameter(String label) {
	  return keywordParameters != null ? keywordParameters.hasField(label) : false;
	}
	
	public boolean hasKeywordParameters() {
	  return keywordParameters != null;
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
	protected boolean intersects(RascalType type) {
	    return type.intersectsWithFunction(this);
	}
	
	@Override
    protected boolean intersectsWithFunction(RascalType other) {
        FunctionType otherType = (FunctionType) other;
        
        if (other.getArity() != getArity()) {
            return false;
        }
        
        // TODO should the return type intersect or just be comparable? 
        return otherType.getReturnType().intersects(getReturnType())
            && otherType.getArgumentTypes().intersects(getArgumentTypes());
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
	    Map<Type,Type> bindings = new HashMap<>();

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
	  Type argumentTypes = getArgumentTypes().lub(f.getArgumentTypes());
	  
	  if (argumentTypes.isTuple() && argumentTypes.getArity() == getArity()) {
	    return RTF.functionType(returnType, 
	        argumentTypes, 
	        getKeywordParameterTypes() == f.getKeywordParameterTypes() ? getKeywordParameterTypes() : TF.voidType());
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
	    return RTF.functionType(returnType, argumentTypes, TF.voidType());
	  }
		
	  return TF.voidType();
	}
		
//	@Override
//	protected boolean isSubtypeOfOverloadedFunction(RascalType type) {
//	  OverloadedFunctionType function = (OverloadedFunctionType) type;
//	  for (FunctionType f : function.getAlternatives()) {
//	    if (!this.isSubtypeOf(f)) {
//		  return false;
//	    }
//	  }
//	  
//	  return true;
//	}

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
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(arg.toString());
			if (argumentTypes.hasFieldNames()) {
			    sb.append(" " + argumentTypes.getFieldName(i));
			}
			
			i++;
		}
		
		if (keywordParameters != null) {
		    i = 0;
	        for (Type arg : keywordParameters) {
	            sb.append(", ");
	            sb.append(arg.toString());
	            if (keywordParameters.hasFieldNames()) {
	                sb.append(" " + keywordParameters.getFieldName(i) + " = ...");
	            }
	            
	            i++;
	        }
		}
		sb.append(')');
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return 19 + 19 * returnType.hashCode() + 23 * argumentTypes.hashCode() 
				+ (keywordParameters != null ? 29 * keywordParameters.hashCode() : 0)
				;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionType) {
			FunctionType other = (FunctionType) o;
			
			if (returnType != other.returnType) { 
				return false;
			}
			
			if (argumentTypes != other.argumentTypes) {
				return false;
			}
			
			if (keywordParameters != other.keywordParameters) {
				return false;
			}

			return true;
		}
		return false;
	}
	
	@Override
	public Type instantiate(Map<Type, Type> bindings) {
		return RTF.functionType(returnType.instantiate(bindings), argumentTypes.instantiate(bindings), keywordParameters);
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
	
//			if (matched instanceof OverloadedFunctionType) {
//				OverloadedFunctionType of = (OverloadedFunctionType) matched;
//				// at least one needs to match (also at most one can match)
//				
//				for (Type f : of.getAlternatives()) {
//					if (this.match(f, bindings)) {
//						return true;
//					}
//				}
//				
//				return false;
//			}
//			else 
				if (matched instanceof FunctionType) {
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
		Set<FunctionType> newAlternatives = new HashSet<>();
		
		if(right instanceof FunctionType) {
			if(TF.tupleType(((FunctionType) right).returnType).isSubtypeOf(this.argumentTypes)) {
				return RTF.functionType(this.returnType, ((FunctionType) right).getArgumentTypes(), ((FunctionType) right).keywordParameters);
			}
		} else {
//			if(right instanceof OverloadedFunctionType) {
//			for(FunctionType ftype : ((OverloadedFunctionType) right).getAlternatives()) {
//				if(TF.tupleType(ftype.getReturnType()).isSubtypeOf(this.argumentTypes)) {
//					newAlternatives.add((FunctionType) RTF.functionType(this.returnType, ftype.getArgumentTypes(), ftype.keywordParameters));
//				}
//			}
//		} else {
			throw new IllegalOperationException("compose", this, right);
		}
//		if(!newAlternatives.isEmpty()) 
//			return RTF.overloadedFunctionType(newAlternatives);
		return TF.voidType();
	}

	@Override
	public IValue randomValue(Random random, IValueFactory vf, TypeStore store, Map<Type, Type> typeParameters,
			int maxDepth, int maxBreadth) {
		// TODO Auto-generated method stub
		throw new RuntimeException("randomValue on FunctionType not yet implemented");
	}
}
