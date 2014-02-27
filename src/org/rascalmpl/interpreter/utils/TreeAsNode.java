package org.rascalmpl.interpreter.utils;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.values.uptr.TreeAdapter;

public class TreeAsNode implements INode {
  private final String name;
  private final IList args;

  public TreeAsNode(IConstructor tree) {
    this.name = TreeAdapter.getConstructorName(tree);
    this.args = TreeAdapter.isContextFree(tree) ? TreeAdapter.getASTArgs(tree) : TreeAdapter.getArgs(tree);
  }
  
  @Override
  public Type getType() {
    return TypeFactory.getInstance().nodeType();
  }

  @Override
  public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isEqual(IValue other) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IValue get(int i) throws IndexOutOfBoundsException {
    // TODO: this should deal with regular expressions in the "right" way, such as skipping 
    // over optionals and alternatives.
    return args.get(i);
  }

  @Override
  public INode set(int i, IValue newChild) throws IndexOutOfBoundsException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int arity() {
    return args.length();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Iterable<IValue> getChildren() {
    return args;
  }

  @Override
  public Iterator<IValue> iterator() {
    return args.iterator();
  }

  @Override
  public INode replace(int first, int second, int end, IList repl) throws FactTypeUseException,
      IndexOutOfBoundsException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAnnotatable() {
    return false;
  }

  @Override
  public IAnnotatable<? extends INode> asAnnotatable() {
    throw new IllegalOperationException(
        "Facade cannot be viewed as annotatable.", getType());
  }

  @Override
  public boolean mayHaveKeywordParameters() {
    return false;
  }

  @Override
  public IWithKeywordParameters<? extends INode> asWithKeywordParameters() {
    throw new IllegalOperationException(
        "Facade cannot be viewed as with keyword parameters.", getType());
  }

}