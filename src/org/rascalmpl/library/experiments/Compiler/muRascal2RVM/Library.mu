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

function DO_ALL[2, pat, ^val, co]{
   co = init(pat, ^val);
   while(hasNext(co)){
         if(next(co)){
            yield true;
         };
   };
   return false;
} 

// ***** Enumerators for all types *****

function ENUM_LITERAL[1, ^lit]{
   return ^lit;
}

function ENUM_LIST[1, ^lst, last, i]{
   last = size(^lst) - 1;
   i = 0;
   while(i < last){
      yield get ^lst[i];
      i = i + 1;
   };
   return get ^lst[last];
}

function ENUM_SET[1, ^set, ^lst, last, i]{
   ^lst = set2list(^set);
   last = size(^lst) - 1;
   i = 0;
   while(i < last){
      yield get ^lst[i];
      i = i + 1;
   };
   return get ^lst[last];
}

function ENUM_MAP[1, ^map, ^klst, last, i]{
   ^klst = keys(^map);
   last = size(^klst) - 1;
   i = 0;
   while(i < last){
      yield get ^klst[i];
      i = i + 1;
   };
   return get ^klst[last];
}

function ENUM_NODE[1, ^nd, last, i, lst]{
   lst = get_name_and_children(^nd);
   last = size(lst) - 2;
   i = 1;  // skip name
   while(i < last){
      yield get lst[i];
      i = i + 1;
   };
   return get lst[last];
}

function ENUM_TUPLE[1, ^tup, last, i]{
   last = size(^tup) - 1;
   i = 0;
   while(i < last){
      yield get ^tup[i];
      i = i + 1;
   };
   return get ^tup[last];
}

function ENUMERATE[2, enumerator, pat, cpat, elm]{
   while(hasNext(enumerator)){
     elm = next(enumerator);
     cpat = init(pat, elm);
     while(hasNext(cpat)){
       if(next(cpat)){
          if(hasNext(enumerator)){
             yield true;
          } else {
             return true;
          };
       };
     };
   }; 
   return false;     
}
        
function ENUMERATE_AND_MATCH[2,  pat, ^val]{
  if(^val is list){
     ENUMERATE(init(create(ENUM_LIST, ^val)), pat);
  } else {
    if(^val is node){
      ENUMERATE(init(create(ENUM_NODE, ^val)), pat);
    } else {
      if(^val is map){
        ENUMERATE(init(create(ENUM_MAP, ^val)), pat);
      } else {
        if(^val is set){
           ENUMERATE(init(create(ENUM_SET, ^val)), pat);
        } else {
          if(^val is tuple){
             ENUMERATE(init(create(ENUM_TUPLE, ^val)), pat);
          } else {
             ENUMERATE(init(create(ENUM_LITERAL, ^val)), pat);
          };
        };
      };
    };
  };  
  // Add cases for rel/lrel?
  return ^val;
}

// ***** Ranges *****

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

function MATCH[2, pat, ^subject, cpat]{
   // println("MATCH", pat, ^subject);
   cpat = init(pat, ^subject);
   while(hasNext(cpat)){
      if(next(cpat)){
         yield true;
      } else {
        return false;
      };
   };
   return false;
}

function MATCH_N[2, pats, subjects, plen, slen, p, pat]{
   // println("MATCH_N", pats, subjects);
   plen = size(pats);
   slen = size(subjects);
   if(plen != slen){
      // println("MATCH_N: unequal length", plen, slen);
      return false;
   };
   p = 0;
   while(p < plen){
     // println("MATCH_N: init ", p);
     set pats[p] = init(get pats[p], get subjects[p]);
     p = p + 1;
   };
   
   while(true){
     p = 0;
     while(p < plen){
       // println("p = ", p);
       pat = get pats[p];
       if(hasNext(pat)){
          if(next(pat)){
              p = p + 1;
           } else {
              return false;
           };   
       } else {
         return false;
       };
     };
     // println("MATCH_N yields true");
     yield true; 
   };
}

