module Library

/*
function ONE[1,arg, carg]{
   carg = init(create(arg));
   return next(arg);
}

function ALL[1,arg,carg]{
   carg = init(create(arg));
   while(hasNext(carg)){
        yield next(carg);
   };
   return false;
}  
*/    

// Initialize a pattern with a given value and exhaust all its possibilities

coroutine DO_ALL[2, pat, iVal, co]{
   co = init(pat, iVal);
   while(next(co)) {
       yield;
   };
}

// ***** Enumerators for all types ***** NOTE: all ENUM declarations get extra parameter 'rval'

coroutine ENUM_LITERAL[2, iLit, rVal]{
   return iLit;
}

coroutine ENUM_LIST[2, iLst, rVal, len, j]{
   len = size_list(iLst);
   guard len > 0;
   j = 0;
   while(j < (len - 1)) {
      yield get_list iLst[j];
      j = j + 1;
   };
   return get_list iLst[j];
}

coroutine ENUM_SET[2, iSet, rVal, iLst, len, j]{
   iLst = set2list(iSet);
   len = size_list(iLst);
   guard len > 0;
   j = 0;
   while(j < (len - 1)) {
      yield get_list iLst[j];
      j = j + 1;
   };
   return get_list iLst[j];
}

coroutine ENUM_MAP[2, iMap, rVal, iKlst, len, j]{
   iKlst = keys(iMap);
   len = size_list(iKlst);
   guard len > 0;
   j = 0;
   while(j < (len - 1)) {
      yield get_list iKlst[j];
      j = j + 1;
   };
   return get_list iKlst[j];
}

coroutine ENUM_NODE[2, iNd, rVal, len, j, array]{
   array = get_name_and_children(iNd);
   len = size_array(array) - 1;
   guard len > 0;
   j = 1;  // skip name
   while(j < len) {
      yield get_array array[j];
      j = j + 1;
   };
   return get_array array[j];
}

coroutine ENUM_TUPLE[2, iTup, rVal, len, j]{
   len = size_tuple(iTup);
   guard len > 0;
   j = 0;
   while(j < (len - 1)) {
      yield get_tuple iTup[j];
      j = j + 1;
   };
   return get_tuple iTup[j];
}

coroutine ENUMERATE_AND_MATCH1[2, enumerator, pat, cpat, iElm]{ 
   // NOTE: slightly changed the original semantics: no backtracking was possible for the last possible value
   enumerator = init(enumerator, ref iElm); // NOTE: reflects the fact that all ENUM declarations get an extra parameter
   while(next(enumerator)) {
     cpat = init(pat, iElm);
     while(next(cpat)){
       yield;
     };
   }; 
}

coroutine ENUMERATE_AND_MATCH[2, pat, iVal]{ 
  // NOTE: initialization of coroutines has been moved into the ENUMERATE_AND_MATCH1 body
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
   enumerator = init(enumerator, ref iElm); // NOTE: reflects the fact that all ENUM declarations get an extra parameter
   while(next(enumerator)) {
     yield iElm;
   }; 
}

