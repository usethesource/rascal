package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.IString;

public class NoOrg extends Atom {
	
	public NoOrg(String s) {
		super(s);
	}

	public NoOrg(IString s) {
		super(s);
	}

	@Override
	public IString reverse() {
		return new NoOrg(value.reverse());
	}

	@Override
	public IString substring(int start, int end) {
		return new NoOrg(value.substring(start, end));
	}

	@Override
	public IString replace(int first, int second, int end, IString repl) {
		return new NoOrg(value.replace(first, second, end, repl));
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

}
