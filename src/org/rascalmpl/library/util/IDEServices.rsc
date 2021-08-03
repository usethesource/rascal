module util::IDEServices

extend analysis::diff::edits::TextEdits;

@doc{
.Synopsis
Open a browser for a given location.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void browse(loc uri);

@doc{
.Synopsis
Open an editor for file at a given location.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
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
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
public java void startJob(str name);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
public java void startJob(str name, int totalWork);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
public java void startJob(str name, int workShare, int totalWork);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
public java void event(str name);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
public java void event(str name, int inc);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
public java void event(int inc);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java int endJob(bool succeeded);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void todo(int work);

@doc{
.Synopsis
Let the IDE apply a list of document edits.

.Description

Asks the IDE to apply document edits as defined in the standard library module
analysis::diff::edits::TextEdits, according to the semantics defined in
analysis::diff::edits::ExecuteTextEdits. However, the IDE can take care of these
changes in order to provide important UI experience features such as "preview"
and "undo". 

Typically a call to this IDE service method is included in the implementation
of refactoring and quick-fix features of the language service protocol.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void applyDocumentsEdits(list[DocumentEdit] edits);