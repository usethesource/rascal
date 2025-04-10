package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import org.rascalmpl.parser.gtd.stack.*;
import org.rascalmpl.parser.gtd.stack.filter.*;
import org.rascalmpl.parser.gtd.stack.filter.follow.*;
import org.rascalmpl.parser.gtd.stack.filter.match.*;
import org.rascalmpl.parser.gtd.stack.filter.precede.*;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;

@SuppressWarnings("all")
public class Sequence4 extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, ITree, ISourceLocation>
    implements IParserTest {
    protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();

    protected static IValue _read(java.lang.String s, io.usethesource.vallang.type.Type type) {
        try {
            return new StandardTextReader().read(VF, org.rascalmpl.values.RascalValueFactory.uptr, type,
                new StringReader(s));
        }
        catch (FactTypeUseException e) {
            throw new RuntimeException("unexpected exception in generated parser", e);
        }
        catch (IOException e) {
            throw new RuntimeException("unexpected exception in generated parser", e);
        }
    }

    protected static java.lang.String _concat(java.lang.String... args) {
        int length = 0;
        for (java.lang.String s : args) {
            length += s.length();
        }
        java.lang.StringBuilder b = new java.lang.StringBuilder(length);
        for (java.lang.String s : args) {
            b.append(s);
        }
        return b.toString();
    }

    protected static final TypeFactory _tf = TypeFactory.getInstance();

    private static final IntegerMap _resultStoreIdMappings;
    private static final IntegerKeyedHashMap<IntegerList> _dontNest;

    protected static void _putDontNest(IntegerKeyedHashMap<IntegerList> result, int parentId, int childId) {
        IntegerList donts = result.get(childId);
        if (donts == null) {
            donts = new IntegerList();
            result.put(childId, donts);
        }
        donts.add(parentId);
    }

    protected int getResultStoreId(int parentId) {
        return _resultStoreIdMappings.get(parentId);
    }

    protected static IntegerKeyedHashMap<IntegerList> _initDontNest() {
        IntegerKeyedHashMap<IntegerList> result = new IntegerKeyedHashMap<IntegerList>();



        return result;
    }

    protected static IntegerMap _initDontNestGroups() {
        IntegerMap result = new IntegerMap();
        int resultStoreId = result.size();



        return result;
    }

    protected boolean hasNestingRestrictions(java.lang.String name) {
        return (_dontNest.size() != 0); // TODO Make more specific.
    }

    protected IntegerList getFilteredParents(int childId) {
        return _dontNest.get(childId);
    }

    // initialize priorities
    static {
        _dontNest = _initDontNest();
        _resultStoreIdMappings = _initDontNestGroups();
    }

    // Production declarations

    private static final IConstructor NONTERMINAL_X =
        (IConstructor) _read(
            "prod(sort(\"X\"),[seq([lit(\"(\"),layouts(\"$default$\"),empty(),layouts(\"$default$\"),lit(\")\")])],{})",
            RascalValueFactory.Production);
    private static final IConstructor cmVndWxhcihlbXB0eSgpKQ0000 =
        (IConstructor) _read("regular(empty())", RascalValueFactory.Production);
    private static final IConstructor EMPTY_PROD =
        (IConstructor) _read("prod(empty(),[],{})", RascalValueFactory.Production);
    private static final IConstructor RegularSequence =
        (IConstructor) _read(
            "regular(seq([lit(\"(\"),layouts(\"$default$\"),empty(),layouts(\"$default$\"),lit(\")\")]))",
            RascalValueFactory.Production);
    private static final IConstructor BC = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
    private static final IConstructor BO = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", RascalValueFactory.Production);
    private static final IConstructor Layouts =
        (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", RascalValueFactory.Production);

    protected static class layouts_$default$ {
        public final static AbstractStackNode<IConstructor>[] EXPECTS;
        static {
            ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
            init(builder);
            EXPECTS = builder.buildExpectArray();
        }

        protected static final void _init_cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00(
            ExpectBuilder<IConstructor> builder) {
            AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];

            tmp[0] = new EpsilonStackNode<IConstructor>(88, 0);
            builder.addAlternative(Sequence4.Layouts, tmp);
        }

        public static void init(ExpectBuilder<IConstructor> builder) {
            _init_cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00(builder);
        }
    }

    protected static class X {
        public final static AbstractStackNode<IConstructor>[] EXPECTS;
        static {
            ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
            init(builder);
            EXPECTS = builder.buildExpectArray();
        }

        protected static final void INIT_X(
            ExpectBuilder<IConstructor> builder) {
            AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];

            tmp[0] = new SequenceStackNode<IConstructor>(105, 0,
                RegularSequence,
                (AbstractStackNode<IConstructor>[]) new AbstractStackNode[] {
                    new LiteralStackNode<IConstructor>(100, 0, BO, new int[] {40}, null, null),
                    new NonTerminalStackNode<IConstructor>(101, 1, "layouts_$default$", null, null),
                    new EmptyStackNode<IConstructor>(102, 2, cmVndWxhcihlbXB0eSgpKQ0000, null, null),
                    new NonTerminalStackNode<IConstructor>(103, 3, "layouts_$default$", null, null),
                    new LiteralStackNode<IConstructor>(104, 4, BC, new int[] {41}, null, null)},
                null, null);
            builder.addAlternative(Sequence4.NONTERMINAL_X, tmp);
        }

        public static void init(ExpectBuilder<IConstructor> builder) {
            INIT_X(builder);
        }
    }

    private int nextFreeStackNodeId = 113;

    protected int getFreeStackNodeId() {
        return nextFreeStackNodeId++;
    }

    // Parse methods

    public AbstractStackNode<IConstructor>[] layouts_$default$() {
        return layouts_$default$.EXPECTS;
    }

    public AbstractStackNode<IConstructor>[] X() {
        return X.EXPECTS;
    }

    public ITree executeParser() {
        return parse("X", null, "()".toCharArray(),
            new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
    }

    public IValue getExpectedResult() throws IOException {
        String expectedInput = "appl(somethingsomthing)";
        return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
    }

    public static void main(String[] args) {
        Sequence4 s4 = new Sequence4();
        IConstructor result = s4.executeParser();
        System.out.println(result);
    }
}
