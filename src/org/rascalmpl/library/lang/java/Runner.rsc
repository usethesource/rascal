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

extend lang::java::Compiler;

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

@benefits{
* This function can use class files from any support loc scheme
}
@pitfalls{
* The current Rascal runtime/interpreter classloaders, including vallang, are always used
before any other class.
}
@javaClass{org.rascalmpl.library.lang.java.JavaRunner}
java list[Message] runJUnitTestClass(str qualifiedName, list[loc] classpath = [], JUnitVersion version = junit4());

test bool factorialMainTest() {
    remove(|memory://target|, recursive=true);
    
    source   = |project://rascal/test/org/rascalmpl/benchmark/Factorial/Factorial.java|;
    qname    = "org.rascalmpl.benchmark.Factorial.Factorial";

    messages = compileJavaSourceFile(
        source, 
        |memory://target|, 
        [|project://rascal/test/|]);

    iprintln(messages);

    runJavaMain(
        qname, 
        [], 
        classpath=[|memory://target|, resolvedCurrentRascalJar()]
    );

    return true;
}