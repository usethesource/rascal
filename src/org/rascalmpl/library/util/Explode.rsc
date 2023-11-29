@licence{
Copyright (c) 2023, NWO-I Centrum Wiskunde & Informatica (CWI) 
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.     
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl}
@synopsis{Explode lifts abstract syntax trees to parse trees}
@description{
The explode function is used to lift abstract syntax trees to concrete syntax trees.
The main difference is that all whitespace and comments are retrieved from the original
files and placed inside of the parse tree.    
}
@benefits{
* when analyzing a parse tree all information including layout and comments is preservation
* when transforming parse trees, the original layout and comments are transformed along; moroever things that remain the same, remain the same.
* unparsing a parse tree returns the exact original input file
* using the `explode` function we can reuse an external parser that produces ASTs, and still have Hi-fidelity source code analyses and transformations.
* the explode function is "type name preserving", such that a data-type named "Expression" becomes a concrete syntax tree type named "Expression"
}
@pitfalls{
* exploding only works correctly if the AST implements the AST specification from analysis::m3::AST.
* exploding takes about as much time as parsing a file
}
module util::Explode

extend ParseTree;
import IO;
import Node;

@synopsis{Turn an AST into a ParseTree, while preserving the name of the type.}
syntax[&T] explode(data[&T] ast) {
    assert ast.src?;
    assert readFile(ast.src.top) == readFile(ast.src);
    assert astNodeSpecification(ast);

    return explode(typeOf(ast), ast, readFile(ast.src.top), ast.src.offset, ast.src.length);
}

syntax[&T] explode(data[&T] ast, str contents, int offset, int length) {
   children = getChildren(ast);
  
   // here we generate a quasi syntax rule that has the structure and the types
   // of the exploded children. each rule starts with separators, has separators
   // in between every child, and ends with separators. Each child node is modified
   // to a syntax node. Lists become iter-star symbols
   rule = prod(\syntax(typeOf(ast)), [
         layouts("*separators*"), 
         *[\syntax(typeOf(c)), layouts("*separators*") | c <- children][..-1], 
         layouts("*separators*")
      ], 
      {});

   children = [
      separatorTree(contents, offset, c.src.offset),
      explode(c, contents, c.src.offset, c.src.length)
      | c <- getChildren(ast)
   ] + [
      separatorTree(contents, last.src.offset + last.src.length, offset + length) | last <- children[-1..]
   ];

   if (syntax[&T] r := appl(rule, children)) {
      return r;
   }
   else {
      throw "unexpected problem while exploding <ast>";
   }
}

Tree separatorTree(str contents, int \start, int end)
   = appl(prod(layouts("separators"),[\iter-star(\char-class([range(1,1114111)])],{}),
     [char(ch) | int ch <- chars(contents[\start..end]]))

Symbol \syntax(str())           = \lex("*identifiers*");
Symbol \syntax(\list(Symbol s)) = \iter-star(\syntax(s));