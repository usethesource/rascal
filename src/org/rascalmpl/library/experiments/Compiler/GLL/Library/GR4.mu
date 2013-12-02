module GR4

declares "cons(adt(\"E\",[]),\"E_\",[label(\"child1\",adt(\"E\",[])),label(\"child2\",adt(\"LIT\",[])),label(\"child3\",adt(\"E\",[]))])"
declares "cons(adt(\"E\",[]),\"E_\",[label(\"child\",adt(\"LIT\",[]))])"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"

coroutine E[3,rI,rTree,iSubject,
              e_lhs,a_lit,tree] {
    
    // E = E + E && E = E - E
	if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    // Dealing with left recursion
	    e_lhs = create(E_PRIME,rI,ref tree,iSubject);
	    while(all( multi(e_lhs) )) {
	        CYCLE(rI,rTree,iSubject,tree);
	    };
	    true; // muRascal detail: dummy expression
	};
	
	// E = a
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    a_lit = create(LIT,"a",rI,ref tree,iSubject);
	    while(all( multi(a_lit) )) {
	        yield(deref rI,cons E_(tree));
	    };
	    true; // muRascal detail: dummy expression
    };

}

coroutine E_PRIME[3,rI,rTree,iSubject,
                    a_lit,tree] {    
    // E = a
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    a_lit = create(LIT,"a",rI,ref tree,iSubject);
	    while(all( multi(a_lit) )) {
	        yield(deref rI,cons E_(tree));
	    };
	    true; // muRascal detail: dummy expression
    };	
}

coroutine CYCLE[4,rI,rTree,iSubject, tree,
                  plus_lit,minus_lit,e_rhs,tree0,tree1,tree2] {
    plus_lit = create(LIT,"+",rI,ref tree1,iSubject);
	e_rhs = create(E,rI,ref tree2,iSubject);
    while(all( multi(plus_lit), multi(e_rhs) )) {
        tree0 = cons E_(tree,tree1,tree2);
        yield(deref rI,tree0);
        CYCLE(rI,rTree,iSubject,tree0);
    };
    
    minus_lit = create(LIT,"-",rI,ref tree1,iSubject);
	e_rhs = create(E,rI,ref tree2,iSubject);
    while(all( multi(minus_lit), multi(e_rhs) )) {
        tree0 = cons E_(tree,tree1,tree2);
        yield(deref rI,tree0);
        CYCLE(rI,rTree,iSubject,tree0);
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

function MAIN[1,args,
              iSubject,e,index,tree] {
    //iSubject = "a+a+a+a";
    iSubject = "a+a-a+a";
    index = 0;
	e = create(E, ref index, ref tree, iSubject);
	while(all(multi(e))) {
	    println("Done!");
	    if(index == size(iSubject)) {
	        println("Recognized: ", tree);
	    };
	};
    return true;
}