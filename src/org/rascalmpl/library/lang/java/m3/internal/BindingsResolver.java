/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.lang.java.m3.internal;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.TypeFactory;

public class BindingsResolver {
	private LimitedTypeStore store;
	private final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private final TypeFactory tf = TypeFactory.getInstance();
	private final boolean collectBindings;
	
	private final Map<String, Integer> anonymousClassCounter = new HashMap<String, Integer>();
	private final Map<String, String> resolvedAnonymousClasses = new HashMap<String, String>();
	private final Map<ISourceLocation, Integer> initializerCounter = new HashMap<ISourceLocation, Integer>();
	private final Map<Initializer, ISourceLocation> initializerLookUp = new HashMap<>();
	
    private io.usethesource.vallang.type.Type typeSymbol;
    private final Map<String, ISourceLocation> locationCache;
    private final boolean debug = false;
	
	BindingsResolver(final LimitedTypeStore typeStore, Map<String, ISourceLocation> cache, boolean collectBindings) {
		this.collectBindings = collectBindings;
		this.store = typeStore;
		this.locationCache = cache;
	}
	
	public ISourceLocation resolveBinding(ASTNode node, boolean tryHard) {
	    if (collectBindings) {
	        if (node instanceof TypeDeclaration) {
	            return resolveBinding(((TypeDeclaration) node).resolveBinding());
	        } else if (node instanceof EnumDeclaration) {
	            return resolveBinding(((EnumDeclaration) node).resolveBinding());
	        } else if (node instanceof AnnotationTypeDeclaration) {
	            return resolveBinding(((AnnotationTypeDeclaration) node).resolveBinding());
	        } else if (node instanceof AnnotationTypeMemberDeclaration) {
	            return resolveBinding(((AnnotationTypeMemberDeclaration) node).resolveBinding());
	        } else if (node instanceof AnonymousClassDeclaration) {
	            return resolveBinding(((AnonymousClassDeclaration) node).resolveBinding());
	        } else if (node instanceof EnumConstantDeclaration) {
	            return resolveBinding(((EnumConstantDeclaration) node).resolveVariable());
	        } else if (node instanceof ClassInstanceCreation) {
	            return resolveBinding(((ClassInstanceCreation) node).resolveConstructorBinding());
	        } else if (node instanceof FieldAccess) {
	            return resolveFieldAccess((FieldAccess) node);
	        } else if (node instanceof MethodInvocation) {
	            return resolveBinding(((MethodInvocation) node).resolveMethodBinding());
	        } else if (node instanceof QualifiedName) {
	            return resolveQualifiedName((QualifiedName) node);
	        } else if (node instanceof SimpleName) {
	            return resolveSimpleName(node, tryHard);
	        } else if (node instanceof SuperFieldAccess) {
	            return resolveBinding(((SuperFieldAccess) node).resolveFieldBinding());
	        } else if (node instanceof SuperMethodInvocation) {
	            return resolveBinding(((SuperMethodInvocation) node).resolveMethodBinding());
	        } else if (node instanceof MemberRef) {
	            return resolveBinding(((MemberRef) node).resolveBinding());
	        } else if (node instanceof MethodDeclaration) {
	            return resolveBinding(((MethodDeclaration) node).resolveBinding());
	        } else if (node instanceof MethodRef) {
	            return resolveBinding(((MethodRef) node).resolveBinding());
	        } else if (node instanceof PackageDeclaration) {
	            return resolveBinding(((PackageDeclaration) node).resolveBinding());
	        } else if (node instanceof Type) {
	            return resolveBinding(((Type) node).resolveBinding());
	        } else if (node instanceof TypeParameter) {
	            return resolveBinding(((TypeParameter) node).resolveBinding());
	        } else if (node instanceof VariableDeclaration) {
	            return resolveVariable(node);
	        } else if (node instanceof ConstructorInvocation) {
	            return resolveBinding(((ConstructorInvocation) node).resolveConstructorBinding());
	        } else if (node instanceof SuperConstructorInvocation) {
	            return resolveBinding(((SuperConstructorInvocation) node).resolveConstructorBinding());
	        } else if (node instanceof TypeDeclarationStatement) {
	            return resolveBinding(((TypeDeclarationStatement) node).resolveBinding());
	        } else if (node instanceof Initializer) {
	            return resolveInitializer((Initializer) node);
	        } 
	    }
	    return makeBinding("unknown", null, null);
	}

