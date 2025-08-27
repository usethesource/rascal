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
        iprintln(toBox(\module));
        println(format(toBox(\module)));
        return layoutDiff(\module, parse(#start[Module], format(toBox(\module)), \module@\loc.top));
    }
    catch e:ParseError(loc place): { 
        writeFile(|tmp:///temporary.rsc|, format(toBox(\module)));
        println("Formatted module contains a parse error here: <place>");
        throw e;
    }
}


/* Modules */

Box toBox(Toplevel* toplevels) = V([toBox(t) | t <- toplevels], vs=2);

Box toBox((Module) `<Tags tags> module <QualifiedName name> <Import* imports> <Body body>`)
    = V([
        toBox(tags),
        H([L("module"), toBox(name)]),
        toBox(imports),
        toBox(body)
    ], vs=1);

Box toBox(Import* imports) = V([toBox(i) | i <- imports]);

Box toBox((Import) `import <ImportedModule m>;`)
    = H([L("import"), H0([toBox(m), L(";")])]);

Box toBox((Import) `extend <ImportedModule m>;`)
    = H([L("extend"), H0([toBox(m), L(";")])]);

Box toBox((QualifiedName) `<{Name "::"}+ names>`)
    = H0([SL([toBox(n) | n <- names], L("::"), op=H([]))]);
    
Box toBox((Visibility) ``) = NULL();

/* Declarations */

Box toBox((Declaration) `<Tags t> <Visibility v> data <UserType t> <CommonKeywordParameters ps>;`)
    = V([
        toBox(t),
        H([
            toBox(v), L("data"), H0([toBox(t), <toBox(ps)>, L(";")])
        ])
    ]);

Box toBox((Declaration) `<Tags t> <Visibility v> data <UserType typ> <CommonKeywordParameters ps> = <{Variant "|"}+ vs>;`)
    = V([
        toBox(t),
        H([toBox(v), L("data"), H0([toBox(typ), toBox(ps)])]),
        I([H([HOV([G([
                L("="),
                *[L("|"), toBox(va) | va <- vs][1..] // host the bars `|` up to the same level of `=`
            ])])
        ]), L(";")], hs=0)
    ]);

Box toBox((Variant) `<Name n>(<{TypeArg ","}* args>, <{KeywordFormal ","}+ kws>)`)
    = H0([
        toBox(n),
        L("("),
        HOV([toBox(args)]),
        H([
            L(","),
            HOV([toBox(kws)])
        ]),
        L(")")
    ]);

Box toBox((Variant) `<Name n>(<{TypeArg ","}* args>)`)
    = H0([
        toBox(n),
        L("("),
        HOV([toBox(args)]),
        L(")")
    ]);

Box toBox((Variant) `<Name n>(<{TypeArg ","}* args>
                    '<{KeywordFormal ","}+ kws>)`)
    = H0([
        H0([toBox(n), L("(")]),
        H0([toBox(args), L(",")]),
        HOV([toBox(kws)]),
        L(")")
    ]);

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
        H([toBox(vis), H0([toBox(sig), L(";")])])
    ]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Expression exp>;`)
    = V([
        toBox(tags),
        HOV([
            H([toBox(vis), toBox(sig)]),
            I([H([L("="), H0([toBox(exp), L(";")])])])
        ])
    ]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> = <Expression exp> when <{Expression ","}+ conds>;`)
    = V([
        toBox(tags),
        HOV([
            H([toBox(vis), toBox(sig)]),
            I([H([L("="), toBox(exp)])])
        ]),
        I([H([L("when"), H0([HOV([toBox(conds)]), L(";")])])])
    ]);

Box toBox((FunctionDeclaration) `<Tags tags> <Visibility vis> <Signature sig> { <Statement* stats> }`)
    = V([
        toBox(tags),
        H([toBox(vis), toBox(sig), L("{")]),
        I([toBox(stats)]),
        L("}")
    ]);
    
Box toBox(Tag* tags) = V([toBox(t) | Tag t <- tags]);

Box toBox((Tag) `@synopsis<TagString c>`) 
    = H0([
        L("@"), L("synopsis"), 
        HV([toBox(c)])
    ]);

Box toBox((Tag) `@<Name n> <TagString c>`) 
    = HOV([
        H0([L("@"), L("<n>")]),
        toBox(c)
    ])
    when "<n>" != "synopsis";

