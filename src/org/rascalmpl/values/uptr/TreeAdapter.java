package org.rascalmpl.values.uptr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.parser.MappingsCache;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.visitors.IdentityTreeVisitor;

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

	public static boolean hasSortName(IConstructor tree) {
		return ProductionAdapter.hasSortName(getProduction(tree));
	}

	public static String getSortName(IConstructor tree)
			throws FactTypeUseException {
		return ProductionAdapter.getSortName(getProduction(tree));
	}

	public static String getConstructorName(IConstructor tree) {
		return ProductionAdapter.getConstructorName(getProduction(tree));
	}

	public static boolean isProduction(IConstructor tree, String sortName,
			String consName) {
		IConstructor prod = getProduction(tree);
		return ProductionAdapter.getSortName(prod).equals(sortName)
				&& ProductionAdapter.getConstructorName(prod).equals(consName);
	}

	public static boolean isLexToCf(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isLexToCf(getProduction(tree))
				: false;
	}

	public static boolean isContextFree(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter
				.isContextFree(getProduction(tree)) : false;
	}

	public static boolean isList(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isList(getProduction(tree))
				: false;
	}

	public static IList getArgs(IConstructor tree) {
		if (isAppl(tree)) {
			return (IList) tree.get("args");
		}

		throw new ImplementationError("Node has no args");
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
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory
				.getValueFactory());

		for (int i = 0; i < children.length(); i++) {
			IValue kid = children.get(i);
			writer.append(kid);
			// skip layout and/or separators
			if (isNewSeparatedList(tree)) {
				i += getSeparatorCount(tree);
			}
			else {
				i += (isSeparatedList(tree) ? 3 : 1);
			}
		}
		return writer.done();
	}

	private static int getSeparatorCount(IConstructor tree) {
		return SymbolAdapter.getSeparators(ProductionAdapter.getRhs(getProduction(tree))).length();
	}

	private static boolean isNewSeparatedList(IConstructor tree) {
		return ProductionAdapter.isNewSeparatedList(getProduction(tree));
	}

	public static boolean isLexical(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter.isLexical(getProduction(tree))
				: false;
	}

	public static boolean isCfOptLayout(IConstructor tree) {
		return isAppl(tree) ? ProductionAdapter
				.isCfOptLayout(getProduction(tree)) : false;
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
		if (getSortName(tree).equals("<START>")) {
			return getArgs(tree).delete(0).delete(1);
		}
		
		if (!isContextFree(tree)) {
			throw new ImplementationError(
					"This is not a context-free production: " + tree);
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

	protected static class PositionAnnotator {
		private final IConstructor tree;
		private final MappingsCache<PositionNode, IConstructor> cache;
		private final boolean windowsOS = System.getProperty("os.name")
				.toLowerCase().indexOf("win") != -1;
		private boolean inLayout = false;
		private boolean labelLayout = false;

		public PositionAnnotator(IConstructor tree) {
			super();

			this.tree = tree;
			this.cache = new MappingsCache<PositionNode, IConstructor>();
		}

		public IConstructor addPositionInformation(URI location) {
			Factory.getInstance(); // make sure everything is declared
			try {
				return addPosInfo(tree, location, new Position()); // Fix
																	// filename
																	// so URI's
																	// work.
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}

		private IConstructor addPosInfo(IConstructor tree, URI location,
				Position cur) throws MalformedURLException {
			IValueFactory factory = ValueFactoryFactory.getValueFactory();
			int startLine = cur.line;
			int startCol = cur.col;
			int startOffset = cur.offset;
			PositionNode positionNode = new PositionNode(tree, cur.offset);
			IConstructor result = cache.get(positionNode);
			
			if (result != null) {
				ISourceLocation loc = getLocation(result);
				cur.col = loc.getEndColumn();
				cur.line = loc.getEndLine();
				cur.offset += loc.getLength();
				return result;
			}

			if (isChar(tree)) {
				cur.offset++;
				char character = ((char) getCharacter(tree));

				if (character == '\r') {
					cur.col++;
					cur.sawCR = true;
				} else if (character == '\n') {
					cur.col = 0;
					cur.line++;

					// Workaround to funny stuff in Eclipse editors
					if (windowsOS && !cur.sawCR) {
						cur.offset++;
					}
					cur.sawCR = false;
				} else {
					cur.col++;
					cur.sawCR = false;
				}
				return tree;
			}

			if (isAppl(tree)) {
				boolean outermostLayout = false;
				IList args = getArgs(tree);

				if (isLayout(tree)) {
					inLayout = true;
					outermostLayout = true;
				}

				// TODO: Talk to Jurgen about this
//				if (!TreeAdapter.isLexToCf(tree)) {
					IListWriter newArgs = factory.listWriter(Factory.Tree);
					for (IValue arg : args) {
						newArgs
						.append(addPosInfo((IConstructor) arg, location,
								cur));
					}
					tree = tree.set("args", newArgs.done());
//				}
				
				if (!labelLayout && outermostLayout) {
					inLayout = false;
					if (! isComment(tree))
						return tree;
				} else if (!labelLayout && inLayout) {
					if (! isComment(tree))
						return tree;
				}
			} else if (isAmb(tree)) {
				ISet alts = getAlternatives(tree);
				ISetWriter newAlts = ValueFactoryFactory.getValueFactory()
						.setWriter(Factory.Tree);
				Position save = cur;
				Position newPos = save;
				ISetWriter cycles = ValueFactoryFactory.getValueFactory()
						.setWriter(Factory.Tree);

				for (IValue arg : alts) {
					cur = save.clone();

					IValue newArg = addPosInfo((IConstructor) arg, location,
							cur);

					if (cur.offset != save.offset) {
						newPos = cur;
						newAlts.insert(newArg);
					} else if (newPos.offset == save.offset) {
						cycles.insert(arg);
					} else {
						newAlts.insert(newArg);
					}
				}

				cur = save;
				cur.col = newPos.col;
				cur.line = newPos.line;
				cur.offset = newPos.offset;
				cur.sawCR = newPos.sawCR;

				for (IValue arg : cycles.done()) {
					IValue newArg = addPosInfo((IConstructor) arg, location,
							cur);
					newAlts.insert(newArg);
				}
				
				tree = tree.set("alternatives", newAlts.done());
			} else if (!isCycle(tree)) {
				throw new ImplementationError("unhandled tree: " + tree + "\n");
			}

			ISourceLocation loc = factory.sourceLocation(location, startOffset,
					cur.offset - startOffset, startLine, cur.line, startCol,
					cur.col);
			result = tree.setAnnotation(Factory.Location, loc);

			cache.putUnsafe(positionNode, result);

			return result;
		}

		private static class Position {
			public int col = 0;
			public int line = 1;
			public int offset = 0;
			public boolean sawCR = false;

			public Position clone() {
				Position tmp = new Position();
				tmp.col = col;
				tmp.line = line;
				tmp.offset = offset;
				tmp.sawCR = sawCR;
				return tmp;
			}

			@Override
			public String toString() {
				return "offset: " + offset + ", line: " + line + ", col:" + col
						+ ", sawCR:" + sawCR;

			}
		}

		private static class PositionNode {
			private final IConstructor tree;
			private final int offset;

			public PositionNode(IConstructor tree, int offset) {
				super();

				this.tree = tree;
				this.offset = offset;
			}

			public int hashCode() {
				return ((offset << 32) ^ tree.hashCode());
			}

			public boolean equals(Object o) {
				if (o.getClass() != getClass())
					return false;

				PositionNode other = (PositionNode) o;

				return (offset == other.offset && tree == other.tree); // NOTE:
																		// trees
																		// are
																		// shared,
																		// so
																		// they
																		// are
																		// pointer
																		// equal.
			}
		}
	}

	private static class Unparser extends IdentityTreeVisitor {
		private final OutputStream fStream;

		public Unparser(OutputStream stream) {
			fStream = stream;
		}

		@Override
		public IConstructor visitTreeAmb(IConstructor arg)
				throws VisitorException {
			((ISet) arg.get("alternatives")).iterator().next().accept(this);
			return arg;
		}

		@Override
		public IConstructor visitTreeChar(IConstructor arg)
				throws VisitorException {
			try {
				fStream.write(((IInteger) arg.get("character")).intValue());
				return arg;
			} catch (IOException e) {
				throw new VisitorException(e);
			}
		}

		@Override
		public IConstructor visitTreeAppl(IConstructor arg)
				throws VisitorException {
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

		if (TreeAdapter.isLexToCf(tree)) {
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

	public static IConstructor locateAnnotatedTree(IConstructor tree, String label, int offset) {
		ISourceLocation l = TreeAdapter.getLocation(tree);

		if (l == null) {
			throw new IllegalArgumentException(
					"locate assumes position information on the tree");
		}

		if (TreeAdapter.isLexToCf(tree)) {
			if (l.getOffset() <= offset
					&& offset < l.getOffset() + l.getLength()) {
				if (tree.hasAnnotation(label)) {
					return tree;
				}
				
				// we are in position, but no annotation, so zoom out
			}

			// we're at a leaf, but not in position (so zoom out)
			return null;
		}

		if (TreeAdapter.isAmb(tree)) {
			if (tree.hasAnnotation(label)) {
				return tree;
			}
			
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
					IConstructor result = locateAnnotatedTree((IConstructor) child, label, offset);

					if (result != null) {
						return result;
					}
				}
			}

			if (l.getOffset() <= offset
					&& l.getOffset() + l.getLength() >= offset) {
				if (tree.hasAnnotation(label)) {
					return tree;
				}
				
				// in scope, but no annotation, so zoom out
				return null;
			}
		}

		return null;
	}

	public static void unparse(IConstructor tree, OutputStream stream)
			throws IOException, FactTypeUseException {
		try {
			if (tree.getConstructorType() == Factory.ParseTree_Top) {
				tree.get("top").accept(new Unparser(stream));
			} else if (tree.getType() == Factory.Tree) {
				tree.accept(new Unparser(stream));
			} else {
				throw new ImplementationError("Can not unparse this "
						+ tree.getType());
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

	public static String yield(IConstructor tree) throws FactTypeUseException {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			unparse(tree, stream);
			return stream.toString();
		} catch (IOException e) {
			throw new ImplementationError("Method yield failed", e);
		}
	}

	public static boolean isContextFreeInjectionOrSingleton(IConstructor tree) {
		IConstructor prod = getProduction(tree);
		if (isAppl(tree)) {
			if (!ProductionAdapter.isList(prod)
					&& ProductionAdapter.getLhs(prod).length() == 1) {
				IConstructor rhs = ProductionAdapter.getRhs(prod);
				if (SymbolAdapter.isCf(rhs)) {
					rhs = SymbolAdapter.getSymbol(rhs);
					if (SymbolAdapter.isSort(rhs)) {
						return true;
					}
				}
			}
		} else if (isList(tree)
				&& SymbolAdapter.isCf(ProductionAdapter.getRhs(prod))) {
			if (getArgs(tree).length() == 1) {
				return true;
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
				IConstructor sym = ProductionAdapter.getRhs(prod);

				if (SymbolAdapter.isCf(sym) || SymbolAdapter.isLex(sym)) {
					sym = SymbolAdapter.getSymbol(sym);
				}

				if (SymbolAdapter.isIterStar(sym)
						|| SymbolAdapter.isIterStarSep(sym)) {
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
				IConstructor sym = ProductionAdapter.getRhs(prod);

				if (SymbolAdapter.isCf(sym) || SymbolAdapter.isLex(sym)) {
					sym = SymbolAdapter.getSymbol(sym);
				}

				if (SymbolAdapter.isIterPlus(sym)
						|| SymbolAdapter.isIterPlusSep(sym) 
						|| SymbolAdapter.isIterPlusSeps(sym)
						|| SymbolAdapter.isIterStarSeps(sym)) {
					return true;
				}
			}
		}
		return false;

	}

	public static boolean isCFList(IConstructor tree) {
		return isAppl(tree)
				&& isContextFree(tree)
				&& (SymbolAdapter.isPlusList(ProductionAdapter
						.getRhs(getProduction(tree))) || SymbolAdapter
						.isStarList(ProductionAdapter
								.getRhs(getProduction(tree))));
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

	public static boolean hasPreferAttribute(IConstructor tree) {
		return ProductionAdapter.hasPreferAttribute(getProduction(tree));
	}

	public static boolean hasAvoidAttribute(IConstructor tree) {
		return ProductionAdapter.hasAvoidAttribute(getProduction(tree));
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
		return ProductionAdapter.hasLexAttribute(getProduction(tree)); 
	}
}
