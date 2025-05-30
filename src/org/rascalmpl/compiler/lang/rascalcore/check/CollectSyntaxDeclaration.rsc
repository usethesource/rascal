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
@bootstrapParser
module lang::rascalcore::check::CollectSyntaxDeclaration

/*
    Check syntax declarations
*/

extend lang::rascalcore::check::CheckerCommon;

import IO;
import Set;
import util::Maybe;

import lang::rascalcore::agrammar::definition::Symbols;
import lang::rascalcore::agrammar::definition::Attributes;

import lang::rascal::\syntax::Rascal;

// ---- syntax definition -----------------------------------------------------

void collect(current: (SyntaxDefinition) `<Visibility vis> layout <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, layoutSyntax(), layoutId(), c, vis=getVis(vis, publicVis()));
}

void collect (current: (SyntaxDefinition) `lexical <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, lexicalSyntax(), lexicalId(), c);
}

void collect (current: (SyntaxDefinition) `keyword <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, keywordSyntax(), keywordId(), c);
}

void collect (current: (SyntaxDefinition) `<Start strt> syntax <Sym defined> = <Prod production>;`, Collector c){
    declareSyntax(current, contextFreeSyntax(), nonterminalId(), c);
}

public int nalternatives = 0;
public int syndefCounter = 0;

void declareSyntax(SyntaxDefinition current, SyntaxRole syntaxRole, IdRole idRole, Collector c, Vis vis=publicVis()){
    // println("declareSyntax: <current>");
    Sym defined = current.defined;
    Prod production = current.production;
    nonterminalType = defsym2AType(defined, syntaxRole);

    if(isADTAType(nonterminalType)){
        adtName = nonterminalType.adtName;

        typeParameters = getTypeParameters(defined);
        if(!isEmpty(typeParameters)){
            nonterminalType = nonterminalType[parameters=[ aparameter("<tp.nonterminal>", treeType,closed=true)| tp <- typeParameters ]];
        }

        dt = defType(current is language && current.\start is present ? \start(nonterminalType) : nonterminalType);
        dt.vis = vis;
        dt.md5 = md5Hash("<current is language ? "<current.\start>" : ""><adtName><syndefCounter><unparseNoLayout(defined)>");
        syndefCounter += 1;

        // Define the syntax symbol itself and all labelled alternatives as constructors
        c.define(adtName, idRole, current, dt);

        adtParentScope = c.getScope();
        c.enterScope(current);
            beginDefineOrReuseTypeParameters(c,closed=false);
                collect(defined, c);
            endDefineOrReuseTypeParameters(c);

            // visit all the productions in the parent scope of the syntax declaration
            c.push(currentAdt, <current, [], 0, adtParentScope>);
                beginUseTypeParameters(c,closed=true);
                    collect(production, c);
                endUseTypeParameters(c);
            c.pop(currentAdt);
        c.leaveScope(current);
    } else {
        c.report(error(defined, "Lhs of syntax definition not supported"));
    }
}

// ---- Prod ------------------------------------------------------------------

AProduction getProd(AType adtType, Tree tree, Solver s){
    symType = s.getType(tree);
    if(aprod(AProduction p) := symType) return p;
    return prod(adtType, [symType]/*, src=getLoc(tree)*/);
}

void collect(current: (Prod) `: <Name referenced>`, Collector c){
    c.use(referenced, {constructorId()});
    c.fact(current, referenced);
}

void requireNonLayout(Tree current, AType u, str msg, Solver s){
    if(isLayoutAType(u)) s.report(error(current, "Layout type %t not allowed %v", u, msg));
}

AProduction computeProd(Tree current, str name, AType adtType, ProdModifier* modifiers, list[Sym] symbols, Solver s) {
    args = [s.getType(sym) | sym <- symbols];
    m2a = mods2attrs(modifiers);
    src = getLoc(current);
    p = isEmpty(m2a) ? prod(adtType, args/*, src=src*/) : prod(adtType, args, attributes=m2a/*, src=src*/);
    if(name != ""){
        p.alabel = name;
    }

    forbidConsecutiveLayout(current, args, s);
    if(!isEmpty(args)){
        requireNonLayout(current, args[0], "at begin of production", s);
        requireNonLayout(current, args[-1], "at end of production", s);
    }
    return associativity(adtType, \mods2assoc(modifiers), p);
}

private bool isTerminalSym((Sym) `<Sym symbol> @ <IntegerLiteral _>`) = isTerminalSym(symbol);
private bool isTerminalSym((Sym) `<Sym symbol> $`) = isTerminalSym(symbol);
private bool isTerminalSym((Sym) `^ <Sym symbol>`) = isTerminalSym(symbol);
private bool isTerminalSym((Sym) `<Sym symbol> ! <NonterminalLabel _>`) = isTerminalSym(symbol);
private bool isTerminalSym((Sym) `()`) = true;
private default bool isTerminalSym(Sym s) =  s is characterClass || s is literal || s is caseInsensitiveLiteral;

private AType removeChainRule(aprod(prod(AType adt1,[AType adt2]))) = adt2 when isNonTerminalAType(adt2);
private default AType removeChainRule(AType t) = t;

void collect(current: (Prod) `<ProdModifier* modifiers> <Name name> : <Sym* syms>`, Collector c){
    symbols = [sym | sym <- syms];

    if(<Tree adt, _, _, loc adtParentScope> := c.top(currentAdt)){
        // Compute the production type
        c.calculate("named production", current, adt + symbols,
            AType(Solver s) {
                try {
                    return s.getType(current);
                 } catch _: {
                    res = aprod(computeProd(current, unescape("<name>"), s.getType(adt), modifiers, symbols, s) /* no labels on assoc groups [label=unescape("<name>")]*/);
                    return res;
                 }
            });
        inLexicalAdt = false;
        if(SyntaxDefinition sd := adt){
            inLexicalAdt = sd is \lexical;
        } else {
            println("*** Collect: <adt> is not a SyntaxDefinition ***");
        }
        qualName = "<SyntaxDefinition sd := adt ? sd.defined.nonterminal : "???">_<unescape("<name>")>";

         // Define the constructor
        c.defineInScope(adtParentScope, unescape("<name>"), constructorId(), name, defType([current],
            AType(Solver s){
                ptype = s.getType(current);
                if(aprod(AProduction cprod) := ptype){
                    if(size(symbols) > 0){ // switch to size on concrete syntax
                        s.fact(syms, ptype);
                    }
                    def = cprod.def;
                    fields = [ inLexicalAdt && isLexicalAType(stp) ? astr() : stp
                             |
                               sym <- symbols,
                               !isTerminalSym(sym),
                               tsym := s.getType(sym),
                               isNonTerminalAType(tsym),
                               stp := getSyntaxType(removeChainRule(tsym), s)
                             ];

                    def = \start(sdef) := def ? sdef : def;
                    //def = \start(sdef) := def ? sdef : unset(def, "alabel");
                    return acons(def, fields, [], alabel=unescape("<name>"));
                 } else throw "Unexpected type of production: <ptype>";
            })[md5=md5Hash("<adt><unparseNoLayout(current)>")]);
        beginUseTypeParameters(c,closed=true);
            c.push(currentAlternative, <adt, "<name>", syms>);
                collect(symbols, c);
            c.pop(currentAlternative);
        endUseTypeParameters(c);
    } else {
        throw "collect Named Prod: currentAdt not found";
    }
}

void collect(current: (Prod) `<ProdModifier* modifiers> <Sym* syms>`, Collector c){
    symbols = [sym | sym <- syms];

    if(<Tree adt, _, _, _> := c.top(currentAdt)){
        c.calculate("unnamed production", current, adt + symbols,
            AType(Solver s){
                res = aprod(computeProd(current, "", s.getType(adt), modifiers, symbols, s));
                return res;
            });
        beginUseTypeParameters(c,closed=true);
            c.push(currentAlternative, <adt, "", syms>);
                collect(symbols, c);
            c.pop(currentAlternative);
        endUseTypeParameters(c);
    } else {
        throw "collect Unnamed Prod: currentAdt not found";
    }
}

private AProduction associativity(AType nt, nothing(), AProduction p) = p;
private default AProduction associativity(AType nt, just(AAssociativity a), AProduction p) = associativity(nt, a, {p});

void collect(current: (Prod) `<Assoc ass> ( <Prod group> )`, Collector c){
    asc = AAssociativity::aleft();
    switch("<ass>"){
    case "assoc":       asc = AAssociativity::aleft();
    case "left":        asc = AAssociativity::aleft();
    case "non-assoc":   asc = AAssociativity::\a-non-assoc();
    case "right":       asc = AAssociativity::aright();
    }

    if(<Tree adt, _, _, _> := c.top(currentAdt)){
        c.calculate("assoc", current, [adt, group],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(associativity(adtType, asc, {getProd(adtType, group, s)}));
            });
        collect(group, c);
    } else {
        throw "collect Named Prod: currentAdt not found";
    }
}

list[Prod] normalizeAlt((Prod) `<Prod lhs> | <Prod rhs>`)
    = [*normalizeAlt(lhs), *normalizeAlt(rhs)];

default list[Prod] normalizeAlt(Prod p) = [p];

void collect(current: (Prod) `<Prod lhs> | <Prod rhs>`,  Collector c){
    if(<Tree adt, _,  _, _> := c.top(currentAdt)){
        alts = normalizeAlt(current);
        c.calculate("alt production", current, [adt, *alts],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(achoice(adtType, {getProd(adtType, p, s) | p <- alts}));
            });
        c.push(inAlternative, true);
            collect(alts, c);
        c.pop(inAlternative);
        if(isEmpty(c.getStack(inAlternative))){
            nalternatives += 1;
              c.define("alternative-<nalternatives>", nonterminalId(), current, defType(current)[md5=md5Hash(unparseNoLayout(current))]);
        }
    } else {
        throw "collect alt: currentAdt not found";
    }
}

void collect(current: (Prod) `<Prod lhs> \> <Prod rhs>`,  Collector c){
    if(<Tree adt, _, _, _> := c.top(currentAdt)){
        c.calculate("first production", current, [adt, lhs, rhs],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(priority(adtType, [getProd(adtType, lhs, s), getProd(adtType, rhs, s)]));
            });
        collect(lhs, rhs, c);
    } else {
        throw "collect alt: currentAdt not found";
    }
}