module experiments::CoreRascal::Translation::RascalExpressions

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
/*
  This is an experiment to see how some Rascal Expressions can be translated to the Core language.
  Since we have no AST (yet) for RascalExpressions we make it up here.
  I assume the following coroutine model here:
  - a coroutine is a functions that contains a yield in its body.
  - calling a coroutine returns a coroutine value.
  - Inside the coroutine "yield v" returns value v from the coroutine.
  - Outside coroutine c, "c.resume(y)": resumes coroutine c; the suspended yield in coroutine c returns y.
  - "c.hasMore()": the coroutine can return more results.
  - "c.close()": shut coroutine c down.
  
  NOTE: there are several issues here:
  - Initialization of the coroutine
  - How is the argument of resume typed?
  - How is communicated that the coroutine is exhausted?
  
  Example: Countdown
  
  int countDown(int n){
    while(n > 0 ){
    	yield n;
    	n -= 1;
    }
  }
  
  c = countDown(10);							// for(l <- countDown(10)) println(l);
  while(c.hasMore()) println(c.resume());
  
  Example: preorder traversal
  
  data TNODE = tnode(str key, TNODE left, TNODE right) | tleaf(str name);
  
  str inorder(TNODE n){
     if(tleaf(str name) := n) yield name;
     else {
        c = inorder(n.left);                             // for(l <- inorder(n.left)) yield l;
        while(c.hasMore()) yield c.resume("");
        
     	yield n.key;
     	c = inorder(n.right);
     	while(c.hasMore()) yield c.resume("");
     }
  }
  
  rascal> [l | l <- inorder(tnode("a", tnode("b", leaf("l1"), leaf("l2")), leaf("l3")))];
  ==> ["a", "b", "l1", "l2", "l3"];
*/

// Literals
data RascalExp = boolCon(bool b) | intCon(int n) | strCon(str s) | listCon(list[RascalExpr] exps) | nodeCon(str name, list[RascalExpr] args);

Exp translate(boolCon(true)) = \true();
Exp translate(boolCons(false)) = \false();
Exp translate(intCon(int n)) = number(n);
// strCon, enz.

Exp translate(listCon(list[RascalExpr] exps)) = lst([translate(re) | re <- exps]);
Exp translate(nodeCon(str name, list[RascalExpr] args)) = nd(name, ([translate(re) | re <- args]));


data RascalExp = var(str name);
Exp translate(var(str name)) = id(name);

// Boolean operators

data RascalExp = 
	  \and(RascalExp lhs, RascalExp rhs)
	| \or(RascalExp lhs, RascalExp rhs)
	| \not(RascalExp lhs)
	;

	
/*
Translation schemas for true, false, and, or, not:

boolCon(true) ==>
  	"bool trueFun () = yield true;"
  	
boolCon(false) ==>
  	"bool falseFun () = yield false;"

\and(e1, e2)==>
	"bool andFun () {
		c1 = <bool_translate(e1)>;
		c2 = <bool_translate(e2)>;
		while (c1.hasMore()){
		       if(c1.resume()){
		            while(c2.hasMore()){
		       			if(c2.resume())
		       				yield true;
		       		}
		       } else 
		       		return false;
		}
		return false;
    }"
    
\or(e1, e2)==>
	"bool orFun () {
		c1 = <bool_translate(e1)>;
		c2 = <bool_translate(e2)>;
		while (c1.hasMore()){
			if(c1.resume())
		       yield true;
		}
		while (c2.hasMore()){
			if(c2.resume())
		       yield true;
		}
		return false;
    }"

\not(e)==>
	"bool notFun () {
		c = <bool_translate(e)>;
		while (c.hasMore())
		       yield !c.resume();
		return false;
    }"
    
Example:

\and(\true(), \false()) ==>
	"bool andFun () {
		c1 = bool trueFun () = yield true;
		c2 = bool falseFun () = yield false;
		while (c1.hasMore()){
		       if(c1.resume()){
		            while(c2.hasMore()){
		       			if(c2.resume())
		       				yield true;
		       		}
		       } else 
		       		return false;
		}
		return false;
    }"

*/

	
data RascalExp =
       less(RascalExp lhs, RascalExp rhs)
     | add(RascalExp lhs, RascalExp rhs)
     ;

/*
less(e1,e2) ==>
     "<translate(e1)> \< <translate(e2)>";
     
Example:
less(intCon(3), intCon(4)) ==>
     number(3) < number(4);
          
\and(less(intCon(3), intCon(4)), \true()) ==>

bool andFun () {
		c1 = bool lessFun () = number(3) < number(4);  // <== who wraps < in a coroutine?
		c2 = bool trueFun () = yield true;
		while (c1.hasMore()){
		       if(c1.resume()){
		            while(c2.hasMore()){
		       			if(c2.resume())
		       				yield true;
		       		}
		       } else 
		       		return false;
		}
		return false;
    }"

*/
	
data Pattern = boolPat(bool b) | intPat(int n) | strPat(str s) | listPat(list[Pattern] pats) | nodePat(str name, list[Patterns] pats);

data Pattern = var(str name);

data RascalExp =
       match(Pattern pat, RascalExpression exp);
       
/*
match(boolPat(bool b), RascalExpression exp)) ==>
    "<b> == <translate(exp)>;"
    
match(intPat(int n), RascalExpression exp)) ==>
    "intCon(<n>) == <translate(exp)>;"
    
match(var(str name), RascalExpression exp)) ==>
    "var(<name>) = <translate(exp)>; true"
    
    
match(nodePat(str name, list[Patterns] pats), RascalExpression exp)) ==>
    "subject = <translate(exp)>; args = getArgs(subject);
     <size(pats)> == size(args) && <name> == fun(subject)) &&
     
     	<for(i <- index(pats)){>
         <translate(match(pats[i], args[i])> &&
     	<}>
        true
    "
Example:

match(nodePat("f", [intPat(3), var("X"), boolPat(true)]), nodeCon("f", [intCon(3), intCon(4), boolCon(true)])   ==>

	subject = nodeCon("f", [intCon(3), intCon(4), boolCon(true)]); 
	args = getArgs(subject);
	3 == size(args) && "f" == fun(subject) &&
	intCon(3) == args[0] &&
	X = args[1] &&
	boolPat(true) == args[2]
    
*/
