module util::IDEServices

extend analysis::diff::edits::TextEdits;
import analysis::diff::edits::ExecuteTextEdits;
extend Content;
extend Message;

@synopsis{Open a browser for a given location.}
@description{
Starts an _interactive_ browser for a given URI, typically in a tab embedded in the IDE.
However, this depends on the current IDE context. Some editors do not support this feature.
A browser window for the OS default browser will be started instead.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void browse(loc uri, str title = "<uri>", ViewColumn viewColumn = activeViewColumn());

@synopsis{Open an editor for file at a given location.}
@description{
Based on the current IDE context an editor will be "opened". This means
for most IDEs that the language services associated with the file extension
will be activated. However, this depends entirely on the IDE and the currently
registered languages. 
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary}
java void edit(loc uri, ViewColumn viewColumn = activeViewColumn());

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
java void applyDocumentsEdits(list[FileSystemChange] edits);

void applyFileSystemEdits(list[FileSystemChange] edits) {
    applyDocumentsEdits(edits);
}


@synopsis{Asks the IDE to show a "browser window" with the given interactive Content.}
@description{
Just like ((browse)), with the important distinction that this starts both
a web _client_ and a web _server_.
}
@benefits{
* quickly spin-up and manage interactive visuals without worrying about garbage collection and memory leaks, or port numbers
* shows visuals _inside_ the current IDE. Combines very well with ((edit)) to show visuals side-by-side with code.
}
@pitfalls{
* the web servers will remain active until 30 minutes after
the last interaction. After that a `404` (not found) http error will be produced and 
((showInteractiveContent)) has to be called again to re-activate the visual.
}
@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
java void showInteractiveContent(Content content, str title=content.title, ViewColumn viewColumn=content.viewColumn);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
java void showMessage(Message message);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
java void logMessage(Message message);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
java void registerDiagnostics(list[Message] messages);

@javaClass{org.rascalmpl.library.util.IDEServicesLibrary} 
java void unregisterDiagnostics(list[loc] resources);

@synopsis{Fixes are an extension to error messages that allow for interactive code fixes in the IDE.}
@description{
This definition adds lists of ((CodeAction))s as optional fields to any message. In collaboration
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
data CodeAction(list[FileSystemChange] edits = [], Command command = noop(), str title = command.title)
    = action();

@synopsis{Commands are an open data-type for describing interactive functions that may be attached to CodeActions.}
@description{
Commands are simply immutable constructors with parameters. To use a command you can attach it to a ((module:Message))
via a ((CodeAction)), and then have it executed by the respective language server.
}
data Command(str title="") 
    = noop();

@synopsis{Utility function for testing code actions.}
@benefits{
* test code actions outside of the IDE context, for example while running unit tests.
* this function is synchronous and blocks until the IO is finished. After running it you
can test for changed file contents without waiting, in most cases (see pitfalls).
}
@description{
* `action` is the action to execute
* `evaluator` is used to evaluate action.command if it is present.
* the return value is the return value of the evaluated command, or `true` if no command is present.
}
@pitfalls{
* ((CodeAction))s may use the other features of ((util::IDEServices)), and thus start editors or browsers as side-effects.
* ((CodeAction))s code actions with ((FileSystemChanges-FileSystemChange))s will write to disk and change the original files.
* ((util::IDEServices::Command))s can only be executed by a parametrized command `evaluator``; if you do not provide it then 
this test function will throw ((CallFailed)) exceptions for every unsupported (((util::IDEServices::Command)).
* (((util::IDEServices::Command))s can start asynchronous effects by calling non-blocking functions that schedule other effects.
An example is the starting and running of web ((Library:module:Content)) via ((showInteractiveContent)). Testing properties of the
rendered content will require the use of asynchronous testing frameworks, like Selenium. 
* Never call ((testCodeAction)) to execute actions in an interactive context. That must be done by the IDE client
to synchronize the contents of editors and parse trees, etc. This function is only for unit testing code actions.
}
value testCodeAction(CodeAction action, value (Command _) evaluator = value (noop()) { return true; }) {
    if (action.edits?) {
        executeFileSystemChanges(action.edits);
    }

    if (action.command?) {
        return evaluator(action.command);
    }

    return true;
}
