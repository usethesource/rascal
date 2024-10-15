/** 
 * Copyright (c) 2016, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.test.infrastructure.RascalJUnitTestRunner;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalFunctionValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import junit.framework.TestCase;

/**
 * These tests check the hard contract on the integer values for `int IValue.getMatchFingerprint()`.
 * Do not change these tests unless you are absolutely sure you know what you are doing. For one
 * thing, changing fingerprints implies a pretty hairy bootstrap dependency hoop you have to jump through.
 * Typically you would have to add a mode to the run-time to make it switch between the next version
 * of the fingerprints for new code, and the old version of the fingerprints for the current run-time
 * that runs the compiler.
 */
public class MatchFingerprintTest extends TestCase {
    private final GlobalEnvironment heap = new GlobalEnvironment();
    private final ModuleEnvironment root = new ModuleEnvironment("root", heap);
    private final Evaluator eval = new Evaluator(IRascalValueFactory.getInstance(), Reader.nullReader(), System.err, System.out, root, heap, RascalJUnitTestRunner.getCommonMonitor());
    private final RascalFunctionValueFactory VF = eval.getFunctionValueFactory();
    private final TypeFactory TF = TypeFactory.getInstance();

    public void testFunctionFingerPrintStability() {
        Type intint = TF.functionType(TF.integerType(), TF.tupleType(TF.integerType()), TF.tupleEmpty());

        IFunction func = VF.function(intint, (args, kwargs) -> {
            return VF.integer(0);
        });
        
        // these magic numbers are sacred
        assertEquals(func.getMatchFingerprint(), "func".hashCode() + 89 * intint.hashCode());
    }

    public void testTreeApplFingerPrintStability() {
        String prodString = "prod(sort(\"E\"),[],{})";
        ISourceLocation loc = VF.sourceLocation("BLABLA");

        try {
            IConstructor prod = (IConstructor) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Production, new StringReader(prodString));
            ITree tree = VF.appl(prod, VF.list());

            assertEquals(tree.getMatchFingerprint(), "appl".hashCode() + 131 * 2);
            assertEquals(tree.getConcreteMatchFingerprint(), "appl".hashCode() + 41 * prod.hashCode());

            // and now WITH a keyword parameter
            tree = (ITree) tree.asWithKeywordParameters().setParameter("src", loc);

            assertEquals(tree.getMatchFingerprint(), "appl".hashCode() + 131 * 2);
            assertEquals(tree.getConcreteMatchFingerprint(), "appl".hashCode() + 41 * prod.hashCode());
        }
        catch (FactTypeUseException | IOException e) {
            fail(e.getMessage());
        }
    }
    
    public void testTreeFingerprintDifferentiation() {
        String prodString1 = "prod(sort(\"E\"),[],{})";
        String prodString2 = "prod(sort(\"E\"),[empty()],{})";

        try {
            IConstructor prod1= (IConstructor) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Production, new StringReader(prodString1));
            ITree tree1 = VF.appl(prod1, VF.list());
            IConstructor prod2= (IConstructor) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Production, new StringReader(prodString2));
            ITree tree2 = VF.appl(prod2, VF.list());

            // these two assert explain the different between `getMatchFingerprint` and `getConcreteMatchFingerprint`
            assertEquals(tree1.getMatchFingerprint(), tree2.getMatchFingerprint());
            assertNotEquals(tree1.getConcreteMatchFingerprint(), tree2.getMatchFingerprint());
        }
        catch (FactTypeUseException | IOException e) {
            fail(e.getMessage());
        }
    }

    public void testTreeAmbFingerPrintStability() {
        String prodString1 = "prod(sort(\"E\"),[],{})";
        String prodString2 = "prod(sort(\"E\"),[empty()],{})";
        ISourceLocation loc = VF.sourceLocation("BLABLA");

        try {
            IConstructor prod1= (IConstructor) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Production, new StringReader(prodString1));
            ITree tree1 = VF.appl(prod1, VF.list());
            IConstructor prod2= (IConstructor) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Production, new StringReader(prodString2));
            ITree tree2 = VF.appl(prod2, VF.list());

            ITree amb = VF.amb(VF.set(tree1, tree2));

            assertEquals(amb.getMatchFingerprint(), "amb".hashCode() + 131);
            assertEquals(amb.getConcreteMatchFingerprint(), "amb".hashCode() + 43 * TreeAdapter.getType(amb).hashCode());

            // and now WITH a keyword parameter
            amb = (ITree) amb.asWithKeywordParameters().setParameter("src", loc);

            assertEquals(amb.getMatchFingerprint(), "amb".hashCode() + 131);
            assertEquals(amb.getConcreteMatchFingerprint(), "amb".hashCode() + 43 * TreeAdapter.getType(amb).hashCode());
        }
        catch (FactTypeUseException | IOException e) {
            fail(e.getMessage());
        }
    }

    public void testTreeCharFingerPrintStability() {
        ISourceLocation loc = VF.sourceLocation("BLABLA");

        try {
            ITree theChar = (ITree) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Tree, new StringReader("char(32)"));
           
            assertEquals(theChar.getMatchFingerprint(), "char".hashCode() + 131);
            assertEquals(theChar.getConcreteMatchFingerprint(), "char".hashCode() + ((IInteger) theChar.get(0)).intValue());

            // and now WITH a keyword parameter
            theChar = (ITree) theChar.asWithKeywordParameters().setParameter("src", loc);

            assertEquals(theChar.getMatchFingerprint(), "char".hashCode() + 131);
            assertEquals(theChar.getConcreteMatchFingerprint(), "char".hashCode() + ((IInteger) theChar.get(0)).intValue());
        }
        catch (FactTypeUseException | IOException e) {
            fail(e.getMessage());
        }
    }

     public void testTreeCycleFingerPrintStability() {
        ISourceLocation loc = VF.sourceLocation("BLABLA");

        try {
            ITree theCycle = (ITree) new StandardTextReader().read(VF, RascalFunctionValueFactory.getStore(), RascalFunctionValueFactory.Tree, new StringReader("cycle(sort(\"A\"), 3)"));
           
            assertEquals(theCycle.getMatchFingerprint(), "cycle".hashCode() + 2 * 131);
            assertEquals(theCycle.getConcreteMatchFingerprint(), "cycle".hashCode() + 13 * theCycle.get(0).hashCode());

            // and now WITH a keyword parameter
            theCycle = (ITree) theCycle.asWithKeywordParameters().setParameter("src", loc);

            assertEquals(theCycle.getMatchFingerprint(), "cycle".hashCode() + 2 * 131);
            assertEquals(theCycle.getConcreteMatchFingerprint(), "cycle".hashCode() + 13 * theCycle.get(0).hashCode());
        }
        catch (FactTypeUseException | IOException e) {
            fail(e.getMessage());
        }
    }    
}
