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
import IO;
import util::IDEServices;
import Location;

@synopsis{Format an entire Rascal file, in-place.}
void formatRascalFile(loc \module) {
    start[Module] tree = parse(#start[Module], \module);
    edits = formatRascalModule(tree);
    executeFileSystemChanges([changed(edits)]);
}

@synopsis{Format any Rascal module and dump the result as a string}
void debugFormatRascalFile(loc \module) {
    newFile = executeTextEdits(readFile(\module), formatRascalModule(parse(#start[Module], \module)));
    newLoc = |tmp:///| + relativize(|project://rascal|, \module).path;
    writeFile(newLoc, newFile);
    edit(newLoc);
    return;
}

@synopsis{Format a Rascal module string}
str formatRascalString(str \module) 
    = executeTextEdits(\module, formatRascalModule(parse(#start[Module], \module, |tmp:///temporary.rsc|)));

@synopsis{Top-level work-horse for formatting Rascal modules}
@benefits{
* retains source code comments 
* uses Box for adaptive nested formatting
}
list[TextEdit] formatRascalModule(start[Module] \module) {
    try {
        return layoutDiff(\module, parse(#start[Module], format(toBox(\module)), \module@\loc.top));
    }
    catch e:ParseError(loc place): { 
        writeFile(|tmp:///temporary.rsc|, format(toBox(\module)));
        println("Formatted module contains a parse error here: <|tmp:///temporary.rsc|(place.offset, place.length)>");
        throw e;
    }
}

/* Modules */

Box toBox(Toplevel* toplevels) = toClusterBox(toplevels);

Box toBox((Module) `<Tags tags> module <QualifiedName name> <Import* imports> <Body body>`)
    = V(toBox(tags),
        H(L("module"), toBox(name)),
        toClusterBox(imports),
        toBox(body), vs=1);

Box toBox(Import* imports) = [toBox(i) | i <- imports];

Box toBox((Import) `import <ImportedModule m>;`)
    = H(L("import"), H0(toBox(m), L(";")));

Box toBox((Import) `extend <ImportedModule m>;`)
    = H(L("extend"), H0(toBox(m), L(";")));
    
Box toBox((Visibility) ``) = NULL();

/* Syntax definitions */

Box toBox((SyntaxDefinition) `<Start st> syntax <Sym defined> = <Prod production>;`)
    = (production is \all || production is \first)
        ? V(H(toBox(st), L("syntax"), toBox(defined)),
            I(G(L("="), U(toBox(production)), gs=2, op=[]),
                L(";")))
        :  // single rule case
          H(toBox(st), L("syntax"), toBox(defined), L("="), H0(toBox(production), L(";")))
        ;

Box toBox((SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`)
    = (production is \all || production is \first)
        ? V(H(L("lexical"), toBox(defined)),
            I(G(L("="), U(toBox(production)), gs=2, op=[]),
                L(";")))
        :  // single rule case
          H(L("lexical"), toBox(defined), L("="), H0(toBox(production), L(";")))
        ;

Box toBox((SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`)
    = (production is \all || production is \first)
        ? V(H(L("keyword"), toBox(defined)),
            I(G(L("="), U(toBox(production)), gs=2, op=[]),
                L(";")))
        :  // single rule case
          H(L("keyword"), toBox(defined), L("="), H0(toBox(production), L(";")))
        ;

Box toBox((SyntaxDefinition) `<Visibility v> layout <Sym defined> = <Prod production>;`)
    = (production is \all || production is \first)
        ? V(H(toBox(v), L("layout"), toBox(defined)),
            I(G(L("="), U(toBox(production)), gs=2, op=[]),
                L(";")))
        :  // single rule case
          H(toBox(v), L("layout"), toBox(defined), L("="), H0(toBox(production), L(";")))
        ;


Box toBox((Prod) `<Prod lhs> | <Prod rhs>`) 
    = U(toBox(lhs), L("|"), toBox(rhs));

Box toBox((Prod) `<Prod lhs> \> <Prod rhs>`) 
    = U(toBox(lhs), L("\>"), toBox(rhs));

Box toBox((Prod) `:<Name n>`) 
    = H0(L(":"), toBox(n));

Box toBox((Prod) `<ProdModifier* modifiers> <Name name> : <Sym* syms>`)
    = H(toBox(modifiers), H0(toBox(name), L(":")), [toBox(s) | s <- syms]);

Box toBox((Prod) `<ProdModifier* modifiers> <Sym* syms>`)
    = H(toBox(modifiers), [toBox(s) | s <- syms]);

Box toBox((Prod) `<Assoc a> (<Prod g>)`)
    = H(toBox(a), G(L("("), U(toBox(g)), L(")"), gs=2, op=[]));

/* symbols */
Box toBox((Sym) `{<Sym e> <Sym sep>}*`) = H0(L("{"), H1(toBox(e), toBox(sep)), L("}"), L("*"));
Box toBox((Sym) `{<Sym e> <Sym sep>}+`) = H0(L("{"), H1(toBox(e), toBox(sep)), L("}"), L("+"));
Box toBox((Sym) `<Sym e>*`) = H0(toBox(e), L("*"));
Box toBox((Sym) `<Sym e>+`) = H0(toBox(e), L("+"));
Box toBox((Sym) `<Sym e>?`) = H0(toBox(e), L("?"));
Box toBox((Sym) `(<Sym first> <Sym+ sequence>)`) 
    = H0(L("("), H1(toBox(first), *[toBox(e) | Sym e <- sequence]),L(")"));

Box toBox((Sym) `start[<Nonterminal s>]`) = H0(L("start"), L("["), toBox(s), L("]"));

Box toBox((Sym) `(<Sym first> | <{Sym "|"}+ alternatives>)`) 
    = H0(L("("), H1(toBox(first), *[L("|"), toBox(e) | Sym e <- alternatives]),L(")"));

Box toBox((Class) `[<Range* ranges>]`)
    = H0(L("["), *[toBox(r) | r <- ranges], L("]"));

Box toBox((Range) `<Char s> - <Char e>`)
    = H0(toBox(s), L("-"), toBox(e));

/* Declarations */

Box toBox((QualifiedName) `<{Name "::"}+ names>`)
    = L("<names>");

Box toBox((Tag) `@<Name n> <TagString contents>`)
    = H0(L("@"), toBox(n), toBox(contents));

Box toBox((Tag) `@<Name n> = <Expression exp>`)
    = H0(L("@"), toBox(n), L("="), toBox(exp));

Box toBox((Tag) `@<Name n>`)
    = H0(L("@"), toBox(n));

Box toBox(QualifiedName n) = L("<n>");

Box toBox((Declaration) `<Tags t> <Visibility v> alias  <UserType user> = <Type base>;`)
    = V(toBox(t),
        H(toBox(v), L("alias"), toBox(user), L("="), H0(toBox(base), L(";"))));

Box toBox((Declaration) `<Tags tg> <Visibility v> data <UserType typ> <CommonKeywordParameters ps>;`)
    = V(toBox(tg),
        H(toBox(v), L("data"), H0(toBox(typ), toBox(ps), L(";"))));

Box toBox((Declaration) `<Tags t> <Visibility v> data <UserType typ> <CommonKeywordParameters ps> = <Variant va>;`)
    = HV(V(toBox(t),
        H(toBox(v), L("data"), H0(toBox(typ), toBox(ps)))),
        I(H(L("="), H0(toBox(va), L(";")))));

Box toBox((Declaration) `<Tags t> <Visibility v> data <UserType typ> <CommonKeywordParameters ps> = <Variant v> | <{Variant "|"}+ vs>;`)
    = V(toBox(t),
        H(toBox(v), L("data"), H0(toBox(typ), toBox(ps))),
        I([G([
                L("="),
                toBox(v),
                *[L("|"), toBox(va) | Variant va <- vs] // hoist the bars `|` up to the same level of `=`
            ]), L(";")], hs=0));

Box toBox((Declaration) `<Tags tags> <Visibility visibility> <Type typ> <{Variable ","}+ variables>;`)
    = H1(toBox(tags), toBox(visibility), toBox(typ), SL([toBox(v) | v <- variables], L(",")), L(";"));

Box toBox((Declarator) `<Type typ> <Name name>`) 
    = H(toBox(typ), toBox(name));

Box toBox((Declarator) `<Type typ> <Name name> = <Expression initial>`) 
    = HV(H(toBox(typ), toBox(name)), I(toExpBox(L("="), initial)));

Box toBox((Declarator) `<Type typ> <Variable first>, <{Variable ","}+ variables>`) 
    = HV(toBox(typ), I(HOV(toBox(first), L(","), SL([toBox(v) | v <- variables], L(",")))));

Box toBox((CommonKeywordParameters) `(<{KeywordFormal ","}+ fs>)`)
    = H0(L("("), toBox(fs), L(")"));

Box toBox((Variant) `<Name n>(<{TypeArg ","}* args>, <{KeywordFormal ","}+ kws>)`)
    = HV([
        H0(toBox(n), L("(")),
        I(H0([toBox(args)], L(","))),
        I(toBox(kws)),
        L(")")
    ], hs=0);

Box toBox((Variant) `<Name n>(<{TypeArg ","}* args>)`)
    = HV(H0(toBox(n), L("(")),
        I(toBox(args)),
        L(")"));

Box toBox((Variant) `<Name n>(<{TypeArg ","}* args>
                    '<{KeywordFormal ","}+ kws>)`)
    = HV([
        H0(toBox(n), L("(")),
        I(H0(toBox(args))),
        I(toBox(kws)),
        L(")")
    ], hs=0);

Box toBox(FunctionModifier* modifiers) = [toBox(b) | b <- modifiers];

Box toBox((Signature) `<FunctionModifiers modifiers> <Type typ>  <Name name> <Parameters parameters> throws <{Type ","}+ exs>`)
    = HOV([
        H(toBox(modifiers), toBox(typ), H0(toBox(name), toBox(parameters))), 
        H(L("throws"), [toBox(e) | e <- exs])], hs=1);

Box toBox((Signature) `<FunctionModifiers modifiers> <Type typ>  <Name name> <Parameters parameters>`)
    = H(toBox(modifiers), toBox(typ), H0(toBox(name), toBox(parameters)));

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> ;`)
    = V(toBox(tags),
        H(toBox(vis), H0(toBox(sig), L(";"))));

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Expression exp>;`)
    = V(toBox(tags),
        HOV(H(toBox(vis), toBox(sig)),
            I(HOV(G(L("="), toBox(exp), gs=2, op=[]), L(";"))))) when !(exp is \visit);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Label l> <Visit vst>;`)
    = V(toBox(tags),
        H(toBox(vis), toBox(sig)),
        I(H(L("="), H0(toBox(l), toBox(vst), L(";")))));

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Expression exp> when <{Expression ","}+ conds>;`)
    = V(toBox(tags),
        HOV(H(toBox(vis), toBox(sig)),
            I(G(L("="), toBox(exp), gs=2, op=[]))),
        I(H(L("when"), H0(toBox(conds), L(";")))));

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> { <Statement* stats> }`)
    = V(toBox(tags),
        H(toBox(vis), toBox(sig), L("{")),
        I(toClusterBox(stats)),
        L("}"));
    
Box toBox(Tag* tags) = [toBox(t) | Tag t <- tags];

Box toBox((Tag) `@synopsis<TagString c>`) 
    = H0(L("@"), L("synopsis"), 
        toBox(c));

Box toBox((Tag) `@<Name n> <TagString c>`) 
    = HOV(H0(L("@"), L("<n>")),
        toBox(c))
    when "<n>" != "synopsis";

Box toBox((Tag) `@<Name n>`) 
    = H0(L("@"), L("<n>"));

Box toBox((Parameters) `( <Formals formals> <KeywordFormals keywordFormals>)`)
    = H0(L("("), HV(toBox(formals), toBox(keywordFormals)), L(")"));

Box toBox((Parameters) `( <Formals formals> ... <KeywordFormals keywordFormals>)`)
    = H0(L("("), H(H0(toBox(formals), L("...")), toBox(keywordFormals)), L(")"));

/* Statements */

Box toBox((Statement) `assert <Expression expression>;`)
    = H1(L("assert"), H0(toBox(expression), L(";")));

Box toBox((Statement) `assert <Expression expression> : <Expression msg>;`)
    = HOV(L("assert"), HV(toBox(expression), H0([H(L(":"), toBox(msg))], L(";"))));

Box toBox((Statement) `fail;`)
    = H0(L("fail"), L(";"));

Box toBox((Statement) `fail <Name n>;`)
    = H1(L("fail"),H0(toBox(n), L(";")));

Box toBox((Statement) `break;`)
    = H0(L("break"), L(";"));

Box toBox((Statement) `break <Name n>;`)
    = H1(L("break"),H0(toBox(n), L(";")));

Box toBox((Statement) `continue;`)
    = H0(L("continue"), L(";"));

Box toBox((Statement) `continue <Name n>;`)
    = H1(L("continue"), H0(toBox(n), L(";")));

Box toBox((Statement) `filter;`)
    = H0(L("filter"), L(";"));


Box toBox((Statement) `<LocalVariableDeclaration declaration>;`)
    = H0(toBox(declaration), L(";"));

Box toBox((Statement) `solve(<{QualifiedName ","}+ vars> <Bound bound>) <Statement body>`) 
    = V(H0(L("solve"), L("("), toBox(vars), toBox(bound), H1(L(")"), blockOpen(body))),
        indentedBlock(body),
        blockClose(body));

Box toBox(Statement* stmts) = toClusterBox(stmts);
Box toBox(Statement+ stmts) = toClusterBox(stmts);

Box toBox((Statement) `return <Expression e>;`)
    = HV([HV([L("return"), I(toBox(e))],hs=1), L(";")], hs=0);

Box toBox((Statement) `return <Statement e>`)
    = HV([L("return"), I(toBox(e))], hs=1)
        when !(e is expression);

// if with a block statement is formatted differently then without the block
Box toBox(s:(Statement) `<Label label> if (<{Expression ","}+ cs>) 
                      '  <Statement sts>`)
    = (Statement) `if (<Expression cond>) println(<Expression log>);` := s ? 
    H1(HB([H1(L("if"), L("(")), toExpBox(cond), L(")")]),
        H0(L("println"), L("("), toExpBox(log), L(")"), L(";")))
    : (Statement) `if (<Expression cond>) { println(<Expression log>); }` := s ?
     H1(H0(H0(H1(L("if"), L("(")), toBox(cond), L(")"))),
        L("{"),
        H0(L("println"), L("("), toBox(log), L(")"), L(";")),
        L("}"))
    :
    V(HV(H0(toBox(label), H(L("if"), L("("))), 
            H0(toBox(cs), L(")")),
            blockOpen(sts)),
        indentedBlock(sts),
        blockClose(sts));


Box toBox((Statement) `<Label label> if (<{Expression ","}+ cs>)
                      '  <Statement sts>
                      'else
                      ' <Statement ests>`)
    = V(H(H0(toBox(label), H(L("if"), L("("))), 
            H0(toBox(cs), L(")")),
            blockOpen(sts)),
        indentedBlock(sts),
        blockClose(sts),
        H(L("else"), blockOpen(ests)),
        indentedBlock(ests),
        blockClose(ests));

Box toBox({Expression ","}+ cs)
    = SL([toExpBox(c) | Expression c <- cs], L(","));

Box toBox((Statement) `<Label label> while (<{Expression ","}+ cs>)
                      '  <Statement sts>`)
    = V(H(H0(toBox(label), L("while")), 
            H0(L("("), toBox(cs), L(")")),
            blockOpen(sts)),
        indentedBlock(sts),
        blockClose(sts));

Box toBox((Statement) `<Expression exp>;`)
    = H0(toBox(exp), L(";"));

Box toBox((Statement) `throw <Statement e>`)
    = H(L("throw"), toBox(e));

Box toBox((Statement) `<Label label> for(<{Expression ","}+ gs> ) <Statement block>`) 
    = V(H0(toBox(label), H(L("for"), L("(")), toBox(gs), H(L(")"), blockOpen(block))),
        indentedBlock(block),
        blockClose(block));

 Box toBox((Statement) `switch(<Expression e>) { <Case+ cases> }`)  
    = V(H0(L("switch"), L("("), toBox(e), H(L(")"), L("{"))),
        I(toClusterBox(cases)),
        L("}"));

Box toBox((Case) `case <Pattern p>:  <Statement block>`)
    = V(H(L("case"), H0(toBox(p), L(":")), blockOpen(block)), indentedBlock(block), blockClose(block));

Box toBox((Case) `case <Pattern p> =\> <Expression repl>`)
    = H(L("case"), toBox(p), I(L("=\>"), toBox(repl)));

Box toBox((Case) `default: <Statement block>`)
    = V(H(L("default:"), blockOpen(block)), indentedBlock(block), blockClose(block));


Box toBox((Statement) `try <Statement body> <Catch+ handlers>`)
    = V(H(L("try"), blockOpen(body)),
        indentedBlock(body),
        blockClose(body),
        toBox(handlers));

Box toBox((Statement) `try <Statement body> <Catch+ handlers> finally <Statement fBody>`)
    = V(H(L("try"), blockOpen(body)),
        indentedBlock(body),
        blockClose(body),
        toBox(handlers),
        H(L("finally"), blockOpen(fBody)),
        indentedBlock(fBody),
        blockClose(fBody));

Box toBox((Catch) `catch: <Statement body>`)
    = V(H(H0(L("catch"), L(":")), blockOpen(body)),
        indentedBlock(body),
        blockClose(body));

Box toBox((Catch) `catch <Pattern p>: <Statement body>`)
    = V(H(L("catch"), H0(toBox(p), L(":")), blockOpen(body)),
        indentedBlock(body),
        blockClose(body));



// These are three reusable buildig blocks to avoid case-distinction overloads, with and without curlies
// Using these functions  avoids a combinatorial explosion of overloads for syntax constructs
// with multiple blocks of statements, like if-then-else and try-catch-finally.
Box blockOpen(Statement s) = s is nonEmptyBlock ? L("{") : NULL();

Box blockClose(Statement s) = s is nonEmptyBlock ? L("}") : NULL();

Box indentedBlock((Statement) `{<Statement+ st>}`)
    = I(toClusterBox([s | s <- st]));

default Box indentedBlock(Statement s) = I(toBox(s));

Box toBox((Statement) `<Assignable able> <Assignment operator> <Expression s>;`)
    = HV(toBox(able), I(H0(toExpBox(toBox(operator), s), L(";"))));

Box toBox((Statement) `<Assignable able> <Assignment operator> <Statement s>`)
    = H(H(toBox(able), toBox(operator), blockOpen(s)), 
        indentedBlock(s), 
        blockClose(s))
    when !(s is expression);

Box toBox((Assignable) `<Assignable rec>.<Name field>`)
    = H0(toBox(rec), L("."), toBox(field));

Box toBox((Assignable) `<Assignable rec>?<Expression def>`)
    = H1(toBox(rec), L("?"), toExpBox(def));

Box toBox((Assignable) `<Name name> (<{Assignable ","}+ args>)`)
    = H0(toBox(name), L("("), SL([toBox(a) | a <- args], L(",")), L(")"));

Box toBox((Assignable) `\< <{Assignable ","}+ args> \>`)
    = H0(L("\<"), SL([toBox(a) | a <- args], L(",")), L("\>"));

Box toBox((Assignable) `<Assignable rec>[<Expression sub>]`)
    = H0(toBox(rec), L("["), toExpBox(sub), L("]"));

Box toBox((Assignable) `<Assignable rec>[<Expression from>..<Expression to>]`)
    = H0(toBox(rec), L("["), H1(toBox(from)), L(".."), H1(toBox(to)), L("]"));

Box toBox((Assignable) `<Assignable rec>[<Expression from>, <Expression second>..<Expression to>]`)
    = H0(toBox(rec), L("["), H1(toBox(from)), H1(L(","), H1(toBox(second))), L(".."), H1(toBox(to)), L("]"));

Box toBox((Variable) `<Name name> = <Expression initial>`)
    = HV(toBox(name), I(toExpBox(L("="), initial)));

/* Visit */

Box toBox((Visit) `visit(<Expression subject>) { <Case+ cases> }`)
    = V(H1(H0(L("visit"), L("("), toExpBox(subject), L(")")), L("{")),
        I(toClusterBox(cases)),
        L("}"));

Box toBox((Visit) `<Strategy strategy> visit(<Expression subject>) { <Case+ cases> }`)
    = V(H1(toBox(strategy), H0(L("visit"), L("("), toExpBox(subject), L(")")), L("{")),
        I(toClusterBox(cases)),
        L("}"));

/* Expressions / Patterns */

Box toBox((Expression) `[ <Type typ> ] <Expression e>`)
    = H1(H0(L("["), toBox(typ), L("]")), toExpBox(e));

Box toBox((Pattern) `[ <Type typ> ] <Pattern e>`)
    = H1(H0(L("["), toBox(typ), L("]")), toExpBox(e));

Box toBox((Expression) `#<Type t>`) = H0(L("#"), toBox(t));

Box toBox((Expression) `<Expression e>[<OptionalExpression optFirst>..<OptionalExpression optLast>]`)
    = H0(toBox(e), L("["), toBox(optFirst), L(".."), toBox(optLast), L("]"));

Box toBox((Expression) `<Expression e>[<OptionalExpression optFirst>, <Expression second>..<OptionalExpression optLast>]`)
    = H0(toBox(e), L("["), toBox(optFirst), H1(L(","), toBox(second)), L(".."), toBox(optLast), L("]"));

Box toBox((Expression) `\< <{Expression ","}+ elems> \>`) 
    = H0(L("\<"), toBox(elems), L("\>"));

Box toBox((Expression) `type(<Expression sym>, <Expression grammar>)`)
    = H0(L("type"), L("("), toExpBox(sym), H1(L(","), toExpBox(grammar)), L(")"));

Box toBox((Expression) `( <{Mapping[Expression] ","}* mappings>)`)
    = HOV([L("("),
        AG([toBox(m.from), L(":"), toBox(m.to) |  m <- mappings], gs=3, columns=[l(), c(), l()], rs=L(",")),
        L(")")
    ], hs=0);

Box toBox((Pattern) `\< <{Pattern ","}+ elems> \>`) 
    = H0(L("\<"), toBox(elems), L("\>"));

Box toBox((Pattern) `type(<Pattern sym>, <Pattern grammar>)`)
    = H0(L("type"), L("("), toBox(sym), H1(L(","), toBox(grammar)), L(")"));

Box toBox((Pattern) `( <{Mapping[Pattern] ","}* mappings>)`)
    = HOV([L("("),
        AG([toBox(m.from), L(":"), toBox(m.to) |  m <- mappings], gs=3, columns=[l(), c(), l()], rs=L(",")),
        L(")")
    ], hs=0);


Box toBox((Expression) `{ }`)
    = H0(L("{"), L("}"));

Box toBox((Pattern) `{ }`)
    = H0(L("{"), L("}"));

Box toBox((Expression) `{ <{Expression ","}+ elems>}`)
    = H0(L("{"),
        A([toRow(e) | e <- elems], rs=L(",")),
        L("}")) when elems[0] is \tuple;

Box toBox((Expression) `{ <{Expression ","}+ elems>}`)
    = H0(L("{"),
        toBox(elems),
        L("}")) when !(elems[0] is \tuple); 

Box toBox((Pattern) `{ <{Pattern ","}+ elems>}`)
    = H0(L("{"),
        A([toRow(e) | e <- elems], rs=L(",")),
        L("}")) when elems[0] is \tuple;

Row toRow((Expression) `\< <Expression a>, <{Expression ","}* m>, <Expression b> \>`)
    = R([
        SL([H0(L("\<"), toBox(a)), *[toBox(e) | e <- m], H0(toBox(b), L("\>"))], L(","))  
    ]);

Row toRow((Pattern) `\< <Pattern a>, <{Pattern ","}* m>, <Pattern b> \>`)
    = R([
        SL([H0(L("\<"), toBox(a)), *[toBox(e) | e <- m], H0(toBox(b), L("\>"))], L(","))  
    ]);

default Row toRow(Expression e) = R([toBox(e)]);

Box toBox((Pattern) `{ <{Pattern ","}+ elems>}`)
    = H0(L("{"),
        toBox(elems),
        L("}")) when !(elems[0] is \tuple); 

Box toBox((Pattern) `[<{Pattern ","}* elems>]`)
    = H0(L("["),
        toBox(elems),
        L("]"));

Box toBox((Expression) `<Expression exp>@<Name name>`)
    = H0(toExpBox(exp), L("@"), toBox(name));

Box toBox((ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`)
    = H0(toBox(pre),toExpBox(expression),toBox(tail));

Box toBox((ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`)
    = H0(toBox(mid),toExpBox(expression),toBox(tail));

Box toBox((LocationLiteral) `<ProtocolPart protocolPart><PathPart pathPart>`)
    = H0(toBox(protocolPart), toBox(pathPart));

Box toBox((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`)
    = HOV(toBox(condition),
        I(H(L("?"), toExpBox(thenExp))),
        I(H(L(":"), toExpBox(elseExp))));

// call without kwargs
Box toBox((Expression) `<Expression caller>(<{Expression ","}* arguments>)`)
    = HOV([H0(toBox(caller), L("(")), I(toBox(arguments)), L(")")], hs=0);

// call with kwargs
Box toBox((Expression) `<Expression caller>(<{Expression ","}* arguments>, <{KeywordArgument[Expression] ","}+ kwargs>)`)
    = HOV([H(HOV([H0(toBox(caller), L("(")), I(toBox(arguments))], hs=0), L(",")), I(toBox(kwargs)), L(")")], hs=0);

// call with kwargs no-comma
Box toBox((Expression) `<Expression caller>(<{Expression ","}* arguments> <{KeywordArgument[Expression] ","}+ kwargs>)`)
    = HOV([H0(H0(toBox(caller)), L("(")), I(toBox(arguments)), I(toBox(kwargs)), L(")")], hs=0);

Box toBox({KeywordArgument[&T] ","}+ args) 
    = SL([toBox(a) | a <- args], L(","));

// call without kwargs
Box toBox((Pattern) `<Pattern caller>(<{Pattern ","}* arguments>)`)
    = HOV([H0(toBox(caller), L("(")), toBox(arguments), L(")")], hs=0);

// call with kwargs
Box toBox((Pattern) `<Pattern caller>(<{Pattern ","}* arguments>, <{KeywordArgument[Pattern] ","}+ kwargs>)`)
    = HOV([H(HOV([H0(toBox(caller), L("(")), I(toBox(arguments))], hs=0), L(",")), toBox(kwargs), L(")")], hs=0);

// call with kwargs no-comma
Box toBox((Pattern) `<Pattern caller>(<{Pattern ","}* arguments> <{KeywordArgument[Pattern] ","}+ kwargs>)`)
    = HOV([H0(toBox(caller), L("(")), I(toBox(arguments)), I(toBox(kwargs)), L(")")], hs=0);

/* continue with expressions */

Box toBox((Expression) `[<{Expression ","}* elements>]`)
    = HOV([
        L("["),
        I(toBox(elements)),
        L("]")
    ], hs=0);

Box toBox((Expression) `<Expression cont>[<{Expression ","}+ subscripts>]`)
    = H0(toBox(cont), L("["), toBox(subscripts), L("]"));

Box toBox((Expression)`[<Expression first>..<Expression last>]`)
    = H0(L("["), toBox(first),L(".."),toBox(last), L("]"));

Box toBox((Expression)`[<Expression first>,<Expression second>..<Expression last>]`)
    = H0(L("["), toBox(first), H(L(","), toBox(second)), L(".."), toBox(last), L("]"));

Box toBox((Expression) `<Expression exp>.<Name field>`)
    = H0(toBox(exp), L("."), toBox(field));

Box toBox((Expression)`<Expression exp>[<Name key> = <Expression repl>]`)
    = H0(toBox(exp), L("["),H(toBox(key), L("="), toBox(repl)), L("]"));

Box toBox((Expression) `<Expression exp>\<<{Field ","}+ fields>\>`)
    = H0(toBox(exp),L("\<"), toBox(fields), L("\>"));

Box toBox((Expression) `(<Expression init> | <Expression result> | <{Expression ","}+ gs>)`)
    = HOV(toExpBox(L("("), init),
        toExpBox(L("|"), result),
        toExpBox(L("|"), gs),
        L(")"));

Box toBox((Expression) `any(<{Expression ","}+ gens>)`)
    = H0(H0(L("any"), L("(")),
        toBox(gens),
        L(")"));

Box toBox((Expression) `all(<{Expression ","}+ gens>)`)
    = H0(H0(L("all"), L("(")),
        toBox(gens),
        L(")"));

Box toBox((Expression) `[<{Expression ","}+ results> | <{Expression ","}+ gens>]`)
    = HV(H0(L("["), toBox(results)),
        H0(H(L("|"), toBox(gens)), L("]")));

Box toBox((Expression) `{<{Expression ","}+ results> | <{Expression ","}+ gens>}`)
    = HOV([
        H0(L("{"), toBox(results)),
        H0(H(L("|"), toBox(gens))),
        L("}")
    ], hs=0);

Box toBox((Expression) `(<Expression from> : <Expression to> | <{Expression ","}+ gens>)`)
    = HOV(H0(L("("), HOV(H0(toBox(from), L(":")), I(toBox(to)))),
        H1(L("|"), toBox(gens)),
        L(")"));

Box toBox((Expression) `<Expression exp>[@ <Name name> = <Expression val>]`)
    = H0(toBox(exp), L("["), L("@"), H(toBox(name), L("="), toBox(val)), L("]"));

/* String templates and string literals */

@synopsis{A temporary box to indicate a string literal must be split to the next line.}
data Box = CONTINUE();

@synopsis{String literals require complex re-structuring to flatten complex structures into a list of lines.}
Box toBox(StringLiteral l) {
    list[list[Box]] group([]) = [];
    list[list[Box]] group([Box b]) = [[b]];
    list[list[Box]] group([*Box pre, CONTINUE(), *Box post]) = [[*pre], *group(post)];
    default list[list[Box]] group(list[Box] line) = [line];

    // we collect a flat list of boxes, with intermittents CONTINUE() boxes at the top-level,
    // and then we split the list by the CONTINUE boxes
    lines = group(flatString(l));
   
    // every vertically positioned line, except the first one, starts with the continuation character
    return V(H0(lines[0]),
        *[H0(L("\'"), *line) | line <- lines[1..]]);
}

list[Box] flatString((StringTail) `<MidStringChars mid><Expression e><StringTail tail>`)
    = [*flatString(mid), toBox(e), *flatString(tail)];

list[Box] flatString((StringTail) `<MidStringChars mid><StringTemplate e><StringTail tail>`)
    = [*flatString(mid), *flatString(e), *flatString(tail)];

list[Box] flatString((StringTail) `<PostStringChars ch>`) = flatString(ch);

list[Box] flatString((StringLiteral) `<PreStringChars pre><Expression template><StringTail tail>`)
    = [*flatString(pre), toBox(template), *flatString(tail)];

list[Box] flatString((StringLiteral) `<PreStringChars pre><StringTemplate template><StringTail tail>`) 
    = [*flatString(pre), *flatString(template), *flatString(tail)];

list[Box] flatString((StringLiteral) `"<StringCharacter* sc>"`) 
    = [L("\""),  *[*flatString(s) | s <-sc], L("\"")];

list[Box] flatString((StringConstant) `"<StringCharacter* sc>"`)
    = [L("\""), *[*flatString(s) | s <-sc], L("\"")];

list[Box] flatString((MidStringChars) `\><StringCharacter* sc>\<`)
    = [L("\>"), *[*flatString(s) | s <-sc], L("\<")];

list[Box] flatString((PostStringChars) `\><StringCharacter* sc>"`)
    = [L("\>"), *[*flatString(s) | s <-sc], L("\"")];

list[Box] flatString((PreStringChars) `"<StringCharacter* sc>\<`)
    = [L("\""), *[*flatString(s) | s <-sc], L("\<")];

list[Box] flatString(StringCharacter s) = [CONTINUE()] when s is continuation;

list[Box] flatString((StringMiddle) `<MidStringChars mid>`) = flatString(mid);

list[Box] flatString((StringMiddle) `<MidStringChars mid><StringTemplate template><StringMiddle tail>`) 
    = [*flatString(mid), *flatString(template), *flatString(tail)];

list[Box] flatString((StringMiddle) `<MidStringChars mid><Expression e><StringMiddle tail>`) 
    = [*flatString(mid), toBox(e), *flatString(tail)];


default list[Box] flatString(StringCharacter c) = [L("<c>")];

// Even in templates we keep making sure continuations signs can float to the top list
// We also keep the conditional structures as horizontal as possible, "in line" with the way string templates are interpreted
// We never change whitespace inside of the string literals!
list[Box] flatString((StringTemplate) `if(<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post>}`)
    = [
        H0(H1(L("if"), L("(")), toBox(conds), H1(L(")"), L("{")), I(toBox(pre))), 
        *flatString(body),
        I(toBox(post)),
        L("}")
    ];

list[Box] flatString((StringTemplate) `for(<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post>}`)
    = [
        H0(H1(L("for"), L("(")), toBox(conds), H1(L(")"), L("{")), I(toBox(pre))), 
        *flatString(body),
        I(toBox(post)),
        L("}")
    ];

list[Box] flatString((StringTemplate) `if(<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post>} else { <Statement* preE> <StringMiddle elseS>  <Statement* postE>}`)
    = [
        H0(H1(L("if"), L("(")), toBox(conds), H1(L(")"), L("{"))), 
        I(toBox(pre)), 
        *flatString(body), 
        I(toBox(post)), 
        L("}"),
        H1(L("else"), L("{")),
        I(toBox(preE)), 
        *flatString(elseS), 
        I(toBox(postE)), 
        L("}")
    ];

/* Types */

Box toBox((FunctionType) `<Type typ>(<{TypeArg ","}* args>)`)
    = H0(toBox(typ), L("("), toBox(args), L(")"));

Box toBox((Sym) `&<Nonterminal n>`)
    = H0(L("&"), toBox(n));

Box toBox((Sym) `<Nonterminal n>[<{Sym ","}+ ps>]`)
    = H0(toBox(n),L("["),toBox(ps),L("]"));

Box toBox((StructuredType)`<BasicType bt>[<{TypeArg ","}+ args>]`)
    = H0(toBox(bt),L("["), toBox(args), L("]"));

Box toBox((UserType)`<QualifiedName bt>[<{Type ","}+ args>]`)
    = H0(toBox(bt),L("["), toBox(args), L("]"));

Box toBox((TypeVar) `&<Name n>`)
    = H0(L("&"), toBox(n));

Box toBox((TypeVar) `&<Name n> \<: <Type bound>`)
    = H(H0(L("&"), 
            toBox(n)),
        L("\<:"), 
        toBox(bound));

// this should not be necessary
Box _HV([_H([])]) = U([]);
Box _HV([_V([])]) = U([]);
Box _HV([_U([])]) = U([]);

// helpful short-hands might end up in box::syntax::Box 
Box H0(Box boxes...) = _H(boxes, hs=0);
Box H1(Box boxes...) = _H(boxes, hs=1);
Box V0(Box boxes...) = _V(boxes, hs=0);
Box V1(Box boxes...) = _V(boxes, vs=1);

// TODO: finish this 
void cleanMeUp() {
    loc me = |project://rascal/src/org/rascalmpl/library/lang/rascal/format/Rascal.rsc|;
    m = parse(#start[Module], me);

    m = innermost visit(m) {
        // we remove the nested [brackets] which are now taken care of by varargs overloads
        case (Expression) `H([<{Expression ","}+ exps1>])`   => (Expression) `H(<{Expression ","}+ exps1>)`
        case (Expression) `H0([<{Expression ","}+ exps1>])`   => (Expression) `H0(<{Expression ","}+ exps1>)`
        case (Expression) `H1([<{Expression ","}+ exps1>])`   => (Expression) `H1(<{Expression ","}+ exps1>)`
        case (Expression) `H([<{Expression ","}+ exps2>], hs=0)`   => (Expression) `H0(<{Expression ","}+ exps2>)`
        case (Expression) `H([<{Expression ","}+ exps3>], hs=1)`   => (Expression) `H1(<{Expression ","}+ exps3>)`
        case (Expression) `V([<{Expression ","}+ exps4>])`   => (Expression) `V(<{Expression ","}+ exps4>)`
        case (Expression) `V0([<{Expression ","}+ exps4>])`   => (Expression) `V0(<{Expression ","}+ exps4>)`
        case (Expression) `V1([<{Expression ","}+ exps4>])`   => (Expression) `V1(<{Expression ","}+ exps4>)`
        case (Expression) `V([<{Expression ","}+ exps5>], vs=<Expression v>)`   => (Expression) `V(<{Expression ","}+ exps5>, vs=<Expression v>)`
        case (Expression) `HV([<{Expression ","}+ exps6>])`  => (Expression) `HV(<{Expression ","}+ exps6>)`
        case (Expression) `HOV([<{Expression ","}+ exps7>])` => (Expression) `HOV(<{Expression ","}+ exps7>)`
        case (Expression) `I([<{Expression ","}+ exps8>])`   => (Expression) `I(<{Expression ","}+ exps8>)`
        case (Expression) `I([<{Expression ","}+ exps9>], is=<Expression i>)`   => (Expression) `I(<{Expression ","}+ exps9>, is=<Expression i>)`
        case (Expression) `G([<{Expression ","}+ exps10>], gs=<Expression g>, op=<Expression op>)` => (Expression) `G(<{Expression ","}+ exps10>, gs=<Expression g>, op=<Expression op>)`
        case (Expression) `G([<{Expression ","}+ exps11>], op=<Expression op>, gs=<Expression gs>)`=> (Expression) `G(<{Expression ","}+ exps11>, gs=<Expression g>, op=<Expression op>)`
        case (Expression) `U([<{Expression ","}+ exps12>])`=> (Expression) `U(<{Expression ","}+ exps12>)`

        // singleton H, HV, HOV and V have no effect, but the others do!
        case (Expression) `H(<Expression singleton1>)`   => singleton1
        case (Expression) `V(<Expression singleton2>)`   => singleton2
        case (Expression) `HV(<Expression singleton3>)`  => singleton3
        case (Expression) `HOV(<Expression singleton4>)` => singleton4
    }

    writeFile(me, "<m>");
}