Box toBox((Tag) `@<Name n>`) 
    = HOV([
        H0([L("@"), L("<n>")])
    ]);

Box toBox((Parameters) `( <Formals formals> <KeywordFormals keywordFormals>)`)
    = H([L("("), HV([toBox(formals), toBox(keywordFormals)]), L(")")], hs=0);

Box toBox((Parameters) `( <Formals formals> ... <KeywordFormals keywordFormals>)`)
    = H([L("("), H([H([toBox(formals), L("...")], hs=0), toBox(keywordFormals)]), L(")")], hs=0);

/* Statements */

// TODO retain original grouping
Box toBox(Statement* stmts) = V([toBox(st) | st <- stmts]);

Box toBox((Statement) `return <Expression e>;`)
    = HV([L("return"), I([H([toBox(e), L(";")], hs=0)])]);
	
// if with a block statement is formatted differently then without the block
Box toBox((Statement) `<Label label> if (<{Expression ","}+ cs>) 
                      '  <Statement sts>`)
    = V([
        H([
            H0([toBox(label), L("if")]), 
            H0([L("("), toBox(cs), L(")")]),
            blockOpen(sts)
        ]),
        indentedBlock(sts),
        blockClose(sts)
    ]);

Box toBox((Statement) `<Label label> if (<{Expression ","}+ cs>)
                      '  <Statement sts>
                      'else
                      ' <Statement ests>`)
    = V([
        H([
            H0([toBox(label), L("if")]), 
            H0([L("("), toBox(cs), L(")")]),
            blockOpen(sts)
        ]),
        indentedBlock(sts),
        blockClose(sts),
        H([L("else"), blockOpen(ests)]),
        indentedBlock(ests),
        blockClose(ests)
    ]);

Box toBox((Statement) `<Expression exp>;`)
    = H0([toBox(exp), L(";")]);

Box toBox((Statement) `throw <Statement e>`)
    = H([L("throw"), toBox(e)]);

Box toBox((Statement) `<Label label> for(<{Expression ","}+ gs> ) <Statement block>`) 
    = HOV([
        H0([toBox(label), H([L("for"), L("(")]), HV([toBox(gs)]), H([L(")"), blockOpen(block)])]),
        indentedBlock(block),
        blockClose(block)
    ]);

 Box toBox((Statement) `switch(<Expression e>) { <Case+ cases> }`)  
    = V([
        H0([L("switch"), L("("), toBox(e), H([L(")"), L("{")])]),
        I([V([toBox(cases)], vs=1)]),
        L("}")
    ]);

Box toBox((Case) `case <Pattern p>:  <Statement block>`)
    = HOV([H([L("case"), H0([toBox(p), L(":")]), blockOpen(block)]), indentedBlock(block), blockClose(block)]);

Box toBox((Case) `case <Pattern p> =\> <Expression repl>`)
    = H([L("case"), toBox(p), HV([I([L("=\>"), toBox(repl)])])]);

Box toBox((Case) `default: <Statement block>`)
    = HOV([H([L("default:"), blockOpen(block)]), I([indentedBlock(block)]), blockClose(block)]);


Box toBox((Statement) `try <Statement body> <Catch+ handlers>`)
    = V([
        H([L("try"), blockOpen(body)]),
        indentedBlock(body),
        blockClose(body),
        V([toBox(handlers)])
    ]);

Box toBox((Statement) `try <Statement body> <Catch+ handlers> finally <Statement fBody>`)
    = V([
        H([L("try"), blockOpen(body)]),
        indentedBlock(body),
        blockClose(body),
        V([toBox(handlers)]),
        H([L("finally"), blockOpen(fBody)]),
        indentedBlock(fBody),
        blockClose(fBody)
    ]);

Box toBox((Catch) `catch: <Statement body>`)
    = V([
        H([H0([L("catch"), L(":")]), blockOpen(body)]),
        indentedBlock(body),
        blockClose(body)
    ]);

Box toBox((Catch) `catch <Pattern p>: <Statement body>`)
    = V([
        H([L("catch"), H0([toBox(p), L(":")]), blockOpen(body)]),
        indentedBlock(body),
        blockClose(body)
    ]);



