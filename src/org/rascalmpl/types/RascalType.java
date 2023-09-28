package org.rascalmpl.types;

import io.usethesource.vallang.type.ExternalType;
import io.usethesource.vallang.type.Type;

public abstract class RascalType extends ExternalType {
    protected final static RascalTypeFactory RTF = RascalTypeFactory.getInstance();
    public abstract <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> visitor) throws E;

    @Override
    protected Type lubWithExternal(Type type) {
        assert type instanceof RascalType;
        return lub((RascalType) type);
    }

    @Override
    protected Type glbWithExternal(Type type) {
        assert type instanceof RascalType;
        return glb((RascalType) type);
    }
    
    @Override
    protected boolean intersectsWithExternal(Type type) {
        assert type instanceof RascalType;
        System.err.println(this.toString() + "Intersects with " + type);
        return intersects((RascalType) type);
    }

    protected abstract Type lub(RascalType type);
    protected abstract Type glb(RascalType type);
    protected abstract boolean intersects(RascalType type);

    @Override
    protected boolean isSubtypeOfExternal(Type type) {
        assert type instanceof RascalType;
        return ((RascalType) type).isSupertypeOf(this);
    }

    protected abstract boolean isSupertypeOf(RascalType type);

    public boolean isSubtypeOfNonTerminal(RascalType type) {
        return false;
    }

    protected boolean isSubtypeOfFunction(RascalType type) {
        return false;
    }

    protected boolean isSubtypeOfReified(RascalType type) {
        return false;
    }

    protected Type lubWithNonTerminal(RascalType type) {
        return TF.valueType();
    }

    protected Type lubWithFunction(RascalType type) {
        return TF.valueType();
    }

    protected Type lubWithReified(RascalType type) {
        return TF.valueType();
    }

    protected Type glbWithNonTerminal(RascalType type) {
        return TF.voidType();
    }

    protected Type glbWithFunction(RascalType type) {
        return TF.voidType();
    }

    protected Type glbWithReified(RascalType type) {
        return TF.voidType();
    }

    
    protected boolean intersectsWithNonTerminal(RascalType type) {
        return false;
    }

    protected boolean intersectsWithFunction(RascalType type) {
        return false;
    }

    protected boolean intersectsWithReified(RascalType type) {
        return false;
    }
    
    public boolean isNonterminal() {
        return false;
    }

    public boolean isFunction() {
        return false;
    }

    public boolean isReified() {
        return false;
    }

    public boolean isRoleModifier() {
        return false;
    }

    public static boolean isNonterminal(Type type) {
        return type.isExternalType() && ((RascalType) type).isNonterminal();
    }

    public static boolean isReified(Type type) {
        return type.isExternalType() && ((RascalType) type).isReified();
    }

    public static boolean isFunction(Type type) {
        return type.isExternalType() && ((RascalType) type).isFunction();
    }

     public static boolean isRoleModifier(Type type) {
        return type.isExternalType() && ((RascalType) type).isRoleModifier();
    }

}
