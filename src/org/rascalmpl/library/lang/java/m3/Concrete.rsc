@license{Copyright (c) 2018, Rodin Aarssen, Centrum Wiskunde & Informatica (CWI) 
All rights reserved. 
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 

}
@doc{
.Synopsis
Concrete syntax functions
}
module lang::java::m3::Concrete

import Node;
import String;

import lang::java::m3::AST;

@concreteSyntax{Declaration}
Declaration parseDeclaration(str code) {
  if (startsWith(trim(code), "package") || startsWith(trim(code), "import")) {
    str context = code;
    Declaration cu = createAstFromString(|bla:///|, context, false);
    return unsetRec(cu);
  } else {
    str context = "class C { <code> }";
    Declaration cu = createAstFromString(|bla:///|, context, false);
    return unsetRec(cu.types[0].body[0]);
  }
}

@concreteHole{Declaration}
str makeDeclarationHole(int id) = "void _myMethod<id>() {}";

@concreteSyntax{Expression}
Expression parseExpression(str code) {
  str context = "class Foo {
                '  public void main() {
                '    assert <code>;
                '  }
                '}";
  Declaration cu = createAstFromString(|bla:///|, context, false);
  return unsetRec(cu.types[0].body[0].impl.statements[0].expression);
}

@concreteHole{Expression}
str makeExpressionHole(int id) = "_myHole(<id>)";

@concreteSyntax{Statement}
Statement parseStatement(str code) {
  str context = "class Foo {
                '  public void main() {
                '    <code>;
                '  }
                '}";
  Declaration cu = createAstFromString(|bla:///|, context, false);
  return unsetRec(cu.types[0].body[0].impl.statements[0]);
}

@concreteHole{Statement}
str makeStatementHole(int id) = "myHole(<id>);";

@concreteSyntax{Type}
Type parseType(str code) {
  str context = "class Foo {
                '  public <code> main() {
                '  }
                '}";
  Declaration cu = createAstFromString(|bla:///|, context, false);
  return unsetRec(cu.types[0].body[0].\return);
}

@concreteHole{Type}
str makeTypeHole(int id) = "myType<id>End";