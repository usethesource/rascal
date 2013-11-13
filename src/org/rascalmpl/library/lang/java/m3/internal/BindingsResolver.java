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
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
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

public class BindingsResolver {
	private TypeStore store;
	private final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private final TypeFactory tf = TypeFactory.getInstance();
	private final boolean collectBindings;
	
	private final Map<String, Integer> anonymousClassCounter = new HashMap<String, Integer>();
	private final Map<String, String> resolvedAnonymousClasses = new HashMap<String, String>();
	private final Map<ISourceLocation, Integer> initializerCounter = new HashMap<ISourceLocation, Integer>();
	private final Map<Initializer, ISourceLocation> initializerLookUp = new HashMap<>();
	
  private org.eclipse.imp.pdb.facts.type.Type typeSymbol;
	
	BindingsResolver(final TypeStore store, boolean collectBindings) {
		this.collectBindings = collectBindings;
		this.store = store;
	}
	
	public ISourceLocation resolveBinding(ASTNode node) {
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
        return resolveBinding(((FieldAccess) node).resolveFieldBinding());
      } else if (node instanceof MethodInvocation) {
        return resolveBinding(((MethodInvocation) node).resolveMethodBinding());
      } else if (node instanceof QualifiedName) {
        return resolveQualifiedName((QualifiedName) node);
      } else if (node instanceof SimpleName) {
        return resolveBinding(((SimpleName) node).resolveBinding());
      } else if (node instanceof SuperFieldAccess) {
        return resolveBinding(((SuperFieldAccess) node).resolveFieldBinding());
      } else if (node instanceof SuperMethodInvocation) {
        return resolveBinding(((SuperMethodInvocation) node).resolveMethodBinding());
//      } else if (node instanceof Expression) {
//        return resolveBinding(((Expression) node).resolveTypeBinding());
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
    	VariableDeclaration n = (VariableDeclaration) node;
        ISourceLocation result = resolveBinding(n.resolveBinding());
        // Have to move towards parent to make the binding unique
        if (result.getScheme() == "unresolved") {
        	result = resolveBinding(n.getParent(), n.getName().getIdentifier());
        }
        return result;
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
		return convertBinding("unknown", null, null);
	}
	
	private ISourceLocation resolveBinding(ASTNode parentNode, String childName) {
		ISourceLocation parentBinding = resolveBinding(parentNode);
		// Something has to declare it!!!
		while(parentBinding.getScheme().equals("unknown") || parentBinding.getScheme().equals("unresolved")) {
			if (parentNode == null) {
				//truely unresolved/unknown
				return convertBinding("unresolved", null, null);
			}
			parentNode = parentNode.getParent();
			parentBinding = resolveBinding(parentNode);
		}
		String key = "";
		for (String storedKey: EclipseJavaCompiler.cache.keySet()) {
			if (EclipseJavaCompiler.cache.get(storedKey).equals(parentBinding)) {
				key = storedKey;
				break;
			}
		}
		key += "#".concat(childName);
		if (EclipseJavaCompiler.cache.containsKey(key)) {
			return EclipseJavaCompiler.cache.get(key);
		}
		// FIXME: May not be variable only!!!
		ISourceLocation childBinding = convertBinding("java+variable", null, parentBinding.getPath().concat("/").concat(childName));
		EclipseJavaCompiler.cache.put(key, childBinding);
		return childBinding;
	}
	
	private ISourceLocation resolveQualifiedName(QualifiedName node) {
		ISourceLocation parent = resolveBinding(node.getQualifier().resolveTypeBinding());
		ISourceLocation name = resolveBinding(node.getName());
		
		if (parent.getScheme().equals("java+array") && name.getScheme().equals("unresolved")) {
			return convertBinding("java+field", null, resolveBinding(node.getQualifier()).getPath() + "/" + node.getName().getIdentifier());
		}
		
		return name;
	}
	
