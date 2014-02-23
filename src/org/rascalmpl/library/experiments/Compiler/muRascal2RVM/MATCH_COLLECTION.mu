/*
 * Experiment to unify list and set matching into "collection matching".
 * The core algorithm is MATCH_COLLECTION, it has as parameters:
 * - a list of patterns "pats"
 * - a function "accept" that returns true when we have achieved a complete match of the subject
 * - the subject "iSubject" itself
 * - a value "progress" that determines the progress of the match.
 */

coroutine MATCH_COLLECTION[4, 
     pats,		// Coroutines to match collection elements
     accept,	// Function that accepts a complete match
	 iSubject,	// The subject (a collection like list or set)
	 progress,	// progress of the match

	 patlen,	// Length of pattern array
	 p,			// Cursor in patterns
	 matchers	// Currently active pattern matchers
	]{
   
     patlen   = size_array(pats);
    
     if(patlen == 0){
        if(accept(iSubject, progress)){
           return;
        } else {
          exhaust;
        };
     };
     
     p        = 0; 
     matchers = make_array(patlen);
     put_array(matchers, p, init(get_array(pats, p), iSubject, ref progress));
    
     while(true){
           while(next(get_array(matchers, p))) {   // Move forward
                 if((p == patlen - 1) && accept(iSubject, progress)) {
                    yield; 
                 } else {
                   if(p < patlen - 1){
                      p = p + 1;
                      put_array(matchers, p, init(get_array(pats, p), iSubject, ref progress));
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

/*
 * List matching creates a specific instance of MATCH_COLLECTION
 */

coroutine MATCH_LIST[2, pats, iSubject, cpat]{
   guard iSubject is list;
   
   cpat = init(MATCH_COLLECTION(pats, accept_list_match, iSubject, 0));  	// how to pass this function parameter?
   while(next(cpat)){
         yield;
   };
}

/*
 * A list match is acceptable when the cursor points at the end of the list
 */
function accept_list_match[2, iSubject, cursor]{
   return (size(iSubject) == cursor);
}

/*
 * Note: in the current implementation all patterns in list pattern get an extra parameter: "available"
 * that gives the number of available list elements. In the new set up list patterns have to compute this themselves.
 */

// A literal in a list (OLD)
coroutine MATCH_COLLECTION[4, pat, iSubject, rNext, available, start, elm]{
	guard available > 0;
	start = deref rNext;
	
	elm =  get_list(iSubject, start);
    if(equal(pat, elm)){
       return(start + 1);
    };
}

// A literal in a list (NEW)
coroutine MATCH_LITERAL_IN_LIST[3, pat, iSubject, rNext, start, elm]{
    start = deref rNext;					// <==
	guard start < size_list(iSubject);		// <==
	
	elm =  get_list(iSubject, start);
    if(equal(pat, elm)){
       return(start + 1);
    };
}

/*
 * Set matching creates a specific instance of MATCH_COLLECTION
 * Note: the current implementation passes a pair <iLiterals, pats>
 */
coroutine MATCH_SET[3, iLiterals, pats, iSubject, cpat, remaining]{
      guard iSubject is set;
      
      if(subset(iLiterals, iSubject)) {
         iSubject = prim("set_subtract_set", iSubject, iLiterals);
         remaining = mset(iSubject);
         cpat = init(MATCH_COLLECTION(pats, accept_set_match, iSubject, remaining);	// how to pass this function parameter?
         while(next(cpat)){
               yield;
         };
      };
}

/*
 * A set match is acceptable when the set of remaining elements is empty
 */
function accept_set_match[2, iSubject, remaining]{
   return (size_mset(remaining) == 0);
}