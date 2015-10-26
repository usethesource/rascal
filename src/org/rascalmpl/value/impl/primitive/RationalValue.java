/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Anya Helene Bagge - initial implementation
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.primitive;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

/*package*/ class RationalValue extends AbstractNumberValue implements IRational {
	public static final Type RATIONAL_TYPE = TypeFactory.getInstance().rationalType();

	protected final IInteger num;
	protected final IInteger denom;

	/*package*/ static IRational newRational(IInteger a, IInteger b) {
		return new RationalValue(a, b);
	}

	private RationalValue(IInteger num, IInteger denom) {
		if(denom.signum() < 0) {
			num = num.negate();
			denom = denom.negate();
		}
		// normalize infinites
		if(denom.signum() == 0) {
			if(num.signum() > 0)
				num = intOne();
			else if(num.signum() < 0)
				num = intOne().negate();
			else
				throw new ArithmeticException("Illegal fraction 0/0");

		}
		else if(num.signum() == 0) {
			denom = intOne();
		}
		else {
			IInteger gcd = gcd(num, denom);
			while(gcd.compare(intOne()) != 0) {
				num = num.divide(gcd);
				denom = denom.divide(gcd);
				gcd = gcd(num, denom);
			}
		}
		this.num = num;
		this.denom = denom;
	}

	@Override
	public IRational add(IRational other) {
		// (num*other.denom + denom*other.num) / denom*other.denom
		return toRational(
				num.multiply(other.denominator()).add(denom.multiply(other.numerator())),
				denom.multiply(other.denominator()));
	}

	@Override
	public IReal add(IReal other) {
		return toReal(other.precision()).add(other);
	}

	@Override
	public INumber add(IInteger other) {
		return toRational(num.add(other.multiply(denom)), denom);
	}

	@Override
	public IRational subtract(IRational other) {
		// (num*other.denom - denom*other.num) / denom*other.denom
		return toRational(
				num.multiply(other.denominator()).subtract(denom.multiply(other.numerator())),
				denom.multiply(other.denominator()));
	}

	@Override
	public INumber subtract(IReal other) {
		return toReal(other.precision()).subtract(other);
	}

	@Override
	public INumber subtract(IInteger other) {
		return toRational(num.subtract(other.multiply(denom)), denom);
	}

	@Override
	public IRational multiply(IRational other) {
		return toRational(num.multiply(other.numerator()),
				denom.multiply(other.denominator()));
	}

	@Override
	public IReal multiply(IReal other) {
		return toReal(other.precision()).multiply(other);
	}

	@Override
	public INumber multiply(IInteger other) {
		return toRational(num.multiply(other), denom);
	}

	// TODO: should we perhaps drop this and only have the other divide?
	// or vice-versa?
	@Override
	public IRational divide(IRational other) {
		return toRational(num.multiply(other.denominator()),
				denom.multiply(other.numerator()));
	}

	@Override
	public IReal divide(IReal other, int precision) {
		return toReal(precision).divide(other, precision);
	}

	@Override
	public IRational divide(IInteger other, int precision) {
		return divide(other); // forget precision
	}

	@Override
	public IRational divide(IInteger other) {
		return toRational(num, denom.multiply(other));
	}


	@Override
	public INumber divide(IRational other, int precision) {
		return toRational(num.multiply(other.denominator()),
				denom.multiply(other.numerator()));
	}

	@Override
	public IBool less(IRational other) {
		return BoolValue.getBoolValue(compare(other) < 0);
	}

	@Override
	public IBool less(IReal other) {
		return other.greater(this);
	}

	@Override
	public IBool less(IInteger other) {
		return less(other.toRational());
	}

	@Override
	public IBool greater(IRational other) {
		return BoolValue.getBoolValue(compare(other) > 0);
	}

	@Override
	public IBool greater(IReal other) {
		return other.less(this);
	}

	@Override
	public IBool greater(IInteger other) {
		return greater(other.toRational());
	}

	@Override
	public IBool equal(IRational other) {
		return BoolValue.getBoolValue(compare(other) == 0);
	}

	@Override
	public IBool equal(IReal other) {
		return other.equal(this);
	}

	@Override
	public IBool equal(IInteger other) {
		return equal(other.toRational());
	}

	@Override
	public IBool lessEqual(IRational other) {
		return BoolValue.getBoolValue(compare(other) <= 0);
	}

	@Override
	public IBool lessEqual(IReal other) {
		return other.greaterEqual(this);
	}

	@Override
	public IBool lessEqual(IInteger other) {
		return lessEqual(other.toRational());
	}

	@Override
	public IBool greaterEqual(IRational other) {
		return BoolValue.getBoolValue(compare(other) >= 0);
	}

	@Override
	public IBool greaterEqual(IReal other) {
		return other.lessEqual(this);
	}

	@Override
	public IBool greaterEqual(IInteger other) {
		return greaterEqual(other.toRational());
	}

	@Override
	public boolean isEqual(IValue other) {
		return equals(other);
	}

	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;

		if(o.getClass() == getClass()){
			RationalValue other = (RationalValue) o;
			return num.equals(other.num) && denom.equals(other.denom);
		}

		return false;
	}

	@Override
	public int compare(INumber other) {
		if(isIntegerType(other)) {
			IInteger div = num.divide(denom);
			IInteger rem = num.remainder(denom);
			if(div.compare(other) != 0)
				return div.compare(other);
			else
				return rem.signum();
		}
		else if(isRationalType(other)){
			IRational diff = subtract((IRational)other);
			return diff.signum();
		}
		else {
			assert other instanceof IReal;
			return toReal(((IReal) other).precision()).compare(other);
		}
	}

	@Override
	public Type getType() {
		return RATIONAL_TYPE;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		return v.visitRational(this);
	}

	@Override
	public IRational negate() {
		return toRational(num.negate(), denom);
	}

	@Override
	public IReal toReal(int precision) {
		IReal r1 = num.toReal(precision);
		IReal r2 = denom.toReal(precision);
		r1 = r1.divide(r2, precision);
		return r1;
	}

	@Override
	public IInteger toInteger() {
		return num.divide(denom);
	}

	@Override
	public String getStringRepresentation() {
		return num.getStringRepresentation() + "r" + (denom.equals(intOne()) ? "" : denom.getStringRepresentation());
	}

	@Override
	public int compare(IRational other) {
		IRational diff = subtract(other);
		return diff.signum();
	}

	@Override
	public int signum() {
		return num.signum();
	}

	@Override
	public IRational abs() {
		return toRational(num.abs(), denom);
	}

	@Override
	public IInteger floor() {
		return num.divide(denom);
	}

	@Override
	public IInteger round() {
		return toReal(2).round().toInteger();
	}

	@Override
	public IRational toRational() {
		return this;
	}

	public IRational toRational(IInteger n, IInteger d) {
		return newRational(n, d);
	}

	@Override
	public IRational remainder(IRational other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		if(denom.equals(intOne()))
			return num.hashCode();
		else {
			final int prime = 31;
			int result = 1;
			result = prime * result + num.hashCode();
			result = prime * result + denom.hashCode();
			return result;
		}
	}

	@Override
	public IInteger numerator() {
		return num;
	}

	@Override
	public IInteger denominator() {
		return denom;
	}

	@Override
	public IInteger remainder() {
		return num.remainder(denom);
	}

	protected IInteger gcd(IInteger n, IInteger d) {
		n = n.abs();
		d = d.abs();
		while(d.signum() > 0) {
			IInteger tmp = d;
			d = n.mod(d);
			n = tmp;
		}
		return n;
	}
	protected IInteger intOne() {
		return IntegerValue.INTEGER_ONE;
	}

	@Override
	public double doubleValue() {
		return num.doubleValue() / denom.doubleValue();
	}
}
