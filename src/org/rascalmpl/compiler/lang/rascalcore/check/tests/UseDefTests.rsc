module lang::rascalcore::check::tests::UseDefTests

import lang::rascalcore::check::tests::StaticTestingUtils;

bool localVar1() =
     useDefOK("module LocalVar1
                void main() {
                  int x = 10;
                  int y = x + x;
                }", ("x": <0, {1, 2}>)); 

bool globalVar1() =
     useDefOK("module LocalVar1
                int x = 10;
                void main() {
                  int y = x + x;
                }", ("x": <0, {1, 2}>));

bool fun1() =
     useDefOK("module Fun
                int f(int n) = n;
                int main() = f(3);", 
                ("f": <0, {1}>)); 

bool formal1() =
     useDefOK("module Formal
                int f(int n) = n;", 
                ("n": <0, {1}>)); 

bool kw1() =
     useDefOK("module KW
                int f(int n = 0) = n;", 
                ("n": <0, {1}>)); 

bool field1() =
    useDefOK("module Field
                data D = d(int n);
                value main(){
                    x = d(10);
                    return x.n;
                }",
                ("n": <0, {1}>)); 

bool kwfield1() =
    useDefOK("module KWField
                data D = d(int n = 0);
                value main(){
                    x = d(n = 10);
                    return x.n;
                }",
                ("n": <0, {1, 2}>)); 

