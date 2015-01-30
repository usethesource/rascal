@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@doc{
Synopsis: Monitor the progress of a task/job
}
module util::Monitor

@doc{
Synopsis: Log the __start__ of a job.

Description:

The various forms of `startJob` do the following:
# Register a job with a name, a default amount of work contributed to the overall task,
  and an unknown amount of steps to do.
# Register a job with a name and a total amount of steps to do (this will also be the amount
  of work contributed to the parent job, if any
# Register a job with a name, the amount this will contribute to the overall task,
  and a total amount of steps to do.
}
@javaClass{org.rascalmpl.library.util.Monitor}
@reflect
public java void startJob(str name);

@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java void startJob(str name, int totalWork);

@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java void startJob(str name, int workShare, int totalWork);

@doc{
Synopsis: Log the __start__ of an event (it is ended by another `event` or by `endJob`).

Description:
The various forms of `event` behave as follows:
# Log the start of an event.
# Log the start of an event with the amount of work that will be done when it's finished.
  An event is finished when the next event is logged, or when [endJob] is called.
# Log the start of an event with the amount of work that will be done when it's finished.
  An event is finished when the next event is logged, or when [endJob] is called.
}
@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java void event(str name);

@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java void event(str name, int inc);

@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java void event(int inc);

@doc{
Synopsis: Log the __end__ of a job.
Description:
  This should always be called once for every startJob, unless an exception is thrown.
  Returns the amount of work completed for this job (to help in future estimates)
}
@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java int endJob(bool succeeded);

@doc{
Synopsis: Set the estimated remaining work for the current (sub)job.

Description:
  Set the estimated remaining work for the current (sub)job. 
  The argument `work` is the amount of work remaining to be done, or 0 for unknown.
}
@javaClass{org.rascalmpl.library.util.Monitor}
@reflect 
public java void todo(int work); 
