 
 module Library

/*
function main[1,1,args] { return next(init(create(TRUE))); }

function TRUE[0,0,] { return true; }   // should be true
 
function FALSE[0,0,] { return false; }


function AND_U_U[2,2,lhs,rhs]{
  return prim("and_bool_bool", lhs, rhs);
}

function AND_M_U[2,2,lhs,rhs,clhs]{
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

function AND_U_M[2,2,lhs,rhs,crhs]{
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

function AND_M_M[2,2,lhs,rhs,clhs,crhs]{
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

function ONE[1,1,arg, carg]{
   carg = init(create(arg));
   return next(arg);
}

function ALL[1,1,arg,carg]{
   carg = init(create(arg));
   while(hasNext(carg)){
        yield next(carg);
   };
   return false;
}        
*/

// ***** Generators for all types *****

function GEN_LIST[1, 1, _lst, last, i]{
   last = muprim("subtraction_mint_mint", prim("size_list", _lst), 1);
   i = 0;
   while(muprim("less_mint_mint", i, last)){
      yield muprim("subscript_list_mint", _lst, i);
      i = muprim("addition_mint_mint", i, 1);
   };
   return muprim("subscript_list_mint", _lst, last);
}

function GEN_NODE[1, 1, _nd, last, i, lst]{
   lst = muprim("get_name_and_children", _nd);
   last = muprim("subtraction_mint_mint", muprim("size_array", lst), 2);
   i = 1;  // skip name
   while(muprim("less_mint_mint", i, last)){
      yield muprim("subscript_array_mint", lst, i);
      i = muprim("addition_mint_mint", i, 1);
   };
   return muprim("subscript_array_mint", lst, last);
}

function GEN_VALUE[1, 1, _val, co, res]{

  if(muprim("is_list", _val)){
     yield _val;
     co = init(create(fun GEN_LIST, _val));
     while(hasNext(co)){
        res = next(co);
        if(hasNext(co)){
           yield res;
        } else {
          return res;
        };
     };
  };
  if(muprim("is_node", _val)){
     yield _val;
     co = init(create(fun GEN_NODE, _val));
     while(hasNext(co)){
        res = next(co);
        if(hasNext(co)){
           yield res;
        } else {
          return res;
        };
     };
  };
  // Add cases for set/rel/tuple/map/...
  return _val;
}

// ***** Pattern matching *****

function MATCH[1,2,pat,_subject,cpat]{
   cpat = init(pat, _subject);
   while(hasNext(cpat)){
      if(next(cpat)){
         yield true;
      } else {
        return false;
      };
   };
   return false;
}

function MATCH_N[1, 2, pats, subjects, plen, slen, p, pat]{
   prim("println", ["MATCH_N", pats, subjects]);
   plen = muprim("size_array", pats);
   slen = muprim("size_array", subjects);
   if(muprim("not_equals_mint_mint", plen, slen)){
      prim("println", ["MATCH_N: unequal length", plen, slen]);
      return false;
   };
   p = 0;
   while(muprim("less_mint_mint", p, plen)){
     prim("println",  ["MATCH_N: init ", p]);
     set pats[p] = init(get pats[p], get subjects[p]);
     p = muprim("addition_mint_mint", p, 1);
   };
   
   while(true){
     p = 0;
     while(muprim("less_mint_mint", p, plen)){
       prim("println", ["p = ", p]);
       pat = get pats[p];
       if(hasNext(pat)){
          if(next(pat)){
              p = muprim("addition_mint_mint", p, 1);
           } else {
              return false;
           };   
       } else {
         return false;
       };
     };
     prim("println", ["MATCH_N yields true"]);
     yield true; 
   };
}

function MATCH_CALL_OR_TREE[1, 2, pats, _subject, cpats]{
    prim("println", ["MATCH_CALL_OR_TREE", pats, _subject]);
    if(muprim("is_node", _subject)){
      cpats = init(create(fun MATCH_N, pats, muprim("get_name_and_children", _subject)));
      while(hasNext(cpats)){
        prim("println", ["MATCH_CALL_OR_TREE", "hasNext=true"]);
        if(next(cpats)){
           yield true;
        } else {
           return false;
        };
      };
    };
    return false;
}

function MATCH_TUPLE[1, 2, pats, _subject, cpats]{
    prim("println", ["MATCH_TUPLE", pats, _subject]);
    if(muprim("is_tuple", _subject)){
      cpats = init(create(fun MATCH_N, pats, muprim("get_tuple_elements", _subject)));
      while(hasNext(cpats)){
        prim("println", ["MATCH_TUPLE", "hasNext=true"]);
        if(next(cpats)){
           yield true;
        } else {
           return false;
        };
      };
    };
    return false;
}

function MATCH_INT[1,2,pat, _subject, res]{
  if(prim("is_int", _subject)){
     res = muprim("equals_rint_rint", pat, _subject);
     prim("println", ["MATCH_INT", pat, _subject, res]);
     return res;
  };
  return false;
}

function MATCH_STR[1,2,pat, _subject, res]{
   if(muprim("is_str", _subject)){
     res = muprim("equals_str_str", pat, _subject);
     prim("println", ["MATCH_STR", pat, _subject, res]);
     return res;
   };
   return false;  
}

function MATCH_VAR[1, 2, varref, _subject]{
   deref varref = _subject;
   return true;
}

