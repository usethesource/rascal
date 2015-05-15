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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.values.uptr;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory.Tree;
import org.rascalmpl.values.uptr.visitors.TreeVisitor;

public class TreeAdapter {

	private TreeAdapter() {
		super();
	}

	public static boolean isAppl(Tree tree) {
		assert tree instanceof Tree;
		return ((Tree) tree).isAppl();
	}

	private static int findLabelPosition(Tree tree, String label) {
	  if (!TreeAdapter.isAppl(tree)) {
      throw new ImplementationError("can not call getArg on a non-tree");
    }
    
    IConstructor prod = TreeAdapter.getProduction(tree);

    if (!ProductionAdapter.isDefault(prod)) {
      return -1;
    }
    
    IList syms = ProductionAdapter.getSymbols(prod);

    for (int i = 0; i < syms.length(); i++) {
      IConstructor sym = (IConstructor) syms.get(i);

      while (SymbolAdapter.isConditional(sym)) {
        sym = SymbolAdapter.getSymbol(sym);
      }

      if (SymbolAdapter.isLabel(sym)) {
        if (SymbolAdapter.getLabel(sym).equals(label)) {
          return i;
        }
      }
    }
    
    return -1;
	}
	
	public static Tree getArg(Tree tree, String label) {
	  return (Tree) getArgs(tree).get(findLabelPosition(tree, label));
	}
	
	public static Tree setArg(Tree tree, String label, IConstructor newArg) {
	  return setArgs(tree, getArgs(tree).put(findLabelPosition(tree, label), newArg));
	}
	
	public static boolean isAmb(Tree tree) {
		return tree.getConstructorType() == RascalValueFactory.Tree_Amb;
	}
	
	public static boolean isTop(Tree tree) {
		return SymbolAdapter.isStartSort(getType(tree));
	}

	public static boolean isChar(Tree tree) {
		return tree.getConstructorType() == RascalValueFactory.Tree_Char;
	}

	public static boolean isCycle(Tree tree) {
		return tree.getConstructorType() == RascalValueFactory.Tree_Cycle;
	}

	public static boolean isComment(Tree tree) {
		IConstructor treeProd = getProduction(tree);
		if (treeProd != null) {
			String treeProdCategory = ProductionAdapter.getCategory(treeProd);
			if (treeProdCategory != null && treeProdCategory.equals("Comment")) return true;
		}
		return false;
	}
	
	public static IConstructor getProduction(Tree tree) {
		return (IConstructor) tree.get("prod");
	}
	
	public static IConstructor getType(Tree tree) {
		if (isAppl(tree)) {
			IConstructor sym = ProductionAdapter.getType(getProduction(tree));
			
			if (SymbolAdapter.isStarList(sym) && !getArgs(tree).isEmpty()) {
				sym = SymbolAdapter.starToPlus(sym);
			}
			
			return sym;
		}
		else if (isCycle(tree)) {
			return (IConstructor) tree.get("symbol");
		}
		else if (isAmb(tree)) {
			return getType((Tree) getAlternatives(tree).iterator().next());
		}
		throw new ImplementationError("Tree does not have a type");
	}

