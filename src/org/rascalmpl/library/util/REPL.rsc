module util::REPL

extend Content;

alias Completion = map[str completion, str group];

data REPL
  = repl(
     str title = "", 
     str welcome = "", 
     str prompt = "\n\>",
     str quit = "", 
     loc history = |home:///.term-repl-history|, 
     Content (str command) handler = echo,
     Completion(str line, str word) completor = noSuggestions,
     str () stacktrace = str () { return ""; }
   );

private Content echo(str line) = plainText(line);
   
private Completion noSuggestions(str _, str _) = ();

alias Terminal = tuple[void() run, void(str) send];

@javaClass{org.rascalmpl.library.util.TermREPL}
@reflect{
Makes closures
}
java Terminal newREPL(REPL repl, 
  
  // filling in defaults from the repl constructor, for use in the Java code:
  str title = repl.title, 
  str welcome = repl.welcome, 
  str prompt = repl.prompt, 
  str quit = repl.quit,
  loc history = repl.history,
  Content (str ) handler = repl.handler,
  Completion(str , str) completor = repl.completor,
  str () stacktrace = repl.stacktrace);

void startREPL(REPL repl, 
  
  // filling in defaults from the repl constructor, for use in the Java code:
  str title = repl.title, 
  str welcome = repl.welcome, 
  str prompt = repl.prompt, 
  str quit = repl.quit,
  loc history = repl.history,
  Content (str ) handler = repl.handler,
  Completion(str , str) completor = repl.completor,
  str () stacktrace = repl.stacktrace) {
  
  Terminal tm = newREPL(repl, title=title, welcome=welcome,
    prompt=prompt, quit=quit, history=history, 
    handler=handler, completor=completor, stacktrace=stacktrace);
  tm.run();  
}
