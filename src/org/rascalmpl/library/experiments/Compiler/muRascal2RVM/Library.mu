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
   last = size_list(^lst) - 1;
   i = 0;
   while(i < last){
      yield get_list ^lst[i];
      i = i + 1;
   };
   return get_list ^lst[last];
}

function ENUM_SET[1, ^set, ^lst, last, i]{
   ^lst = set2list(^set);
   last = size_list(^lst) - 1;
   i = 0;
   while(i < last){
      yield get_list ^lst[i];
      i = i + 1;
   };
   return get_list ^lst[last];
}

function ENUM_MAP[1, ^map, ^klst, last, i]{
   ^klst = keys(^map);
   last = size_list(^klst) - 1;
   i = 0;
   while(i < last){
      yield get_list ^klst[i];
      i = i + 1;
   };
   return get_list ^klst[last];
}

function ENUM_NODE[1, ^nd, last, i, lst]{
   lst = get_name_and_children(^nd);
   last = size_array(lst) - 2;
   i = 1;  // skip name
   while(i < last){
      yield get_list lst[i];
      i = i + 1;
   };
   return get_list lst[last];
}

function ENUM_TUPLE[1, ^tup, last, i]{
   last = size_tuple(^tup) - 1;
   i = 0;
   while(i < last){
      yield get_tuple ^tup[i];
      i = i + 1;
   };
   return get_tuple ^tup[last];
}

function ENUMERATE_AND_MATCH1[2, enumerator, pat, cpat, elm]{
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
  typeswitch(^val){
    case list:  ENUMERATE_AND_MATCH1(init(create(ENUM_LIST, ^val)), pat);
    case node:  ENUMERATE_AND_MATCH1(init(create(ENUM_NODE, ^val)), pat);
    case map:   ENUMERATE_AND_MATCH1(init(create(ENUM_MAP, ^val)), pat);
    case set:   ENUMERATE_AND_MATCH1(init(create(ENUM_SET, ^val)), pat);
    case tuple: ENUMERATE_AND_MATCH1(init(create(ENUM_TUPLE, ^val)), pat);
    default:    ENUMERATE_AND_MATCH1(init(create(ENUM_LITERAL, ^val)), pat);
  };
  // Add cases for rel/lrel?
  return ^val;
}

function ENUMERATE_AND_ASSIGN1[2, enumerator, varref, elm]{
   while(hasNext(enumerator)){
     elm = next(enumerator);
     deref varref = elm;
     yield true;
   }; 
   return false;     
}

function ENUMERATE_AND_ASSIGN[2, varref, ^val]{
  typeswitch(^val){
    case list:  ENUMERATE_AND_ASSIGN1(init(create(ENUM_LIST, ^val)), varref);
    case node:  ENUMERATE_AND_ASSIGN1(init(create(ENUM_NODE, ^val)), varref);
    case map:   ENUMERATE_AND_ASSIGN1(init(create(ENUM_MAP, ^val)), varref);
    case set:   ENUMERATE_AND_ASSIGN1(init(create(ENUM_SET, ^val)), varref);
    case tuple: ENUMERATE_AND_ASSIGN1(init(create(ENUM_TUPLE, ^val)), varref);
    default:    ENUMERATE_AND_ASSIGN1(init(create(ENUM_LITERAL, ^val)), varref);
  };
  // Add cases for rel/lrel?
  return false;
}

function ENUMERATE_CHECK_AND_ASSIGN1[3, enumerator, typ, varref, elm]{
   while(hasNext(enumerator)){
     elm = next(enumerator);
     if(subtype(typeOf(elm), typ)){
     	deref varref = elm;
        yield true;
     };
   }; 
   return false;      
}

