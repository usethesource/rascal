module lang::rascalcore::compile::muRascal2Java::Tests

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::compile::muRascal2Java::JGenie;
import lang::rascalcore::compile::muRascal2Java::Conversions;
import lang::rascalcore::compile::util::Names;
import List;
import Map;
import Set;

import String;

// Generate a test class for a Rascal module

str generateTestClass(str packageName, str className, list[MuFunction] functions, JGenie jg){
    return "<if(!isEmpty(packageName)){>package <packageName>;<}>
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
           'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils.*;
           'import org.rascalmpl.core.library.lang.rascalcore.compile.runtime.*;
           'import org.rascalmpl.exceptions.Throw;
           'import org.rascalmpl.exceptions.RuntimeExceptionFactory;
           '
           '@SuppressWarnings(\"unused\")
           'class <className>Tests extends org.rascalmpl.core.library.lang.rascalcore.compile.runtime.$RascalModule {
           '    <className> $me;
           '    final GenerateActuals generator = new GenerateActuals(5, 5, 10);
       
           '    public <className>Tests(){
           '        ModuleStore store = new ModuleStore();
           '        $me = store.importModule(<className>.class, <className>::new);                       
           '    }
           '    <for(f <- functions){>
           '    <generateTestMethod(f, className, jg)><}>
           '}\n";
}

// Generate a test method per function with "test" modifier

str generateTestMethod(MuFunction f, str className, JGenie jg){
    if("test" notin f.modifiers) return "";
    
    test_name = getJavaName(f.uniqueName);
    test_name_uniq = "<test_name>_<f.src.begin.line>";
    formals = f.ftype.formals;
    expected = f.tags["expected"] ? "";
    ignored = !isEmpty(domain(f.tags) & {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});
    if(ignored){
        return "@Ignore
               'void <test_name_uniq>(){ }\n";
    }
    fun_name = "$me.<getFunctionName(f)>";
    
    externalArgs = "";                 
    if(!isEmpty(f.externalVars)){
      externalArgs = intercalate(", ", [ "new ValueRef\<<jtype>\>(<className>.<var.name>)" | var <- sort(f.externalVars), var.pos >= 0, jtype := atype2javatype(var.atype)]);
    }  
    if(isEmpty(formals)){
        if(isEmpty(expected)){
            return "@Test
                   'void <test_name_uniq>(){
                   '   assertTrue(<fun_name>(<externalArgs>).getValue());
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
    types = "new io.usethesource.vallang.type.Type[] {<intercalate(", ", ["<isADTType(tp) ? "$me." : ""><atype2vtype(tp)>" | tp <- formals])>}";
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