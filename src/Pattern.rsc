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
import Set;

import constraints::Constraint;
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

//
// Directly inside a container, does this pattern represent a name?
//
public bool spliceablePattern(Pattern p) {
	switch(p) {
        case (Pattern)`<Name n>` : return true;
        case (Pattern)`<QualifiedName qn>` : return true;
        case (Pattern) `<Type t> <Name n>` : return isListType(convertType(t));
        default: return false;
	}
}

public ConstraintBase gatherPatternConstraints(STBuilder st, ConstraintBase cb, Pattern patPC) {
	
	void gatherPatternConstraints(Pattern pat) {
	    switch(pat) {
	        case (Pattern)`<BooleanLiteral _>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeBoolType());
	            return;
	        }
	        
	        case (Pattern)`<DecimalIntegerLiteral il>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeIntType());
	            return;
	        }

	        case (Pattern)`<OctalIntegerLiteral il>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeIntType());
	            return;
	        }

	        case (Pattern)`<HexIntegerLiteral il>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeIntType());
	            return;
	        }
	
	        case (Pattern)`<RealLiteral rl>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeRealType());
	            return;
	        }
	
	        case (Pattern)`<StringLiteral sl>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeStrType());
	            return;
	        }
	
	        case (Pattern)`<LocationLiteral ll>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeFailType("Location literals are not allowed in patterns",pat@\loc));
	            return;
	        }
	
	        case (Pattern)`<DateTimeLiteral dtl>` : {
	            cb = addConstraintForLoc(cb, pat@\loc, makeFailType("Datetime literals are not allowed in patterns", pat@\loc));
	            return;
	        }
	        
	        case (Pattern)`<RegExpLiteral rl>` : {
	            list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
	            for (ntree <- names) {
			        if (ntree@\loc in st.itemUses<0>) {
					    RType itemType = makeVoidType();
			        	definingIds = st.itemUses[ntree@\loc];
					    if (size(definingIds) > 1) {
					        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
					        cb.constraints = cb.constraints + { ConstrainType(getTypeForItem(st,itemId), makeStrType(), ntree@\loc) | itemId <- definingIds };
					    } else {
					        itemType = getTypeForItem(st, getOneFrom(definingIds));
					        cb.constraints = cb.constraints + ConstrainType(itemType, makeStrType, ntree@\loc);
					    }
				        cb = addConstraintForLoc(cb, ntree@\loc, itemType);
			        } else {
			            cb = addConstraintForLoc(cb, ntree@\loc, makeFailType("No definition for this variable found",ntree@\loc));
			        }
	            }
	            cb = addConstraintForLoc(cb, pat@\loc, makeStrType());
	            return;
	        }
	        
	        case (Pattern)`_` : {
		        if (pat@\loc in st.itemUses<0>) {
				    RType itemType = makeVoidType();
		        	definingIds = st.itemUses[pat@\loc];
				    if (size(definingIds) > 1) {
				        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
				    } else if (size(definingIds) == 1) {
				        itemType = getTypeForItem(st, getOneFrom(definingIds));
				    }
			        cb = addConstraintForLoc(cb, pat@\loc, itemType);
		        } else {
		            cb = addConstraintForLoc(cb, pat@\loc, makeFailType("No definition for this variable found",pat@\loc));
		        }
	            return;
	        }
	        
	        case (Pattern)`<Name n>` : {
		        if (n@\loc in st.itemUses<0>) {
				    RType itemType = makeVoidType();
		        	definingIds = st.itemUses[n@\loc];
				    if (size(definingIds) > 1) {
				        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
				    } else if (size(definingIds) == 1) {
				        itemType = getTypeForItem(st, getOneFrom(definingIds));
				    }
			        cb = addConstraintForLoc(cb, n@\loc, itemType);
		        } else {
		            cb = addConstraintForLoc(cb, n@\loc, makeFailType("No definition for this variable found",n@\loc));
		        }
	            return;
	        }
	        
	        case (Pattern)`<QualifiedName qn>` : {
		        if (qn@\loc in st.itemUses<0>) {
				    RType itemType = makeVoidType();
		        	definingIds = st.itemUses[qn@\loc];
				    if (size(definingIds) > 1) {
				        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
				    } else if (size(definingIds) == 1) {
				        itemType = getTypeForItem(st, getOneFrom(definingIds));
				    }
			        cb = addConstraintForLoc(cb, qn@\loc, itemType);
		        } else {
		            cb = addConstraintForLoc(cb, qn@\loc, makeFailType("No definition for this variable found",qn@\loc));
		        }
	            return;
	        }
	        
	        case (Pattern) `<Type t> <Name n>` : {
	            // NOTE: We don't additionally constrain the name here to be of type t since
	            // it was defined to be of type t during the name resolution phase.
		        if (n@\loc in st.itemUses<0>) {
				    RType itemType = makeVoidType();
		        	definingIds = st.itemUses[n@\loc];
				    if (size(definingIds) > 1) {
				        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
				    } else if (size(definingIds) == 1) {
				        itemType = getTypeForItem(st, getOneFrom(definingIds));
				    }
			        cb = addConstraintForLoc(cb, pat@\loc, itemType);
		        } else {
		            cb = addConstraintForLoc(cb, pat@\loc, makeFailType("No definition for this variable found",n@\loc));
		        }
	            return;
	        }
	        
	        case (Pattern) `[<{Pattern ","}* pl>]` : {
	            < cb, tlub > = makeFreshType(cb);
	            list[RType] lubs = [ makeVoidType() ];
	            for (pli <- pl) {
	                RType elementType = makeVoidType();
	                
	                if ((Pattern)`_*` := pli) {
				        if (pli@\loc in st.itemUses<0>) {
				        	definingIds = st.itemUses[pli@\loc];
						    if (size(definingIds) > 1) {
						        elementType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
						    } else if (size(definingIds) == 1) {
						        elementType = getTypeForItem(st, getOneFrom(definingIds));
						    }
					        cb = addConstraintForLoc(cb, pli@\loc, elementType);
				        } else {
				            cb = addConstraintForLoc(cb, pli@\loc, makeFailType("No definition for this variable found",pli@\loc));
				        }
	                } else if ((Pattern)`<QualifiedName qn> *` := pli) {
				        if (qn@\loc in st.itemUses<0>) {
				        	definingIds = st.itemUses[qn@\loc];
						    if (size(definingIds) > 1) {
						        elementType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
						    } else if (size(definingIds) == 1) {
						        elementType = getTypeForItem(st, getOneFrom(definingIds));
						    }
					        cb = addConstraintForLoc(cb, pli@\loc, elementType);
				        } else {
				            cb = addConstraintForLoc(cb, pli@\loc, makeFailType("No definition for this variable found",pli@\loc));
				        }
	                } else {
	                    gatherPatternConstraints(pli);
	                    elementType = typeForLoc(cb, pli@\loc);
	
	                    if (spliceablePattern(pli))
	                        elementType = SpliceableElement(elementType);
	                }
	
	                lubs += elementType;
	            }
	
	            cb.constraints = cb.constraints + LubOfList(lubs, tlub, pat@\loc);
	            cb = addConstraintForLoc(cb, pat@\loc, makeListType(tlub));
	            return;
	        }
	
	        
	        case (Pattern) `{<{Pattern ","}* pl>}` : {
	            < cb, tlub > = makeFreshType(cb);
	            list[RType] lubs = [ makeVoidType() ];
	            for (pli <- pl) {
	                RType elementType = makeVoidType();
	                
	                if ((Pattern)`_*` := pli) {
				        if (pli@\loc in st.itemUses<0>) {
				        	definingIds = st.itemUses[pli@\loc];
						    if (size(definingIds) > 1) {
						        elementType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
						    } else if (size(definingIds) == 1) {
						        elementType = getTypeForItem(st, getOneFrom(definingIds));
						    }
					        cb = addConstraintForLoc(cb, pli@\loc, elementType);
				        } else {
				            cb = addConstraintForLoc(cb, pli@\loc, makeFailType("No definition for this variable found",pli@\loc));
				        }
	                } else if ((Pattern)`<QualifiedName qn> *` := pli) {
				        if (qn@\loc in st.itemUses<0>) {
				        	definingIds = st.itemUses[qn@\loc];
						    if (size(definingIds) > 1) {
						        elementType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
						    } else if (size(definingIds) == 1) {
						        elementType = getTypeForItem(st, getOneFrom(definingIds));
						    }
					        cb = addConstraintForLoc(cb, pli@\loc, elementType);
				        } else {
				            cb = addConstraintForLoc(cb, pli@\loc, makeFailType("No definition for this variable found",pli@\loc));
				        }
	                } else {
	                    gatherPatternConstraints(pli);
	                    elementType = typeForLoc(cb, pli@\loc);
	
	                    if (spliceablePattern(pli))
	                        elementType = SpliceableElement(elementType);
	                }
	
	                lubs += elementType;
	            }
	
	            cb.constraints = cb.constraints + LubOfSet(lubs, tlub, pat@\loc);
	            cb = addConstraintForLoc(cb, pat@\loc, makeSetType(tlub));
	            return;
	        }
	        
	        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
	            // TODO: Should throw unimplemented for now, need to implement
	            // the reified types as part of the "prelude"...
	            list[RType] reifierTypes = [ ];
	            for (pli <- pl) {
	                gatherPatternConstraints(pli);
	                reifierTypes += typeForLoc(cb, pli@\loc);            
	            }
	            if (size(reifierTypes) > 1)
	                cb = addConstraintForLoc(cb, pat@\loc, makeFailType("Reified types have an arity of at most 1",pat@\loc));
	            return; // TODO: Add rest of logic for reified type constructors
	        }
	
	        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
	            < cb, tr > = makeFreshType(cb);
	            gatherPatternConstraints(p1);
	            
	            list[RType] paramTypes = [ ];
	            for (pli <- pl) {
	                gatherPatternConstraints(pli);
	                paramTypes += typeForLoc(cb, pli@\loc);            
	            }
	
	            cb.constraints = cb.constraints + CallOrTree(typeForLoc(cb, p1@\loc), paramTypes, tr, pat@\loc);
	            cb = addConstraintForLoc(cb, pat@\loc, tr);
	            return;
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
	            gatherPatternConstraints(p);
	            
	            // Need to decide -- reachable from where? Don't want to have to check
	            // for this in every other pattern... For now, put in Value, because everything
	            // is reachable from Value
	            cb.constraints = cb.constraints + PatternReachable(makeValueType(), typeForLoc(cb,p@\loc), pat@\loc);
	            
	            cb = addConstraintForLoc(cb, pat@\loc, makeValueType());
	            return;
	        }
	
	        case (Pattern) `<Name n> : <Pattern p>` : {
	            gatherPatternConstraints(p);
		        if (n@\loc in st.itemUses<0>) {
				    RType itemType = makeVoidType();
		        	definingIds = st.itemUses[n@\loc];
				    if (size(definingIds) > 1) {
				        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
				    } else if (size(definingIds) == 1) {
				        itemType = getTypeForItem(st, getOneFrom(definingIds));
				    }
			        cb = addConstraintForLoc(cb, pat@\loc, itemType);
	                cb.constraints = cb.constraints + Comparable(itemType, typeForLoc(cb,p@\loc), U(), pat@\loc);
		        } else {
		            cb = addConstraintForLoc(cb, pat@\loc, makeFailType("No definition for this variable found",n@\loc));
		        }
	            return;
	        }
	        
	        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
	            gatherPatternConstraints(p);
		        if (n@\loc in st.itemUses<0>) {
				    RType itemType = makeVoidType();
		        	definingIds = st.itemUses[n@\loc];
				    if (size(definingIds) > 1) {
				        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
				    } else if (size(definingIds) == 1) {
				        itemType = getTypeForItem(st, getOneFrom(definingIds));
				    }
			        cb = addConstraintForLoc(cb, pat@\loc, itemType);
	                cb.constraints = cb.constraints + Comparable(itemType, typeForLoc(cb,p@\loc), U(), pat@\loc);
		        } else {
		            cb = addConstraintForLoc(cb, pat@\loc, makeFailType("No definition for this variable found",n@\loc));
		        }
	            return;
	        }
	
	        case (Pattern) `[ <Type t> ] <Pattern p>` : {
	            // TODO: My read on this is that p can be anything, but will only match
	            // the subject if it can match when considered to be of type t.
	            // Look into the interpreter to get the actual interpretation.
	            // Adding a Comparable constraint anyway.
	            gatherPatternConstraints(p);
	            cb.constraints = cb.constraints + Comparable(convertType(t), typeForLoc(cb,p@\loc), U(), pat@\loc);
	            cb = addConstraintForLoc(cb, pat@\loc, convertType(t));
	            return;
	        }           
	        
	        case (Pattern) `! <Pattern p>` : {
	            gatherPatternConstraints(p);
	            cb = addConstraintForLoc(cb, pat@\loc, typeForLoc(cb,p@\loc));
	            return;
	        }
	    }

	    if (prod(label("Map",_),_,_) := pat[0]) {
	        list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
	        list[RType] domains = [ makeVoidType() ];
	        list[RType] ranges = [ makeVoidType() ];
	        for ( < md, mr > <- mapContents ) {
	            gatherPatternConstraints(md);
	            gatherPatternConstraints(mr);
	            domains += typeForLoc(cb,md@\loc); ranges += typeForLoc(cb,mr@\loc);
	        }
	        < cb, domainLub > = makeFreshType(cb);
	        < cb, rangeLub > = makeFreshType(cb);
	        cb.constraints = cb.constraints + LubOf(domains, domainLub, pat@\loc);
	        cb.constraints = cb.constraints + LubOf(ranges, rangeLub, pat@\loc);
	        RType mapType = makeMapType(domainLub, rangeLub);
	        cb = addConstraintForLoc(cb, pat@\loc, mapType);
	        return;
	    }
	    
	    if (prod(label("Tuple",_),_,_) := pat[0]) {
	        list[Pattern] tupleContents = getTuplePatternContents(pat);
	        list[RType] tupleTypes = [ ];
	        for (tc <- tupleContents) {
	            gatherPatternConstraints(tc);
	            tupleTypes += typeForLoc(cb, tc@\loc);
	        }
	        RType tupleType = makeTupleType(tupleTypes);
	        cb = addConstraintForLoc(cb, pat@\loc, tupleType);
	        return;
	    }

	    throw "Missing case for <pat>";
	}
	
	gatherPatternConstraints(patPC);
	return cb;
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