	public static String getSortName(Tree tree)
			throws FactTypeUseException {
		return ProductionAdapter.getSortName(getProduction(tree));
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.values.uptr.ProductionAdapter#getConstructorName(IConstructor tree)
	 */
	public static String getConstructorName(Tree tree) {
		return ProductionAdapter.getConstructorName(getProduction(tree));
	}

	public static boolean isProduction(Tree tree, String sortName,
			String consName) {
		IConstructor prod = getProduction(tree);
		return ProductionAdapter.getSortName(prod).equals(sortName)
				&& ProductionAdapter.getConstructorName(prod).equals(consName);
	}

	public static boolean isContextFree(Tree tree) {
		return isAppl(tree) ? ProductionAdapter
				.isContextFree(getProduction(tree)) : false;
	}

	public static boolean isList(Tree tree) {
		return isAppl(tree) ? ProductionAdapter.isList(getProduction(tree))
				: false;
	}
	
	public static boolean isOpt(Tree tree) {
		return isAppl(tree) ? ProductionAdapter.isOpt(getProduction(tree))
				: false;
	}

	public static IList getArgs(Tree tree) {
		if (isAppl(tree)) {
			return (IList) tree.get("args");
		}

		throw new ImplementationError("Node has no args: " + tree.getName());
	}

	public static org.rascalmpl.values.uptr.RascalValueFactory.Tree setArgs(Tree tree, IList args) {
		if (isAppl(tree)) {
			return (org.rascalmpl.values.uptr.RascalValueFactory.Tree) tree.set("args", args);
		}

		throw new ImplementationError("Node has no args: " + tree.getName());
	}		
	
	public static Tree setProduction(Tree tree, IConstructor prod) {
    if (isAppl(tree)) {
      return (Tree) tree.set("prod", prod);
    }

    throw new ImplementationError("Node has no args: " + tree.getName());
  } 
	
	public static boolean isLiteral(Tree tree) {
		return isAppl(tree) ? ProductionAdapter.isLiteral(getProduction(tree))
				: false;
	}

	public static IList getListASTArgs(Tree tree) {
		if (!isList(tree)) {
			throw new ImplementationError(
					"This is not a context-free list production: " + tree);
		}
		IList children = getArgs(tree);
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();

		for (int i = 0; i < children.length(); ++i) {
			IValue kid = children.get(i);
			writer.append(kid);
			// skip layout and/or separators
			if (isSeparatedList(tree)) {
				i += getSeparatorCount(tree);
			} else {
				++i;
			}
		}
		return writer.done();
	}

	public static int getSeparatorCount(Tree tree) {
		return SymbolAdapter.getSeparators(ProductionAdapter.getType(getProduction(tree))).length();
	}

	public static boolean isLexical(Tree tree) {
		return isAppl(tree) ? ProductionAdapter.isLexical(getProduction(tree))
				: false;
	}
	
	public static boolean isSort(Tree tree) {
		return isAppl(tree) ? ProductionAdapter.isSort(getProduction(tree))
				: false;
	}

	public static boolean isLayout(Tree tree) {
		return isAppl(tree) ? ProductionAdapter.isLayout(getProduction(tree))
				: false;
	}

	public static boolean isSeparatedList(Tree tree) {
		return isAppl(tree) ? isList(tree)
				&& ProductionAdapter.isSeparatedList(getProduction(tree))
				: false;
	}

	public static IList getASTArgs(Tree tree) {
		if (SymbolAdapter.isStartSort(TreeAdapter.getType(tree))) {
			return getArgs(tree).delete(0).delete(1);
		}
		
		if (isLexical(tree)) {
			throw new ImplementationError("This is not a context-free production: " + tree);
		}

		IList children = getArgs(tree);
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();

		for (int i = 0; i < children.length(); i++) {
			Tree kid = (Tree) children.get(i);
			if (!isLiteral(kid) && !isCILiteral(kid)) {
				writer.append(kid);
			}
			// skip layout
			i++;
		}
		return writer.done();
	}

	public static boolean isCILiteral(Tree tree) {
		return isAppl(tree) ? ProductionAdapter
				.isCILiteral(getProduction(tree)) : false;
	}

	public static ISet getAlternatives(Tree tree) {
		if (isAmb(tree)) {
			return (ISet) tree.get("alternatives");
		}
		
		throw new ImplementationError("Node has no alternatives");
	}

	public static ISourceLocation getLocation(Tree tree) {
		return (ISourceLocation) tree.asAnnotatable().getAnnotation(RascalValueFactory.Location);
	}

	public static Tree setLocation(Tree tree, ISourceLocation loc) {
		return (Tree) tree.asAnnotatable().setAnnotation(RascalValueFactory.Location, loc);
	}
	
	public static int getCharacter(Tree tree) {
		return ((IInteger) tree.get("character")).intValue();
	}

	private static class Unparser extends TreeVisitor<IOException> {
		private final Writer fStream;

		public Unparser(Writer stream) {
			fStream = stream;
		}
		
		/**
		 * This Visitor tries to find if this tree contains a cycle, without going in to the amb parts 
		 */
		private static class CycleDetector  extends TreeVisitor<IOException> {
			public boolean result = false;

			@Override
			public Tree visitTreeCycle(Tree arg) throws IOException {
				result = true;
				return arg;
			}
			@Override
			public Tree visitTreeAppl(Tree arg) throws IOException {
				if (!result) {
					IList children = (IList) arg.get("args");
					for (IValue child : children) {
						child.accept(this);
						if (result) {
							break;
						}
					}
				}
				return arg;
			}
			@Override
			public Tree visitTreeAmb(Tree arg) throws IOException {
				// don't go into other amb trees with cycles
				return arg;
			}
			@Override
			public Tree visitTreeChar(Tree arg) throws IOException {
				return arg;
			}
			public static boolean detect(Tree tree) throws IOException {
				CycleDetector look = new CycleDetector();
				tree.accept(look);
				return look.result;
			}
		}
		
		public Tree visitTreeAmb(Tree arg) throws IOException {
			ISet alts = TreeAdapter.getAlternatives(arg);
			
			if (alts.isEmpty()) {
				return arg;
			}
			
			Iterator<IValue> alternatives = alts.iterator();
			// do not try to print the alternative with the cycle in it.
			// so lets try to find the tree without the cycle
			Tree tree = (Tree)alternatives.next();
			while (alternatives.hasNext() && CycleDetector.detect(tree) ) {
				tree = (Tree)alternatives.next();
			}
			tree.accept(this);
			return arg;
		}
		
		public Tree visitTreeCycle(Tree arg) throws IOException {
			return arg;
		}
		
		public Tree visitTreeChar(Tree arg) throws IOException {
		  fStream.write(Character.toChars(((IInteger) arg.get("character")).intValue()));
			return arg;
		}
		
		public Tree visitTreeAppl(Tree arg) throws IOException {
			IList children = (IList) arg.get("args");
			for (IValue child : children) {
				child.accept(this);
			}
			return arg;
		}
	}

	public static IConstructor locateLexical(Tree tree, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException(
					"locate assumes position information on the tree");
		}

		if (TreeAdapter.isLexical(tree)) {
			if (l.getOffset() <= offset
					&& offset < l.getOffset() + l.getLength()) {
				return tree;
			}

			return null;
		}

		if (TreeAdapter.isAmb(tree)) {
			return null;
		}

		if (TreeAdapter.isAppl(tree)) {
			IList children = TreeAdapter.getASTArgs(tree);

			for (IValue child : children) {
				ISourceLocation childLoc = TreeAdapter.getLocation((Tree) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateLexical((Tree) child,
							offset);

					if (result != null) {
						return result;
					}
					break;
				}
			}

			if (l.getOffset() <= offset
					&& l.getOffset() + l.getLength() >= offset) {
				return tree;
			}
		}

		return null;
	}

