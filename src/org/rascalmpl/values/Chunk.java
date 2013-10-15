package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.asserts.ImplementationError;


public class Chunk extends Atom {
	private static final OriginValueFactory vf = (OriginValueFactory) ValueFactoryFactory.getValueFactory();
	
	private final IString value;
	private final ISourceLocation origin;

	public Chunk(ISourceLocation origin, String value) {
		this(origin, vf.baseString(value));
	}

	private Chunk(ISourceLocation origin, IString str) {
		this.origin = origin;
		this.value = str;
	}

	public ISourceLocation getOrigin() {
		return origin;
	}


	@Override
	public int length() {
		return value.length();
	}

	@Override
	public String getValue() {
		return value.getValue();
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
			if (isLineBreak(c)) {
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
			if (isLineBreak(c)) {
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


	private boolean isLineBreak(int c) {
		return c == '\n';
	}

	@Override
	public int charAt(int index) {
		return value.charAt(index);
	}


	@Override
	public IString replace(int first, int second, int end, IString repl) {
		throw new ImplementationError("Not yet implemented");
	}

	@Override
	public int compare(IString other) {
		return value.compare(other);
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int indexOf(String str) {
		return getValue().indexOf(str);
	}


}

