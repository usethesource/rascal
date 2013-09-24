module experiments::Compiler::Examples::Tst


// Helper function to generate a getter
private str genGetter(map[str,str] fields, str x) {
  return "public <fields[x]> get<x>() {
         '  return <x>;
         '}";
}

// Generate a class with given name and fields.

public str genClass(str name, map[str,str] fields) { 
  return 
    "public class <name> {
    '  <for (x <- fields) {>
    '  <genGetter(fields, x)><}>
    '}";
}

value main(list[value] args){
  return genClass("Person", ("age" : "int"));
 
 
 //return genGetter( ("age" : "int"), "age");
}