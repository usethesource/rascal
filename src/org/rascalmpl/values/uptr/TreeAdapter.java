/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.visitors.TreeVisitor;

public class TreeAdapter {

	private TreeAdapter() {
		super();
	}

	public static boolean isAppl(IConstructor tree) {
		return tree.getConstructorType() == Factory.Tree_Appl;
	}

	public static boolean isAmb(IConstructor tree) {
		return tree.getConstructorType() == Factory.Tree_Amb;
	}

	public static boolean isChar(IConstructor tree) {
		return tree.getConstructorType() == Factory.Tree_Char;
	}

	public static boolean isCycle(IConstructor tree) {
		return tree.getConstructorType() == Factory.Tree_Cycle;
	}

	public static boolean isComment(IConstructor tree) {
		IConstructor treeProd = getProduction(tree);
		if (treeProd != null) {
			String treeProdCategory = ProductionAdapter.getCategory(treeProd);
			if (treeProdCategory != null && treeProdCategory.equals("Comment")) return true;
		}
		return false;
	}
	
	public static IConstructor getProduction(IConstructor tree) {
		return (IConstructor) tree.get("prod");
	}
	
	public static IConstructor getType(IConstructor tree) {
		if (isAppl(tree)) {
			return ProductionAdapter.getType(getProduction(tree));
		}
		else if (isCycle(tree)) {
			return (IConstructor) tree.get("symbol");
		}
		else if (isAmb(tree)) {
			return getType((IConstructor) getAlternatives(tree).iterator().next());
		}
		throw new ImplementationError("Tree does not have a type");
	}

	public static String getSortName(IConstructor tree)
			throws FactTypeUseException {
		return ProductionAdapter.getSortName(getProduction(tree));
	}

	/* (non-Javadoc)
	 * @see org.rascalmpl.values.uptr.ProductionAdapter#getConstructorName(IConstructor tree)
	 */
	public static String getConstructorName(IConstructor tree) {
		return ProductionAdapter.getConstructorName(getProduction(tree));
	}

	public static boolean isProduction(IConstructor tree, String sortName,
			String consName) {
		IConstructor prod = getProduction(tree);
		return ProductionAdapter.getSortName(prod).equals(sortName)
				&& ProductionAdapter.getConstructorName(prod).equals(consName);
	}

	public static boolean isContextFree(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter
				.isContextFree(getProduction(tree)) : false;
	}

	public static boolean isList(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isList(getProduction(tree))
				: false;
	}
	
	public static boolean isOpt(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isOpt(getProduction(tree))
				: false;
	}

	public static IList getArgs(IConstructor tree) {
		if (isAppl(tree)) {
			return (IList) tree.get("args");
		}

		throw new ImplementationError("Node has no args: " + tree.getName());
	}

	public static IConstructor setArgs(IConstructor tree, IList args) {
		if (isAppl(tree)) {
			return tree.set("args", args);
		}

		throw new ImplementationError("Node has no args: " + tree.getName());
	}		
	
	public static boolean isLiteral(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isLiteral(getProduction(tree))
				: false;
	}

	public static IList getListASTArgs(IConstructor tree) {
		if (!isList(tree)) {
			throw new ImplementationError(
					"This is not a context-free list production: " + tree);
		}
		IList children = getArgs(tree);
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory.getValueFactory());

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

	private static int getSeparatorCount(IConstructor tree) {
		return SymbolAdapter.getSeparators(ProductionAdapter.getType(getProduction(tree))).length();
	}

	public static boolean isLexical(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isLexical(getProduction(tree))
				: false;
	}
	
	public static boolean isSort(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isSort(getProduction(tree))
				: false;
	}

	public static boolean isLayout(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isLayout(getProduction(tree))
				: false;
	}

	private static boolean isSeparatedList(IConstructor tree) {
		return isAppl(tree) ? isList(tree)
				&& ProductionAdapter.isSeparatedList(getProduction(tree))
				: false;
	}

	public static IList getASTArgs(IConstructor tree) {
		if (SymbolAdapter.isStartSort(TreeAdapter.getType(tree))) {
			return getArgs(tree).delete(0).delete(1);
		}
		
		if (isLexical(tree)) {
			throw new ImplementationError("This is not a context-free production: " + tree);
		}

		IList children = getArgs(tree);
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory
				.getValueFactory());

