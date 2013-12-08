module GR3

declares "cons(adt(\"S\",[]),\"S_\",[label(\"child1\",adt(\"S\",[])),label(\"child2\",adt(\"LIT\",[])),label(\"child3\",adt(\"LIT\",[]))])"
declares "cons(adt(\"S\",[]),\"S_\",[label(\"child\",adt(\"EPSILON\",[]))])"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"

coroutine S[3,rI,rTree,iSubject,
              len,s,a_lit,d_lit,tree1,tree2,tree3,cont,index] {
    
    len = size(iSubject);
    // Non (direct or indirect) left recursive cases first
    // S = a
	if(muprim("less_mint_mint", deref rI, len)) {
	    a_lit = create(LIT,"a",rI,ref tree1,iSubject);
	    while(all( multi(a_lit) )) {
	        yield(deref rI,cons S_(tree1));
	    };
	    true; // muRascal detail: dummy expression
	};
	
    // S = S a d
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
        s = init(create(S,rI,ref tree1,iSubject));
        a_lit = create(LIT,"a",rI,ref tree2,iSubject);
        d_lit = create(LIT,"d",rI,ref tree3,iSubject);
        cont = true;
	    while(cont) {
	        if(next(s)) { /* always true in case of left recursion */ };
	        cont = false;
	        while(all( multi(a_lit), multi(d_lit) )) {
	            cont = true;
	            yield(deref rI,cons S_(tree1,tree2,tree3));
	        };
	    };
	    true; // muRascal detail: dummy expression
    };
	
}

coroutine LIT[4,iLit,rI,rTree,iSubject,
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
              iSubject,s,index,tree] {
    iSubject = "aadadad";
    index = 0;
	s = init(create(S, ref index, ref tree, iSubject));	
	while(next(s)) {
	    if(index == size(iSubject)) {
	        println("Recognized: ", tree);
	    };
	};
	
    return true;
}