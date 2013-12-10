module GR8

declares "cons(adt(\"A\",[]),\"A_\",[label(\"child1\",adt(\"B\",[])),label(\"child2\",adt(\"LIT\",[]))])"
declares "cons(adt(\"A\",[]),\"A_\",[label(\"child1\",adt(\"C\",[])),label(\"child2\",adt(\"LIT\",[]))])"
declares "cons(adt(\"A\",[]),\"A_\",[label(\"child\",adt(\"LIT\",[]))])"
declares "cons(adt(\"B\",[]),\"B_\",[label(\"child1\",adt(\"A\",[])),label(\"child2\",adt(\"LIT\",[]))])"
declares "cons(adt(\"C\",[]),\"C_\",[label(\"child1\",adt(\"A\",[])),label(\"child2\",adt(\"LIT\",[]))])"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"

coroutine A[5,iSubject,rI,rTree,rCont,start_id,
              b,c,c_lit,d_lit,e_lit,
              tree,tree1,tree2,
              start,
              recur,
              cont,
              index,
              rcont,
              loop] {
    
    start = muprim("equal",start_id,"start");
    recur = muprim("equal",start_id,"A"); // Defines the recursion start
    
    // A = e
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
	    e_lit = create(LIT,"e",iSubject,rI,ref tree);
	    while(all( multi(e_lit) )) {
	        if(recur) {
	            yield(deref rI,cons A_(tree),false);
	        } else {
	            yield(deref rI,cons A_(tree),deref rCont);
	        };
	    };
	    true; // muRascal detail: dummy expression
    };
    
    if(recur) { // Do not expand with left recursive alternatives if not explicitly set
        if(muprim("equivalent_mbool_mbool",deref rCont,false)) {
            exhaust;
        };
    };
    
    // A = B c && A = C d
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
        if(start) {
            b = init(create(B,iSubject,rI,ref tree1,rCont,"A"));
            c = init(create(C,iSubject,rI,ref tree1,rCont,"A"));
        } else {
            b = init(create(B,iSubject,rI,ref tree1,rCont,start_id)); // Pass 'start_id' to the left nonterminal to mark a recursion start
            c = init(create(C,iSubject,rI,ref tree1,rCont,start_id)); // Pass 'start_id' to the left nonterminal to mark a recursion start
        };
	    c_lit = create(LIT,"c",iSubject,rI,ref tree2);
	    d_lit = create(LIT,"d",iSubject,rI,ref tree2);
	    
	    // Running left recursive alternatives in parallel
	    loop = true;
	    while(loop) {
	        loop = false;
	        cont = false;
	        // First left recursive alternative    
	        index = deref rI;
	        if(next(b)) {
	            loop = true;
	            while(all( multi(c_lit) )) {        
	                if(start) { // if the recursion start
	                    cont = true;
	                };
	                yield(deref rI,cons A_(tree1,tree2),deref rCont);
	            }; 
	            true;
	        };	        
	        deref rI = index;
	        // Second left recursive alternative       
	        if(next(c)) {
	            loop = true;
	            while(all( multi(d_lit) )) {
	                if(start) { // if the recursion start
	                    cont = true;
	                };
	                yield(deref rI,cons A_(tree1,tree2),deref rCont);
	            };
	            true;  
	        };
	        deref rI = index;
	        if(start && cont) {
	            deref rCont = cont;
	        };
	    };	    
	    // Un-do upon a failure
	    deref rCont = true;
	    
	};
		
}

coroutine B[5,iSubject,rI,rTree,rCont,start_id,
              a,f_lit,
              tree1,tree2] {   
    // No base cases    
    // B = A f
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
        a = create(A,iSubject,rI,ref tree1,rCont,start_id); // Pass 'start_id' to the left nonterminal to mark a recursion start
	    f_lit = create(LIT,"f",iSubject,rI,ref tree2);
	    
	    while(all( multi(a), multi(f_lit) )) {
	        yield(deref rI,cons B_(tree1,tree2),deref rCont);
	    };	    
	    true; // muRascal detail: dummy expression
	};	
}

coroutine C[5,iSubject,rI,rTree,rCont,start_id,
              a,g_lit,
              tree1,tree2] {
    // No base cases  
    // C = A g
    if(muprim("less_mint_mint", deref rI, size(iSubject))) {
        a = create(A,iSubject,rI,ref tree1,rCont,start_id); // Pass 'start_id' to the left nonterminal to mark a recursion start
	    g_lit = create(LIT,"g",iSubject,rI,ref tree2);
	    
	    while(all( multi(a), multi(g_lit) )) {
	        yield(deref rI,cons C_(tree1,tree2),deref rCont);
	    };	    
	    true; // muRascal detail: dummy expression
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
              iSubject,a,index,tree,cont] {
    //iSubject = "efcfc";
    iSubject = "egdgdgd";
    //iSubject = "egdfcgd";
    cont = true;
    index = 0;
	a = create(A,iSubject,ref index,ref tree,ref cont,"start");
	while(all(multi(a))) {
	    println("Done!");
	    if(index == size(iSubject)) {
	        println("Recognized: ", tree);
	    };
	};
    return true;
}