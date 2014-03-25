module Library

// Specific to delimited continuations (only experimental)

declares "cons(adt(\"Gen\",[]),\"NEXT\",[ label(\"cont\",func(\\value(),[])) ])"
declares "cons(adt(\"Gen\",[]),\"EXHAUSTED\",[])"


function NEXT(gen) {
    if(muprim("equal",muprim("get_name",gen),"NEXT")) {
        return true
    }
    return false
}


// Semantics of the all operator

coroutine ALL(tasks) guard { var len = size_array(tasks); len > 0 } {
    var workers = make_array(len),
        j = 0  
    workers[j] = create(tasks[j]())
    while(true) {
        while(next(workers[j])) {
            if(j == len - 1) {
                yield
            } else {
                j = j + 1
                workers[j] = create(tasks[j]())
            }
        }
        if(j > 0) {
            j = j - 1
        } else {
            exhaust
        }
    }
}

coroutine OR(tasks) guard { var len = size_array(tasks); len > 0 } {
    var j = 0 
    while(j < len) {
        tasks[j]()()
        j = j + 1
    }
}

coroutine ONE(task) {
    return next(create(task))
}

function RASCAL_ALL(genArray, generators) { 
    var len = size_array(genArray), 
        genInits = make_array(len),
        j = 0, 
        forward = true, 
        gen   
    while(true) {
        if(generators[j]) {
            if(forward) {
                genInits[j] = create(genArray[j])
            }
            gen = genInits[j]
            if(next(gen)) {
                forward = true
                j = j + 1
            } else {
                forward = false
                j = j - 1
            }
        } else {
            if(forward) {
                if(genArray[j]()) {
                    forward = true
                    j = j + 1
                } else {
                    return false
                }
            } else {
                j = j - 1
            }
        }
        if(j <= 0) {
           return true
        }
        if(j == len) {
           forward = false
           j = j - 2
           if(j < 0) {
              return true
           }
        }
    }
}

// Initialize a pattern with a given value and exhaust all its possibilities

/******************************************************************************************/
/*					Enumerators for all types 											  */
/******************************************************************************************/


// Enumerators are used by
// - ENUMERATE_AND_MATCH
// - ENUMERATE_AND_ASSIGN
// - ENUMERATE_CHECK_AND_ASSIGN
// All ENUM declarations have a parameter 'rVal' that is used to yield their value

coroutine ENUM_LITERAL(iLit, rVal) {
    yield iLit
}

coroutine ENUM_LIST(iLst, rVal) guard { var len = size_list(iLst); len > 0 } {
    var j = 0
    while(j < len) {
        yield get_list(iLst, j)
        j = j + 1
    }
}

coroutine ENUM_SET(iSet, rVal) 
guard { 
    var iLst = set2list(iSet), 
        len = size_list(iLst)
    len > 0 
}
{
    var j = 0
    while(j < len) {
        yield get_list(iLst, j)
        j = j + 1
    }
}

coroutine ENUM_MAP(iMap, rVal) 
guard { 
    var iKlst = keys(iMap), 
        len = size_list(iKlst) 
    len > 0 
}
{
    var j = 0
    while(j < len) {
        yield get_list(iKlst, j)
        j = j + 1
    }
}

coroutine ENUM_NODE(iNd, rVal) 
guard { 
    var array = get_children_and_keyword_params_as_values(iNd),
        len = size_array(array); 
    len > 0 
}
{
	var j = 0
	while(j < len) {
	    yield array[j]
	    j = j + 1
	}
}

coroutine ENUM_TUPLE(iTup, rVal) guard { var len = size_tuple(iTup); len > 0 } {
    var j = 0
    while(j < len) {
        yield get_tuple(iTup, j)
        j = j + 1
    }
}

coroutine ENUMERATE_AND_MATCH1(enumerator, pat) {
    var iElm 
    enumerator = create(enumerator, ref iElm)
    while(next(enumerator)) {
        pat(iElm)
    }
}

coroutine ENUMERATE_AND_MATCH(pat, iVal) { 
    typeswitch(iVal) {
        case list:         ENUMERATE_AND_MATCH1(ENUM_LIST   (iVal), pat)
        case lrel:         ENUMERATE_AND_MATCH1(ENUM_LIST   (iVal), pat)
        case node:         ENUMERATE_AND_MATCH1(ENUM_NODE   (iVal), pat)
        case constructor:  ENUMERATE_AND_MATCH1(ENUM_NODE   (iVal), pat)
        case map:          ENUMERATE_AND_MATCH1(ENUM_MAP    (iVal), pat)
        case set:          ENUMERATE_AND_MATCH1(ENUM_SET    (iVal), pat)
        case rel:          ENUMERATE_AND_MATCH1(ENUM_SET    (iVal), pat)
        case tuple:        ENUMERATE_AND_MATCH1(ENUM_TUPLE  (iVal), pat)
        default:           ENUMERATE_AND_MATCH1(ENUM_LITERAL(iVal), pat)
    }
}

coroutine ENUMERATE_AND_ASSIGN(rVar, iVal) {
    typeswitch(iVal) {
        case list:         ENUM_LIST   (iVal, rVar)
        case lrel:         ENUM_LIST   (iVal, rVar)
        case node:         ENUM_NODE   (iVal, rVar)
        case constructor:  ENUM_NODE   (iVal, rVar)
        case map:          ENUM_MAP    (iVal, rVar)
        case set:          ENUM_SET    (iVal, rVar)
        case rel:          ENUM_SET    (iVal, rVar)
        case tuple:        ENUM_TUPLE  (iVal, rVar)
        default:           ENUM_LITERAL(iVal, rVar)
    }
}

