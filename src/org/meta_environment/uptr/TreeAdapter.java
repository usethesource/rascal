package org.meta_environment.uptr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.uptr.visitors.IdentityTreeVisitor;

public class TreeAdapter {
	private IConstructor tree;
	private ProductionAdapter prod;
	
	public TreeAdapter(IConstructor tree) {
		if (tree.getType().getAbstractDataType() != Factory.Tree) {
			throw new FactTypeError("TreeWrapper will only wrap UPTR Trees, not " +  tree.getType());
		}
		this.tree = tree;
	}
	
	public boolean isAppl() {
		return tree.getType() == Factory.Tree_Appl;
	}
	
	public boolean isAmb() {
		return tree.getType() == Factory.Tree_Amb;
	}
	
	public boolean isChar() {
		return tree.getType() == Factory.Tree_Char;
	}
	
	public boolean isCycle() {
		return tree.getType() == Factory.Tree_Cycle;
	}
	
	public ProductionAdapter getProduction() {
		if (prod == null) {
		  prod = new ProductionAdapter((IConstructor) tree.get("prod"));
		}
		
		return prod;
	}
	
	public String getSortName() throws FactTypeError {
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
		else {
			throw new FactTypeError("this node has no args");
		}
	}
	
	public boolean isLiteral() {
		return isAppl() ? getProduction().isLiteral() : false;
	}
	
	
	public IList getListASTArgs() {
		if (!isContextFree() || !isList()) {
			throw new FactTypeError("This is not a context-free list production: "
					+ tree);
		}
		IList children = getArgs();
		IListWriter writer = Factory.Args.writer(ValueFactory.getInstance());
		
		for (int i = 0; i < children.length(); i++) {
			IValue kid = children.get(i);
			writer.append(kid);	
			// skip layout and/or separators
			i += (isSeparatedList() ? 3 : 1);
		}
		return writer.done();
	}
	
	public boolean isLexical() {
		return getProduction().isLexical();
	}

	private boolean isSeparatedList() {
		return isList() && getProduction().isSeparatedList();
	}

	public IList getASTArgs() {
		if (!isContextFree()) {
			throw new FactTypeError("This is not a context-free production: "
					+ tree);
		}

		IList children = getArgs();
		IListWriter writer = Factory.Args.writer(ValueFactory.getInstance());

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

	private boolean isCILiteral() {
		return isAppl() ? getProduction().isCILiteral() : false;
	}

	public ISet getAlternatives() {
		if (isAmb()) {
		  return (ISet) tree.get("alternatives");
		}
		else {
			throw new FactTypeError("this node has no alternatives");
		}
	}
	
	private static class Unparser extends IdentityTreeVisitor {
		private OutputStream fStream;

		public Unparser(OutputStream stream) {
			fStream = stream;
		}

		@Override
		public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
			((ISet) arg.get("alternatives")).iterator().next().accept(this);
			return arg;
		}

		@Override
		public IInteger visitTreeCharacter(IInteger arg) throws VisitorException {
			try {
				fStream.write(arg.getValue());
				return arg;
			} catch (IOException e) {
				throw new VisitorException(e);
			}
		}
		
		@Override
		public INode visitTreeAppl(IConstructor arg) throws VisitorException {
			IList children = (IList) arg.get("args");
			for (IValue child : children) {
				child.accept(this);
			}
			return arg;
		}
	}
	
	public void unparse(OutputStream stream) throws IOException, FactTypeError {
		try {
			if (tree.getType() == Factory.ParseTree_Top) {
				tree.get("top").accept(new Unparser(stream));
			} else if (tree.getType().getAbstractDataType() == Factory.Tree) {
				tree.accept(new Unparser(stream));
			} else {
				throw new FactTypeError("Can not unparse this "
						+ tree.getType());
			}
		} catch (VisitorException e) {
			Throwable cause = e.getCause();

			if (cause instanceof IOException) {
				throw (IOException) cause;
			}
			else {
				System.err.println("Unexpected error in unparse: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public String yield() throws FactTypeError {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			unparse(stream);
			return stream.toString();
		} catch (IOException e) {
			throw new FactTypeError("yield failed", e);
		}
	}


}
