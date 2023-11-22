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

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
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
        else if (arg instanceof NonTerminalType) {
            IConstructor symbol = ((NonTerminalType) arg).getSymbol();

            if (isSort(symbol) || isParameterizedSort(symbol)) {
                return applyToSyntax((NonTerminalType) arg);
            }
            else if (isLex(symbol) || isParameterizedLex(symbol)) {
                return applyToLexical((NonTerminalType) arg);
            }
            else if (isLayouts(symbol)) {
                return applyToLayout((NonTerminalType) arg);
            }
            else if (isKeyword(symbol)) {
                return applyToKeyword((NonTerminalType) arg);
            }
        }
        else if (arg.isAbstractData()) {
            return applyToData(arg);
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
        public String toString() {
            return "syntax[" + arg.toString() + "]";
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_SyntaxModifier, arg.asSymbol(vf, store, grammar, done));
        }

        @Override
        protected Type lubWithModifySyntax(RascalType type) {
            if (type instanceof Syntax) {
                return TF.modifyToSyntax(((ModifySyntaxRole) type).arg.lub(arg));
            }
            else if (type instanceof Lexical) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Layout) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Keyword) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Data) {
                return tf.nodeType();
            }

            return tf.nodeType();
        }

        @Override
        protected Type glbWithModifySyntax(RascalType type) {
            if (type instanceof Syntax) {
                return TF.modifyToSyntax(((ModifySyntaxRole) type).arg.glb(arg));
            }
            
            return tf.voidType();
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Syntax;
        }

        @Override
        protected boolean isSupertypeOf(RascalType type) {
            assert isOpen();

            if (type instanceof NonTerminalType) {
                 NonTerminalType nt = (NonTerminalType) type;
                 IConstructor sym = nt.getSymbol();

                 return SymbolAdapter.isSort(sym) || SymbolAdapter.isParameterizedSort(sym);
            }

            return false;
        }

        @Override
        public boolean isSubtypeOfNonTerminal(RascalType type) {
            assert isOpen(); // otherwise we wouldn't be here 

            NonTerminalType nt = (NonTerminalType) type;
            IConstructor sym = nt.getSymbol();

            return SymbolAdapter.isSort(sym) || SymbolAdapter.isParameterizedSort(sym);
        }

        @Override
        public boolean match(Type matched, Map<Type, Type> bindings) throws FactTypeUseException {
            if (matched instanceof NonTerminalType) {
                IConstructor sym = ((NonTerminalType) matched).getSymbol();

                if (SymbolAdapter.isSort(sym) || SymbolAdapter.isParameterizedSort(sym)) {
                    return arg.match(matched, bindings);
                }
            }
            else if (matched.isBottom()) {
                return arg.match(matched, bindings);
            }

            return false;
        }

        @Override
        public Type instantiate(Map<Type, Type> bindings) {
            return TF.modifyToSyntax(arg.instantiate(bindings));
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
        public String toString() {
            return "lexical[" + arg.toString() + "]";
        }

        @Override
        protected boolean isSupertypeOf(RascalType type) {
            assert isOpen();

            if (type instanceof NonTerminalType) {
                 NonTerminalType nt = (NonTerminalType) type;
                 IConstructor sym = nt.getSymbol();

                 return SymbolAdapter.isLex(sym) || SymbolAdapter.isParameterizedLex(sym);
            }

            return false;
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
        protected Type lubWithModifySyntax(RascalType type) {
            if (type instanceof Syntax) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Lexical) {
                return TF.modifyToLexical(((ModifySyntaxRole) type).arg.lub(arg));
            }
            else if (type instanceof Layout) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Keyword) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Data) {
                return tf.nodeType();
            }

            return tf.nodeType();
        }

         @Override
        protected Type glbWithModifySyntax(RascalType type) {
            if (type instanceof Lexical) {
                return TF.modifyToLexical(((ModifySyntaxRole) type).arg.glb(arg));
            }

            return tf.voidType();
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
        public boolean match(Type matched, Map<Type, Type> bindings) throws FactTypeUseException {
            if (matched instanceof NonTerminalType) {
                IConstructor sym = ((NonTerminalType) matched).getSymbol();

                if (SymbolAdapter.isLex(sym) || SymbolAdapter.isParameterizedLex(sym)) {
                    return arg.match(matched, bindings);
                }
            }
            else if (matched.isBottom()) {
                return arg.match(matched, bindings);
            }

            return false;
        }

        @Override
        public Type instantiate(Map<Type, Type> bindings) {
            return TF.modifyToLexical(arg.instantiate(bindings));
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
        public String toString() {
            return "layout[" + arg.toString() + "]";
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Layout;
        }

        @Override
        protected boolean isSupertypeOf(RascalType type) {
            assert isOpen();

            if (type instanceof NonTerminalType) {
                 NonTerminalType nt = (NonTerminalType) type;
                 IConstructor sym = nt.getSymbol();

                 return SymbolAdapter.isLayouts(sym);
            }

            return false;
        }


        @Override
        protected Type lubWithModifySyntax(RascalType type) {
            if (type instanceof Syntax) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Lexical) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Layout) {
                return TF.modifyToLayout(((ModifySyntaxRole) type).arg.lub(arg));
                
            }
            else if (type instanceof Keyword) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Data) {
                return tf.nodeType();
            }

            return tf.nodeType();
        }

        @Override
        protected Type glbWithModifySyntax(RascalType type) {
            if (type instanceof Layout) {
                return TF.modifyToLayout(((ModifySyntaxRole) type).arg.glb(arg));  
            }
            
            return tf.voidType();
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
        public boolean match(Type matched, Map<Type, Type> bindings) throws FactTypeUseException {
            if (matched instanceof NonTerminalType) {
                IConstructor sym = ((NonTerminalType) matched).getSymbol();

                if (SymbolAdapter.isLayouts(sym)) {
                    return arg.match(matched, bindings);
                }
            }
            else if (matched.isBottom()) {
                return arg.match(matched, bindings);
            }

            return false;
        }

        @Override
        public Type instantiate(Map<Type, Type> bindings) {
            return TF.modifyToLayout(arg.instantiate(bindings));
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
        public String toString() {
            return "keyword[" + arg.toString() + "]";
        }

        @Override
        public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done) {
            return vf.constructor(RascalValueFactory.Symbol_KeywordModifier, arg.asSymbol(vf, store, grammar, done));
        }

        @Override
        protected Type lubWithModifySyntax(RascalType type) {
            if (type instanceof Syntax) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Lexical) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Layout) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Keyword) {
                 return TF.modifyToKeyword(((ModifySyntaxRole) type).arg.lub(arg));            
            }
            else if (type instanceof Data) {
                return tf.nodeType();
            }

            return tf.nodeType();
        }

        @Override
        protected Type glbWithModifySyntax(RascalType type) {
            if (type instanceof Keyword) {
                 return TF.modifyToKeyword(((ModifySyntaxRole) type).arg.glb(arg));            
            }
            

            return tf.voidType();
        }

        @Override
        protected boolean isSupertypeOf(RascalType type) {
            if (type instanceof NonTerminalType) {
                 NonTerminalType nt = (NonTerminalType) type;
                 IConstructor sym = nt.getSymbol();

                 return SymbolAdapter.isKeyword(sym);
            }

            return false;
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
        public boolean match(Type matched, Map<Type, Type> bindings) throws FactTypeUseException {
            if (matched instanceof NonTerminalType) {
                IConstructor sym = ((NonTerminalType) matched).getSymbol();

                if (SymbolAdapter.isKeyword(sym)) {
                    return arg.match(matched, bindings);
                }
            }
            else if (matched.isBottom()) {
                return arg.match(matched, bindings);
            }

            return false;
        }

        @Override
        public Type instantiate(Map<Type, Type> bindings) {
            return TF.modifyToKeyword(arg.instantiate(bindings));
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
        public String toString() {
            return "data[" + arg.toString() + "]";
        }

        @Override
        public boolean isSubtypeOfSyntaxModifier(RascalType type) {
            assert isOpen();
            return type instanceof Data;
        }

         @Override
        protected boolean isSupertypeOf(RascalType type) {
            assert isOpen();
            // Here is it essential that {@link NonTerminalType#isAbstractData()} returns true.
            return type.isAbstractData(); 
        }

        @Override
        protected Type lubWithModifySyntax(RascalType type) {
            if (type instanceof Syntax) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Lexical) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Layout) {
                return RascalValueFactory.Tree;
            }
            else if (type instanceof Keyword) {
                 return RascalValueFactory.Tree;
            }
            else if (type instanceof Data) {
                return this;
            }

            return tf.nodeType();
        }

        @Override
        protected Type glbWithModifySyntax(RascalType type) {
            if (type instanceof Data) {
                return this;
            }

            return tf.voidType();
        }

        @Override
        protected boolean isSupertypeOf(Type type) {
            assert isOpen();
            return type.isBottom() || type.isAbstractData();
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
        public boolean match(Type matched, Map<Type, Type> bindings) throws FactTypeUseException {
            if (matched instanceof NonTerminalType) {
                // here a `syntax` or `lexical` or `keyword` or `layout` would match against a
                // literal data[&T], and we need the &T to bind with `Tree` and not `Statement` or
                // some non-terminal name. Note that this is correct because `Tree` is an
                // algebraic `data` type.
                return arg.match(RascalValueFactory.Tree, bindings);
            }
            else if (matched.isAbstractData() || matched.isBottom()) {
                return arg.match(matched, bindings);    
            }
            
            return false;
        }

        @Override
        public Type instantiate(Map<Type, Type> bindings) {
            return TF.modifyToData(arg.instantiate(bindings));
        }

        @Override
        public Type applyToSyntax(NonTerminalType arg) {
            String name = SymbolAdapter.getName(arg.getSymbol());

            if (arg.isParameterized()) {
                Type[] params = SymbolAdapter.getParameters(arg.getSymbol()).stream().map(c -> TF.nonTerminalType((IConstructor) c)).toArray(Type[]::new);
                return tf.abstractDataType(new TypeStore(), name, params);
            }
            else {
                return tf.abstractDataType(new TypeStore(), name);
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
            return tf.abstractDataType(new TypeStore(), name);
        }

        @Override
        public Type applyToData(Type arg) {
            return arg;
        }    
        
        @Override
        protected Type lubWithAbstractData(Type type) {
            return type.lub(arg);
        }

        @Override
        protected Type glbWithAbstractData(Type type) {
            return type.glb(arg);
        }
    }

    @Override
    public boolean isOpen() {
        return arg.isOpen();
    }

    @Override
    protected Type lubWithAbstractData(Type type) {
        // this is for all the syntax roles that are not data. Data overrides this
        if (type == RascalValueFactory.Tree) {
            return type;
        }

        return tf.valueType();
    }

    @Override
	protected Type glbWithAbstractData(Type type) {
        // this is for all the syntax roles that are not data. Data overrides this
	  return type == RascalValueFactory.Tree ? this : tf.voidType(); 
	}

    @Override
    abstract protected boolean isSupertypeOf(RascalType type);

    @Override
    protected boolean isSubtypeOfNode(Type type) {
        // all syntax roles are sub-type of node
       return true;
    }

    @Override
    protected Type lub(RascalType type) {
        return type.lubWithModifySyntax(this);
    }

    @Override
    protected Type glb(RascalType type) {
        return type.glbWithModifySyntax(this);
    }

    @Override
    abstract protected Type glbWithModifySyntax(RascalType type);

    @Override
    protected boolean intersects(RascalType type) {
        return type.intersectsWithModifySyntax(this);
    }

    @Override
    protected boolean intersectsWithModifySyntax(RascalType type) {
        // since we have only one parameter, intersection coincides with subtype comparability
        return this.comparable(type);
    }

    @Override
    public Type asAbstractDataType() {
        return RascalValueFactory.Symbol;
    }

    @Override
    abstract public IConstructor asSymbol(IValueFactory vf, TypeStore store, ISetWriter grammar, Set<IConstructor> done);

    @Override
    abstract public Type instantiate(Map<Type, Type> bindings);

    @Override
    abstract public boolean match(Type matched, Map<Type, Type> bindings) throws FactTypeUseException;

    @Override
    abstract public String toString();

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
