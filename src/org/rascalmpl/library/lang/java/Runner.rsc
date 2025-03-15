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
@synopsis{Simple Rascal API for executing JVM bytecode, as static main method or as Junit4 test class.}
@benefits{
* Run the static `main` method of any .class file in JVM bytecode format, stored anywhere reachable through a `loc` and using any library
reachable through a `loc`, to JVM bytecode using the current JVM.
* Run any Junit4 test class in JVM bytecode format, stored anywhere reachable through a `loc` and using
any library also reachable through a `loc`
}
@pitfalls{
* If you are looking for Java analysis and transformation support in Rascal please go find
the [java-air](http://www.rascalmpl.org/docs/Packages/RascalAir) package. The current module
only provides a Java to bytecode compilation API. 
}
module lang::java::Runner

import IO;
import lang::java::Compiler;
import String;
import util::FileSystem;
import util::Reflective;
import util::UUID;
import Location;

@synopsis{Execute the static main function of a (compiled) java class}
@benefits{
* This function can use class files from any support loc scheme
}
@pitfalls{
* The current Rascal runtime/interpreter classloaders, including vallang, are always used
before any other class.
}
@javaClass{org.rascalmpl.library.lang.java.JavaRunner}
java void runJavaMain(str qualifiedName, list[str] args, list[loc] classpath=[]);

data JUnitVersion
    = junit4();

@synsopsis{Execute JUnit test classes directly from Rascal}
@benefits{
* This function can use class files from any support loc scheme
* Classes are loaded from the `classpath` parameter with any `loc` scheme that supports class loading.
}
@pitfalls{
* The current Rascal runtime/interpreter classloaders, including vallang, are always used
before any other class.
}
@javaClass{org.rascalmpl.library.lang.java.JavaRunner}
java list[Message] runJUnitTestClass(str qualifiedName, list[loc] classpath = [], JUnitVersion version = junit4());

@synopsis{Locate the right classpath for JUnit}
@javaClass{org.rascalmpl.library.lang.java.JavaRunner}
@benefits{
* Yhis comes in handy for the compiler `libs` parameter, if the test still needs to be compiled from source.
}
java loc getJUnitClassPath(JUnitVersion version=junit4());

test bool factorialMainTest() {
    root = uuid()[scheme="memory"];
    target = root + "target";

    source   = |project://rascal/test/org/rascalmpl/benchmark/Factorial/Factorial.java|;
    qname    = "org.rascalmpl.benchmark.Factorial.Factorial";

    messages = compileJavaSourceFile(
        source, 
        target, 
        [|project://rascal/test/|]);

    runJavaMain(
        qname, 
        [], 
        classpath=[target, resolvedCurrentRascalJar()]
    );

    return true;
}

test bool junitTestRunTest() {
    root = uuid()[scheme="memory"];
    target = root + "target";
    sources = root + "sources";
    sourceFile = sources + "TheTestClass.java";

    code = "import org.junit.Test;
           'import static org.junit.Assert.assertTrue;
           'public class TheTestClass {
           '    @Test
           '    public void aTestExample() {
           '        assertTrue(true);
           '    }
           '}";

    writeFile(sourceFile, code);

    messages = compileJavaSourceFile(sourceFile, target, [sources], libs=[resolvedCurrentRascalJar(), getJUnitClassPath()]);

    assert messages == [] : "example should compile without errors: <messages>";

    qname = replaceAll(relativize([sources], sourceFile)[extension=""].path[1..], "/", ".");

    results = runJUnitTestClass(qname, classpath=[target, getJUnitClassPath()]);

    assert [info("aTestExample(TheTestClass) started", loc _), info("aTestExample(TheTestClass) finished", _)] := results;

    code = "import org.junit.Test;
           'import static org.junit.Assert.assertTrue;
           'public class TheTestClass {
           '    @Test
           '    public void aTestExample() {
           '        assertTrue(false);
           '    }
           '}";

    writeFile(sourceFile, code);

    messages = compileJavaSourceFile(sourceFile, target, [sources], libs=[resolvedCurrentRascalJar(), getJUnitClassPath()]);

    assert messages == [] : "example should compile without errors: <messages>"; 

    results = runJUnitTestClass(qname, classpath=[target, getJUnitClassPath()]);

    assert [
        info("aTestExample(TheTestClass) started", _), 
        error("aTestExample(TheTestClass) failed", _),
        info("aTestExample(TheTestClass) finished", _)] := results;

    return true;
}