		for (int i = 0; i < children.length(); i++) {
			IConstructor kid = (IConstructor) children.get(i);
			if (!isLiteral(kid) && !isCILiteral(kid)) {
				writer.append(kid);
			}
			// skip layout
			i++;
		}
		return writer.done();
	}

	public static boolean isCILiteral(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter
				.isCILiteral(getProduction(tree)) : false;
	}

	public static ISet getAlternatives(IConstructor tree) {
		if (isAmb(tree)) {
			return (ISet) tree.get("alternatives");
		}
		
		throw new ImplementationError("Node has no alternatives");
	}

	public static ISourceLocation getLocation(IConstructor tree) {
		return (ISourceLocation) tree.getAnnotation(Factory.Location);
	}

	public static int getCharacter(IConstructor tree) {
		return ((IInteger) tree.get("character")).intValue();
	}

	private static class Unparser extends TreeVisitor {
		private final Writer fStream;

		public Unparser(Writer stream) {
			fStream = stream;
		}
		
		public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
			((ISet) arg.get("alternatives")).iterator().next().accept(this);
			return arg;
		}
		
		public IConstructor visitTreeCycle(IConstructor arg) throws VisitorException {
			return arg;
		}
		
		public IConstructor visitTreeChar(IConstructor arg) throws VisitorException {
			try {
				fStream.write(Character.toChars(((IInteger) arg.get("character")).intValue()));
			} catch (IOException e) {
				throw new VisitorException(e);
			}
			return arg;
		}
		
		public IConstructor visitTreeAppl(IConstructor arg) throws VisitorException {
			IList children = (IList) arg.get("args");
			for (IValue child : children) {
				child.accept(this);
			}
			return arg;
		}
	}

	public static IConstructor locateLexical(IConstructor tree, int offset) {
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
						.getLocation((IConstructor) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateLexical((IConstructor) child,
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
	public static IConstructor locateAnnotatedTree(IConstructor tree, String label, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException(
					"locate assumes position information on the tree");
		}

		if (TreeAdapter.isAmb(tree)) {
			if (tree.hasAnnotation(label)) {
				return tree;
			}
			
			return null;
		}

		if (TreeAdapter.isAppl(tree) && !TreeAdapter.isLexical(tree)) {
			IList children = TreeAdapter.getASTArgs(tree);

			for (IValue child : children) {
				ISourceLocation childLoc = TreeAdapter
						.getLocation((IConstructor) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateAnnotatedTree((IConstructor) child, label, offset);

					if (result != null) {
						return result;
					}
				}
			}
		}
		
		if (l.getOffset() <= offset
				&& l.getOffset() + l.getLength() >= offset) {
			if (tree.hasAnnotation(label)) {
				return tree;
			}
		}

		return null;
	}

	public static void unparse(IConstructor tree, Writer stream)
			throws IOException, FactTypeUseException {
		try {
			if (tree.getType().isSubtypeOf(Factory.Tree)) { // == Factory.Tree) {
				tree.accept(new Unparser(stream));
			} else {
				throw new ImplementationError("Can not unparse this " + tree + " (type = "
						+ tree.getType() + ")") ;
			}
		} catch (VisitorException e) {
			Throwable cause = e.getCause();

			if (cause instanceof IOException) {
				throw (IOException) cause;
			}

			throw new ImplementationError("Unexpected error in unparse: "
					+ e.getMessage());
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

	public static boolean isInjectionOrSingleton(IConstructor tree) {
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

	public static boolean isAmbiguousList(IConstructor tree) {
		if (isAmb(tree)) {
			IConstructor first = (IConstructor) getAlternatives(tree)
					.iterator().next();
			if (isList(first)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNonEmptyStarList(IConstructor tree) {
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

	public static boolean isPlusList(IConstructor tree) {
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
	public static boolean isEpsilon(IConstructor tree) {
		if (isAppl(tree)) {
			for (IValue arg : getArgs(tree)) {
				boolean argResult = isEpsilon((IConstructor) arg);

				if (argResult == false) {
					return false;
				}
			}

			return true;
		}

		if (isAmb(tree)) {
			return isEpsilon((IConstructor) getAlternatives(tree).iterator()
					.next());
		}

		if (isCycle(tree)) {
			return true;
		}

		// is a character
		return false;
	}

	public static IList searchCategory(IConstructor tree, String category) {
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory
				.getValueFactory());
		if (isAppl(tree)) {
			String s = ProductionAdapter.getCategory(getProduction(tree));
			if (s == category)
				writer.append(tree);
			else {
				IList z = getArgs(tree);
				for (IValue q : z) {
					if (!(q instanceof IConstructor))
						continue;
					IList p = searchCategory((IConstructor) q, category);
					writer.appendAll(p);
				}
			}
		}
		return writer.done();
	}

	public static boolean isRascalLexical(IConstructor tree) {
		return SymbolAdapter.isLex(getType(tree)); 
	}

	public static IConstructor locateDeepestContextFreeNode(IConstructor tree, int offset) {
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
						.getLocation((IConstructor) child);
	
				if (childLoc == null) {
					continue;
				}
	
				if (childLoc.getOffset() <= offset
						&& offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateDeepestContextFreeNode((IConstructor) child,
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

	public static boolean isEmpty(IConstructor kid) {
		return isAppl(kid) && SymbolAdapter.isEmpty(ProductionAdapter.getType(getProduction(kid)));
	}

	public static int getCycleLength(IConstructor tree) {
		return new Integer(((IInteger) tree.get("cycleLength")).getStringRepresentation()).intValue();
	}

	public static IConstructor getCycleType(IConstructor tree) {
		return (IConstructor) tree.get("symbol");
	}
}
