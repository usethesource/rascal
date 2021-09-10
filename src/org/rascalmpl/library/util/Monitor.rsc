@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@doc{
.Synopsis
Monitor the progress of a task/job.
}
module util::Monitor

@doc{
.Synopsis
Log the __start__ of a job.

.Description

jobStart registers a new current job on the job stack with an amount of
steps todo and how much work it contributes (when it ends) to its parent job (if any).
}
@javaClass{org.rascalmpl.library.util.Monitor}
public java void jobStart(str label, int work=1, int totalWork=100);

@doc{
  Log to the user that a certain event has happened under
  the currently registered Job.
}
@javaClass{org.rascalmpl.library.util.Monitor}
public java void jobStep(str label, str message, int work = 1);

@javaClass{org.rascalmpl.library.util.Monitor} 
public java int jobEnd(str label, bool success=true);

@javaClass{org.rascalmpl.library.util.Monitor} 
public java void jobTodo(str label, int work=100);

@javaClass{org.rascalmpl.library.util.Monitor} 
public java void jobIsCancelled(str label);

@javaClass{org.rascalmpl.library.util.Monitor} 
public java void jobWarning(str message, loc src);