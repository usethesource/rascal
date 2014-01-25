module GR5A

/* Variation of GR5:
 * syntax E = E "+" E
 *          | E "-" E
 *          | E "*" E
 *          | E "/" E
 *          | "-" E
 *          | E "+"
 *          | "(" E ")"
 *          | Char
 *          ;
 * syntax Char = "a";
 */
 
declares "cons(adt(\"E\",[]),\"E_1\",[label(\"child1_e1\",adt(\"E\",[])),label(\"child2_e1\",adt(\"LIT\",[])),label(\"child3_e1\",adt(\"E\",[]))])"   // E = E "+"/"-"/"*"/"/"/ E
declares "cons(adt(\"E\",[]),\"E_2\",[label(\"child1_e2\",adt(\"LIT\",[])),label(\"child2_e2\",adt(\"E\",[]))])"                                      // E = "-" E
declares "cons(adt(\"E\",[]),\"E_3\",[label(\"child1_e3\",adt(\"E\",[])),label(\"child2_e3\",adt(\"LIT\",[]))])"                                      // E = E "+"
declares "cons(adt(\"E\",[]),\"E_4\",[label(\"child1_e4\",adt(\"LIT\",[])),label(\"child2_e4\",adt(\"E\",[])),label(\"child3_e4\",adt(\"LIT\",[]))])" // E = "(" E ")"
declares "cons(adt(\"E\",[]),\"E_5\",[label(\"child_e5\",adt(\"CHAR\",[]))])"                                                                         // E = Char
declares "cons(adt(\"CHAR\",[]),\"CHAR_\",[label(\"child\",adt(\"LIT\",[]))])"                                                                        // Char = "a"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"                                                                                    // terminals
declares "cons(adt(\"EPSILON\",[]),\"EPSILON_\",[])"                                                                                                  // epsilon

declares "cons(adt(\"Marker\",[]),\"RECUR\",[label(\"child\",str())])"                                                                                // Marker

