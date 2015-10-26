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
 *   * Jurgen Vinju - extensions and fixes
 *   * Davy Landman - added mathematical functions
 *   * Paul Klint - Precision handling
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.impl.util.BigDecimalCalculations;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/*
 * TODO: provide specializations for smaller values, similar to IntegerValue / BigIntegerValue.
 */
/*package*/ class BigDecimalValue extends AbstractNumberValue implements IReal {
	private final static Type DOUBLE_TYPE = TypeFactory.getInstance().realType();
	
	protected final BigDecimal value;

	/*package*/ static IReal newReal(BigDecimal value) {
		return new BigDecimalValue(value);
	}

	/*package*/ static IReal newReal(String value) {
		return new BigDecimalValue(new BigDecimal(value));
	}

	/*package*/ static IReal newReal(String value, int precision) throws NumberFormatException {
		return new BigDecimalValue(new BigDecimal(value, new MathContext(precision)));
	}

	/*package*/ static IReal newReal(double value) {
		checkNanAndInfinity(value);
		return new BigDecimalValue(BigDecimal.valueOf(value));
	}

	public static void checkNanAndInfinity(double value) {
		if (Double.isNaN(value)) {
			throw new NumberFormatException("no support for NaN");
		}
		if (Double.isInfinite(value)) {
			throw new NumberFormatException("no support for infinity");
		}
	}

	/*package*/ static IReal newReal(double value, int precision) {
		checkNanAndInfinity(value);
		return new BigDecimalValue(new BigDecimal(value, new MathContext(precision)));
	}

	private BigDecimalValue(BigDecimal value){
		super();
		
		this.value = value;
	}
	
	private BigDecimalValue(BigDecimal value, int precision){
		super();
		
		this.value = new BigDecimal(value.toEngineeringString(), new MathContext(precision));
	}

	@Override
	public IReal abs() {
		return newReal(value.abs());
	}
	
	@Override
	public IReal toReal(int precision) {
		return this;
	}
	
	@Override
	public Type getType(){
		return DOUBLE_TYPE;
	}
	
	@Override
	public float floatValue(){
		return value.floatValue();
	}
	
	@Override
	public double doubleValue(){
		return value.doubleValue();
	}
	
	@Override
	public IInteger toInteger(){
		return IntegerValue.newInteger(value.toBigInteger());
	}
	
	@Override
	public IRational toRational(){
    	throw new UnsupportedOperationException();
	}
	
	@Override
	public IReal floor(){
		return BigDecimalValue.newReal(value.setScale(0, RoundingMode.FLOOR));
	}
	
	@Override
	public IReal round(){
		return BigDecimalValue.newReal(value.setScale(0, RoundingMode.HALF_UP));
	}
	
	@Override
	public IReal add(IReal other){
		return BigDecimalValue.newReal(value.add(((BigDecimalValue) other).value));
	}
	
	@Override
	public INumber add(IInteger other) {
		return add(other.toReal(value.precision()));
	}
	
	@Override
	public INumber add(IRational other) {
		return add(other.toReal(value.precision()));
	}
	
	@Override
	public IReal subtract(IReal other){
		return BigDecimalValue.newReal(value.subtract(((BigDecimalValue) other).value));
	}
	
	@Override
	public INumber subtract(IInteger other) {
		return subtract(other.toReal(value.precision()));
	}
	
	@Override
	public INumber subtract(IRational other) {
		return subtract(other.toReal(value.precision()));
	}
	
	@Override
	public IReal multiply(IReal other){
		//int precision = Math.min(Math.max(value.precision(), other.precision()), BaseValueFactory.PRECISION);
		//MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
		return BigDecimalValue.newReal(value.multiply(((BigDecimalValue) other).value));
	}
	
	@Override
	public INumber multiply(IInteger other) {
		return multiply(other.toReal(value.precision()));
	}
	
	@Override
	public INumber multiply(IRational other) {
		return multiply(other.toReal(value.precision()));
	}
	
	@Override
	public IReal divide(IReal other, int precision){
		// make sure the precision is *at least* the same as that of the arguments
		precision = Math.max(Math.max(value.precision(), other.precision()), precision);
		MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
		return BigDecimalValue.newReal(value.divide(((BigDecimalValue) other).value, mc));
	}
	
	@Override
	public IReal divide(IInteger other, int precision) {
		return divide(other.toReal(precision), precision);
	}
	
	@Override
	public IReal divide(IRational other, int precision) {
		return divide(other.toReal(precision), precision);
	}
	
	@Override
	public IReal negate(){
		return BigDecimalValue.newReal(value.negate());
	}
	
	@Override
	public int precision(){
		return value.precision();
	}
	
	@Override
	public int scale(){
		return value.scale();
	}
	
	@Override
	public IInteger unscaled(){
		return IntegerValue.newInteger(value.unscaledValue());
	}
	
	@Override
	public IBool equal(IReal other){
	  return BoolValue.getBoolValue(compare(other) == 0);
	}

	@Override
	public IBool equal(IInteger other) {
	  return equal(other.toReal(value.precision()));
	}

	@Override
	public IBool equal(IRational other) {
	  return equal(other.toReal(value.precision()));
	}
	  
	@Override
	public IBool greater(IReal other){
		return BoolValue.getBoolValue(compare(other) > 0);
	}
	
	@Override
	public IBool greater(IInteger other) {
		return greater(other.toReal(value.precision()));
	}
	
	@Override
	public IBool greater(IRational other) {
		return greater(other.toReal(value.precision()));
	}
	
	@Override
	public IBool greaterEqual(IReal other){
		return BoolValue.getBoolValue(compare(other) >= 0);
	}
	
	@Override
	public IBool greaterEqual(IInteger other) {
		return greaterEqual(other.toReal(value.precision()));
	}
	
	@Override
	public IBool greaterEqual(IRational other) {
		return greaterEqual(other.toReal(value.precision()));
	}
	
	
	@Override
	public IBool less(IReal other){
		return BoolValue.getBoolValue(compare(other) < 0);
	}
	
	@Override
	public IBool less(IInteger other) {
		return less(other.toReal(value.precision()));
	}
	
	@Override
	public IBool less(IRational other) {
		return less(other.toReal(value.precision()));
	}
	
	@Override
	public IBool lessEqual(IReal other){
		return BoolValue.getBoolValue(compare(other) <= 0);
	}
	
	@Override
	public IBool lessEqual(IInteger other) {
		return lessEqual(other.toReal(value.precision()));
	}
	
	@Override
	public IBool lessEqual(IRational other) {
		return lessEqual(other.toReal(value.precision()));
	}
	
	@Override
	public int compare(IReal other){
		return value.compareTo(((BigDecimalValue) other).value);
	}
	
	@Override
	public int compare(INumber other) {
		return compare(other.toReal(value.precision()));
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitReal(this);
	}

	/*
	 * Description and implementation from the (now removed) reference implementation:
	 *
	 * // Java BigDecimals have a bug, their even though 3.0 and 3.00 are equal,
	 * // their hashCode() is not, which is against the equals/hashCode() contract.
	 * // To work around this, we use this simple trick here which is correct but
	 * // might lead to many collisions.
	 * // return Double.valueOf(value.doubleValue()).hashCode();
	 */
	public int hashCode(){
		// BigDecimals don't generate consistent hashcodes for things that are actually 'equal'.
		// This code rectifies this problem.
		long bits = Double.doubleToLongBits(value.doubleValue());
		return (int) (bits ^ (bits >>> 32));
	}
	
	public boolean equals(Object o){
		if(o == null) return false;
		
		if(o.getClass() == getClass()){
			BigDecimalValue otherDouble = (BigDecimalValue) o;
			return (value.equals(otherDouble.value));
		}
		
		return false;
	}
	
	@Override
	public boolean isEqual(IValue o){
		return equals(o);
	}
	
	@Override
	public String getStringRepresentation(){
		StringBuilder sb = new StringBuilder();
		String decimalString = value.toString();
		sb.append(decimalString);
		if (!decimalString.matches(".*[\\.Ee].*")) {
		  sb.append(".");
		}
		return sb.toString();
	}
	
	@Override
	public int signum() {
		return value.signum();
	}

	@Override
	public IReal log(IInteger base, int precision) {
		return log(base.toReal(precision), precision);
	}
	
	@Override
	public IReal log(IReal base, int precision) {
		IReal lnBase = base.ln(precision + 1);
		IReal lnThis = this.ln(precision + 1);
		return lnThis.divide(lnBase, precision);
	}

	@Override
	public IReal ln(int precision) {
		return newReal(BigDecimalCalculations.ln(value, precision));
	}

	@Override
	public IReal sqrt(int precision) {
		return newReal(BigDecimalCalculations.sqrt(value, precision));
	}

	@Override
	public IReal nroot(IInteger n, int precision) {
		return newReal(BigDecimalCalculations.intRoot(value, new BigInteger(n.getTwosComplementRepresentation()), precision));
	}
	
	@Override
	public IReal exp(int precision) {
		return newReal(BigDecimalCalculations.exp(value, precision));
	}

	@Override
	public IReal pow(IInteger power) {
		if (power.signum() < 0) {
			// negative power is 1/(this^-power)
			return newReal(
						BigDecimal.ONE.divide(value.pow(power.negate().intValue()), value.precision(), RoundingMode.HALF_EVEN)
				);
		}
		return newReal(value.pow(power.intValue()));
	}
	@Override
	public IReal pow(IReal power, int precision) {
		BigDecimal actualPower = null;
		if (power instanceof BigDecimalValue) {
			actualPower = ((BigDecimalValue)power).value;
		}
		else {
			actualPower = new BigDecimal(power.getStringRepresentation());
		}
		return newReal(BigDecimalCalculations.pow(value, actualPower, precision));
	}

	@Override
	public IReal tan(int precision) {
		return newReal(BigDecimalCalculations.tan(value, precision));
	}

	@Override
	public IReal sin(int precision) {
		return newReal(BigDecimalCalculations.sin(value, precision));
	}

	@Override
	public IReal cos(int precision) {
		return newReal(BigDecimalCalculations.cos(value, precision));
	}

	public static IReal pi(int precision) {
		if (precision < 0 || precision > 1000)
			throw new IllegalArgumentException("PI max precision is 1000");
		return newReal(BigDecimalCalculations.PI.setScale(precision, BigDecimal.ROUND_HALF_EVEN));
	}
	
	public static IReal e(int precision) {
		if (precision < 0 || precision > 1000)
			throw new IllegalArgumentException("E max precision is 1000");
		return newReal(BigDecimalCalculations.E.setScale(precision, BigDecimal.ROUND_HALF_EVEN));
	}	
}
