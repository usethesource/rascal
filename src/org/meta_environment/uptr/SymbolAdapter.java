package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class SymbolAdapter {
	private IConstructor tree;

	public SymbolAdapter(IConstructor tree) {
		if (tree.getType().getAbstractDataType() != Factory.Symbol) {
			throw new FactTypeError("TreeWrapper will only wrap UPTR symbols, not " +  tree.getType());
		}
		this.tree = tree;
	}

	public boolean isCf() {
		return tree.getType() == Factory.Symbol_Cf;
	}

	public boolean isLex() {
		return tree.getType() == Factory.Symbol_Lex;
	}
	
	public boolean isSort() {
		return tree.getType() == Factory.Symbol_Sort;
	}

	public SymbolAdapter getSymbol() {
		if (isCf() || isLex()) {
			return new SymbolAdapter((IConstructor) tree.get("symbol"));
		}
		else {
			throw new FactTypeError("Symbol does not have a child named symbol: " + tree);
		}
	}
	
	public String getName() {
		if (isSort()) {
			return ((IString) tree.get("string")).getValue();
		}
		else if (isParameterizedSort()) {
			return ((IString) tree.get("sort")).getValue();
		}
		else {
			throw new FactTypeError("Symbol does not have a child named \"name\": " + tree);
		}
	}

	public boolean isParameterizedSort() {
		return tree.getType() == Factory.Symbol_ParameterizedSort;
	}
	
	public boolean isLiteral() {
		return tree.getType() == Factory.Symbol_Lit;
	}

	public boolean isCILiteral() {
		return tree.getType() == Factory.Symbol_CiLit;
	}

	public boolean isIterPlusSep() {
		return tree.getType() == Factory.Symbol_IterPlusSep;
	}
	
	public boolean isIterStarSep() {
		return tree.getType() == Factory.Symbol_IterStarSep;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SymbolAdapter)) {
			return false;
		}
		return tree.equals(((SymbolAdapter)obj).tree);
	}
}
