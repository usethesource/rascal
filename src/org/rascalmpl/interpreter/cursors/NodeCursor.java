package org.rascalmpl.interpreter.cursors;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;

public class NodeCursor extends Cursor implements INode {
	public NodeCursor(INode value) {
		super(value);
	}

	public NodeCursor(INode value, Context ctx) {
		super(value, ctx);
	}
	
	private INode getNode() {
		return (INode)getWrappedValue();
	}

	@Override
	public IValue get(int i) throws IndexOutOfBoundsException {
		Context ctx = new NodeIndexContext(getCtx(), i, getNode());
		return CursorFactory.makeCursor(getNode().get(i), ctx);
	}

	@Override
	public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
		return new NodeCursor(getNode().set(i, newChild), getCtx());
	}

	@Override
	public int arity() {
		return getNode().arity();
	}

	@Override
	public String getName() {
		return getNode().getName();
	}

	@Override
	public Iterable<IValue> getChildren() {
		return new Iterable<IValue>() {
			@Override
			public Iterator<IValue> iterator() {
				return NodeCursor.this.iterator();
			}
		};
	}

	@Override
	public Iterator<IValue> iterator() {
		return new Iterator<IValue>() {
			Iterator<IValue> iter = getNode().iterator();
			int i = 0;
			
			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public IValue next() {
				return NodeCursor.this.get(i++);
			}

			@Override
			public void remove() {
			}
		};
	}

	@Override
	public INode replace(int first, int second, int end, IList repl)
			throws FactTypeUseException, IndexOutOfBoundsException {
		return new NodeCursor(getNode().replace(first, second, end, repl), getCtx());
	}
	
	@Override
	public IAnnotatable<? extends INode> asAnnotatable() {
		return getNode().asAnnotatable();
	}

	@Override
	public boolean mayHaveKeywordParameters() {
		return getNode().mayHaveKeywordParameters();
	}

	@Override
	public IWithKeywordParameters<? extends INode> asWithKeywordParameters() {
		return getNode().asWithKeywordParameters();
	}

}
