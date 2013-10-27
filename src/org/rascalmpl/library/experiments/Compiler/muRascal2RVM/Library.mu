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

coroutine DO_ALL[2, pat, ^val, co]{
   co = init(pat, ^val);
   while(next(co)) {
       yield;
   };
} 

// ***** Enumerators for all types ***** NOTE: all ENUM declarations get extra parameter 'rval'

coroutine ENUM_LITERAL[2, ^lit, rval]{
   return ^lit;
}

coroutine ENUM_LIST[2, ^lst, rval, len, i]{
   len = size_list(^lst);
   guard len > 0;
   i = 0;
   while(i < (len - 1)) {
      yield get_list ^lst[i];
      i = i + 1;
   };
   return get_list ^lst[i];
}

coroutine ENUM_SET[2, ^set, rval, ^lst, len, i]{
   ^lst = set2list(^set);
   len = size_list(^lst);
   guard len > 0;
   i = 0;
   while(i < (len - 1)) {
      yield get_list ^lst[i];
      i = i + 1;
   };
   return get_list ^lst[i];
}

coroutine ENUM_MAP[2, ^map, rval, ^klst, len, i]{
   ^klst = keys(^map);
   len = size_list(^klst);
   guard len > 0;
   i = 0;
   while(i < (len - 1)) {
      yield get_list ^klst[i];
      i = i + 1;
   };
   return get_list ^klst[i];
}

coroutine ENUM_NODE[2, ^nd, rval, len, i, array]{
   array = get_name_and_children(^nd);
   len = size_array(array) - 1;
   guard len > 0;
   i = 1;  // skip name
   while(i < len) {
      yield get_array array[i];
      i = i + 1;
   };
   return get_array array[i];
}

coroutine ENUM_TUPLE[2, ^tup, rval, len, i]{
   len = size_tuple(^tup);
   guard len > 0;
   i = 0;
   while(i < (len - 1)) {
      yield get_tuple ^tup[i];
      i = i + 1;
   };
   return get_tuple ^tup[i];
}

coroutine ENUMERATE_AND_MATCH1[2, enumerator, pat, cpat, elm]{ 
	/* NOTE: slightly changed the original semantics: no backtracking was possible for the last possible value */
   enumerator = init(enumerator, ref elm); // NOTE: reflects the fact that all ENUM declarations get an extra parameter
   while(next(enumerator)) {
     cpat = init(pat, elm);
     while(next(cpat)){
       yield;
     };
   }; 
}

coroutine ENUMERATE_AND_MATCH[2, pat, ^val]{ 
  /* NOTE: initialization of coroutines has been moved into the ENUMERATE_AND_MATCH1 body */
  /* NOTE: apparently, we have an example of stackful coroutines here */
  typeswitch(^val) {
    case list:         ENUMERATE_AND_MATCH1(create(ENUM_LIST,   ^val), pat);
    case lrel:         ENUMERATE_AND_MATCH1(create(ENUM_LIST,   ^val), pat);
    case node:         ENUMERATE_AND_MATCH1(create(ENUM_NODE,   ^val), pat);
    case constructor:  ENUMERATE_AND_MATCH1(create(ENUM_NODE,   ^val), pat);
    case map:          ENUMERATE_AND_MATCH1(create(ENUM_MAP,    ^val), pat);
    case set:          ENUMERATE_AND_MATCH1(create(ENUM_SET,    ^val), pat);
    case rel:          ENUMERATE_AND_MATCH1(create(ENUM_SET,    ^val), pat);
    case tuple:        ENUMERATE_AND_MATCH1(create(ENUM_TUPLE,  ^val), pat);
    default:           ENUMERATE_AND_MATCH1(create(ENUM_LITERAL,^val), pat);
  };
}

coroutine ENUMERATE_AND_ASSIGN1[2, enumerator, varref, elm]{
   enumerator = init(enumerator, ref elm); // NOTE: reflects the fact that all ENUM declarations get an extra parameter
   while(next(enumerator)) {
     yield elm;
   }; 
}

