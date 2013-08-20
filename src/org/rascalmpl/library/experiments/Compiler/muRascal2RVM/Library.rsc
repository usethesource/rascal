module experiments::Compiler::muRascal2RVM::Library

import Prelude;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::muRascal2RVM::mu2rvm;

list[MuFunction] library = [

// Boolean expressions

muFunction("TRUE", 1, 0, 0, 
	[
		muReturn(muCon(true))
	]
),
       
muFunction("FALSE", 1, 0, 0, 
	[
		muReturn(muCon(false))
	]
),

muFunction("AND_U_U", 1, 2, 2, 
	[				
		muReturn(muCallPrim("and_bool_bool", [muLoc("lhs", 0), muLoc("rhs", 1)]))
	]
),
            
muFunction("AND_M_U", 1, 2, 2, 
	[					
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("lhs", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muIfelse(muNext(muLoc("lhs", 0)),
			     [ muIfelse(muLoc("rhs", 1),
			                [ muYield(muNext(muLoc("rhs", 1))) ],
			                [ ])
			     ],
			     [])              
			]),
		muReturn(muCon(false))
	]
),

muFunction("AND_U_M", 1, 2, 2, 
	[					 
		muIfelse(muLoc("lhs", 0),
			[ 
			  muAssignLoc("rhs",  1, muInit(muCreate(muLoc("rhs", 1)))),
			  muWhile(muHasNext(muLoc("rhs", 1)),
					  [ muYield(muNext(muLoc("rhs", 1))) ])
		    ],
			[]),
		muReturn(muCon(false))
	]
),

muFunction("AND_M_M", 1, 2, 2, 
	[					
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("lhs", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muIfelse(muNext(muLoc("lhs", 0)),
			     [ muAssign("rhs", 1, 1, muInit(muCreate(muLoc("lrs", 1)))),
			       muWhile(muHasNext(muLoc("lrs", 1)),
						   [ muYield(muNext(muLoc("lrs", 1))) ])
				],
				[])
			]),
		muReturn(muCon(false))
	]
),

muFunction("AND_U_U", 1, 2, 2, 
	[				
		muReturn(muCallPrim("and_bool_bool", [muLoc("lhs", 0), muLoc("lrs", 1)]))
	]
),
            
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
	    muReturn(muCallPrim("equals_num_num", [muVar("pat", 1, 0), muVar("subject", 1, 1)]))	
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
		//muAssignLoc("patlen", 2, muCall("size", muLoc("pats", 0))),
		//muAssignLoc("sublen", 3, muCall("size", muLoc("subject", 1))),
		muAssignLoc("p", 4, muCon(0)),
		muAssignLoc("cursor", 5, muCon(0)),
		muAssignLoc("forward", 6, muCon(true)),
		muAssignLoc("matcher", 7, muInit(muCallPrim("subscript_list_int", [muLoc("pats", 0), muCon(0)]), [muLoc("subject", 1), muLoc("cursor", 5)])),
		muAssignLoc("matchers", 7, muCallPrim("make_list", [muCon(0)]))
		
		
		
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
];

void run(){
  muP = muModule("Library", [], library, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
}