coroutine E[3,iSubject,rI,rTree,
            recurE,char,tree,minus_lit,e,tree1,tree2,has,break,lbracket,rbracket,tree3,e_lhs,e_rhs,plus_lit,mult_lit,divide_lit,break_rhs] {
    // Marker for left recursive non-terminals
    recurE = cons RECUR("E");
    yield(deref rI,recurE);
    
    // Non-left recursive cases first: E = "-" E; E = "(" E ")"; E = Char;
    minus_lit = init(create(LIT,"-",iSubject,rI,ref tree1));
    while(next(minus_lit)) {
    
        e = init(create(E,iSubject,rI,ref tree2));
        // The use of left (also indirect) recursive non-terminals
        has = false;
        break = false;
	    if(next(e)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	    while(muprim("not_mbool",break)) {
	        if(next(e)) { true; };
	        if(muprim("equal",tree2,recurE)) {
	            if(muprim("not_mbool",has)) {
	                break = true;
	            };
	            has = false;
	        } else {
	            has = true;
	            yield(deref rI, cons E_2(tree1,tree2));
	        };
	    };
	               
    };
    
    lbracket = init(create(LIT,"(",iSubject,rI,ref tree1));
    while(next(lbracket)) {
        
        e = init(create(E,iSubject,rI,ref tree2));
        // The use of left (also indirect) recursive non-terminals
        has = false;
        break = false;
	    if(next(e)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	    while(muprim("not_mbool",break)) {
	        if(next(e)) { true; };
	        if(muprim("equal",tree2,recurE)) {
	            if(muprim("not_mbool",has)) {
	                break = true;
	            };
	            has = false;
	        } else {
	            has = true;
	            
	            rbracket = init(create(LIT,")",iSubject,rI,ref tree3));
	            while(next(rbracket)) {
	                yield(deref rI, cons E_4(tree1,tree2,tree3));
	            };
	            
	        };
	    };
    };
     
    char = init(create(CHAR,iSubject,rI,ref tree));
    while(next(char)) {
        yield(deref rI,cons E_5(tree));
    };
    
    // Left recursive (also indirect) cases in the end: E = E "+" E | E "-" E | E "*" E | E "/" E | E "+" ;
    e_lhs = init(create(E,iSubject,rI,ref tree1));
    while(true) {
        
        // TODO: could be simplified, see GR8/B and GR8/C
        break = false;
        while(muprim("not_mbool",break)) {
            if(next(e_lhs)) { true; };
            if(muprim("equal",tree1,recurE)) {
                break = true;
            } else {
                plus_lit = init(create(LIT,"+",iSubject,rI,ref tree2));
                while(next(plus_lit)) {
                    
                    e_rhs = init(create(E,iSubject,rI,ref tree3));
                    // The use of left (also indirect) recursive non-terminals
                    has = false;
                    break_rhs = false;
	                if(next(e_rhs)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	                while(muprim("not_mbool",break_rhs)) {
	                    if(next(e_rhs)) { true; };
	                    if(muprim("equal",tree3,recurE)) {
	                        if(muprim("not_mbool",has)) {
	                            break_rhs = true;
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
                    break_rhs = false;
	                if(next(e_rhs)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	                while(muprim("not_mbool",break_rhs)) {
	                    if(next(e_rhs)) { true; };
	                    if(muprim("equal",tree3,recurE)) {
	                        if(muprim("not_mbool",has)) {
	                            break_rhs = true;
	                        };
	                        has = false;
	                    } else {
	                        has = true;
	                        yield(deref rI, cons E_1(tree1,tree2,tree3));
	                    };
	               };
                    
                };
                
                mult_lit = init(create(LIT,"*",iSubject,rI,ref tree2));
                while(next(mult_lit)) {
                    
                    e_rhs = init(create(E,iSubject,rI,ref tree3));
                    // The use of left (also indirect) recursive non-terminals
                    has = false;
                    break_rhs = false;
	                if(next(e_rhs)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	                while(muprim("not_mbool",break_rhs)) {
	                    if(next(e_rhs)) { true; };
	                    if(muprim("equal",tree3,recurE)) {
	                        if(muprim("not_mbool",has)) {
	                            break_rhs = true;
	                        };
	                        has = false;
	                    } else {
	                        has = true;
	                        yield(deref rI, cons E_1(tree1,tree2,tree3));
	                    };
	               };
                    
                };
                
                divide_lit = init(create(LIT,"/",iSubject,rI,ref tree2));
                while(next(divide_lit)) {
                    
                    e_rhs = init(create(E,iSubject,rI,ref tree3));
                    // The use of left (also indirect) recursive non-terminals
                    has = false;
                    break_rhs = false;
	                if(next(e_rhs)) { true; }; // Eats the first, left-recursion specific, 'recurE'
	                while(muprim("not_mbool",break_rhs)) {
	                    if(next(e_rhs)) { true; };
	                    if(muprim("equal",tree3,recurE)) {
	                        if(muprim("not_mbool",has)) {
	                            break_rhs = true;
	                        };
	                        has = false;
	                    } else {
	                        has = true;
	                        yield(deref rI, cons E_1(tree1,tree2,tree3));
	                    };
	               };
                    
                };
                
                plus_lit = init(create(LIT,"+",iSubject,rI,ref tree2));
                while(next(plus_lit)) {
                    yield(deref rI, cons E_3(tree1,tree2));
                };
                
                0;
            };
        };
        
        yield(deref rI,recurE);
        
    };

}

coroutine CHAR[3,iSubject,rI,rTree,
               a_lit,tree] {
    a_lit = init(create(LIT,"a",iSubject,rI,ref tree));
    while(next(a_lit)) {
        yield(deref rI,cons CHAR_(tree));
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
    iSubject = "a+a/a++a+(a/-a)";   // success
    //iSubject = "a+a/a++a+(b/-a)"; // failure
    
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