coroutine ENUMERATE_AND_ASSIGN[2, ^val, varref]{ // NOTE: flipped parameters
  /* NOTE: initialization of coroutines has been moved into the ENUMERATE_AND_ASSIGN1 body */
  /* NOTE: apparently, we have an example of stackful coroutines here */
  typeswitch(^val) {
    case list:         ENUMERATE_AND_ASSIGN1(create(ENUM_LIST,   ^val), varref);
    case lrel:         ENUMERATE_AND_ASSIGN1(create(ENUM_LIST,   ^val), varref);
    case node:         ENUMERATE_AND_ASSIGN1(create(ENUM_NODE,   ^val), varref);
    case constructor:  ENUMERATE_AND_ASSIGN1(create(ENUM_NODE,   ^val), varref);
    case map:          ENUMERATE_AND_ASSIGN1(create(ENUM_MAP,    ^val), varref);
    case set:          ENUMERATE_AND_ASSIGN1(create(ENUM_SET,    ^val), varref);
    case rel:          ENUMERATE_AND_ASSIGN1(create(ENUM_SET,    ^val), varref);
    case tuple:        ENUMERATE_AND_ASSIGN1(create(ENUM_TUPLE,  ^val), varref);
    default:           ENUMERATE_AND_ASSIGN1(create(ENUM_LITERAL,^val), varref);
  };
}

coroutine ENUMERATE_CHECK_AND_ASSIGN1[3, enumerator, typ, varref, elm]{
   enumerator = init(enumerator, ref elm); // NOTE: reflects the fact that all ENUM declarations get an extra parameter
   while(next(enumerator)){
     if(subtype(typeOf(elm), typ)){
     	yield elm;
     };
   }; 
}

coroutine ENUMERATE_CHECK_AND_ASSIGN[3, ^val, typ, varref]{ // NOTE: flipped parameters
  /* NOTE: initialization of coroutines has been moved into the ENUMERATE_CHECK_AND_ASSIGN1 body */
  /* NOTE: apparently, we have an example of stackful coroutines here */
  typeswitch(^val){
    case list:         ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_LIST,   ^val), typ, varref);
    case lrel:         ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_LIST,   ^val), typ, varref);
    case node:         ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_NODE,   ^val), typ, varref);
    case constructor:  ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_NODE,   ^val), typ, varref);
    case map:          ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_MAP,    ^val), typ, varref);
    case set:          ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_SET,    ^val), typ, varref);
    case rel:          ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_SET,    ^val), typ, varref);
    case tuple:        ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_TUPLE,  ^val), typ, varref);
    default:           ENUMERATE_CHECK_AND_ASSIGN1(create(ENUM_LITERAL,^val), typ, varref);
  };
}


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

// ***** Pattern matching *****

coroutine MATCH[2, pat, ^subject, cpat]{
   cpat = init(pat, ^subject);
   while(next(cpat)){
      yield;
   };
}

