@bootstrapParser
module rascal::checker::constraints::StringTemplate

import List;
import ParseTree;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Handle string templates
//
public RType checkStringTemplate(StringTemplate s) {
    switch(s) {
        case `for (<{Expression ","}+ gens>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            set[RType] res = { e@rtype | e <- gens } + { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post };
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if (`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if (`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case `if (<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            set[RType] res = { e@rtype | e <- conds } + { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post };
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if (`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if (`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case `if (<{Expression ","}+ conds>) { <Statement* preThen> <StringMiddle bodyThen> <Statement* postThen> } else { <Statement* preElse> <StringMiddle bodyElse> <Statement* postElse> }` : {
            set[RType] res = { e@rtype | e <- conds } + { getInternalStatementType(st@rtype) | st <- preThen } + 
                                         { getInternalStatementType(st@rtype) | st <- postThen } +
                                         { getInternalStatementType(st@rtype) | st <- preElse } + { getInternalStatementType(st@rtype) | st <- postElse };
                list[Tree] ipl = prodFilter(bodyThen, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if (`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if (`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
                ipl = prodFilter(bodyElse, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if (`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if (`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case `while (<Expression cond>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            set[RType] res = { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post } + cond@rtype;
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if (`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if (`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case `do { <Statement* pre> <StringMiddle body> <Statement* post> } while (<Expression cond>)` : {
            set[RType] res = { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post } + cond@rtype;
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if (`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if (`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }
    }

    throw "Unexpected string template syntax at location <s@\loc>, no match";
}