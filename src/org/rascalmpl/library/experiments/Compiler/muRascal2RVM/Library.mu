module Library

// Semantics of the all operator

coroutine ALL[1, coArray, 
                 len, j, coInits, co] {
    len = size_array(coArray);
    j = 0;
    // coiInits is not created!
    put_array(coInits,j,init(get_array(coArray,j)));
    while(j >= 0) {
        co = get_array(coInits,j);
        if(next(co)) {
            if(j == len - 1) {
                yield;
            } else {
                j = j + 1;
                put_array(coInits,j,init(get_array(coArray,j)));
            };
        } else {
            j = j - 1; };
    };
}

function RASCAL_ALL[2, genArray, generators, 
                        len, j, gen, genInits, forward] {
    len = size_array(genArray);
    j = 0;
    genInits = make_array(len);
    forward = true;
    while(true){
        if(get_array(generators, j)){
           if(forward){
              put_array(genInits,j,init(get_array(genArray,j)));
           };
           gen = get_array(genInits,j);
           if(next(gen)) {
              forward = true;
              j = j + 1;
           } else {
             forward = false;
             j = j - 1;
           };
        } else {
          if(forward){
             if(get_array(genArray, j)()){
                forward = true;
                j = j + 1;
             } else {
               return false;
             };
           } else {
             j = j - 1;
           };
        };
        if(j <= 0){
           return true;
        };
        if(j == len){
           forward = false;
           j = j - 2;
           if(j < 0){
              return true;
           };
        };
    };
}

// Initialize a pattern with a given value and exhaust all its possibilities

coroutine DO_ALL[2, pat, iVal, co]{
   co = init(pat, iVal);
   while(next(co)) {
       yield;
   };
}

// ***** Enumerators for all types 

// These are used by
// - ENEMERATE_AND_MATCH
// - ENUMERATE_AND_ASSIGN
// - ENUMERATE_CHECK_AND_ASSIGN
// All ENUM declarations have a parameter 'rVal' that is used to yield their value

coroutine ENUM_LITERAL[2, iLit, rVal]{
   return iLit;
}

coroutine ENUM_LIST[2, iLst, rVal, len, j]{
   len = size_list(iLst);
   guard len > 0;
   j = 0;
   while(j < len) {
      yield get_list(iLst, j);
      j = j + 1;
   };
}

coroutine ENUM_SET[2, iSet, rVal, iLst, len, j]{
   iLst = set2list(iSet);
   len = size_list(iLst);
   guard len > 0;
   j = 0;
   while(j < len) {
      yield get_list(iLst, j);
      j = j + 1;
   };
}

coroutine ENUM_MAP[2, iMap, rVal, iKlst, len, j]{
   iKlst = keys(iMap);
   len = size_list(iKlst);
   guard len > 0;
   j = 0;
   while(j < len) {
      yield get_list(iKlst, j);
      j = j + 1;
   };
}

coroutine ENUM_NODE[2, iNd, rVal, len, j, array]{
   array = get_name_and_children(iNd);
   len = size_array(array);
   guard len > 1;
   j = 1;  // skip name
   while(j < len) {
      yield get_array(array, j);
      j = j + 1;
   };
}

coroutine ENUM_TUPLE[2, iTup, rVal, len, j]{
   len = size_tuple(iTup);
   guard len > 0;
   j = 0;
   while(j < len) {
      yield get_tuple(iTup, j);
      j = j + 1;
   };
}

coroutine ENUMERATE_AND_MATCH1[2, enumerator, pat, cpat, iElm]{ 
   enumerator = init(enumerator, ref iElm);
   while(next(enumerator)) {
     cpat = init(pat, iElm);
     while(next(cpat)){
       yield;
     };
   }; 
}

coroutine ENUMERATE_AND_MATCH[2, pat, iVal]{ 
  // NOTE: apparently, we have an example of stackful coroutines here
  typeswitch(iVal) {
    case list:         ENUMERATE_AND_MATCH1(create(ENUM_LIST,   iVal), pat);
    case lrel:         ENUMERATE_AND_MATCH1(create(ENUM_LIST,   iVal), pat);
    case node:         ENUMERATE_AND_MATCH1(create(ENUM_NODE,   iVal), pat);
    case constructor:  ENUMERATE_AND_MATCH1(create(ENUM_NODE,   iVal), pat);
    case map:          ENUMERATE_AND_MATCH1(create(ENUM_MAP,    iVal), pat);
    case set:          ENUMERATE_AND_MATCH1(create(ENUM_SET,    iVal), pat);
    case rel:          ENUMERATE_AND_MATCH1(create(ENUM_SET,    iVal), pat);
    case tuple:        ENUMERATE_AND_MATCH1(create(ENUM_TUPLE,  iVal), pat);
    default:           ENUMERATE_AND_MATCH1(create(ENUM_LITERAL,iVal), pat);
  };
}

coroutine ENUMERATE_AND_ASSIGN1[2, enumerator, rVar, iElm]{
   enumerator = init(enumerator, ref iElm);
   while(next(enumerator)) {
     yield iElm;
   }; 
}

