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
((jobStart)) registers a new current job on the job stack with an amount of
steps todo and how much work it contributes (when it ends) to its parent job (if any).
* The `label` _identifies_ as running task, which could be presented in the UI with a specific progress bar (for example)
* Using ((jobStep)) with the same label, you can advance the amount of work done for the task.
* All tasks should eventually end with ((jobEnd)), but most UI's will clean up open left-over tasks at natural intervals as well.
* Use ((jobTodo)) to register additional dynamically discovered work for a task. The `totalWork` constant for that
specific task will be increased by the given amount.


}
@benefits{
* It is adviced to use the "block" functions `job` instead of the raw `jobStart`, `jobStep` and `jobEnd`
functions because these guarantee each started task is always ended, with and without exceptions. This improves
the user experience for your users. Also these functions help by providing the job label in the scope of the task,
such that this "magic constant" does not need to be repeated.
}
@pitfalls{
* The job label is both an identity and a user facing string constant. Future versions of the API may
split the identify from the label for better accuracy and better UI.
}
@javaClass{org.rascalmpl.library.util.Monitor}
java void jobStart(str label, int work=1, int totalWork=100);

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
@description{
The convenience function that is passed to the block can be used inside the block to register steps
with a parameterized workload and the same label as the job name.
}
@benefits{
* the block body does not need to repeat the `name` parameter when ending the job or making steps
* the job is always properly ended, even when exceptions are thrown
}
@pitfalls{
* additional work with ((jobTodo)) is still possible, but you have to repeat the right job label.
}
&T job(str label, &T (void (str message, int worked) step) block, int totalWork=100) {
  try {
    jobStart(label, totalWork=totalWork);
    return block((str message, int worked) { 
      jobStep(label, message, work=worked);
    });
  }
  catch x: {
     throw x;
  }
  finally {
    jobEnd(label);
  }
}

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
@description{
The convenience function that is passed to the block can be used inside the block to register steps
with a parameterized workload and the same label as the job name.
}
@benefits{
* the block body does not need to repeat the `name` parameter when ending the job or making steps
* the job is always properly ended, even when exceptions are thrown
}
@pitfalls{
* additional work with ((jobTodo)) is still possible, but you have to repeat the right job label.
}
&T job(str label, &T (void (int worked) step) block, int totalWork=1) {
  try {
    jobStart(label, totalWork=totalWork);
    return block((int worked) { 
      jobStep(label, label, work=worked);
    });
  }
  catch x: {
    throw x;
  }
  finally {
    jobEnd(label);
  }
}

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
@description{
The convenience function that is passed to the block can be used inside the block to register steps
with workload `1` and the same label as the job name.
}
@benefits{
* the block body does not need to repeat the `name` parameter when ending the job or making steps
* the job is always properly ended, even when exceptions are thrown
}
@pitfalls{
* additional work with ((jobTodo)) is still possible, but you have to repeat the right job label.
}
&T job(str label, &T (void () step) block, int totalWork=1) {
  try {
    jobStart(label, totalWork=totalWork);
    return block(() {
      jobStep(label, label, work=1);
    });
  }
  catch x: {
    throw x;
  }
  finally {
    jobEnd(label);
  }
}

@synopsis{A job block guarantees a start and end, and provides easy access to the stepper interface.}
@benefits{
* the block code does not need to remember to end the job with the same job name.
* the job is always properly ended, even when exceptions are thrown
}
&T job(str label, &T () block, int totalWork=1) {
  try {
    jobStart(label, totalWork=totalWork);
    return block();
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
  horses    = 28;
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
      
      if (progress[h] >= distance) {
        break race;
      }
    }
  
  for (int h <- [0..horses]) 
    jobEnd(labels[h]);

  return true;
}

test bool simpleAsyncPrintTest() {
  jobStart("job", totalWork=3);
  println("a");
  jobStep("job", "step 1", work=1);
  println("b");
  jobStep("job", "step 2", work=1);
  println("c");
  jobStep("job", "step 3", work=1);
  println("d");
  jobEnd("job");
  return true;
}

test bool unfinishedInputTest() {
  jobStart("job", totalWork=26);
  for (/<l:[a-z]>/ := "abcdefghijklmnopqrstuwvxyz") {
    print(l); // no newline!
    jobStep("job", "letter <l>", work=1);
    if (arbInt(10) == 0) {
      println(); // break it
    }
  }
  // println(); // flush it
  jobEnd("job");
  return true;
}

test bool unfinishedLinesAtTheEndTest() {
  jobStart("job", totalWork=3);
  print("ab\nc");
  jobStep("job", "1.5", work=1);
  print("d\ne");
  jobStep("job", "2.5", work=1);
  print("f\ngh\n");
  jobStep("job", "3", work=1);
  jobEnd("job");
  return true;
}