    private ISourceLocation resolveVariable(ASTNode node) {
        VariableDeclaration n = (VariableDeclaration) node;
    	IVariableBinding bin = n.resolveBinding(); 
        ISourceLocation result = resolveBinding(n.resolveBinding());
        // Have to move towards parent to make the binding unique
        if (result.getScheme() == "unresolved") {
        	result = resolveBinding(n.getParent(), bin, n.getName());
        }
        return result;
    }

    private ISourceLocation resolveSimpleName(ASTNode node, boolean tryHard) {
        SimpleName n = (SimpleName) node;
        IBinding resolveBinding = n.resolveBinding();
        ISourceLocation result = resolveBinding(resolveBinding);
        // Have to move towards parent to make the binding unique
        if (result.getScheme() == "unresolved" && tryHard) {
        	result = resolveBinding(n.getParent(), resolveBinding, n);
        }
        return result;
    }

    private ISourceLocation resolveFieldAccess(FieldAccess node) {
        ITypeBinding tb = node.getExpression().resolveTypeBinding();
        
        if (tb != null && tb.isArray() && "length".equals(node.getName().getIdentifier())) {
            ISourceLocation arrayType = resolveBinding(tb);
            return makeBinding("java+arrayLength", arrayType.getAuthority(), arrayType.getPath());
        }
        
        return resolveBinding(node.resolveFieldBinding());
    }
	
	private ISourceLocation resolveBinding(ASTNode parentNode, IBinding resolvedBinding, SimpleName nodeName) {
		ISourceLocation parentBinding = resolveBinding(parentNode, false);
		// Something has to declare it!!!
		while(parentBinding.getScheme().equals("unknown") || parentBinding.getScheme().equals("unresolved")) {
			if (parentNode == null) {
				//truely unresolved/unknown
				return makeBinding("unresolved", null, null);
			}
			parentNode = parentNode.getParent();
			parentBinding = resolveBinding(parentNode, false);
		}

		// TODO: @ashimshahi please check this additional null check and the way we return an unresolved binding here
		if (resolvedBinding == null) {
			return makeBinding("unresolved", null, null);
		}
		
		String key = resolvedBinding.getKey();
		// Binding keys for initializers are not unique so we always force them to be recomputed
		if (!(parentNode instanceof Initializer)) {
			if (locationCache.containsKey(key)) {
				return locationCache.get(key);
			}
		}
		String qualifiedName = parentBinding.getPath();
		String[] bindingKeys = key.split("#");
		
		if (bindingKeys.length > 2) {
			// scoped variable that may have the same name as some other scoped variable
			for (int i = 1; i < bindingKeys.length - 1; i++) {
				if (!qualifiedName.endsWith("/"))
					qualifiedName += "/";
				qualifiedName += "scope("+bindingKeys[i]+")/";
			}
		}
		
		// FIXME: May not be variable only!!!
		ISourceLocation childBinding = makeBinding("java+variable", null, qualifiedName.concat(nodeName.getIdentifier()));
		locationCache.put(key, childBinding);
		return childBinding;
	}
	
	private ISourceLocation resolveQualifiedName(QualifiedName node) {
		ITypeBinding tb = node.getQualifier().resolveTypeBinding();
		
		if (tb != null && tb.isArray()  && "length".equals(node.getName().getIdentifier())) {
		    ISourceLocation arrayType = resolveBinding(tb);
		    return makeBinding("java+arrayLength", arrayType.getAuthority(), arrayType.getPath());
		}
		
		return resolveBinding(node.getName(), false);
	}
	
	private ISourceLocation resolveInitializer(Initializer node) {
		if (initializerLookUp.containsKey(node)) {
			return initializerLookUp.get(node); 
		}
		int initCounter = 1;
		ISourceLocation parent = resolveBinding(node.getParent(), true);
		if (initializerCounter.containsKey(parent)) {
	      initCounter = initializerCounter.get(parent) + 1;
	    }
		initializerCounter.put(parent, initCounter);
		String key = "";
		for (String storedKey: locationCache.keySet()) {
			if (locationCache.get(storedKey).equals(parent)) {
				key = storedKey;
				break;
			}
		}
		key += "$initializer" + initCounter;
		ISourceLocation result = makeBinding("java+initializer", null, parent.getPath() + "$initializer" + initCounter);
		locationCache.put(key, result);
		initializerLookUp.put(node, result);
		return result;
	}
	
