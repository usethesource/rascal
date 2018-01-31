module lang::rascalcore::compile::RascalLanguageServer

import IO;
import ValueIO; 
import Type;
import Set;
import util::Reflective;
import lang::rascalcore::compile::RVM::AST;
import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;


int processId;
str rootPath;
ClientCapabilities capabilities;

map[str, ModuleSummary] modules = ();
PathConfig pcfg;

data ModuleSummary =
    summary(map[loc from, Symbol tp] locationTypes,
            rel[loc from, loc to] useDef);

void makeSummary(
    loc moduleLoc,    // name of Rascal module
    PathConfig pcfg){           // path configuration
   
   qualifiedModuleName = getModuleName(moduleLoc, pcfg);
   if(<true, cloc> := cachedConfigReadLoc(qualifiedModuleName,pcfg)){
      println(cloc);
      Configuration c = readBinaryValueFile(#Configuration, cloc);
      store = c.store;
      locationTypes = c.locationTypes;
      for(l <- locationTypes){
        println("<l> -\> <locationTypes[l]>");
      }
      
      definitions = c.definitions;
      uses = c.uses + c.narrowedUses;
      use_def = {<use, def> | <int uid, loc def> <- definitions, loc use <- (uses[uid] ? {})};
      for(<loc use, loc def> <- use_def){
        println("<use>\n-\> <def>");
      }
      modules[moduleLoc.path] = summary(locationTypes, use_def);
   } else {
       throw "Config file does not exist for: <qualifiedModuleName>";
   }    
   return;            
}

/**
 * TextDocumentSyncKind: Defines how the host (editor) should sync document changes to the language server.
 */
 
 alias TextDocumentSyncKind = int;
 
    /**
     * Documents should not be synced at all.
     */
    int TextDocumentSyncKind_None = 0;
    /**
     * Documents are synced by always sending the full content of the document.
     */
     int TextDocumentSyncKind_Full = 1;
    /**
     * Documents are synced by sending the full content on open. After that only incremental 
     * updates to the document are sent.
     */
     int TextDocumentSyncKind_Incremental = 2;

/**
 * Completion options.
 */
data CompletionOptions =
    completionOptions(
    /**
     * The server provides support to resolve additional information for a completion item.
     */
    bool resolveProvider = false,

    /**
     * The characters that trigger completion automatically.
     */
    list[str] triggerCharacters = []
    );

/**
 * Signature help options.
 */
data SignatureHelpOptions =
    signatureHelpOptions(
    /**
     * The characters that trigger signature help automatically.
     */
    list[str] triggerCharacters = []
    );

/**
 * Code Lens options.
 */
data CodeLensOptions =
    codeLensOptions(
    /**
     * Code lens has a resolve provider as well.
     */
    bool resolveProvider = false
    );
    
/**
 * Format document on type options
 */
data DocumentOnTypeFormattingOptions =
    documentOnTypeFormattingOptions(
    /**
     * A character on which formatting should be triggered, like closing brace
     */
    str firstTriggerCharacter = "}",
    /**
     * More trigger characters.
     */
    list[str] moreTriggerCharacter = []
    );
    
data ServerCapabilities =
    serverCapabilities(
    /**
     * Defines how text documents are synced.
     */
    int textDocumentSync = TextDocumentSyncKind_None,
    /**
     * The server provides hover support.
     */
    bool hoverProvider = false,
    /**
     * The server provides completion support.
     */
    CompletionOptions completionProvider = completionOptions(),
    /**
     * The server provides signature help support.
     */
    SignatureHelpOptions signatureHelpProvider = signatureHelpOptions(),
    /**
     * The server provides goto definition support.
     */
    bool definitionProvider = true,
    /**
     * The server provides find references support.
     */
    bool referencesProvider = true,
    /**
     * The server provides document highlight support.
     */
    bool documentHighlightProvider = false,
    /**
     * The server provides document symbol support.
     */
    bool documentSymbolProvider = true,
    /**
     * The server provides workspace symbol support.
     */
    bool workspaceSymbolProvider = true,
    /**
     * The server provides code actions.
     */
    bool codeActionProvider = false,
    /**
     * The server provides code lens.
     */
    CodeLensOptions codeLensProvider = codeLensOptions(),
    /**
     * The server provides document formatting.
     */
    bool documentFormattingProvider = false,
    /**
     * The server provides document range formatting.
     */
    bool documentRangeFormattingProvider = false,
    /**
     * The server provides document formatting on typing.
     */
    DocumentOnTypeFormattingOptions documentOnTypeFormattingProvider = documentOnTypeFormattingOptions(),
    /**
     * The server provides rename support.
     */
    bool renameProvider = false
    );

data ClientCapabilities =
    clientCapabilities();
    
//↩️ initialize



ServerCapabilities initialize(
    /**
     * The process Id of the parent process that started
     * the server. Is null if the process has not been started by another process.
     * If the parent process is not alive then the server should exit (see exit notification) its process.
     */
     int aProcessId, 
     /**
     * The rootPath of the workspace. Is null
     * if no folder is open.
     */
     str aRootPath, 
     /**
     * User provided initialization options.
     */
     PathConfig apcfg,       // value initializationOptions, //<== PathConfig
     /**
     * The capabilities provided by the client (editor)
     */
     ClientCapabilities theCapabilities
     ){
    processId = aProcessId;
    rootPath = aRootPath;
    capabilities = theCapabilities;
    pcfg = apcfg;
    modules = ();
    return serverCapabilities();
}
//↩️ shutdown
//➡️ exit
//➡️ $/cancelRequest
//Window
//
//⬅️ window/showMessage
//↪️ window/showMessageRequest
//⬅️ window/logMessage
//⬅️ telemetry/event
//Workspace

/**
 * The file event type.
 */
alias FileChangeType = int;
    /**
     * The file got created.
     */
    int FileChangeType_Created = 1;
    /**
     * The file got changed.
     */
    int FileChangeType_Changed = 2;
    /**
     * The file got deleted.
     */
    int FileChangeType_Deleted = 3;

/**
 * An event describing a file change.
 */
data FileEvent =
    fileEvent(
    /**
     * The file's URI.
     */
    loc uri,
    /**
     * The change type.
     */
    FileChangeType \type
    );

//➡️ workspace/didChangeConfiguration
//➡️ workspace/didChangeWatchedFiles

void didChangeWatchedFiles(list[FileEvent] changes){ }
//↩️ workspace/symbol

//Document

data TextDocumentItem =
    textDocumentItem(
    /**
     * The text document's URI.
     */
    loc uri,    // was: string

    /**
     * The text document's language identifier.
     */
    str languageId,

    /**
     * The version number of this document (it will strictly increase after each
     * change, including undo/redo).
     */
    int version,

    /**
     * The content of the opened text document.
     */
    str text
    ); 

data Position =
    position(
    /**
     * Line position in a document (zero-based).
     */
    int line,

    /**
     * Character offset on a line in a document (zero-based).
     */
    int character
    );
    
data Range =
    range(
    /**
     * The range's start position.
     */
    Position \start,

    /**
     * The range's end position.
     */
    Position end
    );
    
/**
 * An event describing a change to a text document. If range and rangeLength are omitted
 * the new text is considered to be the full content of the document.
 */
data TextDocumentContentChangeEvent =
    textDocumentContentChangeEvent(
    /**
     * The range of the document that changed.
     */
    Range range,

    /**
     * The length of the range that got replaced.
     */
    int rangeLength,

    /**
     * The new text of the document.
     */
    str text
    );

//⬅️ textDocument/publishDiagnostics
//➡️ textDocument/didChange

alias TextDocumentIdentifier = loc;

data VersionedTextDocumentIdentifier =
    versionedTextDocumentIdentifier(TextDocumentIdentifier id, int version);

void didClose(TextDocumentIdentifier id){ }

//➡️ textDocument/didOpen

void didOpen(TextDocumentIdentifier textDocument){
    makeSummary(textDocument, pcfg);
    return;
}

//➡️ textDocument/didSave

void didSave(TextDocumentIdentifier textDocument) { }

//↩️ textDocument/completion
//↩️ completionItem/resolve
//↩️ textDocument/hover
//↩️ textDocument/signatureHelp
//↩️ textDocument/references

list[loc] references(loc use){
    return toList(modules[use.path].useDef[use]);
}

//↩️ textDocument/documentHighlight
//↩️ textDocument/documentSymbol
//↩️ textDocument/formatting
//↩️ textDocument/rangeFormatting
//↩️ textDocument/onTypeFormatting
//↩️ textDocument/definition

//list[loc] definition(TextD pos){
//    
//}
//↩️ textDocument/codeAction
//↩️ textDocument/codeLens
//↩️ codeLens/resolve
//↩️ textDocument/documentLink
//↩️ documentLink/resolve
//↩️ textDocument/rename

value main(){
    p1 = pathConfig(srcs=[|file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/|]);
    initialize(0, "", p1, clientCapabilities());
    facLoc = getModuleLocation("lang::rascalcore::compile::Examples::Fac", p1);
    println(facLoc);
    facUse = |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fac.rsc|(237,3,<13,19>,<13,22>);
    didOpen(facLoc);
    println(references(facUse));
    didClose(facLoc);
    return true;
}