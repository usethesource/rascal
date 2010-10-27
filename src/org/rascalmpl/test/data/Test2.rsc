module Test2
import IO;
import zoo::pico::syntax::Main;

public void test(){
  if(`declare <{\ID-TYPE "," }* decls>;` := `declare x: natural, y : string;`){
     
       println("decls: ", decls);
      
       L = [Id | ` <\PICO-ID Id> : <TYPE Type> ` <- decls];
       
      println("L =", L);
  }
}
