@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::checker::constraints::Pattern

import List;
import ParseTree;
import IO;

import lang::rascal::checker::ListUtils;
import lang::rascal::checker::TreeUtils;
import lang::rascal::types::Types;
import lang::rascal::types::SubTypes;
import lang::rascal::types::TypeSignatures;
import lang::rascal::checker::Annotations;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::scoping::ScopedTypes;
import lang::rascal::checker::constraints::Constraints;

import lang::rascal::syntax::RascalRascal;

public ConstraintBase gatherPatternConstraints(STBuilder st, ConstraintBase cb, Pattern pat) {
    switch(pat) {
        case (Pattern)`<BooleanLiteral _>` :
            return addConstraintForLoc(cb, pat@\loc, makeBoolType());
        
        case (Pattern)`<DecimalIntegerLiteral il>` :
            return addConstraintForLoc(cb, pat@\loc, makeIntType());

        case (Pattern)`<OctalIntegerLiteral il>` :
            return addConstraintForLoc(cb, pat@\loc, makeIntType());

        case (Pattern)`<HexIntegerLiteral il>` :
            return addConstraintForLoc(cb, pat@\loc, makeIntType());

        case (Pattern)`<RealLiteral rl>` :
            return addConstraintForLoc(cb, pat@\loc, makeRealType());

        case (Pattern)`<StringLiteral sl>` :
            return addConstraintForLoc(cb, pat@\loc, makeStrType());

        case (Pattern)`<LocationLiteral ll>` :
            return addConstraintForLoc(cb, pat@\loc, makeLocType());

        case (Pattern)`<DateTimeLiteral dtl>` :
            return addConstraintForLoc(cb, pat@\loc, makeDateTimeType());
        
        case (Pattern)`<RegExpLiteral rl>` : {
            list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
            for (ntree <- names) {
                < cb, tn > = makeFreshType(cb);
                cb = addConstraintForLoc(cb, ntree@\loc, tn);
                cb.constraints = cb.constraints + ConstrainType(tn, makeStrType(), ntree@\loc); 
                if (ntree@\loc in st.itemUses<0>) {
                    cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[ntree@\loc], ntree@\loc);
                } else {
                    cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", ntree@\loc));
                }
            }
            return addConstraintForLoc(cb, pat@\loc, makeStrType());
        }
        
        case (Pattern)`_` : {
            < cb, tn > = makeFreshType(cb);
            if (pat@\loc in st.itemUses<0>)
                cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[pat@\loc], pat@\loc);
            else
                cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", pat@\loc), pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, tn);
        }
        
        case (Pattern)`<Name n>` : {
            < cb, tn > = makeFreshType(cb);
            if (n@\loc in st.itemUses<0>)
                cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[n@\loc], pat@\loc);
            else
                cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", n@\loc), pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, tn);
        }
        
        case (Pattern)`<QualifiedName qn>` : {
            < cb, tn > = makeFreshType(cb);
            if (qn@\loc in st.itemUses<0>)
                cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[qn@\loc], pat@\loc);
            else
                cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", qn@\loc), pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, tn);
        }
        
        case (Pattern) `<Type t> <Name n>` : {
            // NOTE: We don't additionally constrain the name here to be of type t since
            // it was defined to be of type t during the name resolution phase.
            < cb, tn > = makeFreshType(cb);
            if (n@\loc in st.itemUses<0>)
                cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[n@\loc], pat@\loc);
            else
                cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", n@\loc), pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, tn);
        }
        
        case (Pattern) `[<{Pattern ","}* pl>]` : {
            < cb, tlub > = makeFreshType(cb);
            list[RType] lubs = [ makeVoidType() ];
            for (pli <- pl) {
                RType elementType = makeVoidType();
                
                if ((Pattern)`_*` := pli) {
                    < cb, tn > = makeFreshType(cb);
                    elementType = tn;
                    cb = addConstraintForLoc(cb, pli@\loc, makeListType(elementType));
                    if (pli@\loc in st.itemUses<0>)
                        cb.constraints = cb.constraints + DefinedBy(makeListType(elementType), st.itemUses[pli@\loc], pat@\loc);
                    else
                        cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", pli@\loc), pat@\loc);
                } else if ((Pattern)`<QualifiedName qn> *` := pli) {
                    < cb, tn > = makeFreshType(cb);
                    elementType = tn;
                    cb = addConstraintForLoc(cb, pli@\loc, makeListType(elementType));
                    if (qn@\loc in st.itemUses<0>)
                        cb.constraints = cb.constraints + DefinedBy(makeListType(elementType), st.itemUses[qn@\loc], pat@\loc);
                    else
                        cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", qn@\loc), pat@\loc);
                } else {
                    cb = gatherPatternConstraints(st, cb, pli);
                    elementType = typeForLoc(cb, pli@\loc);

                    if ((Pattern)`[<{Pattern ","}* _>]` !:= pli)
                        elementType = SpliceableElement(elementType);
                }

                lubs += elementType;
            }

            cb.constraints = cb.constraints + LubOfList(lubs, tlub, pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, makeListType(tlub));
        }

        
        case (Pattern) `{<{Pattern ","}* pl>}` : {
            < cb, tlub > = makeFreshType(cb);
            list[RType] lubs = [ makeVoidType() ];
            for (pli <- pl) {
                RType elementType = makeVoidType();
                
                if ((Pattern)`_*` := pli) {
                    < cb, tn > = makeFreshType(cb);
                    elementType = tn;;
                    cb = addConstraintForLoc(cb, pli@\loc, makeSetType(elementType));
                    if (pli@\loc in st.itemUses<0>)
                        cb.constraints = cb.constraints + DefinedBy(makeSetType(elementType), st.itemUses[pli@\loc], pat@\loc);
                    else
                        cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", pli@\loc), pat@\loc);
                } else if ((Pattern)`<QualifiedName qn> *` := pli) {
                    < cb, tn > = makeFreshType(cb);
                    elementType = tn;
                    cb = addConstraintForLoc(cb, pli@\loc, makeSetType(elementType));
                    if (qn@\loc in st.itemUses<0>)
                        cb.constraints = cb.constraints + DefinedBy(makeSetType(elementType), st.itemUses[qn@\loc], pat@\loc);
                    else
                        cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", qn@\loc), pat@\loc);
                } else {
                    cb = gatherPatternConstraints(st, cb, pli);
                    elementType = typeForLoc(cb, pli@\loc);

                    if ((Pattern)`{<{Pattern ","}* _>}` !:= pli)
                        elementType = SpliceableElement(elementType);
                }

                lubs += elementType;
            }

            cb.constraints = cb.constraints + LubOfSet(lubs, tlub, pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, makeSetType(tlub));
        }
        
        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
            // TODO: Should throw unimplemented for now, need to implement
            // the reified types as part of the "prelude"...
            list[RType] reifierTypes = [ ];
            for (pli <- pl) {
                cb = gatherPatternConstraints(st, cb, pli);
                reifierTypes += typeForLoc(cb, pli@\loc);            
            }
            if (size(reifierTypes) > 1)
                cb = addConstraintForLoc(cb, pat@\loc, makeFailType("Reified types have an arity of at most 1",pat@\loc));
            return cb; // TODO: Add rest of logic for reified type constructors
        }

        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
            < cb, tr > = makeFreshType(cb);
            < cb, tt > = gatherPatternConstraints(st, cb, p1);
            
            list[RType] paramTypes = [ ];
            for (pli <- pl) {
                cb = gatherPatternConstraints(st, cb, pli);
                paramTypes += typeForLoc(cb, pli@\loc);            
            }

            cb.constraints = cb.constraints + CallOrTree(tt, paramTypes, tr, pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, tr);
        }
        
