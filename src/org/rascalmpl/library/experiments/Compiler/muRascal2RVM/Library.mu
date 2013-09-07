module Library


/*

function main[1,args] { return next(init(create(TRUE))); }
 
function TRUE[0,] { return true; }   // should be true
 
function FALSE[0,] { return false; }


function AND_U_U[2,lhs,rhs]{
  return prim("and_bool_bool", lhs, rhs);
}

function AND_M_U[2,lhs,rhs,clhs]{
   clhs = init(create(lhs));
   while(hasNext(clhs)){
     if(next(clhs)){
        if(rhs){
           yield true;
        };
     };          
   };
   return 0;
}

function AND_U_M[2,lhs,rhs,crhs]{
   if(lhs){
      crhs = init(create(rhs));
      while(hasNext(crhs)){
        if(next(crhs)){
           yield true;
        } else {
          return false;
        };
      };         
   };
   return false;
}

function AND_M_M[2,lhs,rhs,clhs,crhs]{
   clhs = init(create(lhs));
   while(hasNext(clhs)){
     if(next(clhs)){
        crhs = init(create(rhs));
        while(hasNext(crhs)){
          if(next(crhs)){
             yield true;
          } else {
            return false;
          };
        };       
     };          
   };
   return false;
}

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

// ***** Generators for all types *****

function ENUM_LITERAL[2, ^lit]{
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

function do_enum[2, enum, pat, cpat, elm]{
   while(hasNext(enum)){
     elm = next(enum);
     cpat = init(pat, elm);
     while(hasNext(cpat)){
       if(next(cpat)){
          if(hasNext(enum)){
             yield true;
          } else {
             return false;
          };
       };
     };
   };      
}
        
function ENUMERATE_AND_MATCH[2,  pat, ^val]{
  if(^val is list){
     do_enum(init(create(ENUM_LIST, ^val)), pat);
  } else {
    if(^val is node){
      do_enum(init(create(ENUM_NODE, ^val)), pat);
    } else {
      if(^val is map){
        do_enum(init(create(ENUM_MAP, ^val)), pat);
      } else {
        if(^val is set){
           do_enum(init(create(ENUM_SET, ^val)), pat);
      };
    };
  };  
  // Add cases for set/rel/tuple/...
  return ^val;
}

function RANGE[3, pat, ^first, ^end, i, n, cpat]{
   i = mint(^first);
   n = mint(^end);
   if(i < n){
      while(i < n){
        cpat = init(pat, rint(i));
        while(hasNext(cpat)){
            if(next(cpat)){
               yield true;
            };
        }; 
        i = i + 1;
      };
      return false;
   } else {
      while(i > n){
        cpat = init(pat, rint(i));
        while(hasNext(cpat)){
            if(next(cpat)){
               yield true;
            };
        }; 
        i = i - 1;
      };
      return false;
   };
}

function RANGE_STEP[4, pat, ^first, ^second, ^end, i, n, step, cpat]{
   i = mint(^first);
   n = mint(^end);
   if(i < n){
      step = mint(^second) - i;
      if(step <= 0){
         return false;
      };   
      while(i < n){
        cpat = init(pat, rint(i));
        while(hasNext(cpat)){
            if(next(cpat)){
               yield true;
            };
        }; 
        i = i + step;
      };
      return false;
   } else {
      step = mint(^second) - i;
      if(step >= 0){
         return false;
      };   
      while(i > n){
        cpat = init(pat, rint(i));
        while(hasNext(cpat)){
            if(next(cpat)){
               yield true;
            };
        }; 
        i = i + step;
      };
      return false;
   };
}

// ***** Pattern matching *****

function MATCH[2,pat,^subject,cpat]{
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
   println("MATCH_N", pats, subjects);
   plen = size(pats);
   slen = size(subjects);
   if(plen != slen){
      println("MATCH_N: unequal length", plen, slen);
      return false;
   };
   p = 0;
   while(p < plen){
     println("MATCH_N: init ", p);
     set pats[p] = init(get pats[p], get subjects[p]);
     p = p + 1;
   };
   
   while(true){
     p = 0;
     while(p < plen){
       println("p = ", p);
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
     println("MATCH_N yields true");
     yield true; 
   };
}

function MATCH_CALL_OR_TREE[2, pats, ^subject, cpats]{
    println("MATCH_CALL_OR_TREE", pats, ^subject);
    if(^subject is node){
      cpats = init(create(MATCH_N, pats, get_name_and_children(^subject)));
      while(hasNext(cpats)){
        println("MATCH_CALL_OR_TREE", "hasNext=true");
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
    println("MATCH_TUPLE", pats, ^subject);
    if(^subject is tuple){
      cpats = init(create(MATCH_N, pats, get_tuple_elements(^subject)));
      while(hasNext(cpats)){
        println("MATCH_TUPLE", "hasNext=true");
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
  if(equal(typeOf(pat), typeOf(^subject))){
      res = equal(pat, ^subject);
      println("MATCH_LITERAL", pat, ^subject, res);
     return res;
  };
  return false;
}

function MATCH_VAR[2, varref, ^subject]{
   deref varref = ^subject;
   return true;
}

function MATCH_TYPED_VAR[3, typ, varref, ^subject]{
   if(subtype(typeOf(^subject), typ)){
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

function MATCH_AS_TYPE[3, typ, pat, ^subject, cpat]{
   if(equal(typ, typeOf(^subject))){
     cpat = init(pat, ^subject);
     while(hasNext(cpat)){
       yield true;
     };
   };  
   return false;
}
/*
function MATCH_DESCENDANT[2, pat, ^subject, gen, cpat]{
   gen = init(create(fun GEN_VALUE, ^subject));
   while(hasNext(gen)){
       cpat = init(pat, ^subject);
       while(hasNext(cpat)){
          yield true;
       };
   };
   return false;
}
*/
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
   deref varref = get ^subject[start];
   return [true, start + 1];
}

function MATCH_MULTIVAR_IN_LIST[4, varref, ^subject, start, available, len]{
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
