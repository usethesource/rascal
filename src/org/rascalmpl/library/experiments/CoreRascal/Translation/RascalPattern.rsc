@bootstrapParser
module experiments::CoreRascal::Translation::RascalPattern

import Prelude;

import lang::rascal::\syntax::Rascal;
import experiments::CoreRascal::Translation::RascalExpression;

import experiments::CoreRascal::muRascal::AST;

/*********************************************************************/
/*                  Patterns                                         */
/*********************************************************************/
 
list[MuExp] translatePat(p:(Pattern) `<BooleanLiteral b>`) = [ muCreate("MATCH_BOOL", translate(b)) ];

list[MuExp] translatePat(p:(Pattern) `<IntegerLiteral n>`) = [ muCreate("MATCH_INT", translate(n)) ];
     
list[MuExp] translatePat(p:(Pattern) `<StringLiteral s>`, Expression subject) =   [ muCreate("MATCH_STR", translate(b)) ];
     
list[MuExp] translatePat(p:(Pattern) `<QualifiedName name>`, Expression subject) =  "coroutine (<getType(name@\loc)> subject) resume bool () { yield subject == <translate(name)>; }";
     
list[MuExp] translatePat(p:(Pattern) `<Type tp> <Name name>`) = "coroutine (<tp> subject) resume bool () { <name> = subject; yield true; }";

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
  
  coroutine void countDown(int n) int next(){
    while(n > 0 ){
    	f(n);
    	n -= 1;
    }
    return;
  }
  
  coroutine int bla(){
    b =  ALL(countDown(10));
    while(l.hasNext()) yield l.next();
    r = countDown(5);
    while(r.hasNext()) yield r.next();
  }
  
  int f(int nn) {
    yield nn
  }
  
  c = create(countDown).init(10);							// for(l <- countDown(10)) println(l);
  while(c.hasNext()) println(c.next());
  
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

list[MuExp] translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
  
  return [ muCreate("MATCH_LIST", [ *translatePat(pat) | pat <- pats ]) ];
 
  
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

bool backtrackFree(p:(Pattern) `[<{Pattern ","}* pats>]`) = false;
bool backtrackFree(p:(Pattern) `{<{Pattern ","}* pats>}`) = false;

default bool backtrackFree(Pattern p) = true;