	private ISourceLocation resolveInitializer(Initializer node) {
		if (initializerLookUp.containsKey(node)) {
			return initializerLookUp.get(node); 
		}
		int initCounter = 1;
		ISourceLocation parent = resolveBinding(node.getParent());
		if (initializerCounter.containsKey(parent)) {
	      initCounter = initializerCounter.get(parent) + 1;
	    }
		initializerCounter.put(parent, initCounter);
		String key = "";
		for (String storedKey: EclipseJavaCompiler.cache.keySet()) {
			if (EclipseJavaCompiler.cache.get(storedKey).equals(parent)) {
				key = storedKey;
				break;
			}
		}
		key += "$initializer" + initCounter;
		ISourceLocation result = convertBinding("java+initializer", null, parent.getPath() + "$initializer" + initCounter);
		EclipseJavaCompiler.cache.put(key, result);
		initializerLookUp.put(node, result);
		return result;
	}
	
	public ISourceLocation resolveBinding(IBinding binding) {
		if (binding == null) {
	      return convertBinding("unresolved", null, null);
	    }
		if (binding instanceof ITypeBinding) {
	      return resolveBinding((ITypeBinding) binding);
	    } else if (binding instanceof IMethodBinding) {
	      return resolveBinding((IMethodBinding) binding);
	    } else if (binding instanceof IPackageBinding) {
	      return resolveBinding((IPackageBinding) binding);
	    } else if (binding instanceof IVariableBinding) {
	      return resolveBinding((IVariableBinding) binding);
	    }
		return convertBinding("unknown", null, null);
	}
	
	public IConstructor resolveType(ISourceLocation uri, IBinding binding, boolean isDeclaration) {
		if (binding == null) {
			return null;
		}
	    if (binding instanceof ITypeBinding) {
	      return computeTypeSymbol(uri, (ITypeBinding) binding, isDeclaration);
	    } else if (binding instanceof IMethodBinding) {
	      return computeMethodTypeSymbol(uri, (IMethodBinding) binding, isDeclaration);
	    } else if (binding instanceof IVariableBinding) {
	      return resolveType(uri, ((IVariableBinding) binding).getType(), isDeclaration);
	    }
	    
	    return null;
	}
	
	public IConstructor computeMethodTypeSymbol(IMethodBinding binding, boolean isDeclaration) {
		if (binding == null)
			return null;
		ISourceLocation decl = resolveBinding(binding);
		return computeMethodTypeSymbol(decl, binding, isDeclaration);
	}
	
  private IConstructor computeMethodTypeSymbol(ISourceLocation decl, IMethodBinding binding, boolean isDeclaration) {
    IList parameters = computeTypes(binding.getParameterTypes(), false);
    
    if (binding.isConstructor()) {
      return constructorSymbol(decl, parameters);
    } else {
      IList typeParameters = computeTypes(isDeclaration ? binding.getTypeParameters() : binding.getTypeArguments(), isDeclaration);
      IConstructor retSymbol = computeTypeSymbol(binding.getReturnType(), false);
      
      return methodSymbol(decl, typeParameters, retSymbol,  parameters);
    }
  }

  private IList computeTypes(ITypeBinding[] bindings, boolean isDeclaration) {
    IListWriter parameters = values.listWriter();
    for (ITypeBinding parameterType: bindings) {
        parameters.append(computeTypeSymbol(parameterType, isDeclaration));
    }
    return parameters.done();
  }

  private org.eclipse.imp.pdb.facts.type.Type getTypeSymbol() {
    if (typeSymbol == null) {
      typeSymbol = store.lookupAbstractDataType("TypeSymbol");
    }
    return typeSymbol;
  }