coroutine ENUMERATE_CHECK_AND_ASSIGN1(enumerator, typ, rVar) {
    var iElm
    enumerator = create(enumerator, ref iElm) 
    while(next(enumerator)) {
        if(subtype(typeOf(iElm), typ)) {
     	    yield iElm
        }
    } 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN(typ, rVar, iVal) {
    typeswitch(iVal) {
        case list:         ENUMERATE_CHECK_AND_ASSIGN1(ENUM_LIST   (iVal), typ, rVar)
        case lrel:         ENUMERATE_CHECK_AND_ASSIGN1(ENUM_LIST   (iVal), typ, rVar)
        case node:         ENUMERATE_CHECK_AND_ASSIGN1(ENUM_NODE   (iVal), typ, rVar)
        case constructor:  ENUMERATE_CHECK_AND_ASSIGN1(ENUM_NODE   (iVal), typ, rVar)
        case map:          ENUMERATE_CHECK_AND_ASSIGN1(ENUM_MAP    (iVal), typ, rVar)
        case set:          ENUMERATE_CHECK_AND_ASSIGN1(ENUM_SET    (iVal), typ, rVar)
        case rel:          ENUMERATE_CHECK_AND_ASSIGN1(ENUM_SET    (iVal), typ, rVar)
        case tuple:        ENUMERATE_CHECK_AND_ASSIGN1(ENUM_TUPLE  (iVal), typ, rVar)
        default:           ENUMERATE_CHECK_AND_ASSIGN1(ENUM_LITERAL(iVal), typ, rVar)
    }
}

/******************************************************************************************/
/*					Ranges  												  		      */
/******************************************************************************************/

coroutine RANGE_INT(pat, iFirst, iEnd) {
    var j = mint(iFirst), 
        n = mint(iEnd)
    if(j < n) {
        while(j < n) {
            pat(rint(j))
            j = j + 1
        }
    } else {
        while(j > n) {
            pat(rint(j)) 
            j = j - 1
        }
    }
}

coroutine RANGE(pat, iFirst, iEnd) {
    var j = iFirst, 
        n = iEnd, 
        rone
    if(iFirst is int && iEnd is int) {
        rone = rint(1)
    } else {
        rone = prim("num_to_real", rint(1))
    }
    if(prim("less", j, n)) {
        while(prim("less", j, n)) {
            pat(j)
            j = prim("add", j, rone)
        }
    } else {
        while(prim("greater", j, n)) {
            pat(j)
            j = prim("subtract", j, rone)
       }
    }
}

coroutine RANGE_STEP_INT(pat, iFirst, iSecond, iEnd) {
    var j = mint(iFirst), 
        n = mint(iEnd), 
        step
    if(j < n) {
        step = mint(iSecond) - j
        if(step <= 0) {
            exhaust
        }   
        while(j < n) {
            pat(rint(j))
            j = j + step
        }
        exhaust
    } else {
        step = mint(iSecond) - j
        if(step >= 0) {
            exhaust
        }   
        while(j > n) {
            pat(rint(j))
            j = j + step;
        }
        exhaust
    }
}

coroutine RANGE_STEP(pat, iFirst, iSecond, iEnd) {
    var n = iEnd, 
        j, step, mixed
    if(iFirst is int && iSecond is int && iEnd is int) {
        j = iFirst
        mixed = false
    } else {
        j = prim("num_to_real", iFirst)
        mixed = true
    }
    if(prim("less", j, n)) {
        step = prim("subtract", iSecond, j)
        if(mixed){
            step = prim("num_to_real", step)
        }
        if(prim("lessequal", step, rint(0))) {
            exhaust
        }
        while(prim("less", j, n)) {
            pat(j)
            j = prim("add", j, step)
        }
        exhaust
    } else {
        step = prim("subtract", iSecond, j)
        if(mixed){
            step = prim("num_to_real", step)
        }
        if(prim("greaterequal", step, rint(0))) {
            exhaust
        }
        while(prim("greater", j, n)) {
            pat(j)
            j = prim("add", j, step)
        }
        exhaust
    }
}

/******************************************************************************************/
/*					Pattern matching  												  	  */
/******************************************************************************************/

// Use N patterns to match N subjects

coroutine MATCH_N(pats, subjects) 
guard { 
    var plen = size_array(pats), 
        slen = size_array(subjects) 
    plen == slen 
} 
{
    var ipats = make_array(plen),
        j = 0, 
        pat
    ipats[j] = create(pats[j], subjects[j])
    while((j >= 0) && (j < plen)) {
        pat = ipats[j]
        if(next(pat)) {
            if(j < (plen - 1)) {
                j = j + 1
                ipats[j] = create(pats[j], subjects[j])
            } else {
                yield
            }
        } else {
            j = j - 1
        }
    }   
}

// Match a call pattern with a simple string as function symbol

coroutine MATCH_SIMPLE_CALL_OR_TREE(iName, pats, iSubject) guard iSubject is node {
    var args      
    if(equal(iName, get_name(iSubject))) {
        args = get_children_and_keyword_params_as_map(iSubject)
        MATCH_N(pats, args)
        exhaust
    }
    if(has_label(iSubject, iName)) {
        args = get_children_without_layout_or_separators(iSubject)
        MATCH_N(pats, args)
    }
}

// Match a call pattern with an arbitrary pattern as function symbol

coroutine MATCH_CALL_OR_TREE(pats, iSubject) guard iSubject is node {
    var args = get_name_and_children_and_keyword_params_as_map(iSubject)
    MATCH_N(pats, args)
}

coroutine MATCH_KEYWORD_PARAMS(keywords, pats, iSubject) guard iSubject is map { 
    var len = size_array(keywords), 
        subjects, j, kw
    if(len == 0) {
        return
    }
    subjects = make_array(len)
    j = 0
    while(j < len) {
        kw = keywords[j]
        if(map_contains_key(iSubject, kw)) {
            subjects[j] = get_map(iSubject, kw)
        } else {
            exhaust
        }
        j = j + 1
    }
    MATCH_N(pats, subjects)
}

coroutine MATCH_REIFIED_TYPE(pat, iSubject) guard iSubject is node { 
    var nc = get_name_and_children_and_keyword_params_as_map(iSubject), 
        konstructor = nc[0], 
        symbol = nc[1]
    if(equal(konstructor, "type") && equal(symbol, pat)) {
        yield
    }
}

coroutine MATCH_TUPLE(pats, iSubject) guard iSubject is tuple {
    MATCH_N(pats, get_tuple_elements(iSubject))
}

coroutine MATCH_LITERAL(pat, iSubject) guard equal(pat, iSubject) {
    yield
}

coroutine MATCH_VAR(rVar, iSubject) {
    var iVal 
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(equal(iSubject, iVal)) {
            yield iSubject
        }
        exhaust
    }
    yield iSubject
    undefine(rVar)
}

