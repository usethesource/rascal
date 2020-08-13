/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.core.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.IllegalOperationException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;
import io.usethesource.vallang.type.TypeFactory.TypeReifier;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.core.values.uptr.RascalValueFactory;

//public class OverloadedFunctionType extends RascalType {
//	private final Set<FunctionType> alternatives;
//	private final Type returnType;
//	private static final TypeFactory TF = TypeFactory.getInstance();
//    private static final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
//
//	/*package*/ OverloadedFunctionType(Set<FunctionType> alternatives) {
//		this.alternatives = alternatives;
//		this.returnType = alternatives.iterator().next().getReturnType();
//	}
//	
//	public static class Reifier implements TypeReifier {
//
//	    @Override
//	    public Type getSymbolConstructorType() {
//	        throw new UnsupportedOperationException();
//	    }
//	    
//        @Override
//        public Set<Type> getSymbolConstructorTypes() {
//           return Arrays.stream(new Type[] { 
//                   normalSymbolType(),
//                   deprecatedSymbolType(), // TODO: can be removed after bootstrap
//           }).collect(Collectors.toSet()); 
//        }
//
//        private Type normalSymbolType() {
//            return symbols().typeSymbolConstructor("overloaded", TF.setType(symbols().symbolADT()), "alternatives");
//        }
//
//        private Type deprecatedSymbolType() {
//            return symbols().typeSymbolConstructor("overloaded", TF.setType(symbols().symbolADT()), "overloads", TF.setType(symbols().symbolADT()), "defaults");
//        }
//
//        @Override
//        public Type fromSymbol(IConstructor symbol, TypeStore store, Function<IConstructor, Set<IConstructor>> grammar) {
//            Set<FunctionType> newAlts = new HashSet<>();
//            
//            if (symbol.getConstructorType() == deprecatedSymbolType()) {
//                // TODO remove after bootstrap
//                for (IValue alt : ((ISet) symbol.get("overloads"))) {
//                    Type fromSymbol = symbols().fromSymbol((IConstructor) alt, store, grammar);
//                    newAlts.add((FunctionType) fromSymbol); 
//                }
//                
//                for (IValue alt : ((ISet) symbol.get("defaults"))) {
//                    Type fromSymbol = symbols().fromSymbol((IConstructor) alt, store, grammar);
//                    if (fromSymbol.isConstructor()) {
//                        newAlts.add((FunctionType) RTF.functionType(fromSymbol.getAbstractDataType(), fromSymbol.getFieldTypes(), TF.voidType()));
//                    } else {
//                        newAlts.add((FunctionType) fromSymbol);
//                    }
//                }
//            }
//            else {
//                for (IValue alt : ((ISet) symbol.get("alternatives"))) {
//                    newAlts.add((FunctionType) symbols().fromSymbol((IConstructor) alt, store, grammar)); 
//                }
//            }
//            
//            return RTF.overloadedFunctionType(newAlts);
//        }
//
//        @Override
//        public void asProductions(Type type, IValueFactory vf, TypeStore store, ISetWriter grammar,
//                Set<IConstructor> done) {
//            for (Type alt : ((OverloadedFunctionType) type).getAlternatives()) {
//                alt.asProductions(vf, store, grammar, done);
//            } 
//        }
//        
//        @Override
//        public IConstructor toSymbol(Type type, IValueFactory vf, TypeStore store,  ISetWriter grammar, Set<IConstructor> done) {
//            ISetWriter w = vf.setWriter();
//            for (Type alt : ((OverloadedFunctionType) type).getAlternatives()) {
//              w.insert(alt.asSymbol(vf, store, grammar, done));
//            }
//            return vf.constructor(normalSymbolType(), w.done());
//        }
//
//        
//        @Override
//        public boolean isRecursive() {
//            return true;
//        }
//        
//        @Override
//        public Type randomInstance(Supplier<Type> next, TypeStore store, RandomTypesConfig rnd) {
//            int size = rnd.nextInt(5) + 2;
//            Set<FunctionType> alts = new HashSet<>(); 
//            Type returnType = next.get();
//            int arity = rnd.nextInt(4);
//                    
//            while (size-- > 0) {
//                alts.add((FunctionType) RascalTypeFactory.getInstance().functionType(returnType, randomTuple(next, store, rnd, arity), null));
//            }
//            
//            return RascalTypeFactory.getInstance().overloadedFunctionType(alts);
//        }
//    }
//    
//    @Override
//    public TypeReifier getTypeReifier() {
//        return new Reifier();
//    }
//    
//	@Override
//	public boolean isOverloadedFunction() {
//		return true;
//	}
//	
//	@Override
//	public Type asAbstractDataType() {
//		return RascalValueFactory.Production;
//	}
//	
//	public Type getKeywordParameterTypes() {
//		// TODO: what does this union mean in case of overlapping names?
//		ArrayList<String> labels = new ArrayList<>();
//		ArrayList<Type> types  = new ArrayList<>();
//
//		for (FunctionType f : alternatives) {
//			for (String label : f.getKeywordParameterTypes().getFieldNames()) {
//				if (!labels.contains(label)) {
//					labels.add(label);
//					types.add(f.getKeywordParameterType(label));
//				}
//				// TODO: a clash, but we silently ignore it here?
//			}
//		}
//
//		return TF.tupleType(types.toArray(new Type[types.size()]), labels.toArray(new String[labels.size()]));
//	}
//
//	public int size() {
//		return alternatives.size();
//	}
//	
//	public Type getReturnType() {
//		return returnType;
//	}
//	
//	@Override
//	public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
//	  return visitor.visitOverloadedFunction(this);
//	}
//	
//	@Override
//	protected boolean isSupertypeOf(RascalType type) {
//	  return type.isSubtypeOfOverloadedFunction(this);
//	}
//	
//	@Override
//	protected Type lub(RascalType type) {
//	  return type.lubWithOverloadedFunction(this);
//	}
//	
//	@Override
//	protected Type glb(RascalType type) {
//	  return type.glbWithOverloadedFunction(this);
//	}
//	
//	public Set<FunctionType> getAlternatives() {
//		return Collections.unmodifiableSet(alternatives);
//	}
//	
//	@Override
//	public int getArity() {
//	    int arity = alternatives.stream().findFirst().get().getArity();
//
//	    assert !alternatives.stream().filter(t -> t.getArity() != arity).findAny().isPresent();
//	    
//	    return arity;
//	}
//	
//	@Override
//	protected boolean isSubtypeOfOverloadedFunction(RascalType type) {
//	  OverloadedFunctionType of = (OverloadedFunctionType) type;
//
//	  // if this has at least one alternative that is a sub-type of the other, 
//	  // then yes, this function can act as the other and should be a sub-type
//	  
//	  // TODO: this is broken because of defaults. We should distinguish!
//	  for(FunctionType f : getAlternatives()) {
//	    if(f.isSubtypeOf(of)) {
//	      return true;
//	    }
//	  }
//	  
//	  for(FunctionType f : of.getAlternatives()) {
//		  if(!this.isSubtypeOf(f)) {
//			  return false;
//		  }
//	  }
//
//	  return true;
//	}
//	
//	@Override
//	protected boolean isSubtypeOfFunction(RascalType type) {
//	// TODO: this is broken because of defaults. We should distinguish!
//	  
//	  for (FunctionType a : alternatives) {
//	    if (a.isSubtypeOf(type)) {
//	      return true;
//	    }
//	  }
//	  return false;
//	}
//	
//	@Override
//	protected Type lubWithOverloadedFunction(RascalType type) {		
//	  if(this == type) {
//	    return this;
//	  }
//	  
//	  OverloadedFunctionType of = (OverloadedFunctionType) type;
//
//	  Set<FunctionType> newAlternatives = new HashSet<>();
//	  
//	  for(FunctionType f : getAlternatives()) {
//		  for(FunctionType g : of.getAlternatives()) {
//			  Type lub = f.lubWithFunction(g);
//			  if(lub instanceof FunctionType)
//				  newAlternatives.add((FunctionType)lub);
//		  }
//	  }
//	  
//	  if(!newAlternatives.isEmpty())
//		  return RTF.overloadedFunctionType(newAlternatives);
//	  
//	  return TF.valueType();
//	}
//		
//	@Override
//	protected Type lubWithFunction(RascalType type) {
//	  FunctionType f = (FunctionType) type;
//
//	  Set<FunctionType> newAlternatives = new HashSet<>();
//	  newAlternatives.add(f);
//	  
//	  return this.lubWithOverloadedFunction((RascalType)RTF.overloadedFunctionType(newAlternatives));
//	}
//	
//	@Override
//	protected Type glbWithOverloadedFunction(RascalType type) {
//	  if(this == type) {
//		return this;
//	  }
//		  
//	  OverloadedFunctionType of = (OverloadedFunctionType) type;
//		  
//	  Set<FunctionType> newAlternatives = new HashSet<>();
//	  
//	  if(getReturnType() == of.getReturnType()) {
//	    newAlternatives.addAll(getAlternatives());
//	    newAlternatives.addAll(of.getAlternatives());
//	    return RTF.overloadedFunctionType(newAlternatives);
//	  }
//	  
//	  Type returnType = getReturnType().glb(of.getReturnType());
//		
//	  for(FunctionType f : getAlternatives()) {
//	      newAlternatives.add((FunctionType)RTF.functionType(returnType, f.getArgumentTypes(), f.getKeywordParameterTypes()));
//	  }
//		  
//	  for(FunctionType f : of.getAlternatives()) {
//		  newAlternatives.add((FunctionType)RTF.functionType(returnType, f.getArgumentTypes(), f.getKeywordParameterTypes()));
//	  }
//		  
//	  return RTF.overloadedFunctionType(newAlternatives);
//	}
//
//	@Override
//	protected Type glbWithFunction(RascalType type) {
//	  FunctionType f = (FunctionType) type;
//
//	  Set<FunctionType> newAlternatives = new HashSet<>();
//	  newAlternatives.add(f);
//		  
//	  return this.glbWithOverloadedFunction((RascalType)RTF.overloadedFunctionType(newAlternatives));
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if(obj == null) {
//			return false;
//		}
//		
//		if (obj.getClass().equals(getClass())) {
//			OverloadedFunctionType f = (OverloadedFunctionType) obj;
//			return alternatives.equals(f.alternatives);
//		}
//		return false;
//	}
//	
//	@Override
//	public int hashCode() {
//		// TODO: better hashCode?
//		return 31 + alternatives.hashCode();
//	}
//	
//	@Override
//	public String toString() {
//	    StringBuffer b = new StringBuffer();
//		b.append(getReturnType() + "(");
//		int i = 0;
//		for (FunctionType t : alternatives) {
//		    //assert t.getReturnType() == getReturnType();
//		    if (i++ != 0) {
//		        b.append(" + ");
//		    }
//		    b.append(t.getArgumentTypes().toString() + " ");
//		    
//		    if (t.getKeywordParameterTypes() != null) {
//		        b.append(t.getKeywordParameterTypes().toString() + " ");
//		    }
//		}
//		b.append(")");
//		
//		return b.toString();
//	}
//	
//	@Override
//	public Type compose(Type right) {
//		if (right.isBottom()) {
//			return right;
//		}
//		Set<FunctionType> newAlternatives = new HashSet<>();
//		if(right instanceof FunctionType) {
//			for(FunctionType ftype : this.alternatives) {
//				if(TF.tupleType(((FunctionType) right).getReturnType()).isSubtypeOf(ftype.getArgumentTypes())) {
//					newAlternatives.add((FunctionType) RTF.functionType(ftype.getReturnType(), ((FunctionType) right).getArgumentTypes(), ((FunctionType) right).getKeywordParameterTypes()));
//				}
//			}
//		} else if(right instanceof OverloadedFunctionType) {
//			for(FunctionType ftype : ((OverloadedFunctionType) right).getAlternatives()) {
//				for(FunctionType gtype : this.alternatives) {
//					if(TF.tupleType(ftype.getReturnType()).isSubtypeOf(gtype.getArgumentTypes())) {
//						newAlternatives.add((FunctionType) RTF.functionType(gtype.getReturnType(), ftype.getArgumentTypes(), ftype.getKeywordParameterTypes()));
//					}
//				}
//			}
//		} else {
//			throw new IllegalOperationException("compose", this, right);
//		}
//		if(!newAlternatives.isEmpty()) 
//			return RTF.overloadedFunctionType(newAlternatives);
//		return TF.voidType();
//	}
//
//	@Override
//	public IValue randomValue(Random random, IValueFactory vf, TypeStore store, Map<Type, Type> typeParameters,
//			int maxDepth, int maxBreadth) {
//		// TODO Auto-generated method stub
//		throw new RuntimeException("randomValue not implemented on OverloadedFunctionType");
//	}
//
//    @Override
//    protected boolean intersects(RascalType type) {
//        // TODO: do not know what this should mean
//        return false;
//    }
//
//}
