module experiments::Compiler::Examples::Tst3

 data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
 
 /*
value main(list[value] args) = { <t1, t2> | INTERSECT({TYPESET t1, *TYPESET t2}) :=  INTERSECT({SET("b"), SET("a")})};
  						   //{ <SET("b"),{SET("a")}>, <SET("a"),{SET("b")}>};
*/
  	 	
value main(list[value] args) = {<t1, rest, t2> | {INTERSECT({TYPESET t1, *TYPESET rest}),  t2} :=  {INTERSECT({SET("a"), SET("b")}) , SET("b")}};

//  				           { <SET("a"),{SET("b")},SET("b")>, <SET("b"),{SET("a")},SET("b")>};
 
 /* 
 public test bool testSet67() = {<t1, rest> | {INTERSECT({TYPESET t1, *TYPESET rest}),  t1} :=  {INTERSECT({SET("a"), SET("b")}), SET("b")}}==
  				           {<SET("b"),{SET("a")}>};  
  				           
*/