	public ISourceLocation resolveBinding(IBinding binding) {
		if (binding == null) {
			return makeBinding("unresolved", null, null);
	    }
		if (binding instanceof ITypeBinding) {
			return resolveBinding((ITypeBinding) binding);
		} 
		else if (binding instanceof IMethodBinding) {
			return resolveBinding((IMethodBinding) binding);
	    } 
		else if (binding instanceof IPackageBinding) {
			return resolveBinding((IPackageBinding) binding);
	    } 
		else if (binding instanceof IVariableBinding) {
			return resolveBinding((IVariableBinding) binding);
	    }
		else if (binding instanceof IModuleBinding) {
			return makeBinding("java+module", "", ((IModuleBinding) binding).getName());
		}

		return makeBinding("unknown", null, null);
	}
	
	public IConstructor resolveType(IBinding binding, boolean isDeclaration) {
	    if (debug) {
	        System.err.println("resolving " + binding);
	    }
		IConstructor result = unresolvedSym();
		if (binding != null) {
			ISourceLocation uri = resolveBinding(binding);
		    if (binding instanceof ITypeBinding) {
				return computeTypeSymbol(uri, (ITypeBinding) binding, isDeclaration);
		    } 
			else if (binding instanceof IMethodBinding) {
				return computeMethodTypeSymbol(uri, (IMethodBinding) binding, isDeclaration);
		    } 
			else if (binding instanceof IVariableBinding) {
				return resolveType(((IVariableBinding) binding).getType(), isDeclaration);
		    }
			else if (binding instanceof IModuleBinding) {
				return computeTypeSymbol(uri);
			}
		}
	    return result;
	}
	
  private IConstructor computeMethodTypeSymbol(ISourceLocation decl, IMethodBinding binding, boolean isDeclaration) {
    IList parameters = computeTypes(isDeclaration ? binding.getParameterTypes() : binding.getTypeArguments(), false);
    
    if (binding.isConstructor()) {
      return constructorSymbol(decl, parameters);
    } else {
      IList typeParameters = computeTypes(isDeclaration ? binding.getTypeParameters() : binding.getTypeArguments(), isDeclaration);
      IConstructor retSymbol = resolveType(binding.getReturnType(), false);
      
      return methodSymbol(decl, typeParameters, retSymbol,  parameters);
    }
  }

  private IList computeTypes(ITypeBinding[] bindings, boolean isDeclaration) {
    IListWriter parameters = values.listWriter();
    for (ITypeBinding parameterType: bindings) {
    	IConstructor arg = resolveType(parameterType, isDeclaration);
        parameters.append(arg);
    }
    return parameters.done();
  }

  private io.usethesource.vallang.type.Type getTypeSymbol() {
    if (typeSymbol == null) {
      typeSymbol = store.lookupAbstractDataType("TypeSymbol");
    }
    return typeSymbol;
  }

