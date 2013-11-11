package org.rascalmpl.values;

import org.apache.commons.lang.WordUtils;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;

public class Insincere extends Atom {
	private final ISet origins;

	public Insincere(String s, ISet origins) {
		super(s);
		this.origins = origins;
	}

	public Insincere(IString s, ISet origins) {
		super(s);
		this.origins = origins;
	}

	public ISet getOrigins() {
		return origins;
	}
	
	@Override
	public IString reverse() {
		return new Insincere(value.reverse(), origins);
	}

	@Override
	public IString substring(int start, int end) {
		return new Insincere(value.substring(start, end), origins);
	}

	@Override
	public IString replace(int first, int second, int end, IString repl) {
		return new Insincere(value.replace(first, second, end, repl), origins);
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public OrgString capitalize() {
		return new Insincere(WordUtils.capitalize(getValue()), origins);
	}

}
