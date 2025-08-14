@license{
Copyright (c) 2022, NWO-I Centrum Wiskunde & Informatica (CWI) 
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
}
@synopsis{Composes a default formatter for Rascal modules}
@description{
This module composes and describes a "standard" formatting style for Rascal.
There could be other styles of course. Other styles can be build by 
writing different `toBox` rules.
}
@bootstrapParser
module lang::rascal::format::Rascal
 
// by extending these modules we compose a `toBox` function
// which handles all relevant constructs of Rascal
extend lang::box::util::Tree2Box;
extend lang::rascal::\syntax::Rascal;

import ParseTree;
import analysis::diff::edits::ExecuteTextEdits;
import analysis::diff::edits::HiFiLayoutDiff;
import analysis::diff::edits::TextEdits;
import lang::box::\syntax::Box;
import lang::box::util::Box2Text;
import String;

@synopsis{Format an entire Rascal file, in-place.}
void formatRascalFile(loc \module) {
    start[Module] tree = parse(#start[Module], \module);
    edits = formatRascalModule(tree);
    executeFileSystemChanges(changed(\module, edits));
}

@synopsis{Format a Rascal module string}
str formatRascalString(str \module) 
    = executeTextEdits(\module, formatRascalModule(parse(#start[Module], \module, |unknown:///|)));

@synopsis{Top-level work-horse for formatting Rascal modules}
@benefits{
* retains source code comments 
* uses Box for adaptive nested formatting
}
list[TextEdit] formatRascalModule(start[Module] \module) 
    = layoutDiff(\module, parse(#start[Module], format(toBox(\module)), \module@\loc.top));


/* Modules */

Box toBox(Toplevel* toplevels) = V([toBox(t) | t <- toplevels], vs=2);

Box toBox((Visibility) ``) = NULL();

/* Declarations */

Box toBox(FunctionModifier* modifiers) = H([toBox(b) | b <- modifiers]);

Box toBox((Signature) `<FunctionModifiers modifiers> <Type typ>  <Name name> <Parameters parameters> throws <{Type ","}+ exs>`)
    = HOV([
        H([toBox(modifiers), toBox(typ), H([toBox(name), toBox(parameters)], hs=0)]), 
        H([L("throws"), HV([toBox(e) | e <- exs])])], hs=1);

Box toBox((Signature) `<FunctionModifiers modifiers> <Type typ>  <Name name> <Parameters parameters>`)
    = H([toBox(modifiers), toBox(typ), H([toBox(name), toBox(parameters)], hs=0)]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> ;`)
    = V([
        toBox(tags),
        H([toBox(vis), toBox(sig), L(";")])
    ]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Expression exp>;`)
    = V([
        toBox(tags),
        H([toBox(vis), toBox(sig)]),
        I([H([L("="), H([toBox(exp), L(";")], hs=0)])])
    ]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Expression exp> when <{Expression ","}+ conds>;`)
    = V([
        toBox(tags),
        H([toBox(vis), toBox(sig)]),
        I([H([L("="), toBox(exp)])]),
        I([L("when"), V([toBox(c) | c <- conds])])
    ]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> { <Statement* stats> }`)
    = V([
        toBox(tags),
        H([toBox(vis), toBox(sig), L("{")]),
        I([toBox(stats)]),
        L("}")
    ]);
    
Box toBox(Tag* tags) = V([toBox(t) | Tag t <- tags]);

Box toBox((Tag) `@synopsis <TagString c>`) 
    = H([
        L("@"), L("synopsis"), L("{"),
        H([L("<l>") | l <- split("\n", "<c>"[1..-1])]),
        L("}")]
    , hs=0);

Box toBox((Tag) `@<Name n> <TagString c>`) 
    = HOV([
        H([L("@"), L("<n>")], hs=0),
        toBox(c)]
    , hs=0)
    when "<n>" != "synopsis";

// syntax Parameters
// 	= \default: "(" Formals formals KeywordFormals keywordFormals ")" 
// 	| varArgs: "(" Formals formals "..." KeywordFormals keywordFormals ")" ;

Box toBox((Parameters) `( <Formals formals> <KeywordFormals keywordFormals>)`)
    = H([L("("), H([toBox(formals), toBox(keywordFormals)]), L(")")], hs=0);

/* Statements */

Box toBox(Statement* stmts) 
    = V([toBox(s) | s <- stmts]);

Box toBox((Statement) `return <Expression e>;`)
    = HV([L("return"), I([H([toBox(e), L(";")], hs=0)])]);

/* Expressions */

Box toBox((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`)
    =  HOV([
        toBox(condition),
        I([H([L("?"), toBox(thenExp)])]),
        I([H([L(":"), toBox(elseExp)])])
    ]);