package org.rascalmpl.value.impl.reference;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.exceptions.UnexpectedChildTypeException;
import org.rascalmpl.value.impl.AbstractDefaultAnnotatable;
import org.rascalmpl.value.impl.AbstractDefaultWithKeywordParameters;
import org.rascalmpl.value.impl.AnnotatedConstructorFacade;
import org.rascalmpl.value.impl.ConstructorWithKeywordParametersFacade;
import org.rascalmpl.value.impl.func.NodeFunctions;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.AbstractSpecialisedImmutableMap;
import io.usethesource.capsule.ImmutableMap;

/**
 * Implementation of a typed tree node with access to children via labels
 */
public class Constructor extends Node implements IConstructor {
	/*package*/ Constructor(Type type, IValue[] children) {
		super(type.getName(), type, children);
		
	}
	
	/*package*/ Constructor(Type type) {
		this(type, new IValue[0]);
	}
	
	/*package*/ Constructor(Type type, IValue[] children, Map<String,IValue> kwParams) {
    this(type, getAllChildren(type, children, kwParams));
  }

	private static IValue[] getAllChildren(Type type, IValue[] children, Map<String, IValue> kwParams) {
	  IValue[] allChildren = new IValue[children.length + kwParams.size()];
    System.arraycopy(children, 0, allChildren, 0, children.length);
    
    for (Entry<String,IValue> entry : kwParams.entrySet()) {
      allChildren[type.getFieldIndex(entry.getKey())] = entry.getValue();
    }
    
    return allChildren;
  }

  private Constructor(Constructor other, int childIndex, IValue newChild) {
		super(other, childIndex, newChild);
	}

	@Override
	public Type getType() {
		return getConstructorType().getAbstractDataType();
	}
	
	@Override
	public Type getUninstantiatedConstructorType() {
	  return fType;
	}
	
	public Type getConstructorType() {
	  if (fType.getAbstractDataType().isParameterized()) {
      assert fType.getAbstractDataType().isOpen();
    
      // this assures we always have the most concrete type for constructors.
      Type[] actualTypes = new Type[fChildren.length];
      for (int i = 0; i < fChildren.length; i++) {
        actualTypes[i] = fChildren[i].getType();
      }
    
      Map<Type,Type> bindings = new HashMap<Type,Type>();
      fType.getFieldTypes().match(TypeFactory.getInstance().tupleType(actualTypes), bindings);
      
      for (Type field : fType.getAbstractDataType().getTypeParameters()) {
        if (!bindings.containsKey(field)) {
          bindings.put(field, TypeFactory.getInstance().voidType());
        }
      }
      
      return fType.instantiate(bindings);
    }
    
    return fType;
	} 

	public IValue get(String label) {
		return super.get(fType.getFieldIndex(label));
	}

	public Type getChildrenTypes() {
		return fType.getFieldTypes();
	}

	@Override
	public IConstructor set(int i, IValue newChild) throws IndexOutOfBoundsException {
		checkChildType(i, newChild);
		return new Constructor(this, i, newChild);
	}

	
	public IConstructor set(String label, IValue newChild) throws FactTypeUseException {
		int childIndex = fType.getFieldIndex(label);
		checkChildType(childIndex, newChild);
		return new Constructor(this, childIndex, newChild);
	}
	
	private void checkChildType(int i, IValue newChild) {
		Type type = newChild.getType();
		Type expectedType = getConstructorType().getFieldType(i);
		if (!type.isSubtypeOf(expectedType)) {
			throw new UnexpectedChildTypeException(expectedType, type);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		else if(obj == null) {
			return false;
		}
		else if (getClass() == obj.getClass()) {
			Constructor other = (Constructor) obj;
			return fType.comparable(other.fType) && super.equals(obj);
		}
		return false;
	}
	
	@Override
	public boolean isEqual(IValue value) {
	  return NodeFunctions.isEqual(getValueFactory(), this, value);
	}

	@Override
	public int hashCode() {
		 return 17 + ~super.hashCode();
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		return v.visitConstructor(this);
	}
	
	public boolean declaresAnnotation(TypeStore store, String label) {
		return store.getAnnotationType(getType(), label) != null;
	}

	public boolean has(String label) {
		return getConstructorType().hasField(label);
	}
	
	/**
	 * TODO: Create and move to {@link AbstractConstructor}.
	 */
	@Override
	public boolean isAnnotatable() {
		return true;
	}
	
	/**
	 * TODO: Create and move to {@link AbstractConstructor}.
	 */
	@Override
	public IAnnotatable<? extends IConstructor> asAnnotatable() {
		return new AbstractDefaultAnnotatable<IConstructor>(this) {
			@Override
			protected IConstructor wrap(IConstructor content,
					ImmutableMap<String, IValue> annotations) {
				return new AnnotatedConstructorFacade(content, annotations);
			}
		};
	}
	
	@Override
	public boolean mayHaveKeywordParameters() {
	  return true;
	}
	
	@Override
	public IWithKeywordParameters<IConstructor> asWithKeywordParameters() {
	  return new AbstractDefaultWithKeywordParameters<IConstructor>(this, AbstractSpecialisedImmutableMap.<String,IValue>mapOf()) {
		  @Override
		  protected IConstructor wrap(IConstructor content, ImmutableMap<String, IValue> parameters) {
			  return new ConstructorWithKeywordParametersFacade(content, parameters);
		  }

		  @Override
		  public boolean hasParameters() {
			  return parameters != null && parameters.size() > 0;
		  }

		  @Override
		  public java.util.Set<String> getParameterNames() {
			  return parameters.keySet();
		  }

		  @Override
		  public Map<String, IValue> getParameters() {
			  return Collections.unmodifiableMap(parameters);
		  }
    };
	}
}
