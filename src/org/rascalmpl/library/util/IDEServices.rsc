module util::IDEServices

extend analysis::diff::edits::TextEdits;
extend Content;
extend Message;


@synopsis{

Open a browser for a given location.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void browse(loc uri);


@synopsis{

Open an editor for file at a given location.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void edit(loc uri);


@synopsis{

Let the IDE apply a list of document edits.

}
@description{

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


@synopsis{

Asks the IDE to show a "browser window" with the given interactive Content.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void showInteractiveContent(Content content);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void showMessage(Message message);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void logMessage(Message message);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void registerDiagnostics(list[Message] messages);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void unregisterDiagnostics(list[loc] resources);
