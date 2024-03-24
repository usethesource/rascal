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
java void jobStart(str label, int work=1, int totalWork=0);

@synopsis{Log to the user that a certain event has happened under the currently registered Job.}
@javaClass{org.rascalmpl.library.util.Monitor}
java void jobStep(str label, str message, int work = 1);

@javaClass{org.rascalmpl.library.util.Monitor} 
@synopsis{Log the end of a job}
java int jobEnd(str label, bool success=true);

@javaClass{org.rascalmpl.library.util.Monitor} 
@synopsis{Register additional work for the identied job.}
java void jobTodo(str label, int work=1);

@javaClass{org.rascalmpl.library.util.Monitor} 
@synopsis{Poll if the given job has been cancelled by a user interaction.}
java void jobIsCancelled(str label);

@javaClass{org.rascalmpl.library.util.Monitor} 
@synopsis{Register a warning in the same UI that job progress is shown in.}
java void jobWarning(str message, loc src);

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
@benefits{
* job blocks help to avoid repeating the job label all the time
* job blocks make sure that every job is stoped, no matter the exceptions
}
@pitfalls{
* additional work with ((jobTodo)) is still possible, but you have to repeat the right job label.
}
void job(str label, void (void (str message, int worked) step) block) {
   try {
     jobStart(label);
     block((str message, int worked) { jobStep(label, message, work=worked);});
   }
   catch x: {
     throw x;
   }
   finally {
     jobEnd(label);
   }
}

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
void job(str label, void (void (int worked) step) block) {
   try {
     jobStart(label);
     block((int worked) { jobStep(label, label, work=worked);});
   }
   catch x: {
     throw x;
   }
   finally {
     jobEnd(label);
   }
}

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
void job(str label, void (void () step) block) {
   try {
     jobStart(label);
     block(() { jobStep(label, label, work=1);});
   }
   catch x: {
     throw x;
   }
   finally {
     jobEnd(label);
   }
}

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
void job(str label, void () block) {
   try {
     jobStart(label);
     block();
   }
   catch x: {
     throw x;
   }
   finally {
     jobEnd(label);
   }
}

@synopsis{Puts the monitor API to work by racing 5 horses against each other.}
test bool horseRaceTest() {
  distance  = 3000000;
  stride    = 50;
  horses    = 5;
  handicaps = [ arbInt(stride * 15 / 100)    | _ <- [0..horses]];
  labels    = [ "Horse <h> (handicap is <handicaps[h]>)" | h <- [0..horses]];
  progress  = [ 0                            | _ <- [0..horses]];
 
  for (int h <- [0..horses]) 
    jobStart(labels[h], totalWork=distance);

  race:while (true) 
    for(int h <- [0..horses]) {
      advance      = arbInt(stride - handicaps[h]);
      progress[h] += advance;
      
      jobStep(labels[h], "Pacing horse <h> with <advance>...", work=advance);
      println("Annoying commentator blabla <arbInt(100)>");
      if (progress[h] >= distance) {
        break race;
      }
    }
  
  for (int h <- [0..horses]) 
    jobEnd(labels[h]);

  return true;
}