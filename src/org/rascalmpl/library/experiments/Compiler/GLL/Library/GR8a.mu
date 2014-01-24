module GR8A

/*
 * syntax A = B "c" | C "d" | "e" ;
 * syntax B = A "f";
 * syntax C = A "g";
 */
 
declares "cons(adt(\"A\",[]),\"A_1\",[label(\"child1_1\",adt(\"B\",[])),label(\"child2\",adt(\"LIT\",[]))])" // A = B "c"
declares "cons(adt(\"A\",[]),\"A_2\",[label(\"child1_2\",adt(\"C\",[])),label(\"child2\",adt(\"LIT\",[]))])" // A = C "d"
declares "cons(adt(\"A\",[]),\"A_3\",[label(\"child\",adt(\"LIT\",[]))])"                                    // A = "e"
declares "cons(adt(\"B\",[]),\"B_\",[label(\"child1\",adt(\"A\",[])),label(\"child2\",adt(\"LIT\",[]))])"    // B = A "f"
declares "cons(adt(\"C\",[]),\"C_\",[label(\"child1\",adt(\"A\",[])),label(\"child2\",adt(\"LIT\",[]))])"    // C = A "g"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"                                           // terminals

declares "cons(adt(\"Marker\",[]),\"RECUR\",[label(\"child\",str())])"                                       // Marker

coroutine A[3,iSubject,rI,rTree,
              recurA,e_lit,tree,b,c_lit,tree1,tree2,break,c,d_lit] {
    // Marker for left recursive non-terminals
    recurA = cons RECUR("A");
    yield(deref rI,recurA);
    
    // Non-left recursive cases first: A = "e"
    e_lit = init(create(LIT,"e"),iSubject,rI,ref tree);
    while(next(e_lit)) {
        yield(deref rI,cons A_3(tree));
    };
    
    // Left recursive (also indirect) cases in the end: A = B "c" | C "d"
    b = init(create(B,iSubject,rI,ref tree1));
    c = init(create(C,iSubject,rI,ref tree1));
        
    while(true) {
        
        break = false;
        while(muprim("not_mbool",break)) {
            if(next(b)) { true; };
            if(muprim("equal",tree1,recurA)) {
                break = true;
            } else {
                if(muprim("equal",muprim("get_name",tree1), "RECUR")) {
                    yield(deref rI,tree1); // propagate the marker upwards
                } else {
                    c_lit = init(create(LIT,"c",iSubject,rI,ref tree2));
                    while(next(c_lit)) {
                        yield(deref rI,cons A_1(tree1,tree2));
                    };
                    0;
                };
            };
        };
        
        break = false;
        while(muprim("not_mbool",break)) {
            if(next(c)) { true; };
            if(muprim("equal",tree1,recurA)) {
                break = true;
            } else {
                if(muprim("equal",muprim("get_name",tree1), "RECUR")) {
                    yield(deref rI,tree1); // propagate the marker upwards
                } else {
                    d_lit = init(create(LIT,"d",iSubject,rI,ref tree2));
                    while(next(d_lit)) {
                        yield(deref rI,cons A_2(tree1,tree2));
                    };
                    0;
                };
            };
        };
        
        yield(deref rI,recurA);
        
    };
    
    // Note: in presence of left recursive cases the stream of generated values is (and has to be) infinite and contains special delimiters (markers)
    // Note: alternatives are merged (see below)
}

coroutine B[3,iSubject,rI,rTree,
            recurB,a,tree1,f_lit,tree2] {
    // Marker for left recursive non-terminals
    recurB = cons RECUR("B");
    yield(deref rI,recurB);
    
    // Non-left recursive cases first: none!
    
    // Left recursive (also indirect) cases in the end: B = A "f"
    a = init(create(A,iSubject,rI,ref tree1));
    while(next(a)) {
        if(muprim("equal",muprim("get_name",tree1),"RECUR")) {
            yield(deref rI,tree1);
        } else {
            f_lit = init(create(LIT,"f",iSubject,rI,ref tree2));
            while(next(f_lit)) {
                yield(deref rI,cons B_(tree1,tree2));
            };
            0;
        };
    };
    
    // Note: in presence of left recursive cases the stream of generated values is (and has to be) infinite and contains special delimiters (markers)
}

coroutine C[3,iSubject,rI,rTree,
            recurC,a,tree1,g_lit,tree2] {
    // Marker for left recursive non-terminals
    recurC = cons RECUR("C");
    yield(deref rI,recurC);
    
    // Non-left recursive cases first: none!
    
    // Left recursive (also indirect) cases in the end: B = A "f"
    a = init(create(A,iSubject,rI,ref tree1));
    while(next(a)) {
        if(muprim("equal",muprim("get_name",tree1),"RECUR")) {
            yield(deref rI,tree1);
        } else {
            g_lit = init(create(LIT,"g",iSubject,rI,ref tree2));
            while(next(g_lit)) {
                yield(deref rI,cons C_(tree1,tree2));
            };
            0;
        };
    };
    
    // Note: in presence of left recursive cases the stream of generated values is (and has to be) infinite and contains special delimiters (markers)
}

// Basic coroutine
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

function MAIN[2,args,kwargs,
              iSubject,a,index,tree,recurA,has] {
    //iSubject = "efcfc";         // success
    //iSubject = "egdgdgd";       // success
    //iSubject = "egdfcgd";       // success
    iSubject = "egdgdgdfcfcgd";   // success
    //iSubject = "aegdfcgd";      // failure
    //iSubject = "egdfcagd";      // failure
    //iSubject = "egdfcgd";       // failure
    index = 0;
	a = init(create(A,iSubject,ref index,ref tree));
	recurA = cons RECUR("A");
	
	has = false;
	if(next(a)) { true; }; // Eats the first 'recurA'
	while(true) {
	    if(next(a)) { true; };
	    if(muprim("equal",tree,recurA)) {
	        if(muprim("not_mbool",has)) {
	            return true;
	        };
	        has = false;
	    } else {
	        if(muprim("not_mbool",muprim("equal",muprim("get_name",tree),"RECUR"))) {
	            has = true;
	            if(index == size(iSubject)) {
	                println("Recognized: ", tree);
	            };
	        };
	    };
	};
    return true;
}