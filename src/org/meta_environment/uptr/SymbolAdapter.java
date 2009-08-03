package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;


public class SymbolAdapter {
	private IConstructor tree;

	public SymbolAdapter(IConstructor tree) {
		if (tree.getType() != Factory.Symbol) {
			throw new ImplementationError("TreeWrapper will only wrap UPTR symbols, not " +  tree.getType());
		}
		this.tree = tree;
	}

	public boolean isCf() {
		return tree.getConstructorType() == Factory.Symbol_Cf;
	}

	public boolean isLex() {
		return tree.getConstructorType() == Factory.Symbol_Lex;
	}
	
	public boolean isSort() {
		return tree.getConstructorType() == Factory.Symbol_Sort;
	}

	public SymbolAdapter getSymbol() {
		if (isCf() || isLex() || isOpt() || isIterPlus() || isIterPlusSep() || isIterStar() || isIterStarSep()) {
			return new SymbolAdapter((IConstructor) tree.get("symbol"));
		}
		
		throw new ImplementationError("Symbol does not have a child named symbol: " + tree);
	}
	
	public SymbolAdapter getSeparator() {
		if (isIterPlusSep() || isIterStarSep()) {
			return new SymbolAdapter((IConstructor) tree.get("separator"));
		}
		
		throw new ImplementationError("Symbol does not have a child named separator: " + tree);
	}
	
	public String getName() {
		if (isSort()) {
			return ((IString) tree.get("string")).getValue();
		}
		else if (isParameterizedSort()) {
			return ((IString) tree.get("sort")).getValue();
		}
		else {
			throw new ImplementationError("Symbol does not have a child named \"name\": " + tree);
		}
	}

	public boolean isParameterizedSort() {
		return tree.getConstructorType() == Factory.Symbol_ParameterizedSort;
	}
	
	public boolean isLiteral() {
		return tree.getConstructorType() == Factory.Symbol_Lit;
	}

	public boolean isCILiteral() {
		return tree.getConstructorType() == Factory.Symbol_CiLit;
	}

	public boolean isIterPlusSep() {
		return tree.getConstructorType() == Factory.Symbol_IterPlusSep;
	}
	
	public boolean isIterStar() {
		return tree.getConstructorType() == Factory.Symbol_IterStar;
	}
	
	public boolean isIterPlus() {
		return tree.getConstructorType() == Factory.Symbol_IterPlus;
	}
	
	public boolean isLayout() {
		return tree.getConstructorType() == Factory.Symbol_Layout;
	}
	
	public boolean isStarList() {
		SymbolAdapter sym = this;
		
		if (sym.isCf() || sym.isLex()) {
			sym = sym.getSymbol();
		}
		
		return sym.isIterStar() || sym.isIterStarSep();
	}
	
	public boolean isPlusList() {
		SymbolAdapter sym = this;
		
		if (sym.isCf() || sym.isLex()) {
			sym = sym.getSymbol();
		}
		
		return sym.isIterPlus() || sym.isIterPlusSep();
	}
	
	public boolean isAnyList() {
		return isStarList() || isPlusList();
	}
	
	public boolean isCfOptLayout() {
		if (isCf()) {
			SymbolAdapter sym = getSymbol();
			if (sym.isOpt()) {
				sym = sym.getSymbol();
				if (sym.isLayout()) {
					return true;
				}
			}
		}
		
		return false;
	}
	private boolean isOpt() {
		return tree.getConstructorType() == Factory.Symbol_Opt;
	}

	public boolean isIterStarSep() {
		return tree.getConstructorType() == Factory.Symbol_IterStarSep;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SymbolAdapter)) {
			return false;
		}
		return tree.equals(((SymbolAdapter)obj).tree);
	}

	public IConstructor getTree() {
		return tree;
	}

	public IConstructor setSymbol(IConstructor sym) {
		if (isCf() || isLex() || isOpt() || isIterPlus() || isIterPlusSep() || isIterStar() || isIterStarSep()) {
			return ((IConstructor) tree.set("symbol", sym));
		}
		
		throw new ImplementationError("Symbol does not have a child named symbol: " + tree);
	}

}
