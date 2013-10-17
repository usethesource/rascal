package org.rascalmpl.values;

import org.apache.commons.lang.WordUtils;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.asserts.ImplementationError;


public class Chunk extends Atom {
	private final ISourceLocation origin;

	public Chunk(ISourceLocation origin, String value) {
		super(value);
		this.origin = origin;
	}

	private Chunk(ISourceLocation origin, IString str) {
		super(str);
		this.origin = origin;
	}

	public ISourceLocation getOrigin() {
		return origin;
	}
		
	@Override
	public IString reverse() {
		// What happens with origin?
		return new Chunk(origin, value.reverse());
	}


	@Override
	public IString substring(int start, int end) {
		ISourceLocation loc = getOrigin();
		int offset = loc.getOffset() + start;
		int length = end - start;
		int beginLine = loc.getBeginLine();
		int beginColumn = loc.getBeginColumn();
		for (int i = 0; i < start; i++) {
			int c = charAt(i);
			if (c == '\n') {
				beginLine++;
				beginColumn = 0;
			}
			else {
				beginColumn++;
			}
		}
		// This is not right yet....
		int endLine = beginLine;
		int endColumn = beginColumn;
		for (int i = start; i < end; i++) {
			int c = charAt(i);
			if (c == '\n') {
				endLine++;
				endColumn = 0;
			}
			else {
				endColumn++;
			}
		}
		return new Chunk(
		  vf.sourceLocation(loc, offset, length, beginLine, endLine, beginColumn, endColumn),
		  value.substring(start, end));
	}

	@Override
	public IString replace(int first, int second, int end, IString repl) {
		throw new ImplementationError("Not yet implemented");
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public OrgString capitalize() {
		if (length() == 0) {
			return this;
		}
		String s = getValue();
		return new Concat(
				new NoOrg(WordUtils.capitalize(s.substring(0, 1))),
				(OrgString)substring(1, length()));
	}

}

