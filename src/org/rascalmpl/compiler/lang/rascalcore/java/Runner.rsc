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
module lang::rascalcore::java::Runner

extend lang::rascalcore::java::Compiler;

@javaClass{org.rascalmpl.compiler.lang.rascalcore.java.JavaRunner}
@synopsis{Execute the static main function of a (compiled) java class}
@benefits{
* This function can use class files from any support loc scheme
}
@pitfalls{
* The current Rascal runtime/interpreter classloaders, including vallang, are always used
before any other class.
}
java void runJavaMain(str qualifiedName, list[str] args, list[loc] classpath=[]);


@benefits{
* This function can use class files from any support loc scheme
}
@pitfalls{
* The current Rascal runtime/interpreter classloaders, including vallang, are always used
before any other class.
}
@javaClass{org.rascalmpl.compiler.lang.rascalcore.java.JavaRunner}
java list[Message] runJUnitTestClass(str qualifiedName, list[loc] classpath=[]);