module util::amalga::AmalgaREPL

import String;
import util::REPL;

public REPL amalgaRepl 
       = repl("amalga", "hello", "\>", |home:///.amalgaHistory|, 
         CommandResult (str line) { 
         	return <handler(line), [], "\>">; 
         },
         Completion(str line, int cursor) { return <completor(line,cursor), []>; });
         
@javaClass{org.rascalmpl.library.util.amalga.AmalgaREPL}
@reflect
public java str handler(str name);

@javaClass{org.rascalmpl.library.util.amalga.AmalgaREPL}
@reflect
public java int completor(str line, int cursor);