coroutine MATCH_ANONYMOUS_VAR(iSubject) {
    yield
}

coroutine MATCH_TYPED_VAR(typ, rVar, iSubject) guard subtype(typeOf(iSubject), typ) {
    yield iSubject
    undefine(rVar)
    exhaust
}

coroutine MATCH_TYPED_ANONYMOUS_VAR(typ, iSubject) guard subtype(typeOf(iSubject), typ) {
    yield
}

coroutine MATCH_VAR_BECOMES(rVar, pat, iSubject) {
    var cpat = create(pat, iSubject)
    while(next(cpat)) {
        yield iSubject
    }
}

coroutine MATCH_TYPED_VAR_BECOMES(typ, rVar, pat, iSubject) guard subtype(typeOf(iSubject), typ) {
    var cpat = create(pat, iSubject)
    while(next(cpat)) {
        yield iSubject
    }
}

coroutine MATCH_AS_TYPE(typ, pat, iSubject) guard subtype(typeOf(iSubject), typ) {
    pat(iSubject)
}

coroutine MATCH_ANTI(pat, iSubject) {
    var cpat = create(pat, iSubject)
   	if(next(cpat)) {
	    exhaust
	} else {
	    yield
   	}
}

/******************************************************************************************/
/*					Match a "collection"												  */
/******************************************************************************************/

// MATCH_COLLECTION is a generic controller for matching "collections". Currently list and set matching
// are implemented as instances of MATH_COLLECTION.
// The algorithm has as parameters:
// - a list of patterns "pats"
// - a function "accept" that returns true when we have achieved a complete match of the subject
// - the subject "iSubject" itself
// - a value "progress" that determines the progress of the match.

coroutine MATCH_COLLECTION(pats,     // Coroutines to match collection elements
                           accept,   // Function that accepts a complete match
	                       iSubject, // The subject (a collection like list or set)
	                       progress  // Progress of the match
	                       ) {	
    var patlen = size_array(pats),  
        j = 0,
        matchers
    
    if(patlen == 0) {
        if(accept(iSubject, progress)) {
            yield 
        }
        exhaust
    }
    
    matchers = make_array(patlen); 
    matchers[j] = create(pats[j], iSubject, ref progress)
    while(true){
        while(next(matchers[j])) {   // Move forward
            if((j == patlen - 1) && accept(iSubject, progress)) {
                yield 
            } else {
                if(j < patlen - 1){
                    j = j + 1
                    matchers[j] = create(pats[j], iSubject, ref progress)
                }  
            }
        } 
        if(j > 0) {                  // If possible, move backward
            j  = j - 1
        } else {
            exhaust
        }
    }
}

/******************************************************************************************/
/*					List matching  												  		  */
/******************************************************************************************/


// List matching creates a specific instance of MATCH_COLLECTION

coroutine MATCH_LIST(pats, iSubject) guard iSubject is list {
    MATCH_COLLECTION(pats, Library::ACCEPT_LIST_MATCH::2, iSubject, 0)
}

// A list match is acceptable when the cursor points at the end of the list

function ACCEPT_LIST_MATCH(iSubject, cursor) {
    return size(iSubject) == cursor
}


// All coroutines that may occur in a list pattern have the following parameters:
// - pat: the actual pattern to match one or more list elements
// - iSubject: the subject list
// - rNext: reference variable to return next cursor position
// [- available: the number of remaining, unmatched, elements in the subject list <== has disappeared]

// Any pattern in a list not handled by a special case

coroutine MATCH_PAT_IN_LIST(pat, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } { 
    var cpat = create(pat, get_list(iSubject, start))
    while(next(cpat)) {
       yield start + 1   
    }
} 

// A literal in a list

coroutine MATCH_LITERAL_IN_LIST(pat, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    var elm =  get_list(iSubject, start)  
    if(equal(pat, elm)) {
        yield start + 1
    }
}

coroutine MATCH_VAR_IN_LIST(rVar, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    var iElem = get_list(iSubject, start), 
        iVal
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(equal(iElem, iVal)) {
            yield(iElem, start + 1)
        }
        exhaust
    }
    yield(iElem, start + 1)
    undefine(rVar)
}

