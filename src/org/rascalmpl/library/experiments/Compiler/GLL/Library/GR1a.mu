module GR1

declares "cons(adt(\"S\",[]),\"S_\",[label(\"child1\",adt(\"A_LIT\",[])),label(\"child2\",adt(\"S\",[]))])"
declares "cons(adt(\"S\",[]),\"S_\",[label(\"child1\",adt(\"A\",[])),label(\"child2\",adt(\"S\",[])),label(\"child3\",adt(\"D_LIT\",[]))])"
declares "cons(adt(\"S\",[]),\"S_\",[label(\"child\",adt(\"EPSILON\",[]))])"

declares "cons(adt(\"A\",[]),\"A_\",[label(\"child\",adt(\"A_LIT\",[]))])"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"
declares "cons(adt(\"EPSILON\",[]),\"EPSILON_\",[])"

coroutine S[3,rI,rTree,iSubject,
              s,a_lit,a,d,epsilon,tree0,tree1,tree2,tree3,recognized,index0,index] {
    
    // S = a S
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    a_lit = create(LIT,"a",rI,ref tree1,iSubject);
	    s = create(S,rI,ref tree2,iSubject);
	    while(all( multi(a_lit), multi(s) )) {
	        println("S = a S");
	        yield(deref rI,cons S_(tree1,tree2));
	    };
	    true; // muRascal detail: dummy expression
    };
	
	// S = S A d
	if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	
	    // Dealing with left recursion
	    s = create(S_PRIME,rI,ref tree1,iSubject);
	    while(all( multi(s) )) {
	        
	        index0 = deref rI;
	        tree0 = tree1;
	        recognized = true;
	        while(recognized) {
	            recognized = false;
	            index = deref rI;
	            a = create(A,ref index,ref tree2,iSubject);
	            d = create(LIT,"d",ref index,ref tree3,iSubject);
	            while(all(multi(a), multi(d))) {
	                println("S = S A d");
	                recognized = true;
	                deref rI = index;
	                tree0 = cons S_(tree0,tree2,tree3);
	                yield(deref rI,tree0);
	            };
	        };
	        deref rI = index0;
	        
	    };
	    true; // muRascal detail: dummy expression
	};
	
	// S = epsilon
	epsilon = create(EPSILON,rI,ref tree1,iSubject);
	while(all( multi(epsilon) )) {
	   println("S = epsilon");
	   yield(deref rI,cons S_(tree1));
	};

}

coroutine S_PRIME[3,rI,rTree,iSubject,
              s,a_lit,a,d,epsilon,tree1,tree2,tree3] {
    
    // S = a S
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    a_lit = create(LIT,"a",rI,ref tree1,iSubject);
	    s = create(S,rI,ref tree2,iSubject);
	    while(all( multi(a_lit), multi(s) )) {
	        println("S = a S");
	        yield(deref rI,cons S_(tree1,tree2));
	    };
	    true; // muRascal detail: dummy expression
    };
	
	// S = epsilon
	epsilon = create(EPSILON,rI,ref tree1,iSubject);
	while(all( multi(epsilon) )) {
	   println("S = epsilon");
	   yield(deref rI,cons S_(tree1));
	};

}

coroutine A[3,rI,rTree,iSubject,
              a,tree] {
    a = create(LIT,"a",rI,ref tree,iSubject);
    while(all(multi(a))) {
        println("A = a");
        yield(deref rI,cons A_(tree));
    };
}

coroutine LIT[4,iLit,rI,rTree,iSubject,
                index,lit] {
    guard deref rI < size(iSubject);
    index = deref rI;
    lit = muprim("subscript_str_mint",iSubject, deref rI);
    if(equal(lit,iLit)) {
        println(iLit,"; next: ", 1 + deref rI);
        yield(1 + deref rI,cons LIT_(iLit));
    };
    // Un-do upon a failure
    deref rI = index;
}

coroutine EPSILON[3,rI,rTree,iSubject] {
    return(deref rI,cons EPSILON_());
}

function MAIN[1,args,
              iSubject,s,index,tree] {
    iSubject = "aadad";
    index = 0;
	s = create(S, ref index, ref tree, iSubject);
	while(all(multi(s))) {
	    if(index == size(iSubject)) {
	        println("Recognized: ", tree);
	    };
	};
    return true;
}