module box::rsc::Types
import rascal::\old-syntax::Rascal;
import box::Box;
import box::Concrete;
public Box getTypes(Tree q) {
    return NULL();
    }

/*
if (StructuredType a:=q) 
switch(a) {
	case `<BasicType basicType> [ <{TypeArg ","}+  c > ] `: return HV(0, [evPt(basicType), L("["),evPt(c), L("]")]) ;
       }
if (UserType a:=q) 
switch(a) {
	// case `<QualifiedName name> `: return NULL();
	case `<QualifiedName name> [ <{Typ ","}+  c > ] `:  return HV(0, [evPt(name), L("["),evPt(c), L("]")]);
}

if (BasicType a:=q) 
switch(a) {
	case `bool `: return NULL();
	case `int `: return NULL();
	case `real `: return NULL();
	case `num `: return NULL();
	case `str `: return NULL();
	case `value `: return NULL();
	case `node `: return NULL();
	case `void `: return NULL();
	case `loc `: return NULL();
	case `list `: return NULL();
	case `set `: return NULL();
	case `bag `: return NULL();
	case `map `: return NULL();
	case `rel `: return NULL();
	case `tuple `: return NULL();
	case `lex `: return NULL();
	case `type `: return NULL();
	case `adt `: return NULL();
	case `constructor `: return NULL();
	case `fun `: return NULL();
	case `non-terminal `: return NULL();
	case `reified `: return NULL();
	case `datetime `: return NULL();
}
if (DataTypeSelector a:=q) 
switch(a) {
	case `<QualifiedName sort> . <Name production> `: return NULL();
}
if (TypeVar a:=q) 
switch(a) {
	case `& <Name name> `: return NULL();
	case `& <Name name> <: <Type bound> `: return NULL();
}
if (TypeArg a:=q) 
switch(a) {
	case `<Type typ> `: return NULL();
	case `<Type typ> <Name name> `: return NULL();
}

if (Type a:=q) 
switch(a) {
	case `<BasicType basic> `: return NULL();
	case `<StructuredType structured> `: return NULL();
	case `<FunctionType function> `: return NULL();
	case `<TypeVar typeVar> `: return NULL();
	case `<UserType user> `: return NULL();
	case `<DataTypeSelector selector> `: return NULL();
	case `( <Type typ> ) `: return NULL();
}
if (FunctionType a:=q) 
switch(a) {
	case `<Type typ> ( <{TypeArg ","}*  c > ) `: return NULL();
}
*/