coroutine MATCH_TYPED_VAR_IN_LIST(typ, rVar, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    var iElem = get_list(iSubject, start), 
        iVal
    if(subtype(typeOf(iElem), typ)) {
        yield(iElem, start + 1)
    }
}

coroutine MATCH_ANONYMOUS_VAR_IN_LIST(iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    yield start + 1
}

coroutine MATCH_TYPED_ANONYMOUS_VAR_IN_LIST(typ, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    var iElem = get_list(iSubject, start) 
    if(subtype(typeOf(iElem), typ)) {
        yield start + 1
    }
}

coroutine MATCH_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, iSubject, rNext) {
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = mint(iMinLen), 
        maxLen = min(mint(iMaxLen), available - mint(iLookahead)), 
        iVal
    if(is_defined(rVar)) {  
        iVal = deref rVar                        /* TODO: check length */
        if(occurs(iVal, iSubject, start)) {
            yield(iVal, start + size_list(iVal))
        }
        exhaust
    }    
    while(len <= maxLen) {
        yield(sublist(iSubject, start, len), start + len)
        len = len + 1
    }
    undefine(rVar)
}

coroutine MATCH_LAST_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, iSubject, rNext) 
guard { 
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = min(mint(iMaxLen), max(available - mint(iLookahead), 0))
    len >= 0 
}
{    
    var maxLen = len, 
        iVal  
    if(is_defined(rVar)) {
        iVal = deref rVar                        /* TODO: check length */
        if(occurs(iVal, iSubject, start)) {
            yield(iVal, start + size_list(iVal))
        }
        exhaust
    }   
    while(len <= maxLen) {	                     // TODO: loop?
        yield(sublist(iSubject, start, len), start + len)
        len = len + 1
    }
    undefine(rVar)
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_LIST(iMinLen, iMaxLen, iLookahead, iSubject, rNext) {
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = mint(iMinLen)
    available = min(mint(iMaxLen), available - mint(iLookahead))
    while(len <= available) {
        yield start + len
        len = len + 1
    }
}

coroutine MATCH_LAST_ANONYMOUS_MULTIVAR_IN_LIST(iMinLen, iMaxLen, iLookahead, iSubject, rNext) 
guard { 
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = min(mint(iMaxLen), available - mint(iLookahead)) 
    len >= mint(iMinLen) 
} 
{
    while(len <= available) {
        yield start + len
        len = len + 1
    }
}

coroutine MATCH_TYPED_MULTIVAR_IN_LIST(typ, rVar, iMinLen, iMaxLen, iLookahead, iSubject, rNext) {
   var start = deref rNext, 
       available = size_list(iSubject) - start, 
       len = mint(iMinLen), 
       sub 
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(subtype(typeOf(iSubject), typ)) {
        while(len <= available) {
            yield(sublist(iSubject, start, len), start + len)
            len = len + 1
        }
    } else {
        while(len <= available) {
            sub = sublist(iSubject, start, len);
            if(subtype(typeOf(sub), typ)) {
                yield(sub, start + len)
                len = len + 1
            } else {
                exhaust
            }
        }
    }
}

coroutine MATCH_LAST_TYPED_MULTIVAR_IN_LIST(typ, rVar, iMinLen, iMaxLen, iLookahead, iSubject, rNext) {
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = mint(iMinLen), 
        elmType
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(subtype(typeOf(iSubject), typ)) {
        while(len <= available) {
            yield(sublist(iSubject, start, len), start + len)
            len = len + 1
        }
    } else {
        elmType = elementTypeOf(typ)
        while(len < available) {
            if(subtype(typeOf(get_list(iSubject, start + len)), elmType)) {
                len = len + 1
            } else {
                yield(sublist(iSubject, start, len), start + len)
                exhaust
            }
        }
        yield(sublist(iSubject, start, len), start + len)
    }
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_LIST(typ, iMinLen, iMaxLen, iLookahead, iSubject, rNext) {
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = mint(iMinLen)  
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(subtype(typeOf(iSubject), typ)) {
        while(len <= available) {
            yield start + len
            len = len + 1
        }
    } else {
        while(len <= available) {
            if(subtype(typeOf(sublist(iSubject, start, len)), typ)) {
                yield start + len
                len = len + 1
            } else {
                exhaust
            }
        }
    }
}

coroutine MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_LIST(typ, iMinLen, iMaxLen, iLookahead, iSubject, rNext) {
    var start = deref rNext, 
        available = size_list(iSubject) - start, 
        len = mint(iMinLen), 
        elmType
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(subtype(typeOf(iSubject), typ)) {
        while(len <= available) {
            yield start + len
            len = len + 1
        }
    } else {
        elmType = elementTypeOf(typ)
        while(len < available) {
            if(subtype(typeOf(get_list(iSubject, start + len)), elmType)) {
                len = len + 1
            } else {
                yield start + len
                exhaust
            }
        }
        yield start + len
    }
}

// Primitives for matching of concrete list patterns

// Tree node in concrete pattern: appl(iProd, argspat), where argspat is a list pattern
coroutine MATCH_APPL_IN_LIST(iProd, argspat, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    var iElem = get_list(iSubject, start), 
        children = get_children(iElem), 
        cpats
    if(equal(get_name(iElem), "appl") && equal(iProd, children[0])) {
        cpats = create(argspat, children[1])
        while(next(cpats)) {
            yield start + 1
        }
    }
}

// Match appl(prod(lit(S),_,_), _) in a concrete list
coroutine MATCH_LIT_IN_LIST(iProd, iSubject, rNext) guard { var start = deref rNext; start < size_list(iSubject) } {
    var iElem = get_list(iSubject, start), 
        children = get_children(iElem)
    if(equal(get_name(iElem), "appl") && equal(iProd, children[0])) {
	    yield start + 1
    }
}

// Match and skip optional layout in concrete patterns
coroutine MATCH_OPTIONAL_LAYOUT_IN_LIST(iSubject, rNext) { 
    var start = deref rNext, 
        iElem, children, prod, prodchildren
    if(start < size_list(iSubject)) {
        iElem = get_list(iSubject, start)
        if(iElem is node && equal(get_name(iElem), "appl")) {
            children = get_children(iElem)
            prod = children[0]
            prodchildren = get_children(prod)
            if(equal(get_name(prodchildren[0]), "layouts")) {
    	        yield start + 1
    		    exhaust
    	    }
    	}
    }
    yield start
} 

// Match a (or last) multivar in a concrete list

coroutine MATCH_CONCRETE_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, applConstr, listProd, applProd, iSubject, rNext) {
    var start = deref rNext, 
        cavailable = size(iSubject) - start, 
        clen = mint(iMinLen), 
        maxLen = min(mint(iMaxLen), max(cavailable - mint(iLookahead), 0)), 
        iVal, end
    if(is_defined(rVar)) {
        iVal = deref rVar                        /* TODO: check length */
        if(occurs(iVal, iSubject, start)) {
            yield(iVal, start + size_list(iVal))
        }
        exhaust
    }
    while(clen <= maxLen) {
        end = start + clen
        yield(MAKE_CONCRETE_LIST(applConstr, listProd, applProd, sublist(iSubject, start, clen)), end)
        clen = clen + 2
    }
    undefine(rVar)
}

coroutine MATCH_LAST_CONCRETE_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, applConstr, listProd, applProd, iSubject, rNext) 
guard { 
    var start = deref rNext, 
        cavailable = size(iSubject) - start, 
        clen = min(mint(iMaxLen), max(cavailable - mint(iLookahead), 0))
    clen >= mint(iMinLen) 
} 
{   
    var iVal, end    
    if(is_defined(rVar)) {
        iVal = deref rVar                         /* TODO: check length */
        if(occurs(iVal, iSubject, start)) {
             yield(iVal, start + size_list(iVal))
        }
        exhaust
    }
    end = start + clen
    yield(MAKE_CONCRETE_LIST(applConstr, listProd, applProd, sublist(iSubject, start, clen)), end)
    undefine(rVar)
}

