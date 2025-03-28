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
@ignore{to be fixed in typechecker}
test bool kwfield1() =
    useDefOK("module KWField
                data D = d(int n = 0);
                value main(){
                    x = d(n = 10);
                    return x.n;
                }",
                ("n": <0, {1, 2}>)); 

