package org.meta_environment.rascal.interpreter.env;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.Symbols;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.ProductionAdapter;
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
		else if (cons.getConstructorType() == Factory.Tree_Appl) {
			this.symbol = new TreeAdapter(cons).getProduction().getRhs().getTree();
		}
		else if (cons.getConstructorType() == Factory.Tree_Amb) {
			IConstructor first = (IConstructor) new TreeAdapter(cons).getAlternatives().iterator().next();
			this.symbol = new TreeAdapter(first).getProduction().getRhs().getTree();
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
		else if (other == Factory.Tree) {
			return true;
		}
		
		return super.isSubtypeOf(other);
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
}
