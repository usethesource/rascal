package org.rascalmpl.types;

import static org.rascalmpl.values.parsetrees.SymbolAdapter.isKeyword;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isLayouts;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isLex;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isParameterizedLex;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isParameterizedSort;
import static org.rascalmpl.values.parsetrees.SymbolAdapter.isSort;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.rascalmpl.ast.Nonterminal;
import org.rascalmpl.ast.SyntaxRoleModifier;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * A ModifySyntaxRole is a lazy modification operation on a Type that
 * preserves its name, but not its syntax role. The lazyness is of essence
 * due to type parameters: for `syntax[&T]` we must remember to modify
 * to the `syntax` role until after the substitution of the type parameter.
 * A lazy role has an effect during pattern matching, where it filters for
 * types that have a certain role (e.g. `syntax[&T] _` only matches non-terminals
 * that are not lexical, layout or keywords).
 * 
 * However, once a type parameter is made concrete, then ModifySyntaxRole
 * should be `apply`ed. The canonical form of a ModifySyntaxRole for non-open
 * types is the actually modified type representation. So a NonterminalType
 * or an AbstractDatatype, etc. It is absolutely essential that the _apply_
 * method is executed by the RascalTypeFactory and by the `Type.instantiate`
 * implementations below to guarantee the canonical forms.
 */
public abstract class ModifySyntaxRole extends RascalType {
    private final static RascalTypeFactory TF = RascalTypeFactory.getInstance();
    private final static TypeFactory tf = TypeFactory.getInstance();

    /**
     * This is the type to be modified.
     */
    protected final Type arg;
    
    public ModifySyntaxRole(Type param) {
        this.arg = param;
    }

    /**
     * `apply` modifies the parameter type to "become" a type that is indicated
     * by the modifier class. This happens at construction time, when the
     * type is made: syntax[data[X]] => syntax[X], and also after type-parameter
     * substitution. 
     * 
     * Because there is a case distinction on both the receiver and the parameter
     * type, we use the "double dispatch" design pattern to quickly reduce any
     * combination of modifier and type kinds to the right result. 
     * 
     * Typically `apply` guarantees "name preservation". So the name of the
     * resulting type is equal to the name of the parameter type.
     */
    public Type apply() {
        if (arg.isParameter()) {
            return this;
        }
        else if (arg instanceof ModifySyntaxRole) {
            return applyToRole((ModifySyntaxRole) arg);
        }
        else if (arg.isAbstractData()) {
            return applyToData(arg);
        }
        else if (arg instanceof NonTerminalType) {
            Type kind = ((NonTerminalType) arg).getSymbol().getConstructorType();

            if (kind == RascalValueFactory.Symbol_Sort || kind == RascalValueFactory.Symbol_ParameterizedSort) {
                return applyToSyntax((NonTerminalType) arg);
            }
            else if (kind == RascalValueFactory.Symbol_Lex || kind == RascalValueFactory.Symbol_ParameterizedLex) {
                return applyToLexical((NonTerminalType) arg);
            }
            else if (kind == RascalValueFactory.Symbol_Layouts) {
                return applyToLayout((NonTerminalType) arg);
            }
            else if (kind == RascalValueFactory.Symbol_Keywords) {
                return applyToKeyword((NonTerminalType) arg);
            }
        }
        
        // this should not happen, but for robustness sake we return the unmodified type.
        assert false  : "can not modify " + arg + " to " + this; 
        
        // `data[int] => int`
        return arg;
    }

    protected abstract Type applyToRole(ModifySyntaxRole role);
    protected abstract Type applyToSyntax(NonTerminalType role);
    protected abstract Type applyToLexical(NonTerminalType role);
    protected abstract Type applyToLayout(NonTerminalType role);
    protected abstract Type applyToKeyword(NonTerminalType role);
    protected abstract Type applyToData(Type role);

