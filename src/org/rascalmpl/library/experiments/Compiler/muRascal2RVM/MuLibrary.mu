module Library

/******************************************************************************************/
/*                                                                                        */
/*                  MuRascal functions and coroutines called from generated code          */
/*                                                                                        */
/******************************************************************************************/

/******************************************************************************************/
/*                    Enumerators for all types                                           */
/******************************************************************************************/

function NEXT(gen) {
    if(muprim("equal",muprim("get_name",gen),"NEXT")) {
        return true
    }
    return false
}

coroutine ONE(task) {
    if(next(create(task))){
    	return;
    }
}

// Enumerators are used by
// - ENUMERATE_AND_MATCH
// - ENUMERATE_AND_ASSIGN
// - ENUMERATE_CHECK_AND_ASSIGN
// All ENUM declarations have a parameter 'rVal' that is used to yield their value

coroutine ENUM_LITERAL(iLit, rVal) {
    yield iLit
}

coroutine ENUM_LIST(iLst, rVal)
{
    var iter = iterator(iLst);
    while(hasNext(iter)){
        yield getNext(iter)
    }
}

coroutine ENUM_SET(iSet, rVal)
{
	var iter = iterator(iSet);
    while(hasNext(iter)){
        yield getNext(iter)
    }
}

coroutine ENUM_MAP(iMap, rVal)
{
  	var iter = iterator(iMap);
    while(hasNext(iter)){
        yield getNext(iter)
    }
}

coroutine ENUM_NODE(iNd, rVal) 
{
   var iter
   
   //println("ENUM_NODE", iNd);
   if(iNd is appl){            	// A appl (concrete) node?
      if(iNd is concretelist){	// regular(opname(), ...)
         iter = iterator(prim("get_concrete_list_elements", iNd))
        
         while(hasNext(iter)) {
               yield getNext(iter)
         }
      } else {                                    
        return iNd;                                // Concrete node, but not a concrete list
      }
   } else {                                        // Not a concrete list
      iter = iterator(get_children_and_keyword_values(iNd))
      
      while(hasNext(iter)){
      	yield getNext(iter)
     }
   }  	                  
}

coroutine ENUM_NODE_NO_KEYWORD_PARAMS(iNd, rVal) 
{
   var iter = iterator(get_children(iNd));
  
   // TODO concrete appl case?
 
   while(hasNext(iter)){
   		yield getNext(iter)
   }                  
}

coroutine ENUM_NODE_APPL_ARGS(iNd, rVal) 
{
   var iter = iterator(prim("get_appl_args", iNd))
  
   // TODO concrete appl case?
   // TODO get_nonlayout_args
 
   while(hasNext(iter)){
   		yield getNext(iter)
   }                  
}

coroutine ENUM_TUPLE(iTup, rVal)
{
    var iter = iterator(iTup);
    while(hasNext(iter)){
        yield getNext(iter)
    }
}

// deprecated
coroutine ENUMERATE_AND_MATCH1(enumerator, pat) {
    var iElm 
    enumerator = create(enumerator, ref iElm)
    while(next(enumerator)) {
        pat(iElm)
    }
}

// deprecated
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

coroutine ENUMERATE_AND_MATCH_LIST(iLst, pat) {
    var iElm, 
        enumerator = create(ENUM_LIST, iLst, ref iElm);
    while(next(enumerator)) {
        pat(iElm)
    }
}

coroutine ENUMERATE_AND_MATCH_NODE(iNode, pat) {
    var iElm,
        enumerator = create(ENUM_NODE, iNode, ref iElm);
    while(next(enumerator)) {
        pat(iElm)
    }
}

coroutine ENUMERATE_AND_MATCH_MAP(iMap, pat) {
    var iElm,
        enumerator = create(ENUM_MAP, iMap, ref iElm);
    while(next(enumerator)) {
        pat(iElm)
    }
}

coroutine ENUMERATE_AND_MATCH_SET(iSet, pat) {
    var iElm, 
        enumerator = create(ENUM_SET, iSet, ref iElm);
    while(next(enumerator)) {
        pat(iElm)
    }
}

coroutine ENUMERATE_AND_MATCH_TUPLE(iTup, pat) {
    var iElm, 
        enumerator = create(ENUM_TUPLE, iTup, ref iElm);
    while(next(enumerator)) {
        pat(iElm)
    }
}

coroutine ENUMERATE_AND_MATCH_LITERAL(iLit, pat) {
    var iElm, 
        enumerator = create(ENUM_LITERAL, iLit, ref iElm);
    while(next(enumerator)) {
        pat(iElm)
    }
}

