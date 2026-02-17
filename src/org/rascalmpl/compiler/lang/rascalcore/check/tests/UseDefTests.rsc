module lang::rascalcore::check::tests::UseDefTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool localVar1() =
     useDefOK("module LocalVar1
                void main() {
                  int x = 10;
                  int y = x + x;
                }", ("x": <0, {1, 2}>)); 

test bool globalVar1() =
     useDefOK("module LocalVar1
                int x = 10;
                void main() {
                  int y = x + x;
                }", ("x": <0, {1, 2}>));

test bool fun1() =
     useDefOK("module Fun
                int f(int n) = n;
                int main() = f(3);", 
                ("f": <0, {1}>)); 

test bool overloadedFun() {
     mtext = "module OverloadedFun
                int f(int n) = n;    // first declaration of f
                int f(bool b) = 0;   // second declaration of f
                int main1() = f(3);
                int main2() = f(false);";
     return    useDefOK(mtext, ("f": <0, {2}>)) // check uses of first declaration of f
            && useDefOK(mtext, ("f": <1, {3}>)) // check uses of second declaration of f
            ;
}

test bool formal1() =
     useDefOK("module Formal
                int f(int n) = n;", 
                ("n": <0, {1}>)); 

test bool kw1() =
     useDefOK("module KW
                int f(int n = 0) = n;", 
                ("n": <0, {1}>)); 

test bool field1() =
    useDefOK("module Field
                data D = d(int n);
                value main(){
                    x = d(10);
                    return x.n;
                }",
                ("n": <0, {1}>)); 

test bool overloadedField() {
    mtext = "module OverloadedField
                data D = d(int n);  // first declaration of n
                data E = e(int n);  // second declaration of n
                value main(){
                    x = d(10);
                    y = e(20);
                    return x.n + y.n;
                }";
    return   useDefOK(mtext, ("n": <0, {2}>)) // check uses of first declaration of n
          && useDefOK(mtext, ("n": <1, {3}>)) // check uses of second declaration of n
          ;
}

test bool syntaxField1() =
    useDefOK("module Field
                syntax D = d: N n;
                syntax N = \"n\";
                value main(){
                    x = [D] \"n\";
                    return x.n;
                }",
                ("n": <0, {1}>));

@ignore{to be fixed in typechecker}

test bool kwfield1() =
    useDefOK("module KWField
                data D = d(int n = 0);
                value main(){
                    x = d(n = 10);
                    return x.n;
                }",
                ("n": <0, {1, 2}>)); 

test bool syntaxField1() =
    useDefOK("module SyntaxField
                syntax C = \"c\";
                syntax D = C c;
                value main(){
                    x = [D] \"c\";
                    return x.c;
                }",
                ("c": <0, {1}>));

test bool overloadedSyntaxField(){
    mtext = "module SyntaxField
                syntax C = \"c\";
                syntax D = C c;   // First declaration of c
                syntax E = \"e\";
                syntax F = E c;   // Second declaration of c
                value main(){
                    x = [D] \"c\";
                    y = [F] \"e\";
                    return [x.c,  y.c];
                }";
      return   useDefOK(mtext, ("c": <0, {2}>)) // check uses of first declaration of c
            && useDefOK(mtext, ("c": <1, {3}>)) // check uses of first declaration of c
            ;
}