    /** this represents `syntax[&T]` */
    public static class Syntax extends ModifySyntaxRole {
        public Syntax(Type arg) {
            super(arg);
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_SyntaxModifier, arg.asSymbol(vf, store, grammar, done));
        }

        
        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Syntax;
        }

        @Override
        public boolean isSubtypeOfNonTerminal(RascalType type) {
            assert isOpen(); // otherwise we wouldn't be here 

            NonTerminalType nt = (NonTerminalType) type;
            IConstructor sym = nt.getSymbol();

            return SymbolAdapter.isSort(sym) || SymbolAdapter.isParameterizedSort(sym);
        }

        @Override
        protected boolean isSubtypeOfAbstractData(Type type) {
            // syntax[T] <: Tree
            return type == RascalValueFactory.Tree;
        }

        @Override
        protected Type applyToRole(ModifySyntaxRole role) {
            return TF.modifyToSyntax(role.arg);
        }

        @Override
        public Type applyToSyntax(NonTerminalType arg) {
            return arg;
        }

        @Override
        public Type applyToLexical(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            if (arg.isParameterized()) {
                return TF.syntaxType(name, SymbolAdapter.getParameters(arg.getSymbol()));
            }
            else {
                return TF.syntaxType(name);
            }
        }

        @Override
        public Type applyToKeyword(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.syntaxType(name);
        }

        @Override
        public Type applyToLayout(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.syntaxType(name);
        }

        @Override
        public Type applyToData(Type arg) {
            String name = arg.getName();

            if (arg.isParameterized()) {
                return TF.syntaxType(name, StreamSupport.stream(arg.getTypeParameters().spliterator(), false).toArray(Type[]::new));
            }
            else {
                return TF.syntaxType(name);
            }
        }

        @Override
        public String getName() {
            return SymbolAdapter.getName(((NonTerminalType) arg).getSymbol());
        }
    }
 
    /** this represents `lexical[&T]` */
    public static class Lexical extends ModifySyntaxRole {
        public Lexical(Type arg) {
            super(arg);
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Lexical;
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_LexicalModifier, arg.asSymbol(vf, store, grammar, done));
        }

        @Override
        public boolean isSubtypeOfNonTerminal(RascalType type) {
            assert isOpen(); // otherwise we wouldn't be here 

            NonTerminalType nt = (NonTerminalType) type;
            IConstructor sym = nt.getSymbol();

            return SymbolAdapter.isLex(sym) || SymbolAdapter.isParameterizedLex(sym);
        }

        @Override
        protected boolean isSubtypeOfAbstractData(Type type) {
            // syntax[T] <: Tree
            return type == RascalValueFactory.Tree;
        }

        @Override
        protected Type applyToRole(ModifySyntaxRole role) {
            return TF.modifyToLexical(role.arg);
        }

        @Override
        public Type applyToLexical(NonTerminalType arg) {
            return arg;
        }

        @Override
        public Type applyToSyntax(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            if (arg.isParameterized()) {
                return TF.lexicalType(name, SymbolAdapter.getParameters(arg.getSymbol()));
            }
            else {
                return TF.lexicalType(name);
            }
        }

        @Override
        public Type applyToKeyword(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.lexicalType(name);
        }

        @Override
        public Type applyToLayout(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.lexicalType(name);
        }

        @Override
        public Type applyToData(Type arg) {
            String name = arg.getName();

            if (arg.isParameterized()) {
                return TF.lexicalType(name, StreamSupport.stream(arg.getTypeParameters().spliterator(), false).toArray(Type[]::new));
            }
            else {
                return TF.lexicalType(name);
            }
        }

        @Override
        public String getName() {
            return SymbolAdapter.getName(((NonTerminalType) arg).getSymbol());
        }
    }
    public static class Layout extends ModifySyntaxRole {
        public Layout(Type arg) {
            super(arg);
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Layout;
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_LayoutModifier, arg.asSymbol(vf, store, grammar, done));
        }

        @Override
        public boolean isSubtypeOfNonTerminal(RascalType type) {
            assert isOpen(); // otherwise we wouldn't be here 

            NonTerminalType nt = (NonTerminalType) type;
            IConstructor sym = nt.getSymbol();

            return SymbolAdapter.isLayouts(sym);
        }

        @Override
        protected boolean isSubtypeOfAbstractData(Type type) {
            // syntax[T] <: Tree
            return type == RascalValueFactory.Tree;
        }

        @Override
        protected Type applyToRole(ModifySyntaxRole role) {
            return TF.modifyToLayout(role.arg);
        }

        @Override
        public Type applyToSyntax(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            assert !arg.isParameterized() : "layout is never parameterized";
            
            return TF.layoutType(name);
        }

        @Override
        public Type applyToLexical(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            assert !arg.isParameterized() : "layout is never parameterized";
            
            return TF.layoutType(name);
        }

        @Override
        public Type applyToKeyword(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.layoutType(name);
        }

        @Override
        public Type applyToLayout(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.layoutType(name);
        }

        @Override
        public Type applyToData(Type arg) {
            String name = arg.getName();

            assert !arg.isParameterized() : "layout is never parameterized";
            
            return TF.layoutType(name);
        }

        @Override
        public String getName() {
            return SymbolAdapter.getName(((NonTerminalType) arg).getSymbol());
        }
    }
    public static class Keyword extends ModifySyntaxRole {
        public Keyword(Type arg) {
            super(arg);
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_KeywordModifier, arg.asSymbol(vf, store, grammar, done));
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Keyword;
        }

        @Override
        public boolean isSubtypeOfNonTerminal(RascalType type) {
            assert isOpen(); // otherwise we wouldn't be here 

            NonTerminalType nt = (NonTerminalType) type;
            IConstructor sym = nt.getSymbol();

            return SymbolAdapter.isKeyword(sym);
        }

        @Override
        protected boolean isSubtypeOfAbstractData(Type type) {
            // syntax[T] <: Tree
            return type == RascalValueFactory.Tree;
        }

        @Override
        protected Type applyToRole(ModifySyntaxRole role) {
            return TF.modifyToKeyword(role.arg);
        }

        @Override
        public Type applyToSyntax(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            assert !arg.isParameterized() : "keyword is never parameterized";

            return TF.keywordType(name);
        }

        @Override
        public Type applyToLexical(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            assert !arg.isParameterized() : "keyword is never parameterized";

            return TF.keywordType(name);
        }

        @Override
        public Type applyToKeyword(NonTerminalType arg) {
            return arg;
        }

        @Override
        public Type applyToLayout(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.keywordType(name);
        }

        @Override
        public Type applyToData(Type arg) {
            String name = arg.getName();

            assert !arg.isParameterized() : "keyword is never parameterized";
  
            return TF.keywordType(name);
        }
    }
    public static class Data extends ModifySyntaxRole {
        public Data(Type arg) {
            super(arg);
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Data;
        }

        @Override
        protected boolean isSupertypeOf(Type type) {
            return type.isSubtypeOf(this);
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_DataModifier, arg.asSymbol(vf, store, grammar, done));
        }

        @Override
        protected Type applyToRole(ModifySyntaxRole role) {
            return TF.modifyToData(role.arg);
        }

        @Override
        protected boolean isSubtypeOfAbstractData(Type type) {
            assert isOpen();
            return true;
        }

        @Override
        public Type applyToSyntax(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            if (arg.isParameterized()) {
                Type[] params = SymbolAdapter.getParameters(arg.getSymbol()).stream().map(c -> TF.nonTerminalType((IConstructor) c)).toArray(Type[]::new);
                return TypeFactory.getInstance().abstractDataType(new TypeStore(), name, params);
            }
            else {
                return TypeFactory.getInstance().abstractDataType(new TypeStore(), name);
            }
        }

        @Override
        public Type applyToLexical(NonTerminalType arg) {
            return applyToSyntax(arg);
        }

        @Override
        public Type applyToKeyword(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TF.syntaxType(name);
        }

        @Override
        public Type applyToLayout(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());
            return TypeFactory.getInstance().abstractDataType(new TypeStore(), name);
        }

        @Override
        public Type applyToData(Type arg) {
            return arg;
        }       
    }

    @Override
    protected boolean isSupertypeOf(RascalType type) {
        return type.isSubtypeOfSyntaxModifier(this);
    }

    @Override
    protected boolean isSubtypeOfNode(Type type) {
        // all syntax roles are sub-type of node
       return true;
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
    public Type asAbstractDataType() {
        return RascalValueFactory.Symbol;
    }

    @Override
    abstract public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done);

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