coroutine ENUMERATE_AND_ASSIGN[2, rVar, iVal]{
  // NOTE: apparently, we have an example of stackful coroutines here
  typeswitch(iVal) {
    case list:         ENUMERATE_AND_ASSIGN1(create(ENUM_LIST,   iVal), rVar);
    case lrel:         ENUMERATE_AND_ASSIGN1(create(ENUM_LIST,   iVal), rVar);
    case node:         ENUMERATE_AND_ASSIGN1(create(ENUM_NODE,   iVal), rVar);
    case constructor:  ENUMERATE_AND_ASSIGN1(create(ENUM_NODE,   iVal), rVar);
    case map:          ENUMERATE_AND_ASSIGN1(create(ENUM_MAP,    iVal), rVar);
    case set:          ENUMERATE_AND_ASSIGN1(create(ENUM_SET,    iVal), rVar);
    case rel:          ENUMERATE_AND_ASSIGN1(create(ENUM_SET,    iVal), rVar);
    case tuple:        ENUMERATE_AND_ASSIGN1(create(ENUM_TUPLE,  iVal), rVar);
    default:           ENUMERATE_AND_ASSIGN1(create(ENUM_LITERAL,iVal), rVar);
  };
}

coroutine ENUMERATE_CHECK_AND_ASSIGN1[3, enumerator, typ, rVar, iElm]{
   enumerator = init(enumerator, ref iElm); 
   while(next(enumerator)){
     if(subtype(typeOf(iElm), typ)){
     	yield iElm;
     };
   }; 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN[3, typ, rVar, iVal]{
  // NOTE: apparently, we have an example of stackful coroutines here
  typeswitch(iVal){
    case list:         ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_LIST,   iVal), typ, rVar);
    case lrel:         ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_LIST,   iVal), typ, rVar);
    case node:         ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_NODE,   iVal), typ, rVar);
    case constructor:  ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_NODE,   iVal), typ, rVar);
    case map:          ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_MAP,    iVal), typ, rVar);
    case set:          ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_SET,    iVal), typ, rVar);
    case rel:          ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_SET,    iVal), typ, rVar);
    case tuple:        ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_TUPLE,  iVal), typ, rVar);
    default:           ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_LITERAL,iVal), typ, rVar);
  };
}

// ***** Ranges ***** // NOTE: skipped this for now

coroutine RANGE_INT[3, pat, iFirst, iEnd, j, n]{
   j = mint(iFirst);
   n = mint(iEnd);
   if(j < n) {
      while(j < n) {
        DO_ALL(pat, rint(j));
        j = j + 1;
      };
   } else {
      while(j > n) {
        DO_ALL(pat, rint(j)); 
        j = j - 1;
      };
   };
}

coroutine RANGE[3, pat, iFirst, iEnd, j, n, rone]{
   j = iFirst;
   n = iEnd;
   if(iFirst is int && iEnd is int){
     rone = rint(1);
   } else {
     rone = prim("num_to_real", rint(1));
   };
   if(prim("less", j, n)) {
      while(prim("less", j, n)) {
        DO_ALL(pat, j);
        j = prim("add", j, rone);
      };
   } else {
      while(prim("greater", j, n)) {
        DO_ALL(pat, j); 
        j = prim("subtract", j, rone);
      };
   };
}

coroutine RANGE_STEP_INT[4, pat, iFirst, iSecond, iEnd, j, n, step]{
   j = mint(iFirst);
   n = mint(iEnd);
   if(j < n) {
      step = mint(iSecond) - j;
      if(step <= 0) {
         exhaust;
      };   
      while(j < n) {
        DO_ALL(pat, rint(j));
        j = j + step;
      };
      exhaust;
   } else {
      step = mint(iSecond) - j;
      if(step >= 0) {
         exhaust;
      };   
      while(j > n) {
        DO_ALL(pat, rint(j));
        j = j + step;
      };
      exhaust;
   };
}

coroutine RANGE_STEP[4, pat, iFirst, iSecond, iEnd, j, n, step, mixed]{
   n = iEnd;
   if(iFirst is int && iSecond is int && iEnd is int){
     j = iFirst;
     mixed = false;
   } else {
     j = prim("num_to_real", iFirst);
     mixed = true;
   };
   if(prim("less", j, n)) {
      step = prim("subtract", iSecond, j);
      if(mixed){
        step = prim("num_to_real", step);
      };
      if(prim("lessequal", step, rint(0))) {
         exhaust;
      };   
      while(prim("less", j, n)) {
        DO_ALL(pat, j);
        j = prim("add", j, step);
      };
      exhaust;
   } else {
      step = prim("subtract", iSecond, j);
      if(mixed){
        step = prim("num_to_real", step);
      };
      if(prim("greaterequal", step, rint(0))) {
         exhaust;
      };   
      while(prim("greater", j, n)) {
        DO_ALL(pat, j);
        j = prim("add", j, step);
      };
      exhaust;
   };
}

// ***** Pattern matching *****

coroutine MATCH[2, pat, iSubject, cpat]{
   cpat = init(pat, iSubject);
   while(next(cpat)){
      yield;
   };
}