// These are three reusable buildig blocks to avoid case-distinction overloads, with and without curlies
// Using these functions  avoids a combinatorial explosion of overloads for syntax constructs
// with multiple blocks of statements, like if-then-else and try-catch-finally.
Box blockOpen(Statement s) = s is nonEmptyBlock ? L("{") : NULL();

Box blockClose(Statement s) = s is nonEmptyBlock ? L("}") : NULL();

Box indentedBlock((Statement) `{<Statement+ st>}`)
    = I([V([toBox(st)])]);

default Box indentedBlock(Statement s) = I([toBox(s)]);

/* Expressions / Patterns */

Box toBox((Expression) `\< <{Expression ","}+ elems> \>`) 
    = H0([L("\<"), HV([toBox(elems)]), L("\>")]);

Box toBox((Expression) `type(<Expression sym>, <Expression grammar>)`)
    = H0([L("type"), L("("), toBox(sym), H1([L(","), toBox(grammar)]), L(")")]);

Box toBox((Expression) `( <{Mapping[Expression] ","}* mappings>)`)
    = HOV([L("("),
        AG([toBox(m.from), L(":"), toBox(m.to) |  m <- mappings], gs=3, columns=[l(), c(), l()], rs=L(",")),
        L(")")
    ], hs=0);

Box toBox((Pattern) `\< <{Pattern ","}+ elems> \>`) 
    = H0([L("\<"), HV([toBox(elems)]), L("\>")]);

Box toBox((Pattern) `type(<Pattern sym>, <Pattern grammar>)`)
    = H0([L("type"), L("("), toBox(sym), H1([L(","), toBox(grammar)]), L(")")]);

Box toBox((Pattern) `( <{Mapping[Pattern] ","}* mappings>)`)
    = HOV([L("("),
        AG([toBox(m.from), L(":"), toBox(m.to) |  m <- mappings], gs=3, columns=[l(), c(), l()], rs=L(",")),
        L(")")
    ], hs=0);


Box toBox((Expression) `{ }`)
    = H([L("{"), L("}")]);

Box toBox((Pattern) `{ }`)
    = H([L("{"), L("}")]);

Box toBox((Expression) `{ <{Expression ","}+ elems>}`)
    = H0([
        L("{"),
        AG([L("\<"), SL([toBox(f) | f <- e.elements ], L(",")), L("\>")  | e <- elems], gs=3, rs=L(",")),
        L("}")
    ]) when elems[0] is \tuple;

Box toBox((Expression) `{ <{Expression ","}+ elems>}`)
    = H0([
        L("{"),
        HV([toBox(elems)]),
        L("}")
    ]) when !(elems[0] is \tuple); 

Box toBox((Pattern) `{ <{Pattern ","}+ elems>}`)
    = H0([
        L("{"),
        AG([L("\<"), SL([toBox(f) | f <- e.elements ], L(",")), L("\>")  | e <- elems], gs=3, rs=L(",")),
        L("}")
    ]) when elems[0] is \tuple;

Box toBox((Pattern) `{ <{Pattern ","}+ elems>}`)
    = H0([
        L("{"),
        HV([toBox(elems)]),
        L("}")
    ]) when !(elems[0] is \tuple); 

Box toBox((Pattern) `[<{Pattern ","}* elems>]`)
    = H0([
        L("["),
        HV([toBox(elems)]),
        L("]")
    ]);

Box toBox((Expression) `<Expression exp>@<Name name>`)
    = H0([toBox(exp), L("@"), toBox(name)]);

