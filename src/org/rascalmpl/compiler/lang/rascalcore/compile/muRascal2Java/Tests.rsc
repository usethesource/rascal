@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::muRascal2Java::Tests

import lang::rascalcore::compile::muRascal::AST;

//import lang::rascalcore::check::AType;
//import lang::rascalcore::check::ATypeUtils;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;

import List;
import Map;
import Set;
import String;

// Generate a test class for a Rascal module

str generateTestClass(str packageName, str className, list[MuFunction] functions, JGenie jg){
    //jg.generatingTests(true);
    
    testMethods = "<for(f <- functions){>
                  '    <generateTestMethod(f, className, jg)><}>
                  '";
    if(isEmpty(trim(testMethods))) return "";
    
    res  = "<if(!isEmpty(packageName)){>package <packageName>;<}>
           'import java.util.*;
           'import java.util.stream.Stream;
           'import io.usethesource.vallang.*;
           'import io.usethesource.vallang.type.*;
           '
           'import static org.junit.Assert.fail;
           'import static org.junit.jupiter.api.Assertions.assertTrue;
           'import static org.junit.jupiter.api.DynamicTest.dynamicTest;
           '
           'import org.junit.jupiter.api.Test;
           'import org.junit.Ignore;
           'import org.junit.jupiter.api.DynamicTest;
           'import org.junit.jupiter.api.TestFactory;
           'import org.rascalmpl.runtime.utils.*;
           'import org.rascalmpl.runtime.*;
           'import org.rascalmpl.exceptions.Throw;
           'import org.rascalmpl.exceptions.RuntimeExceptionFactory;
           'import org.rascalmpl.values.parsetrees.ITree;
           '
           '@SuppressWarnings(\"unused\")
           'class <className>Tests extends org.rascalmpl.runtime.$RascalModule {
           '    <className> $me;
           '    final GenerateActuals generator = new GenerateActuals(5, 5, 10);
           '
           '    public <className>Tests(){
           '        super(new RascalExecutionContext(System.in, System.out, System.err, null, null, <packageName>.<className>.class));
           '        ModuleStore store = rex.getModuleStore();
           '        store.importModule(<className>.class, this.rex, <className>::new);   
           '        $me = store.getModule(<className>.class);                     
           '    }
           '    <testMethods>
           '}\n";
    //jg.generatingTests(false);
    return res;
}

// Generate a test method per function with "test" modifier

str generateTestMethod(MuFunction f, str _className, JGenie jg){
   
    if("test" notin f.modifiers) return "";
    
    test_name = asJavaName(f.uniqueName);
    test_name_uniq = "<test_name>_<f.src.begin.line>";
    formals = f.ftype.formals;
    expected = f.tags["expected"] ? "";
   
    if(!isEmpty(domain(f.tags) & {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"})){
        return "@Ignore
               'void <test_name_uniq>(){ }\n";
    }
    fun_name = "$me.<getFunctionName(f)>";
    
    externalArgs = "";                 
    //if(!isEmpty(f.externalRefs)){
    //  externalArgs = intercalate(", ", [ "new ValueRef\<<jtype>\>(<className>.<var.name>)" | var <- sort(f.externalRefs), var.pos >= 0, jtype := atype2javatype(var.atype)]);
    //}  
    if(isEmpty(formals)){
        if(isEmpty(expected)){
            return "@Test
                   'void <test_name_uniq>(){
                   '   assertTrue(((IBool)<fun_name>(<externalArgs>)).getValue());
                   '}\n";
        } else {
            return "@Test
                   'void <test_name_uniq>(){
                   '    try {
                   '        <fun_name>(<externalArgs>);
                   '    } catch (Throw e) { // Temporary to enable interop with Prelude
                   '        if(((IConstructor) e.getException()).getConstructorType().getName() == \"<expected>\") {
                   '            assertTrue(true);
                   '            return;
                   '         }
                   '         fail(\"Expected `<expected>`, got: \" + e);
                   '    } catch (Exception e){
                   '            fail(\"Expected `<expected>`, but got Java exception `\" + e + \"`\");
                   '    }
                   '    fail(\"Expected `<expected>`, but nothing was thrown\");
                   '}\n";
        }
    }
    types = "new io.usethesource.vallang.type.Type[] {<intercalate(", ", ["<isADTAType(tp) ? "" : "$me."><atype2vtype(tp, jg, inTest=true)>" | tp <- formals])>}";
    //types = "new io.usethesource.vallang.type.Type[] {<intercalate(", ", ["<isADTAType(tp) ? "$me." : ""><atype2vtype(tp, jg)>" | tp <- formals])>}";
    argTypes = f.ftype.formals;
    actuals = intercalate(", ", ["(<atype2javatype(argTypes[i])>)args[<i>]" | i <- index(formals)]);
    if(!isEmpty(externalArgs)){
        actuals += ", " + externalArgs;
    }
    if(isEmpty(expected)){
        return "@TestFactory
               'Stream\<DynamicTest\> <test_name_uniq>(){
               '    return generator.generateActuals(<types>, $me.$TS).map((args) -\> dynamicTest(\"<test_name>\", () -\> assertTrue(((IBool)$me.<test_name>(<actuals>)).getValue(), () -\> $displayTestArgs(args))));
               '}\n";
     } else {
        return "@TestFactory
               'Stream\<DynamicTest\> <test_name_uniq>(){
               '    return generator.generateActuals(<types>, $TS).map((args) -\> dynamicTest(\"<test_name>\", () -\> {
               '        try {
               '            $me.<test_name>(<actuals>);
               '            fail(\"Expected `<expected>`, but nothing was thrown\");
               '        } catch (Throw e) {
               '            if(((IConstructor) e.getException()).getConstructorType() == RuntimeExceptionFactory.<expected>) {
               '                assertTrue(true);
               '                return;
               '             }
               '             fail(\"Expected `<expected>`, got: \" + e);
               '        } catch (Exception e){
               '            fail(\"Expected `<expected>`, but got Java exception `\" + e + \"`\");
               '        }
               '    }/*, () -\> $displayTestArgs(args)*/));
               '}\n";
     }
}