coroutine MATCH_N[2, pats, subjects, ipats, plen, slen, p, pat]{
   plen = size_array(pats);
   slen = size_array(subjects);
   //println("MATCH_N: pats    ", plen, pats);
   //println("MATCH_N: subjects", slen, subjects);
   guard plen == slen;
   p = 0;
   ipats = make_array(plen);
   put_array(ipats, p, init(get_array(pats, p), get_array(subjects, p)));
   while((p >= 0) && (p < plen)) {
       pat = get_array(ipats, p);
       if(next(pat)) {
           //println("MATCH_N succeeds:", p);
           if(p < (plen - 1)) {
               p = p + 1;
               put_array(ipats, p, init(get_array(pats, p), get_array(subjects, p)));
           } else {
               yield;
           };
       } else {
           //println("MATCH_N fails:", p);
           p = p - 1;
       };
   };   
}

coroutine MATCH_CALL_OR_TREE[2, pats, iSubject, cpats]{
    //("MATCH_CALL_OR_TREE", pats, " AND ", iSubject, iSubject is node);
    guard iSubject is node;
    cpats = init(create(MATCH_N, pats, get_name_and_children(iSubject)));
    while(next(cpats)) {
        yield;
    };
    //("MATCH_CALL_OR_TREE fails", pats, " AND ", iSubject);
}

coroutine MATCH_REIFIED_TYPE[2, pat, iSubject, nc, konstructor, symbol]{
    guard iSubject is node;
    nc = get_name_and_children(iSubject);
    konstructor = get_array(nc, 0);
    symbol = get_array(nc, 1);
    if(equal(konstructor, "type") && equal(symbol, pat)) { // NOTE: the second equal? Should not it be a match?
        return;
    };
}

coroutine MATCH_TUPLE[2, pats, iSubject, cpats]{
    guard iSubject is tuple;
    cpats = init(create(MATCH_N, pats, get_tuple_elements(iSubject)));
    while(next(cpats)) {
        yield;
    };
}

coroutine MATCH_LITERAL[2, pat, iSubject]{
    println("MATCH_LITERAL", pat, " and ", iSubject);
    guard (equal(typeOf(pat),typeOf(iSubject)) 
    		&& equal(pat, iSubject));
    return;
}

coroutine MATCH_VAR[2, rVar, iSubject, iVal]{
   //println("MATCH_VAR", rVar, iSubject);
   if(is_defined(rVar)){
      iVal = deref rVar;
      //println("MATCH_VAR, iVal =", iVal);
      if(subtype(typeOf(iSubject), typeOf(iVal)) && equal(iSubject, iVal)){
         return iSubject;
      };
      exhaust;
   };
   yield iSubject;
   undefine(rVar);
   exhaust;
}

coroutine MATCH_ANONYMOUS_VAR[1, iSubject]{
   return;
}

coroutine MATCH_TYPED_VAR[3, typ, rVar, iSubject, iVal]{
   //println("MATCH_TYPED_VAR", typ, rVar, iSubject);
   guard subtype(typeOf(iSubject), typ);
   //if(is_defined(rVar)){
   //    iVal = deref rVar;
   //   //println("MATCH_TYPED_VAR, iVal =", iVal);
   //    if(subtype(typeOf(iSubject), typ) && equal(iSubject, iVal)){
   //       return iSubject;
   //    };
   //   exhaust;
   //};
   yield iSubject;
   undefine(rVar);
   exhaust; 
}

coroutine MATCH_TYPED_ANONYMOUS_VAR[2, typ, iSubject]{
   println("MATCH_TYPED_ANONYMOUS_VAR", typ, iSubject, typeOf(iSubject));
   guard subtype(typeOf(iSubject), typ);
   println("MATCH_TYPED_ANONYMOUS_VAR return true");
   return;
}

coroutine MATCH_VAR_BECOMES[3, rVar, pat, iSubject, cpat]{
   cpat = init(pat, iSubject);
   while(next(cpat)) {
       yield iSubject;
   };
}

coroutine MATCH_TYPED_VAR_BECOMES[4, typ, rVar, pat, iSubject, cpat]{
   guard subtype(typeOf(iSubject), typ);
   cpat = init(pat, iSubject);
   while(next(cpat)) {
       yield iSubject;
   };
}

coroutine MATCH_AS_TYPE[3, typ, pat, iSubject]{ // NOTE: example of stackful coroutines is here
   guard subtype(typeOf(iSubject), typ);
   DO_ALL(pat, iSubject);
}

coroutine MATCH_ANTI[2, pat, iSubject, cpat]{
	   cpat = init(pat, iSubject);
   	if(next(cpat)) {
	      exhaust;
	   } else {
	     return;
   	};
}

// ***** List matching *****

coroutine MATCH_LIST[2, 
     pats,       // Coroutines to match list elements
	 iSubject,   // The subject list
			   
	 patlen,     // Length of pattern array
	 sublen,     // Length of subject list
	 p,          // Cursor in patterns
     cursor,     // Cursor in subject
	 matchers    // Currently active pattern matchers
	]{
     guard iSubject is list;
     
     patlen   = size_array(pats);
     matchers = make_array(patlen);
     sublen   = size_list(iSubject);
     if(patlen == 0){
        if(sublen == 0){
           return;
        } else {
          exhaust;
        };
     };
     p        = 0; 
     cursor   = 0;
     put_array(matchers, p, 
               init(get_array(pats, p), 
                    iSubject, ref cursor, sublen));
     
     while(true){
           while(next(get_array(matchers, p))) {   // Move forward
                 if((p == patlen - 1) && (cursor == sublen)) {
                    yield; 
                 } else {
                   if(p < patlen - 1){
                      p = p + 1;
                      put_array(matchers, p, 
                                init(get_array(pats, p), iSubject, 
                                     ref cursor, sublen - cursor));
                };  
           };
         }; 
         if(p > 0) {  // If possible, move backward
            p  = p - 1;
         } else {
           exhaust;
         };
    };
}

