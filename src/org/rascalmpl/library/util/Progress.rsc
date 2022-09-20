@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - jouke.stoel@cwi.nl - CWI}
module util::Progress

import String;
import IO;
import util::Math;

@doc{
  .Synopsis
  This progressbar can be used in terminal applications to show the progress of some process in the terminal.
  
  .Description
  The total number of steps is the only required parameter to be passed in. All other parameters are optional.
   - `prefix` is the string that is displayed in front of the progress bar (default "").
   - `length` is the length (number of characters) of the displayed bar (default 50).
   - `limit` allows for the throtteling of the number of times the progress bar is printed. For instance if the total is 1000 and the limit is set to 100 then the progress bar will be updated every 10 iterations. 
   - `fill` is the character used for the percentage used (default "\u2588").
   - `unfill` is the character used for the unused part (default "-").
   - `printEnd` is the character used at the end of the line (default "\r").
   
  The return is a tuple with 2 functions, the `report` and the `finished` function.
  - `report(str suffix)` needs to be called for every iteration update. The suffix is displayed after the progressbar and can differ per iteration
  - `finished()` can be called at the end of the iteration to add a new line to the terminal  

  It is inspired on the progressbar described here: https://stackoverflow.com/questions/3173320/text-progress-bar-in-the-console

  .Examples
  ```rascal-shell
  import util::Progress;
  
  int total = 10; 
  pb = progressBar(total, length = 15, limit = 100);
  
  for (i <- [0..total]) {
    pb.report(" : <i+1> of <total>");
  }
  
  pb.finished();  
  ```

}
tuple[void(str) report, void() finished] progressBar(int total, str prefix = "Progress:", int length = 50, int limit = total, str fill = "\u2588", str unfill = "-", str printEnd = "\r") {
    limit = limit > total ? total : limit;
    
    int iteration = 0;
    int showAt = floor(total / limit);
    
    return <void (str suffix) {
      iteration+=1;

      if (iteration % showAt == 0 || iteration == total) {
        int perc = percent(iteration, total); 
        int filled = floor((length * iteration) / total);    
        print("\r<prefix> |<left("", filled, fill)><left("", length - filled, unfill)>| <perc>% <suffix><printEnd>");
      }
    }, void () { println(); }>;
}

@doc {
  .Synopsis
  Simple spinner to display progress for some terminal process for which the total number of steps is not known.
      
  .Description
  `prefix` - Contains the string displayed in front the spinner (default " ").
     
   It returns a function that can be called to make the spinner spin one rotation.
   This function takes a `suffix` string parameter that will be displayed behind the spinner  
  
  .Examples  
  ```rascal-shell
  import util::Progress;
  import util::Math;
  
  sp = spinner();
  
  while (n := arbInt(100), n != 1) {
    sp("<n>");
  }
  ```  
}
void (str) spinner(str prefix = " ", str printEnd = "\r") {
  int stat = 0;
  
  return void (str suffix) {
    switch (stat) { 
      case 0: print("\r<prefix>\\ <suffix> <printEnd>");
      case 1: print("\r<prefix>| <suffix> <printEnd>");
      case 2: print("\r<prefix>/ <suffix> <printEnd>");
      case 3: print("\r<prefix>- <suffix> <printEnd>");
    }
    
    stat = (stat+1) % 4;
  };
}