  private IConstructor methodSymbol(ISourceLocation decl, IList typeParameters, IConstructor retSymbol, IList parameters) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "method", tf.tupleType(decl.getType(), typeParameters.getType(), retSymbol.getType(), parameters.getType()));
    return values.constructor(cons, decl, typeParameters, retSymbol, parameters);
  }

  private IConstructor constructorSymbol(ISourceLocation decl, IList parameters) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "constructor", tf.tupleType(decl.getType(), parameters.getType()));
    return values.constructor(cons, decl, parameters);
  }

  private IConstructor parameterNode(ISourceLocation decl, ITypeBinding bound, boolean isDeclaration) {
    if (bound != null) {
      IConstructor boundSym = boundSymbol(bound, isDeclaration);
      org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "typeParameter", tf.tupleType(decl.getType(), boundSym.getType()));
      return values.constructor(cons, decl, boundSym);
    }
    else {
      return parameterNode(decl);
    }
  }

  private IConstructor parameterNode(ISourceLocation decl) {
    IConstructor boundSym = unboundedSym();
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "typeParameter", tf.tupleType(decl.getType(), boundSym.getType()));
    return values.constructor(cons, decl, boundSym);
  }

  private IConstructor unboundedSym() {
    org.eclipse.imp.pdb.facts.type.Type boundType = store.lookupAbstractDataType("Bound");
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(boundType, "unbounded", tf.voidType());
    return values.constructor(cons);
  }

  public IConstructor computeTypeSymbol(ITypeBinding binding, boolean isDeclaration) {
	  if (binding == null)
		  return null;
	  ISourceLocation decl = resolveBinding(binding);
	  return computeTypeSymbol(decl, binding, isDeclaration);
  }

   private IConstructor computeTypeSymbol(ISourceLocation decl, ITypeBinding binding, boolean isDeclaration) {
    if (binding.isPrimitive()) {
      return primitiveSymbol(binding.getName());
    }
    else if (binding.isArray()) {
      return arraySymbol(computeTypeSymbol(binding.getComponentType(), isDeclaration), binding.getDimensions());
    }
    else if (binding.isNullType()) {
      return nullSymbol(); 
    }
    else if (binding.isEnum()) {
      return enumSymbol(decl);
    }
    else if (binding.isTypeVariable()) {
      ITypeBinding bound = binding.getBound();
      
      if (bound == null) {
        return parameterNode(decl);
      }
      else {
        return parameterNode(decl, bound, isDeclaration);
      } 
    }
    else if (binding.isWildcardType()) {
      ITypeBinding bound = binding.getBound();
      
      if (bound == null) {
        return wildcardSymbol(unboundedSym());
      }
      else {
        return wildcardSymbol(boundSymbol(binding.getBound(), isDeclaration));
      }
    }  
    else if (binding.isClass()) {
      return classSymbol(decl, computeTypes(isDeclaration ? binding.getTypeParameters() : binding.getTypeArguments(), isDeclaration));
    }
    else if (binding.isCapture()) {
      ITypeBinding[] typeBounds = binding.getTypeBounds();
      ITypeBinding wildcard = binding.getWildcard();
      
      if (typeBounds.length == 0) {
        return captureSymbol(unboundedSym(), computeTypeSymbol(wildcard, isDeclaration));
      }
      else {
        return captureSymbol(boundSymbol(typeBounds[0], isDeclaration), computeTypeSymbol(wildcard, isDeclaration));
      }
    }
    else if (binding.isInterface()) {
      return interfaceSymbol(decl, computeTypes(binding.getTypeArguments(), isDeclaration));
    }
    
    return null;
  }

  private IConstructor captureSymbol(IConstructor bound, IConstructor wildcard) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "capture", tf.tupleType(bound.getType(), wildcard.getType()));
    return values.constructor(cons, bound, wildcard);
  }

  private IConstructor wildcardSymbol(IConstructor boundSymbol) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "wildcard", tf.tupleType(boundSymbol.getType()));
    return values.constructor(cons, boundSymbol);
  }

  private IConstructor boundSymbol(ITypeBinding bound, boolean isDeclaration) {
    IConstructor boundSym = computeTypeSymbol(bound, isDeclaration);
    
    org.eclipse.imp.pdb.facts.type.Type boundType = store.lookupAbstractDataType("Bound");
    
    if (bound.isUpperbound()) {
      org.eclipse.imp.pdb.facts.type.Type sup = store.lookupConstructor(boundType, "super", tf.tupleType(boundSym.getType()));
      return values.constructor(sup, boundSym);
    }
    else {
      org.eclipse.imp.pdb.facts.type.Type ext = store.lookupConstructor(boundType, "extends", tf.tupleType(boundSym.getType()));
      return values.constructor(ext, boundSym);
    }
  }

  private IConstructor enumSymbol(ISourceLocation decl) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "enum", tf.tupleType(decl.getType()));
    return values.constructor(cons, decl);
  }

  private IConstructor interfaceSymbol(ISourceLocation decl, IList typeParameters) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "interface", tf.tupleType(decl.getType(), typeParameters.getType()));
    return values.constructor(cons, decl, typeParameters);

  }

  private IConstructor classSymbol(ISourceLocation decl, IList typeParameters) {
    if (decl.getURI().getPath().equals("/java/lang/Object")) {
      org.eclipse.imp.pdb.facts.type.Type obj = store.lookupConstructor(getTypeSymbol(), "object", tf.voidType());
      return values.constructor(obj);
    }
    else {
      org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "class", tf.tupleType(decl.getType(), typeParameters.getType()));
      return values.constructor(cons, decl, typeParameters);
    }
}

  private IConstructor nullSymbol() {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "null", tf.voidType());
    return values.constructor(cons);
  }

  private IConstructor primitiveSymbol(String name) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), name, tf.voidType());
    return values.constructor(cons);
  }

  private IConstructor arraySymbol(IConstructor elem, int dimensions) {
    org.eclipse.imp.pdb.facts.type.Type cons = store.lookupConstructor(getTypeSymbol(), "array", tf.tupleType(elem.getType(), tf.integerType()));
    return values.constructor(cons, elem, values.integer(dimensions));
  }

  private ISourceLocation resolveBinding(IMethodBinding binding) {
		if (binding == null) {
			return convertBinding("unresolved", null, null);
		}
		if (EclipseJavaCompiler.cache.containsKey(binding.getKey()))
			return EclipseJavaCompiler.cache.get(binding.getKey());
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
		signature = signature.concat(binding.getName() + "(" + params + ")");
		String scheme = "unknown";
		if (binding.isConstructor()) {
	      scheme = "java+constructor";
	    } else {
	      scheme = "java+method";
	    }
		ISourceLocation result = convertBinding(scheme, null, signature);
		EclipseJavaCompiler.cache.put(binding.getKey(), result);
		return result;
	}
	
	private ISourceLocation resolveBinding(IPackageBinding binding) {
		if (binding == null) {
	      return convertBinding("unresolved", null, null);
	    }
		if (EclipseJavaCompiler.cache.containsKey(binding.getKey()))
			return EclipseJavaCompiler.cache.get(binding.getKey());
		ISourceLocation result = convertBinding("java+package", null, binding.getName().replaceAll("\\.", "/"));
		EclipseJavaCompiler.cache.put(binding.getKey(), result);
		return result;
	}
	
	private ISourceLocation resolveBinding(ITypeBinding binding) {
		if (binding == null) {
			return convertBinding("unresolved", null, null);
		}
		if (EclipseJavaCompiler.cache.containsKey(binding.getKey()))
			return EclipseJavaCompiler.cache.get(binding.getKey());
		
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
				System.err.println("Should not happen");
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
		}
		
		if (binding.isPrimitive()) {
			scheme = "java+primitiveType";
		}
		
		if (binding.isWildcardType()) {
			return convertBinding("unknown", null, null);
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
		ISourceLocation result = convertBinding(scheme, null, qualifiedName.replaceAll("\\.", "/"));
		EclipseJavaCompiler.cache.put(binding.getKey(), result);
		return result;
	}
	
	private ISourceLocation resolveBinding(IVariableBinding binding) {
		if (binding == null) {
	      return convertBinding("unresolved", null, null);
	    }
		if (EclipseJavaCompiler.cache.containsKey(binding.getKey()))
			return EclipseJavaCompiler.cache.get(binding.getKey());
		String qualifiedName = "";
		
		ITypeBinding declaringClass = binding.getDeclaringClass();
		if (declaringClass != null) {
			qualifiedName = getPath(resolveBinding(declaringClass));
		} else {
			IMethodBinding declaringMethod = binding.getDeclaringMethod();
			if (declaringMethod != null) {
				qualifiedName = getPath(resolveBinding(declaringMethod));
			}
		}
		
		if (!qualifiedName.isEmpty()) {
	      qualifiedName = qualifiedName.concat("/");
	    } else {
	    	return convertBinding("unresolved", null, null);
	    }
		
		String scheme = "java+variable";
		if (binding.isEnumConstant()) {
	      scheme = "java+enumConstant";
	    } else if (binding.isParameter()) {
	      scheme = "java+parameter";
	    } else if (binding.isField()) {
	      scheme = "java+field";
	    }
		ISourceLocation result = convertBinding(scheme, null, qualifiedName.concat(binding.getName()));
		EclipseJavaCompiler.cache.put(binding.getKey(), result);
		return result;
	}
	
	protected ISourceLocation convertBinding(String scheme, String authority, String path) {
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