// All coroutines that may occur in a list pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - iSubject: the subject list
// - rNext: reference variable to return next cursor position
// - available: the number of remaining, unmatched, elements in the subject list

// Any pattern in a list not handled by a special case
coroutine MATCH_PAT_IN_LIST[4, pat, iSubject, rNext, available, start, cpat]{ 
    guard available > 0;
    start = deref rNext;
    cpat = init(pat, get_list(iSubject, start));
    
    while(next(cpat)) {
       yield (start + 1);   
    };
} 

// A literal in a list
coroutine MATCH_LITERAL_IN_LIST[4, pat, iSubject, rNext, available, start, elm]{
	guard available > 0;
	start = deref rNext;
	elm =  get_list(iSubject, start);
    if(equal(typeOf(pat),typeOf(elm)) && equal(pat, elm)){
       //println("MATCH_LITERAL_IN_LIST: true", pat, start, elm);
       return(start + 1);
    };
    //println("MATCH_LITERAL_IN_LIST: false", pat, start, elm);
}

// Tree node in concrete pattern: appl(iProd, argspat), where argspat is a list pattern
coroutine MATCH_APPL_IN_LIST[5, iProd, argspat, iSubject, rNext, available, start, iElem, children, cpats]{
    //println("MATCH_APPL_IN_LIST", iProd, argspat, " AND ", iSubject, iSubject is node);
    start = deref rNext;
    iElem = get_list(iSubject, start);
    guard iElem is node;
    children = get_children(iElem);
    if(equal(get_name(iElem), "appl") && equal(iProd, get_array(children, 0))){
       cpats = init(argspat, get_array(children, 1));
       while(next(cpats)) {
          yield(start + 1);
       };
    };
    //println("MATCH_APPL_IN_LIST fails",  iProd, argspat, " AND ", iSubject);
}

// An arbitrary pattern in a list: used to skip layout in concrete patterns
coroutine MATCH_ARB_IN_LIST[3, iSubject, rNext, available, start]{ 
    guard available > 0;
    start = deref rNext;
    //println("MATCH_ARB_IN_LIST", start, get_list(iSubject, start));
    return(start + 1);
} 

coroutine MATCH_VAR_IN_LIST[4, rVar, iSubject, rNext, available, start, iVal, iElem]{
   start = deref rNext;
   //println("MATCH_VAR_IN_LIST", iSubject, start, available);
   guard available > 0;
   
   iElem = get_list(iSubject, start);
   if(is_defined(rVar)){
      iVal = deref rVar;
      if(subtype(typeOf(iElem), typeOf(iVal)) && equal(iElem, iVal)){
         return(iElem, start + 1);
      };
      exhaust;
   };
   return(iElem, start + 1);
}

// TODO: Add MATCH_TYPED_VAR_IN_LIST

coroutine MATCH_ANONYMOUS_VAR_IN_LIST[3, iSubject, rNext, available]{
   guard available > 0;
   return (deref rNext + 1);
}


coroutine MATCH_MULTIVAR_IN_LIST[5, rVar, iLookahead, iSubject, rNext, available, start, len, iVal]{
    start = deref rNext;
    len = 0;
    available = available - mint(iLookahead);
    if(is_defined(rVar)){
      iVal = deref rVar;
      if(/*subtype(typeOf(iElem), typeOf(iVal)) && */ occurs(iVal, iSubject, start)){
         yield(iVal, start + size_list(iVal));
         undefine(rVar);
      };
      exhaust;
    };
    // Note: an example of multi argument 'yield'! (could not have been replaced with one reference due to the create-init design)
    while(len <= available) {
        yield(sublist(iSubject, start, len), start + len);
        len = len + 1;
    };
}

