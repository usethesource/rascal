module Library


function TRUE[0,0,] { return f(true); }   // should be true
 
function FALSE[0,0,] { return f(false); }


function AND_U_U[2,2,lhs:0,rhs:1]{
  return prim("and_bool_bool", lhs, rhs);
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
   return 0;
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
   return 0;
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
   return 0;
}

function ONE[1,1,arg:0, carg:1]{
   carg = init(creat(arg));
   return next(arg);
}

function ALL[1,1,arg:0,carg:1]{
   carg = init(creat(arg));
   while(hasNext(carg)){
        yield next(carg)
   };
   return false;
}        

// Pattern matching

function MATCH[1,3,pat:0,subject:1,cpat:2]{
   cpat = init(create(pat), subject);
   while(hasNext(cpat)){
      if(next(cpat)){
         yield true
      } else {
        return false
      }
   };
   return false;
}

function MATCH_INT[1,2,pat:0,subject:1]{
   return prim("equals_num_num", pat, subject);
}

function make_matcher[1,4,pats:0,n:1,subject:2,cursor:4]{
   return init(create(prim("subscript_list_int", pats, n)), subject, cursor);
}

function MATCH_LIST[1, 2, pat:0,subject:1,patlen:2,sublen:3,
						  p:4,cursor:5,forward:6,matcher:7,
						  matchers:8,pats:9,success:10,nextCursor:11]{

     patlen = prim("size_list", pats);
     sublen = prim("size_list", subject);
     p = 0; 
     cursor = 0;
     forward = true;
     matcher = make_matcher(pats, subject, cursor);
     matchers = prim("make_list", 0);
     while(true){
         while(hasMore(matcher)){
        	<success, nextCursor> = next(matcher,forward);
            if(success){
               forward = true;
               cursor = nextCursor;
               matchers = prim("addition_elm_list", matcher, matchers);
               p = prim("addition_num_num", p, 1);
               if(prim("and_bool_bool",
                       prim("equals_num", p, patlen),
                       prim("equals_num", cursor, sublen))) {
              	   yield true 
               } else {
                   matcher = make_matcher(pats, p, subject, cursor)
               }    
            }
         };
         if(prim("greater_num_num", p, 0)){
               p = prim("subtraction_num_num", p, 1);
               matcher = prim("head_list", matchers);
               matchers = prim("tail_list", matchers);
               forward = false
         } else {
               return false
         }
     };
}

function MATCH_PAT_IN_LIST[1, 3, pat:0, subject:1, start:2,cpat:3]{
    cpat = init(create(pat), subject, start);
    /*
    while(hasNext(cpat)){
       if(next(cpat)){
          return <true, prim("addition_num_num", start, 1)>
       }   
    };
    return <false, start>
  */  
} 
 
 /*
 coroutine MATCH_LIST_VAR (VAR) (subject, start){
    int pos = start;
    while(pos < size(subject)){
        VAR = subject[start .. pos];
        yield <true, pos>;
     }
     return <false, start>;
 }
*/