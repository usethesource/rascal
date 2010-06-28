module box::rascal::Constructors
import rascal::\old-syntax::Constructors;
import rascal::\old-syntax::Expressions;
import box::Box;
import box::Concrete;
import rascal::\old-syntax::Literals;
public Box getConstructors(Tree q) {
if (Expression a:=q) 
switch(a) {
	case `<Literal literal> `: return NULL();
	case `<QualifiedName qualifiedName> `: return NULL();
	case `<BasicType basicType> ( <{Expression ","}*  c > ) `: return NULL();
	case `<Expression expression> ( <{Expression ","}*  c > ) `: return NULL();
	case `[ <{Expression ","}*  c > ] `:   {
	         list[Box] h1 = [L("[")]; h2 = [L("]")]; 
             return H(h1+getArgs(c, #Expression)+h2);
             }
	case `{ <{Expression ","}*  c > } `: {
	         list[Box] h1 = [L("{")]; h2 = [L("}")]; 
             return H(h1+getArgs(c, #Expression)+h2);
             }
	// case `< <{Expression ","}+  c > > `: return NULL();
	// case `<Expression from> : <Expression to> `: return NULL();
	// case `( <{Mapping ","}*  c > ) `: 
    //         {
	//        list[Box] h1 = [L("(")]; h2 = [L(")")]; 
    //        return H(h1+getArgs(c, #Expression)+h2);
    //        }
}
return NULL();
}