function MATCH_TYPED_VAR[1, 3, typ, varref, _subject]{
   if(muprim("equals_type_type", typ, prim("typeOf", _subject))){
     deref varref = _subject;
     return true;
   };
   return false;  
}

function MATCH_VAR_BECOMES[1, 3, varref, pat, _subject, cpat]{
   cpat = init(pat, _subject);
   while(hasNext(cpat)){
     deref varref = _subject;
     yield true;
   };
   return false;
}

function MATCH_TYPED_VAR_BECOMES[1, 4, typ, varref, pat, _subject, cpat]{
   if(muprim("equals_type_type", typ, muprim("typeOf", _subject))){
     cpat = init(pat, _subject);
     while(hasNext(cpat)){
       deref varref = _subject;
       yield true;
     };
   };  
   return false;
}

function MATCH_AS_TYPE[1, 3, typ, pat, _subject, cpat]{
   if(muprim("equals_type_type", typ, muprim("typeOf", _subject))){
     cpat = init(pat, _subject);
     while(hasNext(cpat)){
       yield true;
     };
   };  
   return false;
}

function MATCH_DESCENDANT[1, 2, pat, _subject, gen, cpat]{
   gen = init(create(fun GEN_VALUE, _subject));
   while(hasNext(gen)){
       cpat = init(pat, _subject);
       while(hasNext(cpat)){
          yield true;
       };
   };
   return false;
}

// ***** List matching *****

function MATCH_LIST[1, 2, pats,   						// A list of coroutines to match list elements
						  _subject,						// The subject list
						  patlen,						// Length of pattern list
						  patlen1,						// patlen - 1
						  sublen,						// Length of subject list
						  p,							// Cursor in patterns
						  cursor,						// Cursor in subject
						  forward,
						  matcher,						// Currently active pattern matcher
						  matchers,						// List of currently active pattern matchers
						  success,						// Success flag of last macth
						  nextCursor					// Cursor movement of last successfull match
					]{

     patlen   = muprim("size_array", pats);
     patlen1 =  muprim("subtraction_mint_mint", patlen, 1);
     sublen   = muprim("size_list", _subject);
     p        = 0; 
     cursor   = 0;
     forward  = true;
     matcher  = init(get pats[p], _subject, cursor, sublen);
     matchers = muprim("make_array_of_size", patlen);
     set matchers[0] = matcher;
     
     while(true){
     	// Move forward
     	 forward = hasNext(matcher);
     	 // prim("println", ["At head", p, cursor, forward]);
         while(muprim("and_mbool_mbool", forward, hasNext(matcher))){
        	[success, nextCursor] = next(matcher);
            if(success){ 
               forward = true;
               cursor = nextCursor;
               // prim("println", ["SUCCESS", p, cursor]);
               if(muprim("and_mbool_mbool",
                       muprim("equals_mint_mint", p, patlen1),
                       muprim("equals_mint_mint", cursor, sublen))) {
                   // prim("println", ["*** YIELD", p, cursor]);
              	   yield true;
              	   // prim("println", ["Back from yield", p, cursor]); 
               } else {
                 if(muprim("less_mint_mint", p, patlen1)){
                   p = muprim("addition_mint_mint", p, 1);
                   // prim("println", ["Forward", p, cursor]);
                   matcher  = init(get pats[p], _subject, cursor,  muprim("subtraction_mint_mint", sublen, cursor));
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
           if(muprim("greater_mint_mint", p, 0)){
               p        = muprim("subtraction_mint_mint", p, 1);
               // prim("println", ["Previous", p, cursor]);
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

function MATCH_PAT_IN_LIST[1, 4, pat, _subject, start, available, cpat]{
    if(muprim("less_equal_mint_mint", available, 0)){
       return [false, start];
    };   
    cpat = init(pat, muprim("subscript_list_mint", _subject, start));
    
    while(hasNext(cpat)){
       if(next(cpat)){
          return [true, muprim("addition_mint_mint", start, 1)];
       };   
    };
    return [false, start];
} 

function MATCH_VAR_IN_LIST[1, 4, varref, _subject, start, available]{
   if(muprim("less_equal_mint_mint", available, 0)){
       return [false, start];
   }; 
   deref varref = muprim("subscript_list_mint", _subject, start);
   return [true, muprim("addition_mint_mint", start, 1)];
}

function MATCH_MULTIVAR_IN_LIST[1, 4, varref, _subject, start, available, len]{
    len = 0;
    while(muprim("less_equal_mint_mint", len, available)){
        deref varref = muprim("sublist_list_mint_mint", _subject, start, len);
        // prim("println", ["MATCH_MULTIVAR_IN_LIST", prim("addition_mint_mint", start, len)]);
        yield [true, muprim("addition_mint_mint", start, len)];
        len = muprim("addition_mint_mint", len, 1);
     };
     return [false, start];
}

function MATCH_TYPED_MULTIVAR_IN_LIST[1, 5, typ, varref, _subject, start, available, len]{
    if(muprim("equals_type_type", typ, muprim("typeOf", _subject))){
       len = 0;
       while(muprim("less_equal_mint_mint", len, available)){
          deref varref = muprim("sublist_list_mint_mint", _subject, start, len);
          // prim("println", ["MATCH_MULTIVAR_IN_LIST", prim("addition_mint_mint", start, len)]);
          yield [true, muprim("addition_mint_mint", start, len)];
          len = muprim("addition_mint_mint", len, 1);
       };
     };
     return [false, start];
}