// Skip a separator that may be present before or after a matching multivar
function SKIP_OPTIONAL_SEPARATOR(iSubject, start, offset, sep, available) {
    var elm, children, prod, prodchildren   
    if(available >= offset + 2) {
        elm = get_list(iSubject, start + offset)
        if(elm is node) {
            children = get_children(elm)
            prod = children[0]
            prodchildren = get_children(prod)
            if(equal(prodchildren[0], sep)) {
    	        return 2
    	    }
        }
    }
    return 0
}

coroutine MATCH_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, sep, applConstr, listProd, applProd, iSubject, rNext) { 
    var start = deref rNext, 
        cavailable = size(iSubject) - start, 
        len =  mint(iMinLen), 
        skip_leading_separator = SKIP_OPTIONAL_SEPARATOR(iSubject, start, 0, sep, cavailable), 
        skip_trailing_separator = 0, 
        maxLen = max(cavailable - (mint(iLookahead) + skip_leading_separator), 0),
        iVal, sublen, end	  	
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(occurs(iVal, iSubject, start)) {	     // TODO: check length
            yield(iVal, start + size_list(iVal))
        }
        exhaust
    }  
    while(len <= maxLen) {
        if(len == 0) {
            sublen = 0
        } else {
           sublen = (4 * (len - 1)) + 1
        }
        end = start + skip_leading_separator + sublen
        skip_trailing_separator = SKIP_OPTIONAL_SEPARATOR(iSubject, end, 1, sep, maxLen)
        end = end + skip_trailing_separator
        yield(MAKE_CONCRETE_LIST(applConstr, listProd, applProd, sublist(iSubject, start + skip_leading_separator, sublen)), end)
        len = len + 1
    }
    undefine(rVar)
}

coroutine MATCH_LAST_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, sep, applConstr, listProd, applProd, iSubject, rNext) 
guard { 
    var start = deref rNext, 
        cavailable = size(iSubject) - start, 
        skip_leading_separator =  SKIP_OPTIONAL_SEPARATOR(iSubject, start, 0, sep, cavailable), 
        skip_trailing_separator = 0,
        sublen = max(cavailable - (mint(iLookahead) + skip_leading_separator), 0)      
    {
        if(mint(iLookahead) > 0 && sublen >= 2) {
            // skip trailing separator
            sublen = sublen - 2
   	        skip_trailing_separator = 2
        }
        sublen >= mint(iMinLen)
    } 
}
{   
    var iVal, end  
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(occurs(iVal, iSubject, start)) {	     // TODO: check length
            yield(iVal, start + size_list(iVal))
        }
        exhaust
    }
    end = start + skip_leading_separator + sublen + skip_trailing_separator
    yield(MAKE_CONCRETE_LIST(applConstr, listProd, applProd, sublist(iSubject, start + skip_leading_separator, sublen)), end)
    undefine(rVar)
}

function MAKE_CONCRETE_LIST(applConstr, listProd, applProd, elms) {
    var listResult = prim("appl_create", applConstr, listProd, prim("list_create", prim("appl_create", applConstr, applProd, elms)))
    return listResult
}

/******************************************************************************************/
/*					Set matching  												  		  */
/******************************************************************************************/

// Set matching creates a specific instance of MATCH_COLLECTION

