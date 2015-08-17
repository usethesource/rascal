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
import java.io.OutputStreamWriter;
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
import org.rascalmpl.values.uptr.visitors.TreeVisitor;

public class TreeAdapter {

	private TreeAdapter() {
		super();
	}

	public static boolean isTree(IConstructor cons) {
		return cons instanceof ITree;
	}
	
	public static boolean isAppl(ITree tree) {
		return tree.isAppl();
	}

	private static int findLabelPosition(ITree tree, String label) {
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
	
	public static ITree getArg(ITree tree, String label) {
	  return (ITree) getArgs(tree).get(findLabelPosition(tree, label));
	}
	
	public static ITree setArg(ITree tree, String label, IConstructor newArg) {
	  return setArgs(tree, getArgs(tree).put(findLabelPosition(tree, label), newArg));
	}
	
	public static boolean isAmb(ITree tree) {
		return tree.isAmb();
	}
	
	public static boolean isTop(ITree tree) {
		return SymbolAdapter.isStartSort(getType(tree));
	}

	public static boolean isChar(ITree tree) {
		return tree.isChar();
	}

	public static boolean isCycle(ITree tree) {
		return tree.isCycle();
	}

	public static boolean isComment(ITree tree) {
		IConstructor treeProd = getProduction(tree);
		if (treeProd != null) {
			String treeProdCategory = ProductionAdapter.getCategory(treeProd);
			if (treeProdCategory != null && treeProdCategory.equals("Comment")) return true;
		}
		return false;
	}
	
	public static IConstructor getProduction(ITree tree) {
		return tree.getProduction();
	}
	
	public static IConstructor getType(ITree tree) {
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
			return getType((ITree) getAlternatives(tree).iterator().next());
		}
		throw new ImplementationError("ITree does not have a type");
	}

