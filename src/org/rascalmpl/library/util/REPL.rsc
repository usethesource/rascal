module util::REPL

extend Content;

alias Completion
 = tuple[int offset, list[str] suggestions];

data REPL
  = repl(
     str title = "", 
     str welcome = "", 
     str prompt = "\n\>", 
     loc history = |home:///.term-repl-history|, 
     Response (str command) handler = echo,
     Completion(str line, int cursor) completor = noSuggestions,
     str () stacktrace = str () { return ""; }
   );

data RuntimeException = interrupt();

private Response echo("quit") { throw interrupt(); }
private default Response echo(str line) = plain(line);
   
private Completion noSuggestions(str _, int _) = <0, []>;

@javaClass{org.rascalmpl.library.util.TermREPL}
@reflect
java void startREPL(REPL repl, 
  
  // filling in defaults from the repl constructor, for use in the Java code:
  str title = repl.title, 
  str welcome = repl.welcome, 
  str prompt = repl.prompt, 
  loc history = repl.history,
  Response (str ) handler = repl.handler,
  Completion(str , int) completor = repl.completor,
  str () stacktrace = repl.stacktrace);
