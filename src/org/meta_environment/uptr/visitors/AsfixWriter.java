package org.meta_environment.uptr.visitors;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.uptr.TreeAdapter;

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
			ISet set = new TreeAdapter(arg).getAlternatives();
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
			writer.write(new TreeAdapter(arg).getProduction().tree.toString());
			writer.write(", [");
			IList list = new TreeAdapter(arg).getArgs();
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
			writer.write(new Integer(new TreeAdapter(arg).getCharacter()).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arg;
	}

	@Override
	public IConstructor visitTreeCycle(IConstructor arg)  throws VisitorException {
		throw new UnsupportedOperationException("no support for cycles");
	}


}
