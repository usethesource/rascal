package org.meta_environment.rascal.interpreter.types;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.utils.Symbols;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ProductionAdapter;
import org.meta_environment.uptr.SymbolAdapter;
import org.meta_environment.uptr.TreeAdapter;

/**
 * This is an "extension" of the PDB's type system with a special kind of type
 * that implements the mapping between SDF's sort names and Rascal types. These
 * types should never escape into the PDB, that would break a lot...
 */
public class ConcreteSyntaxType extends Type {
	private IConstructor symbol;

	public ConcreteSyntaxType(IConstructor cons) {
		if (cons.getType() == Factory.Symbol) {
			this.symbol = cons;
		}
		else if (cons.getType() == Factory.Production) {
			this.symbol = new ProductionAdapter(cons).getRhs().getTree();
		}
		else if (cons.getConstructorType() == Factory.Tree_Appl) {
			this.symbol = new TreeAdapter(cons).getProduction().getRhs().getTree();
		}
		else if (cons.getConstructorType() == Factory.Tree_Amb) {
			IConstructor first = (IConstructor) new TreeAdapter(cons).getAlternatives().iterator().next();
			this.symbol = new TreeAdapter(first).getProduction().getRhs().getTree();
		}
		else {
			throw new ImplementationError("Invalid concrete syntax type constructor");
		}
	}
	
	public ConcreteSyntaxType(org.meta_environment.rascal.ast.Type type) {
		this((IConstructor) Symbols.typeToSymbol(type));
	}
	
	public IConstructor getSymbol() {
		return symbol;
	}
	
	@Override
	public boolean isAbstractDataType() {
		return true;
	}
	
	@Override
	public boolean isNodeType() {
		return true;
	}
	
	public boolean isConcreteListType() {
		return new SymbolAdapter(getSymbol()).isAnyList();
	}
	
	@Override
	public String getName() {
		return Factory.Tree.getName();
	}
	
	@Override
	public Type getTypeParameters() {
		return Factory.Tree.getTypeParameters();
	}
	
	@Override
	public <T> T accept(ITypeVisitor<T> visitor) {
		return visitor.visitAbstractData(Factory.Tree);
	}
	
	@Override
	public boolean isSubtypeOf(Type other) {
		
		if (other.equals(this)) {
			return true;
		}
		
		if (other == Factory.Tree) {
			return true;
		}

		if (other instanceof ConcreteSyntaxType) {
			SymbolAdapter sym = new SymbolAdapter(symbol);
			SymbolAdapter otherSym = new SymbolAdapter(((ConcreteSyntaxType)other).symbol);
			if (sym.isPlusList() && otherSym.isStarList()) {
				return true; // TODO add check if they have the same element type
			}
		}
		
		
		return super.isSubtypeOf(other);
	}
	
	public boolean isConcreteCFList() {
		SymbolAdapter sym = new SymbolAdapter(symbol); 
		return sym.isCf() && (sym.isPlusList() || sym.isStarList());
	}
	
	public boolean isCompatibleWith(ProductionAdapter prod) {
		return prod.getRhs().equals(symbol);
	}
	
	public boolean isCompatibleWith(TreeAdapter tree) {
		if (tree.isAppl()) {
			return isCompatibleWith(tree.getProduction());
		}
		else if (tree.isAmb()) {
			IValue first = tree.getAlternatives().iterator().next();
			return isCompatibleWith(new TreeAdapter((IConstructor) first));
		}
		return false;
	}
	
	@Override
	public Type lub(Type other) {
		if (other.equals(this)) {
			return this;
		}
		else if (other.isSubtypeOf(Factory.Tree)) {
			return Factory.Tree;
		}
		return super.lub(other);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			ConcreteSyntaxType other = (ConcreteSyntaxType) obj;
			return symbol.equals(other.symbol);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return symbol.toString();
	}

	public ConcreteSyntaxType getConcreteCFListElementType() {
		SymbolAdapter sym = new SymbolAdapter(symbol);
		// We assume it is cfListType
		// cf(iter...(SYM)) -> SYM
		return new ConcreteSyntaxType(sym.getSymbol().getSymbol().getTree());
	}
}