function MATCH_CALL_OR_TREE[2, pats, ^subject, cpats]{
    // println("MATCH_CALL_OR_TREE", pats, ^subject);
    if(^subject is node){
      cpats = init(create(MATCH_N, pats, get_name_and_children(^subject)));
      while(hasNext(cpats)){
        // println("MATCH_CALL_OR_TREE", "hasNext=true");
        if(next(cpats)){
           yield true;
        } else {
           return false;
        };
      };
    };
    return false;
}

function MATCH_TUPLE[2, pats, ^subject, cpats]{
    // println("MATCH_TUPLE", pats, ^subject);
    if(^subject is tuple){
      cpats = init(create(MATCH_N, pats, get_tuple_elements(^subject)));
      while(hasNext(cpats)){
        // println("MATCH_TUPLE", "hasNext=true");
        if(next(cpats)){
           yield true;
        } else {
           return false;
        };
      };
    };
    return false;
}

function MATCH_LITERAL[2, pat, ^subject, res]{
  // println("MATCH_LITERAL", pat, ^subject);
  if(equal(typeOf(pat), typeOf(^subject))){
     return equal(pat, ^subject);
  };
  return false;
}

function MATCH_VAR[2, varref, ^subject]{
//   if(is_defined(deref varref)){
//      return equal(deref varref, ^subject);
//   };
   deref varref = ^subject;
   return true;
}

function MATCH_TYPED_VAR[3, typ, varref, ^subject]{
   if(subtype(typeOf(^subject), typ)){
//     if(is_defined(deref varref)){
//         return equal(deref varref, ^subject);
//      };
      deref varref = ^subject;
      return true;
   };
   return false;  
}

function MATCH_VAR_BECOMES[3, varref, pat, ^subject, cpat]{
   cpat = init(pat, ^subject);
   while(hasNext(cpat)){
     deref varref = ^subject;
     yield true;
   };
   return false;
}

function MATCH_TYPED_VAR_BECOMES[4, typ, varref, pat, ^subject, cpat]{
   if(equal(typ, typeOf(^subject))){
     cpat = init(pat, ^subject);
     while(hasNext(cpat)){
       deref varref = ^subject;
       yield true;
     };
   };  
   return false;
}

function MATCH_AS_TYPE[3, typ, pat, ^subject]{
   if(equal(typ, typeOf(^subject))){
      DO_ALL(pat, ^subject);
   };  
   return false;
}

function MATCH_ANTI[2, pat, ^subject, cpat]{
	cpat = init(pat, ^subject);
	if(next(cpat)){
	   return false;
	} else {
	   return true;
	};
}

// ***** List matching *****

function MATCH_LIST[2, pats,   						// A list of coroutines to match list elements
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

     patlen   = size(pats);
     patlen1 =  patlen - 1;
     sublen   = size(^subject);
     p        = 0; 
     cursor   = 0;
     forward  = true;
     matcher  = init(get pats[p], ^subject, cursor, sublen);
     matchers = make_array(patlen);
     set matchers[0] = matcher;
     
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
                   matcher  = init(get pats[p], ^subject, cursor,  sublen - cursor);
                   set matchers[p] = matcher;
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
               matcher  = get matchers[p];
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
 
    cpat = init(pat, get ^subject[start]);
    
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
//      if(equal(deref varref, get ^subject[start])){
//         return [true, start + 1];
//      } else {
//         return [ false, start];
//      };
//   };
   deref varref = get ^subject[start];
   return [true, start + 1];
}