Box toBox((ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`)
    = H0([toBox(pre),toBox(expression),toBox(tail)]);

Box toBox((ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`)
    = H0([toBox(mid),toBox(expression),toBox(tail)]);

Box toBox((LocationLiteral) `<ProtocolPart protocolPart><PathPart pathPart>`)
    = H0([toBox(protocolPart), toBox(pathPart)]);

Box toBox((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`)
    = HOV([
        toBox(condition),
        I([H([L("?"), toBox(thenExp)])]),
        I([H([L(":"), toBox(elseExp)])])
    ]);

// call without kwargs
Box toBox((Expression) `<Expression caller>(<{Expression ","}* arguments>)`)
    = H0([toBox(caller), L("("), HOV([toBox(arguments)]), L(")")]);

// call with kwargs
Box toBox((Expression) `<Expression caller>(<{Expression ","}* arguments>, <{KeywordArgument[Expression] ","}+ kwargs>)`)
    = H0([toBox(caller), L("("), H0([HOV([toBox(arguments)]), L(",")]), HOV([toBox(kwargs)]), L(")")]);

// call with kwargs no-comma
Box toBox((Expression) `<Expression caller>(<{Expression ","}* arguments> <{KeywordArgument[Expression] ","}+ kwargs>)`)
    = H0([toBox(caller), L("("), V([toBox(arguments),toBox(kwargs)]), L(")")]);

Box toBox({KeywordArgument[&T] ","}+ args) 
    = SL([toBox(a) | a <- args], L(","), hs=0);

// call without kwargs
Box toBox((Pattern) `<Pattern caller>(<{Pattern ","}* arguments>)`)
    = H0([toBox(caller), L("("), HOV([toBox(arguments)]), L(")")]);

// call with kwargs
Box toBox((Pattern) `<Pattern caller>(<{Pattern ","}* arguments>, <{KeywordArgument[Pattern] ","}+ kwargs>)`)
    = H0([toBox(caller), L("("), H0([HOV([toBox(arguments)]), L(",")]), HOV([toBox(kwargs)]), L(")")]);

// call with kwargs no-comma
Box toBox((Pattern) `<Pattern caller>(<{Pattern ","}* arguments> <{KeywordArgument[Pattern] ","}+ kwargs>)`)
    = H0([toBox(caller), L("("), V([toBox(arguments),toBox(kwargs)]), L(")")]);

/* continue with expressions */

Box toBox((Expression) `<Expression cont>[<{Expression ","}+ subscripts>]`)
    = H0([toBox(cont), L("["), HV([toBox(subscripts)]), L("]")]);

Box toBox((Expression)`[<Expression first>..<Expression last>]`)
    = H0([L("["), toBox(first),L(".."),toBox(last), L("]")]);

Box toBox((Expression)`[<Expression first>,<Expression second>..<Expression last>]`)
    = H0([L("["), toBox(first), H([L(","), toBox(second)]), L(".."), toBox(last), L("]")]);

Box toBox((Expression) `<Expression exp>.<Name field>`)
    = H0([toBox(exp), L("."), toBox(field)]);

Box toBox((Expression)`<Expression exp>[<Name key> = <Expression repl>]`)
    = H0([toBox(exp), L("["),H([toBox(key), L("="), toBox(repl)]), L("]")]);

Box toBox((Expression) `<Expression exp>\<<{Field ","}+ fields>\>`)
    = H0([toBox(exp),L("\<"), HV([toBox(fields)]), L("\>")]);

Box toBox((Expression) `(<Expression init> | <Expression result> | <{Expression ","}+ gs>)`)
    = HOV([
        L("("),
        H([toBox(init), I([H([L("|"), toBox(result)])]), H([L("|"), HV([toBox(gs)])])]),
        L(")")
    ]);

Box toBox((Expression) `any(<{Expression ","}+ gens>)`)
    = H0([
        H0([L("any"), L("(")]),
        HV([toBox(gens)]),
        L(")")
    ]);

Box toBox((Expression) `all(<{Expression ","}+ gens>)`)
    = H0([
        H0([L("all"), L("(")]),
        HV([toBox(gens)]),
        L(")")
    ]);

Box toBox((Expression) `[<{Expression ","}+ results> | <{Expression ","}+ gens>]`)
    = HV([
        H0([L("["), HV([toBox(results)])]),
        H0([H([L("|"), HOV([toBox(gens)])]), L("]")])
    ]);

Box toBox((Expression) `{<{Expression ","}+ results> | <{Expression ","}+ gens>}`)
    = HV([
        H0([L("{"), HV([toBox(results)])]),
        H0([H([L("|"), HOV([toBox(gens)])]), L("}")])
    ]);

Box toBox((Expression) `(<Expression from> : <Expression to> | <{Expression ","}+ gens>)`)
    = HV([
        H0([L("{"), HOV([toBox(from), H([L(":"), toBox(to)])])]),
        H0([H([L("|"), HOV([toBox(gens)])]), L("}")])
    ]);

Box toBox((Expression) `<Expression exp>[@ <Name name> = <Expression val>]`)
    = H0([toBox(exp), L("["), L("@"), H([toBox(name), L("="), toBox(val)]), L("]")]);

/* String templates */

// String literals and string templates

@synopsis{A temporary box to indicate a string literal must be split to the next line.}
data Box = CONTINUE();

@synopsis{String literals require complex re-structuring to flatten complex structures into a list of lines.}
Box toBox(StringLiteral l) {
    list[list[Box]] group([]) = [];
    list[list[Box]] group([Box b]) = [[b]];
    list[list[Box]] group([*Box pre, CONTINUE(), *Box post]) = [[*pre], *group(post)];
    default list[list[Box]] group(list[Box] line) = [line];

    lines = group(flatString(l));
    
   
        return V([
            H0(lines[0]),
            *[H0([L("\'"), *line]) | line <- lines[1..]]]);
   
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

/*
 mid: MidStringChars mid 
	| template: MidStringChars mid StringTemplate template StringMiddle tail 
	| interpolated: MidStringChars mid Expression expression StringMiddle tail ;
    */

list[Box] flatString((StringMiddle) `<MidStringChars mid>`) = flatString(mid);

list[Box] flatString((StringMiddle) `<MidStringChars mid><StringTemplate template><StringMiddle tail>`) 
    = [*flatString(mid), *flatString(template), *flatString(tail)];

list[Box] flatString((StringMiddle) `<MidStringChars mid><Expression e><StringMiddle tail>`) 
    = [*flatString(mid), toBox(e), *flatString(tail)];


default list[Box] flatString(StringCharacter c) = [L("<c>")];

// even in templates we keep making sure continuations can float to the top
list[Box] flatString((StringTemplate) `if(<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post>}`)
    = [
        H0([H1([L("if"), L("(")]), HV([toBox(conds)]), H1([L(")"), L("{")]), V([I([toBox(pre)])])]), 
        *flatString(body),
        V([I([toBox(post)])]),
        L("}")
    ];

list[Box] flatString((StringTemplate) `for(<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post>}`)
    = [
        H0([H1([L("for"), L("(")]), HV([toBox(conds)]), H1([L(")"), L("{")]), V([I([toBox(pre)])])]), 
        *flatString(body),
        V([I([toBox(post)])]),
        L("}")
    ];

list[Box] flatString((StringTemplate) `if(<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post>} else { <Statement* preE> <StringMiddle elseS>  <Statement* postE>}`)
    = [
        H0([H1([L("if"), L("(")]), HV([toBox(conds)]), H1([L(")"), L("{")])]), 
        V([I([toBox(pre)])]), 
        *flatString(body), 
        V([I([toBox(post)])]), 
        L("}"),
        H1([L("else"), L("{")]),
        V([I([toBox(preE)])]), 
        *flatString(elseS), 
        V([I([toBox(postE)])]), 
        L("}")
    ];

/* Types */

Box toBox((FunctionType) `<Type typ>(<{TypeArg ","}* args>)`)
    = H0([toBox(typ), L("("), HV([toBox(args)]), L(")")]);

Box toBox((Sym) `&<Nonterminal n>`)
    = H0([L("&"), toBox(n)]);

Box toBox((Sym) `<Nonterminal n>[<{Sym ","}+ ps>]`)
    = H0([toBox(n),L("["),HV([toBox(ps)]),L("]")]);

Box toBox((StructuredType)`<BasicType bt>[<{TypeArg ","}+ args>]`)
    = H0([toBox(bt),L("["), toBox(args), L("]")]);

Box toBox((UserType)`<QualifiedName bt>[<{Type ","}+ args>]`)
    = H0([toBox(bt),L("["), toBox(args), L("]")]);

Box toBox((TypeVar) `&<Name n>`)
    = H0([L("&"), toBox(n)]);

Box toBox((TypeVar) `&<Name n> \<: <Type bound>`)
    = H([
        H0([
            L("&"), 
            toBox(n)
        ]),
        L("\<:"), 
        toBox(bound)
    ]);

// this should not be necessary
Box HV([H([])]) = U([]);
Box HV([V([])]) = U([]);
Box HV([U([])]) = U([]);

// helpful short-hands might end up in box::syntax::Box 
Box H0(list[Box] boxes) = H(boxes, hs=0);
Box H1(list[Box] boxes) = H(boxes, hs=0);
Box V0(list[Box] boxes) = V(boxes, hs=0);
Box V1(list[Box] boxes) = V(boxes, hs=0);