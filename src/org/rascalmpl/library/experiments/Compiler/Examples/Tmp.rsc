module experiments::Compiler::Examples::Tmp

// Generate a class with given name and fields.


//private str genGetter(map[str,str] fields, str x) {
//  return "<x>";
//}
//
//public str genClass(str name, map[str,str] fields) { 
//  return 
//    "xxx<for (x <- fields) {>
//    'yy<genGetter(fields, "age")><}>
//    '}";
//}
//
//value main(list[value] args){
//  return genClass("Person", ("first" : "String", "last" : "String", "age" : "int", "married" : "boolean"));
//}

value f(1) = 10;
value f(3) = 30;
default value f(int n) = n;

value main(list[value] args){
  return f(5);
}