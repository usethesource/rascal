module experiments::Compiler::Examples::Tst

public bool greaterEqual(int i, int j) = i == j || i > j;

value main(list[value] args){
   //str Y = "Y\<";
   //if(/^abc<x:.*>def<Y>ghi<z:[0-9]+>$/ := "abcxyzdefY\<ghi123456789")
   //   return x + "/" + z;
   //else return "nomatch";
   return greaterEqual(4, 3);
}   