module experiments::Compiler::Examples::Tst


value main(list[value] args){
   str Y = "Y\<";
   if(/^abc<x:.*>def<Y>ghi<z:[0-9]+>$/ := "abcxyzdefY\<ghi123456789")
      return x + "/" + z;
   else return "nomatch";
}   