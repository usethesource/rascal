module lang::rascalcore::compile::Benchmarks::BTemplate

// Challenge: use no library functions

// Capitalize the first character of a string

str capitalize(str c) =
("a" : "A", "b" : "B", "c" : "C", "d" : "D", "e" : "E", "f" : "F", "g" : "G",
 "h" : "H", "i" : "I", "j" : "J", "k" : "K", "l" : "L", "m" : "M", "n" : "N",
 "o" : "O", "p" : "P", "q" : "Q", "r" : "R", "s" : "S", "t" : "T", "u" : "U",
 "v" : "V", "w" : "W", "x" : "X", "y" : "Y", "z" : "Z")[c[0]] + c[1..];

// Helper function to generate a setter
private str genSetter(map[str,str] fields, str x) {
  return "public void set<capitalize(x)>(<fields[x]> <x>) {
         '  this.<x> = <x>;
         '}";
}

// Helper function to generate a getter
private str genGetter(map[str,str] fields, str x) {
  return "public <fields[x]> get<capitalize(x)>() {
         '  return <x>;
         '}";
}

// Generate a class with given name and fields.

public str genClass(str name, map[str,str] fields) { 
  return 
    "public class <name> {
    '  <for (x <- fields) {>
    '  private <fields[x]> <x>;
    '  <genSetter(fields, x)>
    '  <genGetter(fields, x)><}>
    '}";
}

value main(){
  for(int i <- [0 .. 5000]){
      genClass("Person", ("first" : "String", "last" : "String", "age" : "int", "married" : "boolean"));
  }
  return 0;
}