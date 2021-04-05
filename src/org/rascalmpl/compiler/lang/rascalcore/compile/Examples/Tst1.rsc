module lang::rascalcore::compile::Examples::Tst1

keyword KEY = "k" | "e"| | "y";

test bool  dispatchTestKeyword() { 
    int f((KEY) `k`) = 1;
    int f((KEY) `e`) = 2;
    int f((KEY) `y`) = 3;
        
    return [f((KEY)`k`),f((KEY)`e`),f((KEY)`y`)] == [1,2,3];
}

//import ParseTree; 
//import String;

//lexical Example = ([A-Z] head [a-z]* tail)+ words;
//
//value main() = [w.head | w <- t.words] //["C", "C", "B", "F"] == [ "<w.head>" |  w <- t.words ]
//  when Example t := [Example] "CamelCaseBaseFeest";