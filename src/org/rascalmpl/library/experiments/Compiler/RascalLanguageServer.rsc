module experiments::Compiler::RascalLanguageServer

import IO;
import ValueIO;
import Type;
import util::Reflective;
import experiments::Compiler::RVM::AST;
import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

data ModuleSummary =
    summary(map[loc from, Symbol tp] locationTypes,
            rel[loc from, loc to] useDef);

ModuleSummary ideSupport(
    str qualifiedModuleName,    // name of Rascal module
    PathConfig pcfg){           // path configuration
     
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
      for(<use, def> <- use_def){
        println("<use>\n-\> <def>");
      }
      return summary(locationTypes, use_def);
      
   } else {
       throw "Config file does not exist for: <qualifiedModuleName>";
   }                
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
    
data InitializeParams =
    initializeParams(
    /**
     * The process Id of the parent process that started
     * the server. Is null if the process has not been started by another process.
     * If the parent process is not alive then the server should exit (see exit notification) its process.
     */
     int processId, 
     /**
     * The rootPath of the workspace. Is null
     * if no folder is open.
     */
     str rootPath, 
     /**
     * User provided initialization options.
     */
     PathConfig pcfg,       // value initializationOptions, //<== PathConfig
     /**
     * The capabilities provided by the client (editor)
     */
     ClientCapabilities capabilities
     );

data ClientCapabilities =
    clientCapabilities();
    
//↩️ initialize

int processId;
str rootPath;
ClientCapabilities capabilities;

map[str, ModuleSummary] modules;
PathConfig pcfg;

ServerCapabilities initialize(InitializeParams initializeParams){
    processId = initializeParams.processId;
    rootPath = initializeParams.rootPath;
    capabilities = initializeParams.capabilities;
    pcfg = initializeParams.pcfg;
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

void didOpen(TextDocumentItem textDocument){ }

//➡️ textDocument/didSave

void didSave(TextDocumentIdentifier textDocument) { }

//↩️ textDocument/completion
//↩️ completionItem/resolve
//↩️ textDocument/hover
//↩️ textDocument/signatureHelp
//↩️ textDocument/references
//↩️ textDocument/documentHighlight
//↩️ textDocument/documentSymbol
//↩️ textDocument/formatting
//↩️ textDocument/rangeFormatting
//↩️ textDocument/onTypeFormatting
//↩️ textDocument/definition

list[loc] definition(Position pos){

}
//↩️ textDocument/codeAction
//↩️ textDocument/codeLens
//↩️ codeLens/resolve
//↩️ textDocument/documentLink
//↩️ documentLink/resolve
//↩️ textDocument/rename