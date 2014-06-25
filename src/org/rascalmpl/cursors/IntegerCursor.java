package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;

public class IntegerCursor extends Cursor implements IInteger {
	
	public IntegerCursor(IValue value) {
		super(value);
	}
	
	public IntegerCursor(IValue value, Context ctx) {
		super(value, ctx);
	}

	private IInteger getInt() {
		return ((IInteger)getValue());
	}
	
	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return getValue().accept(v);
	}

	@Override
	public INumber add(INumber other) {
		return new IntegerCursor(getInt().add(other), getCtx());
	}

	@Override
	public IReal add(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber add(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber subtract(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber subtract(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber subtract(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber multiply(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReal multiply(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber multiply(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber divide(INumber other, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReal divide(IReal other, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber divide(IInteger other, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INumber divide(IRational other, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger toInteger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRational toRational() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool equal(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool equal(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool equal(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool equal(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool less(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool less(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool less(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greater(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greater(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greater(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool lessEqual(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool lessEqual(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool lessEqual(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greaterEqual(INumber other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greaterEqual(IReal other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greaterEqual(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compare(INumber other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IInteger add(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger subtract(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger multiply(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger divide(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRational divide(IRational other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger remainder(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger negate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInteger mod(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReal toReal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool less(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greater(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool lessEqual(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBool greaterEqual(IInteger other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getTwosComplementRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int compare(IInteger other) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int signum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IInteger abs() {
		// TODO Auto-generated method stub
		return null;
	}

}
