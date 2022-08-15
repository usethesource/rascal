@synopsis{compiles .rsc and .md files to markdown by executing Rascal-specific code and inlining its output}
@description{
  This compiler collects .rsc files and .md files from a PathConfig's srcs folders.
  
  Every .rsc file is compiled to a .md file with an outline of the declarations contained
  in the file and the contents of the @synopsis, @description, @pitfalls, @benefits, @examples
  tags with those declarations. @doc is also supported for backward compatibility's purposes.
  The resulting markdown is processed by the rest of the compiler, as if written by hand.

  Every .md file is scanned for ```rascal-shell elements. The contents between the ``` are
  executed by a private Rascal REPL and the output is captured in different ways. Normal IO
  via stderr and stdout is literally printed back and HTML or image output is inlined into 
  the document.

  For (nested) folders in the srcs folders, which do not contain an `index.md` file, or
  a <name>.md file where the name is equal to the name of the current folder, a fresh index.md
  file is generated which ${includes} all the (generated) .md files of the current folder and the index.md
  file of every nested folder.
}
module lang::rascal::tutor::Compiler

// THIS FILE IS UNDER CONSTRUCTION (FIRST VERSION)

import Message;
import Exception;
import IO;
import String;
import List;
import Location;
import util::Reflective;

list[Message] compile(PathConfig pcfg=pathConfig())
  = [*compile(src, pcfg=pcfg[srcs=[src]]) | src <- pcfg.srcs];

list[Message] compile(loc src, PathConfig pcfg=pathConfig()) {
    if (isDirectory(src)) {
        return compileDirectory(src, pcfg=pcfg);
    }
    else if (src.extension == "rsc") {
        return compileRascal(src, pcfg=pcfg);
    }
    else if (src.extension in {"md", "concept"}) {
        return compileMarkdown(src, pcfg=pcfg);
    }
    else if (src.extension in {"png","jpg","svg","jpeg", "html", "js"}) {
        try {
            copy(src, pcfg.bin + relativize(pcfg.srcs[0], src).path);
            return [];
        }
        catch IO(str message): {
            return [error(message, src)];
        }
    }
    else {
        return [];
    }
}

list[Message] compileDirectory(loc d, PathConfig pcfg=pathConfig()) {
    return [*compile(s, pcfg=pcfg) | s <- d.ls];
}
 
list[Message] compileRascal(loc m, PathConfig pcfg=pathConfig()) {
    return [];
}

data Output 
  = out(str content)
  | err(Message message)
  ;

list[Message] compileMarkdown(loc m, PathConfig pcfg=pathConfig()) {
  list[Output] output = compileMarkdown(readFileLines(m), m.begin.line, m.offset, pcfg=pcfg);

  // write the output lines to disk (filtering error)
  writeFile(
    (pcfg.bin + relativize(pcfg.srcs[0], m))[extension="md"], 
    "<for (out(l) <- output) {><l>
    '<}>"
  );

   // return the errors, filtering the output:
   return [m | err(Message m) <- output];
}

list[Output] compileMarkdown([str line:/^\s*```rascal-shell<rest:.*>$/, *block, /^\s*```/, *str rest], int line, int offset, PathConfig pcfg = pathConfig())
  = [
      *compileRascalShell(block, /errors/ := rest, /continued/ := rest, line+1, offset + size(line) + 1, pcfg=pcfg),
      *compileMarkdown(rest, line + 1 + size(block) + 1, offset + size(line) + (0 | it + size(b) | b <- block), pcfg=pcfg)
    ];

list[Output] compileMarkdown([], int line, int offset, PathConfig pcfg = pathConfig()) = [];

default list[Output] compileMarkdown([str head, *str tail], int line, int offset, PathConfig pcfg = pathConfig()) 
  = [
      out(head),
      *compileMarkdown(tail, line + 1, offset + size(head) + 1, pcfg=pcfg)
    ];

list[Output] compileRascalShell(list[str] block, bool allowErrors, bool isContinued, int line, int offset, PathConfig pcfg = pathConfig()) {
  return [];
}
  