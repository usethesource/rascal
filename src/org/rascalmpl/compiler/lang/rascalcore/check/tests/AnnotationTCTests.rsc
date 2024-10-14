@bootstrapParser
module lang::rascalcore::check::tests::AnnotationTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;
 
test bool AnnotationNotAllowed1() = unexpectedType("1 [@an=3];");

test bool AnnotationNotAllowed2() = unexpectedType("1@ann;");

test bool AnnotationNotAllowed31() = unexpectedTypeInModule("
    module AnnotationNotAllowed31
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        value main() = f()[@pos=true];
    ");
  	
test bool AnnotationNotAllowed41() = unexpectedTypeInModule("
    module AnnotationNotAllowed41
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        value main() = f() [@wrongpos=true];
    ");
 
test bool UndefinedValueError11() = uninitializedInModule("
    module UndefinedValueError11
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        void main(){
            F someF; 
            someF@pos;
    ");
 
test bool UndefinedValueError21() = uninitializedInModule("
    module UndefinedValueError21
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        void main(){
            F someF; s
            someF [@pos=3];
        }
    ");
test bool UninitializedVariableError1() = uninitializedInModule("
    module UninitializedVariableError1
        data F = f() | f(int n) | g(int n) | deep(F f);
        anno int F@pos;
        value main(){
            F someF; 
            someF@pos = 3;
        }
    ");
  	