// deprecated
coroutine ENUMERATE_AND_ASSIGN(rVar, iVal) {
    //println("ENUMERATE_AND_ASSIGN", iVal);
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

// deprecated
coroutine ENUMERATE_CHECK_AND_ASSIGN1(enumerator, typ, rVar) {
    var iElm
    enumerator = create(enumerator, ref iElm) 
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

// deprecated
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

coroutine ENUMERATE_CHECK_AND_ASSIGN_LIST(iLst, typ, rVar) {
    var iElm,
        enumerator = create(ENUM_LIST, iLst, ref iElm);
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN_NODE(iNode, typ, rVar) {
    var iElm,
        enumerator = create(ENUM_NODE, iNode, ref iElm);
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN_MAP(iMap, typ, rVar) {
    var iElm,
        enumerator = create(ENUM_MAP, iMap, ref iElm);
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN_SET(iSet, typ, rVar) {
    var iElm,
        enumerator = create(ENUM_SET, iSet, ref iElm);
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN_TUPLE(iTup, typ, rVar) {
    var iElm,
        enumerator = create(ENUM_TUPLE, iTup, ref iElm); 
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN_LITERAL(iLit, typ, rVar) {
    var iElm,
        enumerator = create(ENUM_LITERAL, iLit, ref iElm); 
    while(next(enumerator)) {
        if(value_is_subtype(iElm, typ)) {
             yield iElm
        }
    } 
}

/******************************************************************************************/
/*                    Ranges                                                              */
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
    var j, 
        n, 
        rone 
   
   if(iFirst is int && iEnd is int){
         j = iFirst
         n = iEnd
         rone = rint(1)
   } else {
      j = prim("num_to_real", iFirst)
      n = prim("num_to_real", iEnd)
      rone = muprim("one_dot_zero", 1) // we cannot have zero args here :-(
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
        j, step
    if(iFirst is int && iEnd is int) {
        j = iFirst
    } else {
        j = prim("num_to_real", iFirst)
    }
    if(prim("less", j, n)) {
        step = prim("subtract", iSecond, j)
        
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
/*                    Pattern matching                                                          */
/******************************************************************************************/

// Use N patterns to match N subjects

coroutine MATCH_N(pats, subjects) 
guard { 
    var plen = size_array(pats), 
        slen = size_array(subjects) 
    plen == slen 
} 
{
    var ipats,
        j = 0, 
        pat
    if(plen == 0){
       yield
       exhaust
    }
    ipats = make_array(plen)
    ipats[j] = create(pats[j], subjects[j])
    //println("MATCH_N, plen, slen", plen, slen);
    while((j >= 0) && (j < plen)) {
        //println("MATCH_N", j, "pats[j]", pats[j], "subjects[j]", subjects[j])
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

// Match an (abstract or concrete) call pattern with a simple string as function symbol, with keyword fields

coroutine MATCH_SIMPLE_CALL_OR_TREE(iName, pats, iSubject)
guard
	iSubject is node
{
    if(equal(iName, get_name(iSubject))) {
        MATCH_N(pats,  get_children_and_keyword_mmap(iSubject))
        exhaust
    }
    if(has_label(iSubject, iName)) {
        MATCH_N(pats, get_children_without_layout_or_separators_with_keyword_map(iSubject))
    }
}

// Match a concrete call pattern with a simple string as function symbol, with keyword fields

coroutine MATCH_CONCRETE_SIMPLE_CALL_OR_TREE(iName, pats, iSubject)
guard
    iSubject is node
{
    if(has_label(iSubject, iName)) {
        MATCH_N(pats, get_children_without_layout_or_separators_with_keyword_map(iSubject))
    }
}

// Match an (abstract or concrete) call pattern with a simple string as function symbol, without keyword fields

coroutine MATCH_SIMPLE_CALL_OR_TREE_NO_KEYWORD_PARAMS(iName, pats, iSubject)
guard
    iSubject is node
{
    if(equal(iName, get_name(iSubject))) {
        MATCH_N(pats, get_children(iSubject))
        exhaust
    }
    if(has_label(iSubject, iName)) {
        MATCH_N(pats, get_children_without_layout_or_separators_without_keyword_map(iSubject))
    }
}

// Match a concrete call pattern with a simple string as function symbol, without keyword fields

coroutine MATCH_CONCRETE_SIMPLE_CALL_OR_TREE_NO_KEYWORD_PARAMS(iName, pats, iSubject)
guard
    iSubject is node
{
    if(has_label(iSubject, iName)) {
        MATCH_N(pats, get_children_without_layout_or_separators_without_keyword_map(iSubject))
    }
}

// Match a call pattern with an arbitrary pattern as function symbol, with keyword fields

coroutine MATCH_CALL_OR_TREE(pats, iSubject)
guard
	iSubject is node
{
    MATCH_N(pats, get_name_and_children_and_keyword_mmap(iSubject))
}

// Match a call pattern with an arbitrary pattern as function symbol, but without keyword params

coroutine MATCH_CALL_OR_TREE_NO_KEYWORD_PARAMS(pats, iSubject)
guard
    iSubject is node
{
    MATCH_N(pats, get_name_and_children(iSubject))
}

// Match a concrete syntax tree of the form appl(prod, args)

coroutine MATCH_CONCRETE_TREE(prod, pat, iSubject)
guard
	iSubject is appl
{
	var args = get_children(iSubject),
	    cpat;
	//println("MATCH_CONCRETE_TREE", args[0], prod);
	if(equal(args[0], prod)){
		 cpat = create(pat, args[1])
    	 while(next(cpat)) {
        	yield
    	}
	}
}

coroutine MATCH_KEYWORD_PARAMS(keywords, pats, kwMap)
guard
	kwMap is mmap
{ 
    var len = size_array(keywords), 
        subjects, j, kw
    //println("MATCH_KEYWORD_PARAMS", len, keywords, pats, kwMap)
    if(len == 0) {
        return
    }
    subjects = make_array(len)
    j = 0
    while(j < len) {
        kw = mstr(keywords[j])
        if(mmap_contains_key(kwMap, kw)) {
            subjects[j] = get_mmap(kwMap, kw)
        } else {
            exhaust
        }
        j = j + 1
    }
    //println("MATCH_KEYWORD_PARAMS, calling MATCH_N", pats, subjects)
    MATCH_N(pats, subjects)
}

coroutine MATCH_REIFIED_TYPE(pat, iSubject)
guard
	iSubject is node
{ 
    var nc = get_name_and_children_and_keyword_mmap(iSubject), // TODO kw args?
        konstructor = nc[0], 
        symbol = nc[1]
    if(equal(konstructor, "type") && equal(symbol, pat)) {
        yield
    }
}

coroutine MATCH_TUPLE(pats, iSubject)
guard
	iSubject is tuple
{
    MATCH_N(pats, get_tuple_elements(iSubject))
}

coroutine MATCH_LITERAL(pat, iSubject)
//guard
//	equal(pat, iSubject)
{
	//println("MATCH_LITERAL", pat, iSubject)
	if(equal(pat, iSubject)){
    	//println("MATCH_LITERAL, true", pat, iSubject)
    	yield
    } else {
    	//println("MATCH_LITERAL, false", pat, iSubject)
    	exhaust
    }
   
}

coroutine MATCH_SUBSTRING(pat, rBegin, rEnd, iSubject)
//guard iSubject is str
{

    var len = size(iSubject);
    //println("MATCH_SUBSTRING:", pat, deref rBegin, rEnd, iSubject);
    if(is_tail(iSubject, pat, mint(deref rBegin))){
        //println("MATCH_SUBSTRING succeeds", pat, deref rBegin, size(pat), iSubject);
        deref rEnd = len
        yield
    }
    //println("MATCH_SUBSTRING fails", pat, deref rBegin, iSubject);
}

coroutine MATCH_VAR(rVar, iSubject) {
    var iVal 
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(match(iSubject, iVal)) {
            yield iSubject
        }
        exhaust
    }
    yield iSubject
    undefine(rVar)
}

coroutine MATCH_NEW_VAR(rVar, iSubject) {
    yield iSubject
}

coroutine MATCH_ANONYMOUS_VAR(iSubject) {
    //println("MATCH_ANONYMOUS_VAR", iSubject)
    yield
}

coroutine MATCH_TYPED_VAR(typ, rVar, iSubject) 
guard 
    value_is_subtype(iSubject, typ)   
{
    //println("MATCH_TYPED_VAR", typ, prim("value_to_string", iSubject))
    yield iSubject
    undefine(rVar)
    exhaust
}

coroutine MATCH_TYPED_ANONYMOUS_VAR(typ, iSubject) 
guard 
	value_is_subtype(iSubject, typ)
{
	//println("MATCH_TYPED_ANONYMOUS_VAR", typ, prim("value_to_string", iSubject))
    yield
}

coroutine MATCH_VAR_BECOMES(rVar, pat, iSubject) {
    var cpat = create(pat, iSubject)
   /* if(is_defined(rVar)){
       while(next(cpat)) {
            if(match(iSubject, deref rVar)){
               yield iSubject
            } else {
              exhaust
            }
       }
       exhaust
    }*/
    while(next(cpat)) {
        yield iSubject
    }
}

coroutine MATCH_TYPED_VAR_BECOMES(typ, rVar, pat, iSubject) 
guard 
	value_is_subtype(iSubject, typ)
{
    var cpat = create(pat, iSubject)
    while(next(cpat)) {
        yield iSubject
    }
}

coroutine MATCH_AS_TYPE(typ, pat, iSubject)
guard
	value_is_subtype(iSubject, typ)
{
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
/*                    Match a "collection"                                                */
/******************************************************************************************/

// MATCH_COLLECTION is a generic controller for matching "collections". Currently list and set matching
// are implemented as instances of MATCH_COLLECTION.
// The algorithm has as parameters:
// - a list of patterns "pats"
// - a function "accept" that returns true when we have achieved a complete match of the subject
// - the subject "subject" itself

coroutine MATCH_COLLECTION(pats,       // Coroutines to match collection elements
                           accept,     // Function that accepts a complete match
                           subject     // The subject (a collection like list or set, together with optional admin)
                           ) {
    var patlen = size_array(pats),     // Length of pattern array
        j = 0,                         // Cursor in patterns
        matchers                       // Currently active pattern matchers
        
    if(patlen == 0) {
        if(accept(subject)) {
            yield 
        }
        exhaust
    }
    
    matchers = make_array(patlen)
    matchers[j] = create(pats[j], ref subject)   
    while(true) {
    	
        while(next(matchers[j])) {                       // Move forward
        	//println("MATCH_COLLECTION", j);
            if((j == patlen - 1) && accept(subject)) {
                yield 
            } else {
                if(j < patlen - 1) {
                //println("MATCH_COLLECTION, move forwards");
                    j = j + 1
                    matchers[j] = create(pats[j], ref subject)
                }  
            }
        } 
        if(j > 0) {                                      // If possible, move backward
        	//println("MATCH_COLLECTION, move backwards");
            j  = j - 1
        } else {
        	//println("MATCH_COLLECTION, exhausted");
            exhaust
        }
    }
}

/******************************************************************************************/
/*					List matching  												  		  */
/******************************************************************************************/

// List matching creates a specific instance of MATCH_COLLECTION

coroutine MATCH_LIST(pats, iList)
guard
	iList is list
{
    
    var subject = MAKE_SUBJECT(iList, 0)
    
    //println("MATCH_LIST", iList);
    MATCH_COLLECTION(pats, Library::ACCEPT_LIST_MATCH::1, subject)
}

// A list match is acceptable when the cursor points at the end of the list

function ACCEPT_LIST_MATCH(subject) {
   //println("ACCEPT_LIST_MATCH", GET_SUBJECT_CURSOR(subject), size_list(GET_SUBJECT_LIST(subject)) == GET_SUBJECT_CURSOR(subject))
   //return size_list(GET_SUBJECT_LIST(subject)) == GET_SUBJECT_CURSOR(subject)
   return muprim("accept_list_match", subject)
}

// Get the list from a subject -- inlined by implode --
function GET_SUBJECT_LIST(subject) { 
    return subject[0] 
}

// Get the list cursor from a subject -- inlined by implode --
function GET_SUBJECT_CURSOR(subject) {
	/*if(subject[1] < size_list(subject[0])){
		println("GET_SUBJECT_CURSOR", subject[1], get_list(subject[0], subject[1]))
	} else {
		println("GET_SUBJECT_CURSOR", subject[1])
	}*/
    return subject[1] 
}

// Make a subject -- inlined by implode --

function MAKE_SUBJECT(iList, cursor) {
   var ar = make_array(2)
   ar[0] = iList
   ar[1] = cursor
   return ar
}

// All coroutines that may occur in a list pattern have the following parameters:
// - pat: the actual pattern to match one or more list elements
// - subject: a tuple consiting of
//   -- iSubject: the subject list
//   -- cursor: current position in subject list

// Any pattern in a list not handled by a special case

coroutine MATCH_PAT_IN_LIST(pat, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject)
    start < size_list(iList) 
} 
{ 
    var cpat = create(pat, get_list(iList, start))
    //println("MATCH_PAT_IN_LIST", pat, start)
    while(next(cpat)) {
        yield MAKE_SUBJECT(iList, start + 1)   
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
} 

// A literal in a list

coroutine MATCH_LITERAL_IN_LIST(pat, rSubject) 
//guard { 
//   var iList = GET_SUBJECT_LIST(deref rSubject), 
//        start = GET_SUBJECT_CURSOR(deref rSubject) 
//    start < size_list(iList) 
//} 
{
	var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject),
        elm; 
 
 	//println("MATCH_LITERAL_IN_LIST",  get_list(iList, start))
    if(start < size_list(iList)){ 
	    elm = get_list(iList, start)
	    //println("MATCH_LITERAL_IN_LIST, true", pat, elm);
	    if(equal(pat, elm)){
	        yield MAKE_SUBJECT(iList, start + 1)
    	} 
    	deref rSubject = MAKE_SUBJECT(iList, start)
    } else {
    	//println("MATCH_LITERAL_IN_LIST, false", pat, elm);
    	exhaust
    }
}

coroutine MATCH_VAR_IN_LIST(rVar, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject) 
    start < size_list(iList) 
}
{
    var iElem = get_list(iList, start), 
        iVal
    //println("MATCH_VAR_IN_LIST", iElem);
    if(is_defined(rVar)) {
        //println("MATCH_VAR_IN_LIST, var is defined:", deref rVar);
        iVal = deref rVar
        if(match(iElem, iVal)) {
            yield(iElem, MAKE_SUBJECT(iList, start + 1))
        }
        exhaust
    }
    //println("MATCH_VAR_IN_LIST, var is undefined");
    yield(iElem, MAKE_SUBJECT(iList, start + 1))
    undefine(rVar)
}

coroutine MATCH_NEW_VAR_IN_LIST(rVar, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject) 
    start < size_list(iList) 
}
{
    var iElem = get_list(iList, start)
    yield(iElem, MAKE_SUBJECT(iList, start + 1))
}

coroutine MATCH_TYPED_VAR_IN_LIST(typ, rVar, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject) 
    start < size_list(iList) 
}
{
    var iElem = get_list(iList, start)
    if(value_is_subtype(iElem, typ)) {
        yield(iElem, MAKE_SUBJECT(iList, start + 1))
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_ANONYMOUS_VAR_IN_LIST(rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject)
    start < size_list(iList) 
}
{
    yield MAKE_SUBJECT(iList, start + 1)
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_TYPED_ANONYMOUS_VAR_IN_LIST(typ, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start = GET_SUBJECT_CURSOR(deref rSubject)
    start < size_list(iList) 
}
{
    var iElem = get_list(iList, start)
    if(value_is_subtype(iElem, typ)) {
        yield MAKE_SUBJECT(iList, start + 1)
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start, 
        len = mint(iMinLen), 
        maxLen = min(mint(iMaxLen), available - mint(iLookahead)),
        iVal       
    if(is_defined(rVar)) {
        iVal = deref rVar                /* TODO: check length */
        if(occurs(iVal, iList, start)) {
            yield(iVal, MAKE_SUBJECT(iList, start + size_list(iVal)))
        }
        deref rSubject = MAKE_SUBJECT(iList, start)
        exhaust
    }  
    while(len <= maxLen) {
        yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
        len = len + 1
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
    undefine(rVar)
}

coroutine MATCH_NEW_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start, 
        len = mint(iMinLen), 
        maxLen = min(mint(iMaxLen), available - mint(iLookahead))
        
    while(len <= maxLen) {
        yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
        len = len + 1
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_LAST_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start, 
        len = min(mint(iMaxLen), max(available - mint(iLookahead), 0))
    len >= 0
}
{
    var maxLen = len, 
        iVal
    if(is_defined(rVar)) {
        
        iVal = deref rVar                /* TODO: check length */
        if(occurs(iVal, iList, start)) {
            yield(iVal, MAKE_SUBJECT(iList, start + size_list(iVal)))
        }
        deref rSubject = MAKE_SUBJECT(iList, start)
        exhaust
    } 
    while(len <= maxLen) {               // TODO: loop?
        yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
        len = len + 1
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
    undefine(rVar)
}

coroutine MATCH_LAST_NEW_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start, 
        len = min(mint(iMaxLen), max(available - mint(iLookahead), 0))
    len >= 0
}
{
    var maxLen = len
  
    while(len <= maxLen) {               // TODO: loop?
        yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
        len = len + 1
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_LIST(iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start, 
        len = mint(iMinLen) 
    available = min(mint(iMaxLen), available - mint(iLookahead))
    while(len <= available) {
        yield MAKE_SUBJECT(iList, start + len)
        len = len + 1
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_LAST_ANONYMOUS_MULTIVAR_IN_LIST(iMinLen, iMaxLen, iLookahead, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start,
        len = min(mint(iMaxLen), available - mint(iLookahead))
    len >= mint(iMinLen)
}
{
    while(len <= available) {
        yield MAKE_SUBJECT(iList, start + len)
        len = len + 1
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_TYPED_MULTIVAR_IN_LIST(typ, rVar, iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), available = size_list(iList) - start,
        len = mint(iMinLen), 
        sub
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(value_is_subtype(iList, typ)) {
        while(len <= available) {
            yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
            len = len + 1
        }
    } else {
        while(len <= available) {
            sub = sublist(iList, start, len)
            if(value_is_subtype(sub, typ)) {
                yield(sub, MAKE_SUBJECT(iList, start + len))
                len = len + 1
            } else {
                deref rSubject = MAKE_SUBJECT(iList, start)
                exhaust
            }
        }
    }
}

coroutine MATCH_LAST_TYPED_MULTIVAR_IN_LIST(typ, rVar, iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start,
        len = mint(iMinLen), 
        elmType
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(value_is_subtype(iList, typ)) {
        while(len <= available) {
            yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
            len = len + 1
        }
    } else {
        elmType = elementTypeOf(typ)
        while(len < available) {
            if(value_is_subtype(get_list(iList, start + len), elmType)) {
                len = len + 1
            } else {
                yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
                deref rSubject = MAKE_SUBJECT(iList, start)
                exhaust
            }
        }
        yield(sublist(iList, start, len), MAKE_SUBJECT(iList, start + len))
    }
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_LIST(typ, iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start,
        len = mint(iMinLen)
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(value_is_subtype(iList, typ)) {
        while(len <= available) {
            yield MAKE_SUBJECT(iList, start + len)
            len = len + 1
        }
    } else {
        while(len <= available) {
            if(value_is_subtype(sublist(iList, start, len), typ)) {
                yield  MAKE_SUBJECT(iList, start + len)
                len = len + 1
            } else {
                deref rSubject = MAKE_SUBJECT(iList, start)
                exhaust
            }
        }
   }
   deref rSubject = MAKE_SUBJECT(iList, start)
}

coroutine MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_LIST(typ, iMinLen, iMaxLen, iLookahead, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject), 
        start =  GET_SUBJECT_CURSOR(deref rSubject), 
        available = size_list(iList) - start,
        len = mint(iMinLen), elmType
    available = min(mint(iMaxLen), available - mint(iLookahead))
    if(value_is_subtype(iList, typ)) {
        while(len <= available) {
            yield MAKE_SUBJECT(iList, start + len)
            len = len + 1
        }
    } else {
        elmType = elementTypeOf(typ)
        while(len < available) {
            if(value_is_subtype(get_list(iList, start + len), elmType)) {
                len = len + 1
            } else {
                yield MAKE_SUBJECT(iList, start + len)
                deref rSubject = MAKE_SUBJECT(iList, start)
                exhaust
            }
        }
        yield MAKE_SUBJECT(iList, start + len)
    }
    deref rSubject = MAKE_SUBJECT(iList, start)
}

// Primitives for matching of concrete list patterns

// Tree node in concrete pattern: appl(iProd, argspat), where argspat is a list pattern
coroutine MATCH_APPL_IN_LIST(iProd, argspat, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject);
        start < size_list(iList) 
} {
    var iElem = get_list(iList, start), 
        children = get_children(iElem), 
        cpats
    
    //println("MATCH_APPL_IN_LIST, iProd", iProd)
    //println("MATCH_APPL_IN_LIST, iElem", iElem)
    // TODO: this appl can be checked faster!
    if(iElem is appl && equal(iProd, children[0])) {
        cpats = create(argspat, children[1])
        while(next(cpats)) {
            yield MAKE_SUBJECT(iList, start+1)
        }
    }
    //println("MATCH_APPL_IN_LIST exhausted", start, iElem)
}

// Match appl(prod(lit(S),_,_), _) in a concrete list
coroutine MATCH_LIT_IN_LIST(iProd, rSubject)
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject); 
        start < size_list(iList) 
} {
    var iElem = get_list(iList, start), 
        children = get_children(iElem)  
      //println("MATCH_LIT_IN_LIST", iProd, iElem)
     // TODO: this appl can be checked faster!    
    if(iElem is appl && equal(iProd, children[0])) {
        yield MAKE_SUBJECT(iList, start + 1)
    }
}

// Match and skip optional layout in concrete patterns
coroutine MATCH_OPTIONAL_LAYOUT_IN_LIST(rSubject) { 
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject), 
        iElem, children, prod, prodchildren;
        
    //println("MATCH_OPTIONAL_LAYOUT_IN_LIST", iList)
    if(start < size_list(iList)) {
        iElem = get_list(iList, start)
        //println("MATCH_OPTIONAL_LAYOUT_IN_LIST", iElem)
         // TODO: this appl can be checked faster!
        if(iElem is node && iElem is appl) {
            children = get_children(iElem)
            prod = children[0]
            prodchildren = get_children(prod)
            // TODO: this dynamic check should be unneccesary
            if(equal(get_name(prodchildren[0]), "layouts")) {
                //println("MATCH_OPTIONAL_LAYOUT_IN_LIST, skipping:", iElem)
                yield MAKE_SUBJECT(iList, start + 1)
                exhaust
            }
            //println("MATCH_OPTIONAL_LAYOUT_IN_LIST, no layout")
        }
    }
    yield MAKE_SUBJECT(iList, start)
} 

// Match a (or last) multivar in a concrete list

coroutine MATCH_CONCRETE_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, holeProd, rSubject) {
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject),
        cavailable = size(iList) - start, 
        clen = mint(iMinLen), 
        maxLen = min(mint(iMaxLen), max(cavailable - mint(iLookahead), 0)), 
        iVal, end
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(size_list(iVal) <= maxLen && occurs(iVal, iList, start)) {
            yield(iVal, MAKE_SUBJECT(iList, start + size_list(iVal)))
        }
        exhaust
    }  
    while(clen <= maxLen) {
        end = start + clen
        yield(MAKE_CONCRETE_LIST(holeProd, sublist(iList, start, clen)), MAKE_SUBJECT(iList,end))
        clen = clen + 2
    }
    undefine(rVar)
}

coroutine MATCH_LAST_CONCRETE_MULTIVAR_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, holeProd, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject), 
        cavailable = size(iList) - start, 
        clen = min(mint(iMaxLen), max(cavailable - mint(iLookahead), 0))
    clen >= mint(iMinLen) 
} 
{
    var iVal, end
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(occurs(iVal, iList, start)) {
             yield(iVal, MAKE_SUBJECT(iList, start + size_list(iVal)))
        }
        exhaust
    }
    end = start + clen
    yield(MAKE_CONCRETE_LIST(holeProd, sublist(iList, start, clen)), MAKE_SUBJECT(iList,end))
    undefine(rVar)
}

// Skip an optional separator that may be present before or after a matching multivar
// iList:	subject list
// start: 	start index of current list match in subject
// offset:	offset relative to start index
// sep:		list separator
// available:
//			total number of list elements available for current list match

function SKIP_OPTIONAL_SEPARATOR(iList, start, offset, sep, available) {
    var elm, children, prod, prodchildren
    
    //println("SKIP_OPTIONAL_SEPARATOR", start, offset, sep, available, size_list(iList))
      
    if(available >= offset + 2){
        elm = get_list(iList, start + offset)
        if(elm is node) {
            children = get_children(elm)
           // println("SKIP_OPTIONAL_SEPARATOR, children", children);
            prod = children[0]
            prodchildren = get_children(prod)
            if(equal(prodchildren[0], sep)) {
                //println("SKIP_OPTIONAL_SEPARATOR, skipping separator:", elm)
                return 2
            }
        }
    }
    return 0
}

// Concrete match of a multivar in concrete list with separators, given:
// rVar		reference variable representing the multivar in the pattern
// iMinLen	minimal length of the list to be matched
// iMaxLen	maximal length of the list to be matched
// iLookAhead
//			number of non-null list elements following this multivar
// sep		separator
// holeProd	production to build a value for the multivar, given a sublist of matched elements
// rSuject	reference variable representing the concrete list subject

coroutine MATCH_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, sep, holeProd, rSubject) { 
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject), 
        cavailable = size(iList) - start, 
        len =  mint(iMinLen), 
        skip_leading_separator = SKIP_OPTIONAL_SEPARATOR(iList, start, 0, sep, cavailable), 
        skip_trailing_separator = 0, 
        maxLen = max(cavailable - (mint(iLookahead) + skip_leading_separator), 0),
        iVal, sublen, end             
    if(is_defined(rVar)) {
        iVal = deref rVar
        if(size_list(iVal) <= maxLen && occurs(iVal, iList, start)) {
            yield(iVal, MAKE_SUBJECT(iList, start + size_list(iVal)))
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
        skip_trailing_separator = SKIP_OPTIONAL_SEPARATOR(iList, end, 1, sep, maxLen)
        end = end + skip_trailing_separator
        yield(MAKE_CONCRETE_LIST(holeProd, sublist(iList, start + skip_leading_separator, sublen)), MAKE_SUBJECT(iList,end))
        len = len + 1
    }
    undefine(rVar)
}

// Concrete match of the last multivar in a concrete list with separators

coroutine MATCH_LAST_CONCRETE_MULTIVAR_WITH_SEPARATORS_IN_LIST(rVar, iMinLen, iMaxLen, iLookahead, sep, holeProd, rSubject) 
guard { 
    var iList = GET_SUBJECT_LIST(deref rSubject),
        start = GET_SUBJECT_CURSOR(deref rSubject), 
        cavailable = size(iList) - start, 
        skip_leading_separator =  SKIP_OPTIONAL_SEPARATOR(iList, start, 0, sep, cavailable), 
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
        if(occurs(iVal, iList, start)) {
            yield(iVal, MAKE_SUBJECT(iList, start + size_list(iVal)))
        }
        exhaust
    }
    end = start + skip_leading_separator + sublen + skip_trailing_separator
    yield(MAKE_CONCRETE_LIST(holeProd, sublist(iList, start + skip_leading_separator, sublen)), MAKE_SUBJECT(iList,end))
    undefine(rVar)
}

// Make a concrete list given
// concListProd:  	production of the concrete list
// elms:			actual elements of the concrete list (including layout and separators)

function MAKE_CONCRETE_LIST(concListProd, elms) {
    var listResult
    
    //println("MAKE_CONCRETE_LIST, concListProd", concListProd)
    //println("MAKE_CONCRETE_LIST, elms", elms)
    
    listResult = prim("appl_create", concListProd, elms)
    
    //println("MAKE_CONCRETE_LIST", listResult)
    return listResult
}

/******************************************************************************************/
/*					Set matching  												  		  */
/******************************************************************************************/

// Set matching creates a specific instance of MATCH_COLLECTION

coroutine MATCH_SET(iLiterals, pats, iSubject)
guard
	iSubject is set
{
    //println("MATCH_SET:", iLiterals, iSubject)
    if(size_set(iLiterals) == 0 || subset(iLiterals, iSubject)) {
        //iSubject = prim("set_subtract_set", iSubject, iLiterals)
        MATCH_COLLECTION(pats, Library::ACCEPT_SET_MATCH::1, mset_set_subtract_set(iSubject, iLiterals))
    }
}

// A set match is acceptable when the set of remaining elements is empty

function ACCEPT_SET_MATCH(subject) {
   return size_mset(subject) == 0
}

/*
coroutine ENUM_MSET(set, rElm) {
    var iLst = mset2list(set), 
        len = size_list(iLst), 
        j = 0
    while(j < len) {
        yield get_list(iLst, j)
        j = j + 1
    }
}
*/

coroutine ENUM_MSET(set, rElm) {
	var iter = iterator(set)
	
	while(hasNext(iter)){
		yield getNext(iter)
	}
}


// All coroutines that may occur in a set pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - available: the remaining, unmatched, elements in the subject set
// - rRemaining: reference parameter to return remaining set elements

coroutine MATCH_PAT_IN_SET(pat, rSubject)
guard {
	var available = deref rSubject;
	size_mset(available) > 0
} {
    var gen = create(ENUM_MSET, available, ref elm), 
        cpat, elm
    while(next(gen)) {
        cpat = create(pat, elm)
        while(next(cpat)) {
            yield mset_subtract_elm(available, elm)
            deref rSubject = available
        }
    }
}

coroutine MATCH_VAR_IN_SET(rVar, rSubject)
guard {
	var available = deref rSubject;
	size_mset(available) > 0
} {
    var elm, gen  
    if(is_defined(rVar)) {
        elm = deref rVar
        if(is_element_mset(elm, available)) {
            yield(elm, mset_subtract_elm(available, elm))
            deref rSubject = available
        }
        exhaust
    }
    gen = create(ENUM_MSET, available, ref elm)
    while(next(gen)) {
        yield(elm, mset_subtract_elm(available, elm))
        deref rSubject = available
    }
    undefine(rVar)
}

coroutine MATCH_NEW_VAR_IN_SET(rVar, rSubject)
guard {
	var available = deref rSubject;
	size_mset(available) > 0
} {
    var elm, gen  
    gen = create(ENUM_MSET, available, ref elm)
    while(next(gen)) {
        yield(elm, mset_subtract_elm(available, elm))
        deref rSubject = available
    }
}

coroutine MATCH_TYPED_VAR_IN_SET(typ, rVar, rSubject)
guard {
	var available = deref rSubject;
	size_mset(available) > 0
} {
    var gen = create(ENUM_MSET, available, ref elm),
        elm
    while(next(gen)) {
        if(value_is_subtype(elm, typ)) {
            yield(elm, mset_subtract_elm(available, elm))
            deref rSubject = available
        }
    }
}

coroutine MATCH_ANONYMOUS_VAR_IN_SET(rSubject)
guard {
	var available = deref rSubject;
	size_mset(available) > 0
} {
    var gen = create(ENUM_MSET, available, ref elm),
        elm
    while(next(gen)) { 
        yield mset_subtract_elm(available, elm)
        deref rSubject = available
   }
}

coroutine MATCH_TYPED_ANONYMOUS_VAR_IN_SET(typ, rSubject)
guard {
	var available = deref rSubject;
	size_mset(available) > 0
} {
    var gen = create(ENUM_MSET, available, ref elm),
        elm
    while(next(gen)) { 
        if(value_is_subtype(elm, typ)) {
            yield mset_subtract_elm(available, elm)
            deref rSubject = available
        }
    }
}

coroutine MATCH_MULTIVAR_IN_SET(rVar, rSubject) {
    var available = deref rSubject, 
        gen, subset
    if(is_defined(rVar)) {
        subset = deref rVar
        if(subset_set_mset(subset, available)) {
            yield(subset, mset_subtract_set(available, subset))
            deref rSubject = available
        }
        exhaust
    }
    gen = create(ENUM_SUBSETS, available, ref subset)
    while(next(gen)) {
        yield(set(subset), mset_subtract_mset(available, subset))
        deref rSubject = available
    }
    undefine(rVar)
}

coroutine MATCH_NEW_MULTIVAR_IN_SET(rVar, rSubject) {
    var available = deref rSubject, 
        gen, subset

    gen = create(ENUM_SUBSETS, available, ref subset)
    while(next(gen)) {
        yield(set(subset), mset_subtract_mset(available, subset))
        deref rSubject = available
    }
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_SET(rSubject) {
    var available = deref rSubject, 
        gen = create(ENUM_SUBSETS, available, ref subset),
        subset
    while(next(gen)) {
        yield mset_subtract_mset(available, subset)
        deref rSubject = available
    }
}

coroutine MATCH_LAST_MULTIVAR_IN_SET(rVar, rSubject) {
    var available = deref rSubject, 
        subset
    if(is_defined(rVar)) {
        subset = deref rVar
        if(equal_set_mset(subset, available)) {
            yield(subset, mset_empty())
            deref rSubject = available
        }
        exhaust
    }
    yield(set(available), mset_empty())
    deref rSubject = available
    undefine(rVar)
}

coroutine MATCH_LAST_NEW_MULTIVAR_IN_SET(rVar, rSubject) {
    var available = deref rSubject, 
        subset
    yield(set(available), mset_empty())
    deref rSubject = available
}


coroutine MATCH_LAST_ANONYMOUS_MULTIVAR_IN_SET(rSubject) {
    var available = deref rSubject
    yield mset_empty()
    deref rSubject = available
}

coroutine MATCH_TYPED_MULTIVAR_IN_SET(typ, rVar, rSubject) { 
    var available = deref rSubject, 
        gen = create(ENUM_SUBSETS, available, ref subset),
        subset
    while(next(gen)) {
        if(subtype(typeOfMset(subset), typ)) {
            yield(set(subset), mset_subtract_mset(available, subset))
            deref rSubject = available
        }
    }
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_SET(typ, rSubject) {
    var available = deref rSubject, 
        gen = create(ENUM_SUBSETS, available, ref subset),
        subset
    while(next(gen)) {
        if(subtype(typeOfMset(subset), typ)) {
            yield mset_subtract_mset(available, subset)
            deref rSubject = available
        }
    }
}

coroutine MATCH_LAST_TYPED_MULTIVAR_IN_SET(typ, rVar, rSubject)
guard {
	var available = deref rSubject;
	subtype(typeOfMset(available), typ)
} {
    yield(set(available), mset_empty())
    deref rSubject = available
}

coroutine MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_SET(typ, rSubject)
guard {
	var available = deref rSubject;
	subtype(typeOfMset(available), typ)
} {
    yield mset_empty()
    deref rSubject = available
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
/*					 descendant matching  												  */
/******************************************************************************************/

// ***** Match and descent for all types *****
// Enforces the same left-most innermost traversal order as the interpreter
// uses precomputed reachable types to avoid searching irrelevant subtrees
// The descendantDescriptor consists of
// - id (a string to enable cashing)
// - HashMap of types and constructors
// - concreteMatch (to distinguish abstract/concrete match)
// There are two versions:
// - DESCENT_AND_MATCH: for abstract trees
// - DESCENT_AND_MATCH_CONCRETE for concrete trees

coroutine DESCENT_AND_MATCH(pat, descendantDescriptor, iVal) {
    var iter = descendant_iterator(iVal, descendantDescriptor);
    while(hasNext(iter)){
        pat(getNext(iter))
    }
}

coroutine DESCENT_AND_MATCH_CONCRETE(pat, descendantDescriptor, iNd) {
    var iter = descendant_iterator(iNd, descendantDescriptor);
    while(hasNext(iter)){
        pat(getNext(iter))
    }
}

// ***** Regular expressions *****

coroutine MATCH_REGEXP(iRegexp, varrefs, iSubject) {
    var matcher = muprim("regexp_compile", iRegexp, iSubject), 
        j, rVar
    //println("MATCH_REGEXP", iRegexp, iSubject)
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

coroutine MATCH_REGEXP_IN_VISIT(iRegexp, varrefs, rBegin, rEnd, iSubject) {
    var matcher = muprim("regexp_compile", iRegexp, iSubject), 
        j, rVar
    muprim("regexp_set_region", matcher, mint(deref rBegin), mint(deref rEnd))
    while(muprim("regexp_find", matcher)) {
        j = 0 
        while(j < size_array(varrefs)) {
            rVar = varrefs[j]
            deref rVar = muprim("regexp_group", matcher, j + 1)
            j = j + 1;
        }
        deref rBegin = muprim("regexp_begin", matcher);
        deref rEnd = muprim("regexp_end", matcher);
        //println("MATCH_REGEXP_IN_VISIT succeeds", substring(iSubject, mint(deref rBegin), mint(deref rEnd)), iSubject);
        yield
        muprim("regexp_set_region", matcher, deref rBegin, deref rEnd)
    }
    //println("MATCH_REGEXP_IN_VISIT fails", iSubject, deref rBegin, deref rEnd);
}