module box::rascal::Constructors
import box::Box;
import box::Concrete;
import rascal::\old-syntax::Rascal;

public Box getConstructors(Tree q) {
if (Expression a:=q) 
switch(a) {
	case `<Literal literal> `: return NULL();
	case `<QualifiedName qualifiedName> `: return NULL();
	case `<BasicType basicType> ( <{Expression ","}*  c > ) `: return NULL();
	case `<Expression expression> ( <{Expression ","}*  c > ) `: return NULL();
	case `[ <{Expression ","}*  c > ] `:   {
             return getConstructor(c, #Expression,"[", "]");
             }
	case `{ <{Expression ","}*  c > } `: {
	         return getConstructor(c, #Expression,"{", "}");
             }
	// case `<<{Expression ","}+  c >>`: return NULL();
        /*
	case (Expression)`<<Expression ei>>`: {
	          return getConstructor(ei, #Expression,"<", ">");
	          }
	case `<<Expression ei>, <{Expression ","}* el>>` : return NULL();
         */
	// case `<Expression from> : <Expression to> `: return NULL();
	// case `( <{Mapping ","}*  c > ) `: 
    //         {
	//        list[Box] h1 = [L("(")]; h2 = [L(")")]; 
    //        return H(h1+getArgs(c, #Expression)+h2);
    //        }
}
return NULL();
}
