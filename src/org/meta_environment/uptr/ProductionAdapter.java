package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;

public class ProductionAdapter {
	private ITree tree;

	public ProductionAdapter(ITree tree) {
		if (tree.getType() != Factory.Production) {
			throw new FactTypeError("ProductionWrapper only wraps UPTR productions, not " + tree.getType());
		}
		this.tree = tree;
	}
	
	public String getConstructorName() {
		for (IValue attr : getAttributes()) {
			if (attr.getType().isTreeSortType() && ((ITree) attr).getTreeNodeType() == Factory.Attr_Term) {
				IValue value = ((ITree)attr).get("value");
				if (value.getType().isTreeSortType() && ((ITree) value).getTreeNodeType() == Factory.Constructor_Name) {
					return ((IString) ((ITree) value).get("name")).getValue();
				}
			}
		}
		throw new FactTypeError("Production does not have constructor name: " + this.tree);
	}
	
	public SymbolAdapter getRhs() {
		return new SymbolAdapter((ITree) tree.get("rhs"));
	}
	
	public IList getLhs() {
		return (IList) tree.get("lhs");
	}
	
	public boolean isContextFree() {
		return getRhs().isCf();
	}
	
	
	
	public String getSortName() {
		SymbolAdapter rhs = getRhs();
		
		if (rhs.isCf() || rhs.isLex()) {
			rhs = rhs.getSymbol();
			if (rhs.isSort()) {
				return rhs.getName();
			}
		}
			
		throw new FactTypeError("Production does not have a sort name: " + tree);
	}
	
	public IList getAttributes() {
		ITree attributes = (ITree) tree.get("attributes");
		
		if (attributes.getTreeNodeType() == Factory.Attributes_Attrs) {
			return (IList) attributes.get("attrs");
		}
		else {
			return (IList) Factory.Attrs.make(ValueFactory.getInstance());
		}
	}

	public boolean isLiteral() {
		return getRhs().isLiteral();
	}

	public boolean isCILiteral() {
		return getRhs().isCILiteral();
	}

	public boolean isList() {
		return tree.getTreeNodeType() == Factory.Production_List;
	}

	public boolean isSeparatedList() {
		SymbolAdapter rhsSym = getRhs();
		if (rhsSym.isLex() || rhsSym.isCf()) {
			rhsSym = rhsSym.getSymbol();
		}
		return rhsSym.isIterPlusSep() || rhsSym.isIterStarSep();
	}

	public boolean isLexical() {
		return getRhs().isLex();
	}

	public boolean isLexToCf() {
		if (!isContextFree()) {
			return false;
		}
		IList lhs = getLhs();
		if (lhs.length() != 1) {
			return false;
		}
		SymbolAdapter lhsSym = new SymbolAdapter((ITree)lhs.get(0));
		if (!lhsSym.isLex()) {
			return false;
		}
		SymbolAdapter rhsSym = getRhs();
		return lhsSym.getSymbol().equals(rhsSym.getSymbol());
	}

}