coroutine MATCH_N[2, pats, subjects, ipats, plen, slen, p, pat]{ /* NOTE: fixed semantics */
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

coroutine MATCH_CALL_OR_TREE[2, pats, ^subject, cpats]{
    guard ^subject is node;
    cpats = init(create(MATCH_N, pats, get_name_and_children(^subject)));
    while(next(cpats)) {
        yield;
    };
}

coroutine MATCH_REIFIED_TYPE[2, pat, ^subject, nc, konstructor, symbol]{
    guard ^subject is node;
    nc = get_name_and_children(^subject);
    konstructor = get_array nc[0];
    symbol = get_array nc[1];
    if(equal(konstructor, "type") && equal(symbol, pat)) { // NOTE: the second equal?
        return;
    };
}

coroutine MATCH_TUPLE[2, pats, ^subject, cpats]{
    guard ^subject is tuple;
    cpats = init(create(MATCH_N, pats, get_tuple_elements(^subject)));
    while(next(cpats)) {
        yield;
    };
}

coroutine MATCH_LITERAL[2, pat, ^subject, res]{
    guard ( equal(typeOf(pat),typeOf(^subject)) && equal(pat, ^subject) );
}

coroutine MATCH_VAR[2, ^subject, varref]{ // NOTE: flipped parameters
   return ^subject;
}

coroutine MATCH_ANONYMOUS_VAR[1, ^subject]{
   return;
}

coroutine MATCH_TYPED_VAR[3, ^subject, typ, varref]{ // NOTE: flipped parameters
   guard subtype(typeOf(^subject), typ);
   return ^subject;  
}

coroutine MATCH_TYPED_ANONYMOUS_VAR[2, typ, ^subject]{
   guard subtype(typeOf(^subject), typ);
   return;
}

coroutine MATCH_VAR_BECOMES[3, pat, ^subject, varref, cpat]{ // NOTE: flipped parameters
   cpat = init(pat, ^subject);
   while(next(cpat)){
       yield ^subject;
   };
}

coroutine MATCH_TYPED_VAR_BECOMES[4, pat, ^subject, typ, varref, cpat]{ // NOTE: flipped parameters
   guard subtype(typeOf(^subject), typ);
   cpat = init(pat, ^subject);
   while(next(cpat)){
       yield ^subject;
   };
}

coroutine MATCH_AS_TYPE[3, typ, pat, ^subject]{ // NOTE: example of stackful coroutines is here
   guard subtype(typeOf(^subject), typ);
   DO_ALL(pat, ^subject);
}

coroutine MATCH_ANTI[2, pat, ^subject, cpat]{
	cpat = init(pat, ^subject);
	if(next(cpat)){
	   exhaust;
	} else {
	   return;
	};
}

// ***** List matching *****

coroutine MATCH_LIST[2, pats,   					// A list of coroutines to match list elements
					   ^subject,					// The subject list
					   
					   patlen,						// Length of pattern list
					   patlen1,						// patlen - 1
					   sublen,						// Length of subject list
					   p,							// Cursor in patterns
					   cursor,						// Cursor in subject
					   forward,
					   matcher,						// Currently active pattern matcher
					   matchers,					// List of currently active pattern matchers
					   success,						// Success flag of last macth
					   nextCursor					// Cursor movement of last successfull match
					]{
     guard ^subject is list;
     
     patlen   = size_array(pats);
     patlen1 =  patlen - 1;
     sublen   = size_list(^subject);
     p        = 0; 
     cursor   = 0;
     forward  = true;
     matcher  = init(get_array pats[p], ^subject, cursor, sublen);
     matchers = make_array(patlen);
     set_array matchers[0] = matcher;
     
     while(true){
     	// Move forward
     	 forward = hasNext(matcher);
     	 // prim("println", ["At head", p, cursor, forward]);
         while(forward && hasNext(matcher)){
        	[success, nextCursor] = next(matcher);
            if(success){ 
               forward = true;
               cursor = nextCursor;
               // prim("println", ["SUCCESS", p, cursor]);
               if((p == patlen1) && (cursor == sublen)) {
                   // prim("println", ["*** YIELD", p, cursor]);
              	   yield true;
              	   // prim("println", ["Back from yield", p, cursor]); 
               } else {
                 if(p < patlen1){
                   p = p + 1;
                   // prim("println", ["Forward", p, cursor]);
                   matcher  = init(get_array pats[p], ^subject, cursor,  sublen - cursor);
                   set_array matchers[p] = matcher;
                 } else {
                   if(hasNext(matcher)){
                     // explore more alternatives
                   } else {
                      // prim("println", ["Backward", p, cursor]);
                      forward = false;
                   };
                 };  
               };
            } else {
              // prim("println", ["No success, Backward", p, cursor]);
              forward = false;
            };
         }; 
         // If possible, move backward
         if(forward){
           // nothing
         } else {  
           if(p > 0){
               p        = p - 1;
               matcher  = get_array matchers[p];
               forward  = true;
           } else {
         	   // prim("println", ["RETURN FALSE", p, cursor]);
               return false;
           };
         };
     };
}

// All coroutines that may occur in a list pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - start: the start index in the subject list
// - available: the number of remianing, unmatched, elements in the subject list

function MATCH_PAT_IN_LIST[4, pat, ^subject, start, available, cpat]{

    if(available <= 0){
       return [false, start];
    }; 
 
    cpat = init(pat, get_list ^subject[start]);
    
    while(hasNext(cpat)){
       if(next(cpat)){
          return [true, start + 1];
       };   
    };
    return [false, start];
} 

function MATCH_VAR_IN_LIST[4, varref, ^subject, start, available]{
   if(available <= 0){
       return [false, start];
   }; 
//   if(is_defined(deref varref)){
//      if(equal(deref varref, get_list ^subject[start])){
//         return [true, start + 1];
//      } else {
//         return [ false, start];
//      };
//   };
   deref varref = get_list ^subject[start];
   return [true, start + 1];
}

function MATCH_ANONYMOUS_VAR_IN_LIST[3, ^subject, start, available]{
   if(available <= 0){
       return [false, start];
   }; 
   return [true, start + 1];
}

function MATCH_MULTIVAR_IN_LIST[4, varref, ^subject, start, available, len]{
//   if(is_defined(deref varref)){
//       if(starts_with(deref varref, ^subject, start)){
//          return [ true, start + size_list(deref varref) ];
//       } else {
//         return [false, start];
//       };
//    };
    len = 0;
    while(len <= available){
        deref varref = sublist(^subject, start, len);
        // prim("println", ["MATCH_MULTIVAR_IN_LIST", prim("addition_mint_mint", start, len)]);
        yield [true, start + len];
        len = len + 1;
     };
     return [false, start];
}

function MATCH_ANONYMOUS_MULTIVAR_IN_LIST[3, ^subject, start, available, len]{
    len = 0;
    while(len <= available){
        // prim("println", ["MATCH_MULTIVAR_IN_LIST", prim("addition_mint_mint", start, len)]);
        yield [true, start + len];
        len = len + 1;
     };
     return [false, start];
}

function MATCH_TYPED_MULTIVAR_IN_LIST[5, typ, varref, ^subject, start, available, len]{
    if(subtype(typeOf(^subject), typ)){
//       if(is_defined(deref varref)){
//          if(starts_with(deref varref, ^subject, start)){
//             return [ true, start + size_list(deref varref) ];
//          } else {
//            return [false, start];
//          };
//       };
       len = 0;
       while(len <= available){
          deref varref = sublist(^subject, start, len);
          // prim("println", ["MATCH_MULTIVAR_IN_LIST", prim("addition_mint_mint", start, len)]);
          yield [true, start + len];
          len = len + 1;
       };       
    };
    return [false, start];
}

function MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_LIST[4, typ, ^subject, start, available, len]{
    if(subtype(typeOf(^subject), typ)){
       len = 0;
       while(len <= available){
          yield [true, start + len];
          len = len + 1;
       };       
    };
    return [false, start];
}

// ***** SET matching *****

function MATCH_SET[2,  pair,	   					// A pair of literals, and patterns (other patterns first, multiivars last) to match set elements
					   ^subject,					// The subject set
					   ^literals,					// The literals that occur in the set pattern
					   pats,						// the patterns
					   subject1,					// subject minus literals as mset
					   patlen,						// Length of pattern list
					   patlen1,						// patlen - 1
					   p,							// Cursor in patterns
					   current,						// Current mset to be matched
					   forward,
					   matcher,						// Currently active pattern matcher
					   matchers,					// List of currently active pattern matchers
					   success,						// Success flag of last macth
					   remaining					// Remaining mset as determined by last successfull match
					]{
      if(^subject is set) {
          // continue
      } else {
          return false;
      };
      ^literals = get_array pair[0];
      pats      = get_array pair[1];
      
     if(subset(^literals, ^subject)){
        subject1 = mset_destructive_subtract_set(mset(^subject), ^literals);
     	patlen    = size_array(pats);
     	if(patlen == 0){
     	   success = size_mset(subject1) == 0;
     	   return success;
     	};    
     	patlen1   =  patlen - 1;
     	p         = 0;
     	forward   = true;
     	matcher   = init(get_array pats[p], subject1);
     	matchers  = make_array(patlen);
     	set_array matchers[0] = matcher;
     	
     	while(true){
     	// Move forward
     	 forward = hasNext(matcher);
     	 //println("At head, pattern", p);
         while(forward && hasNext(matcher)){
        	[success, remaining] = next(matcher);
            if(success){ 
               //println("success, remaining = ", remaining);
               forward = true;
               current = remaining;
               if((p == patlen1) && (size_mset(current) == 0)) {
              	   yield true;
              	   //println("Back from yield, at pattern", p); 
               } else {
                 if(p < patlen1){
                   p = p + 1;
                   //println("Move right to pattern", p);
                   matcher  = init(get_array pats[p], current);
                   set_array matchers[p] = matcher;
                 } else {
                   if(hasNext(matcher)){
                     // explore more alternatives
                   } else {
                      forward = false;
                   };
                 };  
               };
            } else {
              forward = false;
            };
         }; 
         // If possible, move backward
         if(forward){
           // nothing
         } else {  
           if(p > 0){
               p        = p - 1;
               //println("Move left to pattern", p);
               matcher  = get_array matchers[p];
               forward  = true;
           } else {
               return false;
           };
         };
      };
        
     } else {
       return false;
     };
}

function ENUM_MSET[1, set, ^lst, last, i]{
   ^lst = mset2list(set);
   last = size_list(^lst) - 1;
   i = 0;
   while(i < last){
      yield get_list ^lst[i];
      i = i + 1;
   };
   return get_list ^lst[last];
}

// All coroutines that may occur in a set pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - available: the remaining, unmatched, elements in the subject set

function MATCH_PAT_IN_SET[2, pat, available, gen, cpat, elm]{
    if(size_mset(available) == 0){
       return [ false, available ];
    }; 

    gen = init(create(ENUM_MSET, available));
    while(hasNext(gen)){
        elm = next(gen);
        cpat = init(pat, elm);
        while(hasNext(cpat)){
           if(next(cpat)){
              yield [ true, mset_destructive_subtract_elm(available, elm) ];
              available = mset_destructive_add_elm(available, elm);
           };
        };
    };
    return [ false, available ];
} 

function MATCH_VAR_IN_SET[2, varref, available, gen, elm]{
   if(size_mset(available) == 0){
       return [ false, available ];
   };
 
   gen = init(create(ENUM_MSET, available));
   while(hasNext(gen)){
	     elm = next(gen);
	     deref varref = elm;
	     yield [ true, mset_destructive_subtract_elm(available, elm) ];
	     available = mset_destructive_add_elm(available, elm);
   };
   return [ false, available ];
}

function MATCH_ANONYMOUS_VAR_IN_SET[1, available, gen, elm]{
   if(size_set(available) == 0){
       return [ false, available ];
   };
 
   gen = init(create(ENUM_MSET, available));
   while(hasNext(gen)){ 
        elm = next(gen);
        yield [ true, mset_destructive_subtract_elm(available, elm) ];
        available = mset_destructive_add_elm(available, elm);
   };
   return [ false, available ];
}

function MATCH_MULTIVAR_IN_SET[2, varref, available, gen, subset]{
   gen = init(create(ENUM_SUBSETS, available));
   while(hasNext(gen)){
	     subset = next(gen);
	     deref varref = set(subset);
	     yield [ true,mset_destructive_subtract_mset(available, subset) ];
	     available = mset_destructive_add_mset(available, subset);
   };
   return [ false, available ];
}

function MATCH_ANONYMOUS_MULTIVAR_IN_SET[1, available, gen, subset]{
   gen = init(create(ENUM_SUBSETS, available));
   while(hasNext(gen)){
	     subset = next(gen);
	     yield [ true,mset_destructive_subtract_mset(available, subset) ];
	     available = mset_destructive_add_mset(available, subset);
   };
   return [ false, available ];
}

function MATCH_TYPED_MULTIVAR_IN_SET[3, typ, varref, available, gen, subset]{
    //println("MATCH_TYPED_MULTIVAR_IN_SET", typ, varref, available);
    if(subtype(typeOf(available), typ)){
	   gen = init(create(ENUM_SUBSETS, available));
	   while(hasNext(gen)){
	         subset = next(gen);
	   		 deref varref = set(subset);
	   		 //println("MATCH_TYPED_MULTIVAR_IN_SET, assigns", varref, subset);
	          yield [ true,mset_destructive_subtract_mset(available, subset) ];
	          available = mset_destructive_add_mset(available, subset);
	   };
    };
    //println("MATCH_TYPED_MULTIVAR_IN_SET: returns false");
    return [ false, available ];
}

function MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_SET[2, typ, available, gen, subset]{
    //println("MATCH_TYPED_MULTIVAR_IN_SET", typ, available);
 
    if(subtype(typeOf(available), typ)){
	   gen = init(create(ENUM_SUBSETS, available));
       while(hasNext(gen)){
             subset = next(gen);
	          yield [ true,mset_destructive_subtract_mset(available, subset) ];
	          available = mset_destructive_add_mset(available, subset);
	   };
    };
    return [ false, available ];
}

// the power set of a set of size n has 2^n-1 elements 
// so we enumerate the numbers 0..2^n-1
// if the nth bit of a number i is 1 then
// the nth element of the set should be in the
// ith subset 
 
function ENUM_SUBSETS[1, set, lst, i, j, last, elIndex, sub]{
    //println("ENUM_SUBSETS for:", set);
    lst = mset2list(set); 
    last = 2 pow size_mset(set);
    i = last - 1;
    while(i >= 0){
        //println("ENUM_SUBSETS", "i = ", i);
        j = i;
        elIndex = 0; 
        sub = make_mset();
        while(j > 0){
           if(j mod 2 == 1){
              //println("ENUM_SUBSETS", "j = ", j, "elIndex =", elIndex);
              sub = mset_destructive_add_elm(sub, get_list lst[elIndex]);
           };
           elIndex = elIndex + 1;
           j = j / 2;
        };
        //println("ENUM_SUBSETS returns:", sub, "i =", i, "last one = ", i == 0);
        if(i == 0){
           return sub;
        } else {
           yield sub;
        }; 
        i = i - 1;  
    };
}

// ***** Descendent pattern ***

function MATCH_DESCENDANT[2, pat, ^subject, gen, cpat]{
   //println("MATCH_DESCENDANT", pat, ^subject);
   DO_ALL(create(MATCH_AND_DESCENT, pat),  ^subject);
   return false;
}

// ***** Match and descent for all types *****

function MATCH_AND_DESCENT[2, pat, ^val]{
  //println("MATCH_AND_DESCENT", pat, ^val);
  DO_ALL(pat, ^val);
  
  //println("MATCH_AND_DESCENT", "outer match completed"); 
  typeswitch(^val){
    case list:        DO_ALL(create(MATCH_AND_DESCENT_LIST, pat), ^val);
    case lrel:        DO_ALL(create(MATCH_AND_DESCENT_LIST, pat), ^val);
    case node:        DO_ALL(create(MATCH_AND_DESCENT_NODE, pat), ^val);
    case constructor: DO_ALL(create(MATCH_AND_DESCENT_NODE, pat), ^val);
    case map:         DO_ALL(create(MATCH_AND_DESCENT_MAP, pat), ^val);
    case set:         DO_ALL(create(MATCH_AND_DESCENT_SET, pat), ^val);
    case rel:         DO_ALL(create(MATCH_AND_DESCENT_SET, pat), ^val);
    case tuple:       DO_ALL(create(MATCH_AND_DESCENT_TUPLE, pat), ^val);
    default:          return false;
  };  
  return false;
}
/*
function VISIT[1, visitor]{
   //println("VISIT", visitor);
   while(hasNext(visitor)){
        if(next(visitor)){
           if(hasNext(visitor)){
              yield true;
           } else {
             return true;
           };
        };
   }; 
   return false;     
}   
*/

function MATCH_AND_DESCENT_LITERAL[2, pat, ^subject, res]{
  //println("MATCH_AND_DESCENT_LITERAL", pat, ^subject);
  if(equal(typeOf(pat), typeOf(^subject))){
     res = equal(pat, ^subject);
     return res;
  };
  
  return MATCH_AND_DESCENT(create(MATCH_LITERAL, pat), ^subject);
}

function MATCH_AND_DESCENT_LIST[2, pat, ^lst, last, i]{
   //println("MATCH_AND_DESCENT_LIST", pat, ^lst);
   last = size_list(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^lst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_SET[2, pat, ^set, ^lst, last, i]{
   //println("MATCH_AND_DESCENT_SET", pat, ^set);
   ^lst = set2list(^set);
   last = size_list(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^lst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_MAP[2, pat, ^map, ^klst, ^vlst, last, i]{
   ^klst = keys(^map);
   ^vlst = values(^map);
   last = size_list(^klst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^klst[i]);
      DO_ALL(pat, get_list ^vlst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^klst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^vlst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_NODE[2, pat, ^nd, last, i, ar]{
   ar = get_name_and_children(^nd);
   last = size_array(ar);
   i = 0; 
   while(i < last){
      DO_ALL(pat, get_array ar[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_array ar[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_TUPLE[2, pat, ^tup, last, i]{
   last = size_tuple(^tup);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_tuple ^tup[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_tuple ^tup[i]);
      i = i + 1;
   };
   return false;
}

// ***** Regular expressions *****

function MATCH_REGEXP[3, ^regexp, varrefs, ^subject, matcher, i, varref]{
   matcher = muprim("regexp_compile", ^regexp, ^subject);
   while(muprim("regexp_find", matcher)){
     i = 0; 
     while(i < size_array(varrefs)){
        varref = get_array varrefs[i];
        deref varref = muprim("regexp_group", matcher, i + 1);
        i = i + 1;
     };
     yield true;
   };
   return false;
}

// ***** Traverse functions *****

function TRAVERSE_TOP_DOWN[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
							  matched, changed] {
	matched = false; // ignored	
	changed = false;
	^subject = phi(^subject, ref matched, ref changed);
	if(rebuild) {
		deref beenChanged = changed || deref beenChanged;
		changed = false;
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_TOP_DOWN::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;	
		return ^subject;
	};
	return VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_TOP_DOWN::5, phi, hasMatch, ref changed, rebuild);
}

function TRAVERSE_TOP_DOWN_BREAK[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
									matched, changed] {
	matched = false;
	changed = false;
	^subject = phi(^subject, ref matched, ref changed);
	deref beenChanged = changed || deref beenChanged;	
	if(deref hasMatch = matched || deref hasMatch) {	
		return ^subject;
	};
	if(rebuild) {
		changed = false;
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;
		return ^subject;
	};	
	return VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, hasMatch, ref changed, rebuild);
}

function TRAVERSE_BOTTOM_UP[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
							   matched, changed] {
	matched = false; // ignored
	changed = false;
	if(rebuild) {
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_BOTTOM_UP::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;
		changed = false;
	} else {
		VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_BOTTOM_UP::5, phi, hasMatch, ref changed, rebuild);
	};
	^subject = phi(^subject, ref matched, ref changed);
	deref beenChanged = changed || deref beenChanged;
	return ^subject;
}

function TRAVERSE_BOTTOM_UP_BREAK[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
									 matched, changed] {
	matched = false;
	changed = false;
	if(rebuild) {
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;
		changed = false;
	} else {
		VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, hasMatch, ref changed, rebuild);
	};		
	if(deref hasMatch) {	
		return ^subject;
	};
	^subject = phi(^subject, ref matched, ref changed);
	deref hasMatch = matched || deref hasMatch;
	deref beenChanged = changed || deref beenChanged;	
	return ^subject;
}

function VISIT_CHILDREN[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild, 
						   children] {
	if((^subject is list) || (^subject is set) || (^subject is tuple) || (^subject is node)) {
		children = VISIT_NOT_MAP(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild);
	} else {
		if(^subject is map) {
			children = VISIT_MAP(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild); // special case of map
		};
	};
	if(deref beenChanged) {
		return typeswitch(^subject) {
	    			case list:  prim("list", children);
	    			case lrel:  prim("list", children);
	    			case set:   prim("set",  children);
	    			case rel:   prim("set",  children);
	    			case tuple: prim("tuple",children);
	    			case node:  prim("node", muprim("get_name", ^subject), children);
	    			case constructor: 
	                			prim("constructor", muprim("typeOf_constructor", ^subject), children);	    
	    			case map:   children; // special case of map	    
	    			default:    ^subject;
				};
	};
	return ^subject;
}

function VISIT_NOT_MAP[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
						  iarray, enumerator, ^child, i, childHasMatch, childBeenChanged] {
	iarray = make_iarray(size(^subject));
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^child, ^subject);
	i = 0;
	while(all(multi(enumerator))) {
		childHasMatch = false;
		childBeenChanged = false;
		^child = traverse_fun(phi, ^child, ref childHasMatch, ref childBeenChanged, rebuild);
		set_array iarray[i] = ^child;
		i = i + 1;
		deref hasMatch = childHasMatch || deref hasMatch;
		deref beenChanged = childBeenChanged || deref beenChanged;
	};
	return iarray;
}

function VISIT_MAP[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
					  writer, enumerator, ^key, ^val, childHasMatch, childBeenChanged] {
	writer = prim("mapwriter_open");
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^key, ^subject);
	while(all(multi(enumerator))) {
		^val = prim("map_subscript", ^subject, ^key);
		
		childHasMatch = false;
		childBeenChanged = false;
		^key = traverse_fun(phi, ^key, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
		deref beenChanged = childBeenChanged || deref beenChanged;
		
		childHasMatch = false;
		childBeenChanged = false;
		^val = traverse_fun(phi, ^val, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
		deref beenChanged = childBeenChanged || deref beenChanged;
		
		prim("mapwriter_add", writer, ^key, ^val);
	};
	return prim("mapwriter_close", writer);
}

function VISIT_CHILDREN_VOID[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild] {	
	if((^subject is list) || (^subject is set) || (^subject is tuple) || (^subject is node)) {
		VISIT_NOT_MAP_VOID(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild);
		return ^subject;
	};
	if(^subject is map) {
		VISIT_MAP_VOID(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild); // special case of map
	};
	return ^subject;
}

function VISIT_NOT_MAP_VOID[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
						       enumerator, ^child, childHasMatch, childBeenChanged] {
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^child, ^subject);
	childBeenChanged = false; // ignored
	while(all(multi(enumerator))) {
		childHasMatch = false;
		traverse_fun(phi, ^child, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
	};
	return;
}

function VISIT_MAP_VOID[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
					       enumerator, ^key, ^val, childHasMatch, childBeenChanged] {
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^key, ^subject);
	childBeenChanged = false; // ignored  
	while(all(multi(enumerator))) {
		childHasMatch = false;
		traverse_fun(phi, ^key, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
		
		childHasMatch = false;
		traverse_fun(phi, prim("map_subscript", ^subject, ^key), ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;	
	};
	return;
}