  private IConstructor methodSymbol(ISourceLocation decl, IList typeParameters, IConstructor retSymbol, IList parameters) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "method", tf.tupleType(decl.getType(), typeParameters.getType(), retSymbol.getType(), parameters.getType()));
    return values.constructor(cons, decl, typeParameters, retSymbol, parameters);
  }

  private IConstructor constructorSymbol(ISourceLocation decl, IList parameters) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "constructor", tf.tupleType(decl.getType(), parameters.getType()));
    return values.constructor(cons, decl, parameters);
  }

  private IConstructor parameterNode(ISourceLocation decl, ITypeBinding[] bound, boolean isDeclaration, boolean isUpperbound) {
	IConstructor boundSym = unboundedSym();
    if (isDeclaration) {
      if (bound.length > 0) {
        boundSym = boundSymbol(bound, isDeclaration, isUpperbound);
      }
      io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "typeParameter", tf.tupleType(decl.getType(), boundSym.getType()));
      return values.constructor(cons, decl, boundSym);
    }
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "typeArgument", tf.tupleType(decl.getType()));
    return values.constructor(cons, decl);
  }

  private IConstructor unboundedSym() {
    io.usethesource.vallang.type.Type boundType = store.lookupAbstractDataType("Bound");
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(boundType, "unbounded", tf.voidType());
    return values.constructor(cons);
  }
  
  private IConstructor unresolvedSym() {
	io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "unresolved", tf.voidType());
    return values.constructor(cons);
  }

  private Set<ITypeBinding> wildCardBoundsVisited = new HashSet<>();
  
    private IConstructor computeTypeSymbol(ISourceLocation decl) {
		return moduleSymbol(decl);
	}

	private IConstructor computeTypeSymbol(ISourceLocation decl, ITypeBinding binding, boolean isDeclaration) {
    if (binding.isPrimitive()) {
      return primitiveSymbol(binding.getName());
    }
    else if (binding.isArray()) {
      return arraySymbol(resolveType(binding.getComponentType(), isDeclaration), binding.getDimensions());
    }
    else if (binding.isNullType()) {
      return nullSymbol(); 
    }
    else if (binding.isEnum()) {
      return enumSymbol(decl);
    }
    else if (binding.isTypeVariable()) {
    	return parameterNode(decl, binding.getTypeBounds(), isDeclaration, true);
    }
    else if (binding.isWildcardType()) {
      /*
       * For wildcards the isUpperbound() information is only correct in the binding here.
       * The previous implementation tried to look for that information in the type binding of the bounds,
       * which wasn't correct.
       */
      ITypeBinding bound = binding.getBound();
      
      if (bound == null) {
        return wildcardSymbol(unboundedSym());
      }
      else {
        return wildcardSymbol(boundSymbol(new ITypeBinding[] { bound }, isDeclaration, binding.isUpperbound()));
      }
    }
    else if (binding.isClass()) {
      return classSymbol(decl, computeTypes(isDeclaration ? binding.getTypeParameters() : binding.getTypeArguments(), isDeclaration));
    }
    else if (binding.isCapture()) {
      ITypeBinding[] typeBounds = binding.getTypeBounds();
      ITypeBinding wildcard = binding.getWildcard();
      
      if (typeBounds.length == 0) {
        return captureSymbol(unboundedSym(), resolveType(wildcard, isDeclaration));
      }
      else {
    	  // the type graph has cycles in case of abstract classes which extend some wildcard like class X extends ? extends Enum<?>.
    	  // we fix this by recording the bounds on the stack and if we end up in a binding which we are already resolving we bail out.
    	  for (ITypeBinding b : typeBounds) {
    		  if (wildCardBoundsVisited.contains(b)) {
    			  return captureSymbol(unboundedSym(), resolveType(wildcard, isDeclaration));
    		  }
    		  wildCardBoundsVisited.add(b);
    	  }
         IConstructor res = captureSymbol(boundSymbol(typeBounds, isDeclaration, true), resolveType(wildcard, isDeclaration));
         for (ITypeBinding b : typeBounds) {
        	 wildCardBoundsVisited.remove(b);
         }
         return res;
      }
    }
    else if (binding.isInterface()) {
      return interfaceSymbol(decl, computeTypes(isDeclaration ? binding.getTypeParameters() : binding.getTypeArguments(), isDeclaration));
    }
    
    return null;
  }

  private IConstructor captureSymbol(IConstructor bound, IConstructor wildcard) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "capture", tf.tupleType(bound.getType(), wildcard.getType()));
    return values.constructor(cons, bound, wildcard);
  }

  private IConstructor wildcardSymbol(IConstructor boundSymbol) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "wildcard", tf.tupleType(boundSymbol.getType()));
    return values.constructor(cons, boundSymbol);
  }

  static int stackdepth =0;
  private IConstructor boundSymbol(ITypeBinding[] bound, boolean isDeclaration, boolean isUpperbound) {
	// Assumption: Anything appearing in a bound symbol is not a declaration
	  stackdepth++;
	  IList boundSym = computeTypes(bound, false);
	  if (stackdepth > 5) {
		  System.err.println("100!");
	  }
	  stackdepth--;
    
    io.usethesource.vallang.type.Type boundType = store.lookupAbstractDataType("Bound");
    
    if (!isUpperbound) {
      io.usethesource.vallang.type.Type sup = store.lookupConstructor(boundType, "super", tf.tupleType(boundSym.getType()));
      return values.constructor(sup, boundSym);
    }
    else {
      io.usethesource.vallang.type.Type ext = store.lookupConstructor(boundType, "extends", tf.tupleType(boundSym.getType()));
      return values.constructor(ext, boundSym);
    }
  }

  private IConstructor enumSymbol(ISourceLocation decl) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "enum", tf.tupleType(decl.getType()));
    return values.constructor(cons, decl);
  }