coroutine MATCH_SET(iLiterals, pats, iSubject) guard iSubject is set {
    if(subset(iLiterals, iSubject)) {
        iSubject = prim("set_subtract_set", iSubject, iLiterals)
        MATCH_COLLECTION(pats, Library::ACCEPT_SET_MATCH::2, mset(iSubject), mset(iSubject))
    }
}

// A set match is acceptable when the set of remaining elements is empty

function ACCEPT_SET_MATCH(iSubject, remaining) {
    return size_mset(remaining) == 0
}

coroutine ENUM_MSET(set, rElm) {
    var iLst = mset2list(set), 
        len = size_list(iLst), 
        j = 0
    while(j < len) {
        yield get_list(iLst, j)
        j = j + 1
    }
}

// All coroutines that may occur in a set pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - available: the remaining, unmatched, elements in the subject set
// - rRemaining: reference parameter to return remaining set elements

coroutine MATCH_PAT_IN_SET(pat, available, rRemaining) guard size_mset(available) > 0 {
    var gen = create(ENUM_MSET, available, ref elm), 
        cpat, elm
    while(next(gen)) {
        cpat = create(pat, elm)
        while(next(cpat)) {
            yield mset_destructive_subtract_elm(available, elm)
            available = mset_destructive_add_elm(available, elm)
        }
    }
   deref rRemaining = available
}

coroutine MATCH_VAR_IN_SET(rVar, available, rRemaining) guard size_mset(available) > 0 {
    var gen, elm  
    if(is_defined(rVar)) {
        elm = deref rVar
        if(is_element_mset(elm, available)) {
            yield(elm, mset_destructive_subtract_elm(available, elm))
            available = mset_destructive_add_elm(available, elm)
        }
        exhaust
    }
    gen = create(ENUM_MSET, available, ref elm)
    while(next(gen)) {
	    yield(elm, mset_destructive_subtract_elm(available, elm))
	    available = mset_destructive_add_elm(available, elm)
    }
    undefine(rVar)
    deref rRemaining = available
}

coroutine MATCH_TYPED_VAR_IN_SET(typ, rVar, available, rRemaining) guard size_mset(available) > 0 {
    var gen = create(ENUM_MSET, available, ref elm),
        elm
    while(next(gen)) {
        if(subtype(typeOf(elm), typ)) {
            yield(elm, mset_destructive_subtract_elm(available, elm))
	        available = mset_destructive_add_elm(available, elm)
	    }
    }
    deref rRemaining = available
}

coroutine MATCH_ANONYMOUS_VAR_IN_SET(available, rRemaining) guard size_mset(available) > 0 { 
    var gen = create(ENUM_MSET, available, ref elm),
        elm
    while(next(gen)) { 
        yield mset_destructive_subtract_elm(available, elm)
        available = mset_destructive_add_elm(available, elm)
   }
   deref rRemaining = available
}

coroutine MATCH_TYPED_ANONYMOUS_VAR_IN_SET(typ, available, rRemaining) guard size_mset(available) > 0 {
	var gen = create(ENUM_MSET, available, ref elm),
	    elm
    while(next(gen)) { 
        if(subtype(typeOf(elm), typ)){
            yield mset_destructive_subtract_elm(available, elm)
            available = mset_destructive_add_elm(available, elm)
        }
   }
   deref rRemaining = available
}

