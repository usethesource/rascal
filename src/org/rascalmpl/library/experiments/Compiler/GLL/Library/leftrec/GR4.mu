module GR4

/*
 * syntax E = E "+" E
 *          | E "-" E
 *          | a
 *          ;
 */
 
declares "cons(adt(\"E\",[]),\"E_1\",[label(\"child1\",adt(\"E\",[])),label(\"child2\",adt(\"LIT\",[])),label(\"child3\",adt(\"E\",[]))])" // E = E +/- E
declares "cons(adt(\"E\",[]),\"E_2\",[label(\"child\",adt(\"LIT\",[]))])"                                                                  // E = a

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"                                                                         // terminals

declares "cons(adt(\"Marker\",[]),\"RECUR\",[label(\"child\",str())])"                                                                      // Marker

coroutine E[3,iSubject,rI,rTree,
            recurE,a_lit,tree,e_lhs,tree1,plus_lit,tree2,e_rhs,tree3,minus_lit,break,has] {
    // Marker for left recursive non-terminals
    recurE = cons RECUR("E");
    yield(deref rI,recurE);
    
    // Non-left recursive cases first: E = "a"
    a_lit = init(create(LIT,"a",iSubject,rI,ref tree));
    while(next(a_lit)) {
        yield(deref rI,cons E_2(tree));
    };
    
    // Left recursive (also indirect) cases in the end: E = E "+" E | E "-" E
    e_lhs = init(create(E,iSubject,rI,ref tree1));
    while(next(e_lhs)) {
        if(muprim("equal",tree1,recurE)) {
            yield(deref rI,tree1);
        } else {
            plus_lit = init(create(LIT,"+",iSubject,rI,ref tree2));
            while(next(plus_lit)) {
                
                e_rhs = init(create(E,iSubject,rI,ref tree3));
                // The use of left (also indirect) recursive non-terminals
                has = false;
                break = false;
	            if(next(e_rhs)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	            while(muprim("not_mbool",break)) {
	                if(next(e_rhs)) { true; };
	                if(muprim("equal",tree3,recurE)) {
	                    if(muprim("not_mbool",has)) {
	                        break = true;
	                    };
	                    has = false;
	                } else {
	                    has = true;
	                    yield(deref rI, cons E_1(tree1,tree2,tree3));
	                };
	            };
                    
            };
            
            minus_lit = init(create(LIT,"-",iSubject,rI,ref tree2));
            while(next(minus_lit)) {
                
                e_rhs = init(create(E,iSubject,rI,ref tree3));
                // The use of left (also indirect) recursive non-terminals
                has = false;
                break = false;
	            if(next(e_rhs)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	            while(muprim("not_mbool",break)) {
	                if(next(e_rhs)) { true; };
	                if(muprim("equal",tree3,recurE)) {
	                    if(muprim("not_mbool",has)) {
	                        break = true;
	                    };
	                    has = false;
	                } else {
	                    has = true;
	                    yield(deref rI, cons E_1(tree1,tree2,tree3));
	                };
	            };
                    
            };
            0;
        };
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
    //iSubject = "a+a+a+a"; // success
    iSubject = "a+a-a+a";   // success
    //iSubject = "a-a+a-a"; // success
    //iSubject = "a+a";     // success
    //iSubject = "a+a-b+a"; // failure
    
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