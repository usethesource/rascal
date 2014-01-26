module GR11

/*
 * syntax E = A "+" D
 *          | A "-" D
 *          | "a"
 *          ;
 * syntax A = B "c";
 * syntax B = E
 *  	   | "b"
 * 	       ;
 * syntax D = "k" F;   
 * syntax F = E
 *         | "g"
 *         ;
 */
 
declares "cons(adt(\"E\",[]),\"E_1\",[label(\"child1\",adt(\"A\",[])),label(\"child2\",adt(\"LIT\",[])),label(\"child3\",adt(\"D\",[]))])" // E = A +/- D
declares "cons(adt(\"E\",[]),\"E_2\",[label(\"child\",adt(\"LIT\",[]))])"                                                                  // E = a

declares "cons(adt(\"A\",[]),\"A_\",[label(\"child1\",adt(\"B\",[])),label(\"child2\",adt(\"LIT\",[]))])"                                  // A = B c

declares "cons(adt(\"B\",[]),\"B_1\",[label(\"child1\",adt(\"E\",[]))])"                                                                   // B = E
declares "cons(adt(\"B\",[]),\"B_2\",[label(\"child\",adt(\"LIT\",[]))])"                                                                  // B = b

declares "cons(adt(\"D\",[]),\"D_\",[label(\"child1\",adt(\"LIT\",[])),label(\"child2\",adt(\"F\",[]))])"                                  // D = k F

declares "cons(adt(\"F\",[]),\"F_1\",[label(\"child1\",adt(\"E\",[]))])"                                                                   // F = E
declares "cons(adt(\"F\",[]),\"F_2\",[label(\"child\",adt(\"LIT\",[]))])"                                                                  // F = g

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"                                                                         // terminals

declares "cons(adt(\"Marker\",[]),\"RECUR\",[label(\"child\",str())])"                                                                     // Marker

coroutine E[3,iSubject,rI,rTree,
            recurE,a_lit,tree,a,tree1,plus_lit,tree2,minus_lit,d,tree3] {
    // Marker for left recursive non-terminals
    recurE = cons RECUR("E");
    yield(deref rI,recurE);
    
    // Non-left recursive cases first: E = "a"
    a_lit = init(create(LIT,"a",iSubject,rI,ref tree));
    while(next(a_lit)) {
        yield(deref rI,cons E_2(tree));
    };
    
    // Left recursive (also indirect) cases in the end: E = A "+" D | A "-" D;
    a = init(create(A,iSubject,rI,ref tree1));
    while(next(a)) {
        if(muprim("equal",tree1,recurE)) {
            yield(deref rI,tree1);
        } else {
            if(muprim("equal",muprim("get_name",tree1), "RECUR")) {
                yield(deref rI,tree1); // propagate the marker upwards (indirect recursion)
            } else {
                
                plus_lit = init(create(LIT,"+",iSubject,rI,ref tree2));
                while(next(plus_lit)) {
                    d = init(create(D,iSubject,rI,ref tree3));
                    while(next(d)) {
                        yield(deref rI, cons E_1(tree1,tree2,tree3));
                    }; 
                };
                
                minus_lit = init(create(LIT,"-",iSubject,rI,ref tree2));
                while(next(minus_lit)) {
                    d = init(create(D,iSubject,rI,ref tree3));
                    while(next(d)) {
                        yield(deref rI, cons E_1(tree1,tree2,tree3));
                    }; 
                };
                0;
                    
            };
        };
    };
}

coroutine A[3,iSubject,rI,rTree,
            recurA,b,tree1,c_lit,tree2] {
    // Marker for left recursive non-terminals
    recurA = cons RECUR("A");
    yield(deref rI,recurA);
    
    // Non-left recursive cases first: none!
    
    // Left recursive (also indirect) cases in the end: A = B "c"
    b = init(create(B,iSubject,rI,ref tree1));
    while(next(b)) {
        if(muprim("equal",muprim("get_name",tree1),"RECUR")) {
            yield(deref rI,tree1); // propagate the marker upwards (indirect recursion)
        } else {
            c_lit = init(create(LIT,"c",iSubject,rI,ref tree2));
            while(next(c_lit)) {
                yield(deref rI,cons A_(tree1,tree2));
            };
            0;
        };
    };
    
    // Note: in presence of left recursive cases the stream of generated values is (and has to be) infinite and contains special delimiters (markers)
}

coroutine B[3,iSubject,rI,rTree,
            recurB,e,tree,b_lit] {
    // Marker for left recursive non-terminals
    recurB = cons RECUR("B");
    yield(deref rI,recurB);
    
    // Non-left recursive cases first: B = "b"
    b_lit = init(create(LIT,"b",iSubject,rI,ref tree));
    while(next(b_lit)) {
        yield(deref rI,cons B_2(tree));
    };
    
    // Left recursive (also indirect) cases in the end: B = E
    e = init(create(E,iSubject,rI,ref tree));
    while(next(e)) {
        if(muprim("equal",muprim("get_name",tree),"RECUR")) {
            yield(deref rI,tree); // propagate the marker upwards (indirect recursion)
        } else {
            yield(deref rI,cons B_1(tree));
        };
    };
    
    // Note: in presence of left recursive cases the stream of generated values is (and has to be) infinite and contains special delimiters (markers)
}

coroutine D[3,iSubject,rI,rTree,
            k_lit,tree1,f,tree2] {
    k_lit = init(create(LIT,"k",iSubject,rI,ref tree1));
    while(next(k_lit)) {
        f = init(create(F,iSubject,rI,ref tree2));
        while(next(f)) {
            yield(deref rI,cons D_(tree1,tree2));
        };
    };
}

coroutine F[3,iSubject,rI,rTree,
            e,tree,break,has,g_lit,recurE] {
            
    e = init(create(E,iSubject,rI,ref tree));
    // The use of left (also indirect) recursive non-terminals
    recurE = cons RECUR("E");
    has = false;
    break = false;
	if(next(e)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	while(muprim("not_mbool",break)) {
	    if(next(e)) { true; };
	    if(muprim("equal",tree,recurE)) {
	        if(muprim("not_mbool",has)) {
	            break = true;
	        };
	        has = false;
	    } else {
	        if(muprim("not_mbool",muprim("equal",muprim("get_name",tree),"RECUR"))) {
	            has = true;
	            yield(deref rI,cons F_1(tree));
	        };
	    };
	};
	
	g_lit = init(create(LIT,"g",iSubject,rI,ref tree));
	while(next(g_lit)) {
	    yield(deref rI,cons F_2(tree));
	};
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
              iSubject,e,index,tree,recurE,has] {
    iSubject = "ac+kac-kac+ka";   // success
    //iSubject = "ac+kac-wac+ka"; // failure
    
    index = 0;
	e = init(create(E,iSubject,ref index,ref tree));
	recurE = cons RECUR("E");
	
	has = false;
	if(next(e)) { true; }; // Eats the first, left recursion specific, 'recurE'
	while(true) {
	    if(next(e)) { true; };
	    if(muprim("equal",tree,recurE)) {
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