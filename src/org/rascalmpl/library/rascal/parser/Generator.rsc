module Generator

import rascal::parser::Grammar;

public str generateJava(str name, Grammar g) {
  return 
"public class <name> {

}
";
}