function ENUMERATE_CHECK_AND_ASSIGN[3, typ, varref, ^val]{
  typeswitch(^val){
    case list:  ENUMERATE_CHECK_AND_ASSIGN1(init(create(ENUM_LIST, ^val)), typ, varref);
    case node:  ENUMERATE_CHECK_AND_ASSIGN1(init(create(ENUM_NODE, ^val)), typ, varref);
    case map:   ENUMERATE_CHECK_AND_ASSIGN1(init(create(ENUM_MAP, ^val)), typ, varref);
    case set:   ENUMERATE_CHECK_AND_ASSIGN1(init(create(ENUM_SET, ^val)), typ, varref);
    case tuple: ENUMERATE_CHECK_AND_ASSIGN1(init(create(ENUM_TUPLE, ^val)), typ, varref);
    default:    ENUMERATE_CHECK_AND_ASSIGN1(init(create(ENUM_LITERAL, ^val)), typ, varref);
  };
  // Add cases for rel/lrel?
  return false;
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
   plen = size_array(pats);
   slen = size_array(subjects);
   if(plen != slen){
      // println("MATCH_N: unequal length", plen, slen);
      return false;
   };
   p = 0;
   while(p < plen){
     // println("MATCH_N: init ", p);
     set_array pats[p] = init(get_array pats[p], get_array subjects[p]);
     p = p + 1;
   };
   
   while(true){
     p = 0;
     while(p < plen){
       // println("p = ", p);
       pat = get_array pats[p];
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

function MATCH_REIFIED_TYPE[2, pat, ^subject, nc, konstructor, symbol]{
    if(^subject is node){
       nc = get_name_and_children(^subject);
       konstructor = get_array nc[0];
       symbol = get_array nc[1];
       if(equal(konstructor, "type") && equal(symbol, pat)){
          return true;
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

function MATCH_ANONYMOUS_VAR[1, ^subject]{
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

function MATCH_TYPED_ANONYMOUS_VAR[2, typ, ^subject]{
   if(subtype(typeOf(^subject), typ)){
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
    if(equal(typ, typeOf(^subject))){
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
    if(equal(typ, typeOf(^subject))){
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
      ^literals = get_array pair[0];
      pats      = get_array pair[1];
      
     if(subset(^literals, ^subject)){
        subject1 = mset_subtract_set(mset(^subject), ^literals);
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
     	 println("At head", p);
         while(forward && hasNext(matcher)){
        	[success, remaining] = next(matcher);
            if(success){ 
               forward = true;
               current = remaining;
               if((p == patlen1) && (size_mset(current) == 0)) {
              	   yield true;
              	   println("Back from yield", p); 
               } else {
                 if(p < patlen1){
                   p = p + 1;
                   println("Move right to", p);
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
               println("Move left to", p);
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

function MATCH_PAT_IN_SET[2, pat, available, available1, gen, cpat, elm]{
    if(size_mset(available) == 0){
       return [ false, available ];
    }; 
    available1 = mset_copy(available);
    gen = init(create(ENUM_MSET, available1));
    while(hasNext(gen)){
        elm = next(gen);
        cpat = init(pat, elm);
        while(hasNext(cpat)){
           if(next(cpat)){
              yield [ true, mset_subtract_elm(available1, elm) ];
           };
        };
    };
    return [ false, available ];
} 

function MATCH_VAR_IN_SET[2, varref, available, available1, gen, elm]{
   if(size_mset(available) == 0){
       return [ false, available ];
   };
   available1 = mset_copy(available);
   gen = init(create(ENUM_MSET, available1));
   while(hasNext(gen)){
	     elm = next(gen);
	     deref varref = elm;
	     yield [ true, mset_subtract_elm(available1, elm) ];
   };
   return [ false, available ];
}

function MATCH_ANONYMOUS_VAR_IN_SET[1, available, available1, gen, elm]{
   if(size_set(available) == 0){
       return [ false, available ];
   };
   available1 = mset_copy(available);
   gen = init(create(ENUM_MSET, available1));
   while(hasNext(gen)){
        elm = next(gen);
        yield [ true, mset_subtract_elm(available1, elm) ];
   };
   return [ false, available ];
}

function MATCH_MULTIVAR_IN_SET[2, varref, available, available1, gen, subset]{
   available1 = mset_copy(available);
   gen = init(create(ENUM_SUBSETS, available1));
   while(hasNext(gen)){
	     subset = next(gen);
	     deref varref = set(subset);
	     yield [ true, mset_subtract_mset(available1, subset) ];
   };
   return [ false, available ];
}

function MATCH_ANONYMOUS_MULTIVAR_IN_SET[1, available, available1, gen, subset]{
   available1 = mset_copy(available);
   gen = init(create(ENUM_SUBSETS, available1));
   while(hasNext(gen)){
	     subset = next(gen);
	     yield [ true, mset_subtract_mset(available1, subset) ];
   };
   return [ false, available ];
}

function MATCH_TYPED_MULTIVAR_IN_SET[3, typ, varref, available, available1, gen, subset]{
    println("MATCH_TYPED_MULTIVAR_IN_SET", typ, varref, available);
    available1 = mset_copy(available);
    if(equal(typ, typeOf(available))){
	   gen = init(create(ENUM_SUBSETS, available1));
	   while(hasNext(gen)){
	         subset = next(gen);
	   		 deref varref = set(subset);
	   		  println("MATCH_TYPED_MULTIVAR_IN_SET, assigns", subset);
	         yield [ true, mset_subtract_mset(available1, subset) ];
	   };
    };
    return [ false, available ];
}

function MATCH_TYPED_ANONYMOUS_MULTIVAR_IN_SET[2, typ, available, available1, gen, subset]{
    println("MATCH_TYPED_MULTIVAR_IN_SET", typ, available);
    available1 = mset_copy(available);
    if(equal(typ, typeOf(available))){
	   gen = init(create(ENUM_SUBSETS, available1));
       while(hasNext(gen)){
             subset = next(gen);
	          yield [ true, mset_subtract_mset(available1, subset) ];
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
    println("ENUM_SUBSETS for:", set);
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
              sub = mset_add_elm(sub, get_list lst[elIndex]);
           };
           elIndex = elIndex + 1;
           j = j / 2;
        };
        println("ENUM_SUBSETS returns:", sub, "i =", i, "last one = ", i == 0);
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
   last = size_list(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_SET[2, pat, ^set, ^lst, last, i]{
   // println("MATCH_AND_DESCENT_SET", pat, ^set);
   ^lst = set2list(^set);
   last = size_list(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^lst[i]);
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
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_TUPLE[2, pat, ^tup, last, i]{
   last = size_tuple(^tup) - 1;
   i = 0;
   while(i < last){
      DO_ALL(pat, get_tuple ^tup[i]);
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
  typeswitch(^val){
    case list:  VISIT(init(create(MATCH_AND_DESCENT_LIST, pat, ^val)));
    case node:  VISIT(init(create(MATCH_AND_DESCENT_NODE, pat, ^val)));
    case map:   VISIT(init(create(MATCH_AND_DESCENT_MAP, pat, ^val)));
    case set:   VISIT(init(create(MATCH_AND_DESCENT_SET, pat, ^val)));
    case tuple: VISIT(init(create(MATCH_AND_DESCENT_TUPLE, pat, ^val)));
    default:    return false;
  };  
  // Add cases for rel/lrel?
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