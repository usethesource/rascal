@bootstrapParser
module experiments::CoreRascal::Translation::RascalPattern

import Prelude;

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import lang::rascal::\syntax::Rascal;
import experiments::CoreRascal::Translation::RascalExpression;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/
 
str translatePat(p:(Pattern) `<BooleanLiteral b>`) = "coroutine (bool subject) resume bool () { yield subject == <translate(b)>; }";

str translatePat(p:(Pattern) `<IntegerLiteral n>`) = "coroutine (int subject) resume bool () { yield subject == <translate(n)>; }";
     
str translatePat(p:(Pattern) `<StringLiteral s>`) =  "coroutine (str subject) resume bool () { yield subject == <translate(s)>; }";
     
str translatePat(p:(Pattern) `<QualifiedName name>`) =  "coroutine (<getType(name@\loc)> subject) resume bool () { yield subject == <translate(name)>; }";
     
str translatePat(p:(Pattern) `<Type tp> <Name name>`) = "coroutine (<tp> subject) resume bool () { <name> = subject; yield true; }";

/*
  I assume the following coroutine model here:
  - a coroutine is a functions that contains a yield in its body.
  - calling a coroutine returns a coroutine value.
  - Inside the coroutine "yield v" returns value v from the coroutine.
  - Outside coroutine c, "c.resume(y)": resumes coroutine c; the suspended yield in coroutine c returns y.
  - "c.hasMore()": the coroutine can return more results.
  - "c.close()": shut coroutine c down.
  
  NOTE: there are several issues here:
  - Initialization of the coroutine (choice: a start function)
  - How is the argument of resume typed? (choice: in the header of the declaration)
  - How is communicated that the coroutine is exhausted? (choice: hasMore)
  
  Example: Countdown
  
  coroutine int countDown(int n) next(){
    while(n > 0 ){
    	yield n;
    	n -= 1;
    }
  }
  
  c = countDown(10).start();							// for(l <- countDown(10)) println(l);
  while(c.hasMore()) println(c.resume());
  
  Example: preorder traversal
  
  data TNODE = tnode(str key, TNODE left, TNODE right) | tleaf(str name);
  
  coroutine inorder(TNODE n) resume str (){
     if(tleaf(str name) := n) yield name;
     else {
        c = inorder(n.left).start();                             // for(l <- inorder(n.left)) yield l;
        while(c.hasMore()) yield c.resume("");
        
     	yield n.key;
     	c = inorder(n.right).start();
     	while(c.hasMore()) yield c.resume("");
     }
  }
  
  rascal> [l | l <- inorder(tnode("a", tnode("b", leaf("l1"), leaf("l2")), leaf("l3")))];
  ==> ["a", "b", "l1", "l2", "l3"];
*/

// List matching.
// All coroutines involved in list matching have the signature:
//		coroutine (list[&T] subject) resume tuple[bool,int](bool forward, int startPos)
// 1. Handle the list pattern
// 2. Wrap all other patterns to comply with the above signature
// 3. The cases of list variables are handled specially.

str translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
  defs = {}; uses = {};
  for(pat <- pats){
  	<ds, us> = getVars(pat);
  	defs += ds;
  	uses += us;
  }
  
  return
     "coroutine (value subject) resume tuple[bool,int](int) {
     '  <for(<tp, name> <- defs){><tp> <name>;<}>
     '  int sublen = size(subject);
     '  list[coroutine] matchers = [<intercalate(",\n", [translatePatAsListElem(pat) + ".start(subject)" | pat <- pats])>];
     '  int patlen = size(matchers);
     '  lits[int] upto = [0 | n \<- [0 .. patlen]];
     '  int p = 0; 
     '  int s = 0;
     '  bool forward = true;
     '  while(true){
     '    while(matchers[p].hasMore()){
     '       \<success, nextS\> = matchers[p].resume(forward, s);
     '       if(success){
     '          forward = true;
     '          upto[p] = s = nextS;
     '          p += 1;
     '          if(p == patlen && s == sublen)
     '         	   yield true; 
     '       }
     '    }
     '    if(p \> 0){
     '          p -= 1;
     '          s = p \> 0 ? upto[p - 1] : 0;
     '          forward = false;
     '    } else
     '          return \<false, 0\>;
     '  }
     '}
     ";
}

// Translate patterns as element of a list pattern

str translatePatAsListElem(p:(Pattern) `* <QualifiedName name>`) = 
   "coroutine (list[&T] subject) resume tuple[bool,int](bool forward, int startPos) { 
   '  while(true){
   '     int endPos = startPos;
   '     \<forward, startPos\> = yield \<true, endPos\>;
   '     if(forward && endPos \< size(subject)){
   '        endPos += 1;
   '        <name> = subject[startPos .. endPos];
   '        yield \<true, endPos\>;
   '     }
   '  } 
   '}";
  
default str translatePatAsListElem(Pattern p) = asListElem(translatePat(p));

// Wrap the translation of a pattern element as element of a list pattern

str asListElem(str code) = 
    "coroutine (list[&T] subject) resume tuple[bool,int](int startPos) { 
    '  while(true) { c = <code>.start(subject[startPos]); yield \<c.resume(), startPos + 1\>; 
    '}";
    
/*********************************************************************/
/*                  End of Patterns                                  */
/*********************************************************************/