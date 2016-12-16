package org.rascalmpl.value.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Test;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.impl.AbstractDefaultAnnotatable;
import org.rascalmpl.value.impl.AbstractDefaultWithKeywordParameters;
import org.rascalmpl.value.impl.persistent.ValueFactory;
import org.rascalmpl.value.io.binary.util.PrePostIValueIterator;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.RandomValues;
import org.rascalmpl.value.visitors.IValueVisitor;


public class PrePostIValueIteratorTest {

	private static IValueFactory vf = ValueFactory.getInstance();

    protected PrePostIValueIterator iterator(IValue root) {
        return new PrePostIValueIterator(root);
    }
    private class ReferenceVisitor
            implements IValueVisitor<Void, RuntimeException> {
        private List<ValueTuple> result;

        public ReferenceVisitor(List<ValueTuple> result) {
            this.result = result;
        }

        @Override
        public Void visitString(IString o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            return null;
        }

        @Override
        public Void visitReal(IReal o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            return null;
        }

        @Override
        public Void visitRational(IRational o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            result.add(new ValueTuple(o.numerator(), true));
            result.add(new ValueTuple(o.denominator(), true));
            result.add(new ValueTuple(o, false));
            return null;
        }

        @Override
        public Void visitList(IList o) throws RuntimeException {
            visitIterable(o);
            return null;
        }

        private void visitIterable(Iterable<IValue> o) {
            result.add(new ValueTuple((IValue)o, true));
            for (IValue v: o) {
                v.accept(this);
            }
            result.add(new ValueTuple((IValue)o, false));
        }

        @Override
        public Void visitRelation(ISet o) throws RuntimeException {
            return visitSet(o);
        }

        @Override
        public Void visitListRelation(IList o) throws RuntimeException {
            return visitList(o);
        }

        @Override
        public Void visitSet(ISet o) throws RuntimeException {
            visitIterable(o);
            return null;
        }

        @Override
        public Void visitSourceLocation(ISourceLocation o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            return null;
        }

        @Override
        public Void visitTuple(ITuple o) throws RuntimeException {
            visitIterable(o);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Void visitNode(INode o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            for (IValue v: o) {
                v.accept(this);
            }
            if(o.mayHaveKeywordParameters()){
                if(o.asWithKeywordParameters().hasParameters()){
                    visitIterator(((AbstractDefaultWithKeywordParameters<INode>) o.asWithKeywordParameters()).internalGetParameters().entryIterator());
                    
                }
            } else {
                if(o.asAnnotatable().hasAnnotations()){
                    visitIterator(((AbstractDefaultAnnotatable<INode>)o.asAnnotatable()).internalGetAnnotations().entryIterator());
                }
            }
            result.add(new ValueTuple(o, false));
            return null;
        }

        private void visitIterator(Iterator<Entry<String, IValue>> entryIterator) {
            // since the PrePostValueIterator uses a stack, we see the annotations an keyword params in reverse (but in pairs)
            List<Entry<String, IValue>> reverseEntries = new ArrayList<>();
            while (entryIterator.hasNext()) {
                Entry<String, IValue> param = entryIterator.next();
                reverseEntries.add(0, new AbstractMap.SimpleImmutableEntry<String, IValue>(param.getKey(), param.getValue()));
            }
            
            for (Entry<String, IValue> param: reverseEntries) {
                IString key = vf.string(param.getKey());
                result.add(new ValueTuple(key, true));
                param.getValue().accept(this);
            }
        }

        @Override
        public Void visitConstructor(IConstructor o) throws RuntimeException {
            visitNode(o);
            return null;
        }

        @Override
        public Void visitInteger(IInteger o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            return null;
        }

        @Override
        public Void visitMap(IMap o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            for(IValue key : o){
                o.get(key).accept(this);
                key.accept(this);
            }
            result.add(new ValueTuple(o, false));
            return null;
        }

        @Override
        public Void visitBoolean(IBool boolValue) throws RuntimeException {
            result.add(new ValueTuple(boolValue, true));
            return null;
        }

        @Override
        public Void visitExternal(IExternalValue externalValue) throws RuntimeException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Void visitDateTime(IDateTime o) throws RuntimeException {
            result.add(new ValueTuple(o, true));
            return null;
        }
    }

    private static class ValueTuple {
        public final IValue v;
        public final boolean b;
        public ValueTuple(IValue v, boolean b) {
            this.v = v;
            this.b = b;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj instanceof ValueTuple) {
                ValueTuple other = (ValueTuple)obj;
                if (other.b != b) {
                    return false;
                }
                return other.v.equals(v);
            }
            return false;
        }
        @Override
        public String toString() {
            return v.toString() + " begin: " + b;
        }
    }

