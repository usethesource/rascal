package org.meta_environment.uptr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.uptr.visitors.IdentityTreeVisitor;


public class TreeAdapter {
	private IConstructor tree;
	private ProductionAdapter prod;
	
	public TreeAdapter(IConstructor tree) {
		if (tree.getType() != Factory.Tree) {
			throw new ImplementationError("TreeWrapper will only wrap UPTR Trees, not " +  tree.getType());
		}
		this.tree = tree;
	}
	
	public boolean isAppl() {
		return tree.getConstructorType() == Factory.Tree_Appl;
	}
	
	public boolean isAmb() {
		return tree.getConstructorType() == Factory.Tree_Amb;
	}
	
	public boolean isChar() {
		return tree.getConstructorType() == Factory.Tree_Char;
	}
	
	public boolean isCycle() {
		return tree.getConstructorType() == Factory.Tree_Cycle;
	}
	
	public ProductionAdapter getProduction() {
		if (prod == null) {
		  prod = new ProductionAdapter((IConstructor) tree.get("prod"));
		}	
		return prod;
	}
	
	public boolean hasSortName() {
		return getProduction().hasSortName();
	}
	
	public String getSortName() throws FactTypeUseException {
		return getProduction().getSortName();
	}
	
	public String getConstructorName() {
		return getProduction().getConstructorName();
	}
	
	public boolean isProduction(String sortName, String consName) {
		ProductionAdapter prod = getProduction();
		return prod.getSortName().equals(sortName) &&
		prod.getConstructorName().equals(consName);
	}

	public boolean isLexToCf() {
		return isAppl() ? getProduction().isLexToCf() : false;
	}
	
	public boolean isContextFree() {
		return isAppl() ? getProduction().isContextFree() : false;
	}
	
	public boolean isList() {
		return isAppl() ? getProduction().isList() : false;
	}
	
	public IList getArgs() {
		if (isAppl()) {
		  return (IList) tree.get("args");
		}
		
		throw new ImplementationError("Node has no args");
	}
	
	public boolean isLiteral() {
		return isAppl() ? getProduction().isLiteral() : false;
	}
	
	
	public IList getListASTArgs() {
		if (!isContextFree() || !isList()) {
			throw new ImplementationError("This is not a context-free list production: "
					+ tree);
		}
		IList children = getArgs();
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory.getValueFactory());
		