private IConstructor interfaceSymbol(ISourceLocation decl, IList typeParameters) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "interface", tf.tupleType(decl.getType(), typeParameters.getType()));
	return values.constructor(cons, decl, typeParameters);
}

private IConstructor classSymbol(ISourceLocation decl, IList typeParameters) {
    if (decl.getPath().equals("/java/lang/Object")) {
      io.usethesource.vallang.type.Type obj = store.lookupConstructor(getTypeSymbol(), "object", tf.voidType());
      return values.constructor(obj);
    }
    else {
      io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "class", tf.tupleType(decl.getType(), typeParameters.getType()));
      return values.constructor(cons, decl, typeParameters);
    }
}

private IConstructor nullSymbol() {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "null", tf.voidType());
    return values.constructor(cons);
}

  private IConstructor primitiveSymbol(String name) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), name, tf.voidType());
    return values.constructor(cons);
  }

  private IConstructor moduleSymbol(ISourceLocation name) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "module", tf.sourceLocationType());
    return values.constructor(cons, name);
  }

  private IConstructor arraySymbol(IConstructor elem, int dimensions) {
    io.usethesource.vallang.type.Type cons = store.lookupConstructor(getTypeSymbol(), "array", tf.tupleType(elem.getType(), tf.integerType()));
    return values.constructor(cons, elem, values.integer(dimensions));
  }

  private ISourceLocation resolveBinding(IMethodBinding binding) {
		if (binding == null) {
			return makeBinding("unresolved", null, null);
		}
		if (locationCache.containsKey(binding.getKey()))
			return locationCache.get(binding.getKey());
		String signature = resolveBinding(binding.getDeclaringClass()).getPath();
		if (!signature.isEmpty()) {
	      signature = signature.concat("/");
	    }
		String params = "";
		
		for (ITypeBinding parameterType: binding.getMethodDeclaration().getParameterTypes()) {
			if (!params.isEmpty()) {
		        params = params.concat(",");
		      }
			
			if (parameterType.isTypeVariable()) {
			  params = params.concat(parameterType.getName()); 
			}
			else {
			  params = params.concat(getPath(resolveBinding(parameterType)).replaceAll("/", "."));
			}
		}
		signature = signature.concat(binding.getMethodDeclaration().getName() + "(" + params + ")");
		String scheme = "unknown";
		if (binding.isConstructor()) {
	      scheme = "java+constructor";
	    } else {
	      scheme = "java+method";
	    }
		ISourceLocation result = makeBinding(scheme, null, signature);
		locationCache.put(binding.getKey(), result);
		return result;
	}
	
	private ISourceLocation resolveBinding(IPackageBinding binding) {
		if (binding == null) {
	      return makeBinding("unresolved", null, null);
	    }
		if (locationCache.containsKey(binding.getKey()))
			return locationCache.get(binding.getKey());
		ISourceLocation result = makeBinding("java+package", null, binding.getName().replaceAll("\\.", "/"));
		locationCache.put(binding.getKey(), result);
		return result;
	}
	
	private ISourceLocation resolveBinding(ITypeBinding binding) {
	    try {
	        if (binding == null) {
	            return makeBinding("unresolved", null, null);
	        }

	        if (locationCache.containsKey(binding.getKey())) {
	            return locationCache.get(binding.getKey());
	        }

	        String scheme = binding.isInterface() ? "java+interface" : "java+class";
	        String qualifiedName = binding.getTypeDeclaration().getQualifiedName();

	        if (qualifiedName.isEmpty()) {
	            if (binding.getDeclaringMethod() != null) {
	                qualifiedName = resolveBinding(binding.getDeclaringMethod()).getPath();
	            }
	            else if (binding.getDeclaringClass() != null) {
	                qualifiedName = resolveBinding(binding.getDeclaringClass()).getPath();
	            }
	            else {
	                if (debug) {
	                    System.err.println("No defining class or method for " + binding.getClass().getCanonicalName());
	                    System.err.println("Most probably anonymous class in initializer");
	                }
	            }
	        }

	        if (binding.isEnum()) {
	            scheme = "java+enum";
	        }

	        if (binding.isArray()) {
	            scheme = "java+array";
	        }

	        if (binding.isTypeVariable()) {
	            scheme = "java+typeVariable";
	            if (binding.getDeclaringMethod() != null) {
	                qualifiedName = resolveBinding(binding.getDeclaringMethod()).getPath() + "/" + qualifiedName;
	            } else if (binding.getDeclaringClass() != null) {
	                qualifiedName = resolveBinding(binding.getDeclaringClass()).getPath() + "/" + qualifiedName;
	            }
	        }

	        if (binding.isPrimitive()) {
	            scheme = "java+primitiveType";
	        }

	        if (binding.isWildcardType()) {
	            return makeBinding("unknown", null, null);
	        }

	        if (binding.isLocal()) {
	            qualifiedName = qualifiedName.concat("/").concat(binding.getName());
	        }

	        if (binding.isAnonymous()) {
	            String key = binding.getKey();
	            if (resolvedAnonymousClasses.containsKey(key)) {
	                qualifiedName = resolvedAnonymousClasses.get(key);
	            }
	            else {
	                int anonCounter = 1;
	                if (anonymousClassCounter.containsKey(qualifiedName)) {
	                    anonCounter = anonymousClassCounter.get(qualifiedName) + 1;
	                }
	                else { 
	                    anonCounter = 1;
	                }
	                anonymousClassCounter.put(qualifiedName, anonCounter);
	                qualifiedName += "$anonymous" + anonCounter;
	                resolvedAnonymousClasses.put(key, qualifiedName);
	            }
	            scheme = "java+anonymousClass";
	        }
	        ISourceLocation result = makeBinding(scheme, null, qualifiedName.replaceAll("\\.", "/"));
	        locationCache.put(binding.getKey(), result);
	        return result;
		}
        catch (org.eclipse.jdt.internal.compiler.problem.AbortCompilation e) {
            // work around internal error of JDT compiler which can throw this exception 
            // after calling getKey()
            
            return makeBinding("unknown", null, null);
        }
	}
	
	private ISourceLocation resolveBinding(IVariableBinding binding) {
		if (binding == null) {
	      return makeBinding("unresolved", null, null);
	    }
		
		String qualifiedName = "";
		
		ITypeBinding declaringClass = binding.getDeclaringClass();
		if (declaringClass != null) {
			qualifiedName = getPath(resolveBinding(declaringClass));
		} else {
			IMethodBinding declaringMethod = binding.getDeclaringMethod();
			if (declaringMethod != null) {
				qualifiedName = getPath(resolveBinding(declaringMethod));
			}
			else {
			    //binding.getDeI
			}
		}
		
		if (!qualifiedName.isEmpty()) {
	      qualifiedName = qualifiedName.concat("/");
	    } else {
	    	return makeBinding("unresolved", null, null);
	    }
		
		if (locationCache.containsKey(binding.getKey()))
			return locationCache.get(binding.getKey());
		
		String bindingKey = binding.getKey();
		String[] bindingKeys = bindingKey.split("#");
		
		if (bindingKeys.length > 2) {
			// scoped variable that may have the same name as some other scoped variable
			for (int i = 1; i < bindingKeys.length - 1; i++) {
				if (!qualifiedName.endsWith("/"))
					qualifiedName += "/";
				qualifiedName += "scope("+bindingKeys[i]+")/";
			}
		}
		
		String scheme = "java+variable";
		if (binding.isEnumConstant()) {
	      scheme = "java+enumConstant";
	    } else if (binding.isParameter()) {
	      scheme = "java+parameter";
	    } else if (binding.isField()) {
	      scheme = "java+field";
	    }
		
		ISourceLocation result = makeBinding(scheme, null, qualifiedName.concat(binding.getName()));
		locationCache.put(binding.getKey(), result);
		return result;
	}
	
	protected ISourceLocation makeBinding(String scheme, String authority, String path) {
		try {
			return values.sourceLocation(scheme, authority, path);
		} catch (URISyntaxException | UnsupportedOperationException e) {
			throw new RuntimeException("Should not happen", e);
		}
	}
	
	private String getPath(ISourceLocation iSourceLocation) {
		String path = iSourceLocation.getPath();
		return path.substring(1, path.length());
	}
}