coroutine MATCH_LAST_MULTIVAR_IN_LIST[5, rVar, iLookahead, iSubject, rNext, available, start, len, iVal]{
    len = available - mint(iLookahead);
    guard(len >= 0);
    start = deref rNext;
    if(is_defined(rVar)){
      iVal = deref rVar;
      if(/*subtype(typeOf(iElem), typeOf(iVal)) &&*/ occurs(iVal, iSubject, start)){
         yield(iVal, start + size_list(iVal));
         undefine(rVar);
      };
      exhaust;
    };
    
    // Note: an example of multi argument 'yield'! (could not have been replaced with one reference due to the create-init design)
    while(len <= available) {
        yield(sublist(iSubject, start, len), start + len);
        len = len + 1;
    };
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_LIST[4, iLookahead, iSubject, rNext, available, start, len]{
    start = deref rNext;
    len = 0;
    available = available - mint(iLookahead);
    while(len <= available){
        yield (start + len);
        len = len + 1;
     };
}

coroutine MATCH_LAST_ANONYMOUS_MULTIVAR_IN_LIST[4, iLookahead, iSubject, rNext, available, start, len]{
    len = available - mint(iLookahead);
    guard(len >= 0);
    start = deref rNext;
    while(len <= available){
        yield (start + len);
        len = len + 1;
    };
}

coroutine MATCH_TYPED_MULTIVAR_IN_LIST[6, typ, rVar, iLookahead, iSubject, rNext, available, start, len, sub]{
	start = deref rNext;
    len = 0;
    available = available - mint(iLookahead);
    while(len <= available){
        sub = sublist(iSubject, start, len);
        if(subtype(typeOf(sub), typ)){
           yield(sub , start + len);
           len = len + 1;
        } else {
           exhaust;
        };
    };
}

coroutine MATCH_LAST_TYPED_MULTIVAR_IN_LIST[6, typ, rVar, iLookahead, iSubject, rNext, available, start, len, elmType]{
    println("MATCH_LAST_TYPED_MULTIVAR_IN_LIST", typ, iSubject, available, typeOf(iSubject));
    start = deref rNext;
    available = available - mint(iLookahead);
    elmType = elementTypeOf(typ);
    len = 0;
    while(len < available){
       if(subtype(typeOf(get_list(iSubject, start + len)), elmType)){
         len = len + 1;
       } else {
         return (sublist(iSubject, start, len), start + len);
       };
    };
    return (sublist(iSubject, start, len), start + len);
//    while(len <= available){
//        sub = sublist(iSubject, start, len);
//        if(subtype(typeOf(sub), typ)){
//           yield(sub, start + len);
//           len = len + 1;
//         } else {
//           exhaust;
//         };
//    };
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_LIST[5, typ, iLookahead, iSubject, rNext, available, start, len, sub]{
    println("MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_LIST", typ, iSubject, available, typeOf(iSubject));
    start = deref rNext;
    len = 0;
    available = available - mint(iLookahead);
    while(len <= available){
        sub = sublist(iSubject, start, len);
        if(subtype(typeOf(sub), typ)){
           yield (start + len);
           len = len + 1;
        } else {
          exhaust;
        };
    };
}

coroutine MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_LIST[5, typ, iLookahead, iSubject, rNext, available, start, len, elmType]{
    println("MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_LIST", typ, iSubject, available, typeOf(iSubject));
    start = deref rNext;
    available = available - mint(iLookahead);
    elmType = elementTypeOf(typ);
    len = 0;
     while(len < available){
       if(subtype(typeOf(get_list(iSubject, start + len)), elmType)){
         len = len + 1;
       } else {
         return (start + len);
       };
    };
    return (start + len);
}

// ***** SET matching *****

coroutine MATCH_SET[2, 
    pair,	   					    // A pair of literals, and patterns (other patterns first, multivars last) to match set elements
					    iSubject,   					// The subject set
					   
					    iLiterals,  					// The literals that occur in the set pattern
					    pats,						       // the patterns
					    subject1,   					// subject minus literals as mset
					    patlen,						     // Length of pattern list
					    patlen1,    						// patlen - 1
					    p,          							// Cursor in patterns
					    current,						    // Current mset to be matched
					    matcher,    						// Currently active pattern matcher
					    matchers,   					// List of currently active pattern matchers
					    remaining					   // Remaining mset as determined by last successfull match
					    ]{
	    guard iSubject is set;
	  
      iLiterals = get_array(pair, 0);
      pats      = get_array(pair, 1);
      
      if(subset(iLiterals, iSubject)) {
          // continue
      } else {
          exhaust;
      };
      
      subject1 = mset_destructive_subtract_set(mset(iSubject), iLiterals);
      patlen   = size_array(pats);
      
      if(patlen == 0) {
          if(size_mset(subject1) == 0) {
     	      return;
     	  } else {
     	      exhaust;
     	  };
      };
      
      patlen1   =  patlen - 1;
      p         = 0;
      matcher   = init(get_array(pats, p), subject1, ref remaining);
      matchers  = make_array(patlen);
      put_array(matchers, 0, matcher);
     	
      while(true){
          // Move forward
     	    while(next(matcher)){
              current = remaining;
              if((p == patlen1) && (size_mset(current) == 0)) {
                  yield; 
              } else {
                  if(p < patlen1){
                      p = p + 1;
                      matcher  = init(get_array(pats, p), current, ref remaining);
                      put_array(matchers, p, matcher);
                  };  
              };
          }; 
          // If possible, move backward
          if(p > 0){
               p       = p - 1;
               matcher = get_array(matchers, p);
           } else {
               exhaust;
           };
      };     
}

coroutine ENUM_MSET[2, set, rElm, iLst, len, j]{
    // NOTE: added an extra parameter
    iLst = mset2list(set);
    len = size_list(iLst);
    j = 0;
    while(j < len) {
        yield get_list(iLst, j);
        j = j + 1;
    };
}

// All coroutines that may occur in a set pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - available: the remaining, unmatched, elements in the subject set
// - rRemaining: reference parameter to return remaining set elements

coroutine MATCH_PAT_IN_SET[3, pat, available, rRemaining, gen, cpat, elm]{

	    guard size_mset(available) > 0;
    
    gen = init(create(ENUM_MSET, available, ref elm));
    while(next(gen)) {
        cpat = init(pat, elm);
        while(next(cpat)) {
            yield mset_destructive_subtract_elm(available, elm);
            available = mset_destructive_add_elm(available, elm);
        };
    };
}

coroutine MATCH_VAR_IN_SET[3, rVar, available, rRemaining, gen, elm]{
	guard size_mset(available) > 0;
 	if(is_defined(rVar)){
      elm = deref rVar;
      if(is_element_mset(elm, available)){
         yield(elm, mset_destructive_subtract_elm(available, elm));
         undefine(rVar);
      };
      exhaust;
    };
    gen = init(create(ENUM_MSET, available, ref elm));
    while(next(gen)) {
	          yield(elm, mset_destructive_subtract_elm(available, elm));
	          available = mset_destructive_add_elm(available, elm);
    };
}

coroutine MATCH_ANONYMOUS_VAR_IN_SET[2, available, rRenaming, gen, elm]{
	guard size_mset(available) > 0;
    
    gen = init(create(ENUM_MSET, available, ref elm));
    while(next(gen)) { 
          yield mset_destructive_subtract_elm(available, elm);
          available = mset_destructive_add_elm(available, elm);
   };
}

coroutine MATCH_MULTIVAR_IN_SET[3, rVar, available, rRemaining, gen, subset]{
    if(is_defined(rVar)){
      subset = deref rVar;
      if(subset_set_mset(subset, available)){
         yield(subset, mset_destructive_subtract_set(available, subset));
         undefine(rVar);
      };
      exhaust;
    };
    gen = init(create(ENUM_SUBSETS, available, ref subset));
    while(next(gen)) {
	          yield(set(subset), mset_destructive_subtract_mset(available, subset));
	          available = mset_destructive_add_mset(available, subset);
    };
}

coroutine MATCH_LAST_MULTIVAR_IN_SET[3, rVar, available, rRemaining, subset]{
    if(is_defined(rVar)){
      subset = deref rVar;
      if(equal_set_mset(subset, available)){
         return(subset,  mset_empty());
      };
      exhaust;
    };

    return(set(available), mset_empty());
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_SET[2, available, rRemaining, gen, subset]{
    gen = init(create(ENUM_SUBSETS, available, ref subset));
    while(next(gen)) {
	      yield mset_destructive_subtract_mset(available, subset);
	      available = mset_destructive_add_mset(available, subset);
    };
}

coroutine MATCH_LAST_ANONYMOUS_MULTIVAR_IN_SET[2, available, rRemaining]{
    return mset_empty();
}

coroutine MATCH_TYPED_MULTIVAR_IN_SET[4, typ, rVar, available, rRemaining, gen, subset, iSubset]{    
    gen = init(create(ENUM_SUBSETS, available, ref subset));
    while(next(gen)) {
          iSubset = set(subset);
          if(subtype(typeOf(iSubset), typ)){
	         yield(iSubset, mset_destructive_subtract_mset(available, subset));
	         available = mset_destructive_add_mset(available, subset);
	      };
   };
}

coroutine MATCH_LAST_TYPED_MULTIVAR_IN_SET[4, typ, rVar, available, rRemaining]{
	guard subtype(typeOf(available), typ);
    return(set(available), mset_empty());
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_SET[3, typ, available, rRemaining, gen, subset]{
    guard subtype(typeOf(available), typ);
    
    gen = init(create(ENUM_SUBSETS, available, ref subset));
    while(next(gen)) {
          yield mset_destructive_subtract_mset(available, subset);
	          available = mset_destructive_add_mset(available, subset);
	    };
}

coroutine MATCH_LAST_TYPED_ANONYMOUS_MULTIVAR_IN_SET[3, typ, available, rRemaining, gen, subset]{
    guard subtype(typeOf(available), typ);
    return mset_empty();
}

// The power set of a set of size n has 2^n-1 elements 
// so we enumerate the numbers 0..2^n-1
// if the nth bit of a number i is 1 then
// the nth element of the set should be in the
// ith subset 
 
coroutine ENUM_SUBSETS[2, set, rSubset, lst, k, j, last, elIndex, sub]{
    lst = mset2list(set); 
    last = 2 pow size_mset(set);
    k = last - 1;
    while(k >= 0) {
        j = k;
        elIndex = 0; 
        sub = make_mset();
        while(j > 0) {
           if(j mod 2 == 1){
              sub = mset_destructive_add_elm(sub, get_list(lst, elIndex));
           };
           elIndex = elIndex + 1;
           j = j / 2;
        };
        if(k == 0) {
           return sub;
        } else {
           yield sub;
        }; 
        k = k - 1;  
    };
}

// ***** Descendent pattern ***

coroutine MATCH_DESCENDANT[2, pat, iSubject, gen, cpat]{
   DO_ALL(create(MATCH_AND_DESCENT, pat), iSubject);
}

// ***** Match and descent for all types *****
// Enforces the same left-most innermost traversal order as the interpreter

coroutine MATCH_AND_DESCENT[2, pat, iVal]{
  typeswitch(iVal){
    case list:        DO_ALL(create(MATCH_AND_DESCENT_LIST, pat), iVal);
    case lrel:        DO_ALL(create(MATCH_AND_DESCENT_LIST, pat), iVal);
    case node:        DO_ALL(create(MATCH_AND_DESCENT_NODE, pat), iVal);
    case constructor: DO_ALL(create(MATCH_AND_DESCENT_NODE, pat), iVal);
    case map:         DO_ALL(create(MATCH_AND_DESCENT_MAP, pat),  iVal);
    case set:         DO_ALL(create(MATCH_AND_DESCENT_SET, pat),  iVal);
    case rel:         DO_ALL(create(MATCH_AND_DESCENT_SET, pat),  iVal);
    case tuple:       DO_ALL(create(MATCH_AND_DESCENT_TUPLE, pat),iVal);
    default:          true;
  };  
  DO_ALL(pat, iVal);
}

coroutine MATCH_AND_DESCENT_LITERAL[2, pat, iSubject, res]{
  if(equal(typeOf(pat), typeOf(iSubject)) && equal(pat, iSubject)){
      return;
  };
  
  MATCH_AND_DESCENT(create(MATCH_LITERAL, pat), iSubject);
}

coroutine MATCH_AND_DESCENT_LIST[2, pat, iLst, last, j]{
   last = size_list(iLst);
   j = 0;
   while(j < last){
      DO_ALL(pat, get_list(iLst, j));
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list(iLst, j));
      j = j + 1;
   };
}

coroutine MATCH_AND_DESCENT_SET[2, pat, iSet, iLst, last, j]{
   iLst = set2list(iSet);
   last = size_list(iLst);
   j = 0;
   while(j < last){
      DO_ALL(pat, get_list(iLst, j));
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list(iLst, j));
      j = j + 1;
   };
}

coroutine MATCH_AND_DESCENT_MAP[2, pat, iMap, iKlst, iVlst, last, j]{
   iKlst = keys(iMap);
   iVlst = values(iMap);
   last = size_list(iKlst);
   j = 0;
   while(j < last){
      DO_ALL(pat, get_list(iKlst, j));
      DO_ALL(pat, get_list(iVlst, j));
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list(iKlst, j));
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list(iVlst, j));
      j = j + 1;
   };
}

