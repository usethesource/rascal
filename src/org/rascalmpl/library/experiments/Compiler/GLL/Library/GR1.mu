module GR1

coroutine S[2,rI,iSubject,
              s,a_lit,a,d,epsilon] {
    
    // S = a S
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    s = create(S,rI,iSubject);
	    a_lit = create(A_LIT,rI,iSubject);
	    while(all( multi(a_lit), multi(s) )) {
	        println("S = a S");
	        yield deref rI;
	    };    
    };
	
	// S = A S d
	if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    s = create(S,rI,iSubject);
	    a = create(A,rI,iSubject);
	    d = create(D_LIT,rI,iSubject);
	    while(all( multi(a), multi(s), multi(d) )) {
	        println("S = A S d");
	        yield deref rI;
	    };
	};
	
	// S = epsilon
	epsilon = create(EPSILON,rI,iSubject);
	while(all( multi(epsilon) )) {
	   println("S = epsilon");
	   yield deref rI;
	};
}

coroutine A[2,rI,iSubject,a] {
    a = create(A_LIT,rI,iSubject);
    while(all(multi(a))) {
        println("A = a");
        yield deref rI;
    };
}

coroutine A_LIT[2,rI,iSubject,
                  index,lit] {
    guard deref rI < size(iSubject);
    index = deref rI;
    lit = muprim("subscript_str_mint",iSubject, deref rI);
    if(equal(lit,"a")) {
        println("a","; next: ", 1 + deref rI);
        yield 1 + deref rI;
    };
    // Un-do upon a failure
    deref rI = index;
}

coroutine D_LIT[2,rI,iSubject,
                  index,lit] {
    guard deref rI < size(iSubject);
    index = deref rI;
    lit = muprim("subscript_str_mint", iSubject, deref rI);
    if(equal(lit,"d")) {
        println("d","; next: ", 1 + deref rI);
        yield 1 + deref rI;
    };
    // Un-do upon a failure
    deref rI = index;
}

coroutine EPSILON[2,rI,iSubject] {
    return deref rI;
}

function MAIN[1,args,
              iSubject,s,index] {
    iSubject = "aad";
    index = 0;
	s = create(S, ref index, iSubject);
	while(all(multi(s))) {
	    if(index == size(iSubject)) {
	        println("Recognized");
	    };
	};
    return true;
}