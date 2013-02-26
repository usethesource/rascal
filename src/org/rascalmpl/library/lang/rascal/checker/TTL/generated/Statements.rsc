module lang::rascal::checker::TTL::generated::Statements
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import util::Eval;

test bool tst(){
  checkResult = checkExpString("x = 1;", importedModules=[], initialDecls = []);
  if(!hasType("x", #int, checkResult)) return false;
  return true;
}
test bool tst(){
  checkResult = checkExpString("x = 1; x = false;", importedModules=[], initialDecls = []);
  
  for(msg <-  getMessages(checkResult)){
        if(/Expected int/ := msg)
  	      return true;
  }
  return true;
}
test bool tst(){
  checkResult = checkExpString("x;", importedModules=["M1"], initialDecls = []);
  if(!hasType("x", #int, checkResult)) return false;
  return true;
}
test bool tst(){
  checkResult = checkExpString("y;", importedModules=[], initialDecls = ["int y = 1;"]);
  if(!hasType("y", #int, checkResult)) return false;
  return true;
}
test bool tst(){
  checkResult = checkExpString("int y = 1; x = y;", importedModules=[], initialDecls = []);
  if(!hasType("x", #int, checkResult)) return false;
  return true;
}@expect{ArgumentMismatch}
test bool tst(){
  checkResult = checkExpString("triple([1,2,3]);", importedModules=[], initialDecls = ["int triple(int x) = 3 * x;"]);
  
  return true;
}
test bool tst(){
  checkResult = checkExpString("x = 3; x = 3.5;", importedModules=[], initialDecls = []);
  if(!hasType("x", #num, checkResult)) return false;
  return true;
}
