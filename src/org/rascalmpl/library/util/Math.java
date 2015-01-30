/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.util;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Math {
	private final IValueFactory values;
	private final Random random;
	
	public Math(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}

	public IValue arbInt()
	//@doc{arbInt -- return an arbitrary integer value}
	{
	   return values.integer(random.nextInt());
	}

	public IValue arbInt(IInteger limit)
	//@doc{arbInt -- return an arbitrary integer value in the interval [0, limit).}
	{
		// TODO allow big ints
	   return values.integer(random.nextInt(limit.intValue()));
	}

	public IValue arbReal()
	//@doc{arbReal -- returns an arbitrary real value in the interval [0.0,1.0).}
	{
	  return values.real(random.nextDouble());
	}
	
	public void arbSeed(IInteger seed){
		random.setSeed(seed.intValue());
	}
	
	public IValue cos(INumber x){
		try {
			return x.toReal().cos(values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue denominator(IRational n)
	{
	  return n.denominator();
	}
	
	public IValue E()
	//@doc{e -- returns the constant E}
	{
		return values.e(values.getPrecision());
	}
	
	public IValue exp(INumber x){
		try {
			return x.toReal().exp(values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue ln(INumber x) {
		try {
			return x.toReal().ln(values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}

	public IValue log(INumber x, INumber base) {
		try {
			return x.toReal().log(base.toReal(), values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue numerator(IRational n)
	{
		return n.numerator();
	}

	public IValue nroot(INumber x, IInteger y){
		try {
			return x.toReal().nroot(y, values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue PI()
	//@doc{pi -- returns the constant PI}
	{
		return values.pi(values.getPrecision());
	}
	
	public IValue pow(INumber x, IInteger y){
		try {
			return x.toReal().pow(y);
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	public IValue pow(INumber x, IReal y){
		try {
			return x.toReal().pow(y, values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue precision(INumber x){
		if(x.getType().isInteger()){
			IInteger k = (IInteger) x;
			return values.integer(k.toReal().precision());
		}
		if(x.getType().isRational()){
			IRational k = (IRational) x;
			return values.integer(k.toReal().precision());
		}
		return values.integer(((IReal) x).precision());
	}
	
	public IValue unscaled(IReal x) {
	  return x.unscaled();
	}
	
	public IValue precision(INumber x, IInteger precision){
		return values.real(x.toString(), precision.intValue());
	}
	
	public IValue setPrecision(IInteger precision){
		return values.integer(values.setPrecision(precision.intValue()));
	}
	
	public IValue scale(INumber x){
		try {
			if(x.getType().isInteger()){
				IInteger k = (IInteger) x;
				return values.integer(k.toReal().scale());
			}
			if(x.getType().isRational()){
				IRational k = (IRational) x;
				return values.integer(k.toReal().scale());
			}
			return values.integer(((IReal) x).scale());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}

	public IValue remainder(IRational n)
	{
	  return n.remainder();
	}
	
	public IValue round(INumber d) {
		return d.toReal().round().toInteger();
	}
	
	public IValue sin(INumber x){
		try {
			return x.toReal().sin(values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue sqrt(INumber x){
		try {
			return x.toReal().sqrt(values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue tan(INumber x){
		try {
			return x.toReal().tan(values.getPrecision());
		} catch (ArithmeticException ae) {
			throw RuntimeExceptionFactory.arithmeticException(ae.getMessage(), null, null);
		}
	}
	
	public IValue toInt(INumber d)
	//@doc{toInteger -- convert a real to integer.}
	{
	  return d.toInteger();
	}
	
	public IValue toRat(IInteger numerator, IInteger denominator)
	//@doc{toRat -- convert two integers to a rat value.}
	{
	  return values.rational(numerator, denominator);
	}
	
	public IValue toReal(INumber n)
	//@doc{toReal -- convert a number value to a real value.}
	{
	  return n.toReal();
	}

	public IValue toString(INumber d)
	//@doc{toString -- convert a real to a string.}
	{
	  return values.string(d.toString());
	}
	
	public IValue toReal(IRational n)
	//@doc{toReal -- convert a rational value to a real value.}
	{
	  return n.toReal();
	}

	public IValue toInt(IRational n)
	//@doc{toReal -- convert a rational value to a integer value.}
	{
	  return n.toInteger();
	}
	
	public IValue toReal(IInteger n)
	//@doc{toReal -- convert a rational value to a integer value.}
	{
	  return values.real(n.intValue());
	}

	
}
