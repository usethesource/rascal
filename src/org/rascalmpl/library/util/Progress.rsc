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
  This progress bar can be used in terminal applications to show the progress of some process.
  
  .Description
  The total number of steps is the only required parameter to be passed in. All other parameters are optional.
   - `prefix` is the string that is displayed in front of the progress bar (default "").
   - `length` is the length (number of characters) of the displayed bar (default 50).
   - `fill` is the character used for the percentage done (default "\u2588").
   - `unfill` is the character used for the not done part (default "-").
   - `printEnd` is the character used at the end of the line (default "\r").
   
  The return is a tuple with 2 functions, the `report` and the `finished` function.
  - `report(str suffix)` needs to be called for every iteration update. The suffix is displayed after the progressbar and can differ per iteration
  - `finished()` can be called at the end of the iteration to add a new line to the terminal  

  It is inspired on the progressbar described here: https://stackoverflow.com/questions/3173320/text-progress-bar-in-the-console

  .Examples
  ```rascal-shell
  import util::Progress;
  
  int total = 100000; 
  pb = progressBar(total, length = 15);
  
  for (i <- [0..total]) {
    pb.report(" : <i+1> of <total>");
  }
  
  pb.finished();  
  ```
}
tuple[void(str) report, void() finished] progressBar(int total, str prefix = "Progress:", int length = 50, str fill = "\u2588", str unfill = "-", str printEnd = "\r") {
    iteration = 1;

    return <void (str suffix) {
      int perc = percent(iteration, total); 
      int filled = floor((length * iteration) / total);    
      print("\r<prefix> |<left("", filled, fill)><left("", length - filled, unfill)>| <perc>% <suffix><printEnd>");
      iteration+=1;
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
  
  while (n := arbInt(100000), n != 1) {
    sp("<n>");
  }
  ```  
}
void (str) spinner(str prefix = " ") {
  int stat = 0;
  
  return void (str suffix) {
    visit (stat) { 
      case 0: print("\r<prefix>\\ <suffix>");
      case 1: print("\r<prefix>| <suffix>");
      case 2: print("\r<prefix>/ <suffix>");
      case 3: print("\r<prefix>- <suffix>");
    }
    
    stat = (stat+1) % 4;
  };

}