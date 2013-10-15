package org.eclipse.imp.pdb.facts.impl.primitive;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;


public class Chunk extends Atom {
	private static final IRascalValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final IString value;
	private final ISourceLocation origin;

	public Chunk(ISourceLocation origin, String value) {
		this(origin, StringValue.newString(value));
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
		// make new loc offset += start
		// length -= (length - end);
		ISourceLocation loc = getOrigin();
		int offset = loc.getOffset() + start;
		int length = end - start;
		int beginLine = loc.getBeginLine();
		int beginColumn = loc.getBeginColumn();
		for (int i = 0; i < start; i++) {
			int c = charAt(i);
			if (isLineBreak(c)) {
				beginLine++;
			}
			else {
				beginColumn++;
			}
		}
		int endLine = loc.getEndLine();
		int endColumn = loc.getEndColumn();
		for (int i = length() - 1; i > end; i--) {
			int c = charAt(i);
			if (isLineBreak(c)) {
				endLine--;
			}
			else {
				endColumn--;
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
	public boolean isEqual(IValue other) {
		return value.isEqual(other);
	}


	@Override
	public int compare(IString other) {
		return value.compare(other);
	}

	@Override
	public void accept(IOrgStringVisitor visitor) {
		visitor.visit(this);
	}


}