coroutine MATCH_AND_DESCENT_NODE[2, pat, iNd, last, j, ar]{
   ar = get_name_and_children(iNd);
   last = size_array(ar);
   j = 1; 
   while(j < last){
      //DO_ALL(pat, get_array(ar, j));
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_array(ar, j));
      j = j + 1;
   };
}

coroutine MATCH_AND_DESCENT_TUPLE[2, pat, iTup, last, j]{
   last = size_tuple(iTup);
   j = 0;
   while(j < last){
      DO_ALL(pat, get_tuple(iTup, j));
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_tuple(iTup, j));
      j = j + 1;
   };
}

// ***** Regular expressions *****

coroutine MATCH_REGEXP[3, iRegexp, varrefs, iSubject, matcher, j, rVar]{
   matcher = muprim("regexp_compile", iRegexp, iSubject);
   while(muprim("regexp_find", matcher)){
     j = 0; 
     while(j < size_array(varrefs)){
        rVar = get_array(varrefs, j);
        deref rVar = muprim("regexp_group", matcher, j + 1);
        j = j + 1;
     };
     yield;
   };
}

// ***** Traverse functions *****

function TRAVERSE_TOP_DOWN[5, phi, iSubject, rHasMatch, rBeenChanged, rebuild, 
							  matched, changed] {
	matched = false; // ignored	
	changed = false;
	iSubject = phi(iSubject, ref matched, ref changed);
	if(rebuild) {
		deref rBeenChanged = changed || deref rBeenChanged;
		changed = false;
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_TOP_DOWN::5, phi, rHasMatch, ref changed, rebuild);
		deref rBeenChanged = changed || deref rBeenChanged;	
		return iSubject;
	};
	return VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_TOP_DOWN::5, phi, rHasMatch, ref changed, rebuild);
}