function MATCH_MULTIVAR_IN_LIST[4, varref, ^subject, start, available, len]{
//   if(is_defined(deref varref)){
//       if(starts_with(deref varref, ^subject, start)){
//          return [ true, start + size(deref varref) ];
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

function MATCH_TYPED_MULTIVAR_IN_LIST[5, typ, varref, ^subject, start, available, len]{
    if(equal(typ, typeOf(^subject))){
//       if(is_defined(deref varref)){
//          if(starts_with(deref varref, ^subject, start)){
//             return [ true, start + size(deref varref) ];
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

// ***** SET matching *****

function MATCH_SET[2,  pair,	   					// A pair of literals, and patterns (other patterns first, multiivars last) to match set elements
					   ^subject,					// The subject list
					   ^literals,					// The literals that occur in the set pattern
					   pats,						// the patterns
					   ^subject1,					// subject minus literals
					   patlen,						// Length of pattern list
					   patlen1,						// patlen - 1
					   p,							// Cursor in patterns
					   ^current,					// Current set to be matched
					   forward,
					   matcher,						// Currently active pattern matcher
					   matchers,					// List of currently active pattern matchers
					   success,						// Success flag of last macth
					   ^remaining					// Remaining set as determined by last successfull match
					]{
      ^literals = get pair[0];
      pats      = get pair[1];
      
     if(subset(^literals, ^subject)){
        ^subject1 = set_subtract_set(^subject, ^literals);
     	patlen    = size(pats);
     	if(patlen == 0){
     	   success = size(^subject1) == 0;
     	   return success;
     	};    
     	patlen1   =  patlen - 1;
     	p         = 0;
     	forward   = true;
     	matcher   = init(get pats[p], ^subject1);
     	matchers  = make_array(patlen);
     	set matchers[0] = matcher;
     	
     	while(true){
     	// Move forward
     	 forward = hasNext(matcher);
     	 //println("At head", p);
         while(forward && hasNext(matcher)){
        	[success, ^remaining] = next(matcher);
            if(success){ 
               forward = true;
               ^current = ^remaining;
               if((p == patlen1) && (size(^current) == 0)) {
              	   yield true;
              	   //println("Back from yield", p); 
               } else {
                 if(p < patlen1){
                   p = p + 1;
                   //println("Move right to", p);
                   matcher  = init(get pats[p], ^current);
                   set matchers[p] = matcher;
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
               //println("Move left to", p);
               matcher  = get matchers[p];
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

// All coroutines that may occur in a set pattern have the following parameters:
// - pat: the actual pattern to match one or more elements
// - start: the start index in the subject list
// - available: the remaining, unmatched, elements in the subject set

function MATCH_PAT_IN_SET[2, pat, ^available, gen, cpat, elm]{
    if(size(^available) == 0){
       return [ false, ^available ];
    }; 
    
    gen = init(create(ENUM_SET, ^available));
    while(hasNext(gen)){
        elm = next(gen);
        cpat = init(pat, elm);
        while(hasNext(cpat)){
           if(next(cpat)){
              yield [ true, set_subtract_elm(^available, elm) ];
           };
        };
    };
    return [ false, ^available ];
} 

function MATCH_VAR_IN_SET[2, varref, ^available, gen, elm]{
   if(size(^available) == 0){
       return [ false, ^available ];
   };
//   if(is_defined(deref varref)){
//       if(is_element(deref varref, ^available)){
//	         return [ true, set_subtract_elm(^available, deref varref) ];
//	    };
//   } else {
	    gen = init(create(ENUM_SET, ^available));
	    while(hasNext(gen)){
	        elm = next(gen);
	   		deref varref = elm;
	        yield [ true, set_subtract_elm(^available, elm) ];
	    };
//   };
    return [ false, ^available ];
}

function MATCH_MULTIVAR_IN_SET[2, varref, ^available, gen, ^subset]{
//    if(is_defined(deref varref)){
//         if(subset(deref varref, ^available)){
//	         return [ true, set_subtract_set(^available, deref varref) ];
//	      };
//	} else {
	    gen = init(create(ENUM_SUBSETS, ^available));
	    while(hasNext(gen)){
	        ^subset = next(gen);
	   		deref varref = ^subset;
	        yield [ true, set_subtract_set(^available, ^subset) ];
	    };
//    };
    return [ false, ^available ];
}

function MATCH_TYPED_MULTIVAR_IN_SET[3, typ, varref, ^available, gen, ^subset]{
    // println("MATCH_TYPED_MULTIVAR_IN_SET", typ, varref, ^available);
    if(equal(typ, typeOf(^available))){
//       if(is_defined(deref varref)){
//          // println("MATCH_TYPED_MULTIVAR_IN_SET, is_defined:", deref varref);
//          if(subset(deref varref, ^available)){
//	         return [ true, set_subtract_set(^available, deref varref) ];
//	      };
//	   } else {
	       gen = init(create(ENUM_SUBSETS, ^available));
	       while(hasNext(gen)){
	          ^subset = next(gen);
	   		  deref varref = ^subset;
	          yield [ true, set_subtract_set(^available, ^subset) ];
	       };
//	   };
    };
    return [ false, ^available ];
}

// the power set of a set of size n has 2^n-1 elements 
// so we enumerate the numbers 0..2^n-1
// if the nth bit of a number i is 1 then
// the nth element of the set should be in the
// ith subset 

function ENUM_SUBSETS[1, ^set, lst, i, j, last, elIndex, ^sub]{
    // println("ENUM_SUBSETS for:", ^set);
    lst = set2list(^set);
    last = 2 pow size(^set);
    i = last - 1;
    while(i >= 0){
        //println("ENUM_SUBSETS", "i = ", i);
        j = i;
        elIndex = 0;
        ^sub = make_set();
        while(j > 0){
           if(j mod 2 == 1){
              //println("ENUM_SUBSETS", "j = ", j, "elIndex =", elIndex);
              ^sub = set_add_elm(^sub, get lst[elIndex]);
           };
           elIndex = elIndex + 1;
           j = j / 2;
        };
        // println("ENUM_SUBSETS returns:", ^sub, "i =", i, "last one = ", i == 0);
        if(i == 0){
           return ^sub;
        } else {
           yield ^sub;
        }; 
        i = i - 1;  
    };
}

// ***** Descendent pattern ***

function MATCH_DESCENDANT[2, pat, ^subject, gen, cpat]{
   // println("MATCH_DESCENDANT", pat, ^subject);
   DO_ALL(create(MATCH_AND_DESCENT, pat),  ^subject);
   return false;
}

// ***** Match and descent for all types *****

function MATCH_AND_DESCENT_LITERAL[2, pat, ^subject, res]{
  // println("MATCH_AND_DESCENT_LITERAL", pat, ^subject);
  if(equal(typeOf(pat), typeOf(^subject))){
     res = equal(pat, ^subject);
     return res;
  };
  
  return MATCH_AND_DESCENT(create(MATCH_LITERAL, pat), ^subject);
}

function MATCH_AND_DESCENT_LIST[2, pat, ^lst, last, i]{
   // println("MATCH_AND_DESCENT_LIST", pat, ^lst);
   last = size(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_SET[2, pat, ^set, ^lst, last, i]{
   // println("MATCH_AND_DESCENT_SET", pat, ^set);
   ^lst = set2list(^set);
   last = size(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_MAP[2, pat, ^map, ^klst, ^vlst, last, i]{
   ^klst = keys(^map);
   ^vlst = values(^map);
   last = size(^klst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get ^klst[i]);
      DO_ALL(pat, get ^vlst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_NODE[2, pat, ^nd, last, i, lst]{
   lst = get_name_and_children(^nd);
   last = size(lst);
   i = 0; 
   while(i < last){
      DO_ALL(pat, get lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_TUPLE[2, pat, ^tup, last, i]{
   last = size(^tup) - 1;
   i = 0;
   while(i < last){
      DO_ALL(pat, get ^tup[i]);
      i = i + 1;
   };
   return false;
}
 
function VISIT[1, visitor]{
   // println("VISIT", visitor);
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

function MATCH_AND_DESCENT[2, pat, ^val]{
  // println("MATCH_AND_DESCENT", pat, ^val);
  DO_ALL(pat, ^val);
  
  // println("MATCH_AND_DESCENT", "outer match failed");    
  if(^val is list){
     return VISIT(init(create(MATCH_AND_DESCENT_LIST, pat, ^val)));
  } else {
    if(^val is node){
      VISIT(init(create(MATCH_AND_DESCENT_NODE, pat, ^val)));
    } else {
      if(^val is map){
        VISIT(init(create(MATCH_AND_DESCENT_MAP, pat, ^val)));
      } else {
        if(^val is set){
           VISIT(init(create(MATCH_AND_DESCENT_SET, pat, ^val)));
        } else {
          if(^val is tuple){
             VISIT(init(create(MATCH_AND_DESCENT_TUPLE, pat, ^val)));
          } else {
             return false;
          };
        };
      };
    };
  };  
  // Add cases for rel/lrel?
  return false;
}