module box::rsc::Expressions
import rascal::\old-syntax::Rascal;
import box::Box;
import box::Concrete;
import IO;
public Box getExpressions(Tree q) {
if (Expression a:=q)  
switch(a) {
     case `<Expression lhs> in <Expression rhs> `: return H([evPt(lhs),L(" in "), evPt(rhs)]);
     case `<Expression lhs> notin <Expression rhs> `: return H([evPt(lhs),L(" notin "), evPt(rhs)]);
    }
return NULL();
}
/*
if (Parameters a:=q) 
switch(a) {
	case `( <Formals formals> ) `: return getConstructor(getA(formals)[0],  "(", ")");
	case `( <Formals formals> ... ) `: return NULL();
}
if (Formal a:=q) 
 switch(a) {
	case `<Type typ> <Name name> `: return H(1, [evPt(typ), evPt(name)]);
    }
if (PathPart a:=q) 
switch(a) {
	case `<PrePathChars pre> <Expression expression> <PathTail tail> `: return NULL();
}
if (PathTail a:=q) 
switch(a) {
	case `<MidPathChars mid> <Expression expression> <PathTail tail> `: return NULL();
	case `<PostPathChars post> `: return NULL();
}

if (StringTail a:=q) 
switch(a) {
	case `<MidStringChars mid> <Expression expression> <StringTail tail> `: return NULL();
	case `<MidStringChars mid> <StringTemplate template> <StringTail tail> `: return NULL();
	case `<PostStringChars post> `: return NULL();
}
*/
/*
if (Expression a:=q) 
switch(a) {
	case `<Type typ> <Parameters parameters> { } `: return NULL();
	case `<Parameters parameters> { <Statement* statements> } `: return NULL();
	case `{ } `: return NULL();
	case `<Label label> <Visit vis> `: return NULL();
	case `( <Expression expression> ) `: return NULL();
	case `[ <Expression first> .. <Expression last> ] `: return NULL();
	case `[ <Expression first> , <Expression second> .. <Expression last> ] `: return NULL();
	case `# <Type typ> `: return NULL();
	case `<BasicType basicType> ( <{Expression ","}*  c > ) `: return NULL();
	case `<Expression expression> ( <{Expression ","}*  c > ) `: return NULL();
	case `<Expression expression> [ <Name key> = <Expression replacement> ] `: return NULL();
	case `<Expression expression> . <Name field> `: return NULL();
	case `<Expression expression> < <{Field ","}+  c > > `: return NULL();
	case `<Expression expression> [ <{Expression ","}+  c > ] `: return NULL();
	case `<Expression argument> ? `: return NULL();
	case `! <Expression argument> `: return NULL();
	case `- <Expression argument> `: return NULL();
	case `<Expression argument> * `: return NULL();
	case `<Expression argument> + `: return NULL();
	case `<Expression expression> @ <Name name> `: return NULL();
	case `<Expression expression> [ @ <Name name> = <Expression val> ] `: return NULL();
	case `<Expression lhs> o <Expression rhs> `: return NULL();
	case `<Expression lhs> * <Expression rhs> `: return NULL();
	case `<Expression lhs> join <Expression rhs> `: return NULL();
	// case `* <Expression rhs> `: return NULL();
	case `<Expression lhs> join <Expression rhs> `: return NULL();
	case `<Expression lhs> / <Expression rhs> `: return NULL();
	case `<Expression lhs> % <Expression rhs> `: return NULL();
	case `/ <Expression rhs> `: return NULL();
	case `<Expression lhs> % <Expression rhs> `: return NULL();
	case `<Expression lhs> & <Expression rhs> `: return NULL();
	// case `+ <Expression rhs> `: return NULL();
	case `<Expression lhs> - <Expression rhs> `: return NULL();
	case `<Expression lhs> + <Expression rhs> `: return NULL();
	case `<Expression lhs> - <Expression rhs> `: return NULL();
	
	
	    case `notin <Expression rhs> `: return NULL();
	case `<Expression lhs> in <Expression rhs> `: return NULL();
	// case `< <Expression rhs> `: return NULL();
	case `<Expression lhs> <= <Expression rhs> `: return NULL();
	case `<Expression lhs> > <Expression rhs> `: return NULL();
	case `<Expression lhs> >= <Expression rhs> `: return NULL();
	case `<Expression lhs> < <Expression rhs> `: return NULL();
	case `<Expression lhs> <= <Expression rhs> `: return NULL();
	case `<Expression lhs> > <Expression rhs> `: return NULL();
	case `<Expression lhs> >= <Expression rhs> `: return NULL();
	// case `== <Expression rhs> `: return NULL();
	case `<Expression lhs> != <Expression rhs> `: return NULL();
	case `<Expression condition> ? <Expression thenExp> : <Expression elseExp> `: return NULL();
	case `<Expression lhs> == <Expression rhs> `: return NULL();
	case `<Expression lhs> != <Expression rhs> `: return NULL();
	case `<Expression condition> ? <Expression thenExp> : <Expression elseExp> `: return NULL();
	case `<Expression lhs> ? <Expression rhs> `: return NULL();
	case `<Expression lhs> ==> <Expression rhs> `: return NULL();
	case `<Expression lhs> <==> <Expression rhs> `: return NULL();
	// case `==> <Expression rhs> `: return NULL();
	case `<Expression lhs> <==> <Expression rhs> `: return NULL();
	case `<Expression lhs> && <Expression rhs> `: return NULL();
	case `<Expression lhs> || <Expression rhs> `: return NULL();
	// case `+ `: return NULL();
	// case `* `: return NULL();
	// case `- `: return NULL();
}
*/

/*
if (Formals a:=q) 
switch(a) {
	case `<{Formal ","}*  c > `: return NULL();
}
if (Field a:=q) 
switch(a) {
	case `<Name fieldName> `: return NULL();
	case `<IntegerLiteral fieldIndex> `: return NULL();
}
if (StringTemplate a:=q) 
switch(a) {
	case `for ( <{Expression ","}+  c > ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> } `: return NULL();
	case `if ( <{Expression ","}+  c > ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> } `: return NULL();
	case `if ( <{Expression ","}+  c > ) { <Statement* preStatsThen> <StringMiddle thenString> <Statement* postStatsThen> } else { <Statement* preStatsElse> <StringMiddle elseString> <Statement* postStatsElse> } `: return NULL();
	case `while ( <Expression condition> ) { <Statement* preStats> <StringMiddle body> <Statement* postStats> } `: return NULL();
	case `do { <Statement* preStats> <StringMiddle body> <Statement* postStats> } while ( <Expression condition> ) `: return NULL();
}
if (StringLiteral a:=q) 
switch(a) {
	case `<PreStringChars pre> <Expression expression> <StringTail tail> `: return NULL();
	case `<PreStringChars pre> <StringTemplate template> <StringTail tail> `: return NULL();
}
if (StringMiddle a:=q) 
switch(a) {
	case `<MidStringChars mid> `: return NULL();
	case `<MidStringChars mid> <Expression expression> <StringMiddle tail> `: return NULL();
	case `<MidStringChars mid> <StringTemplate template> <StringMiddle tail> `: return NULL();
}
if (ProtocolPart a:=q) 
switch(a) {
	case `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail> `: return NULL();
}
if (ProtocolTail a:=q) 
switch(a) {
	case `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail> `: return NULL();
	case `<PostProtocolChars post> `: return NULL();
}
*/

