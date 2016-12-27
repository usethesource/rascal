/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.value.io;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
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
import org.rascalmpl.value.io.binary.util.StacklessStructuredVisitor;
import org.rascalmpl.value.io.binary.util.StructuredIValueVisitor;
import org.rascalmpl.value.io.reference.ReferenceStructuredIValueVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.RandomValues;
import org.rascalmpl.values.ValueFactoryFactory;

public class StacklessStructuredVisitorTest {
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	
	

    @Test
    public void singleString() {
        testVisitStructure(vf.string("a"));
    }


    @Test
    public void listWithTwoElements() {
        testVisitStructure(vf.list(vf.string("a"), vf.list()));
    }
    
    @Test
    public void correctOrderSmallValues() {
        for (IValue v: RandomValues.getTestValues(vf)) {
            testVisitStructure(v);
        }
    }

    @Test
    public void correctOrderSmallValuesSkipping() {
        for (IValue v: RandomValues.getTestValues(vf)) {
            testVisitStructureSkipped(v);
        }
    }
    
    @Test
    public void randomValuesCorrect() {
        TypeStore ts = new TypeStore();
        Type tp = RandomValues.addNameType(ts);
        Random r = new Random(42);
        for (int i = 0; i < 1000; i++) {
            testVisitStructure(RandomValues.generate(tp, ts, vf, r, 10));
        }
    }
    @Test
    public void randomValuesCorrectSkipping() {
        TypeStore ts = new TypeStore();
        Type tp = RandomValues.addNameType(ts);
        Random r = new Random(42);
        for (int i = 0; i < 1000; i++) {
            testVisitStructureSkipped(RandomValues.generate(tp, ts, vf, r, 10));
        }
    }

	private static class CollectAll implements StructuredIValueVisitor<RuntimeException> {
	    
	    public List<Object> result = new ArrayList<>();

	    @Override
	    public void enterNamedValue(String name) {
	        result.add(name);
	    }
	    
	    @Override
	    public void enterNamedValueValue(IValue val) {
	        result.add(val);
	        
	    }
	    @Override
	    public void leaveNamedValue() {
            result.add("leave");
	    }

	    
        @Override
        public boolean enterConstructor(IConstructor cons) throws RuntimeException {
            result.add(cons);
            return true;
        }

        @Override
        public void enterConstructorArguments(int arity) throws RuntimeException {
            result.add("pa:" + arity);
        }

        @Override
        public void enterConstructorKeywordParameters(int arity) throws RuntimeException {
            result.add("kw:" + arity);
        }

        @Override
        public void enterConstructorAnnotations(int arity) throws RuntimeException {
            result.add("an:" + arity);
        }

        @Override
        public void leaveConstructor() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public boolean enterNode(INode cons) throws RuntimeException {
            result.add(cons);
            return true;
        }

        @Override
        public void enterNodeArguments(int arity) throws RuntimeException {
            result.add("pa:" + arity);
        }

        @Override
        public void enterNodeKeywordParameters(int arity) throws RuntimeException {
            result.add("kw:" + arity);
        }

        @Override
        public void enterNodeAnnotations(int arity) throws RuntimeException {
            result.add("an:" + arity);
        }

        @Override
        public void leaveNode() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public boolean enterList(IList lst) throws RuntimeException {
            result.add(lst);
            return true;
        }

        @Override
        public void enterListElements(int arity) throws RuntimeException {
            result.add(arity);
        }

        @Override
        public void leaveList() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public boolean enterSet(ISet set) throws RuntimeException {
            result.add(set);
            return true;
        }

        @Override
        public void enterSetElements(int arity) throws RuntimeException {
            result.add(arity);
        }

        @Override
        public void leaveSet() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public boolean enterMap(IMap map) throws RuntimeException {
            result.add(map);
            return true;
        }

        @Override
        public void enterMapElements(int arity) throws RuntimeException {
            result.add(arity);
        }

        @Override
        public void leaveMap() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public boolean enterTuple(ITuple tuple) throws RuntimeException {
            result.add(tuple);
            return true;
        }

        @Override
        public void enterTupleElements(int arity) throws RuntimeException {
            result.add(arity);
        }

        @Override
        public void leaveTuple() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public boolean enterExternalValue(IExternalValue externalValue) throws RuntimeException {
            result.add(externalValue);
            return true;
        }

        @Override
        public void enterExternalValueConstructor() throws RuntimeException {
            result.add("cons");
        }

        @Override
        public void leaveExternalValue() throws RuntimeException {
            result.add("leave");
        }

        @Override
        public void visitString(IString o) throws RuntimeException {
            result.add(o);
        }

        @Override
        public void visitInteger(IInteger o) throws RuntimeException {
            result.add(o);
        }

        @Override
        public void visitReal(IReal o) throws RuntimeException {
            result.add(o);
        }

        @Override
        public void visitRational(IRational o) throws RuntimeException {
            result.add(o);
        }

        @Override
        public void visitSourceLocation(ISourceLocation o) throws RuntimeException {
            result.add(o);
        }

        @Override
        public void visitBoolean(IBool boolValue) throws RuntimeException {
            result.add(boolValue);
        }

        @Override
        public void visitDateTime(IDateTime o) throws RuntimeException {
            result.add(o);
        }
	}
	
	private static class CollectAllSkipping extends CollectAll {
	    @Override
	    public boolean enterList(IList lst) throws RuntimeException {
	        super.enterList(lst);
	        return false;
	    }
	}

    private void testVisitStructure(IValue val) {
        CollectAll expected = new CollectAll();
        ReferenceStructuredIValueVisitor.accept(val, expected);
        CollectAll got = new CollectAll();
        StacklessStructuredVisitor.accept(val, got);
        compareLists(expected.result, got.result);
    }

    private void testVisitStructureSkipped(IValue val) {
        CollectAll expected = new CollectAllSkipping();
        ReferenceStructuredIValueVisitor.accept(val, expected);
        CollectAll got = new CollectAllSkipping();
        StacklessStructuredVisitor.accept(val, got);
        compareLists(expected.result, got.result);
    }

    private void compareLists(List<Object> expected, List<Object> actual) {
        assertEquals("We should visit the same amount of elements", expected.size(), actual.size());
        for (int i=0; i < expected.size(); i++) {
            assertEquals("The " + i + "th element in the stream differs", expected.get(i), actual.get(i));
        }
    }

}