function TRAVERSE_TOP_DOWN_BREAK[5, phi, iSubject, rHasMatch, rBeenChanged, rebuild, 
									matched, changed] {
	matched = false;
	changed = false;
	iSubject = phi(iSubject, ref matched, ref changed);
	deref rBeenChanged = changed || deref rBeenChanged;	
	if(deref rHasMatch = matched || deref rHasMatch) {	
		return iSubject;
	};
	if(rebuild) {
		changed = false;
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, rHasMatch, ref changed, rebuild);
		deref rBeenChanged = changed || deref rBeenChanged;
		return iSubject;
	};	
	return VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, rHasMatch, ref changed, rebuild);
}

function TRAVERSE_BOTTOM_UP[5, phi, iSubject, rHasMatch, rBeenChanged, rebuild, 
							   matched, changed] {
	matched = false; // ignored
	changed = false;
	if(rebuild) {
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_BOTTOM_UP::5, phi, rHasMatch, ref changed, rebuild);
		deref rBeenChanged = changed || deref rBeenChanged;
		changed = false;
	} else {
		VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_BOTTOM_UP::5, phi, rHasMatch, ref changed, rebuild);
	};
	iSubject = phi(iSubject, ref matched, ref changed);
	deref rBeenChanged = changed || deref rBeenChanged;
	return iSubject;
}

