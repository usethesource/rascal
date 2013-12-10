module GR4a

declares "cons(adt(\"E\",[]),\"E_\",[label(\"child1\",adt(\"E\",[])),label(\"child2\",adt(\"LIT\",[])),label(\"child3\",adt(\"E\",[]))])"
declares "cons(adt(\"E\",[]),\"E_\",[label(\"child\",adt(\"LIT\",[]))])"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"

coroutine E[5,iSubject,rI,rTree,rCont,start_id,
              e_lhs,e_rhs,plus_lit,minus_lit,a_lit,
              tree,tree1,tree2,tree3,
              start,
              recur,
              cont] {
    
    start = muprim("equal",start_id,"start");
    recur = muprim("equal",start_id,"E"); // Defines the recursion start
    
    // E = a
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    a_lit = create(LIT,"a",iSubject,rI,ref tree);
	    while(all( multi(a_lit) )) {
	        if(recur) {
	            yield(deref rI,cons E_(tree),false);
	        } else {
	            yield(deref rI,cons E_(tree),deref rCont);
	        };
	    };
	    true; // muRascal detail: dummy expression
    };
    
    if(recur) { // Do not expand with left recursive alternatives if not explicitly set
        if(muprim("equivalent_mbool_mbool",deref rCont,false)) {
            exhaust;
        };
    };
    
    // E = E +/- E
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
        if(start) {
            e_lhs = create(E,iSubject,rI,ref tree1,rCont,"E");
        } else {
            e_lhs = create(E,iSubject,rI,ref tree1,rCont,start_id); // Pass 'id' to the left nonterminal to mark a recursion start
        };
	    plus_lit  = create(LIT,"+",iSubject,rI,ref tree2);
	    minus_lit = create(LIT,"-",iSubject,rI,ref tree2);
	    cont = true;
	    e_rhs = create(E,iSubject,rI,ref tree3,ref cont,"start");
	    
	    while(all( multi(e_lhs) )) {
	        while(all( multi(plus_lit), multi(e_rhs) )) {        
	            if(muprim("equivalent_mbool_mbool",start,true)) { // if the recursion start
	                deref rCont = true;
	            };
	            yield(deref rI,cons E_(tree1,tree2,tree3),deref rCont);
	        };
	        
	        while(all( multi(minus_lit), multi(e_rhs) )) {
	            if(muprim("equivalent_mbool_mbool",start,true)) { // if the recursion start
	                deref rCont = true;
	            };
	            yield(deref rI,cons E_(tree1,tree2,tree3),deref rCont);
	        };
	    };	    
	    // Un-do upon a failure
	    deref rCont = true;
	    
	};
		
}

coroutine LIT[4,iLit,iSubject,rI,rTree,
                index,lit] {
    guard deref rI < size(iSubject);
    index = deref rI;
    lit = muprim("subscript_str_mint",iSubject, deref rI);
    if(equal(lit,iLit)) {
        yield(1 + deref rI,cons LIT_(iLit));
    };
    // Un-do upon a failure
    deref rI = index;
}

function MAIN[1,args,
              iSubject,e,index,tree,cont] {
    //iSubject = "a+a+a+a";
    iSubject = "a+a-a+a";
    //iSubject = "a-a+a-a";
    //iSubject = "a+a";
    cont = true;
    index = 0;
	e = create(E,iSubject,ref index,ref tree,ref cont,"start");
	while(all(multi(e))) {
	    println("Done!");
	    if(index == size(iSubject)) {
	        println("Recognized: ", tree);
	    };
	};
    return true;
}