		for (int i = 0; i < children.length(); i++) {
			IValue kid = children.get(i);
			writer.append(kid);	
			// skip layout and/or separators
			i += (isSeparatedList() ? 3 : 1);
		}
		return writer.done();
	}
	
	public boolean isLexical() {
		return isAppl() ? getProduction().isLexical() : false;
	}
	
	public boolean isLayout() {
		return isAppl() ? getProduction().isLayout() : false;
	}

	private boolean isSeparatedList() {
		return isAppl() ? isList() && getProduction().isSeparatedList() : false;
	}

	public IList getASTArgs() {
		if (!isContextFree()) {
			throw new ImplementationError("This is not a context-free production: "
					+ tree);
		}

		IList children = getArgs();
		IListWriter writer = Factory.Args.writer(ValueFactoryFactory.getValueFactory());

		for (int i = 0; i < children.length(); i++) {
			IValue kid = children.get(i);
			TreeAdapter treeAdapter = new TreeAdapter((IConstructor) kid);
			if (!treeAdapter.isLiteral() && !treeAdapter.isCILiteral()) {
				writer.append(kid);	
			} 
			// skip layout
			i++;
		}
		return writer.done();
	}

	public boolean isCILiteral() {
		return isAppl() ? getProduction().isCILiteral() : false;
	}
	
	

	public ISet getAlternatives() {
		if (isAmb()) {
		  return (ISet) tree.get("alternatives");
		}
		
		throw new ImplementationError("Node has no alternatives");
	}
	
	public IConstructor addPositionInformation(String filename) {
		Factory.getInstance(); // make sure everything is declared
		try {
			return addPosInfo(tree, filename, new Position());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ISourceLocation getLocation() {
		return (ISourceLocation) tree.getAnnotation(Factory.Location);
	}
	
	public String getPath() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getURL().getPath();
		}
		return null;
	}

	private class Position {
		public int col = 0;
		public int line = 1;
		public int offset = 0;
		
		@Override
		public Position clone() {
			Position tmp = new Position();
			tmp.col = col;
			tmp.line = line;
			tmp.offset = offset;
			return tmp;
		}
	}
	
	public int getCharacter() {
		return ((IInteger) tree.get("character")).intValue();
	}
	
	// TODO this code breaks in the presence of cycles
	private IConstructor addPosInfo(IConstructor t, String filename, Position cur) throws MalformedURLException {
		IValueFactory factory = ValueFactoryFactory.getValueFactory();
		TreeAdapter tree = new TreeAdapter(t);
		
		if (tree.isChar()) {
			int val = tree.getCharacter();
			
			if (val == '\n') {
				cur.col = 0;
				cur.line++;
				cur.offset++;
			}
			else if (val == '\r') {
				cur.offset++;
			}
			else {
				cur.col++;
				cur.offset++;
			}
			
			return t;
		}
		
		Position start = cur.clone();
		
		if (tree.isAppl()) {
			IList args = tree.getArgs();
			IListWriter newArgs = factory.listWriter(Factory.Tree);
			
			for (IValue arg : args) {
				newArgs.append(addPosInfo((IConstructor) arg, filename, cur));
			}
			
			t = t.set("args", newArgs.done());
		}
		else if (tree.isAmb()) {
			Position tmpCur = null; // there are always at least 2 alternatives
			
			ISet alts = tree.getAlternatives();
			assert alts.size() >= 2;
			ISetWriter newAlts = ValueFactoryFactory.getValueFactory().setWriter(Factory.Tree);
			
			for (IValue arg : alts) {
				tmpCur = start.clone();
				newAlts.insert(addPosInfo((IConstructor) arg, filename, tmpCur));
			}
			
			cur.col = tmpCur.col;
			cur.line = tmpCur.line;
			cur.offset = tmpCur.offset;
			t = t.set("alternatives", newAlts.done());
		}
		
		if (!tree.isLayout() && !tree.isLexical()) {
			ISourceLocation loc = factory.sourceLocation(new URL("file://" + filename), start.offset, cur.offset - start.offset, start.line, cur.line, start.col, cur.col);
			return t.setAnnotation(Factory.Location, loc);
		}
		
		return t;
	}
	
	private static class Unparser extends IdentityTreeVisitor {
		private final OutputStream fStream;

		public Unparser(OutputStream stream) {
			fStream = stream;
		}

		@Override
		public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
			((ISet) arg.get("alternatives")).iterator().next().accept(this);
			return arg;
		}

		@Override
		public IConstructor visitTreeChar(IConstructor arg) throws VisitorException {
			try {
				fStream.write(((IInteger) arg.get("character")).intValue());
				return arg;
			} catch (IOException e) {
				throw new VisitorException(e);
			}
		}
		
		@Override
		public IConstructor visitTreeAppl(IConstructor arg) throws VisitorException {
			IList children = (IList) arg.get("args");
			for (IValue child : children) {
				child.accept(this);
			}
			return arg;
		}

	}
	
	public void unparse(OutputStream stream) throws IOException, FactTypeUseException {
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
			
			System.err.println("Unexpected error in unparse: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String yield() throws FactTypeUseException {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			unparse(stream);
			return stream.toString();
		} catch (IOException e) {
			throw new ImplementationError("Method yield failed", e);
		}
	}

	public IConstructor getTree() {
		return tree;
	}

	public boolean isContextFreeInjectionOrSingleton() {
		if (isAppl()) {
			ProductionAdapter prod = getProduction();
			if (!prod.isList() && prod.getLhs().length() == 1) {
				SymbolAdapter rhs = prod.getRhs();
				if (rhs.isCf()) {
					rhs = rhs.getSymbol();
					if (rhs.isSort()) {
						return true;
					}
				}
			}
		}
		else if (isList() && getProduction().getRhs().isCf()) {
			if (getArgs().length() == 1) {
				return true;
			}
		}
		return false;
	}

	public boolean isAmbiguousList() {
		if (isAmb()) {
			IConstructor first = (IConstructor) getAlternatives().iterator().next();
			TreeAdapter tree = new TreeAdapter(first);
			if (tree.isList()) {
				return true;
			}
		}
		return false;
	}

	public boolean isNonEmptyStarList() {
		if (isAppl()) {
			ProductionAdapter prod = getProduction();
			
			if (prod.isList()) {
				SymbolAdapter sym = prod.getRhs();
				
				if (sym.isCf() || sym.isLex()) {
					sym = sym.getSymbol();
				}
				
				if (sym.isIterStar() || sym.isIterStarSep()) {
					return getArgs().length() > 0;
				}
			}
		}
		return false;
	}

	public boolean isCFList() {
		return isAppl() && isContextFree() && (getProduction().getRhs().isPlusList() ||
				getProduction().getRhs().isStarList());
	}

	/**
	 * @return true iff the tree does not have any characters, it's just an empty derivation
	 */
	public boolean isEpsilon() {
		// TODO: optimize this
//		return false;
		return yield().length() == 0;
	}

	public boolean hasPreferAttribute() {
		return getProduction().hasPreferAttribute();
	}

	public boolean hasAvoidAttribute() {
		return getProduction().hasAvoidAttribute();
	}
}