	/**
	 * This finds the most specific (smallest) annotated tree which has its yield around the given offset.
	 */
	public static Tree locateAnnotatedTree(Tree tree, String label, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException(
					"locate assumes position information on the tree");
		}

		if (TreeAdapter.isAmb(tree)) {
			if (tree.asAnnotatable().hasAnnotation(label)) {
				return tree;
			}
			
			return null;
		}

		if (TreeAdapter.isAppl(tree) && !TreeAdapter.isLexical(tree)) {
			IList children = TreeAdapter.getArgs(tree); //TreeAdapter.getASTArgs(tree);

			for (IValue child : children) {
				ISourceLocation childLoc = TreeAdapter.getLocation((Tree) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					Tree result = locateAnnotatedTree((Tree) child, label, offset);

					if (result != null) {
						return result;
					}
				}
			}
		}
		
		if (l.getOffset() <= offset
				&& l.getOffset() + l.getLength() >= offset) {
			if (tree.asAnnotatable().hasAnnotation(label)) {
				return tree;
			}
		}

		return null;
	}

	public static void unparse(IConstructor tree, Writer stream)
			throws IOException, FactTypeUseException {
	  if (tree.getType().isSubtypeOf(RascalValueFactory.Tree)) { 
	    tree.accept(new Unparser(stream));
	  } else {
	    throw new ImplementationError("Can not unparse this " + tree + " (type = "
	        + tree.getType() + ")") ;
	  }
	}

	public static String yield(IConstructor tree, int limit) throws FactTypeUseException {
		Writer stream = new LimitedResultWriter(limit);
		
		try {
			unparse(tree, stream);
			return stream.toString();
		}
		catch (IOLimitReachedException e) {
			return stream.toString();			
		}
		catch (IOException e) {
			throw new ImplementationError("Method yield failed", e);
		}
	}
	
	public static String yield(IConstructor tree) throws FactTypeUseException {
		try {
			Writer stream = new CharArrayWriter();
			unparse(tree, stream);
			return stream.toString();
		} catch (IOException e) {
			throw new ImplementationError("Method yield failed", e);
		}
	}

	public static boolean isInjectionOrSingleton(Tree tree) {
		IConstructor prod = getProduction(tree);
		if (isAppl(tree)) {
			if (ProductionAdapter.isDefault(prod)) {
			   return ProductionAdapter.getSymbols(prod).length() == 1;
			}
			else if (ProductionAdapter.isList(prod)) {
				return getArgs(tree).length() == 1;
			}
		}
		return false;
	}

	public static boolean isAmbiguousList(Tree tree) {
		if (isAmb(tree)) {
			Tree first = (Tree) getAlternatives(tree).iterator().next();
			if (isList(first)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNonEmptyStarList(Tree tree) {
		if (isAppl(tree)) {
			IConstructor prod = getProduction(tree);

			if (ProductionAdapter.isList(prod)) {
				IConstructor sym = ProductionAdapter.getType(prod);

				if (SymbolAdapter.isIterStar(sym) || SymbolAdapter.isIterStarSeps(sym)) {
					return getArgs(tree).length() > 0;
				}
			}
		}
		return false;
	}

	public static boolean isPlusList(Tree tree) {
		if (isAppl(tree)) {
			IConstructor prod = getProduction(tree);

			if (ProductionAdapter.isList(prod)) {
				IConstructor sym = ProductionAdapter.getType(prod);

				if (SymbolAdapter.isIterPlus(sym) || SymbolAdapter.isIterPlusSeps(sym)) { 
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * @return true if the tree does not have any characters, it's just an empty
	 *         derivation
	 */
	public static boolean isEpsilon(Tree tree) {
		if (isAppl(tree)) {
			for (IValue arg : getArgs(tree)) {
				boolean argResult = isEpsilon((Tree) arg);

				if (argResult == false) {
					return false;
				}
			}

			return true;
		}

		if (isAmb(tree)) {
			return isEpsilon((Tree) getAlternatives(tree).iterator()
					.next());
		}

		if (isCycle(tree)) {
			return true;
		}

		// is a character
		return false;
	}

	public static IList searchCategory(Tree tree, String category) {
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();
		if (isAppl(tree)) {
			String s = ProductionAdapter.getCategory(getProduction(tree));
			if (s == category)
				writer.append(tree);
			else {
				IList z = getArgs(tree);
				for (IValue q : z) {
					if (!(q instanceof IConstructor))
						continue;
					IList p = searchCategory((Tree) q, category);
					writer.appendAll(p);
				}
			}
		}
		return writer.done();
	}

	public static boolean isRascalLexical(Tree tree) {
		return SymbolAdapter.isLex(getType(tree)); 
	}

	public static IConstructor locateDeepestContextFreeNode(Tree tree, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);
	
		if (l == null) {
			throw new IllegalArgumentException(
					"locate assumes position information on the tree");
		}
	
		if (TreeAdapter.isLexical(tree)) {
			if (l.getOffset() <= offset
					&& offset < l.getOffset() + l.getLength()) {
				return tree;
			}
	
			return null;
		}
	
		if (TreeAdapter.isAmb(tree)) {
			return null;
		}
	
		if (TreeAdapter.isAppl(tree)) {
			IList children = TreeAdapter.getASTArgs(tree);
	
			for (IValue child : children) {
				ISourceLocation childLoc = TreeAdapter
						.getLocation((Tree) child);
	
				if (childLoc == null) {
					continue;
				}
	
				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateDeepestContextFreeNode((Tree) child,
							offset);
	
					if (result != null) {
						return result;
					}
					break;
				}
			}
	
			if (l.getOffset() <= offset
					&& l.getOffset() + l.getLength() >= offset) {
				return tree;
			}
		}
	
		return null;
	}

	public static boolean isEmpty(Tree kid) {
		return isAppl(kid) && SymbolAdapter.isEmpty(ProductionAdapter.getType(getProduction(kid)));
	}

	public static int getCycleLength(Tree tree) {
		return new Integer(((IInteger) tree.get("cycleLength")).getStringRepresentation()).intValue();
	}

	public static IConstructor getCycleType(Tree tree) {
		return (IConstructor) tree.get("symbol");
	}

	public static Tree getStartTop(Tree prefix) {
		return (Tree) getArgs(prefix).get(1);
	}

	public static IList getNonLayoutArgs(Tree treeSubject) {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter();
		for (IValue v : getArgs(treeSubject)) {
			if (!TreeAdapter.isLayout((Tree) v)) {
				w.append(v);
			}
		}
		return w.done();
	}
}
