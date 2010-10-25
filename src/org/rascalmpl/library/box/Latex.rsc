module box::Latex
import IO;
import SystemAPI;
import box::Box;

public text intro(loc locationIntro) {
   println("INTRO:<locationIntro>");
   return getFileContent(locationIntro.path);
   }
   
public text finish(loc locationEnd) {
    println("FINISH:<locationEnd>");
   return getFileContent(locationEnd.path);
   }