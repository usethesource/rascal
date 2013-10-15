package org.eclipse.imp.pdb.facts.impl.primitive;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;

public class NoOrg extends Atom {
	private final IString value;
	
	public NoOrg(String s) {
		value = StringValue.newString(s);
	}
	
	private NoOrg(IString v) {
		value = v;
	}
	
	@Override
	public String getValue() {
		return value.getValue();
	}

	@Override
	public IString reverse() {
		return new NoOrg(value.reverse());
	}

	@Override
	public int length() {
		return value.length();
	}

	@Override
	public IString substring(int start, int end) {
		return new NoOrg(value.substring(start, end));
	}

	@Override
	public int compare(IString other) {
		return value.compare(other);
	}

	@Override
	public int charAt(int index) {
		return value.charAt(index);
	}

	@Override
	public IString replace(int first, int second, int end, IString repl) {
		return new NoOrg(value.replace(first, second, end, repl));
	}

	@Override
	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

}
