@bootstrapParser
module lang::rascalcore::check::CollectSyntaxDeclaration

extend lang::rascalcore::check::CheckerCommon;

import Set;
//import Node;
import util::Maybe;

import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;

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
    declareSyntax(current, contextFreeSyntax(), nonterminalId(), c/*, isStart=strt is present*/);
}

void declareSyntax(SyntaxDefinition current, SyntaxRole syntaxRole, IdRole idRole, Collector c, /*bool isStart=false,*/ Vis vis=publicVis()){
   //println("declareSyntax: <current>");
    Sym defined = current.defined;
    Prod production = current.production;
    nonterminalType = defsym2AType(defined, syntaxRole);
     
    if(isADTType(nonterminalType)){
        adtName = nonterminalType.adtName;
       
        typeParameters = getTypeParameters(defined);
        if(!isEmpty(typeParameters)){
            nonterminalType = nonterminalType[parameters=[ aparameter("<tp.nonterminal>", treeType)| tp <- typeParameters ]];
        }
        
        dt = defType(nonterminalType);
        dt.vis = vis;        
        
        // Define the syntax symbol itself and all labelled alternatives as constructors
        c.define(adtName, idRole, current, dt);

        adtParentScope = c.getScope();
        c.enterScope(current);
            for(tp <- typeParameters){
                c.define("<tp.nonterminal>", typeVarId(), tp.nonterminal, defType(aparameter("<tp.nonterminal>", treeType)));
            }
            
            // visit all the productions in the parent scope of the syntax declaration
            c.push(currentAdt, <current, [], adtParentScope>);
                collect(production, c);
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
    return prod(adtType, [symType], src=getLoc(tree));
}

void collect(current: (Prod) `: <Name referenced>`, Collector c){
    c.use(referenced, {constructorId()});
    c.fact(current, referenced);
}

void requireNonLayout(Tree current, AType u, str msg, Solver s){
    if(isLayoutType(u)) s.report(error(current, "Layout type %t not allowed %v", u, msg));
}

AProduction computeProd(Tree current, str name, AType adtType, ProdModifier* modifiers, list[Sym] symbols, Solver s) {
    //try {   
    //    if(ap: aprod(p) := s.getType(current)){
    //        return aprod(ap);
    //    }     
    //} catch e:;
    args = [s.getType(sym) | sym <- symbols];  
    m2a = mods2attrs(modifiers);
    src = getLoc(current);
    p = isEmpty(m2a) ? prod(adtType, args, src=src) : prod(adtType, args, attributes=m2a, src=src);
    if(name != ""){
        p.label = name;
    }
    
    forbidConsecutiveLayout(current, args, s);
    if(!isEmpty(args)){
        requireNonLayout(current, args[0], "at begin of production", s);
        requireNonLayout(current, args[-1], "at end of production", s);
    }
    return associativity(adtType, \mods2assoc(modifiers), p);
}

private bool isTerminal(Sym s) =  s is characterClass || s is literal || s is caseInsensitiveLiteral;

void collect(current: (Prod) `<ProdModifier* modifiers> <Name name> : <Sym* syms>`, Collector c){
    symbols = [sym | sym <- syms];
    
    typeParametersInSymbols = {*getTypeParameters(sym) | sym <- symbols };
    for(tv <- typeParametersInSymbols){
        c.use(tv.nonterminal, {typeVarId()});
    }
    
    if(<Tree adt, list[KeywordFormal] _, loc adtParentScope> := c.top(currentAdt)){
        // Compute the production type
        c.calculate("named production", current, adt + symbols,
            AType(Solver s) {
                try return s.getType(current); catch _: /*not yet known*/;
                res = aprod(computeProd(current, unescape("<name>"), s.getType(adt), modifiers, symbols, s) /* no labels on assoc groups [label=unescape("<name>")]*/);
                return res;
            });
        //qualName = unescape("<name>"); // 
        qualName = "<SyntaxDefinition sd := adt ? sd.defined.nonterminal : "???">_<unescape("<name>")>";
        
         // Define the constructor (using a location annotated with "cons" to differentiate from the above)
        c.defineInScope(adtParentScope, unescape("<name>") /*qualName*/, constructorId(), getLoc(current)[fragment="cons"], defType([current], 
            AType(Solver s){
                ptype = s.getType(current);
                if(aprod(AProduction cprod) := ptype){
                    if(size([sym | sym <- symbols]) > 0){ // switch to size on concrete syntax
                        s.fact(syms, ptype);
                    }
                    def = cprod.def;
                    fields = [ t | sym <- symbols, !isTerminal(sym), tsym := s.getType(sym), t := removeConditional(tsym), isNonTerminalType(t)];
                                                
                    //fields = cprod has atypes ? [ t | sym <- cprod.atypes, tsym := s.getType(sym), t := removeConditional(tsym), isNonTerminalType(t)]
                    //                          : [];          
                    def = \start(sdef) := def ? sdef : def;
                    //def = \start(sdef) := def ? sdef : unset(def, "label");
                    return acons(def, fields, [], label=unescape("<name>"));
                 } else throw "Unexpected type of production: <ptype>";
            }));
        collect(symbols, c);
    } else {
        throw "collect Named Prod: currentAdt not found";
    }
}

void collect(current: (Prod) `<ProdModifier* modifiers> <Sym* syms>`, Collector c){
    symbols = [sym | sym <- syms];
    typeParametersInSymbols = {*getTypeParameters(sym) | sym <- symbols };
    for(tv <- typeParametersInSymbols){
        c.use(tv.nonterminal, {typeVarId()});
    }
 
    if(<Tree adt, list[KeywordFormal] _, loc _> := c.top(currentAdt)){
        c.calculate("unnamed production", current, adt + symbols,
            AType(Solver s){
                res = aprod(computeProd(current, "", s.getType(adt), modifiers, symbols, s));
                return res;
            });
        collect(symbols, c);
    } else {
        throw "collect Unnamed Prod: currentAdt not found";
    }
}

private AProduction associativity(AType nt, nothing(), AProduction p) = p;
private default AProduction associativity(AType nt, just(Associativity a), AProduction p) = associativity(nt, a, {p});

void collect(current: (Prod) `<Assoc ass> ( <Prod group> )`, Collector c){
    asc = Associativity::\left();
    switch("<ass>"){
    case "assoc":       asc = Associativity::\left();
    case "left":        asc = Associativity::\left();
    case "non-assoc":   asc = Associativity::\non-assoc();
    case "right":       asc = Associativity::\right();
    }
    
    if(<Tree adt, list[KeywordFormal] _, loc _> := c.top(currentAdt)){
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

void collect(current: (Prod) `<Prod lhs> | <Prod rhs>`,  Collector c){
    if(<Tree adt, list[KeywordFormal] _, loc _> := c.top(currentAdt)){
        c.calculate("alt production", current, [adt, lhs, rhs],
            AType(Solver s){
                adtType = s.getType(adt);
                return aprod(choice(adtType, {getProd(adtType, lhs, s), getProd(adtType, rhs, s)}));
            });
        c.push(inAlternative, true);
            collect(lhs, rhs, c);
        c.pop(inAlternative);
        if(isEmpty(c.getStack(inAlternative))){
              c.define("production", nonterminalId(), current, defType(current));
        }
    } else {
        throw "collect alt: currentAdt not found";
    }
}
 
void collect(current: (Prod) `<Prod lhs> \> <Prod rhs>`,  Collector c){
    if(<Tree adt, list[KeywordFormal] _, loc _> := c.top(currentAdt)){
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

default void collect(Prod current, Collector c){
    throw "collect Prod, missed case <current>";
}