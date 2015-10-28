/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.exceptions.UnexpectedTypeException;
import org.rascalmpl.value.impl.AbstractValue;
import org.rascalmpl.value.type.TypeFactory;

/*package*/ abstract class AbstractNumberValue extends AbstractValue implements INumber{
	private final static TypeFactory typeFactory = TypeFactory.getInstance();
	
	/*package*/ AbstractNumberValue(){
		super();
	}

	@Override
	public INumber add(INumber other){
		if(isIntegerType(other)){
			return add(other.toInteger());
		}
		if(isRealType(other)){
			return add(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return add(other.toRational());
		}
		
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}
	
	@Override
	public INumber divide(INumber other, int precision){
		if(isIntegerType(other)){
			return divide(other.toInteger(), precision);
		}
		if(isRealType(other)){
			return divide(other.toReal(precision), precision);
		}
		if(isRationalType(other)){
			return divide(other.toRational(), precision);
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}

	@Override
	public IBool greater(INumber other){
		if(isIntegerType(other)){
			return greater(other.toInteger());
		}
		if(isRealType(other)){
			return greater(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return greater(other.toRational());
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}
	

  @Override
  public IBool equal(INumber other){
    if(isIntegerType(other)){
      return equal(other.toInteger());
    }
    if(isRealType(other)){
      return equal(other.toReal(((IReal) other).precision()));
    }
    if(isRationalType(other)){
      return equal(other.toRational());
    }
    throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
  }
  
	@Override
	public IBool greaterEqual(INumber other){
		if(isIntegerType(other)){
			return greaterEqual(other.toInteger());
		}
		if(isRealType(other)){
			return greaterEqual(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return greaterEqual(other.toRational());
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}
	
	@Override
	public IBool less(INumber other){
		if(isIntegerType(other)){
			return less(other.toInteger());
		}
		if(isRealType(other)){
			return less(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return less(other.toRational());
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}
	
	@Override
	public IBool lessEqual(INumber other){
		if(isIntegerType(other)){
			return lessEqual(other.toInteger());
		}
		if(isRealType(other)){
			return lessEqual(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return lessEqual(other.toRational());
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}

	@Override
	public INumber multiply(INumber other){
		if(isIntegerType(other)){
			return multiply(other.toInteger());
		}
		if(isRealType(other)){
			return multiply(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return multiply(other.toRational());
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}

	@Override
	public INumber subtract(INumber other){
		if(isIntegerType(other)){
			return subtract(other.toInteger());
		}
		if(isRealType(other)){
			return subtract(other.toReal(((IReal) other).precision()));
		}
		if(isRationalType(other)){
			return subtract(other.toRational());
		}
		throw new UnexpectedTypeException(typeFactory.numberType(), other.getType());
	}

  protected boolean isRationalType(INumber other) {
    return other.getType().equivalent(TypeFactory.getInstance().rationalType());
  }

  protected boolean isRealType(INumber other) {
    return other.getType().equivalent(TypeFactory.getInstance().realType());
  }

  protected boolean isIntegerType(INumber other) {
    return other.getType().equivalent(TypeFactory.getInstance().integerType());
  }
}
