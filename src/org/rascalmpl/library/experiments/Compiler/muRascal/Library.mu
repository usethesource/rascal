module library

function TRUE[0,0,] { return f(1) }   // should be true

function FALSE[0,0,] { return f(0) }

function AND_U_U[2,2,lhs:0,rhs:1]{
  return prim("and_bool_bool", lhs, rhs)
}

function AND_M_U[2,2,lhs:0,rhs:1,clhs:2]{
   clhs = init(create(lhs));
   while(hasNext(clhs)){
     if(next(clhs)){
        if(rhs){
           yield 1
        }
     }          
   };
   return 0
}

function AND_U_M[2,2,lhs:0,rhs:1,crhs:2]{
   if(lhs){
      crhs = init(create(rhs));
      while(hasNext(crhs)){
        if(next(crhs)){
           yield 1
        } else {
          return 0
        }
      }          
   };
   return 0
}

function AND_M_M[2,2,lhs:0,rhs:1,clhs:2,crhs:3]{
   clhs = init(create(lhs));
   while(hasNext(clhs)){
     if(next(clhs)){
        crhs = init(create(rhs));
        while(hasNext(crhs)){
          if(next(crhs)){
             yield 1
          } else {
            return 0
          }
        }       
     }          
   };
   return 0
}

/*
            
muFunction("ONE", 1, 1, 1, 
	[
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("arg", 0)))),
		muReturn(muNext(muLoc("arg", 0)))
	]
),
					
muFunction("ALL", 1, 1, 1, 
	[					
		muAssignLoc("arg", 0, muInit(muCreate(muLoc("arg", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muYield(muNext(muLoc("lhs", 0)))
			]),
	    muReturn(muCon(false))		
	]
),

// Pattern matching

muFunction("MATCH", 1, 2, 1, 
	[					
		muInit(muVar("pat", 1, 0), [muVar("subject", 1, 1)]),
		muWhile(muHasNext(muVar("pat", 1, 0)),
			[ muYield(muNext(muVar("pat", 1, 0)))
			]),
	    muReturn(muCon(false))		
	]
),

muFunction("MATCH_INT", 1, 2, 1, 
	[					
	    muReturn(muCallPrim("equals_num_num", muVar("pat", 1, 0), muVar("subject", 1, 1)))	
	]
),

muFunction("MATCH_LIST", 1, 2, 1, 
	[					
		muInit(muVar("pat", 1, 0), [muVar("subject", 1, 1)]),
		// list matching
	    muReturn(muCon(true))		
	]
),

muFunction("MATCH_LIST", 1, 2, 7,
	[
		muAssignLoc("patlen", 2, muCall("size", muLoc("pats", 0))),
		muAssignLoc("sublen", 3, muCall("size", muLoc("subject", 1))),
		muAssignLoc("p", 4, muCon(0)),
		muAssignLoc("cursor", 5, muCon(0)),
		muAssignLoc("forward", 6, muCon(true)),
		assignLoc("matcher", 7, muInit(muCallPrim("subscript_list", [muLoc("pats", 0), muCon(0)]), [muLoc("subject", 1), muLoc("cursor", 5)])),
		assignLoc("matchers", 7, callPrim("make_list", [muCon(0)]))
		
		
		
	]
)
/*
coroutine MATCH_LIST (pats:0) (subject:1) {
     int patlen:2 = size(pats);
     int sublen:3 = size(subject);
     int p:4 = 0; 
     int cursor:5 = 0;
     bool forward:6 = true;
     matcher:7 = pats[0].init(subject, cursor);
     matchers:8 = [];
     while(true){
        while(matcher.hasMore()){
        	<success, nextCursor> = matcher.next(forward);
            if(success){
               forward = true;
               cursor = nextCursor;
               matchers = [matcher] + matchers;
               p += 1;
               if(p == patlen && cursor == sublen)
              	   yield true; 
               else
                   matcher = pats[p].init(subject, cursor)
            }
         }
         if(p > 0){
               p -= 1;
               matcher = head(matchers);
               matchers = tail(matchers);
               forward = false;
         } else {
               return false;
         }
     }
}
     
coroutine MATCH_PAT_IN_LIST (pat) (subject, start){
    pat.init(subject[start]);
    while(pat.hasNext())
       if(pat.next()){
          return <true, start + 1>;
    }
    return <false, start>;
 } 
 
 coroutine MATCH_LIST_VAR (VAR) (subject, start){
    int pos = start;
    while(pos < size(subject)){
        VAR = subject[start .. pos];
        yield <true, pos>;
     }
     return <false, start>;
 }
*/