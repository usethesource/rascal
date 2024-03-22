@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}

@synopsis{Monitor the progress of a task/job.}
@bootstrapParser
module util::Monitor

import util::Math;
import IO;

@synopsis{Log the start of a job.}
@description{
jobStart registers a new current job on the job stack with an amount of
steps todo and how much work it contributes (when it ends) to its parent job (if any).
}
@javaClass{org.rascalmpl.library.util.Monitor}
java void jobStart(str label, int work=1, int totalWork=100);

@synopsis{Log to the user that a certain event has happened under
  the currently registered Job.}
@javaClass{org.rascalmpl.library.util.Monitor}
java void jobStep(str label, str message, int work = 1);

@javaClass{org.rascalmpl.library.util.Monitor} 
java int jobEnd(str label, bool success=true);

@javaClass{org.rascalmpl.library.util.Monitor} 
java void jobTodo(str label, int work=100);

@javaClass{org.rascalmpl.library.util.Monitor} 
java void jobIsCancelled(str label);

@javaClass{org.rascalmpl.library.util.Monitor} 
java void jobWarning(str message, loc src);

@synopsis{Puts the monitor API to work by racing 5 horses against each other.}
test bool horseRaceTest() {
  distance  = 1000000;
  stride    = 100;
  horses    = 5;
  handicaps = [ arbInt(stride * 15 / 100)    | _ <- [0..horses]];
  labels    = [ "Horse <h> (<handicaps[h]>)" | h <- [0..horses]];
  progress  = [ 0                            | _ <- [0..horses]];
 
  for (int h <- [0..horses]) 
    jobStart(labels[h], totalWork=distance);

  race:while (true) 
    for(int h <- [0..horses]) {
      advance      = arbInt(stride - handicaps[h]);
      progress[h] += advance;
      
      jobStep(labels[h], "pacing...", work=advance);
      // println("Annoying commentator blabla");
      if (progress[h] >= distance) {
        break race;
      }
    }
  
  for (int h <- [0..horses]) 
    jobEnd(labels[h]);

  return true;
}