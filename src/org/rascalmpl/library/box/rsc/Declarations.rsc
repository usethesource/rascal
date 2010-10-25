module box::rsc::Declarations
import rascal::\old-syntax::Rascal;
import box::Concrete;
import box::Box;
import IO;

public Box getDeclarations(Tree q) {
   if (Variable a:=q) 
   switch(a) {
	//  case `<Name name> `: return NULL();
	 case `<Name name> = <Expression initial> `: return HV(0, [evPt(name), L("="), evPt(initial)]);
         }
    if (Signature a:=q) 
     switch(a) {
	case `<Type typ> <FunctionModifiers modifiers> <Name name> <Parameters parameters> `:  
                 return  HV(1, [evPt(typ), evPt(modifiers)  , HV(0, [evPt(name), evPt(parameters)])]);                                             
	case `<Type typ> <FunctionModifiers modifiers> <Name name> <Parameters parameters> throws <{Type ","}+  c > `: 
                  return HV(1, [evPt(typ), evPt(modifiers)  , HV(0, [evPt(name), evPt(parameters)]), L("throws"), evPt(c)]);        
        }
    return NULL();
    }


/*

if (Toplevel a:=q) 
switch(a) {
	case `<Declaration declaration> `: return NULL();
}

if (FunctionDeclaration a:=q) 
switch(a) {
	case `<Tags tags> <Visibility visibility> <Signature signature> <FunctionBody body> `: return NULL();
	case `<Tags tags> <Visibility visibility> <Signature signature> ; `: return NULL();
}
if (FunctionModifier a:=q) 
switch(a) {
	case `java `: return NULL();
}
if (Visibility a:=q) 
switch(a) {
	case `public `: return NULL();
	case `private `: return NULL();
	default: return NULL();
}
if (Variant a:=q) 
switch(a) {
	case `<Name name> ( <{TypeArg ","}*  c > ) `: return NULL();
}
if (Kind a:=q) 
switch(a) {
	case `module `: return NULL();
	case `function `: return NULL();
	case `rule `: return NULL();
	case `variable `: return NULL();
	case `data `: return NULL();
	case `view `: return NULL();
	case `alias `: return NULL();
	case `anno `: return NULL();
	case `tag `: return NULL();
	case `all `: return NULL();
}
if (FunctionModifiers a:=q) 
switch(a) {
	case `<FunctionModifier* modifiers> `: return NULL();
}
if (Test a:=q) 
switch(a) {
	case `<Tags tags> test <Expression expression> `: return NULL();
	case `<Tags tags> test <Expression expression> : <StringLiteral labeled> `: return NULL();
}
if (FunctionBody a:=q) 
switch(a) {
	case `{ <Statement* statements> } `: return NULL();
}
if (Alternative a:=q) 
switch(a) {
	case `<Name name> <Type typ> `: return NULL();
}
if (Declaration a:=q) 
switch(a) {
	case `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ; `: return NULL();
	case `<Tags tags> <Visibility visibility> data <UserType user> = <{Variant "|"}+  c > ; `: return NULL();
	case `<Tags tags> <Visibility visibility> data <UserType user> ; `: return NULL();
	case `<FunctionDeclaration functionDeclaration> `: return NULL();
	case `<Test tst> ; `: return NULL();
	case `<Tags tags> <Visibility visibility> <Type typ> <{Variable ","}+  c > ; `: return NULL();
	case `<Tags tags> rule <Name name> <PatternWithAction patternAction> ; `: return NULL();
	case `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ; `: return NULL();
	case `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+  c > ; `: return NULL();
	         {
	         list[Box] h = [evPt(kind)];
             return H(h+getArgs(c));
             }
}
*/