    private List<ValueTuple> collect(PrePostIValueIterator fl) {
        List<ValueTuple> result = new ArrayList<>();
        while (fl.hasNext()) {
            fl.next();
            result.add(new ValueTuple(fl.getValue(), fl.atBeginning()));
        }
        return result;
    }

    private List<ValueTuple> collectSkip(PrePostIValueIterator fl) {
        List<ValueTuple> result = new ArrayList<>();
        while (fl.hasNext()) {
            fl.next();
            result.add(new ValueTuple(fl.getValue(), fl.atBeginning()));
            if (fl.getValue().getType().isList()) {
                assert fl.atBeginning();
                fl.skipValue();
            }
        }
        return result;
    }

    private List<ValueTuple> referenceOrder(IValue target) {
        List<ValueTuple> result = new ArrayList<>();
        target.accept(new ReferenceVisitor(result));
        return result;
    }
    
    private List<ValueTuple> referenceOrderSkip(IValue target) {
        List<ValueTuple> result = new ArrayList<>();
        target.accept(new ReferenceVisitor(result) {
            @Override
            public Void visitList(IList o) throws RuntimeException {
                result.add(new ValueTuple(o, true));
                return null;
            }
        });
        return result;
    }

    @Test
    public void countSingleString() {
        IValue v = vf.string("a");
        List<ValueTuple> result = collect(iterator(v));
        assertEquals(1, result.size());
        assertSame(v, result.get(0).v);
    }

    @Test
    public void countNestedValues() {
        IValue v = vf.list(vf.string("a"), vf.list());
        List<ValueTuple> result = collect(iterator(v));
        assertEquals(2 /* list */ + 1 /* string */ + 2 /* list */, result.size());
    }
    
    @Test
    public void correctOrderSmallValues() {
        for (IValue v: RandomValues.getTestValues(vf)) {
            testIteratorOrder(v);
        }
    }

    @Test
    public void correctOrderSmallValuesSkipping() {
        for (IValue v: RandomValues.getTestValues(vf)) {
            testIteratorOrderSkipped(v);
        }
    }
    
    @Test
    public void correctOrderSharing() {
        for (IValue v: RandomValues.getTestValues(vf)) {
            testIteratorOrder(vf.list(vf.string("a"), v, vf.string("b"), v));
        }
    }
    
    @Test
    public void randomValuesCorrect() {
        TypeStore ts = new TypeStore();
        Type tp = RandomValues.addNameType(ts);
        Random r = new Random(42);
        for (int i = 0; i < 100; i++) {
            testIteratorOrder(RandomValues.generate(tp, ts, vf, r, 10));
        }
    }
    @Test
    public void randomValuesCorrectSkipping() {
        TypeStore ts = new TypeStore();
        Type tp = RandomValues.addNameType(ts);
        Random r = new Random(42);
        for (int i = 0; i < 100; i++) {
            testIteratorOrderSkipped(RandomValues.generate(tp, ts, vf, r, 10));
        }
    }

    private void testIteratorOrder(IValue target) {
        compareLists(referenceOrder(target), collect(iterator(target)));
    }
    private void testIteratorOrderSkipped(IValue target) {
        compareLists(referenceOrderSkip(target), collectSkip(iterator(target)));
    }

    private void compareLists(List<ValueTuple> expected, List<ValueTuple> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i=0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
            if (!(expected.get(i).v instanceof IString)) {
                // the iterators create new IStrings for the kw/annos
                assertSame(expected.get(i).v, actual.get(i).v);
            }
        }
    }

}
