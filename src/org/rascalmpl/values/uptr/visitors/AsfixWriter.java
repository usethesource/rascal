package org.rascalmpl.values.uptr.visitors;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.values.uptr.TreeAdapter;

public class AsfixWriter extends IdentityTreeVisitor {

	private Writer writer;
	int nesting = 0;

	public AsfixWriter(Writer writer) {
		this.writer = writer;
		this.nesting = 0;
	}
	
	
	@Override
	public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
		try {
			writer.write("amb([\n");
			ISet set = TreeAdapter.getAlternatives(arg);
			int len = set.size();
			int i = 0;
			for (IValue x: set) {
				x.accept(this);
				if (i < len - 1) {
					writer.write(",\n");
				}
				i++;
			}
			writer.write("])");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IConstructor visitTreeAppl(IConstructor arg) throws VisitorException {
		arg = arg.removeAnnotations();
		try {
			writer.write("appl(");
			writer.write(TreeAdapter.getProduction(arg).toString());
			writer.write(", [");
			IList list = TreeAdapter.getArgs(arg);
			int len = list.length();
			int i = 0;
			nesting++;
			for (IValue x: list) {
				for (int j = 0; j < nesting; j++) {
//					writer.write(" ");
				}
				x.accept(this);
				if (i < len - 1) {
					writer.write(", \n");
				}
				i++;
			}
			nesting--;
			writer.write("])");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public IConstructor visitTreeChar(IConstructor arg) throws VisitorException {
		try {
			writer.write(new Integer(TreeAdapter.getCharacter(arg)).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arg;
	}

	@Override
	public IConstructor visitTreeCycle(IConstructor arg)  throws VisitorException {
		throw new UnsupportedOperationException("no support for cycles");
	}

	public IValue visitExternal(IExternalValue externalValue) {
		throw new UnsupportedOperationException();
	}
	
	public IConstructor visitTreeError(IConstructor arg) throws VisitorException{
		// TODO Implement, so error trees are unparseable.
		throw new UnsupportedOperationException();
	}
	
	public IConstructor visitTreeExpected(IConstructor arg) throws VisitorException{
		// Don't write anything.
		return null;
	}
}
