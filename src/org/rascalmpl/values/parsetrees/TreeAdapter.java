/*******************************************************************************
 * Copyright (c) 2009-2015 CWI All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI 
 * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl 
 * Paul Klint - Paul.Klint@cwi.nl - CWI 
 * Mark Hills - Mark.Hills@cwi.nl (CWI) 
 * Arnold Lankamp - Arnold.Lankamp@cwi.nl 
 * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.values.parsetrees;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.visitors.TreeVisitor;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public class TreeAdapter {
	private static final String CHARACTER_TREE_ITEM = "character";
	private static final String NO_POSITION_INFORMATION_ERROR = "locate assumes position information on the tree";
	private static final String NO_ARGS_EXCEPTION_MESSAGE = "Node has no args: ";
	public static final String NORMAL = "Normal";
	public static final String TYPE = "Type";
	public static final String IDENTIFIER = "Identifier";
	public static final String VARIABLE = "Variable";
	public static final String CONSTANT = "Constant";
	public static final String COMMENT = "Comment";
	public static final String TODO = "Todo";
	public static final String QUOTE = "Quote";
	public static final String META_AMBIGUITY = "MetaAmbiguity";
	public static final String META_VARIABLE = "MetaVariable";
	public static final String META_KEYWORD = "MetaKeyword";
	public static final String META_SKIPPED = "MetaSkipped";
	public static final String NONTERMINAL_LABEL = "NonterminalLabel";
	public static final String RESULT = "Result";
	public static final String STDOUT = "StdOut";
	public static final String STDERR = "StdErr";


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

			if (SymbolAdapter.isLabel(sym) && SymbolAdapter.getLabel(sym).equals(label)) {
				return i;
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
			if (treeProdCategory != null && treeProdCategory.equals(COMMENT))
				return true;
		}
		return false;
	}

	public static IConstructor getProduction(ITree tree) {
		return tree.getProduction();
	}

	/**
	 * This function assumes that getLabeledField does not return null for the same parameters!
	 */
	public static ITree putLabeledField(ITree tree, String field, ITree repl) {
		if (isAppl(tree)) {
			IConstructor prod = TreeAdapter.getProduction(tree);

			if (ProductionAdapter.isDefault(prod)) {
				int index = SymbolAdapter.indexOfLabel(ProductionAdapter.getSymbols(prod), field);
				IList args = getArgs(tree);

				if (index != -1) {
					return setArgs(tree, args.put(index, repl));
				}
			}
			else if (ProductionAdapter.isRegular(prod)) {
				IConstructor sym = ProductionAdapter.getType(prod);
				IList args = getArgs(tree);
				IList syms;
				int index;

				switch (sym.getName()) {
					case "seq":
						syms = SymbolAdapter.getSymbols(sym);
						index = SymbolAdapter.indexOfLabel(syms, field);
						if (index != -1) {
							return setArgs(tree, args.put(index, repl));
						}
						break;
					case "opt":
						sym = SymbolAdapter.getSymbol(sym);
						if (SymbolAdapter.isLabel(sym) && SymbolAdapter.getLabel(sym).equals(field)) {
							if (args.length() == 0) {
								return setArgs(tree, args.append(repl));
							}
							else {
								return setArgs(tree, args.put(0, repl));
							}
						}
						break;
					case "alt":
						syms = SymbolAdapter.getSymbols(sym);
						index = SymbolAdapter.indexOfLabel(syms, field);
						if (index != -1) {
							sym = (IConstructor) syms.get(index);
							if (SymbolAdapter.isEqual(getType((ITree) args.get(0)), sym)) {
								return setArgs(tree, args.put(0, repl));
							}
						}
						break;
					default:
						return null;
				}
			}
		}

		return null;
	}

	public static class FieldResult {
		public IConstructor symbol;
		public ITree tree;

		public FieldResult(IConstructor symbol, ITree tree) {
			this.symbol = symbol;
			this.tree = tree;
		}
	}

	public static FieldResult getLabeledField(ITree tree, String field) {
		if (isAppl(tree)) {
			IConstructor prod = TreeAdapter.getProduction(tree);

			if (ProductionAdapter.isDefault(prod)) {
				IList syms = ProductionAdapter.getSymbols(prod);
				int index = SymbolAdapter.indexOfLabel(syms, field);

				if (index != -1) {
					IConstructor sym = (IConstructor) syms.get(index);
					return new FieldResult(SymbolAdapter.stripLabelsAndConditions(sym),
						(ITree) getArgs(tree).get(index));
				}
			}
			else if (ProductionAdapter.isRegular(prod)) {
				IConstructor sym = ProductionAdapter.getType(prod);
				IList args = getArgs(tree);
				IList syms;
				int index;

				switch (sym.getName()) {
					case "seq":
						syms = SymbolAdapter.getSymbols(sym);
						index = SymbolAdapter.indexOfLabel(syms, field);
						if (index != -1) {
							sym = (IConstructor) syms.get(index);
							return new FieldResult(SymbolAdapter.stripLabelsAndConditions(sym),
								(ITree) args.get(index));
						}
						break;
					case "opt":
						if (args.length() == 0) {
							return null;
						}
						sym = SymbolAdapter.getSymbol(sym);
						if (SymbolAdapter.isLabel(sym) && SymbolAdapter.getLabel(sym).equals(field)) {
							return new FieldResult(SymbolAdapter.stripLabelsAndConditions(sym), (ITree) args.get(0));
						}
						break;
					case "alt":
						ISet alts = SymbolAdapter.getAlternatives(sym);
						for (IValue elt : alts) {
							sym = (IConstructor) elt;

							// first find a matching label in the alt symbol
							if (SymbolAdapter.isLabel(sym) && SymbolAdapter.getLabel((IConstructor) elt).equals(field)) {
								sym = SymbolAdapter.stripLabelsAndConditions(sym);
						
								// now find the tree with the type that matches the label in the args list
								for (IValue arg : args) {
									if (SymbolAdapter.isEqual(getType((ITree) arg), sym)) {
										return new FieldResult(sym, (ITree) arg);
									}
								}
							}
						}
						break;
					default:
						return null;
				}
			}
		}

		return null;
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
		else if (isChar(tree)) {
			return SymbolAdapter.charClass(TreeAdapter.getCharacter(tree));
		}
		else if (isAmb(tree)) {
			return getType((ITree) getAlternatives(tree).iterator().next());
		}
		throw new ImplementationError("ITree does not have a type");
	}

	public static String getSortName(ITree tree) {
		return ProductionAdapter.getSortName(getProduction(tree));
	}

	/*
	 * (non-Javadoc)
	 * @see org.rascalmpl.values.uptr.ProductionAdapter#getConstructorName(IConstructor tree)
	 */
	public static String getConstructorName(ITree tree) {
		return ProductionAdapter.getConstructorName(getProduction(tree));
	}

	public static boolean isProduction(ITree tree, String sortName, String consName) {
		IConstructor prod = getProduction(tree);
		return ProductionAdapter.getSortName(prod).equals(sortName)
			&& ProductionAdapter.getConstructorName(prod).equals(consName);
	}

	public static boolean isContextFree(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isContextFree(getProduction(tree));
	}

	public static boolean isList(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isList(getProduction(tree));
	}

	public static boolean isOpt(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isOpt(getProduction(tree));
	}

	public static IList getArgs(ITree tree) {
		if (isAppl(tree)) {
			return (IList) tree.get("args");
		}

		throw new ImplementationError(NO_ARGS_EXCEPTION_MESSAGE + tree.getName());
	}

	public static ITree setArgs(ITree tree, IList args) {
		if (isAppl(tree)) {
			return (ITree) tree.set("args", args);
		}

		throw new ImplementationError(NO_ARGS_EXCEPTION_MESSAGE + tree.getName());
	}

	public static ITree setProduction(ITree tree, IConstructor prod) {
		if (isAppl(tree)) {
			return (ITree) tree.set("prod", prod);
		}

		throw new ImplementationError(NO_ARGS_EXCEPTION_MESSAGE + tree.getName());
	}

	public static boolean isLiteral(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isLiteral(getProduction(tree));
	}

	public static IList getListASTArgs(ITree tree) {
		if (!isList(tree)) {
			throw new ImplementationError("This is not a context-free list production: " + tree);
		}
		IList children = getArgs(tree);
		IListWriter writer = ValueFactoryFactory.getValueFactory().listWriter();

		for (int i = 0; i < children.length(); i += 2) {
			IValue kid = children.get(i);
			writer.append(kid);
			// skip layout and/or separators
			if (isSeparatedList(tree)) {
				i += getSeparatorCount(tree) - 1;
			}
		}
		return writer.done();
	}
	
	public static Stream<ITree> streamListASTArgs(ITree tree) {
		if (!isList(tree)) {
			throw new ImplementationError("This is not a context-free list production: " + tree);
		}

		IList children = getArgs(tree);
		int jump = isSeparatedList(tree) ? getSeparatorCount(tree) - 1 : 2;
		
		return IntStream.range(0, children.size())
			.filter(i -> i % jump == 0)
			.mapToObj(children::get)
			.map(t -> (ITree) t)
			;
	}

	public static int getSeparatorCount(ITree tree) {
		IConstructor nt = ProductionAdapter.getType(getProduction(tree));
		return SymbolAdapter.isSepList(nt) ? SymbolAdapter.getSeparators(nt).length() : 0;
	}

	public static boolean isLexical(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isLexical(getProduction(tree));
	}

	public static boolean isSort(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isSort(getProduction(tree));
	}

	public static boolean isLayout(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isLayout(getProduction(tree));
	}

	public static boolean isSeparatedList(ITree tree) {
		return isAppl(tree) && isList(tree) && ProductionAdapter.isSeparatedList(getProduction(tree));
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

		for (int i = 0; i < children.length(); i += 2 /* skip layout */) {
			ITree kid = (ITree) children.get(i);
			if (!isLiteral(kid) && !isCILiteral(kid)) {
				writer.append(kid);
			}
		}
		return writer.done();
	}

	public static Stream<ITree> streamASTArgs(ITree tree) {
		if (SymbolAdapter.isStartSort(TreeAdapter.getType(tree))) {
			return Stream.of((ITree) getArgs(tree).get(1));
		}

		if (isLexical(tree)) {
			throw new ImplementationError("This is not a context-free production: " + tree);
		}

		IList children = getArgs(tree);

		return IntStream.range(0, children.size())
			.filter(i -> i % 2 == 0)
			.mapToObj(children::get)
			.map(t -> (ITree) t)
			.filter(t -> !isLiteral(t) && !isCILiteral(t))
			;
	}

	public static boolean isCILiteral(ITree tree) {
		return isAppl(tree) && ProductionAdapter.isCILiteral(getProduction(tree));
	}

	public static ISet getAlternatives(ITree tree) {
		if (isAmb(tree)) {
			return (ISet) tree.get("alternatives");
		}

		throw new ImplementationError("Node has no alternatives");
	}

	public static ISourceLocation getLocation(ITree tree) {
		return (ISourceLocation) tree.asWithKeywordParameters().getParameter(RascalValueFactory.Location);
	}

	public static ITree setLocation(ITree tree, ISourceLocation loc) {
		return (ITree) tree.asWithKeywordParameters().setParameter(RascalValueFactory.Location, loc);
	}

	public static int getCharacter(ITree tree) {
		return ((IInteger) tree.get(CHARACTER_TREE_ITEM)).intValue();
	}

	private static class Unparser extends TreeVisitor<IOException> {
		protected final Writer fStream;
		private final boolean fHighlight;
		private final Map<String, Ansi> ansiOpen = new HashMap<>();

		private final Map<String, Ansi> ansiClose = new HashMap<>();

		public Unparser(Writer stream, boolean highlight) {
			fStream = stream;
			fHighlight = highlight;

			ansiOpen.put(NORMAL, Ansi.ansi().a(Attribute.ITALIC_OFF).a(Attribute.INTENSITY_BOLD_OFF).fg(Color.DEFAULT)
				.fgBright(Color.DEFAULT));
			ansiClose.put(NORMAL, Ansi.ansi().a(Attribute.ITALIC_OFF).a(Attribute.INTENSITY_BOLD_OFF).fg(Color.DEFAULT)
				.fgBright(Color.DEFAULT));

			ansiOpen.put(NONTERMINAL_LABEL, Ansi.ansi().a(Attribute.ITALIC).fg(Color.CYAN));
			ansiClose.put(NONTERMINAL_LABEL, Ansi.ansi().a(Attribute.ITALIC_OFF).fg(Color.DEFAULT));

			ansiOpen.put(META_KEYWORD, Ansi.ansi().fg(Color.MAGENTA));
			ansiClose.put(META_KEYWORD, Ansi.ansi().fg(Color.DEFAULT));

			ansiOpen.put(META_VARIABLE, Ansi.ansi().a(Attribute.ITALIC).fgBright(Color.GREEN));
			ansiClose.put(META_VARIABLE, Ansi.ansi().a(Attribute.ITALIC_OFF).fgBright(Color.DEFAULT));

			ansiOpen.put(META_AMBIGUITY, Ansi.ansi().a(Attribute.INTENSITY_BOLD).fgBright(Color.RED));
			ansiClose.put(META_AMBIGUITY, Ansi.ansi().a(Attribute.INTENSITY_BOLD_OFF).fgBright(Color.DEFAULT));

			ansiOpen.put(META_SKIPPED, Ansi.ansi().bgBright(Color.RED));
			ansiClose.put(META_SKIPPED, Ansi.ansi().bgBright(Color.WHITE));

			ansiOpen.put(COMMENT, Ansi.ansi().a(Attribute.ITALIC).fg(Color.GREEN));
			ansiClose.put(COMMENT, Ansi.ansi().a(Attribute.ITALIC_OFF).fg(Color.DEFAULT));
		}

		/**
		 * This Visitor tries to find if this tree contains a cycle, without going in to the amb parts
		 */
		private static class CycleDetector extends TreeVisitor<IOException> {
			private boolean result = false;

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
			ITree tree = (ITree) alternatives.next();
			while (alternatives.hasNext() && CycleDetector.detect(tree)) {
				tree = (ITree) alternatives.next();
			}
			tree.accept(this);
			return arg;
		}

		public ITree visitTreeCycle(ITree arg) throws IOException {
			return arg;
		}

		public ITree visitTreeChar(ITree arg) throws IOException {
			fStream.write(Character.toChars(((IInteger) arg.get(CHARACTER_TREE_ITEM)).intValue()));
			return arg;
		}

		public ITree visitTreeAppl(ITree arg) throws IOException {
			boolean reset = false;
			String category = null;

			if (fHighlight) {
				IConstructor prod = TreeAdapter.getProduction(arg);
				category = ProductionAdapter.getCategory(prod);

				if (category == null && (TreeAdapter.isLiteral(arg) || TreeAdapter.isCILiteral(arg))) {
					category = META_KEYWORD;

					for (IValue child : TreeAdapter.getArgs(arg)) {
						int c = TreeAdapter.getCharacter((ITree) child);
						if (c != '-' && !Character.isJavaIdentifierPart(c)) {
							category = null;
						}
					}
				}

				if (category != null) {
					Ansi code = ansiOpen.get(category);
					if (code != null) {
						fStream.write(code.toString());
						reset = true;
					}
				}
			}

			IList children = (IList) arg.get("args");
			for (IValue child : children) {
				child.accept(this);
			}

			if (fHighlight && reset) {
				Ansi code = ansiClose.get(category);
				if (code != null) {
					fStream.write(code.toString());
				}
			}
			return arg;
		}
	}

	private static class UnparserWithFocus extends Unparser {

		private ISourceLocation focus;
		private final String BACKGROUND_ON = Ansi.ansi().bgBright(Color.CYAN).toString();
		private final String BACKGROUND_OFF = Ansi.ansi().bg(Color.DEFAULT).toString();

		private boolean insideFocus = false;

		public UnparserWithFocus(Writer stream, ISourceLocation focus) {
			super(stream, true);
			this.focus = focus;
		}

		@Override
		public ITree visitTreeChar(ITree arg) throws IOException {
			char[] chars = Character.toChars(((IInteger) arg.get(CHARACTER_TREE_ITEM)).intValue());
			if (insideFocus) {
				for (int i = 0; i < chars.length; i++) {
					if (chars[i] == '\n') {
						fStream.write(BACKGROUND_OFF);
						fStream.write(chars[i]);
						fStream.write(BACKGROUND_ON);
					}
					else {
						fStream.write(chars[i]);
					}
				}
			}
			else {
				fStream.write(Character.toChars(((IInteger) arg.get(CHARACTER_TREE_ITEM)).intValue()));
			}
			return arg;
		}

		@Override
		public ITree visitTreeAppl(ITree arg) throws IOException {
			ISourceLocation argLoc = getLocation(arg);
			if (argLoc != null && argLoc.getOffset() == focus.getOffset() && argLoc.getLength() == focus.getLength()) {
				fStream.write(BACKGROUND_ON);
				insideFocus = true;
				super.visitTreeAppl(arg);
				insideFocus = false;
				fStream.write(BACKGROUND_OFF);
			}
			else {
				super.visitTreeAppl(arg);
			}
			return arg;
		}
	}

	public static IConstructor locateLexical(ITree tree, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException(NO_POSITION_INFORMATION_ERROR);
		}

		if (TreeAdapter.isLexical(tree)) {
			if (l.getOffset() <= offset && offset < l.getOffset() + l.getLength()) {
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

				if (childLoc.getOffset() <= offset && offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateLexical((ITree) child, offset);

					if (result != null) {
						return result;
					}
					break;
				}
			}

			if (l.getOffset() <= offset && l.getOffset() + l.getLength() >= offset) {
				return tree;
			}
		}

		return null;
	}

	/**
	 * Locate a lexical by line and column position
	 * 
	 * @param tree   is the haystack
	 * @param line   line position of the lexical
	 * @param column column offset
	 * @return
	 */
	public static ITree locateLexical(ITree tree, int line, int column) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException("no position info");
		}

		if (!l.hasLineColumn()) {
			return null;
		}

		if (TreeAdapter.isLexical(tree)) {
			if (l.getBeginLine() == line && l.getBeginColumn() <= column && column <= l.getEndColumn()) {
				// found a lexical that has the cursor inside of it
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

				// only go down in the right range, such that
				// finding the lexical is in O(log filesize)
				if (childLoc.getBeginLine() <= line && line <= childLoc.getEndLine()) {
					if (childLoc.getBeginLine() == line && childLoc.getEndColumn() == line) {
						// go down to the right column
						if (childLoc.getBeginColumn() <= column && column <= childLoc.getEndColumn()) {
							ITree result = locateLexical((ITree) child, line, column);
							if (result != null) {
								return result;
							}
						}
					}
					else { // in the line range, but not on the exact line yet
						ITree result = locateLexical((ITree) child, line, column);

						if (result != null) {
							return result;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * This finds the most specific (smallest) annotated tree which has its yield around the given
	 * offset.
	 */
	public static ITree locateAnnotatedTree(ITree tree, String label, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException(NO_POSITION_INFORMATION_ERROR);
		}

		if (TreeAdapter.isAmb(tree)) {
			if (tree.asWithKeywordParameters().hasParameter(label)) {
				return tree;
			}

			return null;
		}

		if (TreeAdapter.isAppl(tree) && !TreeAdapter.isLexical(tree)) {
			IList children = TreeAdapter.getArgs(tree);

			for (IValue child : children) {
				ISourceLocation childLoc = TreeAdapter.getLocation((ITree) child);

				if (childLoc == null) {
					continue;
				}

				if (childLoc.getOffset() <= offset && offset < childLoc.getOffset() + childLoc.getLength()) {
					ITree result = locateAnnotatedTree((ITree) child, label, offset);

					if (result != null) {
						return result;
					}
				}
			}
		}

		if (l.getOffset() <= offset && l.getOffset() + l.getLength() >= offset
			&& tree.asWithKeywordParameters().hasParameter(label)) {
			return tree;
		}

		return null;
	}



	public static void unparse(IConstructor tree, Writer stream) throws IOException {
		unparse(tree, false, stream);
	}

	public static void unparse(IConstructor tree, boolean highlight, Writer stream) throws IOException {
		if (tree instanceof ITree) {
			tree.accept(new Unparser(stream, highlight));
		}
		else {
			throw new ImplementationError("Can not unparse this " + tree + " (type = " + tree.getType() + ")");
		}
	}

	public static void unparseWithFocus(IConstructor tree, Writer stream, ISourceLocation focus) throws IOException {
		if (tree instanceof ITree) {
			tree.accept(new UnparserWithFocus(stream, focus));
		}
		else {
			throw new ImplementationError("Can not unparse this " + tree + " (type = " + tree.getType() + ")");
		}
	}

	public static String yield(IConstructor tree, boolean highlight, int limit) {
		Writer stream = new LimitedResultWriter(limit);

		try {
			unparse(tree, highlight, stream);
			return stream.toString();
		}
		catch (/*IOLimitReachedException*/ RuntimeException e) {
			return stream.toString();
		}
		catch (IOException e) {
			throw new ImplementationError("Method yield failed", e);
		}
	}

	public static String yield(IConstructor tree, int limit) {
		return yield(tree, false, limit);
	}

	public static String yield(IConstructor tree) {
		return yield(tree, false);
	}

	public static String yield(IConstructor tree, boolean highlight) {
		try {
			Writer stream = new CharArrayWriter();
			unparse(tree, highlight, stream);
			return stream.toString();
		}
		catch (IOException e) {
			throw new ImplementationError("Method yield failed", e);
		}
	}

	public static void yield(IConstructor tree, Writer out) throws IOException {
		unparse(tree, out);
	}

	public static void yield(IConstructor tree, boolean highlight, Writer out) throws IOException {
		unparse(tree, highlight, out);
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
	 * @return true if the tree does not have any characters, it's just an empty derivation
	 */
	public static boolean isEpsilon(ITree tree) {
		if (isAppl(tree)) {
			for (IValue arg : getArgs(tree)) {

				if (!isEpsilon((ITree) arg)) {
					return false;
				}
			}

			return true;
		}

		if (isAmb(tree)) {
			return isEpsilon((ITree) getAlternatives(tree).iterator().next());
		}

		// if it is not a cycle, it is a character
		return isCycle(tree);
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
			throw new IllegalArgumentException(NO_POSITION_INFORMATION_ERROR);
		}

		if (TreeAdapter.isLexical(tree)) {
			if (l.getOffset() <= offset && offset < l.getOffset() + l.getLength()) {
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

				if (childLoc.getOffset() <= offset && offset < childLoc.getOffset() + childLoc.getLength()) {
					IConstructor result = locateDeepestContextFreeNode((ITree) child, offset);

					if (result != null) {
						return result;
					}
					break;
				}
			}

			if (l.getOffset() <= offset && l.getOffset() + l.getLength() >= offset) {
				return tree;
			}
		}

		return null;
	}

	public static boolean isEmpty(ITree kid) {
		return isAppl(kid) && SymbolAdapter.isEmpty(ProductionAdapter.getType(getProduction(kid)));
	}

	public static int getCycleLength(ITree tree) {
		return ((IInteger) tree.get("cycleLength")).intValue();
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
