package org.meta_environment.uptr;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.uptr.visitors.IdentityTreeVisitor;

public class Unparser {
	private static class Visitor extends IdentityTreeVisitor {
		private OutputStream fStream;

		public Visitor(OutputStream stream) {
			fStream = stream;
		}

		@Override
		public ITree visitTreeAmb(ITree arg) throws VisitorException {
			((ISet) arg.get("alternatives")).iterator().next().accept(this);
			return arg;
		}

		@Override
		public ITree visitTreeCharacter(ITree arg) throws VisitorException {
			try {
				fStream.write(((IInteger) arg.get("character")).getValue());
				return arg;
			} catch (IOException e) {
				throw new VisitorException(e);
			}
		}
	}
	
	public static void unparse(ITree tree, OutputStream stream) throws IOException, FactTypeError {
		try {
			if (tree.getTreeNodeType() == Factory.ParseTree_Top) {
				tree.get("top").accept(new Visitor(stream));
			} else if (tree.getType() == Factory.Tree) {
				tree.accept(new Visitor(stream));
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
}
