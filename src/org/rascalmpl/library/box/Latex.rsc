module box::Latex
import IO;
import box::Box;

public text intro(loc locationIntro) {
   return readFileLines(locationIntro);
   }
   
public text finish(loc locationEnd) {
   return readFileLines(locationEnd);
   }