/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Arnold Lankamp - interfaces and implementation
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/**
 * Specialized implementation for integer values that fall outside the 32-bit range.
 * 
 * @author Arnold Lankamp
 */
/*package*/ class BigIntegerValue extends AbstractNumberValue implements IInteger, ICanBecomeABigInteger{
	private final static Type INTEGER_TYPE = TypeFactory.getInstance().integerType();
	
	protected final BigInteger value;
	
	/*package*/ BigIntegerValue(BigInteger value){
		super();
		if(value.equals(BigInteger.ZERO))
			value = BigInteger.ZERO;
		if(value.equals(BigInteger.ONE))
			value = BigInteger.ONE;

		this.value = value;
	}
	
	@Override
	public IInteger toInteger() {
		return this;
	}

	@Override
	public Type getType(){
		return INTEGER_TYPE;
	}

	@Override
	public int intValue(){
		return value.intValue();
	}

	@Override
	public long longValue(){
		return value.longValue();
	}

	@Override
	public double doubleValue(){
		return value.doubleValue();
	}

	@Override
	public IReal toReal(int precision){
		// precision is ignored intentionally
		return BigDecimalValue.newReal(new BigDecimal(value));
	}

	@Override
	public IRational toRational(){
		return RationalValue.newRational(this, IntegerValue.INTEGER_ONE);
	}
	@Override
	public byte[] getTwosComplementRepresentation(){
		return value.toByteArray();
	}
	
	@Override
	public BigInteger toBigInteger(){
		return value;
	}
	
	@Override
	public IInteger add(IInteger other){
		BigInteger o = ((ICanBecomeABigInteger) other).toBigInteger();
		BigInteger result = value.add(o);
		if(result == value)
			return this;
		else if(result == o)
			return other;
		int length = result.bitLength();
		if(length <= 31){
			return IntegerValue.newInteger(result.intValue());
		}
		
		return IntegerValue.newInteger(result);
	}
	
	@Override
	public IReal add(IReal other) {
		return (IReal) other.add(this);
	}
	
	@Override
	public IRational add(IRational other) {
		return (IRational) other.add(this);
	}
	
	@Override
	public IInteger subtract(IInteger other){
		BigInteger result = value.subtract(((ICanBecomeABigInteger) other).toBigInteger());
		if(result == value)
			return this;
		
		int length = result.bitLength();
		if(length <= 31){
			return IntegerValue.newInteger(result.intValue());
		}
		
		return IntegerValue.newInteger(result);
	}
	
	@Override
	public INumber subtract(IReal other) {
		return toReal(other.precision()).subtract(other);
	}

	@Override
	public INumber subtract(IRational other) {
		return toRational().subtract(other);
	}
	
	@Override
	public IInteger multiply(IInteger other){
		BigInteger o = ((ICanBecomeABigInteger) other).toBigInteger();
		BigInteger result = value.multiply(o);
		if(result == value)
			return this;
		else if(result == o)
			return other;
		// The result of this operation can never fit in a 32-bit integer, so no need to check.
		return IntegerValue.newInteger(result);
	}
	
	@Override
	public IReal multiply(IReal other) {
		return (IReal) other.multiply(this);
	}
	
	@Override
	public IRational multiply(IRational other) {
		return (IRational) other.multiply(this);
	}
	@Override
	public IInteger divide(IInteger other){
		BigInteger result = value.divide(((ICanBecomeABigInteger) other).toBigInteger());
		if(result == value)
			return this;
		
		int length = result.bitLength();
		if(length <= 31){
			return IntegerValue.newInteger(result.intValue());
		}
		
		return IntegerValue.newInteger(result);
	}
	
	@Override
	public IRational divide(IRational other) {
		return toRational().divide(other);
	}

	@Override
	public INumber divide(IInteger other, int precision) {
		return toReal(precision).divide(other, precision);
	}
	
	@Override
	public INumber divide(IRational other, int precision) {
		return toReal(precision).divide(other, precision);
	}

	@Override
	public IReal divide(IReal other, int precision) {
		return toReal(precision).divide(other, precision);
	}
	
	@Override
	public IInteger mod(IInteger other){
		BigInteger result = value.mod(((ICanBecomeABigInteger) other).toBigInteger());
		
		if(other instanceof IntegerValue){
			int integerResult = result.intValue();
			return IntegerValue.newInteger(integerResult);
		}
		
		return IntegerValue.newInteger(result);
	}
	
	@Override
	public IInteger remainder(IInteger other){
		BigInteger result = value.remainder(((ICanBecomeABigInteger) other).toBigInteger());
		
		if(other instanceof IntegerValue){
			int integerResult = result.intValue();
			return IntegerValue.newInteger(integerResult);
		}
		
		return IntegerValue.newInteger(result);
	}
	
	@Override
	public IInteger negate(){
		return IntegerValue.newInteger(value.negate());
	}
	
	@Override
	public IBool equal(IInteger other){
    return BoolValue.getBoolValue(compare(other) == 0);
  }
  
  @Override
  public IBool equal(IReal other) {
    return other.equal(this);
  }
  
  @Override
  public IBool equal(IRational other) {
    return other.equal(this);
  }
  
	@Override
	public IBool greater(IInteger other){
		return BoolValue.getBoolValue(compare(other) > 0);
	}
	
	@Override
	public IBool greater(IReal other) {
		return other.less(this);
	}
	
	@Override
	public IBool greater(IRational other) {
		return other.less(this);
	}
	
	@Override
	public IBool greaterEqual(IInteger other){
		return BoolValue.getBoolValue(compare(other) >= 0);
	}

	@Override
	public IBool greaterEqual(IReal other) {
		return other.lessEqual(this);
	}
	
	@Override
	public IBool greaterEqual(IRational other) {
		return other.lessEqual(this);
	}
	
	@Override
	public IBool less(IInteger other){
		return BoolValue.getBoolValue(compare(other) < 0);
	}
	
	@Override
	public IBool less(IReal other) {
		return other.greater(this);
	}
	
	@Override
	public IBool less(IRational other) {
		return other.greater(this);
	}
	
	@Override
	public IBool lessEqual(IInteger other){
		return BoolValue.getBoolValue(compare(other) <= 0);
	}
	
	@Override
	public IBool lessEqual(IReal other) {
		return other.greaterEqual(this);
	}
	
	@Override
	public IBool lessEqual(IRational other) {
		return other.greaterEqual(this);
	}
	
	@Override
	public int compare(IInteger other){
		return value.compareTo(((ICanBecomeABigInteger) other).toBigInteger());
	}
	
	@Override
	public int compare(INumber other) {
		if (isIntegerType(other)) {
			return compare(other.toInteger());
		}
		else if (isRationalType(other)) {
			return toRational().compare(other);
		}
		else {
			assert other instanceof IReal;
			return toReal(((IReal) other).precision()).compare(other);
		}
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E{
		return v.visitInteger(this);
	}
	
	public int hashCode(){
		return value.hashCode();
	}
	
	public boolean equals(Object o){
		if(o == null) return false;
		else if(o == this) return true;
		
		if(o.getClass() == getClass()){
			BigIntegerValue otherInteger = (BigIntegerValue) o;
			return value.equals(otherInteger.value);
		}
		
		return false;
	}
	
	@Override
	public boolean isEqual(IValue other){
		return equals(other);
	}
	
	@Override
	public String getStringRepresentation(){
		return value.toString();
	}

	@Override
	public int signum() {
		return value.signum();
	}
	
	@Override
	public IInteger abs() {
		return IntegerValue.newInteger(value.abs());
	}

}