function TRAVERSE_BOTTOM_UP_BREAK[5, phi, iSubject, rHasMatch, rBeenChanged, rebuild, 
									 matched, changed] {
	matched = false;
	changed = false;
	if(rebuild) {
		iSubject = VISIT_CHILDREN(iSubject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, rHasMatch, ref changed, rebuild);
		deref rBeenChanged = changed || deref rBeenChanged;
		changed = false;
	} else {
		VISIT_CHILDREN_VOID(iSubject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, rHasMatch, ref changed, rebuild);
	};		
	if(deref rHasMatch) {	
		return iSubject;
	};
	iSubject = phi(iSubject, ref matched, ref changed);
	deref rHasMatch = matched || deref rHasMatch;
	deref rBeenChanged = changed || deref rBeenChanged;	
	return iSubject;
}

function VISIT_CHILDREN[6, iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild, 
						   children] {
	if((iSubject is list) || (iSubject is set) || (iSubject is tuple) || (iSubject is node)) {
		children = VISIT_NOT_MAP(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild);
	} else {
		if(iSubject is map) {
			children = VISIT_MAP(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild); // special case of map
		};
	};
	if(deref rBeenChanged) {
		return typeswitch(iSubject) {
	    			case list:  prim("list", children);
	    			case lrel:  prim("list", children);
	    			case set:   prim("set",  children);
	    			case rel:   prim("set",  children);
	    			case tuple: prim("tuple",children);
	    			case node:  prim("node", muprim("get_name", iSubject), children);
	    			case constructor: 
	                			prim("constructor", muprim("typeOf_constructor", iSubject), children);	    
	    			case map:   children; // special case of map	    
	    			default:    iSubject;
				};
	};
	return iSubject;
}

function VISIT_NOT_MAP[6, iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild,
						  iarray, enumerator, iChild, j, childHasMatch, childBeenChanged] {
	iarray = make_iarray(size(iSubject));
	enumerator = create(ENUMERATE_AND_ASSIGN, ref iChild, iSubject);
	j = 0;
	while(all(multi(enumerator))) {
		childHasMatch = false;
		childBeenChanged = false;
		iChild = traverse_fun(phi, iChild, ref childHasMatch, ref childBeenChanged, rebuild);
		put_array(iarray, j, iChild);
		j = j + 1;
		deref rHasMatch = childHasMatch || deref rHasMatch;
		deref rBeenChanged = childBeenChanged || deref rBeenChanged;
	};
	return iarray;
}

function VISIT_MAP[6, iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild,
					  writer, enumerator, iKey, iVal, childHasMatch, childBeenChanged] {
	writer = prim("mapwriter_open");
	enumerator = create(ENUMERATE_AND_ASSIGN, ref iKey, iSubject);
	while(all(multi(enumerator))) {
		iVal = prim("map_subscript", iSubject, iKey);
		
		childHasMatch = false;
		childBeenChanged = false;
		iKey = traverse_fun(phi, iKey, ref childHasMatch, ref childBeenChanged, rebuild);
		deref rHasMatch = childHasMatch || deref rHasMatch;
		deref rBeenChanged = childBeenChanged || deref rBeenChanged;
		
		childHasMatch = false;
		childBeenChanged = false;
		iVal = traverse_fun(phi, iVal, ref childHasMatch, ref childBeenChanged, rebuild);
		deref rHasMatch = childHasMatch || deref rHasMatch;
		deref rBeenChanged = childBeenChanged || deref rBeenChanged;
		
		prim("mapwriter_add", writer, iKey, iVal);
	};
	return prim("mapwriter_close", writer);
}

function VISIT_CHILDREN_VOID[6, iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild] {	
	if((iSubject is list) || (iSubject is set) || (iSubject is tuple) || (iSubject is node)) {
		VISIT_NOT_MAP_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild);
		return iSubject;
	};
	if(iSubject is map) {
		VISIT_MAP_VOID(iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild); // special case of map
	};
	return iSubject;
}

function VISIT_NOT_MAP_VOID[6, iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild,
						       enumerator, iChild, childHasMatch, childBeenChanged] {
	enumerator = create(ENUMERATE_AND_ASSIGN, ref iChild, iSubject);
	childBeenChanged = false; // ignored
	while(all(multi(enumerator))) {
		childHasMatch = false;
		traverse_fun(phi, iChild, ref childHasMatch, ref childBeenChanged, rebuild);
		deref rHasMatch = childHasMatch || deref rHasMatch;
	};
	return;
}

function VISIT_MAP_VOID[6, iSubject, traverse_fun, phi, rHasMatch, rBeenChanged, rebuild,
					       enumerator, iKey, iVal, childHasMatch, childBeenChanged] {
	enumerator = create(ENUMERATE_AND_ASSIGN, ref iKey, iSubject);
	childBeenChanged = false; // ignored  
	while(all(multi(enumerator))) {
		childHasMatch = false;
		traverse_fun(phi, iKey, ref childHasMatch, ref childBeenChanged, rebuild);
		deref rHasMatch = childHasMatch || deref rHasMatch;
		
		childHasMatch = false;
		traverse_fun(phi, prim("map_subscript", iSubject, iKey), ref childHasMatch, ref childBeenChanged, rebuild);
		deref rHasMatch = childHasMatch || deref rHasMatch;	
	};
	return;
}