package org.meta_environment.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;


public class ProductionAdapter {
	private IConstructor tree;

	public ProductionAdapter(IConstructor tree) {
		if (tree.getType() != Factory.Production) {
			throw new ImplementationError("ProductionWrapper only wraps UPTR productions, not " + tree.getType());
		}
		this.tree = tree;
	}
	
	public String getConstructorName() {
		for (IValue attr : getAttributes()) {
			if (attr.getType().isAbstractDataType() && ((IConstructor) attr).getConstructorType() == Factory.Attr_Term) {
				IValue value = ((IConstructor)attr).get("term");
				if (value.getType().isNodeType() && ((INode) value).getName().equals("cons")) {
					return ((IString) ((INode) value).get(0)).getValue();
				}
			}
		}
		return null;
	}
	
	public SymbolAdapter getRhs() {
		return new SymbolAdapter((IConstructor) tree.get("rhs"));
	}
	
	public IList getLhs() {
		return (IList) tree.get("lhs");
	}
	
	public boolean isContextFree() {
		return getRhs().isCf();
	}
	
	public boolean isLayout() {
		return getRhs().isLayout();
	}
	
	public String getSortName() {
		SymbolAdapter rhs = getRhs();
		
		if (rhs.isCf() || rhs.isLex()) {
			rhs = rhs.getSymbol();
			if (rhs.isSort() || rhs.isParameterizedSort()) {
				return rhs.getName();
			}
		}
			
		throw new ImplementationError("Production does not have a sort name: " + tree);
	}
	
	public IList getAttributes() {
		if (isList()) {
			return (IList) Factory.Attrs.make(ValueFactoryFactory.getValueFactory());
		}
		IConstructor attributes = (IConstructor) tree.get("attributes");
		
		if (attributes.getConstructorType() == Factory.Attributes_Attrs) {
			return (IList) attributes.get("attrs");
		}
		
		return (IList) Factory.Attrs.make(ValueFactoryFactory.getValueFactory());
	}

	public boolean isLiteral() {
		return getRhs().isLiteral();
	}

	public boolean isCILiteral() {
		return getRhs().isCILiteral();
	}

	public boolean isList() {
		return tree.getConstructorType() == Factory.Production_List;
	}

	public boolean isSeparatedList() {
		SymbolAdapter rhsSym = getRhs();
		if (rhsSym.isLex() || rhsSym.isCf()) {
			rhsSym = rhsSym.getSymbol();
		}
		return rhsSym.isIterPlusSep() || rhsSym.isIterStarSep();
	}

	public boolean isLexical() {
		SymbolAdapter rhsSym = getRhs();
		if (rhsSym.isLex() || rhsSym.isCf()) {
			rhsSym = rhsSym.getSymbol();
		}
		return rhsSym.isLayout();
	}

	public boolean isLexToCf() {
		if (!isContextFree()) {
			return false;
		}
		if (isList()) {
			return false;
		}
		
		IList lhs = getLhs();
		if (lhs.length() != 1) {
			return false;
		}
		SymbolAdapter lhsSym = new SymbolAdapter((IConstructor)lhs.get(0));
		if (!lhsSym.isLex()) {
			return false;
		}
		SymbolAdapter rhsSym = getRhs();
		return lhsSym.getSymbol().equals(rhsSym.getSymbol());
	}

	public String getCategory() {
		if (!isList()) {
			for (IValue attr : getAttributes()) {
				if (attr.getType().isAbstractDataType() && ((IConstructor) attr).getConstructorType() == Factory.Attr_Term) {
					IValue value = ((IConstructor)attr).get("term");
					if (value.getType().isNodeType() && ((INode) value).getName().equals("category")) {
						return ((IString) ((INode) value).get(0)).getValue();
					}
				}
			}
		}
		return null;
	}


}
