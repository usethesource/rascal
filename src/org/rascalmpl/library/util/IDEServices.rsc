module util::IDEServices

extend analysis::diff::edits::TextEdits;
extend Content;
extend Message;

@synopsis{Open a browser for a given location.}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void browse(loc uri, str title = "<uri>", int viewColumn=1);


@synopsis{Open an editor for file at a given location.}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void edit(loc uri);


@synopsis{Let the IDE apply a list of document edits.}
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


@synopsis{Asks the IDE to show a "browser window" with the given interactive Content.}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void showInteractiveContent(Content content, str title=content.title, int viewColumn=content.viewColumn);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void showMessage(Message message);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void logMessage(Message message);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void registerDiagnostics(list[Message] messages);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
public java void unregisterDiagnostics(list[loc] resources);

@synopsis{Fixes are an extension to error messages that allow for interactive code fixes in the IDE.}
@description{
This definition adds lists of ((CodeActions)) as optional fields to any message. In collaboration
with a language server, these messages then lead to interactive quick fixes in IDEs.
}
data Message(list[CodeAction] fixes = []);

@synopsis{Code actions bundle synchronous text edits and command execution with a title for the menu option.}
@description{
For any action instance, the IDE will:
* show a menu option with the given title.
* if the title is selected, then the (optional) edits will be executed first
* and then the (optional) command is executed via the `execution` service of the language service protocol.
}
data CodeAction
    = action(list[DocumentEdit] edits = [], Command command = noop(), str title = command.title);

@synopsis{Commands are an open data-type for describing interactive functions that may be attached to CodeActions.}
@description{
Commands are simply immutable constructors with parameters. To use a command you can attach it to a ((Message))
via a ((CodeAction)), and then have it executed by the respective language server.
}
data Command(str title="") 
    = noop();