	public static String getSortName(ITree tree)
			throws FactTypeUseException {
		return ProductionAdapter.getSortName(getProduction(tree));
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.values.uptr.ProductionAdapter#getConstructorName(IConstructor tree)
	 */
	public static String getConstructorName(ITree tree) {
		return ProductionAdapter.getConstructorName(getProduction(tree));
	}

	public static boolean isProduction(ITree tree, String sortName,
			String consName) {
		IConstructor prod = getProduction(tree);
		return ProductionAdapter.getSortName(prod).equals(sortName)
				&& ProductionAdapter.getConstructorName(prod).equals(consName);
	}

	public static boolean isContextFree(ITree tree) {
		return isAppl(tree) ? ProductionAdapter
				.isContextFree(getProduction(tree)) : false;
	}

	public static boolean isList(ITree tree) {
		return isAppl(tree) ? ProductionAdapter.isList(getProduction(tree))
				: false;
	}
	
	public static boolean isOpt(ITree tree) {
		return isAppl(tree) ? ProductionAdapter.isOpt(getProduction(tree))
				: false;
	}

	public static IList getArgs(ITree tree) {
		if (isAppl(tree)) {
			return (IList) tree.get("args");
		}

		throw new ImplementationError("Node has no args: " + tree.getName());
	}

	public static ITree setArgs(ITree tree, IList args) {
		if (isAppl(tree)) {
			return (ITree) tree.set("args", args);
		}

		throw new ImplementationError("Node has no args: " + tree.getName());
	}		
	
	public static ITree setProduction(ITree tree, IConstructor prod) {
    if (isAppl(tree)) {
      return (ITree) tree.set("prod", prod);
    }

    throw new ImplementationError("Node has no args: " + tree.getName());
  } 
	
	public static boolean isLiteral(ITree tree) {
		return isAppl(tree) ? ProductionAdapter.isLiteral(getProduction(tree))
				: false;
	}

	public static IList getListASTArgs(ITree tree) {
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

	public static int getSeparatorCount(ITree tree) {
		return SymbolAdapter.getSeparators(ProductionAdapter.getType(getProduction(tree))).length();
	}

	public static boolean isLexical(ITree tree) {
		return isAppl(tree) ? ProductionAdapter.isLexical(getProduction(tree))
				: false;
	}
	
	public static boolean isSort(ITree tree) {
		return isAppl(tree) ? ProductionAdapter.isSort(getProduction(tree))
				: false;
	}

	public static boolean isLayout(ITree tree) {
		return isAppl(tree) ? ProductionAdapter.isLayout(getProduction(tree))
				: false;
	}

	public static boolean isSeparatedList(ITree tree) {
		return isAppl(tree) ? isList(tree)
				&& ProductionAdapter.isSeparatedList(getProduction(tree))
				: false;
	}

	public static IList getASTArgs(ITree tree) {
		if (SymbolAdapter.isStartSort(TreeAdapter.getType(tree))) {
			return getArgs(tree).delete(0).delete(1);
		}
		
		if (isLexical(tree)) {
			throw new ImplementationError("This is not a context-free production: " + tree);
		}

		IList children = getArgs(tree);
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();

		for (int i = 0; i < children.length(); i++) {
			ITree kid = (ITree) children.get(i);
			if (!isLiteral(kid) && !isCILiteral(kid)) {
				writer.append(kid);
			}
			// skip layout
			i++;
		}
		return writer.done();
	}

	public static boolean isCILiteral(ITree tree) {
		return isAppl(tree) ? ProductionAdapter
				.isCILiteral(getProduction(tree)) : false;
	}

	public static ISet getAlternatives(ITree tree) {
		if (isAmb(tree)) {
			return (ISet) tree.get("alternatives");
		}
		
		throw new ImplementationError("Node has no alternatives");
	}

	public static ISourceLocation getLocation(ITree tree) {
		return (ISourceLocation) tree.asAnnotatable().getAnnotation(RascalValueFactory.Location);
	}

	public static ITree setLocation(ITree tree, ISourceLocation loc) {
		return (ITree) tree.asAnnotatable().setAnnotation(RascalValueFactory.Location, loc);
	}
	
	public static int getCharacter(ITree tree) {
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
			public ITree visitTreeCycle(ITree arg) throws IOException {
				result = true;
				return arg;
			}
			@Override
			public ITree visitTreeAppl(ITree arg) throws IOException {
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
			public ITree visitTreeAmb(ITree arg) throws IOException {
				// don't go into other amb trees with cycles
				return arg;
			}
			@Override
			public ITree visitTreeChar(ITree arg) throws IOException {
				return arg;
			}
			public static boolean detect(ITree tree) throws IOException {
				CycleDetector look = new CycleDetector();
				tree.accept(look);
				return look.result;
			}
		}
		
		public ITree visitTreeAmb(ITree arg) throws IOException {
			ISet alts = TreeAdapter.getAlternatives(arg);
			
			if (alts.isEmpty()) {
				return arg;
			}
			
			Iterator<IValue> alternatives = alts.iterator();
			// do not try to print the alternative with the cycle in it.
			// so lets try to find the tree without the cycle
			ITree tree = (ITree)alternatives.next();
			while (alternatives.hasNext() && CycleDetector.detect(tree) ) {
				tree = (ITree)alternatives.next();
			}
			tree.accept(this);
			return arg;
		}
		
		public ITree visitTreeCycle(ITree arg) throws IOException {
			return arg;
		}
		
		public ITree visitTreeChar(ITree arg) throws IOException {
		  fStream.write(Character.toChars(((IInteger) arg.get("character")).intValue()));
			return arg;
		}
		
		public ITree visitTreeAppl(ITree arg) throws IOException {
			IList children = (IList) arg.get("args");
			for (IValue child : children) {
				child.accept(this);
			}
			return arg;
		}
	}

	public static IConstructor locateLexical(ITree tree, int offset) {
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
				ISourceLocation childLoc = TreeAdapter.getLocation((ITree) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateLexical((ITree) child,
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
	public static ITree locateAnnotatedTree(ITree tree, String label, int offset) {
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
				ISourceLocation childLoc = TreeAdapter.getLocation((ITree) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					ITree result = locateAnnotatedTree((ITree) child, label, offset);

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
	  if (tree instanceof ITree) { 
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

  public static void yield(IConstructor tree, Writer out) throws IOException {
    unparse(tree, out);
  }

	public static boolean isInjectionOrSingleton(ITree tree) {
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

	public static boolean isAmbiguousList(ITree tree) {
		if (isAmb(tree)) {
			ITree first = (ITree) getAlternatives(tree).iterator().next();
			if (isList(first)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNonEmptyStarList(ITree tree) {
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

	public static boolean isPlusList(ITree tree) {
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
	public static boolean isEpsilon(ITree tree) {
		if (isAppl(tree)) {
			for (IValue arg : getArgs(tree)) {
				boolean argResult = isEpsilon((ITree) arg);

				if (argResult == false) {
					return false;
				}
			}

			return true;
		}

		if (isAmb(tree)) {
			return isEpsilon((ITree) getAlternatives(tree).iterator()
					.next());
		}

		if (isCycle(tree)) {
			return true;
		}

		// is a character
		return false;
	}

	public static IList searchCategory(ITree tree, String category) {
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
					IList p = searchCategory((ITree) q, category);
					writer.appendAll(p);
				}
			}
		}
		return writer.done();
	}

	public static boolean isRascalLexical(ITree tree) {
		return SymbolAdapter.isLex(getType(tree)); 
	}

	public static IConstructor locateDeepestContextFreeNode(ITree tree, int offset) {
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
						.getLocation((ITree) child);
	
				if (childLoc == null) {
					continue;
				}
	
				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateDeepestContextFreeNode((ITree) child,
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

	public static boolean isEmpty(ITree kid) {
		return isAppl(kid) && SymbolAdapter.isEmpty(ProductionAdapter.getType(getProduction(kid)));
	}

	public static int getCycleLength(ITree tree) {
		return new Integer(((IInteger) tree.get("cycleLength")).getStringRepresentation()).intValue();
	}

	public static IConstructor getCycleType(ITree tree) {
		return (IConstructor) tree.get("symbol");
	}

	public static ITree getStartTop(ITree prefix) {
		return (ITree) getArgs(prefix).get(1);
	}

	public static IList getNonLayoutArgs(ITree treeSubject) {
		IListWriter w = ValueFactoryFactory.getValueFactory().listWriter();
		for (IValue v : getArgs(treeSubject)) {
			if (!TreeAdapter.isLayout((ITree) v)) {
				w.append(v);
			}
		}
		return w.done();
	}

}
