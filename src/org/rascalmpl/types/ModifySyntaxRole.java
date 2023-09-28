package org.rascalmpl.types;

import static org.rascalmpl.values.parsetrees.SymbolAdapter.isKeyword;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isLayouts;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isLex;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isParameterizedLex;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isParameterizedSort;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isSort;

import java.util.Map;
import java.util.Random;

import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public abstract class ModifySyntaxRole extends RascalType {
    private final static RascalTypeFactory TF = RascalTypeFactory.getInstance();
    private final static TypeFactory tf = TypeFactory.getInstance();
    protected final Type arg;
    
    public ModifySyntaxRole(Type param) {
        this.arg = param;
    }

    public abstract Type apply();
    protected abstract Type applyFromSyntax(ModifySyntaxRole role);
    protected abstract Type applyFromLexical(ModifySyntaxRole role);
    protected abstract Type applyFromLayout(ModifySyntaxRole role);
    protected abstract Type applyFromKeyword(ModifySyntaxRole role);
    protected abstract Type applyFromData(ModifySyntaxRole role);

    /*
     * These methods convert
     */
    public abstract Type toSyntax();
    public abstract Type toLexical();
    public abstract Type toKeyword();
    public abstract Type toLayout();
    public abstract Type toData();

    /** this represents `syntax[&T]` */
    public static class Syntax extends ModifySyntaxRole {
        public Syntax(Type arg) {
            super(arg);
        }

        
        @Override
        public Type toSyntax() {
            return this;
        }

        @Override
        public Type toLexical() {
            NonTerminalType type = (NonTerminalType) arg;
            String name = getName();

            if (type.isParameterized()) {
                return TF.lexicalType(name, SymbolAdapter.getParameters(type.getSymbol()));
            }
            else {
                return TF.lexicalType(name);
            }
        }

        @Override
        public String getName() {
            return SymbolAdapter.getName(((NonTerminalType) arg).getSymbol());
        }

        @Override
        public Type toKeyword() {
            return TF.keywordType(getName(), new Type[0]);
        }

        @Override
        public Type toLayout() {
            return toKeyword();
        }

        @Override
        public Type toData() {
            NonTerminalType type = (NonTerminalType) arg;

            if (arg.isParameterized()) {
                return tf.abstractDataType(new TypeStore(), getName());
            }
            else {
                return tf.abstractDataType(new TypeStore(), getName(), SymbolAdapter.getParameters(type.getSymbol()).stream().map(c -> TF.nonTerminalType((IConstructor) c)).toArray(Type[]::new)));
            }
        }


        @Override
        public Type apply() {
            if (arg instanceof ModifySyntaxRole) {
                return ((ModifySyntaxRole) arg).applyFromSyntax(this);
            }
            else {
                // TODO take care of regular symbols
                return this;
            }
        }


        @Override
        protected Type applyFromSyntax(ModifySyntaxRole role) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'applyFromSyntax'");
        }


        @Override
        protected Type applyFromLexical(ModifySyntaxRole role) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'applyFromLexical'");
        }


        @Override
        protected Type applyFromLayout(ModifySyntaxRole role) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'applyFromLayout'");
        }


        @Override
        protected Type applyFromKeyword(ModifySyntaxRole role) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'applyFromKeyword'");
        }


        @Override
        protected Type applyFromData(ModifySyntaxRole role) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'applyFromData'");
        }
    }
 
    /** this represents `lexical[&T]` */
    public static class Lexical extends ModifySyntaxRole {
        public Lexical(Type arg) {
            super(arg);
        }

        @Override
        public String getName() {
            return SymbolAdapter.getName(((NonTerminalType) arg).getSymbol());
        }

        @Override
        public Type toSyntax() {
            NonTerminalType type = (NonTerminalType) arg;
            String name = getName();
            if (type.isParameterized()) {
                return TF.syntaxType(name, SymbolAdapter.getParameters(type.getSymbol()));
            }
            else {
                return TF.syntaxType(name);
            }
        }

        @Override
        public Type toLexical() {
            return this;
        }

        @Override
        public Type fromKeyword(Type arg) {
            NonTerminalType type = (NonTerminalType) arg;
            String name = SymbolAdapter.getName(type.getSymbol());
            return TF.lexicalType(name);
        }

        @Override
        public Type fromLayout(Type arg) {
            return fromKeyword(arg);
        }

        @Override
        public Type fromData(Type arg) {
            return TF.lexicalType(arg.getName());
        }
    }

    public static class Layout extends ModifySyntaxRole {
        public Layout(Type arg) {
            super(arg);
        }

        @Override
        public Type fromSyntax(Type arg) {
            NonTerminalType type = (NonTerminalType) arg;
            String name = SymbolAdapter.getName(type.getSymbol());
            if (type.isParameterized()) {
                return TF.layoutType(name, SymbolAdapter.getParameters(type.getSymbol()));
            }
            else {
                return TF.layoutType(name);
            }
        }

        @Override
        public Type fromLexical(Type arg) {
            return fromSyntax(arg);
        }

        @Override
        public Type fromKeyword(Type arg) {
            NonTerminalType type = (NonTerminalType) arg;
            String name = SymbolAdapter.getName(type.getSymbol());
            return TF.layoutType(name);
        }

        @Override
        public Type fromLayout(Type arg) {
            return arg;
        }

        @Override
        public Type fromData(Type arg) {
            return TF.lexicalType(arg.getName());
        }
    }

    public static class Keyword extends ModifySyntaxRole {
        public Keyword(Type arg) {
            super(arg);
        }

        @Override
        public Type fromSyntax(Type arg) {
            NonTerminalType type = (NonTerminalType) arg;
            String name = SymbolAdapter.getName(type.getSymbol());
            if (type.isParameterized()) {
                return TF.layoutType(name, SymbolAdapter.getParameters(type.getSymbol()));
            }
            else {
                return TF.layoutType(name);
            }
        }

        @Override
        public Type fromLexical(Type arg) {
            return fromSyntax(arg);
        }

        @Override
        public Type fromKeyword(Type arg) {
            NonTerminalType type = (NonTerminalType) arg;
            String name = SymbolAdapter.getName(type.getSymbol());
            return TF.layoutType(name);
        }

        @Override
        public Type fromLayout(Type arg) {
            return arg;
        }

        @Override
        public Type fromData(Type arg) {
            return TF.lexicalType(arg.getName());
        }
    }

    public static class Data extends ModifySyntaxRole {
        public Data(Type arg) {
            super(arg);
        }

        @Override
        public Type fromSyntax(Type arg) {
            NonTerminalType type = (NonTerminalType) arg;
            String name = SymbolAdapter.getName(type.getSymbol());

            if (type.isParameterized()) {
                return tf.abstractDataType(new TypeStore(), name, SymbolAdapter.getParameters(type.getSymbol()).stream().map(c -> TF.nonTerminalType((IConstructor) c)).toArray(Type[]::new));
            }
            else {
                return tf.abstractDataType(new TypeStore(), name);
            }
        }

        @Override
        public Type fromLexical(Type arg) {
            return fromSyntax(arg);
        }

        @Override
        public Type fromKeyword(Type arg) {
            return fromSyntax(arg);
        }

        @Override
        public Type fromLayout(Type arg) {
            return fromSyntax(arg);
        }

        @Override
        public Type fromData(Type arg) {
            return arg;
        }
    }

    public static class Other extends ModifySyntaxRole {
        public Other(Type arg) {
            super(arg);
        }

        @Override
        public Type fromSyntax(Type arg) {
            return arg;
        }

        @Override
        public Type fromLexical(Type arg) {
            return arg;
        }

        @Override
        public Type fromKeyword(Type arg) {
            return arg;
        }

        @Override
        public Type fromLayout(Type arg) {
            return arg;
        }

        @Override
        public Type fromData(Type arg) {
            return arg;
        }
    }

    public Type getParameter() {
        return param;
    }

    @Override
    public boolean isOpen() {
        return param.isOpen();
    }

    @Override
    protected Type lub(RascalType type) {
        return null;
    }

    @Override
    protected Type glb(RascalType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'glb'");
    }

    @Override
    protected boolean intersects(RascalType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'intersects'");
    }

    @Override
    protected boolean isSupertypeOf(RascalType type) {
        if (type.isRoleModifier()) {

        }
        else if (isOpen()) {

        }
        else {

        }
    }

    @Override
    public Type asAbstractDataType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'asAbstractDataType'");
    }

    @Override
    public IValue randomValue(Random random, IValueFactory vf, TypeStore store, Map<Type, Type> typeParameters,
        int maxDepth, int maxBreadth) {
        return arg.randomValue(random, vf, store, typeParameters, maxDepth, maxBreadth);
    }

    @Override
    public <T, E extends Throwable> T accept(IRascalTypeVisitor<T, E> v) throws E {
        return v.visitRoleModifier(this);
    }
}