//        case (Pattern) `<<Pattern pi>>` : {
//            < cb, ti > = gatherPatternConstraints(st, cb, pi);
//            return addConstraintForLoc(cb, pat@\loc, makeTupleType([ti]));
//        }
        
//        case (Pattern) `<<Pattern pi>, <{Pattern ","}* pl>>` : {
//            < cb, ti > = gatherPatternConstraints(st, cb, pi);
//            list[RType] tupleTypes = [ ti ];
//            for (pli <- pl) {
//                < cb, ti > = gatherPatternConstraints(st, cb, pli);
//                tupleTypes += ti;            
//            }
//            return addConstraintForLoc(cb, pat@\loc, makeTupleType(tupleTypes));
//        }

        case (Pattern) `/ <Pattern p>` : {
            cb = gatherPatternConstraints(st, cb, p);
            
            // Need to decide -- reachable from where? Don't want to have to check
            // for this in every other pattern... For now, put in Value, because everything
            // is reachable from Value
            cb.constraints = cb.constraints + PatternReachable(makeValueType(), typeForLoc(cb,p@\loc), pat@\loc);
            
            return addConstraintForLoc(cb, pat@\loc, makeValueType());
        }

        case (Pattern) `<Name n> : <Pattern p>` : {
            cb = gatherPatternConstraints(st, cb, p);
            < cb, tn > = makeFreshType(cb);
                 
            if (n@\loc in st.itemUses<0>) {
                cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[n@\loc], pat@\loc);
                cb.constraints = cb.constraints + Comparable(tn, typeForLoc(cb,p@\loc), U(), pat@\loc);
            } else {
                cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", n@\loc), pat@\loc);
            }
            return cb = addConstraintForLoc(cb, pat@\loc, tn);
        }
        
        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
            cb = gatherPatternConstraints(st, cb, p);
            < cb, tn > = makeFreshType(cb);
                 
            if (n@\loc in st.itemUses<0>) {
                cb.constraints = cb.constraints + DefinedBy(tn, st.itemUses[n@\loc], pat@\loc);
                cb.constraints = cb.constraints + Comparable(tn, typeForLoc(cb,p@\loc), U(), pat@\loc);
            } else {
                cb.constraints = cb.constraints + ConstrainType(tn, makeFailType("No definition for this name found", n@\loc), pat@\loc);
            }
            return addConstraintForLoc(cb, pat@\loc, tn);
        }

        case (Pattern) `[ <Type t> ] <Pattern p>` : {
            // TODO: My read on this is that p can be anything, but will only match
            // the subject if it can match when considered to be of type t.
            // Look into the interpreter to get the actual interpretation.
            // Adding a Comparable constraint anyway.
            cb = gatherPatternConstraints(st, cb, p);
            cb.constraints = cb.constraints + Comparable(convertType(t), typeForLoc(cb,p@\loc), U(), pat@\loc);
            return addConstraintForLoc(cb, pat@\loc, convertType(t));
        }           
        
        case (Pattern) `! <Pattern p>` : {
            cb = gatherPatternConstraints(st, cb, p);
            return addConstraintForLoc(cb, pat@\loc, typeForLoc(cb,p@\loc));
        }
    }

    if (prod(label("Map",_),_,_) := pat[0]) {
        list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
        list[RType] domains = [ makeVoidType() ];
        list[RType] ranges = [ makeVoidType() ];
        for ( < md, mr > <- mapContents ) {
            cb = gatherPatternConstraints(st, cb, md);
            cb = gatherPatternConstraints(st, cb, mr);
            domains += typeForLoc(cb,md@\loc); ranges += typeForLoc(cb,mr@\loc);
        }
        < cb, domainLub > = makeFreshType(cb);
        < cb, rangeLub > = makeFreshType(cb);
        cb.constraints = cb.constraints + LubOf(domains, domainLub, pat@\loc);
        cb.constraints = cb.constraints + LubOf(ranges, rangeLub, pat@\loc);
        RType mapType = makeMapType(domainLub, rangeLub);
        return addConstraintForLoc(cb, pat@\loc, mapType);
    }
    
    if (prod(label("Tuple",_),_,_) := pat[0]) {
        list[Pattern] tupleContents = getTuplePatternContents(pat);
        list[RType] tupleTypes = [ ];
        for (tc <- tupleContents) {
            cb = gatherPatternConstraints(st, cb, tc);
            tupleTypes += typeForLoc(cb, tc@\loc);
        }
        RType tupleType = makeTupleType(tupleTypes);
        return addConstraintForLoc(cb, pat@\loc, tupleType);
    }

    throw "Missing case for <pat>";
}

public list[Pattern] getTuplePatternContents(Pattern pat) {
    return [ p | Pattern p <- getTupleItems(pat) ];
}

public list[Tree] getTupleItems(Tree t) {
    list[Tree] tupleParts = [ ];

    // t[1] holds the parse tree contents for the tuple
    if (list[Tree] tupleTop := t[1]) {
        // tupleTop[0] = <, tupleTop[1] = layout, tupleTop[2] = tuple contents, tupleTop[3] = layout, tupleTop[4] = >, so we get out 2
        if (appl(_,list[Tree] tupleItems) := tupleTop[2]) {
            if (size(tupleItems) > 0) {
                // The tuple items include layout and commas, so we use a mod 4 to account for this: we have
                // item layout comma layout item layout comma layout etc
                tupleParts = [ tupleItems[n] | n <- [0..size(tupleItems)-1], n % 4 == 0];
            }
        }
    }

    return tupleParts;
}
