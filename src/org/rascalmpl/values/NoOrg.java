package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.IString;

public class NoOrg extends Atom {
	private static final OriginValueFactory vf = (OriginValueFactory) ValueFactoryFactory.getValueFactory();
	
	private final IString value;
	
	public NoOrg(String s) {
		value = vf.baseString(s);
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
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int indexOf(String str) {
		return getValue().indexOf(str);
	}

}
