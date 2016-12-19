module util::IDEServices

@doc{
.Synopsis
Open a browser for a given location.
}
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
java void browse(loc uri);

@doc{
.Synopsis
Open an editor for file at a given location.
}
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
java void edit(loc uri);

@doc{
.Synopsis
Log the __start__ of a job.

.Description

The various forms of `startJob` do the following:

* Register a job with a name, a default amount of work contributed to the overall task,
  and an unknown amount of steps to do.
* Register a job with a name and a total amount of steps to do (this will also be the amount
  of work contributed to the parent job, if any
* Register a job with a name, the amount this will contribute to the overall task,
  and a total amount of steps to do.
}
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
public java void startJob(str name);

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
public java void startJob(str name, int totalWork);

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
public java void startJob(str name, int workShare, int totalWork);

@doc{
}
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
public java void event(str name);

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
public java void event(str name, int inc);

@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices}
public java void event(int inc);

@doc{
}
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices} 
public java int endJob(bool succeeded);

@doc{
}
@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.BasicIDEServices} 
public java void todo(int work);