coroutine ENUMERATE_AND_ASSIGN[2, rVar, iVal]{
  // NOTE: initialization of coroutines has been moved into the ENUMERATE_AND_ASSIGN1 body
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
   enumerator = init(enumerator, ref iElm); // NOTE: reflects the fact that all ENUM declarations get an extra parameter
   while(next(enumerator)){
     if(subtype(typeOf(iElm), typ)){
     	yield iElm;
     };
   }; 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN[3, typ, rVar, iVal]{
  // NOTE: initialization of coroutines has been moved into the ENUMERATE_CHECK_AND_ASSIGN1 body
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

/*
// ***** Ranges ***** // NOTE: skipped this for now

function RANGE[3, pat, ^first, ^end, i, n]{
   i = mint(^first);
   n = mint(^end);
   if(i < n){
      while(i < n){
        DO_ALL(pat, rint(i));
        i = i + 1;
      };
   } else {
      while(i > n){
        DO_ALL(pat, rint(i)); 
        i = i - 1;
      };
   };
   return false;
}

function RANGE_STEP[4, pat, ^first, ^second, ^end, i, n, step]{
   i = mint(^first);
   n = mint(^end);
   if(i < n){
      step = mint(^second) - i;
      if(step <= 0){
         return false;
      };   
      while(i < n){
        DO_ALL(pat, rint(i));
        i = i + step;
      };
      return false;
   } else {
      step = mint(^second) - i;
      if(step >= 0){
         return false;
      };   
      while(i > n){
        DO_ALL(pat, rint(i));
        i = i + step;
      };
      return false;
   };
}
*/

// ***** Pattern matching *****

coroutine MATCH[2, pat, iSubject, cpat]{
   cpat = init(pat, iSubject);
   while(next(cpat)){
      yield;
   };
}

coroutine MATCH_N[2, pats, subjects, ipats, plen, slen, p, pat]{ // NOTE: fixed semantics
   plen = size_array(pats);
   slen = size_array(subjects);
   guard plen == slen;
   p = 0;
   ipats = make_array(plen);
   set_array ipats[p] = init(get_array pats[p], get_array subjects[p]);
   while((p >= 0) && (p < plen)) {
       pat = get_array ipats[p];
       if(next(pat)) {
           if(p < (plen - 1)) {
               p = p + 1;
               set_array ipats[p] = init(get_array pats[p], get_array subjects[p]);
           } else {
               yield;
           };
       } else {
           p = p - 1;
       };
   };   
}

coroutine MATCH_CALL_OR_TREE[2, pats, iSubject, cpats]{
    guard iSubject is node;
    cpats = init(create(MATCH_N, pats, get_name_and_children(iSubject)));
    while(next(cpats)) {
        yield;
    };
}

coroutine MATCH_REIFIED_TYPE[2, pat, iSubject, nc, konstructor, symbol]{
    guard iSubject is node;
    nc = get_name_and_children(iSubject);
    konstructor = get_array nc[0];
    symbol = get_array nc[1];
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
    guard (equal(typeOf(pat),typeOf(iSubject)) 
    		&& equal(pat, iSubject));
    return;
}

coroutine MATCH_VAR[2, rVar, iSubject]{
   return iSubject;
}

coroutine MATCH_ANONYMOUS_VAR[1, iSubject]{
   return;
}

coroutine MATCH_TYPED_VAR[3, typ, rVar, iSubject]{
   guard subtype(typeOf(iSubject), typ);
   return iSubject;  
}

coroutine MATCH_TYPED_ANONYMOUS_VAR[2, typ, iSubject]{
   guard subtype(typeOf(iSubject), typ);
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

coroutine MATCH_LIST[2, pats,   					// A list of coroutines to match list elements
					    iSubject,					// The subject list
					   
					    patlen,						// Length of pattern list
					    patlen1,					// patlen - 1
					    sublen,						// Length of subject list
					    p,							// Cursor in patterns
					    cursor,						// Cursor in subject
					    matcher,					// Currently active pattern matcher
					    matchers,					// List of currently active pattern matchers
					    nextCursor					// Cursor movement of last successfull match
					 ]{
     guard iSubject is list;
     
     patlen   = size_array(pats);
     patlen1 =  patlen - 1;
     sublen   = size_list(iSubject);
     p        = 0; 
     cursor   = 0;
     matcher  = init(get_array pats[p], iSubject, cursor, ref nextCursor, sublen);
     matchers = make_array(patlen);
     set_array matchers[0] = matcher;
     
     while(true) {
     	// Move forward
     	while(next(matcher)) {
            cursor = nextCursor;
            if((p == patlen1) && (cursor == sublen)) {
                yield; 
            } else {
                if(p < patlen1){
                    p = p + 1;
                    matcher  = init(get_array pats[p], iSubject, cursor, ref nextCursor, sublen - cursor);
                    set_array matchers[p] = matcher;
                };  
            };
         }; 
         // If possible, move backward
         if(p > 0) {
             p        = p - 1;
             matcher  = get_array matchers[p];
         } else {
             exhaust;
         };
     };
}

// All coroutines that may occur in a list pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - start: the start index in the subject list
// - available: the number of remaining, unmatched, elements in the subject list

coroutine MATCH_PAT_IN_LIST[5, pat, iSubject, start, rNext, available, cpat]{ 
   // NOTE: added an extra parameter 'rNext'
    guard available > 0;
    cpat = init(pat, get_list iSubject[start]);
    
    // NOTE: replaced 'return' with 'yield'
    while(next(cpat)) {
       yield start + 1;   
    };
} 

coroutine MATCH_VAR_IN_LIST[5, rVar, iSubject, start, rNext, available]{
   // NOTE: added an extra parameter 'rNext'
   guard available > 0;
   // NOTE: an example of multi argument 'return'! (could not have been replaced with one reference due to the create-init design)
   return(get_list iSubject[start], start + 1);
}

coroutine MATCH_ANONYMOUS_VAR_IN_LIST[4, iSubject, start, rNext, available]{
   // Note: added an extra parameter 'rNext'
   guard available > 0;
   return start + 1;
}

coroutine MATCH_MULTIVAR_IN_LIST[5, rVar, iSubject, start, rNext, available, len]{
	// NOTE: added an extra parameter 'rNext'
    len = 0;
    // Note: an example of multi argument 'yield'! (could not have been replaced with one reference due to the create-init design)
    while(len <= available) {
        yield(sublist(iSubject, start, len), start + len);
        len = len + 1;
     };
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_LIST[4, iSubject, start, rNext, available, len]{
	// NOTE: added an extra parameter 'rNext'
    len = 0;
    while(len <= available){
        yield start + len;
        len = len + 1;
     };
}

coroutine MATCH_TYPED_MULTIVAR_IN_LIST[6, typ, rVar, iSubject, start, rNext, available, len]{
	// NOTE: added an extra parameter 'rNext'
	guard subtype(typeOf(iSubject), typ);
    len = 0;
    // Note: an example of multi argument 'yield'! (could not have been replaced with one reference due to the create-init design)
    while(len <= available){
        yield(sublist(iSubject, start, len), start + len);
        len = len + 1;
    };
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_LIST[5, typ, iSubject, start, rNext, available, len]{
	// NOTE: added an extra parameter 'rNext'
	guard subtype(typeOf(iSubject), typ);
    len = 0;
    while(len <= available){
        yield start + len;
        len = len + 1;
    };
}

// ***** SET matching *****

coroutine MATCH_SET[2, pair,	   					// A pair of literals, and patterns (other patterns first, multivars last) to match set elements
					   iSubject,					// The subject set
					   
					   iLiterals,					// The literals that occur in the set pattern
					   pats,						// the patterns
					   subject1,					// subject minus literals as mset
					   patlen,						// Length of pattern list
					   patlen1,						// patlen - 1
					   p,							// Cursor in patterns
					   current,						// Current mset to be matched
					   matcher,						// Currently active pattern matcher
					   matchers,					// List of currently active pattern matchers
					   remaining					// Remaining mset as determined by last successfull match
					]{
	  
      iLiterals = get_array pair[0];
      pats      = get_array pair[1];      
      guard ((iSubject is set) && subset(iLiterals, iSubject));
      
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
      matcher   = init(get_array pats[p], subject1, ref remaining);
      matchers  = make_array(patlen);
      set_array matchers[0] = matcher;
     	
      while(true){
          // Move forward
     	  while(next(matcher)){
              current = remaining;
              if((p == patlen1) && (size_mset(current) == 0)) {
                  yield; 
              } else {
                  if(p < patlen1){
                      p = p + 1;
                      matcher  = init(get_array pats[p], current, ref remaining);
                      set_array matchers[p] = matcher;
                  };  
              };
          }; 
          // If possible, move backward
          if(p > 0){
               p       = p - 1;
               matcher = get_array matchers[p];
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
        yield get_list iLst[j];
        j = j + 1;
    };
}

// All coroutines that may occur in a set pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - available: the remaining, unmatched, elements in the subject set

coroutine MATCH_PAT_IN_SET[3, pat, available, rRemaining, gen, cpat, elm]{
	// NOTE: added an extra parameter
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
	// NOTE: added an extra parameter
	guard size_mset(available) > 0;
 
    gen = init(create(ENUM_MSET, available, ref elm));
    while(next(gen)) {
	    yield(elm, mset_destructive_subtract_elm(available, elm));
	    available = mset_destructive_add_elm(available, elm);
    };
}

coroutine MATCH_ANONYMOUS_VAR_IN_SET[2, available, rRenaming, gen, elm]{
	// NOTE: added an extra parameter
	guard size_set(available) > 0;
    
    gen = init(create(ENUM_MSET, available, ref elm));
    while(next(gen)) { 
        yield mset_destructive_subtract_elm(available, elm);
        available = mset_destructive_add_elm(available, elm);
   };
}

coroutine MATCH_MULTIVAR_IN_SET[3, rVar, available, rRemaining, gen, subset]{
	// NOTE: added an extra parameter
    gen = init(create(ENUM_SUBSETS, available, ref subset));
    while(next(gen)) {
	    yield(set(subset), mset_destructive_subtract_mset(available, subset));
	    available = mset_destructive_add_mset(available, subset);
    };
}

coroutine MATCH_ANONYMOUS_MULTIVAR_IN_SET[2, available, rRemaining, gen, subset]{
	// NOTE: added an extra parameter
    gen = init(create(ENUM_SUBSETS, available, ref subset));
    while(next(gen)) {
	    yield mset_destructive_subtract_mset(available, subset);
	    available = mset_destructive_add_mset(available, subset);
    };
}

coroutine MATCH_TYPED_MULTIVAR_IN_SET[4, typ, rVar, available, rRemaining, gen, subset]{
	// NOTE: added an extra parameter
	guard subtype(typeOf(available), typ);
    
    gen = init(create(ENUM_SUBSETS, available, ref subset));
	while(next(gen)) {
	    yield(set(subset), mset_destructive_subtract_mset(available, subset));
	    available = mset_destructive_add_mset(available, subset);
	};
}

coroutine MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_SET[3, typ, available, rRemaining, gen, subset]{
	// NOTE: added an extra parameter
	guard subtype(typeOf(available), typ);
    
    gen = init(create(ENUM_SUBSETS, available));
    while(next(gen)) {
        yield mset_destructive_subtract_mset(available, subset);
	    available = mset_destructive_add_mset(available, subset);
	};
}

// the power set of a set of size n has 2^n-1 elements 
// so we enumerate the numbers 0..2^n-1
// if the nth bit of a number i is 1 then
// the nth element of the set should be in the
// ith subset 
 
coroutine ENUM_SUBSETS[2, set, rSubset, lst, k, j, last, elIndex, sub]{
	// NOTE: added an extra parameter
    lst = mset2list(set); 
    last = 2 pow size_mset(set);
    k = last - 1;
    while(k >= 0) {
        j = k;
        elIndex = 0; 
        sub = make_mset();
        while(j > 0) {
           if(j mod 2 == 1){
              sub = mset_destructive_add_elm(sub, get_list lst[elIndex]);
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