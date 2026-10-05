package org.rascalmpl.types;

import java.util.Map;
import java.util.Random;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.values.RascalFunctionValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeFactory.RandomTypesConfig;
import io.usethesource.vallang.type.TypeStore;

/**
 * This is the result of `lub` of AbstractDataTypes, a sorts, lexicals, layouts or keywords.
 * We don't know temporarily what kind of syntax it is, but we do still know its name
 */
public class NamedPlaceholder extends RascalType {
    private final String name;
    private final boolean isNonterminal;

    // TODO: what about possible type parameters?

    public NamedPlaceholder(String name, boolean isNonterminal) {
        this.name = name;
        this.isNonterminal = isNonterminal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNamedPlaceholder() {
        return true;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o instanceof NamedPlaceholder) {
            NamedPlaceholder p = (NamedPlaceholder) o;
            return p.getName().equals(name) 
                && isNonterminal == p.isNonterminal;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 91 + name.hashCode() + 131 * Boolean.hashCode(isNonterminal);
    }

    @Override
    protected Type lubWithNamedPlaceholder(RascalType type) {
        if (type.isNamedPlaceholder()) {
            if (type.getName().equals(name)) {
                return this;
            }   
        }

        return TypeFactory.getInstance().nodeType();
    }

    @Override
    protected Type glbWithNamedPlaceholder(RascalType type) {
        return TypeFactory.getInstance().voidType();
    }

    @Override
    protected boolean intersectsWithNamedPlaceholder(RascalType type) {
        // TODO: how about node and value?
        if (type.isNamedPlaceholder()) {
            return true;   
        }

        return false;
    }


    @Override
    public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E {
       return visitor.visitNamedPlaceHolder(this);
    }

    @Override
    protected Type lub(RascalType type) {
        if (type == this) {
            return this;
        }   

        return type.lubWithNamedPlaceholder(this);
    }

    @Override
    protected Type glb(RascalType type) {
        if (type == this) {
            return this;
        }   

        return type.glbWithNamedPlaceholder(this);
    }

    @Override
    public boolean intersects(Type other) {
        if (other instanceof RascalType) {
            return intersects((RascalType) other);
        }
        
        else if (other.isNode()) {
            return true;
        }
        else if (other.isTop()) {
            return true;
        }
        
        return false;
    }

    @Override
    protected boolean intersects(RascalType other) {
        if (other == this) {
            return true;
        }   
        else if (isNonterminal && other instanceof NonTerminalType) {
            return true;
        }
        
        return other.intersectsWithNamedPlaceholder(this);
    }


    @Override
    public boolean isSubtypeOfNonTerminal(RascalType type) {
        return false;
    }

    @Override
    protected boolean isSupertypeOf(RascalType type) {
        // TODO check if this is correct
        return isSupertypeOf((Type) type);
    }

    @Override
    protected boolean isSupertypeOf(Type type) {
        if (SymbolAdapter.isNamed(((NonTerminalType) type).getSymbol())) {
            return SymbolAdapter.getName(((NonTerminalType) type).getSymbol()).equals(name);
        }
        else if (type.isAbstractData()) {
            return type.getName().equals(name);
        }
    
        return type.isSubtypeOf(this);
    }

    @Override
    public Type asAbstractDataType() {
        throw new NotYetImplemented("this should not happen?");
    }

    @Override
    protected Type lubWithAbstractData(Type type) {
        if (getName().equals(type.getName())) {
            return this;
        }

        return TypeFactory.getInstance().nodeType();
    }

    @Override
    protected Type lubWithNonTerminal(RascalType type) {
        NonTerminalType nt = (NonTerminalType) type;

        if (SymbolAdapter.getName(nt.getSymbol()).equals(getName())) {
            return this;
        }
        else if (isNonterminal) {
            return RascalFunctionValueFactory.Tree;
        }
        else {
            return TypeFactory.getInstance().nodeType();
        }
    }

    @Override
    public IValue randomValue(Random random, RandomTypesConfig typesConfig, IValueFactory vf, TypeStore store,
        Map<Type, Type> typeParameters, int maxDepth, int maxBreadth) {
        throw new UnsupportedOperationException("Unimplemented method 'randomValue' on named syntax placeholders");
    }
    
    @Override
    public String toString() {
        return "?role[" + name + "]";
    }
}
