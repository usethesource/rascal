/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.parsetrees;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class ProductionAdapter {
	
	private ProductionAdapter() {
		super();
	}

	/**
	 * @return a constructor name if present or null otherwise
	 */
	public static String getConstructorName(IConstructor tree) {
		IConstructor def = getDefined(tree);
		
		if (SymbolAdapter.isLabel(def)) {
			return SymbolAdapter.getLabel(def);
		}
		
		return null;
	}
	
	public static IConstructor getType(IConstructor tree) {
		return SymbolAdapter.delabel(getDefined(tree));
	}
	
	public static IConstructor getDefined(IConstructor tree) {
		return (IConstructor) tree.get(0);
	}
	
	public static IConstructor setDefined(IConstructor tree, IConstructor sym) {
    return (IConstructor) tree.set(0 /*def */, sym);
  }
	
	public static IList getSymbols(IConstructor tree) {
		if (isDefault(tree)) {
			return (IList) tree.get(1 /*symbols */);
		}
		return null;
	}
	
	public static IList getASTSymbols(IConstructor tree) {
		if (isLexical(tree)) {
			throw new ImplementationError("This is not a context-free production: " + tree);
		}

		IList children = getSymbols(tree);
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();

		for (int i = 0; i < children.length(); i+=2 /* to skip the layout */) {
			IConstructor kid = (IConstructor) children.get(i);
			if (!SymbolAdapter.isLiteral(kid) && !SymbolAdapter.isCILiteral(kid)) {
				writer.append(kid);
			}
		}
		
		return writer.done();
	}
	
	public static boolean isContextFree(IConstructor tree) {
		IConstructor t = getType(tree);
		return SymbolAdapter.isSort(t) || SymbolAdapter.isParameterizedSort(t);
	}
	
	public static boolean isLayout(IConstructor tree) {
		return SymbolAdapter.isLayouts(getType(tree));
	}
	
	public static String getSortName(IConstructor tree) {
		IConstructor rhs = getType(tree);
		
		if (SymbolAdapter.isSort(rhs) 
				|| SymbolAdapter.isLex(rhs) 
				|| SymbolAdapter.isLayouts(rhs) 
				|| SymbolAdapter.isParameterizedSort(rhs)
				|| SymbolAdapter.isKeyword(rhs)) {
			return SymbolAdapter.getName(rhs);
		} 
		
		return "";
	}
	
	public static ISet getAttributes(IConstructor tree) {
		if (isDefault(tree)) {
			return (ISet) tree.get(2 /* "attributes" */);
		}
		
		return ValueFactoryFactory.getValueFactory().set();
	}

	public static boolean isLiteral(IConstructor tree) {
		return SymbolAdapter.isLiteral(getType(tree));
	}

	public static boolean isCILiteral(IConstructor tree) {
		return SymbolAdapter.isCILiteral(getType(tree));
	}

	public static boolean isList(IConstructor tree) {
		return tree.getConstructorType() == RascalValueFactory.Production_Regular
		    && SymbolAdapter.isAnyList(getType(tree)); 
	}
	
	public static boolean isOpt(IConstructor tree) {
		return tree.getConstructorType() == RascalValueFactory.Production_Regular
			&& SymbolAdapter.isOpt(getType(tree));
	}
	
	public static boolean isDefault(IConstructor tree) {
		return tree.getConstructorType() == RascalValueFactory.Production_Default;
	}
	
	public static boolean isRegular(IConstructor tree) {
		return tree.getConstructorType() == RascalValueFactory.Production_Regular;
	}
	
	public static boolean isSkipped(IConstructor tree) {
        return tree.getConstructorType() == RascalValueFactory.Production_Skipped;
    }

	public static boolean isError(IConstructor tree) {
		return tree.getConstructorType() == RascalValueFactory.Production_Error;
	}

	public static boolean isSeparatedList(IConstructor tree) {
		IConstructor rhs = getType(tree);
		return SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs);
	}

	public static boolean isLexical(IConstructor tree) {
		return SymbolAdapter.isLex(getType(tree));
	}
	
	public static boolean isSort(IConstructor tree) {
		return SymbolAdapter.isSort(getType(tree));
	}

	public static boolean isKeyword(IConstructor tree) {
		return SymbolAdapter.isKeyword(getType(tree));
	}

	public static String getCategory(IConstructor tree) {
		return getTagValue(tree, "category");
	}

	public static String getTagValue(IConstructor tree, String name) {
		if (!isRegular(tree)) {
			for (IValue attr : getAttributes(tree)) {
				if (attr.getType().isAbstractData() && ((IConstructor) attr).getConstructorType() == RascalValueFactory.Attr_Tag) {
					IValue value = ((IConstructor)attr).get("tag");
					if (value.getType().isNode() && ((INode) value).getName().equals(name)) {
						return ((IString) ((INode) value).get(0)).getValue();
					}
				}
			}
		}
		return null;
	}

	public static IConstructor getTree(IConstructor tree) {
		return tree;
	}

	public static boolean hasAttribute(IConstructor tree, IValue wanted) {
		return getAttributes(tree).contains(wanted);
	}
	
	public static boolean shouldFlatten(IConstructor surrounding, IConstructor nested) {
		if (ProductionAdapter.isList(nested)) {
			IConstructor nestedRhs = ProductionAdapter.getType(nested);
			IConstructor surroundingRhs = ProductionAdapter.getType(surrounding);
			
			if (SymbolAdapter.isEqual(surroundingRhs, nestedRhs)) {
				return true;
			}
			
			if ((SymbolAdapter.isIterPlus(surroundingRhs) && SymbolAdapter.isIterStar(nestedRhs)) || (SymbolAdapter.isIterStar(surroundingRhs) && SymbolAdapter.isIterPlus(nestedRhs))) {
				return SymbolAdapter.isEqual(SymbolAdapter.getSymbol(surroundingRhs), SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.isEqual(SymbolAdapter.getSeparators(surroundingRhs), SymbolAdapter.getSeparators(nestedRhs));
			}
			
			if ((SymbolAdapter.isIterPlusSeps(surroundingRhs) && SymbolAdapter.isIterStarSeps(nestedRhs)) || (SymbolAdapter.isIterStarSeps(surroundingRhs) && SymbolAdapter.isIterPlusSeps(nestedRhs))) {
				return SymbolAdapter.isEqual(SymbolAdapter.getSymbol(surroundingRhs), SymbolAdapter.getSymbol(nestedRhs)) && SymbolAdapter.isEqual(SymbolAdapter.getSeparators(surroundingRhs), SymbolAdapter.getSeparators(nestedRhs));
			}
		}
		return false;
	}
}