coroutine MATCH_MULTIVAR_IN_SET(rVar, available, rRemaining) {
    var gen, subset
    if(is_defined(rVar)) {
        subset = deref rVar
        if(subset_set_mset(subset, available)){
            yield(subset, mset_destructive_subtract_set(available, subset))
            available = mset_destructive_add_mset(available, subset)
        }
        exhaust
    }
    gen = create(ENUM_SUBSETS, available, ref subset)
    while(next(gen)) {
	    yield(set(subset), mset_destructive_subtract_mset(available, subset))
	    available = mset_destructive_add_mset(available, subset)
    }
    deref rRemaining = available
    undefine(rVar)
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_SET(available, rRemaining) {
    var gen = create(ENUM_SUBSETS, available, ref subset),
        subset
    while(next(gen)) {
	    yield mset_destructive_subtract_mset(available, subset)
	    available = mset_destructive_add_mset(available, subset)
    }
    deref rRemaining = available
}

coroutine MATCH_LAST_MULTIVAR_IN_SET(rVar, available, rRemaining) {
    var subset
    if(is_defined(rVar)) {
        subset = deref rVar;
        if(equal_set_mset(subset, available)) {
            yield(subset,  mset_empty());
        }
        exhaust
    }
    yield(set(available), mset_empty())
    deref rRemaining = available
    undefine(rVar)
}

coroutine MATCH_LAST_ANONYMOUS_MULTIVAR_IN_SET(available, rRemaining) {
    yield mset_empty()
}

coroutine MATCH_TYPED_MULTIVAR_IN_SET(typ, rVar, available, rRemaining) {    
    var gen = create(ENUM_SUBSETS, available, ref subset), 
        subset, iSubset, tmp
    while(next(gen)) {
        iSubset = set(subset)
        if(subtype(typeOf(iSubset), typ)) {
            tmp = mset_destructive_subtract_mset(available, subset)
	        yield(iSubset, tmp)
	        available = mset_destructive_add_mset(available, subset)
	    }
    }
    deref rRemaining = available
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_SET(typ, available, rRemaining) {
    var gen = create(ENUM_SUBSETS, available, ref subset),
        subset
    while(next(gen)) {
        if(subtype(typeOfMset(subset), typ)) {
            yield mset_destructive_subtract_mset(available, subset)
	        deref rRemaining = mset_destructive_add_mset(available, subset)
	    }
    }
    deref rRemaining = available
}

coroutine MATCH_LAST_TYPED_MULTIVAR_IN_SET(typ, rVar, available, rRemaining) guard subtype(typeOfMset(available), typ) {
    yield(set(available), mset_empty())
}

coroutine MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_SET(typ, available, rRemaining) guard subtype(typeOfMset(available), typ) {
    yield mset_empty()
}

// The power set of a set of size n has 2^n-1 elements 
// so we enumerate the numbers 0..2^n-1
// if the nth bit of a number i is 1 then
// the nth element of the set should be in the
// ith subset 
 
coroutine ENUM_SUBSETS(set, rSubset) {
    var lst = mset2list(set), 
        last = 2 pow size_mset(set), 
        k = last - 1, 
        j, elIndex, sub
    while(k >= 0) {
        j = k
        elIndex = 0 
        sub = make_mset()
        while(j > 0) {
            if(j mod 2 == 1) {
                sub = mset_destructive_add_elm(sub, get_list(lst, elIndex))
            }
            elIndex = elIndex + 1
            j = j / 2
        }
        if(k == 0) {
            yield sub
            exhaust
        } else {
            yield sub
        }
        k = k - 1  
    }
}

/******************************************************************************************/
/*					Descendant matching  												  */
/******************************************************************************************/


// ***** Match and descent for all types *****
// Enforces the same left-most innermost traversal order as the interpreter

coroutine MATCH_AND_DESCENT(pat, iVal) {
    typeswitch(iVal) {
        case list:        MATCH_AND_DESCENT_LIST (pat, iVal)
        case lrel:        MATCH_AND_DESCENT_LIST (pat, iVal)
        case node:        MATCH_AND_DESCENT_NODE (pat, iVal)
        case constructor: MATCH_AND_DESCENT_NODE (pat, iVal)
        case map:         MATCH_AND_DESCENT_MAP  (pat, iVal)
        case set:         MATCH_AND_DESCENT_SET  (pat, iVal)
        case rel:         MATCH_AND_DESCENT_SET  (pat, iVal)
        case tuple:       MATCH_AND_DESCENT_TUPLE(pat,iVal)
        default:          true
    }  
    pat(iVal)
}

coroutine MATCH_AND_DESCENT_LITERAL(pat, iSubject) {
    if(equal(pat, iSubject)) {
        yield
        exhaust
    }
    MATCH_AND_DESCENT(MATCH_LITERAL(pat), iSubject)
}

coroutine MATCH_AND_DESCENT_LIST(pat, iLst) {
    var last = size_list(iLst), 
        j = 0
    while(j < last) {
        MATCH_AND_DESCENT(pat, get_list(iLst, j))
        j = j + 1
    }
}

coroutine MATCH_AND_DESCENT_SET(pat, iSet) {
    var iLst = set2list(iSet), 
        last = size_list(iLst), 
        j = 0
    while(j < last) {
        MATCH_AND_DESCENT(pat, get_list(iLst, j))
        j = j + 1
    }
}

coroutine MATCH_AND_DESCENT_MAP(pat, iMap) {
    var iKlst = keys(iMap), 
        iVlst = values(iMap), 
        last = size_list(iKlst), 
        j = 0
    while(j < last) {
        MATCH_AND_DESCENT(pat, get_list(iKlst, j))
        MATCH_AND_DESCENT(pat, get_list(iVlst, j))
        j = j + 1
    }
}

coroutine MATCH_AND_DESCENT_NODE(pat, iNd) {
    var ar = get_children_and_keyword_params_as_values(iNd), 
        last = size_array(ar), 
        j = 0
    while(j < last) {
        MATCH_AND_DESCENT(pat, ar[j])
        j = j + 1
    }
}

coroutine MATCH_AND_DESCENT_TUPLE(pat, iTup) {
    var last = size_tuple(iTup), 
        j = 0
    while(j < last) {
        MATCH_AND_DESCENT(pat, get_tuple(iTup, j))
        j = j + 1
    }
}

// ***** Regular expressions *****

coroutine MATCH_REGEXP(iRegexp, varrefs, iSubject) {
    var matcher = muprim("regexp_compile", iRegexp, iSubject), 
        j, rVar
    while(muprim("regexp_find", matcher)) {
        j = 0 
        while(j < size_array(varrefs)) {
            rVar = varrefs[j]
            deref rVar = muprim("regexp_group", matcher, j + 1)
            j = j + 1;
        }
        yield
    }
}

// ***** Traverse functions *****

function TRAVERSE_TOP_DOWN(phi, iSubject, rHasMatch, rBeenChanged, rebuild) {
	var matched = false, 
	    changed = false
	iSubject = phi(iSubject, ref matched, ref changed)
	if(rebuild) {
		deref rBeenChanged = changed || deref rBeenChanged
		changed = false
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_TOP_DOWN::5, phi, rHasMatch, ref changed, rebuild)
		deref rBeenChanged = changed || deref rBeenChanged
		return iSubject
	}
	return VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_TOP_DOWN::5, phi, rHasMatch, ref changed, rebuild)
}

function TRAVERSE_TOP_DOWN_BREAK(phi, iSubject, rHasMatch, rBeenChanged, rebuild) {
	var matched = false, 
	    changed = false
	iSubject = phi(iSubject, ref matched, ref changed)
	deref rBeenChanged = changed || deref rBeenChanged	
	if(deref rHasMatch = matched || deref rHasMatch) {	
		return iSubject
	}
	if(rebuild) {
		changed = false
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, rHasMatch, ref changed, rebuild)
		deref rBeenChanged = changed || deref rBeenChanged
		return iSubject
	}
	return VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, rHasMatch, ref changed, rebuild)
}

