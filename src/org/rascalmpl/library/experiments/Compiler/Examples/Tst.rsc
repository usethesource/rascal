module experiments::Compiler::Examples::Tst
 	
// 	  data D = d(int i) | d();
// 	  
//      D d(int i) { if (i % 2 == 0) fail d; else return d();}
//
//  	  public bool main(list[value] args) = d(2) := d(2);
      //public test bool fail2() = d(3) == d();
      
 //     public bool main(list[value] args)  {
	//	return {for (x <- [1,2,3]) { f = () { append x; }; f();}} 
	//	    == [1,2,3];
	//}

// public bool main(list[value] args) = all(int X <- {1,2,3}, X >= 2);

// Generate a class with given name and fields.

public str genClass(str name, map[str,str] fields) { 
  return 
    "public class {
    '  <for (x <- fields) {>
    '  PPPPPP<}>
    '}";
}

value main(list[value] args){
  return genClass("Person", ("first" : "String", "last" : "String", "age" : "int", "married" : "boolean"));
}