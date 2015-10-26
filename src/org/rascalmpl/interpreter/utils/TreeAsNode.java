package org.rascalmpl.interpreter.utils;

import java.util.Iterator;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class TreeAsNode implements INode {
  private final String name;
  private final IList args;
  private final ITree tree;

  public TreeAsNode(ITree tree) {
    this.name = TreeAdapter.getConstructorName(tree);
    this.args = TreeAdapter.isContextFree(tree) ? TreeAdapter.getASTArgs(tree) : TreeAdapter.getArgs(tree);
    this.tree = tree;
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
    return tree.isAnnotatable();
  }

  @Override
  public IAnnotatable<? extends INode> asAnnotatable() {
    return tree.asAnnotatable();
  }

  @Override
  public boolean mayHaveKeywordParameters() {
    return false;
  }

  @Override
  public IWithKeywordParameters<? extends INode> asWithKeywordParameters() {
    return tree.asWithKeywordParameters();
  }

}