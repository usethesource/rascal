package org.rascalmpl.interpreter.utils;

import java.util.Iterator;

import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

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
    public boolean match(IValue other) {
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
    public INode setChildren(IValue[] childArray) {
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
    public boolean mayHaveKeywordParameters() {
        return tree.mayHaveKeywordParameters();
    }

    @Override
    public IWithKeywordParameters<? extends INode> asWithKeywordParameters() {
        return tree.asWithKeywordParameters();
    }

}