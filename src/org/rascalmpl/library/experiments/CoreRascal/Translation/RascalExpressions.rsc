@bootstrapParser
module experiments::CoreRascal::Translation::RascalExpressions

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import lang::rascal::\syntax::Rascal;
import Prelude;
import util::Reflective;
import util::ValueUI;
import ParseTree;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;

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
  - Initialization of the coroutine (choice: a start function)
  - How is the argument of resume typed? (choice: in the header of the declaration)
  - How is communicated that the coroutine is exhausted? (choice: hasMore)
  
  Example: Countdown
  
  coroutine countDown(int n) resume int (){
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

list[loc] libSearchPath = [|std:///|, |eclipse-std:///|];

loc Example1 = |std:///experiments/CoreRascal/Translation/Examples/Example1.rsc|;

Configuration config = newConfiguration();

Symbol getType(loc l) = config.locationTypes[l];

str getType(Expression e) = getName(getType(e@\loc));

void parse(){
   Module M = parseModule(Example1, libSearchPath);
   config = checkModule(M.top, newConfiguration());  // .top is needed to remove start! Ugly!
   top-down visit(M.top){
    case FunctionDeclaration decl: {
   		 println("<decl.expression> translates to");
   		 println("<translate(decl.expression)>");
   		 }
   }
 }
 
// Generate code for completely type-resolved operators
str infix(str op, Expression e) = "infix(\"<op>-<getType(e.lhs)>-<getType(e.rhs)>\", <translate(e.lhs)>, <translate(e.rhs)>)";
str prefix(str op, Expression arg) = "prefix(\"<op>-<getType(arg)>\", <translate(arg)>)";
str postfix(str op, Expression arg) = "postfix(\"<op>-<getType(arg)>\", <translate(arg)>)";

// Translate expressions
str translate((BooleanLiteral) `<BooleanLiteral b>`) = "<b>" == true ? "\\true())" : "\\false())";
str translate((Expression) `<BooleanLiteral b>`) = translate(b);
 
str translate((IntegerLiteral) `<IntegerLiteral n>`) = "number(<n>)";
str translate((Expression) `<IntegerLiteral n>`) = translate(n);
 
str translate((StringLiteral) `<StringLiteral s>`) = "strCon(<s>)";
str translate((Expression) `<StringLiteral s>`) = translate(s);
 
str translate((QualifiedName) `<QualifiedName v>`) = "var(<v>, <getType(v@\loc)>)";
str translate((Expression) `<QualifiedName v>`) = translate(v);

str translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) {
    elems = [ translate(elem) | elem <- es ];
    return "mkList([<intercalate(",", elems)>])";
}
 
str translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("add", e);
str translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);
str translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lesseq", e);
str translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);
str translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greatereq", e);
str translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = infix("equal", lhs, e);
str translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = infix("notequal", e);
 
str translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e) + ".start().resume()";

str translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e)  + ".start().resume()";

default str translate(Expression e) = "default for Expression: <e>";

// End of expression cases

// Utilities for boolean operators
 
// Is an expression free of backtracking? 
bool backtrackFree(e:(Expression) `<Pattern pat> := <Expression exp>`) = false;
default bool backtrackFree(Expression e) = true;

// Get all variables that are introduced by a pattern.
tuple[set[tuple[str,str]],set[str]] getVars(Pattern p) {
  defs = {};
  uses = {};
  visit(p){
    case (Pattern) `<Type tp> <Name name>`: defs += <"<tp>", "<name>">;
    case (Pattern) `<QualifiedName name>`: uses += "<name>";
    case (Pattern) `*<QualifiedName name>`: uses += "<name>";
  };
  return <defs, uses>;
}

// Translate Boolean operators
str translateBool(e:(Expression) `<Expression lhs> && <Expression rhs>`) =
 backtrackFree(lhs) ?
   (backtrackFree(rhs) ? "coroutine () resume bool () { yield infix(\"and\", e); }"
                       : "coroutine () resume bool () {
		                 '  lhsAnd = <translate(lhs)>;
		 			     '  if(lhsAnd == \\true()){
		 			     '     rhsAnd = <translateBool(rhs)>.start();
		 			     '     while(rhsAnd.hasMore()){
		       			 '        if(rhsAnd.resume())
		       			 '           yield true;
		       		     '  }
		       		     '  return false;
		       		     '}")
		       		   :
   (backtrackFree(rhs) ? "coroutine () resume bool (){
                         '  lhsAnd = <translateBool(lhs)>.start();
		 			     '  rhsAnd = <translate(rhs)>;
		 			     '  while (lhsAnd.hasMore()){
		                 '     if(lhsAnd.resume())
		                 '        if(rhsAnd) 
		                 '           yield true;
		                 '        else
		                 '           return false;
		                 '  }
		                 '  return false;
		                 '}"
		               : "coroutine () resume bool () {
		                 '  lhsAnd = <translateBool(lhs)>.start();
		                 '  rhsAnd = <translateBool(rhs)>.start();
		                 '  while (lhsAnd.hasMore()){
		                 '     if(lhsAnd.resume()){
		                 '        while(rhsAnd.hasMore()){
		       			 '          if(rhsAnd.resume())
		       			 '             yield true;
		       		     '        }
		                 '     } else 
		       		     '       return false;
		                 '  }
		                 '  return false;
                         '}");
 // similar for or and not.
 
 // Translate match operator
 str translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`)  =
    "coroutine () resume bool(){
    '  matcher = <translatePat(pat)>;
    '  subject = <translate(exp)>;
    '  matcher.start(subject);
    '  while(matcher.hasMore()){
    '    if(matcher.resume())
    '       yield true;
    '    else
    '       return false;
    '  }
    '}";  
 
 // Translate patterns
 
str translatePat(p:(Pattern) `<BooleanLiteral b>`) = "coroutine (bool subject) resume bool () { yield subject == <translate(b)>; }";

str translatePat(p:(Pattern) `<IntegerLiteral n>`) = "coroutine (int subject) resume bool () { yield subject == <translate(n)>; }";
     
str translatePat(p:(Pattern) `<StringLiteral s>`) =  "coroutine (str subject) resume bool () { yield subject == <translate(s)>; }";
     
str translatePat(p:(Pattern) `<QualifiedName name>`) =  "coroutine (<getType(name@\loc)> subject) resume bool () { yield subject == <translate(name)>; }";
     
str translatePat(p:(Pattern) `<Type tp> <Name name>`) = "coroutine (<tp> subject) resume bool () { <tp> <name> = subject; yield true; }";

str translatePat(p:(Pattern) `[<{Pattern ","}* pats>]`) {
  defs = {}; uses = {};
  for(pat <- pats){
  	<ds, us> = getVars(pat);
  	defs += ds;
  	uses += us;
  }
  
  return
     "coroutine (value subject) resume tuple[bool,int](int) {
     '  <for(<tp, nm> <- defs){><tp> <name>;<}>
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