function TRAVERSE_BOTTOM_UP(phi, iSubject, rHasMatch, rBeenChanged, rebuild) {
	var matched = false, 
	    changed = false
	if(rebuild) {
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_BOTTOM_UP::5, phi, rHasMatch, ref changed, rebuild)
		deref rBeenChanged = changed || deref rBeenChanged
		changed = false
	} else {
		VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_BOTTOM_UP::5, phi, rHasMatch, ref changed, rebuild)
	}
	iSubject = phi(iSubject, ref matched, ref changed)
	deref rBeenChanged = changed || deref rBeenChanged
	return iSubject
}

function TRAVERSE_BOTTOM_UP_BREAK(phi, iSubject, rHasMatch, rBeenChanged, rebuild) { 
	var matched = false, 
	    changed = false
	if(rebuild) {
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, rHasMatch, ref changed, rebuild)
		deref rBeenChanged = changed || deref rBeenChanged
		changed = false
	} else {
		VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, rHasMatch, ref changed, rebuild)
	}
	if(deref rHasMatch) {	
		return iSubject
	}
	iSubject = phi(iSubject, ref matched, ref changed)
	deref rHasMatch = matched || deref rHasMatch
	deref rBeenChanged = changed || deref rBeenChanged	
	return iSubject
}

function VISIT_CHILDREN(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) { 
	var children
	if((iSubject is list) || (iSubject is set) || (iSubject is tuple) || (iSubject is node)) {
		children = VISIT_NOT_MAP(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild)
	} else {
		if(iSubject is map) {
			children = VISIT_MAP(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) // special case of map
		}
	}
	if(deref rBeenChanged) {
		return typeswitch(iSubject) {
	    			case list:  prim("list", children)
	    			case lrel:  prim("list", children)
	    			case set:   prim("set",  children)
	    			case rel:   prim("set",  children)
	    			case tuple: prim("tuple",children)
	    			case node:  prim("node", muprim("get_name", iSubject), children)
	    			case constructor: 
	                			prim("constructor", muprim("typeOf_constructor", iSubject), children)	    
	    			case map:   children // special case of map	    
	    			default:    iSubject
				}
	}
	return iSubject
}

function VISIT_NOT_MAP(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) {
	var iarray = make_iarray(size(iSubject)), 
	    enumerator = create(ENUMERATE_AND_ASSIGN, ref iChild, iSubject), 
	    j = 0,
	    iChild, childHasMatch, childBeenChanged
	while(next(enumerator)) {
		childHasMatch = false
		childBeenChanged = false
		iChild = traverse_fun(phi, iChild, ref childHasMatch, ref childBeenChanged, rebuild)
		iarray[j] = iChild
		j = j + 1
		deref rHasMatch = childHasMatch || deref rHasMatch
		deref rBeenChanged = childBeenChanged || deref rBeenChanged
	}
	return iarray
}

function VISIT_MAP(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) {
	var writer = prim("mapwriter_open"), 
	    enumerator = create(ENUMERATE_AND_ASSIGN, ref iKey, iSubject),
	    iKey, iVal, childHasMatch, childBeenChanged	    
	while(next(enumerator)) {
		iVal = prim("map_subscript", iSubject, iKey)
		
		childHasMatch = false
		childBeenChanged = false
		iKey = traverse_fun(phi, iKey, ref childHasMatch, ref childBeenChanged, rebuild)
		deref rHasMatch = childHasMatch || deref rHasMatch
		deref rBeenChanged = childBeenChanged || deref rBeenChanged
		
		childHasMatch = false
		childBeenChanged = false
		iVal = traverse_fun(phi, iVal, ref childHasMatch, ref childBeenChanged, rebuild)
		deref rHasMatch = childHasMatch || deref rHasMatch
		deref rBeenChanged = childBeenChanged || deref rBeenChanged
		
		prim("mapwriter_add", writer, iKey, iVal)
	}
	return prim("mapwriter_close", writer)
}

function VISIT_CHILDREN_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) {	
	if((iSubject is list) || (iSubject is set) || (iSubject is tuple) || (iSubject is node)) {
		VISIT_NOT_MAP_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild)
		return iSubject
	}
	if(iSubject is map) {
		VISIT_MAP_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) // special case of map
	}
	return iSubject
}

function VISIT_NOT_MAP_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) {
	var enumerator = create(ENUMERATE_AND_ASSIGN, ref iChild, iSubject), 
	    childBeenChanged = false,
	    iChild, childHasMatch	
	while(next(enumerator)) {
		childHasMatch = false
		traverse_fun(phi, iChild, ref childHasMatch, ref childBeenChanged, rebuild)
		deref rHasMatch = childHasMatch || deref rHasMatch
	}
	return
}

function VISIT_MAP_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild) {
	var enumerator = create(ENUMERATE_AND_ASSIGN, ref iKey, iSubject), 
	    childBeenChanged = false,
	    iKey, iVal, childHasMatch
	while(next(enumerator)) {
		childHasMatch = false
		traverse_fun(phi, iKey, ref childHasMatch, ref childBeenChanged, rebuild)
		deref rHasMatch = childHasMatch || deref rHasMatch
		
		childHasMatch = false
		traverse_fun(phi, prim("map_subscript", iSubject, iKey), ref childHasMatch, ref childBeenChanged, rebuild)
		deref rHasMatch = childHasMatch || deref rHasMatch
	}
	return
}